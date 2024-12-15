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

@Entity
@Table(name = "nivel_cnae", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_nivel_cnae"}),
    @UniqueConstraint(columnNames = {"num_nivel_cnae"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NivelCnae.findAll", query = "SELECT n FROM NivelCnae n"),
    @NamedQuery(name = "NivelCnae.findByIdeNivelCnae", query = "SELECT n FROM NivelCnae n WHERE n.ideNivelCnae = :ideNivelCnae"),
    @NamedQuery(name = "NivelCnae.findByNumNivelCnae", query = "SELECT n FROM NivelCnae n WHERE n.numNivelCnae = :numNivelCnae"),
    @NamedQuery(name = "NivelCnae.findByNomNivelCnae", query = "SELECT n FROM NivelCnae n WHERE n.nomNivelCnae = :nomNivelCnae")})
public class NivelCnae implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_nivel_cnae", nullable = false)
    private Integer ideNivelCnae;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_nivel_cnae", nullable = false)
    private int numNivelCnae;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "nom_nivel_cnae", nullable = false, length = 15)
    private String nomNivelCnae;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideNivelCnae", fetch = FetchType.EAGER)
    private Collection<Cnae> cnaeCollection;

    public NivelCnae() {
    }

    public NivelCnae(Integer ideNivelCnae) {
        this.ideNivelCnae = ideNivelCnae;
    }

    public NivelCnae(Integer ideNivelCnae, int numNivelCnae, String nomNivelCnae) {
        this.ideNivelCnae = ideNivelCnae;
        this.numNivelCnae = numNivelCnae;
        this.nomNivelCnae = nomNivelCnae;
    }

    public Integer getIdeNivelCnae() {
        return ideNivelCnae;
    }

    public void setIdeNivelCnae(Integer ideNivelCnae) {
        this.ideNivelCnae = ideNivelCnae;
    }

    public int getNumNivelCnae() {
        return numNivelCnae;
    }

    public void setNumNivelCnae(int numNivelCnae) {
        this.numNivelCnae = numNivelCnae;
    }

    public String getNomNivelCnae() {
        return nomNivelCnae;
    }

    public void setNomNivelCnae(String nomNivelCnae) {
        this.nomNivelCnae = nomNivelCnae;
    }

    @XmlTransient
    public Collection<Cnae> getCnaeCollection() {
        return cnaeCollection;
    }

    public void setCnaeCollection(Collection<Cnae> cnaeCollection) {
        this.cnaeCollection = cnaeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNivelCnae != null ? ideNivelCnae.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof NivelCnae)) {
            return false;
        }
        NivelCnae other = (NivelCnae) object;
        if ((this.ideNivelCnae == null && other.ideNivelCnae != null) || (this.ideNivelCnae != null && !this.ideNivelCnae.equals(other.ideNivelCnae))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.NivelCnae[ ideNivelCnae=" + ideNivelCnae + " ]";
    }
    
}
