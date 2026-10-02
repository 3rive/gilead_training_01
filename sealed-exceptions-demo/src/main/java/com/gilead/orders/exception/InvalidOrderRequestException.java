package com.gilead.orders.exception;

public final class InvalidOrderRequestException extends OrderException {

    private final String field;

    public InvalidOrderRequestException(String field, String message) {
        super("INVALID_ORDER", message);
        this.field = field;
    }

    public String field() {
        return field;
    }
}
