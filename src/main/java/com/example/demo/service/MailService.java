package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.dto.BookingDto;

@Service
public interface MailService {

	void sendBookingMail(BookingDto bookingDto, String uuid);
}
