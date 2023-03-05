package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class AuthService {

    private static final String AUTH_SERVICE_URL = "http://localhost:3000/auth/authenticate";
    private final RetryTemplate retryTemplate;
    private final JsonMapper jsonMapper;
    private final HttpClient httpClient;

    public AuthService(RetryTemplate retryTemplate, JsonMapper jsonMapper) {
        this.retryTemplate = retryTemplate;
        this.jsonMapper = jsonMapper;
        this.httpClient = HttpClient
                .newBuilder()
                .build();
    }

    public String fetchToken(String username, String password) {
        final var authRequest = buildRequest(username, password);
        final URI uri = URI.create(AUTH_SERVICE_URL);
        final HttpRequest httpRequest = HttpRequest.newBuilder(uri).POST(HttpRequest.BodyPublishers.ofString(authRequest)).build();

        return retryTemplate.execute((c) -> {
            try {
                final HttpResponse<String> send = this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

                return jsonMapper.readValue(send.body(), AuthenticateResponse.class).token;
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String buildRequest(String username, String password) {
        try {
            return jsonMapper.writeValueAsString(new AuthenticateRequest(username, password));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private record AuthenticateRequest(String username, String password) {

    }

    private record AuthenticateResponse(String token) {

    }

}
