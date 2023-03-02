package com.Metersdata.springboot.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Arrays;

@Configuration
@EnableCaching
public class CacheConfiguration extends CachingConfigurerSupport {

    @Bean
    @Primary
    public CacheManager cacheSubscriptionManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("SubscriptionExternalKeyCache")));
        return cacheManager;
    }
    @Bean
    public CacheManager cacheSmartMeterDCUManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        System.out.println("cache smartMeterDCU");
        cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("SmartMeterDCUCache")));
        return cacheManager;
    }

}
