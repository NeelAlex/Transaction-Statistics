package com.n26.dto;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class TransactionDTO {

	private String amount;
	private String timestamp;
	
	public TransactionDTO() {
		
	}
	
	public TransactionDTO(String amount, String timestamp) {
		this.amount = amount;
		this.timestamp = timestamp;
	}
	
	public String getAmount() {
		return amount;
	}
	
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	public String getTimestamp() {
		return timestamp;
	} 
	
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
