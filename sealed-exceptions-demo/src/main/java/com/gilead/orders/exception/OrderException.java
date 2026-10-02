package com.gilead.orders.exception;

/**
 * Closed set of failures the order API is allowed to raise.
 *
 * <p>Because this type is sealed, {@code GlobalExceptionHandler} can switch on it
 * and the compiler checks that every permitted subtype has a branch. Adding a new
 * order failure means listing it here and mapping it to an HTTP response; otherwise
 * the project does not compile.
 */
public abstract sealed class OrderException extends RuntimeException
        permits OrderNotFoundException, InsufficientStockException, InvalidOrderRequestException {

    private final String errorCode;

    protected OrderException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String errorCode() {
        return errorCode;
    }
}
