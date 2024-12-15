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
@Table(name = "unidade_medida_tempo", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_unidade_medida_tempo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnidadeMedidaTempo.findAll", query = "SELECT u FROM UnidadeMedidaTempo u"),
    @NamedQuery(name = "UnidadeMedidaTempo.findByIdeUnidadeMedidaTempo", query = "SELECT u FROM UnidadeMedidaTempo u WHERE u.ideUnidadeMedidaTempo = :ideUnidadeMedidaTempo"),
    @NamedQuery(name = "UnidadeMedidaTempo.findByNomUnidadeMedidaTempo", query = "SELECT u FROM UnidadeMedidaTempo u WHERE u.nomUnidadeMedidaTempo = :nomUnidadeMedidaTempo")})
public class UnidadeMedidaTempo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_unidade_medida_tempo", nullable = false)
    private Integer ideUnidadeMedidaTempo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "nom_unidade_medida_tempo", nullable = false, length = 20)
    private String nomUnidadeMedidaTempo;

    public UnidadeMedidaTempo() {
    }

    public UnidadeMedidaTempo(Integer ideUnidadeMedidaTempo) {
        this.ideUnidadeMedidaTempo = ideUnidadeMedidaTempo;
    }

    public UnidadeMedidaTempo(Integer ideUnidadeMedidaTempo, String nomUnidadeMedidaTempo) {
        this.ideUnidadeMedidaTempo = ideUnidadeMedidaTempo;
        this.nomUnidadeMedidaTempo = nomUnidadeMedidaTempo;
    }

    public Integer getIdeUnidadeMedidaTempo() {
        return ideUnidadeMedidaTempo;
    }

    public void setIdeUnidadeMedidaTempo(Integer ideUnidadeMedidaTempo) {
        this.ideUnidadeMedidaTempo = ideUnidadeMedidaTempo;
    }

    public String getNomUnidadeMedidaTempo() {
        return nomUnidadeMedidaTempo;
    }

    public void setNomUnidadeMedidaTempo(String nomUnidadeMedidaTempo) {
        this.nomUnidadeMedidaTempo = nomUnidadeMedidaTempo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideUnidadeMedidaTempo != null ? ideUnidadeMedidaTempo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof UnidadeMedidaTempo)) {
            return false;
        }
        UnidadeMedidaTempo other = (UnidadeMedidaTempo) object;
        if ((this.ideUnidadeMedidaTempo == null && other.ideUnidadeMedidaTempo != null) || (this.ideUnidadeMedidaTempo != null && !this.ideUnidadeMedidaTempo.equals(other.ideUnidadeMedidaTempo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.UnidadeMedidaTempo[ ideUnidadeMedidaTempo=" + ideUnidadeMedidaTempo + " ]";
    }
    
}
