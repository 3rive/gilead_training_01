package com.gilead.medicalinventory.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MedicineTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static Medicine getMedicineSample1() {
        return new Medicine().id(1L).name("name1").sku("sku1").genericName("genericName1").strength("strength1").reorderLevel(1);
    }

    public static Medicine getMedicineSample2() {
        return new Medicine().id(2L).name("name2").sku("sku2").genericName("genericName2").strength("strength2").reorderLevel(2);
    }

    public static Medicine getMedicineRandomSampleGenerator() {
        return new Medicine()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .sku(UUID.randomUUID().toString())
            .genericName(UUID.randomUUID().toString())
            .strength(UUID.randomUUID().toString())
            .reorderLevel(intCount.incrementAndGet());
    }
}
