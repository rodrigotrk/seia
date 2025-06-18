package br.gov.ba.seia.enumerator;

/**
 * @author alexandre.queiroz (alexandre@montreal.com.br)
 */
public enum MetodoRecuperacaoIntervencaoEnum {

	RECONFORMACAO_TOPOGRAFICA(1),
	REVEGETACAO(2),
	OUTROS_METODOS(3),
	NAO_SE_APLICA(4);
	
	private Integer id;

	private MetodoRecuperacaoIntervencaoEnum(Integer id){
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
}
