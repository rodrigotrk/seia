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
@Table(name = "tipo_produto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoProduto.findAll", query = "SELECT t FROM TipoProduto t"),
    @NamedQuery(name = "TipoProduto.findByIdeTipoProduto", query = "SELECT t FROM TipoProduto t WHERE t.ideTipoProduto = :ideTipoProduto"),
    @NamedQuery(name = "TipoProduto.findByDscTipoProduto", query = "SELECT t FROM TipoProduto t WHERE t.dscTipoProduto = :dscTipoProduto")})
public class TipoProduto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_produto")
    private Integer ideTipoProduto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "dsc_tipo_produto")
    private String dscTipoProduto;

    public TipoProduto() {
    }

    public TipoProduto(Integer ideTipoProduto) {
        this.ideTipoProduto = ideTipoProduto;
    }

    public TipoProduto(Integer ideTipoProduto, String dscTipoProduto) {
        this.ideTipoProduto = ideTipoProduto;
        this.dscTipoProduto = dscTipoProduto;
    }

    public Integer getIdeTipoProduto() {
        return ideTipoProduto;
    }

    public void setIdeTipoProduto(Integer ideTipoProduto) {
        this.ideTipoProduto = ideTipoProduto;
    }

    public String getDscTipoProduto() {
        return dscTipoProduto;
    }

    public void setDscTipoProduto(String dscTipoProduto) {
        this.dscTipoProduto = dscTipoProduto;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoProduto != null ? ideTipoProduto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoProduto)) {
            return false;
        }
        TipoProduto other = (TipoProduto) object;
        if ((this.ideTipoProduto == null && other.ideTipoProduto != null) || (this.ideTipoProduto != null && !this.ideTipoProduto.equals(other.ideTipoProduto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TipoProduto[ ideTipoProduto=" + ideTipoProduto + " ]";
    }
    
}
