package org.example.config;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.util.concurrent.Executors;

@Configuration
public class JsonConfig {

    @Bean
    public JsonMapper jsonMapper() {
        return JsonMapper.builder().build();
    }

    @Bean
    public HttpClient httpClient(){
        return HttpClient
                .newBuilder()
                .executor(Executors.newVirtualThreadPerTaskExecutor())
                .build();
    }
}
