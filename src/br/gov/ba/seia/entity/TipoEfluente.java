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
@Table(name = "tipo_efluente", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_tipo_efluente"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoEfluente.findAll", query = "SELECT t FROM TipoEfluente t"),
    @NamedQuery(name = "TipoEfluente.findByIdeTipoEfluente", query = "SELECT t FROM TipoEfluente t WHERE t.ideTipoEfluente = :ideTipoEfluente"),
    @NamedQuery(name = "TipoEfluente.findByNomTipoEfluente", query = "SELECT t FROM TipoEfluente t WHERE t.nomTipoEfluente = :nomTipoEfluente")})
public class TipoEfluente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_efluente", nullable = false)
    private Integer ideTipoEfluente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nom_tipo_efluente", nullable = false, length = 50)
    private String nomTipoEfluente;

    public TipoEfluente() {
    }

    public TipoEfluente(Integer ideTipoEfluente) {
        this.ideTipoEfluente = ideTipoEfluente;
    }

    public TipoEfluente(Integer ideTipoEfluente, String nomTipoEfluente) {
        this.ideTipoEfluente = ideTipoEfluente;
        this.nomTipoEfluente = nomTipoEfluente;
    }

    public Integer getIdeTipoEfluente() {
        return ideTipoEfluente;
    }

    public void setIdeTipoEfluente(Integer ideTipoEfluente) {
        this.ideTipoEfluente = ideTipoEfluente;
    }

    public String getNomTipoEfluente() {
        return nomTipoEfluente;
    }

    public void setNomTipoEfluente(String nomTipoEfluente) {
        this.nomTipoEfluente = nomTipoEfluente;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoEfluente != null ? ideTipoEfluente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoEfluente)) {
            return false;
        }
        TipoEfluente other = (TipoEfluente) object;
        if ((this.ideTipoEfluente == null && other.ideTipoEfluente != null) || (this.ideTipoEfluente != null && !this.ideTipoEfluente.equals(other.ideTipoEfluente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoEfluente[ ideTipoEfluente=" + ideTipoEfluente + " ]";
    }
    
}
