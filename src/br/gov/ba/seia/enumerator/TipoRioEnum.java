package br.gov.ba.seia.enumerator;

import br.gov.ba.seia.entity.TipoRio;

public enum TipoRioEnum {

	PERENE(1, "Perene"),
	INTERMITENTE(2, "Intermitente"),
	EFEMERO(3, "Efêmero"),
	;

	private Integer id;
	private String dsc;

	private TipoRioEnum(Integer id, String dsc) {
		this.id = id;
		this.dsc = dsc;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDsc() {
		return dsc;
	}

	public void setDsc(String dsc) {
		this.dsc = dsc;
	}
	
	public static TipoRioEnum getEnum(TipoRio tipoRio) {
		TipoRioEnum[] enums = TipoRioEnum.values();
		for(int i = 0; i < enums.length ; i++){
			if(enums[i].getId() == tipoRio.getIdeTipoRio()) {
				return enums[i];
			}
		}
		throw new IllegalArgumentException("Tipo de rio não encontrado!");
	}

}
