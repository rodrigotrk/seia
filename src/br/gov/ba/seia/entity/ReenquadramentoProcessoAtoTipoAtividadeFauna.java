package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "reenquadramento_processo_ato_tipo_atividade_fauna")
public class ReenquadramentoProcessoAtoTipoAtividadeFauna implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private ReenquadramentoProcessoAtoTipoAtividadeFaunaPK reenquadramentoProcessoAtoTipoAtividadeFaunaPK;
	
	@JoinColumn(name="ide_reenquadramento_processo_ato", referencedColumnName="ide_reenquadramento_processo_ato", insertable = false, updatable = false)
	@ManyToOne
	private ReenquadramentoProcessoAto ideReenquadramentoProcessoAto;
	
	@JoinColumn(name="ide_tipo_atividade_fauna", referencedColumnName="ide_tipo_atividade_fauna", insertable = false, updatable = false)
	@ManyToOne
	private TipoAtividadeFauna ideTipoAtividadeFauna;

	/**
	 * Construtor vazio
	 *
	 */
	public ReenquadramentoProcessoAtoTipoAtividadeFauna() {
		
	}
	
	public void gerarPK() {
		reenquadramentoProcessoAtoTipoAtividadeFaunaPK = new ReenquadramentoProcessoAtoTipoAtividadeFaunaPK(
			ideReenquadramentoProcessoAto.getIdeReenquadramentoProcessoAto(),
			ideTipoAtividadeFauna.getIdeTipoAtividadeFauna()
		);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideReenquadramentoProcessoAto == null) ? 0 : ideReenquadramentoProcessoAto.hashCode());
		result = prime * result + ((reenquadramentoProcessoAtoTipoAtividadeFaunaPK == null) ? 0
				: reenquadramentoProcessoAtoTipoAtividadeFaunaPK.hashCode());
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
		ReenquadramentoProcessoAtoTipoAtividadeFauna other = (ReenquadramentoProcessoAtoTipoAtividadeFauna) obj;
		if (ideReenquadramentoProcessoAto == null) {
			if (other.ideReenquadramentoProcessoAto != null)
				return false;
		} else if (!ideReenquadramentoProcessoAto.equals(other.ideReenquadramentoProcessoAto))
			return false;
		if (reenquadramentoProcessoAtoTipoAtividadeFaunaPK == null) {
			if (other.reenquadramentoProcessoAtoTipoAtividadeFaunaPK != null)
				return false;
		} else if (!reenquadramentoProcessoAtoTipoAtividadeFaunaPK
				.equals(other.reenquadramentoProcessoAtoTipoAtividadeFaunaPK))
			return false;
		return true;
	}

	public ReenquadramentoProcessoAtoTipoAtividadeFaunaPK getReenquadramentoProcessoAtoTipoAtividadeFaunaPK() {
		return reenquadramentoProcessoAtoTipoAtividadeFaunaPK;
	}

	public void setReenquadramentoProcessoAtoTipoAtividadeFaunaPK(
			ReenquadramentoProcessoAtoTipoAtividadeFaunaPK reenquadramentoProcessoAtoTipoAtividadeFaunaPK) {
		this.reenquadramentoProcessoAtoTipoAtividadeFaunaPK = reenquadramentoProcessoAtoTipoAtividadeFaunaPK;
	}

	public ReenquadramentoProcessoAto getIdeReenquadramentoProcessoAto() {
		return ideReenquadramentoProcessoAto;
	}

	public void setIdeReenquadramentoProcessoAto(ReenquadramentoProcessoAto ideReenquadramentoProcessoAto) {
		this.ideReenquadramentoProcessoAto = ideReenquadramentoProcessoAto;
	}

	public TipoAtividadeFauna getIdeTipoAtividadeFauna() {
		return ideTipoAtividadeFauna;
	}

	public void setIdeTipoAtividadeFauna(TipoAtividadeFauna ideTipoAtividadeFauna) {
		this.ideTipoAtividadeFauna = ideTipoAtividadeFauna;
	}

}