package br.gov.ba.seia.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;

@Entity
@Table(name="cerh_lancamento_abastecimento_publico")
public class CerhLancamentoAbastecimentoPublico extends AbstractEntityHist {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ide_cerh_lancamento_abastecimento_publico")
	@SequenceGenerator(name = "cerh_lancamento_abastecimento_publico_seq", sequenceName = "cerh_lancamento_abastecimento_publico_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_lancamento_abastecimento_publico_seq")
	private Integer ideCerhLancamentoAbastecimentoPublico;

	@OneToOne
	@JoinColumn(name = "ide_cerh_lancamento_caracterizacao_origem", referencedColumnName = "ide_cerh_lancamento_caracterizacao_origem")
	private CerhLancamentoCaracterizacaoOrigem ideCerhLancamentoCaracterizacaoOrigem;

	@Historico
	@ManyToOne
	@JoinColumn(name="ide_cerh_tipo_prestador_servico")
	private CerhTipoPrestadorServico ideCerhTipoPrestadorServico;
	
	public CerhLancamentoAbastecimentoPublico() {
	}

	public CerhLancamentoAbastecimentoPublico(CerhLancamentoCaracterizacaoOrigem ideCerhLancamentoCaracterizacaoOrigem) {
		this.ideCerhLancamentoCaracterizacaoOrigem = ideCerhLancamentoCaracterizacaoOrigem;
	}

	public CerhLancamentoAbastecimentoPublico(CerhTipoPrestadorServico cerhTipoPrestadorServico,CerhLancamentoCaracterizacaoOrigem cerhLancamentoCaracterizacaoOrigem) {
		ideCerhTipoPrestadorServico = cerhTipoPrestadorServico;
		ideCerhLancamentoCaracterizacaoOrigem = cerhLancamentoCaracterizacaoOrigem;
	}

	public Integer getIdeCerhLancamentoAbastecimentoPublico() {
		return ideCerhLancamentoAbastecimentoPublico;
	}

	public void setIdeCerhLancamentoAbastecimentoPublico(Integer ideCerhLancamentoAbastecimentoPublico) {
		this.ideCerhLancamentoAbastecimentoPublico = ideCerhLancamentoAbastecimentoPublico;
	}

	public CerhLancamentoCaracterizacaoOrigem getIdeCerhLancamentoCaracterizacaoOrigem() {
		return ideCerhLancamentoCaracterizacaoOrigem;
	}

	public void setIdeCerhLancamentoCaracterizacaoOrigem(
			CerhLancamentoCaracterizacaoOrigem ideCerhLancamentoCaracterizacaoOrigem) {
		this.ideCerhLancamentoCaracterizacaoOrigem = ideCerhLancamentoCaracterizacaoOrigem;
	}

	public CerhTipoPrestadorServico getIdeCerhTipoPrestadorServico() {
		return ideCerhTipoPrestadorServico;
	}

	public void setIdeCerhTipoPrestadorServico(CerhTipoPrestadorServico ideCerhTipoPrestadorServico) {
		this.ideCerhTipoPrestadorServico = ideCerhTipoPrestadorServico;
	}

}