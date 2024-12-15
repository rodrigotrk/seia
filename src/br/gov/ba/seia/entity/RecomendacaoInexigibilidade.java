package br.gov.ba.seia.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "RECOMENDACAO_INEXIGIBILIDADE")
@XmlRootElement
public class RecomendacaoInexigibilidade extends AbstractEntity {
	
	private static final long serialVersionUID = -3625452698740258214L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "IDE_RECOMENDACAO_INEXIGIBILIDADE", nullable = false)
	private Integer ideRecomendacaoInexigibilidade;
	
	@Column(name = "DES_RECOMENDACAO_INEXIGIBILIDADE", nullable = false)
	private String desRecomendacaoInexigibilidade;
	
	@Column(name = "ind_ativo", nullable = false)
	private Boolean indAtivo;
	
	public Integer getIdeRecomendacaoInexigibilidade() {
		return ideRecomendacaoInexigibilidade;
	}
	
	public void setIdeRecomendacaoInexigibilidade(Integer ideRecomendacaoInexigibilidade) {
		this.ideRecomendacaoInexigibilidade = ideRecomendacaoInexigibilidade;
	}
	
	public String getDesRecomendacaoInexigibilidade() {
		return desRecomendacaoInexigibilidade;
	}
	
	public void setDesRecomendacaoInexigibilidade(String desRecomendacaoInexigibilidade) {
		this.desRecomendacaoInexigibilidade = desRecomendacaoInexigibilidade;
	}
	
	public Boolean getIndAtivo() {
		return indAtivo;
	}
	
	public void setIndAtivo(Boolean indAtivo) {
		this.indAtivo = indAtivo;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((desRecomendacaoInexigibilidade == null) ? 0 : desRecomendacaoInexigibilidade.hashCode());
		result = prime * result + ((ideRecomendacaoInexigibilidade == null) ? 0 : ideRecomendacaoInexigibilidade.hashCode());
		result = prime * result + ((indAtivo == null) ? 0 : indAtivo.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		RecomendacaoInexigibilidade other = (RecomendacaoInexigibilidade) obj;
		if(desRecomendacaoInexigibilidade == null) {
			if(other.desRecomendacaoInexigibilidade != null) return false;
		} else if(!desRecomendacaoInexigibilidade.equals(other.desRecomendacaoInexigibilidade)) return false;
		if(ideRecomendacaoInexigibilidade == null) {
			if(other.ideRecomendacaoInexigibilidade != null) return false;
		} else if(!ideRecomendacaoInexigibilidade.equals(other.ideRecomendacaoInexigibilidade)) return false;
		if(indAtivo == null) {
			if(other.indAtivo != null) return false;
		} else if(!indAtivo.equals(other.indAtivo)) return false;
		return true;
	}
}
