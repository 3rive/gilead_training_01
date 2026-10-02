package com.gilead.payments;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentServerTest {

    private static final String BODY = """
            {"customerId":"priya","billReference":"BESCOM-445219","amountMinor":248000,"currency":"INR"}
            """;

    @Test
    void replayReturnsTheOriginalReceiptAndDoesNotChargeAgain() throws Exception {
        CountingGateway gateway = new CountingGateway();
        IdempotentPaymentService payments = new IdempotentPaymentService(gateway);
        try (PaymentServer server = PaymentServer.start(payments)) {
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> first = post(client, server, "attempt-1", BODY);
            HttpResponse<String> retry = post(client, server, "attempt-1", BODY);

            assertEquals(200, first.statusCode());
            assertEquals(200, retry.statusCode());
            assertEquals(first.body(), retry.body());
            assertFalse(first.headers().firstValue("Idempotent-Replayed").isPresent());
            assertEquals("true", retry.headers().firstValue("Idempotent-Replayed").orElseThrow());
            assertTrue(first.body().contains("\"outcome\":\"CAPTURED\""));
            assertTrue(first.body().contains("\"amountMinor\":248000"));
            assertEquals(1, gateway.calls);
        }
    }

    @Test
    void aReusedKeyForAnotherAmountIsRejected() throws Exception {
        CountingGateway gateway = new CountingGateway();
        IdempotentPaymentService payments = new IdempotentPaymentService(gateway);
        try (PaymentServer server = PaymentServer.start(payments)) {
            HttpClient client = HttpClient.newHttpClient();
            assertEquals(200, post(client, server, "attempt-1", BODY).statusCode());

            String other = """
                    {"customerId":"priya","billReference":"BESCOM-445219","amountMinor":10000,"currency":"INR"}
                    """;
            HttpResponse<String> conflict = post(client, server, "attempt-1", other);
            assertEquals(409, conflict.statusCode());
            assertTrue(conflict.body().contains("different payment"));
            assertEquals(1, gateway.calls);
        }
    }

    @Test
    void missingKeyIsABadRequest() throws Exception {
        IdempotentPaymentService payments = new IdempotentPaymentService(new CountingGateway());
        try (PaymentServer server = PaymentServer.start(payments)) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://127.0.0.1:" + server.port() + "/payments"))
                    .timeout(Duration.ofSeconds(5))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(BODY))
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(400, response.statusCode());
        }
    }

    private static HttpResponse<String> post(HttpClient client, PaymentServer server, String key, String body)
            throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:" + server.port() + "/payments"))
                .timeout(Duration.ofSeconds(5))
                .header("Content-Type", "application/json")
                .header("Idempotency-Key", key)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static final class CountingGateway implements CardGateway {
        private int calls;

        @Override
        public synchronized ChargeOutcome charge(PaymentRequest request) {
            calls++;
            return new ChargeOutcome.Captured();
        }
    }
}
