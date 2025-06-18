package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "tipo_certificado")
@XmlRootElement
public class TipoCertificado implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ide_tipo_certificado", nullable = false)
	private Integer ideTipoCertificado;

	@Column(name = "dsc_tipo_certificado", nullable = false, length = 15)
	private String dscTipoCertificado;

	public TipoCertificado() {
	}

	public TipoCertificado(Integer ideTipoCertificado) {
		super();
		this.ideTipoCertificado = ideTipoCertificado;
	}

	public Integer getIdeTipoCertificado() {
		return ideTipoCertificado;
	}

	public void setIdeTipoCertificado(Integer ideTipoCertificado) {
		this.ideTipoCertificado = ideTipoCertificado;
	}

	public String getDscTipoCertificado() {
		return dscTipoCertificado;
	}

	public void setDscTipoCertificado(String dscTipoCertificado) {
		this.dscTipoCertificado = dscTipoCertificado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideTipoCertificado == null) ? 0 : ideTipoCertificado.hashCode());
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
		TipoCertificado other = (TipoCertificado) obj;
		if (ideTipoCertificado == null) {
			if (other.ideTipoCertificado != null)
				return false;
		} else if (!ideTipoCertificado.equals(other.ideTipoCertificado))
			return false;
		return true;
	}

}
