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
 * The persistent class for the tipo_caracterizacao_projeto database table.
 * 
 */
@Entity
@XmlRootElement
@Table(name="tipo_caracterizacao_projeto")
public class TipoCaracterizacaoProjeto implements Serializable,BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_caracterizacao_projeto_ide_tipo_caracterizacao_projeto_generator")
	@SequenceGenerator(name = "tipo_caracterizacao_projeto_ide_tipo_caracterizacao_projeto_generator", sequenceName = "tipo_caracterizacao_projeto_ide_tipo_caracterizacao_projeto_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_tipo_caracterizacao_projeto")
	private Integer ideTipoCaracterizacaoProjeto;

	@NotNull
	@Size(min = 1, max = 50)
	@Column(name="dsc_tipo_caracterizacao_projeto")
	private String dscTipoCaracterizacaoProjeto;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

    public TipoCaracterizacaoProjeto() {
    }

	public TipoCaracterizacaoProjeto(Integer ideTipoCaracterizacaoProjeto) {
		this.ideTipoCaracterizacaoProjeto = ideTipoCaracterizacaoProjeto;
	}

	public Integer getIdeTipoCaracterizacaoProjeto() {
		return this.ideTipoCaracterizacaoProjeto;
	}

	public void setIdeTipoCaracterizacaoProjeto(Integer ideTipoCaracterizacaoProjeto) {
		this.ideTipoCaracterizacaoProjeto = ideTipoCaracterizacaoProjeto;
	}

	public String getDscTipoCaracterizacaoProjeto() {
		return this.dscTipoCaracterizacaoProjeto;
	}

	public void setDscTipoCaracterizacaoProjeto(String dscTipoCaracterizacaoProjeto) {
		this.dscTipoCaracterizacaoProjeto = dscTipoCaracterizacaoProjeto;
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
		result = prime
				* result
				+ ((ideTipoCaracterizacaoProjeto == null) ? 0
						: ideTipoCaracterizacaoProjeto.hashCode());
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
		TipoCaracterizacaoProjeto other = (TipoCaracterizacaoProjeto) obj;
		if (ideTipoCaracterizacaoProjeto == null) {
			if (other.ideTipoCaracterizacaoProjeto != null)
				return false;
		} else if (!ideTipoCaracterizacaoProjeto
				.equals(other.ideTipoCaracterizacaoProjeto))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		return new Long(ideTipoCaracterizacaoProjeto);
	}

}