package br.gov.ba.seia.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.SituacaoAtualFlorestaProducaoEnum;

/**
 * @author Alexandre Queiroz
 * @since 20/12/2016
 *
 * */

@Entity
@Table(name="situacao_atual_floresta_producao")
public class SituacaoAtualFlorestaProducao extends AbstractEntity{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_situacao_atual_floresta_producao")
	private Integer ideSituacaoAtualFlorestaProducao;

	@Column(name="des_situacao_floresta_producao")
	private String desSituacaoFlorestaProducao;

	@Temporal(TemporalType.DATE)
	@Column(name="dtc_criacao")
	private Date dtcCriacao;

	@Column(name="dtc_exclusao")
	private String dtcExclusao;

	@Column(name="ind_excluido")
	private Boolean indExcluido;

	public SituacaoAtualFlorestaProducao() {
	}
	
	public SituacaoAtualFlorestaProducao(Integer ideSituacaoAtualFlorestaProducao) {
		this.ideSituacaoAtualFlorestaProducao = ideSituacaoAtualFlorestaProducao;
	}

	public SituacaoAtualFlorestaProducao(SituacaoAtualFlorestaProducaoEnum ideSituacaoAtualFlorestaProducao) {
		this.ideSituacaoAtualFlorestaProducao = ideSituacaoAtualFlorestaProducao.getId();
	}

	public Integer getIdeSituacaoAtualFlorestaProducao() {
		return this.ideSituacaoAtualFlorestaProducao;
	}

	public void setIdeSituacaoAtualFlorestaProducao(Integer ideSituacaoAtualFlorestaProducao) {
		this.ideSituacaoAtualFlorestaProducao = ideSituacaoAtualFlorestaProducao;
	}

	public String getDesSituacaoFlorestaProducao() {
		return this.desSituacaoFlorestaProducao;
	}

	public void setDesSituacaoFlorestaProducao(String desSituacaoFlorestaProducao) {
		this.desSituacaoFlorestaProducao = desSituacaoFlorestaProducao;
	}

	public Date getDtcCriacao() {
		return this.dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public String getDtcExclusao() {
		return this.dtcExclusao;
	}

	public void setDtcExclusao(String dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	public Boolean getIndExcluido() {
		return this.indExcluido;
	}

	public void setIndExcluido(Boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

}