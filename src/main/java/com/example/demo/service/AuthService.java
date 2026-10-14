package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.LoginDto;
import com.example.demo.dto.TroupeDto;

@Service
public interface AuthService {
	
	boolean ckeck(LoginDto dto);
	
	void registNewTroupe(TroupeDto dto);

}
