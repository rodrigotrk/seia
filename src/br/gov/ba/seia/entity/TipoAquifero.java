package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "tipo_aquifero", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom_tipo_aquifero"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoAquifero.findAll", query = "SELECT t.ideTipoAquifero, t.nomTipoAquifero, t.indAtivo FROM TipoAquifero t"),
    @NamedQuery(name = "TipoAquifero.findByIdeTipoAquifero", query = "SELECT t FROM TipoAquifero t WHERE t.ideTipoAquifero = :ideTipoAquifero"),
    @NamedQuery(name = "TipoAquifero.findByNomTipoAquifero", query = "SELECT t FROM TipoAquifero t WHERE t.nomTipoAquifero = :nomTipoAquifero")})
public class TipoAquifero implements Serializable, BaseEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_tipo_aquifero", nullable = false)
    private Integer ideTipoAquifero;
    @Basic(optional = false)
    @NotNull
    
    @Column(name = "nom_tipo_aquifero", nullable = false, length = 50)
    private String nomTipoAquifero;
    
    @Column(name="ind_ativo")
    private Boolean indAtivo;

	
    public TipoAquifero() {
    }

    public TipoAquifero(Integer ideTipoAquifero, String nomTipoAquifero, Boolean indAtivo) {
        this.ideTipoAquifero = ideTipoAquifero;
        this.nomTipoAquifero = nomTipoAquifero;
        this.indAtivo = indAtivo;
    }
    
    public TipoAquifero(Integer ideTipoAquifero) {
        this.ideTipoAquifero = ideTipoAquifero;
    }

    public TipoAquifero(Integer ideTipoAquifero, String nomTipoAquifero) {
        this.ideTipoAquifero = ideTipoAquifero;
        this.nomTipoAquifero = nomTipoAquifero;
    }

    public Integer getIdeTipoAquifero() {
        return ideTipoAquifero;
    }

    public void setIdeTipoAquifero(Integer ideTipoAquifero) {
        this.ideTipoAquifero = ideTipoAquifero;
    }

    public String getNomTipoAquifero() {
        return nomTipoAquifero;
    }

    public void setNomTipoAquifero(String nomTipoAquifero) {
        this.nomTipoAquifero = nomTipoAquifero;
    }
    
    public Boolean getIndAtivo(){
    	return indAtivo;
    }
    
    public void setIndAtivo(Boolean indAtivo){
    	this.indAtivo = indAtivo;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideTipoAquifero != null ? ideTipoAquifero.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof TipoAquifero)) {
            return false;
        }
        TipoAquifero other = (TipoAquifero) object;
        if ((this.ideTipoAquifero == null && other.ideTipoAquifero != null) || (this.ideTipoAquifero != null && !this.ideTipoAquifero.equals(other.ideTipoAquifero))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.TipoAquifero[ ideTipoAquifero=" + ideTipoAquifero + " ]";
    }

	@Override
	public Long getId() {
		return new Long(this.ideTipoAquifero);
	}
    
}
