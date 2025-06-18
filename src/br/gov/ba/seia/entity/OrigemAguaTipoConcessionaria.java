package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the origem_agua_tipo_concessionaria database table.
 * 
 */
@Entity
@Table(name="origem_agua_tipo_concessionaria")
@NamedQueries({
	@NamedQuery(name="OrigemAguaTipoConcessionaria.removerOrigemAguaTipoConcessionaria", query="DELETE FROM OrigemAguaTipoConcessionaria s WHERE s.caracterizacaoAmbientalOrigemAgua = :ideCaracterizacaoAmbientalOrigemAgua")	
})
public class OrigemAguaTipoConcessionaria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="ORIGEM_AGUA_TIPO_CONCESSIONARIA_IDEORIGEMAGUATIPOCONCESSIONARIA_GENERATOR", sequenceName="ORIGEM_AGUA_TIPO_CONCESSIONARIA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ORIGEM_AGUA_TIPO_CONCESSIONARIA_IDEORIGEMAGUATIPOCONCESSIONARIA_GENERATOR")
	@Column(name="ide_origem_agua_tipo_concessionaria")
	private Integer ideOrigemAguaTipoConcessionaria;

	//bi-directional many-to-one association to CaracterizacaoAmbientalOrigemAgua
	@ManyToOne
	@JoinColumn(name="ide_caracterizacao_ambiental_origem_agua")
	private CaracterizacaoAmbientalOrigemAgua caracterizacaoAmbientalOrigemAgua;

	//bi-directional many-to-one association to TipoConcessionaria
	@ManyToOne
	@JoinColumn(name="ide_tipo_concessionaria")
	private TipoConcessionaria tipoConcessionaria;

	public OrigemAguaTipoConcessionaria() {
	}

	public Integer getIdeOrigemAguaTipoConcessionaria() {
		return this.ideOrigemAguaTipoConcessionaria;
	}

	public void setIdeOrigemAguaTipoConcessionaria(Integer ideOrigemAguaTipoConcessionaria) {
		this.ideOrigemAguaTipoConcessionaria = ideOrigemAguaTipoConcessionaria;
	}

	public CaracterizacaoAmbientalOrigemAgua getCaracterizacaoAmbientalOrigemAgua() {
		return this.caracterizacaoAmbientalOrigemAgua;
	}

	public void setCaracterizacaoAmbientalOrigemAgua(CaracterizacaoAmbientalOrigemAgua caracterizacaoAmbientalOrigemAgua) {
		this.caracterizacaoAmbientalOrigemAgua = caracterizacaoAmbientalOrigemAgua;
	}

	public TipoConcessionaria getTipoConcessionaria() {
		return this.tipoConcessionaria;
	}

	public void setTipoConcessionaria(TipoConcessionaria tipoConcessionaria) {
		this.tipoConcessionaria = tipoConcessionaria;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideOrigemAguaTipoConcessionaria == null) ? 0
						: ideOrigemAguaTipoConcessionaria.hashCode());
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
		OrigemAguaTipoConcessionaria other = (OrigemAguaTipoConcessionaria) obj;
		if (ideOrigemAguaTipoConcessionaria == null) {
			if (other.ideOrigemAguaTipoConcessionaria != null)
				return false;
		} else if (!ideOrigemAguaTipoConcessionaria
				.equals(other.ideOrigemAguaTipoConcessionaria))
			return false;
		return true;
	}

	
}