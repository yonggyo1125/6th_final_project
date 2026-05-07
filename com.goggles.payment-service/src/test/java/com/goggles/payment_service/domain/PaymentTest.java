package com.goggles.payment_service.domain;

import static com.goggles.payment_service.domain.PaymentFixture.createReadyPayment;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import legacy.domain.Payment;
import legacy.domain.PaymentMethod;
import legacy.domain.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PaymentTest {

  private Payment payment;

  @BeforeEach
  void setUp() {
    payment = createReadyPayment();
  }

  @Test
  void 결제_생성_성공() {
    assertThat(payment.getStatus()).isEqualTo(PaymentStatus.READY);
    assertThat(payment.getMethod()).isEqualTo(PaymentMethod.TOSS);
    assertThat(payment.getTransactionId()).isNull();
    assertThat(payment.getPaidAt()).isNull();
    assertThat(payment.getFailReason()).isNull();
  }

  @Test
  void 결제_성공_상태전이() {
    payment.success("test_transaction_key", null);

    assertThat(payment.getStatus()).isEqualTo(PaymentStatus.SUCCESS);
    assertThat(payment.getTransactionId()).isEqualTo("test_transaction_key");
    assertThat(payment.getPaidAt()).isNotNull();
  }

  @Test
  void 결제_실패_상태전이() {
    payment.fail("test_transaction_key", "잔액 부족", null);

    assertThat(payment.getStatus()).isEqualTo(PaymentStatus.FAIL);
    assertThat(payment.getTransactionId()).isEqualTo("test_transaction_key");
    assertThat(payment.getFailReason()).isEqualTo("잔액 부족");
  }

  @Test
  void 결제_취소_상태전이() {
    payment.success("test_transaction_key", null);
    payment.cancel(null);

    assertThat(payment.getStatus()).isEqualTo(PaymentStatus.CANCEL);
  }

  @Test
  void READY_아닌_상태에서_success_호출시_예외() {
    payment.fail("test_transaction_key", "잔액 부족", null);

    assertThatThrownBy(() -> payment.success("test_transaction_key", null))
        .isInstanceOf(IllegalStateException.class);
  }

  @Test
  void READY_아닌_상태에서_fail_호출시_예외() {
    payment.success("test_transaction_key", null);

    assertThatThrownBy(() -> payment.fail("test_transaction_key", "잔액 부족", null))
        .isInstanceOf(IllegalStateException.class);
  }

  @Test
  void SUCCESS_아닌_상태에서_cancel_호출시_예외() {
    assertThatThrownBy(() -> payment.cancel(null)).isInstanceOf(IllegalStateException.class);
  }

  @Test
  void 금액이_음수면_예외() {
    assertThatThrownBy(() -> Payment.create(PaymentFixture.ORDER_ID, -1L))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
