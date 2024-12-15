package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "secao_revalidacao")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "SecaoRevalidacao.findAll", query = "SELECT s FROM SecaoRevalidacao s"),
		@NamedQuery(name = "SecaoRevalidacao.findByIdeSecaoRevalidacao", query = "SELECT s FROM SecaoRevalidacao s WHERE s.ideSecaoRevalidacao = :ideSecaoRevalidacao")})
public class SecaoRevalidacao implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "secao_revalidacao_ide_secao_revalidacao_seq")
	@SequenceGenerator(name = "secao_revalidacao_ide_secao_revalidacao_seq", sequenceName = "secao_revalidacao_ide_secao_revalidacao_seq", allocationSize = 1)
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_secao_revalidacao", nullable = false)
	private Integer ideSecaoRevalidacao;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 200)
	@Column(name = "dsc_secao_revalidacao", nullable = false, length = 200)
	private String dscSecaoRevalidacao;
	
	@JoinColumn(name = "ide_pai_secao_revalidacao", referencedColumnName = "ide_secao_revalidacao")
	@ManyToOne
	private SecaoRevalidacao idePaiSecaoRevalidacao;
    
	public SecaoRevalidacao() {
	}

	public SecaoRevalidacao(SecaoRevalidacao idePaiSecaoRevalidacao) {
		this.idePaiSecaoRevalidacao = idePaiSecaoRevalidacao;
	}

	public SecaoRevalidacao(Integer ideSecaoRevalidacao) {
		this.ideSecaoRevalidacao = ideSecaoRevalidacao;
	}

	

	public Integer getIdeSecaoRevalidacao() {
		return ideSecaoRevalidacao;
	}

	public void setIdeSecaoRevalidacao(Integer ideSecaoRevalidacao) {
		this.ideSecaoRevalidacao = ideSecaoRevalidacao;
	}

	public String getDscSecaoRevalidacao() {
		return dscSecaoRevalidacao;
	}

	public void setDscSecaoRevalidacao(String dscSecaoRevalidacao) {
		this.dscSecaoRevalidacao = dscSecaoRevalidacao;
	}

	public SecaoRevalidacao getIdePaiSecaoRevalidacao() {
		return idePaiSecaoRevalidacao;
	}

	public void setIdePaiSecaoRevalidacao(SecaoRevalidacao idePaiSecaoRevalidacao) {
		this.idePaiSecaoRevalidacao = idePaiSecaoRevalidacao;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideSecaoRevalidacao != null ? ideSecaoRevalidacao.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		
		
		if (!(object instanceof SecaoRevalidacao)) {
			return false;
		}
		SecaoRevalidacao other = (SecaoRevalidacao) object;
		if ((this.ideSecaoRevalidacao == null && other.ideSecaoRevalidacao != null)
				|| (this.ideSecaoRevalidacao != null && !this.ideSecaoRevalidacao.equals(other.ideSecaoRevalidacao))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {

		return String.valueOf(this.ideSecaoRevalidacao);
	}		
	
}