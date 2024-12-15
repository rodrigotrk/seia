package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
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
import javax.validation.constraints.NotNull;



@Entity
@Table(name = "categoria_documento_perfil_area")
public class CategoriaDocumentoPerfilArea implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categoria_documento_peril_area_seq")
	@SequenceGenerator(name = "categoria_documento_peril_area_seq", sequenceName = "categoria_documento_peril_area_seq", allocationSize = 1)
	@NotNull
	@Column(name = "ide_categoria_documento_perfil_area")
	private Integer ideCategoriaDocumentoPerfilArea;
	
	@JoinColumn(name = "ide_categoria_documento", referencedColumnName = "ide_categoria_documento")
    @ManyToOne(fetch = FetchType.EAGER)
	private CategoriaDocumento ideCategoriaDocumento;
	
	@JoinColumn(name = "ide_area", referencedColumnName = "ide_area")
    @ManyToOne(fetch = FetchType.EAGER)
	private Area ideArea;
	
	@JoinColumn(name = "ide_perfil", referencedColumnName = "ide_perfil")
    @ManyToOne(fetch = FetchType.EAGER)
	private Perfil idePerfil;
	
	public Integer getIdeCategoriaDocumentoPerfilArea() {
		return ideCategoriaDocumentoPerfilArea;
	}


	public void setIdeCategoriaDocumentoPerfilArea(
			Integer ideCategoriaDocumentoPerfilArea) {
		this.ideCategoriaDocumentoPerfilArea = ideCategoriaDocumentoPerfilArea;
	}


	public CategoriaDocumento getIdeCategoriaDocumento() {
		return ideCategoriaDocumento;
	}


	public void setIdeCategoriaDocumento(CategoriaDocumento ideCategoriaDocumento) {
		this.ideCategoriaDocumento = ideCategoriaDocumento;
	}


	public Area getIdeArea() {
		return ideArea;
	}


	public void setIdeArea(Area ideArea) {
		this.ideArea = ideArea;
	}


	public Perfil getIdePerfil() {
		return idePerfil;
	}


	public void setIdePerfil(Perfil idePerfil) {
		this.idePerfil = idePerfil;
	}
	
	@Override
	public String toString() {
		return ideCategoriaDocumentoPerfilArea.toString();
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideCategoriaDocumentoPerfilArea == null) ? 0
						: ideCategoriaDocumentoPerfilArea.hashCode());
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
		CategoriaDocumentoPerfilArea other = (CategoriaDocumentoPerfilArea) obj;
		if (ideCategoriaDocumentoPerfilArea == null) {
			if (other.ideCategoriaDocumentoPerfilArea != null)
				return false;
		} else if (!ideCategoriaDocumentoPerfilArea
				.equals(other.ideCategoriaDocumentoPerfilArea))
			return false;
		return true;
	}	
	
}
