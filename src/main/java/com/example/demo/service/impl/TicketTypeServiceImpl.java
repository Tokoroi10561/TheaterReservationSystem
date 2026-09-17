package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.TicketType;
import com.example.demo.repository.TicketTypeRepository;
import com.example.demo.service.TicketTypeService;
@Service
public class TicketTypeServiceImpl implements TicketTypeService {

	@Autowired
	private TicketTypeRepository ticketTypeRepository;
	
	@Override
	public List<TicketType> getAllTicketType() {
		return ticketTypeRepository.findAllTicketType();
	}
}
