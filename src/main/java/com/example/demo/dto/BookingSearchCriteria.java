package com.example.demo.dto;

import com.example.demo.constant.BookingStatus;
import com.example.demo.constant.PaymentStatus;

public record BookingSearchCriteria (
	String keyword,
	Long stageId,
	Long ticketTypeId,
	Long staffId,
	BookingStatus bookingStatus,
	PaymentStatus paymentStatus,
	String sortBy
	) {}
