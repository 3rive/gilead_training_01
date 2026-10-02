package com.gilead.checkout;

public final class FraudDeclinedException extends CheckoutFailedException {

    public FraudDeclinedException(String orderId) {
        super("Card declined for order " + orderId);
    }
}
