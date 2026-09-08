package com.example.demo.constant;

import lombok.Getter;

@Getter
public enum StageStatus {
	RECEPTION("受付中"),
	SOLD_OUT("完売"),
	CLOSED("受付終了"),
	CANCELLED("キャンセル");
	
	private final String label;
	
	StageStatus(String label){
		this.label = label;
	}
}
