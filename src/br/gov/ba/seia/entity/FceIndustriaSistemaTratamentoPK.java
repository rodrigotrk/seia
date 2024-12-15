package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The primary key class for the fce_industria_sistema_tratamento database table.
 * 
 */
@Embeddable
public class FceIndustriaSistemaTratamentoPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;


	@Column(name="ide_fce_industria")
	private Integer ideFceIndustria;

	@Column(name="ide_tipo_sistema_tratamento")
	private Integer ideTipoSistemaTratamento;

	public FceIndustriaSistemaTratamentoPK() {
	}

	public FceIndustriaSistemaTratamentoPK(FceIndustria ideFceIndustria,TipoSistemaTratamento ideTipoSistemaTratamento) {
		this.ideFceIndustria = ideFceIndustria.getIdeFceIndustria();
		this.ideTipoSistemaTratamento = ideTipoSistemaTratamento.getIdeTipoSistemaTratamento();
	}

	public Integer getIdeFceIndustria() {
		return ideFceIndustria;
	}
	public void setIdeFceIndustria(Integer ideFceIndustria) {
		this.ideFceIndustria = ideFceIndustria;
	}
	public Integer getIdeTipoSistemaTratamento() {
		return ideTipoSistemaTratamento;
	}
	public void setIdeTipoSistemaTratamento(Integer ideTipoSistemaTratamento) {
		this.ideTipoSistemaTratamento = ideTipoSistemaTratamento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ideFceIndustria == null) ? 0 : ideFceIndustria.hashCode());
		result = prime
				* result
				+ ((ideTipoSistemaTratamento == null) ? 0
						: ideTipoSistemaTratamento.hashCode());
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
		FceIndustriaSistemaTratamentoPK other = (FceIndustriaSistemaTratamentoPK) obj;
		if (ideFceIndustria == null) {
			if (other.ideFceIndustria != null) {
				return false;
			}
		} else if (!ideFceIndustria.equals(other.ideFceIndustria)) {
			return false;
		}
		if (ideTipoSistemaTratamento == null) {
			if (other.ideTipoSistemaTratamento != null) {
				return false;
			}
		} else if (!ideTipoSistemaTratamento
				.equals(other.ideTipoSistemaTratamento)) {
			return false;
		}
		return true;
	}
}