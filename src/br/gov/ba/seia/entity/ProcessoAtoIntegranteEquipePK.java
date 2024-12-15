package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProcessoAtoIntegranteEquipePK implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
    @Column(name = "ide_processo_ato", nullable=false)
    private Integer ideProcessoAto;
    
    @Column(name = "ide_equipe_integrante", nullable = false)
    private Integer ideIntegranteEquipe;

    public ProcessoAtoIntegranteEquipePK() {
    	
    }

	public ProcessoAtoIntegranteEquipePK(Integer ideProcessoAto,Integer ideIntegranteEquipe) {
		super();
		this.ideProcessoAto = ideProcessoAto;
		this.ideIntegranteEquipe = ideIntegranteEquipe;
	}

	public Integer getIdeProcessoAto() {
		return ideProcessoAto;
	}

	public void setIdeProcessoAto(Integer ideProcessoAto) {
		this.ideProcessoAto = ideProcessoAto;
	}

	public Integer getIdeIntegranteEquipe() {
		return ideIntegranteEquipe;
	}

	public void setIdeIntegranteEquipe(Integer ideIntegranteEquipe) {
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
		ProcessoAtoIntegranteEquipePK other = (ProcessoAtoIntegranteEquipePK) obj;
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
