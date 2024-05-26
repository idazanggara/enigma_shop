package com.enigma.enigma_shop.dto.request;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
/*
 * Anotasi Validasi dari JPA/Hibernate
 * @NotBlank -> Validasi tidak boleh kosong termasuk whitespace (spasi kosong). (Khusus untuk string lah)
 * @NotNull -> Tidak boleh null/kosong (Number, Object)
 * @Min -> Number untuk menentukan minimal angka yg harus di isi
 * @Max -> Number untuk menentukan max angka yg harus di isi
 * @Size -> Berapa banyak panjang character pada string (Parameternya ada min & max)
 * @Email -> untuk menentukan email itu valid atau tidak
 * */
public class NewProductRequest {
	@NotBlank(message = "name is required")
	private String name;

	@NotNull(message = "price is required")
	@Min(value = 0, message = "price must be greater than or equal 0")
	private Long price;

	@NotNull(message = "stock is required")
	@Min(value = 0, message = "stock must be greater than or equal 0")
	private Integer stock;

	// jadi semua error dari validasinya nanti akan ngethrow custom validasi yg sudah di buatkan yaitu :
//	ConstraintViolationException
	// jadi nanti perlu kita tangkap errornya, untuk kita kirim ke client

	// tapi sebelum itu, bagaimana untuk ngetrigger atau ngebuat validasi kita digunakan oleh program kita?

	// untuk triggernya kita buat ValidationUtil di util

	private MultipartFile image;
}


