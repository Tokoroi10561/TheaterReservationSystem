package com.example.demo.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.example.demo.constant.BookingStatus;
import com.example.demo.constant.PaymentMethod;
import com.example.demo.constant.PaymentStatus;
import com.example.demo.entity.Booking;

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
public class BookingDto {

	@NotNull(message = "idは必須です")
	private Long id;
	
	@NotBlank(message = "名前は必須です")
	@Size(min = 1, max = 100, message = "名前は1文字以上100文字以下で入力してください")
	private String name;
	
	@NotBlank(message = "メールアドレスは必須です")
	@Email(message = "メールアドレスの形式が正しくありません")
	private String email;
	
	@Size(max = 20, message = "電話番号は20文字以下で入力してください")
	@Pattern(regexp = "^0\\d{9,10}$", message = "電話番号はハイフンなしの10桁もしくは11桁で入力してください")
	private String phone;
	
	@Size(max = 255, message = "住所は255文字以内で入力してください")
	private String address;
	
	@Size(max = 1000, message = "備考は1000文字以内で入力してください")
	private String remarks;
	
	@NotNull(message = "トークンは必須です")
	@Size(max = 255, message = "トークンは255文字以内で入力してください")
	private String token;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
	
	private PaymentMethod paymentMethod;
	
	private BookingStatus bookingStatus;
	
	private PaymentStatus paymentStatus;
	
	@NotNull(message = "ステージidは必須です")
	private Long stageId;
	
	@NotNull(message = "スタッフidは必須です")
	private Long staffId;
	
	public Booking toEntity() {
		return Booking.builder()
				.name(this.name)
				.email(this.email)
				.phone(this.phone)
				.address(this.address)
				.remarks(this.remarks)
				.token(this.token)
				.createdAt(this.createdAt)
				.updatedAt(this.updatedAt)
				.paymentMethod(this.paymentMethod)
				.bookingStatus(this.bookingStatus)
				.paymentStatus(this.paymentStatus)
//				.stage(this.stageId)
//				.staff(this.staffId)
				.build();
	}
	
	public static BookingDto fromEntity(Booking booking) {
		return BookingDto.builder()
				.id(booking.getId())
				.name(booking.getName())
				.email(booking.getEmail())
				.phone(booking.getPhone())
				.address(booking.getAddress())
				.remarks(booking.getRemarks())
				.token(booking.getToken())
				.createdAt(booking.getCreatedAt())
				.updatedAt(booking.getUpdatedAt())
				.paymentMethod(booking.getPaymentMethod())
				.bookingStatus(booking.getBookingStatus())
				.paymentStatus(booking.getPaymentStatus())
				.stageId(booking.getStage().getId())
				.staffId(booking.getStaff().getId())
				.build();
	}
}
