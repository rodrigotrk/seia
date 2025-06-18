package br.gov.ba.seia.enumerator;

public enum TipoInstalacaoEnum {
	RACEWAY_OU_SIMILAR(5, "Raceway ou Similar"),
	OUTROS(16, "Outros");

	private Integer id;
	private String nomTipoInstalacao;

	private TipoInstalacaoEnum(Integer id, String nomTipoInstalacao){
		this.id = id;
		this.nomTipoInstalacao = nomTipoInstalacao;
	}

	public Integer getId() {
		return id;
	}

	public String getNomTipoInstalacao() {
		return nomTipoInstalacao;
	}

}
