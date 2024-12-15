package br.gov.ba.seia.enumerator;

public enum TipoPrestadorServicoEnum {
	
	ADMINSTRACAO_DIRETA_PREFEITURA(1, "Administração direta (prefeitura)"),
	ADMINSTRACAO_INDIRETA_SAAE_SIMILARES(2,"Administração indireta (SAAE ou similares)"),
	CONSESSIONARIA_COMP_ESTADUAIS_EMPRESA_PRIVADA(3,"concessionária (comp. Estaduais, empresa privada)"),
	AUTORIZADA_ASSOCIACOES_COOPERATIVAS(4,"Autorizada (Associações, cooperativas)");
	
	private Integer id;
	private String valor;
	
	TipoPrestadorServicoEnum(Integer id, String valor){		
		this.id = id;
		this.valor = valor;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
}