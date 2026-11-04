package com.example.demo.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.constant.TroupeStatus;
import com.example.demo.dto.AuthDto;
import com.example.demo.dto.TroupeDto;
import com.example.demo.entity.Troupe;
import com.example.demo.exception.AlreadyExistsException;
import com.example.demo.exception.NoDataFoundException;
import com.example.demo.repository.TroupeRepository;
import com.example.demo.service.TroupeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TroupeServiceImpl implements TroupeService {

	private final TroupeRepository troupeRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Transactional
	@Override
	public void register(AuthDto authDto) {
		log.info("劇団の新規登録メソッドを開始します。");
		String email = authDto.getEmail();
		boolean exist = troupeRepository.findByEmail(email).isPresent();
		if(!exist) {

			String hashedPassword = passwordEncoder.encode(authDto.getPassword());
			
			Troupe troupe = authDto.toEntity();
			troupe.setPasswordHash(hashedPassword);
			
			log.info("劇団を新規登録しました。 劇団名: {}", authDto.getName());
			troupeRepository.save(troupe);
		}else {
			log.warn("すでにメールアドレスが存在するため、登録できません。メールアドレス: {}", authDto.getEmail());
			throw new AlreadyExistsException("すでにメールアドレスが存在します");
		}
	}
	
	@Override
	@Transactional
	public void update(TroupeDto troupeDto) {
		Troupe troupe = getById(troupeDto.getId());
		
		troupe.setName(troupeDto.getName());
		troupe.setEmail(troupeDto.getEmail());
		troupe.setSignature(troupeDto.getSignature());
		
		troupeRepository.save(troupe);
		
		log.info("劇団更新処理が完了しました。 劇団Id: {}", troupe.getId());
	}
	
	@Override
	@Transactional
	public void delete(Long id) {
		Troupe troupe = getById(id);
		
		troupe.setStatus(TroupeStatus.WITHDRAWED);
		
		troupeRepository.save(troupe);
		log.info("劇団が削除されました。");
	}

	@Override
	public Troupe getById(Long id) {
		return troupeRepository.findById(id).orElseThrow(() ->
			new NoDataFoundException("このIdは存在しません" + id)
		);
	}
	
	@Override
	public Troupe getByEmail(String email) {
		return troupeRepository.findByEmail(email).orElseThrow(() ->
			new NoDataFoundException("このemailは存在しません" + email)
		);
	}
}
