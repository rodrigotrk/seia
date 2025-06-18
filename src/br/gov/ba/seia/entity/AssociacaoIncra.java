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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;


@Entity
@Table(name="associacao_incra")
@XmlRootElement
@NamedQuery(name="AssociacaoIncra.findAll", query="SELECT a FROM AssociacaoIncra a")
public class AssociacaoIncra implements Serializable {
	private static final long serialVersionUID = -6311043984007037306L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ASSOCIACAO_INCRA_SEQ")
	@SequenceGenerator(name="ASSOCIACAO_INCRA_SEQ", sequenceName = "ASSOCIACAO_INCRA_SEQ", allocationSize = 1)
	@Basic(optional = false)
	@Column(name="ide_associacao_incra", nullable = false)
	@NotNull
	private Integer ideAssociacaoIncra;

	@Basic
	@Column(name="des_email")
	private String desEmail;
	
	@JoinColumn(name="ide_pessoa_juridica", referencedColumnName ="ide_pessoa_juridica", nullable = false)
	@OneToOne(optional=false)
	private PessoaJuridica idePessoaJuridica;
	
	@Basic(optional = false)
	@Column(name="nom_presidente", nullable = false)
	private String nomPresidente;
	
	@Basic
	@Column(name="num_telefone")
	private String numTelefone;
	
	@Transient
	private boolean indEdicao;

	@Transient
	private boolean indExcluido;
	
	@Transient
	private boolean indVisualizacao;

	public AssociacaoIncra() {
	}
	
	public AssociacaoIncra(Integer ideAssociacaoIncra) {
		this.ideAssociacaoIncra = ideAssociacaoIncra;
	}


	public Integer getIdeAssociacaoIncra() {
		return this.ideAssociacaoIncra;
	}

	public void setIdeAssociacaoIncra(Integer ideAssociacaoIncra) {
		this.ideAssociacaoIncra = ideAssociacaoIncra;
	}


	public String getDesEmail() {
		return this.desEmail;
	}

	public void setDesEmail(String desEmail) {
		this.desEmail = desEmail;
	}


	public PessoaJuridica getIdePessoaJuridica() {
		return this.idePessoaJuridica;
	}

	public void setIdePessoaJuridica(PessoaJuridica idePessoaJuridica) {
		this.idePessoaJuridica = idePessoaJuridica;
	}


	public String getNomPresidente() {
		return this.nomPresidente;
	}

	public void setNomPresidente(String nomPresidente) {
		this.nomPresidente = nomPresidente;
	}


	public String getNumTelefone() {
		return this.numTelefone;
	}

	public void setNumTelefone(String numTelefone) {
		this.numTelefone = numTelefone;
	}
	
	@Override
	public String toString() {
		return String.valueOf(ideAssociacaoIncra);
	}
	
	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideAssociacaoIncra != null ? ideAssociacaoIncra.hashCode() : 0);
		return hash;
    }

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof AssociacaoIncra)) {
			return false;
		}
		AssociacaoIncra other = (AssociacaoIncra) object;
		if ((this.ideAssociacaoIncra == null && other.ideAssociacaoIncra != null)
				|| (this.ideAssociacaoIncra != null && !this.ideAssociacaoIncra.equals(other.ideAssociacaoIncra))) {
			return false;
		}
		return true;
	}


	public boolean isIndEdicao() {
		return indEdicao;
	}


	public void setIndEdicao(boolean indEdicao) {
		this.indEdicao = indEdicao;
	}


	public boolean isIndExcluido() {
		return indExcluido;
	}


	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}


	public boolean isIndVisualizacao() {
		return indVisualizacao;
	}


	public void setIndVisualizacao(boolean indVisualizacao) {
		this.indVisualizacao = indVisualizacao;
	}
                                       
}