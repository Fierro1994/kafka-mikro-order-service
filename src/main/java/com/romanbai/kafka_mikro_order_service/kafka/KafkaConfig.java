package com.romanbai.kafka_mikro_order_service.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;


@Configuration
public class KafkaConfig {

  @Autowired
  private ProducerFactory<String, String> producerFactory;

  @Bean
  public KafkaTemplate<String, String> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory);
  }

  @Bean
  public NewTopic createTopicBean() {
    return TopicBuilder.name("order-topic")
        .partitions(3)
        .replicas(3)
        .build();
  }


}
