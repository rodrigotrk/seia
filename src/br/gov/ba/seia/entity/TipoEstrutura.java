package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.enumerator.TipoEstruturaEnum;
import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * The persistent class for the tipo_estrutura database table.
 *
 */
@Entity
@Table(name = "tipo_estrutura")
public class TipoEstrutura implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "TIPO_ESTRUTURA_IDETIPOESTRUTURA_GENERATOR", sequenceName = "TIPO_ESTRUTURA_IDE_TIPO_ESTRUTURA_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TIPO_ESTRUTURA_IDETIPOESTRUTURA_GENERATOR")
	@Column(name = "ide_tipo_estrutura")
	private Integer ideTipoEstrutura;

	@Column(name = "nom_tipo_estrutura")
	private String nomTipoEstrutura;

	public TipoEstrutura() {
	}

	public TipoEstrutura(TipoEstruturaEnum enumerator) {
		this.ideTipoEstrutura = enumerator.getIdeTipoEstrutura();
		this.nomTipoEstrutura = enumerator.getNomTipoEstrutura();
	}

	public TipoEstrutura(Integer ide, String nome) {
		this.ideTipoEstrutura = ide;
		this.nomTipoEstrutura = nome;
	}

	public Integer getIdeTipoEstrutura() {
		return this.ideTipoEstrutura;
	}

	public void setIdeTipoEstrutura(Integer ideTipoEstrutura) {
		this.ideTipoEstrutura = ideTipoEstrutura;
	}

	public String getNomTipoEstrutura() {
		return this.nomTipoEstrutura;
	}

	public void setNomTipoEstrutura(String nomTipoEstrutura) {
		this.nomTipoEstrutura = nomTipoEstrutura;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideTipoEstrutura == null) ? 0 : ideTipoEstrutura.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		TipoEstrutura other = (TipoEstrutura) obj;
		if(ideTipoEstrutura == null){
			if(other.ideTipoEstrutura != null) {
				return false;
			}
		}
		else if(!ideTipoEstrutura.equals(other.ideTipoEstrutura)) {
			return false;
		}
		return true;
	}

	@Override
	public Long getId() {
		return new Long(this.ideTipoEstrutura);
	}

}