package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author luis
 */
@Entity
@Table(name = "distribuidora_posto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DistribuidoraPosto.findAll", query = "SELECT d FROM DistribuidoraPosto d"),
    @NamedQuery(name = "DistribuidoraPosto.findByIdeDistribuidoraPosto", query = "SELECT d FROM DistribuidoraPosto d WHERE d.ideDistribuidoraPosto = :ideDistribuidoraPosto"),
    @NamedQuery(name = "DistribuidoraPosto.findByDscDistribuidoraPosto", query = "SELECT d FROM DistribuidoraPosto d WHERE d.dscDistribuidoraPosto = :dscDistribuidoraPosto")})
public class DistribuidoraPosto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ide_distribuidora_posto")
    private Integer ideDistribuidoraPosto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "dsc_distribuidora_posto")
    private String dscDistribuidoraPosto;

    public DistribuidoraPosto() {
    }

    public DistribuidoraPosto(Integer ideDistribuidoraPosto) {
        this.ideDistribuidoraPosto = ideDistribuidoraPosto;
    }

    public DistribuidoraPosto(Integer ideDistribuidoraPosto, String dscDistribuidoraPosto) {
        this.ideDistribuidoraPosto = ideDistribuidoraPosto;
        this.dscDistribuidoraPosto = dscDistribuidoraPosto;
    }

    public Integer getIdeDistribuidoraPosto() {
        return ideDistribuidoraPosto;
    }

    public void setIdeDistribuidoraPosto(Integer ideDistribuidoraPosto) {
        this.ideDistribuidoraPosto = ideDistribuidoraPosto;
    }

    public String getDscDistribuidoraPosto() {
        return dscDistribuidoraPosto;
    }

    public void setDscDistribuidoraPosto(String dscDistribuidoraPosto) {
        this.dscDistribuidoraPosto = dscDistribuidoraPosto;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideDistribuidoraPosto != null ? ideDistribuidoraPosto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof DistribuidoraPosto)) {
            return false;
        }
        DistribuidoraPosto other = (DistribuidoraPosto) object;
        if ((this.ideDistribuidoraPosto == null && other.ideDistribuidoraPosto != null) || (this.ideDistribuidoraPosto != null && !this.ideDistribuidoraPosto.equals(other.ideDistribuidoraPosto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.DistribuidoraPosto[ ideDistribuidoraPosto=" + ideDistribuidoraPosto + " ]";
    }
    
}
