package com.example.demo.constant;

import lombok.Getter;

@Getter
public enum ShowOption {

	STAFF_SUPPORT("扱い"),
	PHONE_REQUIRED("電話番号の記入"),
	ADDRESS_REQUIRED("住所の記入");
	
	private final String label;
	
	ShowOption(String label) {
		this.label = label;
	}
}
