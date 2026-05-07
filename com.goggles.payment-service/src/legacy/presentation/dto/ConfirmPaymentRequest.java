package legacy.presentation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ConfirmPaymentRequest {

  @NotNull(message = "paymentKey는 필수입니다.")
  private String paymentKey;
}
