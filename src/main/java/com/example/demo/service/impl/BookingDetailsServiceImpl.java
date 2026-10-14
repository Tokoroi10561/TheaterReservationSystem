package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.BookingDetailsDto;
import com.example.demo.entity.Booking;
import com.example.demo.entity.BookingDetails;
import com.example.demo.service.BookingDetailsService;

@Service
public class BookingDetailsServiceImpl implements BookingDetailsService {

	public List<BookingDetailsDto> convertBookingDetailsDto(Booking booking) {
		List<BookingDetails> bookingDetails = booking.getBookingDetails();
		
		List<BookingDetailsDto> bookingDetailsDto = new ArrayList<>();
		for(BookingDetails bookingDetail: bookingDetails) {
			BookingDetailsDto dto = BookingDetailsDto.fromEntity(bookingDetail);
			
			bookingDetailsDto.add(dto);
		}
		return bookingDetailsDto;
	}
}
