package org.example;

import jdk.incubator.concurrent.StructuredTaskScope;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Service
public class OrderService {

    private final AccountingService accountingService;

    private final DeliveryService deliveryService;

    private final AuthService authService;

    private final ExecutorService executorService;

    public OrderService(AccountingService accountingService, DeliveryService deliveryService, AuthService authService, ExecutorService executorService) {
        this.accountingService = accountingService;
        this.deliveryService = deliveryService;
        this.authService = authService;
        this.executorService = executorService;
    }

    public OrderDetails fetchOrderDetails(long orderId, ApiCredentials apiCredentials) {
        final Future<String> tokenFuture = executorService.submit(() -> this.authService.fetchToken(apiCredentials));

        final String token;
        try {
            token = tokenFuture.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new FetchException(e);
        }

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
