package com.enigma.enigma_shop.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthRequest {
	private String username;
	private String password;
}

// role enggak kita berikan ya, karena nanti role bisa mereka otak atik ya