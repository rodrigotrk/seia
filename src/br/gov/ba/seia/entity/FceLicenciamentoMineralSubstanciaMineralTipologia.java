package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.util.Util;

/**
 * The persistent class for the fce_licenciamento_mineral_substancia_mineral
 * database table.
 * 
 */
@Entity
@Table(name = "fce_licenciamento_mineral_substancia_mineral_tipologia")
@NamedQueries({
	@NamedQuery(name = "FceLicenciamentoMineralSubstanciaMineralTipologia.removeByIdeFceLicenciamentoMineral", query = "DELETE FROM FceLicenciamentoMineralSubstanciaMineralTipologia f WHERE f.fceLicenciamentoMineral.ideFceLicenciamentoMineral = :ideFceLicenciamentoMineral"),
	@NamedQuery(name = "FceLicenciamentoMineralSubstanciaMineralTipologia.removeByIdeFceLicenciamentoMineralAndIdeSubstanciaMineralTipologia", query = "DELETE FROM FceLicenciamentoMineralSubstanciaMineralTipologia f WHERE f.fceLicenciamentoMineral.ideFceLicenciamentoMineral = :ideFceLicenciamentoMineral AND f.substanciaMineralTipologia.ideSubstanciaMineralTipologia = :ideSubstanciaMineralTipologia")
})
public class FceLicenciamentoMineralSubstanciaMineralTipologia implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FceLicenciamentoMineralSubstanciaMineralTipologiaPK ideFceLicenciamentoMineralSubstanciaMineralPK;

	@Basic(optional = false)
	@Column(name = "num_producao_anual", nullable = true, precision = 12, scale = 2)
	private Double numProducaoAnual;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_fce_licenciamento_mineral", nullable = false, insertable = false, updatable = false)
	private FceLicenciamentoMineral fceLicenciamentoMineral;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_substancia_mineral_tipologia", nullable = false, insertable = false, updatable = false)
	private SubstanciaMineralTipologia substanciaMineralTipologia;

	@Transient
	private boolean confirmado;
	@Transient
	private boolean edicao;

	public FceLicenciamentoMineralSubstanciaMineralTipologia() {

	}

	public FceLicenciamentoMineralSubstanciaMineralTipologia(FceLicenciamentoMineral fceLicenciamentoMineral, SubstanciaMineralTipologia substanciaMineral) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
		this.substanciaMineralTipologia = substanciaMineral;
	}

	public Double getNumProducaoAnual() {
		return numProducaoAnual;
	}

	public void setNumProducaoAnual(Double numProducaoAnual) {
		this.numProducaoAnual = numProducaoAnual;
	}

	public FceLicenciamentoMineral getFceLicenciamentoMineral() {
		return fceLicenciamentoMineral;
	}

	public void setFceLicenciamentoMineral(FceLicenciamentoMineral fceLicenciamentoMineral) {
		this.fceLicenciamentoMineral = fceLicenciamentoMineral;
	}

	public SubstanciaMineralTipologia getSubstanciaMineralTipologia() {
		return substanciaMineralTipologia;
	}

	public void setSubstanciaMineralTipologia(SubstanciaMineralTipologia substanciaMineral) {
		this.substanciaMineralTipologia = substanciaMineral;
	}

	public boolean isConfirmado() {
		return confirmado;
	}

	public void setConfirmado(boolean confirmado) {
		this.confirmado = confirmado;
	}

	public boolean isEdicao() {
		return edicao;
	}

	public void setEdicao() {
		edicao = true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fceLicenciamentoMineral == null) ? 0 : fceLicenciamentoMineral.hashCode());
		result = prime * result + ((substanciaMineralTipologia == null) ? 0 : substanciaMineralTipologia.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		FceLicenciamentoMineralSubstanciaMineralTipologia other = (FceLicenciamentoMineralSubstanciaMineralTipologia) obj;
		if(fceLicenciamentoMineral == null){
			if(other.fceLicenciamentoMineral != null)
				return false;
		}
		else if(!fceLicenciamentoMineral.equals(other.fceLicenciamentoMineral))
			return false;
		if(substanciaMineralTipologia == null){
			if(other.substanciaMineralTipologia != null)
				return false;
		}
		else if(!substanciaMineralTipologia.equals(other.substanciaMineralTipologia))
			return false;
		return true;
	}

	public FceLicenciamentoMineralSubstanciaMineralTipologiaPK getIdeFceLicenciamentoMineralSubstanciaMineralPK() {
		return ideFceLicenciamentoMineralSubstanciaMineralPK;
	}

	public void setIdeFceLicenciamentoMineralSubstanciaMineralPK(FceLicenciamentoMineralSubstanciaMineralTipologiaPK ideFceLicenciamentoMineralSubstanciaMineralPK) {
		this.ideFceLicenciamentoMineralSubstanciaMineralPK = ideFceLicenciamentoMineralSubstanciaMineralPK;
	}

	public boolean isOutros(){
		return !Util.isNullOuVazio(substanciaMineralTipologia) 
				&& !Util.isNullOuVazio(substanciaMineralTipologia.getIdeSubstanciaMineral()) 
				&& substanciaMineralTipologia.getIdeSubstanciaMineral().isOutros();
	}
}