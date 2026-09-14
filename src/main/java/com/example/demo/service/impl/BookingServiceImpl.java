package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.constant.BookingStatus;
import com.example.demo.dto.BookingDto;
import com.example.demo.entity.Booking;
import com.example.demo.entity.Stage;
import com.example.demo.exception.NoDataFoundException;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.StageRepository;
import com.example.demo.service.BookingService;

public class BookingServiceImpl implements BookingService {
	
	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private StageRepository stageRepository;
	
	@Override
	public void createBooking(BookingDto bookingDto) {
		//予約しようとしているstageIdのステージがデータベースに存在するかを確認
		//無ければNoDataFoundExceptionを投げる
		Long stageId = bookingDto.getStageId();
		
		//そのステージの座席上限と現在の予約済みの総数をデータベースから計算して比較
		//満席であれば自作したStageSoldOutExceptionを投げる
		
		Stage stage = stageRepository.findById(stageId).orElseThrow(()
				-> new NoDataFoundException("ステージが存在しません" + stageId)
				);
		
		int capacity = stage.getCapacity();
		
		List<Booking> bookings = bookingRepository.findByStageId(stageId, BookingStatus.RESERVED);
		
		for(Booking booking: bookings) {
			
		}
		//チェックをクリアしたら、javaのUUID機能を使ってその予約のためだけのランダム文字列を生成
		
		//データベースへのセット親(ステータスやトークン)
		//データベースへのセット子(チケットタイプと枚数の内訳)
		//BookingRepository.save()
//		bookingRepository.save();
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
