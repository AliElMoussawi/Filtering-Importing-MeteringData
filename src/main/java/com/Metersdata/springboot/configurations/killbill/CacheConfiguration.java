package com.Metersdata.springboot.configurations.killbill;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import org.killbill.billing.client.model.gen.Subscription;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;


import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfiguration {

    @Bean
    public Cache<String, Subscription> subscriptionCache() {
        return Caffeine.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).build();
    }
}
