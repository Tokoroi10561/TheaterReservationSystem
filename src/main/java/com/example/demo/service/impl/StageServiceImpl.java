package com.example.demo.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.constant.CapacityStatus;
import com.example.demo.constant.StageStatus;
import com.example.demo.entity.Booking;
import com.example.demo.entity.Stage;
import com.example.demo.exception.NoDataFoundException;
import com.example.demo.repository.StageRepository;
import com.example.demo.service.StageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StageServiceImpl implements StageService {

	private final StageRepository stageRepository;
	
	@Override
	public List<Stage> getAllStageByShowId(Long showId) {
		return stageRepository.findAllStageByShowId(showId);
	}
	
	@Override
	public Stage stageExist(Long stageId) {
		Stage stage = stageRepository.findById(stageId).orElseThrow(() -> 
		new NoDataFoundException("ステージが存在しません" + stageId)
	);
	log.info("ステージが存在しました。 stageId: {}", stage.getId());
	return stage;
	}
	
	public void changeStageStatus(Stage stage, int total, Booking booking) {
		int capacity = stage.getCapacity();
		
		double wideOpen = capacity * 0.3;
		double available = capacity * 0.7;
		
		LocalDateTime now = LocalDateTime.now();
		
		LocalDateTime stageTime = booking.getStage().getStartTime();
		LocalDate stageDay = stageTime.toLocalDate();
		LocalDateTime deadline = stageDay.minusDays(1).atTime(23, 59);
		
		if(!now.isAfter(deadline)) {
			if(wideOpen > total) {
				stage.setCapacityStatus(CapacityStatus.WIDE_OPEN);
			}else if(available > total) {
				stage.setCapacityStatus(CapacityStatus.AVALILABLE);
			}else if(capacity <= total) {
				stage.setCapacityStatus(CapacityStatus.FULL);
				stage.setStageStatus(StageStatus.SOLD_OUT);
			}else {
				stage.setCapacityStatus(CapacityStatus.LIMITED);
			}
		}else {
			stage.setStageStatus(StageStatus.CLOSED);
		}
	}
	
}
