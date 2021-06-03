package com.n26.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonFormat;

public class StatisticsDTO {
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private BigDecimal sum;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private BigDecimal avg;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private BigDecimal max;
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private BigDecimal min;
	private Long count;
	
	
	public StatisticsDTO() {
		this.sum = new BigDecimal(0);
		this.avg = new BigDecimal(0);
		this.max = new BigDecimal(0);
		this.min = new BigDecimal(0);
		this.count = 0L;
	}


	public StatisticsDTO(BigDecimal sum, BigDecimal avg, BigDecimal max, BigDecimal min, Long count) {
		this.sum = sum;
		this.avg = avg;
		this.max = max;
		this.min = min;
		this.count = count;
	}
	
	public BigDecimal getSum() {
		return sum;
	}


	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}


	public BigDecimal getAvg() {
		return avg;
	}


	public void setAvg(BigDecimal avg) {
		this.avg = avg;
	}


	public BigDecimal getMax() {
		return max;
	}


	public void setMax(BigDecimal max) {
		this.max = max;
	}


	public BigDecimal getMin() {
		return min;
	}


	public void setMin(BigDecimal min) {
		this.min = min;
	}


	public Long getCount() {
		return count;
	}


	public void setCount(Long count) {
		this.count = count;
	}


	public void assignDefaultValues() {
		this.setSum(BigDecimal.ZERO);
		this.setAvg(BigDecimal.ZERO);
		this.setMax(BigDecimal.ZERO);
		this.setMin(BigDecimal.ZERO);
		this.setCount(0L);
		this.setAvg(this.getAvg().setScale(2, BigDecimal.ROUND_HALF_UP));
		this.setMax(this.getMax().setScale(2, BigDecimal.ROUND_HALF_UP));
		this.setMin(this.getMin().setScale(2, BigDecimal.ROUND_HALF_UP));
		this.setSum(this.getSum().setScale(2, BigDecimal.ROUND_HALF_UP));
	}
}
