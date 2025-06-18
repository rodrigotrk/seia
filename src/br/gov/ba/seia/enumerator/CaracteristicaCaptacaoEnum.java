package br.gov.ba.seia.enumerator;

public enum CaracteristicaCaptacaoEnum {

	FIO_D_AGUA(1),
	BARRAGEM_EXISTENTE(2),
	BARRAGEM_A_CONSTRUIR(3);
	private Integer id;

	private CaracteristicaCaptacaoEnum(Integer id){
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

}
