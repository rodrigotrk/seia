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
 * @author luis
 */
@Entity
@Table(name = "tipo_permeabilidade")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoPermeabilidade.findAll", query = "SELECT t FROM TipoPermeabilidade t"),
    @NamedQuery(name = "TipoPermeabilidade.findByIdeTipoPermeabilidade", query = "SELECT t FROM TipoPermeabilidade t WHERE t.ideTipoPermeabilidade = :ideTipoPermeabilidade"),
    @NamedQuery(name = "TipoPermeabilidade.findByDscTipoPermeabilidade", query = "SELECT t FROM TipoPermeabilidade t WHERE t.dscTipoPermeabilidade = :dscTipoPermeabilidade")})
public class TipoPermeabilidade implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_permeabilidade")
    private Integer ideTipoPermeabilidade;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "dsc_tipo_permeabilidade")
    private String dscTipoPermeabilidade;

    public TipoPermeabilidade() {
    }

    public TipoPermeabilidade(Integer ideTipoPermeabilidade) {
        this.ideTipoPermeabilidade = ideTipoPermeabilidade;
    }

    public TipoPermeabilidade(Integer ideTipoPermeabilidade, String dscTipoPermeabilidade) {
        this.ideTipoPermeabilidade = ideTipoPermeabilidade;
        this.dscTipoPermeabilidade = dscTipoPermeabilidade;
    }

    public Integer getIdeTipoPermeabilidade() {
        return ideTipoPermeabilidade;
    }

    public void setIdeTipoPermeabilidade(Integer ideTipoPermeabilidade) {
        this.ideTipoPermeabilidade = ideTipoPermeabilidade;
    }

    public String getDscTipoPermeabilidade() {
        return dscTipoPermeabilidade;
    }

    public void setDscTipoPermeabilidade(String dscTipoPermeabilidade) {
        this.dscTipoPermeabilidade = dscTipoPermeabilidade;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoPermeabilidade != null ? ideTipoPermeabilidade.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoPermeabilidade)) {
            return false;
        }
        TipoPermeabilidade other = (TipoPermeabilidade) object;
        if ((this.ideTipoPermeabilidade == null && other.ideTipoPermeabilidade != null) || (this.ideTipoPermeabilidade != null && !this.ideTipoPermeabilidade.equals(other.ideTipoPermeabilidade))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TipoPermeabilidade[ ideTipoPermeabilidade=" + ideTipoPermeabilidade + " ]";
    }
    
}
