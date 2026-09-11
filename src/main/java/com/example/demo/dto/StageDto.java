package com.example.demo.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.example.demo.constant.StageStatus;
import com.example.demo.entity.Show;
import com.example.demo.entity.Stage;

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
public class StageDto {

	@NotNull(message = "idは必須です")
	private Long id;
	
	@NotNull(message = "開演時間は必須です")
	private LocalDateTime startTime;
	
	@NotNull(message = "開場時間は必須です")
	private LocalDateTime endTime;
	
	@NotNull(message = "開場時間は必須です")
	private LocalDateTime openTime;
	
	@NotNull(message = "席数上限は必須です")
	@Size(min = 1, message = "席数は1以上の数字を入力してください")
	private Integer capacity;
	
	@NotNull(message = "ステージステータスは必須です")
	private StageStatus status;
	
	@NotNull(message = "公演IDは必須です")
	private Show showId;
	
	public Stage toEntity() {
		return Stage.builder()
				.startTime(this.startTime)
				.endTime(this.endTime)
				.openTime(this.openTime)
				.capacity(this.capacity)
				.status(this.status)
//				.showId(this.showId)
				.build();
	}
	
	public static StageDto fromEntity(Stage stage) {
		return StageDto.builder()
				.startTime(stage.getStartTime())
				.endTime(stage.getEndTime())
				.openTime(stage.getOpenTime())
				.capacity(stage.getCapacity())
				.status(stage.getStatus())
//				.showId(stage.getShowId())
				.build();
	}
}
