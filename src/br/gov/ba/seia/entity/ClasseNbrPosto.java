package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author luis
 */
@Entity
@Table(name = "classe_nbr_posto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ClasseNbrPosto.findAll", query = "SELECT c FROM ClasseNbrPosto c"),
    @NamedQuery(name = "ClasseNbrPosto.findByIdeClasseNbrPosto", query = "SELECT c FROM ClasseNbrPosto c WHERE c.ideClasseNbrPosto = :ideClasseNbrPosto"),
    @NamedQuery(name = "ClasseNbrPosto.findByDscClasseNbrPosto", query = "SELECT c FROM ClasseNbrPosto c WHERE c.dscClasseNbrPosto = :dscClasseNbrPosto")})
public class ClasseNbrPosto implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "ide_classe_nbr_posto")
    private Integer ideClasseNbrPosto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "dsc_classe_nbr_posto")
    private String dscClasseNbrPosto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideClasseNbrPosto")
    private Collection<LacPostoCombustivel> postoCombustivelCollection;

    public ClasseNbrPosto() {
    }

    public ClasseNbrPosto(Integer ideClasseNbrPosto) {
        this.ideClasseNbrPosto = ideClasseNbrPosto;
    }

    public ClasseNbrPosto(Integer ideClasseNbrPosto, String dscClasseNbrPosto) {
        this.ideClasseNbrPosto = ideClasseNbrPosto;
        this.dscClasseNbrPosto = dscClasseNbrPosto;
    }

    public Integer getIdeClasseNbrPosto() {
        return ideClasseNbrPosto;
    }

    public void setIdeClasseNbrPosto(Integer ideClasseNbrPosto) {
        this.ideClasseNbrPosto = ideClasseNbrPosto;
    }

    public String getDscClasseNbrPosto() {
        return dscClasseNbrPosto;
    }

    public void setDscClasseNbrPosto(String dscClasseNbrPosto) {
        this.dscClasseNbrPosto = dscClasseNbrPosto;
    }

    @XmlTransient
    public Collection<LacPostoCombustivel> getPostoCombustivelCollection() {
        return postoCombustivelCollection;
    }

    public void setPostoCombustivelCollection(Collection<LacPostoCombustivel> postoCombustivelCollection) {
        this.postoCombustivelCollection = postoCombustivelCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideClasseNbrPosto != null ? ideClasseNbrPosto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ClasseNbrPosto)) {
            return false;
        }
        ClasseNbrPosto other = (ClasseNbrPosto) object;
        if ((this.ideClasseNbrPosto == null && other.ideClasseNbrPosto != null) || (this.ideClasseNbrPosto != null && !this.ideClasseNbrPosto.equals(other.ideClasseNbrPosto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ClasseNbrPosto[ ideClasseNbrPosto=" + ideClasseNbrPosto + " ]";
    }
    
}
