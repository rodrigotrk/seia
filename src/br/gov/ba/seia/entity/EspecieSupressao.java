package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "especie_supressao")
public class EspecieSupressao implements Serializable, BaseEntity, Comparable<EspecieSupressao> {

	private static final long serialVersionUID = 242098290396580619L;

	@Id
	@SequenceGenerator(name = "ESPECIE_SUPRESSAO_GENERATOR", sequenceName = "especie_supressao_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ESPECIE_SUPRESSAO_GENERATOR")
	@Column(name = "ide_especie_supressao")
	private Integer ideEspecieSupressao;

	@Column(name = "nom_especie_supressao")
	private String nomEspecieSupressao;

	@Column(name = "ind_ativo")
	private Boolean indAtivo;
	
	@OneToMany
	@JoinColumn(name="ide_especie_supressao", referencedColumnName="ide_especie_supressao")
	private Collection<EspecieSupressaoNomePopularEspecie> especieSupressaoNomePopularEspecieCollection;
	
	@Transient
	private List<NomePopularEspecie> removidosNomePopularEspecie;

	public Integer getIdeEspecieSupressao() {
		return ideEspecieSupressao;
	}

	public void setIdeEspecieSupressao(Integer ideEspecieSupressao) {
		this.ideEspecieSupressao = ideEspecieSupressao;
	}

	public String getNomEspecieSupressao() {
		return nomEspecieSupressao;
	}

	public void setNomEspecieSupressao(String nomEspecieSupressao) {
		this.nomEspecieSupressao = nomEspecieSupressao;
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public Collection<EspecieSupressaoNomePopularEspecie> getEspecieSupressaoNomePopularEspecieCollection() {
		return especieSupressaoNomePopularEspecieCollection;
	}

	public void setEspecieSupressaoNomePopularEspecieCollection(
			Collection<EspecieSupressaoNomePopularEspecie> especieSupressaoNomePopularEspecieCollection) {
		this.especieSupressaoNomePopularEspecieCollection = especieSupressaoNomePopularEspecieCollection;
	}

	public List<NomePopularEspecie> getRemovidosNomePopularEspecie() {
		return removidosNomePopularEspecie;
	}

	public void setRemovidosNomePopularEspecie(
			List<NomePopularEspecie> removidosNomePopularEspecie) {
		this.removidosNomePopularEspecie = removidosNomePopularEspecie;
	}

	@Override
	public Long getId() {
		return new Long(this.ideEspecieSupressao);
	}
	
	@Override
	public int compareTo(EspecieSupressao o) {
		return getNomEspecieSupressao().compareTo(o.getNomEspecieSupressao());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideEspecieSupressao == null) ? 0 : ideEspecieSupressao
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
		EspecieSupressao other = (EspecieSupressao) obj;
		if (ideEspecieSupressao == null) {
			if (other.ideEspecieSupressao != null)
				return false;
		} else if (!ideEspecieSupressao.equals(other.ideEspecieSupressao))
			return false;
		return true;
	}
	
}
