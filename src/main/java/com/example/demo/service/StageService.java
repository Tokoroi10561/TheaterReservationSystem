package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Stage;

@Service
public interface StageService {

	List<Stage> getAllStage();
}
