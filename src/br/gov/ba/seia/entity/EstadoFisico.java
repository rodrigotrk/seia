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
@Table(name = "estado_fisico", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_estado_fisico"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EstadoFisico.findAll", query = "SELECT e FROM EstadoFisico e"),
    @NamedQuery(name = "EstadoFisico.findByIdeEstadoFisico", query = "SELECT e FROM EstadoFisico e WHERE e.ideEstadoFisico = :ideEstadoFisico"),
    @NamedQuery(name = "EstadoFisico.findByNomEstadoFisico", query = "SELECT e FROM EstadoFisico e WHERE e.nomEstadoFisico = :nomEstadoFisico")})
public class EstadoFisico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_estado_fisico", nullable = false)
    private Integer ideEstadoFisico;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "nom_estado_fisico", nullable = false, length = 15)
    private String nomEstadoFisico;

    public EstadoFisico() {
    }

    public EstadoFisico(Integer ideEstadoFisico) {
        this.ideEstadoFisico = ideEstadoFisico;
    }

    public EstadoFisico(Integer ideEstadoFisico, String nomEstadoFisico) {
        this.ideEstadoFisico = ideEstadoFisico;
        this.nomEstadoFisico = nomEstadoFisico;
    }

    public Integer getIdeEstadoFisico() {
        return ideEstadoFisico;
    }

    public void setIdeEstadoFisico(Integer ideEstadoFisico) {
        this.ideEstadoFisico = ideEstadoFisico;
    }

    public String getNomEstadoFisico() {
        return nomEstadoFisico;
    }

    public void setNomEstadoFisico(String nomEstadoFisico) {
        this.nomEstadoFisico = nomEstadoFisico;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideEstadoFisico != null ? ideEstadoFisico.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof EstadoFisico)) {
            return false;
        }
        EstadoFisico other = (EstadoFisico) object;
        if ((this.ideEstadoFisico == null && other.ideEstadoFisico != null) || (this.ideEstadoFisico != null && !this.ideEstadoFisico.equals(other.ideEstadoFisico))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.EstadoFisico[ ideEstadoFisico=" + ideEstadoFisico + " ]";
    }
    
}
