package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.example.demo.entity.Staff;

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
public class StaffDto {

	@NotNull(message = "idは必須です")
	private Long id;
	
	@NotBlank(message = "スタッフ名は必須です")
	@Size(min = 1, max = 100, message = "スタッフ名は1文字以上100文字以下で入力してください")
	private String name;
	
	@Size(max = 100, message = "所属団体名は100文字以内で入力してください")
	private String belong;
	
	@NotNull(message = "公演idは必須です")
	private Long showId;
	
	public Staff toEntity() {
		return Staff.builder()
				.name(this.name)
				.belong(this.belong)
//				.showId(this.showId)
				.build();
	}
	
	public static StaffDto fromEntity(Staff staff) {
		return StaffDto.builder()
				.id(staff.getId())
				.name(staff.getName())
				.belong(staff.getBelong())
				.showId(staff.getShow().getId())
				.build();
	}
}
