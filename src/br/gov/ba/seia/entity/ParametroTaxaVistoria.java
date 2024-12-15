package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "parametro_taxa_vistoria", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"val_area_minima", "val_area_maximo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParametroTaxaVistoria.findAll", query = "SELECT p FROM ParametroTaxaVistoria p"),
    @NamedQuery(name = "ParametroTaxaVistoria.findByIdeParametroTaxaVistoria", query = "SELECT p FROM ParametroTaxaVistoria p WHERE p.ideParametroTaxaVistoria = :ideParametroTaxaVistoria"),
    @NamedQuery(name = "ParametroTaxaVistoria.findByValAreaMinima", query = "SELECT p FROM ParametroTaxaVistoria p WHERE p.valAreaMinima = :valAreaMinima"),
    @NamedQuery(name = "ParametroTaxaVistoria.findByValAreaMaximo", query = "SELECT p FROM ParametroTaxaVistoria p WHERE p.valAreaMaximo = :valAreaMaximo"),
    @NamedQuery(name = "ParametroTaxaVistoria.findByValTaxaVistoria", query = "SELECT p FROM ParametroTaxaVistoria p WHERE p.valTaxaVistoria = :valTaxaVistoria")})
public class ParametroTaxaVistoria implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_parametro_taxa_vistoria", nullable = false)
    private Integer ideParametroTaxaVistoria;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "val_area_minima", nullable = false, precision = 10, scale = 4)
    private BigDecimal valAreaMinima;
    @Basic(optional = false)
    @NotNull
    @Column(name = "val_area_maximo", nullable = false, precision = 10, scale = 2)
    private BigDecimal valAreaMaximo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "val_taxa_vistoria", nullable = false, precision = 10, scale = 2)
    private BigDecimal valTaxaVistoria;

    public ParametroTaxaVistoria() {
    }

    public ParametroTaxaVistoria(Integer ideParametroTaxaVistoria) {
        this.ideParametroTaxaVistoria = ideParametroTaxaVistoria;
    }

    public ParametroTaxaVistoria(Integer ideParametroTaxaVistoria, BigDecimal valAreaMinima, BigDecimal valAreaMaximo, BigDecimal valTaxaVistoria) {
        this.ideParametroTaxaVistoria = ideParametroTaxaVistoria;
        this.valAreaMinima = valAreaMinima;
        this.valAreaMaximo = valAreaMaximo;
        this.valTaxaVistoria = valTaxaVistoria;
    }

    public Integer getIdeParametroTaxaVistoria() {
        return ideParametroTaxaVistoria;
    }

    public void setIdeParametroTaxaVistoria(Integer ideParametroTaxaVistoria) {
        this.ideParametroTaxaVistoria = ideParametroTaxaVistoria;
    }

    public BigDecimal getValAreaMinima() {
        return valAreaMinima;
    }

    public void setValAreaMinima(BigDecimal valAreaMinima) {
        this.valAreaMinima = valAreaMinima;
    }

    public BigDecimal getValAreaMaximo() {
        return valAreaMaximo;
    }

    public void setValAreaMaximo(BigDecimal valAreaMaximo) {
        this.valAreaMaximo = valAreaMaximo;
    }

    public BigDecimal getValTaxaVistoria() {
        return valTaxaVistoria;
    }

    public void setValTaxaVistoria(BigDecimal valTaxaVistoria) {
        this.valTaxaVistoria = valTaxaVistoria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideParametroTaxaVistoria != null ? ideParametroTaxaVistoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ParametroTaxaVistoria)) {
            return false;
        }
        ParametroTaxaVistoria other = (ParametroTaxaVistoria) object;
        if ((this.ideParametroTaxaVistoria == null && other.ideParametroTaxaVistoria != null) || (this.ideParametroTaxaVistoria != null && !this.ideParametroTaxaVistoria.equals(other.ideParametroTaxaVistoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.ParametroTaxaVistoria[ ideParametroTaxaVistoria=" + ideParametroTaxaVistoria + " ]";
    }
    
}
