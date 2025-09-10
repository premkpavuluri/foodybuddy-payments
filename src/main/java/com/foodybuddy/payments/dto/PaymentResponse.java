package com.foodybuddy.payments.dto;

import com.foodybuddy.payments.entity.PaymentMethod;
import com.foodybuddy.payments.entity.PaymentStatus;
import java.time.LocalDateTime;

public class PaymentResponse {
    private Long id;
    private String paymentId;
    private String orderId;
    private Double amount;
    private PaymentStatus status;
    private PaymentMethod method;
    private String transactionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public PaymentResponse() {}
    
    public PaymentResponse(Long id, String paymentId, String orderId, Double amount, 
                          PaymentStatus status, PaymentMethod method, String transactionId,
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.method = method;
        this.transactionId = transactionId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getPaymentId() {
        return paymentId;
    }
    
    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
    
    public String getOrderId() {
        return orderId;
    }
    
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
    
    public Double getAmount() {
        return amount;
    }
    
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    
    public PaymentStatus getStatus() {
        return status;
    }
    
    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
    
    public PaymentMethod getMethod() {
        return method;
    }
    
    public void setMethod(PaymentMethod method) {
        this.method = method;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
