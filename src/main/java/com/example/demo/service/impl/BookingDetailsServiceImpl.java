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
		List<BookingDetails> bookingDetailsList = booking.getBookingDetails();
		
		List<BookingDetailsDto> bookingDetailsDtoList = new ArrayList<>();
		for(BookingDetails bookingDetails: bookingDetailsList) {
			BookingDetailsDto bookingDetailsDto = BookingDetailsDto.fromEntity(bookingDetails);
			
			bookingDetailsDtoList.add(bookingDetailsDto);
		}
		return bookingDetailsDtoList;
	}
}
