package br.gov.ba.seia.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "RECOMENDACAO_ATIVIDADE_INEXIGIVEL")
@XmlRootElement
public class RecomendacaoAtividadeInexigivel extends AbstractEntity {
	
	private static final long serialVersionUID = -7421849202985323273L;
	
	@Id
	@SequenceGenerator(name = "RECOMENDACAO_ATIVIDADE_INEXIGIVEL_SEQ", sequenceName = "RECOMENDACAO_ATIVIDADE_INEXIGIVEL_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RECOMENDACAO_ATIVIDADE_INEXIGIVEL_SEQ")
	@Column(name = "IDE_RECOMENDACAO_ATIVIDADE_INEXIGIVEL", nullable = false)
	private Integer ideRecomendacaoAtividadeInexigivel;
	
	@JoinColumn(name = "ide_atividade_inexigivel", referencedColumnName = "ide_atividade_inexigivel")
	@ManyToOne(optional = false)
	private AtividadeInexigivel atividadeInexigivel;
	
	@JoinColumn(name = "IDE_RECOMENDACAO_INEXIGIBILIDADE", referencedColumnName = "IDE_RECOMENDACAO_INEXIGIBILIDADE")
	@ManyToOne(optional = false)
	private RecomendacaoInexigibilidade recomendacaoInexigibilidade;
	
	@Column(name = "ind_ativo", nullable = false)
	private Boolean indAtivo;
	
	public Integer getIdeRecomendacaoAtividadeInexigivel() {
		return ideRecomendacaoAtividadeInexigivel;
	}
	
	public void setIdeRecomendacaoAtividadeInexigivel(Integer ideRecomendacaoAtividadeInexigivel) {
		this.ideRecomendacaoAtividadeInexigivel = ideRecomendacaoAtividadeInexigivel;
	}
	
	public AtividadeInexigivel getAtividadeInexigivel() {
		return atividadeInexigivel;
	}
	
	public void setAtividadeInexigivel(AtividadeInexigivel atividadeInexigivel) {
		this.atividadeInexigivel = atividadeInexigivel;
	}
	
	public RecomendacaoInexigibilidade getRecomendacaoInexigibilidade() {
		return recomendacaoInexigibilidade;
	}
	
	public void setRecomendacaoInexigibilidade(RecomendacaoInexigibilidade recomendacaoInexigibilidade) {
		this.recomendacaoInexigibilidade = recomendacaoInexigibilidade;
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
		result = prime * result + ((atividadeInexigivel == null) ? 0 : atividadeInexigivel.hashCode());
		result = prime * result + ((ideRecomendacaoAtividadeInexigivel == null) ? 0 : ideRecomendacaoAtividadeInexigivel.hashCode());
		result = prime * result + ((indAtivo == null) ? 0 : indAtivo.hashCode());
		result = prime * result + ((recomendacaoInexigibilidade == null) ? 0 : recomendacaoInexigibilidade.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		RecomendacaoAtividadeInexigivel other = (RecomendacaoAtividadeInexigivel) obj;
		if(atividadeInexigivel == null) {
			if(other.atividadeInexigivel != null) return false;
		} else if(!atividadeInexigivel.equals(other.atividadeInexigivel)) return false;
		if(ideRecomendacaoAtividadeInexigivel == null) {
			if(other.ideRecomendacaoAtividadeInexigivel != null) return false;
		} else if(!ideRecomendacaoAtividadeInexigivel.equals(other.ideRecomendacaoAtividadeInexigivel)) return false;
		if(indAtivo == null) {
			if(other.indAtivo != null) return false;
		} else if(!indAtivo.equals(other.indAtivo)) return false;
		if(recomendacaoInexigibilidade == null) {
			if(other.recomendacaoInexigibilidade != null) return false;
		} else if(!recomendacaoInexigibilidade.equals(other.recomendacaoInexigibilidade)) return false;
		return true;
	}
}
