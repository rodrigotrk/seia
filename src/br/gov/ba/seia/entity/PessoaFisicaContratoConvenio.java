package br.gov.ba.seia.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "pessoa_fisica_contrato_convenio")
public class PessoaFisicaContratoConvenio extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PessoaFisicaContratoConvenioPK pessoaFisicaContratoConvenioPK;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ide_pessoa_fisica",nullable = false, insertable = false, updatable = false)
	private PessoaFisica idePessoaFisica;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "ide_contrato_convenio",nullable = false, insertable = false, updatable = false)
	private ContratoConvenio ideContratoConvenio;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_criacao")
	private Date dtcCriacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dtc_exclusao")
	private Date dtcExclusao;
	
	@Column(name="ind_ativo")
	private boolean indAtivo;
	
	
	public PessoaFisicaContratoConvenio() {
	}

	public PessoaFisicaContratoConvenio (PessoaFisicaContratoConvenioPK pessoaFisicaContratoConvenioPK){
		this.pessoaFisicaContratoConvenioPK=pessoaFisicaContratoConvenioPK;
	}

	public PessoaFisicaContratoConvenioPK getPessoaFisicaContratoConvenioPK() {
		return pessoaFisicaContratoConvenioPK;
	}

	public void setPessoaFisicaContratoConvenioPK(
			PessoaFisicaContratoConvenioPK pessoaFisicaContratoConvenioPK) {
		this.pessoaFisicaContratoConvenioPK = pessoaFisicaContratoConvenioPK;
	}

	public PessoaFisica getIdePessoaFisica() {
		return idePessoaFisica;
	}


	public void setIdePessoaFisica(PessoaFisica idePessoaFisica) {
		this.idePessoaFisica = idePessoaFisica;
	}


	public ContratoConvenio getIdeContratoConvenio() {
		return ideContratoConvenio;
	}


	public void setIdeContratoConvenio(ContratoConvenio ideContratoConvenio) {
		this.ideContratoConvenio = ideContratoConvenio;
	}


	public Date getDtcCriacao() {
		return dtcCriacao;
	}


	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}


	public Date getDtcExclusao() {
		return dtcExclusao;
	}


	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}


	public boolean isIndAtivo() {
		return indAtivo;
	}


	public void setIndAtivo(boolean indAtivo) {
		this.indAtivo = indAtivo;
	}

	/**
	 * Verifica o hascode considerando os campos ideContratoConvenio e o idePessoaFisica
	 */
	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(ideContratoConvenio)
				.append(idePessoaFisica)
				.toHashCode();
	}

	/**
	 * Verifica se os objetos são iguais considerando os campos ideContratoConvenio e o idePessoaFisica
	 */
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass()) {
			return false;
		}
		PessoaFisicaContratoConvenio other = (PessoaFisicaContratoConvenio) obj;
		
		return new EqualsBuilder()
                .append(ideContratoConvenio, other.ideContratoConvenio)
                .append(idePessoaFisica, other.idePessoaFisica)
                .isEquals();
	}

	@Override
	public String toString() {
		return "PessoaFisicaContratoConvenio [idePessoaFisica="
				+ idePessoaFisica + ", ideContratoConvenio="
				+ ideContratoConvenio + "]";
	}

}