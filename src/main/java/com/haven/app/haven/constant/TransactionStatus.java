package com.haven.app.haven.constant;

public enum TransactionStatus {
    PENDING("PENDING"),
    BOOKED("BOOKED"),
    CANCELLED("CANCELLED"),
    START("START"),
    DONE("DONE");

    private String value;

    TransactionStatus(String value) {
        this.value = value;
    }

    public static TransactionStatus fromValue(String value) {
        for (TransactionStatus status : TransactionStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return null;
    }
}
