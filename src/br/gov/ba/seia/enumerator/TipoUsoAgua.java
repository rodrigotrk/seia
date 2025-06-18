package br.gov.ba.seia.enumerator;


public enum TipoUsoAgua {
	SUBTERRANEO(1, "empreendimento_lbl_incluir_ponto_captacao_subterranea"),
	SUPERFICIAL(2,"empreendimento_lbl_incluir_ponto_captacao_superficial"),
	LANCAMENTO(3,"empreendimento_lbl_incluir_ponto_receptor_residuos"),
	INTERVENCAO(4,"cefir_lbl_ponto_de_intercencao");

	private Integer id;
	private String chaveLabel;

	private TipoUsoAgua(Integer pId, String chave) {
		this.id = pId;
		this.chaveLabel = chave;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLabel() {
		return this.getDeclaringClass().getCanonicalName() + "." + this.name();
	}

	public String getChaveLabel() {
		return this.chaveLabel;
	}

}
