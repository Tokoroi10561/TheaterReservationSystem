package com.example.demo.constant;

import lombok.Getter;

@Getter
public enum PaymentStatus {
	PENDING("未払い"),
	COMPLETED("支払い済み"),
	FAILED("支払い失敗"),
	REFUNDED("返金済み");

	private final String label;

	PaymentStatus(String label) {
		this.label = label;
	}
}
