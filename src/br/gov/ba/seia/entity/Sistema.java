package br.gov.ba.seia.entity;

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

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "sistema")
@XmlRootElement
public class Sistema extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sistema_ide_sistema_seq")
	@SequenceGenerator(name = "sistema_ide_sistema_seq", sequenceName = "sistema_ide_sistema_seq", allocationSize = 1)
	@Column(name = "ide_sistema", nullable = false)
	private Integer ideSistema;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 40)
	@Column(name = "nom_sistema", nullable = false, length = 50)
	private String nomSistema;

	public Sistema() {

	}

	public Sistema(Integer ideSistema, String nomSistema) {
		this.ideSistema = ideSistema;
		this.nomSistema = nomSistema;
	}

	public Sistema(Integer ideSistema) {
		this.ideSistema = ideSistema;
	}

	public Integer getIdeSistema() {
		return this.ideSistema;
	}

	public void setIdeSistema(Integer ideSistema) {
		this.ideSistema = ideSistema;
	}

	public String getNomSistema() {
		return nomSistema;
	}

	public void setNomSistema(String nomSistema) {
		this.nomSistema = nomSistema;
	}
}