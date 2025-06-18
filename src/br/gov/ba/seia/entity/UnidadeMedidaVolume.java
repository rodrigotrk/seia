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
@Table(name = "unidade_medida_volume", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"cod_unidade_volume"}),
    @UniqueConstraint(columnNames = {"nom_unidade_volume"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnidadeMedidaVolume.findAll", query = "SELECT u FROM UnidadeMedidaVolume u"),
    @NamedQuery(name = "UnidadeMedidaVolume.findByIdeUnidadeMedidaVolume", query = "SELECT u FROM UnidadeMedidaVolume u WHERE u.ideUnidadeMedidaVolume = :ideUnidadeMedidaVolume"),
    @NamedQuery(name = "UnidadeMedidaVolume.findByCodUnidadeVolume", query = "SELECT u FROM UnidadeMedidaVolume u WHERE u.codUnidadeVolume = :codUnidadeVolume"),
    @NamedQuery(name = "UnidadeMedidaVolume.findByNomUnidadeVolume", query = "SELECT u FROM UnidadeMedidaVolume u WHERE u.nomUnidadeVolume = :nomUnidadeVolume")})
public class UnidadeMedidaVolume implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_unidade_medida_volume", nullable = false)
    private Integer ideUnidadeMedidaVolume;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "cod_unidade_volume", nullable = false, length = 3)
    private String codUnidadeVolume;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nom_unidade_volume", nullable = false, length = 50)
    private String nomUnidadeVolume;

    public UnidadeMedidaVolume() {
    }

    public UnidadeMedidaVolume(Integer ideUnidadeMedidaVolume) {
        this.ideUnidadeMedidaVolume = ideUnidadeMedidaVolume;
    }

    public UnidadeMedidaVolume(Integer ideUnidadeMedidaVolume, String codUnidadeVolume, String nomUnidadeVolume) {
        this.ideUnidadeMedidaVolume = ideUnidadeMedidaVolume;
        this.codUnidadeVolume = codUnidadeVolume;
        this.nomUnidadeVolume = nomUnidadeVolume;
    }

    public Integer getIdeUnidadeMedidaVolume() {
        return ideUnidadeMedidaVolume;
    }

    public void setIdeUnidadeMedidaVolume(Integer ideUnidadeMedidaVolume) {
        this.ideUnidadeMedidaVolume = ideUnidadeMedidaVolume;
    }

    public String getCodUnidadeVolume() {
        return codUnidadeVolume;
    }

    public void setCodUnidadeVolume(String codUnidadeVolume) {
        this.codUnidadeVolume = codUnidadeVolume;
    }

    public String getNomUnidadeVolume() {
        return nomUnidadeVolume;
    }

    public void setNomUnidadeVolume(String nomUnidadeVolume) {
        this.nomUnidadeVolume = nomUnidadeVolume;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideUnidadeMedidaVolume != null ? ideUnidadeMedidaVolume.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof UnidadeMedidaVolume)) {
            return false;
        }
        UnidadeMedidaVolume other = (UnidadeMedidaVolume) object;
        if ((this.ideUnidadeMedidaVolume == null && other.ideUnidadeMedidaVolume != null) || (this.ideUnidadeMedidaVolume != null && !this.ideUnidadeMedidaVolume.equals(other.ideUnidadeMedidaVolume))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.UnidadeMedidaVolume[ ideUnidadeMedidaVolume=" + ideUnidadeMedidaVolume + " ]";
    }
    
}
