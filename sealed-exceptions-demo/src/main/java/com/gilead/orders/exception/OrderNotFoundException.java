package com.gilead.orders.exception;

public final class OrderNotFoundException extends OrderException {

    private final String orderId;

    public OrderNotFoundException(String orderId) {
        super("ORDER_NOT_FOUND", "Order " + orderId + " was not found");
        this.orderId = orderId;
    }

    public String orderId() {
        return orderId;
    }
}
