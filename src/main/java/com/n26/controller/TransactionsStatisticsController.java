package com.n26.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.n26.dto.StatisticsDTO;
import com.n26.dto.TransactionDTO;
import com.n26.service.TransactionsStatisticsService;

@RestController
public class TransactionsStatisticsController {

	@Autowired
	private TransactionsStatisticsService service;
	
	@GetMapping("/statistics")
	public StatisticsDTO getStatistics() {
		return service.getStatistics();
	}
	
	@PostMapping("/transactions")
	public ResponseEntity<TransactionDTO> postTransactions(@RequestBody TransactionDTO transaction) {
		return service.postTransaction(transaction);
	}
	
	@DeleteMapping("/transactions")
	public ResponseEntity<TransactionDTO> deleteTransactions() {
		return service.deleteTransactions();
	}
	
}
