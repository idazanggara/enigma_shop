package com.enigma.enigma_shop.entity;

import com.enigma.enigma_shop.constant.ConstantTable;
import com.enigma.enigma_shop.constant.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = ConstantTable.PAYMENT)
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	@Column(name = "token")
	private String token;

	@Column(name = "redirect_url")
	private String redirectUrl;

	@Column(name = "transaction_status")
	private String transactionStatus;

	// ordered, pending, settlement, cancel, deny, expire, failure
//	@Enumerated(EnumType.STRING)
//	@Column(name = "transaction_status")
//	private TransactionStatus transactionStatus;

//	@OneToOne(mappedBy = "payment") // ini harus sama dengan attribute di transaction
//	private Transaction transaction;
}
