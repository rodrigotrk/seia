package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author luis
 */
@Entity
@Table(name = "tipo_area_abastecimento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoAreaAbastecimento.findAll", query = "SELECT t FROM TipoAreaAbastecimento t"),
    @NamedQuery(name = "TipoAreaAbastecimento.findByIdeTipoAreaAbastecimento", query = "SELECT t FROM TipoAreaAbastecimento t WHERE t.ideTipoAreaAbastecimento = :ideTipoAreaAbastecimento"),
    @NamedQuery(name = "TipoAreaAbastecimento.findByDscTipoAreaAbastecimento", query = "SELECT t FROM TipoAreaAbastecimento t WHERE t.dscTipoAreaAbastecimento = :dscTipoAreaAbastecimento")})
public class TipoAreaAbastecimento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_area_abastecimento")
    private Integer ideTipoAreaAbastecimento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "dsc_tipo_area_abastecimento")
    private String dscTipoAreaAbastecimento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoAreaAbastecimento")
    private Collection<AreaAbastecimentoPostoCombustivel> areaAbastecimentoPostoCombustivelCollection;

    public TipoAreaAbastecimento() {
    }

    public TipoAreaAbastecimento(Integer ideTipoAreaAbastecimento) {
        this.ideTipoAreaAbastecimento = ideTipoAreaAbastecimento;
    }

    public TipoAreaAbastecimento(Integer ideTipoAreaAbastecimento, String dscTipoAreaAbastecimento) {
        this.ideTipoAreaAbastecimento = ideTipoAreaAbastecimento;
        this.dscTipoAreaAbastecimento = dscTipoAreaAbastecimento;
    }

    public Integer getIdeTipoAreaAbastecimento() {
        return ideTipoAreaAbastecimento;
    }

    public void setIdeTipoAreaAbastecimento(Integer ideTipoAreaAbastecimento) {
        this.ideTipoAreaAbastecimento = ideTipoAreaAbastecimento;
    }

    public String getDscTipoAreaAbastecimento() {
        return dscTipoAreaAbastecimento;
    }

    public void setDscTipoAreaAbastecimento(String dscTipoAreaAbastecimento) {
        this.dscTipoAreaAbastecimento = dscTipoAreaAbastecimento;
    }

    @XmlTransient
    public Collection<AreaAbastecimentoPostoCombustivel> getAreaAbastecimentoPostoCombustivelCollection() {
        return areaAbastecimentoPostoCombustivelCollection;
    }

    public void setAreaAbastecimentoPostoCombustivelCollection(Collection<AreaAbastecimentoPostoCombustivel> areaAbastecimentoPostoCombustivelCollection) {
        this.areaAbastecimentoPostoCombustivelCollection = areaAbastecimentoPostoCombustivelCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoAreaAbastecimento != null ? ideTipoAreaAbastecimento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoAreaAbastecimento)) {
            return false;
        }
        TipoAreaAbastecimento other = (TipoAreaAbastecimento) object;
        if ((this.ideTipoAreaAbastecimento == null && other.ideTipoAreaAbastecimento != null) || (this.ideTipoAreaAbastecimento != null && !this.ideTipoAreaAbastecimento.equals(other.ideTipoAreaAbastecimento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TipoAreaAbastecimento[ ideTipoAreaAbastecimento=" + ideTipoAreaAbastecimento + " ]";
    }
    
}
