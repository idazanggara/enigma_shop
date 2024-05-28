package com.enigma.enigma_shop.repository;

import com.enigma.enigma_shop.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, String> {
}
