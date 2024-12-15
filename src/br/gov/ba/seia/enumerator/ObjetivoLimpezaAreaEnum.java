package br.gov.ba.seia.enumerator;

public enum ObjetivoLimpezaAreaEnum {

	READEQUACAO_AREA_UTILIZACAO_AGROPECUARIA_PASTOREIO(1),LIMPEZA_DE_AREA_SILVICULTURA(2),ACEIROS_OU_PICADAS(3),MANUTENCAO_INFRAESTRUTURA(4), OUTROS(5);

	private Integer id;
	
	ObjetivoLimpezaAreaEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getNomeObjetivoLimpezaAreaEnum(Integer ide) {
		if(ide.equals(1))
			return "Readequação de área à utilização agropecuária ou pastoreio";
		else if(ide.equals(2))
			return "Limpeza de área de silvicultura";	
		else if(ide.equals(3))
			return "Limpeza de aceiros ou picadas";
		else if(ide.equals(4))
			return "Manutenção de infraestrutura";
		else if(ide.equals(5))
			return "Outros";
		else
			return "ItemInvalido";
	}

}
