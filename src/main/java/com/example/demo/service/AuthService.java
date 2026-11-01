package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.AuthDto;
import com.example.demo.dto.LoginDto;

@Service
public interface AuthService {
	
	void registerNewTroupe(AuthDto dto);
	
	boolean check(LoginDto dto);
	
	Long getTroupeId(LoginDto loginDto);
}
