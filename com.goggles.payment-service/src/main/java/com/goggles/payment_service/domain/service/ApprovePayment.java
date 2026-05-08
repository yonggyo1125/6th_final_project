package com.goggles.payment_service.domain.service;

import com.goggles.payment_service.domain.OrderDetail;
import com.goggles.payment_service.domain.PaymentId;
/*
curl --request POST \
  --url https://api.tosspayments.com/v1/payments/confirm \
  --header 'Authorization: Basic dGVzdF9za196WExrS0V5cE5BcldtbzUwblgzbG1lYXhZRzVSOg==' \
  --header 'Content-Type: application/json' \
  --data '{"paymentKey":"5EnNZRJGvaBX7zk2yd8ydw26XvwXkLrx9POLqKQjmAw4b0e1","orderId":"a4CWyWY5m89PNh7xJwhk1","amount":1000}'
 */
public interface ApprovePayment {
    ApproveResult request(PaymentId paymentId, String paymentKey, OrderDetail orderDetail);
}
