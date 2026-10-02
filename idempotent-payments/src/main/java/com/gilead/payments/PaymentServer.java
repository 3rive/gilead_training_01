package com.gilead.payments;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * {@code POST /payments} with an {@code Idempotency-Key} header.
 * A replay sends the original receipt and {@code Idempotent-Replayed: true}.
 */
public final class PaymentServer implements AutoCloseable {

    private final HttpServer server;

    private PaymentServer(HttpServer server) {
        this.server = server;
    }

    public static PaymentServer start(IdempotentPaymentService payments) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/payments", exchange -> handle(payments, exchange));
        server.start();
        return new PaymentServer(server);
    }

    public int port() {
        return server.getAddress().getPort();
    }

    @Override
    public void close() {
        server.stop(0);
    }

    private static void handle(IdempotentPaymentService payments, HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            send(exchange, 405, false, PaymentJson.error("POST is required"));
            return;
        }
        String key = exchange.getRequestHeaders().getFirst("Idempotency-Key");
        String body = new String(read(exchange.getRequestBody()), StandardCharsets.UTF_8);
        try {
            IdempotentPaymentService.requireKey(key);
            PaymentRequest request = PaymentJson.request(body);
            PaymentResult result = payments.pay(key, request);
            send(exchange, 200, result.replayed(), PaymentJson.receipt(result.receipt()));
        } catch (IllegalArgumentException badRequest) {
            send(exchange, 400, false, PaymentJson.error(badRequest.getMessage()));
        } catch (IdempotencyConflictException conflict) {
            send(exchange, 409, false, PaymentJson.error(conflict.getMessage()));
        } catch (PaymentUnavailableException unavailable) {
            send(exchange, 503, false, PaymentJson.error(unavailable.getMessage()));
        } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            send(exchange, 503, false, PaymentJson.error("Payment was interrupted"));
        }
    }

    private static byte[] read(InputStream input) throws IOException {
        return input.readAllBytes();
    }

    private static void send(HttpExchange exchange, int status, boolean replayed, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        if (replayed) {
            exchange.getResponseHeaders().set("Idempotent-Replayed", "true");
        }
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream output = exchange.getResponseBody()) {
            output.write(bytes);
        }
    }
}
