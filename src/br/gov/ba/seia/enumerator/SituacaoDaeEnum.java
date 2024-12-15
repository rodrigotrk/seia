package br.gov.ba.seia.enumerator;

public enum SituacaoDaeEnum {

	AGUARDANDO_IMPRESSAO(1), EM_ABERTO(2), PAGO(3), VENCIDO(4), CANCELADO(5);

	private Integer id;

	private SituacaoDaeEnum(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}
	
	public SituacaoDaeEnum getAGUARDANDO_IMPRESSAO(){
		return SituacaoDaeEnum.AGUARDANDO_IMPRESSAO;
	}
	public SituacaoDaeEnum getEM_ABERTO(){
		return SituacaoDaeEnum.EM_ABERTO;
	}
	public SituacaoDaeEnum getPAGO(){
		return SituacaoDaeEnum.PAGO;
	}
	public SituacaoDaeEnum getVENCIDO(){
		return SituacaoDaeEnum.VENCIDO;
	}
	public SituacaoDaeEnum getCANCELADO(){
		return SituacaoDaeEnum.CANCELADO;
	}

}
