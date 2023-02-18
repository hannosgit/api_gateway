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

    private final Gson gson;
    private final HttpClient httpClient;

    private final HttpRequest httpRequest = HttpRequest.newBuilder(URI.create("http://localhost:3000/")).GET().build();

    public DeliveryService(Gson gson) {
        this.gson = gson;
        this.httpClient = HttpClient
                .newBuilder()
                .build();
    }

    public Delivery fetchDelivery(long id) {
        try {
            final HttpResponse<String> send = this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            return gson.fromJson(send.body(), Delivery.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
