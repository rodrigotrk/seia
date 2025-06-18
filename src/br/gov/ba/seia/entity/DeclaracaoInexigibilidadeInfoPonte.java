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
@Table(name = "declaracao_inexigibilidade_info_ponte")
@XmlRootElement
public class DeclaracaoInexigibilidadeInfoPonte extends AbstractEntity {
	
	private static final long serialVersionUID = -750756022564348878L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "ide_declaracao_inexigibilidade_info_ponte", nullable = false)
	private Integer ideDeclaracaoInexigibilidadeInfoPonte;
	
	@Column(name = "nom_ponte")
	private String nomPonte;
	
	@JoinColumn(name = "ide_declaracao_inexigibilidade", referencedColumnName = "ide_declaracao_inexigibilidade", nullable = false)
	@ManyToOne(optional = false)
	private DeclaracaoInexigibilidade declaracaoInexigibilidade;
	
	public Integer getIdeDeclaracaoInexigibilidadeInfoPonte() {
		return ideDeclaracaoInexigibilidadeInfoPonte;
	}
	
	public String getNomPonte() {
		return nomPonte;
	}
	
	public DeclaracaoInexigibilidade getDeclaracaoInexigibilidade() {
		return declaracaoInexigibilidade;
	}
	
	public void setIdeDeclaracaoInexigibilidadeInfoPonte(Integer ideDeclaracaoInexigibilidadeInfoPonte) {
		this.ideDeclaracaoInexigibilidadeInfoPonte = ideDeclaracaoInexigibilidadeInfoPonte;
	}
	
	public void setNomPonte(String nomPonte) {
		this.nomPonte = nomPonte;
	}
	
	public void setDeclaracaoInexigibilidade(DeclaracaoInexigibilidade declaracaoInexigibilidade) {
		this.declaracaoInexigibilidade = declaracaoInexigibilidade;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((declaracaoInexigibilidade == null) ? 0 : declaracaoInexigibilidade.hashCode());
		result = prime * result + ((ideDeclaracaoInexigibilidadeInfoPonte == null) ? 0 : ideDeclaracaoInexigibilidadeInfoPonte.hashCode());
		result = prime * result + ((nomPonte == null) ? 0 : nomPonte.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		DeclaracaoInexigibilidadeInfoPonte other = (DeclaracaoInexigibilidadeInfoPonte) obj;
		if(declaracaoInexigibilidade == null) {
			if(other.declaracaoInexigibilidade != null) return false;
		} else if(!declaracaoInexigibilidade.equals(other.declaracaoInexigibilidade)) return false;
		if(ideDeclaracaoInexigibilidadeInfoPonte == null) {
			if(other.ideDeclaracaoInexigibilidadeInfoPonte != null) return false;
		} else if(!ideDeclaracaoInexigibilidadeInfoPonte.equals(other.ideDeclaracaoInexigibilidadeInfoPonte)) return false;
		if(nomPonte == null) {
			if(other.nomPonte != null) return false;
		} else if(!nomPonte.equals(other.nomPonte)) return false;
		return true;
	}
}
