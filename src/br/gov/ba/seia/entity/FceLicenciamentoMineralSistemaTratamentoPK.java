package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the fce_licenciamento_mineral_sistema_tratamento
 * database table.
 * 
 */
@Embeddable
public class FceLicenciamentoMineralSistemaTratamentoPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "ide_tipo_sistema_tratamento", insertable = false, updatable = false)
	private Integer ideTipoSistemaTratamento;

	@Column(name = "ide_fce_licenciamento_mineral", insertable = false, updatable = false)
	private Integer ideFceLicenciamentoMineral;

	public FceLicenciamentoMineralSistemaTratamentoPK() {
	}

	public FceLicenciamentoMineralSistemaTratamentoPK(FceLicenciamentoMineral fceLicenciamentoMineral, TipoSistemaTratamento tipoSistemaTratamento) {
		this.ideFceLicenciamentoMineral = fceLicenciamentoMineral.getIdeFceLicenciamentoMineral();
		this.ideTipoSistemaTratamento = tipoSistemaTratamento.getIdeTipoSistemaTratamento();
	}

	public Integer getIdeTipoSistemaTratamento() {
		return this.ideTipoSistemaTratamento;
	}

	public void setIdeTipoSistemaTratamento(Integer ideTipoSistemaTratamento) {
		this.ideTipoSistemaTratamento = ideTipoSistemaTratamento;
	}

	public Integer getIdeFceLicenciamentoMineral() {
		return this.ideFceLicenciamentoMineral;
	}

	public void setIdeFceLicenciamentoMineral(Integer ideFceLicenciamentoMineral) {
		this.ideFceLicenciamentoMineral = ideFceLicenciamentoMineral;
	}

	public boolean equals(Object other) {
		if(this == other){
			return true;
		}
		if(!(other instanceof FceLicenciamentoMineralSistemaTratamentoPK)){
			return false;
		}
		FceLicenciamentoMineralSistemaTratamentoPK castOther = (FceLicenciamentoMineralSistemaTratamentoPK) other;
		return this.ideTipoSistemaTratamento.equals(castOther.ideTipoSistemaTratamento) && this.ideFceLicenciamentoMineral.equals(castOther.ideFceLicenciamentoMineral);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ideTipoSistemaTratamento.hashCode();
		hash = hash * prime + this.ideFceLicenciamentoMineral.hashCode();

		return hash;
	}
}