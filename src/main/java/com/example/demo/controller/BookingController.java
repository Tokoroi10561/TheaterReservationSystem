package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

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
import com.example.demo.dto.BookingDetailsDto;
import com.example.demo.dto.BookingDto;
import com.example.demo.entity.Booking;
import com.example.demo.entity.TicketType;
import com.example.demo.exception.NotReservedException;
import com.example.demo.service.BookingDetailsService;
import com.example.demo.service.BookingService;
import com.example.demo.service.MailService;
import com.example.demo.service.StaffService;
import com.example.demo.service.StageService;
import com.example.demo.service.TicketTypeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {
	
	private final BookingService bookingService;
	private final BookingDetailsService bookingDetailsService;
	private final TicketTypeService ticketTypeService;
	private final StaffService staffService;
	private final StageService stageService;
	private final MailService mailService;
	
	//フォーム表示メソッド
	@GetMapping("/form/{showId}")
	public String showForm(Model model, BookingDto bookingDto, @PathVariable Long showId) {
		
		List<TicketType> ticketTypes = ticketTypeService.getAllTicketTypeByShowId(showId);
		
		BookingDto dto = new BookingDto();
		
		bookingService.mapTicketTypeToDto(ticketTypes, dto);
		
		model.addAttribute("bookingDto", dto);
		model.addAttribute("ticketTypes", ticketTypes);
		model.addAttribute("staffs", staffService.getAllStaffByShowId(showId));
		model.addAttribute("stages", stageService.getAllStageByShowId(showId));
		model.addAttribute("paymentMethods", PaymentMethod.values());
		
		return "booking/form";
	}
	
	//フォーム確認メソッド
	@PostMapping("/form/{showId}/confirm")
	public String confirmForm(@Valid @ModelAttribute BookingDto bookingDto,
							  BindingResult bindingResult,
							  HttpSession httpSession,
							  Model model,
							  @PathVariable Long showId
							  ) {
		System.out.println("①confirmForm開始");
		//バリデーション
		if(bindingResult.hasErrors()) {

			System.out.println("②バリデーションエラー");
			
			bindingResult.getAllErrors().forEach(error -> {
				System.out.println(error.getDefaultMessage());
				});
			
			List<TicketType> ticketTypes = ticketTypeService.getAllTicketTypeByShowId(showId);
			
			BookingDto dto = new BookingDto();
			
			bookingService.mapTicketTypeToDto(ticketTypes, dto);
			
			model.addAttribute("bookingDto", dto);
			model.addAttribute("ticketTypes", ticketTypes);
			model.addAttribute("staffs", staffService.getAllStaffByShowId(showId));
			model.addAttribute("stages", stageService.getAllStageByShowId(showId));
			model.addAttribute("paymentMethod", PaymentMethod.values());
			
			
			return "booking/form";
		}
		//セッション保存
		httpSession.setAttribute("bookingDto", bookingDto);
		System.out.println("③セッション保存完了");
		
		//値段計算メソッドを渡す
		int sumPrice = bookingService.calculateTicketSumPrice(bookingDto);
		model.addAttribute("sumPrice", sumPrice);
	
		model.addAttribute("bookingDto", bookingDto);
		
		System.out.println("④confirm画面へ");
		return "booking/confirm";
	}
	
	//フォーム登録メソッド
	@PostMapping("/form/{showId}/register")
	public String registerForm(HttpSession httpSession,
							   Model model,
							   @PathVariable Long showId,
							   RedirectAttributes redirectAttributes
							   ) {
		BookingDto bookingDto = (BookingDto) httpSession.getAttribute("bookingDto");
		if(bookingDto == null) {
			
			System.out.println("⑤バリデーションエラー(bookingDtoがnull)");
			
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
		System.out.println("⑥BookingDtoの作成");
		String uuid = bookingService.createBooking(bookingDto);
		
		System.out.println("⑦セッション削除");
		httpSession.removeAttribute("bookingDto");
		
		System.out.println("⑧メール送信");
		mailService.sendBookingMail(bookingDto, uuid);
		return "redirect:/booking/form/complete";
	}
	
	//フォーム完了表示メソッド
	@GetMapping("/form/complete")
	public String completeForm() {
		return "booking/complete";
	}
	
	//変更前状態表示メソッド
	@GetMapping("/manage")
	public String choseChange(@RequestParam String token, Model model, RedirectAttributes redirectAttributes) {
		//トークンを取得して、その人の予約を持ってくる。
		Booking booking = bookingService.getValidateByTokenAndStatus(token);
		Long showId = booking.getStage().getShow().getId();
		
		BookingStatus status = booking.getBookingStatus();
			//現状の予約表示
		if(status != status.RESERVED) {
			throw new NotReservedException("予約されていません");
		}
			model.addAttribute("booking", booking);
		
		return "booking/manage";
		
	}
	
	//更新フォーム表示メソッド
	@GetMapping("/form/update/{id}")
	public String showUpdateForm(@PathVariable("id") Long bookingId, Model model) {
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
		
		return "update/form";
	}
	
	//フォーム更新確認メソッド
	@PostMapping("/confirm/update/{id}")
	public String confirmUpdateForm(@Valid @ModelAttribute BookingDto bookingDto,
			  BindingResult bindingResult,
			  HttpSession httpSession,
			  Model model,
			  @PathVariable("id") Long bookingId
			  ) {
		if(bindingResult.hasErrors()) {
			Booking booking = bookingService.getById(bookingId);
			Long showId = booking.getStage().getShow().getId();
			
			List<TicketType> ticketTypes = ticketTypeService.getAllTicketTypeByShowId(showId);
			
			bookingService.mapTicketTypeToDto(ticketTypes, bookingDto);
			
			model.addAttribute("bookingDto", bookingDto);
			model.addAttribute("ticketTypes", ticketTypes);
			model.addAttribute("staffs", staffService.getAllStaffByShowId(showId));
			model.addAttribute("stages", stageService.getAllStageByShowId(showId));
			model.addAttribute("paymentMethods", PaymentMethod.values());
			
			return "update/form";
		}
		
		httpSession.setAttribute("bookingDto", bookingDto);
		System.out.println("③セッション保存完了");
		
		//値段計算メソッドを渡す
		int sumPrice = bookingService.calculateTicketSumPrice(bookingDto);
		System.out.println("③計算メソッドを宣言");
		model.addAttribute("sumPrice", sumPrice);
		System.out.println("③計算メソッドを渡す");
		model.addAttribute("bookingDto", bookingDto);
		
		System.out.println("④confirm画面へ");
		
		return "update/confirm";
	}
	
	//フォーム更新メソッド
	@PostMapping("/update/{id}")
	public String updateForm(HttpSession httpSession,
							   Model model,
							   @PathVariable("id") Long bookingId,
							   RedirectAttributes redirectAttributes
							   ) {
		BookingDto bookingDto = (BookingDto) httpSession.getAttribute("bookingDto");
				
		System.out.println("⑥BookingDtoの作成");
		String uuid = bookingService.updateBooking(bookingDto);
		
		System.out.println("⑦セッション削除");
		httpSession.removeAttribute("bookingDto");
		
		System.out.println("⑧メール送信");
		mailService.sendBookingMail(bookingDto, uuid);
		
		return "redirect:/booking/form/complete";
	}
	
	//フォーム削除メソッド
	@PostMapping("/delete/{id}")
	public String deleteForm(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		Booking booking = bookingService.getById(id);
		
		String token = booking.getToken();
		
		bookingService.deleteBooking(token);
		
		return "redirect:/booking/delete/form";
	}
	
	//フォーム削除完了表示メソッド
		@GetMapping("/delete/form")
		public String deleteForm(Model model) {
			return "delete/complete";
		}

}
