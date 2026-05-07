package com.goggles.payment_service.infrastructure.toss;

import com.fasterxml.jackson.databind.JsonNode;
import com.goggles.payment_service.domain.service.CancelPayment;
import com.goggles.payment_service.domain.service.CancelResult;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Slf4j
@Component
@RequiredArgsConstructor
public class TossCancelPayment implements CancelPayment {

  private final TossApiHelper tossApiHelper;

  @Override
  public CancelResult cancel(String paymentId, String paymentKey, String cancelReason) {
    RestClient restClient = tossApiHelper.getRestClient();
    String idempotencyKey = paymentId + "-cancel";

    log.info("토스 결제 취소 요청 시작, Payment Key: {}", paymentKey);

    try {
      JsonNode result =
          restClient
              .post()
              .uri(uriBuilder -> uriBuilder.path("/{paymentKey}/cancel").build(paymentKey))
              .header("idempotency-Key", idempotencyKey)
              .body(Map.of("cancelReason", cancelReason))
              .retrieve()
              .body(JsonNode.class);

      log.info("토스 결제 취소 성공, Payment Key: {}", paymentKey);
      return CancelResult.builder()
          .success(true)
          .paymentLog(result != null ? result.toString() : null)
          .build();
    } catch (RestClientResponseException e) {
      JsonNode result = e.getResponseBodyAs(JsonNode.class);

      String code = tossApiHelper.parseCode(result);
      String message = tossApiHelper.parseMessage(result);

      log.error(
          "토스 결제 취소 실패, HTTP 상태코드: {}, Payment Key: {}, 에러코드: {}, 에러메세지: {}",
          e.getStatusCode().value(),
          paymentKey,
          code,
          message);

      return CancelResult.builder()
          .success(false)
          .failReason("[%s]%s".formatted(code, message))
          .paymentLog(tossApiHelper.parseLog(result))
          .build();

    } catch (Exception e) {
      log.error("토스 결제 취소 실패, Payment Key: {}, 에러메세지: {}", paymentKey, e.getMessage());

      return CancelResult.builder().success(false).failReason("[UNKNOWN]" + e.getMessage()).build();
    }
  }
}
