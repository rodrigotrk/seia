package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;


@Entity
@XmlRootElement
@Table(name="FCE_ENERGIA_TERMOELETRICA_UNIDADE_COMBUSTIVEL")
@NamedQueries({
	@NamedQuery(name = "FceEnergiaTermoeletricaUnidadeCombustivel.removeByPk", query = "DELETE FROM FceEnergiaTermoeletricaUnidadeCombustivel f WHERE f.ideFceEnergiaTermoeletricaUnidadeCombustivel = :ideFceEnergiaTermoeletricaUnidadeCombustivel"),
	@NamedQuery(name = "FceEnergiaTermoeletricaUnidadeCombustivel.removeByUnidade", query = "DELETE FROM FceEnergiaTermoeletricaUnidadeCombustivel f WHERE f.fceEnergiaTermoeletricaUnidade = :fceEnergiaTermoeletricaUnidade")
})
public class FceEnergiaTermoeletricaUnidadeCombustivel implements Serializable, BaseEntity {
	

	private static final long serialVersionUID = -6968760220766627709L;


	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fce_energia_termoeletrica_unidade_combustivel_seq")
	@SequenceGenerator(name = "fce_energia_termoeletrica_unidade_combustivel_seq", sequenceName = "fce_energia_termoeletrica_unidade_combustivel_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="IDE_FCE_ENERGIA_TERMOELETRICA_UNIDADE_COMBUSTIVEL")
	private Integer ideFceEnergiaTermoeletricaUnidadeCombustivel;

	
	@ManyToOne(optional = false)
    @JoinColumn(name = "IDE_FCE_ENERGIA_TERMOELETRICA_UNIDADE", nullable = false)
	private FceEnergiaTermoeletricaUnidade fceEnergiaTermoeletricaUnidade;

	@ManyToOne(optional = false)
    @JoinColumn(name = "IDE_TIPO_COMBUSTIVEL", nullable = false)
	private TipoCombustivel tipoCombustivel;
	
	
	

	public Integer getIdeFceEnergiaTermoeletricaUnidadeCombustivel() {
		return ideFceEnergiaTermoeletricaUnidadeCombustivel;
	}


	public void setIdeFceEnergiaTermoeletricaUnidadeCombustivel(
			Integer ideFceEnergiaTermoeletricaUnidadeCombustivel) {
		this.ideFceEnergiaTermoeletricaUnidadeCombustivel = ideFceEnergiaTermoeletricaUnidadeCombustivel;
	}


	public FceEnergiaTermoeletricaUnidade getFceEnergiaTermoeletricaUnidade() {
		return fceEnergiaTermoeletricaUnidade;
	}


	public void setFceEnergiaTermoeletricaUnidade(
			FceEnergiaTermoeletricaUnidade fceEnergiaTermoeletricaUnidade) {
		this.fceEnergiaTermoeletricaUnidade = fceEnergiaTermoeletricaUnidade;
	}


	public TipoCombustivel getTipoCombustivel() {
		return tipoCombustivel;
	}


	public void setTipoCombustivel(TipoCombustivel tipoCombustivel) {
		this.tipoCombustivel = tipoCombustivel;
	}


	@Override
	public Long getId() {
		return Long.valueOf(this.ideFceEnergiaTermoeletricaUnidadeCombustivel);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideFceEnergiaTermoeletricaUnidadeCombustivel == null) ? 0
						: ideFceEnergiaTermoeletricaUnidadeCombustivel
								.hashCode());
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
		FceEnergiaTermoeletricaUnidadeCombustivel other = (FceEnergiaTermoeletricaUnidadeCombustivel) obj;
		if (ideFceEnergiaTermoeletricaUnidadeCombustivel == null) {
			if (other.ideFceEnergiaTermoeletricaUnidadeCombustivel != null)
				return false;
		} else if (!ideFceEnergiaTermoeletricaUnidadeCombustivel
				.equals(other.ideFceEnergiaTermoeletricaUnidadeCombustivel))
			return false;
		return true;
	}
	
}