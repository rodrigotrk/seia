package br.gov.ba.seia.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;
import br.gov.ba.seia.interfaces.CerhCaracterizacaoCaptacaoLancamentoInterface;
import br.gov.ba.seia.interfaces.CerhCaracterizacaoFinalidadeInterface;
import br.gov.ba.seia.interfaces.CerhCaracterizacaoInterface;
import br.gov.ba.seia.interfaces.CerhFinalidadeUsoAguaInterface;
import br.gov.ba.seia.interfaces.CerhVazaoSazonalidadeInterface;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name="cerh_lancamento_efluente_caracterizacao")
public class CerhLancamentoEfluenteCaracterizacao extends AbstractEntityHist implements CerhCaracterizacaoInterface, CerhCaracterizacaoFinalidadeInterface, CerhCaracterizacaoCaptacaoLancamentoInterface {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "cerh_lancamento_caracterizacao_origem_seq", sequenceName = "cerh_lancamento_caracterizacao_origem_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_lancamento_caracterizacao_origem_seq")
	@Column(name="ide_cerh_lancamento_efluente_caracterizacao")
	private Integer ideCerhLancamentoEfluenteCaracterizacao;
	
	@Historico(name="Data de início de lançamento")
	@Temporal(TemporalType.DATE)
	@Column(name="dt_inicio_lancamento")
	private Date dtInicioLancamento;

	@Historico(name="Antes do lançamento, o efluente recebe tratamento?")
	@Column(name="ind_efluente_recebe_tratamento")
	private Boolean indEfluenteRecebeTratamento;

	@Historico(name="Nome do corpo hídrico")
	@Column(name="nom_corpo_hidrico")
	private String nomCorpoHidrico;
	
	@Historico(name="Observação")
	@Size(max=500,message = "O campo observação somente suporta 500 caracteres." )
	@Column(name="dsc_observacao")
	private String dscObservacao;

	@Historico(name="Valor do DBO da eficiência do tratamento")
	@Column(name="val_dbo_eficiencia_tratamento")
	private BigDecimal valDboEficienciaTratamento;

	@Historico(name="Valor do DBO do efluente bruto")
	@Column(name="val_dbo_efluente_bruto")
	private BigDecimal valDboEfluenteBruto;

	@Historico(name="Valo do DBO do efluente tratato")
	@Column(name="val_dbo_efluente_tratado")
	private BigDecimal valDboEfluenteTratado;

	@Historico(name="Valor coliformes da eficiencia da tratamento")
	@Column(name="val_coliformes_eficiencia_tratamento")
	private BigDecimal valColiformesEficienciaTratamento;

	@Historico(name="Valor coliformes do efluente tratado")
	@Column(name="val_coliformes_efluente_tratado")
	private BigDecimal valColiformesEfluenteTratado;

	@Historico(name="Valor coliformes da efluente bruto")
	@Column(name="val_coliformes_efluente_bruto")
	private BigDecimal valColiformesEfluenteBruto;

	@Historico(name="Valor do efluente maximo instantanêa")
	@Column(name="val_vazao_efluente_maxima_instantanea")
	private BigDecimal valVazaoEfluenteMaximaInstantanea;

	@Historico(name="Valor da vazão de diluição outorgada")
	@Column(name="val_vazao_diluicao_outorgada")
	private BigDecimal valVazaoDiluicaoOutorgada;

	@ManyToOne
	@JoinColumn(name="ide_cerh_localizacao_geografica")
	private CerhLocalizacaoGeografica ideCerhLocalizacaoGeografica;

	@Historico
	@ManyToOne
	@JoinColumn(name="ide_cerh_situacao_tipo_uso")
	private CerhSituacaoTipoUso ideCerhSituacaoTipoUso;

	@Historico
	@ManyToOne
	@JoinColumn(name="ide_cerh_tratamento_efluente")
	private CerhTratamentoEfluente ideCerhTratamentoEfluente;

	@Historico
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_tipo_corpo_hidrico")
	private TipoCorpoHidrico ideTipoCorpoHidrico;

	@Historico(subTable=true, subTableName="Sazonalidade")
	@OneToMany(mappedBy="ideCerhLancamentoEfluenteCaracterizacao", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Collection<CerhLancamentoEfluenteSazonalidade> cerhLancamentoEfluenteSazonalidadeCollection;
	
	@Historico
	@OneToMany(mappedBy="ideCerhLancamentoEfluenteCaracterizacao", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Collection<CerhLancamentoCaracterizacaoOrigem> cerhLancamentoCaracterizacaoOrigemCollection;
	
	public CerhLancamentoEfluenteCaracterizacao() {
	}

	public CerhLancamentoEfluenteCaracterizacao(CerhLocalizacaoGeografica cerhLocalizacaoGeografica) {
		ideCerhLocalizacaoGeografica = cerhLocalizacaoGeografica;
	}

	public Integer getIdeCerhLancamentoEfluenteCaracterizacao() {
		return ideCerhLancamentoEfluenteCaracterizacao;
	}

	public void setIdeCerhLancamentoEfluenteCaracterizacao(Integer ideCerhLancamentoEfluenteCaracterizacao) {
		this.ideCerhLancamentoEfluenteCaracterizacao = ideCerhLancamentoEfluenteCaracterizacao;
	}

	public Date getDtInicioLancamento() {
		return dtInicioLancamento;
	}

	public void setDtInicioLancamento(Date dtInicioLancamento) {
		this.dtInicioLancamento = dtInicioLancamento;
	}

	public Boolean getIndEfluenteRecebeTratamento() {
		return indEfluenteRecebeTratamento;
	}

	public void setIndEfluenteRecebeTratamento(Boolean indEfluenteRecebeTratamento) {
		this.indEfluenteRecebeTratamento = indEfluenteRecebeTratamento;
	}

	public String getNomCorpoHidrico() {
		return nomCorpoHidrico;
	}

	public void setNomCorpoHidrico(String nomCorpoHidrico) {
		this.nomCorpoHidrico = nomCorpoHidrico;
	}

	public String getDscObservacao() {
		return dscObservacao;
	}

	public void setDscObservacao(String dscObservacao) {
		this.dscObservacao = dscObservacao;
	}

	public BigDecimal getValColiformesEficienciaTratamento() {
		return valColiformesEficienciaTratamento;
	}

	public void setValColiformesEficienciaTratamento(BigDecimal valColiformesEficienciaTratamento) {
		this.valColiformesEficienciaTratamento = valColiformesEficienciaTratamento;
	}

	public BigDecimal getValColiformesEfluenteBruto() {
		return valColiformesEfluenteBruto;
	}

	public void setValColiformesEfluenteBruto(BigDecimal valColiformesEfluenteBruto) {
		this.valColiformesEfluenteBruto = valColiformesEfluenteBruto;
	}

	public BigDecimal getValColiformesEfluenteTratado() {
		return valColiformesEfluenteTratado;
	}

	public void setValColiformesEfluenteTratado(
			BigDecimal valColiformesEfluenteTratado) {
		this.valColiformesEfluenteTratado = valColiformesEfluenteTratado;
	}

	public BigDecimal getValDboEficienciaTratamento() {
		return valDboEficienciaTratamento;
	}

	public void setValDboEficienciaTratamento(BigDecimal valDboEficienciaTratamento) {
		this.valDboEficienciaTratamento = valDboEficienciaTratamento;
	}

	public BigDecimal getValDboEfluenteBruto() {
		return valDboEfluenteBruto;
	}

	public void setValDboEfluenteBruto(BigDecimal valDboEfluenteBruto) {
		this.valDboEfluenteBruto = valDboEfluenteBruto;
	}

	public BigDecimal getValDboEfluenteTratado() {
		return valDboEfluenteTratado;
	}

	public void setValDboEfluenteTratado(BigDecimal valDboEfluenteTratado) {
		this.valDboEfluenteTratado = valDboEfluenteTratado;
	}

	public BigDecimal getValVazaoEfluenteMaximaInstantanea() {
		return valVazaoEfluenteMaximaInstantanea;
	}

	public void setValVazaoEfluenteMaximaInstantanea(BigDecimal valVazaoEfluenteMaximaInstantanea) {
		this.valVazaoEfluenteMaximaInstantanea = valVazaoEfluenteMaximaInstantanea;
	}

	public BigDecimal getValVazaoDiluicaoOutorgada() {
		return valVazaoDiluicaoOutorgada;
	}

	public void setValVazaoDiluicaoOutorgada(BigDecimal valVazaoDiluicaoOutorgada) {
		this.valVazaoDiluicaoOutorgada = valVazaoDiluicaoOutorgada;
	}

	public CerhLocalizacaoGeografica getIdeCerhLocalizacaoGeografica() {
		return ideCerhLocalizacaoGeografica;
	}

	public void setIdeCerhLocalizacaoGeografica(CerhLocalizacaoGeografica ideCerhLocalizacaoGeografica) {
		this.ideCerhLocalizacaoGeografica = ideCerhLocalizacaoGeografica;
	}

	public CerhSituacaoTipoUso getIdeCerhSituacaoTipoUso() {
		return ideCerhSituacaoTipoUso;
	}

	public void setIdeCerhSituacaoTipoUso(CerhSituacaoTipoUso ideCerhSituacaoTipoUso) {
		this.ideCerhSituacaoTipoUso = ideCerhSituacaoTipoUso;
	}

	public CerhTratamentoEfluente getIdeCerhTratamentoEfluente() {
		return ideCerhTratamentoEfluente;
	}

	public void setIdeCerhTratamentoEfluente(CerhTratamentoEfluente ideCerhTratamentoEfluente) {
		this.ideCerhTratamentoEfluente = ideCerhTratamentoEfluente;
	}

	public Collection<CerhLancamentoEfluenteSazonalidade> getCerhLancamentoEfluenteSazonalidadeCollection() {
		return cerhLancamentoEfluenteSazonalidadeCollection;
	}

	public void setCerhLancamentoEfluenteSazonalidadeCollection(Collection<CerhLancamentoEfluenteSazonalidade> cerhLancamentoEfluenteSazonalidadeCollection) {
		this.cerhLancamentoEfluenteSazonalidadeCollection = cerhLancamentoEfluenteSazonalidadeCollection;
	}

	public Collection<CerhLancamentoCaracterizacaoOrigem> getCerhLancamentoCaracterizacaoOrigemCollection() {
		return cerhLancamentoCaracterizacaoOrigemCollection;
	}

	public void setCerhLancamentoCaracterizacaoOrigemCollection(Collection<CerhLancamentoCaracterizacaoOrigem> cerhLancamentoCaracterizacaoOrigemCollection) {
		this.cerhLancamentoCaracterizacaoOrigemCollection = cerhLancamentoCaracterizacaoOrigemCollection;
	}

	public TipoCorpoHidrico getIdeTipoCorpoHidrico() {
		return ideTipoCorpoHidrico;
	}

	public void setIdeTipoCorpoHidrico(TipoCorpoHidrico ideTipoCorpoHidrico) {
		this.ideTipoCorpoHidrico = ideTipoCorpoHidrico;
	}

	@Override
	public void setIde(Integer ideCerhLancamentoEfluenteCaracterizacao) {
		this.ideCerhLancamentoEfluenteCaracterizacao=ideCerhLancamentoEfluenteCaracterizacao;
	}
	
	@Override
	public Date getData() {
		return this.dtInicioLancamento;
	}

	@Override
	public void setData(Date data) {
		this.dtInicioLancamento = data;		
	}

	@Override
	public BigDecimal getValVazaoMaximaInstantanea() {
		return this.valVazaoEfluenteMaximaInstantanea;
	}

	@Override
	public void setValVazaoMaximaInstantanea(BigDecimal vazao) {
		this.valVazaoEfluenteMaximaInstantanea = vazao;		
	}
	
	@Override
	public Collection<CerhVazaoSazonalidadeInterface> getVazaoSazonalidadeCollection() {
		Collection<CerhVazaoSazonalidadeInterface> coll = null;
		if(!Util.isNullOuVazio(this.cerhLancamentoEfluenteSazonalidadeCollection)){
			coll = new ArrayList<CerhVazaoSazonalidadeInterface>();
			for (CerhLancamentoEfluenteSazonalidade vazaoSazonalidade : this.cerhLancamentoEfluenteSazonalidadeCollection) {
				CerhVazaoSazonalidadeInterface inter = vazaoSazonalidade;
				coll.add(inter);
			}
		}
		return coll;
	}
	
	@Override
	public void setVazaoSazonalidadeCollection(Collection<CerhVazaoSazonalidadeInterface> coll) {
		if(Util.isNullOuVazio(this.cerhLancamentoEfluenteSazonalidadeCollection)){
			this.cerhLancamentoEfluenteSazonalidadeCollection = new ArrayList<CerhLancamentoEfluenteSazonalidade>();
		}
		for (CerhVazaoSazonalidadeInterface cerhVazaoSazonalidadeInterface : coll) {
			this.cerhLancamentoEfluenteSazonalidadeCollection.add((CerhLancamentoEfluenteSazonalidade) cerhVazaoSazonalidadeInterface);
		}
	}

	@Override
	public Collection<CerhFinalidadeUsoAguaInterface> getFinalidadeCollection() {
		Collection<CerhFinalidadeUsoAguaInterface> coll = null;
		if(!Util.isNullOuVazio(this.cerhLancamentoCaracterizacaoOrigemCollection)){
			coll = new ArrayList<CerhFinalidadeUsoAguaInterface>();
			for (CerhLancamentoCaracterizacaoOrigem finalidade : this.cerhLancamentoCaracterizacaoOrigemCollection) {
				CerhFinalidadeUsoAguaInterface inter = finalidade;
				coll.add(inter);
			}
		}
		return coll;
	}

	
	
}