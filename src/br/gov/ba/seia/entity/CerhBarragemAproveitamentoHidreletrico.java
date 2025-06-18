package br.gov.ba.seia.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;
import br.gov.ba.seia.interfaces.CerhDadosFinalidadeInterface;

@Entity
@Table(name="cerh_barragem_aproveitamento_hidreletrico")
public class CerhBarragemAproveitamentoHidreletrico extends AbstractEntityHist implements CerhDadosFinalidadeInterface {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_barragem_aproveitamento_hidreletrico_seq")
	@SequenceGenerator(name = "cerh_barragem_aproveitamento_hidreletrico_seq", sequenceName = "cerh_barragem_aproveitamento_hidreletrico_seq", allocationSize = 1)
	@Column(name="ide_cerh_barragem_aproveitamento_hidreletrico")
	private Integer ideCerhBarragemAproveitamentoHidreletrico;

	@Historico(name="Desvio trecho")
	@Column(name="ind_desvio_trecho")
	private Boolean indDesvioTrecho;

	@Historico(name="Está em operação")
	@Column(name="ind_em_operacao")
	private Boolean indEmOperacao;

	@Historico(name="Faz aproveitamento hidrelétrico em PCH")
	@Column(name="ind_pch")
	private Boolean indPch;

	@Historico(name="Potência instalada intervenção")
	@Column(name="ind_potencia_instalada_intervencao")
	private Boolean indPotenciaInstaladaIntervencao;

	@Historico(name="Extensão")
	@Column(name="val_extensao")
	private BigDecimal valExtensao;

	@Historico(name="Potência instalada total")
	@Column(name="val_potencia_instalada_total")
	private BigDecimal valPotenciaInstaladaTotal;

	@Historico(name="Producão anual efetivamente verificada")
	@Column(name="val_producao_anual_efetivamente_verificada")
	private BigDecimal valProducaoAnualEfetivamenteVerificada;
	
	@Historico(name="Trecho vazão reduzida")
	@Column(name="val_trecho_vazao_reduzida")
	private BigDecimal valTrechoVazaoReduzida;

	@Historico(name="Data de inicio da operação")
	@Temporal(TemporalType.DATE)
	@Column(name="dt_inicio_operacao")
	private Date dtInicioOperacao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_cerh_localizacao_geografica_intervencao")
	private CerhLocalizacaoGeografica ideCerhLocalizacaoGeograficaIntervencao;

	@ManyToOne
	@JoinColumn(name="ide_cerh_barragem_caracterizacao_finalidade")
	private CerhBarragemCaracterizacaoFinalidade ideCerhBarragemCaracterizacaoFinalidade;

	@Historico
	@ManyToOne
	@JoinColumn(name="ide_tipo_aproveitamento_hidreletrico")
	private TipoAproveitamentoHidreletrico ideTipoAproveitamentoHidreletrico;

	
	public CerhBarragemAproveitamentoHidreletrico(){
		
	}
	
	public CerhBarragemAproveitamentoHidreletrico(CerhBarragemCaracterizacaoFinalidade cerhFinalidadeAproveitamentoHidreletrico) {
		this.ideCerhBarragemCaracterizacaoFinalidade = cerhFinalidadeAproveitamentoHidreletrico;
	}

	public Integer getIdeCerhBarragemAproveitamentoHidreletrico() {
		return ideCerhBarragemAproveitamentoHidreletrico;
	}

	public void setIdeCerhBarragemAproveitamentoHidreletrico(Integer ideCerhBarragemAproveitamentoHidreletrico) {
		this.ideCerhBarragemAproveitamentoHidreletrico = ideCerhBarragemAproveitamentoHidreletrico;
	}

	public Boolean getIndDesvioTrecho() {
		return indDesvioTrecho;
	}

	public void setIndDesvioTrecho(Boolean indDesvioTrecho) {
		this.indDesvioTrecho = indDesvioTrecho;
	}

	public Boolean getIndEmOperacao() {
		return indEmOperacao;
	}

	public void setIndEmOperacao(Boolean indEmOperacao) {
		this.indEmOperacao = indEmOperacao;
	}

	public Boolean getIndPch() {
		return indPch;
	}

	public void setIndPch(Boolean indPch) {
		this.indPch = indPch;
	}

	public Boolean getIndPotenciaInstaladaIntervencao() {
		return indPotenciaInstaladaIntervencao;
	}

	public void setIndPotenciaInstaladaIntervencao(Boolean indPotenciaInstaladaIntervencao) {
		this.indPotenciaInstaladaIntervencao = indPotenciaInstaladaIntervencao;
	}

	public BigDecimal getValExtensao() {
		return valExtensao;
	}

	public void setValExtensao(BigDecimal valExtensao) {
		this.valExtensao = valExtensao;
	}

	public BigDecimal getValPotenciaInstaladaTotal() {
		return valPotenciaInstaladaTotal;
	}

	public void setValPotenciaInstaladaTotal(BigDecimal valPotenciaInstaladaTotal) {
		this.valPotenciaInstaladaTotal = valPotenciaInstaladaTotal;
	}

	public BigDecimal getValProducaoAnualEfetivamenteVerificada() {
		return valProducaoAnualEfetivamenteVerificada;
	}

	public void setValProducaoAnualEfetivamenteVerificada(BigDecimal valProducaoAnualEfetivamenteVerificada) {
		this.valProducaoAnualEfetivamenteVerificada = valProducaoAnualEfetivamenteVerificada;
	}

	public CerhBarragemCaracterizacaoFinalidade getIdeCerhBarragemCaracterizacaoFinalidade() {
		return ideCerhBarragemCaracterizacaoFinalidade;
	}

	public void setIdeCerhBarragemCaracterizacaoFinalidade(CerhBarragemCaracterizacaoFinalidade ideCerhBarragemCaracterizacaoFinalidade) {
		this.ideCerhBarragemCaracterizacaoFinalidade = ideCerhBarragemCaracterizacaoFinalidade;
	}

	public TipoAproveitamentoHidreletrico getIdeTipoAproveitamentoHidreletrico() {
		return ideTipoAproveitamentoHidreletrico;
	}

	public void setIdeTipoAproveitamentoHidreletrico(TipoAproveitamentoHidreletrico ideTipoAproveitamentoHidreletrico) {
		this.ideTipoAproveitamentoHidreletrico = ideTipoAproveitamentoHidreletrico;
	}

	@Override
	public Integer getIde() {
		return this.ideCerhBarragemAproveitamentoHidreletrico;
	}

	public Date getDtInicioOperacao() {
		return dtInicioOperacao;
	}

	public void setDtInicioOperacao(Date dtInicioOperacao) {
		this.dtInicioOperacao = dtInicioOperacao;
	}

	public CerhLocalizacaoGeografica getIdeCerhLocalizacaoGeograficaIntervencao() {
		return ideCerhLocalizacaoGeograficaIntervencao;
	}

	public void setIdeCerhLocalizacaoGeograficaIntervencao(CerhLocalizacaoGeografica ideCerhLocalizacaoGeograficaIntervencao) {
		this.ideCerhLocalizacaoGeograficaIntervencao = ideCerhLocalizacaoGeograficaIntervencao;
	}

	public BigDecimal getValTrechoVazaoReduzida() {
		return valTrechoVazaoReduzida;
	}

	public void setValTrechoVazaoReduzida(BigDecimal valTrechoVazaoReduzida) {
		this.valTrechoVazaoReduzida = valTrechoVazaoReduzida;
	}
}