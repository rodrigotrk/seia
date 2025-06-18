package br.gov.ba.seia.enumerator;

import br.gov.ba.seia.entity.TipoEstadoConservacao;

/**
 * ENUM para a classe {@link TipoEstadoConservacao}
 *
 * @author eduardo.fernandes
 * @since 26/09/2016
 * @see <a href="http://10.105.17.77/redmine/issues/7823">#7823</a> 
 */
public enum TipoEstadoConservacaoEnum {
	PRESERVADA(1, "Preservada"),
	PARCIALMENTE_DEGRADADA(2, "Parcialmente Degradada"),
	DEGRADADA(3, "Degradada"),
	RECUPERADA(4, "Recuperada");
	
	private Integer id;
	private String dscTipoEstadoConservacao;
	
	TipoEstadoConservacaoEnum(Integer id, String dsc) {
		this.id = id;
		this.dscTipoEstadoConservacao = dsc;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDscTipoEstadoConservacao() {
		return dscTipoEstadoConservacao;
	}

	public void setDscTipoEstadoConservacao(String dscTipoEstadoConservacao) {
		this.dscTipoEstadoConservacao = dscTipoEstadoConservacao;
	}
}
