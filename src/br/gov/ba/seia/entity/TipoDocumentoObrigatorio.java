package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "tipo_documento_obrigatorio")
public class TipoDocumentoObrigatorio implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "tipo_documento_obrigatorio_ide_tipo_documento_obrigatorio", sequenceName = "tipo_documento_obrigatorio_ide_tipo_documento_obrigatorio_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_documento_obrigatorio_ide_tipo_documento_obrigatorio")
	@Column(name = "ide_tipo_documento_obrigatorio", unique = true, nullable = false)
	private Integer ideTipoDocumentoObrigatorio;
	
	@Column(name = "nom_tipo_documento_obrigatorio", unique = true, nullable = false)
	private String nomTipoDocumentoObrigatorio;
	
	@Column(name = "ind_ativo")
	private Boolean indAtivo;

	public TipoDocumentoObrigatorio(){}
	
	public TipoDocumentoObrigatorio(int ideTipoDocumentoObrigatorio){
		this.ideTipoDocumentoObrigatorio = ideTipoDocumentoObrigatorio;
	}
	
	public Integer getIdeTipoDocumentoObrigatorio() {
		return ideTipoDocumentoObrigatorio;
	}
	
	
	public void setIdeTipoDocumentoObrigatorio(Integer ideTipoDocumentoObrigatorio) {
		this.ideTipoDocumentoObrigatorio = ideTipoDocumentoObrigatorio;
	}

	public String getNomTipoDocumentoObrigatorio() {
		return nomTipoDocumentoObrigatorio;
	}

	public void setNomTipoDocumentoObrigatorio(String nomTipoDocumentoObrigatorio) {
		this.nomTipoDocumentoObrigatorio = nomTipoDocumentoObrigatorio;
	}

	public Boolean getIndAtivo() {
		return indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideTipoDocumentoObrigatorio == null) ? 0
						: ideTipoDocumentoObrigatorio.hashCode());
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
		TipoDocumentoObrigatorio other = (TipoDocumentoObrigatorio) obj;
		if (ideTipoDocumentoObrigatorio == null) {
			if (other.ideTipoDocumentoObrigatorio != null)
				return false;
		} else if (!ideTipoDocumentoObrigatorio
				.equals(other.ideTipoDocumentoObrigatorio))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return ideTipoDocumentoObrigatorio.toString();
	}	
	
	
}
