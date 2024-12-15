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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.interfaces.BaseEntity;

/**
 *
 * @author micael.coutinho
 */
@Entity
@Table(name = "sistema_coordenada")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SistemaCoordenada.findAll", query = "SELECT s FROM SistemaCoordenada s"),
    @NamedQuery(name = "SistemaCoordenada.findByIdeSistemaCoordenada", query = "SELECT s FROM SistemaCoordenada s WHERE s.ideSistemaCoordenada = :ideSistemaCoordenada"),
    @NamedQuery(name = "SistemaCoordenada.findByNomSistemaCoordenada", query = "SELECT s FROM SistemaCoordenada s WHERE s.nomSistemaCoordenada = :nomSistemaCoordenada"),
    @NamedQuery(name = "SistemaCoordenada.findBySrid", query = "SELECT s FROM SistemaCoordenada s WHERE s.srid = :srid")})
public class SistemaCoordenada implements Serializable, BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_sistema_coordenada")
	private Integer ideSistemaCoordenada;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 50)
	@Column(name = "nom_sistema_coordenada")
	private String nomSistemaCoordenada;
	
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 20)
	@Column(name = "srid")
	private String srid;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideSistemaCoordenada", fetch = FetchType.LAZY)
	private Collection<LocalizacaoGeografica> localizacaoGeograficaCollection;

    public SistemaCoordenada() {
    }

    public SistemaCoordenada(Integer ideSistemaCoordenada) {
        this.ideSistemaCoordenada = ideSistemaCoordenada;
    }

    public SistemaCoordenada(Integer ideSistemaCoordenada, String nomSistemaCoordenada, String srid) {
        this.ideSistemaCoordenada = ideSistemaCoordenada;
        this.nomSistemaCoordenada = nomSistemaCoordenada;
        this.srid = srid;
    }

    public Integer getIdeSistemaCoordenada() {
        return ideSistemaCoordenada;
    }

    public void setIdeSistemaCoordenada(Integer ideSistemaCoordenada) {
        this.ideSistemaCoordenada = ideSistemaCoordenada;
    }

    public String getNomSistemaCoordenada() {
        return nomSistemaCoordenada;
    }

    public void setNomSistemaCoordenada(String nomSistemaCoordenada) {
        this.nomSistemaCoordenada = nomSistemaCoordenada;
    }

    public String getSrid() {
        return srid;
    }

    public void setSrid(String srid) {
        this.srid = srid;
    }

    @XmlTransient
    public Collection<LocalizacaoGeografica> getLocalizacaoGeograficaCollection() {
        return localizacaoGeograficaCollection;
    }

    public void setLocalizacaoGeograficaCollection(Collection<LocalizacaoGeografica> localizacaoGeograficaCollection) {
        this.localizacaoGeograficaCollection = localizacaoGeograficaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideSistemaCoordenada != null ? ideSistemaCoordenada.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof SistemaCoordenada)) {
            return false;
        }
        SistemaCoordenada other = (SistemaCoordenada) object;
        if ((this.ideSistemaCoordenada == null && other.ideSistemaCoordenada != null) || (this.ideSistemaCoordenada != null && !this.ideSistemaCoordenada.equals(other.ideSistemaCoordenada))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return ""+ideSistemaCoordenada+"";
    }

	@Override
	public Long getId() {
		return new Long(this.ideSistemaCoordenada);
	}
    
}
