package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * The primary key class for the fce_agro_tipo_conservacao database table.
 * 
 */
@Embeddable
public class FceAgroTipoConservacaoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name="ide_fce_agrossilvopastoril")
	private Integer ideFceAgrossilvopastoril;

	@NotNull
	@Column(name="ide_tipo_conservacao_solo_agua")
	private Integer ideTipoConservacaoSoloAgua;

	
    public FceAgroTipoConservacaoPK(Integer ideFceAgrossilvopastoril,Integer ideTipoConservacaoSoloAgua) {
		this.ideFceAgrossilvopastoril = ideFceAgrossilvopastoril;
		this.ideTipoConservacaoSoloAgua = ideTipoConservacaoSoloAgua;
	}
    
	public FceAgroTipoConservacaoPK() {
    }
	public Integer getIdeFceAgrossilvopastoril() {
		return this.ideFceAgrossilvopastoril;
	}
	public void setIdeFceAgrossilvopastoril(Integer ideFceAgrossilvopastoril) {
		this.ideFceAgrossilvopastoril = ideFceAgrossilvopastoril;
	}
	public Integer getIdeTipoConservacaoSoloAgua() {
		return this.ideTipoConservacaoSoloAgua;
	}
	public void setIdeTipoConservacaoSoloAgua(Integer ideTipoConservacaoSoloAgua) {
		this.ideTipoConservacaoSoloAgua = ideTipoConservacaoSoloAgua;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof FceAgroTipoConservacaoPK)) {
			return false;
		}
		FceAgroTipoConservacaoPK castOther = (FceAgroTipoConservacaoPK)other;
		return 
			this.ideFceAgrossilvopastoril.equals(castOther.ideFceAgrossilvopastoril)
			&& this.ideTipoConservacaoSoloAgua.equals(castOther.ideTipoConservacaoSoloAgua);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ideFceAgrossilvopastoril.hashCode();
		hash = hash * prime + this.ideTipoConservacaoSoloAgua.hashCode();
		
		return hash;
    }
}