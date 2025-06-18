package br.gov.ba.seia.enumerator;

public enum TipoBiomaEnum {

	
	TERRESTRE(1,"Terrestre"), 
	AQUATICO(2,"Aquático"),
	;
	
	private Integer id;
	private String nomBioma;

	TipoBiomaEnum(Integer id, String nomBioma) {
		this.id = id;
		this.setNomBioma(nomBioma);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNomBioma() {
		return nomBioma;
	}

	public void setNomBioma(String nomBioma) {
		this.nomBioma = nomBioma;
	}
	
	public static TipoBiomaEnum getEnum(String nomBioma) {
		TipoBiomaEnum[] enums = TipoBiomaEnum.values();
		for(int i = 0; i < enums.length ; i++){
			if(enums[i].getNomBioma().equals(nomBioma)) {
				return enums[i];
			}
		}
	    throw new IllegalArgumentException("Bioma não encontrado!");
	}
	
	public static TipoBiomaEnum getEnum(Integer id) {
		TipoBiomaEnum[] enums = TipoBiomaEnum.values();
		for(int i = 0; i < enums.length ; i++){
			if(enums[i].getId() == id) {
				return enums[i];
			}
		}
		throw new IllegalArgumentException("Bioma não encontrado!");
	}

}
