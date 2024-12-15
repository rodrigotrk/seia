package br.gov.ba.seia.enumerator;

public enum AbaRegistroFlorestaPlantadaEnum {
	
	ABA_ACEITE(0, "abaEtapaUm"), 
	ABA_REGISTRO_FLORESTA_PLANTADA(1, "abaEtapaDois"),
	ABA_RESPONSABILIDADE(2, "abaEtapaTres");

	private int id;
	private String nome;
	
	private AbaRegistroFlorestaPlantadaEnum(int id, String nome){
		this.id = id;
		this.nome = nome;
	}
	
	public int getId(){
		return this.id;	
	}
	
	public String getNome(){
		return this.nome;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public static AbaRegistroFlorestaPlantadaEnum getEnum(Integer id) {
		AbaRegistroFlorestaPlantadaEnum[] enums = AbaRegistroFlorestaPlantadaEnum.values();
		
		for(int i = 0; i < enums.length ; i++){
			if(enums[i].getId() == id) {
				return enums[i];
			}
		}
		
		throw new IllegalArgumentException("Não encontrado!");
	}
	
	public static AbaRegistroFlorestaPlantadaEnum getEnum(String nome) {
		AbaRegistroFlorestaPlantadaEnum[] enums = AbaRegistroFlorestaPlantadaEnum.values();
		
		for(int i = 0; i < enums.length ; i++){
			if(enums[i].getNome().equals(nome)) {
				return enums[i];
			}
		}
		
		throw new IllegalArgumentException("Não encontrado!");
	}

	public static AbaRegistroFlorestaPlantadaEnum avancar(AbaRegistroFlorestaPlantadaEnum enum_) {
		if(enum_.id < AbaRegistroFlorestaPlantadaEnum.ABA_RESPONSABILIDADE.getId()){
			return getEnum(enum_.getId()+1);
		}
		
		return enum_;
	}

	public static AbaRegistroFlorestaPlantadaEnum voltar(AbaRegistroFlorestaPlantadaEnum enum_) {
		if(enum_.id > AbaRegistroFlorestaPlantadaEnum.ABA_ACEITE.getId()){
			return getEnum(enum_.getId()-1);
		}
		
		return enum_;
		
	}
	
}
