package org.example.common;

/**
 * Order details
 *
 * @param orderId  the id of the order
 * @param delivery the delivery of the order
 * @param billInfo the bill of the order
 */
public record OrderDetails(long orderId, Delivery delivery, BillInfo billInfo) {
}
