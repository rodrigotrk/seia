package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The persistent class for the fce_agro_tipo_conservacao database table.
 * 
 */
@Entity
@XmlRootElement
@NamedQuery(name = "FceAgroTipoConservacao.deleteByideFceAgrossilvopastoril", query = "DELETE FROM FceAgroTipoConservacao fc WHERE fc.ideFceAgrossilvopastoril.ideFceAgrossilvopastoril = :ideFceAgrossilvopastoril")
@Table(name="fce_agro_tipo_conservacao")
public class FceAgroTipoConservacao implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceAgroTipoConservacaoPK id;

	@NotNull
	@JoinColumn(name = "ide_fce_agrossilvopastoril", referencedColumnName = "ide_fce_agrossilvopastoril", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private FceAgrossilvopastoril ideFceAgrossilvopastoril;

	@NotNull
	@JoinColumn(name = "ide_tipo_conservacao_solo_agua", referencedColumnName = "ide_tipo_conservacao_solo_agua", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private TipoConservacaoSoloAgua ideTipoConservacaoSoloAgua ;

    public FceAgroTipoConservacao() {
    }

	public FceAgroTipoConservacaoPK getId() {
		return this.id;
	}

	public void setId(FceAgroTipoConservacaoPK id) {
		this.id = id;
	}

	public FceAgrossilvopastoril getIdeFceAgrossilvopastoril() {
		return ideFceAgrossilvopastoril;
	}

	public void setIdeFceAgrossilvopastoril(
			FceAgrossilvopastoril ideFceAgrossilvopastoril) {
		this.ideFceAgrossilvopastoril = ideFceAgrossilvopastoril;
	}

	public TipoConservacaoSoloAgua getIdeTipoConservacaoSoloAgua() {
		return ideTipoConservacaoSoloAgua;
	}

	public void setIdeTipoConservacaoSoloAgua(
			TipoConservacaoSoloAgua ideTipoConservacaoSoloAgua) {
		this.ideTipoConservacaoSoloAgua = ideTipoConservacaoSoloAgua;
	}
	
}