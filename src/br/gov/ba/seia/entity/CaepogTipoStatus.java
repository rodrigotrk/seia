package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.ba.seia.enumerator.CaepogTipoStatusEnum;
import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * Tabela que armazena os tipos de status
 */
@Entity
@Table(name = "caepog_tipo_status")
public class CaepogTipoStatus implements Serializable, BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * Chave primária da tabela CAEPOG_TIPO_STATUS
	 */
	@Id
	@Basic(optional = false)
	@Column(name = "ide_caepog_tipo_status", nullable = false)
	private Integer ideCaepogTipoStatus;
	
	/**
	 * Nome do tipo do status
	 */
	@Column(name = "nom_caepog_tipo_status", length = 100)
	private String nomCaepogTipoStatus;
	
	public CaepogTipoStatus() {
		super();
	}
	
	public CaepogTipoStatus(Integer ideCaepogTipoStatus) {
		this.ideCaepogTipoStatus = ideCaepogTipoStatus;
	}
	
	
	public CaepogTipoStatus(CaepogTipoStatusEnum cts) {
		super();
		this.ideCaepogTipoStatus = cts.getId();
		this.nomCaepogTipoStatus = cts.getNome();
	}

	public CaepogTipoStatus(Integer ideCaepogTipoStatus, String nomCaepogTipoStatus) {
		super();
		this.ideCaepogTipoStatus = ideCaepogTipoStatus;
		this.nomCaepogTipoStatus = nomCaepogTipoStatus;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideCaepogTipoStatus == null) ? 0 : ideCaepogTipoStatus.hashCode());
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
		CaepogTipoStatus other = (CaepogTipoStatus) obj;
		if (ideCaepogTipoStatus == null) {
			if (other.ideCaepogTipoStatus != null)
				return false;
		} else if (!ideCaepogTipoStatus.equals(other.ideCaepogTipoStatus))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CaepogTipoStatus [ideCaepogTipoStatus=" + ideCaepogTipoStatus + "]";
	}

	@Override
	public Long getId() {
		return ideCaepogTipoStatus.longValue();
	}
	
	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public Integer getIdeCaepogTipoStatus() {
		return ideCaepogTipoStatus;
	}

	public void setIdeCaepogTipoStatus(Integer ideCaepogTipoStatus) {
		this.ideCaepogTipoStatus = ideCaepogTipoStatus;
	}

	public String getNomCaepogTipoStatus() {
		return nomCaepogTipoStatus;
	}

	public void setNomCaepogTipoStatus(String nomCaepogTipoStatus) {
		this.nomCaepogTipoStatus = nomCaepogTipoStatus;
	}
}