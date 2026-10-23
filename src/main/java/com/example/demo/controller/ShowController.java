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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.ShowDto;
import com.example.demo.entity.Show;
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
		model.addAttribute("troupeId", troupeId);
		
		return "show/form";
	}
	
	@PostMapping("/comfirm/{troupeId}")
	public String confirm(@Valid@ModelAttribute ShowDto showDto,
						  BindingResult bindingResult,
						  HttpSession httpSession,
						  @PathVariable Long troupeId,
						  Model model) {

		model.addAttribute("troupeId", troupeId);
		if(bindingResult.hasErrors()) {
			return "show/form";
		}
		httpSession.setAttribute("showDto", showDto);
		return "show/confirm";
	}
	
	@PostMapping("/register/{troupeId}")
	public String register(HttpSession httpSession,
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
	public String showList(Model model, @PathVariable Long troupeId) {
		List<Show> showList = showService.findAllActive(troupeId);
		
		model.addAttribute("troupeId", troupeId);		
		model.addAttribute("showList", showList);
		
		return "show/list";
	}
	
	@GetMapping("/detail/{showId}")
	public String showDetail(@PathVariable Long showId, Model model) {
		
		Show show = showService.getById(showId);
		model.addAttribute("show", show);
		model.addAttribute("showId", showId);
		
		return "show/detail";
	}
	
	@GetMapping("/update/form/{showId}")
	public String showUpdateForm(@PathVariable Long showId, Model model) {
		model.addAttribute("showId", showId);
		
		return "show/update/form";
	}
	
	@PostMapping("/update/confirm/{showId}")
	public String confirmUpdate(@Valid@ModelAttribute ShowDto showDto,
			  				  BindingResult bindingResult,
			  				  HttpSession httpSession,
			  				  @PathVariable Long showId,
			  				  Model model) {
		model.addAttribute("showId", showId);
		if(bindingResult.hasErrors()) {
			return "show/update/form";
		}
		httpSession.setAttribute("showDto", showDto);
		return "show/update/confirm";
	}
	
	@PostMapping("/update/regist/{showId}")
	public String registerUpdate(HttpSession httpSession,
							   RedirectAttributes redirectAttributes) {
		ShowDto showDto = (ShowDto)httpSession.getAttribute("showDto");
		
		showService.updateShow(showDto);
		httpSession.removeAttribute("showDto");
		
		return "redirect:/show/complete";
	}
	
	@PostMapping("/delete/{showId}")
	public String delete(@PathVariable Long showId, Model model, RedirectAttributes redirectAttributes) {
		model.addAttribute("showId", showId);
		
		Show show = showService.getById(showId);
		
		Long troupeId = show.getTroupe().getId();
		
		showService.deleteShow(showId);
		
		redirectAttributes.addFlashAttribute("successMessage", "予約を完了しました。");
		
		return "redirect:/show/list/" + troupeId;
	}
}
