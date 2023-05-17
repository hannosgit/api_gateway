package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.example.common.Delivery;
import org.example.common.ServiceAddressConfigProperty;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Future;

@Service
public class DeliveryService {

    private final JsonMapper jsonMapper;
    private final HttpClient httpClient;
    private final URI uri;


    public DeliveryService(JsonMapper jsonMapper, ServiceAddressConfigProperty serviceAddressConfigProperty) {
        this.jsonMapper = jsonMapper;
        this.httpClient = HttpClient.newHttpClient(); // important for performance: 1 client per service
        this.uri = java.net.URI.create("http://" + serviceAddressConfigProperty.delivery() + "/delivery/");
    }


    public Future<Delivery> fetchDeliveryForOrderId(long orderId, String token) {
        final URI uri = this.uri.resolve(String.valueOf(orderId));
        final HttpRequest httpRequest = HttpRequest.newBuilder(uri).header("Authorization", "Authorization: Bearer " + token).GET().build();

        return this.httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::readDeliveryJson);
    }

    private Delivery readDeliveryJson(String stringHttpResponse) {
        try {
            return jsonMapper.readValue(stringHttpResponse, Delivery.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
