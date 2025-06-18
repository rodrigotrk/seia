package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;
import br.gov.ba.seia.util.Util;

/**
 * The persistent class for the servidao_mineraria database table.
 * 
 */
@Entity
@Table(name = "servidao_mineraria")
public class ServidaoMineraria implements Serializable, BaseEntity, Comparable<ServidaoMineraria> {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "SERVIDAO_MINERARIA_IDESERVIDAOMINERARIA_GENERATOR", sequenceName = "SERVIDAO_MINERARIA_IDE_SERVIDAO_MINERARIA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SERVIDAO_MINERARIA_IDESERVIDAOMINERARIA_GENERATOR")
	@Column(name = "ide_servidao_mineraria")
	private Integer ideServidaoMineraria;

	@Column(name = "nom_servidao_mineraria")
	private String nomServidaoMineraria;

	public ServidaoMineraria() {
	}

	public ServidaoMineraria(Integer ide, String nome) {
		this.ideServidaoMineraria = ide;
		this.nomServidaoMineraria = nome;
	}

	public Integer getIdeServidaoMineraria() {
		return this.ideServidaoMineraria;
	}

	public void setIdeServidaoMineraria(Integer ideServidaoMineraria) {
		this.ideServidaoMineraria = ideServidaoMineraria;
	}

	public String getNomServidaoMineraria() {
		return this.nomServidaoMineraria;
	}

	public void setNomServidaoMineraria(String nomServidaoMineraria) {
		this.nomServidaoMineraria = nomServidaoMineraria;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nomServidaoMineraria == null) ? 0 : nomServidaoMineraria.hashCode());
		result = prime * result + ((ideServidaoMineraria == null) ? 0 : ideServidaoMineraria.hashCode());
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
		ServidaoMineraria other = (ServidaoMineraria) obj;
		if(nomServidaoMineraria == null){
			if(other.nomServidaoMineraria != null)
				return false;
		}
		else if(!nomServidaoMineraria.equals(other.nomServidaoMineraria))
			return false;
		if(ideServidaoMineraria == null){
			if(other.ideServidaoMineraria != null)
				return false;
		}
		else if(!ideServidaoMineraria.equals(other.ideServidaoMineraria))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		return new Long(this.ideServidaoMineraria);
	}

	@Override
	public int compareTo(ServidaoMineraria o) {
		if (Util.isNull(this.ideServidaoMineraria)){
			return -1;
		}
		return this.ideServidaoMineraria.compareTo(o.ideServidaoMineraria);
	}

}