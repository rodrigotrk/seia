package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ReenquadramentoProcessoAtoTipoAtividadeFaunaPK implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name="ide_reenquadramento_processo_ato", nullable=false)
	private Integer ideReenquadramentoProcessoAto;
	
	@Column(name="ide_tipo_atividade_fauna", nullable=false)
	private Integer ideTipoAtividadeFauna;
	
	public ReenquadramentoProcessoAtoTipoAtividadeFaunaPK() {
		super();
	}

	public ReenquadramentoProcessoAtoTipoAtividadeFaunaPK(Integer ideReenquadramentoProcessoAto, Integer ideTipoAtividadeFauna) {
		this.ideReenquadramentoProcessoAto = ideReenquadramentoProcessoAto;
		this.ideTipoAtividadeFauna = ideTipoAtividadeFauna;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideTipoAtividadeFauna == null) ? 0 : ideTipoAtividadeFauna.hashCode());
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
		ReenquadramentoProcessoAtoTipoAtividadeFaunaPK other = (ReenquadramentoProcessoAtoTipoAtividadeFaunaPK) obj;
		if (ideTipoAtividadeFauna == null) {
			if (other.ideTipoAtividadeFauna != null)
				return false;
		} else if (!ideTipoAtividadeFauna.equals(other.ideTipoAtividadeFauna))
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

	public Integer getIdeTipoAtividadeFauna() {
		return ideTipoAtividadeFauna;
	}

	public void setIdeTipoAtividadeFauna(Integer ideTipoAtividadeFauna) {
		this.ideTipoAtividadeFauna = ideTipoAtividadeFauna;
	}

}
