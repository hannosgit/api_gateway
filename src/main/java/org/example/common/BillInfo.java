package org.example.common;

/**
 * Billing details
 *
 * @param billId     the id of the bill
 * @param totalPrice the total price of the bill
 * @param accountId  the id of the account
 * @param firstName  the firstname of the customer
 * @param lastName   the lastname of the customer
 * @param address    the address of the customer
 */
public record BillInfo(long billId, int totalPrice, long accountId, String firstName, String lastName, String address) {
}
