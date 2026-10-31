package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.constant.PaymentMethod;
import com.example.demo.constant.ShowOption;
import com.example.demo.constant.ShowStatus;
import com.example.demo.entity.Show;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShowDto {
	
	private Long id;
	
	@NotBlank(message = "タイトルは必須です")
	@Size(min = 1, max = 100, message = "タイトルは1文字以上100文字以内で入力してください")
	private String title;
	
	@Size(max = 100, message = "場所は0文字以上100文字以内で入力してください")
	private String place;

	private MultipartFile flyerImage;
	
	private String flyerFileName;
			
	@Future(message = "未来の時間を入力してください")
	private LocalDateTime deletedAt;
	
	private Long troupeId;
	
	private List<StaffDto> staffs = new ArrayList<>();
	
	private List<StageDto> stages = new ArrayList<>();
	
	private List<TicketTypeDto> ticketTypes = new ArrayList<>();
	
	private Set<ShowOption> options;
	
	private PaymentMethod paymentMethod;
	
	private ShowStatus showStatus;
	
	public Show toEntity() {
		return Show.builder()
				.title(this.title)
				.place(this.place)
				.options(this.options)
				.paymentMethod(PaymentMethod.CASH)
				.showStatus(ShowStatus.DRAFT)
				.build();
	}
	
	public static ShowDto fromEntity(Show show) {
		return ShowDto.builder()
				.id(show.getId())
				.title(show.getTitle())
				.place(show.getPlace())
				.flyerFileName(show.getFlyerFileName())				.deletedAt(show.getDeletedAt())
				.troupeId(show.getTroupe().getId())
				.options(show.getOptions())
				.paymentMethod(show.getPaymentMethod())
				.showStatus(show.getShowStatus())
				.build();
	}
}
