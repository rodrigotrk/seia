package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * The persistent class for the tipo_portaria database table.
 * 
 */
@Entity
@Table(name="tipo_portaria")
public class TipoPortaria implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="tipo_portaria_seq")    
	@SequenceGenerator(name="tipo_portaria_seq", sequenceName="tipo_portaria_seq", allocationSize=1)
	@Column(name="ide_tipo_portaria", unique=true, nullable=false)
	private Integer ideTipoPortaria;

	@Column(name="nom_tipo_portaria", length=20)
	private String nomTipoPortaria;

	@OneToMany(mappedBy="ideTipoPortaria", fetch=FetchType.LAZY)
	private Collection<Portaria> portariaCollection;

	public TipoPortaria() {
	}
	
	public TipoPortaria(Integer ideTipoPortaria) {
		this.ideTipoPortaria = ideTipoPortaria;
	}

	public Integer getIdeTipoPortaria() {
		return this.ideTipoPortaria;
	}

	public void setIdeTipoPortaria(Integer ideTipoPortaria) {
		this.ideTipoPortaria = ideTipoPortaria;
	}

	public String getNomTipoPortaria() {
		return this.nomTipoPortaria;
	}

	public void setNomTipoPortaria(String nomTipoPortaria) {
		this.nomTipoPortaria = nomTipoPortaria;
	}

	public Collection<Portaria> getPortariaCollection() {
		return portariaCollection;
	}

	public void setPortariaCollection(Collection<Portaria> portariaCollection) {
		this.portariaCollection = portariaCollection;
	}

}