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
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import br.gov.ba.seia.anotation.Historico;
import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.interfaces.Auditoria;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.auditoria.AuditoriaUtil;
import flexjson.JSON;

@Entity
@Table(name = "pessoa_fisica")
@XmlRootElement
@NamedNativeQueries({ @NamedNativeQuery(name = "PessoaFisica.findByIdesPessoaFisicaSQL", query = "SELECT * FROM pessoa_fisica pf inner join pessoa p on pf.ide_pessoa_fisica = p.ide_pessoa WHERE ?1 is not null and pf.ide_pessoa_fisica in (?2) and p.ind_excluido = false or p.ind_excluido = ?3", resultClass = PessoaFisica.class) })
@NamedQueries({
		@NamedQuery(name = "PessoaFisica.findAll", query = "SELECT pf FROM PessoaFisica as pf inner join fetch pf.pessoa as p where p.indExcluido = :indExcluido"),
		@NamedQuery(name = "PessoaFisica.findByIdePessoaFisica", query = "SELECT p FROM PessoaFisica p WHERE p.idePessoaFisica = :idePessoaFisica"),
		@NamedQuery(name = "PessoaFisica.findByIdesPessoaFisica", query = "SELECT pf FROM PessoaFisica pf inner join fetch pf.pessoa as p WHERE pf.idePessoaFisica in (:idesPessoaFisica) AND p.indExcluido = :indExcluido"),
		@NamedQuery(name = "PessoaFisica.findByIdePais", query = "SELECT p FROM PessoaFisica p WHERE p.idePais = :idePais"),
		@NamedQuery(name = "PessoaFisica.findByNomPessoa", query = "SELECT p FROM PessoaFisica p WHERE p.nomPessoa = :nomPessoa"),
		@NamedQuery(name = "PessoaFisica.findByTipSexo", query = "SELECT p FROM PessoaFisica p WHERE p.tipSexo = :tipSexo"),
		@NamedQuery(name = "PessoaFisica.findByDtcNascimento", query = "SELECT p FROM PessoaFisica p WHERE p.dtcNascimento = :dtcNascimento"),
		@NamedQuery(name = "PessoaFisica.findByDesNaturalidade", query = "SELECT p FROM PessoaFisica p WHERE p.desNaturalidade = :desNaturalidade"),
		@NamedQuery(name = "PessoaFisica.findByNomPai", query = "SELECT p FROM PessoaFisica p WHERE p.nomPai = :nomPai"),
		@NamedQuery(name = "PessoaFisica.findByNomMae", query = "SELECT p FROM PessoaFisica p WHERE p.nomMae = :nomMae"),
		@NamedQuery(name = "PessoaFisica.findByNumCpf", query = "SELECT pf FROM PessoaFisica pf inner join fetch pf.pessoa as p WHERE pf.numCpf = :numCpf and p.indExcluido = :indExcluido"),
		@NamedQuery(name = "PessoaFisica.findByNumCpfLoginUsuario", query = "SELECT p FROM PessoaFisica p WHERE p.numCpf = :numCpf and p.usuario.dscLogin = :dscLogin"),
		@NamedQuery(name = "PessoaFisica.findByAreaFuncionarioPerfilTipoUsuario", query = "SELECT p FROM PessoaFisica p WHERE p.funcionario.ideArea.ideArea = :ideArea and p.usuario.indTipoUsuario = :indTipoUsuario and p.usuario.idePerfil.idePerfil = :idePerfil"),
		@NamedQuery(name = "PessoaFisica.findByNumCpfOrNome", query = "SELECT distinct pf FROM PessoaFisica as pf inner join fetch pf.pessoa as p where p.indExcluido = :indExcluido and (pf.numCpf = :numCpf or lower(pf.nomPessoa) like :nomPessoa)"),
		@NamedQuery(name = "PessoaFisica.findByNumCpfOrNomeIdesPessoaFisica", query = "SELECT distinct pf FROM PessoaFisica as pf inner join fetch pf.pessoa as p where pf.idePessoaFisica in (:idesPessoaFisica) AND p.indExcluido = :indExcluido AND (pf.numCpf = :numCpf OR lower(pf.nomPessoa) like :nomPessoa)"),
		@NamedQuery(name = "PessoaFisica.findByNumCpfAndNome", query = "SELECT distinct pf FROM PessoaFisica as pf inner join fetch pf.pessoa as p where p.indExcluido = :indExcluido and (pf.numCpf = :numCpf AND lower(pf.nomPessoa) like :nomPessoa)"),
		
})

