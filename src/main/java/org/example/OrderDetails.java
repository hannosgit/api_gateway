package org.example;

public record OrderDetails(long orderId, Delivery delivery, BillInfo billInfo) {
}
