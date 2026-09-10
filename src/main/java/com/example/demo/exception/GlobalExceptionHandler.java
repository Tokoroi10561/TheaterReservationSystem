package com.example.demo.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	/**
	 * データ見つからないエラー(NoDataFoundException)を処理するメソッド
	 */
	@ExceptionHandler(NoDataFoundException.class)
	public String handleNoDataFoundException(NoDataFoundException ex, Model model) {
		model.addAttribute("errorMessage", ex.getMessage());
		
		return "error/no-data";
	}
	
	@ExceptionHandler(StageSoldOutException.class)
	public String handleStageSoldOutException(StageSoldOutException ex, Model model) {
		model.addAttribute("errorMessage", ex.getMessage());
		
		return "error/stage-sold-out";
	}
	
	@ExceptionHandler(TicketQuantityLimitExceededException.class)
	public String handleTicketQuantityLimitExceededException(TicketQuantityLimitExceededException ex, Model model) {
		model.addAttribute("errorMessage", ex.getMessage());
		
		return "error/ticket-quantity-limit-exceeded";
	}
	
	@ExceptionHandler(BookingClosedException.class)
	public String handleBookingNotFoundException(BookingClosedException ex, Model model) {
		model.addAttribute("errorMessage", ex.getMessage());
		
		return "error/booking-not-found";
	}
	
	@ExceptionHandler(Exception.class)
	public String handleException(Exception ex, Model model) {
		model.addAttribute("errorMessage", "予期せぬエラーが発生しました。時間をおいてやり直してください" + ex.getMessage());
		
		return "error/general-error";
	}
}
