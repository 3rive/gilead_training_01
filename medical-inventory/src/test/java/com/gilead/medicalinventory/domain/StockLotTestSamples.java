package com.gilead.medicalinventory.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class StockLotTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static StockLot getStockLotSample1() {
        return new StockLot().id(1L).batchNumber("batchNumber1").quantityOnHand(1);
    }

    public static StockLot getStockLotSample2() {
        return new StockLot().id(2L).batchNumber("batchNumber2").quantityOnHand(2);
    }

    public static StockLot getStockLotRandomSampleGenerator() {
        return new StockLot()
            .id(longCount.incrementAndGet())
            .batchNumber(UUID.randomUUID().toString())
            .quantityOnHand(intCount.incrementAndGet());
    }
}
