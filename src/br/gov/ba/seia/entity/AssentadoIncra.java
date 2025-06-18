package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@XmlRootElement
@Table(name="assentado_incra")
@NamedQuery(name="AssentadoIncra.findAll", query="SELECT a FROM AssentadoIncra a")
public class AssentadoIncra implements Serializable {
	
	private static final long serialVersionUID = 1400983028771211979L;
	
	@Id
	@SequenceGenerator(name="ASSENTADO_INCRA_SEQ", sequenceName="ASSENTADO_INCRA_SEQ" , allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ASSENTADO_INCRA_SEQ")
	@NotNull
	@Column(name="ide_assentado_incra", nullable=false)
	@Basic(optional=false)
	private Integer ideAssentadoIncra;
	
	@Basic	
	@Column(name="cod_sipra")
	private String codSipra;

	@NotNull
	@JoinColumn(name="ide_pessoa_fisica", referencedColumnName="ide_pessoa_fisica", nullable=false)
	@OneToOne(optional=false)
	private PessoaFisica idePessoaFisica;

	public AssentadoIncra() {
	}


	public Integer getIdeAssentadoIncra() {
		return this.ideAssentadoIncra;
	}

	public void setIdeAssentadoIncra(Integer ideAssentadoIncra) {
		this.ideAssentadoIncra = ideAssentadoIncra;
	}

	public String getCodSipra() {
		return this.codSipra;
	}

	public void setCodSipra(String codSipra) {
		this.codSipra = codSipra;
	}

	public PessoaFisica getIdePessoaFisica() {
		return this.idePessoaFisica;
	}

	public void setIdePessoaFisica(PessoaFisica idePessoaFisica) {
		this.idePessoaFisica = idePessoaFisica;
	}
	
	@Override
	public String toString() {
		return String.valueOf(this.ideAssentadoIncra);
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (this.ideAssentadoIncra != null ? this.ideAssentadoIncra.hashCode() : 0);
		return hash;
    }

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof AssentadoIncra)) {
			return false;
		}
		AssentadoIncra other = (AssentadoIncra) object;
		if ((this.ideAssentadoIncra == null && other.ideAssentadoIncra != null)
				|| (this.ideAssentadoIncra != null && !this.ideAssentadoIncra.equals(other.ideAssentadoIncra))) {
			return false;
		}
		return true;
	}

}