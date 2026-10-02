package com.gilead.checkout;

import java.time.Duration;

public final class CheckoutDemo {

    public static void main(String[] args) throws InterruptedException {
        CheckoutRequest request = new CheckoutRequest("ord-42", "BOOK-1", 2, "IE", "tok_visa");

        System.out.println(quote(
                request,
                new WarehouseClient(Duration.ofMillis(80), false),
                new PricingClient(Duration.ofMillis(80)),
                new FraudClient(Duration.ofMillis(40), false),
                Duration.ofMillis(500)));

        WarehouseClient warehouse = new WarehouseClient(Duration.ofSeconds(2), false);
        try {
            quote(request, warehouse, new PricingClient(Duration.ofSeconds(2)),
                    new FraudClient(Duration.ofMillis(30), true), Duration.ofSeconds(3));
        } catch (FraudDeclinedException declined) {
            System.out.println(declined.getMessage() + "; warehouse cancelled=" + warehouse.wasCancelled());
        }
    }

    private static CheckoutQuote quote(
            CheckoutRequest request,
            WarehouseClient warehouse,
            PricingClient pricing,
            FraudClient fraud,
            Duration deadline) throws InterruptedException {
        return new CheckoutService(warehouse, pricing, fraud, deadline).quote(request);
    }
}
