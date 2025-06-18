package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;



@Entity
@Table(name="processo_ato_integrante_equipe")
public class ProcessoAtoIntegranteEquipe implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private ProcessoAtoIntegranteEquipePK processoAtoIntegranteEquipePK;

	@JoinColumn(name="ide_processo_ato", referencedColumnName="ide_processo_ato", insertable=false, updatable=false, nullable=false)
	@ManyToOne(optional=false, fetch=FetchType.EAGER)
	private ProcessoAto ideProcessoAto;

	@JoinColumn(name="ide_equipe_integrante", referencedColumnName="ide_integrante_equipe", insertable=false, updatable=false, nullable=false)
	@ManyToOne(optional=false, fetch=FetchType.EAGER)
	private IntegranteEquipe ideIntegranteEquipe;

	public ProcessoAtoIntegranteEquipe() {
		
	}
	
	public ProcessoAtoIntegranteEquipePK getProcessoAtoIntegranteEquipePK() {
		return processoAtoIntegranteEquipePK;
	}

	public void setProcessoAtoIntegranteEquipePK(
			ProcessoAtoIntegranteEquipePK processoAtoIntegranteEquipePK) {
		this.processoAtoIntegranteEquipePK = processoAtoIntegranteEquipePK;
	}
	
	public ProcessoAto getIdeProcessoAto() {
		return ideProcessoAto;
	}

	public void setIdeProcessoAto(ProcessoAto ideProcessoAto) {
		this.ideProcessoAto = ideProcessoAto;
	}

	public IntegranteEquipe getIdeIntegranteEquipe() {
		return ideIntegranteEquipe;
	}

	public void setIdeIntegranteEquipe(IntegranteEquipe ideIntegranteEquipe) {
		this.ideIntegranteEquipe = ideIntegranteEquipe;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideIntegranteEquipe == null) ? 0 : ideIntegranteEquipe
						.hashCode());
		result = prime * result
				+ ((ideProcessoAto == null) ? 0 : ideProcessoAto.hashCode());
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
		ProcessoAtoIntegranteEquipe other = (ProcessoAtoIntegranteEquipe) obj;
		if (ideIntegranteEquipe == null) {
			if (other.ideIntegranteEquipe != null)
				return false;
		} else if (!ideIntegranteEquipe.equals(other.ideIntegranteEquipe))
			return false;
		if (ideProcessoAto == null) {
			if (other.ideProcessoAto != null)
				return false;
		} else if (!ideProcessoAto.equals(other.ideProcessoAto))
			return false;
		return true;
	}	
}