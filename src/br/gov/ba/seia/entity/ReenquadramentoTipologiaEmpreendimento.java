package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "reenquadramento_tipologia_empreendimento")
@NamedQuery(name = "ReenquadramentoTipologiaEmpreendimento.findAll", query = "SELECT r FROM ReenquadramentoTipologiaEmpreendimento r")
public class ReenquadramentoTipologiaEmpreendimento implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@EmbeddedId
	private ReenquadramentoTipologiaEmpreendimentoPK reenquadramentoTipologiaEmpreendimentoPK;

	@JoinColumn(name = "ide_tipologia", referencedColumnName = "ide_tipologia",nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Tipologia ideTipologia;
	
	@JoinColumn(name = "ide_reenquadramento_processo_ato", 
	referencedColumnName = "ide_reenquadramento_processo_ato", nullable = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private ReenquadramentoProcessoAto ideReenquadramentoProcessoAto;

	public ReenquadramentoTipologiaEmpreendimento() {
		
	}
	
	public void gerarPK() {
		reenquadramentoTipologiaEmpreendimentoPK = new  ReenquadramentoTipologiaEmpreendimentoPK(
				ideReenquadramentoProcessoAto.getIdeReenquadramentoProcessoAto(),
				ideTipologia.getIdeTipologia()
		); 
	}	
	
	public ReenquadramentoTipologiaEmpreendimento(ReenquadramentoTipologiaEmpreendimentoPK reenquadramentoTipologiaEmpreendimentoPK) {
		this.reenquadramentoTipologiaEmpreendimentoPK = reenquadramentoTipologiaEmpreendimentoPK;
	}
	public ReenquadramentoTipologiaEmpreendimentoPK getReenquadramentoTipologiaEmpreendimentoPK() {
		return reenquadramentoTipologiaEmpreendimentoPK;
	}

	public void setReenquadramentoTipologiaEmpreendimentoPK(ReenquadramentoTipologiaEmpreendimentoPK reenquadramentoTipologiaEmpreendimentoPK) {
		this.reenquadramentoTipologiaEmpreendimentoPK = reenquadramentoTipologiaEmpreendimentoPK;
	}

	public ReenquadramentoProcessoAto getIdeReenquadramentoProcessoAto() {
		return ideReenquadramentoProcessoAto;
	}

	public void setIdeReenquadramentoProcessoAto(ReenquadramentoProcessoAto ideReenquadramentoProcessoAto) {
		this.ideReenquadramentoProcessoAto = ideReenquadramentoProcessoAto;
	}

	public Tipologia getIdeTipologia() {
		return ideTipologia;
	}

	public void setIdeTipologia(Tipologia ideTipologia) {
		this.ideTipologia = ideTipologia;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((reenquadramentoTipologiaEmpreendimentoPK == null) ? 0
				: reenquadramentoTipologiaEmpreendimentoPK.hashCode());
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
		ReenquadramentoTipologiaEmpreendimento other = (ReenquadramentoTipologiaEmpreendimento) obj;
		if (reenquadramentoTipologiaEmpreendimentoPK == null) {
			if (other.reenquadramentoTipologiaEmpreendimentoPK != null)
				return false;
		} else if (!reenquadramentoTipologiaEmpreendimentoPK.equals(other.reenquadramentoTipologiaEmpreendimentoPK))
			return false;
		return true;
	}

}