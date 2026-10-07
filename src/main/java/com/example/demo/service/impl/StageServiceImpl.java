package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Stage;
import com.example.demo.repository.StageRepository;
import com.example.demo.service.StageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StageServiceImpl implements StageService {

	private final StageRepository stageRepository;
	
	@Override
	public List<Stage> getAllStageByShowId(Long showId) {
		return stageRepository.findAllStageByShowId(showId);
	}
}
