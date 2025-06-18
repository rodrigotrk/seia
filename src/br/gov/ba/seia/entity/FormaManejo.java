package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "forma_manejo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FormaManejo.findAll", query = "SELECT f FROM FormaManejo f"),
    @NamedQuery(name = "FormaManejo.findByIdeTipoManejo", query = "SELECT f FROM FormaManejo f WHERE f.ideTipoManejo = :ideTipoManejo"),
    @NamedQuery(name = "FormaManejo.findByNomTipoManejo", query = "SELECT f FROM FormaManejo f WHERE f.nomTipoManejo = :nomTipoManejo"),
    @NamedQuery(name = "FormaManejo.findByIndExcluido", query = "SELECT f FROM FormaManejo f WHERE f.indExcluido = :indExcluido"),
    @NamedQuery(name = "FormaManejo.findByDtcCriacao", query = "SELECT f FROM FormaManejo f WHERE f.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "FormaManejo.findByDtcExclusao", query = "SELECT f FROM FormaManejo f WHERE f.dtcExclusao = :dtcExclusao")})
public class FormaManejo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_manejo", nullable = false)
    private Integer ideTipoManejo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nom_tipo_manejo", nullable = false, length = 100)
    private String nomTipoManejo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_criacao", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dtcCriacao;
    @Column(name = "dtc_exclusao")
    @Temporal(TemporalType.DATE)
    private Date dtcExclusao;

    public FormaManejo() {
    }

    public FormaManejo(Integer ideTipoManejo) {
        this.ideTipoManejo = ideTipoManejo;
    }

    public FormaManejo(Integer ideTipoManejo, String nomTipoManejo, boolean indExcluido, Date dtcCriacao) {
        this.ideTipoManejo = ideTipoManejo;
        this.nomTipoManejo = nomTipoManejo;
        this.indExcluido = indExcluido;
        this.dtcCriacao = dtcCriacao;
    }

    public Integer getIdeTipoManejo() {
        return ideTipoManejo;
    }

    public void setIdeTipoManejo(Integer ideTipoManejo) {
        this.ideTipoManejo = ideTipoManejo;
    }

    public String getNomTipoManejo() {
        return nomTipoManejo;
    }

    public void setNomTipoManejo(String nomTipoManejo) {
        this.nomTipoManejo = nomTipoManejo;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoManejo != null ? ideTipoManejo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof FormaManejo)) {
            return false;
        }
        FormaManejo other = (FormaManejo) object;
        if ((this.ideTipoManejo == null && other.ideTipoManejo != null) || (this.ideTipoManejo != null && !this.ideTipoManejo.equals(other.ideTipoManejo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.valueOf(ideTipoManejo);
    }
    
}
