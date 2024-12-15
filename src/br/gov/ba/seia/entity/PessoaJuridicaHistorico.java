package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
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

import br.gov.ba.seia.util.Util;

/**
 *
 * @author carlos.sousa
 */
@Entity
@Table(name = "pessoa_juridica_historico")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PessoaJuridicaHistorico.findAll", query = "SELECT p FROM PessoaJuridicaHistorico p"),
    @NamedQuery(name = "PessoaJuridicaHistorico.findByIdePessoaJuridicaHistorico", query = "SELECT p FROM PessoaJuridicaHistorico p WHERE p.idePessoaJuridicaHistorico = :idePessoaJuridicaHistorico"),
    @NamedQuery(name = "PessoaJuridicaHistorico.findByNomRazaosocial", query = "SELECT p FROM PessoaJuridicaHistorico p WHERE p.nomRazaoSocial = :nomRazaoSocial"),
    @NamedQuery(name = "PessoaJuridicaHistorico.findBynomeFantasia", query = "SELECT p FROM PessoaJuridicaHistorico p WHERE p.nomeFantasia = :nomeFantasia"),
    @NamedQuery(name = "PessoaJuridicaHistorico.findByDtcAbertura", query = "SELECT p FROM PessoaJuridicaHistorico p WHERE p.dtcAbertura = :dtcAbertura"),
    @NamedQuery(name = "PessoaJuridicaHistorico.findByNomNaturezaJuridica", query = "SELECT p FROM PessoaJuridicaHistorico p WHERE p.nomNaturezaJuridica = :nomNaturezaJuridica"),
    @NamedQuery(name = "PessoaJuridicaHistorico.findByDtcHistorico", query = "SELECT p FROM PessoaJuridicaHistorico p WHERE p.dtcHistorico = :dtcHistorico")})
