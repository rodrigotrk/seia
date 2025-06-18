package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import flexjson.JSON;

@Entity
@Table(name = "perfil", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ide_perfil"}),
    @UniqueConstraint(columnNames = {"dsc_perfil"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Perfil.findAll", query = "SELECT p FROM Perfil p"),
    @NamedQuery(name = "Perfil.findByIdePerfil", query = "SELECT p FROM Perfil p WHERE p.idePerfil = :idePerfil"),
    @NamedQuery(name = "Perfil.findByDscPerfil", query = "SELECT p FROM Perfil p WHERE p.dscPerfil = :dscPerfil"),
    @NamedQuery(name = "Perfil.findByIndExcluido", query = "SELECT p FROM Perfil p WHERE p.indExcluido = :indExcluido"),
    @NamedQuery(name = "Perfil.findByDtcCriacao", query = "SELECT p FROM Perfil p WHERE p.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "Perfil.findByDtcExclusao", query = "SELECT p FROM Perfil p WHERE p.dtcExclusao = :dtcExclusao")})
public class Perfil implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="Perfil_IDE_PERFIL_seq")    
    @SequenceGenerator(name="Perfil_IDE_PERFIL_seq", sequenceName="Perfil_IDE_PERFIL_seq", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_perfil", nullable = false)
    private Integer idePerfil;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "dsc_perfil", nullable = false, length = 20)
    private String dscPerfil;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_criacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    @Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;
    @JoinColumn(name = "ide_funcionalidade_principal", referencedColumnName = "ide_funcionalidade")
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Funcionalidade ideFuncionalidadePrincipal;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idePerfil", fetch = FetchType.LAZY)
    private Collection<Usuario> usuarioCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "perfil", fetch = FetchType.LAZY)
    private Collection<RelGrupoPerfilFuncionalidade> relGrupoPerfilFuncionalidadeCollection;    
    
    @OneToMany(mappedBy="idePerfil",fetch=FetchType.LAZY)
    private Collection<CategoriaDocumentoPerfilArea> categoriaDocumentoPerfilAreaCollection;

    public Perfil() {
    }
    public Perfil(Integer idePerfil) {
        this.idePerfil = idePerfil;
    }
    public Perfil(String dscPerfil) {
    	this.dscPerfil = dscPerfil;
    }
    public Perfil(Integer idePerfil, String dscPerfil, boolean indExcluido, Date dtcCriacao) {
        this.idePerfil = idePerfil;
        this.dscPerfil = dscPerfil;
        this.indExcluido = indExcluido;
        this.dtcCriacao = dtcCriacao;
    }

    
    public Integer getIdePerfil() {
        return idePerfil;
    }

    public void setIdePerfil(Integer idePerfil) {
        this.idePerfil = idePerfil;
    }

    @JSON(include = false)
    public String getDscPerfil() {
        return dscPerfil;
    }

    public void setDscPerfil(String dscPerfil) {
        this.dscPerfil = dscPerfil;
    }

    @JSON(include = false)
    public boolean getIndExcluido() {
        return indExcluido;
    }

    public void setIndExcluido(boolean indExcluido) {
        this.indExcluido = indExcluido;
    }

    @JSON(include = false)
    public Date getDtcCriacao() {
        return dtcCriacao;
    }

    public void setDtcCriacao(Date dtcCriacao) {
        this.dtcCriacao = dtcCriacao;
    }

    @JSON(include = false)
    public Date getDtcExclusao() {
        return dtcExclusao;
    }

    public void setDtcExclusao(Date dtcExclusao) {
        this.dtcExclusao = dtcExclusao;
    }

    @JSON(include = false)
    public Funcionalidade getIdeFuncionalidadePrincipal() {
		return ideFuncionalidadePrincipal;
	}
	public void setIdeFuncionalidadePrincipal(
			Funcionalidade pIdeFuncionalidadePrincipal) {
		ideFuncionalidadePrincipal = pIdeFuncionalidadePrincipal;
	}
	@XmlTransient
    public Collection<Usuario> getUsuarioCollection() {
        return usuarioCollection;
    }

    public void setUsuarioCollection(Collection<Usuario> usuarioCollection) {
        this.usuarioCollection = usuarioCollection;
    }

    @XmlTransient
    public Collection<RelGrupoPerfilFuncionalidade> getRelGrupoPerfilFuncionalidadeCollection() {
        return relGrupoPerfilFuncionalidadeCollection;
    }

    public void setRelGrupoPerfilFuncionalidadeCollection(Collection<RelGrupoPerfilFuncionalidade> relGrupoPerfilFuncionalidadeCollection) {
        this.relGrupoPerfilFuncionalidadeCollection = relGrupoPerfilFuncionalidadeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idePerfil != null ? idePerfil.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Perfil)) {
            return false;
        }
        Perfil other = (Perfil) object;
        if ((this.idePerfil == null && other.idePerfil != null) || (this.idePerfil != null && !this.idePerfil.equals(other.idePerfil))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {

    	return String.valueOf(idePerfil);
    }
    
	public Collection<CategoriaDocumentoPerfilArea> getCategoriaDocumentoPerfilAreaCollection() {
		return categoriaDocumentoPerfilAreaCollection;
	}
	
	public void setCategoriaDocumentoPerfilAreaCollection(
			Collection<CategoriaDocumentoPerfilArea> categoriaDocumentoPerfilAreaCollection) {
		this.categoriaDocumentoPerfilAreaCollection = categoriaDocumentoPerfilAreaCollection;
	}
}