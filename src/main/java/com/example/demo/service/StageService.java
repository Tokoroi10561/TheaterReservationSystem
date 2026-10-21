package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.StageDto;
import com.example.demo.entity.Booking;
import com.example.demo.entity.Show;
import com.example.demo.entity.Stage;

@Service
public interface StageService {

	void saveStages(List<StageDto> stageDtos, Show show);
	void deleteStages(List<Stage> stages);
	
	void changeStageStatus(Stage stage, int total, Booking booking);
	
	Stage stageExist(Long stageId);
	
	List<Stage> getAllStageByShowId(Long showId);	
}
