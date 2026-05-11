package com.goggles.payment_service.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class PaymentLog {
    @Id
    @Column(length=45)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(length=30, nullable = false)
    private PaymentStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(length=30, nullable = false)
    private PaymentStatus toStatus;

    @JdbcTypeCode(SqlTypes.JSON)
    private String log;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    protected PaymentLog(PaymentStatus fromStatus, PaymentStatus toStatus, String log) {
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.log = log;
    }
}
