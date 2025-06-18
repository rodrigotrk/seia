package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;


/**
 * The persistent class for the fce_lancamento_efluente_caracterizacao database table.
 * 
 */
@Entity
@Table(name="fce_lancamento_efluente_caracterizacao")
public class FceLancamentoEfluenteCaracterizacao implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceLancamentoEfluenteCaracterizacaoPK ideFceLancamentoEfluenteCaracterizacaoPK;

	@NotNull
	@JoinColumn(name = "ide_fce_lancamento_efluente", referencedColumnName = "ide_fce_lancamento_efluente", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private FceLancamentoEfluente ideFceLancamentoEfluente;

	@NotNull
	@JoinColumn(name = "ide_caracterizacao_efluente", referencedColumnName = "ide_caracterizacao_efluente", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private CaracterizacaoEfluente ideCaracterizacaoEfluente;

	public FceLancamentoEfluenteCaracterizacao() {
	}

	public FceLancamentoEfluenteCaracterizacao(FceLancamentoEfluenteCaracterizacaoPK pk) {
		this.ideFceLancamentoEfluenteCaracterizacaoPK = pk;
	}

	public FceLancamentoEfluenteCaracterizacao(FceLancamentoEfluente fceLancamentoEfluente, CaracterizacaoEfluente caracterizacaoEfluente) {
		this.ideFceLancamentoEfluenteCaracterizacaoPK = new FceLancamentoEfluenteCaracterizacaoPK(fceLancamentoEfluente, caracterizacaoEfluente);
	}

	public FceLancamentoEfluenteCaracterizacaoPK getIdeFceLancamentoEfluenteCaracterizacaoPK() {
		return ideFceLancamentoEfluenteCaracterizacaoPK;
	}

	public void setIdeFceLancamentoEfluenteCaracterizacaoPK(
			FceLancamentoEfluenteCaracterizacaoPK ideFceLancamentoEfluenteCaracterizacaoPK) {
		this.ideFceLancamentoEfluenteCaracterizacaoPK = ideFceLancamentoEfluenteCaracterizacaoPK;
	}

	public FceLancamentoEfluente getIdeFceLancamentoEfluente() {
		return ideFceLancamentoEfluente;
	}

	public void setIdeFceLancamentoEfluente(
			FceLancamentoEfluente ideFceLancamentoEfluente) {
		this.ideFceLancamentoEfluente = ideFceLancamentoEfluente;
	}

	public CaracterizacaoEfluente getIdeCaracterizacaoEfluente() {
		return ideCaracterizacaoEfluente;
	}

	public void setIdeCaracterizacaoEfluente(
			CaracterizacaoEfluente ideCaracterizacaoEfluente) {
		this.ideCaracterizacaoEfluente = ideCaracterizacaoEfluente;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideFceLancamentoEfluenteCaracterizacaoPK == null) ? 0
						: ideFceLancamentoEfluenteCaracterizacaoPK.hashCode());
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
		FceLancamentoEfluenteCaracterizacao other = (FceLancamentoEfluenteCaracterizacao) obj;
		if (ideFceLancamentoEfluenteCaracterizacaoPK == null) {
			if (other.ideFceLancamentoEfluenteCaracterizacaoPK != null) {
				return false;
			}
		} else if (!ideFceLancamentoEfluenteCaracterizacaoPK
				.equals(other.ideFceLancamentoEfluenteCaracterizacaoPK)) {
			return false;
		}
		return true;
	}
}