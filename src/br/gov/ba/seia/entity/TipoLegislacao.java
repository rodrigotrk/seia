package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "tipo_legislacao", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ide_tipo_legislacao"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoLegislacao.findAll", query = "SELECT t FROM TipoLegislacao t"),
    @NamedQuery(name = "TipoLegislacao.findByIdeTipoLegislacao", query = "SELECT t FROM TipoLegislacao t WHERE t.ideTipoLegislacao = :ideTipoLegislacao"),
    @NamedQuery(name = "TipoLegislacao.findByDscTipoLegislacao", query = "SELECT t FROM TipoLegislacao t WHERE t.dscTipoLegislacao = :dscTipoLegislacao")})
public class TipoLegislacao implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="TIPO_LEGISLACAO_IDETIPOLEGISLACAO_GENERATOR", sequenceName="TIPO_LEGISLACAO_IDE_TIPO_LEGISLACAO_SEQ")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPO_LEGISLACAO_IDETIPOLEGISLACAO_GENERATOR")
    @Column(name="ide_tipo_legislacao", unique=true, nullable=false)
    private Integer ideTipoLegislacao;
    @Size(max = 30)
    @Column(name = "dsc_tipo_legislacao", length = 30)
    private String dscTipoLegislacao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoLegislacao", fetch = FetchType.LAZY)
    private Collection<Legislacao> legislacaoCollection;

    public TipoLegislacao() {
    }

    public TipoLegislacao(Integer ideTipoLegislacao) {
        this.ideTipoLegislacao = ideTipoLegislacao;
    }

    public Integer getIdeTipoLegislacao() {
        return ideTipoLegislacao;
    }

    public void setIdeTipoLegislacao(Integer ideTipoLegislacao) {
        this.ideTipoLegislacao = ideTipoLegislacao;
    }

    public String getDscTipoLegislacao() {
        return dscTipoLegislacao;
    }

    public void setDscTipoLegislacao(String dscTipoLegislacao) {
        this.dscTipoLegislacao = dscTipoLegislacao;
    }

    @XmlTransient
    public Collection<Legislacao> getLegislacaoCollection() {
        return legislacaoCollection;
    }

    public void setLegislacaoCollection(Collection<Legislacao> legislacaoCollection) {
        this.legislacaoCollection = legislacaoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoLegislacao != null ? ideTipoLegislacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoLegislacao)) {
            return false;
        }
        TipoLegislacao other = (TipoLegislacao) object;
        if ((this.ideTipoLegislacao == null && other.ideTipoLegislacao != null) || (this.ideTipoLegislacao != null && !this.ideTipoLegislacao.equals(other.ideTipoLegislacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoLegislacao[ ideTipoLegislacao=" + ideTipoLegislacao + " ]";
    }
    
}
