package org.example.config;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.example.FetchException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.retry.support.RetryTemplateBuilder;

@Configuration
public class JsonConfig {

    @Bean
    public JsonMapper jsonMapper() {
        return JsonMapper.builder().build();
    }

    @Bean
    public RetryTemplate retryTemplate(){
        return new RetryTemplateBuilder()
                .retryOn(FetchException.class)
                .fixedBackoff(10)
                .maxAttempts(2)
                .build();
    }

}