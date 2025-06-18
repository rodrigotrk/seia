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
@Table(name = "forma_realocacao_rl", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"ide_forma_realocacao_rl"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FormaRealocacaoRl.findAll", query = "SELECT t FROM FormaRealocacaoRl t")})
public class FormaRealocacaoRl implements Serializable, BaseEntity {
    
	private static final long serialVersionUID = 1503014552765833001L;

	@Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_forma_realocacao_rl", nullable = false)
    private Integer ideFormaRealocacaoRl;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "dsc_tipo_realocacao_rl", nullable = false, length = 60)
    private String dscTipoRealocacaoRl;
    
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

    public FormaRealocacaoRl() {
    }

    public FormaRealocacaoRl(Integer ideFormaRealocacaoRl) {
        this.ideFormaRealocacaoRl = ideFormaRealocacaoRl;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideFormaRealocacaoRl != null ? ideFormaRealocacaoRl.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        //: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FormaRealocacaoRl)) {
            return false;
        }
        FormaRealocacaoRl other = (FormaRealocacaoRl) object;
        if ((this.ideFormaRealocacaoRl == null && other.ideFormaRealocacaoRl != null) || (this.ideFormaRealocacaoRl != null && !this.ideFormaRealocacaoRl.equals(other.ideFormaRealocacaoRl))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
    	return String.valueOf(ideFormaRealocacaoRl);
    }

	@Override
	public Long getId() {
		return Long.valueOf(ideFormaRealocacaoRl);
	}

	public Integer getIdeFormaRealocacaoRl() {
		return ideFormaRealocacaoRl;
	}

	public void setIdeFormaRealocacaoRl(Integer ideFormaRealocacaoRl) {
		this.ideFormaRealocacaoRl = ideFormaRealocacaoRl;
	}

	public String getDscTipoRealocacaoRl() {
		return dscTipoRealocacaoRl;
	}

	public void setDscTipoRealocacaoRl(String dscTipoRealocacaoRl) {
		this.dscTipoRealocacaoRl = dscTipoRealocacaoRl;
	}

	public boolean isIndExcluido() {
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
    
}