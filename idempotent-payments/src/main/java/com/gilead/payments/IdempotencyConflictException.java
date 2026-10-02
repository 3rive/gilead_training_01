package com.gilead.payments;

/**
 * The client reused an idempotency key for a different payment. Replaying it
 * would hide a bug and might charge the wrong bill.
 */
public final class IdempotencyConflictException extends RuntimeException {

    public IdempotencyConflictException(String message) {
        super(message);
    }
}
