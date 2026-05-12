document.addEventListener("DOMContentLoaded", async () => {
    // 테스트용 클라이언트 키 (실제 연동 시 전달받은 키로 교체 필요)
    const clientKey = "test_gck_docs_Ovk5rk1E3wkP19L4bgBvl0OR";
    const customerKey = Math.random().toString(36).substring(2, 12); // 비회원 결제용 임시 키

    const tossPayments = TossPayments(clientKey);
    const widgets = tossPayments.widgets({ customerKey });

    // 결제 금액 설정 (서버에서 받은 amount 사용)
    await widgets.setAmount({
        currency: "KRW",
        value: amount,
    });

    // 결제 수단 UI 렌더링
    await widgets.renderPaymentMethods({
        selector: "#payment-method",
        variantKey: "DEFAULT",
    });

    // 약관 UI 렌더링
    await widgets.renderAgreement({
        selector: "#agreement",
        variantKey: "AGREEMENT",
    });

    const paymentButton = document.getElementById("payment-button");

    paymentButton.addEventListener("click", async () => {
        try {
            await widgets.requestPayment({
                orderId: orderId,
                orderName: orderName,
                successUrl: `http://localhost:10006/success`,
                failUrl: `http://localhost:10006/failure`,
                // 추가 정보가 필요한 경우 customerEmail 등을 추가할 수 있습니다.
            });
        } catch (error) {
            console.error("결제 요청 실패:", error);
        }
    });
});