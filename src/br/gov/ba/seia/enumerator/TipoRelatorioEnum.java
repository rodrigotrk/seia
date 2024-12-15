package br.gov.ba.seia.enumerator;

public enum TipoRelatorioEnum {
	
	PDF("PDF"),
	CSV("CSV"), 
	XLS("XLS"),
	HTML("HTML");
	
	private String value;
	
	TipoRelatorioEnum( String value){		
		this.value = value;
	}
			
	public String getValue(){
		return this.value;
	}
	
	
	public static TipoRelatorioEnum getTipoArquivoEnum(String value) {
		
		for (TipoRelatorioEnum tipoRelatorioEnum : TipoRelatorioEnum.values()) {
			if(tipoRelatorioEnum.getValue().equals(value))
				return tipoRelatorioEnum;
		}
		
		throw new IllegalArgumentException("Não encontrado");
	}
	
}
