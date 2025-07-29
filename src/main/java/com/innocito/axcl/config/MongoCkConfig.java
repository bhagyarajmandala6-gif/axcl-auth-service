package com.innocito.axcl.config;

import io.mongock.runner.springboot.EnableMongock;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableMongock
public class MongoCkConfig {

    /*@Autowired
    private MongoTemplate mongoTemplate;

    @Bean
    public ConnectionDriver connectionDriver() {
        return SpringDataMongoV4Driver.withDefaultLock(mongoTemplate);
    }*/
}