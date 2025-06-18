package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.enumerator.TipoProducaoEnum;
import br.gov.ba.seia.interfaces.BaseEntity;

/**
 * Entidade que representa a tabela <i>sistema_cultivo</i>, utilizada na <b>Abas Viveiro Escavado e Tanque Rede</b> do FCE - Licenciamento para Aquicultura.
 * <ul>
 * 		<li>1 -'Intensivo'</li>
 * 		<li>2 -'Green-water'</li>
 * 		<li>3 -'Mesocosmo'</li>
 * 		<li>4 -'Semi-intensivo'</li>
 * 		<li>5 -'Extensivo'</li>
 * </ul>
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 28/05/2015
 * @see #6934
 */
@Entity
@Table(name="sistema_cultivo")
@NamedQuery(name="SistemaCultivo.findAll", query="SELECT s FROM SistemaCultivo s")
public class SistemaCultivo implements Serializable, BaseEntity, Comparable<SistemaCultivo> {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="SISTEMA_CULTIVO_IDESISTEMACULTIVO_GENERATOR", sequenceName="SISTEMA_CULTIVO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SISTEMA_CULTIVO_IDESISTEMACULTIVO_GENERATOR")
	@Column(name="ide_sistema_cultivo", nullable = false)
	private Integer ideSistemaCultivo;

	@Column(name="ind_ativo", nullable = false)
	private Boolean indAtivo;

	@Column(name="nom_sistema_cultivo", nullable = false)
	private String nomSistemaCultivo;

	@Transient
	private TipoProducaoEnum tipoProducaoEnum;

	@Transient
	private boolean rowSelect = false;

	public SistemaCultivo() {
	}

	public Integer getIdeSistemaCultivo() {
		return this.ideSistemaCultivo;
	}

	public void setIdeSistemaCultivo(Integer ideSistemaCultivo) {
		this.ideSistemaCultivo = ideSistemaCultivo;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public String getNomSistemaCultivo() {
		return this.nomSistemaCultivo;
	}

	public void setNomSistemaCultivo(String nomSistemaCultivo) {
		this.nomSistemaCultivo = nomSistemaCultivo;
	}

	/* (non-Javadoc)
	 * @see br.gov.ba.seia.entity.BaseEntity#getId()
	 */
	@Override
	public Long getId() {
		return new Long(this.ideSistemaCultivo);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideSistemaCultivo == null) ? 0 : ideSistemaCultivo
						.hashCode());
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
		SistemaCultivo other = (SistemaCultivo) obj;
		if (ideSistemaCultivo == null) {
			if (other.ideSistemaCultivo != null) {
				return false;
			}
		} else if (!ideSistemaCultivo.equals(other.ideSistemaCultivo)) {
			return false;
		}
		return true;
	}

	public TipoProducaoEnum getTipoProducaoEnum() {
		return tipoProducaoEnum;
	}

	public void setTipoProducaoEnum(TipoProducaoEnum tipoProducaoEnum) {
		this.tipoProducaoEnum = tipoProducaoEnum;
	}

	@Override
	public int compareTo(SistemaCultivo o) {
		return this.ideSistemaCultivo.compareTo(o.getIdeSistemaCultivo());
	}

	public boolean isRowSelect() {
		return rowSelect;
	}

	public void setRowSelect(boolean rowSelect) {
		this.rowSelect = rowSelect;
	}
}