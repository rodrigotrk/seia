package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ReenquadramentoProcessoAtoObjetivoAtividadeManejoPK implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name="ide_reenquadramento_processo_ato", nullable=false)
	private Integer ideReenquadramentoProcessoAto;
	
	@Column(name="ide_objetivo_atividade_manejo", nullable=false)
	private Integer ideObjetivoAtividadeManejo;
	
	public ReenquadramentoProcessoAtoObjetivoAtividadeManejoPK() {
		super();
	}

	public ReenquadramentoProcessoAtoObjetivoAtividadeManejoPK(Integer ideReenquadramentoProcessoAto, Integer ideObjetivoAtividadeManejo) {
		this.ideReenquadramentoProcessoAto = ideReenquadramentoProcessoAto;
		this.ideObjetivoAtividadeManejo = ideObjetivoAtividadeManejo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideObjetivoAtividadeManejo == null) ? 0 : ideObjetivoAtividadeManejo.hashCode());
		result = prime * result
				+ ((ideReenquadramentoProcessoAto == null) ? 0 : ideReenquadramentoProcessoAto.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReenquadramentoProcessoAtoObjetivoAtividadeManejoPK other = (ReenquadramentoProcessoAtoObjetivoAtividadeManejoPK) obj;
		if (ideObjetivoAtividadeManejo == null) {
			if (other.ideObjetivoAtividadeManejo != null)
				return false;
		} else if (!ideObjetivoAtividadeManejo.equals(other.ideObjetivoAtividadeManejo))
			return false;
		if (ideReenquadramentoProcessoAto == null) {
			if (other.ideReenquadramentoProcessoAto != null)
				return false;
		} else if (!ideReenquadramentoProcessoAto.equals(other.ideReenquadramentoProcessoAto))
			return false;
		return true;
	}

	public Integer getIdeReenquadramentoProcessoAto() {
		return ideReenquadramentoProcessoAto;
	}

	public void setIdeReenquadramentoProcessoAto(Integer ideReenquadramentoProcessoAto) {
		this.ideReenquadramentoProcessoAto = ideReenquadramentoProcessoAto;
	}

	public Integer getIdeObjetivoAtividadeManejo() {
		return ideObjetivoAtividadeManejo;
	}

	public void setIdeObjetivoAtividadeManejo(Integer ideObjetivoAtividadeManejo) {
		this.ideObjetivoAtividadeManejo = ideObjetivoAtividadeManejo;
	}

}
