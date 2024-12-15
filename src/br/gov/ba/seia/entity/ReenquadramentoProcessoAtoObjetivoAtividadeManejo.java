package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "reenquadramento_processo_ato_objetivo_atividade_manejo")
public class ReenquadramentoProcessoAtoObjetivoAtividadeManejo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private ReenquadramentoProcessoAtoObjetivoAtividadeManejoPK reenquadramentoProcessoAtoObjetivoAtividadeManejoPK;
	
	@JoinColumn(name="ide_reenquadramento_processo_ato", referencedColumnName="ide_reenquadramento_processo_ato", nullable = false, insertable = false, updatable = false)
	@ManyToOne
	private ReenquadramentoProcessoAto ideReenquadramentoProcessoAto;
	
	@JoinColumn(name="ide_objetivo_atividade_manejo", referencedColumnName="ide_objetivo_atividade_manejo", nullable = false, insertable = false, updatable = false)
	@ManyToOne
	private ObjetivoAtividadeManejo ideObjetivoAtividadeManejo;

	public ReenquadramentoProcessoAtoObjetivoAtividadeManejo() {
		
	}
	
	public void gerarPK() {
		reenquadramentoProcessoAtoObjetivoAtividadeManejoPK = new ReenquadramentoProcessoAtoObjetivoAtividadeManejoPK(
			ideReenquadramentoProcessoAto.getIdeReenquadramentoProcessoAto(),
			ideObjetivoAtividadeManejo.getIdeObjetivoAtividadeManejo()
		);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideObjetivoAtividadeManejo == null) ? 0 : ideObjetivoAtividadeManejo.hashCode());
		result = prime * result + ((reenquadramentoProcessoAtoObjetivoAtividadeManejoPK == null) ? 0
				: reenquadramentoProcessoAtoObjetivoAtividadeManejoPK.hashCode());
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
		ReenquadramentoProcessoAtoObjetivoAtividadeManejo other = (ReenquadramentoProcessoAtoObjetivoAtividadeManejo) obj;
		if (ideObjetivoAtividadeManejo == null) {
			if (other.ideObjetivoAtividadeManejo != null)
				return false;
		} else if (!ideObjetivoAtividadeManejo.equals(other.ideObjetivoAtividadeManejo))
			return false;
		if (reenquadramentoProcessoAtoObjetivoAtividadeManejoPK == null) {
			if (other.reenquadramentoProcessoAtoObjetivoAtividadeManejoPK != null)
				return false;
		} else if (!reenquadramentoProcessoAtoObjetivoAtividadeManejoPK
				.equals(other.reenquadramentoProcessoAtoObjetivoAtividadeManejoPK))
			return false;
		return true;
	}

	public ReenquadramentoProcessoAtoObjetivoAtividadeManejoPK getReenquadramentoProcessoAtoObjetivoAtividadeManejoPK() {
		return reenquadramentoProcessoAtoObjetivoAtividadeManejoPK;
	}

	public void setReenquadramentoProcessoAtoObjetivoAtividadeManejoPK(
			ReenquadramentoProcessoAtoObjetivoAtividadeManejoPK reenquadramentoProcessoAtoObjetivoAtividadeManejoPK) {
		this.reenquadramentoProcessoAtoObjetivoAtividadeManejoPK = reenquadramentoProcessoAtoObjetivoAtividadeManejoPK;
	}

	public ReenquadramentoProcessoAto getIdeReenquadramentoProcessoAto() {
		return ideReenquadramentoProcessoAto;
	}

	public void setIdeReenquadramentoProcessoAto(ReenquadramentoProcessoAto ideReenquadramentoProcessoAto) {
		this.ideReenquadramentoProcessoAto = ideReenquadramentoProcessoAto;
	}

	public ObjetivoAtividadeManejo getIdeObjetivoAtividadeManejo() {
		return ideObjetivoAtividadeManejo;
	}

	public void setIdeObjetivoAtividadeManejo(ObjetivoAtividadeManejo ideObjetivoAtividadeManejo) {
		this.ideObjetivoAtividadeManejo = ideObjetivoAtividadeManejo;
	}

}