package com.gilead.checkout;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Stand-in for an HTTP call that reserves stock. {@link Thread#sleep} is
 * interruptible, which is what a real client must do so a cancelled checkout
 * stops waiting on the warehouse.
 */
public final class WarehouseClient {

    private final Duration latency;
    private final boolean outOfStock;
    private final AtomicBoolean cancelled = new AtomicBoolean();

    public WarehouseClient(Duration latency, boolean outOfStock) {
        this.latency = latency;
        this.outOfStock = outOfStock;
    }

    public StockHold hold(String sku, int quantity) throws InterruptedException {
        pause();
        if (outOfStock) {
            throw new OutOfStockException(sku, quantity);
        }
        return new StockHold(sku, quantity, "wh-12");
    }

    public boolean wasCancelled() {
        return cancelled.get();
    }

    private void pause() throws InterruptedException {
        try {
            Thread.sleep(latency);
        } catch (InterruptedException interrupted) {
            cancelled.set(true);
            throw interrupted;
        }
    }
}
