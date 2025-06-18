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
import javax.validation.constraints.Size;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;
import br.gov.ba.seia.interfaces.CerhCaracterizacaoInterface;

@Entity
@Table(name="cerh_intervencao_caracterizacao")
public class CerhIntervencaoCaracterizacao extends AbstractEntityHist implements  Cloneable, CerhCaracterizacaoInterface {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_intervencao_caracterizacao_seq")
	@SequenceGenerator(name = "cerh_intervencao_caracterizacao_seq", sequenceName = "cerh_intervencao_caracterizacao_seq", allocationSize = 1)
	@Column(name="ide_cerh_intervencao_caracterizacao")
	private Integer ideCerhIntervencaoCaracterizacao;

	@Historico(name="Está em operação")
	@Column(name="ind_operacao")
	private Boolean indOperacao;

	@Historico(name="Faz aproveitamento hidrelétrico em PCH?")
	@Column(name="ind_pch")
	private Boolean indPch;
	
	@Historico(name="Observação")
	@Size(max=500,message = "O campo observação somente suporta 500 caracteres." )
	@Column(name="dsc_observacao")
	private String dscObservacao;

	@Historico(name="Potência instalada total (MW)")
	@Column(name="ind_potencia_instalada_barragem")
	private Boolean indPotenciaInstaladaBarragem;

	@Historico(name="Nome do corpo hídrico")
	@Column(name="nom_corpo_hidrico")
	private String nomCorpoHidrico;

	@Historico(name="Potência instalada total")
	@Column(name="val_potencia_instalada_total")
	private BigDecimal valPotenciaInstaladaTotal;

	@Historico(name="Valor produção efetivamente verificada")
	@Column(name="val_producao_anual_efetivamente_verificada")
	private BigDecimal valProducaoAnualEfetivamenteVerificada;

	@Historico
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_cerh_intervencao_servico")
	private CerhIntervencaoServico ideCerhIntervencaoServico;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_cerh_localizacao_geografica")
	private CerhLocalizacaoGeografica ideCerhLocalizacaoGeografica;

	@Historico
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_cerh_obras_hidraulicas")
	private CerhObrasHidraulicas ideCerhObrasHidraulicas;

	@Historico
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_cerh_situacao_tipo_uso")
	private CerhSituacaoTipoUso ideCerhSituacaoTipoUso;

	@Historico
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_tipo_intervencao")
	private TipoIntervencao ideTipoIntervencao;
	
	@Historico
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_tipo_corpo_hidrico")
	private TipoCorpoHidrico ideTipoCorpoHidrico;
	
	@Historico(name="Data de inicio da operação")
	@Temporal(TemporalType.DATE)
	@Column(name="dt_inicio_operacao")
	private Date dtInicioOperacao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_cerh_localizacao_geografica_barragem")
	private CerhLocalizacaoGeografica ideCerhLocalizacaoGeograficaBarragem;
	
	public CerhIntervencaoCaracterizacao() {
	}

	public CerhIntervencaoCaracterizacao(CerhLocalizacaoGeografica cerhLocalizacaoGeografica) {
		this.ideCerhLocalizacaoGeografica = cerhLocalizacaoGeografica;
	}

	public Integer getIdeCerhIntervencaoCaracterizacao() {
		return ideCerhIntervencaoCaracterizacao;
	}

	public void setIdeCerhIntervencaoCaracterizacao(Integer ideCerhIntervencaoCaracterizacao) {
		this.ideCerhIntervencaoCaracterizacao = ideCerhIntervencaoCaracterizacao;
	}

	public Boolean getIndOperacao() {
		return indOperacao;
	}

	public void setIndOperacao(Boolean indOperacao) {
		this.indOperacao = indOperacao;
	}

	public Boolean getIndPch() {
		return indPch;
	}

	public void setIndPch(Boolean indPch) {
		this.indPch = indPch;
	}

	public String getDscObservacao() {
		return dscObservacao;
	}

	public void setDscObservacao(String dscObservacao) {
		this.dscObservacao = dscObservacao;
	}

	public Boolean getIndPotenciaInstaladaBarragem() {
		return indPotenciaInstaladaBarragem;
	}

	public void setIndPotenciaInstaladaBarragem(Boolean indPotenciaInstaladaBarragem) {
		this.indPotenciaInstaladaBarragem = indPotenciaInstaladaBarragem;
	}

	public String getNomCorpoHidrico() {
		return nomCorpoHidrico;
	}

	public void setNomCorpoHidrico(String nomCorpoHidrico) {
		this.nomCorpoHidrico = nomCorpoHidrico;
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

	public CerhIntervencaoServico getIdeCerhIntervencaoServico() {
		return ideCerhIntervencaoServico;
	}

	public void setIdeCerhIntervencaoServico(CerhIntervencaoServico ideCerhIntervencaoServico) {
		this.ideCerhIntervencaoServico = ideCerhIntervencaoServico;
	}

	public CerhLocalizacaoGeografica getIdeCerhLocalizacaoGeografica() {
		return ideCerhLocalizacaoGeografica;
	}

	public void setIdeCerhLocalizacaoGeografica(CerhLocalizacaoGeografica ideCerhLocalizacaoGeografica) {
		this.ideCerhLocalizacaoGeografica = ideCerhLocalizacaoGeografica;
	}

	public CerhObrasHidraulicas getIdeCerhObrasHidraulicas() {
		return ideCerhObrasHidraulicas;
	}

	public void setIdeCerhObrasHidraulicas(CerhObrasHidraulicas ideCerhObrasHidraulicas) {
		this.ideCerhObrasHidraulicas = ideCerhObrasHidraulicas;
	}

	public CerhSituacaoTipoUso getIdeCerhSituacaoTipoUso() {
		return ideCerhSituacaoTipoUso;
	}

	public void setIdeCerhSituacaoTipoUso(CerhSituacaoTipoUso ideCerhSituacaoTipoUso) {
		this.ideCerhSituacaoTipoUso = ideCerhSituacaoTipoUso;
	}

	public TipoIntervencao getIdeTipoIntervencao() {
		return ideTipoIntervencao;
	}

	public void setIdeTipoIntervencao(TipoIntervencao ideTipoIntervencao) {
		this.ideTipoIntervencao = ideTipoIntervencao;
	}

	@Override
	public Integer getId() {
		return this.ideCerhIntervencaoCaracterizacao;
	}
	
	@Override
	public void setIde(Integer ideCerhIntervencaoCaracterizacao) {
		this.ideCerhIntervencaoCaracterizacao=ideCerhIntervencaoCaracterizacao;
	}

	public TipoCorpoHidrico getIdeTipoCorpoHidrico() {
		return ideTipoCorpoHidrico;
	}

	public void setIdeTipoCorpoHidrico(TipoCorpoHidrico ideTipoCorpoHidrico) {
		this.ideTipoCorpoHidrico = ideTipoCorpoHidrico;
	}

	public Date getDtInicioOperacao() {
		return dtInicioOperacao;
	}

	public void setDtInicioOperacao(Date dtInicioOperacao) {
		this.dtInicioOperacao = dtInicioOperacao;
	}

	public CerhLocalizacaoGeografica getIdeCerhLocalizacaoGeograficaBarragem() {
		return ideCerhLocalizacaoGeograficaBarragem;
	}

	public void setIdeCerhLocalizacaoGeograficaBarragem(CerhLocalizacaoGeografica ideCerhLocalizacaoGeograficaBarragem) {
		this.ideCerhLocalizacaoGeograficaBarragem = ideCerhLocalizacaoGeograficaBarragem;
	}

}