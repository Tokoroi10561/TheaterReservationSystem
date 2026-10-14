package com.example.demo.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.TroupeDto;
import com.example.demo.entity.Troupe;
import com.example.demo.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
	private final PasswordEncoder passwordEncoder;
	
	public boolean ckeck(TroupeDto dto, Troupe troupe) {
		
	}
}
