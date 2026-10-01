package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.TicketType;

@Service
public interface TicketTypeService {
	
	TicketType getByShowId(Long showId);

	List<TicketType> getAllTicketTypeByShowId(Long showId);
}
