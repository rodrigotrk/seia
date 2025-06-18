package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the tipo_valor database table.
 * 
 */
@Entity
@Table(name="tipo_valor")
public class TipoValor implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="TIPO_VALOR_IDE_TIPO_VALOR_SEQ", sequenceName="TIPO_VALOR_IDE_TIPO_VALOR_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPO_VALOR_IDE_TIPO_VALOR_SEQ")
	@Column(name="ide_tipo_valor")
	private Integer ideTipoValor;

	@Column(name="dsc_tipo_valor")
	private String dscTipoValor;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	public TipoValor() {
	}

	public Integer getIdeTipoValor() {
		return this.ideTipoValor;
	}

	public void setIdeTipoValor(Integer ideTipoValor) {
		this.ideTipoValor = ideTipoValor;
	}

	public String getDscTipoValor() {
		return this.dscTipoValor;
	}

	public void setDscTipoValor(String dscTipoValor) {
		this.dscTipoValor = dscTipoValor;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideTipoValor == null) ? 0 : ideTipoValor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TipoValor other = (TipoValor) obj;
		if (ideTipoValor == null) {
			if (other.ideTipoValor != null) {
				return false;
			}
		} else if (!ideTipoValor.equals(other.ideTipoValor)) {
			return false;
		}
		return true;
	}

	@Override
	public Long getId() {
		return new Long(this.ideTipoValor);
	}

}