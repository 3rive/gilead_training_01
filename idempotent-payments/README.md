# Idempotent bill payment

Priya pays her BESCOM electricity bill in a banking app. She taps Pay for ₹2,480. The bank captures the card, then the response never reaches the phone. The spinner keeps spinning, so she taps Pay again.

That second request is not a second bill. It is the same attempt, sent twice. The service has to charge the card once and hand back the same receipt both times.

## The key

The app creates one id for the attempt and sends it on every try:

```http
POST /payments
Idempotency-Key: 7c9e6679-7425-40de-944b-e07fc1f90ae7
Content-Type: application/json

{"customerId":"priya","billReference":"BESCOM-445219","amountMinor":248000,"currency":"INR"}
```

`amountMinor` is paise. The key names this attempt, not Priya, and not the bill. A new attempt gets a new key.

`IdempotentPaymentService` stores the key with a fingerprint of the body and the receipt:

| What arrives | What happens |
| --- | --- |
| Same key, same bill and amount | Return the stored receipt. The card network is not called. The HTTP response sets `Idempotent-Replayed: true`. |
| Same key, different bill or amount | `409`. The key was already used for another payment, so this is a client bug, not a retry. |
| Same key, while the first charge is still in flight | The duplicate waits and then takes the same receipt. A different body fails immediately. |
| Card network times out | Nothing is stored. The same key may be sent again, and that retry can capture once. |
| Card declined, for example insufficient funds | The decline is stored. A retry returns it and does not hit the card again. |
| Key older than 24 hours | The record has expired. The key may be used for a new charge. |

## Run

Requires JDK 21.

```bash
cd idempotent-payments
mvn test
mvn -q exec:exec
```

The demo prints the lost-response retry (one charge, one receipt) and then the rejected reuse of that key for ₹100.
