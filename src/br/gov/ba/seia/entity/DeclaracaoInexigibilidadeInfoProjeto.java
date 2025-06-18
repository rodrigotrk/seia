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
@Table(name = "declaracao_inexigibilidade_info_projeto")
@XmlRootElement
public class DeclaracaoInexigibilidadeInfoProjeto extends AbstractEntity {
	
	private static final long serialVersionUID = -1427496815275278016L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "ide_declaracao_inexigibilidade_info_projeto", nullable = false)
	private Integer ideDeclaracaoInexigibilidadeInfoProjeto;
	
	@Column(name = "nom_projeto", nullable = false)
	private String nomProjeto;
	
	@JoinColumn(name = "ide_endereco", referencedColumnName = "ide_endereco")
	@ManyToOne(optional = false)
	private Endereco endereco;
	
	@JoinColumn(name = "ide_declaracao_inexigibilidade", referencedColumnName = "ide_declaracao_inexigibilidade", nullable = false)
	@ManyToOne(optional = false)
	private DeclaracaoInexigibilidade declaracaoInexigibilidade;
	
	public Integer getIdeDeclaracaoInexigibilidadeInfoProjeto() {
		return ideDeclaracaoInexigibilidadeInfoProjeto;
	}
	
	public String getNomProjeto() {
		return nomProjeto;
	}
	
	public Endereco getEndereco() {
		return endereco;
	}
	
	public DeclaracaoInexigibilidade getDeclaracaoInexigibilidade() {
		return declaracaoInexigibilidade;
	}
	
	public void setIdeDeclaracaoInexigibilidadeInfoProjeto(Integer ideDeclaracaoInexigibilidadeInfoProjeto) {
		this.ideDeclaracaoInexigibilidadeInfoProjeto = ideDeclaracaoInexigibilidadeInfoProjeto;
	}
	
	public void setNomProjeto(String nomProjeto) {
		this.nomProjeto = nomProjeto;
	}
	
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	
	public void setDeclaracaoInexigibilidade(DeclaracaoInexigibilidade declaracaoInexigibilidade) {
		this.declaracaoInexigibilidade = declaracaoInexigibilidade;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((declaracaoInexigibilidade == null) ? 0 : declaracaoInexigibilidade.hashCode());
		result = prime * result + ((endereco == null) ? 0 : endereco.hashCode());
		result = prime * result + ((ideDeclaracaoInexigibilidadeInfoProjeto == null) ? 0 : ideDeclaracaoInexigibilidadeInfoProjeto.hashCode());
		result = prime * result + ((nomProjeto == null) ? 0 : nomProjeto.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		DeclaracaoInexigibilidadeInfoProjeto other = (DeclaracaoInexigibilidadeInfoProjeto) obj;
		if(declaracaoInexigibilidade == null) {
			if(other.declaracaoInexigibilidade != null) return false;
		} else if(!declaracaoInexigibilidade.equals(other.declaracaoInexigibilidade)) return false;
		if(endereco == null) {
			if(other.endereco != null) return false;
		} else if(!endereco.equals(other.endereco)) return false;
		if(ideDeclaracaoInexigibilidadeInfoProjeto == null) {
			if(other.ideDeclaracaoInexigibilidadeInfoProjeto != null) return false;
		} else if(!ideDeclaracaoInexigibilidadeInfoProjeto.equals(other.ideDeclaracaoInexigibilidadeInfoProjeto)) return false;
		if(nomProjeto == null) {
			if(other.nomProjeto != null) return false;
		} else if(!nomProjeto.equals(other.nomProjeto)) return false;
		return true;
	}
}
