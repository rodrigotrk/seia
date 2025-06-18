package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the fce_licenciamento_mineral_servidao_mineraria
 * database table.
 * 
 */
@Embeddable
public class FceLicenciamentoMineralServidaoMinerariaPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "ide_servidao_mineraria", insertable = false, updatable = false)
	private Integer ideServidaoMineraria;

	@Column(name = "ide_fce_licenciamento_mineral", insertable = false, updatable = false)
	private Integer ideFceLicenciamentoMineral;

	public FceLicenciamentoMineralServidaoMinerariaPK() {
	}

	public FceLicenciamentoMineralServidaoMinerariaPK(FceLicenciamentoMineral fceLicenciamentoMineral, ServidaoMineraria servidaoMineraria) {
		this.ideFceLicenciamentoMineral = fceLicenciamentoMineral.getIdeFceLicenciamentoMineral();
		this.ideServidaoMineraria = servidaoMineraria.getIdeServidaoMineraria();
	}

	public Integer getIdeServidaoMineraria() {
		return this.ideServidaoMineraria;
	}

	public void setIdeServidaoMineraria(Integer ideServidaoMineraria) {
		this.ideServidaoMineraria = ideServidaoMineraria;
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
		if(!(other instanceof FceLicenciamentoMineralServidaoMinerariaPK)){
			return false;
		}
		FceLicenciamentoMineralServidaoMinerariaPK castOther = (FceLicenciamentoMineralServidaoMinerariaPK) other;
		return this.ideServidaoMineraria.equals(castOther.ideServidaoMineraria) && this.ideFceLicenciamentoMineral.equals(castOther.ideFceLicenciamentoMineral);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ideServidaoMineraria.hashCode();
		hash = hash * prime + this.ideFceLicenciamentoMineral.hashCode();

		return hash;
	}
}