package org.example;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class DeliveryService {

    private static final String DELIVERY_SERVICE_URL = "http://localhost:3000/delivery/";
    private final Gson gson;
    private final HttpClient httpClient;


    public DeliveryService(Gson gson) {
        this.gson = gson;
        this.httpClient = HttpClient
                .newBuilder()
                .build();
    }


    public Delivery fetchDeliveryForOrderId(long orderId) {
        try {
            final URI uri = URI.create(DELIVERY_SERVICE_URL).resolve(String.valueOf(orderId));
            final HttpRequest httpRequest = HttpRequest.newBuilder(uri).GET().build();
            final HttpResponse<String> send = this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            return gson.fromJson(send.body(), Delivery.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
