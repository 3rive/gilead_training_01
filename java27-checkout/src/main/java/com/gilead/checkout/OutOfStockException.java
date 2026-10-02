package com.gilead.checkout;

public final class OutOfStockException extends CheckoutFailedException {

    public OutOfStockException(String sku, int quantity) {
        super("No stock for " + quantity + " of " + sku);
    }
}
