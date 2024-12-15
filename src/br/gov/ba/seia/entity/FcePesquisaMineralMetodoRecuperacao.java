package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "fce_pesquisa_mineral_metodo_recuperacao")
@NamedQueries({
	@NamedQuery(name = "FcePesquisaMineralMetodoRecuperacao.removeByIdeFcePesquisaMineral", query = "DELETE FROM FcePesquisaMineralMetodoRecuperacao fpmmr WHERE fpmmr.ideFcePesquisaMineral.ideFcePesquisaMineral = :ideFcePesquisaMineral")
})

public class FcePesquisaMineralMetodoRecuperacao implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FcePesquisaMineralMetodoRecuperacaoPK ideFcePesquisaMineralMetodoRecuperacaoPK;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce_pesquisa_mineral", nullable = false, insertable = false, updatable = false)
	private FcePesquisaMineral ideFcePesquisaMineral;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_metodo_recuperacao_intervencao", nullable = false, insertable = false, updatable = false)
	private MetodoRecuperacaoIntervencao ideMetodoRecuperacaoIntervencao;

	public FcePesquisaMineralMetodoRecuperacao() {
	}

	public FcePesquisaMineralMetodoRecuperacao(FcePesquisaMineral fcePesquisaMineral, MetodoRecuperacaoIntervencao metodoRecuperacaoIntervencao) {
		this.ideFcePesquisaMineral = fcePesquisaMineral;
		this.ideMetodoRecuperacaoIntervencao = metodoRecuperacaoIntervencao;
		this.ideFcePesquisaMineralMetodoRecuperacaoPK = new FcePesquisaMineralMetodoRecuperacaoPK(fcePesquisaMineral, metodoRecuperacaoIntervencao);
	}

	public FcePesquisaMineral getFcePesquisaMineral() {
		return ideFcePesquisaMineral;
	}

	public void setFcePesquisaMineral(FcePesquisaMineral fcePesquisaMineral) {
		this.ideFcePesquisaMineral = fcePesquisaMineral;
	}

	public MetodoRecuperacaoIntervencao getMetodoRecuperacaoIntervencao() {
		return ideMetodoRecuperacaoIntervencao;
	}

	public void setMetodoRecuperacaoIntervencao(MetodoRecuperacaoIntervencao metodoRecuperacaoIntervencao) {
		this.ideMetodoRecuperacaoIntervencao = metodoRecuperacaoIntervencao;
	}
	
	public FcePesquisaMineralMetodoRecuperacaoPK getIdeFcePesquisaMineralMetodoRecuperacaoPK() {
		return ideFcePesquisaMineralMetodoRecuperacaoPK;
	}

	public void setIdeFcePesquisaMineralMetodoRecuperacaoPK(FcePesquisaMineralMetodoRecuperacaoPK ideFcePesquisaMineralMetodoRecuperacaoPK) {
		this.ideFcePesquisaMineralMetodoRecuperacaoPK = ideFcePesquisaMineralMetodoRecuperacaoPK;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideFcePesquisaMineralMetodoRecuperacaoPK == null) ? 0 : ideFcePesquisaMineralMetodoRecuperacaoPK.hashCode());
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
		FcePesquisaMineralMetodoRecuperacao other = (FcePesquisaMineralMetodoRecuperacao) obj;
		if(ideFcePesquisaMineralMetodoRecuperacaoPK == null){
			if(other.ideFcePesquisaMineralMetodoRecuperacaoPK != null)
				return false;
		}
		else if(!ideFcePesquisaMineralMetodoRecuperacaoPK.equals(other.ideFcePesquisaMineralMetodoRecuperacaoPK))
			return false;
		return true;
	}

}