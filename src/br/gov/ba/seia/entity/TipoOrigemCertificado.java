package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Basic;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author micael.coutinho
 */
@Entity
@Table(name = "tipo_origem_certificado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoOrigemCertificado.findAll", query = "SELECT t FROM TipoOrigemCertificado t"),
    @NamedQuery(name = "TipoOrigemCertificado.findByIdeTipoOrigemCertificado", query = "SELECT t FROM TipoOrigemCertificado t WHERE t.ideTipoOrigemCertificado = :ideTipoOrigemCertificado"),
    @NamedQuery(name = "TipoOrigemCertificado.findByDscTipoOrigemCertificado", query = "SELECT t FROM TipoOrigemCertificado t WHERE t.dscTipoOrigemCertificado = :dscTipoOrigemCertificado"),
    @NamedQuery(name = "TipoOrigemCertificado.findByIndExcluido", query = "SELECT t FROM TipoOrigemCertificado t WHERE t.indExcluido = :indExcluido"),
    @NamedQuery(name = "TipoOrigemCertificado.findByDtcCriacao", query = "SELECT t FROM TipoOrigemCertificado t WHERE t.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "TipoOrigemCertificado.findByDtcExclusao", query = "SELECT t FROM TipoOrigemCertificado t WHERE t.dtcExclusao = :dtcExclusao")})
public class TipoOrigemCertificado implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPO_ORIGEM_CERTIFICADO_IDE_TIPO_ORIGEM_CERTIFICADO_SEQ") 
    @SequenceGenerator(name="TIPO_ORIGEM_CERTIFICADO_IDE_TIPO_ORIGEM_CERTIFICADO_SEQ", sequenceName="TIPO_ORIGEM_CERTIFICADO_IDE_TIPO_ORIGEM_CERTIFICADO_SEQ", allocationSize=1)
	@Basic(optional = false)
    @NotNull
	@Column(name = "ide_tipo_origem_certificado", nullable = false)
	private Integer ideTipoOrigemCertificado;
    
	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "dsc_tipo_origem_certificado")
    private String dscTipoOrigemCertificado;
    
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
    
	@OneToMany(mappedBy = "ideTipoOrigemCertificado")
    private Collection<ReservaLegal> reservaLegalCollection;

    public TipoOrigemCertificado() {
    }

    public TipoOrigemCertificado(Integer ideTipoOrigemCertificado) {
        this.ideTipoOrigemCertificado = ideTipoOrigemCertificado;
    }

    public TipoOrigemCertificado(Integer ideTipoOrigemCertificado, String dscTipoOrigemCertificado, boolean indExcluido, Date dtcCriacao) {
        this.ideTipoOrigemCertificado = ideTipoOrigemCertificado;
        this.dscTipoOrigemCertificado = dscTipoOrigemCertificado;
        this.indExcluido = indExcluido;
        this.dtcCriacao = dtcCriacao;
    }

    public Integer getIdeTipoOrigemCertificado() {
        return ideTipoOrigemCertificado;
    }

    public void setIdeTipoOrigemCertificado(Integer ideTipoOrigemCertificado) {
        this.ideTipoOrigemCertificado = ideTipoOrigemCertificado;
    }

    public String getDscTipoOrigemCertificado() {
        return dscTipoOrigemCertificado;
    }

    public void setDscTipoOrigemCertificado(String dscTipoOrigemCertificado) {
        this.dscTipoOrigemCertificado = dscTipoOrigemCertificado;
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
    public Collection<ReservaLegal> getReservaLegalCollection() {
        return reservaLegalCollection;
    }

    public void setReservaLegalCollection(Collection<ReservaLegal> reservaLegalCollection) {
        this.reservaLegalCollection = reservaLegalCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoOrigemCertificado != null ? ideTipoOrigemCertificado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoOrigemCertificado)) {
            return false;
        }
        TipoOrigemCertificado other = (TipoOrigemCertificado) object;
        if ((this.ideTipoOrigemCertificado == null && other.ideTipoOrigemCertificado != null) || (this.ideTipoOrigemCertificado != null && !this.ideTipoOrigemCertificado.equals(other.ideTipoOrigemCertificado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + ideTipoOrigemCertificado;
    }
    
}
