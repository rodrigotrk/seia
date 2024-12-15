package br.gov.ba.seia.enumerator;

public enum MunicipioEnum {
	
	SALVADOR(837, "Salvador"),
	JACOBINA(787, "Jacobina"),
	SANTO_ANTONIO_DE_JESUS(943, "Santo Antônio de Jesus");
	
	private int id;
	private String nomMunicipio;
	
	private MunicipioEnum(int id, String nom){
		this.id = id;
		this.nomMunicipio = nom;
	}
	
	public int getId(){
		return this.id;	
	}

	public String getNomMunicipio() {
		return nomMunicipio;
	}

}
