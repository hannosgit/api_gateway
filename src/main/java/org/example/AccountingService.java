package org.example;

import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class AccountingService {

    private static final String ACCOUNT_SERVICE_URL = "http://localhost:3000/bill/";
    private final Gson gson;
    private final HttpClient httpClient;

    public AccountingService(Gson gson) {
        this.gson = gson;
        this.httpClient = HttpClient
                .newBuilder()
                .build();
    }

    public BillInfo fetchBillInfoForOrder(long orderId) {
        try {
            final URI uri = URI.create(ACCOUNT_SERVICE_URL).resolve(String.valueOf(orderId));
            final HttpRequest httpRequest = HttpRequest.newBuilder(uri).GET().build();
            final HttpResponse<String> send = this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            return gson.fromJson(send.body(), BillInfo.class);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
