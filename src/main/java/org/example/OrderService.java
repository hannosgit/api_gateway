package org.example;

import org.example.common.ApiCredentials;
import org.example.common.BillInfo;
import org.example.common.Delivery;
import org.example.common.OrderDetails;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class OrderService {

    private final AccountingService accountingService;

    private final DeliveryService deliveryService;

    private final AuthService authService;

    public OrderService(AccountingService accountingService, DeliveryService deliveryService, AuthService authService) {
        this.accountingService = accountingService;
        this.deliveryService = deliveryService;
        this.authService = authService;
    }

    public OrderDetails fetchOrderDetails(long orderId, ApiCredentials apiCredentials) {
        try {
            final String token = this.authService.fetchToken(apiCredentials);

            final CompletableFuture<Delivery> deliveryFuture = this.deliveryService.fetchDeliveryForOrderId(orderId, token);
            final CompletableFuture<BillInfo> billInfoFuture = this.accountingService.fetchBillInfoForOrder(orderId, token);

            return new OrderDetails(orderId, deliveryFuture.get(), billInfoFuture.get());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
