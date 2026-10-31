package com.example.demo.constant;

import lombok.Getter;

@Getter
public enum ShowStatus {
	DRAFT("下書き"),
	PUBLISH("公開中"),
	CLOSED("受付終了");
	
	private final String label;
	
	ShowStatus(String label) {
		this.label = label;
	}
}
