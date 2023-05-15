package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.example.common.BillInfo;
import org.example.common.ServiceAddressConfigProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class AccountingService {
    private final JsonMapper jsonMapper;
    private final HttpClient httpClient;
    private final URI uri;

    public AccountingService(JsonMapper jsonMapper, HttpClient httpClient, ServiceAddressConfigProperty serviceAddressConfigProperty) {
        this.jsonMapper = jsonMapper;
        this.httpClient = httpClient;
        this.uri = java.net.URI.create("http://" + serviceAddressConfigProperty.bill() + "/bill/");
    }

    public BillInfo fetchBillInfoForOrder(long orderId, String token) {
        try {
            final URI uri = this.uri.resolve(String.valueOf(orderId));
            final HttpRequest httpRequest = HttpRequest.newBuilder(uri).header("Authorization", "Authorization: Bearer " + token).GET().build();
            final HttpResponse<String> send = this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            return readBillInfoJson(send);
        } catch (IOException | InterruptedException e) {
            throw new FetchException(e);
        }
    }

    private BillInfo readBillInfoJson(HttpResponse<String> stringHttpResponse) {
        try {
            return jsonMapper.readValue(stringHttpResponse.body(), BillInfo.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
