package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.ShowDto;

@Service
public interface ShowService {

	void createShow(ShowDto showDto, Long troupeId);
	void updateShow();
	void deleteShow();
}
