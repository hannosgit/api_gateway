package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@Profile("blocking")
public class BlockingConfig {

    @Bean
    public ExecutorService executorService(){
        return Executors.newCachedThreadPool();
    }

}
