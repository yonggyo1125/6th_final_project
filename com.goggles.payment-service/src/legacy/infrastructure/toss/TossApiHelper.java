package legacy.infrastructure.toss;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Base64;

@Getter
@Component
public class TossApiHelper {

  private static final String TOSS_BASE_URL = "https://api.tosspayments.com/v1/payments";
  private static final String UNKNOWN = "UNKNOWN";

  private final RestClient restClient;

  public TossApiHelper(@Value("${TOSS_API_KEY}") String secretKey) {
    String encodedSecretKey = Base64.getEncoder().encodeToString((secretKey + ":").getBytes());

    restClient =
        RestClient.builder()
            .baseUrl(URI.create("https://api.tosspayments.com/v1/payments"))
            .defaultHeaders(
                headers -> {
                  headers.setBasicAuth(encodedSecretKey);
                  headers.setContentType(MediaType.APPLICATION_JSON);
                })
            .build();
  }

  // 공통 에러 코드 파싱
  public String parseCode(JsonNode result) {
    if (result == null) {
      return UNKNOWN;
    }
    JsonNode codeNode = result.get("code");
    if (codeNode == null) {
      return UNKNOWN;
    }
    return codeNode.asText();
  }

  // 공통 에러 메세지 파싱
  public String parseMessage(JsonNode result) {
    if (result == null) {
      return UNKNOWN;
    }
    JsonNode messageNode = result.get("message");
    if (messageNode == null) {
      return UNKNOWN;
    }
    return messageNode.asText();
  }

  // 공통 응답 로그 파싱
  public String parseLog(JsonNode result) {
    if (result == null) {
      return null;
    }
    return result.toString();
  }
}
