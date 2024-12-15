package br.gov.ba.seia.entity;

import java.util.ArrayList;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.util.Util;

@Entity
@Table(name = "requerimento", uniqueConstraints = { @UniqueConstraint(columnNames = { "num_requerimento" }) })
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Requerimento.findAll", query = "SELECT r FROM Requerimento r"),
	@NamedQuery(name = "Requerimento.findByIdeRequerimento", query = "SELECT r FROM Requerimento r WHERE r.ideRequerimento = :ideRequerimento"),
	@NamedQuery(name = "Requerimento.findByNumRequerimento", query = "SELECT r FROM Requerimento r WHERE r.numRequerimento = :numRequerimento"),
	@NamedQuery(name = "Requerimento.findByNomContato", query = "SELECT r FROM Requerimento r WHERE r.nomContato = :nomContato"),
	@NamedQuery(name = "Requerimento.findByNumTelefone", query = "SELECT r FROM Requerimento r WHERE r.numTelefone = :numTelefone"),
	@NamedQuery(name = "Requerimento.findByDesEmail", query = "SELECT r FROM Requerimento r WHERE r.desEmail = :desEmail"),
	@NamedQuery(name = "Requerimento.findByIndDeclaracao", query = "SELECT r FROM Requerimento r WHERE r.indDeclaracao = :indDeclaracao"),
	@NamedQuery(name = "Requerimento.findByDtcCriacao", query = "SELECT r FROM Requerimento r WHERE r.dtcCriacao = :dtcCriacao"),
	@NamedQuery(name = "Requerimento.findByIndExcluido", query = "SELECT r FROM Requerimento r WHERE r.indExcluido = :indExcluido"),
	@NamedQuery(name = "Requerimento.findByDtcExclusao", query = "SELECT r FROM Requerimento r WHERE r.dtcExclusao = :dtcExclusao"),
	@NamedQuery(name = "Requerimento.findByAnoOrgao", query = "SELECT r FROM Requerimento r WHERE r.ideOrgao.ideOrgao = :ideOrgao AND r.numRequerimento IS NOT NULL AND r.numRequerimento = (SELECT max(rq.numRequerimento) from Requerimento rq WHERE rq.numRequerimento IS NOT NULL  AND rq.ideOrgao.ideOrgao = :ideOrgao)  ORDER BY r.ideRequerimento DESC "),
})
public class Requerimento extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "REQUERIMENTO_IDEREQUERIMENTO_GENERATOR", sequenceName = "REQUERIMENTO_IDE_REQUERIMENTO_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REQUERIMENTO_IDEREQUERIMENTO_GENERATOR")
	@Column(name = "ide_requerimento", nullable = false)
	private Integer ideRequerimento;

	@Basic(optional = false)
	@Column(name = "num_requerimento", nullable = false, length = 40)
	private String numRequerimento;

	@Column(name = "nom_contato", length = 200)
	private String nomContato;

	@Column(name = "num_telefone", length = 20)
	private String numTelefone;

	@Column(name = "des_email", length = 70)
	private String desEmail;

	@Column(name = "ind_declaracao")
	private Boolean indDeclaracao;

	@Column(name = "ind_estado_emergencia")
	private Boolean indEstadoEmergencia;

	@Basic(optional = false)
	@Column(name = "dtc_criacao", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcCriacao;
	
	@Column(name = "dtc_finalizacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcFinalizacao;

	@Basic(optional = false)
	@Column(name = "ind_excluido", nullable = false)
	private boolean indExcluido;

	@Column(name = "dtc_exclusao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcExclusao;
	
	@Column(name = "des_caminho_resumo_original", length = 255)
	private String desCaminhoResumoOriginal;
	
	@Column(name = "dtc_finalizacao_reenquadramento")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcFinalizacaoReenquadramento;
	
	@ManyToMany(mappedBy = "requerimentoCollection", fetch = FetchType.LAZY)
	private Collection<Legislacao> legislacaoCollection;

	@Transient
	private BoletoPagamentoRequerimento boletoPagamentoRequerimento;

	@OneToMany(mappedBy = "ideRequerimento", fetch = FetchType.LAZY)
	private Collection<Processo> processoCollection;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideRequerimento", fetch = FetchType.LAZY)
	private Collection<DocumentoObrigatorioRequerimento> documentoObrigatorioRequerimentoCollection;
	
	@OneToMany(mappedBy = "ideRequerimento", fetch = FetchType.LAZY)
	private Collection<Enquadramento> enquadramentoCollection;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideRequerimento", fetch = FetchType.LAZY)
	private Collection<ComunicacaoRequerimento> comunicacaoRequerimentoCollection;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideRequerimento", fetch = FetchType.LAZY)
	private Collection<ProcessoTramite> processoTramiteCollection;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideRequerimento", fetch = FetchType.LAZY)
	private Collection<TramitacaoRequerimento> tramitacaoRequerimentoCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideRequerimento", fetch = FetchType.LAZY)
	private Collection<TramitacaoRequerimento> tramitacaoRequerimentoAuxCollection;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "requerimento", fetch = FetchType.LAZY)
	private RequerimentoUnico requerimentoUnico;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "requerimento", fetch = FetchType.LAZY)
	private Collection<Certificado> certificados;

	@JoinColumn(name = "ide_tipo_requerimento", referencedColumnName = "ide_tipo_requerimento", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TipoRequerimento ideTipoRequerimento;

	@JoinColumn(name = "ide_orgao", referencedColumnName = "ide_orgao", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Orgao ideOrgao;

	@JoinColumn(name = "ide_endereco_contato", referencedColumnName = "ide_endereco")
	@ManyToOne(fetch = FetchType.LAZY)
	private Endereco ideEnderecoContato;
	
	@JoinColumn(name = "ide_area", referencedColumnName = "ide_area")
	@ManyToOne(fetch = FetchType.LAZY)
	private Area ideArea;
	
	@OneToMany(mappedBy = "ideRequerimento")
	private Collection<RequerimentoTipologia> requerimentoTipologiaCollection;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "requerimento", fetch = FetchType.LAZY)
	private Collection<RequerimentoImovel> requerimentoImovelCollection;
	
	@OneToMany(mappedBy = "ideRequerimento", fetch = FetchType.LAZY)
	private Collection<ObjetivoRequerimentoLimpezaArea> objetivoRequerimentoLimpezaAreaCollection;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideRequerimento", fetch = FetchType.LAZY)
	private Collection<PerguntaRequerimento> perguntaRequerimentoCollection;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "requerimento", fetch = FetchType.LAZY)
	private Collection<RequerimentoPessoa> requerimentoPessoaCollection;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "ideRequerimento", fetch = FetchType.LAZY)
	private Lac lac;

	@OneToMany(mappedBy = "ideRequerimento", fetch = FetchType.LAZY)
	private Collection<DocumentoRepresentacaoRequerimento> documentoRepresentacaoRequerimentoCollection;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideRequerimento", fetch = FetchType.LAZY)
	private Collection<DocumentoIdentificacaoRequerimento> documentoIdentificacaoRequerimentoCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideRequerimento", fetch = FetchType.LAZY)
	private Collection<EmpreendimentoRequerimento> empreendimentoRequerimentoCollection;
	
	@OneToMany(cascade =CascadeType.DETACH, mappedBy = "ideRequerimento", fetch = FetchType.LAZY )
	private Collection<SolicitacaoAdministrativo> solicitacaoAdministrativoCollection;

	@OneToMany(mappedBy = "ideRequerimento", fetch = FetchType.LAZY)
	private Collection<Outorga> outorgaCollection;
	
	@OneToMany(mappedBy = "ideRequerimento", fetch = FetchType.LAZY)
	private Collection<Fce> fceCollection;
	
	@OneToMany(mappedBy = "ideRequerimento", fetch = FetchType.LAZY)
	private Collection<Florestal> florestalCollection;

	@JoinTable(name = "requerimento_tipo_area_protegida", joinColumns = { @JoinColumn(name = "ide_requerimento", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ide_tipo_area_protegida", nullable = false, updatable = false) })
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	private Collection<TipoAreaProtegida> tipoAreaProtegidaCollection;
	
	@JoinTable(name = "requerimento_tipo_area_preservacao_permanente", joinColumns = { @JoinColumn(name = "ide_requerimento", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ide_tipo_area_preservacao_permanente", nullable = false, updatable = false) })
	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	private Collection<TipoAreaPreservacaoPermanente> tipoAreaPreservacaoPermanenteCollection;
	
	@OneToMany(mappedBy = "ideRequerimento", fetch = FetchType.LAZY)
	private Collection<RequerimentoUnidadeConservacao> requerimentoUnidadeConservacaoCollection;
	
	@OneToOne(mappedBy = "requerimento", fetch = FetchType.LAZY)
	private CumprimentoReposicaoFlorestal cumprimentoReposicaoFlorestal;
	
	@OneToMany(mappedBy = "ideRequerimento", fetch = FetchType.LAZY)
	private Collection<AtoDeclaratorio> atoDeclaratorioCollection;
	
	@Transient
	private BoletoDaeRequerimento boletoDaeRequerimeno;
	
	@Transient
	private Collection<ProcessoTramite> collProcessotramiteExclusao;

	@Transient
	private Empreendimento ultimoEmpreendimento;

	@Transient
	private Pessoa requerente;

	@Transient
	private Collection<AtoAmbiental> atosAmbientais;

	@Transient
	private Imovel imovel;

	public Requerimento() {
	}
	
	public Requerimento(String numRequerimento) {
		this.numRequerimento = numRequerimento;
	}

	public Requerimento(Integer ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}

	public Requerimento(Integer ideRequerimento, String numRequerimento, Date dtcCriacao, boolean indExcluido) {
		this.ideRequerimento = ideRequerimento;
		this.numRequerimento = numRequerimento;
		this.dtcCriacao = dtcCriacao;
		this.indExcluido = indExcluido;
	}

	public Requerimento(Integer ideRequerimento, String numRequerimento, Date dtcCriacao) {
		super();
		this.ideRequerimento = ideRequerimento;
		this.numRequerimento = numRequerimento;
		this.dtcCriacao = dtcCriacao;
	}

	public Integer getIdeRequerimento() {
		return ideRequerimento;
	}

	public void setIdeRequerimento(Integer ideRequerimento) {
		this.ideRequerimento = ideRequerimento;
	}

	public String getNumRequerimento() {
		return numRequerimento;
	}
	
	/**
     * Método que retorna o número do requerimento formatado para quebrar linha na grid. 
     * Caso o requerimento nao possua numero, retorna um traco.
     *
     * @author micael.coutinho
     * @return {@link String} já formatada com espaço
     */
	public String getNumRequerimentoTabela() {
		String numTabela;
		
		if(!Util.isNullOuVazio(numRequerimento)){
			int index = numRequerimento.indexOf('/');
			numTabela = numRequerimento.substring(0, index);
			numTabela = numTabela + ' ' + numRequerimento.substring(index);
		} else {
			numTabela = "-";
		}
		
		return numTabela;
	}
	
	/**
	 * Método que retorna o número do requerimento formatado para quebrar linha na grid. 
	 * Caso o requerimento nao possua numero, retorna null
	 *
	 * @author micael.coutinho
	 * @return {@link String} já formatada com espaço
	 */
	public String getNumRequerimentoOrNull() {
		String numTabela;
		
		if(!Util.isNullOuVazio(numRequerimento)){
			int index = numRequerimento.indexOf('/');
			numTabela = numRequerimento.substring(0, index);
			numTabela = numTabela + ' ' + numRequerimento.substring(index);
			
			return numTabela;
		} else {
			return null;
		}
	}

	public void setNumRequerimento(String numRequerimento) {
		this.numRequerimento = numRequerimento;
	}

	public String getNomContato() {
		return nomContato;
	}

	public void setNomContato(String nomContato) {
		this.nomContato = nomContato;
	}

	public String getNumTelefone() {
		return numTelefone;
	}

	public void setNumTelefone(String numTelefone) {
		this.numTelefone = numTelefone;
	}

	public String getDesEmail() {
		return desEmail;
	}

	public void setDesEmail(String desEmail) {
		this.desEmail = desEmail;
	}

	public Boolean getIndDeclaracao() {
		return indDeclaracao;
	}

	public void setIndDeclaracao(Boolean indDeclaracao) {
		this.indDeclaracao = indDeclaracao;
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

	public Boolean getIndEstadoEmergencia() {
		if(Util.isNull(indEstadoEmergencia)) {
			indEstadoEmergencia = false;
		}
		return indEstadoEmergencia;
	}

	public void setIndEstadoEmergencia(Boolean indEstadoEmergencia) {
		this.indEstadoEmergencia = indEstadoEmergencia;
	}


	public RequerimentoUnico getRequerimentoUnico() {
		return requerimentoUnico;
	}

	public void setRequerimentoUnico(RequerimentoUnico requerimentoUnico) {
		this.requerimentoUnico = requerimentoUnico;
	}

	public TipoRequerimento getIdeTipoRequerimento() {
		return ideTipoRequerimento;
	}

	public void setIdeTipoRequerimento(TipoRequerimento ideTipoRequerimento) {
		this.ideTipoRequerimento = ideTipoRequerimento;
	}

	public Orgao getIdeOrgao() {
		return ideOrgao;
	}

	public void setIdeOrgao(Orgao ideOrgao) {
		this.ideOrgao = ideOrgao;
	}

	public Pessoa getRequerente() {
		return requerente;
	}

	public void setRequerente(Pessoa requerente) {
		this.requerente = requerente;
	}

	public Endereco getIdeEnderecoContato() {
		return ideEnderecoContato;
	}

	public void setIdeEnderecoContato(Endereco ideEnderecoContato) {
		this.ideEnderecoContato = ideEnderecoContato;
	}

	@XmlTransient
	public Collection<RequerimentoPessoa> getRequerimentoPessoaCollection() {
		return requerimentoPessoaCollection;
	}

	public void setRequerimentoPessoaCollection(Collection<RequerimentoPessoa> requerimentoPessoaCollection) {
		this.requerimentoPessoaCollection = requerimentoPessoaCollection;
	}

	@XmlTransient
	public Collection<RequerimentoImovel> getRequerimentoImovelCollection() {
		return requerimentoImovelCollection;
	}

	public void setRequerimentoImovelCollection(Collection<RequerimentoImovel> requerimentoImovelCollection) {
		this.requerimentoImovelCollection = requerimentoImovelCollection;
	}

	/**
	 * @return the legislacaoCollection
	 */
	public Collection<Legislacao> getLegislacaoCollection() {
		return legislacaoCollection;
	}

	/**
	 * @param legislacaoCollection
	 *            the legislacaoCollection to set
	 */
	public void setLegislacaoCollection(Collection<Legislacao> legislacaoCollection) {
		this.legislacaoCollection = legislacaoCollection;
	}

	/**
	 * @return the boletoPagamentoRequerimento
	 */
	public BoletoPagamentoRequerimento getBoletoPagamentoRequerimento() {
		return boletoPagamentoRequerimento;
	}

	/**
	 * @param boletoPagamentoRequerimento
	 *            the boletoPagamentoRequerimento to set
	 */
	public void setBoletoPagamentoRequerimento(BoletoPagamentoRequerimento boletoPagamentoRequerimento) {
		this.boletoPagamentoRequerimento = boletoPagamentoRequerimento;
	}

	/**
	 * @return the processoCollection
	 */
	public Collection<Processo> getProcessoCollection() {
		return processoCollection;
	}

	/**
	 * @param processoCollection
	 *            the processoCollection to set
	 */
	public void setProcessoCollection(Collection<Processo> processoCollection) {
		this.processoCollection = processoCollection;
	}

	/**
	 * @return the documentoObrigatorioRequerimentoCollection
	 */
	public Collection<DocumentoObrigatorioRequerimento> getDocumentoObrigatorioRequerimentoCollection() {
		return documentoObrigatorioRequerimentoCollection;
	}

	/**
	 * @param documentoObrigatorioRequerimentoCollection
	 *            the documentoObrigatorioRequerimentoCollection to set
	 */
	public void setDocumentoObrigatorioRequerimentoCollection(
			Collection<DocumentoObrigatorioRequerimento> documentoObrigatorioRequerimentoCollection) {
		this.documentoObrigatorioRequerimentoCollection = documentoObrigatorioRequerimentoCollection;
	}

	/**
	 * @return the comunicacaoRequerimentoCollection
	 */
	public Collection<ComunicacaoRequerimento> getComunicacaoRequerimentoCollection() {
		return comunicacaoRequerimentoCollection;
	}

	/**
	 * @param comunicacaoRequerimentoCollection
	 *            the comunicacaoRequerimentoCollection to set
	 */
	public void setComunicacaoRequerimentoCollection(
			Collection<ComunicacaoRequerimento> comunicacaoRequerimentoCollection) {
		this.comunicacaoRequerimentoCollection = comunicacaoRequerimentoCollection;
	}

	/**
	 * @return the processoTramiteCollection
	 */
	public Collection<ProcessoTramite> getProcessoTramiteCollection() {
		return processoTramiteCollection;
	}

	/**
	 * @param processoTramiteCollection
	 *            the processoTramiteCollection to set
	 */
	public void setProcessoTramiteCollection(Collection<ProcessoTramite> processoTramiteCollection) {
		this.processoTramiteCollection = processoTramiteCollection;
	}

	/**
	 * @return the tramitacaoRequerimentoCollection
	 */
	public Collection<TramitacaoRequerimento> getTramitacaoRequerimentoCollection() {
		return tramitacaoRequerimentoCollection;
	}

	/**
	 * @param tramitacaoRequerimentoCollection
	 *            the tramitacaoRequerimentoCollection to set
	 */
	public void setTramitacaoRequerimentoCollection(Collection<TramitacaoRequerimento> tramitacaoRequerimentoCollection) {
		this.tramitacaoRequerimentoCollection = tramitacaoRequerimentoCollection;
	}

	public Lac getLac() {
		return lac;
	}

	public void setLac(Lac lac) {
		this.lac = lac;
	}

	public Collection<DocumentoRepresentacaoRequerimento> getDocumentoRepresentacaoRequerimentoCollection() {
		return documentoRepresentacaoRequerimentoCollection;
	}

	public void setDocumentoRepresentacaoRequerimentoCollection(
			Collection<DocumentoRepresentacaoRequerimento> documentoRepresentacaoRequerimentoCollection) {
		this.documentoRepresentacaoRequerimentoCollection = documentoRepresentacaoRequerimentoCollection;
	}

	public Collection<DocumentoIdentificacaoRequerimento> getDocumentoIdentificacaoRequerimentoCollection() {
		return documentoIdentificacaoRequerimentoCollection;
	}

	public void setDocumentoIdentificacaoRequerimentoCollection(
			Collection<DocumentoIdentificacaoRequerimento> documentoIdentificacaoRequerimentoCollection) {
		this.documentoIdentificacaoRequerimentoCollection = documentoIdentificacaoRequerimentoCollection;
	}

	public Collection<ProcessoTramite> getCollProcessotramiteExclusao() {
		return collProcessotramiteExclusao;
	}

	public void setCollProcessotramiteExclusao(Collection<ProcessoTramite> collProcessotramiteExclusao) {
		this.collProcessotramiteExclusao = collProcessotramiteExclusao;
	}

	public Collection<Certificado> getCertificados() {
		return certificados;
	}

	public void setCertificados(Collection<Certificado> certificados) {
		this.certificados = certificados;
	}

	public Imovel getImovel() {
		return imovel;
	}

	public void setImovel(Imovel imovel) {
		this.imovel = imovel;
	}

	public Collection<Enquadramento> getEnquadramentoCollection() {
		return enquadramentoCollection;
	}

	public void setEnquadramentoCollection(Collection<Enquadramento> enquadramentoCollection) {
		this.enquadramentoCollection = enquadramentoCollection;
	}

	public BoletoDaeRequerimento getBoletoDaeRequerimento() {
		return boletoDaeRequerimeno;
	}

	public void setBoletoDaeRequerimento(BoletoDaeRequerimento boletoDaeRequerimeno) {
		this.boletoDaeRequerimeno = boletoDaeRequerimeno;
	}

	public Empreendimento getUltimoEmpreendimento() {
		return ultimoEmpreendimento;
	}

	public void setUltimoEmpreendimento(Empreendimento ultimoEmpreendimento) {
		this.ultimoEmpreendimento = ultimoEmpreendimento;
	}

	public Collection<AtoAmbiental> getAtosAmbientais() {
		return Util.isNull(atosAmbientais) ? this.atosAmbientais = new ArrayList<AtoAmbiental>() : atosAmbientais;
	}

	public void setAtosAmbientais(Collection<AtoAmbiental> atosAmbientais) {
		this.atosAmbientais = atosAmbientais;
	}

	public Collection<PerguntaRequerimento> getPerguntaRequerimentoCollection() {
		return perguntaRequerimentoCollection;
	}

	public void setPerguntaRequerimentoCollection(Collection<PerguntaRequerimento> perguntaRequerimentoCollection) {
		this.perguntaRequerimentoCollection = perguntaRequerimentoCollection;
	}

	public Collection<ObjetivoRequerimentoLimpezaArea> getObjetivoRequerimentoLimpezaAreaCollection() {
		return objetivoRequerimentoLimpezaAreaCollection;
	}

	public void setObjetivoRequerimentoLimpezaAreaCollection(Collection<ObjetivoRequerimentoLimpezaArea> objetivoRequerimentoLimpezaAreaCollection) {
		this.objetivoRequerimentoLimpezaAreaCollection = objetivoRequerimentoLimpezaAreaCollection;
	}

	public Collection<EmpreendimentoRequerimento> getEmpreendimentoRequerimentoCollection() {
		return empreendimentoRequerimentoCollection;
	}

	public void setEmpreendimentoRequerimentoCollection(Collection<EmpreendimentoRequerimento> empreendimentoRequerimentoCollection) {
		this.empreendimentoRequerimentoCollection = empreendimentoRequerimentoCollection;
	}

	public BoletoDaeRequerimento getBoletoDaeRequerimeno() {
		return boletoDaeRequerimeno;
	}

	public void setBoletoDaeRequerimeno(BoletoDaeRequerimento boletoDaeRequerimeno) {
		this.boletoDaeRequerimeno = boletoDaeRequerimeno;
	}

	public Area getIdeArea() {
		return ideArea;
	}

	public void setIdeArea(Area ideArea) {
		this.ideArea = ideArea;
	}

	public Collection<RequerimentoTipologia> getRequerimentoTipologiaCollection() {
		return Util.isNull(requerimentoTipologiaCollection) ? requerimentoTipologiaCollection = new ArrayList<RequerimentoTipologia>():requerimentoTipologiaCollection;
	}

	public void setRequerimentoTipologiaCollection(Collection<RequerimentoTipologia> requerimentoTipologiaCollection) {
		this.requerimentoTipologiaCollection = requerimentoTipologiaCollection;
	}

	public Collection<SolicitacaoAdministrativo> getSolicitacaoAdministrativoCollection() {
		return solicitacaoAdministrativoCollection;
	}

	public void setSolicitacaoAdministrativoCollection(
			Collection<SolicitacaoAdministrativo> solicitacaoAdministrativoCollection) {
		this.solicitacaoAdministrativoCollection = solicitacaoAdministrativoCollection;
	}

	public Collection<Outorga> getOutorgaCollection() {
		return outorgaCollection;
	}

	public void setOutorgaCollection(Collection<Outorga> outorgaCollection) {
		this.outorgaCollection = outorgaCollection;
	}

	public Collection<Fce> getFceCollection() {
		return fceCollection;
	}

	public Collection<TramitacaoRequerimento> getTramitacaoRequerimentoAuxCollection() {
		return tramitacaoRequerimentoAuxCollection;
	}

	public void setTramitacaoRequerimentoAuxCollection(
			Collection<TramitacaoRequerimento> tramitacaoRequerimentoAuxCollection) {
		this.tramitacaoRequerimentoAuxCollection = tramitacaoRequerimentoAuxCollection;
	}

	public Date getDtcFinalizacao() {
		return dtcFinalizacao;
	}

	public void setDtcFinalizacao(Date dtcFinalizacao) {
		this.dtcFinalizacao = dtcFinalizacao;
	}

	public Collection<Florestal> getFlorestalCollection() {
		return florestalCollection;
	}

	public void setFlorestalCollection(Collection<Florestal> florestalCollection) {
		this.florestalCollection = florestalCollection;
	}
	
	@XmlTransient
	public Collection<TipoAreaProtegida> getTipoAreaProtegidaCollection() {
		return tipoAreaProtegidaCollection;
	}

	public void setTipoAreaProtegidaCollection(Collection<TipoAreaProtegida> tipoAreaProtegidaCollection) {
		this.tipoAreaProtegidaCollection = tipoAreaProtegidaCollection;
	}
	
	@XmlTransient
	public Collection<TipoAreaPreservacaoPermanente> getTipoAreaPreservacaoPermanenteCollection() {
		return tipoAreaPreservacaoPermanenteCollection;
	}

	public void setTipoAreaPreservacaoPermanenteCollection(
			Collection<TipoAreaPreservacaoPermanente> tipoAreaPreservacaoPermanenteCollection) {
		this.tipoAreaPreservacaoPermanenteCollection = tipoAreaPreservacaoPermanenteCollection;
	}

	@XmlTransient
	public Collection<RequerimentoUnidadeConservacao> getRequerimentoUnidadeConservacaoCollection() {
		return requerimentoUnidadeConservacaoCollection;
	}

	public void setRequerimentoUnidadeConservacaoCollection(Collection<RequerimentoUnidadeConservacao> requerimentoUnidadeConservacaoCollection) {
		this.requerimentoUnidadeConservacaoCollection = requerimentoUnidadeConservacaoCollection;
	}

	public String getDesCaminhoResumoOriginal() {
		return desCaminhoResumoOriginal;
	}

	public void setDesCaminhoResumoOriginal(String desCaminhoResumoOriginal) {
		this.desCaminhoResumoOriginal = desCaminhoResumoOriginal;
	}

	public Date getDtcFinalizacaoReenquadramento() {
		return dtcFinalizacaoReenquadramento;
	}

	public void setDtcFinalizacaoReenquadramento(Date dtcFinalizacaoReenquadramento) {
		this.dtcFinalizacaoReenquadramento = dtcFinalizacaoReenquadramento;
	}

	public CumprimentoReposicaoFlorestal getCumprimentoReposicaoFlorestal() {
		return cumprimentoReposicaoFlorestal;
	}

	public void setCumprimentoReposicaoFlorestal(CumprimentoReposicaoFlorestal cumprimentoReposicaoFlorestal) {
		this.cumprimentoReposicaoFlorestal = cumprimentoReposicaoFlorestal;
	}

	public Collection<AtoDeclaratorio> getAtoDeclaratorioCollection() {
		return atoDeclaratorioCollection;
	}

	public void setAtoDeclaratorioCollection(Collection<AtoDeclaratorio> atoDeclaratorioCollection) {
		this.atoDeclaratorioCollection = atoDeclaratorioCollection;
	}
}