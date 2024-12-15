package br.gov.ba.seia.entity;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.interfaces.Auditoria;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.auditoria.AuditoriaUtil;
import flexjson.JSON;

@Entity
@Table(name = "pessoa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pessoa.findAll", query = "SELECT p FROM Pessoa p"),
    @NamedQuery(name = "Pessoa.findByIdePessoa", query = "SELECT p FROM Pessoa p WHERE p.idePessoa = :idePessoa"),
    @NamedQuery(name = "Pessoa.findByDtcCriacao", query = "SELECT p FROM Pessoa p WHERE p.dtcCriacao = :dtcCriacao"),
    @NamedQuery(name = "Pessoa.findByIndExcluido", query = "SELECT p FROM Pessoa p WHERE p.indExcluido = :indExcluido"),
    @NamedQuery(name = "Pessoa.findByDesEmail", query = "SELECT p FROM Pessoa p WHERE p.desEmail = :desEmail"),
    @NamedQuery(name = "Pessoa.findByDtcExclusao", query = "SELECT p FROM Pessoa p WHERE p.dtcExclusao = :dtcExclusao"),
    @NamedQuery(name = "Pessoa.remove", query = "UPDATE Pessoa p SET p.indExcluido = true, p.dtcExclusao = :dtcExclusao, p.idePessoaFisicaUsuario = :idePessoaFisicaUsuario, p.enderecoIp = :enderecoIp, p.caminhoRequisicao = :caminhoRequisicao  WHERE p.idePessoa = :idePessoa"),
})
public class Pessoa extends AbstractEntity implements Auditoria {
    
	private static final long serialVersionUID = 1L;
    
