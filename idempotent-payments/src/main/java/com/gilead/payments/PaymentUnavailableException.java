package com.gilead.payments;

/**
 * The card network did not give a final answer. The idempotency key was released,
 * so the client may retry the same key.
 */
public final class PaymentUnavailableException extends RuntimeException {

    public PaymentUnavailableException(String message) {
        super(message);
    }
}
