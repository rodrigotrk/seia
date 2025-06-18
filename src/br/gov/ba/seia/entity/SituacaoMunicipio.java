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

@Entity
@Table(name = "situacao_municipio", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ide_situacao_municipio"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SituacaoMunicipio.findAll", query = "SELECT s FROM SituacaoMunicipio s"),
    @NamedQuery(name = "SituacaoMunicipio.findByIdeSituacaoMunicipio", query = "SELECT s FROM SituacaoMunicipio s WHERE s.ideSituacaoMunicipio = :ideSituacaoMunicipio"),
    @NamedQuery(name = "SituacaoMunicipio.findByDscSituacaoMunicipio", query = "SELECT s FROM SituacaoMunicipio s WHERE s.dscSituacaoMunicipio = :dscSituacaoMunicipio")})
public class SituacaoMunicipio implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_situacao_municipio", nullable = false)
    private Integer ideSituacaoMunicipio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "dsc_situacao_municipio", nullable = false, length = 100)
    private String dscSituacaoMunicipio;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideSituacaoMunicipio", fetch = FetchType.LAZY)
    private Collection<Municipio> municipioCollection;

    public SituacaoMunicipio() {
    }

    public SituacaoMunicipio(Integer ideSituacaoMunicipio) {
        this.ideSituacaoMunicipio = ideSituacaoMunicipio;
    }

    public SituacaoMunicipio(Integer ideSituacaoMunicipio, String dscSituacaoMunicipio) {
        this.ideSituacaoMunicipio = ideSituacaoMunicipio;
        this.dscSituacaoMunicipio = dscSituacaoMunicipio;
    }

    public Integer getIdeSituacaoMunicipio() {
        return ideSituacaoMunicipio;
    }

    public void setIdeSituacaoMunicipio(Integer ideSituacaoMunicipio) {
        this.ideSituacaoMunicipio = ideSituacaoMunicipio;
    }

    public String getDscSituacaoMunicipio() {
        return dscSituacaoMunicipio;
    }

    public void setDscSituacaoMunicipio(String dscSituacaoMunicipio) {
        this.dscSituacaoMunicipio = dscSituacaoMunicipio;
    }

    @XmlTransient
    public Collection<Municipio> getMunicipioCollection() {
        return municipioCollection;
    }

    public void setMunicipioCollection(Collection<Municipio> municipioCollection) {
        this.municipioCollection = municipioCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSituacaoMunicipio != null ? ideSituacaoMunicipio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof SituacaoMunicipio)) {
            return false;
        }
        SituacaoMunicipio other = (SituacaoMunicipio) object;
        if ((this.ideSituacaoMunicipio == null && other.ideSituacaoMunicipio != null) || (this.ideSituacaoMunicipio != null && !this.ideSituacaoMunicipio.equals(other.ideSituacaoMunicipio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.SituacaoMunicipio[ ideSituacaoMunicipio=" + ideSituacaoMunicipio + " ]";
    }
    
}
