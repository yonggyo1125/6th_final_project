package legacy.domain.exception;

import com.goggles.common.exception.ConflictException;
import java.util.UUID;

public class DuplicatePaymentException extends ConflictException {

  public DuplicatePaymentException(UUID orderId) {
    super("이미 결제 완료된 주문입니다. orderId = " + orderId);
  }
}
