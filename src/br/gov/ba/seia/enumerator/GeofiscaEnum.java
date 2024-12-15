package br.gov.ba.seia.enumerator;

/**
 * @author alexandre.queiroz (alexandre@montreal.com.br)
 */
public enum GeofiscaEnum {

	RESISTIVIDADE(1),
	ELETRICA(2),
	SISMICA(2),
	RADAR(3),
	MAGNETROMETRICA(4),
	OUTROS(5);
	
	private Integer id;

	private GeofiscaEnum(Integer id){
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
}