public class PessoaFisica extends AbstractEntity implements Auditoria{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pessoafis_pessoa_fk")
	@SequenceGenerator(name = "pessoafis_pessoa_fk", sequenceName = "pessoafis_pessoa_fk", allocationSize = 1)
	@GenericGenerator(name = "pessoafis_pessoa_fk", strategy = "foreign", parameters = @Parameter(name = "property", value = "pessoa"))
	@Column(name = "ide_pessoa_fisica", nullable = false)
	private Integer idePessoaFisica;
	
	@Historico(name="Responsável")
	@Basic(optional = false)
	@Column(name = "nom_pessoa", nullable = false, length = 200)
	private String nomPessoa;
	
	@Column(name = "tip_sexo")
	private Integer tipSexo;
	
	@Column(name = "dtc_nascimento")
	@Temporal(TemporalType.DATE)
	private Date dtcNascimento;
	
	@Column(name = "des_naturalidade", length = 70)
	private String desNaturalidade;
	
	@Column(name = "nom_pai", length = 200)
	private String nomPai;
	
	@Column(name = "nom_mae", length = 200)
	private String nomMae;
	
	@Basic(optional = false)
	@Column(name = "num_cpf", nullable = false, length = 11)
	private String numCpf;
	
	@JoinColumn(name = "ide_pessoa_fisica", referencedColumnName = "ide_pessoa", nullable = false, insertable = false, updatable = false)
	@OneToOne(optional = false, fetch = FetchType.EAGER)
	@PrimaryKeyJoinColumn
	private Pessoa pessoa;
	
	@JoinColumn(name = "ide_pais", referencedColumnName = "ide_pais")
	@ManyToOne(fetch = FetchType.LAZY)
	private Pais idePais;
	
	@JoinColumn(name = "ide_ocupacao", referencedColumnName = "ide_ocupacao")
	@ManyToOne(fetch = FetchType.LAZY)
	private Ocupacao ideOcupacao;
	
	@JoinColumn(name = "ide_estado_civil", referencedColumnName = "ide_estado_civil")
	@ManyToOne(fetch = FetchType.LAZY)
	private EstadoCivil ideEstadoCivil;
	
