package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "nivel_tipologia", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"num_nivel_tipologia"}),
    @UniqueConstraint(columnNames = {"nom_nivel_tecnologia"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NivelTipologia.findAll", query = "SELECT n FROM NivelTipologia n"),
    @NamedQuery(name = "NivelTipologia.findByIdeNivelTipologia", query = "SELECT n FROM NivelTipologia n WHERE n.ideNivelTipologia = :ideNivelTipologia"),
    @NamedQuery(name = "NivelTipologia.findByNumNivelTipologia", query = "SELECT n FROM NivelTipologia n WHERE n.numNivelTipologia = :numNivelTipologia"),
    @NamedQuery(name = "NivelTipologia.findByNomNivelTecnologia", query = "SELECT n FROM NivelTipologia n WHERE n.nomNivelTecnologia = :nomNivelTecnologia")})
public class NivelTipologia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_nivel_tipologia", nullable = false)
    private Integer ideNivelTipologia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_nivel_tipologia", nullable = false)
    private int numNivelTipologia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nom_nivel_tecnologia", nullable = false, length = 100)
    private String nomNivelTecnologia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideNivelTipologia", fetch = FetchType.LAZY)
    private Collection<Tipologia> tipologiaCollection;

    public NivelTipologia() {
    }

    public NivelTipologia(Integer ideNivelTipologia) {
        this.ideNivelTipologia = ideNivelTipologia;
    }

    public NivelTipologia(Integer ideNivelTipologia, int numNivelTipologia, String nomNivelTecnologia) {
        this.ideNivelTipologia = ideNivelTipologia;
        this.numNivelTipologia = numNivelTipologia;
        this.nomNivelTecnologia = nomNivelTecnologia;
    }

    public Integer getIdeNivelTipologia() {
        return ideNivelTipologia;
    }

    public void setIdeNivelTipologia(Integer ideNivelTipologia) {
        this.ideNivelTipologia = ideNivelTipologia;
    }

    public int getNumNivelTipologia() {
        return numNivelTipologia;
    }

    public void setNumNivelTipologia(int numNivelTipologia) {
        this.numNivelTipologia = numNivelTipologia;
    }

    public String getNomNivelTecnologia() {
        return nomNivelTecnologia;
    }

    public void setNomNivelTecnologia(String nomNivelTecnologia) {
        this.nomNivelTecnologia = nomNivelTecnologia;
    }

    @XmlTransient
    public Collection<Tipologia> getTipologiaCollection() {
        return tipologiaCollection;
    }

    public void setTipologiaCollection(Collection<Tipologia> tipologiaCollection) {
        this.tipologiaCollection = tipologiaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNivelTipologia != null ? ideNivelTipologia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof NivelTipologia)) {
            return false;
        }
        NivelTipologia other = (NivelTipologia) object;
        if ((this.ideNivelTipologia == null && other.ideNivelTipologia != null) || (this.ideNivelTipologia != null && !this.ideNivelTipologia.equals(other.ideNivelTipologia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.valueOf(ideNivelTipologia);
    }
    
}
