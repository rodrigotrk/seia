package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import br.gov.ba.seia.util.Util;

@Entity
@Table(name="requerimento_procedimento_especial", uniqueConstraints={@UniqueConstraint(columnNames={"ide_requerimento"})})
@XmlRootElement
public class RequerimentoProcedimentoEspecial implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name="requerimento_procedimento_esp_ide_requerimento_procedimento_seq", sequenceName="requerimento_procedimento_esp_ide_requerimento_procedimento_seq", allocationSize=1)
	@GeneratedValue(generator="requerimento_procedimento_esp_ide_requerimento_procedimento_seq") 
	@Column(name="ide_requerimento_procedimento_especial", nullable=false)
	private Integer ideRequerimentoProcedimentoEspecial;
	
	@JoinColumn(name="ide_requerimento", referencedColumnName="ide_requerimento", nullable=false)
	@OneToOne(fetch=FetchType.EAGER, optional=false)
	private Requerimento ideRequerimento;
	
	@Column(name="dt_assinatura_termo_compromisso")
	@Temporal(TemporalType.DATE)
	private Date dataAssinaturaTermoCompromisso;
	
	public RequerimentoProcedimentoEspecial() {
	
	}

	public RequerimentoProcedimentoEspecial(Integer ideRequerimentoProcedimentoEspecial) {
		super();
		this.ideRequerimentoProcedimentoEspecial = ideRequerimentoProcedimentoEspecial;
	}

	public RequerimentoProcedimentoEspecial(Requerimento ideRequerimento) {
		super();
		this.ideRequerimento = ideRequerimento;
	}
	
	public RequerimentoProcedimentoEspecial(Integer ideRequerimentoProcedimentoEspecial, Requerimento ideRequerimento) {
		super();
		this.ideRequerimentoProcedimentoEspecial = ideRequerimentoProcedimentoEspecial;
		this.ideRequerimento = ideRequerimento;
	}
	
	public Integer getIdeRequerimentoProcedimentoEspecial() {
		return ideRequerimentoProcedimentoEspecial;
	}

	public void setIdeRequerimentoProcedimentoEspecial(
			Integer ideRequerimentoProcedimentoEspecial) {
		this.ideRequerimentoProcedimentoEspecial = ideRequerimentoProcedimentoEspecial;
	}

	public Requerimento getIdeRequerimento() {
		return ideRequerimento;
	}

	public void setIdeRequerimento(Requerimento ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}

	public Date getDataAssinaturaTermoCompromisso() {
		return dataAssinaturaTermoCompromisso;
	}

	public void setDataAssinaturaTermoCompromisso(
			Date dataAssinaturaTermoCompromisso) {
		this.dataAssinaturaTermoCompromisso = dataAssinaturaTermoCompromisso;
	}
	
	public String getDataAssinaturaTermoCompromissoFormatado() {
		if(!Util.isNullOuVazio(dataAssinaturaTermoCompromisso)) {
			SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");  
			return spf.format(dataAssinaturaTermoCompromisso);
		} else {
			return "-";
		}
	}

	/**
	 * Verifica o hascode considerando os campos dataAssinaturaTermoCompromisso, ideRequerimento
	 * e o ideRequerimentoProcedimentoEspecial
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(dataAssinaturaTermoCompromisso)
				.append(ideRequerimento)
				.append(ideRequerimentoProcedimentoEspecial)
				.toHashCode();
	}

	/**
	 * Verifica se os objetos são iguais considerando os campos dataAssinaturaTermoCompromisso, ideRequerimento
	 * e o ideRequerimentoProcedimentoEspecial
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RequerimentoProcedimentoEspecial other = (RequerimentoProcedimentoEspecial) obj;
		
		return new EqualsBuilder()
                .append(dataAssinaturaTermoCompromisso, other.dataAssinaturaTermoCompromisso)
                .append(ideRequerimento, other.ideRequerimento)
                .append(ideRequerimentoProcedimentoEspecial, other.ideRequerimentoProcedimentoEspecial)
                .isEquals();
	}

	@Override
	public String toString() {
		return "RequerimentoProcedimentoEspecial [ideRequerimentoProcedimentoEspecial="
				+ ideRequerimentoProcedimentoEspecial
				+ ", ideRequerimento="
				+ ideRequerimento
				+ ", dataAssinaturaTermoCompromisso="
				+ dataAssinaturaTermoCompromisso+"]";
	}
	
	@Override
	protected RequerimentoProcedimentoEspecial clone() throws CloneNotSupportedException {
		return (RequerimentoProcedimentoEspecial) super.clone();
	}	

}
