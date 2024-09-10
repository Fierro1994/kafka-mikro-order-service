package com.romanbai.kafka_mikro_order_service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.romanbai.kafka_mikro_order_service.dto.OrderEvent;
import com.romanbai.kafka_mikro_order_service.dto.UserEvent;
import com.romanbai.kafka_mikro_order_service.entity.InboxEvent;
import com.romanbai.kafka_mikro_order_service.entity.Order;
import com.romanbai.kafka_mikro_order_service.repository.InboxRepository;
import com.romanbai.kafka_mikro_order_service.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderConsumer {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private InboxRepository inboxRepository;

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Transactional
  @KafkaListener(topics = "user-topic", groupId = "order_group")
  public void consume(String userEventJson) throws JsonProcessingException {
    // Десериализация сообщения
    UserEvent userEvent = objectMapper.readValue(userEventJson, UserEvent.class);

    // Уникальный идентификатор сообщения (можно извлечь из заголовка Kafka, если доступно)
    String messageId = String.valueOf(userEvent.getId());

    // Проверяем, было ли сообщение уже обработано
    if (inboxRepository.existsByMessageId(messageId)) {
      System.out.println("Сообщение уже обработано: " + messageId);
      return; // Сообщение уже обработано, пропускаем
    }

    // Создание нового заказа для пользователя
    Order order = new Order();
    order.setDescription("New order for user: " + userEvent.getName());
    order.setUserId(userEvent.getId());

    Order savedOrder = orderRepository.save(order);

    // Формирование события OrderEvent для Kafka
    OrderEvent orderEvent = new OrderEvent(savedOrder.getId(), savedOrder.getDescription(), userEvent.getId());
    String orderEventJson = objectMapper.writeValueAsString(orderEvent);

    // Отправка события в Kafka
    kafkaTemplate.send("order-topic", orderEventJson);

    // Сохранение записи в таблицу inbox (для предотвращения повторной обработки)
    InboxEvent inboxEvent = new InboxEvent();
    inboxEvent.setMessageId(messageId);
    inboxEvent.setTopic("user-topic");
    inboxEvent.setPayload(userEventJson);
    inboxRepository.save(inboxEvent);

    System.out.println("Сообщение обработано и сохранено в Inbox: " + messageId);
  }
}