package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.example.common.ApiCredentials;
import org.example.common.ServiceAddressConfigProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class AuthService {
    private final JsonMapper jsonMapper;
    private final HttpClient httpClient;
    private final URI uri;

    public AuthService(JsonMapper jsonMapper, ServiceAddressConfigProperty serviceAddressConfigProperty) {
        this.jsonMapper = jsonMapper;
        this.httpClient = HttpClient.newHttpClient(); // important for performance: 1 client per service
        this.uri = java.net.URI.create("http://" + serviceAddressConfigProperty.auth() + "/auth/authenticate");
    }

    public String fetchToken(ApiCredentials apiCredentials) {
        return fetchToken(apiCredentials.username(), apiCredentials.password());
    }

    public String fetchToken(String username, String password) {
        final var authRequest = buildRequest(username, password);
        final HttpRequest httpRequest = HttpRequest.newBuilder(this.uri).POST(HttpRequest.BodyPublishers.ofString(authRequest)).build();

        try {
            final HttpResponse<String> send = this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            return jsonMapper.readValue(send.body(), AuthenticateResponse.class).token;
        } catch (IOException | InterruptedException e) {
            throw new FetchException(e);
        }
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
