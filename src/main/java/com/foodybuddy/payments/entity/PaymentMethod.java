package com.foodybuddy.payments.entity;

/**
 * Payment method enumeration for the Payments service
 * Represents the different payment methods supported by the system
 */
public enum PaymentMethod {
    CREDIT_CARD("Credit Card payment"),
    DEBIT_CARD("Debit Card payment"),
    PAYPAL("PayPal payment"),
    CASH("Cash payment"),
    BANK_TRANSFER("Bank Transfer payment");

    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Check if the payment method requires card details
     */
    public boolean requiresCardDetails() {
        return this == CREDIT_CARD || this == DEBIT_CARD;
    }

    /**
     * Check if the payment method is digital
     */
    public boolean isDigital() {
        return this != CASH;
    }
}
