package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.TroupeDto;

@Service
public interface AuthService {
	
	boolean ckeck(TroupeDto dto);

}
