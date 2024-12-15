package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * The primary key class for the pessoa_fisica_contrato_convenio database table.
 * 
 */
@Embeddable
public class PessoaFisicaContratoConvenioPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="ide_pessoa_fisica")
	private Integer idePessoaFisica;

	@Column(name="ide_contrato_convenio")
	private Integer ideContratoConvenio;

    public PessoaFisicaContratoConvenioPK() {
    }
    
    public PessoaFisicaContratoConvenioPK(Integer idePessoaFisica, Integer ideContratoConvenio){
    	this.idePessoaFisica=idePessoaFisica;
    	this.ideContratoConvenio=ideContratoConvenio;
    }

	public Integer getIdePessoaFisica() {
		return idePessoaFisica;
	}

	public void setIdePessoaFisica(Integer idePessoaFisica) {
		this.idePessoaFisica = idePessoaFisica;
	}

	public Integer getIdeContratoConvenio() {
		return ideContratoConvenio;
	}

	public void setIdeContratoConvenio(Integer ideContratoConvenio) {
		this.ideContratoConvenio = ideContratoConvenio;
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
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PessoaFisicaContratoConvenioPK other = (PessoaFisicaContratoConvenioPK) obj;
		
		return new EqualsBuilder()
                .append(ideContratoConvenio, other.ideContratoConvenio)
                .append(idePessoaFisica, other.idePessoaFisica)
                .isEquals();
	}
    
	
}