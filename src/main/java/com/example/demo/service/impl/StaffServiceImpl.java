package com.example.demo.service.impl;

import com.example.demo.repository.StaffRepository;
import com.example.demo.service.StaffService;

public class StaffServiceImpl implements StaffService {

	private StaffRepository staffRepositroy;
	
	@Override
	public void getAllStaff() {
		staffRepositroy.findAll();
	}
}
