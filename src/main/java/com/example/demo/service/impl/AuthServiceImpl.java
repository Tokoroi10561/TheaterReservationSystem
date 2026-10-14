package com.example.demo.service.impl;

import jakarta.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.constant.TroupeStatus;
import com.example.demo.dto.LoginDto;
import com.example.demo.dto.TroupeDto;
import com.example.demo.entity.Troupe;
import com.example.demo.exception.AlreadyExistsException;
import com.example.demo.repository.TroupeRepository;
import com.example.demo.service.AuthService;
import com.example.demo.service.TroupeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
	private final PasswordEncoder passwordEncoder;
	private final TroupeService troupeService;
	private final TroupeRepository troupeRepository;
	
	@Override
	public boolean ckeck(LoginDto dto){
		String email = dto.getEmail();
		String rawPassword = dto.getPassword();
		
		Troupe troupe = troupeService.getByEmail(email);
		
		String hashedPassword = troupe.getPasswordHash();
		
		boolean isMatch = passwordEncoder.matches(rawPassword, hashedPassword);
		
		TroupeStatus status = troupe.getStatus();
		
		boolean isActive = status == TroupeStatus.ACTICE;
		
		if(isMatch && isActive) {
			return true;
		}else {
			return false;
		}
		
	}
	
	
	//劇団新規登録
	@Transactional
	@Override
	public void registNewTroupe(TroupeDto dto) {
		
		String email = dto.getEmail();
		boolean exist = troupeRepository.findByEmail(email).isPresent();
		if(!exist) {

			String hashedPassword = passwordEncoder.encode(dto.getPassword());
			
			Troupe troupe = dto.toEntity();
			troupe.setPasswordHash(hashedPassword);
			
			troupeRepository.save(troupe);
		}else {
			throw new AlreadyExistsException("すでにメールアドレスが存在します");
		}
	}
}
