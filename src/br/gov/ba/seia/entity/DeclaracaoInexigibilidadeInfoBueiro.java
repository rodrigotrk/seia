package br.gov.ba.seia.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "declaracao_inexigibilidade_info_bueiro")
@XmlRootElement
public class DeclaracaoInexigibilidadeInfoBueiro extends AbstractEntity {
	
	private static final long serialVersionUID = -2995729691829852652L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "ide_declaracao_inexigibilidade_info_bueiro", nullable = false)
	private Integer ideDeclaracaoInexigibilidadeInfoBueiro;
	
	@Column(name = "num_bueiro", nullable = false)
	private Integer numBueiro;
	
	@Column(name = "des_trajeto_via", nullable = false)
	private String desTrajetoVia;
	
	@JoinColumn(name = "ide_declaracao_inexigibilidade", referencedColumnName = "ide_declaracao_inexigibilidade", nullable = false)
	@ManyToOne(optional = false)
	private DeclaracaoInexigibilidade declaracaoInexigibilidade;
	
	public Integer getIdeDeclaracaoInexigibilidadeInfoBueiro() {
		return ideDeclaracaoInexigibilidadeInfoBueiro;
	}
	
	public Integer getNumBueiro() {
		return numBueiro;
	}
	
	public String getDesTrajetoVia() {
		return desTrajetoVia;
	}
	
	public DeclaracaoInexigibilidade getDeclaracaoInexigibilidade() {
		return declaracaoInexigibilidade;
	}
	
	public void setIdeDeclaracaoInexigibilidadeInfoBueiro(Integer ideDeclaracaoInexigibilidadeInfoBueiro) {
		this.ideDeclaracaoInexigibilidadeInfoBueiro = ideDeclaracaoInexigibilidadeInfoBueiro;
	}
	
	public void setNumBueiro(Integer numBueiro) {
		this.numBueiro = numBueiro;
	}
	
	public void setDesTrajetoVia(String desTrajetoVia) {
		this.desTrajetoVia = desTrajetoVia;
	}
	
	public void setDeclaracaoInexigibilidade(DeclaracaoInexigibilidade declaracaoInexigibilidade) {
		this.declaracaoInexigibilidade = declaracaoInexigibilidade;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((declaracaoInexigibilidade == null) ? 0 : declaracaoInexigibilidade.hashCode());
		result = prime * result + ((desTrajetoVia == null) ? 0 : desTrajetoVia.hashCode());
		result = prime * result + ((ideDeclaracaoInexigibilidadeInfoBueiro == null) ? 0 : ideDeclaracaoInexigibilidadeInfoBueiro.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		DeclaracaoInexigibilidadeInfoBueiro other = (DeclaracaoInexigibilidadeInfoBueiro) obj;
		if(declaracaoInexigibilidade == null) {
			if(other.declaracaoInexigibilidade != null) return false;
		} else if(!declaracaoInexigibilidade.equals(other.declaracaoInexigibilidade)) return false;
		if(desTrajetoVia == null) {
			if(other.desTrajetoVia != null) return false;
		} else if(!desTrajetoVia.equals(other.desTrajetoVia)) return false;
		if(ideDeclaracaoInexigibilidadeInfoBueiro == null) {
			if(other.ideDeclaracaoInexigibilidadeInfoBueiro != null) return false;
		} else if(!ideDeclaracaoInexigibilidadeInfoBueiro.equals(other.ideDeclaracaoInexigibilidadeInfoBueiro)) return false;
		return true;
	}
}
