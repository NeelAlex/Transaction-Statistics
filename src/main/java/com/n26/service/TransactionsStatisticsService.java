package com.n26.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.PriorityQueue;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.n26.dto.StatisticsDTO;
import com.n26.dto.TransactionDTO;

@Service
@EnableScheduling
public class TransactionsStatisticsService {

	private final long DURATION = 60;
	private StatisticsDTO statistics;
	private static final PriorityQueue<TransactionDTO> TRANSACTIONS = new PriorityQueue<TransactionDTO>((v1, v2) -> toISO8601(v1.getTimestamp()).compareTo(toISO8601(v2.getTimestamp())));
	
	public StatisticsDTO getStatistics() {
		computeStatistics();
		return statistics;
	}
	
	public ResponseEntity<TransactionDTO> deleteTransactions() {
		TRANSACTIONS.clear();
		statistics = new StatisticsDTO();
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}
	
	public ResponseEntity<TransactionDTO> postTransaction(TransactionDTO transaction) {
		// If any of the field is non parsable.
		if (transaction.getAmount() == null || !isValidAmount(transaction.getAmount()) || transaction.getTimestamp() == null || !isValidIsoDateTime(transaction.getTimestamp())) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}
		OffsetDateTime currentTime = OffsetDateTime.now(ZoneOffset.UTC);
		OffsetDateTime transactionTimestamp = toISO8601(transaction.getTimestamp());
		OffsetDateTime oldTime  = currentTime.minus(DURATION, ChronoUnit.SECONDS);
		// If transaction is in future.
		if (transactionTimestamp.isAfter(currentTime)) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(null);
		}
		// If transaction is older than 60 seconds.
		if (transactionTimestamp.isBefore(oldTime)) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
		synchronized (this) {
			TRANSACTIONS.add(transaction);
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}

	@Async
	private void computeStatistics() {
		clearOldData();
		statistics = new StatisticsDTO();
		statistics.assignDefaultValues();
		if (TRANSACTIONS.size() == 0) {
			return;
		}
		statistics.setMax(getAmount(TRANSACTIONS.peek().getAmount()));
		statistics.setMin(getAmount(TRANSACTIONS.peek().getAmount()));
		statistics.setCount((long) TRANSACTIONS.size());
		for (TransactionDTO transaction : TRANSACTIONS) {
			statistics.setSum((statistics.getSum().add(getAmount(transaction.getAmount()))).setScale(2, BigDecimal.ROUND_HALF_UP));
			statistics.setMax((statistics.getMax().max(getAmount(transaction.getAmount()))).setScale(2, BigDecimal.ROUND_HALF_UP));
			statistics.setMin((statistics.getMin().min(getAmount(transaction.getAmount()))).setScale(2, BigDecimal.ROUND_HALF_UP));			
		}
		statistics.setAvg((statistics.getSum().divide(new BigDecimal(TRANSACTIONS.size()), 2, BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP));
	}
	
	@Scheduled(fixedRate = 60000)
	@Async
	private void clearOldData() {
		OffsetDateTime currentTime = OffsetDateTime.now(ZoneOffset.UTC);
		currentTime = currentTime.minus(DURATION, ChronoUnit.SECONDS);
		synchronized (this) {
			while(!TRANSACTIONS.isEmpty() && toISO8601(TRANSACTIONS.peek().getTimestamp()).isBefore(currentTime)) {
				TRANSACTIONS.poll();
			}
		}
	}
	
	//HELPER METHODS
	private static OffsetDateTime toISO8601(String date) {
	      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
	      return OffsetDateTime.parse(date, dateTimeFormatter); 
	}
	
	private BigDecimal getAmount(String amount) {
		return new BigDecimal(amount);
	}
	
	private boolean isValidIsoDateTime(String date) {
        try { 
        	toISO8601(date);
        	return true;
        } catch (Exception e) {
            return false;
        }
    }
	
	private boolean isValidAmount(String amount) {
		try {
			getAmount(amount);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}
}
