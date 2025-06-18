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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author micael.coutinho
 */
@Entity
@Table(name = "sub_tipo_app")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SubTipoApp.findAll", query = "SELECT s FROM SubTipoApp s"),
    @NamedQuery(name = "SubTipoApp.findByIdeSubTipoApp", query = "SELECT s FROM SubTipoApp s WHERE s.ideSubTipoApp = :ideSubTipoApp"),
    @NamedQuery(name = "SubTipoApp.findByTipoApp", query = "SELECT s FROM SubTipoApp s WHERE s.ideTipoApp = :ideTipoApp"),
    @NamedQuery(name = "SubTipoApp.findByDscSubTipoApp", query = "SELECT s FROM SubTipoApp s WHERE s.dscSubTipoApp = :dscSubTipoApp"),
    @NamedQuery(name = "SubTipoApp.findByIndExcluido", query = "SELECT s FROM SubTipoApp s WHERE s.indExcluido = :indExcluido"),
    @NamedQuery(name = "SubTipoApp.findByDtcCriacao", query = "SELECT s FROM SubTipoApp s WHERE s.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "SubTipoApp.findByDtcExclusao", query = "SELECT s FROM SubTipoApp s WHERE s.dtcExclusao = :dtcExclusao")})
public class SubTipoApp implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SUB_TIPO_APP_IDE_TIPO_APP_SEQ") 
    @SequenceGenerator(name="SUB_TIPO_APP_IDE_TIPO_APP_SEQ", sequenceName="SUB_TIPO_APP_IDE_TIPO_APP_SEQ", allocationSize=1)
	@Basic(optional = false)
    @NotNull
    @Column(name = "ide_sub_tipo_app", nullable = false)
    private Integer ideSubTipoApp;
    
    @JoinColumn(name = "ide_tipo_app", referencedColumnName = "ide_tipo_app")
    @ManyToOne(optional = false)
    private TipoApp ideTipoApp;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "dsc_sub_tipo_app")
    private String dscSubTipoApp;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "dsc_sub_tipo_app_sicar")
    private String dscSubTipoAppSicar;
    
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

    public SubTipoApp() {
    }

    public SubTipoApp(Integer ideSubTipoApp) {
        this.ideSubTipoApp = ideSubTipoApp;
    }

    public SubTipoApp(Integer ideSubTipoApp, String dscSubTipoApp, boolean indExcluido, Date dtcCriacao) {
        this.ideSubTipoApp = ideSubTipoApp;
        this.dscSubTipoApp = dscSubTipoApp;
        this.indExcluido = indExcluido;
        this.dtcCriacao = dtcCriacao;
    }

    public Integer getIdeSubTipoApp() {
        return ideSubTipoApp;
    }

    public void setIdeSubTipoApp(Integer ideSubTipoApp) {
        this.ideSubTipoApp = ideSubTipoApp;
    }

    public TipoApp getIdeTipoApp() {
		return ideTipoApp;
	}

	public void setIdeTipoApp(TipoApp ideTipoApp) {
		this.ideTipoApp = ideTipoApp;
	}

	public String getDscSubTipoApp() {
        return dscSubTipoApp;
    }

    public void setDscSubTipoApp(String dscSubTipoApp) {
        this.dscSubTipoApp = dscSubTipoApp;
    }

    public String getDscSubTipoAppSicar() {
		return dscSubTipoAppSicar;
	}

	public void setDscSubTipoAppSicar(String dscSubTipoAppSicar) {
		this.dscSubTipoAppSicar = dscSubTipoAppSicar;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSubTipoApp != null ? ideSubTipoApp.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof SubTipoApp)) {
            return false;
        }
        SubTipoApp other = (SubTipoApp) object;
        if ((this.ideSubTipoApp == null && other.ideSubTipoApp != null) || (this.ideSubTipoApp != null && !this.ideSubTipoApp.equals(other.ideSubTipoApp))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + ideSubTipoApp;
    }
    
}
