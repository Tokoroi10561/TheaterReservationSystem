package com.example.demo.service;

import com.example.demo.dto.BookingDto;

public interface BookingService {
	
	void createBooking(BookingDto bookingDto);
	
//	void updateBooking(BookingDto bookingDto);
	
	void deleteBooking(String token);
	
//	List<Booking> getAllBookings();
//	
//	List<Booking> getBookingsById(Long Id);
//	
//	List<Booking> getBookingsByName(String name);
//	
//	List<Booking> getBookingsByEmail(String email);
//	
//	List<Booking> getBookingsByStageId(Long stageId);
//	
//	Optional<Booking> getBookingsByToken(String token);
//	
//	List<Booking> getBookingsByStaffId(Long staffId);
}
