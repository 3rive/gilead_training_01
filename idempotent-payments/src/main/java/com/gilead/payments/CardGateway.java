package com.gilead.payments;

/**
 * The side effect idempotency exists to protect. Each {@link #charge} call is one
 * attempt against the card network.
 */
public interface CardGateway {
    ChargeOutcome charge(PaymentRequest request);
}
