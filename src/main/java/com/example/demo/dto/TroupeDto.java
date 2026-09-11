package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.example.demo.entity.Troupe;

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
public class TroupeDto {

	@NotNull(message = "idは必須です")
	private Long id;
	
	@NotBlank(message = "劇団名は必須です")
	@Size(min = 1, max = 100, message = "劇団名は1文字以上100文字以内で入力してください")
	private String name;
	
	@NotBlank(message = "メールアドレスは必須です")
	@Email(message = "メールアドレスの形式が正しくありません")
	private String email;
	
	@NotBlank(message = "パスワードは必須です")
	@Size(min = 8, max = 20, message = "パスワードは8文字以上20文字以内で入力してください")
	private String password;
	
	@Size(max = 20, message = "SNSアカウントは20文字以内で入力してください")
	private String snsAccount;
	
	@Size(max = 1000, message = "署名は1000文字以内で入力してください")
	private String signature;
	
	public Troupe toEntity() {
		return Troupe.builder()
				.id(this.id)
				.name(this.name)
				.email(this.email)
				.passwordHash(this.password)
				.snsAccount(this.snsAccount)
				.signature(this.signature)
				.build();
	}
	
	public static TroupeDto fromEntity(Troupe troupe) {
		return TroupeDto.builder()
				.id(troupe.getId())
				.name(troupe.getName())
				.email(troupe.getEmail())
				.password(troupe.getPasswordHash())
				.snsAccount(troupe.getSnsAccount())
				.signature(troupe.getSignature())
				.build();
	}
}
