package br.gov.ba.seia.enumerator;

public enum TipoAquiculturaEnum {

	CAPTACAO(1,5),
	LANCAMENTO(2,5),
	RIO(3,6),
	BARRAGEM(4,6),
	VIVEIRO_ESCAVADO(5,null),
	TANQUE_REDE(6,null);

	private Integer id;
	private Integer idPai;

	private TipoAquiculturaEnum(Integer id, Integer idPai){
		this.id = id;
		this.idPai = idPai;
	}

	public Integer getId() {
		return id;
	}

	public Integer getIdPai() {
		return idPai;
	}

}
