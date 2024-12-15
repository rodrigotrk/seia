package br.gov.ba.seia.enumerator;

public enum TipoAtividadeInexigivelEnum {

	OUTORGA(1, "Inexigível de Outorga"), LICENCA(2, "Inexigível de Licença");
	
	private Integer ideTipoAtividadeInexigivel;
	private String descricao;
	
	private TipoAtividadeInexigivelEnum(Integer ideTipoAtividadeInexigivel, String descricao) {
		this.ideTipoAtividadeInexigivel = ideTipoAtividadeInexigivel;
		this.descricao = descricao;
	}

	public Integer getIdeTipoAtividadeInexigivel() {
		return ideTipoAtividadeInexigivel;
	}

	public void setIdeTipoAtividadeInexigivel(Integer ideTipoAtividadeInexigivel) {
		this.ideTipoAtividadeInexigivel = ideTipoAtividadeInexigivel;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
