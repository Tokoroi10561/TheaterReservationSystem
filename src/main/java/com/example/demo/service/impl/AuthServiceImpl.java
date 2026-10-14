package com.example.demo.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.TroupeDto;
import com.example.demo.entity.Troupe;
import com.example.demo.service.AuthService;
import com.example.demo.service.TroupeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
	private final PasswordEncoder passwordEncoder;
	private final TroupeService troupeService;
	
	@Override
	public boolean ckeck(TroupeDto dto){
		String email = dto.getEmail();
		String rawPassword = dto.getPassword();
		
		Troupe troupe = troupeService.getByEmail(email);
		
		String hashedPassword = troupe.getPasswordHash();
		
		boolean isSuccess = passwordEncoder.matches(rawPassword, hashedPassword);
		
		return isSuccess;
	}
}
