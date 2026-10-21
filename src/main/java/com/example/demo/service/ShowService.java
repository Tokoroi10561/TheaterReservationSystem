package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.ShowDto;
import com.example.demo.entity.Show;

@Service
public interface ShowService {

	void createShow(ShowDto showDto, Long troupeId);
	void updateShow(ShowDto showDto);
	void deleteShow(Long id);
	
	Show getById (Long id);
}
