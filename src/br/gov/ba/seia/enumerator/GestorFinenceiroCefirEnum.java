package br.gov.ba.seia.enumerator;

public enum GestorFinenceiroCefirEnum {

	INEMA(1, "INEMA"), SEMA(2, "SEMA"), FLEM(3, "FLEM"), EBDA(4, "EBDA");

	private Integer ideGestorFinanceiroCefir;
	private String nomeGestorFinanceiroCefir;

	GestorFinenceiroCefirEnum(Integer ideGestorFinanceiroCefir, String nomeGestorFinanceiroCefir) {
		this.ideGestorFinanceiroCefir = ideGestorFinanceiroCefir;
		this.nomeGestorFinanceiroCefir = nomeGestorFinanceiroCefir;
	}

	public Integer getIdeGestorFinanceiroCefir() {
		return ideGestorFinanceiroCefir;
	}

	public void setIdeGestorFinanceiroCefir(Integer ideGestorFinanceiroCefir) {
		this.ideGestorFinanceiroCefir = ideGestorFinanceiroCefir;
	}

	public String getNomeGestorFinanceiroCefir() {
		return nomeGestorFinanceiroCefir;
	}

	public void setNomeGestorFinanceiroCefir(String nomeGestorFinanceiroCefir) {
		this.nomeGestorFinanceiroCefir = nomeGestorFinanceiroCefir;
	}

}
