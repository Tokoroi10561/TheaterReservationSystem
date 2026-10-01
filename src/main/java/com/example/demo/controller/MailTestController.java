package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.service.MailService;

@Controller
public class MailTestController {
	
	@Autowired
	private MailService mailService;
	
	@GetMapping("/mail-test")
	public String test() {
		mailService.sendTestMail();
		
		return "mail";
	}

}
