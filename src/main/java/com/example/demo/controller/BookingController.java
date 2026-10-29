package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.constant.BookingStatus;
import com.example.demo.constant.PaymentMethod;
import com.example.demo.constant.PaymentStatus;
import com.example.demo.dto.BookingDetailsDto;
import com.example.demo.dto.BookingDto;
import com.example.demo.dto.BookingSearchCriteria;
import com.example.demo.entity.Booking;
import com.example.demo.entity.Staff;
import com.example.demo.entity.Stage;
import com.example.demo.entity.TicketType;
import com.example.demo.exception.NotReservedException;
import com.example.demo.service.BookingDetailsService;
import com.example.demo.service.BookingService;
import com.example.demo.service.MailService;
import com.example.demo.service.StaffService;
import com.example.demo.service.StageService;
import com.example.demo.service.TicketTypeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/booking")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
	
	private final BookingService bookingService;
	private final BookingDetailsService bookingDetailsService;
	private final TicketTypeService ticketTypeService;
	private final StaffService staffService;
	private final StageService stageService;
	private final MailService mailService;
	
	@GetMapping("/form/{showId}")
	public String showForm(Model model, BookingDto bookingDto, @PathVariable Long showId) {
		log.info("フォームの提示");
		
		List<TicketType> ticketTypes = ticketTypeService.getAllTicketTypeByShowId(showId);
		
		BookingDto dto = new BookingDto();
		
		bookingService.mapTicketTypeToDto(ticketTypes, dto);
		
		model.addAttribute("showId", showId);
		model.addAttribute("bookingDto", dto);
		model.addAttribute("ticketTypes", ticketTypes);
		model.addAttribute("staffs", staffService.getAllStaffByShowId(showId));
		model.addAttribute("stages", stageService.getAllStageByShowId(showId));
		model.addAttribute("paymentMethods", PaymentMethod.values());
		
		return "booking/form";
	}
	
	@PostMapping("/confirm/{showId}")
	public String confirm(@Valid @ModelAttribute BookingDto bookingDto,
							  BindingResult bindingResult,
							  HttpSession httpSession,
							  Model model,
							  @PathVariable Long showId
							  ) {
		log.info("フォーム確認メソッド開始");
		model.addAttribute("showId", showId);
		List<TicketType> ticketTypes = ticketTypeService.getAllTicketTypeByShowId(showId);
		model.addAttribute("ticketTypes", ticketTypes);
		if(bindingResult.hasErrors()) {
			log.warn("バリデーションエラー");
			bindingResult.getAllErrors().forEach(error -> {
				System.out.println(error.getDefaultMessage());
				});
			
			
			
			BookingDto dto = new BookingDto();
			
			bookingService.mapTicketTypeToDto(ticketTypes, dto);
			
			model.addAttribute("bookingDto", dto);
			model.addAttribute("staffs", staffService.getAllStaffByShowId(showId));
			model.addAttribute("stages", stageService.getAllStageByShowId(showId));
			model.addAttribute("paymentMethod", PaymentMethod.values());
			
			
			return "booking/form";
		}
		
		httpSession.setAttribute("bookingDto", bookingDto);
		log.info("セッション保存完了");
		
		
		int sumPrice = bookingService.calculateTicketSumPrice(bookingDto);
		model.addAttribute("sumPrice", sumPrice);
		log.info("チケット合計金額: {}", sumPrice);
		
		Stage stage = stageService.stageExist(bookingDto.getStageId());
		Staff staff = staffService.getStaffById(bookingDto.getStaffId());
	
		model.addAttribute("bookingDto", bookingDto);
		model.addAttribute("stage", stage);
		model.addAttribute("staff", staff);
		
		log.info("確認画面投げる");
		return "booking/confirm";
	}
	
	@PostMapping("/register/{showId}")
	public String register(HttpSession httpSession,
							   Model model,
							   @PathVariable Long showId,
							   RedirectAttributes redirectAttributes
							   ) {
		log.info("フォーム登録メソッド開始");

		model.addAttribute("showId", showId);
		
		BookingDto bookingDto = (BookingDto) httpSession.getAttribute("bookingDto");
		if(bookingDto == null) {
			log.warn("バリデーションエラー");
			
			List<TicketType> ticketTypes = ticketTypeService.getAllTicketTypeByShowId(showId);
			
			BookingDto dto = new BookingDto();
			
			bookingService.mapTicketTypeToDto(ticketTypes, dto);
			
			model.addAttribute("bookingDto", dto);
			model.addAttribute("ticketTypes", ticketTypes);
			model.addAttribute("staffs", staffService.getAllStaffByShowId(showId));
			model.addAttribute("stages", stageService.getAllStageByShowId(showId));
			model.addAttribute("paymentMethod", PaymentMethod.values());
			
			return "redirect:/booking/form/" + showId;
		}
		String uuid = bookingService.createBooking(bookingDto);
		log.info("データの保存");
		
		httpSession.removeAttribute("bookingDto");
		log.info("セッションの削除");
		
		mailService.sendBookingMail(bookingDto, uuid);
		log.info("メールの送信");
		return "redirect:/booking/complete" + showId;
	}
	
	@GetMapping("/complete/{showId}")
	public String complete(@PathVariable Long showId, Model model) {
		model.addAttribute("showId", showId);
		log.info("フォーム完了");
		return "booking/complete";
	}
	
	@GetMapping("/manage")
	public String choseChange(@RequestParam String token, Model model, RedirectAttributes redirectAttributes) {
		log.info("更新前状態表示メソッド開始");
		Booking booking = bookingService.getValidateByTokenAndStatus(token);
		
		BookingStatus status = booking.getBookingStatus();
			//現状の予約表示
		if(status != BookingStatus.RESERVED) {
			log.warn("予約されていないため表示ができません");
			throw new NotReservedException("予約されていません");
		}
			model.addAttribute("booking", booking);
			
			int sumPrice = bookingService.calculateTicketSumPrice(booking);
			model.addAttribute("sumPrice", sumPrice);
		
		log.info("更新前状態表示ページを投げる");
		return "booking/manage";
		
	}
	
	@GetMapping("/update/form/{id}")
	public String showUpdateForm(@PathVariable("id") Long bookingId, Model model) {
		log.info("編集フォーム提示メソッド開始");
		Booking booking = bookingService.getById(bookingId);
		
		Long showId = booking.getStage().getShow().getId();
		
		List<TicketType> ticketTypes = ticketTypeService.getAllTicketTypeByShowId(showId);
		
		BookingDto dto = BookingDto.fromEntity(booking);
		
		List<BookingDetailsDto> detailsDto = bookingDetailsService.convertBookingDetailsDto(booking);
		dto.setBookingDetails(detailsDto);
		
//		bookingService.mapTicketTypeToDto(ticketTypes, dto);
		
		model.addAttribute("bookingDto", dto);
		model.addAttribute("ticketTypes", ticketTypes);
		model.addAttribute("staffs", staffService.getAllStaffByShowId(showId));
		model.addAttribute("stages", stageService.getAllStageByShowId(showId));
		model.addAttribute("paymentMethods", PaymentMethod.values());
		
		log.info("編集フォーム提示ページを投げる");
		return "booking/update/form";
	}
	
	@PostMapping("/update/confirm/{id}")
	public String confirmUpdate(@Valid @ModelAttribute BookingDto bookingDto,
			  BindingResult bindingResult,
			  HttpSession httpSession,
			  Model model,
			  @PathVariable("id") Long bookingId
			  ) {
		log.info("編集フォーム確認メソッド開始");
		
		Booking booking = bookingService.getById(bookingId);
		Long showId = booking.getStage().getShow().getId();
		
		List<TicketType> ticketTypes = ticketTypeService.getAllTicketTypeByShowId(showId);
		model.addAttribute("ticketTypes", ticketTypes);
		if(bindingResult.hasErrors()) {
			log.warn("バリデーションエラー");
			
			
			bookingService.mapTicketTypeToDto(ticketTypes, bookingDto);
			
			model.addAttribute("bookingDto", bookingDto);
			model.addAttribute("staffs", staffService.getAllStaffByShowId(showId));
			model.addAttribute("stages", stageService.getAllStageByShowId(showId));
			model.addAttribute("paymentMethods", PaymentMethod.values());
			
			log.info("編集フォーム確認ページ投げる");
			return "booking/update/form";
		}
		
		httpSession.setAttribute("bookingDto", bookingDto);
		log.info("セッション保存");
		
		int sumPrice = bookingService.calculateTicketSumPrice(bookingDto);
		log.info("チケット合計金額: {}", sumPrice);
		model.addAttribute("sumPrice", sumPrice);
		
		Stage stage = stageService.stageExist(bookingDto.getStageId());
		Staff staff = staffService.getStaffById(bookingDto.getStaffId());
		
		model.addAttribute("bookingDto", bookingDto);
		model.addAttribute("stage", stage);
		model.addAttribute("staff", staff);
		log.info("編集フォーム確認ページを投げる");
		
		return "booking/update/confirm";
	}
	
	@PostMapping("/update/register/{id}")
	public String registerUpdate(HttpSession httpSession,
							   Model model,
							   @PathVariable("id") Long bookingId,
							   RedirectAttributes redirectAttributes
							   ) {
		log.info("編集フォーム更新メソッド開始");
		BookingDto bookingDto = (BookingDto) httpSession.getAttribute("bookingDto");
		
		String uuid = bookingService.updateBooking(bookingDto);
		log.info("編集データ保存");
		
		httpSession.removeAttribute("bookingDto");
		log.info("セッション削除");
		
		mailService.sendBookingMail(bookingDto, uuid);
		log.info("メール送信");
		
		Long showId = staffService.getStaffById(bookingDto.getStaffId()).getShow().getId();
		
		return "redirect:/booking/complete/" + showId;
	}
	
	@PostMapping("/delete/customer/{id}")
	public String registerCustomerDelete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		log.info("削除メソッド開始");
		Booking booking = bookingService.getById(id);
		
		String token = booking.getToken();
		
		bookingService.deleteBooking(token);
		log.info("{}様のデータ削除。token: {}", booking.getName(), token);
		
		return "redirect:/booking/delete/customer/complete";
	}
	
	@GetMapping("/delete/customer/complete")
	public String completeDelete(Model model) {
		log.info("削除完了");
		return "booking/delete/complete";
	}
	
	@PostMapping("/delete/{id}")
	public String registerDelete(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
		model.addAttribute("id", id);
		
		Booking booking = bookingService.getById(id);
		Long showId = booking.getStage().getShow().getId();
		bookingService.deleteBooking(booking.getToken());
		
		log.info("{}様のデータを削除。予約ID: {}", booking.getName(), booking.getId());
		
		return "redirect:/booking/list/" + showId;
	}
	
	@GetMapping({"/list/{showId}", "/search/{showId}"})
	public String bookingList(BookingSearchCriteria criteria,
							  @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC)Pageable pageable,
							  Model model,
							  @PathVariable Long showId) {
		String sortBy = criteria.sortBy() != null ? criteria.sortBy() : "";
		
		Sort sort = switch(sortBy) {
			case "stage" ->
				Sort.by("stage.startTime").ascending();
			default ->
				Sort.by(Sort.Direction.DESC,"id");
		};
		
		Pageable customPageable = PageRequest.of(
				pageable.getPageNumber(),
				pageable.getPageSize(),
				sort
				);
		
		Page<Booking> bookingPage = bookingService.searchBookings(showId, criteria, customPageable);
		
		List<TicketType> ticketTypes = ticketTypeService.getAllTicketTypeByShowId(showId);
		
		model.addAttribute("showId", showId);
		model.addAttribute("bookingPage", bookingPage);
		model.addAttribute("bookings", bookingPage.getContent());
		model.addAttribute("allBookingStatus", BookingStatus.values());
		model.addAttribute("allPaymentStatus", PaymentStatus.values());
//		model.addAttribute("paymentMethods", PaymentMethod.values());
		model.addAttribute("criteria", criteria);
		model.addAttribute("ticketTypes", ticketTypes);
		model.addAttribute("staffs", staffService.getAllStaffByShowId(showId));
		model.addAttribute("stages", stageService.getAllStageByShowId(showId));
		
		return "booking/list";
	}
	
	@GetMapping("/detail/{bookingId}")
	public String bookingDetail(@PathVariable Long bookingId, Model model){
		Booking booking = bookingService.getById(bookingId);
		
		model.addAttribute("booking", booking);
		//公演の名前詰めたほうが良いかな
		//これBookingDetailsを詰める必要があるのか？
		
		return "booking/detail";
	}
}
