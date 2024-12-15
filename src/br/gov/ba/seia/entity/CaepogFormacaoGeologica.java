package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * Tabela que armazena a formação geológica
 */
@Entity
@Table(name = "caepog_formacao_geologica")
public class CaepogFormacaoGeologica implements Serializable, BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * Chave primária da tabela CAEPOG_FORMACAO_GEOLOGICA
	 */
	@Id
	@Basic(optional = false)
	@Column(name = "ide_caepog_formacao_geologica", nullable = false)
	private Integer ideCaepogFormacaoGeologica;
	
	/**
	 * Nome da formação geológica
	 */
	@Column(name = "nom_caepog_formacao_geologica", length = 80)
	private String nomCaepogFormacaoGeologica;
	
	public CaepogFormacaoGeologica() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideCaepogFormacaoGeologica == null) ? 0 : ideCaepogFormacaoGeologica.hashCode());
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
		CaepogFormacaoGeologica other = (CaepogFormacaoGeologica) obj;
		if (ideCaepogFormacaoGeologica == null) {
			if (other.ideCaepogFormacaoGeologica != null)
				return false;
		} else if (!ideCaepogFormacaoGeologica.equals(other.ideCaepogFormacaoGeologica))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CaepogFormacaoGeologica [ideCaepogFormacaoGeologica=" + ideCaepogFormacaoGeologica + "]";
	}

	@Override
	public Long getId() {
		return ideCaepogFormacaoGeologica.longValue();
	}
	
	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public Integer getIdeCaepogFormacaoGeologica() {
		return ideCaepogFormacaoGeologica;
	}

	public void setIdeCaepogFormacaoGeologica(Integer ideCaepogFormacaoGeologica) {
		this.ideCaepogFormacaoGeologica = ideCaepogFormacaoGeologica;
	}

	public String getNomCaepogFormacaoGeologica() {
		return nomCaepogFormacaoGeologica;
	}

	public void setNomCaepogFormacaoGeologica(String nomCaepogFormacaoGeologica) {
		this.nomCaepogFormacaoGeologica = nomCaepogFormacaoGeologica;
	}
}