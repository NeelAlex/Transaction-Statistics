package com.n26.log;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import com.n26.dto.TransactionDTO;

@Aspect
@Component
public class Log {
	private static final Logger LOGGER = LoggerFactory.getLogger(Log.class); 
	
	@Before("execution(public com.n26.dto.StatisticsDTO com.n26.controller.TransactionsStatisticsController.getStatistics())")
	public void logBeforeGetStatistics() {
		LOGGER.info("called getStatistics");
	}
	
	@Before("execution(public void com.n26.controller.TransactionsStatisticsController.postTransactions(*))")
	public void logBeforePostTransactions() {
		LOGGER.info("called postTransactions");
	}
	
	@Before("execution(public void com.n26.controller.TransactionsStatisticsController.deleteTransactions())")
	public void logBeforeDeleteTransactions() {
		LOGGER.info("called deleteTransactions");
	}
}
