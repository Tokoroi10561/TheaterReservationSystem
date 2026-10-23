package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
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
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AuthController {
	private final AuthService authService;

	@GetMapping("/login")
	public String showLoginForm() {
		log.info("ログインフォーム投げる");
		return "login";
	}
	
	@GetMapping("/new/form")
	public String showNewForm(@ModelAttribute TroupeDto troupeDto) {
		log.info("新規登録フォーム投げる");
		return "new/form";
	}
	
	@PostMapping("/new/confirm")
	public String confirmNew(@Valid@ModelAttribute TroupeDto troupeDto,
							 BindingResult bindingResult,
							 RedirectAttributes redirectAttributes,
							 HttpSession httpSession
							 ) {
		log.info("新規登録情報確認メソッドの開始");
		if(bindingResult.hasErrors()) {
			log.warn("バリデーションエラー");
			bindingResult.getAllErrors().forEach(error -> {
				System.out.println(error.getDefaultMessage());
				});
			
			log.info("新規登録フォームを投げる");
			return "new/form";
		}
		httpSession.setAttribute("troupeDto", troupeDto);
		log.info("セッション保存");
		return "new/confirm";
	}
	
	@PostMapping("/new/register")
	public String registNew(HttpSession httpSession,
							  RedirectAttributes redirectAttributes) {
		log.info("新規登録メソッド開始");
		TroupeDto troupeDto = (TroupeDto) httpSession.getAttribute("troupeDto");
		
		authService.registNewTroupe(troupeDto);
		log.info("新規登録処理");
		
		httpSession.removeAttribute("troupeDto");
		log.info("セッションの削除");
		
		return "redirect:/new/complete";
	}
	
	@GetMapping("/new/complete")
	public String completeNew() {
		log.info("劇団登録完了ページを投げる");
		return "new/complete";
	}
	
	@PostMapping("/login")
	public String login(@Valid@ModelAttribute LoginDto loginDto,
			BindingResult bindinResult,
			RedirectAttributes redirectAttributes) {
		log.info("ログインメソッドの開始");
		boolean canAuth = authService.check(loginDto);
		
		if(canAuth) {
			
			
			log.info("ログイン確認処理。成功");
			return "redirect:/home";
		}else {
			log.info("ログイン確認処理。失敗");
			return "login";
		}
	}
}
