package com.enigma.enigma_shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestClient;

@Configuration
public class BeanConfiguration {
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	// nah ini cara membuat si PasswordEncoder jadi bean
	// jadi sebenernya menggunakan anotasi @Bean ini, membuat passwordEncoder itu jadi bean kenapa biar kita bisa gunakan library orang yg bukan bean,jadi kita daftarkan sebagai bean


	@Bean
	// jadi si AuthenticationConfiguration, ini adalah sebuah @bean yg akan di inject nanti dan otomatis
	// tapi nanti ada exeption, enggak usah di try-cath tapi di throws aja method authenticationManager
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public RestClient restClient() {
		return RestClient.create();
	}
}
