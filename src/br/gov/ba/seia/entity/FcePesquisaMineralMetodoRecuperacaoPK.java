package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class FcePesquisaMineralMetodoRecuperacaoPK implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "ide_fce_pesquisa_mineral", insertable = false, updatable = false)
	private Integer ideFcePesquisaMineral;

	@Column(name = "ide_metodo_recuperacao_intervencao", insertable = false, updatable = false)
	private Integer ideMetodoRecuperacaoIntervencao;

	public FcePesquisaMineralMetodoRecuperacaoPK() {
	}

	public FcePesquisaMineralMetodoRecuperacaoPK(FcePesquisaMineral fcePesquisaMineral, MetodoRecuperacaoIntervencao metodoRecuperacaoIntervencao) {
		this.ideFcePesquisaMineral = fcePesquisaMineral.getIdeFcePesquisaMineral();
		this.ideMetodoRecuperacaoIntervencao = metodoRecuperacaoIntervencao.getIdeMetodoRecuperacaoIntervencao();
	}

	public Integer getIdeFcePesquisaMineral() {
		return this.ideFcePesquisaMineral;
	}

	public void setIdeFcePesquisaMineral(Integer ideFcePesquisaMineral) {
		this.ideFcePesquisaMineral = ideFcePesquisaMineral;
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
		if(!(other instanceof FcePesquisaMineralMetodoRecuperacaoPK)){
			return false;
		}
		FcePesquisaMineralMetodoRecuperacaoPK castOther = (FcePesquisaMineralMetodoRecuperacaoPK) other;
		return this.ideFcePesquisaMineral.equals(castOther.ideFcePesquisaMineral) && this.ideMetodoRecuperacaoIntervencao.equals(castOther.ideMetodoRecuperacaoIntervencao);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.ideFcePesquisaMineral.hashCode();
		hash = hash * prime + this.ideMetodoRecuperacaoIntervencao.hashCode();

		return hash;
	}
}