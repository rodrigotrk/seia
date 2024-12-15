package br.gov.ba.seia.dto;

import java.math.BigDecimal;

public class BigDecimalDTO {

	private String label;
	private BigDecimal value;
	
	/**
	 * Construtor
	 * @param label
	 * @param value
	 */
	public BigDecimalDTO(String label, BigDecimal value) {
		this.label = label;
		this.value = value;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	
}
