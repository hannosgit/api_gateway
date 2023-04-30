package org.example;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.example.data.Delivery;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class DeliveryService {

    private final RetryTemplate retryTemplate;
    private final JsonMapper jsonMapper;
    private final HttpClient httpClient;
    private final URI uri;


    public DeliveryService(RetryTemplate retryTemplate, JsonMapper jsonMapper, ServiceAddressConfigProperty serviceAddressConfigProperty) {
        this.retryTemplate = retryTemplate;
        this.jsonMapper = jsonMapper;
        this.httpClient = HttpClient
                .newBuilder()
                .build();
        this.uri = java.net.URI.create("http://" + serviceAddressConfigProperty.address() + "/delivery/");
    }


    public Delivery fetchDeliveryForOrderId(long orderId, String token) {
        final URI uri = this.uri.resolve(String.valueOf(orderId));
        final HttpRequest httpRequest = HttpRequest.newBuilder(uri).header("Authorization", "Authorization: Bearer " + token).GET().build();

        return retryTemplate.execute((c) -> {
            try {
                final HttpResponse<String> send = this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

                return jsonMapper.readValue(send.body(), Delivery.class);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
