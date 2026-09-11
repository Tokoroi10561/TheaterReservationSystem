package com.example.demo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

import com.example.demo.entity.BookingDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDetailsDto {

	@NotNull(message = "idは必須です")
	private Long id;
	
	@NotNull(message = "枚数は必須です")
	@Max(value = 10, message = "枚数は10以下で入力してください")
	private Integer quantity;
	
	@NotNull(message = "予約idは必須です")
	private Long bookingId;
	
	public BookingDetails toEntity() {
		return BookingDetails.builder()
				.quantity(this.quantity)
//				.bookingId(this.bookingId)
				.build();
	}
	
	public static BookingDetailsDto fromEntity(BookingDetails bookingDetails) {
		return BookingDetailsDto.builder()
				.id(bookingDetails.getId())
				.quantity(bookingDetails.getQuantity())
				.bookingId(bookingDetails.getBooking().getId())
				.build();
	}
}
