package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "tipologia_tipo_ato")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipologiaTipoAto.findAll", query = "SELECT t FROM TipologiaTipoAto t"),
    @NamedQuery(name = "TipologiaTipoAto.findByIdeTipologiaTipoAto", query = "SELECT t FROM TipologiaTipoAto t WHERE t.ideTipologiaTipoAto = :ideTipologiaTipoAto")})
public class TipologiaTipoAto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPOLOGIA_TIPO_ATO_IDE_TIPOLOGIA_TIPO_ATO_seq")    
    @SequenceGenerator(name="TIPOLOGIA_TIPO_ATO_IDE_TIPOLOGIA_TIPO_ATO_seq", sequenceName="TIPOLOGIA_TIPO_ATO_IDE_TIPOLOGIA_TIPO_ATO_seq", allocationSize=1)
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipologia_tipo_ato", nullable = false)
    private Integer ideTipologiaTipoAto;
    @JoinColumn(name = "ide_tipologia_grupo", referencedColumnName = "ide_tipologia_grupo", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TipologiaGrupo ideTipologiaGrupo;
    @JoinColumn(name = "ide_tipo_ato", referencedColumnName = "ide_tipo_ato")
    @ManyToOne(fetch = FetchType.LAZY)
    private TipoAto ideTipoAto;
    @JoinColumn(name = "ide_ato_ambiental", referencedColumnName = "ide_ato_ambiental")
    @ManyToOne(fetch = FetchType.LAZY)
    private AtoAmbiental ideAtoAmbiental;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipologiaTipoAto", fetch = FetchType.LAZY)
    private Collection<ParametroReferencia> parametroReferenciaCollection;

    public TipologiaTipoAto() {
    }
    
    
    
    
    public TipologiaTipoAto(TipologiaGrupo ideTipologiaGrupo,TipoAto ideTipoAto, AtoAmbiental ideAtoAmbiental) {
    	this.ideTipologiaGrupo = ideTipologiaGrupo;
    	this.ideTipoAto = ideTipoAto;
    	this.ideAtoAmbiental =  ideAtoAmbiental;
    	
        
    }
    

    public TipologiaTipoAto(Integer ideTipologiaTipoAto) {
        this.ideTipologiaTipoAto = ideTipologiaTipoAto;
    }

    public Integer getIdeTipologiaTipoAto() {
        return ideTipologiaTipoAto;
    }

    public void setIdeTipologiaTipoAto(Integer ideTipologiaTipoAto) {
        this.ideTipologiaTipoAto = ideTipologiaTipoAto;
    }

    public TipologiaGrupo getIdeTipologiaGrupo() {
        return ideTipologiaGrupo;
    }

    public void setIdeTipologiaGrupo(TipologiaGrupo ideTipologiaGrupo) {
        this.ideTipologiaGrupo = ideTipologiaGrupo;
    }

    public TipoAto getIdeTipoAto() {
        return ideTipoAto;
    }

    public void setIdeTipoAto(TipoAto ideTipoAto) {
        this.ideTipoAto = ideTipoAto;
    }

    public AtoAmbiental getIdeAtoAmbiental() {
        return ideAtoAmbiental;
    }

    public void setIdeAtoAmbiental(AtoAmbiental ideAtoAmbiental) {
        this.ideAtoAmbiental = ideAtoAmbiental;
    }

    @XmlTransient
    public Collection<ParametroReferencia> getParametroReferenciaCollection() {
        return parametroReferenciaCollection;
    }

    public void setParametroReferenciaCollection(Collection<ParametroReferencia> parametroReferenciaCollection) {
        this.parametroReferenciaCollection = parametroReferenciaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipologiaTipoAto != null ? ideTipologiaTipoAto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipologiaTipoAto)) {
            return false;
        }
        TipologiaTipoAto other = (TipologiaTipoAto) object;
        if ((this.ideTipologiaTipoAto == null && other.ideTipologiaTipoAto != null) || (this.ideTipologiaTipoAto != null && !this.ideTipologiaTipoAto.equals(other.ideTipologiaTipoAto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipologiaTipoAto[ ideTipologiaTipoAto=" + ideTipologiaTipoAto + " ]";
    }
    
}
