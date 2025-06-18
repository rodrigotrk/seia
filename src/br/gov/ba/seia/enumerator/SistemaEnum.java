package br.gov.ba.seia.enumerator;

public enum SistemaEnum {

	SEIA(1, "SEIA"), DESCONHECIDO(2, "DESCONHECIDO"), CERBERUS(3, "CERBERUS"),PROHIDROS(4, "PROHIDROS");

	private Integer id;
	private String nomSistema;

	private SistemaEnum(Integer id, String nomSistema) {
		this.id = id;
		this.nomSistema = nomSistema;
	}

	public Integer getId() {
		return id;
	}

	public String getNomSistema() {
		return nomSistema;
	}

}
