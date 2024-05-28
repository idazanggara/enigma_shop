package com.enigma.enigma_shop.security;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // untuk memanggil classnya dan dijadikan bean
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
	private final AuthenticationFilter authenticationFilter;

	@Bean // kita jadikan bean agar method ini di panggil spring
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity
//						.httpBasic(new Customizer<HttpBasicConfigurer<HttpSecurity>>() {
//							@Override
//							public void customize(HttpBasicConfigurer<HttpSecurity> httpSecurityHttpBasicConfigurer) {
//
//							}
//						}) ini awalnya menggunakan lamda

//							.httpBasic(security -> security.disable()) ini kalau kepanjangan bisa kita ganti ya
						.httpBasic(AbstractHttpConfigurer::disable) // ini method repfrence untuk mempersingkan memanggil method disable. di paramter lamda, selain lamda enggak bisa
						.csrf(AbstractHttpConfigurer::disable) // untuk disable .csrf
						.sessionManagement(cfg -> cfg.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
						.authorizeHttpRequests(req -> req
										.dispatcherTypeMatchers(DispatcherType.ERROR).permitAll() // kalau error kita permit juga
										.requestMatchers("/api/v1/auth/**").permitAll()  // jadi semua yg akse /api/auth kita permit dulu atau disable authnya. dan selain itu baru kita authenticated
										.requestMatchers(HttpMethod.POST, "/api/v1/transactions/status").permitAll()
										.requestMatchers("/api/v1/test-download/**").permitAll()
										.requestMatchers(HttpMethod.GET,"/api/v1/products/**").permitAll()
										.anyRequest().authenticated()) // regist dan login enggak butuh auth
//						.addFilter() cuman filternya belum ada, jadi buat dulu
						.addFilterBefore(
										authenticationFilter, // ini filter kita
										UsernamePasswordAuthenticationFilter.class // ini berdasarkan apa
						)
						.build();
	}
}
