package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "reenquadramento_tipologia")
public class ReenquadramentoTipologia implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REENQUADRAMENTO_TIPOLOGIA_IDEREENQUADRAMENTOTIPOLOGIA_GENERATOR")
	@SequenceGenerator(name = "REENQUADRAMENTO_TIPOLOGIA_IDEREENQUADRAMENTOTIPOLOGIA_GENERATOR", sequenceName = "REENQUADRAMENTO_TIPOLOGIA_SEQ", allocationSize=1)
	@Column(name = "ide_reenquadramento_tipologia")
	private Integer ideReenquadramentoTipologia;

	@JoinColumn(name = "ide_tipologia")
	@ManyToOne(fetch = FetchType.LAZY)
	private Tipologia ideTipologia;

	@JoinColumn(name = "ide_reenquadramento_processo")
	@ManyToOne(fetch = FetchType.LAZY)
	private ReenquadramentoProcesso ideReenquadramentoProcesso;

	public ReenquadramentoTipologia() {

	}

	public Integer getIdeReenquadramentoTipologia() {
		return ideReenquadramentoTipologia;
	}

	public void setIdeReenquadramentoTipologia(Integer ideReenquadramentoTipologia) {
		this.ideReenquadramentoTipologia = ideReenquadramentoTipologia;
	}

	public Tipologia getIdeTipologia() {
		return ideTipologia;
	}

	public void setIdeTipologia(Tipologia ideTipologia) {
		this.ideTipologia = ideTipologia;
	}

	public ReenquadramentoProcesso getIdeReenquadramentoProcesso() {
		return ideReenquadramentoProcesso;
	}

	public void setIdeReenquadramentoProcesso(ReenquadramentoProcesso ideReenquadramentoProcesso) {
		this.ideReenquadramentoProcesso = ideReenquadramentoProcesso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideReenquadramentoTipologia == null) ? 0 : ideReenquadramentoTipologia.hashCode());
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
		ReenquadramentoTipologia other = (ReenquadramentoTipologia) obj;
		if (ideReenquadramentoTipologia == null) {
			if (other.ideReenquadramentoTipologia != null)
				return false;
		} else if (!ideReenquadramentoTipologia.equals(other.ideReenquadramentoTipologia))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		return Long.valueOf(ideReenquadramentoTipologia);
	}

}