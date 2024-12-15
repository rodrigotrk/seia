package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the agro_classificacao_defensivo database table.
 * 
 */
@Embeddable
public class ReenquadramentoTipologiaEmpreendimentoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ide_reenquadramento_processo_ato")
	private Integer ideReenquadramentoProcessoAto;

	@Column(name="ide_tipologia")
	private Integer ideTipologia;

    public ReenquadramentoTipologiaEmpreendimentoPK() {
    }
    
    public ReenquadramentoTipologiaEmpreendimentoPK(Integer ideReenquadramentoProcessoAto, Integer ideTipologia){
    	this.ideReenquadramentoProcessoAto=ideReenquadramentoProcessoAto;
    	this.ideTipologia=ideTipologia;
    }
  
    
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ReenquadramentoTipologiaEmpreendimentoPK)) {
			return false;
		}
		ReenquadramentoTipologiaEmpreendimentoPK castOther = (ReenquadramentoTipologiaEmpreendimentoPK)other;
		return 
			this.ideReenquadramentoProcessoAto.equals(castOther.ideReenquadramentoProcessoAto)
			&& this.ideTipologia.equals(castOther.ideTipologia);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + (this.ideReenquadramentoProcessoAto==null?0 : this.ideReenquadramentoProcessoAto.hashCode());
		hash = hash * prime + this.ideTipologia.hashCode();
		
		return hash;
    }

	public Integer getIdeTipologia() {
		return ideTipologia;
	}

	public void setIdeTipologia(Integer ideTipologia) {
		this.ideTipologia = ideTipologia;
	}

	public Integer getIdeReenquadramentoProcessoAto() {
		return ideReenquadramentoProcessoAto;
	}

	public void setIdeReenquadramentoProcessoAto(Integer ideReenquadramentoProcessoAto) {
		this.ideReenquadramentoProcessoAto = ideReenquadramentoProcessoAto;
	}

	
}