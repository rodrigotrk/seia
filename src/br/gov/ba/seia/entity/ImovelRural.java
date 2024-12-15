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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Parameter;

import br.gov.ba.seia.entity.generic.AbstractEntity;
import br.gov.ba.seia.enumerator.FaseAssentamentoImovelRuralEnum;
import br.gov.ba.seia.enumerator.StatusCadastroImovelRuralEnum;
import br.gov.ba.seia.enumerator.StatusReservaLegalEnum;
import br.gov.ba.seia.enumerator.TipoCadastroImovelRuralEnum;
import br.gov.ba.seia.util.Util;
import javax.persistence.EntityListeners;

/**
 * @author micael.coutinho
 */
@EntityListeners(ImovelRuralListener.class)
@Entity
@Table(name = "imovel_rural")
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "ImovelRural.findByIdeImovelRural", query = "SELECT i FROM ImovelRural i WHERE i.ideImovelRural = :ideImovelRural"),
		@NamedQuery(name = "ImovelRural.findByNumItr", query = "SELECT i FROM ImovelRural i WHERE i.numItr = :numItr"),
		@NamedQuery(name = "ImovelRural.findByNumRegistro", query = "SELECT i FROM ImovelRural i WHERE i.numRegistro = :numRegistro"),
		@NamedQuery(name = "ImovelRural.findByNumCcir", query = "SELECT i FROM ImovelRural i WHERE i.numCcir = :numCcir"),
		@NamedQuery(name = "ImovelRural.findByIndPassivoAmbiental", query = "SELECT i FROM ImovelRural i WHERE i.indPassivoAmbiental = :indPassivoAmbiental"),
		@NamedQuery(name = "ImovelRural.findByIndParaAderido", query = "SELECT i FROM ImovelRural i WHERE i.indParaAderido = :indParaAderido"),
		@NamedQuery(name = "ImovelRural.findByIndAderirPara", query = "SELECT i FROM ImovelRural i WHERE i.indAderirPara = :indAderirPara"),
		@NamedQuery(name = "ImovelRural.findByNumProcessoPara", query = "SELECT i FROM ImovelRural i WHERE i.numProcessoPara = :numProcessoPara"),
		@NamedQuery(name = "ImovelRural.findByIndApp", query = "SELECT i FROM ImovelRural i WHERE i.indApp = :indApp"),
		@NamedQuery(name = "ImovelRural.findByIndAreaProdutiva", query = "SELECT i FROM ImovelRural i WHERE i.indAreaProdutiva = :indAreaProdutiva"),
		@NamedQuery(name = "ImovelRural.findByIndVegetacaoNativa", query = "SELECT i FROM ImovelRural i WHERE i.indVegetacaoNativa = :indVegetacaoNativa"),
		@NamedQuery(name = "ImovelRural.findByIndAgrotoxico", query = "SELECT i FROM ImovelRural i WHERE i.indAgrotoxico = :indAgrotoxico"),
		@NamedQuery(name = "ImovelRural.findByIndOutorga", query = "SELECT i FROM ImovelRural i WHERE i.indOutorga = :indOutorga"),
		@NamedQuery(name = "ImovelRural.findByStsCertificado", query = "SELECT i FROM ImovelRural i WHERE i.stsCertificado = :stsCertificado"),
		@NamedQuery(name = "ImovelRural.findByQtdModuloFiscal", query = "SELECT i FROM ImovelRural i WHERE i.qtdModuloFiscal = :qtdModuloFiscal"),
		@NamedQuery(name = "ImovelRural.findByToken", query = "SELECT i FROM ImovelRural i WHERE i.token = :token"), 
		@NamedQuery(name = "ImovelRural.findByIndBloqueioLimite", query = "SELECT i FROM ImovelRural i WHERE i.indBloqueioLimite = :indBloqueioLimite"),})
