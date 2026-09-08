package com.example.demo.constant;

import lombok.Getter;

@Getter
public enum BookingStatus {
	RESERVED("予約完了"),
	CANCELLED("キャンセル"),
	CHECKED_IN("入場済み");
	
	private final String label;
	
	BookingStatus(String label){
		this.label = label;
	}
}
