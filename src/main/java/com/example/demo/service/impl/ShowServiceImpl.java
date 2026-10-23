package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.ShowDto;
import com.example.demo.entity.Show;
import com.example.demo.entity.Troupe;
import com.example.demo.exception.NoDataFoundException;
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
	
	@Transactional
	@Override
	public void updateShow(ShowDto showDto) {
		
		Show show = getById(showDto.getId());
		
		if(show.getDeletedAt() == null) {
			throw new IllegalArgumentException("すでに削除されています");
		}else {
			show.setTitle(showDto.getTitle());
			show.setPlace(showDto.getPlace());
			show.setFlyerImage(showDto.getFlyerImage());
			
			Troupe troupe = troupeService.getById(showDto.getTroupeId());
			show.setTroupe(troupe);
			
			showRepository.save(show);
			
			staffService.deleteStaffs(show.getStaffs());
			stageService.deleteStages(show.getStages());
			ticketTypeService.deleteTicketTypes(show.getTicketTypes());
			
			staffService.saveStaffs(showDto.getStaffs(), show);
			stageService.saveStages(showDto.getStages(), show);
			ticketTypeService.saveTicketTypes(showDto.getTicketTypes(), show);
		}
	}
	
	@Transactional
	@Override
	public void deleteShow(Long id) {
		Show show = getById(id);
		
		if(show.getDeletedAt() == null) {
			throw new IllegalArgumentException("この公演はすでに削除されています。" + id);
		}else {
			show.setDeletedAt(LocalDateTime.now());
			
			showRepository.save(show);
		}
	}
	
	@Override
	public Show getById(Long id) {
			Show show = showRepository.findById(id).orElseThrow(() ->
				new NoDataFoundException("Idが存在しません" + id)
			);
			
			if(show.getDeletedAt() == null) {
				throw new IllegalArgumentException("この公演はすでに削除されています。" + id);
			}else {
				return show;
			}
	}
	
	@Override
	public List<Show> findAllActive(Long troupeId){
		return showRepository.findAllActive(troupeId);
	}
}
