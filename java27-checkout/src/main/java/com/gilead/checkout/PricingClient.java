package com.gilead.checkout;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

public final class PricingClient {

    private final Duration latency;
    private final AtomicBoolean cancelled = new AtomicBoolean();

    public PricingClient(Duration latency) {
        this.latency = latency;
    }

    public PriceQuote quote(String sku, int quantity, String region) throws InterruptedException {
        try {
            Thread.sleep(latency);
        } catch (InterruptedException interrupted) {
            cancelled.set(true);
            throw interrupted;
        }
        BigDecimal unit = "IE".equals(region) ? new BigDecimal("12.50") : new BigDecimal("11.00");
        return new PriceQuote(sku, quantity, unit.multiply(BigDecimal.valueOf(quantity)), "EUR");
    }

    public boolean wasCancelled() {
        return cancelled.get();
    }
}
