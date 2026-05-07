package legacy.presentation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CreatePaymentRequest {

  @NotNull(message = "주문 ID는 필수입니다.")
  private UUID orderId;

  @NotNull(message = "결제 금액은 필수입니다.")
  @Min(value = 0, message = "결제 금액은 0원 이상이어야 합니다.")
  private Long amount;

}
