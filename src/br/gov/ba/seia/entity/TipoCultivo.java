package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "tipo_cultivo", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_tipo_cultivo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoCultivo.findAll", query = "SELECT t FROM TipoCultivo t"),
    @NamedQuery(name = "TipoCultivo.findByIdeTipoCultivo", query = "SELECT t FROM TipoCultivo t WHERE t.ideTipoCultivo = :ideTipoCultivo"),
    @NamedQuery(name = "TipoCultivo.findByNomTipoCultivo", query = "SELECT t FROM TipoCultivo t WHERE t.nomTipoCultivo = :nomTipoCultivo")})
public class TipoCultivo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_cultivo", nullable = false)
    private Integer ideTipoCultivo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "nom_tipo_cultivo", nullable = false, length = 15)
    private String nomTipoCultivo;

    public TipoCultivo() {
    }

    public TipoCultivo(Integer ideTipoCultivo) {
        this.ideTipoCultivo = ideTipoCultivo;
    }

    public TipoCultivo(Integer ideTipoCultivo, String nomTipoCultivo) {
        this.ideTipoCultivo = ideTipoCultivo;
        this.nomTipoCultivo = nomTipoCultivo;
    }

    public Integer getIdeTipoCultivo() {
        return ideTipoCultivo;
    }

    public void setIdeTipoCultivo(Integer ideTipoCultivo) {
        this.ideTipoCultivo = ideTipoCultivo;
    }

    public String getNomTipoCultivo() {
        return nomTipoCultivo;
    }

    public void setNomTipoCultivo(String nomTipoCultivo) {
        this.nomTipoCultivo = nomTipoCultivo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoCultivo != null ? ideTipoCultivo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoCultivo)) {
            return false;
        }
        TipoCultivo other = (TipoCultivo) object;
        if ((this.ideTipoCultivo == null && other.ideTipoCultivo != null) || (this.ideTipoCultivo != null && !this.ideTipoCultivo.equals(other.ideTipoCultivo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoCultivo[ ideTipoCultivo=" + ideTipoCultivo + " ]";
    }
    
}
