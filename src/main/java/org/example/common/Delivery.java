package org.example.common;

/**
 * Delivery details
 *
 * @param deliveryId     the id of the delivery
 * @param restaurantName the name of the restaurant
 * @param deliveryDate   the delivery date
 * @param totalItems     the total amount of items
 */
public record Delivery(long deliveryId, String restaurantName, String deliveryDate, int totalItems) {
}
