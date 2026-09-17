package com.example.demo.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import com.example.demo.exception.BookingClosedException;
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
	public void createBooking(BookingDto dto) {
		Long stageId = dto.getStageId();
		
		Stage stage = stageExist(stageId);
		int capacity = stage.getCapacity();
		
		List<BookingDetailsDto> bookingDetailsDtos = dto.getBookingDetailsDto();
		int total = 0;
		total = calculateSumDtoTicket(bookingDetailsDtos, total);
		
		List<Booking> bookings = bookingRepository.findByStageId(stageId, BookingStatus.RESERVED);
		total = calculateSumStageTicket(bookings, total);
		
		if(total > capacity) {
			throw new StageSoldOutException("ステージは満席です" + stageId);
		}
		
		String uuid = UUID.randomUUID().toString();
		
		Booking booking = dto.toEntity();
		
		ArrayList<BookingDetails> details = convertDtoToDetails(bookingDetailsDtos, booking);
		
		booking.setToken(uuid);
		booking.setBookingStatus(BookingStatus.RESERVED);
		booking.setBookingDetails(details);
		
		bookingRepository.save(booking);
	}
	
	@Override
	public void updateBooking(BookingDto dto, String token) {
		Booking booking = getValidateByTokenAndStatus(token);
		int total = 0;
		
		checkDeadline(booking);
		
		//新旧のステージID確認
		Long oldStageId = booking.getStage().getId();
		Long newStageId = dto.getStageId();
		
		List<BookingDetails> bookingDetailses = booking.getBookingDetails();
		List<BookingDetailsDto> bookingDetailsDtos = dto.getBookingDetailsDto();
		
		if(oldStageId == newStageId) {
			//ステージを変更しない場合: 二重カウント防止の引き算を入れた残席チェック
			Stage stage = stageExist(newStageId);
			int capacity = stage.getCapacity();
			
			//entityの予約枚数合計を出す
			int totalEntity = 0;
			for(BookingDetails bookingDetails: bookingDetailses) {
				totalEntity += bookingDetails.getQuantity();
			}
			
			//stageの予約枚数の合計を出す
			List<Booking> bookings = bookingRepository.findByStageId(newStageId, BookingStatus.RESERVED);
			total = calculateSumStageTicket(bookings, total);
			
			//dtoの予約枚数を計算する
			int zero = 0;
			int totalDto = calculateSumDtoTicket(bookingDetailsDtos, zero);
			
			
			//entityの予約枚数を合計からマイナスした値と
			if((total - totalEntity) + totalDto > capacity) {
				throw new StageSoldOutException("ステージは満席です" + newStageId);
			}
			
			//ステージ変更しない場合の保存＝detailだけ変更すればよい
			
			ArrayList<BookingDetails> details = convertDtoToDetails(bookingDetailsDtos, booking);
			
			bookingDetailses.clear();
			bookingDetailses.addAll(details);
			
			bookingRepository.save(booking);
			
		}else {
			//ステージを変更する場合
			Stage stage = stageExist(newStageId);
			int capacity = stage.getCapacity();
			
			total = calculateSumDtoTicket(bookingDetailsDtos, total);
			
			List<Booking> bookings = bookingRepository.findByStageId(newStageId, BookingStatus.RESERVED);
			total = calculateSumStageTicket(bookings, total);
			
			if(total > capacity) {
				throw new StageSoldOutException("ステージは満席です" + newStageId);
			}
			
			ArrayList<BookingDetails> details = convertDtoToDetails(bookingDetailsDtos, booking);
			
			bookingDetailses.clear();
			bookingDetailses.addAll(details);
			
			booking.setStage(stage);
			booking.setBookingDetails(details);
			bookingRepository.save(booking);
		}
	}
	
	@Override
	public void deleteBooking(String token) {
		Booking booking = getValidateByTokenAndStatus(token);
		
		checkDeadline(booking);
		
		booking.setBookingStatus(BookingStatus.CANCELLED);
		bookingRepository.save(booking);
	}
	
//	@Override
//	public List<Booking> getAllBookings() {}
	
//	@Override
//	public List<Booking> getBookingsByName(String name) {}
//	
//	@Override
//	public List<Booking> getBookingsByEmail(String email) {}
//	
//	@Override
//	public List<Booking> getBookingsByStageId(Long stageId) {}
	
//	@Override
//	public List<Booking> getBookingsByStaffId(Long staffId) {}
	
	private Stage stageExist(Long stageId) {
		Stage stage = stageRepository.findById(stageId).orElseThrow(() -> 
		new NoDataFoundException("ステージが存在しません" + stageId)
		);
		return stage;
	}
	
	//総予約枚数を計算するメソッド
	private int calculateSumDtoTicket
						(List<BookingDetailsDto> bookingDetailsDtos, int total) {
		for(BookingDetailsDto bookingDetailsDto: bookingDetailsDtos) {
			total += bookingDetailsDto.getQuantity();
		}
		return total;
	}
	
	private int calculateSumStageTicket(List<Booking> bookings, int total) {
		for(Booking booking: bookings) {
			List<BookingDetails> bookingDetails = booking.getBookingDetails();
			
			for(BookingDetails bookingDetail: bookingDetails) {
				int quantity = bookingDetail.getQuantity();
				
				total += quantity;
			}
		}
		return total;
	}
	
	//キャンセル・変更の期限を確認するメソッド
	private void checkDeadline(Booking entity) {
		LocalDateTime now = LocalDateTime.now();
		
		LocalDateTime stageTime = entity.getStage().getStartTime();
		LocalDate stageDay = stageTime.toLocalDate();
		LocalDateTime deadline = stageDay.minusDays(1).atTime(23, 59);
		
		if(now.isAfter(deadline)) {
			throw new BookingClosedException("当日のキャンセルはできません");
		}
	}
	
	
	private Booking getValidateByTokenAndStatus(String token) {
		Booking booking = bookingRepository.findByToken(token).orElseThrow(() ->
			new NoDataFoundException("トークンが存在しません" + token)
		);
		
		if(booking.getBookingStatus() != BookingStatus.RESERVED) {
			throw new NoDataFoundException(booking.getName() + "様の予約はすでにキャンセルされています");
		}
		return booking;
	}
	
	private ArrayList<BookingDetails> convertDtoToDetails (List<BookingDetailsDto> bookingDetailsDtos, Booking booking) {
		ArrayList<BookingDetails> details = new ArrayList<>();
		
		for(BookingDetailsDto bookingDetailsDto: bookingDetailsDtos) {
			Long ticketTypeId = bookingDetailsDto.getTicketTypeId();
			BookingDetails detail = bookingDetailsDto.toEntity();
			
			TicketType ticketType = ticketTypeRepository.findById(ticketTypeId).orElseThrow(() -> 
				new NoDataFoundException("チケットタイプが存在しません" + ticketTypeId)
			);
			detail.setTicketType(ticketType);
			detail.setBooking(booking);
			details.add(detail);
		}
		
		return details;
	}
}
