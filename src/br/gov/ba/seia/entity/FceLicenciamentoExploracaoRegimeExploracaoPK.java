package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the fce_licenciamento_exploracao_regime_exploracao
 * database table.
 * 
 */
@Embeddable
public class FceLicenciamentoExploracaoRegimeExploracaoPK implements Serializable {
	// default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name = "ide_fce_licenciamento_mineral", insertable = false, updatable = false)
	private Integer ideFceLicenciamentoMineral;

	@Column(name = "ide_regime_exploracao", insertable = false, updatable = false)
	private Integer ideRegimeExploracao;

	public FceLicenciamentoExploracaoRegimeExploracaoPK() {

	}

	public FceLicenciamentoExploracaoRegimeExploracaoPK(FceLicenciamentoMineral fceLicenciamentoMineral, RegimeExploracao ideRegimeExploracao) {
		this.ideFceLicenciamentoMineral = fceLicenciamentoMineral.getIdeFceLicenciamentoMineral();
		this.ideRegimeExploracao = ideRegimeExploracao.getIdeRegimeExploracao();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideFceLicenciamentoMineral == null) ? 0 : ideFceLicenciamentoMineral.hashCode());
		result = prime * result + ((ideRegimeExploracao == null) ? 0 : ideRegimeExploracao.hashCode());
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
		FceLicenciamentoExploracaoRegimeExploracaoPK other = (FceLicenciamentoExploracaoRegimeExploracaoPK) obj;
		if(ideFceLicenciamentoMineral == null){
			if(other.ideFceLicenciamentoMineral != null)
				return false;
		}
		else if(!ideFceLicenciamentoMineral.equals(other.ideFceLicenciamentoMineral))
			return false;
		if(ideRegimeExploracao == null){
			if(other.ideRegimeExploracao != null)
				return false;
		}
		else if(!ideRegimeExploracao.equals(other.ideRegimeExploracao))
			return false;
		return true;
	}

}