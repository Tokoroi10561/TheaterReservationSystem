package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.StaffDto;
import com.example.demo.entity.Show;
import com.example.demo.entity.Staff;

@Service
public interface StaffService {
	
	void saveStaffs(List<StaffDto> staffDtos, Show show);
	void deleteStaffs(List<Staff> staffs);
	
	Staff getStaffById(Long id);

	List<Staff> getAllStaffByShowId(Long showId);
}
