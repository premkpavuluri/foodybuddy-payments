package com.foodybuddy.payments.service;

import com.foodybuddy.payments.config.PaymentConfig;
import com.foodybuddy.payments.dto.PaymentResponse;
import com.foodybuddy.payments.dto.ProcessPaymentRequest;
import com.foodybuddy.payments.entity.Payment;
import com.foodybuddy.payments.entity.PaymentMethod;
import com.foodybuddy.payments.entity.PaymentStatus;
import com.foodybuddy.payments.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository paymentRepository;
    private final PaymentConfig paymentConfig;

    public PaymentService(PaymentRepository paymentRepository, PaymentConfig paymentConfig) {
        this.paymentRepository = paymentRepository;
        this.paymentConfig = paymentConfig;
        logger.info("PaymentService initialized with payment config - Simulation enabled: {}, Success rate: {}", 
            paymentConfig.getSimulation().isEnabled(), paymentConfig.getProcessing().getSuccessRate());
    }
    
    public PaymentResponse processPayment(ProcessPaymentRequest request) {
        String paymentId = UUID.randomUUID().toString();
        String transactionId = "TXN_" + System.currentTimeMillis();
        
        logger.info("Processing payment - PaymentId: {}, OrderId: {}, Amount: {}, Method: {}", 
            paymentId, request.getOrderId(), request.getAmount(), request.getMethod());
        
        // Create payment
        Payment payment = new Payment(
                paymentId,
                request.getOrderId(),
                request.getAmount(),
                PaymentStatus.PROCESSING,
                request.getMethod()
        );
        
        payment.setTransactionId(transactionId);
        logger.debug("Payment created with transaction ID: {}", transactionId);
        
        // Simulate payment processing
        try {
            if (paymentConfig.getSimulation().isEnabled()) {
                logger.debug("Simulating payment processing with delay: {}ms", 
                    paymentConfig.getSimulation().getProcessingDelay());
                
                // In a real application, this would call a payment gateway
                Thread.sleep(paymentConfig.getSimulation().getProcessingDelay());
                
                // Simulate success/failure based on configured success rate
                double randomValue = Math.random();
                double successRate = paymentConfig.getProcessing().getSuccessRate();
                
                if (randomValue > (1 - successRate)) {
                    payment.setStatus(PaymentStatus.COMPLETED);
                    logger.info("Payment simulation successful - PaymentId: {}, Random: {}, SuccessRate: {}", 
                        paymentId, randomValue, successRate);
                } else {
                    payment.setStatus(PaymentStatus.FAILED);
                    logger.warn("Payment simulation failed - PaymentId: {}, Random: {}, SuccessRate: {}", 
                        paymentId, randomValue, successRate);
                }
            } else {
                // Real payment processing would go here
                logger.debug("Real payment processing enabled");
                payment.setStatus(PaymentStatus.COMPLETED);
            }
        } catch (InterruptedException e) {
            logger.error("Payment processing interrupted - PaymentId: {}", paymentId, e);
            payment.setStatus(PaymentStatus.FAILED);
        }
        
        // Save payment
        Payment savedPayment = paymentRepository.save(payment);
        logger.info("Payment saved successfully - PaymentId: {}, Status: {}, TransactionId: {}", 
            paymentId, savedPayment.getStatus(), transactionId);
        
        return convertToResponse(savedPayment);
    }
    
    public PaymentResponse getPayment(String paymentId) {
        logger.debug("Retrieving payment - PaymentId: {}", paymentId);
        
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> {
                    logger.error("Payment not found: {}", paymentId);
                    return new RuntimeException("Payment not found: " + paymentId);
                });
        
        logger.debug("Payment retrieved successfully - PaymentId: {}, Status: {}", 
            paymentId, payment.getStatus());
        return convertToResponse(payment);
    }
    
    public List<PaymentResponse> getPaymentsByOrderId(String orderId) {
        logger.debug("Retrieving payments for orderId: {}", orderId);
        
        List<Payment> payments = paymentRepository.findByOrderId(orderId);
        logger.debug("Found {} payments for orderId: {}", payments.size(), orderId);
        
        return payments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public List<PaymentResponse> getAllPayments() {
        logger.debug("Retrieving all payments");
        
        List<Payment> payments = paymentRepository.findAll();
        logger.debug("Found {} payments in database", payments.size());
        
        return payments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }
    
    public PaymentResponse refundPayment(String paymentId) {
        logger.info("Processing refund for paymentId: {}", paymentId);
        
        Payment payment = paymentRepository.findByPaymentId(paymentId)
                .orElseThrow(() -> {
                    logger.error("Payment not found for refund: {}", paymentId);
                    return new RuntimeException("Payment not found: " + paymentId);
                });
        
        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            logger.error("Cannot refund payment - Status: {}, PaymentId: {}", payment.getStatus(), paymentId);
            throw new RuntimeException("Only completed payments can be refunded");
        }
        
        logger.debug("Refunding payment - PaymentId: {}, Amount: {}", paymentId, payment.getAmount());
        payment.setStatus(PaymentStatus.REFUNDED);
        Payment updatedPayment = paymentRepository.save(payment);
        
        logger.info("Refund processed successfully - PaymentId: {}, Status: {}", 
            paymentId, updatedPayment.getStatus());
        
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
