package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.Transient;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "fce_intervencao_caracteristica_extracao")
public class FceIntervencaoCaracteristicaExtracao implements Serializable, BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(generator = "fce_intervencao_caracteristica_extracao_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "fce_intervencao_caracteristica_extracao_seq", sequenceName = "fce_intervencao_caracteristica_extracao_seq", allocationSize = 1)
	@Column(name = "ide_fce_intervencao_caracteristica_extracao", nullable = false)
	private Integer ideFceIntervencaoCaracteristicaExtracao;
	
	@JoinColumn(name = "ide_localizacao_geografica_ini", referencedColumnName = "ide_localizacao_geografica", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	private LocalizacaoGeografica ideLocalizacaoGeograficaIni;
	
	@JoinColumn(name = "ide_localizacao_geografica_fim", referencedColumnName = "ide_localizacao_geografica", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	private LocalizacaoGeografica ideLocalizacaoGeograficaFim;
	
	@JoinColumn(name = "ide_fce_intervencao_mineracao", referencedColumnName = "ide_fce_intervencao_mineracao", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	private FceIntervencaoMineracao ideFceIntervencaoMineracao;
	
	@Column(name = "nom_manancial", nullable = false)
	private String nomManancial;
	
	@Column(name = "nom_coordenada", nullable = false)
	private String nomCoordenada;
	
	@Column(name = "val_periodo_mineracao", nullable = false)
	private Integer valPeriodoMineracao;
	
	@Column(name = "val_vazao_mineral_extraido_polpa", nullable = false, scale = 10, precision = 2)
	private BigDecimal valVazaoMineralExtraidoPolpa;
	
	@Column(name = "val_vazao_agua_polpa", nullable = false, scale = 10, precision = 2)
	private BigDecimal valVazaoAguaPolpa;
	
	@Column(name = "val_vazao_polpa", nullable = false, scale = 10, precision = 2)
	private BigDecimal valVazaoPolpa;
	
	@Column(name = "val_teor_umidade", nullable = false, scale = 10, precision = 2)
	private BigDecimal valTeorUmidade;
	
	@Column(name = "val_area_lavra", nullable = false, scale = 12, precision = 4)
	private BigDecimal valAreaLavra;
	
	@Column(name = "val_producao_maxima_mensal", nullable = false, scale = 10, precision = 2)
	private BigDecimal valProducaoMaximaMensal;
	
	@Column(name = "val_qtde_dias_producao_mes", nullable = false)
	private Integer valQtdeDiasProducaoMes;
	
	@JoinColumn(name = "ide_tipo_mineral_extraido", referencedColumnName = "ide_tipo_mineral_extraido", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER)
	private TipoMineralExtraido ideTipoMineralExtraido;
	
	@OneToMany(mappedBy = "ideFceIntervencaoCaracteristicaExtracao", cascade = CascadeType.ALL)
	private List<FceIntervencaoTipoCaractExtracao> fceIntervencaoTipoCaracticasExtracao;
	
	@Transient
	private boolean editar;
	
	public FceIntervencaoCaracteristicaExtracao() {
		fceIntervencaoTipoCaracticasExtracao = new ArrayList<FceIntervencaoTipoCaractExtracao>();
	}
	
	public Integer getIdeFceIntervencaoCaracteristicaExtracao() {
		return ideFceIntervencaoCaracteristicaExtracao;
	}
	
	public void setIdeFceIntervencaoCaracteristicaExtracao(Integer ideFceIntervencaoCaracteristicaExtracao) {
		this.ideFceIntervencaoCaracteristicaExtracao = ideFceIntervencaoCaracteristicaExtracao;
	}
	
	public LocalizacaoGeografica getIdeLocalizacaoGeograficaIni() {
		return ideLocalizacaoGeograficaIni;
	}
	
	public void setIdeLocalizacaoGeograficaIni(LocalizacaoGeografica ideLocalizacaoGeograficaIni) {
		this.ideLocalizacaoGeograficaIni = ideLocalizacaoGeograficaIni;
	}
	
	public LocalizacaoGeografica getIdeLocalizacaoGeograficaFim() {
		return ideLocalizacaoGeograficaFim;
	}
	
	public void setIdeLocalizacaoGeograficaFim(LocalizacaoGeografica ideLocalizacaoGeograficaFim) {
		this.ideLocalizacaoGeograficaFim = ideLocalizacaoGeograficaFim;
	}
	
	public FceIntervencaoMineracao getIdeFceIntervencaoMineracao() {
		return ideFceIntervencaoMineracao;
	}
	
	public void setIdeFceIntervencaoMineracao(FceIntervencaoMineracao ideFceIntervencaoMineracao) {
		this.ideFceIntervencaoMineracao = ideFceIntervencaoMineracao;
	}
	
	public String getNomManancial() {
		return nomManancial;
	}
	
	public void setNomManancial(String nomManancial) {
		this.nomManancial = nomManancial;
	}
	
	public Integer getValPeriodoMineracao() {
		return valPeriodoMineracao;
	}
	
	public void setValPeriodoMineracao(Integer valPeriodoMineracao) {
		this.valPeriodoMineracao = valPeriodoMineracao;
	}
	
	public BigDecimal getValVazaoMineralExtraidoPolpa() {
		return valVazaoMineralExtraidoPolpa;
	}
	
	public void setValVazaoMineralExtraidoPolpa(BigDecimal valVazaoMineralExtraidoPolpa) {
		this.valVazaoMineralExtraidoPolpa = valVazaoMineralExtraidoPolpa;
	}
	
	public BigDecimal getValVazaoAguaPolpa() {
		return valVazaoAguaPolpa;
	}
	
	public void setValVazaoAguaPolpa(BigDecimal valVazaoAguaPolpa) {
		this.valVazaoAguaPolpa = valVazaoAguaPolpa;
	}
	
	public BigDecimal getValVazaoPolpa() {
		return valVazaoPolpa;
	}
	
	public void setValVazaoPolpa(BigDecimal valVazaoPolpa) {
		this.valVazaoPolpa = valVazaoPolpa;
	}
	
	public BigDecimal getValTeorUmidade() {
		return valTeorUmidade;
	}
	
	public void setValTeorUmidade(BigDecimal valTeorUmidade) {
		this.valTeorUmidade = valTeorUmidade;
	}
	
	public BigDecimal getValAreaLavra() {
		return valAreaLavra;
	}
	
	public void setValAreaLavra(BigDecimal valAreaLavra) {
		this.valAreaLavra = valAreaLavra;
	}
	
	public BigDecimal getValProducaoMaximaMensal() {
		return valProducaoMaximaMensal;
	}
	
	public void setValProducaoMaximaMensal(BigDecimal valProducaoMaximaMensal) {
		this.valProducaoMaximaMensal = valProducaoMaximaMensal;
	}
	
	public Integer getValQtdeDiasProducaoMes() {
		return valQtdeDiasProducaoMes;
	}
	
	public void setValQtdeDiasProducaoMes(Integer valQtdeDiasProducaoMes) {
		this.valQtdeDiasProducaoMes = valQtdeDiasProducaoMes;
	}
	
	public TipoMineralExtraido getIdeTipoMineralExtraido() {
		return ideTipoMineralExtraido;
	}
	
	public void setIdeTipoMineralExtraido(TipoMineralExtraido ideTipoMineralExtraido) {
		this.ideTipoMineralExtraido = ideTipoMineralExtraido;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideFceIntervencaoCaracteristicaExtracao == null) ? 0 : ideFceIntervencaoCaracteristicaExtracao.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		FceIntervencaoCaracteristicaExtracao other = (FceIntervencaoCaracteristicaExtracao) obj;
		if (ideFceIntervencaoCaracteristicaExtracao == null) {
			if (other.ideFceIntervencaoCaracteristicaExtracao != null) return false;
		} else if (!ideFceIntervencaoCaracteristicaExtracao.equals(other.ideFceIntervencaoCaracteristicaExtracao)) return false;
		return true;
	}
	
	@Override
	public Long getId() {
		return new Long(ideFceIntervencaoCaracteristicaExtracao);
	}
	
	public String getNomCoordenada() {
		return nomCoordenada;
	}
	
	public void setNomCoordenada(String nomCoordenada) {
		this.nomCoordenada = nomCoordenada;
	}
	
	public boolean isEditar() {
		return editar;
	}
	
	public void setEditar(boolean editar) {
		this.editar = editar;
	}
	
	public List<FceIntervencaoTipoCaractExtracao> getFceIntervencaoTipoCaracticasExtracao() {
		return fceIntervencaoTipoCaracticasExtracao;
	}
	
	public void setFceIntervencaoTipoCaracticasExtracao(List<FceIntervencaoTipoCaractExtracao> fceIntervencaoTipoCaracticasExtracao) {
		this.fceIntervencaoTipoCaracticasExtracao = fceIntervencaoTipoCaracticasExtracao;
	}
}