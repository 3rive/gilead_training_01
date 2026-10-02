package com.gilead.checkout;

public class CheckoutFailedException extends RuntimeException {

    public CheckoutFailedException(String message) {
        super(message);
    }

    public CheckoutFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
