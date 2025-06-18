package br.gov.ba.seia.enumerator;

public enum ClassificacaoSecaoEnum {
	
	CLASSIFICACAO_SECAO_PONTO(1), 
	CLASSIFICACAO_SECAO_SHAPEFILE(2), 
	CLASSIFICACAO_SECAO_DESENHO(3),
	COM_PONTO_OU_SHAPEFILE(6);

	private Integer id;

	private ClassificacaoSecaoEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getValue(){
		return id.toString();
	}
	
	public String getNomeClassificSec() {
		if(this.id.equals(1)) return "Ponto";
		if(this.id.equals(2)) return "Arquivos Shape";
		if(this.id.equals(3)) return "Desenho";
		return "";
	}
	
	public String getClassificacaoInclusao() {
		if(this.id.equals(1)) return "Coordenadas";
		if(this.id.equals(2)) return "Shapefile";
		if(this.id.equals(3)) return "Desenho";
		return "";
	}
	
}