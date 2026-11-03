package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.constant.ShowStatus;
import com.example.demo.entity.Show;
import com.example.demo.service.BookingService;
import com.example.demo.service.ShowService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {
	
	private final ShowService showService;
	private final BookingService bookingService;
	
	@GetMapping("/dashboard/{troupeId}")
	public String showHome(@PathVariable Long troupeId, Model model) {
		model.addAttribute("troupeId", troupeId);
		
		Show lastUpdatedShow = showService.getLastUpdated(troupeId);
		List<Show> publishedShows = showService.getByTroupeIdAndShowStatus(troupeId, ShowStatus.PUBLISH);
		List<Long> bookingSums = bookingService.calculateSumShowTicket(publishedShows);
		
		//最新の追加5件の詳細
		
		model.addAttribute("lastUpdatedShow", lastUpdatedShow);
		model.addAttribute("publishedShows", publishedShows);
		model.addAttribute("bookingSums", bookingSums);
		
		return "home/dashboard";
	}

}
