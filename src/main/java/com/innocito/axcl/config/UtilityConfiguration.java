package com.innocito.axcl.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.MDC;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Collections;

import static com.innocito.axcl.util.ApplicationConstants.CORRELATION_ID_HEADER;
import static com.innocito.axcl.util.ApplicationConstants.MDC_KEY;

@Configuration
public class UtilityConfiguration {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    @Bean
    public ClientHttpRequestInterceptor correlationIdInterceptor() {
        return (request, body, execution) -> {
            String correlationId = MDC.get(MDC_KEY);
            if (correlationId != null) {
                request.getHeaders().add(CORRELATION_ID_HEADER, correlationId);
            }
            return execution.execute(request, body);
        };
    }

    @Bean
    public RestTemplate getRestTemplate(ClientHttpRequestInterceptor correlationIdInterceptor) {
        return new RestTemplateBuilder()
                .connectTimeout(Duration.ofMillis(5000))
                .readTimeout(Duration.ofMillis(5000))
                .additionalInterceptors(Collections.singletonList(correlationIdInterceptor))
                .build();
    }

    @Bean
    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }
}