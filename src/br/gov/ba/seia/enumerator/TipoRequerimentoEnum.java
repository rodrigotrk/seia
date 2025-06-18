package br.gov.ba.seia.enumerator;


public enum TipoRequerimentoEnum {
	REGULACAO_AMBIENTAL_DO_EMPREENDIMENTO(1),
	REQUERIMENTO_ATO_DECLARATORIO(2),
	REQUERIMENTO_DE_ATO_ADMINISTRATIVO(3),
	REQUERIMENTO_DE_CADASTRO_DE_IMOVEL_RURAL(4);
	
	private Integer ide;
	
	private TipoRequerimentoEnum(Integer i) {
		ide = i;
	}
	
	public Integer getIde() {
		return ide;
	}
	
	public String getNomeTipoRequerimento() {
		if(this.ide.equals(1))
			return "Regulação Ambiental do Empreendimento";
		else if(this.ide.equals(2))
			return "Requerimento de Ato Declaratório";
		else if(this.ide.equals(3))
			return "Requerimento de Ato Administrativo";
		else if(this.ide.equals(4))
			return "Requerimento de Cadastro de Imovel Rural";
		else
			return "ItemInvalido";
	}
	
	public String getNomeTipoRequerimento(Integer ide) {
		if(ide.equals(1))
			return "Regulação Ambiental do Empreendimento";
		else if(ide.equals(2))
			return "Requerimento de Ato Declaratório";
		else if(ide.equals(3))
			return "Requerimento de Ato Administrativo";
		else if(ide.equals(4))
			return "Requerimento de Cadastro de Imovel Rural";
		else
			return "ItemInvalido";
	}
	
}
