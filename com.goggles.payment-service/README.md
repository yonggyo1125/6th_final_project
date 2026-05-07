# Payment Service

결제 처리 및 보상 트랜잭션(취소)을 담당하는 MSA 서비스입니다.

## 기술 스택

| 항목 | 내용 |
|------|------|
| Java | 21 |
| Spring Boot | 3.5.13 |
| Database | PostgreSQL |
| Message Broker | Kafka |
| PG사 | 토스페이먼츠 |

## 포트

| 서비스 | 포트 |
|--------|------|
| Payment Service | 9008 |

## 프로젝트 구조
```aiignore
src/main/java/com/goggles/payment_service/
├── application/                  # 유스케이스
│   ├── PaymentService.java
│   └── PaymentServiceImpl.java
├── domain/                       # 핵심 도메인
│   ├── Payment.java
│   ├── PaymentStatus.java
│   ├── PaymentMethod.java
│   ├── Money.java
│   ├── PaymentRepository.java
│   ├── event/
│   └── service/
├── infrastructure/               # 외부 연동
│   ├── repository/
│   ├── toss/
│   ├── kafka/
│   └── config/
└── presentation/                 # 외부 요청 처리
├── PaymentController.java
└── dto/

```
## 로컬 실행 방법

### 1. 환경 설정 파일 생성

`C:/spring/payment/application-local.yml` 생성

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/goggles
    username: postgres
    password: {비밀번호}
  kafka:
    bootstrap-servers: localhost:9092

eureka:
  client:
    service-url:
      defaultZone: http://localhost:9001/eureka/,http://localhost:9002/eureka/

toss:
  secret-key: {토스 테스트 시크릿 키}

CONFIG_SERVER_URI: http://localhost:9011
CONFIG_PATH: C:/spring/payment/application-local.yml
ZIPKIN_ENDPOINT: http://localhost:9411/api/v2/spans
```

### 2. DB 테이블 생성

```sql
CREATE TABLE p_outbox (
    id              UUID PRIMARY KEY,
    correlation_id  VARCHAR(64)  NOT NULL UNIQUE,
    domain_type     VARCHAR(50)  NOT NULL,
    event_type      VARCHAR(100) NOT NULL,
    payload         TEXT,
    status          VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
    retry_count     INT          NOT NULL DEFAULT 0,
    created_at      TIMESTAMP    NOT NULL,
    updated_at      TIMESTAMP    NOT NULL
);
CREATE INDEX idx_outbox_status ON p_outbox (status);

CREATE TABLE p_inbox (
    id              UUID PRIMARY KEY,
    message_id      UUID         NOT NULL,
    message_group   VARCHAR(100),
    processed_at    TIMESTAMP,
    created_at      TIMESTAMP    NOT NULL,
    updated_at      TIMESTAMP    NOT NULL,
    CONSTRAINT uk_inbox_message_id_group UNIQUE (message_id, message_group)
);
CREATE INDEX idx_inbox_message_group ON p_inbox (message_group);
CREATE INDEX idx_inbox_processed_at  ON p_inbox (processed_at);
```

### 3. Kafka 실행

```bash
cd E:/goggles
docker-compose up -d
```

### 4. 실행 순서
1. EurekaServer-1 (9001)
2. EurekaServer-2 (9002)
3. Config Server (9011)
4. Payment Service (9008)

## 결제 상태 전이
- READY → SUCCESS   결제 승인 성공
- READY → FAIL      결제 승인 실패
- SUCCESS → CANCEL  결제 취소

> 재시도는 새 Payment 생성 (이력 추적 보장)  
> 하나의 orderId에 SUCCESS 결제는 1건만 허용

