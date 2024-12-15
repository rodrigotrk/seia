package br.gov.ba.seia.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "responsavel_empreendimento")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ResponsavelEmpreendimento.findAll", query = "SELECT r FROM ResponsavelEmpreendimento r"),
    @NamedQuery(name = "ResponsavelEmpreendimento.findByIdeResponsavelEmpreendimento", query = "SELECT r FROM ResponsavelEmpreendimento r WHERE r.ideResponsavelEmpreendimento = :ideResponsavelEmpreendimento"),
    @NamedQuery(name = "ResponsavelEmpreendimento.findByDtcCriacao", query = "SELECT r FROM ResponsavelEmpreendimento r WHERE r.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "ResponsavelEmpreendimento.findByIndExcluido", query = "SELECT r FROM ResponsavelEmpreendimento r WHERE r.indExcluido = :indExcluido"),
    @NamedQuery(name = "ResponsavelEmpreendimento.findByDtcExclusao", query = "SELECT r FROM ResponsavelEmpreendimento r WHERE r.dtcExclusao = :dtcExclusao")})
public class ResponsavelEmpreendimento implements Serializable {
    
	private static final long serialVersionUID = 1L;
    
	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RESPONSAVEL_EMPREENDIMENTO_IDE_RESPONSAVEL_EMPREENDIMENTO_seq")    
    @SequenceGenerator(name="RESPONSAVEL_EMPREENDIMENTO_IDE_RESPONSAVEL_EMPREENDIMENTO_seq", sequenceName="RESPONSAVEL_EMPREENDIMENTO_IDE_RESPONSAVEL_EMPREENDIMENTO_seq", allocationSize=1)
    @Basic(optional = false)
    @NotNull
    @Column(name = "ide_responsavel_empreendimento", nullable = false)
    private Integer ideResponsavelEmpreendimento;
    
	@Basic(optional = false)
    @NotNull
    @Column(name = "dtc_criacao", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dtcCriacao;
    
	@Basic(optional = false)
    @NotNull
    @Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;
    
	@Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;
	
	@Column(name = "num_art", nullable = true, length = 15)
	private String numART;
	
	@Column(name = "dsc_caminho_arquivo_art", nullable = true, length = 500)
	private String dscCaminhoArquivoART;
    
	@JoinColumn(name = "ide_pessoa_fisica", referencedColumnName = "ide_pessoa_fisica", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PessoaFisica idePessoaFisica;
    
	@JoinColumn(name = "ide_empreendimento", referencedColumnName = "ide_empreendimento", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Empreendimento ideEmpreendimento;
	
	@OneToMany(mappedBy = "ideResponsavelEmpreendimento", fetch = FetchType.LAZY)
	private List<CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento> cadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento; 
	
	@Transient
	private boolean selecionado;
	
	@Transient
	private boolean isPossuiART;
	
	@Transient
	private List<String> listaCaminhoArquivoParaExcluir;
	
	/*********************
	 * 					 *
	 * XXX: CONSTRUTORES *
	 * 					 *
	 *********************/
	
    public ResponsavelEmpreendimento() {}

    public ResponsavelEmpreendimento(Integer ideResponsavelEmpreendimento) {
        this.ideResponsavelEmpreendimento = ideResponsavelEmpreendimento;
    }

    public ResponsavelEmpreendimento(Integer ideResponsavelEmpreendimento, Date dtcCriacao, boolean indExcluido) {
        this.ideResponsavelEmpreendimento = ideResponsavelEmpreendimento;
        this.dtcCriacao = dtcCriacao;
        this.indExcluido = indExcluido;
    }

    /********************
	 * 					*
	 * XXX: AUXILIARES	*
	 * 					*
	 ********************/
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ideResponsavelEmpreendimento != null ? ideResponsavelEmpreendimento.hashCode() : 0);
        return hash;
    }

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ResponsavelEmpreendimento)) {
			return false;
		}
		ResponsavelEmpreendimento other = (ResponsavelEmpreendimento) object;
		if ((this.ideResponsavelEmpreendimento == null && other.ideResponsavelEmpreendimento != null)
				|| (this.ideResponsavelEmpreendimento != null && !this.ideResponsavelEmpreendimento.equals(other.ideResponsavelEmpreendimento))) {
			return false;
		}
		return true;
	}
    
    @Override
    public String toString() {
        return "br.gov.ba.seia.entity.ResponsavelEmpreendimento[ ideResponsavelEmpreendimento=" + ideResponsavelEmpreendimento + " ]";
    }
    
    /************************
	 *						* 
	 * XXX: GETS AND SETS	*
	 * 				 		*
	 ************************/

    public Integer getIdeResponsavelEmpreendimento() {
        return ideResponsavelEmpreendimento;
    }

    public void setIdeResponsavelEmpreendimento(Integer ideResponsavelEmpreendimento) {
        this.ideResponsavelEmpreendimento = ideResponsavelEmpreendimento;
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

    public PessoaFisica getIdePessoaFisica() {
        return idePessoaFisica;
    }

    public void setIdePessoaFisica(PessoaFisica idePessoaFisica) {
        this.idePessoaFisica = idePessoaFisica;
    }

    public Empreendimento getIdeEmpreendimento() {
        return ideEmpreendimento;
    }

    public void setIdeEmpreendimento(Empreendimento ideEmpreendimento) {
        this.ideEmpreendimento = ideEmpreendimento;
    }

	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

	public String getNumART() {
		return numART;
	}

	public void setNumART(String numART) {
		this.numART = numART;
	}

	public String getDscCaminhoArquivoART() {
		return dscCaminhoArquivoART;
	}

	public void setDscCaminhoArquivoART(String dscCaminhoArquivoART) {
		this.dscCaminhoArquivoART = dscCaminhoArquivoART;
	}

	public boolean isPossuiART() {
		return isPossuiART;
	}

	public void setPossuiART(boolean isPossuiART) {
		this.isPossuiART = isPossuiART;
	}

	public List<String> getListaCaminhoArquivoParaExcluir() {
		return listaCaminhoArquivoParaExcluir;
	}

	public void setListaCaminhoArquivoParaExcluir(List<String> listaCaminhoArquivoParaExcluir) {
		this.listaCaminhoArquivoParaExcluir = listaCaminhoArquivoParaExcluir;
	}

	public List<CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento> getCadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento() {
		return cadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento;
	}

	public void setCadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento(
			List<CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento> cadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento) {
		this.cadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento = cadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento;
	}

}
