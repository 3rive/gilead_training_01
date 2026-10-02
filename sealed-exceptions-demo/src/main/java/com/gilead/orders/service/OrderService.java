package com.gilead.orders.service;

import com.gilead.orders.api.OrderResponse;
import com.gilead.orders.exception.InsufficientStockException;
import com.gilead.orders.exception.InvalidOrderRequestException;
import com.gilead.orders.exception.OrderNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderService {

    private final Map<String, Integer> stock = new ConcurrentHashMap<>(Map.of(
            "BOOK-1", 5,
            "PEN-2", 1
    ));
    private final Map<String, OrderResponse> orders = new ConcurrentHashMap<>();
    private final AtomicInteger sequence = new AtomicInteger();

    public OrderResponse create(String sku, int quantity) {
        if (sku == null || sku.isBlank()) {
            throw new InvalidOrderRequestException("sku", "sku is required");
        }
        if (quantity <= 0) {
            throw new InvalidOrderRequestException("quantity", "quantity must be greater than zero");
        }

        String normalizedSku = sku.trim();
        Integer available = stock.get(normalizedSku);
        if (available == null) {
            throw new InvalidOrderRequestException("sku", "Unknown product " + normalizedSku);
        }
        if (quantity > available) {
            throw new InsufficientStockException(normalizedSku, quantity, available);
        }

        stock.put(normalizedSku, available - quantity);
        String id = "ord-" + sequence.incrementAndGet();
        OrderResponse order = new OrderResponse(id, normalizedSku, quantity);
        orders.put(id, order);
        return order;
    }

    public OrderResponse findById(String orderId) {
        OrderResponse order = orders.get(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }
        return order;
    }
}
