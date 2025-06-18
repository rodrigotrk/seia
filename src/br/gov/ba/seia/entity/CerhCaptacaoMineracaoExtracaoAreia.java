package br.gov.ba.seia.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.gov.ba.seia.entity.generic.AbstractEntityHist;
import br.gov.ba.seia.interfaces.CerhDadosFinalidadeInterface;

@Entity
@Table(name="cerh_captacao_mineracao_extracao_areia")
public class CerhCaptacaoMineracaoExtracaoAreia extends AbstractEntityHist implements CerhDadosFinalidadeInterface  {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_captacao_mineracao_extracao_areia_seq")
	@SequenceGenerator(name = "cerh_captacao_mineracao_extracao_areia_seq", sequenceName = "cerh_captacao_mineracao_extracao_areia_seq", allocationSize = 1)
	@Column(name="ide_cerh_captacao_mineracao_extracao_areia")
	private Integer ideCerhCaptacaoMineracaoExtracaoAreia;

	@Column(name="val_producao_maxima_anual")
	private BigDecimal valProducaoMaximaAnual;

	@Column(name="val_producao_maxima_mensal")
	private BigDecimal valProducaoMaximaMensal;

	@Column(name="val_proporcao_agua_polpa")
	private BigDecimal valProporcaoAguaPolpa;

	@Column(name="val_teor_umidade")
	private Integer valTeorUmidade;

	@Column(name="val_volume_medio_agua")
	private BigDecimal valVolumeMedioAgua;

	@ManyToOne
	@JoinColumn(name="ide_cerh_captacao_caracterizacao_finalidade")
	private CerhCaptacaoCaracterizacaoFinalidade ideCerhCaptacaoCaracterizacaoFinalidade;

	public CerhCaptacaoMineracaoExtracaoAreia() {
	}

	public CerhCaptacaoMineracaoExtracaoAreia(CerhCaptacaoCaracterizacaoFinalidade cerhFinalidadeMineracaoExtracaoAreia) {
		this.ideCerhCaptacaoCaracterizacaoFinalidade = cerhFinalidadeMineracaoExtracaoAreia;
	}

	public Integer getIdeCerhCaptacaoMineracaoExtracaoAreia() {
		return ideCerhCaptacaoMineracaoExtracaoAreia;
	}

	public void setIdeCerhCaptacaoMineracaoExtracaoAreia(Integer ideCerhCaptacaoMineracaoExtracaoAreia) {
		this.ideCerhCaptacaoMineracaoExtracaoAreia = ideCerhCaptacaoMineracaoExtracaoAreia;
	}

	public BigDecimal getValProducaoMaximaAnual() {
		return valProducaoMaximaAnual;
	}

	public void setValProducaoMaximaAnual(BigDecimal valProducaoMaximaMensal) {
		this.valProducaoMaximaAnual = valProducaoMaximaMensal;
	}

	public BigDecimal getValProducaoMaximaMensal() {
		return valProducaoMaximaMensal;
	}

	public void setValProducaoMaximaMensal(BigDecimal valProducaoMediaMensal) {
		this.valProducaoMaximaMensal = valProducaoMediaMensal;
	}

	public BigDecimal getValProporcaoAguaPolpa() {
		return valProporcaoAguaPolpa;
	}

	public void setValProporcaoAguaPolpa(BigDecimal valProporcaoAguaPolpa) {
		this.valProporcaoAguaPolpa = valProporcaoAguaPolpa;
	}

	public Integer getValTeorUmidade() {
		return valTeorUmidade;
	}

	public void setValTeorUmidade(Integer valTeorUmidade) {
		this.valTeorUmidade = valTeorUmidade;
	}

	public BigDecimal getValVolumeMedioAgua() {
		return valVolumeMedioAgua;
	}

	public void setValVolumeMedioAgua(BigDecimal valVolumeMedioAgua) {
		this.valVolumeMedioAgua = valVolumeMedioAgua;
	}

	public CerhCaptacaoCaracterizacaoFinalidade getIdeCerhCaptacaoCaracterizacaoFinalidade() {
		return ideCerhCaptacaoCaracterizacaoFinalidade;
	}

	public void setIdeCerhCaptacaoCaracterizacaoFinalidade(CerhCaptacaoCaracterizacaoFinalidade ideCerhCaptacaoCaracterizacaoFinalidade) {
		this.ideCerhCaptacaoCaracterizacaoFinalidade = ideCerhCaptacaoCaracterizacaoFinalidade;
	}

	@Override
	public Integer getIde() {
		return ideCerhCaptacaoMineracaoExtracaoAreia;
	}
}