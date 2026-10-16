package com.example.demo.constant;

import lombok.Getter;

@Getter
public enum TroupeStatus {
	ACTIVE("通常稼働中"),
	SUSPENDED("利用停止"),
	WITHDRAWED("退会済み");
	
	private final String label;
	
	TroupeStatus(String label){
		this.label = label;
	}
}
