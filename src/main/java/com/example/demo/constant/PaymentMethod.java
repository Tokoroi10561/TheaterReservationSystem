package com.example.demo.constant;

import lombok.Getter;

@Getter
public enum PaymentMethod {
	CASH("当日現金払い"),
	CREDIT_CARD("クレジット決済"),
	BAND("銀行振込"),
	QR("QRコード決済");
	
	private final String label;
	
	PaymentMethod(String label){
		this.label = label;	
	}
}
