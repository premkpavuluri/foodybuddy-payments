package com.foodybuddy.payments.controller;

import com.foodybuddy.payments.dto.PaymentResponse;
import com.foodybuddy.payments.dto.ProcessPaymentRequest;
import com.foodybuddy.payments.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {
    
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    
    @PostMapping("/process")
    public ResponseEntity<PaymentResponse> processPayment(@RequestBody ProcessPaymentRequest request) {
        try {
            PaymentResponse payment = paymentService.processPayment(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(payment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable String paymentId) {
        try {
            PaymentResponse payment = paymentService.getPayment(paymentId);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByOrderId(@PathVariable String orderId) {
        List<PaymentResponse> payments = paymentService.getPaymentsByOrderId(orderId);
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        List<PaymentResponse> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }
    
    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<PaymentResponse> refundPayment(@PathVariable String paymentId) {
        try {
            PaymentResponse payment = paymentService.refundPayment(paymentId);
            return ResponseEntity.ok(payment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Payments service is healthy");
    }
}
