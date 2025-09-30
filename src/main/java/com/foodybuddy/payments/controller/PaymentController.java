package com.foodybuddy.payments.controller;

import com.foodybuddy.payments.dto.PaymentResponse;
import com.foodybuddy.payments.dto.ProcessPaymentRequest;
import com.foodybuddy.payments.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
        logger.info("PaymentController initialized with payment service");
    }
    
    @PostMapping("/process")
    public ResponseEntity<PaymentResponse> processPayment(@RequestBody ProcessPaymentRequest request) {
        logger.info("Processing payment - OrderId: {}, Amount: {}, Method: {}", 
            request.getOrderId(), request.getAmount(), request.getMethod());
        
        try {
            PaymentResponse payment = paymentService.processPayment(request);
            logger.info("Payment processed successfully - PaymentId: {}, Status: {}, TransactionId: {}", 
                payment.getPaymentId(), payment.getStatus(), payment.getTransactionId());
            return ResponseEntity.status(HttpStatus.CREATED).body(payment);
        } catch (Exception e) {
            logger.error("Failed to process payment for orderId: {}", request.getOrderId(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable String paymentId) {
        logger.info("Fetching payment details for paymentId: {}", paymentId);
        
        try {
            PaymentResponse payment = paymentService.getPayment(paymentId);
            logger.info("Payment retrieved successfully - PaymentId: {}, Status: {}", 
                payment.getPaymentId(), payment.getStatus());
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            logger.error("Payment not found: {}", paymentId, e);
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByOrderId(@PathVariable String orderId) {
        logger.info("Fetching payments for orderId: {}", orderId);
        
        List<PaymentResponse> payments = paymentService.getPaymentsByOrderId(orderId);
        logger.info("Retrieved {} payments for orderId: {}", payments.size(), orderId);
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        logger.info("Fetching all payments");
        
        List<PaymentResponse> payments = paymentService.getAllPayments();
        logger.info("Retrieved {} payments successfully", payments.size());
        return ResponseEntity.ok(payments);
    }
    
    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<PaymentResponse> refundPayment(@PathVariable String paymentId) {
        logger.info("Processing refund for paymentId: {}", paymentId);
        
        try {
            PaymentResponse payment = paymentService.refundPayment(paymentId);
            logger.info("Refund processed successfully - PaymentId: {}, Status: {}", 
                payment.getPaymentId(), payment.getStatus());
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            logger.error("Failed to process refund for paymentId: {}", paymentId, e);
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        logger.debug("Health check endpoint called");
        return ResponseEntity.ok("Payments service is healthy");
    }
}
