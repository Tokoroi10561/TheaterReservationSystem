package com.example.demo.service.impl;

import com.example.demo.entity.Troupe;
import com.example.demo.exception.NoDataFoundException;
import com.example.demo.repository.TroupeRepository;
import com.example.demo.service.TroupeService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TroupeServiceImpl implements TroupeService {

	private final TroupeRepository troupeRepository;
	
	@Override
	public Troupe getByEmail(String email) {
		return troupeRepository.findByEmail(email).orElseThrow(() ->
			new NoDataFoundException("この劇団は存在しません")
		);
	}
}
