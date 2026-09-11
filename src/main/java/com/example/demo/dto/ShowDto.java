package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
	
	@NotNull(message = "idは必須です")
	private Long id;
	
	@NotBlank(message = "タイトルは必須です")
	@Size(min = 1, max = 100, message = "タイトルは1文字以上100文字以内で入力してください")
	private String title;
	
	@NotBlank(message = "場所は必須です")
	@Size(min = 1, max = 100, message = "場所は1文字以上100文字以内で入力してください")
	private String place;
	
	@Size(max = 255, message = "ビラ画像のURLは255文字以内で入力してください")
	private String flyerImageUrl;
	
	@NotNull(message = "劇団idは必須です")
	private Long TroupeId;
	
	public Show toEntity() {
		return Show.builder()
				.title(this.title)
				.place(this.place)
				.flyerImageUrl(this.flyerImageUrl)
//				.TroupeId(troupeId)
				.build();
	}
	
	public static ShowDto fromEntity(Show show) {
		return ShowDto.builder()
				.title(show.getTitle())
				.place(show.getPlace())
				.flyerImageUrl(show.getFlyerImageUrl())
//				.troupeId(show.getTroupeId())
				.build();
	}
}
