package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "caracteristica_efluente", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"nom_caracteristica_efluente"})})
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "CaracteristicaEfluente.findAll", query = "SELECT c FROM CaracteristicaEfluente c"),
	@NamedQuery(name = "CaracteristicaEfluente.findByIdeCaracteristicaEfluente", query = "SELECT c FROM CaracteristicaEfluente c WHERE c.ideCaracteristicaEfluente = :ideCaracteristicaEfluente"),
	@NamedQuery(name = "CaracteristicaEfluente.findByNomCaracteristicaEfluente", query = "SELECT c FROM CaracteristicaEfluente c WHERE c.nomCaracteristicaEfluente = :nomCaracteristicaEfluente")})
public class CaracteristicaEfluente implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_caracteristica_efluente", nullable = false)
	private Integer ideCaracteristicaEfluente;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "nom_caracteristica_efluente", nullable = false, length = 100)
	private String nomCaracteristicaEfluente;

	public CaracteristicaEfluente() {
	}

	public CaracteristicaEfluente(Integer ideCaracteristicaEfluente) {
		this.ideCaracteristicaEfluente = ideCaracteristicaEfluente;
	}

	public CaracteristicaEfluente(Integer ideCaracteristicaEfluente, String nomCaracteristicaEfluente) {
		this.ideCaracteristicaEfluente = ideCaracteristicaEfluente;
		this.nomCaracteristicaEfluente = nomCaracteristicaEfluente;
	}

	public Integer getIdeCaracteristicaEfluente() {
		return ideCaracteristicaEfluente;
	}

	public void setIdeCaracteristicaEfluente(Integer ideCaracteristicaEfluente) {
		this.ideCaracteristicaEfluente = ideCaracteristicaEfluente;
	}

	public String getNomCaracteristicaEfluente() {
		return nomCaracteristicaEfluente;
	}

	public void setNomCaracteristicaEfluente(String nomCaracteristicaEfluente) {
		this.nomCaracteristicaEfluente = nomCaracteristicaEfluente;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideCaracteristicaEfluente != null ? ideCaracteristicaEfluente.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		
		if (!(object instanceof CaracteristicaEfluente)) {
			return false;
		}
		CaracteristicaEfluente other = (CaracteristicaEfluente) object;
		if ((this.ideCaracteristicaEfluente == null && other.ideCaracteristicaEfluente != null) || (this.ideCaracteristicaEfluente != null && !this.ideCaracteristicaEfluente.equals(other.ideCaracteristicaEfluente))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "br.gov.ba.seia.entity.CaracteristicaEfluente[ ideCaracteristicaEfluente=" + ideCaracteristicaEfluente + " ]";
	}

	public boolean isColiformes(){
		return this.ideCaracteristicaEfluente.compareTo(2) == 0;
	}
}
