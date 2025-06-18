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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.interfaces.Auditoria;
import br.gov.ba.seia.util.auditoria.AuditoriaUtil;
import br.gov.ba.seia.util.Util;
import flexjson.JSON;

/**
 * @author MJunior
 */
@Entity
@Table(name = "pessoa_juridica")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "PessoaJuridica.findAll", query = "SELECT pj FROM PessoaJuridica as pj inner join fetch pj.pessoa as p where p.indExcluido = :indExcluido"),
		@NamedQuery(name = "PessoaJuridica.findByIdePessoaJuridica", query = "SELECT p FROM PessoaJuridica p WHERE p.idePessoaJuridica = :idePessoaJuridica"),
		@NamedQuery(name = "PessoaJuridica.findByIdesPessoaJuridica", query = "SELECT pj FROM PessoaJuridica as pj inner join fetch pj.pessoa as p WHERE pj.idePessoaJuridica in (:idesPessoaJuridica) AND p.indExcluido = :indExcluido"),
		@NamedQuery(name = "PessoaJuridica.findByNomRazaoSocial", query = "SELECT p FROM PessoaJuridica p WHERE p.nomRazaoSocial = :nomRazaoSocial"),
		@NamedQuery(name = "PessoaJuridica.findBynomeFantasia", query = "SELECT p FROM PessoaJuridica p WHERE p.nomeFantasia = :nomeFantasia"),
		@NamedQuery(name = "PessoaJuridica.findByDtcAbertura", query = "SELECT p FROM PessoaJuridica p WHERE p.dtcAbertura = :dtcAbertura"),
		@NamedQuery(name = "PessoaJuridica.findByNumCnpj", query = "SELECT pj FROM PessoaJuridica pj inner join fetch pj.pessoa as p WHERE pj.numCnpj = :numCnpj AND p.indExcluido = :indExcluido"),
		@NamedQuery(name = "PessoaJuridica.findByNumInscricaoMunicipal", query = "SELECT p FROM PessoaJuridica p WHERE p.numInscricaoMunicipal = :numInscricaoMunicipal"),
		@NamedQuery(name = "PessoaJuridica.findByNumInscricaoEstadual", query = "SELECT p FROM PessoaJuridica p WHERE p.numInscricaoEstadual = :numInscricaoEstadual"),
		@NamedQuery(name = "PessoaJuridica.findByDscCaminhoArquivo", query = "SELECT p FROM PessoaJuridica p WHERE p.dscCaminhoArquivo = :dscCaminhoArquivo"),
		@NamedQuery(name = "PessoaJuridica.findByCnpjOrRazaoSocial", query = "SELECT distinct pj FROM PessoaJuridica as pj inner join fetch pj.pessoa as p where p.indExcluido = :indExcluido and (pj.numCnpj = :numCnpj or lower(pj.nomRazaoSocial) like :nomRazaoSocial)"),
		@NamedQuery(name = "PessoaJuridica.findByCnpjOrRazaoSocialIdesPessoaJuridica", query = "SELECT distinct pj FROM PessoaJuridica as pj inner join fetch pj.pessoa as p where pj.idePessoaJuridica in (:idesPessoaJuridica) AND p.indExcluido = :indExcluido AND (pj.numCnpj = :numCnpj or lower(pj.nomRazaoSocial) like :nomRazaoSocial)"), })
public class PessoaJuridica extends AbstractEntity implements Auditoria{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pessoajur_pessoa_fk")
	@SequenceGenerator(name = "pessoajur_pessoa_fk", sequenceName = "pessoajur_pessoa_fk", allocationSize = 1)
	@GenericGenerator(name = "pessoajur_pessoa_fk", strategy = "foreign", parameters = @Parameter(name = "property", value = "pessoa"))
	@Column(name = "ide_pessoa_juridica", nullable = false)
	private Integer idePessoaJuridica;
	
	@Basic(optional = false)
	@Column(name = "nom_razao_social", nullable = false, length = 200)
	private String nomRazaoSocial;
	
