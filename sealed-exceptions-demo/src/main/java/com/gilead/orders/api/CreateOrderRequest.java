package com.gilead.orders.api;

import jakarta.validation.constraints.NotBlank;

public record CreateOrderRequest(
        @NotBlank String sku,
        int quantity
) {
}
