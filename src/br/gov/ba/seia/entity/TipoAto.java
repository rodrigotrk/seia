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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.enumerator.TipoAtoEnum;
import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "tipo_ato", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_tipo_ato"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoAto.findAll", query = "SELECT t FROM TipoAto t"),
    @NamedQuery(name = "TipoAto.findByIdeTipoAto", query = "SELECT t FROM TipoAto t WHERE t.ideTipoAto = :ideTipoAto"),
    @NamedQuery(name = "TipoAto.findByNomTipoAto", query = "SELECT t FROM TipoAto t WHERE t.nomTipoAto = :nomTipoAto")})
public class TipoAto implements Serializable, BaseEntity {
   
	private static final long serialVersionUID = 1L;
    
	@Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_ato", nullable = false)
    private Integer ideTipoAto;
    
	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "nom_tipo_ato", nullable = false, length = 40)
    private String nomTipoAto;
    
	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "dsc_sigla_tipo_ato", nullable = false, length = 4)
    private String dscSiglaTipoAto;
    
	
	@OneToMany(mappedBy = "ideTipoAto", fetch = FetchType.LAZY)
    private Collection<TipologiaTipoAto> tipologiaTipoAtoCollection;
    
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoAto", fetch = FetchType.LAZY)
	private Collection<AtoAmbiental> atoAmbientalCollection;
    
	@JoinColumn(name = "ide_grupo_processo", referencedColumnName = "ide_grupo_processo", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private GrupoProcesso ideGrupoProcesso;

	
	
    public TipoAto() {
    }

    public TipoAto(Integer ideTipoAto) {
        this.ideTipoAto = ideTipoAto;
    }

    public TipoAto(Integer ideTipoAto, String nomTipoAto) {
        this.ideTipoAto = ideTipoAto;
        this.nomTipoAto = nomTipoAto;
    }
    public TipoAto(TipoAtoEnum tipoAtoEnum) {
        this.ideTipoAto = tipoAtoEnum.getId();
    }
    public Integer getIdeTipoAto() {
        return ideTipoAto;
    }

    public void setIdeTipoAto(Integer ideTipoAto) {
        this.ideTipoAto = ideTipoAto;
    }

    public String getNomTipoAto() {
    
    	return nomTipoAto;
    }

    public void setNomTipoAto(String nomTipoAto) {
        this.nomTipoAto = nomTipoAto;
    }

    @XmlTransient
    public Collection<TipologiaTipoAto> getTipologiaTipoAtoCollection() {
        return tipologiaTipoAtoCollection;
    }

    public void setTipologiaTipoAtoCollection(Collection<TipologiaTipoAto> tipologiaTipoAtoCollection) {
        this.tipologiaTipoAtoCollection = tipologiaTipoAtoCollection;
    }

    @XmlTransient
    public Collection<AtoAmbiental> getAtoAmbientalCollection() {
        return atoAmbientalCollection;
    }

    public void setAtoAmbientalCollection(Collection<AtoAmbiental> atoAmbientalCollection) {
        this.atoAmbientalCollection = atoAmbientalCollection;
    }

    public GrupoProcesso getIdeGrupoProcesso() {
        return ideGrupoProcesso;
    }

    public void setIdeGrupoProcesso(GrupoProcesso ideGrupoProcesso) {
        this.ideGrupoProcesso = ideGrupoProcesso;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoAto != null ? ideTipoAto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoAto)) {
            return false;
        }
        TipoAto other = (TipoAto) object;
        if ((this.ideTipoAto == null && other.ideTipoAto != null) || (this.ideTipoAto != null && !this.ideTipoAto.equals(other.ideTipoAto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoAto[ ideTipoAto=" + ideTipoAto + " ]";
    }

	@Override
	public Long getId() {
		return new Long(this.ideTipoAto);
	}

	public String getDscSiglaTipoAto() {
		return dscSiglaTipoAto;
	}

	public void setDscSiglaTipoAto(String dscSiglaTipoAto) {
		this.dscSiglaTipoAto = dscSiglaTipoAto;
	}
    
}
