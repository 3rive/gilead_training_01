package com.gilead.checkout;

import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;

/**
 * Builds one payable quote from three independent downstream calls.
 *
 * <p>Java 27 structured concurrency (JEP 533, preview) keeps those calls inside
 * one scope. If fraud declines the card, or the warehouse is out of stock, the
 * other calls are cancelled. If the deadline passes, the whole scope is cancelled.
 * Closing the scope waits until every forked thread has actually stopped.
 */
public final class CheckoutService {

    private final WarehouseClient warehouse;
    private final PricingClient pricing;
    private final FraudClient fraud;
    private final Duration deadline;

    public CheckoutService(
            WarehouseClient warehouse,
            PricingClient pricing,
            FraudClient fraud,
            Duration deadline) {
        this.warehouse = warehouse;
        this.pricing = pricing;
        this.fraud = fraud;
        this.deadline = deadline;
    }

    public CheckoutQuote quote(CheckoutRequest request) throws InterruptedException {
        try (var scope = StructuredTaskScope.<Object>open(config -> config
                .withName("checkout-" + request.orderId())
                .withTimeout(deadline))) {

            Subtask<StockHold> stock = scope.fork(() -> warehouse.hold(request.sku(), request.quantity()));
            Subtask<PriceQuote> price = scope.fork(() -> pricing.quote(request.sku(), request.quantity(), request.region()));
            Subtask<FraudDecision> decision = scope.fork(() -> fraud.screen(request.orderId(), request.cardToken()));

            scope.join();

            return new CheckoutQuote(request.orderId(), stock.get(), price.get(), decision.get());
        } catch (ExecutionException failure) {
            throw unwrap(failure);
        }
    }

    private CheckoutFailedException unwrap(ExecutionException failure) {
        Throwable cause = failure.getCause();
        return switch (cause) {
            case CheckoutFailedException checkoutFailed -> checkoutFailed;
            case StructuredTaskScope.CancelledByTimeoutException timeout ->
                    new CheckoutFailedException("Checkout exceeded " + deadline, timeout);
            case null, default -> new CheckoutFailedException("Checkout failed", cause == null ? failure : cause);
        };
    }
}
