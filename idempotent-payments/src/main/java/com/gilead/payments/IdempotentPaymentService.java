package com.gilead.payments;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Charges a card at most once per idempotency key.
 *
 * <p>The key names one attempt, not one customer. A retry of the same bill and
 * amount returns the stored receipt. The same key with a different bill or amount
 * is rejected. A decline is final and is replayed. A network failure is not stored,
 * so the same key can be tried again.
 */
public final class IdempotentPaymentService {

    static final Duration DEFAULT_RETENTION = Duration.ofHours(24);
    static final Duration DEFAULT_WAIT = Duration.ofSeconds(30);

    private final CardGateway gateway;
    private final Clock clock;
    private final Duration retention;
    private final Duration waitTimeout;
    private final ConcurrentHashMap<String, Slot> slots = new ConcurrentHashMap<>();

    public IdempotentPaymentService(CardGateway gateway) {
        this(gateway, Clock.systemUTC(), DEFAULT_RETENTION, DEFAULT_WAIT);
    }

    public IdempotentPaymentService(CardGateway gateway, Clock clock, Duration retention, Duration waitTimeout) {
        this.gateway = Objects.requireNonNull(gateway, "gateway");
        this.clock = Objects.requireNonNull(clock, "clock");
        this.retention = Objects.requireNonNull(retention, "retention");
        this.waitTimeout = Objects.requireNonNull(waitTimeout, "waitTimeout");
        if (retention.isNegative() || retention.isZero()) {
            throw new IllegalArgumentException("retention must be positive");
        }
        if (waitTimeout.isNegative() || waitTimeout.isZero()) {
            throw new IllegalArgumentException("waitTimeout must be positive");
        }
    }

    public PaymentResult pay(String idempotencyKey, PaymentRequest request) throws InterruptedException {
        String key = requireKey(idempotencyKey);
        Objects.requireNonNull(request, "request");
        Slot slot = slots.computeIfAbsent(key, ignored -> new Slot());
        PaymentReceipt replay = begin(slot, request.fingerprint());
        if (replay != null) {
            return new PaymentResult(replay, true);
        }
        try {
            return switch (gateway.charge(request)) {
                case ChargeOutcome.Unavailable unavailable -> {
                    abandon(slot);
                    throw new PaymentUnavailableException(unavailable.reason());
                }
                case ChargeOutcome.Captured() -> complete(slot, request, PaymentOutcome.CAPTURED, "Captured");
                case ChargeOutcome.Declined declined -> complete(slot, request, PaymentOutcome.DECLINED, declined.reason());
            };
        } catch (PaymentUnavailableException unavailable) {
            throw unavailable;
        } catch (RuntimeException failure) {
            abandon(slot);
            throw failure;
        }
    }

    /**
     * @return the stored receipt when this call is a replay, or null when this caller owns the charge
     */
    private PaymentReceipt begin(Slot slot, String fingerprint) throws InterruptedException {
        slot.lock.lock();
        try {
            while (true) {
                if (slot.inProgress) {
                    if (!fingerprint.equals(slot.fingerprint)) {
                        throw conflict();
                    }
                    if (!slot.done.await(waitTimeout.toMillis(), TimeUnit.MILLISECONDS)) {
                        throw new PaymentUnavailableException(
                                "Timed out waiting for the in-flight payment with this idempotency key");
                    }
                    continue;
                }
                if (slot.receipt != null && !expired(slot)) {
                    if (!fingerprint.equals(slot.fingerprint)) {
                        throw conflict();
                    }
                    return slot.receipt;
                }
                slot.inProgress = true;
                slot.fingerprint = fingerprint;
                slot.receipt = null;
                slot.completedAt = null;
                return null;
            }
        } finally {
            slot.lock.unlock();
        }
    }

    private PaymentResult complete(Slot slot, PaymentRequest request, PaymentOutcome outcome, String detail) {
        PaymentReceipt receipt = new PaymentReceipt(
                "pay_" + UUID.randomUUID().toString().replace("-", ""),
                outcome,
                request.customerId(),
                request.billReference(),
                request.amountMinor(),
                request.currency(),
                detail);
        slot.lock.lock();
        try {
            slot.receipt = receipt;
            slot.completedAt = clock.instant();
            slot.inProgress = false;
            slot.done.signalAll();
            return new PaymentResult(receipt, false);
        } finally {
            slot.lock.unlock();
        }
    }

    private void abandon(Slot slot) {
        slot.lock.lock();
        try {
            slot.inProgress = false;
            slot.fingerprint = null;
            slot.receipt = null;
            slot.completedAt = null;
            slot.done.signalAll();
        } finally {
            slot.lock.unlock();
        }
    }

    private boolean expired(Slot slot) {
        Instant completedAt = slot.completedAt;
        return completedAt == null || clock.instant().isAfter(completedAt.plus(retention));
    }

    private static IdempotencyConflictException conflict() {
        return new IdempotencyConflictException(
                "Idempotency key was already used for a different payment");
    }

    static String requireKey(String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new IllegalArgumentException("Idempotency-Key is required");
        }
        if (idempotencyKey.length() > 255) {
            throw new IllegalArgumentException("Idempotency-Key must be at most 255 characters");
        }
        return idempotencyKey;
    }

    private static final class Slot {
        private final ReentrantLock lock = new ReentrantLock();
        private final Condition done = lock.newCondition();
        private boolean inProgress;
        private String fingerprint;
        private PaymentReceipt receipt;
        private Instant completedAt;
    }
}
