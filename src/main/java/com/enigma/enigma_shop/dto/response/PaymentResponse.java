package com.enigma.enigma_shop.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {
	private String id;
	private String token;
	private String redirectUrl;
	private String transactionStatus;
}
