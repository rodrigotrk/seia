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
@Table(name = "tipo_residuo", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_tipo_residuo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoResiduo.findAll", query = "SELECT t FROM TipoResiduo t"),
    @NamedQuery(name = "TipoResiduo.findByIdeTipoResiduo", query = "SELECT t FROM TipoResiduo t WHERE t.ideTipoResiduo = :ideTipoResiduo"),
    @NamedQuery(name = "TipoResiduo.findByNomTipoResiduo", query = "SELECT t FROM TipoResiduo t WHERE t.nomTipoResiduo = :nomTipoResiduo")})
public class TipoResiduo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_residuo", nullable = false)
    private Integer ideTipoResiduo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "nom_tipo_residuo", nullable = false, length = 15)
    private String nomTipoResiduo;

    public TipoResiduo() {
    }

    public TipoResiduo(Integer ideTipoResiduo) {
        this.ideTipoResiduo = ideTipoResiduo;
    }

    public TipoResiduo(Integer ideTipoResiduo, String nomTipoResiduo) {
        this.ideTipoResiduo = ideTipoResiduo;
        this.nomTipoResiduo = nomTipoResiduo;
    }

    public Integer getIdeTipoResiduo() {
        return ideTipoResiduo;
    }

    public void setIdeTipoResiduo(Integer ideTipoResiduo) {
        this.ideTipoResiduo = ideTipoResiduo;
    }

    public String getNomTipoResiduo() {
        return nomTipoResiduo;
    }

    public void setNomTipoResiduo(String nomTipoResiduo) {
        this.nomTipoResiduo = nomTipoResiduo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoResiduo != null ? ideTipoResiduo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoResiduo)) {
            return false;
        }
        TipoResiduo other = (TipoResiduo) object;
        if ((this.ideTipoResiduo == null && other.ideTipoResiduo != null) || (this.ideTipoResiduo != null && !this.ideTipoResiduo.equals(other.ideTipoResiduo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoResiduo[ ideTipoResiduo=" + ideTipoResiduo + " ]";
    }
    
}
