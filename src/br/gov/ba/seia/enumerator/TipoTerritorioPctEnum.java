package br.gov.ba.seia.enumerator;

/**
 * @author italo
 *
 */
public enum TipoTerritorioPctEnum {
	
	PROPRIEDADE(1),
	POSSE(2),
	CONCESSAO(3),
	OUTROS(4);
	
	private Integer id;
	
	TipoTerritorioPctEnum(Integer id){
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

}
