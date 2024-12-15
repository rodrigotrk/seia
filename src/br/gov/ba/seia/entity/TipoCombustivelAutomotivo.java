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
@Table(name = "tipo_combustivel_automotivo", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_tipo_combustivel"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoCombustivelAutomotivo.findAll", query = "SELECT t FROM TipoCombustivelAutomotivo t"),
    @NamedQuery(name = "TipoCombustivelAutomotivo.findByIdeTipoCombustivel", query = "SELECT t FROM TipoCombustivelAutomotivo t WHERE t.ideTipoCombustivel = :ideTipoCombustivel"),
    @NamedQuery(name = "TipoCombustivelAutomotivo.findByNomTipoCombustivel", query = "SELECT t FROM TipoCombustivelAutomotivo t WHERE t.nomTipoCombustivel = :nomTipoCombustivel")})
public class TipoCombustivelAutomotivo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_combustivel", nullable = false)
    private Integer ideTipoCombustivel;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "nom_tipo_combustivel", nullable = false, length = 30)
    private String nomTipoCombustivel;

    public TipoCombustivelAutomotivo() {
    }

    public TipoCombustivelAutomotivo(Integer ideTipoCombustivel) {
        this.ideTipoCombustivel = ideTipoCombustivel;
    }

    public TipoCombustivelAutomotivo(Integer ideTipoCombustivel, String nomTipoCombustivel) {
        this.ideTipoCombustivel = ideTipoCombustivel;
        this.nomTipoCombustivel = nomTipoCombustivel;
    }

    public Integer getIdeTipoCombustivel() {
        return ideTipoCombustivel;
    }

    public void setIdeTipoCombustivel(Integer ideTipoCombustivel) {
        this.ideTipoCombustivel = ideTipoCombustivel;
    }

    public String getNomTipoCombustivel() {
        return nomTipoCombustivel;
    }

    public void setNomTipoCombustivel(String nomTipoCombustivel) {
        this.nomTipoCombustivel = nomTipoCombustivel;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoCombustivel != null ? ideTipoCombustivel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoCombustivelAutomotivo)) {
            return false;
        }
        TipoCombustivelAutomotivo other = (TipoCombustivelAutomotivo) object;
        if ((this.ideTipoCombustivel == null && other.ideTipoCombustivel != null) || (this.ideTipoCombustivel != null && !this.ideTipoCombustivel.equals(other.ideTipoCombustivel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoCombustivelAutomotivo[ ideTipoCombustivel=" + ideTipoCombustivel + " ]";
    }
    
}
