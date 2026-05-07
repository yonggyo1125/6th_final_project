package com.goggles.payment_service.infrastructure.kafka;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEventDto {

  private UUID orderID;
  private Long amount;
  private String customerName;
  private String customerEmail;
  private String orderName;
}
