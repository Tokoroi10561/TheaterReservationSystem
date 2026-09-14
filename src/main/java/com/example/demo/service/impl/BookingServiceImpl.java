package com.example.demo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.constant.BookingStatus;
import com.example.demo.dto.BookingDetailsDto;
import com.example.demo.dto.BookingDto;
import com.example.demo.entity.Booking;
import com.example.demo.entity.BookingDetails;
import com.example.demo.entity.Stage;
import com.example.demo.entity.TicketType;
import com.example.demo.exception.NoDataFoundException;
import com.example.demo.exception.StageSoldOutException;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.StageRepository;
import com.example.demo.repository.TicketTypeRepository;
import com.example.demo.service.BookingService;

public class BookingServiceImpl implements BookingService {
	
	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private StageRepository stageRepository;
	
	@Autowired
	private TicketTypeRepository ticketTypeRepository;
	
	@Override
	public void createBooking(BookingDto bookingDto) {
		Long stageId = bookingDto.getStageId();
		
		Stage stage = stageRepository.findById(stageId).orElseThrow(() -> 
			new NoDataFoundException("ステージが存在しません" + stageId)
		);
		
		int capacity = stage.getCapacity();
		
		List<Booking> bookings = bookingRepository.findByStageId(stageId, BookingStatus.RESERVED);
		
		List<BookingDetailsDto> bookingDetailsDtos = bookingDto.getBookingDetailsDto();
		
		int total = 0;
		
		for(BookingDetailsDto bookingDetailsDto: bookingDetailsDtos) {
			total += bookingDetailsDto.getQuantity();
		}
		
		
		for(Booking booking: bookings) {
			List<BookingDetails> bookingDetails = booking.getBookingDetails();
			
			for(BookingDetails bookingDetail: bookingDetails) {
				int quantity = bookingDetail.getQuantity();
				
				total += quantity;
			}
		}
		
		if(total > capacity) {
			throw new StageSoldOutException("ステージは満席です" + stageId);
		}
		
		//チェックをクリアしたら、javaのUUID機能を使ってその予約のためだけのランダム文字列を生成
		String uuid = UUID.randomUUID().toString();
		
		//データベースへのセット親(ステータスやトークン)
		Booking booking = bookingDto.toEntity();
		booking.setToken(uuid);
		booking.setBookingStatus(BookingStatus.RESERVED);
		
		ArrayList<BookingDetails> details = new ArrayList<>();
		//データベースへのセット子(チケットタイプと枚数の内訳)
		for(BookingDetailsDto bookingDetailsDto: bookingDetailsDtos) {
			Long ticketTypeId = bookingDetailsDto.getTicketTypeId();
			
			BookingDetails detail = bookingDetailsDto.toEntity();
			//チケットタイプからチケットオブジェクトにして、BookingDetailsに代入
			TicketType ticketType = ticketTypeRepository.findById(ticketTypeId).orElseThrow(() -> 
			new NoDataFoundException("ステージが存在しません" + ticketTypeId)
			);
			details.add(ticketType);
			details.add(detail);
			detail.setBooking(booking);
		}
		
		booking.setBookingDetails(details);
		
		bookingRepository.save(booking);
	}
//	
//	@Override
//	public void updateBooking(Booking booking) {}
//	
//	@Override
//	public void deleteBooking(Long Id) {}
//	
//	@Override
//	public List<Booking> getAllBookings() {}
//	
//	@Override
//	public List<Booking> getBookingsById(Long Id) {}
//	
//	@Override
//	public List<Booking> getBookingsByName(String name) {}
//	
//	@Override
//	public List<Booking> getBookingsByEmail(String email) {}
//	
//	@Override
//	public List<Booking> getBookingsByStageId(Long stageId) {}
//	
//	@Override
//	public Optional<Booking> getBookingsByToken(String token) {}
//	
//	@Override
//	public List<Booking> getBookingsByStaffId(Long staffId) {}	
}
