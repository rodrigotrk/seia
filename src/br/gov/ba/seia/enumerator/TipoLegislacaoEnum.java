package br.gov.ba.seia.enumerator;

public enum TipoLegislacaoEnum {
	
	LAC(1);

	private Integer id;

	private TipoLegislacaoEnum(Integer pId) {
		this.id = pId;
	}

	public Integer getId() {
		return id;
	}
	
}
