package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the fce_licenciamento_ambiental_metodo_recuperacao
 * database table.
 * 
 */
@Embeddable
public class FceLicenciamentoMineralMetodoRecuperacaoPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "ide_fce_licenciamento_mineral", insertable = false, updatable = false)
	private Integer ideFceLicenciamentoMineral;

	@Column(name = "ide_metodo_recuperacao_intervencao", insertable = false, updatable = false)
	private Integer ideMetodoRecuperacaoIntervencao;

	public FceLicenciamentoMineralMetodoRecuperacaoPK() {
	}

	public FceLicenciamentoMineralMetodoRecuperacaoPK(FceLicenciamentoMineral fceLicenciamentoMineral, MetodoRecuperacaoIntervencao metodoRecuperacaoIntervencao) {
		this.ideFceLicenciamentoMineral = fceLicenciamentoMineral.getIdeFceLicenciamentoMineral();
		this.ideMetodoRecuperacaoIntervencao = metodoRecuperacaoIntervencao.getIdeMetodoRecuperacaoIntervencao();
	}

	public Integer getIdeFceLicenciamentoMineral() {
		return this.ideFceLicenciamentoMineral;
	}

	public void setIdeFceLicenciamentoMineral(Integer ideFceLicenciamentoMineral) {
		this.ideFceLicenciamentoMineral = ideFceLicenciamentoMineral;
	}

	public Integer getIdeMetodoRecuperacaoIntervencao() {
		return this.ideMetodoRecuperacaoIntervencao;
	}

	public void setIdeMetodoRecuperacaoIntervencao(Integer ideMetodoRecuperacaoIntervencao) {
		this.ideMetodoRecuperacaoIntervencao = ideMetodoRecuperacaoIntervencao;
	}

	public boolean equals(Object other) {
		if(this == other){
			return true;
		}
		if(!(other instanceof FceLicenciamentoMineralMetodoRecuperacaoPK)){
			return false;
		}
		FceLicenciamentoMineralMetodoRecuperacaoPK castOther = (FceLicenciamentoMineralMetodoRecuperacaoPK) other;
		return this.ideFceLicenciamentoMineral.equals(castOther.ideFceLicenciamentoMineral) && this.ideMetodoRecuperacaoIntervencao.equals(castOther.ideMetodoRecuperacaoIntervencao);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ideFceLicenciamentoMineral.hashCode();
		hash = hash * prime + this.ideMetodoRecuperacaoIntervencao.hashCode();

		return hash;
	}
}