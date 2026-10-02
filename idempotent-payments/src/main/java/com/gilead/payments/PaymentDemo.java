package com.gilead.payments;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Priya pays a BESCOM electricity bill from her phone. The first response is lost,
 * so the app sends the same payment again. The card is charged once.
 */
public final class PaymentDemo {

    static final String KEY = "7c9e6679-7425-40de-944b-e07fc1f90ae7";

    public static void main(String[] args) throws InterruptedException {
        CountingGateway gateway = new CountingGateway();
        IdempotentPaymentService payments = new IdempotentPaymentService(gateway);
        PaymentRequest bill = new PaymentRequest("priya", "BESCOM-445219", 248_000, "INR");

        System.out.println("Priya pays BESCOM bill 445219, " + rupees(bill.amountMinor()));
        System.out.println("Idempotency-Key: " + KEY);
        System.out.println();

        PaymentResult first = payments.pay(KEY, bill);
        print("1. First tap. The bank captures the payment, then the response is lost.", first, gateway);

        PaymentResult retry = payments.pay(KEY, bill);
        print("2. The app retries the same key and the same bill.", retry, gateway);

        PaymentRequest other = new PaymentRequest("priya", "BESCOM-445219", 10_000, "INR");
        System.out.println("3. A bug reuses the key for " + rupees(other.amountMinor()) + ".");
        try {
            payments.pay(KEY, other);
        } catch (IdempotencyConflictException conflict) {
            System.out.println("   rejected: " + conflict.getMessage());
            System.out.println("   card network charges: " + gateway.charges());
        }
    }

    private static void print(String step, PaymentResult result, CountingGateway gateway) {
        PaymentReceipt receipt = result.receipt();
        System.out.println(step);
        System.out.println("   receipt " + receipt.paymentId()
                + "  " + receipt.outcome()
                + "  " + rupees(receipt.amountMinor())
                + (result.replayed() ? "  (replayed)" : ""));
        System.out.println("   card network charges: " + gateway.charges());
        System.out.println();
    }

    private static String rupees(long paise) {
        String whole = Long.toString(paise / 100);
        if (whole.length() > 3) {
            int split = whole.length() - 3;
            whole = whole.substring(0, split) + "," + whole.substring(split);
        }
        return "₹" + whole + "." + String.format("%02d", paise % 100);
    }

    private static final class CountingGateway implements CardGateway {
        private final AtomicInteger charges = new AtomicInteger();

        @Override
        public ChargeOutcome charge(PaymentRequest request) {
            charges.incrementAndGet();
            return new ChargeOutcome.Captured();
        }

        int charges() {
            return charges.get();
        }
    }
}
