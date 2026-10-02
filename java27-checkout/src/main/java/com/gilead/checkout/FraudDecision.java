package com.gilead.checkout;

public record FraudDecision(String orderId, boolean approved, String reason) {
}
