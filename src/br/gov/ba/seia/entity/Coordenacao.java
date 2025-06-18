package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.interfaces.BaseEntity;

@Entity
@Table(name = "coordenacao", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"des_sigla"}),
    @UniqueConstraint(columnNames = {"nom_coordenacao"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Coordenacao.findAll", query = "SELECT c FROM Coordenacao c"),
    @NamedQuery(name = "Coordenacao.findByIdeCoordenacao", query = "SELECT c FROM Coordenacao c WHERE c.ideCoordenacao = :ideCoordenacao"),
    @NamedQuery(name = "Coordenacao.findByDesSigla", query = "SELECT c FROM Coordenacao c WHERE c.desSigla = :desSigla"),
    @NamedQuery(name = "Coordenacao.findByNomCoordenacao", query = "SELECT c FROM Coordenacao c WHERE c.nomCoordenacao = :nomCoordenacao"),
    @NamedQuery(name = "Coordenacao.findByDtcCriacao", query = "SELECT c FROM Coordenacao c WHERE c.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "Coordenacao.findByIndExcluido", query = "SELECT c FROM Coordenacao c WHERE c.indExcluido = :indExcluido"),
    @NamedQuery(name = "Coordenacao.findByDtcExclusao", query = "SELECT c FROM Coordenacao c WHERE c.dtcExclusao = :dtcExclusao")})
public class Coordenacao implements Serializable, BaseEntity {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_coordenacao", nullable = false)
    private Integer ideCoordenacao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "des_sigla", nullable = false, length = 10)
    private String desSigla;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nom_coordenacao", nullable = false, length = 100)
    private String nomCoordenacao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_criacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;
    @Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;

    public Coordenacao() {
    }

    public Coordenacao(Integer ideCoordenacao) {
        this.ideCoordenacao = ideCoordenacao;
    }

    public Coordenacao(Integer ideCoordenacao, String desSigla, String nomCoordenacao, Date dtcCriacao, boolean indExcluido) {
        this.ideCoordenacao = ideCoordenacao;
        this.desSigla = desSigla;
        this.nomCoordenacao = nomCoordenacao;
        this.dtcCriacao = dtcCriacao;
        this.indExcluido = indExcluido;
    }

    public Integer getIdeCoordenacao() {
        return ideCoordenacao;
    }

    public void setIdeCoordenacao(Integer ideCoordenacao) {
        this.ideCoordenacao = ideCoordenacao;
    }

    public String getDesSigla() {
        return desSigla;
    }

    public void setDesSigla(String desSigla) {
        this.desSigla = desSigla;
    }

    public String getNomCoordenacao() {
        return nomCoordenacao;
    }

    public void setNomCoordenacao(String nomCoordenacao) {
        this.nomCoordenacao = nomCoordenacao;
    }

    public Date getDtcCriacao() {
        return dtcCriacao;
    }

    public void setDtcCriacao(Date dtcCriacao) {
        this.dtcCriacao = dtcCriacao;
    }

    public boolean getIndExcluido() {
        return indExcluido;
    }

    public void setIndExcluido(boolean indExcluido) {
        this.indExcluido = indExcluido;
    }

    public Date getDtcExclusao() {
        return dtcExclusao;
    }

    public void setDtcExclusao(Date dtcExclusao) {
        this.dtcExclusao = dtcExclusao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideCoordenacao != null ? ideCoordenacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof Coordenacao)) {
            return false;
        }
        Coordenacao other = (Coordenacao) object;
        if ((this.ideCoordenacao == null && other.ideCoordenacao != null) || (this.ideCoordenacao != null && !this.ideCoordenacao.equals(other.ideCoordenacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.Coordenacao[ ideCoordenacao=" + ideCoordenacao + " ]";
    }

	@Override
	public Long getId() {
		return new Long(this.getIdeCoordenacao());
	}
    
}
