package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the fce_licenciamento_ambiental_metodo_recuperacao
 * database table.
 * 
 */
@Entity
@Table(name = "fce_licenciamento_mineral_metodo_recuperacao")
@NamedQuery(name = "FceLicenciamentoMineralMetodoRecuperacao.removeByIdeFceLicenciamentoMineral", query = "DELETE FROM FceLicenciamentoMineralMetodoRecuperacao f WHERE f.fceLicenciamentoMineral.ideFceLicenciamentoMineral = :ideFceLicenciamentoMineral")
public class FceLicenciamentoMineralMetodoRecuperacao implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceLicenciamentoMineralMetodoRecuperacaoPK ideFceLicenciamentoAmbientalMetodoRecuperacaoPK;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce_licenciamento_mineral", nullable = false, insertable = false, updatable = false)
	private FceLicenciamentoMineral fceLicenciamentoMineral;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_metodo_recuperacao_intervencao", nullable = false, insertable = false, updatable = false)
	private MetodoRecuperacaoIntervencao metodoRecuperacaoIntervencao;

	public FceLicenciamentoMineralMetodoRecuperacao() {
	}

	public FceLicenciamentoMineralMetodoRecuperacao(FceLicenciamentoMineral fceLicenciamentoMineral, MetodoRecuperacaoIntervencao metodoRecuperacaoIntervencao) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
		this.metodoRecuperacaoIntervencao = metodoRecuperacaoIntervencao;
		this.ideFceLicenciamentoAmbientalMetodoRecuperacaoPK = new FceLicenciamentoMineralMetodoRecuperacaoPK(fceLicenciamentoMineral, metodoRecuperacaoIntervencao);
	}

	public FceLicenciamentoMineral getFceLicenciamentoMineral() {
		return fceLicenciamentoMineral;
	}

	public void setFceLicenciamentoMineral(FceLicenciamentoMineral fceLicenciamentoMineral) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
	}

	public MetodoRecuperacaoIntervencao getMetodoRecuperacaoIntervencao() {
		return metodoRecuperacaoIntervencao;
	}

	public void setMetodoRecuperacaoIntervencao(MetodoRecuperacaoIntervencao metodoRecuperacaoIntervencao) {
		this.metodoRecuperacaoIntervencao = metodoRecuperacaoIntervencao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideFceLicenciamentoAmbientalMetodoRecuperacaoPK == null) ? 0 : ideFceLicenciamentoAmbientalMetodoRecuperacaoPK.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		FceLicenciamentoMineralMetodoRecuperacao other = (FceLicenciamentoMineralMetodoRecuperacao) obj;
		if(ideFceLicenciamentoAmbientalMetodoRecuperacaoPK == null){
			if(other.ideFceLicenciamentoAmbientalMetodoRecuperacaoPK != null)
				return false;
		}
		else if(!ideFceLicenciamentoAmbientalMetodoRecuperacaoPK.equals(other.ideFceLicenciamentoAmbientalMetodoRecuperacaoPK))
			return false;
		return true;
	}

}