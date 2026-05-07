package legacy.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Money {

  private Long amount;

  public static Money of(Long amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("결제 금액은 0원 이상이어야 합니다.");
    }
    return new Money(amount);
  }

  private Money(Long amount) {
    this.amount = amount;
  }
}
