package com.n26.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.google.gson.Gson;
import com.n26.dto.TransactionDTO;

@RunWith(SpringRunner.class)
@SpringBootTest 
@AutoConfigureMockMvc
public class TransactionsStatisticsControllerTest {

	private static final int DURATION = 60;
	@Autowired
	private MockMvc mvc;
	
	@Test
	public void testGetStatistics() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				.get("/statistics").
				accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk());
	}

	@Test
	public void testPostTransactionsCreated() throws Exception {
		OffsetDateTime currentTime = OffsetDateTime.now(ZoneOffset.UTC);
		TransactionDTO transaction = new TransactionDTO("12.345", currentTime.toString());
		mvc.perform( MockMvcRequestBuilders
			      .post("/transactions")
			      .content(toJson(transaction))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated());
				
	}
	
	@Test
	public void testPostTransactionsOlderThan60() throws Exception {
		OffsetDateTime currentTime = OffsetDateTime.now(ZoneOffset.UTC);
		OffsetDateTime oldTime = currentTime.minus(DURATION, ChronoUnit.SECONDS);
		TransactionDTO transaction = new TransactionDTO("12.345", oldTime.toString());
		mvc.perform( MockMvcRequestBuilders
			      .post("/transactions")
			      .content(toJson(transaction))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());		
	}
	
	@Test
	public void testPostTransactionsIsInFuture() throws Exception {
		OffsetDateTime currentTime = OffsetDateTime.now(ZoneOffset.UTC);
		OffsetDateTime newTime = currentTime.plus(DURATION, ChronoUnit.SECONDS);
		TransactionDTO transaction = new TransactionDTO("12.345", newTime.toString());
		mvc.perform( MockMvcRequestBuilders
			      .post("/transactions")
			      .content(toJson(transaction))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isUnprocessableEntity());		
	}
	
	@Test
	public void testPostTransactionsInvalidJson() throws Exception {
		TransactionDTO transaction = new TransactionDTO("12.345", "xyz");
		mvc.perform( MockMvcRequestBuilders
			      .post("/transactions")
			      .content(toJson(transaction))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isUnprocessableEntity());		
	}
	
	@Test
	public void testPostTransactionsNonParsable() throws Exception {
		OffsetDateTime currentTime = OffsetDateTime.now(ZoneOffset.UTC);
		TransactionDTO transaction = new TransactionDTO("One hundred", currentTime.toString());
		mvc.perform( MockMvcRequestBuilders
			      .post("/transactions")
			      .content(toJson(transaction))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isUnprocessableEntity());		
	}

	@Test
	public void testDeleteTransactions() throws Exception {
		mvc.perform(MockMvcRequestBuilders
				.delete("/transactions")
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNoContent());

	}
	
	// HELPER METHODS
	private String toJson(Object obj) {
		Gson gson = new Gson();
		String json = gson.toJson(obj);
		return json;
	}
}
