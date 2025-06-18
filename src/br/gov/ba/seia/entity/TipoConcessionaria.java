package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the tipo_concessionaria database table.
 * 
 */
@Entity
@Table(name="tipo_concessionaria")
@NamedQuery(name="TipoConcessionaria.findAll", query="SELECT t FROM TipoConcessionaria t")
public class TipoConcessionaria implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TIPO_CONCESSIONARIA_IDETIPOCONCESSIONARIA_GENERATOR", sequenceName="TIPO_CONCESSIONARIA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPO_CONCESSIONARIA_IDETIPOCONCESSIONARIA_GENERATOR")
	@Column(name="ide_tipo_concessionaria")
	private Integer ideTipoConcessionaria;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	@Column(name="nom_concessionarias")
	private String nomConcessionarias;

	//bi-directional many-to-one association to OrigemAguaTipoConcessionaria
	@OneToMany(mappedBy="tipoConcessionaria")
	private List<OrigemAguaTipoConcessionaria> origemAguaTipoConcessionarias;

	public TipoConcessionaria() {
	}

	public Integer getIdeTipoConcessionaria() {
		return this.ideTipoConcessionaria;
	}

	public void setIdeTipoConcessionaria(Integer ideTipoConcessionaria) {
		this.ideTipoConcessionaria = ideTipoConcessionaria;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public String getNomConcessionarias() {
		return this.nomConcessionarias;
	}

	public void setNomConcessionarias(String nomConcessionarias) {
		this.nomConcessionarias = nomConcessionarias;
	}

	public List<OrigemAguaTipoConcessionaria> getOrigemAguaTipoConcessionarias() {
		return this.origemAguaTipoConcessionarias;
	}

	public void setOrigemAguaTipoConcessionarias(List<OrigemAguaTipoConcessionaria> origemAguaTipoConcessionarias) {
		this.origemAguaTipoConcessionarias = origemAguaTipoConcessionarias;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideTipoConcessionaria == null) ? 0 : ideTipoConcessionaria
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
		TipoConcessionaria other = (TipoConcessionaria) obj;
		if (ideTipoConcessionaria == null) {
			if (other.ideTipoConcessionaria != null)
				return false;
		} else if (!ideTipoConcessionaria.equals(other.ideTipoConcessionaria))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		
		return new Long(this.ideTipoConcessionaria);
	}

	
}