package org.example;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class AccountingService {

    private static final String ACCOUNT_SERVICE_URL = "http://localhost:3000/bill/";

    private final JsonMapper jsonMapper;
    private final HttpClient httpClient;

    private final RetryTemplate retryTemplate;

    public AccountingService(JsonMapper jsonMapper, RetryTemplate retryTemplate) {
        this.jsonMapper = jsonMapper;
        this.retryTemplate = retryTemplate;
        this.httpClient = HttpClient
                .newBuilder()
                .build();
    }

    public BillInfo fetchBillInfoForOrder(long orderId, String token) {
        return retryTemplate.execute((context) -> {
            try {
                final URI uri = URI.create(ACCOUNT_SERVICE_URL).resolve(String.valueOf(orderId));
                final HttpRequest httpRequest = HttpRequest.newBuilder(uri).header("Authorization", "Authorization: Bearer " + token).GET().build();
                final HttpResponse<String> send = this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

                return jsonMapper.readValue(send.body(), BillInfo.class);
            } catch (IOException | InterruptedException e) {
                throw new FetchException(e);
            }
        });
    }
}
