package com.enigma.enigma_shop.service.impl;

import com.enigma.enigma_shop.constant.TransactionStatus;
import com.enigma.enigma_shop.dto.request.PaymentCustomerRequest;
import com.enigma.enigma_shop.dto.request.PaymentDetailRequest;
import com.enigma.enigma_shop.dto.request.PaymentItemDetailRequest;
import com.enigma.enigma_shop.dto.request.PaymentRequest;
import com.enigma.enigma_shop.dto.response.PaymentResponse;
import com.enigma.enigma_shop.entity.Payment;
import com.enigma.enigma_shop.entity.Transaction;
import com.enigma.enigma_shop.repository.PaymentRepository;
import com.enigma.enigma_shop.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {
	private final PaymentRepository paymentRepository;
	private final RestClient restClient;
	private final String SECRET_KEY;
	private final String BASE_URL_SNAP;

	@Autowired
	public PaymentServiceImpl(
					PaymentRepository paymentRepository,
					RestClient restClient,
					@Value("${midtrans.api.key}") String SECRET_KEY,
					@Value("${midtrans.api.snap-url}") String BASE_URL_SNAP) {
		this.paymentRepository = paymentRepository;
		this.restClient = restClient;
		this.SECRET_KEY = SECRET_KEY;
		this.BASE_URL_SNAP = BASE_URL_SNAP;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Payment createPayment(Transaction transaction) {

		long amount = transaction.getTransactionDetails()
						.stream()
						// untuk mengkalikan quantiti dan product price per item detail transacktion
						.mapToLong(value -> (value.getQty() * value.getProductPrice()))
						// jadi nanti semisal ada 2 data, nasi dang kangkung, itu di kali dulu untuk price dan quantitynya, nah reduce ini yg nanti akan menambangkan nasi dan kangkung yg di kalikan tadi, trus identitynya itu seperti buat count. nanti totalnya jadi berapa
						//.reduce(0, ((left, right) -> left + right));
						.reduce(0, Long::sum);

		List<PaymentItemDetailRequest> itemDetailRequestList = transaction.getTransactionDetails()
						.stream()
						.map(transactionDetail ->
										// transactionDetail ini gw ubah menjadi PaymentItemDetailRequest
										PaymentItemDetailRequest.builder()
										.name(transactionDetail.getProduct().getName())
										.price(transactionDetail.getProductPrice())
										.quantity(transactionDetail.getQty())
										.build()).toList();

		PaymentRequest request = PaymentRequest.builder()
						.paymentDetail(PaymentDetailRequest.builder()
										.orderId(transaction.getId())
										.amount(amount)
										.build())
						.paymentItemDetails(itemDetailRequestList)
						.paymentMethod(List.of("shopeepay", "gopay"))
						.customer(PaymentCustomerRequest.builder()
										// kita kasih ternari, kalau cutomernya enggak ada, kita kasih Guest
										.name(transaction.getCustomer() != null ?
														transaction.getCustomer().getName() : "Guest")
										.build())
						.build();

		//
		ResponseEntity<Map<String, String>> response = restClient.post()
						.uri(BASE_URL_SNAP)
						// tapi kan kita perlu kirim body pada saat kita panggil, cuman gimana kirim bodynya?
						// bisa gunakan .body ya
						.body(request)
						.header(HttpHeaders.AUTHORIZATION, "Basic " + SECRET_KEY)
						.retrieve() // ambil datanya
						.toEntity(new ParameterizedTypeReference<>() {}); // ini kayak object mapper, bisa dinamis

		// untuk mengambil response dari rest client atau midtransnya
		Map<String, String> body = response.getBody();

		if (body == null){
			return null;
		}


		Payment payment = Payment.builder()
						.token(body.get("token"))
						.redirectUrl(body.get("redirect_url"))
						// ini kita set ordered aja dulu,
						// nanti setelah itu dia bisa diubah ketika berhasil
						// jadi nanti si midtrans akan hit api kita, dan kasih tauin status sekarang
						.transactionStatus("ordered")
						.build();
		paymentRepository.saveAndFlush(payment);


		return payment;
	}

	@Override
	public Map<String, Object> createPayment(Map<String, Object> mapRequest) {
		mapRequest.get("transaction_details");
		return Map.of();
	}
}
