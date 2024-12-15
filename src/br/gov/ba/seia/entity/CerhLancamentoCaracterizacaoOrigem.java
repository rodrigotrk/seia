package br.gov.ba.seia.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import br.gov.ba.seia.interfaces.CerhFinalidadeUsoAguaInterface;

@Entity
@Table(name="cerh_lancamento_caracterizacao_origem")
public class CerhLancamentoCaracterizacaoOrigem extends AbstractEntityHist implements CerhFinalidadeUsoAguaInterface {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="ide_cerh_lancamento_caracterizacao_origem")
	@SequenceGenerator(name = "cerh_lancamento_caracterizacao_origem_seq", sequenceName = "cerh_lancamento_caracterizacao_origem_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_lancamento_caracterizacao_origem_seq")
	private Integer ideCerhLancamentoCaracterizacaoOrigem;

	@Historico
	@ManyToOne
	@JoinColumn(name="ide_tipo_finalidade_uso_agua")
	private TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua;

	@Historico
	@ManyToOne
	@JoinColumn(name="ide_cerh_lancamento_efluente_caracterizacao")
	private CerhLancamentoEfluenteCaracterizacao ideCerhLancamentoEfluenteCaracterizacao;
	
	@Historico
	@OneToOne(mappedBy="ideCerhLancamentoCaracterizacaoOrigem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private CerhLancamentoAbastecimentoPublico ideCerhCaptacaoAbastecimentoPublico;
	
	@Historico
	@OneToOne(mappedBy="ideCerhLancamentoCaracterizacaoOrigem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private CerhLancamentoOutrosUsos ideCerhLancamentoOutrosUsos;
	
	
	public CerhLancamentoCaracterizacaoOrigem() {
		
	}
	
	public CerhLancamentoCaracterizacaoOrigem(TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua,CerhLancamentoEfluenteCaracterizacao ideCerhLancamentoEfluenteCaracterizacao) {
		this.ideTipoFinalidadeUsoAgua = ideTipoFinalidadeUsoAgua;
		this.ideCerhLancamentoEfluenteCaracterizacao = ideCerhLancamentoEfluenteCaracterizacao;
	}

	public Integer getIdeCerhLancamentoCaracterizacaoOrigem() {
		return ideCerhLancamentoCaracterizacaoOrigem;
	}

	public void setIdeCerhLancamentoCaracterizacaoOrigem(Integer ideCerhLancamentoCaracterizacaoOrigem) {
		this.ideCerhLancamentoCaracterizacaoOrigem = ideCerhLancamentoCaracterizacaoOrigem;
	}

	@Override
	public TipoFinalidadeUsoAgua getIdeTipoFinalidadeUsoAgua() {
		return ideTipoFinalidadeUsoAgua;
	}

	@Override
	public void setIdeTipoFinalidadeUsoAgua(TipoFinalidadeUsoAgua ideTipoFinalidadeUsoAgua) {
		this.ideTipoFinalidadeUsoAgua = ideTipoFinalidadeUsoAgua;
	}

	public CerhLancamentoEfluenteCaracterizacao getIdeCerhLancamentoEfluenteCaracterizacao() {
		return ideCerhLancamentoEfluenteCaracterizacao;
	}

	public void setIdeCerhLancamentoEfluenteCaracterizacao(CerhLancamentoEfluenteCaracterizacao ideCerhLancamentoEfluenteCaracterizacao) {
		this.ideCerhLancamentoEfluenteCaracterizacao = ideCerhLancamentoEfluenteCaracterizacao;
	}
	
	public CerhLancamentoAbastecimentoPublico getIdeCerhCaptacaoAbastecimentoPublico() {
		return ideCerhCaptacaoAbastecimentoPublico;
	}

	public void setIdeCerhCaptacaoAbastecimentoPublico(CerhLancamentoAbastecimentoPublico ideCerhCaptacaoAbastecimentoPublico) {
		this.ideCerhCaptacaoAbastecimentoPublico = ideCerhCaptacaoAbastecimentoPublico;
	}

	public CerhLancamentoOutrosUsos getIdeCerhLancamentoOutrosUsos() {
		return ideCerhLancamentoOutrosUsos;
	}

	public void setIdeCerhLancamentoOutrosUsos(	CerhLancamentoOutrosUsos ideCerhLancamentoOutrosUsos) {
		this.ideCerhLancamentoOutrosUsos = ideCerhLancamentoOutrosUsos;
	}
}