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
 * The persistent class for the especie_hibrido database table.
 * 
 */
@Entity
@Table(name="especie_hibrido")
public class EspecieHibrido implements Serializable,BaseEntity, Comparable<EspecieHibrido> {
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "especie_hibrido_ide_especie_hibrido_generator")
	@SequenceGenerator(name = "especie_hibrido_ide_especie_hibrido_generator", sequenceName = "especie_hibrido_ide_especie_hibrido_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_especie_hibrido")
	private Integer ideEspecieHibrido;

	@NotNull
	@Size(min = 1, max = 50)
	@Column(name="dsc_especie_hibrido")
	private String dscEspecieHibrido;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

	@NotNull
	@Size(min = 1, max = 50)
	@Column(name="nom_genero")
	private String nomGenero;

	@NotNull
	@Size(min = 1, max = 50)
	@Column(name="nom_vulgar")
	private String nomVulgar;

    public EspecieHibrido() {
    }

	public Integer getIdeEspecieHibrido() {
		return this.ideEspecieHibrido;
	}

	public void setIdeEspecieHibrido(Integer ideEspecieHibrido) {
		this.ideEspecieHibrido = ideEspecieHibrido;
	}

	public String getDscEspecieHibrido() {
		return this.dscEspecieHibrido;
	}

	public void setDscEspecieHibrido(String dscEspecieHibrido) {
		this.dscEspecieHibrido = dscEspecieHibrido;
	}

	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	public String getNomGenero() {
		return this.nomGenero;
	}

	public void setNomGenero(String nomGenero) {
		this.nomGenero = nomGenero;
	}

	public String getNomVulgar() {
		return this.nomVulgar;
	}

	public void setNomVulgar(String nomVulgar) {
		this.nomVulgar = nomVulgar;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideEspecieHibrido == null) ? 0 : ideEspecieHibrido
						.hashCode());
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
		EspecieHibrido other = (EspecieHibrido) obj;
		if (ideEspecieHibrido == null) {
			if (other.ideEspecieHibrido != null)
				return false;
		} else if (!ideEspecieHibrido.equals(other.ideEspecieHibrido))
			return false;
		return true;
	}

	@Override
	public Long getId() {
		return new Long(ideEspecieHibrido);
	}

	@Override
	public int compareTo(EspecieHibrido arg0) {
		return arg0.dscEspecieHibrido.compareTo(dscEspecieHibrido) * -1;
	}

}