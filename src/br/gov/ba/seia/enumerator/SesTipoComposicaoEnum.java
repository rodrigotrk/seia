package br.gov.ba.seia.enumerator;

public enum SesTipoComposicaoEnum {

	DIGESTOR_ANAEROBIO_ASCENDENTE(1),
	FILTRO_ANEROBIO_ASCENDENTE(2),
	LAGOA_ANAEROBIA(3),
	LAGOA_FACULTATIVA(4),
	LAGOA_MATURACAO(5),
	UNIDADE_DESINFECCAO(6),
	EMISSARIO_lANCAMENTO(7),
	SISTEMA_SIMPLIFICIADO_FOSSA_SUMIDOURO(8),
	LEITO_SECAGEM(9),
	OUTROS(10);

	private Integer id;

	private SesTipoComposicaoEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

}
