package com.foodybuddy.payments.repository;

import com.foodybuddy.payments.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentId(String paymentId);
    List<Payment> findByOrderId(String orderId);
    List<Payment> findByStatus(com.foodybuddy.payments.entity.PaymentStatus status);
}
