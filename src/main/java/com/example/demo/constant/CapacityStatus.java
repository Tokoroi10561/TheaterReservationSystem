package com.example.demo.constant;

import lombok.Getter;

@Getter
public enum CapacityStatus {
	WIDE_OPEN("◎"),
	AVALILABLE("〇"),
	LIMITED("△"),
	FULL("✕");
	
	private final String label;
	
	CapacityStatus(String label){
		this.label = label;
	}
}