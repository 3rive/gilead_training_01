package com.gilead.checkout;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckoutServiceTest {

    private final CheckoutRequest request = new CheckoutRequest("ord-42", "BOOK-1", 2, "IE", "tok_visa");

    @Test
    void quotesWhenEveryDownstreamCallSucceeds() throws InterruptedException {
        CheckoutQuote quote = service(
                new WarehouseClient(Duration.ofMillis(40), false),
                new PricingClient(Duration.ofMillis(40)),
                new FraudClient(Duration.ofMillis(20), false),
                Duration.ofMillis(500)).quote(request);

        assertEquals("ord-42", quote.orderId());
        assertEquals("wh-12", quote.stock().warehouseId());
        assertEquals("25.00", quote.price().total().toPlainString());
        assertTrue(quote.fraud().approved());
    }

    @Test
    void cancelsTheOtherCallsWhenFraudDeclines() {
        WarehouseClient warehouse = new WarehouseClient(Duration.ofSeconds(2), false);
        PricingClient pricing = new PricingClient(Duration.ofSeconds(2));

        FraudDeclinedException failure = assertThrows(FraudDeclinedException.class, () -> service(
                warehouse,
                pricing,
                new FraudClient(Duration.ofMillis(20), true),
                Duration.ofSeconds(5)).quote(request));

        assertEquals("Card declined for order ord-42", failure.getMessage());
        assertTrue(warehouse.wasCancelled());
        assertTrue(pricing.wasCancelled());
    }

    @Test
    void cancelsEveryCallWhenTheDeadlinePasses() {
        WarehouseClient warehouse = new WarehouseClient(Duration.ofSeconds(2), false);
        PricingClient pricing = new PricingClient(Duration.ofSeconds(2));
        FraudClient fraud = new FraudClient(Duration.ofSeconds(2), false);

        CheckoutFailedException failure = assertThrows(CheckoutFailedException.class, () -> service(
                warehouse, pricing, fraud, Duration.ofMillis(80)).quote(request));

        assertInstanceOf(
                java.util.concurrent.StructuredTaskScope.CancelledByTimeoutException.class,
                failure.getCause());
        assertTrue(warehouse.wasCancelled());
        assertTrue(pricing.wasCancelled());
        assertTrue(fraud.wasCancelled());
    }

    private static CheckoutService service(
            WarehouseClient warehouse,
            PricingClient pricing,
            FraudClient fraud,
            Duration deadline) {
        return new CheckoutService(warehouse, pricing, fraud, deadline);
    }
}
