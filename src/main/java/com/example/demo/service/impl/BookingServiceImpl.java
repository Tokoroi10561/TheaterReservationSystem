package com.example.demo.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.example.demo.constant.BookingStatus;
import com.example.demo.dto.BookingDetailsDto;
import com.example.demo.dto.BookingDto;
import com.example.demo.entity.Booking;
import com.example.demo.entity.BookingDetails;
import com.example.demo.entity.Staff;
import com.example.demo.entity.Stage;
import com.example.demo.entity.TicketType;
import com.example.demo.exception.BookingClosedException;
import com.example.demo.exception.NoDataFoundException;
import com.example.demo.exception.StageSoldOutException;
import com.example.demo.repository.BookingRepository;
import com.example.demo.service.BookingService;
import com.example.demo.service.StaffService;
import com.example.demo.service.StageService;
import com.example.demo.service.TicketTypeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
	
	private final BookingRepository bookingRepository;
	private final StageService stageService;
	private final TicketTypeService ticketTypeService;
	private final StaffService staffService;
	
	@Transactional
	@Override
	public String createBooking(BookingDto dto) {
		log.info("予約処理を開始します。");
		
		Long stageId = dto.getStageId();
		
		Stage stage = stageService.stageExist(stageId);
		int capacity = stage.getCapacity();
		
		
		List<BookingDetailsDto> bookingDetailsDtos = dto.getBookingDetails();
		int demandTotal = 0;
		demandTotal = calculateSumDtoTicket(bookingDetailsDtos, demandTotal);
		log.info("注文したチケット数の総数: {}", demandTotal);
		
		int total = 0;
		List<Booking> bookings = bookingRepository.findByStageId(stageId, BookingStatus.RESERVED);
		total = calculateSumStageTicket(bookings, total);
		log.info("すでに注文済みのチケット合計: {}", total);
		
		int all = demandTotal + total;
		log.info("注文したチケットと注文済みの合計: {}", all);
		
		if(all > capacity) {
			log.warn("ステージが満席のため予約に失敗しました。stageId: {}, 要求数: {}, 定員: {}",
					stageId, demandTotal, capacity);
			throw new StageSoldOutException("ステージは満席です" + stageId);
		}
		
		Long staffId = dto.getStaffId();
		
		Staff staff;
		
		if(staffId == null){
			staff = null;
		}else {
			staff = staffService.getStaffById(staffId);
			log.info("スタッフ: {}", staff.getName());
		}
		
		String token = UUID.randomUUID().toString();
		
		Booking booking = dto.toEntity();
		
		
		ArrayList<BookingDetails> details = convertDtoToEntity(bookingDetailsDtos, booking);
		
		booking.setToken(token);
		booking.setBookingStatus(BookingStatus.RESERVED);
		booking.setBookingDetails(details);
		booking.setStage(stage);
		booking.setStaff(staff);
		
		bookingRepository.save(booking);
		log.info("{}様の予約が完了しました。 予約トークン: {}", dto.getName(), token);
		
		stageService.changeStageStatus(stage, all, booking);
		
		return token;
	}
	
	@Transactional
	@Override
	public String updateBooking(BookingDto dto) {
		log.info("予約の更新処理を開始します。");
		
		Booking booking = getById(dto.getId());
		String token = booking.getToken();
		int total = 0;
		checkDeadline(booking);
		
		booking.setName(dto.getName());
		booking.setPhone(dto.getPhone());
		booking.setAddress(dto.getAddress());	
		booking.setRemarks(dto.getRemarks());
		
		Long oldStageId = booking.getStage().getId();
		Long newStageId = dto.getStageId();
		
		List<BookingDetails> details = booking.getBookingDetails();
		List<BookingDetailsDto> bookingDetailsDtos = dto.getBookingDetails();
		
		if(oldStageId.equals(newStageId)) {
			log.info("ステージは変更されていません。 stageId: {}", newStageId);
			Stage stage = stageService.stageExist(newStageId);
			int capacity = stage.getCapacity();
			
			int oldTotal = 0;
			for(BookingDetails bookingDetails: details) {
				oldTotal += bookingDetails.getQuantity();
			}
			log.info("元予約数のチケット合計: {}", oldTotal);
			
			List<Booking> bookings = bookingRepository.findByStageId(newStageId, BookingStatus.RESERVED);
			total = calculateSumStageTicket(bookings, total);
			log.info("ステージ内チケット合計: {}", total);
			
			int zero = 0;
			int newTotal = calculateSumDtoTicket(bookingDetailsDtos, zero);
			log.info("新しいチケット合計: {}", newTotal);
			
			int all = (total - oldTotal) + newTotal;
			
			if(all > capacity) {
				log.warn("ステージが満席のため予約に失敗しました。 stageId: {}, 要求数: {}, 定員: {}",
						newStageId, newTotal, capacity);
				throw new StageSoldOutException("ステージは満席です" + newStageId);
			}
			
			for(int i = 0; i < details.size(); i++) {
				details.get(i).setQuantity(bookingDetailsDtos.get(i).getQuantity());
			}
			
			details.addAll(details);
			
			stageService.changeStageStatus(stage, all, booking);

			bookingRepository.save(booking);
		}else {
			log.info("ステージが変更されました。 old→new: {}→{}", oldStageId, newStageId);
			Stage stage = stageService.stageExist(newStageId);
			int capacity = stage.getCapacity();
			int demandTotal = 0;
			
			demandTotal = calculateSumDtoTicket(bookingDetailsDtos, demandTotal);
			log.info("新しいチケット合計: {}", total);
			
			List<Booking> bookings = bookingRepository.findByStageId(newStageId, BookingStatus.RESERVED);
			total = calculateSumStageTicket(bookings, total);
			log.info("すでに注文済みのチケット合計: {}", total);
			
			int all = demandTotal + total;
			log.info("注文したチケットと注文済みの合計: {}", all);
			
			if(all > capacity) {
				log.warn("ステージが満席のため予約に失敗しました。 stageId: {}, 要求数: {}, 定員: {}",
						newStageId, demandTotal, capacity);
				throw new StageSoldOutException("ステージは満席です" + newStageId);
			}

			for(int i = 0; i < details.size(); i++) {
				details.get(i).setQuantity(bookingDetailsDtos.get(i).getQuantity());
			}
				
			booking.setStage(stage);
			log.info("ステージのセット");
			
			details.addAll(details);
			
			stageService.changeStageStatus(stage, all, booking);
			
			bookingRepository.save(booking);
		}

		log.info("{}様の予約が完了しました。 予約トークン: {}", dto.getName(), token);
		return token;
	}
	
	@Transactional
	@Override
	public void deleteBooking(String token) {
		log.info("予約削除を開始します。");
		Booking booking = getValidateByTokenAndStatus(token);
		
		checkDeadline(booking);
		
		booking.setBookingStatus(BookingStatus.CANCELLED);
		bookingRepository.save(booking);
		
		Long stageId = booking.getStage().getId();
		Stage stage = stageService.stageExist(stageId);
		
		List<Booking> bookings = bookingRepository.findByStageId(stageId, BookingStatus.RESERVED);
		
		int total = 0;
		total = calculateSumStageTicket(bookings, total);
		
		stageService.changeStageStatus(stage, total, booking);
		
		log.info("{}様の予約を削除しました。", booking.getName());
	}
	
	@Override
	public int calculateTicketSumPrice(BookingDto bookingDto) {
		log.info("チケット合計金額計算メソッドを開始します。");
		int sum = 0;
		
		List<BookingDetailsDto> bookingDetails = bookingDto.getBookingDetails();
		
		for(BookingDetailsDto bookingDetail: bookingDetails) {
			int quantity = bookingDetail.getQuantity();
			
			Long ticketId = bookingDetail.getTicketTypeId();
			
			TicketType ticketType = ticketTypeService.getById(ticketId);
			int price = ticketType.getPrice();
			
			int total = quantity * price;
			
			sum += total;
		}
		log.info("合計金額: {}", sum);
		return sum;
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
	
	private void checkDeadline(Booking entity) {
		LocalDateTime now = LocalDateTime.now();
		
		LocalDateTime stageTime = entity.getStage().getStartTime();
		LocalDate stageDay = stageTime.toLocalDate();
		LocalDateTime deadline = stageDay.minusDays(1).atTime(23, 59);
		
		if(now.isAfter(deadline)) {
			log.warn("ステージの予約期限を過ぎているため編集キャンセルはできません。"
					+ "期限: {}", deadline);
			throw new BookingClosedException("当日の編集・キャンセルはできません");
		}
	}
	
	@Override
	public Booking getValidateByTokenAndStatus(String token) {
		Booking booking = bookingRepository.findByToken(token).orElseThrow(() ->
			new NoDataFoundException("トークンが存在しません" + token)
		);
		
		if(booking.getBookingStatus() != BookingStatus.RESERVED) {
			log.warn("予約はすでにキャンセルされています。");
			throw new NoDataFoundException(booking.getName() + "様の予約はすでにキャンセルされています");
		}
		return booking;
	}
	
	private ArrayList<BookingDetails> convertDtoToEntity (List<BookingDetailsDto> bookingDetailsDtos, Booking booking) {
		log.info("DetailをDtoからEntityに変更するメソッドを開始します。");
		ArrayList<BookingDetails> details = new ArrayList<>();
		
		for(BookingDetailsDto bookingDetailsDto: bookingDetailsDtos) {
			Long ticketTypeId = bookingDetailsDto.getTicketTypeId();
			BookingDetails detail = bookingDetailsDto.toEntity();
			
			TicketType ticketType = ticketTypeService.getById(ticketTypeId);
			
			detail.setTicketType(ticketType);
			detail.setBooking(booking);
			details.add(detail);;
		}
		
		return details;
	}
	
	@Override
	public void mapTicketTypeToDto(List<TicketType> ticketTypes, BookingDto dto) {
		
		List<BookingDetailsDto> details = ticketTypes.stream()
				.map(tt -> {
					BookingDetailsDto d = new BookingDetailsDto();
					
					d.setTicketTypeId(tt.getId());
					d.setQuantity(0);
					return d;
				})
				.toList();
		dto.setBookingDetails(details);
	}
	
	@Override
	public Booking getById(Long id){
		return bookingRepository.findById(id).orElseThrow(() ->
		new NoDataFoundException("idが見つかりません")
		);
	}
	
	
}
