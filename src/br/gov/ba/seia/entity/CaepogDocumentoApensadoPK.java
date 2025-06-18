package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Chave primária composta da tabela CAEPOG_DOCUMENTO_APENSADO
 */
@Embeddable
public class CaepogDocumentoApensadoPK implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Chave primária da tabela CAEPOG_DOCUMENTO
	 */
	@Basic(optional = false)
	@Column(name = "ide_caepog_documento", nullable = false)
	private int ideCaepogDocumento;

	/**
	 * Chave primária da tabela CAEPOG
	 */
	@Basic(optional = false)
	@Column(name = "ide_caepog", nullable = false)
	private int ideCaepog;

	public CaepogDocumentoApensadoPK() {
		super();
	}

	public CaepogDocumentoApensadoPK(int ideCaepogDocumento, int ideCaepog) {
		super();
		this.ideCaepogDocumento = ideCaepogDocumento;
		this.ideCaepog = ideCaepog;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ideCaepog;
		result = prime * result + ideCaepogDocumento;
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
		CaepogDocumentoApensadoPK other = (CaepogDocumentoApensadoPK) obj;
		if (ideCaepog != other.ideCaepog)
			return false;
		if (ideCaepogDocumento != other.ideCaepogDocumento)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CaepogDocumentoApensadoPK [ideCaepogDocumento=" + ideCaepogDocumento + ", ideCaepog=" + ideCaepog + "]";
	}
	
	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public int getIdeCaepogDocumento() {
		return ideCaepogDocumento;
	}

	public void setIdeCaepogDocumento(int ideCaepogDocumento) {
		this.ideCaepogDocumento = ideCaepogDocumento;
	}

	public int getIdeCaepog() {
		return ideCaepog;
	}

	public void setIdeCaepog(int ideCaepog) {
		this.ideCaepog = ideCaepog;
	}
}