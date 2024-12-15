package br.gov.ba.seia.enumerator;

public enum SefazCodigoReceitaEnum {
	
	CERH(2214),
	REPO_FLORESTAL(2248);
	
	private int id;
	
	private SefazCodigoReceitaEnum(Integer id){
		this.id = id;
	}
	
	public Integer getId(){
		return this.id;	
	}
	
	public static SefazCodigoReceitaEnum getEnum (int id){
		SefazCodigoReceitaEnum[] enums = SefazCodigoReceitaEnum.values();
		for(int i = 0; i < enums.length ; i++){
			if(enums[i].getId() == id) {
				return enums[i];
			}
		}
		throw new IllegalArgumentException("Código Não encontrado");
	}
}
