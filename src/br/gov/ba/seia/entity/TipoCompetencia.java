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

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "tipo_competencia", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_tipo_competencia"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoCompetencia.findAll", query = "SELECT t FROM TipoCompetencia t"),
    @NamedQuery(name = "TipoCompetencia.findByIdeTipoCompetencia", query = "SELECT t FROM TipoCompetencia t WHERE t.ideTipoCompetencia = :ideTipoCompetencia"),
    @NamedQuery(name = "TipoCompetencia.findByNomTipoCompetencia", query = "SELECT t FROM TipoCompetencia t WHERE t.nomTipoCompetencia = :nomTipoCompetencia")})
public class TipoCompetencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_competencia", nullable = false)
    private Integer ideTipoCompetencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "nom_tipo_competencia", nullable = false, length = 60)
    private String nomTipoCompetencia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoCompetencia", fetch = FetchType.LAZY)
    private Collection<NivelCompetencia> nivelCompetenciaCollection;

    public TipoCompetencia() {
    }

    public TipoCompetencia(Integer ideTipoCompetencia) {
        this.ideTipoCompetencia = ideTipoCompetencia;
    }

    public TipoCompetencia(Integer ideTipoCompetencia, String nomTipoCompetencia) {
        this.ideTipoCompetencia = ideTipoCompetencia;
        this.nomTipoCompetencia = nomTipoCompetencia;
    }

    public Integer getIdeTipoCompetencia() {
        return ideTipoCompetencia;
    }

    public void setIdeTipoCompetencia(Integer ideTipoCompetencia) {
        this.ideTipoCompetencia = ideTipoCompetencia;
    }

    public String getNomTipoCompetencia() {
        return nomTipoCompetencia;
    }

    public void setNomTipoCompetencia(String nomTipoCompetencia) {
        this.nomTipoCompetencia = nomTipoCompetencia;
    }

    @XmlTransient
    public Collection<NivelCompetencia> getNivelCompetenciaCollection() {
        return nivelCompetenciaCollection;
    }

    public void setNivelCompetenciaCollection(Collection<NivelCompetencia> nivelCompetenciaCollection) {
        this.nivelCompetenciaCollection = nivelCompetenciaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoCompetencia != null ? ideTipoCompetencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoCompetencia)) {
            return false;
        }
        TipoCompetencia other = (TipoCompetencia) object;
        if ((this.ideTipoCompetencia == null && other.ideTipoCompetencia != null) || (this.ideTipoCompetencia != null && !this.ideTipoCompetencia.equals(other.ideTipoCompetencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoCompetencia[ ideTipoCompetencia=" + ideTipoCompetencia + " ]";
    }
    
}
