package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "pais", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"sgl_pais"}),@UniqueConstraint(columnNames = {"nom_pais"})})

public class Pais extends AbstractEntity {
	private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "ide_pais", nullable = false)
    private Integer idePais;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "sgl_pais", nullable = false, length = 3)
    private String sglPais;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nom_pais", nullable = false, length = 100)
    private String nomPais;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idePais", fetch = FetchType.LAZY)
    private Collection<PessoaFisica> pessoaFisicaCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idePais", fetch = FetchType.LAZY)
    private Collection<Estado> estadoCollection;

    public Pais() {
    }

    public Pais(Integer idePais) {
        this.idePais = idePais;
    }

    public Pais(Integer idePais, String sglPais, String nomPais) {
        this.idePais = idePais;
        this.sglPais = sglPais;
        this.nomPais = nomPais;
    }

    public Integer getIdePais() {
        return idePais;
    }

    public void setIdePais(Integer idePais) {
        this.idePais = idePais;
    }

    public String getSglPais() {
        return sglPais;
    }

    public void setSglPais(String sglPais) {
        this.sglPais = sglPais;
    }

    public String getNomPais() {
        return nomPais;
    }

    public void setNomPais(String nomPais) {
        this.nomPais = nomPais;
    }

    @XmlTransient
    public Collection<PessoaFisica> getPessoaFisicaCollection() {
        return pessoaFisicaCollection;
    }

    public void setPessoaFisicaCollection(Collection<PessoaFisica> pessoaFisicaCollection) {
        this.pessoaFisicaCollection = pessoaFisicaCollection;
    }

	@XmlTransient
    public Collection<Estado> getEstadoCollection() {
        return estadoCollection;
    }

    public void setEstadoCollection(Collection<Estado> estadoCollection) {
        this.estadoCollection = estadoCollection;
    }
 
}