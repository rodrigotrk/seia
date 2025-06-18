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

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "tipo_telefone", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_tipo_telefone"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoTelefone.findAll", query = "SELECT t FROM TipoTelefone t"),
    @NamedQuery(name = "TipoTelefone.findByIdeTipoTelefone", query = "SELECT t FROM TipoTelefone t WHERE t.ideTipoTelefone = :ideTipoTelefone"),
    @NamedQuery(name = "TipoTelefone.findByNomTipoTelefone", query = "SELECT t FROM TipoTelefone t WHERE t.nomTipoTelefone = :nomTipoTelefone")})
public class TipoTelefone implements Serializable, BaseEntity  {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_telefone", nullable = false)
    private Integer ideTipoTelefone;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "nom_tipo_telefone", nullable = false, length = 15)
    private String nomTipoTelefone;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoTelefone", fetch = FetchType.LAZY)
    private Collection<Telefone> telefoneCollection;

    public TipoTelefone() {
    }

    public TipoTelefone(Integer ideTipoTelefone) {
        this.ideTipoTelefone = ideTipoTelefone;
    }

    public TipoTelefone(Integer ideTipoTelefone, String nomTipoTelefone) {
        this.ideTipoTelefone = ideTipoTelefone;
        this.nomTipoTelefone = nomTipoTelefone;
    }

    public Integer getIdeTipoTelefone() {
        return ideTipoTelefone;
    }

    public void setIdeTipoTelefone(Integer ideTipoTelefone) {
        this.ideTipoTelefone = ideTipoTelefone;
    }

    public String getNomTipoTelefone() {
        return nomTipoTelefone;
    }

    public void setNomTipoTelefone(String nomTipoTelefone) {
        this.nomTipoTelefone = nomTipoTelefone;
    }

    @XmlTransient
    public Collection<Telefone> getTelefoneCollection() {
        return telefoneCollection;
    }

    public void setTelefoneCollection(Collection<Telefone> telefoneCollection) {
        this.telefoneCollection = telefoneCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoTelefone != null ? ideTipoTelefone.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoTelefone)) {
            return false;
        }
        TipoTelefone other = (TipoTelefone) object;
        
        if (this.ideTipoTelefone != null && other.ideTipoTelefone != null && this.ideTipoTelefone.intValue() == other.ideTipoTelefone.intValue()) {
        	return true;
        }
        
        
        if ((this.ideTipoTelefone == null && other.ideTipoTelefone != null) || (this.ideTipoTelefone != null && !this.ideTipoTelefone.equals(other.ideTipoTelefone))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return String.valueOf(ideTipoTelefone);
    }

	@Override
	public Long getId() {
		return new Long(this.ideTipoTelefone);
	}
    
}
