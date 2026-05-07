package legacy.domain.service;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApproveResult {

  private final boolean success;
  private final String paymentKey;
  private final String failReason;
  private final LocalDateTime approveAt;
  private final String paymentLog;
}
