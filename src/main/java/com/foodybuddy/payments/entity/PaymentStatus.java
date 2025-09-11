package com.foodybuddy.payments.entity;

/**
 * Payment status enumeration for the Payments service
 * Represents the lifecycle of a payment in the system
 */
public enum PaymentStatus {
    PENDING("Payment has been initiated and is waiting for processing"),
    PROCESSING("Payment is being processed by the payment gateway"),
    COMPLETED("Payment has been successfully completed"),
    FAILED("Payment processing failed"),
    REFUNDED("Payment has been refunded"),
    CANCELLED("Payment has been cancelled");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Check if the payment can transition to the given status
     */
    public boolean canTransitionTo(PaymentStatus newStatus) {
        return switch (this) {
            case PENDING -> newStatus == PROCESSING || newStatus == CANCELLED;
            case PROCESSING -> newStatus == COMPLETED || newStatus == FAILED || newStatus == CANCELLED;
            case COMPLETED -> newStatus == REFUNDED;
            case FAILED, CANCELLED, REFUNDED -> false;
        };
    }
}
