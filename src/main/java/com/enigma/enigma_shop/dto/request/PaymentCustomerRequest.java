package com.enigma.enigma_shop.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentCustomerRequest {
	@JsonProperty("first_name")
	private String name;
}
