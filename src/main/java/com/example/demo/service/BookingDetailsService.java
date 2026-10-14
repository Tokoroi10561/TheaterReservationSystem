package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.BookingDetailsDto;
import com.example.demo.entity.Booking;

@Service
public interface BookingDetailsService {

	List<BookingDetailsDto> convertBookingDetailsDto(Booking booking);
}
