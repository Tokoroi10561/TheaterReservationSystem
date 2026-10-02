package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.demo.dto.BookingDto;
import com.example.demo.service.MailService;

@Service
public class MailServiceImpl implements MailService{
	
	@Autowired
	private JavaMailSender mailSender;

	@Override
	@Async
	public void sendBookingMail(BookingDto bookingDto) {
		SimpleMailMessage message = new SimpleMailMessage();
		//そのうち劇予約管理アプリ用のメールアドレスに変更する。
		message.setFrom("mizyu1110@gmail.com");
		
		message.setTo(bookingDto.getEmail());
		message.setSubject("予約完了メール");
		message.setText(bookingDto.getName() + "様\n\\n" +
				"ご予約ありがとうございます。");
		
		mailSender.send(message);
	}
}
