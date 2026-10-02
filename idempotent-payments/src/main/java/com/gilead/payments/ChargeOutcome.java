package com.gilead.payments;

/**
 * What the card network did. {@link Unavailable} is transient: the service does
 * not store it, so a later retry with the same key may charge once.
 */
public sealed interface ChargeOutcome permits ChargeOutcome.Captured, ChargeOutcome.Declined, ChargeOutcome.Unavailable {

    record Captured() implements ChargeOutcome {
    }

    record Declined(String reason) implements ChargeOutcome {
        public Declined {
            if (reason == null || reason.isBlank()) {
                throw new IllegalArgumentException("decline reason is required");
            }
        }
    }

    record Unavailable(String reason) implements ChargeOutcome {
        public Unavailable {
            if (reason == null || reason.isBlank()) {
                throw new IllegalArgumentException("unavailable reason is required");
            }
        }
    }
}
