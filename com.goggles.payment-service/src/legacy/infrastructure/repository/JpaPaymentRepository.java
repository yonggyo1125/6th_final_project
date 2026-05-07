package legacy.infrastructure.repository;

import legacy.domain.Payment;
import legacy.domain.PaymentStatus;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPaymentRepository extends JpaRepository<Payment, UUID> {
  Optional<Payment> findByOrderId(UUID orderID);

  boolean existsByOrderIdAndStatus(UUID orderId, PaymentStatus status);
}
