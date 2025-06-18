package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "tipologia_grupo_area")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipologiaGrupoArea.findAll", query = "SELECT t FROM TipologiaGrupoArea t"),
    @NamedQuery(name = "TipologiaGrupoArea.findByIdeTipologiaGrupoArea", query = "SELECT t FROM TipologiaGrupoArea t WHERE t.ideTipologiaGrupoArea = :ideTipologiaGrupoArea")})
public class TipologiaGrupoArea implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="TIPOLOGIA_GRUPO_AREA_IDE_TIPOLOGIA_GRUPO_AREA_seq", sequenceName="TIPOLOGIA_GRUPO_AREA_IDE_TIPOLOGIA_GRUPO_AREA_seq",allocationSize = 1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TIPOLOGIA_GRUPO_AREA_IDE_TIPOLOGIA_GRUPO_AREA_seq")
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipologia_grupo_area", nullable = false)
    private Integer ideTipologiaGrupoArea;
    @JoinColumn(name = "ide_tipologia_grupo", referencedColumnName = "ide_tipologia_grupo", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private TipologiaGrupo ideTipologiaGrupo;
    @JoinColumn(name = "ide_area", referencedColumnName = "ide_area", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Area ideArea;

    public TipologiaGrupoArea() {
    }

    public TipologiaGrupoArea(TipologiaGrupo ideTipologiaGrupo, Area ideArea) {
        
        this.ideTipologiaGrupo = ideTipologiaGrupo;
        this.ideArea = ideArea ;
    }
    
    public TipologiaGrupoArea(Integer ideTipologiaGrupoArea) {
        this.ideTipologiaGrupoArea = ideTipologiaGrupoArea;
    }

    public Integer getIdeTipologiaGrupoArea() {
        return ideTipologiaGrupoArea;
    }

    public void setIdeTipologiaGrupoArea(Integer ideTipologiaGrupoArea) {
        this.ideTipologiaGrupoArea = ideTipologiaGrupoArea;
    }

    public TipologiaGrupo getIdeTipologiaGrupo() {
        return ideTipologiaGrupo;
    }

    public void setIdeTipologiaGrupo(TipologiaGrupo ideTipologiaGrupo) {
        this.ideTipologiaGrupo = ideTipologiaGrupo;
    }

    public Area getIdeArea() {
        return ideArea;
    }

    public void setIdeArea(Area ideArea) {
        this.ideArea = ideArea;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipologiaGrupoArea != null ? ideTipologiaGrupoArea.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipologiaGrupoArea)) {
            return false;
        }
        TipologiaGrupoArea other = (TipologiaGrupoArea) object;
        if ((this.ideTipologiaGrupoArea == null && other.ideTipologiaGrupoArea != null) || (this.ideTipologiaGrupoArea != null && !this.ideTipologiaGrupoArea.equals(other.ideTipologiaGrupoArea))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipologiaGrupoArea[ ideTipologiaGrupoArea=" + ideTipologiaGrupoArea + " ]";
    }
    
}
