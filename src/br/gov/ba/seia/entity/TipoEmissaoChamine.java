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
@Table(name = "tipo_emissao_chamine", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_tipo_emissao_chamine"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoEmissaoChamine.findAll", query = "SELECT t FROM TipoEmissaoChamine t"),
    @NamedQuery(name = "TipoEmissaoChamine.findByIdeTipoEmissaoChamine", query = "SELECT t FROM TipoEmissaoChamine t WHERE t.ideTipoEmissaoChamine = :ideTipoEmissaoChamine"),
    @NamedQuery(name = "TipoEmissaoChamine.findByNomTipoEmissaoChamine", query = "SELECT t FROM TipoEmissaoChamine t WHERE t.nomTipoEmissaoChamine = :nomTipoEmissaoChamine")})
public class TipoEmissaoChamine implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_emissao_chamine", nullable = false)
    private Integer ideTipoEmissaoChamine;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "nom_tipo_emissao_chamine", nullable = false, length = 20)
    private String nomTipoEmissaoChamine;

    public TipoEmissaoChamine() {
    }

    public TipoEmissaoChamine(Integer ideTipoEmissaoChamine) {
        this.ideTipoEmissaoChamine = ideTipoEmissaoChamine;
    }

    public TipoEmissaoChamine(Integer ideTipoEmissaoChamine, String nomTipoEmissaoChamine) {
        this.ideTipoEmissaoChamine = ideTipoEmissaoChamine;
        this.nomTipoEmissaoChamine = nomTipoEmissaoChamine;
    }

    public Integer getIdeTipoEmissaoChamine() {
        return ideTipoEmissaoChamine;
    }

    public void setIdeTipoEmissaoChamine(Integer ideTipoEmissaoChamine) {
        this.ideTipoEmissaoChamine = ideTipoEmissaoChamine;
    }

    public String getNomTipoEmissaoChamine() {
        return nomTipoEmissaoChamine;
    }

    public void setNomTipoEmissaoChamine(String nomTipoEmissaoChamine) {
        this.nomTipoEmissaoChamine = nomTipoEmissaoChamine;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoEmissaoChamine != null ? ideTipoEmissaoChamine.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoEmissaoChamine)) {
            return false;
        }
        TipoEmissaoChamine other = (TipoEmissaoChamine) object;
        if ((this.ideTipoEmissaoChamine == null && other.ideTipoEmissaoChamine != null) || (this.ideTipoEmissaoChamine != null && !this.ideTipoEmissaoChamine.equals(other.ideTipoEmissaoChamine))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoEmissaoChamine[ ideTipoEmissaoChamine=" + ideTipoEmissaoChamine + " ]";
    }
    
}
