package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


/**
 * The persistent class for the agro_tipo_adubacao database table.
 * 
 */
@Entity
@Table(name="agro_tipo_adubacao")
@NamedQueries({
	@NamedQuery(name = "AgroTipoAdubacao.removerByIdeFceAgro", query ="DELETE FROM AgroTipoAdubacao a WHERE a.ideFceAgrossilvopastoril = :ideFceAgrossilvopastoril") })
public class AgroTipoAdubacao implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private AgroTipoAdubacaoPK agroTipoAdubacaoPK;

	@NotNull
	@JoinColumn(name = "ide_fce_agrossilvopastoril", referencedColumnName = "ide_fce_agrossilvopastoril", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private FceAgrossilvopastoril ideFceAgrossilvopastoril;

	//bi-directional many-to-one association to TipoAdubacao
	@NotNull
	@JoinColumn(name="ide_tipo_adubacao", referencedColumnName = "ide_tipo_adubacao", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch=FetchType.LAZY)
	private TipoAdubacao ideTipoAdubacao;

	public AgroTipoAdubacao() {
	}

	public AgroTipoAdubacao(AgroTipoAdubacaoPK agroTipoAdubacaoPK){
		this.agroTipoAdubacaoPK=agroTipoAdubacaoPK;
	}

	public AgroTipoAdubacaoPK getAgroTipoAdubacaoPK() {
		return agroTipoAdubacaoPK;
	}

	public void setAgroTipoAdubacaoPK(AgroTipoAdubacaoPK agroTipoAdubacaoPK) {
		this.agroTipoAdubacaoPK = agroTipoAdubacaoPK;
	}

	public FceAgrossilvopastoril getIdeFceAgrossilvopastoril() {
		return ideFceAgrossilvopastoril;
	}

	public void setIdeFceAgrossilvopastoril(
			FceAgrossilvopastoril ideFceAgrossilvopastoril) {
		this.ideFceAgrossilvopastoril = ideFceAgrossilvopastoril;
	}

	public TipoAdubacao getIdeTipoAdubacao() {
		return ideTipoAdubacao;
	}

	public void setIdeTipoAdubacao(TipoAdubacao ideTipoAdubacao) {
		this.ideTipoAdubacao = ideTipoAdubacao;
	}

}