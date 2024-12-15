package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.ArrayList;
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

import br.gov.ba.seia.util.Util;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "tipo_imovel_requerimento", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_tipo_imovel_requerimento"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoImovelRequerimento.findAll", query = "SELECT t FROM TipoImovelRequerimento t"),
    @NamedQuery(name = "TipoImovelRequerimento.findByIdeTipoImovelRequerimento", query = "SELECT t FROM TipoImovelRequerimento t WHERE t.ideTipoImovelRequerimento = :ideTipoImovelRequerimento"),
    @NamedQuery(name = "TipoImovelRequerimento.findByNomTipoImovelRequerimento", query = "SELECT t FROM TipoImovelRequerimento t WHERE t.nomTipoImovelRequerimento = :nomTipoImovelRequerimento")})
public class TipoImovelRequerimento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_imovel_requerimento", nullable = false)
    private Integer ideTipoImovelRequerimento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "nom_tipo_imovel_requerimento", nullable = false, length = 50)
    private String nomTipoImovelRequerimento;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoImovelRequerimento", fetch = FetchType.LAZY)
    private Collection<RequerimentoImovel> requerimentoImovelCollection;

    public TipoImovelRequerimento() {
    }

    public TipoImovelRequerimento(Integer ideTipoImovelRequerimento) {
        this.ideTipoImovelRequerimento = ideTipoImovelRequerimento;
    }

    public TipoImovelRequerimento(Integer ideTipoImovelRequerimento, String nomTipoImovelRequerimento) {
        this.ideTipoImovelRequerimento = ideTipoImovelRequerimento;
        this.nomTipoImovelRequerimento = nomTipoImovelRequerimento;
    }

    public Integer getIdeTipoImovelRequerimento() {
        return ideTipoImovelRequerimento;
    }

    public void setIdeTipoImovelRequerimento(Integer ideTipoImovelRequerimento) {
        this.ideTipoImovelRequerimento = ideTipoImovelRequerimento;
    }

    public String getNomTipoImovelRequerimento() {
        return nomTipoImovelRequerimento;
    }

    public void setNomTipoImovelRequerimento(String nomTipoImovelRequerimento) {
        this.nomTipoImovelRequerimento = nomTipoImovelRequerimento;
    }

    @XmlTransient
    public Collection<RequerimentoImovel> getRequerimentoImovelCollection() {
        return Util.isNull(requerimentoImovelCollection) ? requerimentoImovelCollection = new ArrayList<RequerimentoImovel>() : requerimentoImovelCollection ;
    }

    public void setRequerimentoImovelCollection(Collection<RequerimentoImovel> requerimentoImovelCollection) {
        this.requerimentoImovelCollection = requerimentoImovelCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoImovelRequerimento != null ? ideTipoImovelRequerimento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoImovelRequerimento)) {
            return false;
        }
        TipoImovelRequerimento other = (TipoImovelRequerimento) object;
        if ((this.ideTipoImovelRequerimento == null && other.ideTipoImovelRequerimento != null) || (this.ideTipoImovelRequerimento != null && !this.ideTipoImovelRequerimento.equals(other.ideTipoImovelRequerimento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ""+ideTipoImovelRequerimento;
    }
    
}
