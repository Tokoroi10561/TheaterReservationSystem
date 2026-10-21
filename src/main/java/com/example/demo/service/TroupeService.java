package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.TroupeDto;
import com.example.demo.entity.Troupe;

@Service
public interface TroupeService {

	void regist(TroupeDto dto);

	Troupe getById(Long id);
	Troupe getByEmail(String email);
}
