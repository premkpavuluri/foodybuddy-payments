package com.foodybuddy.payments.service;

import com.foodybuddy.payments.config.PaymentConfig;
import com.foodybuddy.payments.dto.PaymentResponse;
import com.foodybuddy.payments.dto.ProcessPaymentRequest;
import com.foodybuddy.payments.entity.Payment;
import com.foodybuddy.payments.entity.PaymentMethod;
import com.foodybuddy.payments.entity.PaymentStatus;
import com.foodybuddy.payments.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Payment Service
 * 
 * This service handles all payment-related operations in the FoodyBuddy application.
 * It processes payments, manages payment status, and handles refunds.
 * 
 * Key responsibilities:
 * - Process payments for orders
 * - Simulate payment gateway interactions
 * - Track payment status and transaction details
 * - Handle payment refunds
 * - Provide payment history and details
 */
@Service
@Transactional
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final PaymentConfig paymentConfig;

    public PaymentService(PaymentRepository paymentRepository, PaymentConfig paymentConfig) {
        this.paymentRepository = paymentRepository;
        this.paymentConfig = paymentConfig;
    }
    
    public PaymentResponse processPayment(ProcessPaymentRequest request) {
        String paymentId = UUID.randomUUID().toString();
        String transactionId = "TXN_" + System.currentTimeMillis();
        
        // Create payment
        Payment payment = new Payment(
                paymentId,
                request.getOrderId(),
                request.getAmount(),
                PaymentStatus.PROCESSING,
                request.getMethod()
        );
        
        payment.setTransactionId(transactionId);
        
        // Simulate payment processing
        try {
            if (paymentConfig.getSimulation().isEnabled()) {
                // In a real application, this would call a payment gateway
                Thread.sleep(paymentConfig.getSimulation().getProcessingDelay());
                
                // Simulate success/failure based on configured success rate
                if (Math.random() > (1 - paymentConfig.getProcessing().getSuccessRate())) {
                    payment.setStatus(PaymentStatus.COMPLETED);
                } else {
                    payment.setStatus(PaymentStatus.FAILED);
                }
            } else {
                // Real payment processing would go here
                payment.setStatus(PaymentStatus.COMPLETED);
            }
        } catch (InterruptedException e) {
            payment.setStatus(PaymentStatus.FAILED);
        }
        
        // Save payment
        Payment savedPayment = paymentRepository.save(payment);
        
        return convertToResponse(savedPayment);
    }
    
    public PaymentResponse getPayment(String paymentId) {
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));
        
        return convertToResponse(payment);
    }
    
    public List<PaymentResponse> getPaymentsByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public PaymentResponse refundPayment(String paymentId) {
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + paymentId));
        
        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new RuntimeException("Only completed payments can be refunded");
        }
        
        payment.setStatus(PaymentStatus.REFUNDED);
        Payment updatedPayment = paymentRepository.save(payment);
        
        return convertToResponse(updatedPayment);
    }
    
    private PaymentResponse convertToResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getPaymentId(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getMethod(),
                payment.getTransactionId(),
                payment.getCreatedAt(),
                payment.getUpdatedAt()
        );
    }
}
