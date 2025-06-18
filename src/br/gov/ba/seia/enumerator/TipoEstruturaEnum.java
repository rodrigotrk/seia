package br.gov.ba.seia.enumerator;

public enum TipoEstruturaEnum {

	EMPILHAMENTO_DRENADO(3, "Empilhamento drenado"),
	CAVA_EXAURIDA(4, "Cava Exaurida"),
	NAO_SE_APLICA(6, "Não se aplica"),
	OUTROS(7, "Outros");

	private Integer ideTipoEstrutura;
	private String nomTipoEstrutura;

	private TipoEstruturaEnum(Integer ideTipoEstrutura, String nomTipoEstrutura) {
		this.ideTipoEstrutura = ideTipoEstrutura;
		this.nomTipoEstrutura = nomTipoEstrutura;
	}
	public Integer getIdeTipoEstrutura() {
		return ideTipoEstrutura;
	}
	public void setIdeTipoEstrutura(Integer ideTipoEstrutura) {
		this.ideTipoEstrutura = ideTipoEstrutura;
	}
	public String getNomTipoEstrutura() {
		return nomTipoEstrutura;
	}
	public void setNomTipoEstrutura(String nomTipoEstrutura) {
		this.nomTipoEstrutura = nomTipoEstrutura;
	}

}
