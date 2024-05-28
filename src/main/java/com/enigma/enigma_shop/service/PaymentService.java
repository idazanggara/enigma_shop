package com.enigma.enigma_shop.service;

import com.enigma.enigma_shop.entity.Payment;
import com.enigma.enigma_shop.entity.Transaction;

import java.util.Map;

public interface PaymentService {

	Payment createPayment(Transaction transaction);

	Map<String, Object> createPayment(Map<String, Object> mapRequest);
}
