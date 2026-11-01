package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.AuthDto;
import com.example.demo.dto.TroupeDto;
import com.example.demo.entity.Troupe;

@Service
public interface TroupeService {

	void register(AuthDto dto);
	void update(TroupeDto dto);
	void delete(Long id);

	Troupe getById(Long id);
	Troupe getByEmail(String email);
}
