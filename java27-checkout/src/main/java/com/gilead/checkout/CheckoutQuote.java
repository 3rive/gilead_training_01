package com.gilead.checkout;

public record CheckoutQuote(
        String orderId,
        StockHold stock,
        PriceQuote price,
        FraudDecision fraud
) {
}
