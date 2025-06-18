package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "categoria_documento")
public class CategoriaDocumento implements Serializable, BaseEntity {
	
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATEGORIA_DOCUMENTO_IDE_CATEGORIA_DOCUMENTO_seq")
	@SequenceGenerator(name = "CATEGORIA_DOCUMENTO_IDE_CATEGORIA_DOCUMENTO_seq", sequenceName = "CATEGORIA_DOCUMENTO_IDE_CATEGORIA_DOCUMENTO_seq", allocationSize = 1)
	@NotNull
	@Column(name = "ide_categoria_documento")
	private Integer ideCategoriaDocumento;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 150)
	@Column(name = "nom_categoria")
	private String nomCategoria;
	
	@OneToMany(mappedBy = "ideCategoriaDocumento", fetch = FetchType.LAZY)
    private Collection<CategoriaDocumentoPerfilArea> categoriaDocumentoPerfilAreaCollection;

	public CategoriaDocumento() {}
	
	public CategoriaDocumento(Integer ideCategoriaDocumento) {
		this.ideCategoriaDocumento = ideCategoriaDocumento;
	}
	
	public CategoriaDocumento(Integer ideCategoriaDocumento, String nomCategoria) {
		this.ideCategoriaDocumento = ideCategoriaDocumento;
		this.nomCategoria = nomCategoria;
	}

	public Integer getIdeCategoriaDocumento() {
		return ideCategoriaDocumento;
	}

	public void setIdeCategoriaDocumento(Integer ideCategoriaDocumento) {
		this.ideCategoriaDocumento = ideCategoriaDocumento;
	}

	public String getNomCategoria() {
		return nomCategoria;
	}

	public void setNomCategoria(String nomCategoria) {
		this.nomCategoria = nomCategoria;
	}
	
	public Collection<CategoriaDocumentoPerfilArea> getCategoriaDocumentoPerfilAreaCollection() {
		return categoriaDocumentoPerfilAreaCollection;
	}
	
	public void setCategoriaDocumentoPerfilAreaCollection(Collection<CategoriaDocumentoPerfilArea> categoriaDocumentoPerfilAreaCollection) {
		this.categoriaDocumentoPerfilAreaCollection = categoriaDocumentoPerfilAreaCollection;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideCategoriaDocumento == null) ? 0 : ideCategoriaDocumento.hashCode());
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
		CategoriaDocumento other = (CategoriaDocumento) obj;
		if (ideCategoriaDocumento == null) {
			if (other.ideCategoriaDocumento != null)
				return false;
		} else if (!ideCategoriaDocumento.equals(other.ideCategoriaDocumento))
			return false;
		return true;
	}
	
	@Override
    public String toString() {

    	return String.valueOf(ideCategoriaDocumento);
    }
	
	@Override
	public Long getId() {
		return Long.valueOf(this.ideCategoriaDocumento);
	}
}