	@Column(name = "nom_nome_fantasia", length = 200)
	private String nomeFantasia;
	
	@Column(name = "dtc_abertura")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcAbertura;
	
	@Basic(optional = false)
	@Column(name = "num_cnpj", nullable = false, length = 14)
	private String numCnpj;
	
	@Column(name = "num_inscricao_municipal", length = 30)
	private String numInscricaoMunicipal;
	
	@Column(name = "num_inscricao_estadual", length = 30)
	private String numInscricaoEstadual;
	
	@Column(name = "dsc_caminho_arquivo", length = 1000)
	private String dscCaminhoArquivo;
	
	@JoinColumn(name = "ide_pessoa_juridica", referencedColumnName = "ide_pessoa", nullable = false, insertable = false, updatable = false)
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private Pessoa pessoa;
	
	@JoinColumn(name = "ide_natureza_juridica", referencedColumnName = "ide_natureza_juridica", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private NaturezaJuridica ideNaturezaJuridica;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idePessoaJuridica", fetch = FetchType.LAZY)
	private Collection<PessoaJuridicaHistorico> pessoaJuridicaHistoricoCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idePessoa", fetch = FetchType.LAZY)
	private Collection<PessoaEnderecoHistorico> pessoaEnderecoHistoricoCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idePessoaJuridica", fetch = FetchType.LAZY)
	private Collection<ParticipacaoAcionaria> participacaoAcionariaCollection;
	
	@OneToMany(mappedBy = "idePessoaJuridica", fetch = FetchType.LAZY)
	private Collection<DocumentoIdentificacaoRequerimento> documentoIdentificacaoRequerimentoCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idePessoaJuridica", fetch = FetchType.LAZY)
	private Collection<PessoaRepresentanteHistorico> pessoaRepresentanteHistoricoCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idePessoaJuridica", fetch = FetchType.LAZY)
	private Collection<ProcuradorRepresentante> procuradorRepresentanteCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idePessoaJuridica", fetch = FetchType.LAZY)
	private Collection<PessoaJuridicaCnae> pessoaJuridicaCnaeCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idePessoaJuridica", fetch = FetchType.LAZY)
	private Collection<PessoaAtividadeHistorico> pessoaAtividadeHistoricoCollection;
	
	@OneToMany(cascade = CascadeType.MERGE, mappedBy = "idePessoaJuridica", fetch = FetchType.LAZY)
	private Collection<RepresentanteLegal> representanteLegalCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idePessoaJuridica", fetch = FetchType.LAZY)
	private Collection<ContratoConvenio> contratoConvenioCollection;
	
	@Column(name = "ide_usuario")
	private Integer idePessoaFisicaUsuario;
	
	@Column(name = "endereco_ip")
	private String enderecoIp;

	@Column(name = "caminho_requisicao")
	private String caminhoRequisicao;
	
	@Transient
	private boolean isRepresentanteLegalInativado;

	@Transient
	private boolean isProcuradorRepresentanteInativado;
	
	@Transient
	private boolean isParticipanteAcionarioInativado;
	
	public PessoaJuridica() {}

	public PessoaJuridica(Integer idePessoaJuridica) {
		this.idePessoaJuridica = idePessoaJuridica;
	}

	public PessoaJuridica(Integer idePessoaJuridica, String nomRazaoSocial) {
		this.idePessoaJuridica = idePessoaJuridica;
		this.nomRazaoSocial = nomRazaoSocial;
	}

	public PessoaJuridica(Integer idePessoaJuridica, String nomRazaoSocial, String numCnpj) {
		super();
		this.idePessoaJuridica = idePessoaJuridica;
		this.nomRazaoSocial = nomRazaoSocial;
		this.numCnpj = numCnpj;
	}
	
	public PessoaJuridica(String numCnpj) {
		this.numCnpj = numCnpj;
	}

	public PessoaJuridica(Integer idePessoaJuridica, String nomRazaoSocial, String nomeFantasia, Date dtcAbertura, String numCnpj) {
		this.idePessoaJuridica = idePessoaJuridica;
		this.nomRazaoSocial = nomRazaoSocial;
		this.nomeFantasia = nomeFantasia;
		this.dtcAbertura = dtcAbertura;
		this.numCnpj = numCnpj;
	}

	public Integer getIdePessoaJuridica() {
		return idePessoaJuridica;
	}

	public void setIdePessoaJuridica(Integer idePessoaJuridica) {
		this.idePessoaJuridica = idePessoaJuridica;
	}

	public String getNomRazaoSocial() {
		if (nomRazaoSocial != null) {
			return nomRazaoSocial;
		} else {
			return nomRazaoSocial;
		}

	}

	public void setNomRazaoSocial(String nomRazaoSocial) {
		this.nomRazaoSocial = nomRazaoSocial.trim().replaceAll(" +", " ");
	}

	@JSON(include = false)
	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia.trim().replaceAll(" +", " ");
	}

