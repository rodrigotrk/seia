package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "grupo_processo", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ide_grupo_processo"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GrupoProcesso.findAll", query = "SELECT g FROM GrupoProcesso g"),
    @NamedQuery(name = "GrupoProcesso.findByIdeGrupoProcesso", query = "SELECT g FROM GrupoProcesso g WHERE g.ideGrupoProcesso = :ideGrupoProcesso"),
    @NamedQuery(name = "GrupoProcesso.findByDscGrupoProcesso", query = "SELECT g FROM GrupoProcesso g WHERE g.dscGrupoProcesso = :dscGrupoProcesso"),
    @NamedQuery(name = "GrupoProcesso.findByIndExcluido", query = "SELECT g FROM GrupoProcesso g WHERE g.indExcluido = :indExcluido"),
    @NamedQuery(name = "GrupoProcesso.findByDtcCriacao", query = "SELECT g FROM GrupoProcesso g WHERE g.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "GrupoProcesso.findByDtcExclusao", query = "SELECT g FROM GrupoProcesso g WHERE g.dtcExclusao = :dtcExclusao")})
public class GrupoProcesso implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @Column(name = "ide_grupo_processo", nullable = false)
    private Integer ideGrupoProcesso;
    
    @Column(name = "dsc_grupo_processo", nullable = false, length = 50)
    private String dscGrupoProcesso;
    
    @Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;
    
    @Column(name = "dtc_criacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    
	@Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;
    
    @Column(name = "dsc_sigla_grupo_processo", nullable = false, length = 4)
    private String dscSiglaGrupoProcesso;
    
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideGrupoProcesso", fetch = FetchType.LAZY)
    private Collection<TipoAto> tipoAtoCollection;

    public GrupoProcesso() {
    }

    public GrupoProcesso(Integer ideGrupoProcesso) {
        this.ideGrupoProcesso = ideGrupoProcesso;
    }

    public GrupoProcesso(Integer ideGrupoProcesso, String dscGrupoProcesso, boolean indExcluido, Date dtcCriacao) {
        this.ideGrupoProcesso = ideGrupoProcesso;
        this.dscGrupoProcesso = dscGrupoProcesso;
        this.indExcluido = indExcluido;
        this.dtcCriacao = dtcCriacao;
    }

    public Integer getIdeGrupoProcesso() {
        return ideGrupoProcesso;
    }

    public void setIdeGrupoProcesso(Integer ideGrupoProcesso) {
        this.ideGrupoProcesso = ideGrupoProcesso;
    }

    public String getDscGrupoProcesso() {
        return dscGrupoProcesso;
    }

    public void setDscGrupoProcesso(String dscGrupoProcesso) {
        this.dscGrupoProcesso = dscGrupoProcesso;
    }

    public boolean getIndExcluido() {
        return indExcluido;
    }

    public void setIndExcluido(boolean indExcluido) {
        this.indExcluido = indExcluido;
    }

    public Date getDtcCriacao() {
        return dtcCriacao;
    }

    public void setDtcCriacao(Date dtcCriacao) {
        this.dtcCriacao = dtcCriacao;
    }

    public Date getDtcExclusao() {
        return dtcExclusao;
    }

    public void setDtcExclusao(Date dtcExclusao) {
        this.dtcExclusao = dtcExclusao;
    }
    
    public String getDscSiglaGrupoProcesso() {
		return dscSiglaGrupoProcesso;
	}

	public void setDscSiglaGrupoProcesso(String dscSiglaGrupoProcesso) {
		this.dscSiglaGrupoProcesso = dscSiglaGrupoProcesso;
	}

	@XmlTransient
    public Collection<TipoAto> getTipoAtoCollection() {
        return tipoAtoCollection;
    }

    public void setTipoAtoCollection(Collection<TipoAto> tipoAtoCollection) {
        this.tipoAtoCollection = tipoAtoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideGrupoProcesso != null ? ideGrupoProcesso.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof GrupoProcesso)) {
            return false;
        }
        GrupoProcesso other = (GrupoProcesso) object;
        if ((this.ideGrupoProcesso == null && other.ideGrupoProcesso != null) || (this.ideGrupoProcesso != null && !this.ideGrupoProcesso.equals(other.ideGrupoProcesso))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.GrupoProcesso[ ideGrupoProcesso=" + ideGrupoProcesso + " ]";
    }
    
}
