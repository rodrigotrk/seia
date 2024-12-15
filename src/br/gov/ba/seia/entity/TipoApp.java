package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.util.Util;

/**
 *
 * @author micael.coutinho
 */
@Entity
@Table(name = "tipo_app")
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "TipoApp.findAll", query = "SELECT t FROM TipoApp t"),
	@NamedQuery(name = "TipoApp.findByIdeTipoApp", query = "SELECT t FROM TipoApp t WHERE t.ideTipoApp = :ideTipoApp"),
	@NamedQuery(name = "TipoApp.findByDscTipoApp", query = "SELECT t FROM TipoApp t WHERE t.dscTipoApp = :dscTipoApp"),
	@NamedQuery(name = "TipoApp.findByIndExcluido", query = "SELECT t FROM TipoApp t WHERE t.indExcluido = :indExcluido"),
	@NamedQuery(name = "TipoApp.findByDtcCriacao", query = "SELECT t FROM TipoApp t WHERE t.dtcCriacao = :dtcCriacao"),
	@NamedQuery(name = "TipoApp.findByDtcExclusao", query = "SELECT t FROM TipoApp t WHERE t.dtcExclusao = :dtcExclusao")})
public class TipoApp implements Serializable, Comparable<TipoApp> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPO_APP_IDE_TIPO_APP_SEQ")
	@SequenceGenerator(name="TIPO_APP_IDE_TIPO_APP_SEQ", sequenceName="TIPO_APP_IDE_TIPO_APP_SEQ", allocationSize=1)
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_tipo_app", nullable = false)
	private Integer ideTipoApp;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 150)
	@Column(name = "dsc_tipo_app")
	private String dscTipoApp;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 150)
	@Column(name = "dsc_tipo_app_sicar")
	private String dscTipoAppSicar;

	@Basic(optional = false)
	@NotNull
	@Column(name = "ind_excluido")
	private boolean indExcluido;

	@Basic(optional = false)
	@NotNull
	@Column(name = "dtc_criacao")
	@Temporal(TemporalType.TIME)
	private Date dtcCriacao;

	@Column(name = "dtc_exclusao")
	@Temporal(TemporalType.TIME)
	private Date dtcExclusao;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoApp")
	private Collection<App> appCollection;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoApp")
	private Collection<SubTipoApp> subTipoAppCollection;

	@Transient
	private String nomeImovelRural;

	public TipoApp() {
	}

	public TipoApp(Integer ideTipoApp) {
		this.ideTipoApp = ideTipoApp;
	}

	public TipoApp(Integer ideTipoApp, String dscTipoApp, boolean indExcluido, Date dtcCriacao) {
		this.ideTipoApp = ideTipoApp;
		this.dscTipoApp = dscTipoApp;
		this.indExcluido = indExcluido;
		this.dtcCriacao = dtcCriacao;
	}

	public Integer getIdeTipoApp() {
		return ideTipoApp;
	}

	public void setIdeTipoApp(Integer ideTipoApp) {
		this.ideTipoApp = ideTipoApp;
	}

	public String getDscTipoApp() {
		return dscTipoApp;
	}

	public void setDscTipoApp(String dscTipoApp) {
		this.dscTipoApp = dscTipoApp;
	}

	public String getDscTipoAppSicar() {
		return dscTipoAppSicar;
	}

	public void setDscTipoAppSicar(String dscTipoAppSicar) {
		this.dscTipoAppSicar = dscTipoAppSicar;
	}

	public boolean getIndExcluido() {
		return indExcluido;
	}

	public void setIndExcluido(boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public Date getDtcCriacao() {
		return dtcCriacao;
	}

	public void setDtcCriacao(Date dtcCriacao) {
		this.dtcCriacao = dtcCriacao;
	}

	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	@XmlTransient
	public Collection<App> getAppCollection() {
		return appCollection;
	}

	public void setAppCollection(Collection<App> appCollection) {
		this.appCollection = appCollection;
	}

	public Collection<SubTipoApp> getSubTipoAppCollection() {
		return subTipoAppCollection;
	}

	public void setSubTipoAppCollection(Collection<SubTipoApp> subTipoAppCollection) {
		this.subTipoAppCollection = subTipoAppCollection;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (ideTipoApp != null ? ideTipoApp.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		
		if (!(object instanceof TipoApp)) {
			return false;
		}
		TipoApp other = (TipoApp) object;
		if ((this.ideTipoApp == null && other.ideTipoApp != null) || (this.ideTipoApp != null && !this.ideTipoApp.equals(other.ideTipoApp))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "" + ideTipoApp;
	}

	public String getNomeImovelRural() {
		return nomeImovelRural;
	}

	public void setNomeImovelRural(String nomeImovelRural) {
		this.nomeImovelRural = nomeImovelRural;
	}

	public String getNomeImovelAndApp() {
		return nomeImovelRural + " - " + this.dscTipoApp;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(TipoApp o) {
		if (Util.isNull(getIdeTipoApp())){
			return -1;
		}
		return getIdeTipoApp().compareTo(o.getIdeTipoApp());
	}

}
