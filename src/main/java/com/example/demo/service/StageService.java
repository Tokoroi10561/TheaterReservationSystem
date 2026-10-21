package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.StageDto;
import com.example.demo.entity.Booking;
import com.example.demo.entity.Show;
import com.example.demo.entity.Stage;

@Service
public interface StageService {

	List<Stage> getAllStageByShowId(Long showId);
	Stage stageExist(Long stageId);
	void changeStageStatus(Stage stage, int total, Booking booking);
	void saveStages(List<StageDto> stageDtos, Show show);
}
