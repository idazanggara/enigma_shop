package com.enigma.enigma_shop.dto.response;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class  TransactionResponse {
	private String id;
	private Date transDate;
	private List<TransactionDetailResponse> transactionDetails;

	private PaymentResponse paymentResponse;
	private CustomerResponse customer;

}
