package com.enigma.enigma_shop.service.impl;

import com.enigma.enigma_shop.constant.TransactionStatus;
import com.enigma.enigma_shop.dto.request.TransactionDetailRequest;
import com.enigma.enigma_shop.dto.request.TransactionRequest;
import com.enigma.enigma_shop.dto.request.UpdateTransactionStatusRequest;
import com.enigma.enigma_shop.dto.response.CustomerResponse;
import com.enigma.enigma_shop.dto.response.PaymentResponse;
import com.enigma.enigma_shop.dto.response.TransactionDetailResponse;
import com.enigma.enigma_shop.dto.response.TransactionResponse;
import com.enigma.enigma_shop.entity.*;
import com.enigma.enigma_shop.repository.TransactionDetailRepository;
import com.enigma.enigma_shop.repository.TransactionRepository;
import com.enigma.enigma_shop.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
	// nah kalau begini enggak di sarankan guys, ini namanya
	// Cross Repository
	// jadi kita hanya boleh memanggil 1 repo,
	private final TransactionRepository transactionRepository;

	// jangan ke TransactionDetailRepository, karena cross, jadi ke service yga bener
//	private final TransactionDetailRepository transactionDetailRepository;

	private final TransactionDetailService transactionDetailService;

	private final CustomerService customerService;
	private final ProductService productService;

	// setelah REST CLIENT, UNTUK MIDTRANS
	private final PaymentService paymentService;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public TransactionResponse create(TransactionRequest request) {
		Customer customer = customerService.getById(request.getCustomerId());
		Transaction trx = Transaction.builder()
						.customer(customer)
						.transDate(new Date())
						.build();
		transactionRepository.saveAndFlush(trx);

		List<TransactionDetail> trxDetails = request.getTransactionDetails().stream()
						.map(detailRequest -> {
							Product product = productService.getById(detailRequest.getProductId());
							// buat ngurangi stock
							if(product.getStock() - detailRequest.getQty() < 0) throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "the product currently out of stock");

							product.setStock(product.getStock() - detailRequest.getQty());
							productService.update(product);

							return TransactionDetail.builder()
											.product(product)
											.transaction(trx)
											.qty(detailRequest.getQty())
											.productPrice(product.getPrice())
											.build();
						}).toList();

		transactionDetailService.createBulk(trxDetails);
		trx.setTransactionDetails(trxDetails);

		List<TransactionDetailResponse> trxDetailResponses = trxDetails.stream().map(detail ->
						TransactionDetailResponse.builder()
										.id(detail.getId())
										.productId(detail.getProduct().getId())
										.productPrice(detail.getProductPrice())
										.quantity(detail.getQty())
										.build()).toList();

		Payment payment = paymentService.createPayment(trx);
		trx.setPayment(payment);

		CustomerResponse customerResponse = CustomerResponse.builder()
						.id(trx.getCustomer().getId())
						.name(trx.getCustomer().getName())
						.mobilePhoneNo(trx.getCustomer().getMobilePhoneNo())
						.address(trx.getCustomer().getAddress())
						.status(trx.getCustomer().getStatus())
						.userAccountId(trx.getCustomer().getUserAccount().getId())
						.build();


		PaymentResponse paymentResponse = PaymentResponse.builder()
						.id(payment.getId())
						.token(payment.getToken())
						.redirectUrl(payment.getRedirectUrl())
						.transactionStatus(payment.getTransactionStatus())
						.build();

		return TransactionResponse.builder()
						.id(trx.getId())
						.customer(customerResponse)
						.transDate(trx.getTransDate())
						.transactionDetails(trxDetailResponses)
						.paymentResponse(paymentResponse)
						.build();
	}

	@Override
	public List<Transaction> getAll() {
		return transactionRepository.findAll();
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void updateStatus(UpdateTransactionStatusRequest request) {
		// kita cari transaksinya dari transaksi id yg kritim oleh midtrans
		Transaction transaction = transactionRepository.findById(request.getOrderId())
						.orElseThrow(() ->
										new ResponseStatusException(HttpStatus.NOT_FOUND, "data not found"));
		// paymen langsung kita ubah
		Payment payment = transaction.getPayment();
		payment.setTransactionStatus(request.getTransactionStatus());
		// kita enggak perlu update, karena dengan transactional, dia udah otomatis update, ketika kita set

		// walaupun ini bukan repositorynya ya, kita di transaction tetep bisa ubah payment


	}
}
