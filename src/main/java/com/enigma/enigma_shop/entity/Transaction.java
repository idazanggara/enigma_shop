package com.enigma.enigma_shop.entity;

import com.enigma.enigma_shop.constant.ConstantTable;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder // instance dengan bergaya
@Entity
@Table(name = ConstantTable.TRANSACTION)
public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	@ManyToOne // dari transaksi ke customer relasinya apa?
	@JoinColumn(name = "customer_id") // untuk FK nya, cutomer boleh null ya, mungkin nanti transaksi langsung tanpa data customer
	private Customer customer;

	// OneToMay : Transaction dengan TransactionDetail
	// kita buat transaksidetailnya
	@OneToMany(mappedBy = "transaction") // bedanya apa kalau enggak pakai mapped? jadi nanti otomatis buat tabel baru, t_transaction_transaction_detal kalau kita enggak tambahkan mapped, kalau mau di coba kita drop semua table transaksi
	@JsonManagedReference // ini juga yg handle stackoverflow di taroh di 1toM
	private List<TransactionDetail> transactionDetails;
	// ini nanti jadi masalah stackoverflow

	@Temporal(TemporalType.TIMESTAMP) // kalau date doang enggak detail guys, jadi harus kita detailkan
	@Column(name = "trans_date", updatable = false) // biar enggak boleh di upate
	private Date transDate;

	// seteleh rest-client
	@OneToOne
	@JoinColumn(name = "payment_id", unique = true)
	private Payment payment;
}
