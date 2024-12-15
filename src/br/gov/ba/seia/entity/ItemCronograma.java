package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
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

import flexjson.JSON;

/**
 *
 * @author micael.coutinho
 */
@Entity
@Table(name = "item_cronograma")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ItemCronograma.findAll", query = "SELECT i FROM ItemCronograma i"),
    @NamedQuery(name = "ItemCronograma.findByIdeItemCronograma", query = "SELECT i FROM ItemCronograma i WHERE i.ideItemCronograma = :ideItemCronograma"),
    @NamedQuery(name = "ItemCronograma.findByDscItemCronograma", query = "SELECT i FROM ItemCronograma i WHERE i.dscItemCronograma = :dscItemCronograma")})
public class ItemCronograma implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_item_cronograma")
    private Integer ideItemCronograma;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "dsc_item_cronograma")
    private String dscItemCronograma;
    @OneToMany(mappedBy = "ideItemCronograma")
    private Collection<ControleCronograma> controleCronogramaCollection;

    public ItemCronograma() {
    }

    public ItemCronograma(Integer ideItemCronograma) {
        this.ideItemCronograma = ideItemCronograma;
    }

    public ItemCronograma(Integer ideItemCronograma, String dscItemCronograma) {
        this.ideItemCronograma = ideItemCronograma;
        this.dscItemCronograma = dscItemCronograma;
    }

    @JSON(include = false)
    public Integer getIdeItemCronograma() {
        return ideItemCronograma;
    }

    public void setIdeItemCronograma(Integer ideItemCronograma) {
        this.ideItemCronograma = ideItemCronograma;
    }

    public String getDscItemCronograma() {
        return dscItemCronograma;
    }

    public void setDscItemCronograma(String dscItemCronograma) {
        this.dscItemCronograma = dscItemCronograma;
    }

    @XmlTransient
    public Collection<ControleCronograma> getControleCronogramaCollection() {
        return controleCronogramaCollection;
    }

    public void setControleCronogramaCollection(Collection<ControleCronograma> controleCronogramaCollection) {
        this.controleCronogramaCollection = controleCronogramaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideItemCronograma != null ? ideItemCronograma.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof ItemCronograma)) {
            return false;
        }
        ItemCronograma other = (ItemCronograma) object;
        if ((this.ideItemCronograma == null && other.ideItemCronograma != null) || (this.ideItemCronograma != null && !this.ideItemCronograma.equals(other.ideItemCronograma))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.ItemCronograma[ ideItemCronograma=" + ideItemCronograma + " ]";
    }
    
}
