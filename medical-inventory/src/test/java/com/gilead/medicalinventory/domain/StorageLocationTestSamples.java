package com.gilead.medicalinventory.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class StorageLocationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static StorageLocation getStorageLocationSample1() {
        return new StorageLocation().id(1L).code("code1").name("name1").building("building1");
    }

    public static StorageLocation getStorageLocationSample2() {
        return new StorageLocation().id(2L).code("code2").name("name2").building("building2");
    }

    public static StorageLocation getStorageLocationRandomSampleGenerator() {
        return new StorageLocation()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .building(UUID.randomUUID().toString());
    }
}
