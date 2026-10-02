package com.gilead.checkout;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

public final class FraudClient {

    private final Duration latency;
    private final boolean decline;
    private final AtomicBoolean cancelled = new AtomicBoolean();

    public FraudClient(Duration latency, boolean decline) {
        this.latency = latency;
        this.decline = decline;
    }

    public FraudDecision screen(String orderId, String cardToken) throws InterruptedException {
        try {
            Thread.sleep(latency);
        } catch (InterruptedException interrupted) {
            cancelled.set(true);
            throw interrupted;
        }
        if (cardToken == null || cardToken.isBlank()) {
            throw new CheckoutFailedException("cardToken is required");
        }
        if (decline) {
            throw new FraudDeclinedException(orderId);
        }
        return new FraudDecision(orderId, true, "clear");
    }

    public boolean wasCancelled() {
        return cancelled.get();
    }
}
