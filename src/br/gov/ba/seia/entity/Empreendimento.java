package br.gov.ba.seia.entity;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.util.Util;
import flexjson.JSON;

@Entity
@Table(name = "empreendimento")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Empreendimento.findAll", query = "SELECT e FROM Empreendimento e where e.indExcluido = :indExcluido "),
		@NamedQuery(name = "Empreendimento.findByIdeEmpreendimento", query = "SELECT e FROM Empreendimento e WHERE e.ideEmpreendimento = :ideEmpreendimento"),
		
		@NamedQuery(name = "Empreendimento.findByIdeRequerimento", 
			query = "SELECT e FROM Empreendimento e "
					+ "inner join e.empreendimentoRequerimentoCollection er "
					+ "inner join er.ideRequerimento r WHERE r.ideRequerimento = :ideRequerimento"),
		
		@NamedQuery(name = "Empreendimento.findByNomEmpreendimento", query = "SELECT e FROM Empreendimento e WHERE e.nomEmpreendimento = :nomEmpreendimento"),
		@NamedQuery(name = "Empreendimento.findByValInvestimento", query = "SELECT e FROM Empreendimento e WHERE e.valInvestimento = :valInvestimento"),
		@NamedQuery(name = "Empreendimento.findByDtcCriacao", query = "SELECT e FROM Empreendimento e WHERE e.dtcCriacao = :dtcCriacao"),
		@NamedQuery(name = "Empreendimento.findByIndExcluido", query = "SELECT e FROM Empreendimento e WHERE e.indExcluido = :indExcluido"),
		@NamedQuery(name = "Empreendimento.findByQtdFuncionarios", query = "SELECT e FROM Empreendimento e WHERE e.qtdFuncionarios = :qtdFuncionarios"),
		@NamedQuery(name = "Empreendimento.findByDesEmail", query = "SELECT e FROM Empreendimento e WHERE e.desEmail = :desEmail"),
		@NamedQuery(name = "Empreendimento.findByNumCadastroTecnicoFederal", query = "SELECT e FROM Empreendimento e WHERE e.numCadastroTecnicoFederal = :numCadastroTecnicoFederal"),
		@NamedQuery(name = "Empreendimento.findByDtcValidadeCtf", query = "SELECT e FROM Empreendimento e WHERE e.dtcValidadeCtf = :dtcValidadeCtf"),
		@NamedQuery(name = "Empreendimento.findByIndCorrespondencia", query = "SELECT e FROM Empreendimento e WHERE e.indCorrespondencia = :indCorrespondencia"),
		@NamedQuery(name = "Empreendimento.findByDtcExclusao", query = "SELECT e FROM Empreendimento e WHERE e.dtcExclusao = :dtcExclusao"),
		@NamedQuery(name = "Empreendimento.findByIdePessoaAtivado", query = "SELECT e FROM Empreendimento e WHERE e.indExcluido = :indExcluido AND e.idePessoa.idePessoa = :idePessoa ORDER BY e.nomEmpreendimento"),
		@NamedQuery(name = "Empreendimento.findByIdePessoa", query = "SELECT e FROM Empreendimento e WHERE e.idePessoa.idePessoa = :idePessoa"),
		@NamedQuery(name = "Empreendimento.findByIdePessoaProcuradorPessoaFisica", query = "SELECT e FROM Empreendimento e left join e.procuradorPfEmpreendimentoCollection ppfe left join ppfe.ideProcuradorPessoaFisica ppf left join ppf.ideProcurador p left join e.idePessoa pe WHERE p.idePessoaFisica= :idePessoa and pe.idePessoa = :requerente"),
		@NamedQuery(name = "Empreendimento.findByIdePessoaProcuradorPessoaJuridica", query = "SELECT e FROM Empreendimento e left join e.procuradorRepEmpreendimentoCollection ppje left join ppje.ideProcuradorRepresentante ppj left join ppj.ideProcurador p left join e.idePessoa pe WHERE p.idePessoaFisica= :idePessoa and pe.idePessoa = :requerente"),
		@NamedQuery(name = "Empreendimento.findByRepresentanteLegal", query = "SELECT e FROM Empreendimento e inner join e.idePessoa p inner join p.pessoaJuridica pj inner join pj.representanteLegalCollection rl inner join rl.idePessoaFisica pf WHERE pf.idePessoaFisica = :idePessoa"),
		@NamedQuery(name = "Empreendimento.findByProcuradorPessoaFisica", query = "SELECT e FROM Empreendimento e inner join e.procuradorPfEmpreendimentoCollection ppfe inner join ppfe.ideProcuradorPessoaFisica ppf inner join ppf.ideProcurador p WHERE p.idePessoaFisica= :idePessoa"),
		@NamedQuery(name = "Empreendimento.atualizarLocGeoEmpreendimento", query = "UPDATE Empreendimento e SET e.ideLocalizacaoGeografica = :ideLocGeo where e.ideEmpreendimento = :ideEmpreendimento"),
		@NamedQuery(name = "Empreendimento.findByProcuradorPessoaJuridica", query = "SELECT e FROM Empreendimento e inner join e.procuradorRepEmpreendimentoCollection ppje inner join ppje.ideProcuradorRepresentante ppj inner join ppj.ideProcurador p WHERE p.idePessoaFisica= :idePessoa") })

