package org.example;

import jdk.incubator.concurrent.StructuredTaskScope;
import org.example.common.ApiCredentials;
import org.example.common.BillInfo;
import org.example.common.Delivery;
import org.example.common.OrderDetails;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
        final String token = this.authService.fetchToken(apiCredentials);

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            final Future<Delivery> deliveryFuture = scope.fork(() -> this.deliveryService.fetchDeliveryForOrderId(orderId, token));
            final Future<BillInfo> billInfoFuture = scope.fork(() -> this.accountingService.fetchBillInfoForOrder(orderId, token));

            scope.join();
            scope.throwIfFailed();

            return new OrderDetails(orderId, deliveryFuture.resultNow(), billInfoFuture.resultNow());
        } catch (InterruptedException | ExecutionException e) {
            throw new FetchException(e);
        }
    }


}
