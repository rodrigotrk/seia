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
@Table(name = "natureza_juridica", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_natureza_juridica"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NaturezaJuridica.findAll", query = "SELECT n FROM NaturezaJuridica n"),
    @NamedQuery(name = "NaturezaJuridica.findByIdeNaturezaJuridica", query = "SELECT n FROM NaturezaJuridica n WHERE n.ideNaturezaJuridica = :ideNaturezaJuridica"),
    @NamedQuery(name = "NaturezaJuridica.findByNomNaturezaJuridica", query = "SELECT n FROM NaturezaJuridica n WHERE n.nomNaturezaJuridica = :nomNaturezaJuridica")})
public class NaturezaJuridica implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_natureza_juridica", nullable = false)
    private Integer ideNaturezaJuridica;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "nom_natureza_juridica", nullable = false, length = 60)
    private String nomNaturezaJuridica;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideNaturezaJuridica", fetch = FetchType.LAZY)
    private Collection<PessoaJuridica> pessoaJuridicaCollection;

    public NaturezaJuridica() {
    }

    public NaturezaJuridica(Integer ideNaturezaJuridica) {
        this.ideNaturezaJuridica = ideNaturezaJuridica;
    }

    public NaturezaJuridica(Integer ideNaturezaJuridica, String nomNaturezaJuridica) {
        this.ideNaturezaJuridica = ideNaturezaJuridica;
        this.nomNaturezaJuridica = nomNaturezaJuridica;
    }

    public Integer getIdeNaturezaJuridica() {
        return ideNaturezaJuridica;
    }

    public void setIdeNaturezaJuridica(Integer ideNaturezaJuridica) {
        this.ideNaturezaJuridica = ideNaturezaJuridica;
    }

    public String getNomNaturezaJuridica() {
        return nomNaturezaJuridica;
    }

    public void setNomNaturezaJuridica(String nomNaturezaJuridica) {
        this.nomNaturezaJuridica = nomNaturezaJuridica;
    }

    @XmlTransient
    public Collection<PessoaJuridica> getPessoaJuridicaCollection() {
        return pessoaJuridicaCollection;
    }

    public void setPessoaJuridicaCollection(Collection<PessoaJuridica> pessoaJuridicaCollection) {
        this.pessoaJuridicaCollection = pessoaJuridicaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNaturezaJuridica != null ? ideNaturezaJuridica.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof NaturezaJuridica)) {
            return false;
        }
        NaturezaJuridica other = (NaturezaJuridica) object;
        if ((this.ideNaturezaJuridica == null && other.ideNaturezaJuridica != null) || (this.ideNaturezaJuridica != null && !this.ideNaturezaJuridica.equals(other.ideNaturezaJuridica))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ideNaturezaJuridica.toString();
    }
    
}
