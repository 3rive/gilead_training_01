package com.gilead.medicalinventory;

import com.gilead.medicalinventory.config.AsyncSyncConfiguration;
import com.gilead.medicalinventory.config.EmbeddedSQL;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        MedicalinventoryApp.class,
        AsyncSyncConfiguration.class,
        com.gilead.medicalinventory.config.JacksonHibernateConfiguration.class,
    }
)
@EmbeddedSQL
public @interface IntegrationTest {}
