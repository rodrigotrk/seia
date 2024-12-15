package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "caracteristica_floresta_producao")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "CaracteristicaFlorestaProducao.findByIdeCaracteristicaFlorestaProducao", query = "SELECT c FROM CaracteristicaFlorestaProducao c WHERE c.ideCaracteristicaFlorestaProducao = :ideCaracteristicaFlorestaProducao") })
public class CaracteristicaFlorestaProducao implements Serializable, BaseEntity {

	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_caracteristica_floresta_producao")
	private Integer ideCaracteristicaFlorestaProducao;

	@NotNull
	@Basic(optional = false)
	
	@Column(name = "nom_caracteristica_floresta_producao", nullable = false, length = 50, unique = true)
	private String nomCaracteristicaFlorestaProducao;

	@Column(name = "ind_ativo")
	private Boolean indAtivo;

	@Transient
	private boolean rowSelect = false;

	public CaracteristicaFlorestaProducao() {
	}

	public CaracteristicaFlorestaProducao(Integer ideCaracteristicaFlorestaProducao,
			String nomCaracteristicaFlorestaProducao, Boolean indAtivo) {
		this.ideCaracteristicaFlorestaProducao = ideCaracteristicaFlorestaProducao;
		this.nomCaracteristicaFlorestaProducao = nomCaracteristicaFlorestaProducao;
		this.indAtivo = indAtivo;
	}

	public Integer getIdeCaracteristicaFlorestaProducao() {
		return ideCaracteristicaFlorestaProducao;
	}

	public void setIdeCaracteristicaFlorestaProducao(Integer ideCaracteristicaFlorestaProducao) {
		this.ideCaracteristicaFlorestaProducao = ideCaracteristicaFlorestaProducao;
	}

	public String getNomCaracteristicaFlorestaProducao() {
		return nomCaracteristicaFlorestaProducao;
	}

	public void setNomCaracteristicaFlorestaProducao(String nomCaracteristicaFlorestaProducao) {
		this.nomCaracteristicaFlorestaProducao = nomCaracteristicaFlorestaProducao;
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
		result = prime * result
				+ ((ideCaracteristicaFlorestaProducao == null) ? 0 : ideCaracteristicaFlorestaProducao.hashCode());
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
		CaracteristicaFlorestaProducao other = (CaracteristicaFlorestaProducao) obj;
		if (ideCaracteristicaFlorestaProducao == null) {
			if (other.ideCaracteristicaFlorestaProducao != null)
				return false;
		} else if (!ideCaracteristicaFlorestaProducao.equals(other.ideCaracteristicaFlorestaProducao))
			return false;
		return true;
	}

	public boolean isRowSelect() {
		return rowSelect;
	}

	public void setRowSelect(boolean rowSelect) {
		this.rowSelect = rowSelect;
	}

	@Override
	public Long getId() {
		return new Long(this.ideCaracteristicaFlorestaProducao);
	}

}
