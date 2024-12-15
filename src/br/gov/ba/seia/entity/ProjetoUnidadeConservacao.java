package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name="projeto_unidade_conservacao")
@NamedQuery(name="ProjetoUnidadeConservacao.findAll", query="SELECT p FROM ProjetoUnidadeConservacao p")
public class ProjetoUnidadeConservacao implements Serializable, BaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="PROJETO_UNIDADE_CONSERVACAO_IDEPROJETOUNIDADECONSERVACAO_GENERATOR", sequenceName="PROJETO_UNIDADE_CONSERVACAO_IDE_PROJETO_UNIDADE_CONSERVACAO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PROJETO_UNIDADE_CONSERVACAO_IDEPROJETOUNIDADECONSERVACAO_GENERATOR")
	@Column(name="ide_projeto_unidade_conservacao", updatable=false, unique=true, nullable=false)
	private Integer ideProjetoUnidadeConservacao;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_unidade_conservacao", nullable=false)
	private UnidadeConservacao ideUnidadeConservacao;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ide_tcca_projeto", nullable=false)
	private TccaProjeto ideTccaProjeto;

	public ProjetoUnidadeConservacao() {
	}

	public ProjetoUnidadeConservacao(UnidadeConservacao ideUnidadeConservacao, TccaProjeto ideTccaProjeto) {
		super();
		this.ideUnidadeConservacao = ideUnidadeConservacao;
		this.ideTccaProjeto = ideTccaProjeto;
	}
	
	@Override
	public Long getId() {
		
		if(ideProjetoUnidadeConservacao == null) {
			return ideUnidadeConservacao.getId();
		} else {
			return ideProjetoUnidadeConservacao.longValue();
		}
	}

	public Integer getIdeProjetoUnidadeConservacao() {
		return this.ideProjetoUnidadeConservacao;
	}

	public void setIdeProjetoUnidadeConservacao(Integer ideProjetoUnidadeConservacao) {
		this.ideProjetoUnidadeConservacao = ideProjetoUnidadeConservacao;
	}

	public UnidadeConservacao getIdeUnidadeConservacao() {
		return this.ideUnidadeConservacao;
	}

	public void setIdeUnidadeConservacao(UnidadeConservacao ideUnidadeConservacao) {
		this.ideUnidadeConservacao = ideUnidadeConservacao;
	}

	public TccaProjeto getIdeTccaProjeto() {
		return this.ideTccaProjeto;
	}

	public void setIdeTccaProjeto(TccaProjeto ideTccaProjeto) {
		this.ideTccaProjeto = ideTccaProjeto;
	}
}