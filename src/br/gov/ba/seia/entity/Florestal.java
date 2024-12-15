package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author eduardo.fernandes
 * 
 */

@Entity
@Table(name = "florestal")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Florestal.findAll", query = "SELECT f FROM Florestal f"),
		@NamedQuery(name = "Florestal.findByIdeFlorestal", query = "SELECT f FROM Florestal f WHERE f.ideFlorestal = :ideFlorestal"),
		@NamedQuery(name = "Florestal.findByIdeTipoReservaLegal", query = "SELECT f FROM Florestal f WHERE f.ideTipoReservaLegal = :ideTipoReservaLegal"),
		@NamedQuery(name = "Florestal.findByIdeRequerimento", query = "SELECT f FROM Florestal f WHERE f.ideRequerimento.ideRequerimento = :ideRequerimento"),
		@NamedQuery(name = "Florestal.findByIdeRequerimentoImovel", query = "SELECT f FROM Florestal f WHERE f.ideRequerimento.ideRequerimento = :ideRequerimento and f.ideImovel.ideImovel = :ideImovel"),
		@NamedQuery(name = "Florestal.removeByIdeFlorestal", query = "DELETE FROM Florestal f WHERE f.ideFlorestal = :ideFlorestal"),
		@NamedQuery(name = "Florestal.removeByIdeRequerimento", query = "DELETE FROM Florestal f WHERE f.ideRequerimento.ideRequerimento = :ideRequerimento")})
