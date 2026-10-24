package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
//@RequestMapping("/home")
public class HomeController {
	
	@GetMapping("/home/{troupeId}")
	public String showHome(@PathVariable Long troupeId, Model model) {
		model.addAttribute("troupeId", troupeId);
		
		return "home";
	}

}
