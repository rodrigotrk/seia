package br.gov.ba.seia.enumerator;

/**
 * #6590
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 */
public enum AquiculturaTipoAtividadeEnum {

	PSICULTURA(1),
	CARCINICULTURA(2),
	RANICULTURA(3),
	ALGICUTURA(4),
	MALOCOCULTURA(5);

	private Integer id;

	private AquiculturaTipoAtividadeEnum(Integer id){
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

}
