package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.enumerator.TipoRequerimentoEnum;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "tipo_requerimento", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_tipo_requerimento"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoRequerimento.findAll", query = "SELECT t FROM TipoRequerimento t"),
    @NamedQuery(name = "TipoRequerimento.findByIdeTipoRequerimento", query = "SELECT t FROM TipoRequerimento t WHERE t.ideTipoRequerimento = :ideTipoRequerimento"),
    @NamedQuery(name = "TipoRequerimento.findByNomTipoRequerimento", query = "SELECT t FROM TipoRequerimento t WHERE t.nomTipoRequerimento = :nomTipoRequerimento")})
public class TipoRequerimento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_requerimento", nullable = false)
    private Integer ideTipoRequerimento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nom_tipo_requerimento", nullable = false, length = 50)
    private String nomTipoRequerimento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoRequerimento", fetch = FetchType.LAZY)
    private Collection<Requerimento> requerimentoCollection;

    public TipoRequerimento() {
    }

    public TipoRequerimento(Integer ideTipoRequerimento) {
        this.ideTipoRequerimento = ideTipoRequerimento;
    }

    public TipoRequerimento(Integer ideTipoRequerimento, String nomTipoRequerimento) {
        this.ideTipoRequerimento = ideTipoRequerimento;
        this.nomTipoRequerimento = nomTipoRequerimento;
    }
    
    public TipoRequerimento(TipoRequerimentoEnum tipoReqEnum) {
    	this.ideTipoRequerimento = tipoReqEnum.getIde();
    	this.nomTipoRequerimento = tipoReqEnum.getNomeTipoRequerimento();
    }

    public Integer getIdeTipoRequerimento() {
        return ideTipoRequerimento;
    }

    public void setIdeTipoRequerimento(Integer ideTipoRequerimento) {
        this.ideTipoRequerimento = ideTipoRequerimento;
    }

    public String getNomTipoRequerimento() {
        return nomTipoRequerimento;
    }

    public void setNomTipoRequerimento(String nomTipoRequerimento) {
        this.nomTipoRequerimento = nomTipoRequerimento;
    }

    @XmlTransient
    public Collection<Requerimento> getRequerimentoCollection() {
        return requerimentoCollection;
    }

    public void setRequerimentoCollection(Collection<Requerimento> requerimentoCollection) {
        this.requerimentoCollection = requerimentoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoRequerimento != null ? ideTipoRequerimento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoRequerimento)) {
            return false;
        }
        TipoRequerimento other = (TipoRequerimento) object;
        if ((this.ideTipoRequerimento == null && other.ideTipoRequerimento != null) || (this.ideTipoRequerimento != null && !this.ideTipoRequerimento.equals(other.ideTipoRequerimento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ""+ideTipoRequerimento;
    }
    
}
