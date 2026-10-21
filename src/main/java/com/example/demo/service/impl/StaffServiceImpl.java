package com.example.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.StaffDto;
import com.example.demo.entity.Show;
import com.example.demo.entity.Staff;
import com.example.demo.repository.StaffRepository;
import com.example.demo.service.StaffService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements StaffService {

	private final StaffRepository staffRepository;
	
	@Override
	public List<Staff> getAllStaffByShowId(Long showId) {
		return staffRepository.findAllStaffByShowId(showId);
	}
	
	@Override
	public Staff getStaffById(Long id){
		Staff staff = staffRepository.findById(id).orElse(null);
		
		return staff;
	}
	
	@Override
	public void saveStaffs(List<StaffDto> staffDtos, Show show) {
		for(StaffDto staffDto : staffDtos) {
			Staff staff = staffDto.toEntity();
			staff.setShow(show);
			staffRepository.save(staff);
		}
	}
}
