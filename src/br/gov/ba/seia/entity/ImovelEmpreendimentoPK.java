package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
public class ImovelEmpreendimentoPK implements Serializable {

	private static final long serialVersionUID = -2204630343910944154L;

	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_imovel", nullable = false)
	private Imovel ideImovel;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "Ide_empreendimento", nullable = false)
	private Empreendimento ideEmpreendimento;

	public ImovelEmpreendimentoPK() {}

	public ImovelEmpreendimentoPK(Imovel ideImovel, Empreendimento ideEmpreendimento) {
	
		super();
		this.ideImovel = ideImovel;
		this.ideEmpreendimento = ideEmpreendimento;
	}
	
	public Imovel getIdeImovel() {
		return ideImovel;
	}
	
	public void setIdeImovel(Imovel ideImovel) {
		this.ideImovel = ideImovel;
	}

	public Empreendimento getIdeEmpreendimento() {
		return ideEmpreendimento;
	}

	public void setIdeEmpreendimento(Empreendimento ideEmpreendimento) {
		this.ideEmpreendimento = ideEmpreendimento;
	}

	@Override
	public int hashCode() {
	
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideEmpreendimento == null) ? 0 : ideEmpreendimento.hashCode());
		result = prime * result + ((ideImovel == null) ? 0 : ideImovel.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
	
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ImovelEmpreendimentoPK other = (ImovelEmpreendimentoPK) obj;
		if (ideEmpreendimento == null) {
			if (other.ideEmpreendimento != null) return false;
		} else if (!ideEmpreendimento.equals(other.ideEmpreendimento)) return false;
		if (ideImovel == null) {
			if (other.ideImovel != null) return false;
		} else if (!ideImovel.equals(other.ideImovel)) return false;
		return true;
	}

	@Override
	public String toString() {
		return "ImovelEmpreendimentoPK [ideImovel=" + ideImovel + ", ideEmpreendimento=" + ideEmpreendimento + "]";
	}
}