	@JoinColumn(name = "ide_escolaridade", referencedColumnName = "ide_escolaridade")
	@ManyToOne(fetch = FetchType.EAGER)
	private Escolaridade ideEscolaridade;
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "pessoaFisica", fetch = FetchType.EAGER)
	private Funcionario funcionario;
	
	@OneToOne(mappedBy = "pessoaFisica", fetch = FetchType.LAZY)
	private Usuario usuario;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idePessoa1", fetch = FetchType.LAZY)
	private Collection<PessoaEnderecoHistorico> pessoaEnderecoHistoricoCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idePessoaFisica", fetch = FetchType.LAZY)
	private Collection<PessoaFisicaHistorico> pessoaFisicaHistoricoCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idePessoaFisica", fetch = FetchType.LAZY)
	private Collection<ResponsavelEmpreendimento> responsavelEmpreendimentoCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideProcurador", fetch = FetchType.LAZY)
	private Collection<ProcuradorRepresentante> procuradorRepresentanteCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idePessoaFisica", fetch = FetchType.LAZY)
	private Collection<ProcuradorPessoaFisica> procuradorPessoaFisicaCollection;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideProcurador", fetch = FetchType.LAZY)
	private Collection<ProcuradorPessoaFisica> procuradorPessoaFisicaCollection1;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idePessoaFisica", fetch = FetchType.LAZY)
	private Collection<RepresentanteLegal> representanteLegalCollection;
	
	@OneToMany(mappedBy="idePessoaFisica", fetch=FetchType.LAZY)
    private Collection<FuncionalidadeAcaoPessoaFisica> funcionalidadeAcaoPessoaFisicaCollection;
	
	@OneToMany(mappedBy="idePessoaFisica", fetch=FetchType.LAZY)
    private Collection<ControleTramitacao> controleTramitacaoCollection;
	
	@Column(name = "ide_usuario")
	private Integer idePessoaFisicaUsuario;
	
	@Column(name = "endereco_ip")
	private String enderecoIp;
	
	@Column(name = "caminho_requisicao")
	private String caminhoRequisicao;

	@Transient
	private String numRg;
	
	@Transient
	private boolean isResponsavelTecnico;
	
	@Transient
	private String cpfRepresentante;
	
	public PessoaFisica() {
	}

	public PessoaFisica(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	
	public PessoaFisica(String numCpf) {
		this.numCpf = numCpf;
	}

	public PessoaFisica(Integer idePessoaFisica) {
		this.idePessoaFisica = idePessoaFisica;
	}

	public PessoaFisica(Usuario usuario) {
		this.usuario = usuario;
	}

	public PessoaFisica(Integer idePessoaFisica, String nomPessoa) {
		this.idePessoaFisica = idePessoaFisica;
		this.nomPessoa = nomPessoa.trim().replaceAll("  +", " ");
	}
	
	public PessoaFisica(Integer idePessoaFisica, String nomPessoa, String numCpf) {
		this.idePessoaFisica = idePessoaFisica;
		this.nomPessoa = nomPessoa.trim().replaceAll("  +", " ");
		this.numCpf = numCpf;
	}

	public PessoaFisica(Funcionario funcionario, Usuario usuario) {
		this.funcionario = funcionario;
		this.usuario = usuario;
	}

	public PessoaFisica(String nomPessoa, String numCpf) {
		this.nomPessoa = nomPessoa;
		this.numCpf = numCpf;
	}

	public PessoaFisica(String numCpf, Usuario usuario) {
		this.numCpf = numCpf;
		this.usuario = usuario;
	}

	public PessoaFisica(Integer idePessoaFisica, Pais idePais, String nomPessoa, String numCpf) {
		this.idePessoaFisica = idePessoaFisica;
		this.idePais = idePais;
		this.nomPessoa = nomPessoa.trim().replaceAll("  +", " ");
		this.numCpf = numCpf;
	}

	public Integer getIdePessoaFisica() {
		return idePessoaFisica;
	}

	public void setIdePessoaFisica(Integer idePessoaFisica) {
		this.idePessoaFisica = idePessoaFisica;
	}

	public String getNomPessoa() {
		if (this.nomPessoa != null) {
			return this.nomPessoa.trim();
		} else {
			return nomPessoa;
		}
	}

	public void setNomPessoa(String nomPessoa) {
		this.nomPessoa = nomPessoa.trim().replaceAll("  +", " ");;
	}

	@JSON (include = false)
	public Integer getTipSexo() {
		return tipSexo;
	}

	public void setTipSexo(Integer tipSexo) {
		this.tipSexo = tipSexo;
	}

	@JSON(include = false)
	public Date getDtcNascimento() {
		return dtcNascimento;
	}

	public void setDtcNascimento(Date dtcNascimento) {
		this.dtcNascimento = dtcNascimento;
	}

	@JSON(include = false)
	public String getDesNaturalidade() {
		return desNaturalidade;
	}

	public void setDesNaturalidade(String desNaturalidade) {
		this.desNaturalidade = desNaturalidade;
	}

	@JSON(include = false)
	public String getNomPai() {
		return nomPai;
	}

	public void setNomPai(String nomPai) {
		this.nomPai = nomPai.trim();
	}

	@JSON(include = false)
	public String getNomMae() {
		return nomMae;
	}

	public void setNomMae(String nomMae) {
		this.nomMae = nomMae.trim();
	}

	public String getNumCpf() {
		return numCpf;
	}

	public void setNumCpf(String numCpf) {
		this.numCpf = numCpf;
	}

	@XmlTransient
	public Collection<PessoaEnderecoHistorico> getPessoaEnderecoHistoricoCollection() {
		return pessoaEnderecoHistoricoCollection;
	}

	public void setPessoaEnderecoHistoricoCollection(Collection<PessoaEnderecoHistorico> pessoaEnderecoHistoricoCollection) {
		this.pessoaEnderecoHistoricoCollection = pessoaEnderecoHistoricoCollection;
	}

	@JSON(include = false)
	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	@XmlTransient
	public Collection<PessoaFisicaHistorico> getPessoaFisicaHistoricoCollection() {
		return pessoaFisicaHistoricoCollection;
	}

	public void setPessoaFisicaHistoricoCollection(Collection<PessoaFisicaHistorico> pessoaFisicaHistoricoCollection) {
		this.pessoaFisicaHistoricoCollection = pessoaFisicaHistoricoCollection;
	}

	@XmlTransient
	public Collection<ResponsavelEmpreendimento> getResponsavelEmpreendimentoCollection() {
		return responsavelEmpreendimentoCollection;
	}

	public void setResponsavelEmpreendimentoCollection(Collection<ResponsavelEmpreendimento> responsavelEmpreendimentoCollection) {
		this.responsavelEmpreendimentoCollection = responsavelEmpreendimentoCollection;
	}

	@JSON(include = false)
	public Pessoa getPessoa() {
		if (Util.isNull(pessoa))
			pessoa = new Pessoa();
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	@JSON(include = false)
	public Pais getIdePais() {
		return idePais;
	}

	public void setIdePais(Pais idePais) {
		this.idePais = idePais;
	}

	@JSON(include = false)
	public Ocupacao getIdeOcupacao() {
		return ideOcupacao;
	}

	public void setIdeOcupacao(Ocupacao ideOcupacao) {
		this.ideOcupacao = ideOcupacao;
	}

	@JSON(include = false)
	public EstadoCivil getIdeEstadoCivil() {
		return ideEstadoCivil;
	}

	public void setIdeEstadoCivil(EstadoCivil ideEstadoCivil) {
		this.ideEstadoCivil = ideEstadoCivil;
	}

	@JSON(include = false)
	public Escolaridade getIdeEscolaridade() {
		return ideEscolaridade;
	}

	public void setIdeEscolaridade(Escolaridade ideEscolaridade) {
		this.ideEscolaridade = ideEscolaridade;
	}

	@XmlTransient
	public Collection<ProcuradorRepresentante> getProcuradorRepresentanteCollection() {
		return procuradorRepresentanteCollection;
	}

	public void setProcuradorRepresentanteCollection(Collection<ProcuradorRepresentante> procuradorRepresentanteCollection) {
		this.procuradorRepresentanteCollection = procuradorRepresentanteCollection;
	}

	@JSON(include = false)
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@XmlTransient
	public Collection<ProcuradorPessoaFisica> getProcuradorPessoaFisicaCollection() {
		return procuradorPessoaFisicaCollection;
	}

	public void setProcuradorPessoaFisicaCollection(Collection<ProcuradorPessoaFisica> procuradorPessoaFisicaCollection) {
		this.procuradorPessoaFisicaCollection = procuradorPessoaFisicaCollection;
	}

	@XmlTransient
	public Collection<ProcuradorPessoaFisica> getProcuradorPessoaFisicaCollection1() {
		return procuradorPessoaFisicaCollection1;
	}

	public void setProcuradorPessoaFisicaCollection1(Collection<ProcuradorPessoaFisica> procuradorPessoaFisicaCollection1) {
		this.procuradorPessoaFisicaCollection1 = procuradorPessoaFisicaCollection1;
	}

	@XmlTransient
	public Collection<RepresentanteLegal> getRepresentanteLegalCollection() {
		return representanteLegalCollection;
	}

	public void setRepresentanteLegalCollection(Collection<RepresentanteLegal> representanteLegalCollection) {
		this.representanteLegalCollection = representanteLegalCollection;
	}

	@JSON(include = false)
	public String getNumRg() {
		return numRg;
	}

	public void setNumRg(String numRg) {
		this.numRg = numRg;
	}

	public Collection<FuncionalidadeAcaoPessoaFisica> getFuncionalidadeAcaoPessoaFisicaCollection() {
		return funcionalidadeAcaoPessoaFisicaCollection;
	}

	public void setFuncionalidadeAcaoPessoaFisicaCollection(Collection<FuncionalidadeAcaoPessoaFisica> funcionalidadeAcaoPessoaFisicaCollection) {
		this.funcionalidadeAcaoPessoaFisicaCollection = funcionalidadeAcaoPessoaFisicaCollection;
	}

	public Collection<ControleTramitacao> getControleTramitacaoCollection() {
		return controleTramitacaoCollection;
	}

	public void setControleTramitacaoCollection(Collection<ControleTramitacao> controleTramitacaoCollection) {
		this.controleTramitacaoCollection = controleTramitacaoCollection;
	}

	public boolean isResponsavelTecnico() {
		return isResponsavelTecnico;
	}

	public void setResponsavelTecnico(boolean isResponsavelTecnico) {
		this.isResponsavelTecnico = isResponsavelTecnico;
	}

	public String getCpfRepresentante() {
		return cpfRepresentante;
	}

	public void setCpfRepresentante(String cpfRepresentante) {
		this.cpfRepresentante = cpfRepresentante;
	}

	public String getNumCpfFormatado() {
		return Util.formatarCPF(this.getNumCpf());
	}
	
	public String getNumCpfFormatadoRepresentante() {
		return Util.formatarCPF(this.getCpfRepresentante());
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
	public Integer getIdePessoaFisicaUsuario() {
		return idePessoaFisicaUsuario;
	}

	@Override
	public void setIdePessoaFisicaUsuario(Integer idePessoaFisicaUsuario) {
		this.idePessoaFisicaUsuario = idePessoaFisicaUsuario ;
	}

	@Override
	public void capturarCamposAuditoria() {
		if("/login.xhtml".equals(AuditoriaUtil.obterCaminhoPaginaRequisicao())){
			setIdePessoaFisicaUsuario(33289);
		}else if(Util.isNullOuVazio(AuditoriaUtil.obterUsuario())){
			setIdePessoaFisicaUsuario(0);
		}else{
			setIdePessoaFisicaUsuario(AuditoriaUtil.obterUsuario().getIdePessoaFisica());
		}
		setEnderecoIp(AuditoriaUtil.obterIp());
		setCaminhoRequisicao(AuditoriaUtil.obterCaminhoPaginaRequisicao());
	}	
}