public class Florestal implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "florestal_ide_florestal_generator", sequenceName = "florestal_ide_florestal_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "florestal_ide_florestal_generator")
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_florestal")
	private Integer ideFlorestal;

	@JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Requerimento ideRequerimento;

	@JoinColumn(name = "ide_tipo_solicitacao", referencedColumnName = "ide_tipo_solicitacao")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipoSolicitacao ideTipoSolicitacao;

	@Column(name = "num_area_reserva_legal", precision = 10, scale = 4)
	private BigDecimal numAreaReservaLegal;
	
	@Column(name = "num_area_manejo_cabruca", precision = 10, scale = 4)
	private BigDecimal numAreaManejoCabruca;

	@Column(name = "num_area_servidao_florestal", precision = 10, scale = 4)
	private BigDecimal numAreaServidaoFlorestal;

	@Column(name = "num_processo_aprovacao_servidao_florestal", length = 50)
	private String numProcessoAprovacaoServidaoFlorestal;

	@Column(name = "num_area_processo_aprovacao_servidao_florestal", precision = 10, scale = 4)
	private BigDecimal numAreaProcessoAprovacaoServidaoFlorestal;

	@Column(name = "num_processo_aprovacao_reserva_legal", length = 50)
	private String numProcessoAprovacaoReservaLegal;

	@Column(name = "num_area_processo_aprovacao_reserva_legal", precision = 10, scale = 4)
	private BigDecimal numAreaProcessoAprovacaoReservaLegal;

	@Column(name = "num_area_suprimida", precision = 10, scale = 4)
	private BigDecimal numAreaSuprimida;

	@Column(name = "num_area_queimada", precision = 10, scale = 4)
	private BigDecimal numAreaQueimada;

	@Column(name = "num_area_plano_manejo_florestal_sustentavel", precision = 10, scale = 4)
	private BigDecimal numAreaPlanoManejoFlorestalSustentavel;

	@Column(name = "num_processo_aprovacao_plano_manejo_florestal_sustentavel", length = 50)
	private String numProcessoAprovacaoPlanoManejoFlorestalSustentavel;

	@Column(name = "num_area_corte_unidade_producao", precision = 10, scale = 4)
	private BigDecimal numAreaCorteUnidadeProducao;
	
	@Column(name = "num_processo_licenciamento_estado", length = 50)
	private String numProcessoLicenciamentoEstado;
	
	@Column(name = "num_portaria_estado", length = 50)
	private String numPortariaEstado;
	
	@Column(name = "num_processo_licenciamento_municipio", length = 50)
	private String numProcessoLicenciamentoMunicipio;

	@Column(name = "num_portaria_municipio", length = 50)
	private String numPortariaMunicipio;
	
	@Column(name = "num_processo_registro_floresta_plantada", length = 50)
	private String numProcessoRegistroFlorestaPlantada;

	@Column(name = "num_processo_emissao_credito_reposicao_florestal", length = 50)
	private String numProcessoEmissaoCreditoReposicaoFlorestal;
	
	@Column(name = "num_portaria_emissao_credito_reposicao_florestal", length = 50)
	private String numPortariaEmissaoCreditoReposicaoFlorestal;

	@JoinColumn(name = "ide_imovel", referencedColumnName = "ide_imovel", nullable = true)
	@ManyToOne(fetch = FetchType.LAZY)
	private Imovel ideImovel;

	@JoinColumn(name = "ide_tipo_reserva_legal", referencedColumnName = "ide_tipo_reserva_legal", nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private TipoReservaLegal ideTipoReservaLegal;

	@OneToMany(mappedBy = "ideFlorestal")
	private Collection<FlorestalAtoAmbiental> florestalAtoAmbientalCollection;

	@OneToMany(mappedBy = "ideFlorestal")
	private Collection<FlorestalCaracteristicaFlorestaProducao> caracteristicasFlorestaProducaoCollection;

	@Transient
	private Collection<CaracteristicaFlorestaProducao> caracteristicasFlorestaProducao;

	public Florestal() {
	}

	public Florestal(Requerimento ideRequerimento) {
		super();
		this.ideRequerimento = ideRequerimento;
	}

	public Florestal(Integer ideFlorestal) {
		this.ideFlorestal = ideFlorestal;
	}

	public Integer getIdeFlorestal() {
		return ideFlorestal;
	}

	public void setIdeFlorestal(Integer ideFlorestal) {
		this.ideFlorestal = ideFlorestal;
	}

	public Requerimento getIdeRequerimento() {
		return ideRequerimento;
	}

	public void setIdeRequerimento(Requerimento ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}

	public TipoSolicitacao getIdeTipoSolicitacao() {
		return ideTipoSolicitacao;
	}

	public void setIdeTipoSolicitacao(TipoSolicitacao ideTipoSolicitacao) {
		this.ideTipoSolicitacao = ideTipoSolicitacao;
	}

	public BigDecimal getNumAreaReservaLegal() {
		return numAreaReservaLegal;
	}

	public void setNumAreaReservaLegal(BigDecimal numAreaReservaLegal) {
		this.numAreaReservaLegal = numAreaReservaLegal;
	}

	public BigDecimal getNumAreaServidaoFlorestal() {
		return numAreaServidaoFlorestal;
	}

	public void setNumAreaServidaoFlorestal(BigDecimal numAreaServidaoFlorestal) {
		this.numAreaServidaoFlorestal = numAreaServidaoFlorestal;
	}

	public String getNumProcessoAprovacaoServidaoFlorestal() {
		return numProcessoAprovacaoServidaoFlorestal;
	}

	public void setNumProcessoAprovacaoServidaoFlorestal(String numProcessoAprovacaoServidaoFlorestal) {
		this.numProcessoAprovacaoServidaoFlorestal = numProcessoAprovacaoServidaoFlorestal;
	}

	public BigDecimal getNumAreaProcessoAprovacaoServidaoFlorestal() {
		return numAreaProcessoAprovacaoServidaoFlorestal;
	}

	public void setNumAreaProcessoAprovacaoServidaoFlorestal(BigDecimal numAreaProcessoAprovacaoServidaoFlorestal) {
		this.numAreaProcessoAprovacaoServidaoFlorestal = numAreaProcessoAprovacaoServidaoFlorestal;
	}

	public String getNumProcessoAprovacaoReservaLegal() {
		return numProcessoAprovacaoReservaLegal;
	}

	public void setNumProcessoAprovacaoReservaLegal(String numProcessoAprovacaoReservaLegal) {
		this.numProcessoAprovacaoReservaLegal = numProcessoAprovacaoReservaLegal;
	}

	public BigDecimal getNumAreaProcessoAprovacaoReservaLegal() {
		return numAreaProcessoAprovacaoReservaLegal;
	}

	public void setNumAreaProcessoAprovacaoReservaLegal(BigDecimal numAreaProcessoAprovacaoReservaLegal) {
		this.numAreaProcessoAprovacaoReservaLegal = numAreaProcessoAprovacaoReservaLegal;
	}

	public BigDecimal getNumAreaSuprimida() {
		return numAreaSuprimida;
	}

	public void setNumAreaSuprimida(BigDecimal numAreaSuprimida) {
		this.numAreaSuprimida = numAreaSuprimida;
	}

	public BigDecimal getNumAreaQueimada() {
		return numAreaQueimada;
	}

	public void setNumAreaQueimada(BigDecimal numAreaQueimada) {
		this.numAreaQueimada = numAreaQueimada;
	}

	public BigDecimal getNumAreaPlanoManejoFlorestalSustentavel() {
		return numAreaPlanoManejoFlorestalSustentavel;
	}

	public void setNumAreaPlanoManejoFlorestalSustentavel(BigDecimal numAreaPlanoManejoFlorestalSustentavel) {
		this.numAreaPlanoManejoFlorestalSustentavel = numAreaPlanoManejoFlorestalSustentavel;
	}

	public String getNumProcessoAprovacaoPlanoManejoFlorestalSustentavel() {
		return numProcessoAprovacaoPlanoManejoFlorestalSustentavel;
	}

	public void setNumProcessoAprovacaoPlanoManejoFlorestalSustentavel(
			String numProcessoAprovacaoPlanoManejoFlorestalSustentavel) {
		this.numProcessoAprovacaoPlanoManejoFlorestalSustentavel = numProcessoAprovacaoPlanoManejoFlorestalSustentavel;
	}

	public BigDecimal getNumAreaCorteUnidadeProducao() {
		return numAreaCorteUnidadeProducao;
	}

	public void setNumAreaCorteUnidadeProducao(BigDecimal numAreaCorteUnidadeProducao) {
		this.numAreaCorteUnidadeProducao = numAreaCorteUnidadeProducao;
	}

	public String getNumProcessoLicenciamentoEstado() {
		return numProcessoLicenciamentoEstado;
	}

	public void setNumProcessoLicenciamentoEstado(String numProcessoLicenciamentoEstado) {
		this.numProcessoLicenciamentoEstado = numProcessoLicenciamentoEstado;
	}

	public String getNumPortariaEstado() {
		return numPortariaEstado;
	}

	public void setNumPortariaEstado(String numPortariaEstado) {
		this.numPortariaEstado = numPortariaEstado;
	}

	public String getNumProcessoLicenciamentoMunicipio() {
		return numProcessoLicenciamentoMunicipio;
	}

	public void setNumProcessoLicenciamentoMunicipio(String numProcessoLicenciamentoMunicipio) {
		this.numProcessoLicenciamentoMunicipio = numProcessoLicenciamentoMunicipio;
	}

	public String getNumPortariaMunicipio() {
		return numPortariaMunicipio;
	}

	public void setNumPortariaMunicipio(String numPortariaMunicipio) {
		this.numPortariaMunicipio = numPortariaMunicipio;
	}

	public String getNumProcessoRegistroFlorestaPlantada() {
		return numProcessoRegistroFlorestaPlantada;
	}

	public void setNumProcessoRegistroFlorestaPlantada(String numProcessoRegistroFlorestaPlantada) {
		this.numProcessoRegistroFlorestaPlantada = numProcessoRegistroFlorestaPlantada;
	}

	public String getNumProcessoEmissaoCreditoReposicaoFlorestal() {
		return numProcessoEmissaoCreditoReposicaoFlorestal;
	}

	public void setNumProcessoEmissaoCreditoReposicaoFlorestal(String numProcessoEmissaoCreditoReposicaoFlorestal) {
		this.numProcessoEmissaoCreditoReposicaoFlorestal = numProcessoEmissaoCreditoReposicaoFlorestal;
	}

	public String getNumPortariaEmissaoCreditoReposicaoFlorestal() {
		return numPortariaEmissaoCreditoReposicaoFlorestal;
	}

	public void setNumPortariaEmissaoCreditoReposicaoFlorestal(String numPortariaEmissaoCreditoReposicaoFlorestal) {
		this.numPortariaEmissaoCreditoReposicaoFlorestal = numPortariaEmissaoCreditoReposicaoFlorestal;
	}

	public Imovel getIdeImovel() {
		return ideImovel;
	}

	public void setIdeImovel(Imovel ideImovel) {
		this.ideImovel = ideImovel;
	}

	public TipoReservaLegal getIdeTipoReservaLegal() {
		return ideTipoReservaLegal;
	}

	public void setIdeTipoReservaLegal(TipoReservaLegal ideTipoReservaLegal) {
		this.ideTipoReservaLegal = ideTipoReservaLegal;
	}

	public Collection<FlorestalAtoAmbiental> getFlorestalAtoAmbientalCollection() {
		return florestalAtoAmbientalCollection;
	}

	public void setFlorestalAtoAmbientalCollection(Collection<FlorestalAtoAmbiental> florestalAtoAmbientalCollection) {
		this.florestalAtoAmbientalCollection = florestalAtoAmbientalCollection;
	}

	public Collection<FlorestalCaracteristicaFlorestaProducao> getCaracteristicasFlorestaProducaoCollection() {
		return caracteristicasFlorestaProducaoCollection;
	}

	public void setCaracteristicasFlorestaProducaoCollection(
			Collection<FlorestalCaracteristicaFlorestaProducao> caracteristicasFlorestaProducaoCollection) {
		this.caracteristicasFlorestaProducaoCollection = caracteristicasFlorestaProducaoCollection;
	}

	public Collection<CaracteristicaFlorestaProducao> getCaracteristicasFlorestaProducao() {
		return caracteristicasFlorestaProducao;
	}

	public void setCaracteristicasFlorestaProducao(
			Collection<CaracteristicaFlorestaProducao> caracteristicasFlorestaProducao) {
		this.caracteristicasFlorestaProducao = caracteristicasFlorestaProducao;
	}

	public BigDecimal getNumAreaManejoCabruca() {
		return numAreaManejoCabruca;
	}

	public void setNumAreaManejoCabruca(BigDecimal numAreaManejoCabruca) {
		this.numAreaManejoCabruca = numAreaManejoCabruca;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ideFlorestal == null) ? 0 : ideFlorestal.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "entity.Florestal[ ideFlorestal=" + ideFlorestal + " ]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Florestal other = (Florestal) obj;
		if (ideFlorestal == null) {
			if (other.ideFlorestal != null)
				return false;
		} else if (!ideFlorestal.equals(other.ideFlorestal))
			return false;
		return true;
	}

}