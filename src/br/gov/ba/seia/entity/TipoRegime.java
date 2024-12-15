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
@Table(name = "tipo_regime", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ide_tipo_regime"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoRegime.findAll", query = "SELECT t FROM TipoRegime t"),
    @NamedQuery(name = "TipoRegime.findByIdeTipoRegime", query = "SELECT t FROM TipoRegime t WHERE t.ideTipoRegime = :ideTipoRegime"),
    @NamedQuery(name = "TipoRegime.findByNomTipoRegime", query = "SELECT t FROM TipoRegime t WHERE t.nomTipoRegime = :nomTipoRegime"),
    @NamedQuery(name = "TipoRegime.findByIndPeriodoMensal", query = "SELECT t FROM TipoRegime t WHERE t.indPeriodoMensal = :indPeriodoMensal")})
public class TipoRegime implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_regime", nullable = false)
    private Integer ideTipoRegime;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "nom_tipo_regime", nullable = false, length = 15)
    private String nomTipoRegime;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_periodo_mensal", nullable = false)
    private boolean indPeriodoMensal;

    public TipoRegime() {
    }

    public TipoRegime(Integer ideTipoRegime) {
        this.ideTipoRegime = ideTipoRegime;
    }

    public TipoRegime(Integer ideTipoRegime, String nomTipoRegime, boolean indPeriodoMensal) {
        this.ideTipoRegime = ideTipoRegime;
        this.nomTipoRegime = nomTipoRegime;
        this.indPeriodoMensal = indPeriodoMensal;
    }

    public Integer getIdeTipoRegime() {
        return ideTipoRegime;
    }

    public void setIdeTipoRegime(Integer ideTipoRegime) {
        this.ideTipoRegime = ideTipoRegime;
    }

    public String getNomTipoRegime() {
        return nomTipoRegime;
    }

    public void setNomTipoRegime(String nomTipoRegime) {
        this.nomTipoRegime = nomTipoRegime;
    }

    public boolean getIndPeriodoMensal() {
        return indPeriodoMensal;
    }

    public void setIndPeriodoMensal(boolean indPeriodoMensal) {
        this.indPeriodoMensal = indPeriodoMensal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoRegime != null ? ideTipoRegime.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoRegime)) {
            return false;
        }
        TipoRegime other = (TipoRegime) object;
        if ((this.ideTipoRegime == null && other.ideTipoRegime != null) || (this.ideTipoRegime != null && !this.ideTipoRegime.equals(other.ideTipoRegime))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoRegime[ ideTipoRegime=" + ideTipoRegime + " ]";
    }
    
}
