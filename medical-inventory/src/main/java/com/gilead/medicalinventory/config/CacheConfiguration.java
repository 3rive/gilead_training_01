package com.gilead.medicalinventory.config;

import com.github.benmanes.caffeine.jcache.configuration.CaffeineConfiguration;
import java.util.OptionalLong;
import java.util.concurrent.TimeUnit;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.boot.cache.autoconfigure.JCacheManagerCustomizer;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.config.JHipsterProperties;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Caffeine caffeine = jHipsterProperties.getCache().getCaffeine();

        CaffeineConfiguration<Object, Object> caffeineConfiguration = new CaffeineConfiguration<>();
        caffeineConfiguration.setMaximumSize(OptionalLong.of(caffeine.getMaxEntries()));
        caffeineConfiguration.setExpireAfterWrite(OptionalLong.of(TimeUnit.SECONDS.toNanos(caffeine.getTimeToLiveSeconds())));
        caffeineConfiguration.setStatisticsEnabled(true);
        jcacheConfiguration = caffeineConfiguration;
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.gilead.medicalinventory.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.gilead.medicalinventory.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.gilead.medicalinventory.domain.User.class.getName());
            createCache(cm, com.gilead.medicalinventory.domain.Authority.class.getName());
            createCache(cm, com.gilead.medicalinventory.domain.User.class.getName() + ".authorities");
            createCache(cm, com.gilead.medicalinventory.domain.Category.class.getName());
            createCache(cm, com.gilead.medicalinventory.domain.Supplier.class.getName());
            createCache(cm, com.gilead.medicalinventory.domain.StorageLocation.class.getName());
            createCache(cm, com.gilead.medicalinventory.domain.Medicine.class.getName());
            createCache(cm, com.gilead.medicalinventory.domain.StockLot.class.getName());
            createCache(cm, com.gilead.medicalinventory.domain.StockMovement.class.getName());
            // jhipster-needle-caffeine-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }
}
