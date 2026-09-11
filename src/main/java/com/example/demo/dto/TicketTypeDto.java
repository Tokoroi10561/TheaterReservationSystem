package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.example.demo.entity.TicketType;

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
public class TicketTypeDto {

	@NotNull(message = "idは必須です")
	private Long id;
	
	@NotBlank(message = "チケットタイプ名は必須です")
	@Size(min = 1, max = 100, message = "チケットタイプ名は1文字以上100文字以下で入力してください")
	private String name;
	
	@NotNull(message = "チケット料金は必須です")
	private Integer price;
	
	@NotNull(message = "公演idは必須です")
	private Long showId;
	
	@NotNull(message = "予約明細idは必須です")
	private Long bookingDetailsId;
	
	public TicketType toEntity() {
		return TicketType.builder()
				.name(this.name)
				.price(this.price)
//				.showId(this.showId)
//				.bookindDetailsId(this.bookingDetailsId)
				.build();
	}
	
	public static TicketTypeDto fromEntity(TicketType ticketType) {
		return TicketTypeDto.builder()
				.id(ticketType.getId())
				.name(ticketType.getName())
				.price(ticketType.getPrice())
				.showId(ticketType.getShow().getId())
				.bookingDetailsId(ticketType.getBookingDetails().getId())
				.build();
	}
}
