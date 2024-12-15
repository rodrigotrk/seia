package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the tipo_emissao_atmosferica database table.
 * 
 */
@Entity
@Table(name="tipo_emissao_atmosferica")
public class TipoEmissaoAtmosferica implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="ide_tipo_emissao_atmosferica")
	private Integer ideTipoEmissaoAtmosferica;

	@Column(name="dsc_tipo_emissao_atmosferica")
	private String dscTipoEmissaoAtmosferica;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	public TipoEmissaoAtmosferica() {
	}

	public TipoEmissaoAtmosferica(String string) {
		this.dscTipoEmissaoAtmosferica = string;
	}

	public Integer getIdeTipoEmissaoAtmosferica() {
		return this.ideTipoEmissaoAtmosferica;
	}

	public void setIdeTipoEmissaoAtmosferica(Integer ideTipoEmissaoAtmosferica) {
		this.ideTipoEmissaoAtmosferica = ideTipoEmissaoAtmosferica;
	}

	public String getDscTipoEmissaoAtmosferica() {
		return this.dscTipoEmissaoAtmosferica;
	}

	public void setDscTipoEmissaoAtmosferica(String dscTipoEmissaoAtmosferica) {
		this.dscTipoEmissaoAtmosferica = dscTipoEmissaoAtmosferica;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public boolean isOutros(){
		if(dscTipoEmissaoAtmosferica.compareTo("Outras fontes de poluição do ar") == 0){
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideTipoEmissaoAtmosferica == null) ? 0
						: ideTipoEmissaoAtmosferica.hashCode());
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
		TipoEmissaoAtmosferica other = (TipoEmissaoAtmosferica) obj;
		if (ideTipoEmissaoAtmosferica == null) {
			if (other.ideTipoEmissaoAtmosferica != null) {
				return false;
			}
		} else if (!ideTipoEmissaoAtmosferica
				.equals(other.ideTipoEmissaoAtmosferica)) {
			return false;
		}
		return true;
	}

	@Override
	public Long getId() {
		return new Long(this.ideTipoEmissaoAtmosferica);
	}

}