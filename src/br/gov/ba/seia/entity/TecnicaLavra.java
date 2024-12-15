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

import br.gov.ba.seia.enumerator.TecnicaLavraEnum;
import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * The persistent class for the tecnica_lavra database table.
 * 
 */
@Entity
@Table(name = "tecnica_lavra")
public class TecnicaLavra implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "TECNICA_LAVRA_IDETECNICALAVRA_GENERATOR", sequenceName = "TECNICA_LAVRA_IDE_TECNICA_LAVRA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TECNICA_LAVRA_IDETECNICALAVRA_GENERATOR")
	@Column(name = "ide_tecnica_lavra")
	private Integer ideTecnicaLavra;

	@Column(name = "nom_tecnica_lavra")
	private String nomTecnicaLavra;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_metodo_lavra")
	private MetodoLavra metodoLavra;
	
	@JoinColumn(name = "ide_tecnica_lavra_pai", referencedColumnName = "ide_tecnica_lavra")
	@ManyToOne(fetch = FetchType.LAZY)
	private TecnicaLavra ideTecnicaLavraPai;

	public TecnicaLavra() {
	}

	public TecnicaLavra(Integer ide, String nome) {
		this.ideTecnicaLavra = ide;
		this.nomTecnicaLavra = nome;
	}

	public TecnicaLavra(TecnicaLavraEnum tecnicaLavraEnum) {
		this.ideTecnicaLavra = tecnicaLavraEnum.getIdeTecnicaLavra();
		this.nomTecnicaLavra = tecnicaLavraEnum.getNomTecnicaLavra();
	}

	public Integer getIdeTecnicaLavra() {
		return this.ideTecnicaLavra;
	}

	public void setIdeTecnicaLavra(Integer ideTecnicaLavra) {
		this.ideTecnicaLavra = ideTecnicaLavra;
	}

	public String getNomTecnicaLavra() {
		return this.nomTecnicaLavra;
	}

	public void setNomTecnicaLavra(String nomTecnicaLavra) {
		this.nomTecnicaLavra = nomTecnicaLavra;
	}

	public MetodoLavra getMetodoLavra() {
		return this.metodoLavra;
	}

	public void setMetodoLavra(MetodoLavra metodoLavra) {
		this.metodoLavra = metodoLavra;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nomTecnicaLavra == null) ? 0 : nomTecnicaLavra.hashCode());
		result = prime * result + ((ideTecnicaLavra == null) ? 0 : ideTecnicaLavra.hashCode());
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
		TecnicaLavra other = (TecnicaLavra) obj;
		if(nomTecnicaLavra == null){
			if(other.nomTecnicaLavra != null)
				return false;
		}
		else if(!nomTecnicaLavra.equals(other.nomTecnicaLavra))
			return false;
		if(ideTecnicaLavra == null){
			if(other.ideTecnicaLavra != null)
				return false;
		}
		else if(!ideTecnicaLavra.equals(other.ideTecnicaLavra))
			return false;
		return true;
	}

	public TecnicaLavra getIdeTecnicaLavraPai() {
		return ideTecnicaLavraPai;
	}

	public void setIdeTecnicaLavraPai(TecnicaLavra ideTecnicaLavraPai) {
		this.ideTecnicaLavraPai = ideTecnicaLavraPai;
	}

	@Override
	public Long getId() {
		return new Long(this.ideTecnicaLavra);
	}

}