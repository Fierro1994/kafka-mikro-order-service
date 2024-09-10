package com.romanbai.kafka_mikro_order_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderEvent {
  private Long orderId;
  private String description;
  private Long userId;

  public OrderEvent(Long orderId, String description, Long userId) {
    this.orderId = orderId;
    this.description = description;
    this.userId = userId;
  }
}