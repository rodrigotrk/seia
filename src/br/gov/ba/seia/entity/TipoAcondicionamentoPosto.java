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
@Table(name = "tipo_acondicionamento_posto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoAcondicionamentoPosto.findAll", query = "SELECT t FROM TipoAcondicionamentoPosto t"),
    @NamedQuery(name = "TipoAcondicionamentoPosto.findByIdeTipoAcondicinamentoPosto", query = "SELECT t FROM TipoAcondicionamentoPosto t WHERE t.ideTipoAcondicinamentoPosto = :ideTipoAcondicinamentoPosto"),
    @NamedQuery(name = "TipoAcondicionamentoPosto.findByDscTipoAcondicionamentoPosto", query = "SELECT t FROM TipoAcondicionamentoPosto t WHERE t.dscTipoAcondicionamentoPosto = :dscTipoAcondicionamentoPosto")})
public class TipoAcondicionamentoPosto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_acondicinamento_posto")
    private Integer ideTipoAcondicinamentoPosto;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "dsc_tipo_acondicionamento_posto")
    private String dscTipoAcondicionamentoPosto;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoAcondicionamentoPosto")
    private Collection<ControleResiduoPosto> controleResiduoPostoCollection;

    public TipoAcondicionamentoPosto() {
    }

    public TipoAcondicionamentoPosto(Integer ideTipoAcondicinamentoPosto) {
        this.ideTipoAcondicinamentoPosto = ideTipoAcondicinamentoPosto;
    }

    public TipoAcondicionamentoPosto(Integer ideTipoAcondicinamentoPosto, String dscTipoAcondicionamentoPosto) {
        this.ideTipoAcondicinamentoPosto = ideTipoAcondicinamentoPosto;
        this.dscTipoAcondicionamentoPosto = dscTipoAcondicionamentoPosto;
    }

    public Integer getIdeTipoAcondicinamentoPosto() {
        return ideTipoAcondicinamentoPosto;
    }

    public void setIdeTipoAcondicinamentoPosto(Integer ideTipoAcondicinamentoPosto) {
        this.ideTipoAcondicinamentoPosto = ideTipoAcondicinamentoPosto;
    }

    public String getDscTipoAcondicionamentoPosto() {
        return dscTipoAcondicionamentoPosto;
    }

    public void setDscTipoAcondicionamentoPosto(String dscTipoAcondicionamentoPosto) {
        this.dscTipoAcondicionamentoPosto = dscTipoAcondicionamentoPosto;
    }

    @XmlTransient
    public Collection<ControleResiduoPosto> getControleResiduoPostoCollection() {
        return controleResiduoPostoCollection;
    }

    public void setControleResiduoPostoCollection(Collection<ControleResiduoPosto> controleResiduoPostoCollection) {
        this.controleResiduoPostoCollection = controleResiduoPostoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoAcondicinamentoPosto != null ? ideTipoAcondicinamentoPosto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoAcondicionamentoPosto)) {
            return false;
        }
        TipoAcondicionamentoPosto other = (TipoAcondicionamentoPosto) object;
        if ((this.ideTipoAcondicinamentoPosto == null && other.ideTipoAcondicinamentoPosto != null) || (this.ideTipoAcondicinamentoPosto != null && !this.ideTipoAcondicinamentoPosto.equals(other.ideTipoAcondicinamentoPosto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TipoAcondicionamentoPosto[ ideTipoAcondicinamentoPosto=" + ideTipoAcondicinamentoPosto + " ]";
    }
    
}
