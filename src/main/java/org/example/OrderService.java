package org.example;

import jdk.incubator.concurrent.StructuredTaskScope;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class OrderService {

    private final AccountingService accountingService;

    private final DeliveryService deliveryService;

    public OrderService(AccountingService accountingService, DeliveryService deliveryService) {
        this.accountingService = accountingService;
        this.deliveryService = deliveryService;
    }


    public OrderDetails fetchOrderDetails(long orderId) {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            final Future<Delivery> deliveryFuture = scope.fork(() -> this.deliveryService.fetchDeliveryForOrderId(orderId));
            final Future<BillInfo> billInfoFuture = scope.fork(() -> this.accountingService.fetchBillInfoForOrder(orderId));

            scope.join();
            scope.throwIfFailed();

            return new OrderDetails(orderId, deliveryFuture.resultNow(), billInfoFuture.resultNow());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}
