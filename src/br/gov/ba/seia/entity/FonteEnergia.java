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
@Table(name = "fonte_energia", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_fonte_energia"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FonteEnergia.findAll", query = "SELECT f FROM FonteEnergia f"),
    @NamedQuery(name = "FonteEnergia.findByIdeFonteEnergia", query = "SELECT f FROM FonteEnergia f WHERE f.ideFonteEnergia = :ideFonteEnergia"),
    @NamedQuery(name = "FonteEnergia.findByNomFonteEnergia", query = "SELECT f FROM FonteEnergia f WHERE f.nomFonteEnergia = :nomFonteEnergia")})
public class FonteEnergia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_fonte_energia", nullable = false)
    private Integer ideFonteEnergia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "nom_fonte_energia", nullable = false, length = 20)
    private String nomFonteEnergia;

    public FonteEnergia() {
    }

    public FonteEnergia(Integer ideFonteEnergia) {
        this.ideFonteEnergia = ideFonteEnergia;
    }

    public FonteEnergia(Integer ideFonteEnergia, String nomFonteEnergia) {
        this.ideFonteEnergia = ideFonteEnergia;
        this.nomFonteEnergia = nomFonteEnergia;
    }

    public Integer getIdeFonteEnergia() {
        return ideFonteEnergia;
    }

    public void setIdeFonteEnergia(Integer ideFonteEnergia) {
        this.ideFonteEnergia = ideFonteEnergia;
    }

    public String getNomFonteEnergia() {
        return nomFonteEnergia;
    }

    public void setNomFonteEnergia(String nomFonteEnergia) {
        this.nomFonteEnergia = nomFonteEnergia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideFonteEnergia != null ? ideFonteEnergia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof FonteEnergia)) {
            return false;
        }
        FonteEnergia other = (FonteEnergia) object;
        if ((this.ideFonteEnergia == null && other.ideFonteEnergia != null) || (this.ideFonteEnergia != null && !this.ideFonteEnergia.equals(other.ideFonteEnergia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.FonteEnergia[ ideFonteEnergia=" + ideFonteEnergia + " ]";
    }
    
}
