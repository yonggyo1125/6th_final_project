package legacy.domain;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository {

  Payment save(Payment payment);

  Optional<Payment> findById(UUID id);

  Optional<Payment> findByOrderId(UUID orderID);

  // orderId에 SUCCESS 결제가 이미 있는지 확인(중복 결제 방지)
  boolean existsByOrderIdAndStatus(UUID orderId, PaymentStatus status);
}
