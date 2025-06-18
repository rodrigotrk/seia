package br.gov.ba.seia.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;

@Entity
@Table(name = "requerimento_pessoa")
@XmlRootElement

@NamedQueries({
    @NamedQuery(name = "RequerimentoPessoa.findAll", query = "SELECT r FROM RequerimentoPessoa r"),
    @NamedQuery(name = "RequerimentoPessoa.findByIdeRequerimento", query = "SELECT r FROM RequerimentoPessoa r WHERE r.requerimentoPessoaPK.ideRequerimento = :ideRequerimento"),
    @NamedQuery(name = "RequerimentoPessoa.findByIdePessoa", query = "SELECT r FROM RequerimentoPessoa r WHERE r.requerimentoPessoaPK.idePessoa = :idePessoa"),
    @NamedQuery(name = "RequerimentoPessoa.findByRequerimentoPessoa", query = "SELECT r FROM RequerimentoPessoa r WHERE r.requerimentoPessoaPK.ideRequerimento = :ideRequerimento and r.ideTipoPessoaRequerimento.ideTipoPessoaRequerimento = :ideTipoPessoaRequerimento")})

public class RequerimentoPessoa implements Serializable, Comparable<RequerimentoPessoa> {
    
	private static final long serialVersionUID = 1L;
    
	@EmbeddedId
    protected RequerimentoPessoaPK requerimentoPessoaPK;
    
	@JoinColumn(name = "ide_tipo_pessoa_requerimento", referencedColumnName = "ide_tipo_pessoa_requerimento", nullable = false, insertable = false,  updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private TipoPessoaRequerimento ideTipoPessoaRequerimento;
    
	@JoinColumn(name = "ide_requerimento", referencedColumnName = "ide_requerimento", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Requerimento requerimento;
    
	@JoinColumn(name = "ide_pessoa", referencedColumnName = "ide_pessoa", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Pessoa pessoa;
    
	@Basic(optional = false)
    @NotNull
    @Column(name = "ind_solicitante")
    private boolean indSolicitante;
    
	@Basic(optional = false)
    @NotNull
    @Column(name = "ind_usuario_logado")
    private boolean indUsuarioLogado;
    
    public RequerimentoPessoa() {
    }
    
    public RequerimentoPessoa(RequerimentoPessoaPK requerimentoPessoaPK) {
    this.requerimentoPessoaPK = requerimentoPessoaPK;
    }
    
    public RequerimentoPessoaPK getRequerimentoPessoaPK() {
        return requerimentoPessoaPK;
    }

    public void setRequerimentoPessoaPK(RequerimentoPessoaPK requerimentoPessoaPK) {
        this.requerimentoPessoaPK = requerimentoPessoaPK;
    }

    public TipoPessoaRequerimento getIdeTipoPessoaRequerimento() {
        return ideTipoPessoaRequerimento;
    }

    public void setIdeTipoPessoaRequerimento(TipoPessoaRequerimento ideTipoPessoaRequerimento) {
        this.ideTipoPessoaRequerimento = ideTipoPessoaRequerimento;
    }

    public Requerimento getRequerimento() {
        return requerimento;
    }

    public void setRequerimento(Requerimento requerimento) {
        this.requerimento = requerimento;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (requerimentoPessoaPK != null ? requerimentoPessoaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof RequerimentoPessoa)) {
            return false;
        }
        RequerimentoPessoa other = (RequerimentoPessoa) object;
        if ((this.requerimentoPessoaPK == null && other.requerimentoPessoaPK != null) || (this.requerimentoPessoaPK != null && !this.requerimentoPessoaPK.equals(other.requerimentoPessoaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.RequerimentoPessoa[ requerimentoPessoaPK=" + requerimentoPessoaPK + " ]";
    }

	public boolean isIndSolicitante() {
		return indSolicitante;
	}

	public void setIndSolicitante(boolean indSolicitante) {
		this.indSolicitante = indSolicitante;
	}

	public boolean isIndUsuarioLogado() {
		return indUsuarioLogado;
	}

	public void setIndUsuarioLogado(boolean indUsuarioLogado) {
		this.indUsuarioLogado = indUsuarioLogado;
	}
    
	public boolean isRequerente(){
		return TipoPessoaRequerimentoEnum.REQUERENTE.getId().equals(this.ideTipoPessoaRequerimento.getIdeTipoPessoaRequerimento());
	}
	public boolean isProcurador(){
		return TipoPessoaRequerimentoEnum.PROCURADOR.getId().equals(this.ideTipoPessoaRequerimento.getIdeTipoPessoaRequerimento());
	}
	public boolean isRepresentante(){
		return TipoPessoaRequerimentoEnum.REPRESENTANTE_LEGAL.getId().equals(this.ideTipoPessoaRequerimento.getIdeTipoPessoaRequerimento());
	}

	@Override
	public int compareTo(RequerimentoPessoa reqPessoa) {
		
		if(this.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento() < reqPessoa.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento()) {
			return -1;
		}
		
		if(this.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento() > reqPessoa.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento()) {
			return 1;
		}

		return 0;
	}
}
