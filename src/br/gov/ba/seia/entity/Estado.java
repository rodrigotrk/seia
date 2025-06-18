package br.gov.ba.seia.entity;

import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.entity.generic.AbstractEntity;

@Entity
@Table(name = "estado", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"des_sigla"}),
    @UniqueConstraint(columnNames = {"nom_estado"})})

public class Estado extends AbstractEntity implements Cloneable {
	private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "ide_estado", nullable = false)
    private Integer ideEstado;

    @Column(name = "nom_estado", nullable = false, length = 30)
    private String nomEstado;

    @Column(name = "des_sigla", nullable = false, length = 2)
    private String desSigla;
    
    @Column(name="cod_ibge_estado", nullable = false)
    private Integer codIbgeEstado;

    @JoinColumn(name = "ide_pais", referencedColumnName = "ide_pais", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Pais idePais;

    @OneToMany(mappedBy = "ideEstado", fetch = FetchType.LAZY)
    private Collection<DocumentoIdentificacao> documentoIdentificacaoCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideEstado", fetch = FetchType.LAZY)
    private Collection<Municipio> municipioCollection;

    public Estado() {
    }

    public Estado(Integer ideEstado) {
        this.ideEstado = ideEstado;
    }
    
    public Estado(Integer ideEstado, String nomEstado) {
    	this(ideEstado);
    	this.nomEstado = nomEstado;
    }

    public Estado(Integer ideEstado, String nomEstado, String desSigla) {
    	this(ideEstado, nomEstado);
        this.desSigla = desSigla;
    }
    
    public Integer getIdeEstado() {
        return ideEstado;
    }

    public void setIdeEstado(Integer ideEstado) {
        this.ideEstado = ideEstado;
    }

    public String getNomEstado() {
        return nomEstado;
    }

    public void setNomEstado(String nomEstado) {
        this.nomEstado = nomEstado;
    }

    public String getDesSigla() {
        return desSigla;
    }

    public void setDesSigla(String desSigla) {
        this.desSigla = desSigla;
    }

    @XmlTransient
    public Collection<DocumentoIdentificacao> getDocumentoIdentificacaoCollection() {
        return documentoIdentificacaoCollection;
    }

    public void setDocumentoIdentificacaoCollection(Collection<DocumentoIdentificacao> documentoIdentificacaoCollection) {
        this.documentoIdentificacaoCollection = documentoIdentificacaoCollection;
    }

    public Pais getIdePais() {
        return idePais;
    }

    public void setIdePais(Pais idePais) {
        this.idePais = idePais;
    }

    @XmlTransient
    public Collection<Municipio> getMunicipioCollection() {
        return municipioCollection;
    }

    public void setMunicipioCollection(Collection<Municipio> municipioCollection) {
        this.municipioCollection = municipioCollection;
    }   
    
	public Integer getCodIbgeEstado() {
		return codIbgeEstado;
	}

	public void setCodIbgeEstado(Integer codIbgeEstado) {
		this.codIbgeEstado = codIbgeEstado;
	}

	@Override
	public Estado clone() throws CloneNotSupportedException{
		return (Estado) super.clone();
	}
}
