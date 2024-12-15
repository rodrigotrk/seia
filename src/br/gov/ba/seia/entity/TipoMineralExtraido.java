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


@Entity
@Table(name = "tipo_mineral_extraido")
public class TipoMineralExtraido implements Serializable, BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "tipo_mineral_extraido_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "tipo_mineral_extraido_seq", sequenceName = "tipo_mineral_extraido_seq", allocationSize = 1)
	@Column(name = "ide_tipo_mineral_extraido", nullable = false)
	private Integer ideTipoMineralExtraido;
	
	@Column(name = "nom_tipo_mineral_extraido", nullable = false)
	private String nomTipoMineralExtraido;
	
	@Column(name = "ind_ativo", nullable = false)
	private Boolean indAtivo;

	public TipoMineralExtraido() {
	}

	public Integer getIdeTipoMineralExtraido() {
		return ideTipoMineralExtraido;
	}

	public void setIdeTipoMineralExtraido(Integer ideTipoMineralExtraido) {
		this.ideTipoMineralExtraido = ideTipoMineralExtraido;
	}

	public String getNomTipoMineralExtraido() {
		return nomTipoMineralExtraido;
	}

	public void setNomTipoMineralExtraido(String nomTipoMineralExtraido) {
		this.nomTipoMineralExtraido = nomTipoMineralExtraido;
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
				+ ((ideTipoMineralExtraido == null) ? 0
						: ideTipoMineralExtraido.hashCode());
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
		TipoMineralExtraido other = (TipoMineralExtraido) obj;
		if (ideTipoMineralExtraido == null) {
			if (other.ideTipoMineralExtraido != null)
				return false;
		} else if (!ideTipoMineralExtraido.equals(other.ideTipoMineralExtraido))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		
		return new Long(ideTipoMineralExtraido);
	}
	

}
