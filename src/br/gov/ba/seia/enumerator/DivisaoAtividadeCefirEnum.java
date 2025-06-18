package br.gov.ba.seia.enumerator;

import br.gov.ba.seia.entity.Tipologia;

public enum DivisaoAtividadeCefirEnum {
	AGRICULTURA_E_FLORESTAS(2);
	/*MINERAÇÃO(28),
	INDÚSTRIAS(56),
	TRANSPORTE(201),
	SERVIÇOS(210),
	OBRAS_CIVIS(267), 
	EMPREENDIMENTOS_URBANISTICOS_TURISTICOS_LAZER(282),
	FAUNA_SILVESTRE(393);*/
	
	
	private Integer id;
	
	DivisaoAtividadeCefirEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public static boolean containsDivisaoAtividade(Tipologia tipologia) {
		for (DivisaoAtividadeCefirEnum enumTipologia : DivisaoAtividadeCefirEnum.values()) {						
			if(enumTipologia.getId().equals(tipologia.getIdeTipologia())){
					return true;
			}			
		}
		return false;
	}
	
}
