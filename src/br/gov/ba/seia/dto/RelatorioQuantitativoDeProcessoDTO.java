package br.gov.ba.seia.dto;

public class RelatorioQuantitativoDeProcessoDTO {
	
	private String categoriaDoAto;
	private String siglaDoAto;
	private String nomeDoAto;
	private Integer qtdAtos;
	
	public RelatorioQuantitativoDeProcessoDTO() {
		
	}

	public String getCategoriaDoAto() {
		return categoriaDoAto;
	}

	public void setCategoriaDoAto(String categoriaDoAto) {
		this.categoriaDoAto = categoriaDoAto;
	}

	public String getSiglaDoAto() {
		return siglaDoAto;
	}

	public void setSiglaDoAto(String siglaDoAto) {
		this.siglaDoAto = siglaDoAto;
	}

	public String getNomeDoAto() {
		return nomeDoAto;
	}

	public void setNomeDoAto(String nomeDoAto) {
		this.nomeDoAto = nomeDoAto;
	}

	public Integer getQtdAtos() {
		return qtdAtos;
	}

	public void setQtdAtos(Integer qtdAtos) {
		this.qtdAtos = qtdAtos;
	}		
}