package com.gilead.payments;

import java.util.Locale;

/**
 * One intended debit. Amounts are minor units (paise for INR) so the fingerprint
 * never depends on decimal formatting.
 */
public record PaymentRequest(String customerId, String billReference, long amountMinor, String currency) {

    public PaymentRequest {
        customerId = requireText(customerId, "customerId");
        billReference = requireText(billReference, "billReference");
        if (amountMinor <= 0) {
            throw new IllegalArgumentException("amountMinor must be positive");
        }
        currency = requireText(currency, "currency").toUpperCase(Locale.ROOT);
    }

    /**
     * Identity of the payload. The same idempotency key with a different fingerprint
     * is a conflict, not a retry.
     */
    String fingerprint() {
        return customerId + "|" + billReference + "|" + amountMinor + "|" + currency;
    }

    private static String requireText(String value, String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " is required");
        }
        return value;
    }
}
