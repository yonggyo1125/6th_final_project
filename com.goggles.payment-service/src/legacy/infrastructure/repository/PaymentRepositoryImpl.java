package legacy.infrastructure.repository;

import legacy.domain.Payment;
import legacy.domain.PaymentRepository;
import legacy.domain.PaymentStatus;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

  private final JpaPaymentRepository jpaPaymentRepository;

  @Override
  public Payment save(Payment payment) {
    return jpaPaymentRepository.save(payment);
  }

  @Override
  public Optional<Payment> findById(UUID id) {
    return jpaPaymentRepository.findById(id);
  }

  @Override
  public Optional<Payment> findByOrderId(UUID orderId) {
    return jpaPaymentRepository.findByOrderId(orderId);
  }

  @Override
  public boolean existsByOrderIdAndStatus(UUID orderId, PaymentStatus status) {
    return jpaPaymentRepository.existsByOrderIdAndStatus(orderId, status);
  }
}
