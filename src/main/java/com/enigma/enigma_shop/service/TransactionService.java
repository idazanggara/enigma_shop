package com.enigma.enigma_shop.service;

import com.enigma.enigma_shop.dto.request.TransactionRequest;
import com.enigma.enigma_shop.dto.request.UpdateTransactionStatusRequest;
import com.enigma.enigma_shop.dto.response.TransactionResponse;
import com.enigma.enigma_shop.entity.Transaction;

import java.util.List;

public interface TransactionService {
	TransactionResponse create(TransactionRequest request);

	List<Transaction> getAll();

	void updateStatus(UpdateTransactionStatusRequest request);
}
