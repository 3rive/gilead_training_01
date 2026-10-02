# Checkout fan-out with Java 27 structured concurrency

A checkout page cannot show a payable total until three other systems answer:

- the warehouse, which holds stock
- pricing, which applies tax and the promotion for the region
- fraud screening, which accepts or declines the card

Those calls do not depend on each other, so waiting for them one after another makes the page slow. Starting them on an `ExecutorService` and then calling `Future.get()` has the opposite problem: if fraud declines the card, the warehouse call keeps running and can hold stock for an order that will never be paid.

`CheckoutService` uses `StructuredTaskScope` from Java 27 (JEP 533, seventh preview). The three calls are forked in one scope with an 800 ms deadline in the demo. `join()` returns only when every call has succeeded. If one throws, the scope cancels the others. If the deadline passes, the scope cancels all of them. Leaving the `try` block waits until those threads have stopped.

`StructuredTaskScope` is still a preview API, so the compiler and the JVM both need `--enable-preview`.

## Run

Requires JDK 27.

```bash
cd java27-checkout
mvn test
mvn -q exec:exec
```

The demo prints a successful quote, then a fraud decline where the warehouse call was cancelled instead of running for its full two seconds.
