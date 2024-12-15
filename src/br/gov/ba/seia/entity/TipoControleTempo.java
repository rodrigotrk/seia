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
@Table(name = "tipo_controle_tempo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoControleTempo.findAll", query = "SELECT t FROM TipoControleTempo t"),
    @NamedQuery(name = "TipoControleTempo.findByIdeTipoControleTempo", query = "SELECT t FROM TipoControleTempo t WHERE t.ideTipoControleTempo = :ideTipoControleTempo"),
    @NamedQuery(name = "TipoControleTempo.findByDscTipoControleTempo", query = "SELECT t FROM TipoControleTempo t WHERE t.dscTipoControleTempo = :dscTipoControleTempo")})
public class TipoControleTempo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_controle_tempo")
    private Integer ideTipoControleTempo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "dsc_tipo_controle_tempo")
    private String dscTipoControleTempo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoControleTempo")
    private Collection<ControleResiduoPosto> controleResiduoPostoCollection;

    public TipoControleTempo() {
    }

    public TipoControleTempo(Integer ideTipoControleTempo) {
        this.ideTipoControleTempo = ideTipoControleTempo;
    }

    public TipoControleTempo(Integer ideTipoControleTempo, String dscTipoControleTempo) {
        this.ideTipoControleTempo = ideTipoControleTempo;
        this.dscTipoControleTempo = dscTipoControleTempo;
    }

    public Integer getIdeTipoControleTempo() {
        return ideTipoControleTempo;
    }

    public void setIdeTipoControleTempo(Integer ideTipoControleTempo) {
        this.ideTipoControleTempo = ideTipoControleTempo;
    }

    public String getDscTipoControleTempo() {
        return dscTipoControleTempo;
    }

    public void setDscTipoControleTempo(String dscTipoControleTempo) {
        this.dscTipoControleTempo = dscTipoControleTempo;
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
        hash += (ideTipoControleTempo != null ? ideTipoControleTempo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoControleTempo)) {
            return false;
        }
        TipoControleTempo other = (TipoControleTempo) object;
        if ((this.ideTipoControleTempo == null && other.ideTipoControleTempo != null) || (this.ideTipoControleTempo != null && !this.ideTipoControleTempo.equals(other.ideTipoControleTempo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TipoControleTempo[ ideTipoControleTempo=" + ideTipoControleTempo + " ]";
    }
    
}
