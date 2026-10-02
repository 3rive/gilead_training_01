package com.gilead.checkout;

public record CheckoutRequest(
        String orderId,
        String sku,
        int quantity,
        String region,
        String cardToken
) {
}
