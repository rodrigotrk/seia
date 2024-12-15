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
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;


/**
 * The persistent class for the classificacao_defensivo_agricola database table.
 * 
 */
@Entity
@XmlRootElement
@Table(name="classificacao_defensivo_agricola")
public class ClassificacaoDefensivoAgricola implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "classificao_defensivo_agricola_ide_classificao_defensivo_agricola_generator")
	@SequenceGenerator(name ="classificao_defensivo_agricola_ide_classificao_defensivo_agricola_generator", sequenceName = "classificao_defensivo_agricola_ide_classificao_defensivo_agricola_seq", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_classificacao_defensivo_agricola")
	private Integer ideClassificacaoDefensivoAgricola;

	@NotNull
	@Size(min = 1, max = 20)
	@Column(name="dsc_classificacao_defensivo_agricola")
	private String dscClassificacaoDefensivoAgricola;

	@Column(name="ind_ativo")
	private Boolean indAtivo;

    public ClassificacaoDefensivoAgricola() {
    }

    public ClassificacaoDefensivoAgricola(Integer ideClassificacaoDefensivoAgricola){
    	this.ideClassificacaoDefensivoAgricola=ideClassificacaoDefensivoAgricola;
    }
    
    public ClassificacaoDefensivoAgricola(String dscClassificacaoDefensivoAgricola){
    	this.dscClassificacaoDefensivoAgricola=dscClassificacaoDefensivoAgricola;
    }
    
	public Boolean getIndAtivo() {
		return this.indAtivo;
	}

	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}


	public Integer getIdeClassificacaoDefensivoAgricola() {
		return ideClassificacaoDefensivoAgricola;
	}


	public void setIdeClassificacaoDefensivoAgricola(
			Integer ideClassificacaoDefensivoAgricola) {
		this.ideClassificacaoDefensivoAgricola = ideClassificacaoDefensivoAgricola;
	}


	public String getDscClassificacaoDefensivoAgricola() {
		return dscClassificacaoDefensivoAgricola;
	}


	public void setDscClassificacaoDefensivoAgricola(
			String dscClassificacaoDefensivoAgricola) {
		this.dscClassificacaoDefensivoAgricola = dscClassificacaoDefensivoAgricola;
	}

	@Override
	public Long getId() {
		return new Long(this.ideClassificacaoDefensivoAgricola);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((ideClassificacaoDefensivoAgricola == null) ? 0
						: ideClassificacaoDefensivoAgricola.hashCode());
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
		ClassificacaoDefensivoAgricola other = (ClassificacaoDefensivoAgricola) obj;
		if (ideClassificacaoDefensivoAgricola == null) {
			if (other.ideClassificacaoDefensivoAgricola != null)
				return false;
		} else if (!ideClassificacaoDefensivoAgricola
				.equals(other.ideClassificacaoDefensivoAgricola))
			return false;
		return true;
	}

}