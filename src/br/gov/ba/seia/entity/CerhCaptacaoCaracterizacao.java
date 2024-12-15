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
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.dto.BarragemDTO;
import br.gov.ba.seia.entity.generic.AbstractEntityHist;
import br.gov.ba.seia.interfaces.CerhCaracterizacaoCaptacaoLancamentoInterface;
import br.gov.ba.seia.interfaces.CerhCaracterizacaoFinalidadeInterface;
import br.gov.ba.seia.interfaces.CerhCaracterizacaoInterface;
import br.gov.ba.seia.interfaces.CerhFinalidadeUsoAguaInterface;
import br.gov.ba.seia.interfaces.CerhVazaoSazonalidadeInterface;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name="cerh_captacao_caracterizacao")
public class CerhCaptacaoCaracterizacao extends AbstractEntityHist implements Cloneable, CerhCaracterizacaoInterface, CerhCaracterizacaoFinalidadeInterface, CerhCaracterizacaoCaptacaoLancamentoInterface {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cerh_captacao_caracterizacao_seq")
	@SequenceGenerator(name = "cerh_captacao_caracterizacao_seq", sequenceName = "cerh_captacao_caracterizacao_seq", allocationSize = 1)
	@Column(name="ide_cerh_captacao_caracterizacao")
	private Integer ideCerhCaptacaoCaracterizacao;

	@Historico(name="Data de inicio da captação")
	@Temporal(TemporalType.DATE)
	@Column(name="dt_inicio_captacao")
	private Date dtInicioCaptacao;

	@Historico(name= "Captação em reservatório de barragem?")
	@Column(name="ind_captacao_reservatorio")
	private Boolean indCaptacaoReservatorio;

	@Historico(name= "Nome do corpo Hídrico")
	@Column(name="nom_corpo_hidrico")
	private String nomCorpoHidrico;
	
	@Historico(name= "Observação")
	@Size(max=500,message = "O campo observação somente suporta 500 caracteres." )
	@Column(name="dsc_observacao")
	private String dscObservacao;

	@Historico(name= "Valor do nível dinamico")
	@Column(name="val_nivel_dinamico")
	private BigDecimal valNivelDinamico;

	@Historico(name= "Valor do nível estático")
	@Column(name="val_nivel_estatico")
	private BigDecimal valNivelEstatico;

	@Historico(name= "Valor da profundidade")
	@Column(name="val_profundidade")
	private BigDecimal valProfundidade;

	@Historico(name= "Valor da vazão instantanêa")
	@Column(name="val_vazao_maxima_instantanea")
	private BigDecimal valVazaoMaximaInstantanea;

	@Historico(name= "Valor da vazão de teste")
	@Column(name="val_vazao_teste")
	private BigDecimal valVazaoTeste;

	@Historico
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_barragem")
	private Barragem ideBarragem;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_cerh_localizacao_geografica")
	private CerhLocalizacaoGeografica ideCerhLocalizacaoGeografica;

	@Historico
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_cerh_natureza_poco")
	private CerhNaturezaPoco ideCerhNaturezaPoco;

	@Historico
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_cerh_situacao_tipo_uso")
	private CerhSituacaoTipoUso ideCerhSituacaoTipoUso;
	
	@Historico
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ide_tipo_corpo_hidrico")
	private TipoCorpoHidrico ideTipoCorpoHidrico;

