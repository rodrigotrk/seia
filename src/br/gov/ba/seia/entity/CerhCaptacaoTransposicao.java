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

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;
import br.gov.ba.seia.interfaces.CerhDadosFinalidadeInterface;


@Entity
@Table(name="cerh_captacao_transposicao")
public class CerhCaptacaoTransposicao extends AbstractEntityHist implements CerhDadosFinalidadeInterface  {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_captacao_transposicao_seq")
	@SequenceGenerator(name = "cerh_captacao_transposicao_seq", sequenceName = "cerh_captacao_transposicao_seq", allocationSize = 1)
	@Column(name="ide_cerh_captacao_transposicao")
	private Integer ideCerhCaptacaoTransposicao;

	@ManyToOne
	@JoinColumn(name="ide_cerh_captacao_caracterizacao_finalidade")
	private CerhCaptacaoCaracterizacaoFinalidade ideCerhCaptacaoCaracterizacaoFinalidade;

	@Historico
	@ManyToOne
	@JoinColumn(name="ide_cerh_finalidade_transposicao")
	private CerhFinalidadeTransposicao ideCerhFinalidadeTransposicao;

	public CerhCaptacaoTransposicao() {
	}

	public CerhCaptacaoTransposicao(CerhFinalidadeTransposicao transposicao, CerhCaptacaoCaracterizacaoFinalidade cerhFinalidade) {
		this.ideCerhCaptacaoCaracterizacaoFinalidade = cerhFinalidade;
		this.ideCerhFinalidadeTransposicao = transposicao;
	}

	public Integer getIdeCerhCaptacaoTransposicao() {
		return ideCerhCaptacaoTransposicao;
	}

	public void setIdeCerhCaptacaoTransposicao(Integer ideCerhCaptacaoTransposicao) {
		this.ideCerhCaptacaoTransposicao = ideCerhCaptacaoTransposicao;
	}

	public CerhCaptacaoCaracterizacaoFinalidade getIdeCerhCaptacaoCaracterizacaoFinalidade() {
		return ideCerhCaptacaoCaracterizacaoFinalidade;
	}

	public void setIdeCerhCaptacaoCaracterizacaoFinalidade(CerhCaptacaoCaracterizacaoFinalidade ideCerhCaptacaoCaracterizacaoFinalidade) {
		this.ideCerhCaptacaoCaracterizacaoFinalidade = ideCerhCaptacaoCaracterizacaoFinalidade;
	}

	public CerhFinalidadeTransposicao getIdeCerhFinalidadeTransposicao() {
		return ideCerhFinalidadeTransposicao;
	}

	public void setIdeCerhFinalidadeTransposicao(CerhFinalidadeTransposicao ideCerhFinalidadeTransposicao) {
		this.ideCerhFinalidadeTransposicao = ideCerhFinalidadeTransposicao;
	}

	@Override
	public Integer getIde() {
		return this.ideCerhCaptacaoTransposicao;
	}
}