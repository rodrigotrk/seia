package br.gov.ba.seia.enumerator;

public enum IndiceCobrancaEnum {

	IGPM(1), SELIC(2);

	private Integer id;

	private IndiceCobrancaEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
	
	public IndiceCobrancaEnum getIGPM(){
		return IndiceCobrancaEnum.IGPM;
	}
	public IndiceCobrancaEnum getSELIC(){
		return IndiceCobrancaEnum.SELIC;
	}

}
