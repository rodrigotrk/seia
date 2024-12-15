package br.gov.ba.seia.enumerator;


public enum SistemaCoordenadaEnum {
	GEOGRAFICA_SAD69(1, "4618"),
	UTM_23_SAD69(2, "29193"), 
	UTM_24_SAD69(3, "29194"),
	GEOGRAFICA_SIRGAS_2000(4, "4674"), 
	UTM_23_SIRGAS_2000(5, "31983"), 
	UTM_24_SIRGAS_2000 (6, "31984");

	private Integer id;
	private String srid;

	private SistemaCoordenadaEnum(Integer id, String srid) {
		this.id = id;
		this.srid = srid;
	}

	public Integer getId() {
		return id;
	}
	
	public String getSrid() {
		return this.srid;
	}
	
	public static SistemaCoordenadaEnum getEnum(Integer id) {
		SistemaCoordenadaEnum[] enums = SistemaCoordenadaEnum.values();
		for(int i = 0; i < enums.length ; i++){
			if(enums[i].getId() == id) {
				return enums[i];
			}
		}
		throw new IllegalArgumentException("Sistema Coordenada não encontrado!");
	}

}
