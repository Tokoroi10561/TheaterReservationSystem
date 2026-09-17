package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.BookingDto;

@Service
public interface BookingService {
	
	void createBooking(BookingDto dto);
	
	void updateBooking(BookingDto dto, String token);
	
	void deleteBooking(String token);
	
//	List<Booking> getAllBookings();
//	
//	List<Booking> getBookingsByName(String name);
//	
//	List<Booking> getBookingsByEmail(String email);
//	
//	List<Booking> getBookingsByStageId(Long stageId);
//	
//	List<Booking> getBookingsByStaffId(Long staffId);
}
