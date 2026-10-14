package com.example.demo.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.LoginDto;
import com.example.demo.dto.TroupeDto;
import com.example.demo.service.AuthService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	@GetMapping("/")
	public String showLoginForm(@ModelAttribute LoginDto dto) {
		return "login";
	}
	
	@GetMapping("/new")
	public String showNewForm(@ModelAttribute TroupeDto dto) {
		return "new";
	}
	
	@PostMapping("/new/register")
	public String registerNew(@Valid@ModelAttribute TroupeDto dto, RedirectAttributes redirectAttributes) {
		return "";
	}
	
	@PostMapping("/login")
	public String login(@Valid@ModelAttribute LoginDto dto,
			BindingResult bindinResult,
			RedirectAttributes redirectAttributes) {
		boolean isSuccess = authService.ckeck(dto);
		
		if(isSuccess) {
			return "redirect:/home";
		}else {
			return "login";
		}
	}
}
