package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.BookingDetailsDto;
import com.example.demo.dto.BookingDto;
import com.example.demo.entity.TicketType;
import com.example.demo.service.BookingService;
import com.example.demo.service.MailService;
import com.example.demo.service.StaffService;
import com.example.demo.service.StageService;
import com.example.demo.service.TicketTypeService;

@Controller
@RequestMapping("/booking")
public class BookingController {
	
	@Autowired
	private BookingService bookingService;
	
	@Autowired
	private TicketTypeService ticketTypeService;
	
	@Autowired
	private StaffService staffService;
	
	@Autowired
	private StageService stageService;
	
	@Autowired
	private MailService mailService;
	
	//フォーム表示メソッド
	@GetMapping("/form/{showId}")
	public String showForm(Model model, @ModelAttribute BookingDto bookingDto, @PathVariable Long showId) {
		
		List<TicketType> ticketTypes = ticketTypeService.getAllTicketTypeByShowId(showId);
		
		BookingDto dto = new BookingDto();
		
		List<BookingDetailsDto> details = ticketTypes.stream()
				.map(tt -> {
					BookingDetailsDto d = new BookingDetailsDto();
					
					d.setTicketTypeId(tt.getId());
					d.setQuantity(0);
					return d;
				})
				.toList();
		dto.setBookingDetails(details);
		
		model.addAttribute("bookingDto", dto);
		model.addAttribute("ticketTypes", ticketTypes);
		model.addAttribute("staffs", staffService.getAllStaffByShowId(showId));
		model.addAttribute("stages", stageService.getAllStageByShowId(showId));
		
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
		//バリデーション
		if(bindingResult.hasErrors()) {
			
			model.addAttribute("form", bookingDto);
			model.addAttribute("ticketTypes", ticketTypeService.getAllTicketTypeByShowId(showId));
			model.addAttribute("staffs", staffService.getAllStaffByShowId(showId));
			model.addAttribute("stages", stageService.getAllStageByShowId(showId));
			
			return "booking";
		}
		//セッション保存
		httpSession.setAttribute("bookingDto", bookingDto);
		
		//値段計算メソッドを渡す
		int sumPrice = bookingService.calculateTicketSumPrice(bookingDto);
		model.addAttribute("sumPrice", sumPrice);
	
		model.addAttribute("bookingDto", bookingDto);
		
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
			
			model.addAttribute("ticketTypes", ticketTypeService.getAllTicketTypeByShowId(showId));
			model.addAttribute("staffs", staffService.getAllStaffByShowId(showId));
			model.addAttribute("stages", stageService.getAllStageByShowId(showId));
			
			return "redirect:/booking/form/" + showId;
		}
		
		bookingService.createBooking(bookingDto);
		
		httpSession.removeAttribute("bookingDto");
		
		mailService.sendBookingMail(bookingDto);
		return "redirect:/booking/form/complete";
	}
	
	//フォーム完了表示メソッド
	@GetMapping("/form/complete")
	public String completeForm() {
		return "booking/complete";
	}
	
	//フォーム更新表示メソッド
	
	//フォーム更新確認メソッド
	
	//フォーム更新メソッド
	
	//フォーム削除メソッド

}
