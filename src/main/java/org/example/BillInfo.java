package org.example;

public record BillInfo(long billId, int totalPrice, long accountId, String firstName, String lastName, String address) {
}