	public Date getDtcAbertura() {
		return dtcAbertura;
	}

	public void setDtcAbertura(Date dtcAbertura) {
		this.dtcAbertura = dtcAbertura;
	}

	public String getNumCnpjFormatado() {
		return Util.formatarCNPJ(numCnpj);
	}
	
	public String getNumCnpj() {
		return numCnpj;
	}

	public void setNumCnpj(String numCnpj) {
		this.numCnpj = numCnpj;
	}

	@JSON(include = false)
	public String getNumInscricaoMunicipal() {
		return numInscricaoMunicipal;
	}

	public void setNumInscricaoMunicipal(String numInscricaoMunicipal) {
		this.numInscricaoMunicipal = numInscricaoMunicipal.trim();
	}

	@JSON(include = false)
	public String getNumInscricaoEstadual() {
		return numInscricaoEstadual;
	}

	public void setNumInscricaoEstadual(String numInscricaoEstadual) {
		this.numInscricaoEstadual = numInscricaoEstadual.trim();
	}
	
	@JSON(include = false)
	public String getDscCaminhoArquivo() {
		return dscCaminhoArquivo;
	}

	public void setDscCaminhoArquivo(String dscCaminhoArquivo) {
		this.dscCaminhoArquivo = dscCaminhoArquivo;
	}

	@XmlTransient
	public Collection<PessoaJuridicaHistorico> getPessoaJuridicaHistoricoCollection() {
		return pessoaJuridicaHistoricoCollection;
	}

	public void setPessoaJuridicaHistoricoCollection(Collection<PessoaJuridicaHistorico> pessoaJuridicaHistoricoCollection) {
		this.pessoaJuridicaHistoricoCollection = pessoaJuridicaHistoricoCollection;
	}

	@XmlTransient
	public Collection<PessoaEnderecoHistorico> getPessoaEnderecoHistoricoCollection() {
		return pessoaEnderecoHistoricoCollection;
	}

	public void setPessoaEnderecoHistoricoCollection(Collection<PessoaEnderecoHistorico> pessoaEnderecoHistoricoCollection) {
		this.pessoaEnderecoHistoricoCollection = pessoaEnderecoHistoricoCollection;
	}

	@XmlTransient
	public Collection<ParticipacaoAcionaria> getParticipacaoAcionariaCollection() {
		return participacaoAcionariaCollection;
	}

	public void setParticipacaoAcionariaCollection(Collection<ParticipacaoAcionaria> participacaoAcionariaCollection) {
		this.participacaoAcionariaCollection = participacaoAcionariaCollection;
	}

	@XmlTransient
	public Collection<DocumentoIdentificacaoRequerimento> getDocumentoIdentificacaoRequerimentoCollection() {
		return documentoIdentificacaoRequerimentoCollection;
	}

	public void setDocumentoIdentificacaoRequerimentoCollection(Collection<DocumentoIdentificacaoRequerimento> documentoIdentificacaoRequerimentoCollection) {
		this.documentoIdentificacaoRequerimentoCollection = documentoIdentificacaoRequerimentoCollection;
	}

