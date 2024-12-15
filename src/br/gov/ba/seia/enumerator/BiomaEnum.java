package br.gov.ba.seia.enumerator;

public enum BiomaEnum {

	
	MATA_ATLANTICA(1,"Mata Atlântica"), 
	CAATINGA(2,"Caatinga"),
	CERRADO(3,"Cerrado")	
	;
	
	private Integer id;
	private String nomBioma;

	BiomaEnum(Integer id, String nomBioma) {
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
	
	public static BiomaEnum getEnum(String nomBioma) {
		BiomaEnum[] enums = BiomaEnum.values();
		for(int i = 0; i < enums.length ; i++){
			if(enums[i].getNomBioma().equals(nomBioma)) {
				return enums[i];
			}
		}
	    throw new IllegalArgumentException("Bioma não encontrado!");
	}
	
	public static BiomaEnum getEnum(Integer id) {
		BiomaEnum[] enums = BiomaEnum.values();
		for(int i = 0; i < enums.length ; i++){
			if(enums[i].getId() == id) {
				return enums[i];
			}
		}
		throw new IllegalArgumentException("Bioma não encontrado!");
	}

}
