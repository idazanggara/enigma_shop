package com.enigma.enigma_shop.controller;

import com.enigma.enigma_shop.constant.APIUrl;
import com.enigma.enigma_shop.dto.request.TransactionRequest;
import com.enigma.enigma_shop.dto.request.UpdateTransactionStatusRequest;
import com.enigma.enigma_shop.dto.response.CommonResponse;
import com.enigma.enigma_shop.dto.response.TransactionResponse;
import com.enigma.enigma_shop.entity.Transaction;
import com.enigma.enigma_shop.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.TRANSACTION_API)
public class TransactionController {
	// service, ini kosong dulu

	// setelah buat DTO requestnya baru panggil service dan buat service
	private final TransactionService transactionService;
	@PostMapping
	public ResponseEntity<CommonResponse<TransactionResponse>> createNewTransaction(@RequestBody TransactionRequest request) {
		TransactionResponse transaction = transactionService.create(request);
		CommonResponse<TransactionResponse> response = CommonResponse.<TransactionResponse>builder()
						.statusCode(HttpStatus.CREATED.value())
						.message("successfully save data")
						.data(transaction)
						.build();
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping
	public List<Transaction> getAllTransaction(){
		return transactionService.getAll();
	}

	@PostMapping(path = "/status")
	public ResponseEntity<CommonResponse<?>> updateStatus(
					@RequestBody Map<String, Object> request) {
		UpdateTransactionStatusRequest updateTransactionStatusRequest = UpdateTransactionStatusRequest.builder()
						.orderId(request.get("order_id").toString())
						.transactionStatus(request.get("transaction_status").toString())
						.build();
		transactionService.updateStatus(updateTransactionStatusRequest); // methodnya enggak ada nih diawal
		return ResponseEntity.ok(CommonResponse.builder()
						.statusCode(HttpStatus.OK.value())
						.message("successfully update data")
						.build());
	}

}
