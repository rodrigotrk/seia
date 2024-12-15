package br.gov.ba.seia.enumerator;

public enum TipoArquivoEnum {
	
	IMAGEM(0, "imagem"),
	RELATORIO(1, "relatorio");
	
	private Integer id;
	private String value;
	
	TipoArquivoEnum(Integer id){		
		this.id = id;
	}
	
	TipoArquivoEnum(Integer id, String value){		
		this.id = id;
		this.value = value;
	}
			
	@Override
	public String toString(){
		return this.value;
	}
	
	public Integer getValue(){
		return id;
	}
	
	public static TipoArquivoEnum getTipoArquivoEnum(Integer id) {
		TipoArquivoEnum[] enums = TipoArquivoEnum.values();
		for(int i = 0; i < enums.length ; i++){
			if(enums[i].getValue() == id) {
				return enums[i];
			}
		}
		throw new IllegalArgumentException("Não encontrado");
	}
	
}
