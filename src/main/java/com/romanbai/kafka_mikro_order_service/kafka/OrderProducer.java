package com.romanbai.kafka_mikro_order_service.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  private static final String TOPIC = "order-topic";

  public void sendOrderCreatedEvent(String orderEventJson) {
    kafkaTemplate.send(TOPIC, orderEventJson);
    System.out.println("Sent order event to Kafka: " + orderEventJson);
  }
}