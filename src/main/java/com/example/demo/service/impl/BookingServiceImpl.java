package com.example.demo.service.impl;

import com.example.demo.entity.Booking;
import com.example.demo.service.BookingService;

public class BookingServiceImpl implements BookingService {
	
	@Override
	public void createBooking(Booking booking) {}
	
	@Override
	public void updateBooking(Booking booking) {}
	
	@Override
	public void deleteBooking(Long Id) {}
	
	@Override
	public List<Booking> getAllBookings() {}
	
	@Override
	public List<Booking> getBookingsById(Long Id) {}
	
	@Override
	public List<Booking> getBookingsByName(String name) {}
	
	@Override
	public List<Booking> getBookingsByEmail(String email) {}
	
	@Override
	public List<Booking> getBookingsByStageId(Long stageId) {}
	
	@Override
	public Optional<Booking> getBookingsByToken(String token) {}
	
	@Override
	public List<Booking> getBookingsByStaffId(Long staffId) {}	
}
