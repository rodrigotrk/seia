package br.gov.ba.seia.enumerator;

public enum TipoProducaoEnum {

	FORMAS_JOVENS(1),
	ENGORDA(2);

	private Integer id;

	private TipoProducaoEnum(Integer id){
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

}
