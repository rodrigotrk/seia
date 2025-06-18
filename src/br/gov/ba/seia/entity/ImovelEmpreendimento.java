package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "imovel_empreendimento")
public class ImovelEmpreendimento implements Serializable {
	private static final long serialVersionUID = -2816736009439220044L;
	
	@EmbeddedId
	protected ImovelEmpreendimentoPK imovelEmpreendimentoPK;

	@NotNull
	@JoinColumn(name = "ide_imovel", referencedColumnName = "ide_imovel", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Imovel ideImovel;
	
	@NotNull
	@JoinColumn(name = "ide_empreendimento", referencedColumnName = "ide_empreendimento", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Empreendimento ideEmpreendimento;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "dtc_associacao")
	private Date dtcAssociacao;

	public ImovelEmpreendimento() {}

	public ImovelEmpreendimento(ImovelEmpreendimentoPK imovelEmpreendimentoPK) {
	
		super();
		this.imovelEmpreendimentoPK = imovelEmpreendimentoPK;
	}
	
	public ImovelEmpreendimentoPK getImovelEmpreendimentoPK() {
		return imovelEmpreendimentoPK;
	}
	
	public void setImovelEmpreendimentoPK(ImovelEmpreendimentoPK imovelEmpreendimentoPK) {
		this.imovelEmpreendimentoPK = imovelEmpreendimentoPK;
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
	
	public Date getDtcAssociacao() {
		return dtcAssociacao;
	}
	
	public void setDtcAssociacao(Date dtcAssociacao) {
		this.dtcAssociacao = dtcAssociacao;
	}

	@Override
	public int hashCode() {
	
		final int prime = 31;
		int result = 1;
		result = prime * result + ((imovelEmpreendimentoPK == null) ? 0 : imovelEmpreendimentoPK.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
	
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ImovelEmpreendimento other = (ImovelEmpreendimento) obj;
		if (imovelEmpreendimentoPK == null) {
			if (other.imovelEmpreendimentoPK != null) return false;
		} else if (!imovelEmpreendimentoPK.equals(other.imovelEmpreendimentoPK)) return false;
		return true;
	}

	@Override
	public String toString() {
		return "ImovelEmpreendimento [imovelEmpreendimentoPK=" + imovelEmpreendimentoPK + "]";
	}
}