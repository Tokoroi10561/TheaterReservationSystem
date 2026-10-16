package com.example.demo.service.impl;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.constant.TroupeStatus;
import com.example.demo.dto.LoginDto;
import com.example.demo.dto.TroupeDto;
import com.example.demo.entity.Troupe;
import com.example.demo.exception.AlreadyExistsException;
import com.example.demo.repository.TroupeRepository;
import com.example.demo.service.AuthService;
import com.example.demo.service.TroupeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{
	private final PasswordEncoder passwordEncoder;
	private final TroupeService troupeService;
	private final TroupeRepository troupeRepository;
	
	@Override
	public boolean check(LoginDto dto){
		log.info("ログインチェックメソッドを開始します。");
		String email = dto.getEmail();
		String rawPassword = dto.getPassword();
		
		Troupe troupe = troupeService.getByEmail(email);
		if(troupe == null) {
			log.warn("メールアドレスが存在しません");
			return false;
		}
		
		String hashedPassword = troupe.getPasswordHash();
		
		boolean isMatch = passwordEncoder.matches(rawPassword, hashedPassword);
		
		TroupeStatus status = troupe.getStatus();
		
		boolean isActive = status == TroupeStatus.ACTIVE;
		
		if(isMatch && isActive) {
			log.info("チェックができました。 劇団名: {}", troupe.getName());
			return true;
		}else {
			log.warn("パスワードが間違っているかもしくはアクティブではありません。");
			return false;
		}
		
	}
	
	
	//劇団新規登録
	@Transactional
	@Override
	public void registNewTroupe(TroupeDto dto) {
		log.info("劇団の新規登録メソッドを開始します。");
		String email = dto.getEmail();
		boolean exist = troupeRepository.findByEmail(email).isPresent();
		if(!exist) {

			String hashedPassword = passwordEncoder.encode(dto.getPassword());
			
			Troupe troupe = dto.toEntity();
			troupe.setPasswordHash(hashedPassword);
			
			log.info("劇団を新規登録しました。 劇団名: ", dto.getName());
			troupeRepository.save(troupe);
		}else {
			log.warn("すでにメールアドレスが存在するため、登録できません。メールアドレス: ", dto.getEmail());
			throw new AlreadyExistsException("すでにメールアドレスが存在します");
		}
	}
}