public class ImovelRural extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/************************
	 * /* * //XXX: OBJETOS SIMPLES * /* * /
	 ************************/

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "imovelrur_imo_fk")
	@SequenceGenerator(name = "imovelrur_imo_fk", sequenceName = "imovelrur_imo_fk", allocationSize = 1)
	@GenericGenerator(name = "imovelrur_imo_fk", strategy = "foreign", parameters = @Parameter(name = "property", value = "imovel"))
	@Basic(optional = false)
	@NotNull
	@Column(name = "ide_imovel_rural", nullable = false)
	private Integer ideImovelRural;

	@Size(max = 15)
	@Column(name = "num_itr")
	private String numItr;

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "des_denominacao")
	private String desDenominacao;

	@Basic(optional = false)
	@NotNull
	@Column(name = "val_area")
	private Double valArea;

	@Basic(optional = true)
	@Size(max = 60)
	@Column(name = "des_cartorio")
	private String desCartorio;

	@Basic(optional = true)
	@Size(max = 60)
	@Column(name = "des_comarca")
	private String desComarca;

	@Basic(optional = true)
	@Size(max = 50)
	@Column(name = "des_livro")
	private String desLivro;

	@Basic(optional = true)
	@Column(name = "num_folha")
	private String numFolha;

	@Basic(optional = false)
	@Column(name = "ind_reserva_legal")
	private Boolean indReservaLegal;

	@Size(max = 15)
	@Column(name = "num_matricula")
	private String numMatricula;

	@Size(max = 15)
	@Column(name = "num_registro")
	private String numRegistro;

	@Size(max = 15)
	@Column(name = "num_ccir")
	private String numCcir;

	@Size(max = 15)
	@Column(name = "num_sncr")
	private String numSncr;

	@Basic(optional = false)
	@Column(name = "ind_passivo_ambiental")
	private Boolean indPassivoAmbiental;

	@Column(name = "ind_permissao_asv")
	private Boolean indPermissaoASV;

	@Column(name = "ind_para_aderido")
	private Boolean indParaAderido;

	@Column(name = "ind_aderir_para")
	private Boolean indAderirPara;

	@Size(max = 40)
	@Column(name = "num_processo_para")
	private String numProcessoPara;

	@Basic(optional = false)
	@Column(name = "ind_app")
	private Boolean indApp;

	@Basic(optional = false)
	@Column(name = "ind_area_produtiva")
	private Boolean indAreaProdutiva;

	@Basic(optional = false)
	@Column(name = "ind_vegetacao_nativa")
	private Boolean indVegetacaoNativa;

	@Column(name = "ind_agrotoxico")
	private Boolean indAgrotoxico;

	@Basic(optional = false)
	@Column(name = "ind_outorga")
	private Boolean indOutorga;

	@Column(name = "ind_supressao_vegetacao")
	private Boolean indSupressaoVegetacao;

	@Basic(optional = false)
	@Column(name = "sts_certificado")
	private Boolean stsCertificado;

	@Column(name = "qtd_modulo_fiscal")
	private Double qtdModuloFiscal;

	@Basic(optional = false)
	@Column(name = "status_cadastro")
	private Integer statusCadastro;

	@Size(max = 10)
	@Column(name = "token")
	private String token;

	@Column(name = "prazo_validade")
	@Temporal(TemporalType.DATE)
	private Date prazoValidade;

	@Basic(optional = false)
	@Column(name = "ind_precisa_outorga")
	private Boolean indPrecisaOutorga;

	@Basic(optional = false)
	@Column(name = "ind_precisa_licenca")
	private Boolean indPrecisaLicenca;

	@Basic(optional = false)
	@Column(name = "ind_tem_prad")
	private Boolean indTemPrad;

	@Basic(optional = false)
	@Column(name = "ind_outros_passivos")
	private Boolean indOutrosPassivos;

	@Column(name = "dtc_finalizacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcFinalizacao;

	@Basic(optional = false)
	@Column(name = "ind_deseja_completar_informacoes")
	private Boolean indDesejaCompletarInformacoes;

	@Basic(optional = false)
	@Column(name = "ind_captacao_concessionaria")
	private Boolean indConcessionaria;

	@Basic(optional = false)
	@Column(name = "ind_captacao_precipitacao")
	private Boolean indPrecipitacao;

	@Basic(optional = false)
	@Column(name = "ind_captacao_superficial")
	private Boolean indSuperficial;

	@Basic(optional = false)
	@Column(name = "ind_captacao_subterraneo")
	private Boolean indSubterraneo;

	@Basic(optional = false)
	@Column(name = "ind_lancamento_concessionaria")
	private Boolean indLancamentoConcessionaria;

	@Basic(optional = false)
	@Column(name = "ind_lancamento_manancial")
	private Boolean indLancamentoManancial;

	@Basic(optional = false)
	@Column(name = "ind_intervencao")
	private Boolean indIntervencao;

	@Size(max = 500)
	@Column(name = "dsc_observacao_alteracao_shape")
	private String dscObservacaoAlteracaoShape;

	@Basic(optional = true)
	@Column(name = "ind_tac")
	private Boolean indTac;

	@Basic(optional = true)
	@Column(name = "ind_infracao_supressao")
	private Boolean indInfracaoSupressao;

	@Basic(optional = true)
	@Column(name = "ind_rppn")
	private Boolean indRppn;

	@Basic(optional = true)
	@Column(name = "ind_crf")
	private Boolean indCrf;

	@Basic(optional = true)
	@Column(name = "ind_alteracao_tamanho")
	private Boolean indAlteracaoTamanho;

	@Basic(optional = true)
	@Column(name = "val_alteracao_tamanho")
	private Double valAlteracaoTamanho;

	@Basic(optional = true)
	@Column(name = "ind_prad")
	private Boolean indPrad;

	@Basic(optional = true)
	@Column(name = "ind_deseja_aderir_pra")
	private Boolean indDesejaAderirPra;

	@Basic
	@Size(min = 0, max = 9)
	@Column(name = "cod_sipra", length = 9)
	private String codSipra;

	@Basic(optional = false)
	@Column(name = "val_fracao_ideal")
	private Integer valFracaoIdeal;

	@Column(name = "dtc_criacao_assentamento")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcCriacaoAssentamento;

	@Basic(optional = false)
	@Column(name = "ind_consolidada")
	private Boolean indAreaRuralConsolidada;

	@Basic(optional = true)
	@Column(name = "ind_contrato_convenio")
	private Boolean indContratoConvenio;

	@Column(name = "dtc_primeira_finalizacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dtcPrimeiraFinalizacao;

	@Basic(optional = true)
	@Column(name = "ind_imovel_rural_cda_editado")
	private Boolean indImovelRuralCdaEditado;

	@Basic(optional = true)
	@Column(name = "ind_existia_remanescente_vegetacao_nativa")
	private Boolean indExistiaRemanescenteVegetacaoNativa;

	@Basic(optional = false)
	@Column(name = "ind_suspensao")
	private Boolean indSuspensao;
	
	@Basic(optional = false)
	@Column(name = "ind_bloqueio_limite")
	private Boolean indBloqueioLimite;

	@Basic(optional = false)
	@Column(name = "ind_restaurado")
	private Boolean indRestaurado;
	
	@Column(name = "num_nirf")
	private String numNirf;

	@Size(max = 1000)
	@Column(name = "dsc_termo_autodeclaracao")
	private String dscTermoAutodeclaracao;

	@Transient
	private boolean cedeAreaParaCompensacaoRl;

	/*************************
	 * /* * //XXX: OBJETOS COMPLEXOS * /* * /
	 *************************/

	@JoinColumn(name = "ide_documento_procuracao", referencedColumnName = "ide_documento_imovel_rural")
	@OneToOne(cascade = CascadeType.ALL)
	private DocumentoImovelRural ideDocumentoProcuracao;

	@JoinColumn(name = "ide_contrato_convenio", referencedColumnName = "ide_contrato_convenio_cefir", nullable = true)
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private ContratoConvenioCefir ideContratoConvenioCefir;

	@JoinColumn(name = "ide_localizacao_geografica", referencedColumnName = "ide_localizacao_geografica")
	@ManyToOne
	private LocalizacaoGeografica ideLocalizacaoGeografica;

	@JoinColumn(name = "ide_imovel_rural", referencedColumnName = "ide_imovel", insertable = false, updatable = false)
	@OneToOne(optional = false)
	private Imovel imovel;

	@JoinColumn(name = "ide_requerente_cadastro", referencedColumnName = "ide_pessoa")
	@OneToOne
	private Pessoa ideRequerenteCadastro;

	@JoinColumn(name = "ide_municipio_cartorio", referencedColumnName = "ide_municipio", nullable = true)
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private Municipio ideMunicipioCartorio;

	@JoinColumn(name = "ide_tipo_cadastro_imovel_rural", referencedColumnName = "ide_tipo_cadastro_imovel_rural")
	@OneToOne
	private TipoCadastroImovelRural ideTipoCadastroImovelRural;

	@JoinColumn(name = "ide_fase_assentamento_imovel_rural", referencedColumnName = "ide_fase_assentamento_imovel_rural")
	@OneToOne
	private FaseAssentamentoImovelRural ideFaseAssentamentoImovelRural;

	@JoinColumn(name = "ide_localizacao_geografica_lote", referencedColumnName = "ide_localizacao_geografica")
	@ManyToOne
	private LocalizacaoGeografica ideLocalizacaoGeograficaLote;

	@OneToOne(cascade = CascadeType.REMOVE, mappedBy = "ideImovelRural")
	private VegetacaoNativa vegetacaoNativa;

	@OneToOne(cascade = CascadeType.REMOVE, mappedBy = "ideImovelRural")
	private ReservaLegal reservaLegal;

	@OneToOne(cascade = CascadeType.REMOVE, mappedBy = "ideImovelRural")
	private OutrosPassivosAmbientais outrosPassivosAmbientais;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "ideImovelRural")
	private SupressaoVegetacao supressaoVegetacao;

	@OneToOne(cascade = CascadeType.REMOVE, mappedBy = "ideImovelRural")
	private ImovelRuralSicar imovelRuralSicar;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "ideImovelRural", fetch = FetchType.LAZY)
	private DocumentoImovelRuralPosse documentoImovelRuralPosse;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "ideImovelRural")
	private ImovelRuralTac imovelRuralTac;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "ideImovelRural")
	private ImovelRuralPrad imovelRuralPrad;

	@OneToOne(mappedBy = "ideImovelRural")
	private ImovelRuralRppn ideImovelRuralRppn;

	@OneToOne(mappedBy = "ideImovelRural")
	private AreaRuralConsolidada ideAreaRuralConsolidada;

	@Transient
	private GeoJsonSicar geoJsonSicar;

	@Transient
	private GeoJsonSicar geoJsonSicarAppTotal;

	@JoinColumn(name = "ide_localizacao_geografica_pct", referencedColumnName = "ide_localizacao_geografica")
	@ManyToOne(optional = true)
	private LocalizacaoGeografica ideLocalizacaoGeograficaPct;

	@JoinColumn(name = "ide_localizacao_geografica_pct_limite_territorio", referencedColumnName = "ide_localizacao_geografica")
	@ManyToOne(optional = true)
	private LocalizacaoGeografica ideLocalizacaoGeograficaPctLimiteTerritorio;

	@OneToOne(mappedBy = "ideImovelRural", fetch = FetchType.LAZY)
	private PctImovelRural idePctImovelRural;

	/********************
	 * /* * //XXX: COLLECTIONS * /* * /
	 ********************/

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "ideImovelRural", fetch = FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	private Collection<ResponsavelImovelRural> responsavelImovelRuralCollection;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "ideImovelRural")
	private Collection<App> appCollection;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "ideImovelRural")
	private Collection<AreaProdutiva> areaProdutivaCollection;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideImovelRural")
	private Collection<ProcessoTramiteImovelRural> processoTramiteImovelRuralCollection;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "ideImovelRural")
	private Collection<CadastroAtividadeNaoSujeitaLicImovel> cadastroAtividadeNaoSujeitaLicImovelCollection;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "ideImovelRural", fetch = FetchType.LAZY)
	private Collection<AssentadoIncraImovelRural> assentadoIncraImovelRuralCollection;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "ideImovelRural")
	@LazyCollection(LazyCollectionOption.FALSE)
	private Collection<ImovelRuralUsoAgua> imovelRuralUsoAguaCollection;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ideImovelRural", fetch = FetchType.LAZY)
	private Collection<ImovelRuralMudancaStatusJustificativa> imovelRuralMudancaStatusJustificativaCollection;

	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "ideImovelRural")
	private Collection<ImovelRuralRevalidacao> imovelRuralRevalidacaoCollection;

	@Transient
	private Collection<ProcessoTramiteImovelRural> collProcessotramiteImovelRuralExclusao;

	@Transient
	private GeoJsonSicar geoJsonSicarPctLimiteTerritorio;

	@Transient
	private GeoJsonSicar geoJsonSicarPct;

	/********************
	 * /* * //XXX: CONSTRUTORES * /* * /
	 ********************/

	public ImovelRural() {
		ideTipoCadastroImovelRural = new TipoCadastroImovelRural(1);
		processoTramiteImovelRuralCollection = new ArrayList<ProcessoTramiteImovelRural>();
	}

	public ImovelRural(Integer ideImovelRural) {
		this.ideImovelRural = ideImovelRural;
		this.ideTipoCadastroImovelRural = new TipoCadastroImovelRural(1);
	}

	public ImovelRural(Integer ideImovelRural, String numItr, String desDenominacao, Double valArea, String desCartorio,
			String desComarca, String desLivro, String numFolha, Boolean indReservaLegal, Boolean indPassivoAmbiental,
			Boolean indApp, Boolean indAreaProdutiva, Boolean indVegetacaoNativa, Boolean indAgrotoxico,
			Boolean indOutorga, Boolean indSupressaoVegetacao, Boolean indBloqueioLimite,Boolean indRestaurado) {

		this.ideImovelRural = ideImovelRural;
		this.numItr = numItr;
		this.desDenominacao = desDenominacao;
		this.valArea = valArea;
		this.desCartorio = desCartorio;
		this.desComarca = desComarca;
		this.desLivro = desLivro;
		this.numFolha = numFolha;
		this.indReservaLegal = indReservaLegal;
		this.indPassivoAmbiental = indPassivoAmbiental;
		this.indApp = indApp;
		this.indAreaProdutiva = indAreaProdutiva;
		this.indVegetacaoNativa = indVegetacaoNativa;
		this.indAgrotoxico = indAgrotoxico;
		this.indOutorga = indOutorga;
		this.indSupressaoVegetacao = indSupressaoVegetacao;
		this.indBloqueioLimite = indBloqueioLimite;
		this.indRestaurado = indRestaurado;
		this.ideTipoCadastroImovelRural = new TipoCadastroImovelRural(1);
	}

	/*********************
	 * /* * //XXX: MÉTODOS ÚTEIS * /* * /
	 *********************/

	public Boolean getDesDenominacaoIsNotNull() {
		if (!Util.isNullOuVazio(desDenominacao))
			return true;
		else
			return false;
	}

	public String getDesComarca() {
		if (Util.isNullOuVazio(ideMunicipioCartorio))
			return desComarca;
		else
			return ideMunicipioCartorio.getNomMunicipio();
	}

	public Boolean getStsCertificado() {
		if (Util.isNullOuVazio(stsCertificado) || Util.isNullOuVazio(statusCadastro)
				|| Util.isNullOuVazio(prazoValidade)) {
			return false;
		}

		if (!isCadastrado()) {
			return false;
		}

		return stsCertificado;
	}

	public boolean isRegistroIncompleto() {
		return StatusCadastroImovelRuralEnum.REGISTRO_INCOMPLETO.getId().equals(statusCadastro);
	}

	public boolean isRegistrado() {
		return StatusCadastroImovelRuralEnum.REGISTRADO.getId().equals(statusCadastro);
	}

	public boolean isCadastroIncompleto() {
		return StatusCadastroImovelRuralEnum.CADASTRO_INCOMPLETO.getId().equals(statusCadastro);
	}

	public boolean isCadastrado() {
		return StatusCadastroImovelRuralEnum.CADASTRADO.getId().equals(statusCadastro);
	}

	public boolean isTermoCompromisso() {
		return !Util.isNullOuVazio(stsCertificado) && !stsCertificado && isCadastrado()
				&& !Util.isNullOuVazio(prazoValidade);
	}

	public boolean isAvisoBndes() {
		return isImovelBNDES() && !Util.isNullOuVazio(stsCertificado) && !stsCertificado && isRegistrado();
	}

	@XmlTransient
	public Boolean getTemProcessoTramiteImovelRuralCollection() {
		try {
			if (Util.isNullOuVazio(processoTramiteImovelRuralCollection))
				return false;
			else
				return true;
		} catch (Exception exception) {
			// sem printStackTrace() de propósito! Micael Coutinho
			return false;
		}
	}

	@Transient
	public boolean isImovelCDA() {
		return ideTipoCadastroImovelRural != null && ideTipoCadastroImovelRural.getIdeTipoCadastroImovelRural() != null
				&& ideTipoCadastroImovelRural.getIdeTipoCadastroImovelRural()
						.equals(TipoCadastroImovelRuralEnum.IMOVEL_RURAL_CDA.getTipo());
	}

	@Transient
	public boolean isImovelBNDES() {
		return ideTipoCadastroImovelRural != null && ideTipoCadastroImovelRural.getIdeTipoCadastroImovelRural() != null
				&& ideTipoCadastroImovelRural.getIdeTipoCadastroImovelRural()
						.equals(TipoCadastroImovelRuralEnum.IMOVEL_RURAL_PROJETO_BNDES.getTipo());
	}

	@Transient
	public boolean isImovelINCRA() {
		return ideTipoCadastroImovelRural != null && ideTipoCadastroImovelRural.getIdeTipoCadastroImovelRural() != null
				&& ideTipoCadastroImovelRural.getIdeTipoCadastroImovelRural()
						.equals(TipoCadastroImovelRuralEnum.ASSENTAMENTO_INCRA.getTipo());
	}

	@Transient
	public boolean isImovelPCT() {
		return ideTipoCadastroImovelRural != null && ideTipoCadastroImovelRural.getIdeTipoCadastroImovelRural() != null
				&& ideTipoCadastroImovelRural.getIdeTipoCadastroImovelRural()
						.equals(TipoCadastroImovelRuralEnum.PCT.getTipo());
	}

	public Boolean temAreaMaiorQueMil() {
		if ((!Util.isNullOuVazio(valArea)) && valArea > 1000.0)
			return true;
		else
			return false;
	}

	public String getRegistroMatricula() {
		if (Util.isNullOuVazio(numMatricula))
			return numRegistro;
		else
			return numMatricula;
	}

	public Boolean getIsFinalizado() {
		if (isCadastrado() || isRegistrado()) {
			return true;
		}

		return false;
	}
	
	public Boolean getrenderedDesbloquear() {
		if (!Util.isNullOuVazio(this.indBloqueioLimite)) {
			return indBloqueioLimite;
		}
		return indBloqueioLimite;
	}
	

	public boolean isMenorQueQuatroModulosFiscais() {

		if ((qtdModuloFiscal != null && qtdModuloFiscal <= 4) || (!Util.isNullOuVazio(ideTipoCadastroImovelRural)
				&& ideTipoCadastroImovelRural.getIdeTipoCadastroImovelRural()
						.equals(TipoCadastroImovelRuralEnum.ASSENTAMENTO_INCRA.getTipo()))) {

			return true;
		}

		return false;
	}

	public boolean isObrigatorioRl() {

		if (isImovelPCT()) {
			return false;
		} else if ((isImovelCDA() || isImovelBNDES()) && isMenorQueQuatroModulosFiscais()
				&& (indExistiaRemanescenteVegetacaoNativa == null || !indExistiaRemanescenteVegetacaoNativa)) {
			return false;
		} else if (isMenorQueQuatroModulosFiscais()
				&& (indDesejaCompletarInformacoes == null || !indDesejaCompletarInformacoes)) {
			return false;
		}

		return true;
	}
	

	public boolean isFaseAssentamentoImovelRuralImplantado() {
		if (ideFaseAssentamentoImovelRural == null
				|| ideFaseAssentamentoImovelRural.getIdeFaseAssentamentoImovelRural() == null) {
			return false;
		}

		if (ideFaseAssentamentoImovelRural
				.getIdeFaseAssentamentoImovelRural() != FaseAssentamentoImovelRuralEnum.IMPLANTADO.getId()) {
			return false;
		}

		return true;
	}

	public Boolean getIndContratoConvenio() {
		return indContratoConvenio == null ? false : indContratoConvenio;
	}

	public boolean houveAlteracaoProprietario() {
		if (!Util.isNullOuVazio(imovelRuralMudancaStatusJustificativaCollection)) {

			for (ImovelRuralMudancaStatusJustificativa imovelRuralMudancaStatusJustificativa : imovelRuralMudancaStatusJustificativaCollection) {
				if (imovelRuralMudancaStatusJustificativa.isIndAlterarProprietario()) {
					return true;
				}
			}
		}

		return false;
	}

	public Boolean getReservaLegalAprovadaOuAverbada() {

		if (!Util.isNullOuVazio(reservaLegal) && !Util.isNullOuVazio(reservaLegal.getIdeStatus())
				&& !Util.isNullOuVazio(reservaLegal.getIdeStatus().getIdeStatusReservaLegal())) {

			if (reservaLegal.getIdeStatus().getIdeStatusReservaLegal() == StatusReservaLegalEnum.APROVADA.getId()
					|| reservaLegal.getIdeStatus().getIdeStatusReservaLegal() == StatusReservaLegalEnum.AVERBADA
							.getId()) {

				return true;
			}
		}

		return false;
	}

	public boolean tipologiaGrupoJaFoiCadastrada(Tipologia tipologia) {

		if (!Util.isNullOuVazio(areaProdutivaCollection)) {

			for (AreaProdutiva ap : areaProdutivaCollection) {

				if (ap.getIdeTipologia().getIdeTipologia().equals(tipologia.getIdeTipologia())) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean tipologiaSubgrupoJaFoiCadastrada(Tipologia tipologia) {

		if (!Util.isNullOuVazio(areaProdutivaCollection)) {

			for (AreaProdutiva ap : areaProdutivaCollection) {

				if (!Util.isNullOuVazio(ap.getIdeTipologiaSubgrupo()) && !ap.getIndExcluido()
						&& ap.getIdeTipologiaSubgrupo().getIdeTipologia().equals(tipologia.getIdeTipologia())) {

					return true;
				}
			}
		}

		return false;
	}

	public boolean tipologiaAtividadeJaFoiCadastrada(
			Collection<AreaProdutivaTipologiaAtividade> areaProdutivaTipologiaAtividade) {

		for (AreaProdutivaTipologiaAtividade apta : areaProdutivaTipologiaAtividade) {

			for (AreaProdutiva ap : areaProdutivaCollection) {

				if (!ap.getIndExcluido()) {

					for (AreaProdutivaTipologiaAtividade aptaImovel : ap
							.getAreaProdutivaTipologiaAtividadeCollection()) {

						if (apta.getIdeTipologiaAtividade().equals(aptaImovel.getIdeTipologiaAtividade())) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	public boolean possuiReservaLegal() {
		return reservaLegal != null;
	}

	public boolean possuiNumeroCar() {
		return imovelRuralSicar != null && !Util.isNullOuVazio(imovelRuralSicar.getNumSicar());
	}

	public ImovelRural clone() throws CloneNotSupportedException {
		return (ImovelRural) super.clone();
	}

	/*********************
	 * /* * //XXX: GETS AND SETS * /* * /
	 *********************/

	public ImovelRural(String numSicar) {
		imovelRuralSicar = new ImovelRuralSicar(numSicar);
	}

	public Integer getIdeImovelRural() {
		return ideImovelRural;
	}

	public void setIdeImovelRural(Integer ideImovelRural) {
		this.ideImovelRural = ideImovelRural;
	}

	public String getNumItr() {
		return numItr;
	}

	public void setNumItr(String numItr) {
		this.numItr = numItr;
	}

	public String getDesDenominacao() {
		return desDenominacao;
	}

	public void setDesDenominacao(String desDenominacao) {
		this.desDenominacao = desDenominacao;
	}

	public Double getValArea() {
		return valArea;
	}

	public void setValArea(Double valArea) {
		this.valArea = valArea;
	}

	public String getDesCartorio() {
		return desCartorio;
	}

	public void setDesCartorio(String desCartorio) {
		this.desCartorio = desCartorio;
	}

	public void setDesComarca(String desComarca) {
		this.desComarca = desComarca;
	}

	public String getDesLivro() {
		return desLivro;
	}

	public void setDesLivro(String desLivro) {
		this.desLivro = desLivro;
	}

	public String getNumFolha() {
		return numFolha;
	}

	public void setNumFolha(String numFolha) {
		this.numFolha = numFolha;
	}

	public Boolean getIndReservaLegal() {
		return indReservaLegal;
	}

	public void setIndReservaLegal(Boolean indReservaLegal) {
		this.indReservaLegal = indReservaLegal;
	}

	public String getNumMatricula() {
		return numMatricula;
	}

	public void setNumMatricula(String numMatricula) {
		this.numMatricula = numMatricula;
	}

	public String getNumRegistro() {
		return numRegistro;
	}

	public void setNumRegistro(String numRegistro) {
		this.numRegistro = numRegistro;
	}

	public String getNumCcir() {
		return numCcir;
	}

	public void setNumCcir(String numCcir) {
		this.numCcir = numCcir;
	}

	public String getDscTermoAutodeclaracao() {
		return dscTermoAutodeclaracao;
	}

	public void setDscTermoAutodeclaracao(String dscTermoAutodeclaracao) {
		this.dscTermoAutodeclaracao = dscTermoAutodeclaracao;
	}

	public Boolean getIndPassivoAmbiental() {
		return indPassivoAmbiental;
	}

	public void setIndPassivoAmbiental(Boolean indPassivoAmbiental) {
		this.indPassivoAmbiental = indPassivoAmbiental;
	}

	public Boolean getIndParaAderido() {
		return indParaAderido;
	}

	public void setIndParaAderido(Boolean indParaAderido) {
		this.indParaAderido = indParaAderido;
	}

	public Boolean getIndAderirPara() {
		return indAderirPara;
	}

	public void setIndAderirPara(Boolean indAderirPara) {
		this.indAderirPara = indAderirPara;
	}

	public String getNumProcessoPara() {
		return numProcessoPara;
	}

	public void setNumProcessoPara(String numProcessoPara) {
		this.numProcessoPara = numProcessoPara;
	}

	public Boolean getIndApp() {
		return indApp;
	}

	public void setIndApp(Boolean indApp) {
		this.indApp = indApp;
	}

	public Boolean getIndAreaProdutiva() {
		return indAreaProdutiva;
	}

	public void setIndAreaProdutiva(Boolean indAreaProdutiva) {
		this.indAreaProdutiva = indAreaProdutiva;
	}

	public Boolean getIndVegetacaoNativa() {
		return indVegetacaoNativa;
	}

	public void setIndVegetacaoNativa(Boolean indVegetacaoNativa) {
		this.indVegetacaoNativa = indVegetacaoNativa;
	}

	public Boolean getIndAgrotoxico() {
		return indAgrotoxico;
	}

	public void setIndAgrotoxico(Boolean indAgrotoxico) {
		this.indAgrotoxico = indAgrotoxico;
	}

	public void setStsCertificado(Boolean stsCertificado) {
		this.stsCertificado = stsCertificado;
	}

	public Double getQtdModuloFiscal() {
		return qtdModuloFiscal;
	}

	public void setQtdModuloFiscal(Double qtdModuloFiscal) {
		this.qtdModuloFiscal = qtdModuloFiscal;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public Imovel getImovel() {
		return imovel;
	}

	public void setImovel(Imovel imovel) {
		this.imovel = imovel;
	}

	@XmlTransient
	public Collection<ResponsavelImovelRural> getResponsavelImovelRuralCollection() {
		return responsavelImovelRuralCollection;
	}

	public void setResponsavelImovelRuralCollection(
			Collection<ResponsavelImovelRural> responsavelImovelRuralCollection) {
		this.responsavelImovelRuralCollection = responsavelImovelRuralCollection;
	}

	@XmlTransient
	public VegetacaoNativa getVegetacaoNativa() {
		return vegetacaoNativa;
	}

	public void setVegetacaoNativa(VegetacaoNativa vegetacaoNativa) {
		this.vegetacaoNativa = vegetacaoNativa;
	}

	@XmlTransient
	public ReservaLegal getReservaLegal() {
		return reservaLegal;
	}

	public void setReservaLegal(ReservaLegal reservaLegal) {
		this.reservaLegal = reservaLegal;
	}

	public OutrosPassivosAmbientais getOutrosPassivosAmbientais() {
		return outrosPassivosAmbientais;
	}

	public void setOutrosPassivosAmbientais(OutrosPassivosAmbientais outrosPassivosAmbientais) {
		this.outrosPassivosAmbientais = outrosPassivosAmbientais;
	}

	@XmlTransient
	public Collection<App> getAppCollection() {
		return appCollection;
	}

	public void setAppCollection(Collection<App> appCollection) {
		this.appCollection = appCollection;
	}

	@XmlTransient
	public Collection<AreaProdutiva> getAreaProdutivaCollection() {
		return areaProdutivaCollection;
	}

	public void setAreaProdutivaCollection(Collection<AreaProdutiva> areaProdutivaCollection) {
		this.areaProdutivaCollection = areaProdutivaCollection;
	}

	public Integer getStatusCadastro() {
		return statusCadastro;
	}

	public void setStatusCadastro(Integer statusCadastro) {
		this.statusCadastro = statusCadastro;
	}

	@XmlTransient
	public Collection<ProcessoTramiteImovelRural> getProcessoTramiteImovelRuralCollection() {
		return processoTramiteImovelRuralCollection;
	}

	public void setProcessoTramiteImovelRuralCollection(
			Collection<ProcessoTramiteImovelRural> processoTramiteImovelRuralCollection) {
		this.processoTramiteImovelRuralCollection = processoTramiteImovelRuralCollection;
	}

	public Collection<ProcessoTramiteImovelRural> getCollProcessotramiteImovelRuralExclusao() {
		return collProcessotramiteImovelRuralExclusao;
	}

	public void setCollProcessotramiteImovelRuralExclusao(
			Collection<ProcessoTramiteImovelRural> collProcessotramiteImovelRuralExclusao) {
		this.collProcessotramiteImovelRuralExclusao = collProcessotramiteImovelRuralExclusao;
	}

	public Boolean isIndOutorga() {
		return indOutorga;
	}

	public void setIndOutorga(Boolean indOutorga) {
		this.indOutorga = indOutorga;
	}

	public Boolean getIndSupressaoVegetacao() {
		return indSupressaoVegetacao;
	}

	public void setIndSupressaoVegetacao(Boolean indSupressaoVegetacao) {
		this.indSupressaoVegetacao = indSupressaoVegetacao;
	}

	public SupressaoVegetacao getSupressaoVegetacao() {
		return supressaoVegetacao;
	}

	public void setSupressaoVegetacao(SupressaoVegetacao supressaoVegetacao) {
		this.supressaoVegetacao = supressaoVegetacao;
	}

	public Date getPrazoValidade() {
		return prazoValidade;
	}

	public void setPrazoValidade(Date prazoValidade) {
		this.prazoValidade = prazoValidade;
	}

	public Boolean getIndPrecisaOutorga() {
		return indPrecisaOutorga;
	}

	public void setIndPrecisaOutorga(Boolean indPrecisaOutorga) {
		this.indPrecisaOutorga = indPrecisaOutorga;
	}

	public Boolean getIndPrecisaLicenca() {
		return indPrecisaLicenca;
	}

	public void setIndPrecisaLicenca(Boolean indPrecisaLicenca) {
		this.indPrecisaLicenca = indPrecisaLicenca;
	}

	public Boolean getIndTemPrad() {
		return indTemPrad;
	}

	public void setIndTemPrad(Boolean indTemPrad) {
		this.indTemPrad = indTemPrad;
	}

	public Boolean getIndOutrosPassivos() {
		return indOutrosPassivos;
	}

	public void setIndOutrosPassivos(Boolean indOutrosPassivos) {
		this.indOutrosPassivos = indOutrosPassivos;
	}

	public DocumentoImovelRural getIdeDocumentoProcuracao() {
		return ideDocumentoProcuracao;
	}

	public void setIdeDocumentoProcuracao(DocumentoImovelRural ideDocumentoProcuracao) {
		this.ideDocumentoProcuracao = ideDocumentoProcuracao;
	}

	public Pessoa getIdeRequerenteCadastro() {
		return ideRequerenteCadastro;
	}

	public void setIdeRequerenteCadastro(Pessoa ideRequerenteCadastro) {
		this.ideRequerenteCadastro = ideRequerenteCadastro;
	}

	public Boolean getIndConcessionaria() {
		return indConcessionaria;
	}

	public void setIndConcessionaria(Boolean indConcessionaria) {
		this.indConcessionaria = indConcessionaria;
	}

	public Boolean getIndPrecipitacao() {
		return indPrecipitacao;
	}

	public void setIndPrecipitacao(Boolean indPrecipitacao) {
		this.indPrecipitacao = indPrecipitacao;
	}

	public Boolean getIndSuperficial() {
		return indSuperficial;
	}

	public void setIndSuperficial(Boolean indSuperficial) {
		this.indSuperficial = indSuperficial;
	}

	public Boolean getIndSubterraneo() {
		return indSubterraneo;
	}

	public void setIndSubterraneo(Boolean indSubterraneo) {
		this.indSubterraneo = indSubterraneo;
	}

	public Boolean getIndIntervencao() {
		return indIntervencao;
	}

	public void setIndIntervencao(Boolean indIntervencao) {
		this.indIntervencao = indIntervencao;
	}

	public Boolean getIndLancamentoConcessionaria() {
		return indLancamentoConcessionaria;
	}

	public void setIndLancamentoConcessionaria(Boolean indLancamentoConcessionaria) {
		this.indLancamentoConcessionaria = indLancamentoConcessionaria;
	}

	public Boolean getIndLancamentoManancial() {
		return indLancamentoManancial;
	}

	public void setIndLancamentoManancial(Boolean indLancamentoManancial) {
		this.indLancamentoManancial = indLancamentoManancial;
	}

	public Collection<ImovelRuralUsoAgua> getImovelRuralUsoAguaCollection() {
		return imovelRuralUsoAguaCollection;
	}

	public void setImovelRuralUsoAguaCollection(Collection<ImovelRuralUsoAgua> imovelRuralUsoAguaCollection) {
		this.imovelRuralUsoAguaCollection = imovelRuralUsoAguaCollection;
	}

	public ImovelRuralSicar getImovelRuralSicar() {
		return imovelRuralSicar;
	}

	public void setImovelRuralSicar(ImovelRuralSicar imovelRuralSicar) {
		this.imovelRuralSicar = imovelRuralSicar;
	}

	public Boolean getIndPermissaoASV() {
		return indPermissaoASV;
	}

	public void setIndPermissaoASV(Boolean indPermissaoASV) {
		this.indPermissaoASV = indPermissaoASV;
	}

	public Boolean getIndOutorga() {
		return indOutorga;
	}

	public Date getDtcFinalizacao() {
		return dtcFinalizacao;
	}

	public void setDtcFinalizacao(Date dtcFinalizacao) {
		this.dtcFinalizacao = dtcFinalizacao;
	}

	public Boolean getIndDesejaCompletarInformacoes() {
		return indDesejaCompletarInformacoes;
	}

	public void setIndDesejaCompletarInformacoes(Boolean indDesejaCompletarInformacoes) {
		this.indDesejaCompletarInformacoes = indDesejaCompletarInformacoes;
	}

	public Collection<AssentadoIncraImovelRural> getAssentadoIncraImovelRuralCollection() {
		return assentadoIncraImovelRuralCollection;
	}

	public void setAssentadoIncraImovelRuralCollection(
			Collection<AssentadoIncraImovelRural> assentadoIncraImovelRuralCollection) {
		this.assentadoIncraImovelRuralCollection = assentadoIncraImovelRuralCollection;
	}

	public String getCodSipra() {
		return codSipra;
	}

	public void setCodSipra(String codSipra) {
		this.codSipra = codSipra;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeograficaLote() {
		return ideLocalizacaoGeograficaLote;
	}

	public void setIdeLocalizacaoGeograficaLote(LocalizacaoGeografica ideLocalizacaoGeograficaLote) {
		this.ideLocalizacaoGeograficaLote = ideLocalizacaoGeograficaLote;
	}

	public TipoCadastroImovelRural getIdeTipoCadastroImovelRural() {
		return ideTipoCadastroImovelRural;
	}

	public void setIdeTipoCadastroImovelRural(TipoCadastroImovelRural ideTipoCadastroImovelRural) {
		this.ideTipoCadastroImovelRural = ideTipoCadastroImovelRural;
	}

	public FaseAssentamentoImovelRural getIdeFaseAssentamentoImovelRural() {
		return ideFaseAssentamentoImovelRural;
	}

	public void setIdeFaseAssentamentoImovelRural(FaseAssentamentoImovelRural ideFaseAssentamentoImovelRural) {
		this.ideFaseAssentamentoImovelRural = ideFaseAssentamentoImovelRural;
	}

	public DocumentoImovelRuralPosse getDocumentoImovelRuralPosse() {
		return documentoImovelRuralPosse;
	}

	public void setDocumentoImovelRuralPosse(DocumentoImovelRuralPosse documentoImovelRuralPosse) {
		this.documentoImovelRuralPosse = documentoImovelRuralPosse;
	}

	public Municipio getIdeMunicipioCartorio() {
		return ideMunicipioCartorio;
	}

	public void setIdeMunicipioCartorio(Municipio ideMunicipioCartorio) {
		this.ideMunicipioCartorio = ideMunicipioCartorio;
	}

	public Boolean getIndTac() {
		return indTac;
	}

	public void setIndTac(Boolean indTac) {
		this.indTac = indTac;
	}

	public Boolean getIndInfracaoSupressao() {
		return indInfracaoSupressao;
	}

	public void setIndInfracaoSupressao(Boolean indInfracaoSupressao) {
		this.indInfracaoSupressao = indInfracaoSupressao;
	}

	public Boolean getIndRppn() {
		return indRppn;
	}

	public void setIndRppn(Boolean indRppn) {
		this.indRppn = indRppn;
	}

	public Boolean getIndCrf() {
		return indCrf;
	}

	public void setIndCrf(Boolean indCrf) {
		this.indCrf = indCrf;
	}

	public Boolean getIndAlteracaoTamanho() {
		return indAlteracaoTamanho;
	}

	public void setIndAlteracaoTamanho(Boolean indAlteracaoTamanho) {
		this.indAlteracaoTamanho = indAlteracaoTamanho;
	}

	public Double getValAlteracaoTamanho() {
		return valAlteracaoTamanho;
	}

	public void setValAlteracaoTamanho(Double valAlteracaoTamanho) {
		this.valAlteracaoTamanho = valAlteracaoTamanho;
	}

	public ImovelRuralTac getImovelRuralTac() {
		return imovelRuralTac;
	}

	public void setImovelRuralTac(ImovelRuralTac imovelRuralTac) {
		this.imovelRuralTac = imovelRuralTac;
	}

	public ImovelRuralPrad getImovelRuralPrad() {
		return this.imovelRuralPrad;
	}

	public void setImovelRuralPrad(ImovelRuralPrad imovelRuralPrad) {
		this.imovelRuralPrad = imovelRuralPrad;
	}

	public Boolean getIndPrad() {
		return indPrad;
	}

	public void setIndPrad(Boolean indPrad) {
		this.indPrad = indPrad;
	}

	public Boolean getIndDesejaAderirPra() {
		return indDesejaAderirPra;
	}

	public void setIndDesejaAderirPra(Boolean indDesejaAderirPra) {
		this.indDesejaAderirPra = indDesejaAderirPra;
	}

	public ImovelRuralRppn getIdeImovelRuralRppn() {
		return ideImovelRuralRppn;
	}

	public void setIdeImovelRuralRppn(ImovelRuralRppn ideImovelRuralRppn) {
		this.ideImovelRuralRppn = ideImovelRuralRppn;
	}

	public Date getDtcCriacaoAssentamento() {
		return dtcCriacaoAssentamento;
	}

	public void setDtcCriacaoAssentamento(Date dtcCriacaoAssentamento) {
		this.dtcCriacaoAssentamento = dtcCriacaoAssentamento;
	}

	public Integer getValFracaoIdeal() {
		return valFracaoIdeal;
	}

	public void setValFracaoIdeal(Integer valFracaoIdeal) {
		this.valFracaoIdeal = valFracaoIdeal;
	}

	public AreaRuralConsolidada getIdeAreaRuralConsolidada() {
		return ideAreaRuralConsolidada;
	}

	public void setIdeAreaRuralConsolidada(AreaRuralConsolidada ideAreaRuralConsolidada) {
		this.ideAreaRuralConsolidada = ideAreaRuralConsolidada;
	}

	public Boolean getIndAreaRuralConsolidada() {
		return indAreaRuralConsolidada;
	}

	public void setIndAreaRuralConsolidada(Boolean indAreaRuralConsolidada) {
		this.indAreaRuralConsolidada = indAreaRuralConsolidada;
	}

	public GeoJsonSicar getGeoJsonSicar() {
		return geoJsonSicar;
	}

	public void setGeoJsonSicar(GeoJsonSicar geoJsonSicar) {
		this.geoJsonSicar = geoJsonSicar;
	}

	public GeoJsonSicar getGeoJsonSicarAppTotal() {
		return geoJsonSicarAppTotal;
	}

	public void setGeoJsonSicarAppTotal(GeoJsonSicar geoJsonSicarAppTotal) {
		this.geoJsonSicarAppTotal = geoJsonSicarAppTotal;
	}

	public ContratoConvenioCefir getIdeContratoConvenioCefir() {
		return ideContratoConvenioCefir;
	}

	public void setIdeContratoConvenioCefir(ContratoConvenioCefir ideContratoConvenioCefir) {
		this.ideContratoConvenioCefir = ideContratoConvenioCefir;
	}

	public void setIndContratoConvenio(Boolean indContratoConvenio) {
		this.indContratoConvenio = indContratoConvenio;
	}

	public Date getDtcPrimeiraFinalizacao() {
		return dtcPrimeiraFinalizacao;
	}

	public void setDtcPrimeiraFinalizacao(Date dtcPrimeiraFinalizacao) {
		this.dtcPrimeiraFinalizacao = dtcPrimeiraFinalizacao;
	}

	public Boolean getIndExistiaRemanescenteVegetacaoNativa() {
		return indExistiaRemanescenteVegetacaoNativa;
	}

	public void setIndExistiaRemanescenteVegetacaoNativa(Boolean indExistiaRemanescenteVegetacaoNativa) {
		this.indExistiaRemanescenteVegetacaoNativa = indExistiaRemanescenteVegetacaoNativa;
	}

	public Collection<ImovelRuralMudancaStatusJustificativa> getImovelRuralMudancaStatusJustificativaCollection() {
		return imovelRuralMudancaStatusJustificativaCollection;
	}

	public void setImovelRuralMudancaStatusJustificativaCollection(
			Collection<ImovelRuralMudancaStatusJustificativa> imovelRuralMudancaStatusJustificativaCollection) {
		this.imovelRuralMudancaStatusJustificativaCollection = imovelRuralMudancaStatusJustificativaCollection;
	}

	public Collection<ImovelRuralRevalidacao> getImovelRuralRevalidacaoCollection() {
		return imovelRuralRevalidacaoCollection;
	}

	public void setImovelRuralRevalidacaoCollection(
			Collection<ImovelRuralRevalidacao> imovelRuralRevalidacaoCollection) {
		this.imovelRuralRevalidacaoCollection = imovelRuralRevalidacaoCollection;
	}

	public boolean isCedeAreaParaCompensacaoRl() {
		return cedeAreaParaCompensacaoRl;
	}

	public void setCedeAreaParaCompensacaoRl(boolean cedeAreaParaCompensacaoRl) {
		this.cedeAreaParaCompensacaoRl = cedeAreaParaCompensacaoRl;
	}

	public Boolean getIndImovelRuralCdaEditado() {
		return indImovelRuralCdaEditado;
	}

	public void setIndImovelRuralCdaEditado(Boolean indImovelRuralCdaEditado) {
		this.indImovelRuralCdaEditado = indImovelRuralCdaEditado;
	}

	public Boolean getIndSuspensao() {
		return indSuspensao;
	}

	public void setIndSuspensao(Boolean indSuspensao) {
		this.indSuspensao = indSuspensao;
	}

	public Boolean getIndBloqueioLimite() {
		return indBloqueioLimite;
	}

	public void setIndBloqueioLimite(Boolean indBloqueioLimite) {
		this.indBloqueioLimite = indBloqueioLimite;
	}

	public Boolean getIndRestaurado() {
		return indRestaurado;
	}

	public void setIndRestaurado(Boolean indRestaurado) {
		this.indRestaurado = indRestaurado;
	}

	public String getNumNirf() {
		return numNirf;
	}

	public void setNumNirf(String numNirf) {
		this.numNirf = numNirf;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeograficaPct() {
		return ideLocalizacaoGeograficaPct;
	}

	public void setIdeLocalizacaoGeograficaPct(LocalizacaoGeografica ideLocalizacaoGeograficaPct) {
		this.ideLocalizacaoGeograficaPct = ideLocalizacaoGeograficaPct;
	}

	public String getDscObservacaoAlteracaoShape() {
		return dscObservacaoAlteracaoShape;
	}

	public void setDscObservacaoAlteracaoShape(String dscObservacaoAlteracaoShape) {
		this.dscObservacaoAlteracaoShape = dscObservacaoAlteracaoShape;
	}

	public LocalizacaoGeografica getIdeLocalizacaoGeograficaPctLimiteTerritorio() {
		return ideLocalizacaoGeograficaPctLimiteTerritorio;
	}

	public void setIdeLocalizacaoGeograficaPctLimiteTerritorio(
			LocalizacaoGeografica ideLocalizacaoGeograficaPctLimiteComunidade) {
		this.ideLocalizacaoGeograficaPctLimiteTerritorio = ideLocalizacaoGeograficaPctLimiteComunidade;
	}

	public PctImovelRural getIdePctImovelRural() {
		return idePctImovelRural;
	}

	public void setIdePctImovelRural(PctImovelRural idePctImovelRural) {
		this.idePctImovelRural = idePctImovelRural;
	}

	public Collection<CadastroAtividadeNaoSujeitaLicImovel> getCadastroAtividadeNaoSujeitaLicImovelCollection() {
		return cadastroAtividadeNaoSujeitaLicImovelCollection;
	}

	public void setCadastroAtividadeNaoSujeitaLicImovelCollection(
			Collection<CadastroAtividadeNaoSujeitaLicImovel> cadastroAtividadeNaoSujeitaLicImovelCollection) {
		this.cadastroAtividadeNaoSujeitaLicImovelCollection = cadastroAtividadeNaoSujeitaLicImovelCollection;
	}

	public String getNumSncr() {
		return numSncr;
	}

	public void setNumSncr(String numSncr) {
		this.numSncr = numSncr;
	}

	public GeoJsonSicar getGeoJsonSicarPct() {
		return geoJsonSicarPct;
	}

	public void setGeoJsonSicarPct(GeoJsonSicar geoJsonSicarPct) {
		this.geoJsonSicarPct = geoJsonSicarPct;
	}

	public GeoJsonSicar getGeoJsonSicarPctLimiteTerritorio() {
		return geoJsonSicarPctLimiteTerritorio;
	}

	public void setGeoJsonSicarPctLimiteTerritorio(GeoJsonSicar geoJsonSicarPctLimiteTerritorio) {
		this.geoJsonSicarPctLimiteTerritorio = geoJsonSicarPctLimiteTerritorio;
	}

}
