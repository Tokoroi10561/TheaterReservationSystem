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

import com.example.demo.dto.TroupeDto;
import com.example.demo.entity.Troupe;
import com.example.demo.service.TroupeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/troupe")
@RequiredArgsConstructor
@Slf4j
public class TroupeController {
	
	private final TroupeService troupeService;

	@GetMapping("/update/form/{troupeId}")
	public String showUpdateForm(@PathVariable Long troupeId, Model model) {
		Troupe troupe = troupeService.getById(troupeId);
		TroupeDto troupeDto = TroupeDto.fromEntity(troupe);
		
		model.addAttribute("troupeDto", troupeDto);
		
		return "troupe/update/form";
	}
	
	@PostMapping("/update/confirm/{troupeId}")
	public String confirmUpdate(@Valid@ModelAttribute TroupeDto troupeDto,
								BindingResult bindingResult,
								HttpSession httpSession,
								@PathVariable Long troupeId,
								Model model
								) {
		model.addAttribute("troupeId", troupeId);
		if(bindingResult.hasErrors()) {
			log.warn("バリデーションエラー");
			bindingResult.getAllErrors().forEach(error -> {
				System.out.println(error.getDefaultMessage());
			});
		
			return "troupe/update/form";
		}
		
		httpSession.setAttribute("troupeDto", troupeDto);
		return "troupe/update/confirm";
	}
	
	@PostMapping("/update/register/{troupeId}")
	public String registerUpdate(HttpSession httpSession,
								 RedirectAttributes redirectAttributes,
								 @PathVariable Long troupeId) {
		TroupeDto troupeDto = (TroupeDto)httpSession.getAttribute("troupeDto");
		
		troupeService.update(troupeDto);
		log.info("劇団情報を更新しました。劇団Id: {}", troupeDto.getId());
		httpSession.removeAttribute("troupeDto");
		return "redirect:/complete/" + troupeId;
	}
	
	@GetMapping("/complete/{troupeId}")
	public String completeUpdate(@PathVariable Long troupeId, Model model) {
		model.addAttribute("troupeId", troupeId);
		
		return "troupe/complete";
	}
	
	@PostMapping("/delete/{troupeId}")
	public String delete(@PathVariable Long troupeId, Model model, RedirectAttributes redirectAttributes) {
		
		model.addAttribute("troupeId", troupeId);
		troupeService.delete(troupeId);
		log.info("劇団情報データを削除。公演ID: {}", troupeId);
		
		redirectAttributes.addFlashAttribute("successMessage", "予約を完了しました。");
		
		return "redirect:/login";
	}
	
	@GetMapping("/detail/{troupeId}")
	public String detail(@PathVariable Long troupeId, Model model) {
		Troupe troupe = troupeService.getById(troupeId);
		model.addAttribute("troupe", troupe);
		model.addAttribute("troupeId", troupeId);
		
		return "troupe/detail";
	}
}
