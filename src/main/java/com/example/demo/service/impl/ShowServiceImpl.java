package com.example.demo.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.ShowDto;
import com.example.demo.entity.Show;
import com.example.demo.entity.Troupe;
import com.example.demo.repository.ShowRepository;
import com.example.demo.service.ShowService;
import com.example.demo.service.StaffService;
import com.example.demo.service.StageService;
import com.example.demo.service.TicketTypeService;
import com.example.demo.service.TroupeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShowServiceImpl implements ShowService {
	
	private final ShowRepository showRepository;
	private final TroupeService troupeService;
	private final StaffService staffService;
	private final StageService stageService;
	private final TicketTypeService ticketTypeService;
	
	@Transactional
	@Override
	public void createShow(ShowDto showDto, Long troupeId) {
		Show show = showDto.toEntity();
		Troupe troupe = troupeService.getById(troupeId);
		show.setTroupe(troupe);
		showRepository.save(show);
		
		staffService.saveStaffs(showDto.getStaffs(), show);
		stageService.saveStages(showDto.getStages(), show);
		ticketTypeService.saveTicketTypes(showDto.getTicketTypes(), show);
	}
	
	@Override
	public void updateShow() {
		
	}
	
	@Override
	public void deleteShow() {
		
	}
}
