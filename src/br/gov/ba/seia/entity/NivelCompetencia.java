package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "nivel_competencia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NivelCompetencia.findAll", query = "SELECT n FROM NivelCompetencia n"),
    @NamedQuery(name = "NivelCompetencia.findByIdeNivelCompetencia", query = "SELECT n FROM NivelCompetencia n WHERE n.ideNivelCompetencia = :ideNivelCompetencia"),
    @NamedQuery(name = "NivelCompetencia.findByNumNivelCompetencia", query = "SELECT n FROM NivelCompetencia n WHERE n.numNivelCompetencia = :numNivelCompetencia")})
public class NivelCompetencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_nivel_competencia", nullable = false)
    private Integer ideNivelCompetencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "num_nivel_competencia", nullable = false)
    private int numNivelCompetencia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nivelCompetencia", fetch = FetchType.LAZY)
    private Collection<PorteCompetencia> porteCompetenciaCollection;
    @JoinColumn(name = "ide_tipo_competencia", referencedColumnName = "ide_tipo_competencia", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TipoCompetencia ideTipoCompetencia;
    @OneToMany(mappedBy = "ideNivelCompetencia", fetch = FetchType.LAZY)
    private Collection<Orgao> orgaoCollection;

    public NivelCompetencia() {
    }

    public NivelCompetencia(Integer ideNivelCompetencia) {
        this.ideNivelCompetencia = ideNivelCompetencia;
    }

    public NivelCompetencia(Integer ideNivelCompetencia, int numNivelCompetencia) {
        this.ideNivelCompetencia = ideNivelCompetencia;
        this.numNivelCompetencia = numNivelCompetencia;
    }

    public Integer getIdeNivelCompetencia() {
        return ideNivelCompetencia;
    }

    public void setIdeNivelCompetencia(Integer ideNivelCompetencia) {
        this.ideNivelCompetencia = ideNivelCompetencia;
    }

    public int getNumNivelCompetencia() {
        return numNivelCompetencia;
    }
    public String getNumNivelCompetenciaString() {
        return String.valueOf(numNivelCompetencia);
    }

    public void setNumNivelCompetencia(int numNivelCompetencia) {
        this.numNivelCompetencia = numNivelCompetencia;
    }

    @XmlTransient
    public Collection<PorteCompetencia> getPorteCompetenciaCollection() {
        return porteCompetenciaCollection;
    }

    public void setPorteCompetenciaCollection(Collection<PorteCompetencia> porteCompetenciaCollection) {
        this.porteCompetenciaCollection = porteCompetenciaCollection;
    }

    public TipoCompetencia getIdeTipoCompetencia() {
        return ideTipoCompetencia;
    }

    public void setIdeTipoCompetencia(TipoCompetencia ideTipoCompetencia) {
        this.ideTipoCompetencia = ideTipoCompetencia;
    }

    @XmlTransient
    public Collection<Orgao> getOrgaoCollection() {
        return orgaoCollection;
    }

    public void setOrgaoCollection(Collection<Orgao> orgaoCollection) {
        this.orgaoCollection = orgaoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideNivelCompetencia != null ? ideNivelCompetencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof NivelCompetencia)) {
            return false;
        }
        NivelCompetencia other = (NivelCompetencia) object;
        if ((this.ideNivelCompetencia == null && other.ideNivelCompetencia != null) || (this.ideNivelCompetencia != null && !this.ideNivelCompetencia.equals(other.ideNivelCompetencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {

    	 return String.valueOf(ideNivelCompetencia);
    }
}
