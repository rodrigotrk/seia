package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "caepog_certificado")
public class CaepogCertificado implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Chave primária composta da tabela CAEPOG_CERTIFICADO
	 */
	@EmbeddedId
	@Basic(optional = false)
	private CaepogCertificadoPK ideCaepogCertificadoPK;

	/**
	 * Chave primária da tabela CAEPOG
	 */
	@Basic(optional = false)
	@JoinColumn(name = "ide_caepog", referencedColumnName = "ide_caepog", nullable = true, unique = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Caepog ideCaepog;

	/**
	 * Chave primária da tabela CERTIFICADO
	 */
	@Basic(optional = false)
	@JoinColumn(name = "ide_certificado", referencedColumnName = "ide_certificado", nullable = true, unique = false, insertable = false, updatable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private Certificado ideCertificado;

	public CaepogCertificado() {
		super();
	}

	public CaepogCertificado(CaepogCertificadoPK ideCaepogCertificadoPK, Caepog ideCaepog, Certificado ideCertificado) {
		super();
		this.ideCaepogCertificadoPK = ideCaepogCertificadoPK;
		this.ideCaepog = ideCaepog;
		this.ideCertificado = ideCertificado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideCaepogCertificadoPK == null) ? 0 : ideCaepogCertificadoPK.hashCode());
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
		CaepogCertificado other = (CaepogCertificado) obj;
		if (ideCaepogCertificadoPK == null) {
			if (other.ideCaepogCertificadoPK != null)
				return false;
		} else if (!ideCaepogCertificadoPK.equals(other.ideCaepogCertificadoPK))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CaepogCertificado [ideCaepogCertificadoPK=" + ideCaepogCertificadoPK + "]";
	}
	
	/**
	 * 
	 * 
	 * GET'S AND SET'S
	 * 
	 * 
	 */

	public CaepogCertificadoPK getIdeCaepogCertificadoPK() {
		return ideCaepogCertificadoPK;
	}

	public void setIdeCaepogCertificadoPK(CaepogCertificadoPK ideCaepogCertificadoPK) {
		this.ideCaepogCertificadoPK = ideCaepogCertificadoPK;
	}

	public Caepog getIdeCaepog() {
		return ideCaepog;
	}

	public void setIdeCaepog(Caepog ideCaepog) {
		this.ideCaepog = ideCaepog;
	}

	public Certificado getIdeCertificado() {
		return ideCertificado;
	}

	public void setIdeCertificado(Certificado ideCertificado) {
		this.ideCertificado = ideCertificado;
	}
}