package br.gov.ba.seia.enumerator;

public enum ComunicacaoStatusEnum {
	
	NOVO(1),
	ENVIADO(2),
	ARQUIVADO(3),
	CANCELADO(4);
	private Integer id;
	
	
	ComunicacaoStatusEnum(Integer id) {
		this.id = id;
	}
	
	public Integer getIdComunicacaoStatus() {
		return id;
	}


}
