package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Chave primária composta da tabela CAEPOG_CERTIFICADO
 */
@Embeddable
public class CaepogCertificadoPK implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Chave primária da tabela CAEPOG
	 */
	@Basic(optional = false)
	@Column(name = "ide_caepog", nullable = false)
	private int ideCaepog;

	/**
	 * Chave primária da tabela CERTIFICADO
	 */
	@Basic(optional = false)
	@Column(name = "ide_certificado", nullable = false)
	private int ideCertificado;

	public CaepogCertificadoPK() {
		super();
	}

	public CaepogCertificadoPK(int ideCaepog, int ideCertificado) {
		super();
		this.ideCaepog = ideCaepog;
		this.ideCertificado = ideCertificado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ideCaepog;
		result = prime * result + ideCertificado;
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
		CaepogCertificadoPK other = (CaepogCertificadoPK) obj;
		if (ideCaepog != other.ideCaepog)
			return false;
		if (ideCertificado != other.ideCertificado)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CaepogCertificadoPK [ideCaepog=" + ideCaepog + ", ideCertificado=" + ideCertificado + "]";
	}
	
	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */
	
	public int getIdeCaepog() {
		return ideCaepog;
	}
	
	public void setIdeCaepog(int ideCaepog) {
		this.ideCaepog = ideCaepog;
	}
	
	public int getIdeCertificado() {
		return ideCertificado;
	}
	
	public void setIdeCertificado(int ideCertificado) {
		this.ideCertificado = ideCertificado;
	}
}