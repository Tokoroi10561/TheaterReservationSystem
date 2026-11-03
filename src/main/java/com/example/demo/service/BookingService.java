package com.example.demo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.dto.BookingDto;
import com.example.demo.dto.BookingSearchCriteria;
import com.example.demo.entity.Booking;
import com.example.demo.entity.Show;
import com.example.demo.entity.TicketType;

@Service
public interface BookingService {
	
	String createBooking(BookingDto dto);
	String updateBooking(BookingDto dto);
	void deleteBooking(String token);

	void mapTicketTypeToDto(List<TicketType> ticketTypes, BookingDto dto);
	int calculateTicketSumPrice(BookingDto bookingDto);
	int calculateTicketSumPrice(Booking booking);
	int calculateSumStageTicket(List<Booking> bookings, int total);
	
	Booking getValidateByTokenAndStatus(String token);
	Booking getById(Long id);
//	
//	List<Booking> getAllActiveByShowId(Long showId, BookingStatus status);
	List<Long> calculateSumShowTicket(List<Show> shows);
	Page<Booking> searchBookings(Long showId, BookingSearchCriteria criteria, Pageable pageable);
}
