package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

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
}
