package com.romanbai.kafka_mikro_order_service.repository;

import com.romanbai.kafka_mikro_order_service.entity.InboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InboxRepository extends JpaRepository<InboxEvent, Long> {
  boolean existsByMessageId(String messageId);
}
