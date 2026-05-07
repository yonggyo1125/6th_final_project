package legacy.domain.service;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CancelResult {

  private final boolean success;
  private final String failReason;
  private final String paymentLog;
}
