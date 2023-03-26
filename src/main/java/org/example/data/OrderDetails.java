package org.example.data;

public record OrderDetails(long orderId, Delivery delivery, BillInfo billInfo) {
}
