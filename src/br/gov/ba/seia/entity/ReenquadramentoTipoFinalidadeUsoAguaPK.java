package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the agro_classificacao_defensivo database table.
 * 
 */
@Embeddable
public class ReenquadramentoTipoFinalidadeUsoAguaPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ide_reenquadramento_processo_ato")
	private Integer ideReenquadramentoProcessoAto;

	@Column(name="ide_tipo_finalidade_uso_agua")
	private Integer ideTipoFinalidadeUsoAgua;

    public ReenquadramentoTipoFinalidadeUsoAguaPK() {
    }
    
    public ReenquadramentoTipoFinalidadeUsoAguaPK(Integer ideReenquadramentoProcessoAto, Integer ideTipoFinalidadeUsoAgua){
    	this.ideReenquadramentoProcessoAto=ideReenquadramentoProcessoAto;
    	this.ideTipoFinalidadeUsoAgua=ideTipoFinalidadeUsoAgua;
    }
  
    
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ReenquadramentoTipoFinalidadeUsoAguaPK)) {
			return false;
		}
		ReenquadramentoTipoFinalidadeUsoAguaPK castOther = (ReenquadramentoTipoFinalidadeUsoAguaPK)other;
		return 
			this.ideReenquadramentoProcessoAto.equals(castOther.ideReenquadramentoProcessoAto)
			&& this.ideTipoFinalidadeUsoAgua.equals(castOther.ideTipoFinalidadeUsoAgua);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + (this.ideReenquadramentoProcessoAto==null?0 : this.ideReenquadramentoProcessoAto.hashCode());
		hash = hash * prime + this.ideTipoFinalidadeUsoAgua.hashCode();
		
		return hash;
    }

	

	public Integer getIdeReenquadramentoProcessoAto() {
		return ideReenquadramentoProcessoAto;
	}

	public void setIdeReenquadramentoProcessoAto(Integer ideReenquadramentoProcessoAto) {
		this.ideReenquadramentoProcessoAto = ideReenquadramentoProcessoAto;
	}

	public Integer getIdeTipoFinalidadeUsoAgua() {
		return ideTipoFinalidadeUsoAgua;
	}

	public void setIdeTipoFinalidadeUsoAgua(Integer ideTipoFinalidadeUsoAgua) {
		this.ideTipoFinalidadeUsoAgua = ideTipoFinalidadeUsoAgua;
	}

	
}