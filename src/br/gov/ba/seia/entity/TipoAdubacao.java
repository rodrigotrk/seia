package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the tipo_adubacao database table.
 * 
 */
@Entity
@XmlRootElement
@Table(name="tipo_adubacao")
public class TipoAdubacao implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_adubacao_ide_tipo_adubacao_generator")
	@SequenceGenerator(name = "tipo_adubacao_ide_tipo_adubacao_generator", sequenceName = "tipo_adubacao_ide_tipo_adubacao_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_tipo_adubacao")
	private Integer ideTipoAdubacao;

	@NotNull
	@Size(min = 1, max = 50)
	@Column(name="dsc_tipo_adubacao")
	private String dscTipoAdubacao;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	public TipoAdubacao() {
	}

	public Integer getIdeTipoAdubacao() {
		return this.ideTipoAdubacao;
	}

	public void setIdeTipoAdubacao(Integer ideTipoAdubacao) {
		this.ideTipoAdubacao = ideTipoAdubacao;
	}

	public String getDscTipoAdubacao() {
		return this.dscTipoAdubacao;
	}

	public void setDscTipoAdubacao(String dscTipoAdubacao) {
		this.dscTipoAdubacao = dscTipoAdubacao;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	@Override
	public Long getId() {
		return new Long(this.ideTipoAdubacao);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (ideTipoAdubacao == null ? 0 : ideTipoAdubacao.hashCode());
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
		TipoAdubacao other = (TipoAdubacao) obj;
		if (ideTipoAdubacao == null) {
			if (other.ideTipoAdubacao != null) {
				return false;
			}
		} else if (!ideTipoAdubacao.equals(other.ideTipoAdubacao)) {
			return false;
		}
		return true;
	}


}