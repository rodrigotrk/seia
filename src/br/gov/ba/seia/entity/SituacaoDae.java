package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "situacao_dae")
public class SituacaoDae implements Serializable,BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ide_situacao_dae")
	private Integer ideSitucaoDae;
	
	@Column(name = "dsc_situacao_dae", length = 100)
	private String dscSituacaoDae;
	
	public SituacaoDae(){
		
	}

	public SituacaoDae(Integer ideSitucaoDa){
		this.ideSitucaoDae = ideSitucaoDa;
	}

	public Integer getIdeSitucaoDae() {
		return ideSitucaoDae;
	}

	public void setIdeSitucaoDae(Integer ideSitucaoDae) {
		this.ideSitucaoDae = ideSitucaoDae;
	}

	public String getDscSituacaoDae() {
		return dscSituacaoDae;
	}

	public void setDscSituacaoDae(String dscSituacaoDae) {
		this.dscSituacaoDae = dscSituacaoDae;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideSitucaoDae == null) ? 0 : ideSitucaoDae
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
		SituacaoDae other = (SituacaoDae) obj;
		if (ideSitucaoDae == null) {
			if (other.ideSitucaoDae != null)
				return false;
		} else if (!ideSitucaoDae.equals(other.ideSitucaoDae))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		
		return new Long(this.ideSitucaoDae);
	}
	
	
	
}
