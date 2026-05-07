package legacy.domain;

import legacy.domain.exception.PaymentTransitionException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {
  READY("결제 대기"),
  IN_PROGRESS("결제 승인 진행중"),
  WAITING_FOR_DEPOSIT("가상계좌 입금 대기중"),
  DONE("결제 승인"),
  CANCELLED("결제 승인취소"),
  PARTIAL_CANCELLED("결제 부분취소"),
  ABORTED("결제 승인 실패"),
  EXPIRED("결제 유효시간 만료");

  private final String description;

  // 상태 전이가 가능한지 검증
  public static void validateTransition(PaymentStatus currentStatus, PaymentStatus newStatus) {

    boolean possible = true;
    switch (newStatus) {
      case IN_PROGRESS, WAITING_FOR_DEPOSIT, ABORTED, EXPIRED, DONE -> {
        if (currentStatus != PaymentStatus.READY) {
          possible = false;
        }
      }
      case CANCELLED, PARTIAL_CANCELLED -> {
        if (currentStatus != PaymentStatus.DONE) {
          possible = false;
        }
      }
    }

    if (!possible) {
      throw new PaymentTransitionException(currentStatus, newStatus);
    }
  }
}
