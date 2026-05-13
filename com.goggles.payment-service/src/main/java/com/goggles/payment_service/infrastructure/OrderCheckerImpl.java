package com.goggles.payment_service.infrastructure;

import com.goggles.payment_service.domain.QPayment;
import com.goggles.payment_service.domain.service.OrderChecker;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderCheckerImpl implements OrderChecker {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean isDuplicated(UUID orderId) {
        QPayment payment = QPayment.payment;
        return queryFactory.select(payment.count())
                .from(payment)
                .where(payment.orderDetail.orderId.eq(orderId))
                .stream().allMatch(count -> count > 0L);
    }
}
