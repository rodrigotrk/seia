package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author pedro.carvalho
 */
@Entity
@Table(name = "pessoa_fisica_historico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PessoaFisicaHistorico.findAll", query = "SELECT p FROM PessoaFisicaHistorico p"),
    @NamedQuery(name = "PessoaFisicaHistorico.findByIdePessoaFisicaHistorico", query = "SELECT p FROM PessoaFisicaHistorico p WHERE p.idePessoaFisicaHistorico = :idePessoaFisicaHistorico"),
    @NamedQuery(name = "PessoaFisicaHistorico.findByDtcHistorico", query = "SELECT p FROM PessoaFisicaHistorico p WHERE p.dtcHistorico = :dtcHistorico"),
    @NamedQuery(name = "PessoaFisicaHistorico.findByIdePessoaFisica", query = "SELECT p FROM PessoaFisicaHistorico p WHERE p.idePessoaFisica = :idePessoaFisica"),
    @NamedQuery(name = "PessoaFisicaHistorico.findByIdeUsuarioAlteracao", query = "SELECT p FROM PessoaFisicaHistorico p WHERE p.ideUsuarioAlteracao = :ideUsuarioAlteracao"),
    @NamedQuery(name = "PessoaFisicaHistorico.findBydesEmail", query = "SELECT p FROM PessoaFisicaHistorico p WHERE p.desEmail = :desEmail"),
    @NamedQuery(name = "PessoaFisicaHistorico.findByNomPessoa", query = "SELECT p FROM PessoaFisicaHistorico p WHERE p.nomPessoa = :nomPessoa")})
public class PessoaFisicaHistorico implements Serializable {
    private static final long serialVersionUID = 1L;
    
	@Id
    @SequenceGenerator(name="PESSOA_FISICA_HISTORICO_IDE_PESSOA_FISICA_HISTORICO_seq", sequenceName="PESSOA_FISICA_HISTORICO_IDE_PESSOA_FISICA_HISTORICO_seq", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PESSOA_FISICA_HISTORICO_IDE_PESSOA_FISICA_HISTORICO_seq")
    @Column(name = "ide_pessoa_fisica_historico", nullable = false)
    private Integer idePessoaFisicaHistorico;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_historico", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcHistorico;
    
    @Size(max = 200)
    @Column(name = "nom_pessoa", length = 200)
    private String nomPessoa;
    
    @Size(max = 70)
    @Column(name = "des_email", length = 70)
    private String desEmail;
    
    @JoinColumn(name = "ide_pessoa_fisica", referencedColumnName = "ide_pessoa_fisica", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PessoaFisica idePessoaFisica;
    
    @JoinColumn(name = "ide_usuario_alteracao", referencedColumnName = "ide_pessoa_fisica", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Usuario ideUsuarioAlteracao;
    
    @JoinColumn(name = "ide_procurador_pessoa_fisica", referencedColumnName = "ide_procurador_pessoa_fisica", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private ProcuradorPessoaFisica ideProcuradorPessoaFisica;
    
    @Column(name = "status_procurador")
    private Boolean statusProcurador;
    

    public PessoaFisicaHistorico() {
    }

    public PessoaFisicaHistorico(Integer idePessoaFisicaHistorico) {
        this.idePessoaFisicaHistorico = idePessoaFisicaHistorico;
    }

    public PessoaFisicaHistorico(Integer idePessoaFisicaHistorico, String nomPessoa, String desEmail, PessoaFisica idePessoaFisica, Usuario ideUsuarioAlteracao, Date dtcHistorico) {
        this.idePessoaFisicaHistorico = idePessoaFisicaHistorico;
        this.idePessoaFisica = idePessoaFisica;
        this.nomPessoa = nomPessoa;
        this.desEmail = desEmail;
        this.ideUsuarioAlteracao = ideUsuarioAlteracao;
        this.dtcHistorico = dtcHistorico;
    }

    public Integer getIdePessoaFisicaHistorico() {
        return idePessoaFisicaHistorico;
    }

    public void setIdePessoaFisicaHistorico(Integer idePessoaFisicaHistorico) {
        this.idePessoaFisicaHistorico = idePessoaFisicaHistorico;
    }

    public Date getDtcHistorico() {
        return dtcHistorico;
    }

    public void setDtcHistorico(Date dtcHistorico) {
        this.dtcHistorico = dtcHistorico;
    }

    public PessoaFisica getIdePessoaFisica() {
        return idePessoaFisica;
    }

    public void setIdePessoaFisica(PessoaFisica idePessoaFisica) {
        this.idePessoaFisica = idePessoaFisica;
    }
    
    public String getDesEmail() {
		return desEmail;
	}

	public void setDesEmail(String desEmail) {
		this.desEmail = desEmail;
	}
    
    public Usuario getIdeUsuarioAlteracao() {
		return ideUsuarioAlteracao;
	}

	public void setIdeUsuarioAlteracao(Usuario ideUsuarioAlteracao) {
		this.ideUsuarioAlteracao = ideUsuarioAlteracao;
	}
	
	public String getNomPessoa() {
		return nomPessoa;
	}

	public void setNomPessoa(String nomPessoa) {
		this.nomPessoa = nomPessoa;
	}
	
	public ProcuradorPessoaFisica getIdeProcuradorPessoaFisica() {
		return ideProcuradorPessoaFisica;
	}

	public void setIdeProcuradorPessoaFisica(ProcuradorPessoaFisica ideProcuradorPessoaFisica) {
		this.ideProcuradorPessoaFisica = ideProcuradorPessoaFisica;
	}

	public Boolean getStatusProcurador() {
		return statusProcurador;
	}

	public void setStatusProcurador(Boolean statusProcurador) {
		this.statusProcurador = statusProcurador;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (idePessoaFisicaHistorico != null ? idePessoaFisicaHistorico.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        
        if (!(object instanceof PessoaFisicaHistorico)) {
            return false;
        }
        PessoaFisicaHistorico other = (PessoaFisicaHistorico) object;
        if ((this.idePessoaFisicaHistorico == null && other.idePessoaFisicaHistorico != null) || (this.idePessoaFisicaHistorico != null && !this.idePessoaFisicaHistorico.equals(other.idePessoaFisicaHistorico))) {
            return false;
        }
        return true;
    }


	@Override
    public String toString() {
        return "br.gov.ba.seia.entity.PessoaFisicaHistorico[ idePessoaFisicaHistorico=" + idePessoaFisicaHistorico + " ]";
    }
    
}
