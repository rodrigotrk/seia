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
@Table(name = "tipo_fonte", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_tipo_fonte"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoFonte.findAll", query = "SELECT t FROM TipoFonte t"),
    @NamedQuery(name = "TipoFonte.findByIdeTipoFonte", query = "SELECT t FROM TipoFonte t WHERE t.ideTipoFonte = :ideTipoFonte"),
    @NamedQuery(name = "TipoFonte.findByNomTipoFonte", query = "SELECT t FROM TipoFonte t WHERE t.nomTipoFonte = :nomTipoFonte")})
public class TipoFonte implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_fonte", nullable = false)
    private Integer ideTipoFonte;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nom_tipo_fonte", nullable = false, length = 30)
    private String nomTipoFonte;

    public TipoFonte() {
    }

    public TipoFonte(Integer ideTipoFonte) {
        this.ideTipoFonte = ideTipoFonte;
    }

    public TipoFonte(Integer ideTipoFonte, String nomTipoFonte) {
        this.ideTipoFonte = ideTipoFonte;
        this.nomTipoFonte = nomTipoFonte;
    }

    public Integer getIdeTipoFonte() {
        return ideTipoFonte;
    }

    public void setIdeTipoFonte(Integer ideTipoFonte) {
        this.ideTipoFonte = ideTipoFonte;
    }

    public String getNomTipoFonte() {
        return nomTipoFonte;
    }

    public void setNomTipoFonte(String nomTipoFonte) {
        this.nomTipoFonte = nomTipoFonte;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoFonte != null ? ideTipoFonte.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoFonte)) {
            return false;
        }
        TipoFonte other = (TipoFonte) object;
        if ((this.ideTipoFonte == null && other.ideTipoFonte != null) || (this.ideTipoFonte != null && !this.ideTipoFonte.equals(other.ideTipoFonte))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoFonte[ ideTipoFonte=" + ideTipoFonte + " ]";
    }
    
}
