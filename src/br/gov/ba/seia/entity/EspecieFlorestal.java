package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "especie_florestal")
public class EspecieFlorestal extends AbstractEntity implements Serializable, Comparable<EspecieFlorestal> {
	private static final long serialVersionUID = 242098290396580619L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "especie_florestal_seq")
	@SequenceGenerator(name = "especie_florestal_seq", sequenceName = "especie_florestal_seq", allocationSize = 1)
	@Column(name = "ide_especie_florestal")
	private Integer ideEspecieFlorestal;
	
	@Column(name = "nom_especie_florestal")
	private String nomEspecieFlorestal;
	
	@Column(name = "ind_ativo")
	private Boolean indAtivo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_tipo_especie_florestal", referencedColumnName = "ide_tipo_especie_florestal")
	private TipoEspecieFlorestal ideTipoEspecieFlorestal;
	
	@OneToMany
	@JoinColumn(name = "ide_especie_florestal", referencedColumnName = "ide_especie_florestal")
	private Collection<EspecieFlorestalNomePopularEspecie> especieFlorestalNomePopularEspecieCollection;
	
	@Transient
	private List<NomePopularEspecie> removidosNomePopularEspecie;
	
	@Override
	public int compareTo(EspecieFlorestal o) {
		return getNomEspecieFlorestal().compareTo(o.getNomEspecieFlorestal());
	}
	
	/************************
	 *						* 
	 * XXX: GET'S AND SET'S *
	 * 						*
	 ***********************/
	
	public Integer getIdeEspecieFlorestal() {
		return ideEspecieFlorestal;
	}
	
	public void setIdeEspecieFlorestal(Integer ideEspecieFlorestal) {
		this.ideEspecieFlorestal = ideEspecieFlorestal;
	}
	
	public String getNomEspecieFlorestal() {
		return nomEspecieFlorestal;
	}
	
	public void setNomEspecieFlorestal(String nomEspecieFlorestal) {
		this.nomEspecieFlorestal = nomEspecieFlorestal;
	}
	
	public Boolean getIndAtivo() {
		return indAtivo;
	}
	
	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}
	
	public Collection<EspecieFlorestalNomePopularEspecie> getEspecieFlorestalNomePopularEspecieCollection() {
		return especieFlorestalNomePopularEspecieCollection;
	}
	
	public void setEspecieFlorestalNomePopularEspecieCollection(Collection<EspecieFlorestalNomePopularEspecie> especieFlorestalNomePopularEspecieCollection) {
		this.especieFlorestalNomePopularEspecieCollection = especieFlorestalNomePopularEspecieCollection;
	}
	
	public List<NomePopularEspecie> getRemovidosNomePopularEspecie() {
		return removidosNomePopularEspecie;
	}
	
	public void setRemovidosNomePopularEspecie(List<NomePopularEspecie> removidosNomePopularEspecie) {
		this.removidosNomePopularEspecie = removidosNomePopularEspecie;
	}
}
