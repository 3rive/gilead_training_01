package com.gilead.checkout;

import java.math.BigDecimal;

public record PriceQuote(String sku, int quantity, BigDecimal total, String currency) {
}
