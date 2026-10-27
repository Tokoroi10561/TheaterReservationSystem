package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.ShowDto;
import com.example.demo.entity.Show;

@Service
public interface ShowService {

	void createShow(ShowDto showDto, Long troupeId);
	Long updateShow(ShowDto showDto);
	void deleteShow(Long id);
	
	Show getById (Long id);
	
	List<Show> findAllActive(Long troupeId);
}
