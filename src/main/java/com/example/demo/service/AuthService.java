package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.LoginDto;

@Service
public interface AuthService {
	
	boolean ckeck(LoginDto dto);

}
