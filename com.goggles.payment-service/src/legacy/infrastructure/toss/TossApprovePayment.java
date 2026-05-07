package legacy.infrastructure.toss;

import com.fasterxml.jackson.databind.JsonNode;
import legacy.domain.service.ApprovePayment;
import legacy.domain.service.ApproveResult;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TossApprovePayment implements ApprovePayment {

  private final TossApiHelper tossApiHelper;

  @Override
  public ApproveResult request(String paymentId, String paymentKey, String orderId, Long amount) {
    RestClient restClient = tossApiHelper.getRestClient();

    log.info("토스 결제 승인 요청 시작, 주문 ID: {}, Payment Key: {}, 결제금액: {}", orderId, paymentKey, amount);

    try {
      JsonNode result =
          restClient
              .post()
              .uri(uriBuilder -> uriBuilder.path("/confirm").build())
              .body(
                  Map.of(
                      "paymentKey", paymentKey,
                      "orderId", orderId,
                      "amount", amount))
              .header("Idempotency-Key", paymentId)
              .retrieve()
              .body(JsonNode.class);

      LocalDateTime approvedAt = null;
      if (result != null && result.get("approvedAt") != null) {
        approvedAt =
            LocalDateTime.parse(result.get("approvedAt").asText(), DateTimeFormatter.ISO_DATE_TIME);
      }

      log.info("토스 결제 승인 성공, 주문 ID:{}, Payment Key: {}", orderId, paymentKey);

      return ApproveResult.builder()
          .success(true)
          .paymentKey(paymentKey)
          .approveAt(approvedAt)
          .paymentLog(tossApiHelper.parseLog(result))
          .build();

    } catch (RestClientResponseException e) {
      JsonNode result = e.getResponseBodyAs(JsonNode.class);
      String code = tossApiHelper.parseCode(result);
      String message = tossApiHelper.parseMessage(result);

      log.error(
          "토스 결제 승인 실패, HTTP 상태코드: {}, 주문 ID: {}, 에러코드: {}, 에러메세지: {}",
          e.getStatusCode().value(),
          orderId,
          code,
          message);

      return ApproveResult.builder()
          .success(false)
          .failReason("[%s]%s".formatted(code, message))
          .paymentLog(tossApiHelper.parseLog(result))
          .build();

    } catch (Exception e) {
      log.error("토스 결제 승인 실패, 주문 Id: {}, 에러메세지: {}", orderId, e.getMessage());

      return ApproveResult.builder()
          .success(false)
          .failReason("[UNKNOWN]" + e.getMessage())
          .build();
    }
  }
}
