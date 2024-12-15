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

@Entity
@Table(name = "parametro", uniqueConstraints = { @UniqueConstraint(columnNames = { "ide_parametro" }) })
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Parametro.findAll", query = "SELECT p FROM Parametro p"),
		@NamedQuery(name = "Parametro.findByIdeParametro", query = "SELECT p FROM Parametro p WHERE p.ideParametro = :ideParametro"),
		@NamedQuery(name = "Parametro.findByNomParametro", query = "SELECT p FROM Parametro p WHERE p.nomParametro like :nomParametro")})
public class Parametro implements Serializable {

	private static final long serialVersionUID = 805352282021200269L;
	
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_parametro", nullable = false)
	private int ideParametro;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "nom_parametro", nullable = false, length = 100)
	private String nomParametro;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "dsc_valor", nullable = false, length = 100)
	private String dscValor;

	public Parametro() {
	}
	
	public Parametro(int pIdeParametro) {
		ideParametro = pIdeParametro;
	}

	public int getIdeParametro() {
		return ideParametro;
	}

	public void setIdeParametro(int ideParametro) {
		this.ideParametro = ideParametro;
	}

	public String getNomParametro() {
		return nomParametro;
	}

	public void setNomParametro(String dscParametro) {
		this.nomParametro = dscParametro;
	}

	public String getDscValor() {
		return dscValor;
	}

	public void setDscValor(String dscValor) {
		this.dscValor = dscValor;
	}
}