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
 * @author micael.coutinho
 */
@Entity
@Table(name = "modulo_fiscal")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ModuloFiscal.findAll", query = "SELECT m FROM ModuloFiscal m"),
    @NamedQuery(name = "ModuloFiscal.findByIdeModuloFiscal", query = "SELECT m FROM ModuloFiscal m WHERE m.ideModuloFiscal = :ideModuloFiscal"),
    @NamedQuery(name = "ModuloFiscal.findByNomMunicipio", query = "SELECT m FROM ModuloFiscal m WHERE m.nomMunicipio = :nomMunicipio"),
    @NamedQuery(name = "ModuloFiscal.findByCodIbgeMunicipio", query = "SELECT m FROM ModuloFiscal m WHERE m.codIbgeMunicipio = :codIbgeMunicipio"),
    @NamedQuery(name = "ModuloFiscal.findByModFiscal", query = "SELECT m FROM ModuloFiscal m WHERE m.modFiscal = :modFiscal")})
public class ModuloFiscal implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_modulo_fiscal")
    private Integer ideModuloFiscal;
    @Size(max = 100)
    @Column(name = "nom_municipio")
    private String nomMunicipio;
    @Column(name = "cod_ibge_municipio")
    private Double codIbgeMunicipio;
    @Column(name = "mod_fiscal")
    private Integer modFiscal;

    public ModuloFiscal() {
    }

    public ModuloFiscal(Integer ideModuloFiscal) {
        this.ideModuloFiscal = ideModuloFiscal;
    }

    public Integer getIdeModuloFiscal() {
        return ideModuloFiscal;
    }

    public void setIdeModuloFiscal(Integer ideModuloFiscal) {
        this.ideModuloFiscal = ideModuloFiscal;
    }

    public String getNomMunicipio() {
        return nomMunicipio;
    }

    public void setNomMunicipio(String nomMunicipio) {
        this.nomMunicipio = nomMunicipio;
    }

    public Double getCodIbgeMunicipio() {
        return codIbgeMunicipio;
    }

    public void setCodIbgeMunicipio(Double codIbgeMunicipio) {
        this.codIbgeMunicipio = codIbgeMunicipio;
    }

    public Integer getModFiscal() {
        return modFiscal;
    }

    public void setModFiscal(Integer modFiscal) {
        this.modFiscal = modFiscal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideModuloFiscal != null ? ideModuloFiscal.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ModuloFiscal)) {
            return false;
        }
        ModuloFiscal other = (ModuloFiscal) object;
        if ((this.ideModuloFiscal == null && other.ideModuloFiscal != null) || (this.ideModuloFiscal != null && !this.ideModuloFiscal.equals(other.ideModuloFiscal))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + ideModuloFiscal;
    }
    
}
