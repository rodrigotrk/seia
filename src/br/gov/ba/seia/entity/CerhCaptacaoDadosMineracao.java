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

import br.gov.ba.seia.entity.generic.AbstractEntityHist;
import br.gov.ba.seia.interfaces.CerhDadosFinalidadeInterface;

@Entity
@Table(name="cerh_captacao_dados_mineracao")
public class CerhCaptacaoDadosMineracao extends AbstractEntityHist implements Serializable, CerhDadosFinalidadeInterface  {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_captacao_dados_mineracao_seq")
	@SequenceGenerator(name = "cerh_captacao_dados_mineracao_seq", sequenceName = "cerh_captacao_dados_mineracao_seq", allocationSize = 1)
	@Column(name="ide_cerh_captacao_dados_mineracao")
	private Integer ideCerhCaptacaoDadosMineracao;

	@Column(name="nom_produto")
	private String nomProduto;

	@Column(name="val_consumo_agua")
	private BigDecimal valConsumoAgua;

	@Column(name="val_producao_maxima_mensal")
	private BigDecimal valProducaoMaximaMensal;

	@Column(name="val_producao_maxima_anual")
	private BigDecimal valProducaoMaximaAnual;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_cerh_captacao_caracterizacao_finalidade")
	private CerhCaptacaoCaracterizacaoFinalidade ideCerhCaptacaoCaracterizacaoFinalidade;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_unidade_medida")
	private UnidadeMedida ideUnidadeMedida;

	public CerhCaptacaoDadosMineracao() {
	}

	public CerhCaptacaoDadosMineracao(CerhCaptacaoCaracterizacaoFinalidade cerhFinalidadeMineracao) {
		this.ideCerhCaptacaoCaracterizacaoFinalidade = cerhFinalidadeMineracao;
	}

	public Integer getIdeCerhCaptacaoDadosMineracao() {
		return ideCerhCaptacaoDadosMineracao;
	}

	public void setIdeCerhCaptacaoDadosMineracao(
			Integer ideCerhCaptacaoDadosMineracao) {
		this.ideCerhCaptacaoDadosMineracao = ideCerhCaptacaoDadosMineracao;
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

	public BigDecimal getValProducaoMaximaMensal() {
		return valProducaoMaximaMensal;
	}

	public void setValProducaoMaximaMensal(BigDecimal valConsumoMensal) {
		this.valProducaoMaximaMensal = valConsumoMensal;
	}

	public BigDecimal getValProducaoMaximaAnual() {
		return valProducaoMaximaAnual;
	}

	public void setValProducaoMaximaAnual(BigDecimal valorConsumoAnual) {
		this.valProducaoMaximaAnual = valorConsumoAnual;
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
		return this.ideCerhCaptacaoDadosMineracao;
	}
}