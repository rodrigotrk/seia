package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;

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

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;
import br.gov.ba.seia.interfaces.CerhDadosFinalidadeInterface;

@Entity
@Table(name="cerh_captacao_abastecimento_industrial")
public class CerhCaptacaoAbastecimentoIndustrial extends AbstractEntityHist implements Serializable, CerhDadosFinalidadeInterface  {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_captacao_abastecimento_industrial_seq")
	@SequenceGenerator(name = "cerh_captacao_abastecimento_industrial_seq", sequenceName = "cerh_captacao_abastecimento_industrial_seq", allocationSize = 1)
	@Column(name="ide_cerh_captacao_abastecimento_industrial")
	private Integer ideCerhCaptacaoAbastecimentoIndustrial;

	@Historico(name="Produto")
	@Column(name="nom_produto")
	private String nomProduto;

	@Historico(name="Consumo de água por unidade de produto industrializado (m³/dia)")
	@Column(name="val_consumo_agua")
	private BigDecimal valConsumoAgua;

	@Historico(name="Produção anual")
	@Column(name="val_producao_anual")
	private BigDecimal valProducaoAnual;

	@Historico(name="Produção diaria")
	@Column(name="val_producao_diaria")
	private BigDecimal valProducaoDiaria;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_cerh_captacao_caracterizacao_finalidade")
	private CerhCaptacaoCaracterizacaoFinalidade ideCerhCaptacaoCaracterizacaoFinalidade;

	@Historico
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_unidade_medida")
	private UnidadeMedida ideUnidadeMedida;

	public CerhCaptacaoAbastecimentoIndustrial() {
	}

	public CerhCaptacaoAbastecimentoIndustrial(CerhCaptacaoCaracterizacaoFinalidade cerhFinalidadeAbastecimentoIndustrial) {
		this.ideCerhCaptacaoCaracterizacaoFinalidade = cerhFinalidadeAbastecimentoIndustrial;
	}

	public Integer getIdeCerhCaptacaoAbastecimentoIndustrial() {
		return ideCerhCaptacaoAbastecimentoIndustrial;
	}

	public void setIdeCerhCaptacaoAbastecimentoIndustrial(
			Integer ideCerhCaptacaoAbastecimentoIndustrial) {
		this.ideCerhCaptacaoAbastecimentoIndustrial = ideCerhCaptacaoAbastecimentoIndustrial;
	}

	public String getNomProduto() {
		return nomProduto;
	}

	public void setNomProduto(String nomProduto) {
		this.nomProduto = nomProduto;
	}

	public BigDecimal getValConsumoAgua() {
		return valConsumoAgua;
	}

	public void setValConsumoAgua(BigDecimal valConsumoAgua) {
		this.valConsumoAgua = valConsumoAgua;
	}

	public BigDecimal getValProducaoAnual() {
		return valProducaoAnual;
	}

	public void setValProducaoAnual(BigDecimal valProducaoAnual) {
		this.valProducaoAnual = valProducaoAnual;
	}

	public BigDecimal getValProducaoDiaria() {
		return valProducaoDiaria;
	}

	public void setValProducaoDiaria(BigDecimal valProducaoDiaria) {
		this.valProducaoDiaria = valProducaoDiaria;
	}

	public CerhCaptacaoCaracterizacaoFinalidade getIdeCerhCaptacaoCaracterizacaoFinalidade() {
		return ideCerhCaptacaoCaracterizacaoFinalidade;
	}

	public void setIdeCerhCaptacaoCaracterizacaoFinalidade(CerhCaptacaoCaracterizacaoFinalidade ideCerhCaptacaoCaracterizacaoFinalidade) {
		this.ideCerhCaptacaoCaracterizacaoFinalidade = ideCerhCaptacaoCaracterizacaoFinalidade;
	}

	public UnidadeMedida getIdeUnidadeMedida() {
		return ideUnidadeMedida;
	}

	public void setIdeUnidadeMedida(UnidadeMedida ideUnidadeMedida) {
		this.ideUnidadeMedida = ideUnidadeMedida;
	}

	@Override
	public Integer getIde() {
		return this.ideCerhCaptacaoAbastecimentoIndustrial;
	}
}