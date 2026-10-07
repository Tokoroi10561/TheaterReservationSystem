package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.TicketType;
import com.example.demo.exception.NoDataFoundException;
import com.example.demo.repository.TicketTypeRepository;
import com.example.demo.service.TicketTypeService;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeService {

	private final TicketTypeRepository ticketTypeRepository;
	
	@Override
	public TicketType getById(Long id){
		TicketType ticket = ticketTypeRepository.findById(id).orElseThrow(() ->
		new NoDataFoundException("チケットタイプが存在しません")
		);
		return ticket;
	}
	
	@Override
	public List<TicketType> getAllTicketTypeByShowId(Long showId) {
		return ticketTypeRepository.findAllTicketTypeByShowId(showId);
	}
}
