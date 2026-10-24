package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.LoginDto;
import com.example.demo.dto.TroupeDto;

@Service
public interface AuthService {
	
	void registNewTroupe(TroupeDto dto);
	
	boolean check(LoginDto dto);
	
	Long getTroupeId(LoginDto loginDto);
}
