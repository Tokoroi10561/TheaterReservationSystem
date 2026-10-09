package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.BookingDto;
import com.example.demo.entity.Booking;
import com.example.demo.entity.TicketType;

@Service
public interface BookingService {
	
	String createBooking(BookingDto dto);
	
	String updateBooking(BookingDto dto, String token);
	
	void deleteBooking(String token);
	
	int calculateTicketSumPrice(BookingDto bookingDto);
	
	void mapTicketTypeToDto(List<TicketType> ticketTypes, BookingDto dto);
	
	Booking getValidateByTokenAndStatus(String token);
	
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
