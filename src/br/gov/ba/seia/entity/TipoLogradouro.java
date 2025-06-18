package br.gov.ba.seia.entity;

import java.util.Collection;

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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.entity.generic.AbstractEntity;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "tipo_logradouro", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"sgl_tipo_logradouro"}),
    @UniqueConstraint(columnNames = {"nom_tipo_logradouro"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TipoLogradouro.findAll", query = "SELECT t FROM TipoLogradouro t"),
    @NamedQuery(name = "TipoLogradouro.findByIdeTipoLogradouro", query = "SELECT t FROM TipoLogradouro t WHERE t.ideTipoLogradouro = :ideTipoLogradouro"),
    @NamedQuery(name = "TipoLogradouro.findBySglTipoLogradouro", query = "SELECT t FROM TipoLogradouro t WHERE t.sglTipoLogradouro = :sglTipoLogradouro"),
    @NamedQuery(name = "TipoLogradouro.findByNomTipoLogradouro", query = "SELECT t FROM TipoLogradouro t WHERE t.nomTipoLogradouro = :nomTipoLogradouro")})
public class TipoLogradouro extends AbstractEntity {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "ide_tipo_logradouro", nullable = false)
    private Integer ideTipoLogradouro;

    @Column(name = "sgl_tipo_logradouro", nullable = false, length = 3)
    private String sglTipoLogradouro;
    
    @Column(name = "nom_tipo_logradouro", nullable = false, length = 30)
    private String nomTipoLogradouro;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideTipoLogradouro", fetch = FetchType.LAZY)
    private Collection<Logradouro> logradouroCollection;

    public TipoLogradouro() {
    }

    public TipoLogradouro(Integer ideTipoLogradouro) {
        this.ideTipoLogradouro = ideTipoLogradouro;
    }

    public TipoLogradouro(Integer ideTipoLogradouro, String nomTipoLogradouro) {
        this.ideTipoLogradouro = ideTipoLogradouro;
        this.nomTipoLogradouro = nomTipoLogradouro;
    }
    
    public TipoLogradouro(Integer ideTipoLogradouro, String sglTipoLogradouro, String nomTipoLogradouro) {
    	this.ideTipoLogradouro = ideTipoLogradouro;
    	this.sglTipoLogradouro = sglTipoLogradouro;
    	this.nomTipoLogradouro = nomTipoLogradouro;
    }

    public Integer getIdeTipoLogradouro() {
        return ideTipoLogradouro;
    }

    public void setIdeTipoLogradouro(Integer ideTipoLogradouro) {
        this.ideTipoLogradouro = ideTipoLogradouro;
    }

    public String getSglTipoLogradouro() {
        return sglTipoLogradouro;
    }

    public void setSglTipoLogradouro(String sglTipoLogradouro) {
        this.sglTipoLogradouro = sglTipoLogradouro;
    }

    public String getNomTipoLogradouro() {
        return nomTipoLogradouro;
    }

    public void setNomTipoLogradouro(String nomTipoLogradouro) {
        this.nomTipoLogradouro = nomTipoLogradouro;
    }

    @XmlTransient
    public Collection<Logradouro> getLogradouroCollection() {
        return logradouroCollection;
    }

    public void setLogradouroCollection(Collection<Logradouro> logradouroCollection) {
        this.logradouroCollection = logradouroCollection;
    }
}
