package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the agro_tipo_adubacao database table.
 * 
 */
@Embeddable
public class AgroTipoAdubacaoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ide_fce_agrossilvopastoril")
	private Integer ideFceAgrossilvopastoril;

	@Column(name="ide_tipo_adubacao")
	private Integer ideTipoAdubacao;

	public AgroTipoAdubacaoPK() {
	}

	public AgroTipoAdubacaoPK(Integer ideFceAgrossilvopastoril, Integer ideTipoAdubacao ) {
		this.ideFceAgrossilvopastoril=ideFceAgrossilvopastoril;
		this.ideTipoAdubacao=ideTipoAdubacao;
	}

	public Integer getIdeFceAgrossilvopastoril() {
		return this.ideFceAgrossilvopastoril;
	}
	public void setIdeFceAgrossilvopastoril(Integer ideFceAgrossilvopastoril) {
		this.ideFceAgrossilvopastoril = ideFceAgrossilvopastoril;
	}
	public Integer getIdeTipoAdubacao() {
		return this.ideTipoAdubacao;
	}
	public void setIdeTipoAdubacao(Integer ideTipoAdubacao) {
		this.ideTipoAdubacao = ideTipoAdubacao;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof AgroTipoAdubacaoPK)) {
			return false;
		}
		AgroTipoAdubacaoPK castOther = (AgroTipoAdubacaoPK)other;
		return
				this.ideFceAgrossilvopastoril.equals(castOther.ideFceAgrossilvopastoril)
				&& this.ideTipoAdubacao.equals(castOther.ideTipoAdubacao);

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ideFceAgrossilvopastoril.hashCode();
		hash = hash * prime + this.ideTipoAdubacao.hashCode();

		return hash;
	}
}