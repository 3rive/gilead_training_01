package com.gilead.payments;

public record PaymentReceipt(
        String paymentId,
        PaymentOutcome outcome,
        String customerId,
        String billReference,
        long amountMinor,
        String currency,
        String detail) {
}
