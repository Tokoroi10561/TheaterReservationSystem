package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.repository.StageRepository;
import com.example.demo.service.StageService;

public class StageServiceImpl implements StageService {

	@Autowired
	private StageRepository stageRepository;
	
	@Override
	public void getAllStage() {
		stageRepository.findAll();
	}
}
