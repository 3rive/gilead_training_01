package com.gilead.medicalinventory.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class StockMovementTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static StockMovement getStockMovementSample1() {
        return new StockMovement().id(1L).quantity(1).reason("reason1").referenceNumber("referenceNumber1");
    }

    public static StockMovement getStockMovementSample2() {
        return new StockMovement().id(2L).quantity(2).reason("reason2").referenceNumber("referenceNumber2");
    }

    public static StockMovement getStockMovementRandomSampleGenerator() {
        return new StockMovement()
            .id(longCount.incrementAndGet())
            .quantity(intCount.incrementAndGet())
            .reason(UUID.randomUUID().toString())
            .referenceNumber(UUID.randomUUID().toString());
    }
}
