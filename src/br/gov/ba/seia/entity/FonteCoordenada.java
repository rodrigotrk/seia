package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "fonte_coordenada", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_fonte_coordenada"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FonteCoordenada.findAll", query = "SELECT f FROM FonteCoordenada f"),
    @NamedQuery(name = "FonteCoordenada.findByIdeFonteCoordenada", query = "SELECT f FROM FonteCoordenada f WHERE f.ideFonteCoordenada = :ideFonteCoordenada"),
    @NamedQuery(name = "FonteCoordenada.findByNomFonteCoordenada", query = "SELECT f FROM FonteCoordenada f WHERE f.nomFonteCoordenada = :nomFonteCoordenada")})
public class FonteCoordenada implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_fonte_coordenada", nullable = false)
    private Integer ideFonteCoordenada;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "nom_fonte_coordenada", nullable = false, length = 10)
    private String nomFonteCoordenada;
    @OneToMany(fetch = FetchType.LAZY)
    private Collection<LocalizacaoGeografica> localizacaoGeograficaCollection;

    public FonteCoordenada() {
    }

    public FonteCoordenada(Integer ideFonteCoordenada) {
        this.ideFonteCoordenada = ideFonteCoordenada;
    }

    public FonteCoordenada(Integer ideFonteCoordenada, String nomFonteCoordenada) {
        this.ideFonteCoordenada = ideFonteCoordenada;
        this.nomFonteCoordenada = nomFonteCoordenada;
    }

    public Integer getIdeFonteCoordenada() {
        return ideFonteCoordenada;
    }

    public void setIdeFonteCoordenada(Integer ideFonteCoordenada) {
        this.ideFonteCoordenada = ideFonteCoordenada;
    }

    public String getNomFonteCoordenada() {
        return nomFonteCoordenada;
    }

    public void setNomFonteCoordenada(String nomFonteCoordenada) {
        this.nomFonteCoordenada = nomFonteCoordenada;
    }

    @XmlTransient
    public Collection<LocalizacaoGeografica> getLocalizacaoGeograficaCollection() {
        return localizacaoGeograficaCollection;
    }

    public void setLocalizacaoGeograficaCollection(Collection<LocalizacaoGeografica> localizacaoGeograficaCollection) {
        this.localizacaoGeograficaCollection = localizacaoGeograficaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideFonteCoordenada != null ? ideFonteCoordenada.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof FonteCoordenada)) {
            return false;
        }
        FonteCoordenada other = (FonteCoordenada) object;
        if ((this.ideFonteCoordenada == null && other.ideFonteCoordenada != null) || (this.ideFonteCoordenada != null && !this.ideFonteCoordenada.equals(other.ideFonteCoordenada))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.FonteCoordenada[ ideFonteCoordenada=" + ideFonteCoordenada + " ]";
    }
    
}
