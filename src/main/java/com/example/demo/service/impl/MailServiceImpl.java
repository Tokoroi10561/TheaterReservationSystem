package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.demo.dto.BookingDto;
import com.example.demo.service.MailService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MailServiceImpl implements MailService{
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${app.frontend.url}")
	private String frontendBaseUrl;

	@Override
	@Async
	public void sendBookingMail(BookingDto bookingDto, String uuid) {		
		try {
			String cancelUrl = frontendBaseUrl + "/booking/manage?token=" + uuid;			
			SimpleMailMessage message = new SimpleMailMessage();
			//そのうち劇予約管理アプリ用のメールアドレスに変更する。
			message.setFrom("mizyu1110@gmail.com");
			
			message.setTo(bookingDto.getEmail());
			message.setSubject("予約完了のお知らせ");
			message.setText(bookingDto.getName() + "様\n\n" +
					"ご予約ありがとうございます。" +
					cancelUrl + "\n\n" +
					"※公演前日の23:59までお手続きが可能です。");
			
			mailSender.send(message);
		}catch(Exception e) {
			log.error(
					"メール送信失敗: {}",
					bookingDto.getEmail(),e
			);
		}
	}
}
