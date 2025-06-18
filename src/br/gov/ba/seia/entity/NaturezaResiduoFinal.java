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
@Table(name = "natureza_residuo_final", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_natureza_residuo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NaturezaResiduoFinal.findAll", query = "SELECT n FROM NaturezaResiduoFinal n"),
    @NamedQuery(name = "NaturezaResiduoFinal.findByIdeNaturezaResiduo", query = "SELECT n FROM NaturezaResiduoFinal n WHERE n.ideNaturezaResiduo = :ideNaturezaResiduo"),
    @NamedQuery(name = "NaturezaResiduoFinal.findByNomNaturezaResiduo", query = "SELECT n FROM NaturezaResiduoFinal n WHERE n.nomNaturezaResiduo = :nomNaturezaResiduo")})
public class NaturezaResiduoFinal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_natureza_residuo", nullable = false)
    private Integer ideNaturezaResiduo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "nom_natureza_residuo", nullable = false, length = 10)
    private String nomNaturezaResiduo;

    public NaturezaResiduoFinal() {
    }

    public NaturezaResiduoFinal(Integer ideNaturezaResiduo) {
        this.ideNaturezaResiduo = ideNaturezaResiduo;
    }

    public NaturezaResiduoFinal(Integer ideNaturezaResiduo, String nomNaturezaResiduo) {
        this.ideNaturezaResiduo = ideNaturezaResiduo;
        this.nomNaturezaResiduo = nomNaturezaResiduo;
    }

    public Integer getIdeNaturezaResiduo() {
        return ideNaturezaResiduo;
    }

    public void setIdeNaturezaResiduo(Integer ideNaturezaResiduo) {
        this.ideNaturezaResiduo = ideNaturezaResiduo;
    }

    public String getNomNaturezaResiduo() {
        return nomNaturezaResiduo;
    }

    public void setNomNaturezaResiduo(String nomNaturezaResiduo) {
        this.nomNaturezaResiduo = nomNaturezaResiduo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNaturezaResiduo != null ? ideNaturezaResiduo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof NaturezaResiduoFinal)) {
            return false;
        }
        NaturezaResiduoFinal other = (NaturezaResiduoFinal) object;
        if ((this.ideNaturezaResiduo == null && other.ideNaturezaResiduo != null) || (this.ideNaturezaResiduo != null && !this.ideNaturezaResiduo.equals(other.ideNaturezaResiduo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.NaturezaResiduoFinal[ ideNaturezaResiduo=" + ideNaturezaResiduo + " ]";
    }
    
}
