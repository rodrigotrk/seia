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
@Table(name = "periodicidade", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_periodicidade"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Periodicidade.findAll", query = "SELECT p FROM Periodicidade p"),
    @NamedQuery(name = "Periodicidade.findByIdePeriodicidade", query = "SELECT p FROM Periodicidade p WHERE p.idePeriodicidade = :idePeriodicidade"),
    @NamedQuery(name = "Periodicidade.findByNomPeriodicidade", query = "SELECT p FROM Periodicidade p WHERE p.nomPeriodicidade = :nomPeriodicidade")})
public class Periodicidade implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_periodicidade", nullable = false)
    private Integer idePeriodicidade;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "nom_periodicidade", nullable = false, length = 10)
    private String nomPeriodicidade;

    public Periodicidade() {
    }

    public Periodicidade(Integer idePeriodicidade) {
        this.idePeriodicidade = idePeriodicidade;
    }

    public Periodicidade(Integer idePeriodicidade, String nomPeriodicidade) {
        this.idePeriodicidade = idePeriodicidade;
        this.nomPeriodicidade = nomPeriodicidade;
    }

    public Integer getIdePeriodicidade() {
        return idePeriodicidade;
    }

    public void setIdePeriodicidade(Integer idePeriodicidade) {
        this.idePeriodicidade = idePeriodicidade;
    }

    public String getNomPeriodicidade() {
        return nomPeriodicidade;
    }

    public void setNomPeriodicidade(String nomPeriodicidade) {
        this.nomPeriodicidade = nomPeriodicidade;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePeriodicidade != null ? idePeriodicidade.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Periodicidade)) {
            return false;
        }
        Periodicidade other = (Periodicidade) object;
        if ((this.idePeriodicidade == null && other.idePeriodicidade != null) || (this.idePeriodicidade != null && !this.idePeriodicidade.equals(other.idePeriodicidade))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.Periodicidade[ idePeriodicidade=" + idePeriodicidade + " ]";
    }
    
}
