package com.example.demo.controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.ShowDto;
import com.example.demo.service.ShowService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/show")
@RequiredArgsConstructor
public class ShowController {
	
	private final ShowService showService;

	@GetMapping("/form/{troupeId}")
	public String showForm(@ModelAttribute ShowDto showDto,
						   Model model,
						   @PathVariable Long troupeId) {
		
		return "show/form";
	}
	
	@PostMapping("/comfirm/{troupeId}")
	public String confirm(@Valid@ModelAttribute ShowDto showDto,
							  BindingResult bindingResult,
							  HttpSession httpSession) {
		if(bindingResult.hasErrors()) {
			return "show/form";
		}
		httpSession.setAttribute("showDto", showDto);
		return "show/confirm";
	}
	
	@PostMapping("/regist/{troupeId}")
	public String regist(HttpSession httpSession,
							 @PathVariable Long troupeId,
							 RedirectAttributes redirectAttributes) {
		ShowDto showDto = (ShowDto)httpSession.getAttribute("showDto");
		
		showService.createShow(showDto, troupeId);
		
		httpSession.removeAttribute("showDto");
		
		return "redirect:/show/complete";
	}
	
	@GetMapping("/complete")
	public String complete() {
		return "show/complete";
	}
	
	@GetMapping("/list/{troupeId}")
	public String showList() {
		//showのリストを渡したいよね、だからtroupeの中の公演集を渡したい。
		
		return "show/list";
	}
	
	@GetMapping("/detail/{showId}")
	public String showDetail(@PathVariable Long showId, Model model) {
		
		//showIdからshowを取り出して、それをmodelで渡す
		
		return "show/detail";
	}
	
	@GetMapping("/update/form/{showId}")
	public String showUpdateForm() {
		return "show/update/form";
	}
	
	@PostMapping("/update/confirm/{showId}")
	public String confirmUpdate(@Valid@ModelAttribute ShowDto showDto,
			  				  BindingResult bindingResult,
			  				  HttpSession httpSession) {
			if(bindingResult.hasErrors()) {
				return "show/update/form";
			}
			httpSession.setAttribute("showDto", showDto);
			return "show/update/confirm";
	}
	
	@PostMapping("/update/regist/{showId}")
	public String registUpdate(HttpSession httpSession,
							   RedirectAttributes redirectAttributes) {
		ShowDto showDto = (ShowDto)httpSession.getAttribute("showDto");
		
		showService.updateShow(showDto);
		httpSession.removeAttribute("showDto");
		
		return "redirect:/show/complete";
	}
	
	@PostMapping("/delete/{showId}")
	public void delete(@PathVariable Long showId) {
		showService.deleteShow(showId);
	}
}
