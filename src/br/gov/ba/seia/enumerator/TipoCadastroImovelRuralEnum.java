package br.gov.ba.seia.enumerator;


public enum TipoCadastroImovelRuralEnum {
	COMUM(1),
	ASSENTAMENTO_INCRA(2),
	IMOVEL_RURAL_CDA(3),
	IMOVEL_RURAL_PROJETO_BNDES(4),
	PCT(5)
	;

	private Integer tipo;

	private TipoCadastroImovelRuralEnum(int i) {
		tipo = i;
	}

	public Integer getTipo() {
		return tipo;
	}
}