public class PessoaJuridicaHistorico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
	@SequenceGenerator(name = "PESSOA_JURIDICA_HISTORICO_IDE_PESSOA_JURIDICA_HISTORICO_GENERATOR", sequenceName = "PESSOA_JURIDICA_HISTORICO_IDE_PESSOA_JURIDICA_HISTORICO_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PESSOA_JURIDICA_HISTORICO_IDE_PESSOA_JURIDICA_HISTORICO_GENERATOR")
	@Column(name = "ide_pessoa_juridica_historico", unique = true, nullable = false)
    private Integer idePessoaJuridicaHistorico;
    
    @Size(max = 200)
    @Column(name = "nom_razao_social", length = 200)
    private String nomRazaoSocial;
    
    @Size(min = 1, max = 200)
    @Column(name = "nom_nome_fantasia", length = 200)
    private String nomeFantasia;
    
    @Column(name = "dtc_abertura")
    @Temporal(TemporalType.DATE)
    private Date dtcAbertura;
    
    @Size(min = 1, max = 100)
    @Column(name = "nom_natureza_juridica", length = 100)
    private String nomNaturezaJuridica;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "dtc_historico", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcHistorico;
    
    @Column(name = "num_inscricao_municipal", length = 30)
	private String numInscricaoMunicipal;
    
	@Column(name = "num_inscricao_estadual", length = 30)
	private String numInscricaoEstadual;
	
	@Column(name = "dsc_caminho_arquivo", length = 1000)
	private String dscCaminhoArquivo;
	
    @JoinColumn(name = "ide_pessoa_juridica", referencedColumnName = "ide_pessoa_juridica", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PessoaJuridica idePessoaJuridica;
    
    @JoinColumn(name = "ide_processo", referencedColumnName = "ide_processo")
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Processo ideProcesso;
    
    @JoinColumn(name = "ide_usuario_alteracao", referencedColumnName = "ide_pessoa_fisica", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Usuario ideUsuarioAlteracao;
    
    @JoinColumn(name = "ide_procurador_representante", referencedColumnName = "ide_procurador_representante")
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private ProcuradorRepresentante ideProcuradorRepresentante;
    
    @JoinColumn(name = "ide_representante_legal", referencedColumnName = "ide_representante_legal")
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private RepresentanteLegal ideRepresentanteLegal;
    
    @Column(name = "status_procurador")
    private Boolean statusProcurador;
    
    @Column(name = "status_representante")
    private Boolean statusRepresentante;
    

    public PessoaJuridicaHistorico() {
    }

    public PessoaJuridicaHistorico(Integer idePessoaJuridicaHistorico) {
		this.idePessoaJuridicaHistorico = idePessoaJuridicaHistorico;
    }

    public PessoaJuridicaHistorico(Integer idePessoaJuridicaHistorico, String nomRazaoSocial, String nomeFantasia, Date dtcAbertura, String nomNaturezaJuridica, Date dtcHistorico) {
		this.idePessoaJuridicaHistorico = idePessoaJuridicaHistorico;
		this.nomRazaoSocial = nomRazaoSocial;
		this.nomeFantasia = nomeFantasia;
		this.dtcAbertura = dtcAbertura;
		this.nomNaturezaJuridica = nomNaturezaJuridica;
		this.dtcHistorico = dtcHistorico;
    }
    
	public PessoaJuridicaHistorico(PessoaJuridica pj, Processo p) {
		this.idePessoaJuridica = pj;
		this.nomRazaoSocial = pj.getNomRazaoSocial();
		this.nomeFantasia = pj.getNomeFantasia();
		this.dtcAbertura = pj.getDtcAbertura();
		this.nomNaturezaJuridica = pj.getIdeNaturezaJuridica().getNomNaturezaJuridica();
		this.dtcHistorico = new Date();
		this.numInscricaoEstadual = pj.getNumInscricaoEstadual();
		this.numInscricaoMunicipal = pj.getNumInscricaoMunicipal();
		this.dscCaminhoArquivo = pj.getDscCaminhoArquivo();
		this.ideProcesso = p;
    }

    public Integer getIdePessoaJuridicaHistorico() {
		return idePessoaJuridicaHistorico;
    }

    public void setIdePessoaJuridicaHistorico(Integer idePessoaJuridicaHistorico) {
		this.idePessoaJuridicaHistorico = idePessoaJuridicaHistorico;
    }

    public String getNomRazaosocial() {
		return nomRazaoSocial;
    }

    public void setNomRazaoSocial(String nomRazaoSocial) {
		this.nomRazaoSocial = nomRazaoSocial;
    }

    public String getNomeFantasia() {
		return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
    }

    public Date getDtcAbertura() {
		return dtcAbertura;
    }

    public void setDtcAbertura(Date dtcAbertura) {
		this.dtcAbertura = dtcAbertura;
    }

    public String getNomNaturezaJuridica() {
		return nomNaturezaJuridica;
    }

    public void setNomNaturezaJuridica(String nomNaturezaJuridica) {
		this.nomNaturezaJuridica = nomNaturezaJuridica;
    }

    public Date getDtcHistorico() {
		return dtcHistorico;
    }
    
    public String getDtcHistoricoFormatada() {
		if(!Util.isNullOuVazio(dtcHistorico)){
			SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");  
			return spf.format(dtcHistorico);
		}else
			return new String("");
    }
	
    public void setDtcHistorico(Date dtcHistorico) {
		this.dtcHistorico = dtcHistorico;
    }

    public PessoaJuridica getIdePessoaJuridica() {
		return idePessoaJuridica;
    }

    public void setIdePessoaJuridica(PessoaJuridica idePessoaJuridica) {
		this.idePessoaJuridica = idePessoaJuridica;
    }

    public String getNumInscricaoMunicipal() {
		return numInscricaoMunicipal;
	}

	public void setNumInscricaoMunicipal(String numInscricaoMunicipal) {
		this.numInscricaoMunicipal = numInscricaoMunicipal;
	}

	public String getNumInscricaoEstadual() {
		return numInscricaoEstadual;
	}

	public void setNumInscricaoEstadual(String numInscricaoEstadual) {
		this.numInscricaoEstadual = numInscricaoEstadual;
	}

	public String getDscCaminhoArquivo() {
		return dscCaminhoArquivo;
	}

	public void setDscCaminhoArquivo(String dscCaminhoArquivo) {
		this.dscCaminhoArquivo = dscCaminhoArquivo;
	}

	public Usuario getIdeUsuarioAlteracao() {
		return ideUsuarioAlteracao;
	}

	public void setIdeUsuarioAlteracao(Usuario ideUsuarioAlteracao) {
		this.ideUsuarioAlteracao = ideUsuarioAlteracao;
	}
	
	public ProcuradorRepresentante getIdeProcuradorRepresentante() {
		return ideProcuradorRepresentante;
	}

	public void setIdeProcuradorRepresentante(ProcuradorRepresentante ideProcuradorRepresentante) {
		this.ideProcuradorRepresentante = ideProcuradorRepresentante;
	}

	public RepresentanteLegal getIdeRepresentanteLegal() {
		return ideRepresentanteLegal;
	}

	public void setIdeRepresentanteLegal(RepresentanteLegal ideRepresentanteLegal) {
		this.ideRepresentanteLegal = ideRepresentanteLegal;
	}
	
	public String getNomRazaoSocial() {
		return nomRazaoSocial;
	}

	public Boolean getStatusProcurador() {
		return statusProcurador;
	}

	public void setStatusProcurador(Boolean statusProcurador) {
		this.statusProcurador = statusProcurador;
	}

	public Boolean getStatusRepresentante() {
		return statusRepresentante;
	}

	public void setStatusRepresentante(Boolean statusRepresentante) {
		this.statusRepresentante = statusRepresentante;
	}

	@Override
    public int hashCode() {
		int hash = 0;
		hash += (idePessoaJuridicaHistorico != null ? idePessoaJuridicaHistorico.hashCode() : 0);
		return hash;
    }

    @Override
    public boolean equals(Object object) {
		
		if (!(object instanceof PessoaJuridicaHistorico)) {
		    return false;
		}
		PessoaJuridicaHistorico other = (PessoaJuridicaHistorico) object;
		if ((this.idePessoaJuridicaHistorico == null && other.idePessoaJuridicaHistorico != null) || (this.idePessoaJuridicaHistorico != null && !this.idePessoaJuridicaHistorico.equals(other.idePessoaJuridicaHistorico))) {
		    return false;
		}
		return true;
    }

    @Override
    public String toString() {
		return "br.gov.ba.seia.entity.PessoaJuridicaHistorico[ idePessoaJuridicaHistorico=" + idePessoaJuridicaHistorico + " ]";
    }

	/**
	 * @return the ideProcesso
	 */
	public Processo getIdeProcesso() {
		return ideProcesso;
	}

	/**
	 * @param ideProcesso the ideProcesso to set
	 */
	public void setIdeProcesso(Processo ideProcesso) {
		this.ideProcesso = ideProcesso;
	}
    
}
