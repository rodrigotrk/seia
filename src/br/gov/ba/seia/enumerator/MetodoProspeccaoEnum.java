package br.gov.ba.seia.enumerator;

import br.gov.ba.seia.enumerator.MetodoProspeccaoEnum;

/**
 * @author alexandre.queiroz (alexandre@montreal.com.br)
 */
public enum MetodoProspeccaoEnum {

	SONDAGENS(1),
	POCOS(2),
	TRINCHEIRAS(3),
	GALERIAS(4),
	GEOFISICA(5),
	AMOSTRAGEM(6);

	private Integer id;

	private MetodoProspeccaoEnum(Integer id){
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public static MetodoProspeccaoEnum getEnum(Integer id) {
		MetodoProspeccaoEnum[] enums = MetodoProspeccaoEnum.values();
		for(int i = 0; i < enums.length ; i++){
			if(enums[i].getId() == id) {
				return enums[i];
			}
		}
		throw new IllegalArgumentException("Método de Prospecção não encontrado!");
	}

}
