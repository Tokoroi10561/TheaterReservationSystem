package com.example.demo.controller;

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

import com.example.demo.dto.BookingDto;
import com.example.demo.service.BookingService;
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
	
	//フォーム表示メソッド
	@GetMapping("/form/{showId}")
	public String showForm(Model model, BookingDto bookingDto, @PathVariable Long showId) {
		
		model.addAttribute("form", bookingDto);
		//選択肢の奴の追加(ticketType, Staff, Stage)
		model.addAttribute("ticketTypes", ticketTypeService.getAllTicketTypeByShowId(showId));
		model.addAttribute("staffs", staffService.getAllStaffByShowId(showId));
		model.addAttribute("stages", stageService.getAllStageByShowId(showId));
		
		
		return "booking";
	}
	
	//フォーム確認メソッド
	@PostMapping("/form/confirm")
	public String confirmForm(@Valid @ModelAttribute BookingDto bookingDto,
							  BindingResult bindingResult,
							  HttpSession httpSession,
							  Model model) {
		//バリデーション
		if(bindingResult.hasErrors()) {
			return "booking/{showId}";
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
	@PostMapping("/form/register")
	public String registerForm(HttpSession httpSession) {
		BookingDto bookingDto = (BookingDto) httpSession.getAttribute("bookingDto");
		if(bookingDto == null) {
			return "redirect:/booking/form";
		}
		
		bookingService.createBooking(bookingDto);
		httpSession.removeAttribute("bookingDto");
		return "booking/register";
	}
	
	//フォーム完了表示メソッド
	@GetMapping("/form/complete")
	public String completeForm(RedirectAttributes redirectAttributes) {
		return "redirect:/booking/complete";
	}
	
	//フォーム更新表示メソッド
	
	//フォーム更新確認メソッド
	
	//フォーム更新メソッド
	
	//フォーム削除メソッド

}
