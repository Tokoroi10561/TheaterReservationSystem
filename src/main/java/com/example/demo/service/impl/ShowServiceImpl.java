package com.example.demo.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
		
		show.setFlyerFileName(showDto.getFlyerFileName());
		
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
			
			show.setFlyerFileName(showDto.getFlyerFileName());
			
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
	
	@Override
	@Transactional
	public void saveFile(ShowDto showDto) {
		log.info("ファイル一時保存メソッドを開始します。");
		Path uploadDir = Path.of("uploads/temp");
		
		if(!Files.exists(uploadDir)) {
			try {
				Files.createDirectories(uploadDir);
				log.info("一時保存用ディレクトリを作成しました。");
			} catch (IOException e) {
				log.error("一時保存用ディレクトリ作成失敗", e);
				throw new RuntimeException("一時保存用ディレクトリ作成失敗", e);
			}
		}
		
		MultipartFile file = showDto.getFlyerImage();
		
		String tempFileName = UUID.randomUUID() + ".pdf";
		log.info("DB保存用ファイル名: {}", tempFileName);
		
		if(file != null && !file.isEmpty()) {
			try {
				Files.copy(
						file.getInputStream(),
						uploadDir.resolve(tempFileName)
						);
				log.info("フライヤーを一時保存場所に保存");
			} catch (IOException e) {
				log.error("フライヤー保存失敗", e);
				throw new RuntimeException("フライヤー保存失敗", e);
			}
			
			showDto.setFlyerFileName(tempFileName);
		}
	}
	
	@Override
	@Transactional
	public void registerFile(ShowDto showDto) {
		log.info("ファイル移動メソッドを開始します。");
		
		Path tempDir = Path.of("uploads/temp");
		Path permDir = Path.of("uploads/saved");
		
		if(!Files.exists(permDir)) {
			try {
				Files.createDirectories(permDir);
			}catch(IOException e) {
				log.error("本番ディレクトリ作成失敗。", e);
				throw new RuntimeException("本番ディレクトリ作成失敗");
			}
		}
		
		String fileName = showDto.getFlyerFileName();
		
		if(fileName != null && !fileName.isBlank()) {
			Path sourcePath = tempDir.resolve(fileName);
			Path targetPath = permDir.resolve(fileName);
			
			try {
				if(Files.exists(sourcePath)) {
					Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
				}else {
					log.warn("一時ファイルが見つかりません (すでに削除された可能性): {}", sourcePath);
				}
				
				log.info("ファイルを本番ディレクトリへ移動しました: {}", fileName);
				
			}catch(IOException e) {
				log.error("ファイルの移動に失敗しました", e);
	            throw new RuntimeException("ファイルの移動に失敗しました", e);
			}
		}
	}
}
