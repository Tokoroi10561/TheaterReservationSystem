package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
//@RequestMapping("/home")
public class HomeController {
	
	@GetMapping("/home")
	public String showHome() {
//		model.addAttribute("troupeId", troupeId);
		
		return "home";
	}

}
