package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Staff;
import com.example.demo.repository.StaffRepository;
import com.example.demo.service.StaffService;

@Service
public class StaffServiceImpl implements StaffService {

	private StaffRepository staffRepositroy;
	
	@Override
	public List<Staff> getAllStaff() {
		return staffRepositroy.findAllStaff();
	}
}
