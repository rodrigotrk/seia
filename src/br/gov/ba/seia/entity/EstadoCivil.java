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

@Entity
@Table(name = "estado_civil", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_estado_civil"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoCivil.findAll", query = "SELECT e FROM EstadoCivil e"),
    @NamedQuery(name = "EstadoCivil.findByIdeEstadoCivil", query = "SELECT e FROM EstadoCivil e WHERE e.ideEstadoCivil = :ideEstadoCivil"),
    @NamedQuery(name = "EstadoCivil.findByNomEstadoCivil", query = "SELECT e FROM EstadoCivil e WHERE e.nomEstadoCivil = :nomEstadoCivil")})
public class EstadoCivil implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_estado_civil", nullable = false)
    private Integer ideEstadoCivil;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nom_estado_civil", nullable = false, length = 30)
    private String nomEstadoCivil;
    @OneToMany(mappedBy = "ideEstadoCivil", fetch = FetchType.LAZY)
    private Collection<PessoaFisica> pessoaFisicaCollection;

    public EstadoCivil() {
    }

    public EstadoCivil(Integer ideEstadoCivil) {
        this.ideEstadoCivil = ideEstadoCivil;
    }

    public EstadoCivil(Integer ideEstadoCivil, String nomEstadoCivil) {
        this.ideEstadoCivil = ideEstadoCivil;
        this.nomEstadoCivil = nomEstadoCivil;
    }

    public Integer getIdeEstadoCivil() {
        return ideEstadoCivil;
    }

    public void setIdeEstadoCivil(Integer ideEstadoCivil) {
        this.ideEstadoCivil = ideEstadoCivil;
    }

    public String getNomEstadoCivil() {
        return nomEstadoCivil;
    }

    public void setNomEstadoCivil(String nomEstadoCivil) {
        this.nomEstadoCivil = nomEstadoCivil;
    }

    @XmlTransient
    public Collection<PessoaFisica> getPessoaFisicaCollection() {
        return pessoaFisicaCollection;
    }

    public void setPessoaFisicaCollection(Collection<PessoaFisica> pessoaFisicaCollection) {
        this.pessoaFisicaCollection = pessoaFisicaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideEstadoCivil != null ? ideEstadoCivil.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof EstadoCivil)) {
            return false;
        }
        EstadoCivil other = (EstadoCivil) object;
        if ((this.ideEstadoCivil == null && other.ideEstadoCivil != null) || (this.ideEstadoCivil != null && !this.ideEstadoCivil.equals(other.ideEstadoCivil))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {

    	return String.valueOf(ideEstadoCivil);
    }

}
