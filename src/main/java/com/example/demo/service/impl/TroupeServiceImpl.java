package com.example.demo.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
	
	@Override
	public Troupe getByEmail(String email) {
		return troupeRepository.findByEmail(email).orElseThrow(() ->
			new NoDataFoundException("このemailは存在しません")
		);
	}
	
	@Override
	public Troupe getById(Long id) {
		return troupeRepository.findById(id).orElseThrow(() ->
			new NoDataFoundException("このIdは存在しません" + id)
		);
	}
	
	@Override
	public void regist(TroupeDto dto) {
		log.info("劇団の新規登録メソッドを開始します。");
		String email = dto.getEmail();
		boolean exist = troupeRepository.findByEmail(email).isPresent();
		if(!exist) {

			String hashedPassword = passwordEncoder.encode(dto.getPassword());
			
			Troupe troupe = dto.toEntity();
			troupe.setPasswordHash(hashedPassword);
			
			log.info("劇団を新規登録しました。 劇団名: {}", dto.getName());
			troupeRepository.save(troupe);
		}else {
			log.warn("すでにメールアドレスが存在するため、登録できません。メールアドレス: {}", dto.getEmail());
			throw new AlreadyExistsException("すでにメールアドレスが存在します");
		}
	}
	
	//編集メソッド
	
	//削除メソッド
}
