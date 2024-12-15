package br.gov.ba.seia.entity;

import java.math.BigDecimal;

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
@Table(name = "declaracao_inexigibilidade_info_unidade")
@XmlRootElement
public class DeclaracaoInexigibilidadeInfoUnidade extends AbstractEntity {
	
	private static final long serialVersionUID = -4626675933173658511L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "ide_declaracao_inexigibilidade_info_unidade", nullable = false)
	private Integer ideDeclaracaoInexigibilidadeInfoUnidade;
	
	@Column(name = "nom_unidade", nullable = false)
	private String nomUnidade;
	
	@Column(name = "val_area", nullable = false)
	private BigDecimal valArea;
	
	@Column(name = "val_area_inundada", nullable = false)
	private BigDecimal valAreaInundada;
	
	@JoinColumn(name = "ide_endereco", referencedColumnName = "ide_endereco")
	@ManyToOne(optional = false)
	private Endereco endereco;
	
	@JoinColumn(name = "ide_declaracao_inexigibilidade", referencedColumnName = "ide_declaracao_inexigibilidade", nullable = false)
	@ManyToOne(optional = false)
	private DeclaracaoInexigibilidade declaracaoInexigibilidade;
	
	public Integer getIdeDeclaracaoInexigibilidadeInfoUnidade() {
		return ideDeclaracaoInexigibilidadeInfoUnidade;
	}
	
	public String getNomUnidade() {
		return nomUnidade;
	}
	
	public BigDecimal getValArea() {
		return valArea;
	}
	
	public Endereco getEndereco() {
		return endereco;
	}
	
	public DeclaracaoInexigibilidade getDeclaracaoInexigibilidade() {
		return declaracaoInexigibilidade;
	}
	
	public void setIdeDeclaracaoInexigibilidadeInfoUnidade(Integer ideDeclaracaoInexigibilidadeInfoUnidade) {
		this.ideDeclaracaoInexigibilidadeInfoUnidade = ideDeclaracaoInexigibilidadeInfoUnidade;
	}
	
	public void setNomUnidade(String nomUnidade) {
		this.nomUnidade = nomUnidade;
	}
	
	public void setValArea(BigDecimal valArea) {
		this.valArea = valArea;
	}
	
	public BigDecimal getValAreaInundada() {
		return valAreaInundada;
	}
	
	public void setValAreaInundada(BigDecimal valAreaInundada) {
		this.valAreaInundada = valAreaInundada;
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
		result = prime * result + ((ideDeclaracaoInexigibilidadeInfoUnidade == null) ? 0 : ideDeclaracaoInexigibilidadeInfoUnidade.hashCode());
		result = prime * result + ((nomUnidade == null) ? 0 : nomUnidade.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		DeclaracaoInexigibilidadeInfoUnidade other = (DeclaracaoInexigibilidadeInfoUnidade) obj;
		if(declaracaoInexigibilidade == null) {
			if(other.declaracaoInexigibilidade != null) return false;
		} else if(!declaracaoInexigibilidade.equals(other.declaracaoInexigibilidade)) return false;
		if(endereco == null) {
			if(other.endereco != null) return false;
		} else if(!endereco.equals(other.endereco)) return false;
		if(ideDeclaracaoInexigibilidadeInfoUnidade == null) {
			if(other.ideDeclaracaoInexigibilidadeInfoUnidade != null) return false;
		} else if(!ideDeclaracaoInexigibilidadeInfoUnidade.equals(other.ideDeclaracaoInexigibilidadeInfoUnidade)) return false;
		if(nomUnidade == null) {
			if(other.nomUnidade != null) return false;
		} else if(!nomUnidade.equals(other.nomUnidade)) return false;
		return true;
	}
}