	@XmlTransient
	public Collection<PessoaRepresentanteHistorico> getPessoaRepresentanteHistoricoCollection() {
		return pessoaRepresentanteHistoricoCollection;
	}

	public void setPessoaRepresentanteHistoricoCollection(Collection<PessoaRepresentanteHistorico> pessoaRepresentanteHistoricoCollection) {
		this.pessoaRepresentanteHistoricoCollection = pessoaRepresentanteHistoricoCollection;
	}

	@XmlTransient
	public Collection<ProcuradorRepresentante> getProcuradorRepresentanteCollection() {
		return procuradorRepresentanteCollection;
	}

	public void setProcuradorRepresentanteCollection(Collection<ProcuradorRepresentante> procuradorRepresentanteCollection) {
		this.procuradorRepresentanteCollection = procuradorRepresentanteCollection;
	}

	@XmlTransient
	public Collection<PessoaJuridicaCnae> getPessoaJuridicaCnaeCollection() {
		return pessoaJuridicaCnaeCollection;
	}

	public void setPessoaJuridicaCnaeCollection(Collection<PessoaJuridicaCnae> pessoaJuridicaCnaeCollection) {
		this.pessoaJuridicaCnaeCollection = pessoaJuridicaCnaeCollection;
	}

	@JSON(include = false)
	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	@JSON(include = false)
	public NaturezaJuridica getIdeNaturezaJuridica() {
		return ideNaturezaJuridica;
	}

	public void setIdeNaturezaJuridica(NaturezaJuridica ideNaturezaJuridica) {
		this.ideNaturezaJuridica = ideNaturezaJuridica;
	}

	@XmlTransient
	public Collection<PessoaAtividadeHistorico> getPessoaAtividadeHistoricoCollection() {
		return pessoaAtividadeHistoricoCollection;
	}

	public void setPessoaAtividadeHistoricoCollection(Collection<PessoaAtividadeHistorico> pessoaAtividadeHistoricoCollection) {
		this.pessoaAtividadeHistoricoCollection = pessoaAtividadeHistoricoCollection;
	}

	@XmlTransient
	public Collection<RepresentanteLegal> getRepresentanteLegalCollection() {
		return representanteLegalCollection;
	}

	public void setRepresentanteLegalCollection(Collection<RepresentanteLegal> representanteLegalCollection) {
		this.representanteLegalCollection = representanteLegalCollection;
	}

	public boolean isRepresentanteLegalInativado() {
		return isRepresentanteLegalInativado;
	}

	public void setRepresentanteLegalInativado(boolean isRepresentanteLegalInativado) {
		this.isRepresentanteLegalInativado = isRepresentanteLegalInativado;
	}

	public boolean isProcuradorRepresentanteInativado() {
		return isProcuradorRepresentanteInativado;
	}

	public void setProcuradorRepresentanteInativado(boolean isProcuradorRepresentanteInativado) {
		this.isProcuradorRepresentanteInativado = isProcuradorRepresentanteInativado;
	}

	public boolean isParticipanteAcionarioInativado() {
		return isParticipanteAcionarioInativado;
	}

	public void setParticipanteAcionarioInativado(boolean isParticipanteAcionarioInativado) {
		this.isParticipanteAcionarioInativado = isParticipanteAcionarioInativado;
	}

	public Collection<ContratoConvenio> getContratoConvenioCollection() {
		return contratoConvenioCollection;
	}

	public void setContratoConvenioCollection(
			Collection<ContratoConvenio> contratoConvenioCollection) {
		this.contratoConvenioCollection = contratoConvenioCollection;
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
		setIdePessoaFisicaUsuario(AuditoriaUtil.obterUsuario().getIdePessoaFisica());
		setEnderecoIp(AuditoriaUtil.obterIp());
		setCaminhoRequisicao(AuditoriaUtil.obterCaminhoPaginaRequisicao());
	}	
}
