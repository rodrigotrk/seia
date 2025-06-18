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
@Table(name = "declaracao_inexigibilidade")
@XmlRootElement
public class DeclaracaoInexigibilidade extends AbstractEntity {
	
	private static final long serialVersionUID = 8132173994559065358L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "ide_declaracao_inexigibilidade", nullable = false)
	private Integer ideDeclaracaoInexigibilidade;
	
	@JoinColumn(name = "ide_atividade_inexigivel", referencedColumnName = "ide_atividade_inexigivel")
	@ManyToOne(optional = false)
	private AtividadeInexigivel atividadeInexigivel;
	
	@JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento")
	@ManyToOne(optional = false)
	private Requerimento requerimento;
	
	@Column(name = "ind_ciente_recomendacao")
	private Boolean indCienteRecomendacao;
	
	public Integer getIdeDeclaracaoInexigibilidade() {
		return ideDeclaracaoInexigibilidade;
	}
	
	public AtividadeInexigivel getAtividadeInexigivel() {
		return atividadeInexigivel;
	}
	
	public Requerimento getRequerimento() {
		return requerimento;
	}
	
	public Boolean getIndCienteRecomendacao() {
		return indCienteRecomendacao;
	}
	
	public void setIdeDeclaracaoInexigibilidade(Integer ideDeclaracaoInexigibilidade) {
		this.ideDeclaracaoInexigibilidade = ideDeclaracaoInexigibilidade;
	}
	
	public void setAtividadeInexigivel(AtividadeInexigivel atividadeInexigivel) {
		this.atividadeInexigivel = atividadeInexigivel;
	}
	
	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}
	
	public void setIndCienteRecomendacao(Boolean indCienteRecomendacao) {
		this.indCienteRecomendacao = indCienteRecomendacao;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((atividadeInexigivel == null) ? 0 : atividadeInexigivel.hashCode());
		result = prime * result + ((ideDeclaracaoInexigibilidade == null) ? 0 : ideDeclaracaoInexigibilidade.hashCode());
		result = prime * result + ((indCienteRecomendacao == null) ? 0 : indCienteRecomendacao.hashCode());
		result = prime * result + ((requerimento == null) ? 0 : requerimento.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		DeclaracaoInexigibilidade other = (DeclaracaoInexigibilidade) obj;
		if(atividadeInexigivel == null) {
			if(other.atividadeInexigivel != null) return false;
		} else if(!atividadeInexigivel.equals(other.atividadeInexigivel)) return false;
		if(ideDeclaracaoInexigibilidade == null) {
			if(other.ideDeclaracaoInexigibilidade != null) return false;
		} else if(!ideDeclaracaoInexigibilidade.equals(other.ideDeclaracaoInexigibilidade)) return false;
		if(indCienteRecomendacao == null) {
			if(other.indCienteRecomendacao != null) return false;
		} else if(!indCienteRecomendacao.equals(other.indCienteRecomendacao)) return false;
		if(requerimento == null) {
			if(other.requerimento != null) return false;
		} else if(!requerimento.equals(other.requerimento)) return false;
		return true;
	}
}
