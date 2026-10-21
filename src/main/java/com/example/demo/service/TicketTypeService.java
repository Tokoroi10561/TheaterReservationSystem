package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.TicketTypeDto;
import com.example.demo.entity.Show;
import com.example.demo.entity.TicketType;

@Service
public interface TicketTypeService {
	
	TicketType getById(Long id);

	List<TicketType> getAllTicketTypeByShowId(Long showId);
	
	void saveTicketTypes(List<TicketTypeDto> ticketTypeDtos, Show show);
}
