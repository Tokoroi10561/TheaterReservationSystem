package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.constant.PaymentMethod;
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
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShowServiceImpl implements ShowService {
	
	private final ShowRepository showRepository;
	private final TroupeService troupeService;
	private final StaffService staffService;
	private final StageService stageService;
	private final TicketTypeService ticketTypeService;
	
	@Transactional
	@Override
	public void createShow(ShowDto showDto, Long troupeId) {
		log.info("予約処理を開始します。");
		Show show = showDto.toEntity();
		Troupe troupe = troupeService.getById(troupeId);
		show.setTroupe(troupe);
		showRepository.save(show);
		
		staffService.saveStaffs(showDto.getStaffs(), show);
		stageService.saveStages(showDto.getStages(), show);
		ticketTypeService.saveTicketTypes(showDto.getTicketTypes(), show);
		log.info("{}の公演登録が完了しました。 公演ID: {}", showDto.getTitle(), showDto.getId());
	}
	
	@Transactional
	@Override
	public Long updateShow(ShowDto showDto) {
		log.info("予約更新処理を開始します。");
		Show show = getById(showDto.getId());
		
		if(show.getDeletedAt() != null) {
			log.warn("バリデーションエラー");
			throw new IllegalArgumentException("すでに削除されています");
		}else {
			show.setTitle(showDto.getTitle());
			show.setPlace(showDto.getPlace());
			
			//後で絶対変えないといけない
			show.setPaymentMethod(PaymentMethod.CASH);
			show.setOptions(showDto.getOptions());
//			show.setFlyerImage(showDto.getFlyerImage());
			
			Long troupeId = show.getTroupe().getId();
						
			showRepository.save(show);
			
			staffService.deleteStaffs(show.getStaffs());
			stageService.deleteStages(show.getStages());
			ticketTypeService.deleteTicketTypes(show.getTicketTypes());
			
			showRepository.flush();
			
			staffService.saveStaffs(showDto.getStaffs(), show);
			stageService.saveStages(showDto.getStages(), show);
			ticketTypeService.saveTicketTypes(showDto.getTicketTypes(), show);
			log.info("予約更新処理が完了しました。公演ID: {}", show.getId());
			
			return troupeId;
		}
	}
	
	@Transactional
	@Override
	public void deleteShow(Long id) {
		log.info("予約削除処理を開始します。");
		Show show = getById(id);
		
		if(show.getDeletedAt() != null) {
			log.warn("バリデーションエラー");
			throw new IllegalArgumentException("この公演はすでに削除されています。" + id);
		}else {
			show.setDeletedAt(LocalDateTime.now());
			
			showRepository.save(show);
			log.info("予約削除処理が完了しました。公演ID: {}", show.getId());
		}
	}
	
	@Override
	public Show getById(Long id) {
		log.info("showIdからのshowオブジェクト取得メソッドを開始します");
		Show show = showRepository.findById(id).orElseThrow(() ->
			new NoDataFoundException("Idが存在しません" + id)
		);
		
		if(show.getDeletedAt() != null) {
			log.warn("バリデーションエラー");
			throw new IllegalArgumentException("この公演はすでに削除されています。" + id);
		}else {
			log.info("showIdからのshowオブジェクト取得メソッドが完了しました。 公演ID: {}", id);
			return show;
		}
	}
	
	@Override
	public List<Show> findAllActive(Long troupeId){
		log.info("activeなshowオブジェクト全取得メソッドを開始します。");
		return showRepository.findAllActive(troupeId);
	}
}
