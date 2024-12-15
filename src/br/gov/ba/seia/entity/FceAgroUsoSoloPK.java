package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 * The primary key class for the fce_agro_uso_solo database table.
 * 
 */
@Embeddable
public class FceAgroUsoSoloPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name="ide_fce_agrossilvopastoril")
	private Integer ideFceAgrossilvopastoril;

	@NotNull
	@Column(name="ide_tipo_uso_solo")
	private Integer ideTipoUsoSolo;

    public FceAgroUsoSoloPK() {
    }
    
    
	public FceAgroUsoSoloPK(Integer ideFceAgrossilvopastoril,Integer ideTipoUsoSolo) {
		this.ideFceAgrossilvopastoril = ideFceAgrossilvopastoril;
		this.ideTipoUsoSolo = ideTipoUsoSolo;
	}


	public Integer getIdeFceAgrossilvopastoril() {
		return this.ideFceAgrossilvopastoril;
	}
	public void setIdeFceAgrossilvopastoril(Integer ideFceAgrossilvopastoril) {
		this.ideFceAgrossilvopastoril = ideFceAgrossilvopastoril;
	}
	public Integer getIdeTipoUsoSolo() {
		return this.ideTipoUsoSolo;
	}
	public void setIdeTipoUsoSolo(Integer ideTipoUsoSolo) {
		this.ideTipoUsoSolo = ideTipoUsoSolo;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof FceAgroUsoSoloPK)) {
			return false;
		}
		FceAgroUsoSoloPK castOther = (FceAgroUsoSoloPK)other;
		return 
			this.ideFceAgrossilvopastoril.equals(castOther.ideFceAgrossilvopastoril)
			&& this.ideTipoUsoSolo.equals(castOther.ideTipoUsoSolo);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ideFceAgrossilvopastoril.hashCode();
		hash = hash * prime + this.ideTipoUsoSolo.hashCode();
		
		return hash;
    }
}