public class Empreendimento extends AbstractEntity implements Cloneable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@SequenceGenerator(name = "EMPREENDIMENTO_IDEEMPREENDIMENTO_GENERATOR", sequenceName = "EMPREENDIMENTO_IDE_EMPREENDIMENTO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EMPREENDIMENTO_IDEEMPREENDIMENTO_GENERATOR")
	@Column(name = "ide_empreendimento", nullable = false)
	private Integer ideEmpreendimento;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "nom_empreendimento", nullable = false, length = 200)
	private String nomEmpreendimento;
	
	@Column(name = "val_investimento", nullable = true, precision = 14, scale = 2)
	private Double valInvestimento;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "dtc_criacao", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcCriacao;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "ind_excluido", nullable = false)
	private boolean indExcluido;
	
	@Column(name = "qtd_funcionarios")
	private Integer qtdFuncionarios;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "des_email", nullable = false, length = 70)
	private String desEmail;
	
	@Column(name = "num_cadastro_tecnico_federal", length = 15)
	private String numCadastroTecnicoFederal;
	
	@Column(name = "dtc_validade_ctf")
	@Temporal(TemporalType.DATE)
	private Date dtcValidadeCtf;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "ind_correspondencia", nullable = false)
	private boolean indCorrespondencia;
	
	@Column(name = "dtc_exclusao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcExclusao;

	@Basic(optional = false)
	@Column(name = "ind_endereco_correspondencia")
	private Boolean indEnderecoCorrespondencia;
	
	@Basic(optional = false)
	@Column(name = "ind_cessionario")
	private Boolean indCessionario;
	
	@Column(name = "ind_unidade_conservacao")
	private Boolean indUnidadeConservacao;
	
	@Column(name = "ind_conversao_tcra_lac")
	private Boolean indConversaoTcraLac;
	
	@Basic(optional = false)
	@Column(name = "ind_base_operacional")
	private Boolean indBaseOperacional;
	
	@JoinColumn(name = "ide_pessoa", referencedColumnName = "ide_pessoa")
	@ManyToOne(fetch = FetchType.LAZY)
	private Pessoa idePessoa;
	
	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica")
	@ManyToOne(fetch = FetchType.LAZY)
	private LocalizacaoGeografica ideLocalizacaoGeografica;
	
	@JoinTable(name = "imovel_empreendimento", joinColumns = { 
			@JoinColumn(name = "ide_empreendimento", referencedColumnName = "ide_empreendimento", nullable = false) }, inverseJoinColumns = { 
			@JoinColumn(name = "ide_imovel", referencedColumnName = "ide_imovel", nullable = false) })
	@ManyToMany(fetch = FetchType.LAZY)
	private Collection<Imovel> imovelCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideEmpreendimento", fetch = FetchType.LAZY)
	private Collection<ImovelEmpreendimento> imovelEmpreendimentoCollection;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideEmpreendimento", fetch = FetchType.LAZY)
	private Collection<EmpreendimentoRequerimento> empreendimentoRequerimentoCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "empreendimento", fetch = FetchType.LAZY)
	private Collection<EmpreendimentoTipologia> empreendimentoTipologiaCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideEmpreendimento", fetch = FetchType.LAZY)
	private Collection<EnderecoEmpreendimento> enderecoEmpreendimentoCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideEmpreendimento", fetch = FetchType.LAZY)
	private Collection<ResponsavelEmpreendimento> responsavelEmpreendimentoCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideEmpreendimento", fetch = FetchType.LAZY)
	private Collection<ProcuradorPfEmpreendimento> procuradorPfEmpreendimentoCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideEmpreendimento", fetch = FetchType.LAZY)
	private Collection<ProcuradorRepEmpreendimento> procuradorRepEmpreendimentoCollection;
	
	@OneToMany(mappedBy = "ideEmpreendimentoOrigem", fetch = FetchType.LAZY)
	private Collection<TransferenciaAmbiental> transferenciaAmbientalOrigemCollection;
	
	@OneToMany(mappedBy = "ideEmpreendimentoDestino", fetch = FetchType.LAZY)
	private Collection<TransferenciaAmbiental> transferenciaAmbientalDestinoCollection;
	
	@Transient
	private String nomeOuRazao;
	
	@Transient
	private Endereco endereco;
	
	@Transient
	private Endereco enderecoCorrespondencia;
	
	@Transient
	private Collection<Tipologia> tipologias;
	
	@Transient
	private Municipio municipio;
	
	public Empreendimento() {}

	public Empreendimento(Integer ideEmpreendimento, String nomEmpreendimento, Boolean indConversaoTcraLac) {
		super();
		this.ideEmpreendimento = ideEmpreendimento;
		this.indConversaoTcraLac = indConversaoTcraLac;
		this.nomEmpreendimento = nomEmpreendimento;
	}

	public Empreendimento(String nomEmpreendimento) {
		this.nomEmpreendimento = nomEmpreendimento;
	}

	public Empreendimento(Integer ideEmpreendimento) {
		this.ideEmpreendimento = ideEmpreendimento;
	}

	public Empreendimento(Integer ideEmpreendimento, String nomEmpreendimento, Pessoa idePessoa) {
		this.ideEmpreendimento = ideEmpreendimento;
		this.nomEmpreendimento = nomEmpreendimento;
		this.idePessoa = idePessoa;
	}

	public Empreendimento(Integer ideEmpreendimento, String nomEmpreendimento, Double valInvestimento, Date dtcCriacao, boolean indExcluido, String desEmail,
			boolean indCorrespondencia) {
		this.ideEmpreendimento = ideEmpreendimento;
		this.nomEmpreendimento = nomEmpreendimento;
		this.valInvestimento = valInvestimento;
		this.dtcCriacao = dtcCriacao;
		this.indExcluido = indExcluido;
		this.desEmail = desEmail;
		this.indCorrespondencia = indCorrespondencia;
	}

	public Integer getIdeEmpreendimento() {
		return ideEmpreendimento;
	}

	public void setIdeEmpreendimento(Integer ideEmpreendimento) {
		this.ideEmpreendimento = ideEmpreendimento;
	}

	public String getNomEmpreendimento() {
		return nomEmpreendimento;
	}

	public void setNomEmpreendimento(String nomEmpreendimento) {
		this.nomEmpreendimento = nomEmpreendimento.trim();
	}

	@JSON(include = false)
	public Double getValInvestimento() {
		return valInvestimento;
	}

	public void setValInvestimento(Double valInvestimento) {
		this.valInvestimento = valInvestimento;
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
	public Integer getQtdFuncionarios() {
		return qtdFuncionarios;
	}

	public void setQtdFuncionarios(Integer qtdFuncionarios) {
		this.qtdFuncionarios = qtdFuncionarios;
	}

	@JSON(include = false)
	public String getDesEmail() {
		return desEmail;
	}

	public void setDesEmail(String desEmail) {
		this.desEmail = desEmail;
	}

	@JSON(include = false)
	public String getNumCadastroTecnicoFederal() {
		return numCadastroTecnicoFederal;
	}

	public void setNumCadastroTecnicoFederal(String numCadastroTecnicoFederal) {
		this.numCadastroTecnicoFederal = numCadastroTecnicoFederal;
	}

	@JSON(include = false)
	public Date getDtcValidadeCtf() {
		return dtcValidadeCtf;
	}

	public void setDtcValidadeCtf(Date dtcValidadeCtf) {
		this.dtcValidadeCtf = dtcValidadeCtf;
	}
	
	@JSON(include = false)
	public boolean getIndCorrespondencia() {
		return indCorrespondencia;
	}

	public void setIndCorrespondencia(boolean indCorrespondencia) {
		this.indCorrespondencia = indCorrespondencia;
	}

	@JSON(include = false)
	public Date getDtcExclusao() {
		return dtcExclusao;
	}

	public void setDtcExclusao(Date dtcExclusao) {
		this.dtcExclusao = dtcExclusao;
	}

	@XmlTransient
	public Collection<Imovel> getImovelCollection() {
		return imovelCollection;
	}

	public void setImovelCollection(Collection<Imovel> imovelCollection) {
		this.imovelCollection = imovelCollection;
	}

	@JSON(include = false)
	public Boolean getIndConversaoTcraLac() {
		return Util.isNull(indConversaoTcraLac) ? false : indConversaoTcraLac;
	}

	public void setIndConversaoTcraLac(Boolean indConversaoTcraLac) {
		this.indConversaoTcraLac = indConversaoTcraLac;
	}

	@XmlTransient
	public Collection<EmpreendimentoRequerimento> getEmpreendimentoRequerimentoCollection() {
		return empreendimentoRequerimentoCollection;
	}

	@XmlTransient
	public Collection<EmpreendimentoTipologia> getEmpreendimentoTipologiaCollection() {
		return empreendimentoTipologiaCollection;
	}

	public void setEmpreendimentoTipologiaCollection(Collection<EmpreendimentoTipologia> empreendimentoTipologiaCollection) {
		this.empreendimentoTipologiaCollection = empreendimentoTipologiaCollection;
	}

	@XmlTransient
	public Collection<EnderecoEmpreendimento> getEnderecoEmpreendimentoCollection() {
		return enderecoEmpreendimentoCollection;
	}

	public void setEnderecoEmpreendimentoCollection(Collection<EnderecoEmpreendimento> enderecoEmpreendimentoCollection) {
		this.enderecoEmpreendimentoCollection = enderecoEmpreendimentoCollection;
	}

	@XmlTransient
	public Collection<ResponsavelEmpreendimento> getResponsavelEmpreendimentoCollection() {
		return responsavelEmpreendimentoCollection;
	}

	public void setResponsavelEmpreendimentoCollection(
			Collection<ResponsavelEmpreendimento> responsavelEmpreendimentoCollection) {
		this.responsavelEmpreendimentoCollection = responsavelEmpreendimentoCollection;
	}

	@JSON(include = false)
	public Pessoa getIdePessoa() {
		return idePessoa;
	}

	public void setIdePessoa(Pessoa idePessoa) {
		this.idePessoa = idePessoa;
	}
	
	@JSON(include = false)
	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	@JSON(include = false)
	public String getNomeOuRazao() {
		if (this.getIdePessoa().getPessoaFisica() != null) {
			this.nomeOuRazao = this.getIdePessoa().getPessoaFisica().getNomPessoa();
		} else {

			if (this.getIdePessoa().getPessoaJuridica() != null) {
				this.nomeOuRazao = this.getIdePessoa().getPessoaJuridica().getNomRazaoSocial();
			}
		}
		return nomeOuRazao;
	}

	public void setNomeOuRazao(String nomeOuRazao) {
		this.nomeOuRazao = nomeOuRazao;
	}
	
	@JSON(include = false)
	public Collection<ProcuradorPfEmpreendimento> getProcuradorPfEmpreendimentoCollection() {
		return procuradorPfEmpreendimentoCollection;
	}

	public void setProcuradorPfEmpreendimentoCollection(Collection<ProcuradorPfEmpreendimento> procuradorPfEmpreendimentoCollection) {
		this.procuradorPfEmpreendimentoCollection = procuradorPfEmpreendimentoCollection;
	}

	@JSON(include = false)
	public Collection<ProcuradorRepEmpreendimento> getProcuradorRepEmpreendimentoCollection() {
		return procuradorRepEmpreendimentoCollection;
	}

	public void setProcuradorRepEmpreendimentoCollection(Collection<ProcuradorRepEmpreendimento> procuradorRepEmpreendimentoCollection) {
		this.procuradorRepEmpreendimentoCollection = procuradorRepEmpreendimentoCollection;
	}

	@JSON(include = false)
	public Endereco getEndereco() {
		return endereco;
	}
	
	@JSON(include = false)
	public Endereco getEnderecoParaValidacao() {
		if(!Util.isNullOuVazio(endereco))
			return endereco;
		
		else if(!Util.isLazyInitExcepOuNull(enderecoEmpreendimentoCollection) && !Util.isNullOuVazio(enderecoEmpreendimentoCollection)){
			for (EnderecoEmpreendimento endEmpreend : enderecoEmpreendimentoCollection) {
				return endEmpreend.getIdeEndereco();
			}
		}else{
			return null;
		}
		return null;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	@JSON(include = false)
	public Boolean getIndEnderecoCorrespondencia() {
		return indEnderecoCorrespondencia;
	}

	public void setIndEnderecoCorrespondencia(Boolean indEnderecoCorrespondencia) {
		this.indEnderecoCorrespondencia = indEnderecoCorrespondencia;
	}

	@JSON(include = false)
	public Boolean getIndCessionario() {
		return indCessionario;
	}

	public void setIndCessionario(Boolean indCessionario) {
		this.indCessionario = indCessionario;
	}

	public void setTipologias(Collection<Tipologia> tipologias) {
		this.tipologias = tipologias;
	}

	@JSON(include = false)
	public Collection<Tipologia> getTipologias() {
		return tipologias;
	}

	@JSON(include = false)
	public String getTextoTipologias() {
		String texto = "";
		int pos = 1;
		if(!Util.isNullOuVazio(tipologias)){
			for (Tipologia tipologia : tipologias) {
				if(pos >1 ){
					texto+=", "+ tipologia.getCodTipologia() +" - " + tipologia.getDesTipologia();
				}else{
					texto+=tipologia.getCodTipologia() +" - " +tipologia.getDesTipologia();
				}
				pos++;
			}
		}
	
		return texto;
	}


	@JSON(include = false)
	public Municipio getMunicipio() {
		return municipio;
	}



	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}


	@JSON(include = false)
	public Boolean getIndBaseOperacional() {
		return indBaseOperacional;
	}



	public void setIndBaseOperacional(Boolean indbaseOperacional) {
		this.indBaseOperacional = indbaseOperacional;
	}
	
	@Override
	public Empreendimento clone() throws CloneNotSupportedException {
		return (Empreendimento) super.clone();
	}

	public Boolean getIndUnidadeConservacao() {
		return indUnidadeConservacao;
	}

	public void setIndUnidadeConservacao(Boolean indUnidadeConservacao) {
		this.indUnidadeConservacao = indUnidadeConservacao;
	}

	public Collection<TransferenciaAmbiental> getTransferenciaAmbientalOrigemCollection() {
		return transferenciaAmbientalOrigemCollection;
	}

	public void setTransferenciaAmbientalOrigemCollection(
			Collection<TransferenciaAmbiental> transferenciaAmbientalOrigemCollection) {
		this.transferenciaAmbientalOrigemCollection = transferenciaAmbientalOrigemCollection;
	}

	public Collection<TransferenciaAmbiental> getTransferenciaAmbientalDestinoCollection() {
		return transferenciaAmbientalDestinoCollection;
	}

	public void setTransferenciaAmbientalDestinoCollection(
			Collection<TransferenciaAmbiental> transferenciaAmbientalDestinoCollection) {
		this.transferenciaAmbientalDestinoCollection = transferenciaAmbientalDestinoCollection;
	}

	public Endereco getEnderecoCorrespondencia() {
		return enderecoCorrespondencia;
	}

	public void setEnderecoCorrespondencia(Endereco enderecoCorrespondencia) {
		this.enderecoCorrespondencia = enderecoCorrespondencia;
	}

}