package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.dto.BookingDto;
import com.example.demo.service.BookingService;
import com.example.demo.service.StaffService;
import com.example.demo.service.StageService;
import com.example.demo.service.TicketTypeService;

@Controller
@RequestMapping("booking")
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
	@GetMapping("form")
	public String showForm(Model model, BookingDto bookingDto) {
		
		model.addAttribute("form", bookingDto);
		//選択肢の奴の追加(ticketType, Staff, Stage)
		model.addAttribute("ticketTypes", ticketTypeService.getAllTicketType());
		model.ad
		
		return "form";
	}
	
	//フォーム確認メソッド
	
	//フォーム登録メソッド
	
	//フォーム完了表示メソッド
	
	//フォーム更新表示メソッド
	
	//フォーム更新確認メソッド
	
	//フォーム更新メソッド
	
	//フォーム削除メソッド

}
