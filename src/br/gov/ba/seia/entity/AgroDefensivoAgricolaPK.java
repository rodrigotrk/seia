package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the agro_defensivo_agricola database table.
 * 
 */
@Embeddable
public class AgroDefensivoAgricolaPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ide_fce_agrossilvopastoril")
	private Integer ideFceAgrossilvopastoril;

	@Column(name="ide_tipo_defensivo_agricola")
	private Integer ideTipoDefensivoAgricola;

    public AgroDefensivoAgricolaPK() {
    }
    
    public AgroDefensivoAgricolaPK(Integer ideFceAgrossilvopastoril, Integer ideTipoDefensivoAgricola){
    	this.ideFceAgrossilvopastoril=ideFceAgrossilvopastoril;
    	this.ideTipoDefensivoAgricola=ideTipoDefensivoAgricola;
    }
    
	public Integer getIdeFceAgrossilvopastoril() {
		return this.ideFceAgrossilvopastoril;
	}
	public void setIdeFceAgrossilvopastoril(Integer ideFceAgrossilvopastoril) {
		this.ideFceAgrossilvopastoril = ideFceAgrossilvopastoril;
	}
	public Integer getIdeTipoDefensivoAgricola() {
		return this.ideTipoDefensivoAgricola;
	}
	public void setIdeTipoDefensivoAgricola(Integer ideTipoDefensivoAgricola) {
		this.ideTipoDefensivoAgricola = ideTipoDefensivoAgricola;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof AgroDefensivoAgricolaPK)) {
			return false;
		}
		AgroDefensivoAgricolaPK castOther = (AgroDefensivoAgricolaPK)other;
		return 
			this.ideFceAgrossilvopastoril.equals(castOther.ideFceAgrossilvopastoril)
			&& this.ideTipoDefensivoAgricola.equals(castOther.ideTipoDefensivoAgricola);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ideFceAgrossilvopastoril.hashCode();
		hash = hash * prime + this.ideTipoDefensivoAgricola.hashCode();
		
		return hash;
    }
}