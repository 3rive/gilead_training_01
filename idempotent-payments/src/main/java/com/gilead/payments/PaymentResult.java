package com.gilead.payments;

/**
 * @param replayed true when this call did not touch the card network and returned
 *                  the receipt stored for the idempotency key
 */
public record PaymentResult(PaymentReceipt receipt, boolean replayed) {
}
