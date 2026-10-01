package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.service.MailService;

@Service
public class MailServiceImpl implements MailService{
	
	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void sendTestMail() {
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setFrom("mizyu1110@gamil.com");
		message.setTo("mizyu1110@gmail.com");
		message.setSubject("テストメール");
		message.setText("こんにちは！！！！");
		
		mailSender.send(message);
	}
}
