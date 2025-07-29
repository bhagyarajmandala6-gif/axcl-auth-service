/*
package com.innocito.axcl.config;

import com.google.common.cache.CacheBuilder;
import com.innocito.axcl.util.MessageConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;


@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${cache.expiry.in.minutes}")
    private int expiry;

    @Value("${cache.max.size}")
    private int maxSize;

    @Bean
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager() {
            @Override
            protected Cache createConcurrentMapCache(final String name) {
                return new ConcurrentMapCache(name, CacheBuilder.newBuilder().expireAfterWrite(expiry, TimeUnit.MINUTES)
                        .maximumSize(maxSize).build().asMap(), false);
            }
        };
        cacheManager.setCacheNames(Arrays.asList(MessageConstants.LIFE_STYLE_SURVEY, MessageConstants.LIFE_STYLE_SURVEY_DATA, MessageConstants.STYLE_DATA,
                MessageConstants.FASHION_ACCESSORIES, MessageConstants.CLOTH_ITEM, MessageConstants.ESSENTIAL_IMAGES));
        return cacheManager;
    }
}
*/
