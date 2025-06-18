package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author micael.coutinho
 */
@Entity
@Table(name = "app")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "App.findAll", query = "SELECT a FROM App a"),
		@NamedQuery(name = "App.findByIdeApp", query = "SELECT a FROM App a WHERE a.ideApp = :ideApp"),
		@NamedQuery(name = "App.findByValArea", query = "SELECT a FROM App a WHERE a.valArea = :valArea"),
		@NamedQuery(name = "App.findValAreaByIdeRequerimento", 
			query = "SELECT imoApp FROM Requerimento req "
						+ "INNER JOIN req.empreendimentoRequerimentoCollection empReq "
						+ "INNER JOIN empReq.ideEmpreendimento emp "
						+ "INNER JOIN emp.imovelEmpreendimentoCollection empImo "
						+ "INNER JOIN empImo.ideImovel imo "
						+ "INNER JOIN imo.imovelRural imRur"
						+ " INNER JOIN imRur.appCollection imoApp "
						+ "WHERE req.ideRequerimento = :ideRequerimento") })
public class App implements Serializable,Cloneable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APP_IDE_APP_SEQ")
	@SequenceGenerator(name = "APP_IDE_APP_SEQ", sequenceName = "APP_IDE_APP_SEQ", allocationSize = 1)
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_app", nullable = false)
	private Integer ideApp;

	@Basic(optional = false)
	@NotNull
	@Column(name = "val_area", precision = 14, scale = 4)
	private Double valArea;

	@JoinColumn(name = "ide_imovel_rural", referencedColumnName = "ide_imovel_rural", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private ImovelRural ideImovelRural;

	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica")
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private LocalizacaoGeografica ideLocalizacaoGeografica;

	@JoinColumn(name = "ide_tipo_app", referencedColumnName = "ide_tipo_app", nullable = false)
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private TipoApp ideTipoApp;
	
	@JoinColumn(name = "ide_sub_tipo_app", referencedColumnName = "ide_sub_tipo_app", nullable = false)
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private SubTipoApp ideSubTipoApp;

	@JoinColumn(name = "ide_tipo_estado_conservacao", referencedColumnName = "ide_tipo_estado_conservacao", nullable = false)
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private TipoEstadoConservacao ideTipoEstadoConservacao;

	@Basic(optional = false)
	@Column(name = "ind_processo_recuperacao")
	private Boolean indProcessoRecuperacao;
    
	@Basic(optional = false)
	@Column(name = "ind_deseja_cad_prad")
	private Boolean indDesejaCadPrad;
    
	@Column(name = "dtc_resp_deseja_cad_prad")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcRespDesejaCadPrad;
    
	@OneToOne(mappedBy = "ideApp", cascade = CascadeType.ALL)
	private CronogramaRecuperacao cronogramaRecuperacao;
    
	@Transient
	private boolean indExcluido;
    
	@Transient
	private String codigoPersistirShape;
    
	@Transient
	private GeoJsonSicar geoJsonSicar;
    
	@Size(max = 500)
	@Column(name = "dsc_observacao_alteracao_shape")
	private String dscObservacaoAlteracaoShape;
	
	public App() {
	}

	public App(Integer ideApp) {
		this.ideApp = ideApp;
	}

	public App(Integer ideApp, Double valArea) {
		this.ideApp = ideApp;
		this.valArea = valArea;
	}

	public Integer getIdeApp() {
		return ideApp;
	}

	public void setIdeApp(Integer ideApp) {
		this.ideApp = ideApp;
	}

	public Double getValArea() {
		return valArea;
	}

	public void setValArea(Double valArea) {
		this.valArea = valArea;
	}

	public TipoEstadoConservacao getIdeTipoEstadoConservacao() {
		return ideTipoEstadoConservacao;
	}

	public void setIdeTipoEstadoConservacao(TipoEstadoConservacao ideTipoEstadoConservacao) {
		this.ideTipoEstadoConservacao = ideTipoEstadoConservacao;
	}

	public TipoApp getIdeTipoApp() {
		return ideTipoApp;
	}

	public void setIdeTipoApp(TipoApp ideTipoApp) {
		this.ideTipoApp = ideTipoApp;
	}

	public SubTipoApp getIdeSubTipoApp() {
		return ideSubTipoApp;
	}

	public void setIdeSubTipoApp(SubTipoApp ideSubTipoApp) {
		this.ideSubTipoApp = ideSubTipoApp;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public ImovelRural getIdeImovelRural() {
		return ideImovelRural;
	}

	public void setIdeImovelRural(ImovelRural ideImovelRural) {
		this.ideImovelRural = ideImovelRural;
	}

	public Boolean getIndProcessoRecuperacao() {
		return indProcessoRecuperacao;
	}

	public void setIndProcessoRecuperacao(Boolean indProcessoRecuperacao) {
		this.indProcessoRecuperacao = indProcessoRecuperacao;
	}

	public CronogramaRecuperacao getCronogramaRecuperacao() {
		return cronogramaRecuperacao;
	}

	public void setCronogramaRecuperacao(CronogramaRecuperacao cronogramaRecuperacao) {
		this.cronogramaRecuperacao = cronogramaRecuperacao;
	}
	
	public boolean getIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}


	public String getCodigoPersistirShape() {
		return codigoPersistirShape;
	}

	public void setCodigoPersistirShape(String codigoPersistirShape) {
		this.codigoPersistirShape = codigoPersistirShape;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideApp != null ? ideApp.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof App)) {
			return false;
		}
		App other = (App) object;
		if ((this.ideApp == null && other.ideApp != null) || (this.ideApp != null && !this.ideApp.equals(other.ideApp))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "" + ideApp;
	}
	
	public GeoJsonSicar getGeoJsonSicar() {
		return geoJsonSicar;
	}

	public void setGeoJsonSicar(GeoJsonSicar geoJsonSicar) {
		this.geoJsonSicar = geoJsonSicar;
	}

	public String getDscObservacaoAlteracaoShape() {
		return dscObservacaoAlteracaoShape;
	}

	public void setDscObservacaoAlteracaoShape(String dscObservacaoAlteracaoShape) {
		this.dscObservacaoAlteracaoShape = dscObservacaoAlteracaoShape;
	}
	
	@Override
	public App clone() throws CloneNotSupportedException {
		return (App) super.clone();
	}

	public Boolean getIndDesejaCadPrad() {
		return indDesejaCadPrad;
	}

	public void setIndDesejaCadPrad(Boolean indDesejaCadPrad) {
		this.indDesejaCadPrad = indDesejaCadPrad;
	}

	public Date getDtcRespDesejaCadPrad() {
		return dtcRespDesejaCadPrad;
	}

	public void setDtcRespDesejaCadPrad(Date dtcRespDesejaCadPrad) {
		this.dtcRespDesejaCadPrad = dtcRespDesejaCadPrad;
	}

}
