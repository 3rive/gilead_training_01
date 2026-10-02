package com.gilead.payments;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IdempotentPaymentServiceTest {

    private static final String KEY = "7c9e6679-7425-40de-944b-e07fc1f90ae7";
    private static final PaymentRequest BILL = new PaymentRequest("priya", "BESCOM-445219", 248_000, "INR");

    @Test
    void retriesTheLostResponseWithoutChargingAgain() throws InterruptedException {
        ScriptedCardGateway gateway = new ScriptedCardGateway();
        IdempotentPaymentService payments = new IdempotentPaymentService(gateway);

        PaymentResult first = payments.pay(KEY, BILL);
        PaymentResult retry = payments.pay(KEY, BILL);

        assertFalse(first.replayed());
        assertTrue(retry.replayed());
        assertEquals(first.receipt(), retry.receipt());
        assertEquals(PaymentOutcome.CAPTURED, first.receipt().outcome());
        assertEquals(1, gateway.calls());
    }

    @Test
    void rejectsTheSameKeyWhenTheBillChanges() throws InterruptedException {
        ScriptedCardGateway gateway = new ScriptedCardGateway();
        IdempotentPaymentService payments = new IdempotentPaymentService(gateway);
        payments.pay(KEY, BILL);

        PaymentRequest otherAmount = new PaymentRequest("priya", "BESCOM-445219", 10_000, "INR");
        assertThrows(IdempotencyConflictException.class, () -> payments.pay(KEY, otherAmount));
        assertEquals(1, gateway.calls());
    }

    @Test
    void replaysADeclineWithoutCallingTheCardNetworkAgain() throws InterruptedException {
        ScriptedCardGateway gateway = new ScriptedCardGateway();
        gateway.enqueue(new ChargeOutcome.Declined("Insufficient funds"));
        IdempotentPaymentService payments = new IdempotentPaymentService(gateway);

        PaymentResult first = payments.pay(KEY, BILL);
        PaymentResult retry = payments.pay(KEY, BILL);

        assertEquals(PaymentOutcome.DECLINED, first.receipt().outcome());
        assertEquals("Insufficient funds", retry.receipt().detail());
        assertEquals(first.receipt().paymentId(), retry.receipt().paymentId());
        assertEquals(1, gateway.calls());
    }

    @Test
    void letsTheSameKeyChargeOnceAfterANetworkFailure() throws InterruptedException {
        ScriptedCardGateway gateway = new ScriptedCardGateway();
        gateway.enqueue(new ChargeOutcome.Unavailable("Card network timed out"));
        IdempotentPaymentService payments = new IdempotentPaymentService(gateway);

        assertThrows(PaymentUnavailableException.class, () -> payments.pay(KEY, BILL));
        PaymentResult recovered = payments.pay(KEY, BILL);

        assertEquals(PaymentOutcome.CAPTURED, recovered.receipt().outcome());
        assertFalse(recovered.replayed());
        assertEquals(2, gateway.calls());

        PaymentResult replay = payments.pay(KEY, BILL);
        assertTrue(replay.replayed());
        assertEquals(recovered.receipt().paymentId(), replay.receipt().paymentId());
        assertEquals(2, gateway.calls());
    }

    @Test
    void oneChargeWhenEightTapsArriveTogether() throws Exception {
        ScriptedCardGateway gateway = new ScriptedCardGateway();
        gateway.pause(Duration.ofMillis(80));
        IdempotentPaymentService payments = new IdempotentPaymentService(gateway);

        ExecutorService pool = Executors.newFixedThreadPool(8);
        try {
            List<Future<PaymentResult>> taps = new ArrayList<>();
            for (int i = 0; i < 8; i++) {
                taps.add(pool.submit(() -> payments.pay(KEY, BILL)));
            }
            String paymentId = taps.get(0).get(5, TimeUnit.SECONDS).receipt().paymentId();
            for (Future<PaymentResult> tap : taps) {
                assertEquals(paymentId, tap.get(5, TimeUnit.SECONDS).receipt().paymentId());
            }
        } finally {
            pool.shutdownNow();
        }
        assertEquals(1, gateway.calls());
    }

    @Test
    void aDifferentBillDoesNotWaitBehindAnInFlightCharge() throws Exception {
        ScriptedCardGateway gateway = new ScriptedCardGateway();
        gateway.holdFirstCharge();
        IdempotentPaymentService payments = new IdempotentPaymentService(gateway);

        ExecutorService pool = Executors.newSingleThreadExecutor();
        try {
            Future<PaymentResult> first = pool.submit(() -> payments.pay(KEY, BILL));
            gateway.awaitFirstCharge();

            PaymentRequest other = new PaymentRequest("priya", "BESCOM-990011", 248_000, "INR");
            assertThrows(IdempotencyConflictException.class, () -> payments.pay(KEY, other));
            assertEquals(1, gateway.calls());

            gateway.release();
            assertEquals(PaymentOutcome.CAPTURED, first.get(5, TimeUnit.SECONDS).receipt().outcome());
            assertEquals(1, gateway.calls());
        } finally {
            gateway.release();
            pool.shutdownNow();
        }
    }

    @Test
    void anExpiredKeyCanBeUsedForANewCharge() throws InterruptedException {
        MutableClock clock = new MutableClock(Instant.parse("2026-10-02T09:00:00Z"));
        ScriptedCardGateway gateway = new ScriptedCardGateway();
        IdempotentPaymentService payments = new IdempotentPaymentService(
                gateway, clock, Duration.ofHours(24), Duration.ofSeconds(2));

        PaymentResult first = payments.pay(KEY, BILL);
        clock.advance(Duration.ofHours(24).plusSeconds(1));
        PaymentResult again = payments.pay(KEY, BILL);

        assertFalse(again.replayed());
        assertNotEquals(first.receipt().paymentId(), again.receipt().paymentId());
        assertEquals(2, gateway.calls());
    }

    @Test
    void requiresAKey() {
        IdempotentPaymentService payments = new IdempotentPaymentService(new ScriptedCardGateway());
        assertThrows(IllegalArgumentException.class, () -> payments.pay("  ", BILL));
    }

    private static final class MutableClock extends Clock {
        private Instant current;

        private MutableClock(Instant current) {
            this.current = current;
        }

        private void advance(Duration duration) {
            current = current.plus(duration);
        }

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return current;
        }
    }

    private static final class ScriptedCardGateway implements CardGateway {
        private final java.util.Queue<ChargeOutcome> script = new java.util.concurrent.ConcurrentLinkedQueue<>();
        private final AtomicInteger calls = new AtomicInteger();
        private final CountDownLatch entered = new CountDownLatch(1);
        private final CountDownLatch release = new CountDownLatch(1);
        private volatile boolean hold;
        private volatile Duration pause = Duration.ZERO;

        private void enqueue(ChargeOutcome outcome) {
            script.add(outcome);
        }

        private void pause(Duration pause) {
            this.pause = pause;
        }

        private void holdFirstCharge() {
            hold = true;
        }

        private void awaitFirstCharge() throws InterruptedException {
            assertTrue(entered.await(5, TimeUnit.SECONDS));
        }

        private void release() {
            release.countDown();
        }

        private int calls() {
            return calls.get();
        }

        @Override
        public ChargeOutcome charge(PaymentRequest request) {
            int call = calls.incrementAndGet();
            entered.countDown();
            if (hold && call == 1) {
                try {
                    if (!release.await(5, TimeUnit.SECONDS)) {
                        throw new IllegalStateException("charge was not released");
                    }
                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException(interrupted);
                }
            }
            if (!pause.isZero()) {
                try {
                    Thread.sleep(pause.toMillis());
                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException(interrupted);
                }
            }
            ChargeOutcome outcome = script.poll();
            return outcome == null ? new ChargeOutcome.Captured() : outcome;
        }
    }
}
