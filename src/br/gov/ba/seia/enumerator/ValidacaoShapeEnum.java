package br.gov.ba.seia.enumerator;

public enum ValidacaoShapeEnum {
	
	/**
	 * Ids dos Enuns estão equiparados com os ids do arquivo ClassificacaoSecaoEnum
	 */
	PONTO(1), 
	POLIGONO(2), 
	DESENHO(3), 
	//Exceção para os itens abaixo que não está na base de dados
	POLIGONO_OU_LINHA(4), 
	LINHA(5),
	POLIGONO_OU_PONTO(6); 
	

	private Integer id;

	private ValidacaoShapeEnum(Integer id) {
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
	
}