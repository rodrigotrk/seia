package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the fce_lancamento_efluente_caracterizacao database table.
 * 
 */
@Embeddable
public class FceLancamentoEfluenteCaracterizacaoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ide_fce_lancamento_efluente")
	private Integer ideFceLancamentoEfluente;

	@Column(name="ide_caracterizacao_efluente")
	private Integer ideCaracterizacaoEfluente;

	public FceLancamentoEfluenteCaracterizacaoPK() {
	}

	public FceLancamentoEfluenteCaracterizacaoPK(FceLancamentoEfluente fceLancamentoEfluente, CaracterizacaoEfluente caracterizacaoEfluente){
		ideFceLancamentoEfluente = fceLancamentoEfluente.getIdeFceLancamentoEfluente();
		ideCaracterizacaoEfluente = caracterizacaoEfluente.getIdeCaracterizacaoEfluente();
	}

	public Integer getIdeFceLancamentoEfluente() {
		return ideFceLancamentoEfluente;
	}
	public void setIdeFceLancamentoEfluente(Integer ideFceLancamentoEfluente) {
		this.ideFceLancamentoEfluente = ideFceLancamentoEfluente;
	}
	public Integer getIdeCaracterizacaoEfluente() {
		return ideCaracterizacaoEfluente;
	}
	public void setIdeCaracterizacaoEfluente(Integer ideCaracterizacaoEfluente) {
		this.ideCaracterizacaoEfluente = ideCaracterizacaoEfluente;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideCaracterizacaoEfluente == null) ? 0
						: ideCaracterizacaoEfluente.hashCode());
		result = prime
				* result
				+ ((ideFceLancamentoEfluente == null) ? 0
						: ideFceLancamentoEfluente.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FceLancamentoEfluenteCaracterizacaoPK other = (FceLancamentoEfluenteCaracterizacaoPK) obj;
		if (ideCaracterizacaoEfluente == null) {
			if (other.ideCaracterizacaoEfluente != null) {
				return false;
			}
		} else if (!ideCaracterizacaoEfluente
				.equals(other.ideCaracterizacaoEfluente)) {
			return false;
		}
		if (ideFceLancamentoEfluente == null) {
			if (other.ideFceLancamentoEfluente != null) {
				return false;
			}
		} else if (!ideFceLancamentoEfluente
				.equals(other.ideFceLancamentoEfluente)) {
			return false;
		}
		return true;
	}
}