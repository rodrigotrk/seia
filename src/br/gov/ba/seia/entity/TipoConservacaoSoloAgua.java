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

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the tipo_conservacao_solo_agua database table.
 * 
 */
@Entity
@Table(name="tipo_conservacao_solo_agua")
public class TipoConservacaoSoloAgua implements Serializable,BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipo_conservacao_solo_agua_ide_tipo_conservacao_solo_agua_generator")
	@SequenceGenerator(name = "tipo_conservacao_solo_agua_ide_tipo_conservacao_solo_agua_generator", sequenceName = "tipo_conservacao_solo_agua_ide_tipo_conservacao_solo_agua_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_tipo_conservacao_solo_agua")
	private Integer ideTipoConservacaoSoloAgua;

	@Size(min = 1, max = 50)
	@Column(name="dsc_tipo_conservacao_solo_agua" ,nullable = false, length = 50)
	private String dscTipoConservacaoSoloAgua;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

    public TipoConservacaoSoloAgua() {
    }

	public TipoConservacaoSoloAgua(Integer ideTipoConservacaoSoloAgua) {
		this.ideTipoConservacaoSoloAgua = ideTipoConservacaoSoloAgua;
	}

	public Integer getIdeTipoConservacaoSoloAgua() {
		return this.ideTipoConservacaoSoloAgua;
	}

	public void setIdeTipoConservacaoSoloAgua(Integer ideTipoConservacaoSoloAgua) {
		this.ideTipoConservacaoSoloAgua = ideTipoConservacaoSoloAgua;
	}

	public String getDscTipoConservacaoSoloAgua() {
		return this.dscTipoConservacaoSoloAgua;
	}

	public void setDscTipoConservacaoSoloAgua(String dscTipoConservacaoSoloAgua) {
		this.dscTipoConservacaoSoloAgua = dscTipoConservacaoSoloAgua;
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
				+ ((ideTipoConservacaoSoloAgua == null) ? 0
						: ideTipoConservacaoSoloAgua.hashCode());
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
		TipoConservacaoSoloAgua other = (TipoConservacaoSoloAgua) obj;
		if (ideTipoConservacaoSoloAgua == null) {
			if (other.ideTipoConservacaoSoloAgua != null)
				return false;
		} else if (!ideTipoConservacaoSoloAgua
				.equals(other.ideTipoConservacaoSoloAgua))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		return new Long(ideTipoConservacaoSoloAgua);
	}

}