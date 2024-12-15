package br.gov.ba.seia.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.TipoAtividadeNaoSujeitaLicenciamentoEnum;
import br.gov.ba.seia.util.Util;


/**
 * @author eduardo.fernandes
 * @since 03/11/2016
 * @see <a href="http://10.105.17.77/redmine/issues/">#8187</a>
 */
@Entity
@Table(name="cadastro_atividade_nao_sujeita_lic")
@NamedQuery(name="CadastroAtividadeNaoSujeitaLic.findAll", query="SELECT c FROM CadastroAtividadeNaoSujeitaLic c")
public class CadastroAtividadeNaoSujeitaLic extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "CADASTRO_ATIVIDADE_NAO_SUJEITA_LIC_GENERATOR", sequenceName = "CADASTRO_ATIVIDADE_NAO_SUJEITA_LIC_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CADASTRO_ATIVIDADE_NAO_SUJEITA_LIC_GENERATOR")
	@Column(name="ide_cadastro_atividade_nao_sujeita_lic", nullable = false)
	private Integer ideCadastroAtividadeNaoSujeitaLic;

	@Column(name="dtc_cadastro", nullable = false)
	private Date dtcCadastro;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_pessoa_fisica_cadastro", referencedColumnName = "ide_pessoa_fisica", nullable = false)
	private PessoaFisica idePessoaFisicaCadastro;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_pessoa_requerente", referencedColumnName = "ide_pessoa", nullable = false)
	private Pessoa idePessoaRequerente;

	@Column(name = "num_cadastro")
	private String numCadastro;

	@Column(name="ind_excluido", nullable = false)
	private Boolean indExcluido;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_empreendimento", referencedColumnName = "ide_empreendimento", nullable = false)
	private Empreendimento ideEmpreendimento;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ide_certificado", referencedColumnName = "ide_certificado")
	private Certificado ideCertificado;

	//bi-directional many-to-one association to TipoAtividadeNaoSujeitaLicenciamento
	@ManyToOne
	@JoinColumn(name="ide_tipo_atividade_nao_sujeita_licenciamento", nullable = false)
	private TipoAtividadeNaoSujeitaLicenciamento tipoAtividadeNaoSujeitaLicenciamento;

	//bi-directional many-to-one association to CadastroAtividadeNaoSujeitaLicDocApensado
	@OneToMany(mappedBy = "cadastroAtividadeNaoSujeitaLic")
	private List<CadastroAtividadeNaoSujeitaLicDocApensado> cadastroAtividadeNaoSujeitaLicDocApensados;

	//bi-directional many-to-one association to CadastroAtividadeNaoSujeitaLicStatus
	@OneToMany(mappedBy = "cadastroAtividadeNaoSujeitaLic")
	private List<CadastroAtividadeNaoSujeitaLicStatus> cadastroAtividadeNaoSujeitaLicStatus;

	//bi-directional many-to-one association to PesquisaMineral
	@OneToMany(mappedBy = "cadastroAtividadeNaoSujeitaLic")
	private List<PesquisaMineral> pesquisaMinerals;
	
	//bi-directional many-to-one association to CadastroAtividadeNaoSujeitaLicComunicacao
	@OneToMany(mappedBy = "cadastroAtividadeNaoSujeitaLic")
	private List<CadastroAtividadeNaoSujeitaLicComunicacao> cadastroAtividadeNaoSujeitaLicComunicacaos;
	
	//bi-directional many-to-one association to CadastroAtividadeDocumentoIdentificacaoRepresentacao
	@OneToMany(mappedBy = "ideCadastroAtividadeNaoSujeitaLic")
	private List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> CadastroAtividadeDocumentoIdentificacaoRepresentacaos;
	
	@OneToMany(mappedBy = "ideCadastroAtividadeNaoSujeitaLic")
	private List<CadastroAtividadeNaoSujeitaLicImovel> cadastroAtividadeNaoSujeitoLicImovel;
	
	@OneToMany(mappedBy = "ideCadastroAtividadeNaoSujeitaLic")
	private List<CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento> listaResponsaveisSelecionados;
	
	
	@Column(name = "ind_possui_cefir")
	private Boolean indPossuiCefir;
	/**
	 * Propriedade: indImovelRural
	 * @type: boolean
	 */
	@Transient
	private boolean indImovelRural;

	@Transient
	private CadastroAtividadeNaoSujeitaLicTipoStatus tipoStatus;

	@Transient
	private String nomRequerente;

	@Transient
	private String numCpfcnpjRequerente;

	/**
	 * Propriedade: visualizar
	 * @type: boolean
	 */
	@Transient
	private boolean visualizar;
	
	/**
	 * Propriedade: edicao
	 * @type: boolean
	 */
	@Transient
	private boolean edicao;
	

	public CadastroAtividadeNaoSujeitaLic() {
	}
	
	public CadastroAtividadeNaoSujeitaLic(PessoaFisica idePessoaFisicaCadastro, Pessoa idePessoa, TipoAtividadeNaoSujeitaLicenciamentoEnum atividadeNaoSujeitaLicenciamentoEnum) {
		this.idePessoaFisicaCadastro = idePessoaFisicaCadastro;
		this.idePessoaRequerente = idePessoa;
		this.tipoAtividadeNaoSujeitaLicenciamento = new TipoAtividadeNaoSujeitaLicenciamento(atividadeNaoSujeitaLicenciamentoEnum);
		this.dtcCadastro = new Date();
		this.indExcluido = false;
		this.indImovelRural = false;
		this.cadastroAtividadeNaoSujeitaLicDocApensados = new ArrayList<CadastroAtividadeNaoSujeitaLicDocApensado>();
		this.cadastroAtividadeNaoSujeitaLicStatus = new ArrayList<CadastroAtividadeNaoSujeitaLicStatus>();
		this.indPossuiCefir = false;
	}

	public Integer getIdeCadastroAtividadeNaoSujeitaLic() {
		return this.ideCadastroAtividadeNaoSujeitaLic;
	}

	public void setIdeCadastroAtividadeNaoSujeitaLic(Integer ideCadastroAtividadeNaoSujeitaLic) {
		this.ideCadastroAtividadeNaoSujeitaLic = ideCadastroAtividadeNaoSujeitaLic;
	}

	public Date getDtcCadastro() {
		return this.dtcCadastro;
	}

	public void setDtcCadastro(Date dtcCadastro) {
		this.dtcCadastro = dtcCadastro;
	}

	public PessoaFisica getIdePessoaFisicaCadastro() {
		return this.idePessoaFisicaCadastro;
	}

	public void setIdePessoaFisicaCadastro(PessoaFisica idePessoaFisicaCadastro) {
		this.idePessoaFisicaCadastro = idePessoaFisicaCadastro;
	}

	public Pessoa getIdePessoaRequerente() {
		return this.idePessoaRequerente;
	}

	public void setIdePessoaRequerente(Pessoa idePessoaRequerente) {
		this.idePessoaRequerente = idePessoaRequerente;
	}

	public Boolean getIndExcluido() {
		return this.indExcluido;
	}

	public void setIndExcluido(Boolean indExcluido) {
		this.indExcluido = indExcluido;
	}

	public TipoAtividadeNaoSujeitaLicenciamento getTipoAtividadeNaoSujeitaLicenciamento() {
		return this.tipoAtividadeNaoSujeitaLicenciamento;
	}

	public void setTipoAtividadeNaoSujeitaLicenciamento(TipoAtividadeNaoSujeitaLicenciamento tipoAtividadeNaoSujeitaLicenciamento) {
		this.tipoAtividadeNaoSujeitaLicenciamento = tipoAtividadeNaoSujeitaLicenciamento;
	}

	public List<CadastroAtividadeNaoSujeitaLicDocApensado> getCadastroAtividadeNaoSujeitaLicDocApensados() {
		return this.cadastroAtividadeNaoSujeitaLicDocApensados;
	}

	public void setCadastroAtividadeNaoSujeitaLicDocApensados(List<CadastroAtividadeNaoSujeitaLicDocApensado> cadastroAtividadeNaoSujeitaLicDocApensados) {
		this.cadastroAtividadeNaoSujeitaLicDocApensados = cadastroAtividadeNaoSujeitaLicDocApensados;
	}

	public CadastroAtividadeNaoSujeitaLicDocApensado addCadastroAtividadeNaoSujeitaLicDocApensado(CadastroAtividadeNaoSujeitaLicDocApensado cadastroAtividadeNaoSujeitaLicDocApensado) {
		if (Util.isNull(getCadastroAtividadeNaoSujeitaLicDocApensados())) {
			setCadastroAtividadeNaoSujeitaLicDocApensados(new ArrayList<CadastroAtividadeNaoSujeitaLicDocApensado>());
		}
		getCadastroAtividadeNaoSujeitaLicDocApensados().add(cadastroAtividadeNaoSujeitaLicDocApensado);
		cadastroAtividadeNaoSujeitaLicDocApensado.setCadastroAtividadeNaoSujeitaLic(this);

		return cadastroAtividadeNaoSujeitaLicDocApensado;
	}

	public CadastroAtividadeNaoSujeitaLicDocApensado removeCadastroAtividadeNaoSujeitaLicDocApensado(CadastroAtividadeNaoSujeitaLicDocApensado cadastroAtividadeNaoSujeitaLicDocApensado) {
		getCadastroAtividadeNaoSujeitaLicDocApensados().remove(cadastroAtividadeNaoSujeitaLicDocApensado);
		cadastroAtividadeNaoSujeitaLicDocApensado.setCadastroAtividadeNaoSujeitaLic(null);

		return cadastroAtividadeNaoSujeitaLicDocApensado;
	}

	public List<CadastroAtividadeNaoSujeitaLicStatus> getCadastroAtividadeNaoSujeitaLicStatus() {
		return this.cadastroAtividadeNaoSujeitaLicStatus;
	}

	public void setCadastroAtividadeNaoSujeitaLicStatus(List<CadastroAtividadeNaoSujeitaLicStatus> cadastroAtividadeNaoSujeitaLicStatus) {
		this.cadastroAtividadeNaoSujeitaLicStatus = cadastroAtividadeNaoSujeitaLicStatus;
	}

	public CadastroAtividadeNaoSujeitaLicStatus addCadastroAtividadeNaoSujeitaLicStatus(CadastroAtividadeNaoSujeitaLicStatus cadastroAtividadeNaoSujeitaLicStatus) {
		getCadastroAtividadeNaoSujeitaLicStatus().add(cadastroAtividadeNaoSujeitaLicStatus);
		cadastroAtividadeNaoSujeitaLicStatus.setCadastroAtividadeNaoSujeitaLic(this);

		return cadastroAtividadeNaoSujeitaLicStatus;
	}

	public CadastroAtividadeNaoSujeitaLicStatus removeCadastroAtividadeNaoSujeitaLicStatus(CadastroAtividadeNaoSujeitaLicStatus cadastroAtividadeNaoSujeitaLicStatus) {
		getCadastroAtividadeNaoSujeitaLicStatus().remove(cadastroAtividadeNaoSujeitaLicStatus);
		cadastroAtividadeNaoSujeitaLicStatus.setCadastroAtividadeNaoSujeitaLic(null);

		return cadastroAtividadeNaoSujeitaLicStatus;
	}

	public List<PesquisaMineral> getPesquisaMinerals() {
		return this.pesquisaMinerals;
	}

	public void setPesquisaMinerals(List<PesquisaMineral> pesquisaMinerals) {
		this.pesquisaMinerals = pesquisaMinerals;
	}

	public PesquisaMineral addPesquisaMineral(PesquisaMineral pesquisaMineral) {
		getPesquisaMinerals().add(pesquisaMineral);
		pesquisaMineral.setCadastroAtividadeNaoSujeitaLic(this);

		return pesquisaMineral;
	}

	public PesquisaMineral removePesquisaMineral(PesquisaMineral pesquisaMineral) {
		getPesquisaMinerals().remove(pesquisaMineral);
		pesquisaMineral.setCadastroAtividadeNaoSujeitaLic(null);

		return pesquisaMineral;
	}

	public String getNumCadastro() {
		return numCadastro;
	}
	
	public String getNumCadastroConsulta() {
		String numero = "";
		if(!Util.isNullOuVazio(numCadastro)){
			int index = numCadastro.indexOf('/');
			numero = numCadastro.substring(0, index);
			numero = numero + ' ' + numCadastro.substring(index);
		}
		return numero;
	}

	public void setNumCadastro(String numCadastro) {
		this.numCadastro = numCadastro;
	}

	public Empreendimento getIdeEmpreendimento() {
		return this.ideEmpreendimento;
	}

	public void setIdeEmpreendimento(Empreendimento ideEmpreendimento) {
		this.ideEmpreendimento = ideEmpreendimento;
	}

	public CadastroAtividadeNaoSujeitaLicTipoStatus getTipoStatus() {
		return tipoStatus;
	}

	public void setTipoStatus(CadastroAtividadeNaoSujeitaLicTipoStatus tipoStatus) {
		this.tipoStatus = tipoStatus;
	}

	public String getNumCpfcnpjRequerente() {
		if (!Util.isNullOuVazio(numCpfcnpjRequerente) && numCpfcnpjRequerente.length() == 14) {
			return Util.formatarCNPJ(numCpfcnpjRequerente);
		}
		else {
			return Util.formatarCPF(numCpfcnpjRequerente);
		}
	}

	public void setNumCpfcnpjRequerente(String numCpfcnpjRequerente) {
		this.numCpfcnpjRequerente = numCpfcnpjRequerente;
	}

	public String getNomRequerente() {
		return nomRequerente;
	}

	public void setNomRequerente(String nomRequerente) {
		this.nomRequerente = nomRequerente;
	}

	public boolean isVisualizar() {
		return visualizar;
	}

	public void setVisualizar(boolean visualizar) {
		this.visualizar = visualizar;
	}

	public List<CadastroAtividadeNaoSujeitaLicComunicacao> getCadastroAtividadeNaoSujeitaLicComunicacaos() {
		return cadastroAtividadeNaoSujeitaLicComunicacaos;
	}

	public void setCadastroAtividadeNaoSujeitaLicComunicacaos(
			List<CadastroAtividadeNaoSujeitaLicComunicacao> cadastroAtividadeNaoSujeitaLicComunicacaos) {
		this.cadastroAtividadeNaoSujeitaLicComunicacaos = cadastroAtividadeNaoSujeitaLicComunicacaos;
	}

	public List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> getCadastroAtividadeDocumentoIdentificacaoRepresentacaos() {
		return CadastroAtividadeDocumentoIdentificacaoRepresentacaos;
	}

	public void setCadastroAtividadeDocumentoIdentificacaoRepresentacaos(
			List<CadastroAtividadeDocumentoIdentificacaoRepresentacao> cadastroAtividadeDocumentoIdentificacaoRepresentacaos) {
		CadastroAtividadeDocumentoIdentificacaoRepresentacaos = cadastroAtividadeDocumentoIdentificacaoRepresentacaos;
	}

	public boolean isEdicao() {
		return edicao;
	}

	public void setEdicao(boolean edicao) {
		this.edicao = edicao;
	}

	public CadastroAtividadeNaoSujeitaLicStatus getCadastroUltimoStatus(){
		if(!Util.isNullOuVazio(cadastroAtividadeNaoSujeitaLicStatus)){
			return this.cadastroAtividadeNaoSujeitaLicStatus.get(cadastroAtividadeNaoSujeitaLicStatus.size() - 1);
		}
		return null;
	}

	public Certificado getIdeCertificado() {
		return ideCertificado;
	}

	public void setIdeCertificado(Certificado ideCertificado) {
		this.ideCertificado = ideCertificado;
	}

	/**
	 * Getter do campo indImovelRural
	 *	
	 * @return the indImovelRural
	 */
	public boolean isIndImovelRural() {
		return indImovelRural;
	}

	/**
	 * Setter do campo  indImovelRural
	 * @param indImovelRural the indImovelRural to set
	 */
	public void setIndImovelRural(boolean indImovelRural) {
		this.indImovelRural = indImovelRural;
	}

	/**
	 * Getter do campo cadastroAtividadeNaoSujeitoLicImovel
	 *	
	 * @return the cadastroAtividadeNaoSujeitoLicImovel
	 */
	public List<CadastroAtividadeNaoSujeitaLicImovel> getCadastroAtividadeNaoSujeitoLicImovel() {
		return cadastroAtividadeNaoSujeitoLicImovel;
	}

	/**
	 * Setter do campo  cadastroAtividadeNaoSujeitoLicImovel
	 * @param cadastroAtividadeNaoSujeitoLicImovel the cadastroAtividadeNaoSujeitoLicImovel to set
	 */
	public void setCadastroAtividadeNaoSujeitoLicImovel(
			List<CadastroAtividadeNaoSujeitaLicImovel> cadastroAtividadeNaoSujeitoLicImovel) {
		this.cadastroAtividadeNaoSujeitoLicImovel = cadastroAtividadeNaoSujeitoLicImovel;
	}

	/**
	 * Getter do campo indImovelCefir
	 *	
	 * @return the indImovelCefir
	 */
	public Boolean getIndPossuiCefir() {
		return indPossuiCefir;
	}

	/**
	 * Setter do campo  indImovelCefir
	 * @param indImovelCefir the indImovelCefir to set
	 */
	public void setIndPossuiCefir(Boolean indPossuiCefir) {
		this.indPossuiCefir = indPossuiCefir;
	}

	public List<CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento> getListaResponsaveisSelecionados() {
		return listaResponsaveisSelecionados;
	}

	public void setListaResponsaveisSelecionados(
			List<CadastroAtividadeNaoSujeitaLicResponsavelEmpreendimento> listaResponsaveisSelecionados) {
		this.listaResponsaveisSelecionados = listaResponsaveisSelecionados;
	}
	
 	
	
}