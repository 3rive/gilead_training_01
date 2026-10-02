package com.gilead.orders.api;

import java.util.Map;

public record ErrorResponse(String code, String message, Map<String, Object> details) {

    public ErrorResponse {
        details = details == null ? Map.of() : Map.copyOf(details);
    }
}