	@Id  
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="PESSOA_IDE_PESSOA_seq")    
    @SequenceGenerator(name="PESSOA_IDE_PESSOA_seq", sequenceName="PESSOA_IDE_PESSOA_seq", allocationSize=1)
    @Column(name = "ide_pessoa", nullable = false)
    private Integer idePessoa;
    
    @Column(name = "dtc_criacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcCriacao;
    
    @Column(name = "ind_excluido", nullable = false)
    private boolean indExcluido;
    
	@Column(name = "des_email", length = 70)
    private String desEmail;
    
	@Column(name = "dtc_exclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtcExclusao;
	
	@OneToOne(mappedBy = "pessoa", fetch = FetchType.LAZY)
	private PessoaFisica pessoaFisica;

	@OneToOne(mappedBy = "pessoa", fetch = FetchType.LAZY)
	private PessoaJuridica pessoaJuridica;
    
	@ManyToMany(mappedBy="pessoaCollection", fetch = FetchType.LAZY)
    private Collection<Telefone> telefoneCollection;
    
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idePessoa", fetch = FetchType.LAZY)
    private Collection<BoletoPagamentoRequerimento> boletoPagamentoRequerimentoCollection;
    
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idePessoa", fetch = FetchType.LAZY)
    private Collection<DocumentoIdentificacao> documentoIdentificacaoCollection;
    
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idePessoa", fetch = FetchType.LAZY)
    private Collection<ParticipacaoAcionaria> participacaoAcionariaCollection;
    
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idePessoa", fetch = FetchType.LAZY)
    private Collection<PessoaImovel> pessoaImovelCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idePessoaUpload", fetch = FetchType.LAZY)
    private Collection<DocumentoObrigatorioRequerimento> documentoObrigatorioRequerimentoCollection;
    
    @OneToMany(mappedBy = "idePessoaValidacao", fetch = FetchType.LAZY)
    private Collection<DocumentoObrigatorioRequerimento> documentoObrigatorioRequerimentoCollection1;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idePessoa", fetch = FetchType.LAZY)
    private Collection<EnderecoPessoa> enderecoPessoaCollection;
    
    @OneToMany(mappedBy = "idePessoa", fetch = FetchType.LAZY)
    private Collection<Enquadramento> enquadramentoCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idePessoa", fetch = FetchType.LAZY)
    private Collection<TramitacaoRequerimento> tramitacaoRequerimentoCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idePessoaUpload", fetch = FetchType.LAZY)
    private Collection<ComprovantePagamento> comprovantePagamentoCollection;
    
    @OneToMany(mappedBy = "idePessoa", fetch = FetchType.LAZY)
    private Collection<Empreendimento> empreendimentoCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pessoa", fetch = FetchType.LAZY)
    private Collection<RequerimentoPessoa> requerimentoPessoaCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideProprietario", fetch = FetchType.LAZY)
    private Collection<ImovelRuralMudancaStatusJustificativa> imovelRuralMudancaStatusJustificativaCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ideRequerenteCadastro", fetch = FetchType.LAZY)
	private Collection<ImovelRural> imovelRuralCollection;
    
    @Column(name="ide_usuario")
    private Integer idePessoaFisicaUsuario;
    
    @Column(name="endereco_ip")
    private String enderecoIp;
    
    @Column(name="caminho_requisicao")
    private String caminhoRequisicao;
    
	@Transient
    private String nomeRazao;
	
	@Transient
    private String cpfCnpj;
    
	@Transient
    private Endereco endereco;
    
    @Transient
    private List<Telefone> listaTelefoneTerceiros;
    
    @Transient
    private Collection<RepresentanteLegal> representanteLegalCollection;
    
    @Transient
    private String dscConfirmacaoEmail;
    
    @Transient
    private Empreendimento empreendimento;
    
    public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}

	public Pessoa() {}
    
    public Pessoa(Integer idePessoa) {
        this.idePessoa = idePessoa;
    }
    
    public Pessoa(Integer idePessoa, PessoaFisica pessoaFisica) {
    	this.idePessoa = idePessoa;
    	this.pessoaFisica = pessoaFisica;
    }
    
    public Pessoa(Integer idePessoa, PessoaJuridica pessoaJuridica) {
    	this.idePessoa = idePessoa;
    	this.pessoaJuridica = pessoaJuridica;
    }
    
    public Pessoa(Integer idePessoa, Date dtcCriacao, boolean indExcluido) {
        this.idePessoa = idePessoa;
        this.dtcCriacao = dtcCriacao;
        this.indExcluido = indExcluido;
    }
    
    public Integer getIdePessoa() {
        return idePessoa;
    }
    
    public void setIdePessoa(Integer idePessoa) {
        this.idePessoa = idePessoa;
    }
    
    @JSON(include = false)
    public Date getDtcCriacao() {
        return dtcCriacao;
    }
    
    public void setDtcCriacao(Date dtcCriacao) {
        this.dtcCriacao = dtcCriacao;
    }
    
    @JSON(include = false)
    public boolean getIndExcluido() {
        return indExcluido;
    }
    
    public void setIndExcluido(boolean indExcluido) {
        this.indExcluido = indExcluido;
    }
    
  
    @JSON(include = false)
    public String getDesEmail() {
        return desEmail;
    }
    
    public void setDesEmail(String desEmail) {
        this.desEmail = desEmail;
    }
    
    
    @JSON(include = false)
    public Date getDtcExclusao() {
        return dtcExclusao;
    }
    
    public void setDtcExclusao(Date dtcExclusao) {
        this.dtcExclusao = dtcExclusao;
    }
    
    @XmlTransient
    public Collection<Telefone> getTelefoneCollection() {
        return telefoneCollection;
    }
    
    public void setTelefoneCollection(Collection<Telefone> telefoneCollection) {
        this.telefoneCollection = telefoneCollection;
    }
    
    @XmlTransient
    public Collection<BoletoPagamentoRequerimento> getBoletoPagamentoRequerimentoCollection() {
        return boletoPagamentoRequerimentoCollection;
    }
    
    public void setBoletoPagamentoRequerimentoCollection(Collection<BoletoPagamentoRequerimento> boletoPagamentoRequerimentoCollection) {
        this.boletoPagamentoRequerimentoCollection = boletoPagamentoRequerimentoCollection;
    }
    
    @XmlTransient
    public Collection<DocumentoIdentificacao> getDocumentoIdentificacaoCollection() {
        return documentoIdentificacaoCollection;
    }
    
    public void setDocumentoIdentificacaoCollection(Collection<DocumentoIdentificacao> documentoIdentificacaoCollection) {
        this.documentoIdentificacaoCollection = documentoIdentificacaoCollection;
    }
    
    @XmlTransient
    public Collection<ParticipacaoAcionaria> getParticipacaoAcionariaCollection() {
        return participacaoAcionariaCollection;
    }
    
    public void setParticipacaoAcionariaCollection(Collection<ParticipacaoAcionaria> participacaoAcionariaCollection) {
        this.participacaoAcionariaCollection = participacaoAcionariaCollection;
    }
    
    @XmlTransient
    public Collection<PessoaImovel> getPessoaImovelCollection() {
        return pessoaImovelCollection;
    }
    
    public void setPessoaImovelCollection(Collection<PessoaImovel> pessoaImovelCollection) {
        this.pessoaImovelCollection = pessoaImovelCollection;
    }
    
    @XmlTransient
    public Collection<DocumentoObrigatorioRequerimento> getDocumentoObrigatorioRequerimentoCollection() {
        return documentoObrigatorioRequerimentoCollection;
    }
    
    public void setDocumentoObrigatorioRequerimentoCollection(Collection<DocumentoObrigatorioRequerimento> documentoObrigatorioRequerimentoCollection) {
        this.documentoObrigatorioRequerimentoCollection = documentoObrigatorioRequerimentoCollection;
    }
    
    @XmlTransient
    public Collection<DocumentoObrigatorioRequerimento> getDocumentoObrigatorioRequerimentoCollection1() {
        return documentoObrigatorioRequerimentoCollection1;
    }
    
    public void setDocumentoObrigatorioRequerimentoCollection1(Collection<DocumentoObrigatorioRequerimento> documentoObrigatorioRequerimentoCollection1) {
        this.documentoObrigatorioRequerimentoCollection1 = documentoObrigatorioRequerimentoCollection1;
    }
    
    @XmlTransient
    public Collection<EnderecoPessoa> getEnderecoPessoaCollection() {
        return enderecoPessoaCollection;
    }
    
    public void setEnderecoPessoaCollection(Collection<EnderecoPessoa> enderecoPessoaCollection) {
        this.enderecoPessoaCollection = enderecoPessoaCollection;
    }
    
    @XmlTransient
    public Collection<Enquadramento> getEnquadramentoCollection() {
        return enquadramentoCollection;
    }
    
    public void setEnquadramentoCollection(Collection<Enquadramento> enquadramentoCollection) {
        this.enquadramentoCollection = enquadramentoCollection;
    }
    
    @XmlTransient
    public Collection<TramitacaoRequerimento> getTramitacaoRequerimentoCollection() {
        return tramitacaoRequerimentoCollection;
    }
    
    public void setTramitacaoRequerimentoCollection(Collection<TramitacaoRequerimento> tramitacaoRequerimentoCollection) {
        this.tramitacaoRequerimentoCollection = tramitacaoRequerimentoCollection;
    }
    
    @JSON(include = false)
    public PessoaFisica getPessoaFisica() {
        return pessoaFisica;
    }
    
    public void setPessoaFisica(PessoaFisica pessoaFisica) {
        this.pessoaFisica = pessoaFisica;
    }
    
    @XmlTransient
    public Collection<ComprovantePagamento> getComprovantePagamentoCollection() {
        return comprovantePagamentoCollection;
    }
    
    public void setComprovantePagamentoCollection(Collection<ComprovantePagamento> comprovantePagamentoCollection) {
        this.comprovantePagamentoCollection = comprovantePagamentoCollection;
    }
    
    @XmlTransient
    public Collection<Empreendimento> getEmpreendimentoCollection() {
        return empreendimentoCollection;
    }
    
    public void setEmpreendimentoCollection(Collection<Empreendimento> empreendimentoCollection) {
        this.empreendimentoCollection = empreendimentoCollection;
    }
    
    @XmlTransient
    public Collection<RequerimentoPessoa> getRequerimentoPessoaCollection() {
        return requerimentoPessoaCollection;
    }
    
    public void setRequerimentoPessoaCollection(Collection<RequerimentoPessoa> requerimentoPessoaCollection) {
        this.requerimentoPessoaCollection = requerimentoPessoaCollection;
    }
        
	public Collection<ImovelRuralMudancaStatusJustificativa> getImovelRuralMudancaStatusJustificativaCollection() {
		return imovelRuralMudancaStatusJustificativaCollection;
	}

	public void setImovelRuralMudancaStatusJustificativaCollection(
			Collection<ImovelRuralMudancaStatusJustificativa> imovelRuralMudancaStatusJustificativaCollection) {
		this.imovelRuralMudancaStatusJustificativaCollection = imovelRuralMudancaStatusJustificativaCollection;
	}

	@JSON(include = false)
    public PessoaJuridica getPessoaJuridica() {
        return pessoaJuridica;
    }
    
    public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
        this.pessoaJuridica = pessoaJuridica;
    }
    
    @JSON(include = false)
    public Endereco getEndereco() {
		return endereco;
	}
    
	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}
	
	public String getDscConfirmacaoEmail() {
		return dscConfirmacaoEmail;
	}

	public void setDscConfirmacaoEmail(String dscConfirmacaoEmail) {
		this.dscConfirmacaoEmail = dscConfirmacaoEmail;
	}

	@Transient
	public boolean isValidaEmail() {

		if (getDesEmail().equals(getDscConfirmacaoEmail())){
			return true;
		}else{
			MensagemUtil.erro("Favor informar emails iguais!");
		}
		return false;
	}  
	
    /**
    * Esses metodos sao para centralizar os acessos 'comuns' mas diferenciados de pessoa fisica e juridica.
	*/
    public String getNomeRazao() {
    	if(!Util.isNull(this.getPessoaFisica()) && !Util.isNullOuVazio(this.getPessoaFisica().getNomPessoa())) {
    		return this.getPessoaFisica().getNomPessoa();
    	}
    	else if(!Util.isNull(this.getPessoaJuridica()) && !Util.isNullOuVazio(this.getPessoaJuridica().getNomRazaoSocial())) {
    		return this.getPessoaJuridica().getNomRazaoSocial();
    	}
    	else {
    		return "";
    	}
    }
	
	public void setNomeRazao(String nomeRazao) {
		this.nomeRazao = nomeRazao;
	}
	
	@JSON(include = false)
	public String getCpfCnpj() {
		
		if(!Util.isNull(this.getPessoaFisica())) {
			return this.getPessoaFisica().getNumCpf();
			
		} else if(!Util.isNull(this.getPessoaJuridica())) {
			return this.getPessoaJuridica().getNumCnpj();
		}
		
		return "";
	}
	
	@JSON(include = false)
	public String getCpfCnpjFormatado() {
		
		if(pessoaFisica != null && !Util.isNullOuVazio(pessoaFisica.getNumCpf())) {
    		return Util.formatarCPF(pessoaFisica.getNumCpf());
    		
    	} else if(pessoaJuridica != null && !Util.isNullOuVazio(pessoaJuridica.getNumCnpj())) {
			return Util.formatarCNPJ(pessoaJuridica.getNumCnpj().replaceAll("[./-]", ""));
    	}
		
		return "";
	}
	
	@JSON(include = false)
	public String getTelefones() {
		StringBuilder retorno = new StringBuilder();
		if(!Util.isNullOuVazio(telefoneCollection)) {
			for (Iterator<Telefone> iterator = telefoneCollection.iterator(); iterator.hasNext();) {
				Telefone telefone = (Telefone) iterator.next();
				if(iterator.hasNext()) {
					retorno.append(telefone.getNumTelefone()+", ");
				}
				else{
					retorno.append(telefone.getNumTelefone());
				}
			}
		}
		return retorno.toString();
	}
	
	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}
	
	@JSON(include = false)
	public List<Telefone> getListaTelefoneTerceiros() {
		return listaTelefoneTerceiros;
	}
	
	public void setListaTelefoneTerceiros(List<Telefone> listaTelefoneTerceiros) {
		this.listaTelefoneTerceiros = listaTelefoneTerceiros;
	}
	
	@JSON(include = false)
	public boolean isPF(){
		return !Util.isNull(pessoaFisica);
	}
	
	@JSON(include = false)
	public boolean isPJ(){
		return !Util.isNull(pessoaJuridica);
	}

	public Collection<ImovelRural> getImovelRuralCollection() {
		return imovelRuralCollection;
	}

	public void setImovelRuralCollection(Collection<ImovelRural> imovelRuralCollection) {
		this.imovelRuralCollection = imovelRuralCollection;
	}

	public Collection<RepresentanteLegal> getRepresentanteLegalCollection() {
		return representanteLegalCollection;
	}

	public void setRepresentanteLegalCollection(Collection<RepresentanteLegal> representanteLegalCollection) {
		this.representanteLegalCollection = representanteLegalCollection;
	}

	@Override
	public Integer getIdePessoaFisicaUsuario() {
		return idePessoaFisicaUsuario;
	}

	@Override
	public void setIdePessoaFisicaUsuario(Integer idePessoaFisicaUsuario) {
		this.idePessoaFisicaUsuario = idePessoaFisicaUsuario;
		
	}

	@Override
	public String getEnderecoIp() {
		return enderecoIp;
	}

	@Override
	public void setEnderecoIp(String enderecoIp) {
		this.enderecoIp = enderecoIp;
		
	}

	@Override
	public String getCaminhoRequisicao() {
		return caminhoRequisicao;
	}

	@Override
	public void setCaminhoRequisicao(String caminhoRequisicao) {
		this.caminhoRequisicao = caminhoRequisicao;
		
	}

	@Override
	public void capturarCamposAuditoria() {
		if(Util.isNullOuVazio(AuditoriaUtil.obterUsuario())){
			setIdePessoaFisicaUsuario(0);
		}else{
			
			setIdePessoaFisicaUsuario(AuditoriaUtil.obterUsuario().getIdePessoaFisica());
		}
		setEnderecoIp(AuditoriaUtil.obterIp());
		setCaminhoRequisicao(AuditoriaUtil.obterCaminhoPaginaRequisicao());
	}	
}