	@Historico
	@OneToMany(mappedBy="ideCerhCaptacaoCaracterizacao", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Collection<CerhCaptacaoCaracterizacaoFinalidade> cerhCaptacaoCaracterizacaoFinalidadeCollection;

	@Historico(subTable=true, subTableName="Sazonalidade")
	@OneToMany(mappedBy="ideCerhCaptacaoCaracterizacao", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Collection<CerhCaptacaoVazaoSazonalidade> cerhCaptacaoVazaoSazonalidadeCollection;

	@Transient
	private BarragemDTO barragemDTO;
	
	public CerhCaptacaoCaracterizacao() {
		
	}

	public CerhCaptacaoCaracterizacao(LocalizacaoGeografica localizacaoGeografica) {
		this.ideCerhLocalizacaoGeografica = new CerhLocalizacaoGeografica(localizacaoGeografica);
	}

	public CerhCaptacaoCaracterizacao(CerhLocalizacaoGeografica cerhLocalizacaoGeografica) {
		this.ideCerhLocalizacaoGeografica = cerhLocalizacaoGeografica;
	}

	public CerhCaptacaoCaracterizacao(LocalizacaoGeografica localizacaoGeografica, CerhTipoUso cerhTipoUso) {
		this(localizacaoGeografica);
		this.ideCerhLocalizacaoGeografica.setIdeCerhTipoUso(cerhTipoUso);
	}

	public Integer getIdeCerhCaptacaoCaracterizacao() {
		return ideCerhCaptacaoCaracterizacao;
	}

	public void setIdeCerhCaptacaoCaracterizacao(Integer ideCerhCaptacaoCaracterizacao) {
		this.ideCerhCaptacaoCaracterizacao = ideCerhCaptacaoCaracterizacao;
	}

	public Date getDtInicioCaptacao() {
		return dtInicioCaptacao;
	}

	public void setDtInicioCaptacao(Date dtInicioCaptacao) {
		this.dtInicioCaptacao = dtInicioCaptacao;
	}

	public Boolean getIndCaptacaoReservatorio() {
		return indCaptacaoReservatorio;
	}

	public void setIndCaptacaoReservatorio(Boolean indCaptacaoReservatorio) {
		this.indCaptacaoReservatorio = indCaptacaoReservatorio;
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

	public BigDecimal getValNivelDinamico() {
		return valNivelDinamico;
	}

	public void setValNivelDinamico(BigDecimal valNivelDinamico) {
		this.valNivelDinamico = valNivelDinamico;
	}

	public BigDecimal getValNivelEstatico() {
		return valNivelEstatico;
	}

	public void setValNivelEstatico(BigDecimal valNivelEstatico) {
		this.valNivelEstatico = valNivelEstatico;
	}

	public BigDecimal getValProfundidade() {
		return valProfundidade;
	}

	public void setValProfundidade(BigDecimal valProfundidade) {
		this.valProfundidade = valProfundidade;
	}

	public BigDecimal getValVazaoMaximaInstantanea() {
		return valVazaoMaximaInstantanea;
	}

	public void setValVazaoMaximaInstantanea(BigDecimal valVazaoMaximaInstantanea) {
		this.valVazaoMaximaInstantanea = valVazaoMaximaInstantanea;
	}

	public BigDecimal getValVazaoTeste() {
		return valVazaoTeste;
	}

	public void setValVazaoTeste(BigDecimal valVazaoTeste) {
		this.valVazaoTeste = valVazaoTeste;
	}

	public Barragem getIdeBarragem() {
		return ideBarragem;
	}

	public void setIdeBarragem(Barragem ideBarragem) {
		this.ideBarragem = ideBarragem;
	}

	public CerhLocalizacaoGeografica getIdeCerhLocalizacaoGeografica() {
		return ideCerhLocalizacaoGeografica;
	}

	public void setIdeCerhLocalizacaoGeografica(CerhLocalizacaoGeografica ideCerhLocalizacaoGeografica) {
		this.ideCerhLocalizacaoGeografica = ideCerhLocalizacaoGeografica;
	}

	public CerhNaturezaPoco getIdeCerhNaturezaPoco() {
		return ideCerhNaturezaPoco;
	}

	public void setIdeCerhNaturezaPoco(CerhNaturezaPoco ideCerhNaturezaPoco) {
		this.ideCerhNaturezaPoco = ideCerhNaturezaPoco;
	}

	public CerhSituacaoTipoUso getIdeCerhSituacaoTipoUso() {
		return ideCerhSituacaoTipoUso;
	}

	public void setIdeCerhSituacaoTipoUso(CerhSituacaoTipoUso ideCerhSituacaoTipoUso) {
		this.ideCerhSituacaoTipoUso = ideCerhSituacaoTipoUso;
	}

	public Collection<CerhCaptacaoCaracterizacaoFinalidade> getCerhCaptacaoCaracterizacaoFinalidadeCollection() {
		return cerhCaptacaoCaracterizacaoFinalidadeCollection;
	}

	public void setCerhCaptacaoCaracterizacaoFinalidadeCollection(Collection<CerhCaptacaoCaracterizacaoFinalidade> cerhCaptacaoCaracterizacaoFinalidadeCollection) {
		this.cerhCaptacaoCaracterizacaoFinalidadeCollection = cerhCaptacaoCaracterizacaoFinalidadeCollection;
	}

	public Collection<CerhCaptacaoVazaoSazonalidade> getCerhCaptacaoVazaoSazonalidadeCollection() {
		return cerhCaptacaoVazaoSazonalidadeCollection;
	}

	public void setCerhCaptacaoVazaoSazonalidadeCollection(Collection<CerhCaptacaoVazaoSazonalidade> cerhCaptacaoVazaoSazonalidadeCollection) {
		this.cerhCaptacaoVazaoSazonalidadeCollection = cerhCaptacaoVazaoSazonalidadeCollection;
	}

	public TipoCorpoHidrico getIdeTipoCorpoHidrico() {
		return ideTipoCorpoHidrico;
	}

	public void setIdeTipoCorpoHidrico(TipoCorpoHidrico ideTipoCorpoHidrico) {
		this.ideTipoCorpoHidrico = ideTipoCorpoHidrico;
	}

	@Override
	public void setIde(Integer ideCerhCaptacaoCaracterizacao) {
		this.ideCerhCaptacaoCaracterizacao=ideCerhCaptacaoCaracterizacao;
		
	}
	
	@Override
	public Date getData() {
		return this.dtInicioCaptacao;
	}

	@Override
	public void setData(Date data) {
		this.dtInicioCaptacao = data;		
	}

	@Override
	public Collection<CerhVazaoSazonalidadeInterface> getVazaoSazonalidadeCollection() {
		Collection<CerhVazaoSazonalidadeInterface> coll = new ArrayList<CerhVazaoSazonalidadeInterface>();
		if(!Util.isNullOuVazio(this.cerhCaptacaoVazaoSazonalidadeCollection)){
			for (CerhCaptacaoVazaoSazonalidade cerhCaptacaoVazaoSazonalidade : this.cerhCaptacaoVazaoSazonalidadeCollection) {
				CerhVazaoSazonalidadeInterface inter = cerhCaptacaoVazaoSazonalidade;
				coll.add(inter);
			}
		}
		return coll;
	}

	@Override
	public void setVazaoSazonalidadeCollection(Collection<CerhVazaoSazonalidadeInterface> coll) {
		if(Util.isNullOuVazio(this.cerhCaptacaoVazaoSazonalidadeCollection)){
			this.cerhCaptacaoVazaoSazonalidadeCollection = new ArrayList<CerhCaptacaoVazaoSazonalidade>();
		}
		for (CerhVazaoSazonalidadeInterface cerhVazaoSazonalidadeInterface : coll) {
			this.cerhCaptacaoVazaoSazonalidadeCollection.add((CerhCaptacaoVazaoSazonalidade) cerhVazaoSazonalidadeInterface);
		}
	}

	@Override
	public Collection<CerhFinalidadeUsoAguaInterface> getFinalidadeCollection() {
		Collection<CerhFinalidadeUsoAguaInterface> coll = null;
		if(!Util.isNullOuVazio(this.cerhCaptacaoCaracterizacaoFinalidadeCollection)){
			coll = new ArrayList<CerhFinalidadeUsoAguaInterface>();
			for (CerhCaptacaoCaracterizacaoFinalidade finalidade : this.cerhCaptacaoCaracterizacaoFinalidadeCollection) {
				CerhFinalidadeUsoAguaInterface inter = finalidade;
				coll.add(inter);
			}
		}
		return coll;
	}

	public BarragemDTO getBarragemDTO() {
		return barragemDTO;
	}

	public void setBarragemDTO(BarragemDTO barragemDTO) {
		this.barragemDTO = barragemDTO;
	}
	
}