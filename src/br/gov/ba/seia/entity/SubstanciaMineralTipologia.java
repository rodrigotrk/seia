package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import br.gov.ba.seia.enumerator.TipologiaEnum;


/**
 * The persistent class for the substancia_mineral_tipologia database table.
 * 
 */
@Entity
@Table(name="substancia_mineral_tipologia")
@NamedQuery(name="SubstanciaMineralTipologia.findAll", query="SELECT s FROM SubstanciaMineralTipologia s")
public class SubstanciaMineralTipologia implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Basic(optional = false)
	@SequenceGenerator(name="SUBSTANCIA_MINERAL_TIPOLOGIA_IDE_SUBSTANCIAMINERAL_TIPOLOGIA_GENERATOR", sequenceName="SUBSTANCIA_MINERAL_TIPOLOGIA_IDE_SUBSTANCIA_MINERAL_TIPOLOGIA_SEQ")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SUBSTANCIA_MINERAL_TIPOLOGIA_IDE_SUBSTANCIAMINERAL_TIPOLOGIA_GENERATOR")
	@NotNull
	@Column(name="ide_substancia_mineral_tipologia", nullable = false)
	private Integer ideSubstanciaMineralTipologia;

	@JoinColumn(name = "ide_substancia_mineral", referencedColumnName = "ide_substancia_mineral", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch=FetchType.LAZY)
	private SubstanciaMineral ideSubstanciaMineral;

	@JoinColumn(name = "ide_tipologia", referencedColumnName = "ide_tipologia", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch=FetchType.LAZY)
	private Tipologia ideTipologia;
	
	@Transient
	private Double valAtividade;

	public SubstanciaMineralTipologia() {
	}

	public SubstanciaMineralTipologia(Integer rochasBetuminosas, TipologiaEnum combustiveis) {
		// Auto-generated constructor stub	
	}

	public Integer getIdeSubstanciaMineralTipologia() {
		return this.ideSubstanciaMineralTipologia;
	}

	public void setIdeSubstanciaMineralTipologia(Integer ideSubstanciaMineralTipologia) {
		this.ideSubstanciaMineralTipologia = ideSubstanciaMineralTipologia;
	}

	public SubstanciaMineral getIdeSubstanciaMineral() {
		return this.ideSubstanciaMineral;
	}

	public void setIdeSubstanciaMineral(SubstanciaMineral ideSubstanciaMineral) {
		this.ideSubstanciaMineral = ideSubstanciaMineral;
	}

	public Tipologia getIdeTipologia() {
		return this.ideTipologia;
	}

	public void setIdeTipologia(Tipologia ideTipologia) {
		this.ideTipologia = ideTipologia;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideSubstanciaMineralTipologia == null) ? 0
						: ideSubstanciaMineralTipologia.hashCode());
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
		SubstanciaMineralTipologia other = (SubstanciaMineralTipologia) obj;
		if (ideSubstanciaMineralTipologia == null) {
			if (other.ideSubstanciaMineralTipologia != null)
				return false;
		} else if (!ideSubstanciaMineralTipologia
				.equals(other.ideSubstanciaMineralTipologia))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SubstanciaMineralTipologia ["
				+ ideSubstanciaMineral.getNomSubstanciaMineral() + "/ " + ideTipologia.getDesTipologia() + "]";
	}

	public Double getValAtividade() {
		return valAtividade;
	}

	public void setValAtividade(Double valAtividade) {
		this.valAtividade = valAtividade;
	}
	

	public String getLabelUnidadeMedidaSubstanciaMineral(){
		Integer ROCHAS_BETUMINOSAS = 149;
		if(this.equals(new SubstanciaMineralTipologia(ROCHAS_BETUMINOSAS, TipologiaEnum.COMBUSTIVEIS))){
			return "(m³/Ano)";
		}
		return "(t/Ano)";
	}
}