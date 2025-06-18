package br.gov.ba.seia.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.html.HtmlSelectOneRadio;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.hibernate.LazyInitializationException;
import org.joda.time.DateTime;
import org.jrimum.bopepo.view.BoletoViewer;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.wizard.Wizard;
import org.primefaces.context.RequestContext;
import org.primefaces.event.DateSelectEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.vividsolutions.jts.geom.Point;

import br.gov.ba.seia.dto.ComprovantePagamentoDTO;
import br.gov.ba.seia.dto.ComprovantePagamentoDaeDTO;
import br.gov.ba.seia.dto.RequerimentoDTO;
import br.gov.ba.seia.dto.RequerimentoUnicoDTO;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.BoletoDaeRequerimento;
import br.gov.ba.seia.entity.BoletoPagamentoRequerimento;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.ClassificacaoSecaoGeometrica;
import br.gov.ba.seia.entity.ComprovantePagamento;
import br.gov.ba.seia.entity.ComprovantePagamentoDae;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.Enquadramento;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.FaseEmpreendimento;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.LacLegislacao;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.ObjetivoLimpezaArea;
import br.gov.ba.seia.entity.ObjetivoRequerimentoLimpezaArea;
import br.gov.ba.seia.entity.Orgao;
import br.gov.ba.seia.entity.ParametroTaxaCertificado;
import br.gov.ba.seia.entity.Pergunta;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.Porte;
import br.gov.ba.seia.entity.PorteTipologia;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoExterno;
import br.gov.ba.seia.entity.ProcessoTramite;
import br.gov.ba.seia.entity.ProcuradorPessoaFisica;
import br.gov.ba.seia.entity.ProcuradorRepresentante;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoImovel;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.entity.RequerimentoTipologia;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.entity.SistemaCoordenada;
import br.gov.ba.seia.entity.StatusRequerimento;
import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.entity.TipoAto;
import br.gov.ba.seia.entity.TipoCaptacao;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.TipoImovelRequerimento;
import br.gov.ba.seia.entity.TipoIntervencao;
import br.gov.ba.seia.entity.TipoPessoaRequerimento;
import br.gov.ba.seia.entity.TipoReceptor;
import br.gov.ba.seia.entity.TipoRequerimento;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TipologiaGrupo;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.entity.UnidadeMedida;
import br.gov.ba.seia.entity.UnidadeMedidaTipologiaGrupo;
import br.gov.ba.seia.enumerator.AreaEnum;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.PerfilEnum;
import br.gov.ba.seia.enumerator.PerguntaEnum;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoCertificadoEnum;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoTelefoneEnum;
import br.gov.ba.seia.facade.AtoDeclaratorioFacade;
import br.gov.ba.seia.facade.BoletoPagamentoRequerimentoServiceFacade;
import br.gov.ba.seia.facade.ComprovantePagamentoRequerimentoServiceFacade;
import br.gov.ba.seia.facade.EquadramentoAtoAmbientalServiceFacade;
import br.gov.ba.seia.facade.ProcessoRequerimentoServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.ImpressoraAtoDeclaratorio;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.AtoAmbientalService;
import br.gov.ba.seia.service.BoletoDaeRequerimentoService;
import br.gov.ba.seia.service.CertificadoService;
import br.gov.ba.seia.service.ClassificacaoSecaoGeometricaService;
import br.gov.ba.seia.service.ComprovantePagamentoDaeService;
import br.gov.ba.seia.service.ComprovantePagamentoService;
import br.gov.ba.seia.service.DatumService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.EnquadramentoService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.ImovelRuralService;
import br.gov.ba.seia.service.ImovelService;
import br.gov.ba.seia.service.LacLegislacaoService;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.PerguntaRequerimentoService;
import br.gov.ba.seia.service.PerguntaService;
import br.gov.ba.seia.service.PorteService;
import br.gov.ba.seia.service.ProcessoExternoService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.RepresentanteLegalService;
import br.gov.ba.seia.service.RequerimentoImovelService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.RequerimentoTipologiaService;
import br.gov.ba.seia.service.RequerimentoUnicoService;
import br.gov.ba.seia.service.StatusRequerimentoService;
import br.gov.ba.seia.service.TelefoneService;
import br.gov.ba.seia.service.TipoAtoService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.util.CertificadoUtil;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.PostgisUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

@Named("requerimentoUnicoController")
@ViewScoped
public class RequerimentoUnicoController extends SeiaControllerAb implements Serializable{
	private static final long serialVersionUID = 1L;

	private Wizard wizard;

	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");
	@EJB
	private RequerimentoUnicoService requerimentoUnicoService;
	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;
	private RequerimentoUnico requerimentoUnico;
	private RequerimentoUnico requerimentoUnicoEnquadramento;
	private RequerimentoUnico requerimentoUnicoPago;
	private Empreendimento empreendimento;
	private Pessoa pessoa;
	private Boolean flagEndereco;
	private DataModel<RequerimentoImovel> requerimentoImovelData;
	private DataModel<RequerimentoTipologia> requerimentoTipologiaData;
	private List<SelectItem> collTipoIntervencaoData;
	private List<SelectItem> collTipoFinalidadeUsoAgua;
	private List<SelectItem> collTipoTipoReceptor;
	private List<SelectItem> collTipoCaptacao;
	private List<SelectItem> collDatum;
	private List<SelectItem> collFaseEmpreendimento;
	private List<ObjetivoRequerimentoLimpezaArea> collObjetivoRequerimentoLimpezaArea;
	private List<ObjetivoLimpezaArea> collObjetivoLimpezaArea;
	private List<SelectItem> collEmpreendimento;
	private List<SelectItem> listRequerimentoTipologia;
	private List<String> listTipoIntervencao;
	private List<String> listTipoFinalidadeUsoAgua;
	private List<String> listTipoTipoReceptor;
	private List<String> listTipoCaptacao;
	private List<String> listObjetivoRequerimentoLimpezaArea;
	// upload
	private List<String> listaArquivo;
	private List<String> listaArquivoDaeCertificado;
	private List<String> listaArquivoDaeVistoria;
	private String caminhoArquivo;
	private String caminhoArquivoDaeCertificado;
	private String caminhoArquivoDaeVistoria;
	private StreamedContent arquivoBaixar;
	private StreamedContent arquivoBaixarDaeCertificado;
	private StreamedContent arquivoBaixarDaeVistoria;
	private LocalizacaoGeografica localizacaoGeograficaSelecionada;
	private String grausLatitudeLoc;
	private String minutosLatitudeLoc;
	private String segundosLatitudeLoc;
	private String grausLongitudeLoc;
	private String minutosLongitudeLoc;
	private String segundosLongitudeLoc;
	private String fracaoGrauLatitudeLoc;
	private String fracaoGrauLongitudeLoc;
	private DadoGeografico verticeLoc;
	private DadoGeografico verticeExclusaoLoc;
	private SistemaCoordenada datum;
	@EJB
	private DatumService serviceDatum;
	private String porteIdentificado;
	private RequerimentoImovel requerimentoImovel;
	// Controle Tela - begin
	private Boolean enderecoContato;
	private Boolean usoPassivelOutorgaEmpreendimento;
	private Boolean processoUsoPassivel;
	private Boolean processoAna;
	private Boolean origemAgua;
	private Boolean intervencaoCorpoHidrico;
	private Boolean captacaoBarramento;
	private Boolean vazaoCaptacao;
	private Boolean producaoVolumetricaMadeira;
	private Boolean outroObjetivoLimpezaArea;
	private Boolean limpezaArea;
	private Boolean processoTramiteInema;
	private Boolean processoTramiteAgua;
	private Boolean utilizaEmpreendimentoAgua;
	private Boolean captacaoSuperficial;
	private Boolean captacaoSubterranea;
	private Boolean imovelRural;
	private Imovel imovel;
	private ObjetivoLimpezaArea objLimpezaAreaSelecionado;
	private Boolean vazaoFinalidadeAgua;
	private Boolean localizacaoGeografica;
	private Boolean autorizacaoPerfuracaoPoco;
	private Boolean regularizarPerfuracaoPoco;
	private Boolean regularizarPerfuradoPoco;
	private Boolean construcaoBarragemCorpoHidrico;
	private Boolean autorizacaoConstrucaoBarragemIntervencao;
	private Boolean autorizacaoConstrucaoBarragemCaptacao;
	private Boolean realizaEmissaoResiduosLiquidos;
	private Boolean barragemCaptacaoContruida;
	private Boolean avisoConstrucaoBarragemEmpreendimentoEspecifico;
	private Boolean suprimentoSustentavel;
	private Boolean vincularFlorestaProducao;
	private Boolean transferenciaCreditoReposicaoFlorestal;
	private Boolean cortarFlorestaProducaoRegistrada;
	private Boolean prazoValidade;
	private Boolean florestaVinculadaReposicaoFlorestal;
	private Boolean numeroRenovacaoLicencaAmbiental;
	private Boolean numeroProcessoAprovacaoPlanomanejoFlorestal;
	private Boolean numeroPortariaAutorizacaoSupressaoVegetacao;
	private Boolean numeroRegistroCorteFlorestalProducao;
	private Boolean autorizacaoCorteFlorestaProducao;
	private Boolean numeroAlteracaoLicencaAmbiental;
	private Boolean numeroLicencaAmbiental;
	private Boolean origemMaterialLenhoso;
	private Boolean numeroPortariaSupressaoVegetacaoNativa;
	private Boolean numeroCertificadoRegistroCorteFlorestaProducao;
	private Boolean numeroPortariaAutorizacaoCorteFlorestaProducao;
	private Boolean visualizaIncluirEmpreendimento;
	private Boolean selectedModoCoordenada;
	private Boolean auxBoolean;
	private Boolean tipologiaPrincipal;
	private Boolean temArquivo = false;
	private Boolean visualizaSelectEmpreendimento = true;
	private boolean calculoAtividadeValido;
	private boolean recalcularPorte;
	private boolean termoDeclaracao;
	private boolean requerimentoUnicoPagoExistente;
	private boolean processoTramiteAtivo;
	private boolean calculoPorteAtivo;
	private Boolean atoFlorestal;
	private Boolean atoFauna;
	// Controle Tela - end
	// Controle PopUp Requerimento Im�vel - Begin
	private String grausLatitude;
	private String minutosLatitude;
	private String segundosLatitude;
	private String grausLongitude;
	private String minutosLongitude;
	private String segundosLongitude;
	private String fracaoGrauLatitude;
	private String fracaoGrauLongitude;
	// Controle PopUp Requerimento Im�vel - End
	private Collection<Empreendimento> empreendimentos;
	private Collection<StatusRequerimento> statusRequerimentos;
	private Collection<RequerimentoUnicoDTO> requerimentos;
	private LazyDataModel<RequerimentoUnicoDTO> requerimentosModel;
	private Date periodoInicio;
	private Date periodoFim;
	private Pessoa requerente;
	private ProcessoTramite processoTramite;
	private String idTipologiaPrincipal;
	// DAE
	private Boolean showEmissaoDae;
	private Boolean comprovanteDae;
	private Boolean showUploadComprovanteDae;
	private Boolean showUploadComprovanteDaeVistoria;
	private Boolean disableVistoria;
	private BoletoDaeRequerimento boletoDaeRequerimeno;
	private UploadedFile fileUploadDaeCertificado;
	private UploadedFile fileUploadDaeVistoria;
	private Boolean temArquivoDaeCertificado = false;
	private Boolean temArquivoDaeVistoria = false;
	private ComprovantePagamentoDae comprovantePagamentoDae;
	private ComprovantePagamentoDae comprovantePagamentoDaeCertificado;
	private ComprovantePagamentoDae comprovantePagamentoDaeVistoria;
	private List<ComprovantePagamentoDaeDTO> comprovantePagamentosDae;
	// Controle de popup de processos em tramite
	private String periodoDoProcesso;
	private String tipoDoProcesso;
	private Boolean showPanelProcessosAntigos;
	private Boolean isLicenca;
	private ProcessoExterno processoExterno;
	private Pessoa pessoaRequerenteBoleto;
	private Boolean rendererPoll;
	private List<Municipio> listaMunicipios;
	private Municipio municipioSelecionado;
	private List<Area> listaUrs;
	private Area urSelecionada;
	private Area urBoleto;
	private String numeroProcessoRequerimento;
	private List<ImovelRural> listaImovelRuralDoEmpreend = new ArrayList<ImovelRural>();
	private Integer numeroDeImoveisDoEmpreendimento = new Integer(0);

	private ImovelRural imovelRuralSelecionado = new ImovelRural();

	@EJB
	private MunicipioService municipioService;
	@EJB
	private ImovelRuralService imovelRuralService;
	@EJB
	private ImovelService imovelService;
	@EJB
	private LacLegislacaoService lacLegislacaoService;
	@EJB
	private ProcessoExternoService processoExternoService;
	@EJB
	private ProcessoService processoService;
	@EJB
	private ComprovantePagamentoDaeService comprovantePagamentoDaeService;
	@EJB
	private TelefoneService telefoneService;
	@EJB
	private ComprovantePagamentoService comprovantePagamentoService;
	@EJB
	private BoletoDaeRequerimentoService boletoDaeRequerimentoService;
	@EJB
	private RequerimentoService requerimentoService;
	@EJB
	private EmpreendimentoService empreendimentoService;
	@EJB
	private StatusRequerimentoService statusRequerimentoService;
	@EJB
	private BoletoPagamentoRequerimentoServiceFacade boletoPagamentoRequerimentoService;
	@EJB
	private GerenciaArquivoService gerenciaArquivoservice;
	@EJB
	private ComprovantePagamentoRequerimentoServiceFacade comprovantePagamentoServiceFacade;
	@EJB
	private AreaService areaService;
	@EJB
	private ProcessoRequerimentoServiceFacade processoRequerimentoServiceFacade;
	@EJB
	private EquadramentoAtoAmbientalServiceFacade enquadramentoAtoAmbientalService;
	@EJB
	private ClassificacaoSecaoGeometricaService serviceClassifSecGeometrica;
	@EJB
	private EnquadramentoService enquadramentoService;
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	@EJB
	private AtoAmbientalService atoAmbientalService;
	@EJB
	private CertificadoService certificadoService;
	@EJB
	private PerguntaService perguntaService;

	@EJB
	private PerguntaRequerimentoService perguntaReqService;

	@EJB
	private RepresentanteLegalService repreLegalService;

	@EJB
	private TipoAtoService tipoAtoService;

	@EJB
	private RequerimentoImovelService requerimentoImovelService;
	
	@EJB
	private RequerimentoTipologiaService requerimentoTipologiaService;

	@EJB
	private PorteService porteService;
	
	@EJB
	private AtoDeclaratorioFacade atoDeclaratorioFacade;
	
	// entidades do filtro
	private Empreendimento empreendimentoRequerimento;
	private StatusRequerimento statusRequerimento;
	private String numeroRequerimento;
	// Outros
	private BoletoPagamentoRequerimento boleto;
	private RequerimentoUnicoDTO requerimentoUnicoDTO;
	private List<ComprovantePagamentoDTO> comprovantePagamentos;
	private UploadedFile fileUpload;
	private ComprovantePagamento comprovante;
	private List<Area> areasRequerimento;
	private Integer areaSelecionada;
	private boolean gerarRelatorioDLA = false;
	private boolean aptoDLA = false;
	private boolean desabilitarTudo = false;
	private boolean editando = false;
	private boolean modoEdicao = false;
	private boolean efetuandoEnquadramento = false;
	private String urlVoltar;
	private Boolean dadosContato;
	private Boolean isAtoOutorga;
	private Boolean isAtoRloRlu; // verifica se está enquadrado no Ato RLO ou RLU

	private boolean existeBoletoPagamento = false;

	// Bindings
	private DataTable datatableRequerimentos;
	private LazyDataModel<RequerimentoUnicoDTO> listaRequerimentosDTOModel;
	// localização geo vazao captacao
	private boolean uplShapefile;
	private boolean pnlVerticesHabilitado;
	private List<SelectItem> itemsClassifSecGeometrica;

	private List<TipoAto> listaTipoAto;
	private List<AtoAmbiental> listaAto;

	private TipoAto tipoAto;
	private AtoAmbiental atoAmbiental;
	Map<String, Object> params;

	private boolean exibeCoordenacoes = false;

	private String emailValidacao;
	private ArrayList<Municipio> municipios;
	private List<Porte> listaPortes;
	private List<Pergunta> listaPerguntas;
	private Integer idImovelMomentaneo;
	private Pergunta perguntaProducaoVolumetricaMadeira;
	private Pergunta perguntaRealizarQueimaControlada;
	private Pergunta perguntaAprovPlanoManejoFlorestal;
	private Pergunta perguntaAprovExecPlanManejFlorest;
	private Pergunta perguntaCortarFlorestProducRegist;
	private Pergunta perguntaAprovLocRelocServFlorest;
	private Pergunta perguntaAprovRelocServidFlorest;
	private Pergunta perguntaAprovFututaRelocServidFlorest;
	private Pergunta perguntaTemp;
	private RequerimentoImovel requerimentoImovelAExcluir;
	private ObjetivoRequerimentoLimpezaArea objRequerimentoLimpAreaAExcluir;
	private PerguntaRequerimento perguntaReqTemp;
	private List<ObjetivoRequerimentoLimpezaArea> listaDeObjRequerimentoLimpAreaAExcluir;
	private boolean btnCancelar = false;
	private LocalizacaoGeografica localizacaoGeograficaASerExcluida;
	private Requerimento requerimento;
	
	@Inject
	private AlteracaoStatusController alteracaoStatusController;
	@Inject
	private CertificadoUtil certificadoUtil;

	// private Boolean existeTheGeom = new Boolean(false);
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		skip = true;
		carregarMunicipios();
		carregarUr();
		carregarPortes();
		FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("pt", "BR"));
		listaRequerimentosDTOModel = (LazyDataModel<RequerimentoUnicoDTO>) getAtributoSession("REQUERIMENTOS");
		pessoa = ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().getPessoa();//Ao modificar essa linha procurar saber os possiveis impactos no sistema
		//se a primeira não carregar pessso esse código, atribui o solicitante do req de imovel ao obj pessoa, isso ocorre quando clica-se no visualisar ou editar imovel rural
		if(Util.isNullOuVazio(this.pessoa)) {
			this.pessoa = ContextoUtil.getContexto().getSolicitanteRequerimento();
		} else {
			ContextoUtil.getContexto().setRequerenteRequerimento(this.pessoa);
		}

		FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap();

		this.localizacaoGeograficaASerExcluida = null;
		this.perguntaDalocalizacaoGeograficaASerExcluida = null;
		this.empreendimento = new Empreendimento();
		final Object temp = ContextoUtil.getContexto().getObject();
		if (temp != null && temp instanceof RequerimentoUnicoDTO) {
			requerimentoUnicoDTO = (RequerimentoUnicoDTO) temp;
			modoEdicao = true;
			ContextoUtil.getContexto().setObject(null);
			requerimentoUnico = ((RequerimentoUnicoDTO) temp).getRequerimentoUnico();
			urlVoltar = ((RequerimentoUnicoDTO) temp).getUrlVoltar();
			pessoa = ((RequerimentoUnicoDTO) temp).getPessoa();
			empreendimento = ((RequerimentoUnicoDTO) temp).getEmpreendimento();
			carregarEditar(requerimentoUnico);

			try {
				this.listTipoIntervencao = requerimentoUnicoService.listarTipoIntervencaoPorRequerimento(requerimentoUnico);
				this.listTipoFinalidadeUsoAgua = requerimentoUnicoService.listarTipoFinalidadeUsoAguaPorRequerimento(requerimentoUnico);
				this.listTipoTipoReceptor = requerimentoUnicoService.listarTipoReceptorPorRequerimento(requerimentoUnico);
				this.listTipoCaptacao = requerimentoUnicoService.listarTipoCaptacaoPorRequerimento(requerimentoUnico);
				this.listObjetivoRequerimentoLimpezaArea = requerimentoUnicoService.listarObjetivoLimpezaAreaPorRequerimento(requerimentoUnico);
			} catch (Exception e1) {
				
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e1);
			}

			if (((RequerimentoUnicoDTO) temp).isVisualizar()) {
				desabilitarTudo = true;
			} else {
				editando = true;
				desabilitarTudo = false;
			}
			if (((RequerimentoUnicoDTO) temp).isEfetuandoEnquadramento()) {
				efetuandoEnquadramento = true;
			} else {
				efetuandoEnquadramento = false;
			}
			if (!Util.isNullOuVazio(requerimentoUnico) && !Util.isNullOuVazio(requerimentoUnico.getIdeLocalizacaoGeografica())) {
				Exception erro = null;
				try {
					localizacaoGeograficaSelecionada = localizacaoGeograficaService.carregar(requerimentoUnico.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				} catch (Exception e) {
					erro =e;
					
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				}finally{
					if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
				}

				datum = this.localizacaoGeograficaSelecionada.getIdeSistemaCoordenada();
				erro = null;
				try {
					this.verificarProcessoAna(requerimentoUnico);
				} catch (Exception e) {
					erro =e;
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				}finally{
					if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
				}
			}
		} else {
			this.desabilitarTudo = Boolean.TRUE;
			requerimentoUnico = new RequerimentoUnico();
			requerimentoUnico.setRequerimento(new Requerimento());
			if (pessoa != null) {
				List<Telefone> listaTel = null;
				Exception erro = null;
				try {
					listaTel = telefoneService.buscarTelefonesPorPessoa(pessoa);
				} catch (Exception e) {
					erro =e;
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				}finally{
					if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
				}
				if (!Util.isNullOuVazio(pessoa.getPessoaFisica()) && !Util.isNullOuVazio(pessoa.getPessoaFisica().getNomPessoa())) {
					requerimentoUnico.getRequerimento().setNomContato(pessoa.getPessoaFisica().getNomPessoa());
					if (!Util.isNullOuVazio(pessoa.getPessoaFisica().getPessoa().getDesEmail())) {
						requerimentoUnico.getRequerimento().setDesEmail(pessoa.getPessoaFisica().getPessoa().getDesEmail());
					}
				} else if (!Util.isNullOuVazio(pessoa.getPessoaJuridica())) {
					requerimentoUnico.getRequerimento().setNomContato(pessoa.getPessoaJuridica().getNomRazaoSocial());
					Integer tipoSolicit = ContextoUtil.getContexto().getTipoSolicitante();
					if(Util.isNullOuVazio(tipoSolicit) || tipoSolicit.equals(1)){
						PessoaJuridica pJuridica = ContextoUtil.getContexto().getSolicitanteRequerimento().getPessoaJuridica();
						if(!Util.isNullOuVazio(pJuridica)){
							List<RepresentanteLegal> listRepLegal;
							try {
								listRepLegal = repreLegalService.listarRepresentanteLegalPorPessoaJuridica(pJuridica);
								for (RepresentanteLegal representanteLegal : listRepLegal) {
									if(!Util.isNullOuVazio(representanteLegal.getIdePessoaFisica().getPessoa().getDesEmail())) {
										requerimentoUnico.getRequerimento().setDesEmail(representanteLegal.getIdePessoaFisica().getPessoa().getDesEmail());
									}
								}
							} catch (Exception e) {
								erro =e;
								Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
							}finally{
								if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
							}
						}
					}else if(tipoSolicit.equals(4) || tipoSolicit.equals(2)){
						if(!Util.isNullOuVazio(ContextoUtil.getContexto().getSolicitanteRequerimento().getPessoaFisica())){
							requerimentoUnico.getRequerimento().setDesEmail(ContextoUtil.getContexto().getSolicitanteRequerimento().getPessoaFisica().getPessoa().getDesEmail());
						}else{
							requerimentoUnico.getRequerimento().setDesEmail(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa().getDesEmail());

						}
					}
				}
				if (!listaTel.isEmpty()) {
					erro = null;
					try {
						if (listaTel.size() > 1) {
							requerimentoUnico.getRequerimento().setNumTelefone(getTelefoneParaRequerimento(listaTel).getNumTelefone());
						} else {
							requerimentoUnico.getRequerimento().setNumTelefone(listaTel.get(0).getNumTelefone());
						}
					} catch (Exception e) {
						erro =e;
						requerimentoUnico.getRequerimento().setNumTelefone(listaTel.get(0).getNumTelefone());
						Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
					}finally{
						if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
					}
				}
			}
			this.localizacaoGeograficaSelecionada = new LocalizacaoGeografica();
			this.localizacaoGeograficaSelecionada.setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
			this.datum = new SistemaCoordenada();
			requerimentoUnico.setIdeFaseEmpreendimento(new FaseEmpreendimento());
			this.listTipoCaptacao = new ArrayList<String>();
			this.listObjetivoRequerimentoLimpezaArea = new ArrayList<String>();
			this.listTipoFinalidadeUsoAgua = new ArrayList<String>();
			this.listTipoIntervencao = new ArrayList<String>();
			this.calculoPorteAtivo = true;
			this.listTipoTipoReceptor = new ArrayList<String>();
			requerimentoUnico.getRequerimento().setProcessoTramiteCollection(new ArrayList<ProcessoTramite>());
			this.imovelRural = new Boolean(false);
			this.verticeLoc = new DadoGeografico();
			this.datum = new SistemaCoordenada();
			this.localizacaoGeograficaSelecionada = new LocalizacaoGeografica();
			urlVoltar = "/paginas/identificar-papel/identificar-papel.xhtml";



		}
		boleto = new BoletoPagamentoRequerimento();
		if (collEmpreendimento == null) {
			this.collEmpreendimento = new ArrayList<SelectItem>();
			this.carregarEmpreendimento();
		}
		if(Util.isNullOuVazio(this.collObjetivoRequerimentoLimpezaArea)) {
			this.collObjetivoRequerimentoLimpezaArea = new ArrayList<ObjetivoRequerimentoLimpezaArea>();
		}
		this.collObjetivoLimpezaArea = new ArrayList<ObjetivoLimpezaArea>();
		this.carregarObjetivoLimpezaArea();
		ContextoUtil.getContexto().setListaObjetReqLimpArea(new ArrayList<ObjetivoRequerimentoLimpezaArea>());
		if(Util.isNullOuVazio(this.collObjetivoRequerimentoLimpezaArea)) {
			carregarObjetivoRequerimentoLimpezaArea();
		}
		this.collTipoCaptacao = new ArrayList<SelectItem>();
		this.carregarTipoCapitacao();
		this.collTipoFinalidadeUsoAgua = new ArrayList<SelectItem>();
		this.carregarTipoFinalidadeUsoAgua();
		this.collTipoIntervencaoData = new ArrayList<SelectItem>();
		this.carregarTipoIntervencao();
		this.collTipoTipoReceptor = new ArrayList<SelectItem>();
		this.carregarTipoReceptor();
		this.collFaseEmpreendimento = new ArrayList<SelectItem>();
		this.carregarFaseEmpreendimento();
		if (requerimentoUnico.getRequerimento().getIdeEnderecoContato() != null) {
			this.enderecoContato = new Boolean(true);
			ContextoUtil.getContexto().setRequerimentoEndereco(requerimentoUnico.getRequerimento().getIdeEnderecoContato());
		} else {
			this.enderecoContato = new Boolean(false);
		}
		requerimentoUnico.getRequerimento().setCollProcessotramiteExclusao(new ArrayList<ProcessoTramite>());
		this.selectedModoCoordenada = false;
		auxBoolean = false;
		this.verticeExclusaoLoc = new DadoGeografico();
		this.selectedModoCoordenada = false;
		auxBoolean = false;
		processoTramite = new ProcessoTramite();
		if (desabilitarTudo && !editando && requerimentoUnico.getIdeRequerimentoUnico() != null) {
			termoDeclaracao = Boolean.TRUE;
		}else{
			termoDeclaracao = Boolean.FALSE;
		}
		showEmissaoDae = Boolean.FALSE;
		disableVistoria = Boolean.FALSE;
		showUploadComprovanteDae = Boolean.FALSE;
		showUploadComprovanteDaeVistoria = Boolean.FALSE;
		boletoDaeRequerimeno = new BoletoDaeRequerimento();
		showPanelProcessosAntigos = Boolean.FALSE;
		periodoDoProcesso = "0";
		isLicenca = Boolean.TRUE;
		tipoDoProcesso = "0";
		processoExterno = new ProcessoExterno();
		//Código que ativa e bloqueia o pollAjax responsável por mostrar a
		//mensagem de confirmação do enquadramento
		if (ContextoUtil.getContexto() != null && ContextoUtil.getContexto().getObject() != null && ContextoUtil.getContexto().getObject() instanceof String) {
			rendererPoll = true;
		} else {
			rendererPoll = false;
		}
		carregarTipoAto();
	}

	private void carregarPerguntas() {
		Exception erro =null;
		try {
			this.listaPerguntas = perguntaService.listarPerguntasAtivasComLocGeo();
			for(Pergunta pergunta : listaPerguntas) {
				pergunta.setListRequerimentoImovel(new ArrayList<RequerimentoImovel>());
			}
			if((!Util.isNullOuVazio(perguntaProducaoVolumetricaMadeira) && !perguntaProducaoVolumetricaMadeira.getLocalizacaoGeograficaDaPerguntaIsNotNull()) || Util.isNullOuVazio(perguntaProducaoVolumetricaMadeira)){
				if(Util.isNullOuVazio(perguntaProducaoVolumetricaMadeira) || Util.isNullOuVazio(perguntaProducaoVolumetricaMadeira.getListObjReqLimpArea())) {
					perguntaProducaoVolumetricaMadeira = getPerguntaByCod(listaPerguntas, PerguntaEnum.PERGUNTA_PRODUCAO_VOLUMETRICA_DE_MADEIRA.getCod());
				}
				perguntaProducaoVolumetricaMadeira.definePerguntaDosRequerimentosDeImovel();
				perguntaProducaoVolumetricaMadeira.setResposta(requerimentoUnico.getIndVolumeMaterial());
				if(Util.isNullOuVazio(ContextoUtil.getContexto().getListaObjetReqLimpArea())) {
					perguntaProducaoVolumetricaMadeira.setListObjReqLimpArea(new ArrayList<ObjetivoRequerimentoLimpezaArea>());
				} else {
					perguntaProducaoVolumetricaMadeira.setListObjReqLimpArea(ContextoUtil.getContexto().getListaObjetReqLimpArea());
				}
			}
			if((!Util.isNullOuVazio(perguntaRealizarQueimaControlada) && !perguntaRealizarQueimaControlada.getLocalizacaoGeograficaDaPerguntaIsNotNull()) || Util.isNullOuVazio(perguntaRealizarQueimaControlada)){
				perguntaRealizarQueimaControlada   = getPerguntaByCod(listaPerguntas, PerguntaEnum.PERGUNTA_QUEIMA_CONTROLADA.getCod());
				perguntaRealizarQueimaControlada.definePerguntaDosRequerimentosDeImovel();
				perguntaRealizarQueimaControlada.setResposta(requerimentoUnico.getIndRealizarQueimaControlada());
			}
			if((!Util.isNullOuVazio(perguntaAprovPlanoManejoFlorestal) && !perguntaAprovPlanoManejoFlorestal.getLocalizacaoGeograficaDaPerguntaIsNotNull()) || Util.isNullOuVazio(perguntaAprovPlanoManejoFlorestal)){
				perguntaAprovPlanoManejoFlorestal  = getPerguntaByCod(listaPerguntas, PerguntaEnum.PERGUNTA_APROV_PLANO_MANEJO_FLOREST.getCod());
				perguntaAprovPlanoManejoFlorestal.definePerguntaDosRequerimentosDeImovel();
				perguntaAprovPlanoManejoFlorestal.setResposta(requerimentoUnico.getIndPlanoManejo());
			}
			if((!Util.isNullOuVazio(perguntaAprovExecPlanManejFlorest) && !perguntaAprovExecPlanManejFlorest.getLocalizacaoGeograficaDaPerguntaIsNotNull()) || Util.isNullOuVazio(perguntaAprovExecPlanManejFlorest)){
				perguntaAprovExecPlanManejFlorest  = getPerguntaByCod(listaPerguntas, PerguntaEnum.PERGUNTA_APROV_EXEC_PLANO_MANEJO_FLOR.getCod());
				perguntaAprovExecPlanManejFlorest.definePerguntaDosRequerimentosDeImovel();
				perguntaAprovExecPlanManejFlorest.setResposta(requerimentoUnico.getIndAprovExecPlanoMnjFlor());
			}
			if((!Util.isNullOuVazio(perguntaCortarFlorestProducRegist) && !perguntaCortarFlorestProducRegist.getLocalizacaoGeograficaDaPerguntaIsNotNull()) || Util.isNullOuVazio(perguntaCortarFlorestProducRegist)){
				perguntaCortarFlorestProducRegist  = getPerguntaByCod(listaPerguntas, PerguntaEnum.PERGUNTA_CORT_FLOR_PROD_LIC_REG.getCod());
				perguntaCortarFlorestProducRegist.definePerguntaDosRequerimentosDeImovel();
				perguntaCortarFlorestProducRegist.setResposta(requerimentoUnico.getIndCortFlorProd());
			}
			if((!Util.isNullOuVazio(perguntaAprovLocRelocServFlorest) && !perguntaAprovLocRelocServFlorest.getLocalizacaoGeograficaDaPerguntaIsNotNull()) || Util.isNullOuVazio(perguntaAprovLocRelocServFlorest)){
				perguntaAprovLocRelocServFlorest   = getPerguntaByCod(listaPerguntas, PerguntaEnum.PERGUNTA_APROV_LOC_RELOC_SERV_FLOREST.getCod());
				perguntaAprovLocRelocServFlorest.definePerguntaDosRequerimentosDeImovel();
				perguntaAprovLocRelocServFlorest.setResposta(requerimentoUnico.getIndServidaoFlorestal());
			}
			if((!Util.isNullOuVazio(perguntaAprovRelocServidFlorest) && !perguntaAprovRelocServidFlorest.getLocalizacaoGeograficaDaPerguntaIsNotNull()) || Util.isNullOuVazio(perguntaAprovRelocServidFlorest)){
				perguntaAprovRelocServidFlorest   = getPerguntaByCod(listaPerguntas, PerguntaEnum.PERGUNTA_APROV_RELOCACAO_SERVD_FLOREST.getCod());
				perguntaAprovRelocServidFlorest.definePerguntaDosRequerimentosDeImovel();
				perguntaAprovRelocServidFlorest.setResposta(requerimentoUnico.getIndRelocServidFlorest());
			}
			if((!Util.isNullOuVazio(perguntaAprovFututaRelocServidFlorest) && !perguntaAprovFututaRelocServidFlorest.getLocalizacaoGeograficaDaPerguntaIsNotNull()) || Util.isNullOuVazio(perguntaAprovFututaRelocServidFlorest)){
				perguntaAprovFututaRelocServidFlorest = getPerguntaByCod(listaPerguntas, PerguntaEnum.PERGUNTA_FUTURA_RELOCACAO_SERVD_FLOREST.getCod());
				perguntaAprovFututaRelocServidFlorest.definePerguntaDosRequerimentosDeImovel();
				perguntaAprovFututaRelocServidFlorest.setResposta(requerimentoUnico.getIndRelocServidFlorest());
			}
		} catch (Exception e) {
			erro =e;
			JsfUtil.addErrorMessage("Não foi possível carregar as perguntas com localização Geográfica");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	private Pergunta getPerguntaByCod(List<Pergunta> lista, String cod){
		for (Pergunta pergunta : lista) {
			if(pergunta.getCodPergunta().equalsIgnoreCase(cod)){
				return pergunta;
			}
		}
		JsfUtil.addErrorMessage("A pergunta não foi encontrada no Banco de Dados");
		System.out.println("RequerimentoUnicoController - A pergunta não foi encontrada no Banco de Dados");
		return null;
	}

	public void atualizarLocalizacaoNaTabela(){
		Pergunta p = new Pergunta();
		LocalizacaoGeografica localizacaoGeograficaSelecionada = ContextoUtil.getContexto().getLocalizacaoSalvaNoReq();
		p = ContextoUtil.getContexto().getPergunta();
		if(Util.isNullOuVazio(localizacaoGeograficaSelecionada)) {
			JsfUtil.addErrorMessage("Localização Geográfica retornou nulo. Por favor, contate o suporte Técnico.");
		}
		if(p.getIdePergunta().equals(perguntaProducaoVolumetricaMadeira.getIdePergunta())){
			if(Util.isNullOuVazio(perguntaProducaoVolumetricaMadeira.getListLocalizacaoGeograficaDaPergunta())) {
				perguntaProducaoVolumetricaMadeira.setListLocalizacaoGeograficaDaPergunta(new ArrayList<LocalizacaoGeografica>());
			}
			if(perguntaProducaoVolumetricaMadeira.getResposta()) {
				perguntaProducaoVolumetricaMadeira.getListLocalizacaoGeograficaDaPergunta().add(localizacaoGeograficaSelecionada);
			}
			if(Util.isLazyInitExcepOuNull(perguntaProducaoVolumetricaMadeira.getPerguntaRequerimentoCollection()) || Util.isNullOuVazio(perguntaProducaoVolumetricaMadeira.getPerguntaRequerimentoCollection())) {
				perguntaProducaoVolumetricaMadeira.setPerguntaRequerimentoCollection(new ArrayList<PerguntaRequerimento>());
			}
			perguntaProducaoVolumetricaMadeira.getPerguntaRequerimentoCollection().add(ContextoUtil.getContexto().getPerguntaReq());
			if(!perguntaProducaoVolumetricaMadeira.getResposta()){
				/*try {
					//perguntaProducaoVolumetricaMadeira.setListObjReqLimpArea(clonaListaObjReqLimpArea(ContextoUtil.getContexto().getListaObjetReqLimpArea()));
				} catch (CloneNotSupportedException e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				}*/
			}
		}else if(p.getIdePergunta().equals(perguntaAprovExecPlanManejFlorest.getIdePergunta())){
			if(Util.isNullOuVazio(perguntaAprovExecPlanManejFlorest.getListLocalizacaoGeograficaDaPergunta())) {
				perguntaAprovExecPlanManejFlorest.setListLocalizacaoGeograficaDaPergunta(new ArrayList<LocalizacaoGeografica>());
			}
			perguntaAprovExecPlanManejFlorest.getListLocalizacaoGeograficaDaPergunta().add(localizacaoGeograficaSelecionada);
			if(Util.isLazyInitExcepOuNull(perguntaAprovExecPlanManejFlorest.getPerguntaRequerimentoCollection()) || Util.isNullOuVazio(perguntaAprovExecPlanManejFlorest.getPerguntaRequerimentoCollection())) {
				perguntaAprovExecPlanManejFlorest.setPerguntaRequerimentoCollection(new ArrayList<PerguntaRequerimento>());
			}
			perguntaAprovExecPlanManejFlorest.getPerguntaRequerimentoCollection().add(ContextoUtil.getContexto().getPerguntaReq());
		}else if(p.getIdePergunta().equals(perguntaAprovLocRelocServFlorest.getIdePergunta())){
			if(Util.isNullOuVazio(perguntaAprovLocRelocServFlorest.getListLocalizacaoGeograficaDaPergunta())) {
				perguntaAprovLocRelocServFlorest.setListLocalizacaoGeograficaDaPergunta(new ArrayList<LocalizacaoGeografica>());
			}
			perguntaAprovLocRelocServFlorest.getListLocalizacaoGeograficaDaPergunta().add(localizacaoGeograficaSelecionada);
			if(Util.isLazyInitExcepOuNull(perguntaAprovLocRelocServFlorest.getPerguntaRequerimentoCollection()) || Util.isNullOuVazio(perguntaAprovLocRelocServFlorest.getPerguntaRequerimentoCollection())) {
				perguntaAprovLocRelocServFlorest.setPerguntaRequerimentoCollection(new ArrayList<PerguntaRequerimento>());
			}
			perguntaAprovLocRelocServFlorest.getPerguntaRequerimentoCollection().add(ContextoUtil.getContexto().getPerguntaReq());
		}else if(p.getIdePergunta().equals(perguntaAprovRelocServidFlorest.getIdePergunta())){
			if(Util.isNullOuVazio(perguntaAprovRelocServidFlorest.getListLocalizacaoGeograficaDaPergunta())) {
				perguntaAprovRelocServidFlorest.setListLocalizacaoGeograficaDaPergunta(new ArrayList<LocalizacaoGeografica>());
			}
			perguntaAprovRelocServidFlorest.getListLocalizacaoGeograficaDaPergunta().add(localizacaoGeograficaSelecionada);
			if(Util.isLazyInitExcepOuNull(perguntaAprovRelocServidFlorest.getPerguntaRequerimentoCollection()) || Util.isNullOuVazio(perguntaAprovRelocServidFlorest.getPerguntaRequerimentoCollection())) {
				perguntaAprovRelocServidFlorest.setPerguntaRequerimentoCollection(new ArrayList<PerguntaRequerimento>());
			}
			perguntaAprovRelocServidFlorest.getPerguntaRequerimentoCollection().add(ContextoUtil.getContexto().getPerguntaReq());
		}else if(p.getIdePergunta().equals(perguntaAprovFututaRelocServidFlorest.getIdePergunta())){
			if(Util.isNullOuVazio(perguntaAprovFututaRelocServidFlorest.getListLocalizacaoGeograficaDaPergunta())) {
				perguntaAprovFututaRelocServidFlorest.setListLocalizacaoGeograficaDaPergunta(new ArrayList<LocalizacaoGeografica>());
			}
			perguntaAprovFututaRelocServidFlorest.getListLocalizacaoGeograficaDaPergunta().add(localizacaoGeograficaSelecionada);
			if(Util.isLazyInitExcepOuNull(perguntaAprovFututaRelocServidFlorest.getPerguntaRequerimentoCollection()) || Util.isNullOuVazio(perguntaAprovFututaRelocServidFlorest.getPerguntaRequerimentoCollection())) {
				perguntaAprovFututaRelocServidFlorest.setPerguntaRequerimentoCollection(new ArrayList<PerguntaRequerimento>());
			}
			perguntaAprovFututaRelocServidFlorest.getPerguntaRequerimentoCollection().add(ContextoUtil.getContexto().getPerguntaReq());
		}else if(p.getIdePergunta().equals(perguntaAprovPlanoManejoFlorestal.getIdePergunta())){
			if(Util.isNullOuVazio(perguntaAprovPlanoManejoFlorestal.getListLocalizacaoGeograficaDaPergunta())) {
				perguntaAprovPlanoManejoFlorestal.setListLocalizacaoGeograficaDaPergunta(new ArrayList<LocalizacaoGeografica>());
			}
			perguntaAprovPlanoManejoFlorestal.getListLocalizacaoGeograficaDaPergunta().add(localizacaoGeograficaSelecionada);
			if(Util.isLazyInitExcepOuNull(perguntaAprovPlanoManejoFlorestal.getPerguntaRequerimentoCollection()) || Util.isNullOuVazio(perguntaAprovPlanoManejoFlorestal.getPerguntaRequerimentoCollection())) {
				perguntaAprovPlanoManejoFlorestal.setPerguntaRequerimentoCollection(new ArrayList<PerguntaRequerimento>());
			}
			perguntaAprovPlanoManejoFlorestal.getPerguntaRequerimentoCollection().add(ContextoUtil.getContexto().getPerguntaReq());
		}else if(p.getIdePergunta().equals(perguntaCortarFlorestProducRegist.getIdePergunta())){
			if(Util.isNullOuVazio(perguntaCortarFlorestProducRegist.getListLocalizacaoGeograficaDaPergunta())) {
				perguntaCortarFlorestProducRegist.setListLocalizacaoGeograficaDaPergunta(new ArrayList<LocalizacaoGeografica>());
			}
			perguntaCortarFlorestProducRegist.getListLocalizacaoGeograficaDaPergunta().add(localizacaoGeograficaSelecionada);
			if(Util.isLazyInitExcepOuNull(perguntaCortarFlorestProducRegist.getPerguntaRequerimentoCollection()) || Util.isNullOuVazio(perguntaCortarFlorestProducRegist.getPerguntaRequerimentoCollection())) {
				perguntaCortarFlorestProducRegist.setPerguntaRequerimentoCollection(new ArrayList<PerguntaRequerimento>());
			}
			perguntaCortarFlorestProducRegist.getPerguntaRequerimentoCollection().add(ContextoUtil.getContexto().getPerguntaReq());
		}else if(p.getIdePergunta().equals(perguntaRealizarQueimaControlada.getIdePergunta())){
			if(Util.isNullOuVazio(perguntaRealizarQueimaControlada.getListLocalizacaoGeograficaDaPergunta())) {
				perguntaRealizarQueimaControlada.setListLocalizacaoGeograficaDaPergunta(new ArrayList<LocalizacaoGeografica>());
			}
			perguntaRealizarQueimaControlada.getListLocalizacaoGeograficaDaPergunta().add(localizacaoGeograficaSelecionada);
			if(Util.isLazyInitExcepOuNull(perguntaRealizarQueimaControlada.getPerguntaRequerimentoCollection()) || Util.isNullOuVazio(perguntaRealizarQueimaControlada.getPerguntaRequerimentoCollection())) {
				perguntaRealizarQueimaControlada.setPerguntaRequerimentoCollection(new ArrayList<PerguntaRequerimento>());
			}
			perguntaRealizarQueimaControlada.getPerguntaRequerimentoCollection().add(ContextoUtil.getContexto().getPerguntaReq());
		}else{
			JsfUtil.addErrorMessage("Não foi possível carregar tabela com a localização pois a pergunta a qual pertence não foi encontrada.");
		}
	}

	private void carregarPerguntasEditarVisualisar() {
		Exception erro = null;
		try {
			this.listaPerguntas = perguntaService.listarPerguntasAtivasComLocGeo();
			for(Pergunta pergunta : listaPerguntas) {
				pergunta.setListRequerimentoImovel(new ArrayList<RequerimentoImovel>());
			}
			perguntaProducaoVolumetricaMadeira = getPerguntaByCod(listaPerguntas, PerguntaEnum.PERGUNTA_PRODUCAO_VOLUMETRICA_DE_MADEIRA.getCod());
			perguntaProducaoVolumetricaMadeira.definePerguntaDosRequerimentosDeImovel();
			perguntaProducaoVolumetricaMadeira.setPerguntaRequerimentoCollection(null);
			perguntaProducaoVolumetricaMadeira.setResposta(requerimentoUnico.getIndVolumeMaterial());
			perguntaRealizarQueimaControlada   = getPerguntaByCod(listaPerguntas, PerguntaEnum.PERGUNTA_QUEIMA_CONTROLADA.getCod());
			perguntaRealizarQueimaControlada.definePerguntaDosRequerimentosDeImovel();
			perguntaRealizarQueimaControlada.setPerguntaRequerimentoCollection(null);
			perguntaRealizarQueimaControlada.setResposta(requerimentoUnico.getIndRealizarQueimaControlada());
			perguntaAprovPlanoManejoFlorestal  = getPerguntaByCod(listaPerguntas, PerguntaEnum.PERGUNTA_APROV_PLANO_MANEJO_FLOREST.getCod());
			perguntaAprovPlanoManejoFlorestal.definePerguntaDosRequerimentosDeImovel();
			perguntaAprovPlanoManejoFlorestal.setPerguntaRequerimentoCollection(null);
			perguntaAprovPlanoManejoFlorestal.setResposta(requerimentoUnico.getIndPlanoManejo());
			perguntaAprovExecPlanManejFlorest  = getPerguntaByCod(listaPerguntas, PerguntaEnum.PERGUNTA_APROV_EXEC_PLANO_MANEJO_FLOR.getCod());
			perguntaAprovExecPlanManejFlorest.definePerguntaDosRequerimentosDeImovel();
			perguntaAprovExecPlanManejFlorest.setPerguntaRequerimentoCollection(null);
			perguntaAprovExecPlanManejFlorest.setResposta(requerimentoUnico.getIndAprovExecPlanoMnjFlor());
			perguntaCortarFlorestProducRegist  = getPerguntaByCod(listaPerguntas, PerguntaEnum.PERGUNTA_CORT_FLOR_PROD_LIC_REG.getCod());
			perguntaCortarFlorestProducRegist.definePerguntaDosRequerimentosDeImovel();
			perguntaCortarFlorestProducRegist.setPerguntaRequerimentoCollection(null);
			perguntaCortarFlorestProducRegist.setResposta(requerimentoUnico.getIndCortFlorProd());
			perguntaAprovLocRelocServFlorest   = getPerguntaByCod(listaPerguntas, PerguntaEnum.PERGUNTA_APROV_LOC_RELOC_SERV_FLOREST.getCod());
			perguntaAprovLocRelocServFlorest.definePerguntaDosRequerimentosDeImovel();
			perguntaAprovLocRelocServFlorest.setPerguntaRequerimentoCollection(null);
			perguntaAprovLocRelocServFlorest.setResposta(requerimentoUnico.getIndServidaoFlorestal());
			perguntaAprovRelocServidFlorest   = getPerguntaByCod(listaPerguntas, PerguntaEnum.PERGUNTA_APROV_RELOCACAO_SERVD_FLOREST.getCod());
			perguntaAprovRelocServidFlorest.definePerguntaDosRequerimentosDeImovel();
			perguntaAprovRelocServidFlorest.setPerguntaRequerimentoCollection(null);
			perguntaAprovRelocServidFlorest.setResposta(requerimentoUnico.getIndRelocServidFlorest());
			perguntaAprovFututaRelocServidFlorest   = getPerguntaByCod(listaPerguntas, PerguntaEnum.PERGUNTA_FUTURA_RELOCACAO_SERVD_FLOREST.getCod());
			perguntaAprovFututaRelocServidFlorest.definePerguntaDosRequerimentosDeImovel();
			perguntaAprovFututaRelocServidFlorest.setPerguntaRequerimentoCollection(null);
			perguntaAprovFututaRelocServidFlorest.setResposta(requerimentoUnico.getIndRelocServidFlorest());
			List<PerguntaRequerimento> listaPerguntaReq = perguntaReqService.carregarPerguntaReqDoRequerimento(this.requerimentoUnico.getRequerimento());
			constroiValueChangeDasPerguntasDoAtoFlorestal(requerimentoUnico);

			for (PerguntaRequerimento perguntaRequerimento : listaPerguntaReq) {
				if(perguntaRequerimento.getIdePergunta().getIdePergunta().equals(perguntaProducaoVolumetricaMadeira.getIdePergunta()) && perguntaRequerimento.getIndResposta() == requerimentoUnico.getIndVolumeMaterial()){
					if(Util.isNullOuVazio(perguntaProducaoVolumetricaMadeira.getPerguntaRequerimentoCollection())) {
						perguntaProducaoVolumetricaMadeira.setPerguntaRequerimentoCollection(new ArrayList<PerguntaRequerimento>());
					}
					perguntaRequerimento.setIdeRequerimento(requerimentoUnico.getRequerimento());
					perguntaProducaoVolumetricaMadeira.getPerguntaRequerimentoCollection().add(perguntaRequerimento);
					perguntaProducaoVolumetricaMadeira.setListRequerimentoImovel(requerimentoImovelService.listarReqImoveisDaPergunta(perguntaRequerimento));
					perguntaProducaoVolumetricaMadeira.organizaLocalizacoesDoReqImovel();
					perguntaProducaoVolumetricaMadeira.setResposta(requerimentoUnico.getIndVolumeMaterial());
					requerimentoUnico.setIndVolumeMaterial(perguntaProducaoVolumetricaMadeira.getResposta());
					perguntaProducaoVolumetricaMadeira.defineTodosRequerimentosDeImovelComoDeReqUnico();
					carregarObjetivoRequerimentoLimpezaArea();
					if(!Util.isNullOuVazio(requerimentoUnico.getIndVolumeMaterial()) && !requerimentoUnico.getIndVolumeMaterial()){
						List<ObjetivoRequerimentoLimpezaArea> listObjReqAreaTemp = requerimentoUnicoService.listarObjetivoRequerimentoLimpezaAreaPorRequerimento(requerimentoUnico);
						for (ObjetivoRequerimentoLimpezaArea objetivoRequerimentoLimpezaArea : listObjReqAreaTemp) {
							objetivoRequerimentoLimpezaArea.getIdeLocalizacaoGeografica().setVlrArea(objetivoRequerimentoLimpezaArea.getIdeRequerimentoImovel().getVlrArea());
						}
						perguntaProducaoVolumetricaMadeira.setListObjReqLimpArea(listObjReqAreaTemp);
					}
				}else if(perguntaRequerimento.getIdePergunta().getIdePergunta().equals(perguntaRealizarQueimaControlada.getIdePergunta())){
					perguntaRequerimento.setIdeRequerimento(requerimentoUnico.getRequerimento());
					if(Util.isNullOuVazio(perguntaRealizarQueimaControlada.getPerguntaRequerimentoCollection())) {
						perguntaRealizarQueimaControlada.setPerguntaRequerimentoCollection(new ArrayList<PerguntaRequerimento>());
					}
					perguntaRealizarQueimaControlada.getPerguntaRequerimentoCollection().add(perguntaRequerimento);
					perguntaRealizarQueimaControlada.setListRequerimentoImovel(requerimentoImovelService.listarReqImoveisDaPergunta(perguntaRequerimento));
					perguntaRealizarQueimaControlada.organizaLocalizacoesDoReqImovel();
					perguntaRealizarQueimaControlada.setResposta(requerimentoUnico.getIndRealizarQueimaControlada());
					perguntaRealizarQueimaControlada.defineTodosRequerimentosDeImovelComoDeReqUnico();
					requerimentoUnico.setIndRealizarQueimaControlada(requerimentoUnico.getIndRealizarQueimaControlada());
				}else if(perguntaRequerimento.getIdePergunta().getIdePergunta().equals(perguntaAprovPlanoManejoFlorestal.getIdePergunta())){
					perguntaRequerimento.setIdeRequerimento(requerimentoUnico.getRequerimento());
					if(Util.isNullOuVazio(perguntaAprovPlanoManejoFlorestal.getPerguntaRequerimentoCollection())) {
						perguntaAprovPlanoManejoFlorestal.setPerguntaRequerimentoCollection(new ArrayList<PerguntaRequerimento>());
					}
					perguntaAprovPlanoManejoFlorestal.getPerguntaRequerimentoCollection().add(perguntaRequerimento);
					perguntaAprovPlanoManejoFlorestal.setListRequerimentoImovel(requerimentoImovelService.listarReqImoveisDaPergunta(perguntaRequerimento));
					perguntaAprovPlanoManejoFlorestal.organizaLocalizacoesDoReqImovel();
					perguntaAprovPlanoManejoFlorestal.defineTodosRequerimentosDeImovelComoDeReqUnico();
					requerimentoUnico.setIndPlanoManejo(requerimentoUnico.getIndPlanoManejo());
					perguntaAprovPlanoManejoFlorestal.setResposta(requerimentoUnico.getIndPlanoManejo());
				}else if(perguntaRequerimento.getIdePergunta().getIdePergunta().equals(perguntaAprovExecPlanManejFlorest.getIdePergunta())){
					perguntaRequerimento.setIdeRequerimento(requerimentoUnico.getRequerimento());
					if(Util.isNullOuVazio(perguntaAprovExecPlanManejFlorest.getPerguntaRequerimentoCollection())) {
						perguntaAprovExecPlanManejFlorest.setPerguntaRequerimentoCollection(new ArrayList<PerguntaRequerimento>());
					}
					perguntaAprovExecPlanManejFlorest.getPerguntaRequerimentoCollection().add(perguntaRequerimento);
					perguntaAprovExecPlanManejFlorest.setListRequerimentoImovel(requerimentoImovelService.listarReqImoveisDaPergunta(perguntaRequerimento));
					perguntaAprovExecPlanManejFlorest.organizaLocalizacoesDoReqImovel();
					perguntaAprovPlanoManejoFlorestal.defineTodosRequerimentosDeImovelComoDeReqUnico();
					requerimentoUnico.setIndAprovExecPlanoMnjFlor(requerimentoUnico.getIndAprovExecPlanoMnjFlor());
					perguntaAprovExecPlanManejFlorest.setResposta(requerimentoUnico.getIndAprovExecPlanoMnjFlor());
				}else if(perguntaRequerimento.getIdePergunta().getIdePergunta().equals(perguntaCortarFlorestProducRegist.getIdePergunta())){
					perguntaRequerimento.setIdeRequerimento(requerimentoUnico.getRequerimento());
					if(Util.isNullOuVazio(perguntaCortarFlorestProducRegist.getPerguntaRequerimentoCollection())) {
						perguntaCortarFlorestProducRegist.setPerguntaRequerimentoCollection(new ArrayList<PerguntaRequerimento>());
					}
					perguntaCortarFlorestProducRegist.getPerguntaRequerimentoCollection().add(perguntaRequerimento);
					perguntaCortarFlorestProducRegist.setListRequerimentoImovel(requerimentoImovelService.listarReqImoveisDaPergunta(perguntaRequerimento));
					perguntaCortarFlorestProducRegist.organizaLocalizacoesDoReqImovel();
					perguntaCortarFlorestProducRegist.defineTodosRequerimentosDeImovelComoDeReqUnico();
					requerimentoUnico.setIndCortFlorProd(requerimentoUnico.getIndCortFlorProd());
					perguntaCortarFlorestProducRegist.setResposta(requerimentoUnico.getIndCortFlorProd());

				}else if(perguntaRequerimento.getIdePergunta().getIdePergunta().equals(perguntaAprovLocRelocServFlorest.getIdePergunta())){
					perguntaRequerimento.setIdeRequerimento(requerimentoUnico.getRequerimento());
					if(Util.isNullOuVazio(perguntaAprovLocRelocServFlorest.getPerguntaRequerimentoCollection())) {
						perguntaAprovLocRelocServFlorest.setPerguntaRequerimentoCollection(new ArrayList<PerguntaRequerimento>());
					}
					perguntaAprovLocRelocServFlorest.getPerguntaRequerimentoCollection().add(perguntaRequerimento);
					perguntaAprovLocRelocServFlorest.setListRequerimentoImovel(requerimentoImovelService.listarReqImoveisDaPergunta(perguntaRequerimento));
					perguntaAprovLocRelocServFlorest.organizaLocalizacoesDoReqImovel();
					perguntaAprovLocRelocServFlorest.defineTodosRequerimentosDeImovelComoDeReqUnico();
					requerimentoUnico.setIndServidaoFlorestal(requerimentoUnico.getIndServidaoFlorestal());
					perguntaAprovLocRelocServFlorest.setResposta(requerimentoUnico.getIndServidaoFlorestal());
				}else if(perguntaRequerimento.getIdePergunta().getIdePergunta().equals(perguntaAprovRelocServidFlorest.getIdePergunta())){
					perguntaRequerimento.setIdeRequerimento(requerimentoUnico.getRequerimento());
					if(Util.isNullOuVazio(perguntaAprovRelocServidFlorest.getPerguntaRequerimentoCollection())) {
						perguntaAprovRelocServidFlorest.setPerguntaRequerimentoCollection(new ArrayList<PerguntaRequerimento>());
					}
					perguntaAprovRelocServidFlorest.getPerguntaRequerimentoCollection().add(perguntaRequerimento);
					perguntaAprovRelocServidFlorest.setListRequerimentoImovel(requerimentoImovelService.listarReqImoveisDaPergunta(perguntaRequerimento));
					perguntaAprovRelocServidFlorest.organizaLocalizacoesDoReqImovel();
					perguntaAprovRelocServidFlorest.defineTodosRequerimentosDeImovelComoDeReqUnico();
					requerimentoUnico.setIndRelocServidFlorest(requerimentoUnico.getIndRelocServidFlorest());
					perguntaAprovRelocServidFlorest.setResposta(requerimentoUnico.getIndRelocServidFlorest());
				}else if(perguntaRequerimento.getIdePergunta().getIdePergunta().equals(perguntaAprovFututaRelocServidFlorest.getIdePergunta())){
					perguntaRequerimento.setIdeRequerimento(requerimentoUnico.getRequerimento());
					if(Util.isNullOuVazio(perguntaAprovFututaRelocServidFlorest.getPerguntaRequerimentoCollection())) {
						perguntaAprovFututaRelocServidFlorest.setPerguntaRequerimentoCollection(new ArrayList<PerguntaRequerimento>());
					}
					perguntaAprovFututaRelocServidFlorest.getPerguntaRequerimentoCollection().add(perguntaRequerimento);
					perguntaAprovFututaRelocServidFlorest.setListRequerimentoImovel(requerimentoImovelService.listarReqImoveisDaPergunta(perguntaRequerimento));
					perguntaAprovFututaRelocServidFlorest.organizaLocalizacoesDoReqImovel();
					perguntaAprovFututaRelocServidFlorest.defineTodosRequerimentosDeImovelComoDeReqUnico();
					requerimentoUnico.setIndRelocServidFlorest(requerimentoUnico.getIndRelocServidFlorest());
					perguntaAprovFututaRelocServidFlorest.setResposta(requerimentoUnico.getIndRelocServidFlorest());
				}else{
					System.out.println("Pergunta não Encontrada ou tem requerimentoImovel que deveria estar excluído!");
				}
			}
		} catch (Exception e) {
			erro =e;
			JsfUtil.addErrorMessage("Não foi possível carregar as perguntas com localização Geográfica");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void carregarObjetivoRequerimentoLimpezaAreaEditarVisualizar(Requerimento requerimento) {
	
		Exception erro = null;
		try {
			List<ObjetivoRequerimentoLimpezaArea> listLimpArea = (List<ObjetivoRequerimentoLimpezaArea>) requerimentoUnicoService.listarRequerimentoObjetivoLimpezaAreaPorRequerimento(requerimento);
			if(Util.isNullOuVazio(collObjetivoRequerimentoLimpezaArea)) {
				collObjetivoRequerimentoLimpezaArea = new ArrayList<ObjetivoRequerimentoLimpezaArea>();
			}
			for (ObjetivoRequerimentoLimpezaArea objetivoReqLimpezaArea : listLimpArea) {
				collObjetivoRequerimentoLimpezaArea.add(objetivoReqLimpezaArea);
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	private void carregarTipoAto() {
		Exception erro =null;
		try {
			this.listaTipoAto = (List<TipoAto>) tipoAtoService.listarTiposAto();
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}

	}

	/**
	 * Retona o telefone segundo a regra: primeiramente o celular, se não tiver
	 * celular, retorna o comercial e por último o residencial.
	 * 
	 * @param collectionTel
	 * @return Telefone
	 */
	public Telefone getTelefoneParaRequerimento(List<Telefone> collectionTel) {
		Telefone tel = null;
		Telefone telRequerimento = null;
		Boolean isComercial = Boolean.FALSE;
		// Loop para pegar primeiramente o celular, se não tiver cel, pega o
		// comercial e por ultimo o residencial.
		for (Telefone telefone : collectionTel) {
			tel = telefone;
			if (tel.getIdeTipoTelefone().getIdeTipoTelefone() == TipoTelefoneEnum.CELULAR.getId()) {
				telRequerimento = tel;
				break;
			}
			if (!isComercial && tel.getIdeTipoTelefone().getIdeTipoTelefone() == TipoTelefoneEnum.RESIDENCIAL.getId()) {
				telRequerimento = tel;
			}
			if (tel.getIdeTipoTelefone().getIdeTipoTelefone() == TipoTelefoneEnum.COMERCIAL.getId()) {
				telRequerimento = tel;
				isComercial = true;
			}
		}
		return telRequerimento;
	}

	public void limparQuestionario() {
		enderecoContato = null;
		numeroLicencaAmbiental = null;
		numeroAlteracaoLicencaAmbiental = null;
		usoPassivelOutorgaEmpreendimento = null;
		processoUsoPassivel = null;
		processoAna = null;
		origemAgua = null;
		intervencaoCorpoHidrico = null;
		captacaoBarramento = null;
		vazaoCaptacao = null;
		localizacaoGeografica = null;
		producaoVolumetricaMadeira = null;
		limpezaArea = null;
		processoTramiteInema = null;
		processoTramiteAgua = null;
		utilizaEmpreendimentoAgua = null;
		captacaoSuperficial = null;
		captacaoSubterranea = null;
		imovelRural = null;
		vazaoFinalidadeAgua = null;
		autorizacaoPerfuracaoPoco = null;
		regularizarPerfuracaoPoco = null;
		regularizarPerfuradoPoco = null;
		construcaoBarragemCorpoHidrico = null;
		autorizacaoConstrucaoBarragemIntervencao = null;
		autorizacaoConstrucaoBarragemCaptacao = null;
		realizaEmissaoResiduosLiquidos = null;
		barragemCaptacaoContruida = null;
		avisoConstrucaoBarragemEmpreendimentoEspecifico = null;
		cortarFlorestaProducaoRegistrada = null;
		prazoValidade = null;
		florestaVinculadaReposicaoFlorestal = null;
		numeroProcessoAprovacaoPlanomanejoFlorestal = null;
		numeroPortariaAutorizacaoSupressaoVegetacao = null;
		numeroRegistroCorteFlorestalProducao = null;
		autorizacaoCorteFlorestaProducao = null;
		numeroLicencaAmbiental = null;
		origemMaterialLenhoso = null;
		numeroPortariaSupressaoVegetacaoNativa = null;
		numeroCertificadoRegistroCorteFlorestaProducao = null;
		numeroPortariaAutorizacaoCorteFlorestaProducao = null;
		porteIdentificado = null;
		transferenciaCreditoReposicaoFlorestal = null;
		vincularFlorestaProducao = null;
		suprimentoSustentavel = null;
		atoFauna = null;
		atoFlorestal = null;
	}

	private void carregarEditar(RequerimentoUnico requerimentoUnico) {
		
		try {
			requerimentoUnico = requerimentoUnicoService.recuperarRequerimentoUnicoPorId(requerimentoUnico);
			
			collEmpreendimento = new ArrayList<SelectItem>();
			
			for (EmpreendimentoRequerimento emp : requerimentoUnico.getRequerimento().getEmpreendimentoRequerimentoCollection()) {
				this.empreendimento = emp.getIdeEmpreendimento();
				collEmpreendimento.add(new SelectItem(emp.getIdeEmpreendimento().getIdeEmpreendimento(), emp.getIdeEmpreendimento().getNomEmpreendimento()));	
			}
			this.addAtributoSessao("EMPREENDIMENTO_SESSAO", this.empreendimento);
			
			this.listTipoIntervencao = requerimentoUnicoService.listarTipoIntervencaoPorRequerimento(requerimentoUnico);
			this.listTipoFinalidadeUsoAgua = requerimentoUnicoService.listarTipoFinalidadeUsoAguaPorRequerimento(requerimentoUnico);
			this.listTipoTipoReceptor = requerimentoUnicoService.listarTipoReceptorPorRequerimento(requerimentoUnico);
			this.listTipoCaptacao = requerimentoUnicoService.listarTipoCaptacaoPorRequerimento(requerimentoUnico);
			this.listObjetivoRequerimentoLimpezaArea = requerimentoUnicoService.listarObjetivoLimpezaAreaPorRequerimento(requerimentoUnico);
			
			numeroRenovacaoLicencaAmbiental(construirValueChangeEvent(requerimentoUnico.getIndRevovarLicencaAmbiental()));
			numeroAlteracaoLicencaAmbiental(construirValueChangeEvent(requerimentoUnico.getIndAlterarLicencaAmbiental()));
			numeroPortariaAutorizacaoSupressaoVegetacao(construirValueChangeEvent(requerimentoUnico.getIndProrValidAsv()));
			numeroRegistroCorteFlorestalProducao(construirValueChangeEvent(requerimentoUnico.getIndProrValidRcfp()));
			autorizacaoCorteFlorestaProducao(construirValueChangeEvent(requerimentoUnico.getIndProrValidAcfp()));
			numeroLicencaAmbiental(construirValueChangeEvent(requerimentoUnico.getIndProrValidLa()));
			procesoTramiteInema(construirValueChangeEvent(requerimentoUnico.getIndTramiteInema()));
			
			if (requerimentoUnico.getIndTramiteInema() != null && requerimentoUnico.getIndTramiteInema()) {
				requerimentoUnicoService.recuperarProcessoTramite(requerimentoUnico);
				this.processoTramiteAtivo = Boolean.TRUE;
			} else {
				requerimentoUnico.getRequerimento().setProcessoTramiteCollection(new ArrayList<ProcessoTramite>());
			}

			processoAna(construirValueChangeEvent(requerimentoUnico.getIndTramiteAna()));
			
			if (processoUsoPassivel) {
				usoPassivelOutorgaEmpreendimento(construirValueChangeEvent(requerimentoUnico.getIndProcOutorgaAtende()));
			}
			
			verificarProcessoAna(requerimentoUnico);
			
			requerimentoImovelData = new ListDataModel<RequerimentoImovel>((List<RequerimentoImovel>) 
					requerimentoUnicoService.recuperarImoveisRequerimento(requerimentoUnico.getRequerimento(), empreendimento));
			
			carregarPerguntas();
			carregarEventoDasPerguntasComShape(requerimentoUnico);
			
			List<RequerimentoTipologia> lista = new ArrayList<RequerimentoTipologia>();
			
			if(requerimentoUnicoDTO.isVisualizar()) {
				List<RequerimentoTipologia> listaRequerimentoTipologiaPorRequerimento = (List<RequerimentoTipologia>) 
						requerimentoUnicoService.listarRequerimentoTipologiaPorRequerimento(requerimentoUnico.getRequerimento());
				
				this.verificarTipologias();
				
				for (RequerimentoTipologia requerimentoTipologia : listaRequerimentoTipologiaPorRequerimento) {
					if(!Util.isNull(requerimentoTipologia.getIdeUnidadeMedidaTipologiaGrupo())
						&& !Util.isNull(requerimentoTipologia.getIdeUnidadeMedidaTipologiaGrupo().getIdeUnidadeMedidaTipologiaGrupo())){
						
						lista.add(requerimentoTipologia);
					}
				}
			}
			else {
				Collection<Tipologia> listaTipologiaEmpreendimento = empreendimentoService.buscarTipologias(empreendimento, true, true);
				for(Tipologia tipologia : listaTipologiaEmpreendimento) {
					
					RequerimentoTipologia  requerimentoTipologia = new RequerimentoTipologia();
					
					TipologiaGrupo tipologiaGrupo = tipologia.getTipologiaGrupo();
					tipologiaGrupo.setIdeTipologia(tipologia);
					
					UnidadeMedidaTipologiaGrupo unidadeMedidaTipologiaGrupo = tipologiaGrupo.getUnidadeMedidaTipologiaGrupo();
					
					unidadeMedidaTipologiaGrupo.setIdeTipologiaGrupo(tipologiaGrupo);
					
					requerimentoTipologia.setIdeRequerimento(requerimentoUnico.getRequerimento());
					requerimentoTipologia.setIdeUnidadeMedidaTipologiaGrupo(unidadeMedidaTipologiaGrupo);
					requerimentoTipologia.setValAtividade(new BigDecimal(0));
					
					lista.add(requerimentoTipologia);
				}
				calculoPorteAtivo = true;
			}			
			
			requerimentoTipologiaData = new ListDataModel<RequerimentoTipologia>(lista);
			
			imovelRural = Boolean.FALSE;
			
			if (requerimentoImovelData.isRowAvailable()) {
				imovelRural = Boolean.TRUE;
			}
			
			constroiValueChangeDasPerguntasDoAtoFlorestal(requerimentoUnico);
			atoFauna(construirValueChangeEvent(requerimentoUnico.getIndAtoFauna()));
			
			editando = false;
			this.calculoAtividadeValido = Boolean.TRUE;
			this.requerimentoUnico = requerimentoUnico;

			if(!requerimentoUnico.getIdePorte().equals(new Porte(7))){
				validarPorte();
			}

			/*for (RequerimentoTipologia requerimentoTipologia : requerimentoTipologiaData) {
				if (requerimentoTipologia.isIndTipologiaPrincipal()) {
					this.idTipologiaPrincipal = requerimentoTipologia.getIdeRequerimentoTipologia().toString();
				}
			}*/
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * @param requerimentoUnico
	 * @throws Exception
	 */
	private void verificarProcessoAna(RequerimentoUnico requerimentoUnico)
			throws Exception {
		this.listTipoCaptacao = requerimentoUnicoService.listarTipoCaptacaoPorRequerimento(requerimentoUnico);
		this.listTipoFinalidadeUsoAgua = requerimentoUnicoService.listarTipoFinalidadeUsoAguaPorRequerimento(requerimentoUnico);
		if (usoPassivelOutorgaEmpreendimento || (requerimentoUnico.getIndProcOutorgaAtende() != null && !requerimentoUnico.getIndProcOutorgaAtende())) {
			utilizaEmpreendimentoAgua(construirValueChangeEvent(requerimentoUnico.getIndUtilizaAgua()));
			if (utilizaEmpreendimentoAgua) {
				checkOrigemAgua(null);
				if (captacaoSuperficial != null && captacaoSuperficial) {
					barragemCaptacaoContruida(construirValueChangeEvent(requerimentoUnico.getIndCaptacaoBarramento()));
					if (barragemCaptacaoContruida) {
						autorizacaoConstrucaoBarragemCaptacao(construirValueChangeEvent(requerimentoUnico.getIndBarragemCaptConstruida()));
					}
				}
				if (captacaoSubterranea != null && captacaoSubterranea) {
					autorizacaoPerfuracaoPoco(construirValueChangeEvent(listTipoCaptacao.contains("4")));
				}
				if ((captacaoSuperficial != null && captacaoSuperficial) || (captacaoSubterranea != null && captacaoSubterranea)) {
					this.requerimentoUnico.setNumVazaoCaptacao(requerimentoUnico.getNumVazaoCaptacao());
					verirficarVazaoCaptacao(null);
					checkFinalidadeAgua(null);
					if (localizacaoGeografica) {

						if(!Util.isNullOuVazio(requerimentoUnico.getIdeLocalizacaoGeografica())){
							requerimentoUnicoService.recuperarLocalizacaoGeograficaPorRequerimentoUnico(requerimentoUnico);
							this.requerimentoUnico.setIdeLocalizacaoGeografica(requerimentoUnico.getIdeLocalizacaoGeografica());
							if(!modoEdicao && !desabilitarTudo) {
								resetarLocalizacaoGeografica();
							}
							this.datum = requerimentoUnico.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada();
							this.localizacaoGeograficaSelecionada = requerimentoUnico.getIdeLocalizacaoGeografica();
						}
					}
				}
			}
			realizaEmissaoResiduosLiquidos(construirValueChangeEvent(requerimentoUnico.getIndRealizaEmissaoResiduosLiquidos()));
			intervencaoCorpoHidrico(construirValueChangeEvent(requerimentoUnico.getIndIntervencaoCorpoHidrico()));
			if (intervencaoCorpoHidrico != null && intervencaoCorpoHidrico) {
				checkIntervencaoHidrico(null);
				if (construcaoBarragemCorpoHidrico != null && construcaoBarragemCorpoHidrico) {
					autorizacaoConstrucaoBarragemIntervencao(construirValueChangeEvent(requerimentoUnico.getIndBarragemIntervConstruida()));
				}
			}
			regularizarPerfuracaoPoco(construirValueChangeEvent(requerimentoUnico.getIndRegularizarPerfPoco()));
			if (regularizarPerfuracaoPoco) {
				regularizarPerfuradoPoco(construirValueChangeEvent(requerimentoUnico.getIndPocoRegularizarPerfurado()));
			}
		}
	}

	/**
	 * @param requerimentoUnico
	 */
	private void constroiValueChangeDasPerguntasDoAtoFlorestal(RequerimentoUnico requerimentoUnico) {
		atoFlorestal(construirValueChangeEvent(requerimentoUnico.getIndAtoAmbiental()));
		if (requerimentoUnico.getIndAtoAmbiental()) {
			supressaoVegetal(construirValueChangeEvent(requerimentoUnico.getIndSupressaoVegetacao()));
			if (producaoVolumetricaMadeira) {
				limpezaArea(construirValueChangeEvent(requerimentoUnico.getIndVolumeMaterial()));
			}
			suprimentoSustentavel(construirValueChangeEvent(requerimentoUnico.getIndDeclaracaoPlanoSuprSust()));
			vincularFlorestaProducao(construirValueChangeEvent(requerimentoUnico.getIndVincFlorProdRepFlor()));
			transferirCreditoReposicaoFlorestal(construirValueChangeEvent(requerimentoUnico.getIndTransfCredRepFlorestal()));
			numeroProcessoAprovacaoPlanomanejoFlorestal(construirValueChangeEvent(requerimentoUnico.getIndAprovExecPlanoMnjFlor()));
			cortarFlorestaProducaoRegistrada(construirValueChangeEvent(requerimentoUnico.getIndCortFlorProd()));
			florestaVinculada(construirValueChangeEvent(requerimentoUnico.getIndFlorVincRepFlor()));
			origemMaterialLenhoso(construirValueChangeEvent(requerimentoUnico.getIndRecVolFlorRem()));
			numeroPortariaSupressaoVegetacaoNativa(construirValueChangeEvent(requerimentoUnico.getIndOrigMatLenAsv()));
			numeroCertificadoRegistroCorteFlorestaProducao(construirValueChangeEvent(requerimentoUnico.getIndOrigMatLenRcfp()));
			numeroPortariaAutorizacaoCorteFlorestaProducao(construirValueChangeEvent(requerimentoUnico.getIndOrigMatLenAcfp()));
			aprovaPlanoManejo(construirValueChangeEvent(requerimentoUnico.getIndPlanoManejo()));
			queimaControlada(construirValueChangeEvent(requerimentoUnico.getIndRealizarQueimaControlada()));
		}
	}

	/**
	 * Carrega as perguntas:
	 * 		- Queima Controlada [getIndRealizarQueimaControlada],
	 * 		- limpeza de Area [getIndVolumeMaterial],
	 * 		- Aprovação de Plano de Manejo [getIndPlanoManejo],
	 * 		- Aprovação de execução de Plano de Manejo [getIndAprovExecPlanoMnjFlor],
	 * 		- Cortar Floresta Produção Registrada [getIndCortFlorProd],
	 * 		- Locação e relocação de Servidão ambiental [getIndFlorVincRepFlor];
	 * Para cada pergunta é preciso q o requerimento Unico estaja com a resposta informada e que o mesmo não seja nulo, não precisa ter id, mas tem q ter as respostas, se a resposta for nula será
	 * passado false para carregar o change.
	 * @param requerimentoUnico
	 */
	private void carregarEventoDasPerguntasComShape(RequerimentoUnico requerimentoUnico) {
		imovelRural = true;
		if(!Util.isNullOuVazio(requerimentoUnico.getIndRealizarQueimaControlada())) {
			queimaControlada(construirValueChangeEvent(requerimentoUnico.getIndRealizarQueimaControlada()));
		} else {
			queimaControlada(construirValueChangeEvent(false));
		}
		if(!Util.isNullOuVazio(requerimentoUnico.getIndVolumeMaterial())) {
			limpezaArea(construirValueChangeEvent(requerimentoUnico.getIndVolumeMaterial()));
		} else {
			limpezaArea(construirValueChangeEvent(false));
		}
		if(!Util.isNullOuVazio(requerimentoUnico.getIndPlanoManejo())) {
			aprovaPlanoManejo(construirValueChangeEvent(requerimentoUnico.getIndPlanoManejo()));
		} else {
			aprovaPlanoManejo(construirValueChangeEvent(false));
		}
		if(!Util.isNullOuVazio(requerimentoUnico.getIndAprovExecPlanoMnjFlor())) {
			numeroProcessoAprovacaoPlanomanejoFlorestal(construirValueChangeEvent(requerimentoUnico.getIndAprovExecPlanoMnjFlor()));
		} else {
			numeroProcessoAprovacaoPlanomanejoFlorestal(construirValueChangeEvent(false));
		}
		if(!Util.isNullOuVazio(requerimentoUnico.getIndCortFlorProd())) {
			cortarFlorestaProducaoRegistrada(construirValueChangeEvent(requerimentoUnico.getIndCortFlorProd()));
		} else {
			cortarFlorestaProducaoRegistrada(construirValueChangeEvent(false));
		}
		if(!Util.isNullOuVazio(requerimentoUnico.getIndFlorVincRepFlor())) {
			locRelocServidFlorest(construirValueChangeEvent(requerimentoUnico.getIndFlorVincRepFlor()));
		} else {
			locRelocServidFlorest(construirValueChangeEvent(false));
		}
	}

	private void resetarLocalizacaoGeografica() {
		LocalizacaoGeografica localizacaoGeografica = this.requerimentoUnico.getIdeLocalizacaoGeografica();
		localizacaoGeografica.setIdeLocalizacaoGeografica(null);
		for (DadoGeografico dado : localizacaoGeografica.getDadoGeograficoCollection()) {
			dado.setIdeDadoGeografico(null);
		}
	}

	private void verificarTipologias() throws Exception {
		List<RequerimentoTipologia> listaRequerimentoTipologiaPorRequerimento = (List<RequerimentoTipologia>) requerimentoUnicoService
				.listarRequerimentoTipologia(this.empreendimento);
		for (RequerimentoTipologia requerimentoTipologia : listaRequerimentoTipologiaPorRequerimento) {
			String desTipologia = requerimentoTipologia.getIdeUnidadeMedidaTipologiaGrupo().getIdeTipologiaGrupo().getIdeTipologia().getCodTipologia();
			if(desTipologia.equals("X") || desTipologia.equals("Y")){
				this.calculoPorteAtivo = false;
			}else{
				this.calculoPorteAtivo = true;
				break;
			}
		}
	}

	private ValueChangeEvent construirValueChangeEvent(Boolean valor) {
		if (valor != null) {
			return new ValueChangeEvent(new HtmlSelectOneRadio(), false, valor);
		} else {
			return new ValueChangeEvent(new HtmlSelectOneRadio(), false, false);
		}
	}

	public void limparFormLocGeografica() {
		this.grausLatitudeLoc = "";
		this.minutosLatitudeLoc = "";
		this.segundosLatitudeLoc = "";
		this.grausLongitudeLoc = "";
		this.minutosLongitudeLoc = "";
		this.segundosLongitudeLoc = "";
		this.fracaoGrauLatitudeLoc = "";
		this.fracaoGrauLongitudeLoc = "";
		this.verticeLoc = new DadoGeografico();
	}

	public void definirDatum(AjaxBehaviorEvent event) {
		try {
			SistemaCoordenada datum = serviceDatum.carregar(this.datum.getIdeSistemaCoordenada());
			this.localizacaoGeograficaSelecionada.setIdeSistemaCoordenada(datum);
			this.localizacaoGeograficaSelecionada.setIdeClassificacaoSecao(new ClassificacaoSecaoGeometrica(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId()));
			this.localizacaoGeograficaSelecionada.setDtcCriacao(new Date());
			this.localizacaoGeograficaSelecionada.setDtcExclusao(null);
			this.localizacaoGeograficaSelecionada.setIndExcluido(Boolean.FALSE);
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void incluirVertice() {
		calculaFracaoGrauLatitudeLoc(null);
		calculaFracaoGrauLongitudeLoc(null);
		if (!Util.isNullOuVazio(this.fracaoGrauLatitudeLoc) && !Util.isNullOuVazio(this.fracaoGrauLongitudeLoc)) {
			try {
				Point ponto = PostgisUtil.criarPonto(Double.parseDouble(this.fracaoGrauLatitudeLoc), Double.parseDouble(this.fracaoGrauLongitudeLoc));
				this.verticeLoc.setCoordGeoNumerica(ponto.toString());
				this.verticeLoc.setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
				if(Util.isNullOuVazio(localizacaoGeograficaSelecionada.getDadoGeograficoCollection())) {
					localizacaoGeograficaSelecionada.setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
				}
				localizacaoGeograficaSelecionada.getDadoGeograficoCollection().add(verticeLoc);
				RequestContext.getCurrentInstance().execute("dialogoIncluirVertice.hide()");
				limparFormLocGeografica();
			} catch (Exception exception) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
				JsfUtil.addErrorMessage(exception.getMessage());
			}
		} else {
			RequestContext.getCurrentInstance().addCallbackParam("validationFailed", true);
		}
	}

	public void excluirVertice() {
		try {
			localizacaoGeograficaSelecionada.getDadoGeograficoCollection().remove(verticeExclusaoLoc);
			this.localizacaoGeograficaService.removerById(verticeExclusaoLoc);
			this.limparFormLocGeografica();
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}
	private Pergunta perguntaDalocalizacaoGeograficaASerExcluida;
	private String hintAjuda;

	public void excluirAPorraDaLocalizacao(){
		Exception erro =null;
		try {

			if(btnCancelar && Util.isNull(localizacaoGeograficaASerExcluida)){
				return;
			}

			RequerimentoImovel reqImovelDaLocalizacaoGeografica = perguntaDalocalizacaoGeograficaASerExcluida.getReqImovelDaLocalizacaoGeografica(localizacaoGeograficaASerExcluida);
			requerimentoImovelService.excluirReqImovel(reqImovelDaLocalizacaoGeografica);

			if(!Util.isNull(ContextoUtil.getContexto().getPergunta())) {
				ContextoUtil.getContexto().getPergunta().getListRequerimentoImovel().remove(reqImovelDaLocalizacaoGeografica);
			}

			if(!btnCancelar){
				perguntaDalocalizacaoGeograficaASerExcluida.getListLocalizacaoGeograficaDaPergunta().remove(localizacaoGeograficaASerExcluida);
				perguntaDalocalizacaoGeograficaASerExcluida.getListRequerimentoImovel().remove(reqImovelDaLocalizacaoGeografica);
			}

			if(!Util.isNull(objLimpezaAreaSelecionado)){
				perguntaDalocalizacaoGeograficaASerExcluida.removeLocalizacaoByObjetivo(objLimpezaAreaSelecionado,localizacaoGeograficaASerExcluida);
				removeLocalizacaoByObjetivoDoContexto(objLimpezaAreaSelecionado,localizacaoGeograficaASerExcluida);
			}

			this.objLimpezaAreaSelecionado = null;

			if(!btnCancelar) {
				JsfUtil.addSuccessMessage("Localização Geográfica Excluída com Sucesso!");
			}

			btnCancelar = false;
		} catch (Exception e) {
			erro =e;
			this.objLimpezaAreaSelecionado = null;
			System.err.println(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addSuccessMessage("Erro ao excluir Localização Geográfica!");
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void removeLocalizacaoByObjetivoDoContexto(ObjetivoLimpezaArea objetivoLimpezaArea,LocalizacaoGeografica localizacaoGeografica) {
		List<ObjetivoRequerimentoLimpezaArea> listObjReqLimpArea = ContextoUtil.getContexto().getListaObjetReqLimpArea();
		if(!Util.isNullOuVazio(listObjReqLimpArea)){
			
			try {
				for (ObjetivoRequerimentoLimpezaArea objetivoRequerimentoLimpezaArea : Util.clonaListaObjReqLimpArea(listObjReqLimpArea)) {
					if(objetivoRequerimentoLimpezaArea.getIdeObjetivoLimpezaArea().equals(objetivoLimpezaArea)
							&& objetivoRequerimentoLimpezaArea.getIdeLocalizacaoGeografica().equals(localizacaoGeografica)){
						listObjReqLimpArea.remove(objetivoRequerimentoLimpezaArea);
					}
				}
			} catch (CloneNotSupportedException e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}
		}
	}

	public void excluirLocGeoDePergunta() {
		boolean temExclusao = false;
		try {
			System.out.println();
			if(!Util.isNullOuVazio(requerimentoImovelAExcluir) && Util.isNullOuVazio(requerimentoImovelAExcluir.getIdeLocalizacaoGeografica())){//se ja tem localização pode excluir logo
				if(!Util.isNullOuVazio(ContextoUtil.getContexto().getPerguntaReq()) && !Util.isNullOuVazio(ContextoUtil.getContexto().getPerguntaReq().getIdeLocalizacaoGeografica())){//Se não tem nada no contexto é pq ele nada salvou, então nada deve acontecer
					requerimentoImovel.setIdeLocalizacaoGeografica(ContextoUtil.getContexto().getPerguntaReq().getIdeLocalizacaoGeografica());
					requerimentoImovelAExcluir.setIdeLocalizacaoGeografica(ContextoUtil.getContexto().getPerguntaReq().getIdeLocalizacaoGeografica());
					requerimentoImovelService.excluirReqImovel(requerimentoImovelAExcluir);
					ContextoUtil.getContexto().setPerguntaReq(null);
					temExclusao = true;
				}
			}else{
				if(!Util.isNullOuVazio(requerimentoImovelAExcluir)){
					requerimentoImovelService.excluirReqImovel(requerimentoImovelAExcluir);
					temExclusao = true;
				}else{
					JsfUtil.addWarnMessage("Localização Geográfica chegou vazia.");
				}
			}
			if(!Util.isNullOuVazio(perguntaReqTemp) && !Util.isNullOuVazio(perguntaReqTemp.getIdePergunta())){
				if(!Util.isNullOuVazio(perguntaProducaoVolumetricaMadeira) && !Util.isNullOuVazio(perguntaProducaoVolumetricaMadeira.getIdePergunta()) && perguntaProducaoVolumetricaMadeira.getIdePergunta().equals(perguntaReqTemp.getIdePergunta().getIdePergunta())){
					if(perguntaProducaoVolumetricaMadeira.getResposta()) {
						setaLocGeoELocGeoNalistaComoNull(perguntaProducaoVolumetricaMadeira);
					} else if(temExclusao){
						removeObjetivoReqLimpAreaDoObjLimpAreaSelec();
						setaLocGeoELocGeoNalistaComoNull(perguntaProducaoVolumetricaMadeira);
					}
				}else if(!Util.isNullOuVazio(perguntaRealizarQueimaControlada) && !Util.isNullOuVazio(perguntaRealizarQueimaControlada.getIdePergunta()) && perguntaRealizarQueimaControlada.getIdePergunta().equals(perguntaReqTemp.getIdePergunta().getIdePergunta())){
					setaLocGeoELocGeoNalistaComoNull(perguntaRealizarQueimaControlada);
				}else if(!Util.isNullOuVazio(perguntaCortarFlorestProducRegist) && !Util.isNullOuVazio(perguntaCortarFlorestProducRegist.getIdePergunta()) && perguntaCortarFlorestProducRegist.getIdePergunta().equals(perguntaReqTemp.getIdePergunta().getIdePergunta())){
					setaLocGeoELocGeoNalistaComoNull(perguntaCortarFlorestProducRegist);
				}else if(!Util.isNullOuVazio(perguntaAprovPlanoManejoFlorestal) && !Util.isNullOuVazio(perguntaAprovPlanoManejoFlorestal.getIdePergunta()) && perguntaAprovPlanoManejoFlorestal.getIdePergunta().equals(perguntaReqTemp.getIdePergunta().getIdePergunta())){
					setaLocGeoELocGeoNalistaComoNull(perguntaAprovPlanoManejoFlorestal);
				}else if(!Util.isNullOuVazio(perguntaAprovLocRelocServFlorest) && !Util.isNullOuVazio(perguntaAprovLocRelocServFlorest.getIdePergunta()) && perguntaAprovLocRelocServFlorest.getIdePergunta().equals(perguntaReqTemp.getIdePergunta().getIdePergunta())){
					setaLocGeoELocGeoNalistaComoNull(perguntaAprovLocRelocServFlorest);
				}else if(!Util.isNullOuVazio(perguntaAprovRelocServidFlorest) && !Util.isNullOuVazio(perguntaAprovRelocServidFlorest.getIdePergunta()) && perguntaAprovRelocServidFlorest.getIdePergunta().equals(perguntaReqTemp.getIdePergunta().getIdePergunta())){
					setaLocGeoELocGeoNalistaComoNull(perguntaAprovRelocServidFlorest);
				}else if(!Util.isNullOuVazio(perguntaAprovFututaRelocServidFlorest) && !Util.isNullOuVazio(perguntaAprovFututaRelocServidFlorest.getIdePergunta()) && perguntaAprovFututaRelocServidFlorest.getIdePergunta().equals(perguntaReqTemp.getIdePergunta().getIdePergunta())){
					setaLocGeoELocGeoNalistaComoNull(perguntaAprovFututaRelocServidFlorest);
				}else if(!Util.isNullOuVazio(perguntaAprovExecPlanManejFlorest) && !Util.isNullOuVazio(perguntaAprovExecPlanManejFlorest.getIdePergunta()) && perguntaAprovExecPlanManejFlorest.getIdePergunta().equals(perguntaReqTemp.getIdePergunta().getIdePergunta())){
					setaLocGeoELocGeoNalistaComoNull(perguntaAprovExecPlanManejFlorest);
				}else{
					JsfUtil.addWarnMessage("Imóvel não encontrado nas perguntas.");
				}
			}
			if(temExclusao){
				perguntaReqTemp = new PerguntaRequerimento();
				perguntaReqTemp.setIdePerguntaRequerimento(null);
				perguntaReqTemp.setIdeLocalizacaoGeografica(null);
				if(!btnCancelar){
					JsfUtil.addSuccessMessage("Localização Geográfica Excluída com Sucesso!");
					btnCancelar = false;
				}
			}
		}catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	/**
	 * Só limpa para produção volumetrica de madeira
	 */
	private void removeObjetivoReqLimpAreaDoObjLimpAreaSelec() {
		if(!Util.isNullOuVazio(perguntaProducaoVolumetricaMadeira.getListObjReqLimpArea())) {
			try {
				for (ObjetivoRequerimentoLimpezaArea objReqArea : Util.clonaListaObjReqLimpArea(perguntaProducaoVolumetricaMadeira.getListObjReqLimpArea())) {
					if(objReqArea.getIdeObjetivoLimpezaArea().getIdeObjetivoLimpezaArea().equals(objLimpezaAreaSelecionado.getIdeObjetivoLimpezaArea())){
						perguntaProducaoVolumetricaMadeira.getListObjReqLimpArea().remove(objReqArea);
						break;
					}
				}
				ContextoUtil.getContexto().setListaObjetReqLimpArea(Util.clonaListaObjReqLimpArea(perguntaProducaoVolumetricaMadeira.getListObjReqLimpArea()));
			} catch (CloneNotSupportedException e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}
		}
	}

	/**
	 * 
	 */
	private void setaLocGeoELocGeoNalistaComoNull(Pergunta p) {
		if(!Util.isNullOuVazio(p.getListRequerimentoImovel())) {
			for (RequerimentoImovel reqImovel: p.getListRequerimentoImovel()) {
				if(reqImovel.equals(requerimentoImovelAExcluir)){
					reqImovel.setIdeLocalizacaoGeografica(null);
					reqImovel.setLocalizacaoGeograficaNaLista(null);
					if(!Util.isNullOuVazio(reqImovel.getPerguntaRequerimento())){
						reqImovel.getPerguntaRequerimento().setIdeLocalizacaoGeografica(null);
						reqImovel.getPerguntaRequerimento().setIdePerguntaRequerimento(null);
					}
					if(!Util.isNullOuVazio(p.getListLocalizacaoGeograficaDaPergunta())) {
						p.getListLocalizacaoGeograficaDaPergunta().remove(requerimentoImovelAExcluir.getIdeLocalizacaoGeografica());
					}
					requerimentoImovelAExcluir.setIdeLocalizacaoGeografica(null);
					requerimentoImovelAExcluir.setLocalizacaoGeograficaNaLista(null);
					ContextoUtil.getContexto().setObjectToLocGeo(new Object());
				}
			}
		}

		if(p.getIdePergunta().equals(perguntaReqTemp.getIdePergunta().getIdePergunta())){
			if(!Util.isLazyInitExcepOuNull(p.getPerguntaRequerimentoCollection())) {
				p.getPerguntaRequerimentoCollection().remove(requerimentoImovelAExcluir.getPerguntaRequerimento());
			}
			p.getListRequerimentoImovel().remove(requerimentoImovelAExcluir);
		}
	}

	public void consultar() {
		this.datatableRequerimentos.setFirst(0);
		this.datatableRequerimentos.setPage(1);
		if (validaPeriodo()) {
			if(!Util.isNullOuVazio(numeroRequerimento)) {
				requerimentoUnico.getRequerimento().setNumRequerimento(numeroRequerimento.trim());
			} else {
				requerimentoUnico.getRequerimento().setNumRequerimento(null);
			}
			carregarPaginacaoRequerimentos();
		}
	}

	@SuppressWarnings("serial")
	private void carregarPaginacaoRequerimentos() {
		params = new HashMap<String, Object>();

		params.put("TIPOATO", this.tipoAto);
		params.put("ATO", this.atoAmbiental);
		if(!Util.isNullOuVazio(this.tipoAto) && Util.isNullOuVazio(this.atoAmbiental)){
			params.put("LISTAATOS", this.listaAto);
		}


		if(urSelecionada!=null){
			Exception erro =null;
			try {
				municipios =(ArrayList<Municipio>) municipioService.filtrarMunicipiosUR(urSelecionada);
			} catch (Exception e) {
				erro =e;
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}

		} else {
			municipios = null;
		}

		listaRequerimentosDTOModel = new LazyDataModel<RequerimentoUnicoDTO>() {

			@Override
			public List<RequerimentoUnicoDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
				List<RequerimentoUnicoDTO> lista = null;
				pageCount.firtPage = first;
				pageCount.fPageSize = pageSize;
				Exception erro = null;
				try {
					setPageSize(pageSize);
					lista = requerimentoUnicoService.consultaRequerimentoUnico(requerimentoUnico, periodoInicio, periodoFim, empreendimentoRequerimento,
							requerente, statusRequerimento,municipioSelecionado,municipios, ContextoUtil.getContexto().getUsuarioLogado(), first, pageSize, params);
					listaRequerimentosDTOModel.setRowCount(requerimentoUnicoService.countRequerimentoUnico(pageCount.firtPage, pageCount.fPageSize,
							requerimentoUnico, periodoInicio, periodoFim, empreendimentoRequerimento, requerente, statusRequerimento, municipioSelecionado,municipios,
							ContextoUtil.getContexto().getUsuarioLogado(), params));
				} catch (Exception e) {
					erro =e;
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
					pageCount.firtPage = first;
					pageCount.fPageSize = pageSize;
					listaRequerimentosDTOModel.setRowCount(0);
					JsfUtil.addErrorMessage(e.getMessage());
				}finally{
					if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
				}
				return lista;
			}
		};
		addAtributoSessao("REQUERIMENTOS", listaRequerimentosDTOModel);
	}

	public void atualizarListaRequerimentos() {
		Exception erro = null;
		try {
			requerimentos = requerimentoUnicoService.filtrarRequerimentoUnicoByConsulta(requerimentoUnico, empreendimentoRequerimento, periodoInicio,
					periodoFim, statusRequerimento, requerente, new TipoPessoaRequerimento(TipoPessoaRequerimentoEnum.REQUERENTE.getId()));
		} catch (Exception e) {
			erro=e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public String atualizarListaRequerimentosAction() {
		atualizarListaRequerimentos();
		return "";
	}

	public StreamedContent getGerarDocumentoDeclaracaoPdf() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			Requerimento requerimento = this.requerimentoUnicoDTO.getRequerimentoUnico().getRequerimento();
			List<Certificado> certificados = this.certificadoService.carregarByIdRequerimento(requerimento.getIdeRequerimento());
			for (AtoAmbiental atoAmbiental : this.requerimentoUnicoDTO.getRequerimentoUnico().getAtosAmbientais()) {
				Certificado certificado = certificadoUtil.gerarCertificado(atoAmbiental, requerimento);
				if (!certificados.contains(certificado)) {
					String numeroCertificado = this.certificadoService.gerarNumeroCertificado(certificado);
					certificado.setNumCertificado(numeroCertificado);
					this.certificadoService.salvar(certificado);
				}
			}
			Integer ideRequerimento = requerimentoUnicoDTO.getRequerimentoUnico().getRequerimento().getIdeRequerimento();
			params.put("ide_requerimento", ideRequerimento);
			RelatorioUtil lRelatorio = new RelatorioUtil("documentoDeclaracao.jasper", params);
			return lRelatorio.gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return null;
	}

	public StreamedContent getGerarDocumentoDLA() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ide_requerimento", requerimentoUnicoDTO.getRequerimentoUnico().getIdeRequerimentoUnico());
			RelatorioUtil lRelatorio = new RelatorioUtil("dla.jasper", params);
			DefaultStreamedContent relatorio = (DefaultStreamedContent) lRelatorio.gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
			relatorio.setContentType("application/pdf");
			return relatorio;
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return null;
	}

	public StreamedContent getImprimirRelatorioCorteFloresta(RequerimentoUnicoDTO requerimentoUnicoDTO) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ide_requerimento", requerimentoUnicoDTO.getRequerimentoUnico().getIdeRequerimentoUnico());
			return new RelatorioUtil("relatorio_rcfp.jasper", params).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return null;
	}

	public StreamedContent getRegistroCorteFlorestaProducaoPlantada(RequerimentoUnicoDTO requerimentoUnicoDTO) {
		try {
			return new ImpressoraAtoDeclaratorio().imprimirCertificado(requerimento.getIdeRequerimento(), DocumentoObrigatorioEnum.FORMULARIO_RFP);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public StreamedContent getImprimirDeclaracaoLimpezaArea(RequerimentoUnicoDTO requerimentoUnicoDTO) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ide_requerimento", requerimentoUnicoDTO.getRequerimentoUnico().getIdeRequerimentoUnico());
			return new RelatorioUtil("declaracao_limpeza_area.jasper", params).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return null;
	}

	public StreamedContent getImprimirCertificadoLacGenerico() {
		try {
			Map<String, Object> params = new HashMap<String, Object>();

			if (Util.isNull(requerimentoUnicoDTO.getRequerimentoUnico().getRequerimento().getCertificados())) {
				// LAC GENERICA
			}

			params.put("ide_requerimento", requerimentoUnicoDTO.getRequerimentoUnico().getIdeRequerimentoUnico());

			return new RelatorioUtil("certificado_lac_generico.jasper", params).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}

		return null;
	}

	public StreamedContent getGerarResumoRequerimentoPdf() {
		
	
		
		try {
			List<Imovel> listaImovel = (List<Imovel>) empreendimentoService.obterTipoImovelPorIdeEmpreendimento(requerimentoUnicoDTO.getEmpreendimento().getIdeEmpreendimento()).getImovelCollection();
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ide_requerimento", requerimentoUnicoDTO.getRequerimentoUnico().getIdeRequerimentoUnico());
			params.put("nom_tipo_imovel", listaImovel.isEmpty() ? "Não Identificado" : listaImovel.get(0).getIdeTipoImovel().getNomTipoImovel());
			
			RelatorioUtil lRelatorio = new RelatorioUtil("resumo_requerimento.jasper", params);
			
			return lRelatorio.gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		
		
		return null;
	}

	// Processo Tramite - begin
	public void incluirProcessoTramite() {
		if (periodoDoProcesso.equals("0")) {
			// processos da tabela do seia gerados a partir do dia 05/06/2012
			tratarProcessoNovo();
		} else {
			tratarProcessoLegado();
		}
	}

	public void buscarProcessoRequerimento(RequerimentoUnicoDTO requerimentoUnicoDTO){
		Exception erro =null;
		try {
			setNumeroProcessoRequerimento(processoService.buscarPorRequerimento(requerimentoUnicoDTO.getRequerimentoUnico().getIdeRequerimentoUnico()).getNumProcesso());
		} catch (Exception e) {
			erro=e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	private void tratarProcessoNovo() {
		Exception erro =null;
		try {
			Processo processo = new Processo();
			processo.setNumProcesso(processoExterno.getProcesso());
			Processo processoRecuperado = null;
			processoRecuperado = processoService.filtrarProcessoByNumero(processo);
			if (!Util.isNull(processoRecuperado)) {
				adiconarProcessoTramite(processoRecuperado.getNumProcesso());
			} else {
				limparProcessoTramite();
				JsfUtil.addErrorMessage("O processo informado não foi encontrado. Por favor, tente novamente!");
			}
		} catch (Exception e) {
			erro=e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	private void tratarProcessoLegado() {
		List<String> listaSistemas = new ArrayList<String>();
		listaSistemas.add("PROHIDROS");
		if (tipoDoProcesso.equals("0")) {
			// licença (cerberus e prohidros)
			listaSistemas.add("CERBERUS");
			processoExterno.setSistema("CERBERUS");
		} else {
			// outorga (bdrh)
			listaSistemas.add("BDRH");
			processoExterno.setSistema("");
		}
		Exception erro = null;
		try {
			List<ProcessoExterno> listaProcessoExterno = processoExternoService.buscarProcessoExternoBySistemaNumero(processoExterno, listaSistemas);
			if (!Util.isNull(listaProcessoExterno) && !listaProcessoExterno.isEmpty()) {
				adiconarProcessoTramite(listaProcessoExterno.get(0).getProcesso());
			} else if(tipoDoProcesso.equals("1")){
				adiconarProcessoTramite(processoExterno.getProcesso());
			} else {
				limparProcessoTramite();
				JsfUtil.addErrorMessage("O processo informado não foi encontrado. Por favor, tente novamente!");
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	private void adiconarProcessoTramite(String numProcesso) {
		this.processoTramite = new ProcessoTramite();
		processoTramite.setNumProcessoTramite(numProcesso);
		requerimentoUnico.getRequerimento().getProcessoTramiteCollection().add(processoTramite);
		requerimentoUnico.getRequerimento().setProcessoTramiteCollection(Util.sigletonList(requerimentoUnico.getRequerimento().getProcessoTramiteCollection()));
		RequestContext.getCurrentInstance().execute("dialogProcessoTramite.hide()");
		this.processoTramiteAtivo = Boolean.TRUE;
	}

	public void excluirProcessoTramite() {
		if (this.editando) {
			requerimentoUnico.getRequerimento().getCollProcessotramiteExclusao().add(processoTramite);
		}
		requerimentoUnico.getRequerimento().getProcessoTramiteCollection().remove(processoTramite);
		this.processoTramiteAtivo = requerimentoUnico.getRequerimento().getProcessoTramiteCollection().size() > 0;
	}

	public void alterarLayoutProcessoTramite(ValueChangeEvent event) {
		String value = (String) event.getNewValue();
		if (value.equals("0")) {
			showPanelProcessosAntigos = Boolean.FALSE;
		} else {
			showPanelProcessosAntigos = Boolean.TRUE;
		}
	}

	public void alterarLayoutProcessoAntigo(ValueChangeEvent event) {
		String value = (String) event.getNewValue();
		if (value.equals("0")) {
			isLicenca = Boolean.TRUE;
		} else {
			isLicenca = Boolean.FALSE;
		}
	}

	public void limparProcessoTramite() {
		this.processoTramite = new ProcessoTramite();
		showPanelProcessosAntigos = Boolean.FALSE;
		periodoDoProcesso = "0";
		isLicenca = Boolean.TRUE;
		tipoDoProcesso = "0";
		processoExterno = new ProcessoExterno();
	}

	// Processo Tramite - end
	// Controle da Tela - begin
	public void limparEmpreendimentoProcesso() {
		requerimentoUnico.setIndUtilizaAgua(null);
		this.utilizaEmpreendimentoAgua = Boolean.FALSE;
		this.listTipoCaptacao = new ArrayList<String>();
		this.captacaoSuperficial = false;
		this.barragemCaptacaoContruida = null;
		this.autorizacaoConstrucaoBarragemCaptacao = null;
		requerimentoUnico.setIndCaptacaoBarramento(null);
		requerimentoUnico.setIndBarragemCaptConstruida(null);
		requerimentoUnico.setIndBarragemIntervConstruida(null);
		requerimentoUnico.setIndAutorizacaoConstrucaoBarragemCapt(null);
		requerimentoUnico.setIndAutorizacaoConstrucaoBarragemInterv(null);
		this.captacaoSubterranea = new Boolean(false);
		this.autorizacaoPerfuracaoPoco = false;
		requerimentoUnico.setIndPocoCaptacaoPerfurado(null);
		requerimentoUnico.setIndAutorizacaoPerfuracaoPoco(null);
		requerimentoUnico.setNumVazaoCaptacao(null);
		localizacaoGeografica = null;
		this.localizacaoGeograficaSelecionada = new LocalizacaoGeografica();
		this.verticeLoc = new DadoGeografico();
		this.datum = new SistemaCoordenada();
		this.verticeExclusaoLoc = new DadoGeografico();
		this.selectedModoCoordenada = false;
		requerimentoUnico.setIndRealizaEmissaoResiduosLiquidos(null);
		requerimentoUnico.setIndIntervencaoCorpoHidrico(null);
		this.vazaoFinalidadeAgua = Boolean.FALSE;
		this.listTipoFinalidadeUsoAgua = new ArrayList<String>();
		this.avisoConstrucaoBarragemEmpreendimentoEspecifico = Boolean.FALSE;
		this.intervencaoCorpoHidrico = Boolean.FALSE;
		this.listTipoIntervencao = new ArrayList<String>();
		this.realizaEmissaoResiduosLiquidos = Boolean.FALSE;
		this.listTipoTipoReceptor = new ArrayList<String>();
		requerimentoUnico.setIndRegularizarPerfPoco(null);
		this.regularizarPerfuracaoPoco = Boolean.FALSE;
		requerimentoUnico.setIndPocoRegularizarPerfurado(null);
		this.regularizarPerfuradoPoco = Boolean.FALSE;
		requerimentoUnico.setDtcPerfPoco(null);
		requerimentoUnico.setNumProcessoAutorizacaoPoco(null);
	}

	public void numeroRenovacaoLicencaAmbiental(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.numeroRenovacaoLicencaAmbiental = new Boolean(true);
		} else {
			this.numeroRenovacaoLicencaAmbiental = new Boolean(false);
			requerimentoUnico.setNumRevovarLicencaAmbiental("");
		}
	}

	public void numeroAlteracaoLicencaAmbiental(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.numeroAlteracaoLicencaAmbiental = new Boolean(true);
		} else {
			this.numeroAlteracaoLicencaAmbiental = new Boolean(false);
			requerimentoUnico.setNumAlterarLicencaAmbiental("");
		}
	}

	public void processoAna(ValueChangeEvent event) {
		if (this.processoTramiteInema == null) {
			if ((Boolean) event.getNewValue()) {
				this.processoAna = Boolean.TRUE;
				this.processoUsoPassivel = Boolean.TRUE;
				this.usoPassivelOutorgaEmpreendimento = Boolean.FALSE;
				this.limparEmpreendimentoProcesso();
			} else {
				this.processoAna = Boolean.FALSE;
				this.processoUsoPassivel = Boolean.FALSE;
				this.usoPassivelOutorgaEmpreendimento = Boolean.FALSE;
				requerimentoUnico.setIndProcOutorgaAtende(null);
				this.limparEmpreendimentoProcesso();
			}
		} else if (this.processoTramiteInema) {
			if ((Boolean) event.getNewValue()) {
				this.processoAna = Boolean.TRUE;
				this.processoUsoPassivel = Boolean.TRUE;
				this.usoPassivelOutorgaEmpreendimento = Boolean.FALSE;
				this.limparEmpreendimentoProcesso();
			} else {
				this.processoAna = Boolean.FALSE;
				this.processoUsoPassivel = Boolean.TRUE;
				if(requerimentoUnico.getIdeRequerimentoUnico() == null){
					this.usoPassivelOutorgaEmpreendimento = Boolean.FALSE;
					this.limparEmpreendimentoProcesso();
				}
			}
		} else {
			if ((Boolean) event.getNewValue()) {
				this.processoAna = Boolean.TRUE;
				this.processoUsoPassivel = Boolean.TRUE;
				this.usoPassivelOutorgaEmpreendimento = Boolean.FALSE;
			} else {
				this.processoAna = Boolean.FALSE;
				this.processoUsoPassivel = Boolean.FALSE;
				this.usoPassivelOutorgaEmpreendimento = Boolean.TRUE;
				requerimentoUnico.setIndProcOutorgaAtende(null);
			}
		}
		// if (this.processoTramiteInema != null) {
		// if ((Boolean) event.getNewValue() && processoTramiteInema) {
		// this.processoAna = new Boolean(true);
		// this.usoPassivelOutorgaEmpreendimento = new Boolean(false);
		// this.limparEmpreendimentoProcesso();
		// } else if (!(Boolean) event.getNewValue() && !processoTramiteInema) {
		// this.processoAna = new Boolean(false);
		// this.usoPassivelOutorgaEmpreendimento = new Boolean(true);
		// requerimentoUnico.setNumOutorgaProtocoloAbertura("");
		// } else {
		// if ((Boolean) event.getNewValue()) {
		// this.processoAna = new Boolean(true);
		// } else {
		// this.processoAna = new Boolean(false);
		// requerimentoUnico.setNumOutorgaProtocoloAbertura("");
		// }
		// this.usoPassivelOutorgaEmpreendimento = new Boolean(false);
		// if (!(usoPassivelOutorgaEmpreendimento ||
		// (requerimentoUnico.getIndProcOutorgaAtende() != null &&
		// !requerimentoUnico.getIndProcOutorgaAtende()))) {
		// this.limparEmpreendimentoProcesso();
		// }
		// }
		//
		// } else {
		// if ((Boolean) event.getNewValue()) {
		// this.processoAna = new Boolean(true);
		// } else {
		// this.processoAna = new Boolean(false);
		// }
		// }
	}

	public void origemAgua(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.origemAgua = new Boolean(true);
		} else {
			this.origemAgua = new Boolean(false);
		}
	}

	public void usoPassivelOutorgaEmpreendimento(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.usoPassivelOutorgaEmpreendimento = new Boolean(false);
			this.limparEmpreendimentoProcesso();
		} else {
			this.usoPassivelOutorgaEmpreendimento = new Boolean(true);
		}
	}

	public void enderecoContato(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.enderecoContato = new Boolean(true);
			desabilitarTudo = false;
		} else {
			this.enderecoContato = new Boolean(false);
		}
	}

	public void dadosContato(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.dadosContato = new Boolean(true);
		} else {
			this.dadosContato = new Boolean(false);
		}
	}

	public void procesoTramiteInema(ValueChangeEvent event) {
		if (this.processoAna == null) {
			if ((Boolean) event.getNewValue()) {
				this.processoTramiteInema = Boolean.TRUE;
				this.processoUsoPassivel = Boolean.TRUE;
				this.usoPassivelOutorgaEmpreendimento = Boolean.FALSE;
				this.requerimentoUnico.getRequerimento().setProcessoTramiteCollection(new ArrayList<ProcessoTramite>());
			} else {
				this.processoTramiteInema = Boolean.FALSE;
				this.processoUsoPassivel = Boolean.FALSE;
				this.usoPassivelOutorgaEmpreendimento = Boolean.FALSE;
			}
		} else if (this.processoAna) {
			if ((Boolean) event.getNewValue()) {
				this.processoTramiteInema = Boolean.TRUE;
				this.processoUsoPassivel = Boolean.TRUE;
				requerimentoUnico.setIndProcOutorgaAtende(null);
				this.usoPassivelOutorgaEmpreendimento = Boolean.FALSE;
				this.requerimentoUnico.getRequerimento().setProcessoTramiteCollection(new ArrayList<ProcessoTramite>());
				this.limparEmpreendimentoProcesso();
			} else {
				this.processoTramiteInema = Boolean.FALSE;
				this.processoUsoPassivel = Boolean.TRUE;
				requerimentoUnico.setIndProcOutorgaAtende(null);
				this.usoPassivelOutorgaEmpreendimento = Boolean.FALSE;
				this.limparEmpreendimentoProcesso();
			}
		} else {
			if ((Boolean) event.getNewValue()) {
				this.processoTramiteInema = Boolean.TRUE;
				this.processoUsoPassivel = Boolean.TRUE;
				this.usoPassivelOutorgaEmpreendimento = Boolean.FALSE;
				requerimentoUnico.setIndProcOutorgaAtende(null);
				this.requerimentoUnico.getRequerimento().setProcessoTramiteCollection(new ArrayList<ProcessoTramite>());
				this.limparEmpreendimentoProcesso();
			} else {
				this.processoTramiteInema = Boolean.FALSE;
				this.processoUsoPassivel = Boolean.FALSE;
				this.usoPassivelOutorgaEmpreendimento = Boolean.TRUE;
				requerimentoUnico.setIndProcOutorgaAtende(null);
			}
		}
	}

	public void utilizaEmpreendimentoAgua(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.utilizaEmpreendimentoAgua = new Boolean(true);
		} else {
			this.utilizaEmpreendimentoAgua = new Boolean(false);
			this.listTipoCaptacao = new ArrayList<String>();
			this.captacaoSuperficial = false;
			this.barragemCaptacaoContruida = null;
			this.autorizacaoConstrucaoBarragemCaptacao = null;
			requerimentoUnico.setIndCaptacaoBarramento(null);
			requerimentoUnico.setIndBarragemCaptConstruida(null);
			requerimentoUnico.setIndAutorizacaoConstrucaoBarragemCapt(null);
			this.captacaoSubterranea = new Boolean(false);
			this.autorizacaoPerfuracaoPoco = false;
			requerimentoUnico.setIndPocoCaptacaoPerfurado(null);
			requerimentoUnico.setIndAutorizacaoPerfuracaoPoco(null);
			requerimentoUnico.setNumVazaoCaptacao(null);
			this.localizacaoGeografica = Boolean.FALSE;
			this.localizacaoGeograficaSelecionada = new LocalizacaoGeografica();
			this.verticeLoc = new DadoGeografico();
			this.datum = new SistemaCoordenada();
			this.verticeExclusaoLoc = new DadoGeografico();
			this.selectedModoCoordenada = false;
			this.vazaoFinalidadeAgua = Boolean.FALSE;
			this.listTipoFinalidadeUsoAgua = new ArrayList<String>();
			this.avisoConstrucaoBarragemEmpreendimentoEspecifico = Boolean.FALSE;
		}
	}

	@SuppressWarnings("unchecked")
	public void checkOrigemAgua(ValueChangeEvent event) {
		if(!Util.isNullOuVazio(event)) {
			listTipoCaptacao = (List<String>) event.getNewValue();
		}


		if(!this.listTipoCaptacao.contains(new String("3")) && !this.listTipoCaptacao.contains(new String("4"))){
			requerimentoUnico.setNumVazaoCaptacao(null);
			this.localizacaoGeografica = Boolean.FALSE;
			this.localizacaoGeograficaSelecionada = new LocalizacaoGeografica();
			this.verticeLoc = new DadoGeografico();
			this.datum = new SistemaCoordenada();
			this.verticeExclusaoLoc = new DadoGeografico();
			this.selectedModoCoordenada = false;
			this.listTipoFinalidadeUsoAgua = new ArrayList<String>();
		}

		if (this.listTipoCaptacao.contains(new String("3"))) {
			this.captacaoSuperficial = new Boolean(true);
		} else {
			this.captacaoSuperficial = false;
			this.barragemCaptacaoContruida = null;
			this.autorizacaoConstrucaoBarragemCaptacao = null;
			requerimentoUnico.setIndCaptacaoBarramento(null);
			requerimentoUnico.setIndBarragemCaptConstruida(null);
			requerimentoUnico.setIndAutorizacaoConstrucaoBarragemCapt(null);

		}
		if (this.listTipoCaptacao.contains(new String("4"))) {
			this.captacaoSubterranea = new Boolean(true);
		} else {
			this.captacaoSubterranea = new Boolean(false);
			this.autorizacaoPerfuracaoPoco = false;
			requerimentoUnico.setIndPocoCaptacaoPerfurado(null);
			requerimentoUnico.setIndAutorizacaoPerfuracaoPoco(null);
		}
		this.vazaoFinalidadeAgua = this.captacaoSuperficial || this.captacaoSubterranea;
	}

	public void autorizacaoConstrucaoBarragemIntervencao(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.autorizacaoConstrucaoBarragemIntervencao = new Boolean(false);
			requerimentoUnico.setIndAutorizacaoConstrucaoBarragemInterv(null);
		} else {
			this.autorizacaoConstrucaoBarragemIntervencao = new Boolean(true);
		}
	}

	@SuppressWarnings("unchecked")
	public void checkFinalidadeAgua(ValueChangeEvent event) {
		if(!Util.isNullOuVazio(event)) {
			listTipoFinalidadeUsoAgua= (List<String>) event.getNewValue();
		}

		if (this.listTipoFinalidadeUsoAgua.contains("1") || this.listTipoFinalidadeUsoAgua.contains("5")) {
			this.avisoConstrucaoBarragemEmpreendimentoEspecifico = new Boolean(true);
		} else {
			this.avisoConstrucaoBarragemEmpreendimentoEspecifico = new Boolean(false);
		}
	}

	public void checkObjetivoLimpezaArea(AjaxBehaviorEvent event) {
		if (this.listObjetivoRequerimentoLimpezaArea.contains("5")) {
			this.outroObjetivoLimpezaArea = new Boolean(true);
		} else {
			this.outroObjetivoLimpezaArea = new Boolean(false);
		}
	}

	@SuppressWarnings("unchecked")
	public void checkIntervencaoHidrico(ValueChangeEvent event) {
		if(!Util.isNullOuVazio(event)) {
			this.listTipoIntervencao = (List<String>) event.getNewValue();
		}

		if (this.listTipoIntervencao.contains("7")) {
			this.construcaoBarragemCorpoHidrico = new Boolean(true);
		} else {
			this.construcaoBarragemCorpoHidrico = new Boolean(false);
			this.autorizacaoConstrucaoBarragemIntervencao = new Boolean(false);
			requerimentoUnico.setIndBarragemIntervConstruida(null);
			requerimentoUnico.setIndAutorizacaoConstrucaoBarragemInterv(null);
		}
	}

	public void supressaoVegetal(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.producaoVolumetricaMadeira = new Boolean(true);
			limpezaArea(construirValueChangeEvent(requerimentoUnico.getIndVolumeMaterial()));
		} else {
			this.producaoVolumetricaMadeira = new Boolean(false);
			requerimentoUnico.setIndVolumeMaterial(null);
			limpezaArea = new Boolean(false);
			this.listObjetivoRequerimentoLimpezaArea = new ArrayList<String>();
			if (this.empreendimento.getIdeEmpreendimento() != null) {
				Exception erro = null;
				try {
					requerimentoImovelData = new ListDataModel<RequerimentoImovel>(
							(List<RequerimentoImovel>) requerimentoUnicoService.recuperarImoveisEmpreendimento(this.empreendimento));
				} catch (Exception e) {
					erro =e;
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				}finally{
					if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
				}
			}
		}
	}

	public void limpezaArea(ValueChangeEvent event) {
		if ((!(Boolean) event.getNewValue())) {
			this.limpezaArea = new Boolean(true);
			for (RequerimentoImovel reqImovel : perguntaProducaoVolumetricaMadeira.getListRequerimentoImovel()) {
				if(Util.isNullOuVazio(reqImovel.getPerguntaRequerimento())){
					reqImovel.setPerguntaRequerimento(new PerguntaRequerimento());
					reqImovel.getPerguntaRequerimento().setIdeRequerimento(reqImovel.getRequerimento());
					reqImovel.getPerguntaRequerimento().setIdePergunta(perguntaProducaoVolumetricaMadeira);
				}
				reqImovel.getPerguntaRequerimento().setIndResposta(false);
				perguntaProducaoVolumetricaMadeira.setResposta(false);
			}
			hintAjuda = "Para habilitar esta pergunta é necessário remover a(s) Localização(ões) Geográfica(s) referente(s) à 'Limpeza de Área'";
		} else {
			this.limpezaArea = new Boolean(false);
			for (RequerimentoImovel reqImovel : perguntaProducaoVolumetricaMadeira.getListRequerimentoImovel()) {
				if(Util.isNullOuVazio(reqImovel.getPerguntaRequerimento())){
					reqImovel.setPerguntaRequerimento(new PerguntaRequerimento());
					reqImovel.getPerguntaRequerimento().setIdeRequerimento(reqImovel.getRequerimento());
					reqImovel.getPerguntaRequerimento().setIdePergunta(perguntaProducaoVolumetricaMadeira);
				}
				reqImovel.getPerguntaRequerimento().setIndResposta(true);
				perguntaProducaoVolumetricaMadeira.setResposta(true);
			}
			hintAjuda = "Para habilitar esta pergunta é necessário remover a(s) Localização(ões) Geográfica(s) referente(s) à 'Produção Volumetrica de Madeira'";
		}
	}
	//	JsfUtil.addWarnMessage("Para mudar o valor desta pergunta é necessário remover a localização geográfica relacionada a mesma ou suas dependências.");

	private Boolean testeMsgDesabLocRelocServFlorest = true;
	public boolean getDesabilitaIndLocRelocServFlorest(){
		if(!Util.isNullOuVazio(perguntaAprovLocRelocServFlorest) && !Util.isNullOuVazio(perguntaAprovLocRelocServFlorest.getListRequerimentoImovel())
				&& !Util.isNullOuVazio(perguntaAprovLocRelocServFlorest.getListRequerimentoImovel().get(0)) && !Util.isNullOuVazio(perguntaAprovLocRelocServFlorest.getListRequerimentoImovel().get(0).getIdeLocalizacaoGeografica())
				&& !Util.isNullOuVazio(perguntaAprovLocRelocServFlorest.getListRequerimentoImovel().get(0).getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())){
			if(testeMsgDesabLocRelocServFlorest){
				testeMsgDesabLocRelocServFlorest = false;
			}
			return true;
		}else{
			testeMsgDesabLocRelocServFlorest = true;
			return false;
		}
	}

	private Boolean testeMsgDesabRelocServFlorest = true;
	public boolean getDesabilitaIndRelocServFlorest(){
		if(!Util.isNullOuVazio(perguntaAprovRelocServidFlorest) && !Util.isNullOuVazio(perguntaAprovRelocServidFlorest.getListRequerimentoImovel())
				&& !Util.isNullOuVazio(perguntaAprovRelocServidFlorest.getListRequerimentoImovel().get(0)) && !Util.isNullOuVazio(perguntaAprovRelocServidFlorest.getListRequerimentoImovel().get(0).getIdeLocalizacaoGeografica())
				&& !Util.isNullOuVazio(perguntaAprovRelocServidFlorest.getListRequerimentoImovel().get(0).getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())){
			if(testeMsgDesabRelocServFlorest){
				testeMsgDesabRelocServFlorest = false;
			}
			return true;
		}else{
			testeMsgDesabRelocServFlorest = true;
			return false;
		}
	}

	private Boolean testeMsgDesabFuturaRelocServFlorest = true;
	public boolean getDesabilitaIndFuturaRelocServFlorest(){
		if(!Util.isNullOuVazio(perguntaAprovFututaRelocServidFlorest) && !Util.isNullOuVazio(perguntaAprovFututaRelocServidFlorest.getListRequerimentoImovel())
				&& !Util.isNullOuVazio(perguntaAprovFututaRelocServidFlorest.getListRequerimentoImovel().get(0)) && !Util.isNullOuVazio(perguntaAprovFututaRelocServidFlorest.getListRequerimentoImovel().get(0).getIdeLocalizacaoGeografica())
				&& !Util.isNullOuVazio(perguntaAprovFututaRelocServidFlorest.getListRequerimentoImovel().get(0).getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())){
			if(testeMsgDesabFuturaRelocServFlorest){
				testeMsgDesabFuturaRelocServFlorest = false;
			}
			return true;
		}else{
			testeMsgDesabFuturaRelocServFlorest = true;
			return false;
		}
	}


	public void locRelocServidFlorest(ValueChangeEvent event){
		if (((Boolean) event.getNewValue())) {
			for (RequerimentoImovel reqImovel : perguntaAprovLocRelocServFlorest.getListRequerimentoImovel()) {
				if(Util.isNullOuVazio(reqImovel.getPerguntaRequerimento())){
					reqImovel.setPerguntaRequerimento(new PerguntaRequerimento());
					reqImovel.getPerguntaRequerimento().setIdeRequerimento(reqImovel.getRequerimento());
					reqImovel.getPerguntaRequerimento().setIdePergunta(perguntaAprovLocRelocServFlorest);
				}
				reqImovel.getPerguntaRequerimento().setIndResposta(true);
			}
			perguntaAprovLocRelocServFlorest.setResposta(true);
		} else {
			for (RequerimentoImovel reqImovel : perguntaAprovLocRelocServFlorest.getListRequerimentoImovel()) {
				if(Util.isNullOuVazio(reqImovel.getPerguntaRequerimento())){
					reqImovel.setPerguntaRequerimento(new PerguntaRequerimento());
					reqImovel.getPerguntaRequerimento().setIdeRequerimento(reqImovel.getRequerimento());
					reqImovel.getPerguntaRequerimento().setIdePergunta(perguntaAprovLocRelocServFlorest);
				}
				reqImovel.getPerguntaRequerimento().setIndResposta(false);
			}
			perguntaAprovLocRelocServFlorest.setResposta(false);
		}
	}

	public void aprovRelocServidFlorest(ValueChangeEvent event){
		if (((Boolean) event.getNewValue())) {
			for (RequerimentoImovel reqImovel : perguntaAprovRelocServidFlorest.getListRequerimentoImovel()) {
				if(Util.isNullOuVazio(reqImovel.getPerguntaRequerimento())){
					reqImovel.setPerguntaRequerimento(new PerguntaRequerimento());
					reqImovel.getPerguntaRequerimento().setIdeRequerimento(reqImovel.getRequerimento());
					reqImovel.getPerguntaRequerimento().setIdePergunta(perguntaAprovRelocServidFlorest);
				}
				reqImovel.getPerguntaRequerimento().setIndResposta(true);
			}
			perguntaAprovRelocServidFlorest.setResposta(true);
			for (RequerimentoImovel reqImovel : perguntaAprovFututaRelocServidFlorest.getListRequerimentoImovel()) {
				if(Util.isNullOuVazio(reqImovel.getPerguntaRequerimento())){
					reqImovel.setPerguntaRequerimento(new PerguntaRequerimento());
					reqImovel.getPerguntaRequerimento().setIdeRequerimento(reqImovel.getRequerimento());
					reqImovel.getPerguntaRequerimento().setIdePergunta(perguntaAprovFututaRelocServidFlorest);
				}
				reqImovel.getPerguntaRequerimento().setIndResposta(true);
			}
			perguntaAprovFututaRelocServidFlorest.setResposta(true);
		} else {
			for (RequerimentoImovel reqImovel : perguntaAprovRelocServidFlorest.getListRequerimentoImovel()) {
				if(Util.isNullOuVazio(reqImovel.getPerguntaRequerimento())){
					reqImovel.setPerguntaRequerimento(new PerguntaRequerimento());
					reqImovel.getPerguntaRequerimento().setIdeRequerimento(reqImovel.getRequerimento());
					reqImovel.getPerguntaRequerimento().setIdePergunta(perguntaAprovRelocServidFlorest);
				}
				reqImovel.getPerguntaRequerimento().setIndResposta(false);
			}
			perguntaAprovRelocServidFlorest.setResposta(false);
			for (RequerimentoImovel reqImovel : perguntaAprovFututaRelocServidFlorest.getListRequerimentoImovel()) {
				if(Util.isNullOuVazio(reqImovel.getPerguntaRequerimento())){
					reqImovel.setPerguntaRequerimento(new PerguntaRequerimento());
					reqImovel.getPerguntaRequerimento().setIdeRequerimento(reqImovel.getRequerimento());
					reqImovel.getPerguntaRequerimento().setIdePergunta(perguntaAprovFututaRelocServidFlorest);
				}
				reqImovel.getPerguntaRequerimento().setIndResposta(false);
			}
			perguntaAprovFututaRelocServidFlorest.setResposta(false);
		}
	}

	private Boolean testeMsgDesabVolMadeira = true;
	public boolean getDesabilitaIndVolumeMadeira(){
		if(!Util.isNullOuVazio(perguntaProducaoVolumetricaMadeira) && !Util.isNullOuVazio(perguntaProducaoVolumetricaMadeira.getListRequerimentoImovel())
				&& !Util.isNullOuVazio(perguntaProducaoVolumetricaMadeira.getListRequerimentoImovel().get(0)) && !Util.isNullOuVazio(perguntaProducaoVolumetricaMadeira.getListRequerimentoImovel().get(0).getIdeLocalizacaoGeografica())
				&& !Util.isNullOuVazio(perguntaProducaoVolumetricaMadeira.getListRequerimentoImovel().get(0).getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())){
			if(testeMsgDesabVolMadeira){
				testeMsgDesabVolMadeira = false;
			}
			return true;
		}else if (!Util.isNullOuVazio(perguntaProducaoVolumetricaMadeira) && !Util.isNullOuVazio(perguntaProducaoVolumetricaMadeira.getListRequerimentoImovel())
				&& !Util.isNullOuVazio(perguntaProducaoVolumetricaMadeira.getListRequerimentoImovel().get(0)) && getCollObjetivoRequerimentoLimpezaAreaIsNotNull()){
			if(testeMsgDesabVolMadeira){
				testeMsgDesabVolMadeira = false;
			}
			return true;
		}else{
			testeMsgDesabVolMadeira = true;
			return false;
		}
	}

	public void queimaControlada(ValueChangeEvent event) {
		if (((Boolean) event.getNewValue())) {
			this.requerimentoUnico.setIndRealizarQueimaControlada(new Boolean(true));
			for (RequerimentoImovel reqImovel : perguntaRealizarQueimaControlada.getListRequerimentoImovel()) {
				if(Util.isNullOuVazio(reqImovel.getPerguntaRequerimento())){
					reqImovel.setPerguntaRequerimento(new PerguntaRequerimento());
					reqImovel.getPerguntaRequerimento().setIdeRequerimento(reqImovel.getRequerimento());
					reqImovel.getPerguntaRequerimento().setIdePergunta(perguntaRealizarQueimaControlada);
				}
				reqImovel.getPerguntaRequerimento().setIndResposta(true);
			}
			perguntaRealizarQueimaControlada.setResposta(true);
		} else {
			this.requerimentoUnico.setIndRealizarQueimaControlada(new Boolean(false));
			for (RequerimentoImovel reqImovel : perguntaRealizarQueimaControlada.getListRequerimentoImovel()) {
				if(Util.isNullOuVazio(reqImovel.getPerguntaRequerimento())){
					reqImovel.setPerguntaRequerimento(new PerguntaRequerimento());
					reqImovel.getPerguntaRequerimento().setIdeRequerimento(reqImovel.getRequerimento());
					reqImovel.getPerguntaRequerimento().setIdePergunta(perguntaRealizarQueimaControlada);
				}
				reqImovel.getPerguntaRequerimento().setIndResposta(false);
			}
			perguntaRealizarQueimaControlada.setResposta(false);
		}
	}

	private Boolean testeMsgDesabQueimaControl = true;
	public boolean getDesabilitaIndQueimaControl(){
		if(!Util.isNullOuVazio(perguntaRealizarQueimaControlada) && !Util.isNullOuVazio(perguntaRealizarQueimaControlada.getListRequerimentoImovel())
				&& !Util.isNullOuVazio(perguntaRealizarQueimaControlada.getListRequerimentoImovel().get(0)) && !Util.isNullOuVazio(perguntaRealizarQueimaControlada.getListRequerimentoImovel().get(0).getIdeLocalizacaoGeografica())
				&& !Util.isNullOuVazio(perguntaRealizarQueimaControlada.getListRequerimentoImovel().get(0).getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica()) && requerimentoUnico.getIndRealizarQueimaControlada()){
			if(testeMsgDesabQueimaControl){
				testeMsgDesabQueimaControl = false;
			}
			return true;
		}else{
			testeMsgDesabQueimaControl = true;
			return false;
		}
	}

	public void aprovaPlanoManejo(ValueChangeEvent event) {
		if (((Boolean) event.getNewValue())) {
			this.requerimentoUnico.setIndPlanoManejo(new Boolean(true));
			for (RequerimentoImovel reqImovel : perguntaAprovPlanoManejoFlorestal.getListRequerimentoImovel()) {
				if(Util.isNullOuVazio(reqImovel.getPerguntaRequerimento())){
					reqImovel.setPerguntaRequerimento(new PerguntaRequerimento());
					reqImovel.getPerguntaRequerimento().setIdeRequerimento(reqImovel.getRequerimento());
					reqImovel.getPerguntaRequerimento().setIdePergunta(perguntaAprovPlanoManejoFlorestal);
				}
				reqImovel.getPerguntaRequerimento().setIndResposta(true);
			}
			perguntaAprovPlanoManejoFlorestal.setResposta(true);
		} else {
			this.requerimentoUnico.setIndPlanoManejo(new Boolean(false));
			for (RequerimentoImovel reqImovel : perguntaAprovPlanoManejoFlorestal.getListRequerimentoImovel()) {
				if(Util.isNullOuVazio(reqImovel.getPerguntaRequerimento())){
					reqImovel.setPerguntaRequerimento(new PerguntaRequerimento());
					reqImovel.getPerguntaRequerimento().setIdeRequerimento(reqImovel.getRequerimento());
					reqImovel.getPerguntaRequerimento().setIdePergunta(perguntaAprovPlanoManejoFlorestal);
				}
				reqImovel.getPerguntaRequerimento().setIndResposta(false);
			}
			perguntaAprovPlanoManejoFlorestal.setResposta(false);
		}
	}

	private Boolean testeMsgDesabPlanoManejo = true;
	public boolean getDesabilitaIndPlanoManejo(){
		if(!Util.isNullOuVazio(perguntaAprovPlanoManejoFlorestal) && !Util.isNullOuVazio(perguntaAprovPlanoManejoFlorestal.getListRequerimentoImovel())
				&& !Util.isNullOuVazio(perguntaAprovPlanoManejoFlorestal.getListRequerimentoImovel().get(0)) && !Util.isNullOuVazio(perguntaAprovPlanoManejoFlorestal.getListRequerimentoImovel().get(0).getIdeLocalizacaoGeografica())
				&& !Util.isNullOuVazio(perguntaAprovPlanoManejoFlorestal.getListRequerimentoImovel().get(0).getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())){
			if(testeMsgDesabPlanoManejo){
				testeMsgDesabPlanoManejo = false;
			}
			return true;
		}else{
			testeMsgDesabPlanoManejo = true;
			return false;
		}
	}

	public void autorizacaoPerfuracaoPoco(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.autorizacaoPerfuracaoPoco = new Boolean(false);
			requerimentoUnico.setIndAutorizacaoPerfuracaoPoco(null);
		} else {
			if(Util.isNullOuVazio(requerimentoUnico) || Util.isNullOuVazio(requerimentoUnico.getIdeRequerimentoUnico())){
				JsfUtil.addWarnMessage("Você precisa solicitar o Requerimento para perfuração de poço.");
			}
			this.autorizacaoPerfuracaoPoco = new Boolean(true);
		}
	}

	public void cortarFlorestaProducaoRegistrada(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.cortarFlorestaProducaoRegistrada = new Boolean(true);
			this.prazoValidade = new Boolean(false);
			this.numeroPortariaAutorizacaoSupressaoVegetacao = null;
			requerimentoUnico.setIndProrValidAsv(Boolean.FALSE);
			requerimentoUnico.setNumPortAsv("");
			this.numeroRegistroCorteFlorestalProducao = null;
			requerimentoUnico.setIndProrValidRcfp(Boolean.FALSE);
			requerimentoUnico.setNumPortRcfp("");
			this.autorizacaoCorteFlorestaProducao = null;
			requerimentoUnico.setIndProrValidAcfp(Boolean.FALSE);
			requerimentoUnico.setNumPortAcfp("");
			this.numeroLicencaAmbiental = null;
			if(!Util.isNullOuVazio(perguntaCortarFlorestProducRegist)){
				for (RequerimentoImovel reqImovel : perguntaCortarFlorestProducRegist.getListRequerimentoImovel()) {
					if(Util.isNullOuVazio(reqImovel.getPerguntaRequerimento())){
						reqImovel.setPerguntaRequerimento(new PerguntaRequerimento());
						reqImovel.getPerguntaRequerimento().setIdeRequerimento(reqImovel.getRequerimento());
						reqImovel.getPerguntaRequerimento().setIdePergunta(perguntaCortarFlorestProducRegist);
					}
					reqImovel.getPerguntaRequerimento().setIndResposta(true);
				}
				perguntaCortarFlorestProducRegist.setResposta(true);
			}
		} else {
			this.cortarFlorestaProducaoRegistrada = new Boolean(false);
			this.prazoValidade = new Boolean(true);
			requerimentoUnico.setNumPortCertRegFlorProd("");
			if(!Util.isNullOuVazio(perguntaCortarFlorestProducRegist)) {
				for (RequerimentoImovel reqImovel : perguntaCortarFlorestProducRegist.getListRequerimentoImovel()) {
					if(Util.isNullOuVazio(reqImovel.getPerguntaRequerimento())){
						reqImovel.setPerguntaRequerimento(new PerguntaRequerimento());
						reqImovel.getPerguntaRequerimento().setIdeRequerimento(reqImovel.getRequerimento());
						reqImovel.getPerguntaRequerimento().setIdePergunta(perguntaCortarFlorestProducRegist);
					}
					reqImovel.getPerguntaRequerimento().setIndResposta(false);
				}
			}
			perguntaCortarFlorestProducRegist.setResposta(false);
		}
	}

	private Boolean testeMsgDesabCortFlorestProdReg = true;
	public boolean getDesabilitaIndCortFlorestProdReg(){
		if(!Util.isNullOuVazio(perguntaCortarFlorestProducRegist) && !Util.isNullOuVazio(perguntaCortarFlorestProducRegist.getListRequerimentoImovel())
				&& !Util.isNullOuVazio(perguntaCortarFlorestProducRegist.getListRequerimentoImovel().get(0)) && !Util.isNullOuVazio(perguntaCortarFlorestProducRegist.getListRequerimentoImovel().get(0).getIdeLocalizacaoGeografica())
				&& !Util.isNullOuVazio(perguntaCortarFlorestProducRegist.getListRequerimentoImovel().get(0).getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())){
			if(testeMsgDesabCortFlorestProdReg){
				testeMsgDesabCortFlorestProdReg = false;
			}
			return true;
		}else{
			testeMsgDesabCortFlorestProdReg = true;
			return false;
		}
	}

	public void florestaVinculada(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.florestaVinculadaReposicaoFlorestal = new Boolean(true);
		} else {
			this.florestaVinculadaReposicaoFlorestal = new Boolean(false);
			requerimentoUnico.setNumPortEmissCredRepFlor("");
		}
	}

	public void numeroProcessoAprovacaoPlanomanejoFlorestal(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.numeroProcessoAprovacaoPlanomanejoFlorestal = new Boolean(true);
			for (RequerimentoImovel reqImovel : perguntaAprovExecPlanManejFlorest.getListRequerimentoImovel()) {
				if(Util.isNullOuVazio(reqImovel.getPerguntaRequerimento())){
					reqImovel.setPerguntaRequerimento(new PerguntaRequerimento());
					reqImovel.getPerguntaRequerimento().setIdeRequerimento(reqImovel.getRequerimento());
					reqImovel.getPerguntaRequerimento().setIdePergunta(perguntaAprovExecPlanManejFlorest);
				}
				reqImovel.getPerguntaRequerimento().setIndResposta(true);
				perguntaAprovExecPlanManejFlorest.setResposta(true);
			}
			perguntaAprovExecPlanManejFlorest.setResposta(true);
		} else {
			this.numeroProcessoAprovacaoPlanomanejoFlorestal = new Boolean(false);
			requerimentoUnico.setNumProcessoAprovExecPlanoMnjFlor("");
			if(!Util.isNullOuVazio(perguntaAprovExecPlanManejFlorest)){
				for (RequerimentoImovel reqImovel : perguntaAprovExecPlanManejFlorest.getListRequerimentoImovel()) {
					if(Util.isNullOuVazio(reqImovel.getPerguntaRequerimento())){
						reqImovel.setPerguntaRequerimento(new PerguntaRequerimento());
						reqImovel.getPerguntaRequerimento().setIdeRequerimento(reqImovel.getRequerimento());
						reqImovel.getPerguntaRequerimento().setIdePergunta(perguntaAprovExecPlanManejFlorest);
					}
					reqImovel.getPerguntaRequerimento().setIndResposta(false);
					perguntaAprovExecPlanManejFlorest.setResposta(false);
				}
			}
			perguntaAprovExecPlanManejFlorest.setResposta(false);
		}
	}

	private Boolean testeMsgDesabExecPlanManejFlor = true;
	public boolean getDesabilitaIndExecPlanManejFlor(){
		if(!Util.isNullOuVazio(perguntaAprovExecPlanManejFlorest) && !Util.isNullOuVazio(perguntaAprovExecPlanManejFlorest.getListRequerimentoImovel())
				&& !Util.isNullOuVazio(perguntaAprovExecPlanManejFlorest.getListRequerimentoImovel().get(0)) && !Util.isNullOuVazio(perguntaAprovExecPlanManejFlorest.getListRequerimentoImovel().get(0).getIdeLocalizacaoGeografica())
				&& !Util.isNullOuVazio(perguntaAprovExecPlanManejFlorest.getListRequerimentoImovel().get(0).getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())){
			if(testeMsgDesabExecPlanManejFlor){
				testeMsgDesabExecPlanManejFlor = false;
			}
			return true;
		}else{
			testeMsgDesabExecPlanManejFlor = true;
			return false;
		}
	}

	public void numeroPortariaAutorizacaoSupressaoVegetacao(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.numeroPortariaAutorizacaoSupressaoVegetacao = new Boolean(true);
		} else {
			this.numeroPortariaAutorizacaoSupressaoVegetacao = new Boolean(false);
			requerimentoUnico.setNumPortAsv("");
		}
	}

	public void numeroRegistroCorteFlorestalProducao(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.numeroRegistroCorteFlorestalProducao = new Boolean(true);
		} else {
			this.numeroRegistroCorteFlorestalProducao = new Boolean(false);
			requerimentoUnico.setNumPortRcfp("");
		}
	}

	public void autorizacaoCorteFlorestaProducao(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.autorizacaoCorteFlorestaProducao = new Boolean(true);
		} else {
			this.autorizacaoCorteFlorestaProducao = new Boolean(false);
			requerimentoUnico.setNumPortAcfp("");
		}
	}

	public void numeroLicencaAmbiental(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.numeroLicencaAmbiental = new Boolean(true);
		} else {
			this.numeroLicencaAmbiental = new Boolean(false);
			requerimentoUnico.setNumPortLa("");
		}
	}

	public void origemMaterialLenhoso(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.origemMaterialLenhoso = new Boolean(true);
		} else {
			this.origemMaterialLenhoso = new Boolean(false);
			requerimentoUnico.setIndOrigMatLenAsv(null);
			this.numeroPortariaSupressaoVegetacaoNativa = false;
			requerimentoUnico.setNumOrigMatLenAsv("");
			requerimentoUnico.setIndOrigMatLenRcfp(null);
			this.numeroCertificadoRegistroCorteFlorestaProducao = false;
			requerimentoUnico.setNumOrigMatLenRcfp("");
			requerimentoUnico.setIndOrigMatLenAcfp(null);
			this.numeroPortariaAutorizacaoCorteFlorestaProducao = false;
			requerimentoUnico.setNumOrigMatLenAcfp("");
		}
	}

	public void numeroPortariaSupressaoVegetacaoNativa(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.numeroPortariaSupressaoVegetacaoNativa = new Boolean(true);
		} else {
			this.numeroPortariaSupressaoVegetacaoNativa = new Boolean(false);
		}
	}

	public void numeroCertificadoRegistroCorteFlorestaProducao(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.numeroCertificadoRegistroCorteFlorestaProducao = new Boolean(true);
		} else {
			this.numeroCertificadoRegistroCorteFlorestaProducao = new Boolean(false);
		}
	}

	public void numeroPortariaAutorizacaoCorteFlorestaProducao(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.numeroPortariaAutorizacaoCorteFlorestaProducao = new Boolean(true);
		} else {
			this.numeroPortariaAutorizacaoCorteFlorestaProducao = new Boolean(false);
		}
	}

	public void regularizarPerfuracaoPoco(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.regularizarPerfuracaoPoco = new Boolean(true);
		} else {
			this.regularizarPerfuracaoPoco = new Boolean(false);
			this.regularizarPerfuradoPoco = new Boolean(false);
			requerimentoUnico.setIndPocoRegularizarPerfurado(null);
			requerimentoUnico.setDtcPerfPoco(null);
			requerimentoUnico.setNumProcessoAutorizacaoPoco("");
		}
	}

	public void regularizarPerfuradoPoco(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.regularizarPerfuradoPoco = new Boolean(true);
		} else {
			this.regularizarPerfuradoPoco = new Boolean(false);
			requerimentoUnico.setDtcPerfPoco(null);
			requerimentoUnico.setNumProcessoAutorizacaoPoco("");
		}
	}

	public void autorizacaoConstrucaoBarragemCaptacao(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.autorizacaoConstrucaoBarragemCaptacao = new Boolean(false);
			requerimentoUnico.setIndAutorizacaoConstrucaoBarragemCapt(null);
		} else {
			this.autorizacaoConstrucaoBarragemCaptacao = new Boolean(true);
		}
	}

	public void realizaEmissaoResiduosLiquidos(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.realizaEmissaoResiduosLiquidos = new Boolean(true);
		} else {
			this.realizaEmissaoResiduosLiquidos = new Boolean(false);
			this.listTipoTipoReceptor = new ArrayList<String>();
		}
	}

	public void intervencaoCorpoHidrico(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.intervencaoCorpoHidrico = new Boolean(true);
		} else {
			this.intervencaoCorpoHidrico = new Boolean(false);
			this.listTipoIntervencao = new ArrayList<String>();
		}
	}

	public void barragemCaptacaoContruida(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.barragemCaptacaoContruida = new Boolean(true);
		} else {
			this.barragemCaptacaoContruida = new Boolean(false);
			requerimentoUnico.setIndBarragemCaptConstruida(null);
			this.autorizacaoConstrucaoBarragemCaptacao = Boolean.FALSE;
			requerimentoUnico.setIndAutorizacaoConstrucaoBarragemCapt(null);
		}
	}

	public void avisoConstrucaoBarragemEmpreendimentoEspecifico(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.avisoConstrucaoBarragemEmpreendimentoEspecifico = new Boolean(true);
		} else {
			this.avisoConstrucaoBarragemEmpreendimentoEspecifico = new Boolean(false);
		}
	}

	public void suprimentoSustentavel(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.suprimentoSustentavel = new Boolean(true);
		} else {
			this.suprimentoSustentavel = new Boolean(false);
			requerimentoUnico.setNumProcLicAmbiental("");
		}
	}

	public void vincularFlorestaProducao(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.vincularFlorestaProducao = new Boolean(true);
		} else {
			this.vincularFlorestaProducao = new Boolean(false);
			requerimentoUnico.setNumPortariaLicSilvicultura("");
		}
	}

	public void transferirCreditoReposicaoFlorestal(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.transferenciaCreditoReposicaoFlorestal = new Boolean(true);
		} else {
			this.transferenciaCreditoReposicaoFlorestal = new Boolean(false);
			requerimentoUnico.setNumProcEmissCred("");
		}
	}

	public void verirficarVazaoCaptacao(AjaxBehaviorEvent event) {
		if(!Util.isNullOuVazio(requerimentoUnico.getNumVazaoCaptacao()) && requerimentoUnico.getNumVazaoCaptacao().doubleValue() == 0){
			JsfUtil.addWarnMessage("O valor da vazão de captação deve ser maior que zero.");
		}else{
			if (!Util.isNullOuVazio(requerimentoUnico.getNumVazaoCaptacao())) {
				this.localizacaoGeografica = new Boolean(true);
				obterClassificacoes();
				if (!Util.isNull(this.localizacaoGeograficaSelecionada)
						&& Util.isNullOuVazio(this.localizacaoGeograficaSelecionada.getDadoGeograficoCollection())) {
					this.localizacaoGeograficaSelecionada.setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
				}

				if(Util.isNull(this.verticeLoc)){
					this.verticeLoc = new DadoGeografico();
				}

			} else {
				this.localizacaoGeografica = new Boolean(false);
				this.localizacaoGeograficaSelecionada = new LocalizacaoGeografica();
				this.localizacaoGeograficaSelecionada.setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
				this.verticeLoc = new DadoGeografico();
				this.datum = new SistemaCoordenada();
				this.verticeExclusaoLoc = new DadoGeografico();
				this.selectedModoCoordenada = false;
			}
		}
	}

	public void modoCoordenada(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.selectedModoCoordenada = true;
		} else {
			this.selectedModoCoordenada = false;
		}
	}

	public void atoFlorestal(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.atoFlorestal = true;
		} else {
			this.atoFlorestal = false;
		}
	}

	public void atoFauna(ValueChangeEvent event) {
		if ((Boolean) event.getNewValue()) {
			this.atoFauna = true;
		} else {
			this.atoFauna = false;
			requerimentoUnico.setIndManejoFauna(null);
			requerimentoUnico.setIndEstudoFauna(null);
		}
	}

	// Controle da Tela - end
	// Controle PopUp Localiza��o Geogr�fica - Begin
	public void calculaFracaoGrauLatitudeLoc(ActionEvent event) {
		try {
			this.fracaoGrauLatitudeLoc = PostgisUtil.calculaFracaoGrauLatitude(grausLatitudeLoc, minutosLatitudeLoc, segundosLatitudeLoc).toString();
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void calculaFracaoGrauLongitudeLoc(ActionEvent event) {
		try {
			this.fracaoGrauLongitudeLoc = PostgisUtil.calculaFracaoGrauLongitude(grausLongitudeLoc, minutosLongitudeLoc, segundosLongitudeLoc).toString();
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	// Controle PopUp Localiza��o Geogr�fica - End
	// Controle PopUp Requerimento Im�vel - Begin
	public void calculaFracaoGrauLatitude(ActionEvent event) {
		try {
			NumberFormat formatter = new DecimalFormat("#0.000000");
			this.fracaoGrauLatitude = formatter.format(PostgisUtil.calculaFracaoGrauLongitude(grausLatitude, minutosLatitude, segundosLatitude));
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void calculaFracaoGrauLongitude(ActionEvent event) {
		try {
			NumberFormat formatter = new DecimalFormat("#0.000000");
			this.fracaoGrauLongitude = formatter.format(PostgisUtil.calculaFracaoGrauLongitude(grausLongitude, minutosLongitude, segundosLongitude));
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	// Controle PopUp Requerimento Im�vel - End
	public void iniciarEnquadramento(ActionEvent action) {
		Exception erro = null;
		try {
			RequerimentoUnico buscarRequerimentoUnico = requerimentoUnicoService.buscarRequerimentoUnico(requerimentoUnico);
			Enquadramento enquadramento = enquadramentoService.buscarUltimoEnquadramentoRequerimento(buscarRequerimentoUnico.getRequerimento());
			if (enquadramento == null || !Util.isNullOuVazio(enquadramento.getDscJustificativa())) {
				enquadramento = new Enquadramento();
			}
			enquadramento.setIdeRequerimentoUnico(buscarRequerimentoUnico);
			Pessoa p = new Pessoa(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());
			enquadramento.setIdePessoa(p);
			buscarRequerimentoUnico.setEnquadramento(new ArrayList<Enquadramento>());
			buscarRequerimentoUnico.getEnquadramento().add(enquadramento);
			requerimentoUnico.setEnquadramento(buscarRequerimentoUnico.getEnquadramento());
			enquadramentoAtoAmbientalService.iniciarEnquadramento(enquadramento);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage(e, "Erro ao tentar iniciar o enquadramento");
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void carregarInformacoesEmpreendimento() throws Exception {
		this.dadosContato = false;
		Exception erro =null;
		try {
			if (this.empreendimento.getIdeEmpreendimento().equals(0)) {
				this.desabilitarTudo = Boolean.TRUE;
			} else {
				this.desabilitarTudo = Boolean.FALSE;
				empreendimento = empreendimentoService.carregarById(empreendimento.getIdeEmpreendimento());
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		this.requerimentoUnicoPagoExistente = false;
		this.limparQuestionario();
		requerimentoUnico = new RequerimentoUnico();
		requerimentoUnico.setRequerimento(new Requerimento());
		if (pessoa != null) {
			List<Telefone> listaTel = null;
		
			try {
				listaTel = telefoneService.buscarTelefonesPorPessoa(pessoa);
			} catch (Exception e) {
				erro =e;
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
			if (!Util.isNullOuVazio(pessoa.getPessoaFisica()) && !Util.isNullOuVazio(pessoa.getPessoaFisica().getNomPessoa())) {
				requerimentoUnico.getRequerimento().setNomContato(pessoa.getPessoaFisica().getNomPessoa());
				// if
				// (!Util.isNullOuVazio(pessoa.getPessoaFisica().getPessoa().getDesEmail()))
				//					requerimentoUnico.getRequerimento().setDesEmail(pessoa.getPessoaFisica().getPessoa().getDesEmail());
			} else if (!Util.isNullOuVazio(pessoa.getPessoaJuridica())) {
				requerimentoUnico.getRequerimento().setNomContato(pessoa.getPessoaJuridica().getNomRazaoSocial());
				//				requerimentoUnico.getRequerimento().setDesEmail(this.empreendimento.getDesEmail());
			}
			if (!listaTel.isEmpty()) {
				
				try {
					if (listaTel.size() > 1) {
						requerimentoUnico.getRequerimento().setNumTelefone(getTelefoneParaRequerimento(listaTel).getNumTelefone());
					} else {
						requerimentoUnico.getRequerimento().setNumTelefone(listaTel.get(0).getNumTelefone());
					}
				} catch (Exception e) {
					erro =e;
					requerimentoUnico.getRequerimento().setNumTelefone(listaTel.get(0).getNumTelefone());
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				}finally{
					if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
				}
			}
		}
		requerimentoUnico.getRequerimento().setProcessoTramiteCollection(new ArrayList<ProcessoTramite>());
		requerimentoUnico.getRequerimento().setIdeEnderecoContato(new Endereco());
		requerimentoUnico.getRequerimento().getIdeEnderecoContato().setIdeLogradouro(new Logradouro());
		requerimentoUnico.setIdeFaseEmpreendimento(new FaseEmpreendimento());

		this.collEmpreendimento = new ArrayList<SelectItem>();
		this.carregarEmpreendimento();
		this.collObjetivoRequerimentoLimpezaArea = new ArrayList<ObjetivoRequerimentoLimpezaArea>();
		this.collObjetivoLimpezaArea = new ArrayList<ObjetivoLimpezaArea>();
		carregarObjetivoRequerimentoLimpezaArea();
		this.carregarObjetivoLimpezaArea();
		this.collTipoCaptacao = new ArrayList<SelectItem>();
		this.carregarTipoCapitacao();
		this.collTipoFinalidadeUsoAgua = new ArrayList<SelectItem>();
		this.carregarTipoFinalidadeUsoAgua();
		this.collTipoIntervencaoData = new ArrayList<SelectItem>();
		this.carregarTipoIntervencao();
		this.collTipoTipoReceptor = new ArrayList<SelectItem>();
		this.carregarTipoReceptor();
		this.enderecoContato = new Boolean(false);
		listTipoIntervencao = new ArrayList<String>();
		listTipoFinalidadeUsoAgua = new ArrayList<String>();
		listTipoTipoReceptor = new ArrayList<String>();
		listTipoCaptacao = new ArrayList<String>();
		listObjetivoRequerimentoLimpezaArea = new ArrayList<String>();

		Collection<RequerimentoTipologia> listaRequerimentoTipologia = requerimentoUnicoService.listarRequerimentoTipologia(this.empreendimento);
		this.verificarTipologias();

		List<RequerimentoTipologia> lista = new ArrayList<RequerimentoTipologia>();
		for (RequerimentoTipologia requerimentoTipologia : listaRequerimentoTipologia) {
			if(!Util.isNull(requerimentoTipologia.getIdeUnidadeMedidaTipologiaGrupo())
					&& !Util.isNull(requerimentoTipologia.getIdeUnidadeMedidaTipologiaGrupo().getIdeUnidadeMedidaTipologiaGrupo())){
				lista.add(requerimentoTipologia);
			}
		}

		requerimentoTipologiaData = new ListDataModel<RequerimentoTipologia>(lista);

		requerimentoImovelData = new ListDataModel<RequerimentoImovel>(
				(List<RequerimentoImovel>) requerimentoUnicoService.recuperarImoveisEmpreendimento(this.empreendimento));
		int pog = 0;
		for (RequerimentoTipologia reqTip : requerimentoTipologiaData) {
			pog++;
			reqTip.setIdeRequerimentoTipologia(pog);
		}
		imovelRural = Boolean.FALSE;
		if (requerimentoImovelData.isRowAvailable()) {
			imovelRural = Boolean.TRUE;
		}
		processoTramite = new ProcessoTramite();

		if ((!Util.isNullOuVazio(this.empreendimento) && (!Util.isNullOuVazio(this.empreendimento.getIdeEmpreendimento())))
				&& requerimentoUnicoPago == null) {
			carregarProcessosEmpreendimentos();
		}
		requerimentoUnico.getRequerimento().setDesEmail(this.empreendimento.getDesEmail());
	}

	private void carregarProcessosEmpreendimentos() {
		Exception erro = null;
		try {
			List<Processo> listaProcessos = requerimentoUnicoService.carregarProcessosEmpreendimentos(this.empreendimento);
			for (Processo p : listaProcessos) {
				requerimentoUnico.getRequerimento().getProcessoTramiteCollection()
				.add(new ProcessoTramite(p.getNumProcesso(), requerimentoUnico.getRequerimento()));
			}
			this.processoTramiteAtivo = listaProcessos.size() > 0;
			if (!Util.isNullOuVazio(listaProcessos)) {
				this.procesoTramiteInema(construirValueChangeEvent(true));
				this.requerimentoUnico.setIndTramiteInema(true);
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void utilizarDadosRequerimentoAnterior() {
		RequerimentoUnico requerimentoUnico = requerimentoUnicoPago;
		carregarEditar(requerimentoUnico);
		requerimentoUnico.setIndSupressaoVegetacao(false);
		supressaoVegetal(construirValueChangeEvent(requerimentoUnico.getIndSupressaoVegetacao()));
		requerimentoUnico = processarRequerimentoUnicoExistenteParaInclusao(requerimentoUnico);
		requerimentoUnico.setIdeRequerimentoUnico(null);
		requerimentoUnico.getRequerimento().setIdeRequerimento(null);
		if (this.processoTramiteInema) {
			for (ProcessoTramite processoTramite : this.requerimentoUnico.getRequerimento().getProcessoTramiteCollection()) {
				processoTramite.setIdeProcessoTramite(null);
				processoTramite.setIdeRequerimento(requerimentoUnico.getRequerimento());
			}
			requerimentoUnico.getRequerimento().setProcessoTramiteCollection(this.requerimentoUnico.getRequerimento().getProcessoTramiteCollection());
		} else {
			requerimentoUnico.getRequerimento().setProcessoTramiteCollection(new ArrayList<ProcessoTramite>());
		}
		this.requerimentoUnico = requerimentoUnico;
		// this.requerimentoUnicoPagoExistente = true;
	}

	public RequerimentoUnico processarRequerimentoUnicoExistenteParaInclusao(RequerimentoUnico requerimentoUnico) {
		RequerimentoUnico requerimentoUnicoProcessar = new RequerimentoUnico();
		requerimentoUnicoProcessar.setIndRevovarLicencaAmbiental(requerimentoUnico.getIndRevovarLicencaAmbiental());
		requerimentoUnicoProcessar.setIndAlterarLicencaAmbiental(requerimentoUnico.getIndAlterarLicencaAmbiental());
		requerimentoUnicoProcessar.setIndTramiteInema(requerimentoUnico.getIndTramiteInema());
		requerimentoUnicoProcessar.setIndTramiteAna(requerimentoUnico.getIndTramiteAna());
		requerimentoUnicoProcessar.setIndProcOutorgaAtende(requerimentoUnico.getIndProcOutorgaAtende());
		requerimentoUnicoProcessar.setIndSupressaoVegetacao(requerimentoUnico.getIndSupressaoVegetacao());
		requerimentoUnicoProcessar.setIndPlanoManejo(requerimentoUnico.getIndPlanoManejo());
		requerimentoUnicoProcessar.setIndServidaoFlorestal(requerimentoUnico.getIndServidaoFlorestal());
		requerimentoUnicoProcessar.setNumOutorgaProtocoloAbertura(requerimentoUnico.getNumOutorgaProtocoloAbertura());
		requerimentoUnicoProcessar.setIndUtilizaAgua(requerimentoUnico.getIndUtilizaAgua());
		requerimentoUnicoProcessar.setIndRealizaEmissaoResiduosLiquidos(requerimentoUnico.getIndRealizaEmissaoResiduosLiquidos());
		requerimentoUnicoProcessar.setIndIntervencaoCorpoHidrico(requerimentoUnico.getIndIntervencaoCorpoHidrico());
		requerimentoUnicoProcessar.setIndRegularizarPerfPoco(requerimentoUnico.getIndRegularizarPerfPoco());
		requerimentoUnicoProcessar.setIndPocoRegularizarPerfurado(requerimentoUnico.getIndPocoRegularizarPerfurado());
		requerimentoUnicoProcessar.setIndVolumeMaterial(requerimentoUnico.getIndVolumeMaterial());
		requerimentoUnicoProcessar.setIndCaptacaoBarramento(requerimentoUnico.getIndCaptacaoBarramento());
		requerimentoUnicoProcessar.setIndBarragemCaptConstruida(requerimentoUnico.getIndBarragemCaptConstruida());
		requerimentoUnicoProcessar.setIndAutorizacaoConstrucaoBarragemCapt(requerimentoUnico.getIndAutorizacaoConstrucaoBarragemCapt());
		requerimentoUnicoProcessar.setIndPocoCaptacaoPerfurado(requerimentoUnico.getIndPocoCaptacaoPerfurado());
		requerimentoUnicoProcessar.setIndAutorizacaoPerfuracaoPoco(requerimentoUnico.getIndAutorizacaoPerfuracaoPoco());
		requerimentoUnicoProcessar.setNumVazaoCaptacao(requerimentoUnico.getNumVazaoCaptacao());
		requerimentoUnicoProcessar.setIdePorte(requerimentoUnico.getIdePorte());
		requerimentoUnicoProcessar.setIdeFaseEmpreendimento(requerimentoUnico.getIdeFaseEmpreendimento());
		if (requerimentoUnico.getIdeLocalizacaoGeografica() != null) {
			LocalizacaoGeografica localizacaoGeografica = new LocalizacaoGeografica();
			localizacaoGeografica.setIdeSistemaCoordenada(requerimentoUnico.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada());
			localizacaoGeografica.setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
			try {
				for (DadoGeografico vertice : requerimentoUnico.getIdeLocalizacaoGeografica().getDadoGeograficoCollection()) {
					vertice.setIdeDadoGeografico(null);
					vertice.setIdeLocalizacaoGeografica(null);
					localizacaoGeografica.getDadoGeograficoCollection().add(vertice);
				}
			} catch (Exception e) {
				try {
					requerimentoUnico.setIdeLocalizacaoGeografica((localizacaoGeograficaService.carregar(requerimentoUnico.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())));
					for (DadoGeografico vertice : requerimentoUnico.getIdeLocalizacaoGeografica().getDadoGeograficoCollection()) {
						vertice.setIdeDadoGeografico(null);
						vertice.setIdeLocalizacaoGeografica(null);
						localizacaoGeografica.getDadoGeograficoCollection().add(vertice);
					}
				} catch (Exception e1) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e1);
				}
			}
		}
		requerimentoUnicoProcessar.setDscOutrosobjtLimpArea(requerimentoUnico.getDscOutrosobjtLimpArea());
		requerimentoUnicoProcessar.setDtcInicioLimpArea(requerimentoUnico.getDtcInicioLimpArea());
		requerimentoUnicoProcessar.setDtcFinalLimpArea(requerimentoUnico.getDtcFinalLimpArea());
		requerimentoUnicoProcessar.setIndBarragemIntervConstruida(requerimentoUnico.getIndBarragemIntervConstruida());
		requerimentoUnicoProcessar.setIndAutorizacaoConstrucaoBarragemInterv(requerimentoUnico.getIndAutorizacaoConstrucaoBarragemInterv());
		requerimentoUnicoProcessar.setIndRealizarQueimaControlada(requerimentoUnico.getIndRealizarQueimaControlada());
		requerimentoUnicoProcessar.setIndUtilMatLenhosoArvrMortas(requerimentoUnico.getIndUtilMatLenhosoArvrMortas());
		requerimentoUnicoProcessar.setIndDeclaracaoPlanoSuprSust(requerimentoUnico.getIndDeclaracaoPlanoSuprSust());
		requerimentoUnicoProcessar.setIndVincFlorProdRepFlor(requerimentoUnico.getIndVincFlorProdRepFlor());
		requerimentoUnicoProcessar.setNumPortariaLicSilvicultura(requerimentoUnico.getNumPortariaLicSilvicultura());
		requerimentoUnicoProcessar.setIndTransfCredRepFlorestal(requerimentoUnico.getIndTransfCredRepFlorestal());
		requerimentoUnicoProcessar.setIndAprovExecPlanoMnjFlor(requerimentoUnico.getIndAprovExecPlanoMnjFlor());
		requerimentoUnicoProcessar.setNumProcessoAprovExecPlanoMnjFlor(requerimentoUnico.getNumProcessoAprovExecPlanoMnjFlor());
		requerimentoUnicoProcessar.setIndCortFlorProd(requerimentoUnico.getIndCortFlorProd());
		requerimentoUnicoProcessar.setIndProrValidAsv(requerimentoUnico.getIndProrValidAsv());
		requerimentoUnicoProcessar.setIndProrValidRcfp(requerimentoUnico.getIndProrValidRcfp());
		requerimentoUnicoProcessar.setIndProrValidAcfp(requerimentoUnico.getIndProrValidAcfp());
		requerimentoUnicoProcessar.setIndProrValidLa(requerimentoUnico.getIndProrValidLa());
		requerimentoUnicoProcessar.setIndProrValidNao(requerimentoUnico.getIndProrValidNao());
		requerimentoUnicoProcessar.setNumPortAsv(requerimentoUnico.getNumPortAsv());
		requerimentoUnicoProcessar.setNumPortRcfp(requerimentoUnico.getNumPortRcfp());
		requerimentoUnicoProcessar.setNumPortAcfp(requerimentoUnico.getNumPortAcfp());
		requerimentoUnicoProcessar.setNumPortLa(requerimentoUnico.getNumPortLa());
		requerimentoUnicoProcessar.setNumPortCertRegFlorProd(requerimentoUnico.getNumPortCertRegFlorProd());
		requerimentoUnicoProcessar.setIndFlorVincRepFlor(requerimentoUnico.getIndFlorVincRepFlor());
		requerimentoUnicoProcessar.setIndRelocServidFlorest(requerimentoUnico.getIndRelocServidFlorest());
		requerimentoUnicoProcessar.setNumPortEmissCredRepFlor(requerimentoUnico.getNumPortEmissCredRepFlor());
		requerimentoUnicoProcessar.setIndRecVolFlorRem(requerimentoUnico.getIndRecVolFlorRem());
		requerimentoUnicoProcessar.setIndOrigMatLenAsv(requerimentoUnico.getIndOrigMatLenAsv());
		requerimentoUnicoProcessar.setIndOrigMatLenRcfp(requerimentoUnico.getIndOrigMatLenRcfp());
		requerimentoUnicoProcessar.setIndOrigMatLenAcfp(requerimentoUnico.getIndOrigMatLenAcfp());
		requerimentoUnicoProcessar.setNumOrigMatLenAsv(requerimentoUnico.getNumOrigMatLenAsv());
		requerimentoUnicoProcessar.setNumOrigMatLenRcfp(requerimentoUnico.getNumOrigMatLenRcfp());
		requerimentoUnicoProcessar.setNumOrigMatLenAcfp(requerimentoUnico.getNumOrigMatLenAcfp());
		requerimentoUnicoProcessar.setIndAtoAmbiental(requerimentoUnico.getIndAtoAmbiental());
		requerimentoUnicoProcessar.setIndAtoFauna(requerimentoUnico.getIndAtoFauna());
		requerimentoUnicoProcessar.setIndManejoFauna(requerimentoUnico.getIndManejoFauna());
		requerimentoUnicoProcessar.setIndEstudoFauna(requerimentoUnico.getIndEstudoFauna());
		requerimentoUnicoProcessar.setTipoCaptacaoCollection(requerimentoUnico.getTipoCaptacaoCollection());
		requerimentoUnicoProcessar.setTipoFinalidadeUsoAguaCollection(requerimentoUnico.getTipoFinalidadeUsoAguaCollection());
		requerimentoUnicoProcessar.setTipoIntervencaoCollection(requerimentoUnico.getTipoIntervencaoCollection());
		requerimentoUnicoProcessar.setTipoReceptorCollection(requerimentoUnico.getTipoReceptorCollection());
		Requerimento requerimentoProcessar = new Requerimento();
		requerimentoProcessar.setIdeOrgao(new Orgao(1));
		requerimentoProcessar.setNomContato(this.requerimentoUnico.getRequerimento().getNomContato());
		requerimentoProcessar.setNumTelefone(this.requerimentoUnico.getRequerimento().getNumTelefone());
		requerimentoProcessar.setDesEmail(this.requerimentoUnico.getRequerimento().getDesEmail());
		requerimentoProcessar.setIdeEnderecoContato(this.requerimentoUnico.getRequerimento().getIdeEnderecoContato());
		requerimentoProcessar.setIdeTipoRequerimento(requerimentoUnico.getRequerimento().getIdeTipoRequerimento());
		requerimentoProcessar.setProcessoTramiteCollection(requerimentoUnico.getRequerimento().getProcessoTramiteCollection());
		requerimentoUnicoProcessar.setRequerimento(requerimentoProcessar);
		return requerimentoUnicoProcessar;
	}

	public void validarEmpreendimento() throws Exception {
		if (Util.isNullOuVazio(this.empreendimento)) {
			desabilitarTudo = true;
			visualizaSelectEmpreendimento = true;
			throw new Exception();
		}
		if (!empreendimentoService.isEmpreendimentoValido(this.empreendimento)) {
			desabilitarTudo = true;
			visualizaSelectEmpreendimento = true;
			throw new Exception();
			// JsfUtil.addErrorMessage(BUNDLE.getString("empreendimento_msg_invalido"));
		} else {
			desabilitarTudo = false;
		}

		if (!desabilitarTudo) {
			boolean requerimentoAberto = requerimentoUnicoService.verificarRequerimentoUnicoAberto(empreendimento);
			requerimentoUnicoPago = requerimentoUnicoService.recuperarUltimoRequerimentoUnicoProcessado(empreendimento);
			if (requerimentoAberto) {
				JsfUtil.addWarnMessage("Não será possível abrir um novo requerimento para este empreendimento, pois o mesmo já apresenta um requerimento em aberto aguardando a formação do processo.");
				desabilitarTudo = true;
				throw new Exception();
			} else if (requerimentoUnicoPago != null) {
				utilizarDadosRequerimentoAnterior();
			}
		}

	}

	public void verificarValorAtividade(RowEditEvent event) {
	this.calculoAtividadeValido = true;
		if (requerimentoUnico.getIdePorte() != null) {
			this.recalcularPorte = Boolean.TRUE;
		}
		for (RequerimentoTipologia requerimentoTipologia : requerimentoTipologiaData) {
			UnidadeMedida ideUnidadeMedida = requerimentoTipologia.getIdeUnidadeMedidaTipologiaGrupo().getIdeUnidadeMedida();
			if(!Util.isNull(ideUnidadeMedida) && !Util.isNull(ideUnidadeMedida.getIdeUnidadeMedida())){
				RequerimentoTipologia objetoEvento = (RequerimentoTipologia) event.getObject();

				String valorAtividade = objetoEvento.getValAtividadeString();
				if(objetoEvento.getValAtividadeString().contains(".")){
					valorAtividade = objetoEvento.getValAtividadeString().replace(".", "");
				}
				objetoEvento.setValAtividade(new BigDecimal(valorAtividade.replace(",", "."), new MathContext(0)));

				if (requerimentoTipologia.getIdeUnidadeMedidaTipologiaGrupo().getIdeUnidadeMedidaTipologiaGrupo()
						.equals(objetoEvento.getIdeUnidadeMedidaTipologiaGrupo().getIdeUnidadeMedidaTipologiaGrupo())) {
					requerimentoTipologia.setPreenchidoUsuario(true);
				}
				if (!requerimentoTipologia.getPreenchidoUsuario()) {
					this.calculoAtividadeValido = false;
				}
				
				if (requerimentoTipologia.getPreenchidoUsuario()
						&& (requerimentoTipologia.getValAtividade().equals(new Double(0)) || requerimentoTipologia.getValAtividade().compareTo(new BigDecimal(0)) == 0)) {
					this.calculoAtividadeValido = false;
					requerimentoTipologia.setValAtividade(null);
					requerimentoTipologia.setPreenchidoUsuario(false);
					JsfUtil.addErrorMessage("Para o cálculo do porte informe o valor das atividades corretamente. Não é permitido valor vazio ou menor ou igual a 0.");
				}
			}
		}
	}

	public void setIdTipologiaPrincipal(ValueChangeEvent event){
		String id = (String) event.getNewValue();
		this.idTipologiaPrincipal = id;
		for (RequerimentoTipologia requerimentoTipologia : requerimentoTipologiaData) {
			if (requerimentoTipologia.getIdeRequerimentoTipologia().equals(Integer.valueOf(id))) {
				requerimentoTipologia.setIndTipologiaPrincipal(Boolean.TRUE);
			} else {
				requerimentoTipologia.setIndTipologiaPrincipal(Boolean.FALSE);
			}
		}
	}

	public void validarPorte() {
		if (this.calculoAtividadeValido && calculoPorteAtivo) {


			List<RequerimentoTipologia> listaTipologia = new ArrayList<RequerimentoTipologia>();
			for (RequerimentoTipologia requerimentoTipologia : requerimentoTipologiaData) {
				DecimalFormat df = Util.getDecimalFormatPtBr();
				requerimentoTipologia.setValAtividadeString(df.format(requerimentoTipologia.getValAtividade()));
				UnidadeMedida ideUnidadeMedida = requerimentoTipologia.getIdeUnidadeMedidaTipologiaGrupo().getIdeUnidadeMedida();
				if(!Util.isNull(ideUnidadeMedida) && !Util.isNull(ideUnidadeMedida.getIdeUnidadeMedida())){
					listaTipologia.add(requerimentoTipologia);
				}
			}

			this.recalcularPorte = Boolean.FALSE;
			Exception erro =null;
			try {
				Porte porte = requerimentoUnicoService.verificarPorte(requerimentoTipologiaData);
				int porteIgual = 0;
				if (!porte.getIdePorte().toString().equals("6")) {
					for (RequerimentoTipologia requerimentoTipologia : requerimentoTipologiaData) {
						if (requerimentoTipologia.getPorte().getIdePorte().equals(porte.getIdePorte())) {
							porteIgual++;
						}
					}
					if (porteIgual > 1) {
						this.tipologiaPrincipal = Boolean.TRUE;
						this.listRequerimentoTipologia = new ArrayList<SelectItem>();
						for (RequerimentoTipologia requerimentoTipologia : requerimentoTipologiaData) {
							if (requerimentoTipologia.getPorte().getIdePorte().equals(porte.getIdePorte())) {
								SelectItem item = new SelectItem(requerimentoTipologia.getIdeRequerimentoTipologia(), requerimentoTipologia
										.getIdeUnidadeMedidaTipologiaGrupo().getIdeTipologiaGrupo().getIdeTipologia().getDesTipologia());
								listRequerimentoTipologia.add(item);
							}
						}
					} else {
						this.tipologiaPrincipal = Boolean.FALSE;
						for (RequerimentoTipologia requerimentoTipologia : requerimentoTipologiaData) {
							if (requerimentoTipologia.getPorte().getIdePorte().equals(porte.getIdePorte())) {
								requerimentoTipologia.setIndTipologiaPrincipal(Boolean.TRUE);
							} else {
								requerimentoTipologia.setIndTipologiaPrincipal(Boolean.FALSE);
							}
						}
					}
				} else {
					int quant = 0;
					quant += requerimentoTipologiaData.getRowCount();
					if (quant > 1) {
						this.tipologiaPrincipal = Boolean.TRUE;
						this.listRequerimentoTipologia = new ArrayList<SelectItem>();
						for (RequerimentoTipologia requerimentoTipologia : requerimentoTipologiaData) {
							SelectItem item = new SelectItem(requerimentoTipologia.getIdeRequerimentoTipologia(), requerimentoTipologia
									.getIdeUnidadeMedidaTipologiaGrupo().getIdeTipologiaGrupo().getIdeTipologia().getDesTipologia());
							listRequerimentoTipologia.add(item);
						}
					} else {
						this.tipologiaPrincipal = Boolean.FALSE;
						for (RequerimentoTipologia requerimentoTipologia : requerimentoTipologiaData) {
							requerimentoTipologia.setIndTipologiaPrincipal(Boolean.TRUE);
						}
					}
				}
				requerimentoUnico.setIdePorte(porte);
			} catch (Exception e) {
				erro =e;
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
		} else {
			JsfUtil.addErrorMessage("Para o cálculo do porte informe o valor das atividades corretamente. Não é permitido valor vazio ou menor ou igual a 0. ");
		}
	}

	public void updateRequerimentoTipologia() {
		RequestContext.getCurrentInstance().addPartialUpdateTarget(":formQuestionario");
		RequestContext.getCurrentInstance().addPartialUpdateTarget("requerimentoTipologia");
	}

	//	Metodos do WIZARD
	//	INICIO
	private boolean skip = true;

	private boolean teste;

	private static Logger logger = Logger.getLogger(RequerimentoUnicoController.class.getName());

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public boolean verificarRespostasParaLocalizacoes(){
		if(!Util.isNullOuVazio(this.producaoVolumetricaMadeira) && this.producaoVolumetricaMadeira){
			skip = false;
			return skip;
		}
		if(!Util.isNullOuVazio(this.requerimentoUnico.getIndRealizarQueimaControlada()) && this.requerimentoUnico.getIndRealizarQueimaControlada()){
			skip = false;
			return skip;
		}
		if(!Util.isNullOuVazio(this.requerimentoUnico.getIndPlanoManejo()) && this.requerimentoUnico.getIndPlanoManejo()){
			skip = false;
			return skip;
		}
		if(!Util.isNullOuVazio(this.requerimentoUnico.getIndServidaoFlorestal()) && this.requerimentoUnico.getIndServidaoFlorestal()){
			skip = false;
			return skip;
		}
		if(!Util.isNullOuVazio(this.requerimentoUnico.getIndRelocServidFlorest()) && this.requerimentoUnico.getIndRelocServidFlorest()){
			skip = false;
			return skip;
		}
		if(!Util.isNullOuVazio(this.cortarFlorestaProducaoRegistrada) && this.cortarFlorestaProducaoRegistrada){
			skip = false;
			return skip;
		}
		if(!Util.isNullOuVazio(this.numeroProcessoAprovacaoPlanomanejoFlorestal) && this.numeroProcessoAprovacaoPlanomanejoFlorestal){
			skip = false;
			return skip;
		}
		return skip;
	}

	public String onFlowProcess(FlowEvent event) {
		logger.info("Aba Atual:" + event.getOldStep());
		logger.info("Próximo Aba:" + event.getNewStep());
		skip = true;
		
		if (!this.desabilitarTudo) {
			
			verificarRespostasParaLocalizacoes();
			
			if (event.getOldStep().equals("quest")) {
				if (isDispensaOutorgaPoco()
						&& !Util.isNull(localizacaoGeograficaSelecionada)
						&& (Util.isLazyInitExcepOuNull(localizacaoGeograficaSelecionada.getDadoGeograficoCollection()) || Util.isNullOuVazio(localizacaoGeograficaSelecionada
								.getDadoGeograficoCollection()))) {
					
					JsfUtil.addErrorMessage("É necessário preencher a localização Geográfica do poço.");
					skip = false;
					return "quest";
				}
				
				if (!Util.isNullOuVazio(vazaoFinalidadeAgua) && vazaoFinalidadeAgua && Util.isNullOuVazio(listTipoFinalidadeUsoAgua)) {
					JsfUtil.addErrorMessage("Deve ser selecionado um ou mais receptor(es) de resíduos líquidos.");
					skip = false;
					return "quest";
				}
				
			}
		}
		
		if(!requerimentoUnico.getIndAtoAmbiental()){
			skip = true;
		}

		if(event.getOldStep().equalsIgnoreCase("caracterizacao") && event.getNewStep().equalsIgnoreCase("tabLocalizacoes") && skip){
			logger.info("Aba Retornada:" + event.getNewStep());
			return "quest";
		}
		
		if(skip) {
			skip = false;   //reset in case user goes back
			logger.info("Aba Retornada:" + event.getNewStep());
			return "caracterizacao";
		}
		
		Exception erro = null;
		
		try {
			if(event.getNewStep().equals("tabLocalizacoes")){
				ContextoUtil.getContexto().setRequerimentoDasLocGeoDoReqUnico(requerimentoUnico.getRequerimento());
				carregarListaDeImoveisRurais();
				carregarPerguntas();
				prepararParaLocalizacoes();
				carregarPerguntasEditarVisualisar();
				logger.info("Aba Retornada:" + event.getNewStep());
				return event.getNewStep();
			} else {
				logger.info("Aba Retornada:" + event.getNewStep());
				return event.getNewStep();
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return event.getNewStep();
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	private void carregarListaDeImoveisRurais() throws Exception {
		listaImovelRuralDoEmpreend = new ArrayList<ImovelRural>();
		if(Util.isLazyInitExcepOuNull(empreendimento.getImovelCollection())){
			Exception erro = null;
			try {
				empreendimento = empreendimentoService.carregarById(empreendimento.getIdeEmpreendimento());
			} catch (Exception e) {
				erro =e;
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
		}
		if(empreendimento.getImovelCollection().size()>1){
			numeroDeImoveisDoEmpreendimento = 0;
			if(empreendimento.getIndCessionario()){
				numeroDeImoveisDoEmpreendimento = 1;
			}
			Imovel imovel = empreendimento.getImovelCollection().iterator().next();
			if(Util.isLazyInitExcepOuNull(imovel.getIdeTipoImovel())){
				imovel = imovelService.buscarImovelPorIde(imovel);
			}
			if(imovel.getIdeTipoImovel().getIdeTipoImovel().intValue() != 1){
				numeroDeImoveisDoEmpreendimento = 1;
			}
			else{
				for (Imovel imov : empreendimento.getImovelCollection()) {
					listaImovelRuralDoEmpreend.add(imov.getImovelRural());
					numeroDeImoveisDoEmpreendimento++;
				}
			}
		}else{
			numeroDeImoveisDoEmpreendimento = 1;
			//			imovelRuralSelecionado = imovelRuralService.carregarImoveisByEmpreendimento(empreendimento).iterator().next().getImovelRural();
		}

	}

	public boolean disabilitaIncluirLocalizacaoDaPergunta(Pergunta p){
		if(!Util.isNullOuVazio(p.getCodPergunta())
				&& (
						(
								p.getCodPergunta().equalsIgnoreCase(PerguntaEnum.PERGUNTA_PRODUCAO_VOLUMETRICA_DE_MADEIRA.getCod()) && !p.getResposta()
								&& p.getListObjReqLimpAreaIsNotNull() && p.getListObjReqLimpArea().size() >= numeroDeImoveisDoEmpreendimento.intValue()
								)
								||	(
										!Util.isNullOuVazio(p.getListLocalizacaoGeograficaDaPergunta()) && p.getListLocalizacaoGeograficaDaPergunta().size() >= numeroDeImoveisDoEmpreendimento.intValue()
										)
						)
				) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isDispensaOutorgaPoco() {
		return !Util.isNull(requerimentoUnico.getNumVazaoCaptacao());
	}

	public boolean isTeste() {
		return teste;
	}

	public void setTeste(boolean teste) {
		this.teste = teste;
	}

	//	FIM ****************** FIM
	//

	public void prepararParaLocalizacoes() throws CloneNotSupportedException{
		RequerimentoImovel reqImovelLoc = new RequerimentoImovel();
		if(!Util.isNullOuVazio(empreendimento)){
			Imovel i = new Imovel();
			Exception erro = null;
			try {
				if(!Util.isNullOuVazio(empreendimento.getImovelCollection()) && empreendimento.getImovelCollection().size()<=1) {
					reqImovelLoc.setImovel(empreendimento.getImovelCollection().iterator().next().clone());
				} else{
					if(Util.isNullOuVazio(listaImovelRuralDoEmpreend)){
						i = imovelRuralService.carregarImoveisByEmpreendimento(empreendimento).iterator().next();
						reqImovelLoc.setImovel(i.clone());
					}
				}
			} catch (LazyInitializationException lazyInitExcep) {
				try {
					if(Util.isNullOuVazio(listaImovelRuralDoEmpreend)){
						i = imovelRuralService.carregarImoveisByEmpreendimento(empreendimento).iterator().next();
						reqImovelLoc.setImovel(i.clone());
					}
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				}
			}catch (Exception e) {
				erro = e;
				System.out.println("RequerimentoUnicoController - "+e);
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
		}
		if(!Util.isNullOuVazio(requerimentoUnico.getIndVolumeMaterial())){
			if(requerimentoUnico.getIndVolumeMaterial()){

			}else{
				Exception erro =null;
				try {
					this.collObjetivoLimpezaArea = (List<ObjetivoLimpezaArea>) requerimentoUnicoService.listarTodosObjetivoLimpezaArea();
				} catch (Exception e) {
					erro =e;
					JsfUtil.addErrorMessage("Não foi possível carregar os Objetivos de Limpeza de área!");
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				}finally{
					if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
				}
			}
		}
		reqImovelLoc.setRequerimento(requerimentoUnico.getRequerimento());
		reqImovelLoc.setPerguntaRequerimento(new PerguntaRequerimento());
		reqImovelLoc.getPerguntaRequerimento().setIdeImovel(reqImovelLoc.getImovel());
		reqImovelLoc.getPerguntaRequerimento().setIdeRequerimento(requerimentoUnico.getRequerimento());
		reqImovelLoc.setIdeTipoImovelRequerimento(new TipoImovelRequerimento(1));
		reqImovelLoc.setIndExcluido(false);
		requerimentoImovel = (RequerimentoImovel) reqImovelLoc.clone();
		ContextoUtil.getContexto().setObjectToLocGeo(reqImovelLoc.clone());
		System.out.println();

		boolean existReq = false;
		if(!Util.isNullOuVazio(perguntaTemp) && !Util.isNullOuVazio(perguntaTemp.getListRequerimentoImovel())){
			for (RequerimentoImovel requerimentoImovel : perguntaTemp.getListRequerimentoImovel()){
				if(!Util.isNull(requerimentoImovel.getIdeRequerimentoImovel())){
					existReq = true;
					if(listaImovelRuralDoEmpreend.contains(requerimentoImovel.getImovel().getImovelRural())) {
						listaImovelRuralDoEmpreend.remove(requerimentoImovel.getImovel().getImovelRural().clone());
					}
				}
			}
		}

		if(Util.isNullOuVazio(perguntaTemp) || !existReq){
			Exception erro = null;
			try {
				carregarListaDeImoveisRurais();
			} catch (Exception e) {
				erro =e;
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
		}


	}

	public void limparTela() {
		addAtributoSessao("REQUERIMENTOS", null);
		init();
		numeroRequerimento = "";
		periodoInicio = null;
		periodoFim = null;
		requerente = null;
		statusRequerimento = null;
		empreendimentoRequerimento = null;
		requerimentos = null;
		areaSelecionada = null;
		this.tipoAto = null;
		this.atoAmbiental = null;
		urSelecionada = null;
		municipioSelecionado = null;
		municipios = null;
	}

	public boolean verificarRequerimentoAptoDLA() {
		boolean apto = false;
		Exception erro =null;
		try {
			if(Util.isNullOuVazio(requerimentoTipologiaData)){
				return false;
			}
			for (RequerimentoTipologia requerimentoTipologia : requerimentoTipologiaData) {
				PorteTipologia porteTipologia = requerimentoUnicoService.recuperarPortePequenoPorTipologiaGrupo(requerimentoTipologia
						.getIdeUnidadeMedidaTipologiaGrupo().getIdeTipologiaGrupo());
				if (porteTipologia.getValMinimo().compareTo(requerimentoTipologia.getValAtividade()) == 1) {
					apto = true;
				} else {
					apto = false;
					break;
				}
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		return apto;
	}

	public String salvarRequerimentoUnico() {
		boolean validation = this.validateSalvarRequerimentoUnico();
		this.aptoDLA = verificarRequerimentoAptoDLA();
		if (validation && !editando) {
			return salvar(validation);
		} else if (validation && editando) {
			return editar(validation);
		}
		return "";
	}

	private String editar(boolean validation) {
		try {
			
			requerimentoTipologiaService.removerByRequerimento(requerimentoUnico.getRequerimento().getIdeRequerimento());
			
			if (this.enderecoContato) {
				requerimentoUnico.getRequerimento().setIdeEnderecoContato(ContextoUtil.getContexto().getRequerimentoEndereco());
			} else {
				requerimentoUnico.getRequerimento().setIdeEnderecoContato(null);
			}
			if (aptoDLA) {
				requerimentoUnico.setIndDla(true);
			} else {
				requerimentoUnico.setIndDla(false);
			}
			if (requerimentoImovelData != null) {
				requerimentoUnico.getRequerimento().setRequerimentoImovelCollection(new ArrayList<RequerimentoImovel>());
				for (RequerimentoImovel requerimentoImo : requerimentoImovelData) {
					if (requerimentoImo.getFracaoGrauLatitude() != null && requerimentoImo.getFracaoGrauLongitude() != null) {
						String latitudeFormatada = requerimentoImo.getFracaoGrauLatitude().replaceAll(",", ".");
						String lontitudeFormatada = requerimentoImo.getFracaoGrauLongitude().replaceAll(",", ".");
						Point p = PostgisUtil.criarPonto(Double.parseDouble(latitudeFormatada), Double.parseDouble(lontitudeFormatada));
						DadoGeografico dGeo = new DadoGeografico();
						dGeo.setCoordGeoNumerica(p.toString());
						LocalizacaoGeografica locGeo = new LocalizacaoGeografica();
						locGeo.getDadoGeograficoCollection().add(dGeo);
						requerimentoImo.setIdeLocalizacaoGeografica(locGeo);
					}
					TipoImovelRequerimento tipoImovelRequerimento = new TipoImovelRequerimento();
					tipoImovelRequerimento.setIdeTipoImovelRequerimento(1);
					requerimentoImo.setIdeTipoImovelRequerimento(tipoImovelRequerimento);
					if (requerimentoImo.getDtcCriacao() == null) {
						requerimentoImo.setDtcCriacao(new Date());
					}
					requerimentoUnico.getRequerimento().getRequerimentoImovelCollection().add(requerimentoImo);
				}
			}
			if (Util.isNullOuVazio(requerimentoUnico.getNumVazaoCaptacao())){
				requerimentoUnico.setIdeLocalizacaoGeografica(null);
			} else if (localizacaoGeograficaSelecionada.getDadoGeograficoCollection() != null && localizacaoGeograficaSelecionada.getDadoGeograficoCollection().size() > 0) {
				requerimentoUnico.setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
			}
			if (this.listTipoCaptacao != null && this.listTipoCaptacao.size() > 0) {
				requerimentoUnico.setTipoCaptacaoCollection(new ArrayList<TipoCaptacao>());
				for (String ideTipoCaptacao : this.listTipoCaptacao) {
					TipoCaptacao tipoCaptacao = new TipoCaptacao();
					tipoCaptacao.setIdeTipoCaptacao(new Integer(ideTipoCaptacao));
					requerimentoUnico.getTipoCaptacaoCollection().add(tipoCaptacao);
				}
			}
			if (this.listTipoFinalidadeUsoAgua != null && this.listTipoFinalidadeUsoAgua.size() > 0) {
				requerimentoUnico.setTipoFinalidadeUsoAguaCollection(new ArrayList<TipoFinalidadeUsoAgua>());
				for (String ideTipoFinalidadeUsoAgua : this.listTipoFinalidadeUsoAgua) {
					TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua = new TipoFinalidadeUsoAgua();
					tipoFinalidadeUsoAgua.setIdeTipoFinalidadeUsoAgua(new Integer(ideTipoFinalidadeUsoAgua));
					requerimentoUnico.getTipoFinalidadeUsoAguaCollection().add(tipoFinalidadeUsoAgua);
				}
			}
			if (this.listTipoIntervencao != null && this.listTipoIntervencao.size() > 0) {
				requerimentoUnico.setTipoIntervencaoCollection(new ArrayList<TipoIntervencao>());
				for (String ideTipoIntervencao : this.listTipoIntervencao) {
					TipoIntervencao tipoIntervencao = new TipoIntervencao();
					tipoIntervencao.setIdeTipoIntervencao(new Integer(ideTipoIntervencao));
					requerimentoUnico.getTipoIntervencaoCollection().add(tipoIntervencao);
				}
			}
			if (this.listTipoTipoReceptor != null && this.listTipoTipoReceptor.size() > 0) {
				requerimentoUnico.setTipoReceptorCollection(new ArrayList<TipoReceptor>());
				for (String ideTipoReceptor : this.listTipoTipoReceptor) {
					TipoReceptor tipoReceptor = new TipoReceptor();
					tipoReceptor.setIdeTipoReceptor(new Integer(ideTipoReceptor));
					requerimentoUnico.getTipoReceptorCollection().add(tipoReceptor);
				}
			}
			if (idTipologiaPrincipal != null && !idTipologiaPrincipal.equals("")) {
				for (RequerimentoTipologia requerimentoTipologia : requerimentoTipologiaData) {
					if (requerimentoTipologia.getIdeRequerimentoTipologia().equals(new Integer(this.idTipologiaPrincipal))) {
						requerimentoTipologia.setIndTipologiaPrincipal(Boolean.TRUE);
					}
				}
			}
			requerimentoUnico.setRequerimentoTipologiaCollection(new ArrayList<RequerimentoTipologia>());
			if(!Util.isNullOuVazio(requerimentoTipologiaData)){


				if(!Util.isNullOuVazio(tipologiaPrincipal) && tipologiaPrincipal){

					for (RequerimentoTipologia requerimentoTipologia : requerimentoTipologiaData) {
						if (requerimentoTipologia.getIdeRequerimentoTipologia().toString().equals(idTipologiaPrincipal)) {
							requerimentoTipologia.setIndTipologiaPrincipal(Boolean.TRUE);
						} else {
							requerimentoTipologia.setIndTipologiaPrincipal(Boolean.FALSE);
						}
					}
				}else{
					for (RequerimentoTipologia requerimentoTipologia : requerimentoTipologiaData) {
						if (!Util.isNullOuVazio(requerimentoTipologia.getPorte()) && requerimentoTipologia.getPorte().equals(requerimentoUnico.getIdePorte())) {
							requerimentoTipologia.setIndTipologiaPrincipal(Boolean.TRUE);
						} else {
							requerimentoTipologia.setIndTipologiaPrincipal(Boolean.FALSE);
						}
					}
				}

				for (RequerimentoTipologia requerimentoTipologia : requerimentoTipologiaData) {
					requerimentoUnico.getRequerimentoTipologiaCollection().add(requerimentoTipologia);

				}
				for (RequerimentoTipologia requerimentoTipologia : requerimentoUnico.getRequerimentoTipologiaCollection()) {
					if (requerimentoTipologia.getValAtividadeString() != null && !requerimentoTipologia.getValAtividadeString().equals("")) {
						requerimentoTipologia.setValAtividade(new BigDecimal(requerimentoTipologia.getValAtividadeString().replace(".", "").replace(",", "."), new MathContext(0)));
					}
				}
			}

			removeLocalizacaoPerguntasNaoExibidas();
			requerimentoUnico = requerimentoUnicoService.editarRequerimentoUnico(requerimentoUnico);
			if(aptoDLA){
				this.certificadoService.gerarComprovante(requerimentoUnico.getRequerimento(), TipoCertificadoEnum.DLA);
			}
			JsfUtil.addSuccessMessage("O requerimento "+requerimentoUnico.getRequerimento().getNumRequerimento()+" foi editado com sucesso.");
			ContextoUtil.getContexto().setObject("O requerimento "+requerimentoUnico.getRequerimento().getNumRequerimento()+" foi editado com sucesso.");

		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
		if (aptoDLA && validation) {
			ContextoUtil.getContexto().setVisualizarDLA(true);
			ContextoUtil.getContexto().setObject(requerimentoUnico);
		}
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/paginas/novo-requerimento/consulta.xhtml");
		} catch (IOException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return "/paginas/novo-requerimento/consulta.xhtml?faces-redirect=true";
		}
		return "/paginas/novo-requerimento/consulta.xhtml";
	}

	private String salvar(boolean validation) {
		Exception erro = null;
		try {
			if (aptoDLA) {
				requerimentoUnico.setIndDla(true);
			} else {
				requerimentoUnico.setIndDla(false);
			}
			requerimentoUnicoService.geraNumeroRequerimento(requerimentoUnico.getRequerimento());
			Collection<RequerimentoImovel> listaRequerimentoImovel = new ArrayList<RequerimentoImovel>();
			if (requerimentoImovelData != null) {
				for (RequerimentoImovel requerimentoImo : requerimentoImovelData) {
					if (requerimentoImo.isRequerimentoUnico()) {
						final double latitudeFormatada = Double.parseDouble(requerimentoImo.getFracaoGrauLatitude().replace(",", "."));
						final double longitudeFormatada = Double.parseDouble(requerimentoImo.getFracaoGrauLongitude().replace(",", "."));
						Point p = PostgisUtil.criarPonto(latitudeFormatada, longitudeFormatada);
						DadoGeografico dGeo = new DadoGeografico();
						dGeo.setCoordGeoNumerica(p.toString());
						LocalizacaoGeografica locGeo = new LocalizacaoGeografica();
						locGeo.getDadoGeograficoCollection().add(dGeo);
						requerimentoImo.setIdeLocalizacaoGeografica(locGeo);
						requerimentoImo.setRequerimento(requerimentoUnico.getRequerimento());
						listaRequerimentoImovel.add(requerimentoImo);
						requerimentoImo.setRequerimento(requerimentoUnico.getRequerimento());
						listaRequerimentoImovel.add(requerimentoImo);
					}
				}
			}
			requerimentoUnico.getRequerimento().setRequerimentoImovelCollection(listaRequerimentoImovel);
			if (!Util.isNullOuVazio(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica())) {
				requerimentoUnico.setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
			}
			if (this.listTipoCaptacao != null && this.listTipoCaptacao.size() > 0) {
				requerimentoUnico.setTipoCaptacaoCollection(new ArrayList<TipoCaptacao>());
				for (String ideTipoCaptacao : this.listTipoCaptacao) {
					TipoCaptacao tipoCaptacao = new TipoCaptacao();
					tipoCaptacao.setIdeTipoCaptacao(new Integer(ideTipoCaptacao));
					requerimentoUnico.getTipoCaptacaoCollection().add(tipoCaptacao);
				}
			}
			if (this.listTipoFinalidadeUsoAgua != null && this.listTipoFinalidadeUsoAgua.size() > 0) {
				requerimentoUnico.setTipoFinalidadeUsoAguaCollection(new ArrayList<TipoFinalidadeUsoAgua>());
				for (String ideTipoFinalidadeUsoAgua : this.listTipoFinalidadeUsoAgua) {
					TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua = new TipoFinalidadeUsoAgua();
					tipoFinalidadeUsoAgua.setIdeTipoFinalidadeUsoAgua(new Integer(ideTipoFinalidadeUsoAgua));
					requerimentoUnico.getTipoFinalidadeUsoAguaCollection().add(tipoFinalidadeUsoAgua);
				}
			}
			if (this.listTipoIntervencao != null && this.listTipoIntervencao.size() > 0) {
				requerimentoUnico.setTipoIntervencaoCollection(new ArrayList<TipoIntervencao>());
				for (String ideTipoIntervencao : this.listTipoIntervencao) {
					TipoIntervencao tipoIntervencao = new TipoIntervencao();
					tipoIntervencao.setIdeTipoIntervencao(new Integer(ideTipoIntervencao));
					requerimentoUnico.getTipoIntervencaoCollection().add(tipoIntervencao);
				}
			}
			if (this.listTipoTipoReceptor != null && this.listTipoTipoReceptor.size() > 0) {
				requerimentoUnico.setTipoReceptorCollection(new ArrayList<TipoReceptor>());
				for (String ideTipoReceptor : this.listTipoTipoReceptor) {
					TipoReceptor tipoReceptor = new TipoReceptor();
					tipoReceptor.setIdeTipoReceptor(new Integer(ideTipoReceptor));
					requerimentoUnico.getTipoReceptorCollection().add(tipoReceptor);
				}
			}
			if (idTipologiaPrincipal != null && !idTipologiaPrincipal.equals("")) {
				for (RequerimentoTipologia requerimentoTipologia : requerimentoTipologiaData) {
					if (requerimentoTipologia.getIdeRequerimentoTipologia().equals(Integer.valueOf(this.idTipologiaPrincipal))) {
						requerimentoTipologia.setIndTipologiaPrincipal(Boolean.TRUE);
					}
				}
			} else if (requerimentoTipologiaData.getRowCount() == 1) {
				for (RequerimentoTipologia requerimentoTipologia : requerimentoTipologiaData) {
					requerimentoTipologia.setIndTipologiaPrincipal(Boolean.TRUE);
				}
			}
			if (requerimentoUnico.getRequerimentoTipologiaCollection() == null) {
				requerimentoUnico.setRequerimentoTipologiaCollection(new ArrayList<RequerimentoTipologia>());
			}
			for (RequerimentoTipologia requerimentoTipologia : requerimentoUnico.getRequerimentoTipologiaCollection()) {
				if (requerimentoTipologia.getValAtividadeString() != null && !requerimentoTipologia.getValAtividadeString().equals("")) {
					requerimentoTipologia.setValAtividade(new BigDecimal(requerimentoTipologia.getValAtividadeString().replace(".", "").replace(",", "."), new MathContext(0)));
				}
			}
			listaRequerimentoImovel = new ArrayList<RequerimentoImovel>();
			if (requerimentoImovelData != null) {
				for (RequerimentoImovel requerimentoImo : requerimentoImovelData) {
					if (requerimentoImo.isRequerimentoUnico()) {
						final double latitudeFormatada = Double.parseDouble(requerimentoImo.getFracaoGrauLatitude().replace(",", "."));
						final double longitudeFormatada = Double.parseDouble(requerimentoImo.getFracaoGrauLongitude().replace(",", "."));
						Point p = PostgisUtil.criarPonto(latitudeFormatada, longitudeFormatada);
						DadoGeografico dGeo = new DadoGeografico();
						dGeo.setCoordGeoNumerica(p.toString());
						LocalizacaoGeografica locGeo = new LocalizacaoGeografica();
						locGeo.getDadoGeograficoCollection().add(dGeo);
						requerimentoImo.setIdeLocalizacaoGeografica(locGeo);
						requerimentoImo.setRequerimento(requerimentoUnico.getRequerimento());
						listaRequerimentoImovel.add(requerimentoImo);
					}
				}
			}
			requerimentoUnico.setRequerimentoTipologiaCollection(new ArrayList<RequerimentoTipologia>());
			for (RequerimentoTipologia requerimentoTipologia : requerimentoTipologiaData) {
				if (!Util.isNullOuVazio(requerimentoTipologia.getIdeUnidadeMedidaTipologiaGrupo())
						&& !Util.isNull(requerimentoTipologia.getIdeUnidadeMedidaTipologiaGrupo().getIdeUnidadeMedidaTipologiaGrupo())) {
					requerimentoUnico.getRequerimentoTipologiaCollection().add(requerimentoTipologia);
				}
			}
			if (!calculoPorteAtivo) {
				this.requerimentoUnico.setIdePorte(new Porte(7));// Porte
				// Não
				// Aplicado
			}
			removeLocalizacaoPerguntasNaoExibidas();
			requerimentoUnico = requerimentoUnicoService.cadastrarRequerimentoUnico(requerimentoUnico);
			if(aptoDLA){
				this.certificadoService.gerarComprovante(requerimentoUnico.getRequerimento(), TipoCertificadoEnum.DLA);
			}
			ContextoUtil.getContexto().setObject("O requerimento foi aberto com sucesso. O número para acompanhamento é: "
					+ requerimentoUnico.getRequerimento().getNumRequerimento()+"."+
					" ------------------------------------ "+
					"Você receberá por e-mail a relação de documentos para abertura do processo. Após o envio da documentação você receberá "
					+ "o boleto para pagamento da taxa ambiental.");
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			erro =e;
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		if (aptoDLA && validation) {
			ContextoUtil.getContexto().setVisualizarDLA(true);
			//			ContextoUtil.getContexto().setObject(requerimentoUnico);
		}
		try {
			statusRequerimento = new StatusRequerimento(StatusRequerimentoEnum.AGUARDANDO_ENQUADRAMENTO.getStatus());
			FacesContext.getCurrentInstance().getExternalContext().redirect("/paginas/manter-requerimento_unico/consulta.xhtml");
		} catch (IOException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return "/paginas/manter-requerimento_unico/consulta.xhtml?faces-redirect=true";
		}
		return "/paginas/manter-requerimento_unico/consulta.xhtml";
	}

	private void removeLocalizacaoPerguntasNaoExibidas(){
		Exception erro = null;
		try {
			if(!Util.isNullOuVazio(requerimentoUnico.getIndVolumeMaterial()) && !requerimentoUnico.getIndVolumeMaterial() && !Util.isNullOuVazio(perguntaProducaoVolumetricaMadeira) && perguntaProducaoVolumetricaMadeira.getLocalizacaoGeograficaDaPerguntaIsNotNull()){
				excluirLocalizacaoDePerguntasNegadas(perguntaProducaoVolumetricaMadeira, requerimentoUnico);
			}
			if(!Util.isNullOuVazio(requerimentoUnico.getIndVolumeMaterial()) && requerimentoUnico.getIndVolumeMaterial() && !Util.isNullOuVazio(perguntaProducaoVolumetricaMadeira)){
				List<ObjetivoRequerimentoLimpezaArea> listObjReqAreaTemp = requerimentoUnicoService.listarObjetivoRequerimentoLimpezaAreaPorRequerimento(requerimentoUnico);
				if(!Util.isNullOuVazio(listObjReqAreaTemp) && !listObjReqAreaTemp.isEmpty()) {
					excluirLocalizacaoDePerguntasNegadas(perguntaProducaoVolumetricaMadeira, requerimentoUnico);
				}
			}
			if(!Util.isNullOuVazio(requerimentoUnico.getIndRealizarQueimaControlada()) && !requerimentoUnico.getIndRealizarQueimaControlada() && !Util.isNullOuVazio(perguntaRealizarQueimaControlada) && perguntaRealizarQueimaControlada.getLocalizacaoGeograficaDaPerguntaIsNotNull()){
				excluirLocalizacaoDePerguntasNegadas(perguntaRealizarQueimaControlada, requerimentoUnico);
			}
			if(!Util.isNullOuVazio(requerimentoUnico.getIndPlanoManejo()) && !requerimentoUnico.getIndPlanoManejo() && !Util.isNullOuVazio(perguntaAprovPlanoManejoFlorestal) && perguntaAprovPlanoManejoFlorestal.getLocalizacaoGeograficaDaPerguntaIsNotNull()){
				excluirLocalizacaoDePerguntasNegadas(perguntaAprovPlanoManejoFlorestal, requerimentoUnico);
			}
			if(!Util.isNullOuVazio(requerimentoUnico.getIndAprovExecPlanoMnjFlor()) && !requerimentoUnico.getIndAprovExecPlanoMnjFlor() && !Util.isNullOuVazio(perguntaAprovExecPlanManejFlorest) && perguntaAprovExecPlanManejFlorest.getLocalizacaoGeograficaDaPerguntaIsNotNull()){
				excluirLocalizacaoDePerguntasNegadas(perguntaAprovExecPlanManejFlorest, requerimentoUnico);
			}
			if(!Util.isNullOuVazio(requerimentoUnico.getIndCortFlorProd()) && !requerimentoUnico.getIndCortFlorProd() && !Util.isNullOuVazio(perguntaCortarFlorestProducRegist) && perguntaCortarFlorestProducRegist.getLocalizacaoGeograficaDaPerguntaIsNotNull()){
				excluirLocalizacaoDePerguntasNegadas(perguntaCortarFlorestProducRegist, requerimentoUnico);
			}
			if(!Util.isNullOuVazio(requerimentoUnico.getIndServidaoFlorestal()) && !requerimentoUnico.getIndServidaoFlorestal() && !Util.isNullOuVazio(perguntaAprovLocRelocServFlorest) && perguntaAprovLocRelocServFlorest.getLocalizacaoGeograficaDaPerguntaIsNotNull()){
				excluirLocalizacaoDePerguntasNegadas(perguntaAprovLocRelocServFlorest, requerimentoUnico);
			}
			if(!Util.isNullOuVazio(requerimentoUnico.getIndRelocServidFlorest()) && !requerimentoUnico.getIndRelocServidFlorest() && !Util.isNullOuVazio(perguntaAprovRelocServidFlorest) && perguntaAprovRelocServidFlorest.getLocalizacaoGeograficaDaPerguntaIsNotNull()){
				excluirLocalizacaoDePerguntasNegadas(perguntaAprovRelocServidFlorest, requerimentoUnico);
			}
			if(!Util.isNullOuVazio(requerimentoUnico.getIndRelocServidFlorest()) && !requerimentoUnico.getIndRelocServidFlorest() && !Util.isNullOuVazio(perguntaAprovFututaRelocServidFlorest) && perguntaAprovFututaRelocServidFlorest.getLocalizacaoGeograficaDaPerguntaIsNotNull()){
				excluirLocalizacaoDePerguntasNegadas(perguntaAprovFututaRelocServidFlorest, requerimentoUnico);
			}
		} catch (Exception e) {
			erro =e;
			JsfUtil.addErrorMessage("Erro ao Excluir Localizações de perguntas não Exibidas.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	private void excluirLocalizacaoDePerguntasNegadas(Pergunta pergunta, RequerimentoUnico reqUnico) {
		Exception erro = null;
		try {
			if(!Util.isLazyInitExcepOuNull(pergunta) && !Util.isNullOuVazio(pergunta) && !Util.isLazyInitExcepOuNull(pergunta.getIdePergunta())
					&& !Util.isNullOuVazio(pergunta.getIdePergunta()) && !Util.isNullOuVazio(reqUnico) && !Util.isNullOuVazio(reqUnico.getRequerimento())){
				requerimentoImovelService.excluirReqImovelDaPerguntaDoRequerimento(pergunta, reqUnico.getRequerimento());
			}else{
				JsfUtil.addErrorMessage("Pergunta ou Requerimento Unico chegaram vazios no Servidor.");
			}
		} catch (Exception e) {
			erro =e;
			JsfUtil.addErrorMessage("Erro ao limpar Localização Geográfica de perguntas com respostas alteradas.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public String redirecionar() {
		return "/paginas/manter-requerimento_unico/consulta.xhtml";
	}

	public void excluirRequerimentoUnico() {
		Exception erro = null;
		try {
			RequerimentoUnico requerimentoUnico = (RequerimentoUnico) ContextoUtil.getContexto().getObject();
			StatusRequerimento statusRequeri = new StatusRequerimento();
			statusRequeri.setIdeStatusRequerimento(StatusRequerimentoEnum.CANCELADO.getStatus());
			TramitacaoRequerimento tramitacaoRequerimento = new TramitacaoRequerimento();
			tramitacaoRequerimento.setIdeRequerimento(requerimentoUnico.getRequerimento());
			tramitacaoRequerimento.setDtcMovimentacao(new Date());
			tramitacaoRequerimento.setIdeRequerimento(requerimentoUnico.getRequerimento());
			tramitacaoRequerimento.setIdeStatusRequerimento(statusRequeri);
			tramitacaoRequerimento.setIdePessoa(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa());
			tramitacaoRequerimentoService.salvar(tramitacaoRequerimento);
			JsfUtil.addSuccessMessage("Requerimento único cancelado com sucesso!");
			//this.consultar();
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage("Não foi possível excluir o Requerimento único selecionado.");
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public boolean validateSalvarRequerimentoUnico() {
		boolean valido = true;
		if (requerimentoUnico.getIndTramiteInema()) {
			if (!(requerimentoUnico.getRequerimento().getProcessoTramiteCollection().size() > 0)) {
				JsfUtil.addErrorMessage("Deve ser adicionado pelo menos 1 processo em trâmite ou concluído no INEMA.");
				valido = false;
			}
		}
		if (this.utilizaEmpreendimentoAgua != null && this.utilizaEmpreendimentoAgua && this.listTipoCaptacao.size() == 0) {
			JsfUtil.addErrorMessage("Deve ser selecionado 1 ou mais origens de água.");
			valido = false;
		}
		if ((this.realizaEmissaoResiduosLiquidos != null && this.realizaEmissaoResiduosLiquidos && this.listTipoTipoReceptor.size() == 0)
				|| (this.vazaoFinalidadeAgua != null && this.vazaoFinalidadeAgua && this.listTipoFinalidadeUsoAgua.size() == 0)) {
			JsfUtil.addErrorMessage("Deve ser selecionado um ou mais receptor(es) de resíduos líquidos.");
			valido = false;
		}
		if (this.intervencaoCorpoHidrico != null && this.intervencaoCorpoHidrico && this.listTipoIntervencao.size() == 0) {
			JsfUtil.addErrorMessage("Deve ser selecionado uma ou mais opção(ões) de intervenção em corpo hídrico.");
			valido = false;
		}
		if (requerimentoUnico.getIndAtoAmbiental() && requerimentoUnico.getIndSupressaoVegetacao() && this.limpezaArea != null && this.limpezaArea && Util.isNullOuVazio(this.perguntaProducaoVolumetricaMadeira.getListObjReqLimpArea()) ) {
			JsfUtil.addErrorMessage("Deve ser informada uma ou mais Localizações Geográficas de objetivos de limpeza de área.");
			valido = false;
		}
		if (requerimentoUnico.getIndAtoAmbiental() && requerimentoUnico.getIndSupressaoVegetacao() && !Util.isNullOuVazio(this.limpezaArea) && !this.limpezaArea && !(this.perguntaProducaoVolumetricaMadeira.getLocalizacaoGeograficaDaPerguntaIsNotNull()) ) {
			JsfUtil.addErrorMessage("Deve ser informada uma Localização Geográfica de onde haverá Produção Volumetrica de Madeira.");
			valido = false;
		}
		if (requerimentoUnico.getIndAtoAmbiental() && !Util.isNullOuVazio(this.requerimentoUnico.getIndRealizarQueimaControlada()) && this.requerimentoUnico.getIndRealizarQueimaControlada() && !(this.perguntaRealizarQueimaControlada.getLocalizacaoGeograficaDaPerguntaIsNotNull())) {
			JsfUtil.addErrorMessage("Deve ser informada uma Localização Geográfica de onde haverá Queima Controlada.");
			valido = false;
		}
		if (requerimentoUnico.getIndAtoAmbiental() && !Util.isNullOuVazio(this.requerimentoUnico.getIndPlanoManejo()) && this.requerimentoUnico.getIndPlanoManejo() && !(this.perguntaAprovPlanoManejoFlorestal.getLocalizacaoGeograficaDaPerguntaIsNotNull())) {
			JsfUtil.addErrorMessage("Deve ser informada uma Localização Geográfica do Plano de Manejo Florestal.");
			valido = false;
		}
		if (requerimentoUnico.getIndAtoAmbiental() && !Util.isNullOuVazio(this.requerimentoUnico.getIndAprovExecPlanoMnjFlor()) && this.requerimentoUnico.getIndAprovExecPlanoMnjFlor() && !(this.perguntaAprovExecPlanManejFlorest.getLocalizacaoGeograficaDaPerguntaIsNotNull())) {
			JsfUtil.addErrorMessage("Deve ser informada uma Localização Geográfica da Execução do Plano de Manejo Florestal.");
			valido = false;
		}
		if (requerimentoUnico.getIndAtoAmbiental() && !Util.isNullOuVazio(this.requerimentoUnico.getIndCortFlorProd()) && this.requerimentoUnico.getIndCortFlorProd() && !(this.perguntaCortarFlorestProducRegist.getLocalizacaoGeograficaDaPerguntaIsNotNull())) {
			JsfUtil.addErrorMessage("Deve ser informada uma Localização Geográfica de onde haverá o corte de uma floresta de produção já licenciada ou registrada.");
			valido = false;
		}
		if (requerimentoUnico.getIndAtoAmbiental() && !Util.isNullOuVazio(this.requerimentoUnico.getIndServidaoFlorestal()) && this.requerimentoUnico.getIndServidaoFlorestal() && !(this.perguntaAprovLocRelocServFlorest.getLocalizacaoGeograficaDaPerguntaIsNotNull())) {
			JsfUtil.addErrorMessage("Deve ser informada uma Localização Geográfica de onde haverá locação de Servidão ambiental.");
			valido = false;
		}
		if (requerimentoUnico.getIndAtoAmbiental() && !Util.isNullOuVazio(this.requerimentoUnico.getIndRelocServidFlorest()) && this.requerimentoUnico.getIndRelocServidFlorest() && !(this.perguntaAprovRelocServidFlorest.getLocalizacaoGeograficaDaPerguntaIsNotNull())) {
			JsfUtil.addErrorMessage("Deve ser informada a Localização Geográfica da Servidão ambiental atual.");
			valido = false;
		}
		if (requerimentoUnico.getIndAtoAmbiental() && !Util.isNullOuVazio(this.requerimentoUnico.getIndRelocServidFlorest()) && this.requerimentoUnico.getIndRelocServidFlorest() && !(this.perguntaAprovFututaRelocServidFlorest.getLocalizacaoGeograficaDaPerguntaIsNotNull())) {
			JsfUtil.addErrorMessage("Deve ser informada a Localização Geográfica da futura Servidão ambiental.");
			valido = false;
		}
		if (calculoPorteAtivo && requerimentoUnico.getIdePorte() == null) {
			boolean verificaValor = false;

			for (RequerimentoTipologia requerimentoTipologia : requerimentoTipologiaData) {
				if(requerimentoTipologia.getPreenchidoUsuario().booleanValue()){
					verificaValor = true;
				}else{
					verificaValor = false;
					break;
				}
			}

			if(verificaValor){
				JsfUtil.addErrorMessage("O cálculo do porte é obrigatório.");
			}else{
				JsfUtil.addErrorMessage("Para o cálculo do porte informe o valor das atividades corretamente. Não é permitido valor vazio ou menor ou igual a 0.");
			}

			valido = false;
		}
		if (this.recalcularPorte) {
			JsfUtil.addErrorMessage("O valor da(s) atividade(s) sofreu alteração, o cálculo do porte deve ser realizado novamente.");
			valido = false;
		}
		if (!termoDeclaracao) {
			valido = false;
			JsfUtil.addErrorMessage("O campo Declaração é de preenchimento obrigatório.");
		}
		if (!Util.isNullOuVazio(requerimentoUnico.getNumVazaoCaptacao()) && requerimentoUnico.getNumVazaoCaptacao().doubleValue() == 0) {
			valido = false;
			JsfUtil.addErrorMessage("O valor da vazão de captação deve ser maior que zero.");
		}
		return valido;
	}

	public Boolean validaListaRequerimentoImovel(List<RequerimentoImovel> listReqImov) {
		for (RequerimentoImovel requerimentoImovel : listReqImov) {
			if(Util.isNullOuVazio(requerimentoImovel.getIdeLocalizacaoGeografica())){
				return true;
			}
		}
		return false;
	}

	public void salvarRequerimentoImovel() {
		calculaFracaoGrauLatitude(null);
		this.requerimentoImovel.setFracaoGrauLatitude(this.fracaoGrauLatitude);
		calculaFracaoGrauLongitude(null);
		this.requerimentoImovel.setFracaoGrauLongitude(this.fracaoGrauLongitude);
		Exception erro = null;
		try {
			if (!localizacaoGeograficaService.validaVerticeMunicipio(this.fracaoGrauLatitude, this.fracaoGrauLongitude, empreendimento, null)) {
				JsfUtil.addWarnMessage("Coordenada informada está fora dos limites da Localidade do empreendimento.");
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		RequestContext.getCurrentInstance().execute("dialogoIncluirRequerimentoImovel.hide()");
	}

	public void editarRequerimentoImovel() {
		//		this.grausLatitude = this.requerimentoImovel.getGrausLatitude();
		//		this.minutosLatitude = this.requerimentoImovel.getMinutosLatitude();
		//		this.segundosLatitude = this.requerimentoImovel.getSegundosLatitude();
		//		this.fracaoGrauLatitude = this.requerimentoImovel.getFracaoGrauLatitude();
		//		this.grausLongitude = this.requerimentoImovel.getGrausLongitude();
		//		this.minutosLongitude = this.requerimentoImovel.getMinutosLongitude();
		//		this.segundosLongitude = this.requerimentoImovel.getSegundosLongitude();
		//		this.fracaoGrauLongitude = this.requerimentoImovel.getFracaoGrauLongitude();
	}

	public void openDialogLocGeoRequerimentoImovel(Pergunta perguntaTemp) throws CloneNotSupportedException {
		this.perguntaTemp = perguntaTemp;
		RequerimentoImovel reqImo = new RequerimentoImovel();
		if (!desabilitarTudo) {
			imovelRuralSelecionado = new ImovelRural();
		}
		reqImo.setImovel(imovel);
		Exception erro = null;
		try {
			reqImo.setImovel(this.empreendimento.getImovelCollection().iterator().next());
		} catch (Exception e) {
			erro =e;
			System.out.println("NAO FOI POSSIVEL CARREGAR O IMOVEL A PARTIR DO EMPREENDIMENTO! *******");
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		reqImo.setIdeTipoImovelRequerimento(new TipoImovelRequerimento(1));
		reqImo.setRequerimento(requerimentoUnico.getRequerimento());
		reqImo.setObjetivoRequerimentoLimpezaAreaCollection(null);
		reqImo.setObjetivoRequerimentoLimpezaAreaCollection(new ArrayList<ObjetivoRequerimentoLimpezaArea>());
		ObjetivoRequerimentoLimpezaArea objLimpArea = new ObjetivoRequerimentoLimpezaArea();
		objLimpArea.setIdeObjetivoLimpezaArea(objLimpezaAreaSelecionado);
		objLimpArea.setIdeRequerimento(requerimentoUnico.getRequerimento());
		objLimpArea.setIdeRequerimentoImovel(reqImo);
		requerimentoUnico.getRequerimento().setRequerimentoImovelCollection(new ArrayList<RequerimentoImovel>());
		requerimentoUnico.getRequerimento().getRequerimentoImovelCollection().add(reqImo);
		prepararParaLocalizacoes();

	}

	public void limparRequerimentoImovel() {
	}

	public void limparCoordenadas() {
		grausLatitude = "";
		minutosLatitude = "";
		segundosLatitude = "";
		grausLongitude = "";
		minutosLongitude = "";
		segundosLongitude = "";
		fracaoGrauLatitude = "";
		fracaoGrauLongitude = "";
	}

	public void validarBoletoRequerimento() {
		if (boleto.isZerado() && !showEmissaoDae) {
			this.exibeCoordenacoes = true;
			this.consultarAreas(requerimentoUnicoDTO.getRequerimentoUnico(), this.empreendimento.getTipologias());
		} else {
			this.exibeCoordenacoes = false;
		}
	}

	public void gerarBoleto() {
		Exception erro =null;
		try {
			// Obtem o requerimento selecionado na tela.
			if (Util.isNullOuVazio(requerimentoUnicoDTO)) {
				requerimentoUnicoDTO = (RequerimentoUnicoDTO) ContextoUtil.getContexto().getObject();
			}
			RequerimentoUnico lRequerimento = requerimentoUnicoDTO.getRequerimentoUnico();
			boleto.setIdePessoa(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa());
			Date dataAtual = new Date();

			this.gerarBoleto(lRequerimento, dataAtual);
		} catch (Exception e) {
			erro =e;
			JsfUtil.addErrorMessage("Não foi possível atender a sua solicitação. Favor entrar em contato com o Atendimento.");
			System.out.println("Erro ao gerar boleto:");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void gerarProcessoRequerimento() {
		Exception erro =null;
		try {
			if (exibeCoordenacoes && Util.isNull(this.areaSelecionada)) {
				JsfUtil.addErrorMessage("O campo Coordenação é de preenchimento obrigatório.");
				return;
			}
			this.comprovantePagamentoServiceFacade.gerarProcesso(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa(),
					requerimentoUnicoDTO.getRequerimentoUnico(), new Area(this.areaSelecionada), requerimentoUnicoDTO.getPessoa());
			this.fecharDialogsBoleto();
			JsfUtil.addSuccessMessage("O Processo do requerimento " + requerimentoUnicoDTO.getRequerimentoUnico().getRequerimento().getNumRequerimento()
					+ " foi formado com sucesso");
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	private void gerarBoleto(RequerimentoUnico lRequerimento, Date dataAtual) throws Exception {
			Exception erro =null;
		try {
			if (showEmissaoDae && boleto.isZerado()) {
				salvarBoletoDAE(lRequerimento);
			} else if (boleto.getDtcVencimento().after(dataAtual)) {
				gerarDae();
				boletoPagamentoRequerimentoService.salvarBoletoEnviandoEmail(boleto, lRequerimento.getRequerimento(),showEmissaoDae);
				// Recarrega a lista de requerimento
				//consultar();
				// Limpa os dados do boleto
				boleto = new BoletoPagamentoRequerimento();
				if (RequestContext.getCurrentInstance() != null) {
					this.fecharDialogsBoleto();
				}
				JsfUtil.addSuccessMessage(BUNDLE.getString("requerimento_msg_boleto_sucesso"));
			} else {
				// Data de vencimento menor que data atual
				JsfUtil.addErrorMessage("A Data de Vencimento deve ser maior que a data atual!");
				RequestContext.getCurrentInstance().execute("confirmacaoBoleto.hide()");
			}
		} catch (Exception e) {
			erro =e;
			System.out.println("Erro ao Gerar Boleto");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void salvarBoletoDAE(RequerimentoUnico lRequerimento) throws Exception {
		gerarDae();
		boletoPagamentoRequerimentoService.enviarEmailBoletoDaeLiberado(lRequerimento.getRequerimento());
		Pessoa pessoaLogada = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa();
		boletoPagamentoRequerimentoService.salvarTramitacaoPagamentoLiberado(lRequerimento.getRequerimento(), pessoaLogada);
		JsfUtil.addSuccessMessage("Gravação da taxa do DAE efetuado com sucesso!");
		this.fecharDialogsBoleto();
	}

	private void gerarDae() {
		if (showEmissaoDae) {
			salvarBoletoDaeRequerimento();
		}
	}

	private void fecharDialogsBoleto() {
		RequestContext.getCurrentInstance().addPartialUpdateTarget("formBoleto:boletoCampos");
		// Atualiza componentes na tela
		RequestContext.getCurrentInstance().execute("dialogBoletoIncluir.hide()");
		RequestContext.getCurrentInstance().execute("confirmacaoBoleto.hide()");
	}


	public StreamedContent getBoleto(Integer ideRequerimento) {
		this.requerimentoUnicoDTO = new RequerimentoUnicoDTO(ideRequerimento);
		return getDownloadBoleto();
	}

	public StreamedContent getDownloadBoleto() {
		try {
			// Obtem o requerimento selecionado na tela.
			RequerimentoUnico lRequerimento = requerimentoUnicoDTO.getRequerimentoUnico();
			lRequerimento = requerimentoUnicoService.buscarRequerimentoUnico(lRequerimento);
			boleto = lRequerimento.getRequerimento().getBoletoPagamentoRequerimento();
			BoletoViewer boletoViewer = boletoPagamentoRequerimentoService.gerarBoleto(boleto);
			InputStream stream = new ByteArrayInputStream(boletoViewer.getPdfAsByteArray());
			return new DefaultStreamedContent(stream, "application/pdf", "Boleto Bancário.pdf");
			
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Ocorreu um erro ao gerar o boleto: " + e.getMessage());
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}

	public void salvarDescVertice() {
		//
		System.out.println("O valor da localização geográfica foi alterado com sucesso para: " + localizacaoGeograficaSelecionada.getDesLocalizacaoGeografica());
	}

	@SuppressWarnings("unchecked")
	public void changeRealizaEmissaoResiduosLiquidos(ValueChangeEvent changeEvent) {
		this.listTipoTipoReceptor = (List<String>) changeEvent.getNewValue();
	}

	public void upload(FileUploadEvent event) {
		fileUpload = event.getFile();
	}

	public void trataArquivo(FileUploadEvent event) {
		Exception erro = null;
		try {
			// Obtem o requerimento selecionado na tela.
			RequerimentoUnico lRequerimento = requerimentoUnicoDTO.getRequerimentoUnico();
			lRequerimento = requerimentoUnicoService.buscarRequerimentoUnico(lRequerimento);
			fileUpload = event.getFile();
			caminhoArquivo = gerenciaArquivoservice
					.uploadArquivoComprovanteBoleto(fileUpload, lRequerimento.getRequerimento().getBoletoPagamentoRequerimento());
			temArquivo = true;
			if (listaArquivo == null) {
				listaArquivo = new ArrayList<String>();
			}
			listaArquivo.clear();
			listaArquivo.add(FileUploadUtil.getFileName(caminhoArquivo));
			JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
		} catch (Exception e) {
			erro =e;
			JsfUtil.addErrorMessage("Ocorreu um erro ao gerar o boleto: " + e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void salvarUploadComprovante() {
		Exception erro = null;
		try {

			if (validaUploadComprovante()) {
				// Obtem o requerimento selecionado na tela.
				RequerimentoUnico lRequerimento = requerimentoUnicoDTO.getRequerimentoUnico();
				lRequerimento = requerimentoUnicoService.buscarRequerimentoUnico(lRequerimento);
				// Salva arquivo no diretório
				// gerenciaArquivoservice.uploadArquivoComprovanteBoleto(fileUpload,
				// lRequerimento.getRequerimento().getBoletoPagamentoRequerimento());
				// Registra o comprovante na base
				if(!Util.isNullOuVazio(caminhoArquivo)){
					comprovante = new ComprovantePagamento();
					comprovante.setDscCaminhoArquivo(caminhoArquivo);
					comprovante.setIdePessoaUpload(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa());
					comprovante.setIndComprovanteValidado(false);
				}if (showUploadComprovanteDae) {
					salvarUploadComprovanteDaeCertificado();
				}
				if (showUploadComprovanteDaeVistoria) {
					salvarUploadComprovanteDaeVistoria();

				}
				if (!Util.isNullOuVazio(comprovante) && !Util.isNullOuVazio(comprovante.getDscCaminhoArquivo())) {
					comprovantePagamentoServiceFacade.registraComprovante(comprovante, lRequerimento.getRequerimento());
				}
				if (!Util.isNullOuVazio(comprovantePagamentoDaeCertificado) && !Util.isNullOuVazio(comprovantePagamentoDaeCertificado.getDscCaminhoArquivo())) {
					comprovantePagamentoServiceFacade.registraComprovanteDae(comprovantePagamentoDaeCertificado, lRequerimento.getRequerimento());
				}
				if (!Util.isNullOuVazio(comprovantePagamentoDaeVistoria) && !Util.isNullOuVazio(comprovantePagamentoDaeVistoria.getDscCaminhoArquivo())) {
					comprovantePagamentoServiceFacade.registraComprovanteDae(comprovantePagamentoDaeVistoria, lRequerimento.getRequerimento());
				}


				if (verificaComprovanteSalvo()) {
					// Inserir nova tramita??o com status 7
					StatusRequerimento status = statusRequerimentoService.carregarGet(StatusRequerimentoEnum.COMPROVANTE_ENVIADO.getStatus());
					tramitacaoRequerimentoService.criarTramitacaoRequerimento(lRequerimento.getRequerimento(), status, ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa());
					// Atualiza componentes na tela
					// Recarrega a lista de requerimento
					//consultar();
					fileUpload = null;
					RequestContext.getCurrentInstance().execute("dialogBoletoDownload.hide()");
					RequestContext.getCurrentInstance().addPartialUpdateTarget("boletoDownloadForm:boletoDownloadCampos");
					limpaCamposUploadComprovante();

					JsfUtil.addSuccessMessage(BUNDLE.getString("requerimento_msg_comprovante_sucesso"));
				} else {
					JsfUtil.addErrorMessage("Ocorreu um erro ao salvar o comprovante. Tente novamente.");
				}
			}
		} catch (Exception e) {
			erro =e;
			System.out.println("Ocorreu um erro ao salvar o comprovante");
			JsfUtil.addErrorMessage("Não foi possível atender a sua solicitação. Favor entrar em contato com o Atendimento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void prepararValidarBoleto(RequerimentoUnicoDTO pRequerimentoUnicoDTO) {
		Exception erro = null;
		try {
			this.areaSelecionada = null;
			// Obtem o requerimento selecionado na tela.
			RequerimentoUnico lRequerimento = pRequerimentoUnicoDTO.getRequerimentoUnico();
			requerimentoUnicoDTO = pRequerimentoUnicoDTO;
			lRequerimento = requerimentoUnicoService.buscarRequerimentoUnico(lRequerimento);
			Collection<Tipologia> tipologias = this.empreendimentoService.buscarTipologias(pRequerimentoUnicoDTO.getEmpreendimento(), false, true);
			this.empreendimento = this.empreendimentoService.carregarByIdeRequerimento(lRequerimento.getIdeRequerimentoUnico());
			this.empreendimento.setTipologias(tipologias);
			// Até o momento só haverá um comprovante pro boleto
			carregarComprovantePagamento(lRequerimento);
			comprovantePagamentosDae = new ArrayList<ComprovantePagamentoDaeDTO>();
			comprovantePagamentosDae.addAll(comprovantePagamentoServiceFacade.obterPorRequerimentoDae(lRequerimento.getRequerimento()));
			if (comprovantePagamentosDae.isEmpty()) {
				showEmissaoDae = false;
			} else {
				showEmissaoDae = true;
			}
			// Obtem as áreas do requerimento

			for (RequerimentoPessoa pessoasDoRequerimento : lRequerimento.getRequerimento().getRequerimentoPessoaCollection()) {
				if(pessoasDoRequerimento.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento().equals(TipoPessoaRequerimentoEnum.REQUERENTE.getId())){
					pessoaRequerenteBoleto = pessoasDoRequerimento.getPessoa();
					break;
				}
			}

			this.verificarTipologias();
			consultarAreas(lRequerimento, tipologias);
		} catch (Exception e) {
			erro =e;
			JsfUtil.addErrorMessage("Ocorreu um erro ao obter o comprovante: " + e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void carregarComprovantePagamento(RequerimentoUnico lRequerimento) throws Exception {
		comprovantePagamentos = new ArrayList<ComprovantePagamentoDTO>();
		ComprovantePagamentoDTO comprovante = comprovantePagamentoServiceFacade.obterPorRequerimento(lRequerimento.getRequerimento());
		if (!Util.isNull(comprovante)) {
			comprovantePagamentos.add(comprovante);
		}
	}

	public void salvarValidacaoBoleto() {
		if (validacaoComprovante() && (!Util.isNullOuVazio(comprovantePagamentos)  || !Util.isNullOuVazio(comprovantePagamentosDae)) && (validaComprovanteApresentado(comprovantePagamentos) && validaComprovanteApresentadoDae(comprovantePagamentosDae))) {
			Exception erro =null;
			try {
				Pessoa pessoaLogada = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa();
				if (!Util.isNullOuVazio(comprovantePagamentos)) {
					gerarProcessoComBoleto(pessoaLogada);
				} else {
					gerarProcessoApenasComDAE(pessoaLogada);
				}

				this.certificadoService.gerarCertificado(requerimentoUnicoDTO);

				// Atualiza componentes na tela
				// Recarrega a lista de requerimento
				//consultar();
				RequestContext.getCurrentInstance().execute("dialogBoletoValidarComprovante.hide()");
				JsfUtil.addSuccessMessage(BUNDLE.getString("requerimento_msg_validacao_comprovante_sucesso"));
			} catch (Exception e) {
				erro =e;
				JsfUtil.addErrorMessage("Ocorreu um erro ao validar o comprovante: " + e.getMessage());
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
		}else {
			this.emailValidacao = "Favor acessar o SEIA para verificar pendências no comprovante enviado. \n\n Atte. \n Central de Atendimento/INEMA";
			RequestContext.getCurrentInstance().execute("confirmarEnvioEmail.show()");
		}
	}


	public void gerarProcessoComBoleto(Pessoa pessoaLogada) throws Exception {
		comprovantePagamentos.get(0).getComprovantePagamento().setIdePessoaValidacao(pessoaLogada);
		comprovantePagamentoServiceFacade.validarComprovanteGerarProcesso(comprovantePagamentos.get(0).getComprovantePagamento(), comprovantePagamentosDae,
				requerimentoUnicoDTO.getRequerimentoUnico(), new Area(areaSelecionada), pessoaRequerenteBoleto);
		pessoaRequerenteBoleto = null;
	}

	public void gerarProcessoApenasComDAE(Pessoa pessoaLogada) throws Exception {
		comprovantePagamentoServiceFacade.salvarComprovantePagamentoDAE(pessoaLogada, comprovantePagamentosDae);
		comprovantePagamentoServiceFacade.gerarProcesso(pessoaLogada, requerimentoUnicoDTO.getRequerimentoUnico(), new Area(areaSelecionada),
				pessoaRequerenteBoleto);
		pessoaRequerenteBoleto = null;
	}

	private boolean validacaoComprovante() {
		boolean lValido = true;

		if (Util.isNull(areaSelecionada)
				&& (!Util.isNullOuVazio(comprovantePagamentos)  || !Util.isNullOuVazio(comprovantePagamentosDae))&&
				validaComprovanteApresentado(comprovantePagamentos) && validaComprovanteApresentadoDae(comprovantePagamentosDae)) {
			lValido = false;
			JsfUtil.addErrorMessage("O campo Encaminhar para coordenação é de preenchimento obrigatório.");
		}
		return lValido;
	}

	private boolean validaComprovanteApresentado(List<ComprovantePagamentoDTO> pComprovantePagamentos) {
		for (ComprovantePagamentoDTO comprovantePagamentoDTO : pComprovantePagamentos) {
			if (!comprovantePagamentoDTO.getComprovantePagamento().getIndComprovanteValidado()) {
				return false;
			}
		}
		return true;
	}

	private boolean validaComprovanteApresentadoDae(List<ComprovantePagamentoDaeDTO> pComprovantePagamentosDae) {
		for (ComprovantePagamentoDaeDTO comprovantePagamentoDTO : pComprovantePagamentosDae) {
			if (!comprovantePagamentoDTO.getComprovantePagamentoDae().getIndComprovanteValidado()) {
				return false;
			}
		}
		return true;
	}

	public void enviarEmailPendenciaValidacao(){
		Exception erro =null;
		try {
			RequerimentoUnico requerimentoUnico = requerimentoUnicoService.buscarRequerimentoUnico(requerimentoUnicoDTO.getRequerimentoUnico());
			StatusRequerimento status = statusRequerimentoService.carregarGet(StatusRequerimentoEnum.PENDENCIA_VALIDACAO_COMPROVANTE.getStatus());
			gerarTramitacaoPendenciaValidacao(requerimentoUnico, status);
			comprovantePagamentoServiceFacade.enviarEmailPendenciaValidacao(requerimentoUnico.getRequerimento(), this.emailValidacao);
			RequestContext.getCurrentInstance().execute("emailValidacao.hide()");
			RequestContext.getCurrentInstance().execute("dialogBoletoValidarComprovante.hide()");
			JsfUtil.addSuccessMessage("E-mail de pendência enviado com sucesso.");
			this.emailValidacao = null;
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	private void gerarTramitacaoPendenciaValidacao(RequerimentoUnico requerimentoUnico, StatusRequerimento status) throws Exception {
		Pessoa usuarioLogado = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa();
		tramitacaoRequerimentoService.criarTramitacaoRequerimento(requerimentoUnico.getRequerimento(), status, usuarioLogado);
	}

	public boolean rendered(RequerimentoUnicoDTO pRequerimentoUnico, int acao) {
		switch (acao) {
		case 1: // editar
		case 2: // efetuar enquadramento
			return pRequerimentoUnico.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.AGUARDANDO_ENQUADRAMENTO.getStatus())
					|| pRequerimentoUnico.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.SENDO_ENQUADRADO.getStatus())
					|| pRequerimentoUnico.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.PENDENCIA_ENQUADRAMENTO.getStatus());
		case 3: // enviar documento
			return isEnquadrado(pRequerimentoUnico)
					|| pRequerimentoUnico.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.PENDENCIA_VALIDACAO.getStatus())
					|| pRequerimentoUnico.getStatusRequerimento().getIdeStatusRequerimento()
					.equals(StatusRequerimentoEnum.PENDENCIA_ENVIO_DOCUMENTACAO.getStatus());
		case 4: // efetuar validação prévia
			return pRequerimentoUnico.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.PENDENCIA_VALIDACAO.getStatus())
					|| pRequerimentoUnico.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.EM_VALIDACAO_PREVIA.getStatus());
		case 5: // gravar taxa boleto
			return pRequerimentoUnico.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.VALIDADO.getStatus());
		case 6: // enviar comprovante ou baixar boleto
			return pRequerimentoUnico.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.PAGAMENTO_LIBERADO.getStatus())
					|| pRequerimentoUnico.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.PENDENCIA_VALIDACAO_COMPROVANTE.getStatus());
		case 7: // validar comprovante, baixa manual e gerar processo
			return pRequerimentoUnico.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.COMPROVANTE_ENVIADO.getStatus());
		case 8: // gerar relatório de ato declaratório
			return validaRelatorioAtoDeclaratorio(pRequerimentoUnico) && !isConversaoTcraLac(pRequerimentoUnico) && !isLac(pRequerimentoUnico)
					&& !verificarAtoRegistroCorte(pRequerimentoUnico) && !verificarAtoRegistroFlorestaProducao(pRequerimentoUnico);
		case 9://visualizar Lac Erb
			return !Util.isNull(pRequerimentoUnico.getLac()) && isLacErb(pRequerimentoUnico) && isEnquadrado(pRequerimentoUnico)
					&& !isRequerimentoPago(pRequerimentoUnico);
		case 10://certificado Lac
			return (!Util.isNull(pRequerimentoUnico.getLac())) && isRequerimentoPago(pRequerimentoUnico);// &&
			// isProcessoConcluido(pRequerimentoUnico);
		case 11://relatorio final Lac
			return !Util.isNull(pRequerimentoUnico.getLac()) && isRequerimentoPago(pRequerimentoUnico);
		case 12://visualizar Lac Posto
			return !Util.isNull(pRequerimentoUnico.getLac()) && isLacPosto(pRequerimentoUnico) && isEnquadrado(pRequerimentoUnico);
		case 13:
			return false ;//&& isLacGenerica(pRequerimentoUnico);
		case 14://Gerar Certificado de Corte de Floresta
			return verificarAtoRegistroCorte(pRequerimentoUnico) && isRequerimentoPago(pRequerimentoUnico);
		case 15://Gerar Certificado de Floresta de Produção Plantada
			return verificarAtoRegistroFlorestaProducao(pRequerimentoUnico);
		case 16://relatorio lac Transportadora
			return !Util.isNull(pRequerimentoUnico.getLac()) && !pRequerimentoUnico.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.ENQUADRADO.getStatus()) ;
		case 17://relatorio FCE ASV
			return !Util.isNull(pRequerimentoUnico.getFce()) && !isEnquadrado(pRequerimentoUnico);
		case 18:
			return !pRequerimentoUnico.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.AGUARDANDO_ENQUADRAMENTO.getStatus())
					&& !pRequerimentoUnico.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.CANCELADO.getStatus())
					&& !pRequerimentoUnico.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.FORMADO.getStatus())
					&& isATEND();
		default:
			return false; 
		}
	}
	
	public Boolean isATEND() {
		PessoaFisica pf = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica();
		Area areaPessoaLogada = null;
		try{
			areaPessoaLogada = areaService.buscarAreaPorPessoaFisica(pf.getIdePessoaFisica());
			if(areaPessoaLogada != null) {
				return  areaPessoaLogada.getIdeArea().equals(AreaEnum.ATEND.getId());
			}
			return false;
		}
		catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return false;
		}
	}

	public Boolean isConversaoTcraLac(RequerimentoUnicoDTO pRequerimentoUnico) {
		return pRequerimentoUnico.getEmpreendimento().getIndConversaoTcraLac();
	}

	// private boolean isProcessoConcluido(RequerimentoUnicoDTO
	// pRequerimentoUnico) {
	// return StatusFluxoEnum.CONCLUIDO.getStatus() ==
	// pRequerimentoUnico.getStatusProcesso().getId();
	//}

	private boolean verificarAtoRegistroCorte(RequerimentoUnicoDTO pRequerimentoUnico) {
		Collection<AtoAmbiental> atosAmbientais = pRequerimentoUnico.getRequerimentoUnico().getAtosAmbientais();
		for (AtoAmbiental atoAmbiental : atosAmbientais) {
			if (atoAmbiental.getIdeAtoAmbiental().equals(AtoAmbientalEnum.CORTE_FLORESTA_PRODUCAO.getId())) {
				return true;
			}
		}
		return false;
	}

	private boolean verificarAtoRegistroFlorestaProducao(RequerimentoUnicoDTO pRequerimentoUnico) {
		Collection<AtoAmbiental> atosAmbientais = pRequerimentoUnico.getRequerimentoUnico().getAtosAmbientais();
		for (AtoAmbiental atoAmbiental : atosAmbientais) {
			if (atoAmbiental.getIdeAtoAmbiental().equals(AtoAmbientalEnum.REGISTRO_FLORESTA_PRODUCAO.getId())
					&& pRequerimentoUnico.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.FORMADO.getStatus())) {
				return true;
			}
		}
		return false;
	}


	public void changeTipoAto(ValueChangeEvent event) {
		Exception erro = null;
		try {
			TipoAto tipoAtoSelecionado = (TipoAto) event.getNewValue();
			if (!Util.isNull(tipoAtoSelecionado) && tipoAtoSelecionado.getIdeTipoAto() != 0 && tipoAtoSelecionado.getIdeTipoAto() != -1) {
				listaAto = (List<AtoAmbiental>) atoAmbientalService.obterAtoAmbientalPorTipoAto(tipoAtoSelecionado.getIdeTipoAto());
			} else {
				listaAto = new ArrayList<AtoAmbiental>();
			}
		} catch (Exception e) {
			erro=e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	private boolean isEnquadrado(RequerimentoUnicoDTO pRequerimentoUnico) {
		return pRequerimentoUnico.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.ENQUADRADO.getStatus());
	}

	public boolean isLacPosto(RequerimentoUnicoDTO pRequerimentoUnico) {
		return DocumentoObrigatorioEnum.POSTO.getId().equals(pRequerimentoUnico.getLac().getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio());
	}

	public boolean isLacErb(RequerimentoUnicoDTO pRequerimentoUnico) {
		return DocumentoObrigatorioEnum.ERB.getId().equals(pRequerimentoUnico.getLac().getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio());
	}

	public boolean isLac(RequerimentoUnicoDTO pRequerimentoUnico) {
		return !Util.isNull(pRequerimentoUnico.getLac()) && !Util.isNull(pRequerimentoUnico.getLac().getIdeLac());
	}

	public boolean isLacTransportadora(RequerimentoUnicoDTO pRequerimentoUnico) {
		return DocumentoObrigatorioEnum.TRANSPORTADORA.getId().equals(pRequerimentoUnico.getLac().getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio());
	}

	public boolean isFceAsv(RequerimentoUnicoDTO pRequerimentoUnico) {
		return DocumentoObrigatorioEnum.FCE_ASV.getId().equals(pRequerimentoUnico.getFce().getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio());
	}

	public boolean isLacGenerica(RequerimentoUnicoDTO pRequerimentoUnico) {
		for (AtoAmbiental ato : pRequerimentoUnico.getRequerimentoUnico().getAtosAmbientais()) {
			if (AtoAmbientalEnum.LAC.getId().equals(ato.getIdeAtoAmbiental()) && !isLacPosto(pRequerimentoUnico) && !isLacErb(pRequerimentoUnico)) {
				return true;
			}
		}
		return false;
	}

	public boolean isRequerimentoPago(RequerimentoUnicoDTO pRequerimentoUnico) {
		return pRequerimentoUnico.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.FORMADO.getStatus());
	}

	/*
	 * ZCR/RN0031. A opção Gerar relatório de ato declaratório só será exibida
	 * caso exista um processo relacionado ao Requerimento, o status do processo
	 * seja Concluído, e TODOS os atos relacionados ao processo sejam
	 * Declaratórios.
	 */
	private boolean validaRelatorioAtoDeclaratorio(RequerimentoUnicoDTO pRequerimentoUnicoDTO) {
		return processoRequerimentoServiceFacade.validaRelatorioAtoDeclaratorio(pRequerimentoUnicoDTO);
	}

	private boolean validaPeriodo() {
		if (!Util.validaPeriodo(periodoInicio, periodoFim)) {
			JsfUtil.addErrorMessage(BUNDLE.getString("geral_msg_periodo_invalido"));
			return false;
		}

		boolean requerenteValido = !Util.isNull(requerente) && !Util.isNull(requerente.getIdePessoa()) && requerente.getIdePessoa() > 0;
		boolean statusValido = !Util.isNull(statusRequerimento) && !Util.isNull(statusRequerimento.getIdeStatusRequerimento()) && statusRequerimento.getIdeStatusRequerimento() > 0;
		boolean municipioValido = !Util.isNull(municipioSelecionado) && !Util.isNull(municipioSelecionado.getIdeMunicipio()) && municipioSelecionado.getIdeMunicipio() > 0;
		if(Util.isNullOuVazio(numeroRequerimento) && !requerenteValido && !statusValido && !municipioValido){
			JsfUtil.addErrorMessage("Selecione ao menos um dos filtros: Requerente, Nº do Requerimento, Status, Localidade.");
			return false;
		}

		return true;
	}

	private void consultarEmpreendimentos() {
		Exception erro = null;
		try {
			// Filtrar por requerente
			if (!Util.isNull(requerente)) {
				empreendimentos = empreendimentoService.listarEmpreendimento(requerente);
			}
		} catch (Exception e) {
			erro=e;
			JsfUtil.addErrorMessage("Não foi possível carregar a lista de empreendimentos - " + e.getMessage());
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	private void consultarStatusRequerimentos() {
		Exception erro =null;
		try {
			statusRequerimentos = statusRequerimentoService.listarStatusRequerimento();
		} catch (Exception e) {
			erro=e;
			JsfUtil.addErrorMessage("Não foi possível carregar a lista de status - " + e.getMessage());
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	/**
	 * Verifica se foi enquadrado nos atos RLU ou RLO
	 * */
	public void verificaAto() {
		Enquadramento enquadramentoValidado = new Enquadramento();
		if (!Util.isNull(requerimentoUnicoDTO)) {
			Exception erro = null;
			try {
				enquadramentoValidado = enquadramentoService.buscarEnquadramentoComAtos(requerimentoUnicoDTO.getRequerimentoUnico());
				listaAtoAmbiental = (List<AtoAmbiental>) enquadramentoValidado.getAtoAmbientalCollection();
			} catch (Exception e) {
				erro=e;
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}

			if(!Util.isNullOuVazio(enquadramentoValidado)) {
				for (AtoAmbiental lAtoAmbiental : enquadramentoValidado.getAtoAmbientalCollection()) {
					isAtoRloRlu = lAtoAmbiental.getIdeAtoAmbiental().equals(7) || lAtoAmbiental.getIdeAtoAmbiental().equals(48);
					if (isAtoRloRlu) {
						break;
					}
				}
			}
		} else {
			isAtoRloRlu = false;
		}
	}

	private void consultarAreas(RequerimentoUnico pRequerimento, Collection<Tipologia> tipologias) {
		Exception erro =null;
		try {

			verificaAto();

			if (!this.calculoPorteAtivo || (!Util.isNull(boleto)  && boleto.isZerado())) {
				areasRequerimento = (List<Area>) areaService.listarCoordenacoesAnexo(isAtoRloRlu);
			} else {
				areasRequerimento = areaService.listarPorRequerimento(pRequerimento, isAtoRloRlu);
			}
			if(!Util.isNullOuVazio(pRequerimento.getIdePorte()) && pRequerimento.getIdePorte().getNomPorte().equals("Pequeno")|| pRequerimento.getIdePorte().getNomPorte().equals("Médio")){
				if(Util.isNullOuVazio(empreendimento) || Util.isNullOuVazio(empreendimento.getIdeEmpreendimento())){
					this.empreendimento = this.empreendimentoService.carregarByIdeRequerimento(pRequerimento.getIdeRequerimentoUnico());
				}
				if(!Util.isNullOuVazio(empreendimento.getEndereco().getIdeLogradouro().getIdeBairro().getIdeMunicipio())){
					Area area = areaService.filtrarAreaUrMunicipio(empreendimento.getEndereco().getIdeLogradouro().getIdeBairro().getIdeMunicipio());
					if(!Util.isNullOuVazio(area.getIdeArea())){
						areasRequerimento.add(area);
					}
				}
			}
		} catch (Exception e) {
			erro =e;
			JsfUtil.addErrorMessage("Não foi possível carregar a lista de áreas - " + e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public List<SelectItem> getAreas() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		if (!Util.isNull(areasRequerimento)) {
			for (Area area : areasRequerimento) {
				itens.add(new SelectItem(area.getIdeArea(), area.getNomArea()));
			}
		}
		return itens;
	}

	public void carregarTipoIntervencao() {
		Exception erro =null;
		try {
			Collection<TipoIntervencao> collTipoIntervencao = requerimentoUnicoService.listarTodosTipoIntervencao();
			for (TipoIntervencao tipoIntervencao : collTipoIntervencao) {
				SelectItem item = new SelectItem(tipoIntervencao.getIdeTipoIntervencao(), tipoIntervencao.getNomTipoIntervencao());
				this.collTipoIntervencaoData.add(item);
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void carregarTipoFinalidadeUsoAgua() {
		Exception erro = null;
		try {
			Collection<TipoFinalidadeUsoAgua> collTipoFinalidadeUsoAgua = requerimentoUnicoService.listarTodosTipoFinalidadeUsoAgua();
			for (TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua : collTipoFinalidadeUsoAgua) {
				SelectItem item = new SelectItem(tipoFinalidadeUsoAgua.getIdeTipoFinalidadeUsoAgua(), tipoFinalidadeUsoAgua.getNomTipoFinalidadeUsoAgua());
				this.collTipoFinalidadeUsoAgua.add(item);
			}
		} catch (Exception e) {
			erro= e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void carregarTipoReceptor() {
		Exception erro = null;
		try {
			Collection<TipoReceptor> collTipoReceptor = requerimentoUnicoService.listarTodosTipoReceptor();
			for (TipoReceptor tipoReceptor : collTipoReceptor) {
				SelectItem item = new SelectItem(tipoReceptor.getIdeTipoReceptor(), tipoReceptor.getNomTipoReceptor());
				this.collTipoTipoReceptor.add(item);
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void carregarTipoCapitacao() {
		Exception erro =null;
		try {
			Collection<TipoCaptacao> collTipoCapitacao = requerimentoUnicoService.listarTodosTipoCaptacao();
			for (TipoCaptacao tipoCaptacao : collTipoCapitacao) {
				SelectItem item = new SelectItem(tipoCaptacao.getIdeTipoCaptacao(), tipoCaptacao.getNomTipoCaptacao());
				this.collTipoCaptacao.add(item);
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void carregarObjetivoRequerimentoLimpezaArea() {
		Exception erro =null;
		try {
			List<ObjetivoLimpezaArea> listLimpArea = (List<ObjetivoLimpezaArea>) requerimentoUnicoService.listarTodosObjetivoLimpezaArea();
			if(Util.isNullOuVazio(collObjetivoRequerimentoLimpezaArea)) {
				collObjetivoRequerimentoLimpezaArea = new ArrayList<ObjetivoRequerimentoLimpezaArea>();
			}
			for (ObjetivoLimpezaArea objetivoLimpezaArea : listLimpArea) {
				ObjetivoRequerimentoLimpezaArea objReqLimpAreaTemp = new ObjetivoRequerimentoLimpezaArea();
				objReqLimpAreaTemp.setIdeObjetivoLimpezaArea(objetivoLimpezaArea);
				collObjetivoRequerimentoLimpezaArea.add(objReqLimpAreaTemp);
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void carregarObjetivoLimpezaArea() {
		Exception erro =null;
		try {
			this.collObjetivoLimpezaArea = (List<ObjetivoLimpezaArea>) requerimentoUnicoService.listarTodosObjetivoLimpezaArea();
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void carregarFaseEmpreendimento() {
		Exception erro =null;
		try {
			Collection<FaseEmpreendimento> collFaseEmpreendimento = requerimentoUnicoService.listarTodosFaseEmpreendimento();
			for (FaseEmpreendimento faseEmpreendimento : collFaseEmpreendimento) {
				SelectItem item = new SelectItem(faseEmpreendimento.getIdeFaseEmpreendimento(), faseEmpreendimento.getNomFaseEmpreendimento());
				this.collFaseEmpreendimento.add(item);
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void carregarEmpreendimento() {
		Exception erro = null;
		try {
			// Collection<Empreendimento> collEmpreendimento;
			Collection<Empreendimento> collEmpreendimento = requerimentoUnicoService.listarEmpreendimentoPorPessoa(ContextoUtil.getContexto().getReqPapeisDTO()
					.getRequerente().getPessoa());
			/*
			 * //MUDEI TESTAR if
			 * (ContextoUtil.getContexto().getReqPapeisDTO().getRequerente
			 * ().isIndSolicitante() ||
			 * (ContextoUtil.getContexto().getReqPapeisDTO
			 * ().getSolicitante().getIdeTipoPessoaRequerimento() != null &&
			 * ContextoUtil.getContexto()
			 * .getReqPapeisDTO().getSolicitante().getIdeTipoPessoaRequerimento
			 * ().getIdeTipoPessoaRequerimento()
			 * .equals(TipoPessoaRequerimentoEnum.REPRESENTANTE_LEGAL.getId())))
			 * { } else { collEmpreendimento =
			 * requerimentoUnicoService.listarEmpreendimentoPorProcurador
			 * (ContextoUtil.getContexto().getReqPapeisDTO().getSolicitante()
			 * .getPessoa(),
			 * ContextoUtil.getContexto().getReqPapeisDTO().getRequerente
			 * ().getPessoa()); }
			 */
			for (Empreendimento empreendimento : collEmpreendimento) {
				SelectItem item = new SelectItem(empreendimento.getIdeEmpreendimento(), empreendimento.getNomEmpreendimento());
				this.collEmpreendimento.add(item);
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public String incluirEmpreendimento() {
		if(!Util.isNull(pessoa)){
			ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().setPessoa(pessoa);
		}
		return "/paginas/manter-empreendimento/empreendimento.xhtml";
	}

	public String voltar() {
		return urlVoltar;
	}

	public void limparDadosBoleto() throws Exception {
		boleto = new BoletoPagamentoRequerimento();
		boletoDaeRequerimeno = new BoletoDaeRequerimento();
		disableVistoria = Boolean.FALSE;
		showEmissaoDae = Boolean.FALSE;
		this.areaSelecionada = null;
	}

	public void carregarDadosTipologiaEmpreendimento() throws Exception {
		Collection<Tipologia> tipologias = this.empreendimentoService.buscarTipologias(requerimentoUnicoDTO.getEmpreendimento(), false, true);
		this.empreendimento.setTipologias(tipologias);
		this.exibeCoordenacoes = false;
	}
	
	public void carregarLocalizacaoGeografica() {
		try {
			this.localizacaoGeograficaSelecionada = localizacaoGeograficaService.carregar(this.localizacaoGeograficaSelecionada);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void salvarBoletoDaeRequerimento() {
		boletoDaeRequerimeno.setIdeRequerimento(requerimentoUnicoDTO.getRequerimentoUnico().getRequerimento());
		boletoDaeRequerimeno.setIdePessoa(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa());
		Exception erro =null;
		try {
			boletoDaeRequerimentoService.salvarOuAtualizar(boletoDaeRequerimeno);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void calcularValorTaxaCertificado() {
		this.validarBoletoRequerimento();
		Exception erro = null;
		try {
			DecimalFormat df = Util.getDecimalFormatPtBr();
			Requerimento requerimento = this.requerimentoUnicoDTO.getRequerimentoUnico().getRequerimento();
			this.boletoDaeRequerimeno.setIdeRequerimento(requerimento);
			Double valorTotalCertificado = boletoDaeRequerimentoService.calcularValorTotalCertificado(boletoDaeRequerimeno,
					this.getListaParametrosTaxaRequerimento());
			boletoDaeRequerimeno.setVlrTotalCertificado(BigDecimal.valueOf(valorTotalCertificado));
			boletoDaeRequerimeno.setVlrTotalCertificadoFormatado(df.format(valorTotalCertificado));
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void calcularValorTaxaVistoria() {
		Exception erro =null;
		try {
			DecimalFormat df = Util.getDecimalFormatPtBr();
			BigDecimal valorTotalVistoria = boletoDaeRequerimentoService.calcularValorTotalVistoria(boletoDaeRequerimeno);
			boletoDaeRequerimeno.setVlrTotalVistoria(valorTotalVistoria);
			boletoDaeRequerimeno.setVlrTotalVistoriaFormatado(df.format(valorTotalVistoria));
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void verificarValorTaxaVistoria(ValueChangeEvent event) {
		DecimalFormat df = Util.getDecimalFormatPtBr();
		if ((Boolean) event.getNewValue()) {
			disableVistoria = Boolean.TRUE;
			boletoDaeRequerimeno.setVlrAreaVistoria(new BigDecimal("0.00"));
			boletoDaeRequerimeno.setVlrTotalVistoria(new BigDecimal("0.00"));
			boletoDaeRequerimeno.setVlrTotalVistoriaFormatado(df.format(new BigDecimal("0.00")));
			RequestContext.getCurrentInstance().addPartialUpdateTarget("formBoleto:vlrAreaVistoria");
			RequestContext.getCurrentInstance().addPartialUpdateTarget("formBoleto:valorTotalVistoria");
		} else {
			disableVistoria = Boolean.FALSE;
			RequestContext.getCurrentInstance().addPartialUpdateTarget("formBoleto:vlrAreaVistoria");
			RequestContext.getCurrentInstance().addPartialUpdateTarget("formBoleto:valorTotalVistoria");
		}
	}

	public void trataArquivoDaeCertificado(FileUploadEvent event) {
		Exception erro =null;
		
		try {
			// Obtem o requerimento selecionado na tela.
			RequerimentoUnico lRequerimento = requerimentoUnicoDTO.getRequerimentoUnico();
			lRequerimento = requerimentoUnicoService.buscarRequerimentoUnico(lRequerimento);
			fileUploadDaeCertificado = event.getFile();
			caminhoArquivoDaeCertificado = gerenciaArquivoservice.uploadArquivoComprovanteBoletoDae(fileUploadDaeCertificado, lRequerimento.getRequerimento()
					.getBoletoDaeRequerimento(), "certificado_dae");
			temArquivoDaeCertificado = true;
			if (listaArquivoDaeCertificado == null) {
				listaArquivoDaeCertificado = new ArrayList<String>();
			}
			listaArquivoDaeCertificado.clear();
			listaArquivoDaeCertificado.add(FileUploadUtil.getFileName(caminhoArquivoDaeCertificado));
			JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
		} catch (Exception e) {
			erro =e;
			JsfUtil.addErrorMessage("Ocorreu um erro ao gerar o boleto: " + e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void trataArquivoDaeVistoria(FileUploadEvent event) {
		Exception erro =null;
		try {
			// Obtem o requerimento selecionado na tela.
			RequerimentoUnico lRequerimento = requerimentoUnicoDTO.getRequerimentoUnico();
			lRequerimento = requerimentoUnicoService.buscarRequerimentoUnico(lRequerimento);
			fileUploadDaeVistoria = event.getFile();
			caminhoArquivoDaeVistoria = gerenciaArquivoservice.uploadArquivoComprovanteBoletoDae(fileUploadDaeVistoria, lRequerimento.getRequerimento()
					.getBoletoDaeRequerimento(), "vistoria_dae");
			temArquivoDaeVistoria = true;
			if (listaArquivoDaeVistoria == null) {
				listaArquivoDaeVistoria = new ArrayList<String>();
			}
			listaArquivoDaeVistoria.clear();
			listaArquivoDaeVistoria.add(FileUploadUtil.getFileName(caminhoArquivoDaeVistoria));
			JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
		} catch (Exception e) {
			erro =e;
			JsfUtil.addErrorMessage("Ocorreu um erro ao gerar o boleto: " + e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void trataArquivoValidacaoBoleto(FileUploadEvent event) {
		Exception erro =null;
		try {
			// Obtem o requerimento selecionado na tela.
			RequerimentoUnico lRequerimento = requerimentoUnicoDTO.getRequerimentoUnico();
			lRequerimento = requerimentoUnicoService.buscarRequerimentoUnico(lRequerimento);
			fileUpload = event.getFile();
			if (comprovanteDae) {
				caminhoArquivo = gerenciaArquivoservice.uploadArquivoComprovanteBoletoDae(fileUpload, lRequerimento.getRequerimento()
						.getBoletoDaeRequerimento(), "boleto");
				comprovantePagamentoDaeService.salvarOuAtualizar(comprovantePagamentoDae);
			} else {
				caminhoArquivo = gerenciaArquivoservice.uploadArquivoComprovanteBoleto(fileUpload, lRequerimento.getRequerimento()
						.getBoletoPagamentoRequerimento());
				comprovantePagamentoService.salvarAtualizar(comprovante);
			}
			prepararValidarBoleto(requerimentoUnicoDTO);
			RequestContext.getCurrentInstance().execute("fileUploadComprovantes.hide()");
			RequestContext.getCurrentInstance().addPartialUpdateTarget("validarComprovanteForm:tabelaComprovantes");
			RequestContext.getCurrentInstance().addPartialUpdateTarget("validarComprovanteForm:tabelaComprovantesDae");
			JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public void verificarEmissaoDae() {
		RequerimentoUnico lRequerimento = requerimentoUnicoDTO.getRequerimentoUnico();
		Exception erro = null; 
		try {
			lRequerimento = requerimentoUnicoService.buscarRequerimentoUnico(lRequerimento);
			if (!Util.isNull(lRequerimento.getRequerimento().getBoletoDaeRequerimento())) {
				showUploadComprovanteDae = Boolean.TRUE;
				showUploadComprovanteDaeVistoria = Boolean.FALSE;
				if (!Util.isNull(lRequerimento.getRequerimento().getBoletoDaeRequerimento().getVlrTotalVistoria())
						&& lRequerimento.getRequerimento().getBoletoDaeRequerimento().getVlrTotalVistoria().compareTo(new BigDecimal("0.00")) != 0) {
					showUploadComprovanteDaeVistoria = Boolean.TRUE;
				}
			} else {
				showUploadComprovanteDae = Boolean.FALSE;
				showUploadComprovanteDaeVistoria = Boolean.FALSE;
			}
			this.existeBoletoPagamento = !Util.isNull(boletoPagamentoRequerimentoService.carregarByRequerimento(lRequerimento.getIdeRequerimentoUnico()));
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	private boolean salvarUploadComprovanteDaeCertificado() {
		Exception erro =null;
		try {
			// Obtem o requerimento selecionado na tela.
			RequerimentoUnico lRequerimento = requerimentoUnicoDTO.getRequerimentoUnico();
			lRequerimento = requerimentoUnicoService.buscarRequerimentoUnico(lRequerimento);
			// Salva arquivo no diretório
			String lNomeArquivo = this.caminhoArquivoDaeCertificado ; // gerenciaArquivoservice.uploadArquivoComprovanteBoleto(fileUpload,
			// lRequerimento.getRequerimento().getBoletoPagamentoRequerimento());
			// Registra o comprovante na base
			if (Util.isNull(comprovantePagamentoDaeCertificado)) {
				comprovantePagamentoDaeCertificado = new ComprovantePagamentoDae();
			}
			comprovantePagamentoDaeCertificado.setIdePessoaUpload(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa());
			comprovantePagamentoDaeCertificado.setIndComprovanteValidado(false);
			comprovantePagamentoDaeCertificado.setDscCaminhoArquivo(lNomeArquivo);
			// comprovantePagamentoServiceFacade.registraComprovanteDae(comprovantePagamentoDae,
			// lRequerimento.getRequerimento());
			// Atualiza componentes na tela
			// Recarrega a lista de requerimento
			// consultar();
			fileUploadDaeCertificado = null;
			// RequestContext.getCurrentInstance().execute("dialogBoletoDownload.hide()");
			// RequestContext.getCurrentInstance().addPartialUpdateTarget("boletoDownloadForm:boletoDownloadCampos");
			temArquivoDaeCertificado = false;
			listaArquivoDaeCertificado.clear();
			// JsfUtil.addSuccessMessage(BUNDLE.getString("requerimento_msg_comprovante_sucesso"));
			return true;
		} catch (Exception e) {
			erro =e;
			JsfUtil.addErrorMessage("Ocorreu um erro ao salvar o comprovante: " + e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return false;
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	private boolean salvarUploadComprovanteDaeVistoria() {
		Exception erro =null;
		try {
			// Obtem o requerimento selecionado na tela.
			RequerimentoUnico lRequerimento = requerimentoUnicoDTO.getRequerimentoUnico();
			lRequerimento = requerimentoUnicoService.buscarRequerimentoUnico(lRequerimento);
			// Salva arquivo no diretório
			String lNomeArquivo = caminhoArquivoDaeVistoria; // gerenciaArquivoservice.uploadArquivoComprovanteBoleto(fileUpload,
			// lRequerimento.getRequerimento().getBoletoPagamentoRequerimento());
			// Registra o comprovante na base
			if (Util.isNull(comprovantePagamentoDaeVistoria)) {
				comprovantePagamentoDaeVistoria = new ComprovantePagamentoDae();
			}
			comprovantePagamentoDaeVistoria.setIdePessoaUpload(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa());
			comprovantePagamentoDaeVistoria.setIndComprovanteValidado(false);
			comprovantePagamentoDaeVistoria.setDscCaminhoArquivo(lNomeArquivo);
			// comprovantePagamentoServiceFacade.registraComprovanteDae(comprovantePagamentoDae,
			// lRequerimento.getRequerimento());
			// Atualiza componentes na tela
			// Recarrega a lista de requerimento
			// consultar();
			fileUploadDaeVistoria = null;
			// RequestContext.getCurrentInstance().execute("dialogBoletoDownload.hide()");
			// RequestContext.getCurrentInstance().addPartialUpdateTarget("boletoDownloadForm:boletoDownloadCampos");
			temArquivoDaeVistoria = false;
			listaArquivoDaeVistoria.clear();
			// JsfUtil.addSuccessMessage(BUNDLE.getString("requerimento_msg_comprovante_sucesso"));
			return true;
		} catch (Exception e) {
			erro =e;
			JsfUtil.addErrorMessage("Ocorreu um erro ao salvar o comprovante: " + e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return false;
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public String redirecionarEnquadramento(RequerimentoUnicoDTO pRequerimentoUnico){
		Exception erro = null;
		try {
			if(!Util.isNull(processoService.buscarPorRequerimento(pRequerimentoUnico.getRequerimentoUnico().getIdeRequerimentoUnico()))){
				JsfUtil.addWarnMessage("Já existe processo formado para este requerimento. Favor entrar em contato com a Central de Atendimento!");
				return null;
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		return "/paginas/requerimentos/incluirRequerimentoUnico.xhtml";


	}

	public void salvarRequerimentoParcial(){
		
		Exception erro = null;
		try {
			this.removerRequerimentosIncompletos(this.empreendimento);
			this.validarEmpreendimento();
			this.salvarRequerimentoParcialmente();
			Collection<ProcessoTramite> collProcessoTramite = requerimentoUnico.getRequerimento().getProcessoTramiteCollection();
			requerimentoUnico.getRequerimento().setProcessoTramiteCollection(new ArrayList<ProcessoTramite>());
			requerimentoUnicoService.inserirRequerimento(requerimentoUnico.getRequerimento());
			requerimentoUnico.getRequerimento().setProcessoTramiteCollection(collProcessoTramite);
			for (ProcessoTramite processoTramite : collProcessoTramite) {
				processoTramite.setIdeRequerimento(requerimentoUnico.getRequerimento());
			}
			ContextoUtil.getContexto().setListaObjetReqLimpArea(new ArrayList<ObjetivoRequerimentoLimpezaArea>());
			collObjetivoRequerimentoLimpezaArea = new ArrayList<ObjetivoRequerimentoLimpezaArea>();
			this.collObjetivoLimpezaArea = new ArrayList<ObjetivoLimpezaArea>();
			carregarObjetivoLimpezaArea();
			carregarObjetivoRequerimentoLimpezaArea();
			carregarPerguntas();
			constroiValueChangeDasPerguntasDoAtoFlorestal(requerimentoUnico);
			JsfUtil.addSuccessMessage("Requerimento salvo com sucesso.");
		} catch (Exception e) {
			erro =e;
			//JsfUtil.addSuccessMessage(e.getMessage());
			System.err.println(e.getMessage());
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}

	}


	private void removerRequerimentosIncompletos(Empreendimento empreendimento) {
		this.requerimentoService.removerRequerimentosIncompletos(empreendimento.getIdeEmpreendimento());
	}


	public void salvarRequerimentoParcialmente() throws Exception {
		if (this.enderecoContato) {
			requerimentoUnico.getRequerimento().setIdeEnderecoContato(ContextoUtil.getContexto().getRequerimentoEndereco());
		} else {
			requerimentoUnico.getRequerimento().setIdeEnderecoContato(null);
		}



		Orgao orgao = new Orgao();
		orgao.setIdeOrgao(1);
		orgao = requerimentoUnicoService.recuperarOrgao(orgao);
		TipoRequerimento tipoRequerimento = new TipoRequerimento();
		tipoRequerimento.setIdeTipoRequerimento(1);
		requerimentoUnico.getRequerimento().setIdeOrgao(orgao);
		requerimentoUnico.getRequerimento().setIdeTipoRequerimento(tipoRequerimento);
		requerimentoUnico.getRequerimento().setDtcCriacao(new Date());
		requerimentoUnico.getRequerimento().setIndDeclaracao(true);
		requerimentoUnico.getRequerimento().setDtcCriacao(new Date());
		requerimentoUnico.getRequerimento().setIndExcluido(false);
		
		/*requerimentoUnico.getRequerimento().setEmpreendimentoCollection(new ArrayList<Empreendimento>());
		requerimentoUnico.getRequerimento().getEmpreendimentoCollection().add(empreendimento);*/

		TramitacaoRequerimento tramitacaoRequerimento = new TramitacaoRequerimento();
		tramitacaoRequerimento.setIdePessoa(this.pessoa);
		StatusRequerimento statusRequeri = new StatusRequerimento();
		statusRequeri.setIdeStatusRequerimento(StatusRequerimentoEnum.AGUARDANDO_ENQUADRAMENTO.getStatus());
		tramitacaoRequerimento.setIdeStatusRequerimento(statusRequeri);
		requerimentoUnico.getRequerimento().setTramitacaoRequerimentoCollection(new ArrayList<TramitacaoRequerimento>());
		requerimentoUnico.getRequerimento().getTramitacaoRequerimentoCollection().add(tramitacaoRequerimento);

		// Definiï¿½ï¿½o Requerimento Pessoa
		List<RequerimentoPessoa> pessoasRequerimentos = new ArrayList<RequerimentoPessoa>();
		RequerimentoPessoa reqPessoaAtendente = ContextoUtil.getContexto().getReqPapeisDTO().getAtendente();
		RequerimentoPessoa reqPessoaRequerente = ContextoUtil.getContexto().getReqPapeisDTO().getRequerente();
		RequerimentoPessoa reqPessoaSolicitante = ContextoUtil.getContexto().getReqPapeisDTO().getSolicitante();
		pessoasRequerimentos.add(reqPessoaRequerente);
		if (!reqPessoaRequerente.isIndSolicitante()) {
			pessoasRequerimentos.add(reqPessoaSolicitante);
		}
		if (!reqPessoaRequerente.isIndUsuarioLogado() && !reqPessoaSolicitante.isIndUsuarioLogado()) {
			pessoasRequerimentos.add(reqPessoaAtendente);
		}
		List<ProcuradorPessoaFisica> collProcuradorPessoaFisica = requerimentoUnicoService
				.recuperarProcuradorPessoaFisicaAssinaturaObrigatoriaPorRequerenteEmpreendimento(pessoa, empreendimento);
		for (ProcuradorPessoaFisica procuradorPessoaFisica : collProcuradorPessoaFisica) {
			RequerimentoPessoa requerimentoPessoa = new RequerimentoPessoa();
			requerimentoPessoa.setPessoa(procuradorPessoaFisica.getIdeProcurador().getPessoa());
			TipoPessoaRequerimento tipoPessoaRequerimento = new TipoPessoaRequerimento();
			tipoPessoaRequerimento.setIdeTipoPessoaRequerimento(TipoPessoaRequerimentoEnum.PROCURADOR.getId());
			requerimentoPessoa.setIdeTipoPessoaRequerimento(tipoPessoaRequerimento);
			pessoasRequerimentos.add(requerimentoPessoa);
		}
		List<ProcuradorRepresentante> collProcuradorRepresentante = requerimentoUnicoService
				.recuperarProcuradorRepresentanteAssinaturaObrigatoriaPorRequerenteEmpreendimento(pessoa, empreendimento);
		for (ProcuradorRepresentante procuradorRepresentante : collProcuradorRepresentante) {
			RequerimentoPessoa requerimentoPessoa = new RequerimentoPessoa();
			requerimentoPessoa.setPessoa(procuradorRepresentante.getIdeProcurador().getPessoa());
			TipoPessoaRequerimento tipoPessoaRequerimento = new TipoPessoaRequerimento();
			tipoPessoaRequerimento.setIdeTipoPessoaRequerimento(TipoPessoaRequerimentoEnum.PROCURADOR.getId());
			requerimentoPessoa.setIdeTipoPessoaRequerimento(tipoPessoaRequerimento);
			boolean temPessoaRequerimento = false;
			for (RequerimentoPessoa reqPes : pessoasRequerimentos) {
				if (reqPes.getIdeTipoPessoaRequerimento().equals(requerimentoPessoa.getIdeTipoPessoaRequerimento())
						&& reqPes.getPessoa().equals(requerimentoPessoa.getPessoa())) {
					temPessoaRequerimento = true;
					break;
				}
			}
			if (!temPessoaRequerimento) {
				pessoasRequerimentos.add(requerimentoPessoa);
			}
		}
		List<RepresentanteLegal> collRepresentanteLegal = requerimentoUnicoService.recuperarRepresentanteLegalAssinaturaObrigatoriaPorRequerente(pessoa);
		for (RepresentanteLegal representantelegal : collRepresentanteLegal) {
			RequerimentoPessoa requerimentoPessoa = new RequerimentoPessoa();
			requerimentoPessoa.setPessoa(representantelegal.getIdePessoaFisica().getPessoa());
			TipoPessoaRequerimento tipoPessoaRequerimento = new TipoPessoaRequerimento();
			tipoPessoaRequerimento.setIdeTipoPessoaRequerimento(TipoPessoaRequerimentoEnum.REPRESENTANTE_LEGAL.getId());
			requerimentoPessoa.setIdeTipoPessoaRequerimento(tipoPessoaRequerimento);
			boolean temPessoaRequerimento = false;
			for (RequerimentoPessoa reqPes : pessoasRequerimentos) {
				if (reqPes.getIdeTipoPessoaRequerimento().equals(requerimentoPessoa.getIdeTipoPessoaRequerimento())
						&& reqPes.getPessoa().equals(requerimentoPessoa.getPessoa())) {
					temPessoaRequerimento = true;
					break;
				}
			}
			if (!temPessoaRequerimento) {
				pessoasRequerimentos.add(requerimentoPessoa);
			}
		}
		for (RequerimentoPessoa reqPess : pessoasRequerimentos) {
			reqPess.setRequerimento(requerimentoUnico.getRequerimento());
		}
		requerimentoUnico.getRequerimento().setRequerimentoPessoaCollection(pessoasRequerimentos);
	}

	public Boolean getSalvoParcialmente(){
		return !Util.isNull(requerimentoUnico.getRequerimento()) && !Util.isNull(requerimentoUnico.getRequerimento().getIdeRequerimento()) && Util.isNull(requerimentoUnico.getRequerimento().getNumRequerimento());
	}

	public Boolean getFormularioLacCompleto(RequerimentoUnicoDTO requerimentoUnico) {
		if (isLacPosto(requerimentoUnico) || isLacErb(requerimentoUnico)) {
			Collection<LacLegislacao> legislacoesAceitasLac;
			try {
				legislacoesAceitasLac = this.lacLegislacaoService.carregarByIdeRequerimentoWithLac(requerimentoUnico.getRequerimentoUnico()
						.getIdeRequerimentoUnico());
				return !Util.isNullOuVazio(legislacoesAceitasLac);
			} catch (Exception e) {
				return true;
			}
		}
		return true;
	}

	private List<AtoAmbiental> listaAtoAmbiental;

	public List<AtoAmbiental> getListaAtosDoRequerimentoDTO() {
		Enquadramento enquadramentoValidado = new Enquadramento();
		if (!Util.isNull(requerimentoUnicoDTO)) {
			Exception erro = null;
			try {
				enquadramentoValidado = enquadramentoService.buscarEnquadramentoComAtos(requerimentoUnicoDTO.getRequerimentoUnico());
				listaAtoAmbiental = (List<AtoAmbiental>) enquadramentoValidado.getAtoAmbientalCollection();
			} catch (Exception e) {
				erro =e;
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
			if(!Util.isNullOuVazio(enquadramentoValidado)) {
				for (AtoAmbiental lAtoAmbiental : enquadramentoValidado.getAtoAmbientalCollection()) {
					isAtoOutorga = lAtoAmbiental.getIdeTipoAto().getIdeTipoAto().equals(4);
					if (isAtoOutorga) {
						break;
					}
				}
			}
		}else{
			isAtoOutorga = false;
		}

		return Util.isNull(listaAtoAmbiental) ? new ArrayList<AtoAmbiental>() : listaAtoAmbiental;
	}

	public String getDescricaoListaAtosFlorestaisRequerimento(){
		StringBuilder descricao = new StringBuilder();
		if (!Util.isNull(requerimentoUnicoDTO)) {
			Exception erro = null;
			try {
				Integer ideRequerimentoUnico = requerimentoUnicoDTO.getRequerimentoUnico().getIdeRequerimentoUnico();
				List<ParametroTaxaCertificado> atosFlorestaisPorRequerimentoUnico = atoAmbientalService
						.buscarParametrosTaxaPorRequerimentoUnico(ideRequerimentoUnico);
				int count = 0;
				for (ParametroTaxaCertificado parametro : atosFlorestaisPorRequerimentoUnico) {
					String nomeAto = parametro.getAtoAmbiental().getNomAtoAmbiental();
					if(count != 0){
						this.apensarDescricao(descricao, nomeAto);
					}else{
						descricao.append(nomeAto);
					}
					count++;
				}

			} catch (Exception e) {
				erro =e;
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
		}
		return descricao.toString();
	}

	private void apensarDescricao(StringBuilder descricao, String nomeAto) {
		if(!descricao.toString().contains(nomeAto)){
			descricao.append(", ");
			descricao.append(nomeAto);
		}
	}

	public List<ParametroTaxaCertificado> getListaParametrosTaxaRequerimento(){
		if (!Util.isNull(requerimentoUnicoDTO)) {
			Exception erro = null;
			try {
				Integer ideRequerimentoUnico = requerimentoUnicoDTO.getRequerimentoUnico().getIdeRequerimentoUnico();
				return atoAmbientalService.buscarParametrosTaxaPorRequerimentoUnico(ideRequerimentoUnico);
			} catch (Exception e) {
				erro =e;
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
		}
		return new ArrayList<ParametroTaxaCertificado>();
	}

	public void alterarLayoutBoleto(ValueChangeEvent event) {
		Boolean value = (Boolean) event.getNewValue();
		if (value) {
			showEmissaoDae = Boolean.TRUE;
		} else {
			showEmissaoDae = Boolean.FALSE;
		}
		RequestContext.getCurrentInstance().addPartialUpdateTarget("formBoleto:panelEmissaoDae");
		RequestContext.getCurrentInstance().execute("dialogBoletoIncluir.show()");
	}

	public Boolean getDesabilitaSalvarShape() {
		boolean fShp, fDbf, fShx;
		fShp = false;
		fDbf = false;
		fShx = false;

		if (!listaArquivo.isEmpty()) {
			for (String arquivo : listaArquivo) {
				String teste[] = new String[2];
				teste = arquivo.split("\\.");
				if (teste[1].equals("shp")) {
					fShp = true;
				} else if (teste[1].equals("dbf")) {
					fDbf = true;
				} else if (teste[1].equals("shx")) {
					fShx = true;
				} else {
					break;
				}
			}
		}

		if (fShp && fDbf && fShx) {
			return false;
		} else {
			return true;
		}
	}

	public List<SelectItem> getListaRadios() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem(new Boolean(true), "Sim"));
		itens.add(new SelectItem(new Boolean(false), "Não"));
		return itens;
	}

	public Boolean getProcessoTramiteAgua() {
		return processoTramiteAgua;
	}

	public void setProcessoTramiteAgua(Boolean processoTramiteAgua) {
		this.processoTramiteAgua = processoTramiteAgua;
	}

	public RequerimentoUnico getRequerimentoUnico() {
		return requerimentoUnico;
	}

	public void setRequerimentoUnico(RequerimentoUnico requerimentoUnico) {
		this.requerimentoUnico = requerimentoUnico;
	}

	public void obterClassificacoes() {
		try {
			itemsClassifSecGeometrica = new ArrayList<SelectItem>();
			itemsClassifSecGeometrica.add(new SelectItem("", ResourceBundle.getBundle("/Bundle").getString("geral_lbl_selecione")));
			for (ClassificacaoSecaoGeometrica classifSecGeometrica : serviceClassifSecGeometrica.listarClassificacaoSecaoGeometrica()) {
				itemsClassifSecGeometrica.add(new SelectItem(classifSecGeometrica, classifSecGeometrica.getNomClassificacaoSecao()));
			}
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public Collection<SelectItem> getStatus() {
		if (statusRequerimentos == null) {
			consultarStatusRequerimentos();
		}

		List<SelectItem> itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem("", BUNDLE.getString("geral_lbl_selecione")));
		if(ContextoUtil.getContexto().getUsuarioLogado().getIdePerfil().getIdePerfil()==PerfilEnum.FINANCEIRO.getId()){
			for (StatusRequerimento status : statusRequerimentos) {
				if(status.getIdeStatusRequerimento()==StatusRequerimentoEnum.COMPROVANTE_ENVIADO.getStatus()
						||status.getIdeStatusRequerimento()==StatusRequerimentoEnum.VALIDADO.getStatus()
						||status.getIdeStatusRequerimento()==StatusRequerimentoEnum.PAGAMENTO_LIBERADO.getStatus()
						||status.getIdeStatusRequerimento()==StatusRequerimentoEnum.PENDENCIA_VALIDACAO_COMPROVANTE.getStatus()){
					itens.add(new SelectItem(status, status.getNomStatusRequerimento()));
				}
			}
		} else {
			if (statusRequerimentos != null) {
				for (StatusRequerimento status : statusRequerimentos) {
					itens.add(new SelectItem(status, status.getNomStatusRequerimento()));
				}
			}
		}

		return itens;
	}

	public Collection<SelectItem> getEmpreendimentos() {
		if (empreendimentos == null) {
			consultarEmpreendimentos();
		}
		List<SelectItem> itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem("", BUNDLE.getString("geral_lbl_selecione")));
		if (empreendimentos != null) {
			for (Empreendimento empreendimento : empreendimentos) {
				itens.add(new SelectItem(empreendimento, empreendimento.getNomEmpreendimento()));
			}
		}
		return itens;
	}

	public void setEmpreendimentos(Collection<Empreendimento> empreendimentos) {
		this.empreendimentos = empreendimentos;
	}

	public Collection<RequerimentoUnicoDTO> getRequerimentos() {
		return requerimentos;
	}

	public void setRequerimentos(Collection<RequerimentoUnicoDTO> requerimentos) {
		this.requerimentos = requerimentos;
	}

	public Date getPeriodoInicio() {
		return periodoInicio;
	}

	public void setPeriodoInicio(Date periodoInicio) {
		this.periodoInicio = periodoInicio;
	}

	public Date getPeriodoFim() {
		return periodoFim;
	}

	public void setPeriodoFim(Date periodoFim) {
		if (!Util.isNullOuVazio(periodoFim)) {
			DateTime dt = new DateTime(periodoFim);
			this.periodoFim = dt.plusHours(23).plusMinutes(59).plusSeconds(59).toDate();
		}
	}

	public Pessoa getRequerente() {
		return requerente;
	}

	public void setRequerente(Pessoa requerente) {
		// atualiza lista de empreedimentos
		empreendimentos = null;
		this.requerente = requerente;
	}

	public void selecionarRequerente(PessoaFisica requerente) {
		// atualiza lista de empreedimentos
		empreendimentos = null;
		this.requerente = new Pessoa();
		this.requerente.setIdePessoa(requerente.getIdePessoaFisica());
		PessoaFisica pessoaFisica = new PessoaFisica();
		pessoaFisica.setNomPessoa(requerente.getNomPessoa());
		this.requerente.setPessoaFisica(pessoaFisica);
	}

	public void selecionarRequerentePJ(PessoaJuridica pessoaJuridica){
		empreendimentos = null;
		this.requerente = new Pessoa();
		this.requerente.setIdePessoa(pessoaJuridica.getIdePessoaJuridica());
		this.requerente.setPessoaJuridica(pessoaJuridica);
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}

	public Boolean getFlagEndereco() {
		return flagEndereco;
	}

	public void setFlagEndereco(Boolean flagEndereco) {
		this.flagEndereco = flagEndereco;
	}

	public DataModel<RequerimentoImovel> getRequerimentoImovelData() {
		return requerimentoImovelData;
	}

	public void setRequerimentoImovelData(DataModel<RequerimentoImovel> requerimentoImovelData) {
		this.requerimentoImovelData = requerimentoImovelData;
	}

	public DataModel<RequerimentoTipologia> getRequerimentoTipologiaData() {
		return requerimentoTipologiaData;
	}

	public void setRequerimentoTipologiaData(DataModel<RequerimentoTipologia> requerimentoTipologiaData) {
		this.requerimentoTipologiaData = requerimentoTipologiaData;
	}

	public List<SelectItem> getCollTipoIntervencaoData() {
		return collTipoIntervencaoData;
	}

	public void setCollTipoIntervencaoData(List<SelectItem> collTipoIntervencaoData) {
		this.collTipoIntervencaoData = collTipoIntervencaoData;
	}

	public List<SelectItem> getCollTipoFinalidadeUsoAgua() {
		return collTipoFinalidadeUsoAgua;
	}

	public void setCollTipoFinalidadeUsoAgua(List<SelectItem> collTipoFinalidadeUsoAgua) {
		this.collTipoFinalidadeUsoAgua = collTipoFinalidadeUsoAgua;
	}

	public List<SelectItem> getCollTipoTipoReceptor() {
		return collTipoTipoReceptor;
	}

	public void setCollTipoTipoReceptor(List<SelectItem> collTipoTipoReceptor) {
		this.collTipoTipoReceptor = collTipoTipoReceptor;
	}

	public List<SelectItem> getCollTipoCaptacao() {
		return collTipoCaptacao;
	}

	public void setCollTipoCaptacao(List<SelectItem> collTipoCaptacao) {
		this.collTipoCaptacao = collTipoCaptacao;
	}

	public List<SelectItem> getCollDatum() {
		return collDatum;
	}

	public void setCollDatum(List<SelectItem> collDatum) {
		this.collDatum = collDatum;
	}

	public List<ObjetivoRequerimentoLimpezaArea> getCollObjetivoRequerimentoLimpezaArea() {
		return collObjetivoRequerimentoLimpezaArea;
	}

	public Boolean getCollObjetivoRequerimentoLimpezaAreaIsNotNull() {
		if(!Util.isNullOuVazio(collObjetivoRequerimentoLimpezaArea) && !collObjetivoRequerimentoLimpezaArea.isEmpty()){
			for (ObjetivoRequerimentoLimpezaArea objLImpArea : collObjetivoRequerimentoLimpezaArea) {
				if (!Util.isNullOuVazio(objLImpArea.getIdeObjetivoRequerimentoLimpezaArea())) {
					return true;
				}
			}
			return false;
		} else {
			return false;
		}
	}

	public void setCollObjetivoRequerimentoLimpezaArea(List<ObjetivoRequerimentoLimpezaArea> collObjetivoRequerimentoLimpezaArea) {
		this.collObjetivoRequerimentoLimpezaArea = collObjetivoRequerimentoLimpezaArea;
	}

	public List<SelectItem> getCollEmpreendimento() {
		return collEmpreendimento;
	}

	public void setCollEmpreendimento(List<SelectItem> collEmpreendimento) {
		this.collEmpreendimento = collEmpreendimento;
	}

	public List<String> getListTipoIntervencao() {
		return listTipoIntervencao;
	}

	public void setListTipoIntervencao(List<String> listTipoIntervencao) {
		this.listTipoIntervencao = listTipoIntervencao;
	}

	public List<String> getListTipoFinalidadeUsoAgua() {
		return listTipoFinalidadeUsoAgua;
	}

	public void setListTipoFinalidadeUsoAgua(List<String> listTipoFinalidadeUsoAgua) {
		this.listTipoFinalidadeUsoAgua = listTipoFinalidadeUsoAgua;
	}

	public List<String> getListTipoTipoReceptor() {
		return listTipoTipoReceptor;
	}

	public void setListTipoTipoReceptor(List<String> listTipoTipoReceptor) {
		this.listTipoTipoReceptor = listTipoTipoReceptor;
	}

	public List<String> getListTipoCaptacao() {
		return listTipoCaptacao;
	}

	public void setListTipoCaptacao(List<String> listTipoCaptacao) {
		this.listTipoCaptacao = listTipoCaptacao;
	}

	public List<String> getListObjetivoRequerimentoLimpezaArea() {
		return listObjetivoRequerimentoLimpezaArea;
	}

	public void setListObjetivoRequerimentoLimpezaArea(List<String> listObjetivoRequerimentoLimpezaArea) {
		this.listObjetivoRequerimentoLimpezaArea = listObjetivoRequerimentoLimpezaArea;
	}

	public List<SelectItem> getListRequerimentoTipologia() {
		return listRequerimentoTipologia;
	}

	public void setListRequerimentoTipologia(List<SelectItem> listRequerimentoTipologia) {
		this.listRequerimentoTipologia = listRequerimentoTipologia;
	}

	public String getPorteIdentificado() {
		return porteIdentificado;
	}

	public void setPorteIdentificado(String porteIdentificado) {
		this.porteIdentificado = porteIdentificado;
	}

	public RequerimentoImovel getRequerimentoImovel() {
		return requerimentoImovel;
	}

	public void setRequerimentoImovel(RequerimentoImovel requerimentoImovel) {
		this.requerimentoImovel = requerimentoImovel;
	}

	public Boolean getEnderecoContato() {
		return enderecoContato;
	}

	public void setEnderecoContato(Boolean enderecoContato) {
		this.enderecoContato = enderecoContato;
	}

	public Boolean getProcessoTramiteInema() {
		return processoTramiteInema;
	}

	public void setProcessoTramiteInema(Boolean processoTramiteInema) {
		this.processoTramiteInema = processoTramiteInema;
	}

	public Boolean getUsoPassivelOutorgaEmpreendimento() {
		return usoPassivelOutorgaEmpreendimento;
	}

	public void setUsoPassivelOutorgaEmpreendimento(Boolean usoPassivelOutorgaEmpreendimento) {
		this.usoPassivelOutorgaEmpreendimento = usoPassivelOutorgaEmpreendimento;
	}

	public Boolean getProcessoUsoPassivel() {
		return processoUsoPassivel;
	}

	public void setProcessoUsoPassivel(Boolean processoUsoPassivel) {
		this.processoUsoPassivel = processoUsoPassivel;
	}

	public Boolean getUtilizaEmpreendimentoAgua() {
		return utilizaEmpreendimentoAgua;
	}

	public void setUtilizaEmpreendimentoAgua(Boolean utilizaEmpreendimentoAgua) {
		this.utilizaEmpreendimentoAgua = utilizaEmpreendimentoAgua;
	}

	public Boolean getCaptacaoSuperficial() {
		return captacaoSuperficial;
	}

	public void setCaptacaoSuperficial(Boolean captacaoSuperficial) {
		this.captacaoSuperficial = captacaoSuperficial;
	}

	public Boolean getCaptacaoSubterranea() {
		return captacaoSubterranea;
	}

	public void setCaptacaoSubterranea(Boolean captacaoSubterranea) {
		this.captacaoSubterranea = captacaoSubterranea;
	}

	public Boolean getImovelRural() {
		return imovelRural;
	}

	public void setImovelRural(Boolean imovelRural) {
		this.imovelRural = imovelRural;
	}

	public Boolean getVazaoFinalidadeAgua() {
		return vazaoFinalidadeAgua;
	}

	public void setVazaoFinalidadeAgua(Boolean vazaoFinalidadeAgua) {
		this.vazaoFinalidadeAgua = vazaoFinalidadeAgua;
	}

	public Boolean getLocalizacaoGeografica() {
		return localizacaoGeografica;
	}

	public void setLocalizacaoGeografica(Boolean localizacaoGeografica) {
		this.localizacaoGeografica = localizacaoGeografica;
	}

	public Boolean getProcessoAna() {
		return processoAna;
	}

	public void setProcessoAna(Boolean processoAna) {
		this.processoAna = processoAna;
	}

	public Boolean getIntervencaoCorpoHidrico() {
		return intervencaoCorpoHidrico;
	}

	public void setIntervencaoCorpoHidrico(Boolean intervencaoCorpoHidrico) {
		this.intervencaoCorpoHidrico = intervencaoCorpoHidrico;
	}

	public Boolean getRegularizarPerfuracaoPoco() {
		return regularizarPerfuracaoPoco;
	}

	public void setRegularizarPerfuracaoPoco(Boolean regularizarPerfuracaoPoco) {
		this.regularizarPerfuracaoPoco = regularizarPerfuracaoPoco;
	}

	public Boolean getOrigemAgua() {
		return origemAgua;
	}

	public void setOrigemAgua(Boolean origemAgua) {
		this.origemAgua = origemAgua;
	}

	public Boolean getCaptacaoBarramento() {
		return captacaoBarramento;
	}

	public void setCaptacaoBarramento(Boolean captacaoBarramento) {
		this.captacaoBarramento = captacaoBarramento;
	}

	public Boolean getVazaoCaptacao() {
		return vazaoCaptacao;
	}

	public void setVazaoCaptacao(Boolean vazaoCaptacao) {
		this.vazaoCaptacao = vazaoCaptacao;
	}

	public Collection<StatusRequerimento> getStatusRequerimentos() {
		return statusRequerimentos;
	}

	public void setStatusRequerimentos(Collection<StatusRequerimento> statusRequerimentos) {
		this.statusRequerimentos = statusRequerimentos;
	}

	public Empreendimento getEmpreendimentoRequerimento() {
		return empreendimentoRequerimento;
	}

	public void setEmpreendimentoRequerimento(Empreendimento empreendimentoRequerimento) {
		this.empreendimentoRequerimento = empreendimentoRequerimento;
	}

	public StatusRequerimento getStatusRequerimento() {
		return statusRequerimento;
	}

	public void setStatusRequerimento(StatusRequerimento statusRequerimento) {
		this.statusRequerimento = statusRequerimento;
	}

	public Boolean getProducaoVolumetricaMadeira() {
		return producaoVolumetricaMadeira;
	}

	public void setProducaoVolumetricaMadeira(Boolean producaoVolumetricaMadeira) {
		this.producaoVolumetricaMadeira = producaoVolumetricaMadeira;
	}

	public Boolean getOutroObjetivoLimpezaArea() {
		return outroObjetivoLimpezaArea;
	}

	public void setOutroObjetivoLimpezaArea(Boolean outroObjetivoLimpezaArea) {
		this.outroObjetivoLimpezaArea = outroObjetivoLimpezaArea;
	}

	public Boolean getLimpezaArea() {
		return limpezaArea;
	}

	public void setLimpezaArea(Boolean limpezaArea) {
		this.limpezaArea = limpezaArea;
	}

	public Boolean getAutorizacaoPerfuracaoPoco() {
		return autorizacaoPerfuracaoPoco;
	}

	public Boolean getRegularizarPerfuradoPoco() {
		return regularizarPerfuradoPoco;
	}

	public void setRegularizarPerfuradoPoco(Boolean regularizarPerfuradoPoco) {
		this.regularizarPerfuradoPoco = regularizarPerfuradoPoco;
	}

	public Boolean getConstrucaoBarragemCorpoHidrico() {
		return construcaoBarragemCorpoHidrico;
	}

	public void setConstrucaoBarragemCorpoHidrico(Boolean construcaoBarragemCorpoHidrico) {
		this.construcaoBarragemCorpoHidrico = construcaoBarragemCorpoHidrico;
	}

	public Boolean getAutorizacaoConstrucaoBarragemIntervencao() {
		return autorizacaoConstrucaoBarragemIntervencao;
	}

	public void setAutorizacaoConstrucaoBarragemIntervencao(Boolean autorizacaoConstrucaoBarragemIntervencao) {
		this.autorizacaoConstrucaoBarragemIntervencao = autorizacaoConstrucaoBarragemIntervencao;
	}

	public Boolean getAutorizacaoConstrucaoBarragemCaptacao() {
		return autorizacaoConstrucaoBarragemCaptacao;
	}

	public void setAutorizacaoConstrucaoBarragemCaptacao(Boolean autorizacaoConstrucaoBarragemCaptacao) {
		this.autorizacaoConstrucaoBarragemCaptacao = autorizacaoConstrucaoBarragemCaptacao;
	}

	public Boolean getRealizaEmissaoResiduosLiquidos() {
		return realizaEmissaoResiduosLiquidos;
	}

	public void setRealizaEmissaoResiduosLiquidos(Boolean realizaEmissaoResiduosLiquidos) {
		this.realizaEmissaoResiduosLiquidos = realizaEmissaoResiduosLiquidos;
	}

	public Boolean getBarragemCaptacaoContruida() {
		return barragemCaptacaoContruida;
	}

	public void setBarragemCaptacaoContruida(Boolean barragemCaptacaoContruida) {
		this.barragemCaptacaoContruida = barragemCaptacaoContruida;
	}

	public Boolean getAvisoConstrucaoBarragemEmpreendimentoEspecifico() {
		return avisoConstrucaoBarragemEmpreendimentoEspecifico;
	}

	public void setAvisoConstrucaoBarragemEmpreendimentoEspecifico(Boolean avisoConstrucaoBarragemEmpreendimentoEspecifico) {
		this.avisoConstrucaoBarragemEmpreendimentoEspecifico = avisoConstrucaoBarragemEmpreendimentoEspecifico;
	}

	public Boolean getSuprimentoSustentavel() {
		return suprimentoSustentavel;
	}

	public void setSuprimentoSustentavel(Boolean suprimentoSustentavel) {
		this.suprimentoSustentavel = suprimentoSustentavel;
	}

	public Boolean getVincularFlorestaProducao() {
		return vincularFlorestaProducao;
	}

	public void setVincularFlorestaProducao(Boolean vincularFlorestaProducao) {
		this.vincularFlorestaProducao = vincularFlorestaProducao;
	}

	public Boolean getTransferenciaCreditoReposicaoFlorestal() {
		return transferenciaCreditoReposicaoFlorestal;
	}

	public void setTransferenciaCreditoReposicaoFlorestal(Boolean transferenciaCreditoReposicaoFlorestal) {
		this.transferenciaCreditoReposicaoFlorestal = transferenciaCreditoReposicaoFlorestal;
	}

	public void setRequerimentoUnicoEnquadramento(RequerimentoUnico requerimentoUnicoEnquadramento) {
		this.requerimentoUnicoEnquadramento = requerimentoUnicoEnquadramento;
	}

	public BoletoPagamentoRequerimento getBoleto() {
		return boleto;
	}

	public void setBoleto(BoletoPagamentoRequerimento boleto) {
		this.boleto = boleto;
	}

	public List<ComprovantePagamentoDTO> getComprovantePagamentos() {
		return comprovantePagamentos;
	}

	public void setComprovantePagamentos(List<ComprovantePagamentoDTO> comprovantePagamento) {
		this.comprovantePagamentos = comprovantePagamento;
	}

	public void setRequerimentoUnicoDTO(RequerimentoUnicoDTO requerimentoUnicoDTO) {
		this.requerimentoUnicoDTO = requerimentoUnicoDTO;
	}

	public RequerimentoUnicoDTO getRequerimentoUnicoDTO() {
		return requerimentoUnicoDTO;
	}

	public ComprovantePagamento getComprovante() {
		return comprovante;
	}

	public void setComprovante(ComprovantePagamento comprovante) {
		this.comprovante = comprovante;
	}

	public Integer getAreaSelecionada() {
		return areaSelecionada;
	}

	public void setAreaSelecionada(Integer areaSelecionada) {
		this.areaSelecionada = areaSelecionada;
	}

	public void setAutorizacaoPerfuracaoPoco(Boolean autorizacaoPerfuracaoPoco) {
		this.autorizacaoPerfuracaoPoco = autorizacaoPerfuracaoPoco;
	}

	public Boolean getFlorestaVinculadaReposicaoFlorestal() {
		return florestaVinculadaReposicaoFlorestal;
	}

	public void setFlorestaVinculadaReposicaoFlorestal(Boolean florestaVinculadaReposicaoFlorestal) {
		this.florestaVinculadaReposicaoFlorestal = florestaVinculadaReposicaoFlorestal;
	}

	public Boolean getPrazoValidade() {
		return prazoValidade;
	}

	public void setPrazoValidade(Boolean prazoValidade) {
		this.prazoValidade = prazoValidade;
	}

	public Boolean getCortarFlorestaProducaoRegistrada() {
		return cortarFlorestaProducaoRegistrada;
	}

	public void setCortarFlorestaProducaoRegistrada(Boolean cortarFlorestaProducaoRegistrada) {
		this.cortarFlorestaProducaoRegistrada = cortarFlorestaProducaoRegistrada;
	}

	public Boolean getNumeroProcessoAprovacaoPlanomanejoFlorestal() {
		return numeroProcessoAprovacaoPlanomanejoFlorestal;
	}

	public void setNumeroProcessoAprovacaoPlanomanejoFlorestal(Boolean numeroProcessoAprovacaoPlanomanejoFlorestal) {
		this.numeroProcessoAprovacaoPlanomanejoFlorestal = numeroProcessoAprovacaoPlanomanejoFlorestal;
	}

	public Boolean getNumeroPortariaAutorizacaoSupressaoVegetacao() {
		return numeroPortariaAutorizacaoSupressaoVegetacao;
	}

	public void setNumeroPortariaAutorizacaoSupressaoVegetacao(Boolean numeroPortariaAutorizacaoSupressaoVegetacao) {
		this.numeroPortariaAutorizacaoSupressaoVegetacao = numeroPortariaAutorizacaoSupressaoVegetacao;
	}

	public Boolean getNumeroRegistroCorteFlorestalProducao() {
		return numeroRegistroCorteFlorestalProducao;
	}

	public void setNumeroRegistroCorteFlorestalProducao(Boolean numeroRegistroCorteFlorestalProducao) {
		this.numeroRegistroCorteFlorestalProducao = numeroRegistroCorteFlorestalProducao;
	}

	public Boolean getAutorizacaoCorteFlorestaProducao() {
		return autorizacaoCorteFlorestaProducao;
	}

	public void setAutorizacaoCorteFlorestaProducao(Boolean autorizacaoCorteFlorestaProducao) {
		this.autorizacaoCorteFlorestaProducao = autorizacaoCorteFlorestaProducao;
	}

	public Boolean getNumeroLicencaAmbiental() {
		return numeroLicencaAmbiental;
	}

	public void setNumeroLicencaAmbiental(Boolean numeroLicencaAmbiental) {
		this.numeroLicencaAmbiental = numeroLicencaAmbiental;
	}

	public Boolean getOrigemMaterialLenhoso() {
		return origemMaterialLenhoso;
	}

	public void setOrigemMaterialLenhoso(Boolean origemMaterialLenhoso) {
		this.origemMaterialLenhoso = origemMaterialLenhoso;
	}

	public Boolean getNumeroPortariaSupressaoVegetacaoNativa() {
		return numeroPortariaSupressaoVegetacaoNativa;
	}

	public void setNumeroPortariaSupressaoVegetacaoNativa(Boolean numeroPortariaSupressaoVegetacaoNativa) {
		this.numeroPortariaSupressaoVegetacaoNativa = numeroPortariaSupressaoVegetacaoNativa;
	}

	public Boolean getNumeroCertificadoRegistroCorteFlorestaProducao() {
		return numeroCertificadoRegistroCorteFlorestaProducao;
	}

	public void setNumeroCertificadoRegistroCorteFlorestaProducao(Boolean numeroCertificadoRegistroCorteFlorestaProducao) {
		this.numeroCertificadoRegistroCorteFlorestaProducao = numeroCertificadoRegistroCorteFlorestaProducao;
	}

	public Boolean getNumeroPortariaAutorizacaoCorteFlorestaProducao() {
		return numeroPortariaAutorizacaoCorteFlorestaProducao;
	}

	public void setNumeroPortariaAutorizacaoCorteFlorestaProducao(Boolean numeroPortariaAutorizacaoCorteFlorestaProducao) {
		this.numeroPortariaAutorizacaoCorteFlorestaProducao = numeroPortariaAutorizacaoCorteFlorestaProducao;
	}

	public Boolean getSelectedModoCoordenada() {
		return selectedModoCoordenada;
	}

	public void setSelectedModoCoordenada(Boolean selectedModoCoordenada) {
		this.selectedModoCoordenada = selectedModoCoordenada;
	}

	public String getNumeroRequerimento() {
		return numeroRequerimento;
	}

	public void setNumeroRequerimento(String numeroRequerimento) {
		this.numeroRequerimento = numeroRequerimento;
	}

	public RequerimentoUnico getRequerimentoUnicoEnquadramento() {
		return requerimentoUnicoEnquadramento;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public List<SelectItem> getCollFaseEmpreendimento() {
		return collFaseEmpreendimento;
	}

	public void setCollFaseEmpreendimento(List<SelectItem> collFaseEmpreendimento) {
		this.collFaseEmpreendimento = collFaseEmpreendimento;
	}

	public Boolean getVisualizaIncluirEmpreendimento() {
		if (collEmpreendimento.isEmpty()) {
			visualizaIncluirEmpreendimento = true;
		} else {
			visualizaIncluirEmpreendimento = false;
		}
		return visualizaIncluirEmpreendimento;
	}

	public void setVisualizaIncluirEmpreendimento(Boolean visualizaIncluirEmpreendimento) {
		this.visualizaIncluirEmpreendimento = visualizaIncluirEmpreendimento;
	}

	public LocalizacaoGeografica getLocalizacaoGeograficaSelecionada() {
		return Util.isNull(localizacaoGeograficaSelecionada) 
				? (localizacaoGeograficaSelecionada = new LocalizacaoGeografica())
				: localizacaoGeograficaSelecionada;
	}

	public void setLocalizacaoGeograficaSelecionada(LocalizacaoGeografica localizacaoGeograficaSelecionada) {
		this.localizacaoGeograficaSelecionada = localizacaoGeograficaSelecionada;
	}

	public String getGrausLatitudeLoc() {
		return grausLatitudeLoc;
	}

	public void setGrausLatitudeLoc(String grausLatitudeLoc) {
		this.grausLatitudeLoc = grausLatitudeLoc;
	}

	public String getMinutosLatitudeLoc() {
		return minutosLatitudeLoc;
	}

	public void setMinutosLatitudeLoc(String minutosLatitudeLoc) {
		this.minutosLatitudeLoc = minutosLatitudeLoc;
	}

	public String getSegundosLatitudeLoc() {
		return segundosLatitudeLoc;
	}

	public void setSegundosLatitudeLoc(String segundosLatitudeLoc) {
		this.segundosLatitudeLoc = segundosLatitudeLoc;
	}

	public String getGrausLongitudeLoc() {
		return grausLongitudeLoc;
	}

	public void setGrausLongitudeLoc(String grausLongitudeLoc) {
		this.grausLongitudeLoc = grausLongitudeLoc;
	}

	public String getMinutosLongitudeLoc() {
		return minutosLongitudeLoc;
	}

	public void setMinutosLongitudeLoc(String minutosLongitudeLoc) {
		this.minutosLongitudeLoc = minutosLongitudeLoc;
	}

	public String getSegundosLongitudeLoc() {
		return segundosLongitudeLoc;
	}

	public void setSegundosLongitudeLoc(String segundosLongitudeLoc) {
		this.segundosLongitudeLoc = segundosLongitudeLoc;
	}

	public String getFracaoGrauLatitudeLoc() {
		return fracaoGrauLatitudeLoc;
	}

	public void setFracaoGrauLatitudeLoc(String fracaoGrauLatitudeLoc) {
		this.fracaoGrauLatitudeLoc = fracaoGrauLatitudeLoc;
	}

	public String getFracaoGrauLongitudeLoc() {
		return fracaoGrauLongitudeLoc;
	}

	public void setFracaoGrauLongitudeLoc(String fracaoGrauLongitudeLoc) {
		this.fracaoGrauLongitudeLoc = fracaoGrauLongitudeLoc;
	}

	public DadoGeografico getVerticeLoc() {
		return verticeLoc;
	}

	public void setVerticeLoc(DadoGeografico verticeLoc) {
		this.verticeLoc = verticeLoc;
	}

	public DadoGeografico getVerticeExclusaoLoc() {
		return verticeExclusaoLoc;
	}

	public void setVerticeExclusaoLoc(DadoGeografico verticeExclusaoLoc) {
		this.verticeExclusaoLoc = verticeExclusaoLoc;
	}

	public SistemaCoordenada getDatum() {
		return datum;
	}

	public void setDatum(SistemaCoordenada datum) {
		this.datum = datum;
	}

	public boolean getDesabilitarTudo() {
		return desabilitarTudo;
	}

	public Boolean getTipologiaPrincipal() {
		return tipologiaPrincipal;
	}

	public void setTipologiaPrincipal(Boolean tipologiaPrincipal) {
		this.tipologiaPrincipal = tipologiaPrincipal;
	}

	public Boolean getAuxBoolean() {
		return auxBoolean;
	}

	public void setAuxBoolean(Boolean auxBoolean) {
		this.auxBoolean = auxBoolean;
	}

	public String getGrausLatitude() {
		return grausLatitude;
	}

	public void setGrausLatitude(String grausLatitude) {
		this.grausLatitude = grausLatitude;
	}

	public String getMinutosLatitude() {
		return minutosLatitude;
	}

	public void setMinutosLatitude(String minutosLatitude) {
		this.minutosLatitude = minutosLatitude;
	}

	public String getSegundosLatitude() {
		return segundosLatitude;
	}

	public void setSegundosLatitude(String segundosLatitude) {
		this.segundosLatitude = segundosLatitude;
	}

	public String getGrausLongitude() {
		return grausLongitude;
	}

	public void setGrausLongitude(String grausLongitude) {
		this.grausLongitude = grausLongitude;
	}

	public String getMinutosLongitude() {
		return minutosLongitude;
	}

	public void setMinutosLongitude(String minutosLongitude) {
		this.minutosLongitude = minutosLongitude;
	}

	public String getSegundosLongitude() {
		return segundosLongitude;
	}

	public void setSegundosLongitude(String segundosLongitude) {
		this.segundosLongitude = segundosLongitude;
	}

	public String getFracaoGrauLatitude() {
		return fracaoGrauLatitude;
	}

	public void setFracaoGrauLatitude(String fracaoGrauLatitude) {
		this.fracaoGrauLatitude = fracaoGrauLatitude;
	}

	public String getFracaoGrauLongitude() {
		return fracaoGrauLongitude;
	}

	public void setFracaoGrauLongitude(String fracaoGrauLongitude) {
		this.fracaoGrauLongitude = fracaoGrauLongitude;
	}

	public boolean isEfetuandoEnquadramento() {
		return efetuandoEnquadramento;
	}

	public void setEfetuandoEnquadramento(boolean efetuandoEnquadramento) {
		this.efetuandoEnquadramento = efetuandoEnquadramento;
	}

	public ProcessoTramite getProcessoTramite() {
		return processoTramite;
	}

	public void setProcessoTramite(ProcessoTramite processoTramite) {
		this.processoTramite = processoTramite;
	}

	public String getIdTipologiaPrincipal() {
		return idTipologiaPrincipal;
	}

	public void setIdTipologiaPrincipal(String idTipologiaPrincipal) {
		this.idTipologiaPrincipal = idTipologiaPrincipal;
	}

	public boolean isModoEdicao() {
		return modoEdicao;
	}

	public void setModoEdicao(boolean pModoEdicao) {
		modoEdicao = pModoEdicao;
	}

	public List<String> getListaArquivo() {
		return listaArquivo;
	}

	public void setListaArquivo(List<String> listaArquivo) {
		this.listaArquivo = listaArquivo;
	}

	public Boolean getTemArquivo() {
		return temArquivo;
	}

	public void setTemArquivo(Boolean temArquivo) {
		this.temArquivo = temArquivo;
	}

	public StreamedContent getArquivoBaixar() {
		if (!listaArquivo.isEmpty()) {
			Exception erro = null;
			try {
				arquivoBaixar = gerenciaArquivoservice.getContentFile(caminhoArquivo);
			} catch (Exception e) {
				erro =e;
				JsfUtil.addErrorMessage("Arquivo não encontrado.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
		}
		return arquivoBaixar;
	}

	public void setArquivoBaixar(StreamedContent arquivoBaixar) {
		this.arquivoBaixar = arquivoBaixar;
	}

	public Boolean getNumeroAlteracaoLicencaAmbiental() {
		return numeroAlteracaoLicencaAmbiental;
	}

	public void setNumeroAlteracaoLicencaAmbiental(Boolean numeroAlteracaoLicencaAmbiental) {
		this.numeroAlteracaoLicencaAmbiental = numeroAlteracaoLicencaAmbiental;
	}

	public Boolean getVisualizaSelectEmpreendimento() {
		return visualizaSelectEmpreendimento;
	}

	public void setVisualizaSelectEmpreendimento(Boolean visualizaSelectEmpreendimento) {
		this.visualizaSelectEmpreendimento = visualizaSelectEmpreendimento;
	}

	public boolean isTermoDeclaracao() {
		return termoDeclaracao;
	}

	public void setTermoDeclaracao(boolean termoDeclaracao) {
		this.termoDeclaracao = termoDeclaracao;
	}

	public boolean isRecalcularPorte() {
		return recalcularPorte;
	}

	public void setRecalcularPorte(boolean recalcularPorte) {
		this.recalcularPorte = recalcularPorte;
	}

	public boolean isRequerimentoUnicoPagoExistente() {
		return requerimentoUnicoPagoExistente;
	}

	public void setRequerimentoUnicoPagoExistente(boolean requerimentoUnicoPagoExistente) {
		this.requerimentoUnicoPagoExistente = requerimentoUnicoPagoExistente;
	}

	public DataTable getDatatableRequerimentos() {
		return datatableRequerimentos;
	}

	public void setDatatableRequerimentos(DataTable datatableRequerimentos) {
		this.datatableRequerimentos = datatableRequerimentos;
	}

	public boolean isEditando() {
		return editando;
	}

	public void setEditando(boolean editando) {
		this.editando = editando;
	}

	public List<String> getListaArquivoDaeCertificado() {
		return listaArquivoDaeCertificado;
	}

	public void setListaArquivoDaeCertificado(List<String> listaArquivoDaeCertificado) {
		this.listaArquivoDaeCertificado = listaArquivoDaeCertificado;
	}

	public List<String> getListaArquivoDaeVistoria() {
		return listaArquivoDaeVistoria;
	}

	public void setListaArquivoDaeVistoria(List<String> listaArquivoDaeVistoria) {
		this.listaArquivoDaeVistoria = listaArquivoDaeVistoria;
	}

	public String getCaminhoArquivo() {
		return caminhoArquivo;
	}

	public void setCaminhoArquivo(String caminhoArquivo) {
		this.caminhoArquivo = caminhoArquivo;
	}

	public String getCaminhoArquivoDaeCertificado() {
		return caminhoArquivoDaeCertificado;
	}

	public void setCaminhoArquivoDaeCertificado(String caminhoArquivoDaeCertificado) {
		this.caminhoArquivoDaeCertificado = caminhoArquivoDaeCertificado;
	}

	public String getCaminhoArquivoDaeVistoria() {
		return caminhoArquivoDaeVistoria;
	}

	public void setCaminhoArquivoDaeVistoria(String caminhoArquivoDaeVistoria) {
		this.caminhoArquivoDaeVistoria = caminhoArquivoDaeVistoria;
	}

	public StreamedContent getArquivoBaixarDaeCertificado() {
		if (!listaArquivoDaeCertificado.isEmpty()) {
			Exception erro = null; 
			try {
				arquivoBaixarDaeCertificado = gerenciaArquivoservice.getContentFile(caminhoArquivoDaeCertificado);
			} catch (Exception e) {
				erro =e;
				JsfUtil.addErrorMessage("Arquivo não encontrado.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
		}
		return arquivoBaixarDaeCertificado;
	}

	public void setArquivoBaixarDaeCertificado(StreamedContent arquivoBaixarDaeCertificado) {
		this.arquivoBaixarDaeCertificado = arquivoBaixarDaeCertificado;
	}

	public StreamedContent getArquivoBaixarDaeVistoria() {
		if (!listaArquivoDaeVistoria.isEmpty()) {
			Exception erro = null;
			try {
				arquivoBaixarDaeVistoria = gerenciaArquivoservice.getContentFile(caminhoArquivoDaeVistoria);
			} catch (Exception e) {
				erro =e;
				JsfUtil.addErrorMessage("Arquivo não encontrado.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}finally{
				if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
			}
		}
		return arquivoBaixarDaeVistoria;
	}

	public void setArquivoBaixarDaeVistoria(StreamedContent arquivoBaixarDaeVistoria) {
		this.arquivoBaixarDaeVistoria = arquivoBaixarDaeVistoria;
	}

	public Boolean getShowEmissaoDae() {
		return showEmissaoDae;
	}

	public Boolean getExibeComprovante() {
		return !Util.isNullOuVazio(comprovantePagamentos);
	}

	public void setShowEmissaoDae(Boolean showEmissaoDae) {
		this.showEmissaoDae = showEmissaoDae;
	}

	public Boolean getShowUploadComprovanteDae() {
		return showUploadComprovanteDae;
	}

	public void setShowUploadComprovanteDae(Boolean showUploadComprovanteDae) {
		this.showUploadComprovanteDae = showUploadComprovanteDae;
	}

	public Boolean getDisableVistoria() {
		return disableVistoria;
	}

	public void setDisableVistoria(Boolean disableVistoria) {
		this.disableVistoria = disableVistoria;
	}

	public BoletoDaeRequerimento getBoletoDaeRequerimeno() {
		return boletoDaeRequerimeno;
	}

	public void setBoletoDaeRequerimeno(BoletoDaeRequerimento boletoDaeRequerimeno) {
		this.boletoDaeRequerimeno = boletoDaeRequerimeno;
	}

	public UploadedFile getFileUploadDaeVistoria() {
		return fileUploadDaeVistoria;
	}

	public void setFileUploadDaeVistoria(UploadedFile fileUploadDaeVistoria) {
		this.fileUploadDaeVistoria = fileUploadDaeVistoria;
	}

	public Boolean getTemArquivoDaeCertificado() {
		return temArquivoDaeCertificado;
	}

	public void setTemArquivoDaeCertificado(Boolean temArquivoDaeCertificado) {
		this.temArquivoDaeCertificado = temArquivoDaeCertificado;
	}

	public Boolean getTemArquivoDaeVistoria() {
		return temArquivoDaeVistoria;
	}

	public void setTemArquivoDaeVistoria(Boolean temArquivoDaeVistoria) {
		this.temArquivoDaeVistoria = temArquivoDaeVistoria;
	}

	public ComprovantePagamentoDae getComprovantePagamentoDaeCertificado() {
		return comprovantePagamentoDaeCertificado;
	}

	public void setComprovantePagamentoDaeCertificado(ComprovantePagamentoDae comprovantePagamentoDae) {
		this.comprovantePagamentoDaeCertificado = comprovantePagamentoDae;
	}

	public Boolean getShowUploadComprovanteDaeVistoria() {
		return showUploadComprovanteDaeVistoria;
	}

	public void setShowUploadComprovanteDaeVistoria(Boolean showUploadComprovanteDaeVistoria) {
		this.showUploadComprovanteDaeVistoria = showUploadComprovanteDaeVistoria;
	}

	public ComprovantePagamentoDae getComprovantePagamentoDaeVistoria() {
		return comprovantePagamentoDaeVistoria;
	}

	public void setComprovantePagamentoDaeVistoria(ComprovantePagamentoDae comprovantePagamentoDaeVistoria) {
		this.comprovantePagamentoDaeVistoria = comprovantePagamentoDaeVistoria;
	}

	public List<ComprovantePagamentoDaeDTO> getComprovantePagamentosDae() {
		return comprovantePagamentosDae;
	}

	public void setComprovantePagamentosDae(List<ComprovantePagamentoDaeDTO> comprovantePagamentosDae) {
		this.comprovantePagamentosDae = comprovantePagamentosDae;
	}

	public ComprovantePagamentoDae getComprovantePagamentoDae() {
		return comprovantePagamentoDae;
	}

	public void setComprovantePagamentoDae(ComprovantePagamentoDae comprovantePagamentoDae) {
		this.comprovantePagamentoDae = comprovantePagamentoDae;
	}

	public Boolean getComprovanteDae() {
		return comprovanteDae;
	}

	public void setComprovanteDae(Boolean comprovanteDae) {
		this.comprovanteDae = comprovanteDae;
	}

	public boolean getProcessoTramiteAtivo() {
		return processoTramiteAtivo;
	}

	public void setProcessoTramiteAtivo(boolean processoTramiteAtivo) {
		this.processoTramiteAtivo = processoTramiteAtivo;
	}

	public boolean isGerarRelatorioDLA() {
		return gerarRelatorioDLA;
	}

	public void setGerarRelatorioDLA(boolean gerarRelatorioDLA) {
		this.gerarRelatorioDLA = gerarRelatorioDLA;
	}

	/**
	 * @return the requerimentosModel
	 */
	public LazyDataModel<RequerimentoUnicoDTO> getRequerimentosModel() {
		return requerimentosModel;
	}

	/**
	 * @param requerimentosModel
	 *            the requerimentosModel to set
	 */
	public void setRequerimentosModel(LazyDataModel<RequerimentoUnicoDTO> requerimentosModel) {
		this.requerimentosModel = requerimentosModel;
	}

	public Boolean getDadosContato() {
		return dadosContato;
	}

	public void setDadosContato(Boolean dadosContato) {
		this.dadosContato = dadosContato;
	}

	public LazyDataModel<RequerimentoUnicoDTO> getListaRequerimentosDTOModel() {
		return listaRequerimentosDTOModel;
	}

	public void setListaRequerimentosDTOModel(LazyDataModel<RequerimentoUnicoDTO> listaRequerimentosDTOModel) {
		this.listaRequerimentosDTOModel = listaRequerimentosDTOModel;
	}

	public Boolean getNumeroRenovacaoLicencaAmbiental() {
		return numeroRenovacaoLicencaAmbiental;
	}

	public void setNumeroRenovacaoLicencaAmbiental(Boolean numeroRenovacaoLicencaAmbiental) {
		this.numeroRenovacaoLicencaAmbiental = numeroRenovacaoLicencaAmbiental;
	}

	public Boolean getAtoFlorestal() {
		return atoFlorestal;
	}

	public void setAtoFlorestal(Boolean atoFlorestal) {
		this.atoFlorestal = atoFlorestal;
	}

	public Boolean getAtoFauna() {
		return atoFauna;
	}

	public void setAtoFauna(Boolean atoFauna) {
		this.atoFauna = atoFauna;
	}

	public String getPeriodoDoProcesso() {
		return periodoDoProcesso;
	}

	public void setPeriodoDoProcesso(String periodoDoProcesso) {
		this.periodoDoProcesso = periodoDoProcesso;
	}

	public Boolean getShowPanelProcessosAntigos() {
		return showPanelProcessosAntigos;
	}

	public void setShowPanelProcessosAntigos(Boolean showPanelProcessosAntigos) {
		this.showPanelProcessosAntigos = showPanelProcessosAntigos;
	}

	public String getTipoDoProcesso() {
		return tipoDoProcesso;
	}

	public void setTipoDoProcesso(String tipoDoProcesso) {
		this.tipoDoProcesso = tipoDoProcesso;
	}

	public ProcessoExterno getProcessoExterno() {
		return processoExterno;
	}

	public void setProcessoExterno(ProcessoExterno processoExterno) {
		this.processoExterno = processoExterno;
	}

	public Boolean getIsLicenca() {
		return isLicenca;
	}

	public void setIsLicenca(Boolean isLicenca) {
		this.isLicenca = isLicenca;
	}

	public boolean isUplShapefile() {
		return uplShapefile;
	}

	public void setUplShapefile(boolean uplShapefile) {
		this.uplShapefile = uplShapefile;
	}

	/**
	 * @return the pnlVerticesHabilitado
	 */
	public boolean isPnlVerticesHabilitado() {
		return pnlVerticesHabilitado;
	}

	/**
	 * @param pnlVerticesHabilitado
	 * the pnlVerticesHabilitado to set
	 */
	public void setPnlVerticesHabilitado(boolean pnlVerticesHabilitado) {
		this.pnlVerticesHabilitado = pnlVerticesHabilitado;
	}

	/**
	 * @return the itemsClassifSecGeometrica
	 */
	public List<SelectItem> getItemsClassifSecGeometrica() {
		return itemsClassifSecGeometrica;
	}

	/**
	 * @param itemsClassifSecGeometrica
	 * the itemsClassifSecGeometrica to set
	 */
	public void setItemsClassifSecGeometrica(List<SelectItem> itemsClassifSecGeometrica) {
		this.itemsClassifSecGeometrica = itemsClassifSecGeometrica;
	}

	public void dataPerfuracao(DateSelectEvent event){
		try {
			requerimentoUnico.setDtcPerfPoco(event.getDate());
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Data inválida!");
		}
	}

	public void acaoVazia() {
		System.out.println();
		RequestContext.getCurrentInstance().addPartialUpdateTarget("formQuestionario");
		RequestContext.getCurrentInstance().addPartialUpdateTarget("formQuestionario:panelReqImovelVertice");
	}

	public void validateSalvarBoleto(){

		if (Util.isNull(boleto.getValBoleto())) {
			JsfUtil.addErrorMessage("O campo Valor do Boleto (R$) é de preenchimento obrigatório.");
			return;
		}

		if (!boleto.isZerado() && (!exibeCoordenacoes && Util.isNull(boleto.getDtcVencimento()) && !showEmissaoDae) || (showEmissaoDae && Util.isNull(boleto.getDtcVencimento()))) {
			JsfUtil.addErrorMessage("O campo Data de Vencimento é de preenchimento obrigatório.");
			return;
		}

		if(showEmissaoDae && !disableVistoria && Util.isNullOuVazio(boletoDaeRequerimeno.getVlrTotalVistoria())){
			JsfUtil.addErrorMessage("O campo Área a ser vistoriada (ha)");
			return;
		}

		if ((boleto.getValBoleto() != null && boleto.getDtcVencimento() != null) || showEmissaoDae) {
			Boolean isValid = Boolean.TRUE;
			if (isAtoOutorga) {
				isValid = boleto.getValBoleto() != null;
			}
			if (isValid) {
				RequestContext.getCurrentInstance().execute("confirmacaoBoleto.show()");
			}
		}
	}

	//metodo criado apenas para fazer o poll que carrega as mensagens do growl funcionar
	public void pollAjax(){
		if (ContextoUtil.getContexto() != null && ContextoUtil.getContexto().getObject() != null && ContextoUtil.getContexto().getObject() instanceof String) {
			String msg = (String) ContextoUtil.getContexto().getObject();
			JsfUtil.addSuccessMessage(msg);
			ContextoUtil.getContexto().setObject(null);
			rendererPoll=false;
		}
		return;
	}

	public Boolean getRendererPoll() {
		return rendererPoll;
	}

	public void setRendererPoll(Boolean rendererPoll) {
		this.rendererPoll = rendererPoll;
	}

	public boolean isCalculoPorteAtivo() {
		return calculoPorteAtivo;
	}

	public void setCalculoPorteAtivo(boolean calculoPorteAtivo) {
		this.calculoPorteAtivo = calculoPorteAtivo;
	}

	public void setIsAtoOutorga(Boolean isAtoOutorga){
		this.isAtoOutorga = isAtoOutorga;
	}

	public Boolean getIsAtoOutorga(){
		return isAtoOutorga;
	}

	public List<TipoAto> getListaTipoAto() {
		return listaTipoAto;
	}

	public void setListaTipoAto(List<TipoAto> listaTipoAto) {
		this.listaTipoAto = listaTipoAto;
	}

	public List<AtoAmbiental> getListaAto() {
		return listaAto;
	}

	public void setListaAto(List<AtoAmbiental> listaAto) {
		this.listaAto = listaAto;
	}

	public TipoAto getTipoAto() {
		return tipoAto;
	}

	public void setTipoAto(TipoAto tipoAto) {
		this.tipoAto = tipoAto;
	}

	public AtoAmbiental getAtoAmbiental() {
		return atoAmbiental;
	}

	public void setAtoAmbiental(AtoAmbiental atoAmbiental) {
		this.atoAmbiental = atoAmbiental;
	}

	public boolean isExibeTextoTipologia() {
		return !Util.isNullOuVazio(this.empreendimento) && !Util.isNullOuVazio(this.empreendimento.getTextoTipologias());
	}

	private void carregarMunicipios() {
		Exception erro = null;
		try {
			this.setListaMunicipios((List<Municipio>) municipioService.filtrarListaMunicipiosPorEstado(new Estado(5)));
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}

	}

	private void carregarPortes() {
		Exception erro =null;
		try {
			this.setListaPortes(porteService.listarPorte());
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}

	}

	/**
	 * Método que verifica se o comprovante foi salvo antes de gerar a tramitação de comprovante enviado
	 * Verifica se ocorreu erro ao tentar salvar algum
	 * @return <code>Boolean<code>
	 * @see salvarUploadComprovante()
	 * 
	 */
	public Boolean verificaComprovanteSalvo(){

		if(!Util.isNullOuVazio(comprovante) && Util.isNullOuVazio(comprovante.getIdeComprovantePagamento())) {
			return false;
		}
		if(!Util.isNullOuVazio(comprovantePagamentoDaeCertificado) && Util.isNullOuVazio(comprovantePagamentoDaeCertificado.getIdeComprovantePagamentoDae())) {
			return false;
		}
		if(!Util.isNullOuVazio(comprovantePagamentoDaeVistoria) && Util.isNullOuVazio(comprovantePagamentoDaeVistoria.getIdeComprovantePagamentoDae())) {
			return false;
		}
		if((!Util.isNullOuVazio(comprovante) && !Util.isNullOuVazio(comprovante.getIdeComprovantePagamento())) ||
				(!Util.isNullOuVazio(comprovantePagamentoDaeCertificado) && !Util.isNullOuVazio(comprovantePagamentoDaeCertificado.getIdeComprovantePagamentoDae())) ||
				(!Util.isNullOuVazio(comprovantePagamentoDaeVistoria) && !Util.isNullOuVazio(comprovantePagamentoDaeVistoria.getIdeComprovantePagamentoDae()))) {
			return true;
		}

		return false;
	}

	/**
	 * Método que verifica se o comprovante foi salvo antes de gerar a tramitação de comprovante enviado
	 * 
	 */
	public void limpaCamposUploadComprovante(){
		temArquivo = false;
		temArquivoDaeCertificado = false;
		temArquivoDaeVistoria = false;
		if(!Util.isNull(listaArquivo)) {
			listaArquivo.clear();
		}
		if(!Util.isNull(listaArquivoDaeCertificado)) {
			listaArquivoDaeCertificado.clear();
		}
		if(!Util.isNull(listaArquivoDaeVistoria)) {
			listaArquivoDaeVistoria.clear();
		}

		caminhoArquivo = null;
		caminhoArquivoDaeCertificado = null;
		caminhoArquivoDaeVistoria = null;
		comprovante = null;
		comprovantePagamentoDaeCertificado = null;
		comprovantePagamentoDaeVistoria = null;

	}

	/**
	 * Método que verifica se os foi feito os uploads dos comprovantes
	 * @return <code>Boolean<code>
	 * @see salvarUploadComprovante()
	 * 
	 */
	public Boolean validaUploadComprovante(){

		if(existeBoletoPagamento && Util.isNull(fileUpload)){
			JsfUtil.addErrorMessage("O campo comprovante de pagamento do boleto de licença e/ou outorga é de preenchimento obrigatório.");
			return false;
		}else if(showUploadComprovanteDae && Util.isNull(fileUploadDaeCertificado)){
			JsfUtil.addErrorMessage("O campo comprovante de pagamento do DAE de certificado é de preenchimento obrigatório.");
			return false;
		}else if(showUploadComprovanteDaeVistoria && Util.isNull(fileUploadDaeVistoria)){
			JsfUtil.addErrorMessage("O campo comprovante de pagamento do DAE de vistoria é de preenchimento obrigatório.");
			return false;
		}
		return true;

	}
	
	/**
	 * Inicializa o {@link AlteracaoStatusController} para abrir o popup.
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param requerimentoUnico
	 */
	public void preparaPopupAlteracaoStatus(RequerimentoDTO requerimentoDTO) {
		alteracaoStatusController.setRequerimento(requerimentoDTO.getRequerimento());
		alteracaoStatusController.consultar();
	}

	public Municipio getMunicipioSelecionado() {
		return municipioSelecionado;
	}

	public void setMunicipioSelecionado(Municipio municipioSelecionado) {
		this.municipioSelecionado = municipioSelecionado;
	}

	public List<Municipio> getListaMunicipios() {
		return listaMunicipios;
	}

	public void setListaMunicipios(List<Municipio> listaMunicipios) {
		this.listaMunicipios = listaMunicipios;
	}

	public boolean isExibeCoordenacoes() {
		return exibeCoordenacoes;
	}

	public void setExibeCoordenacoes(boolean exibeCoordenacoes) {
		this.exibeCoordenacoes = exibeCoordenacoes;
	}

	public List<AtoAmbiental> getListaAtoAmbiental() {
		return listaAtoAmbiental;
	}

	public void setListaAtoAmbiental(List<AtoAmbiental> listaAtoAmbiental) {
		this.listaAtoAmbiental = listaAtoAmbiental;
	}

	public boolean isExisteBoletoPagamento() {
		return existeBoletoPagamento;
	}

	public void setExisteBoletoPagamento(boolean existeBoletoPagamento) {
		this.existeBoletoPagamento = existeBoletoPagamento;
	}

	public String getEmailValidacao() {
		return emailValidacao;
	}

	public void setEmailValidacao(String emailValidacao) {
		this.emailValidacao = emailValidacao;
	}

	private void carregarUr() {
		Exception erro = null;
		try {
			this.setListaUrs((List<Area>) areaService.filtrarAreasUR());
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}

	}

	public Area getUrSelecionada() {
		return urSelecionada;
	}

	public void setUrSelecionada(Area urSelecionada) {
		this.urSelecionada = urSelecionada;
	}

	public List<Area> getListaUrs() {
		return listaUrs;
	}

	public void setListaUrs(List<Area> listaUrs) {
		this.listaUrs = listaUrs;
	}

	public Area getUrBoleto() {
		return urBoleto;
	}

	public void setUrBoleto(Area urBoleto) {
		this.urBoleto = urBoleto;
	}

	public List<Porte> getListaPortes() {
		return listaPortes;
	}

	public void setListaPortes(List<Porte> listaPortes) {
		this.listaPortes = listaPortes;
	}

	public String getNumeroProcessoRequerimento() {
		return numeroProcessoRequerimento;
	}

	public void setNumeroProcessoRequerimento(String numeroProcessoRequerimento) {
		this.numeroProcessoRequerimento = numeroProcessoRequerimento;
	}

	public Imovel getImovel() {
		return imovel;
	}

	public void setImovel(Imovel imovel) {
		this.imovel = imovel;
	}

	public ObjetivoLimpezaArea getObjLimpezaAreaSelecionado() {
		return objLimpezaAreaSelecionado;
	}

	public void setObjLimpezaAreaSelecionado(
			ObjetivoLimpezaArea objLimpezaAreaSelecionado) {
		this.objLimpezaAreaSelecionado = objLimpezaAreaSelecionado;
	}

	public List<Pergunta> getListaPerguntas() {
		return listaPerguntas;
	}

	public void setListaPerguntas(List<Pergunta> listaPerguntas) {
		this.listaPerguntas = listaPerguntas;
	}

	public Pergunta getPerguntaProducaoVolumetricaMadeira() {
		return perguntaProducaoVolumetricaMadeira;
	}

	public void setPerguntaProducaoVolumetricaMadeira(
			Pergunta perguntaProducaoVolumetricaMadeira) {
		this.perguntaProducaoVolumetricaMadeira = perguntaProducaoVolumetricaMadeira;
	}

	public Pergunta getPerguntaRealizarQueimaControlada() {
		return perguntaRealizarQueimaControlada;
	}

	public void setPerguntaRealizarQueimaControlada(
			Pergunta perguntaRealizarQueimaControlada) {
		this.perguntaRealizarQueimaControlada = perguntaRealizarQueimaControlada;
	}

	public Pergunta getPerguntaAprovPlanoManejoFlorestal() {
		return perguntaAprovPlanoManejoFlorestal;
	}

	public void setPerguntaAprovPlanoManejoFlorestal(
			Pergunta perguntaAprovPlanoManejoFlorestal) {
		this.perguntaAprovPlanoManejoFlorestal = perguntaAprovPlanoManejoFlorestal;
	}

	public Pergunta getPerguntaAprovExecPlanManejFlorest() {
		return perguntaAprovExecPlanManejFlorest;
	}

	public void setPerguntaAprovExecPlanManejFlorest(
			Pergunta perguntaAprovExecPlanManejFlorest) {
		this.perguntaAprovExecPlanManejFlorest = perguntaAprovExecPlanManejFlorest;
	}

	public Pergunta getPerguntaCortarFlorestProducRegist() {
		return perguntaCortarFlorestProducRegist;
	}

	public void setPerguntaCortarFlorestProducRegist(
			Pergunta perguntaCortarFlorestProducRegist) {
		this.perguntaCortarFlorestProducRegist = perguntaCortarFlorestProducRegist;
	}

	public Pergunta getPerguntaAprovLocRelocServFlorest() {
		return perguntaAprovLocRelocServFlorest;
	}

	public void setPerguntaAprovLocRelocServFlorest(
			Pergunta perguntaAprovLocRelocServFlorest) {
		this.perguntaAprovLocRelocServFlorest = perguntaAprovLocRelocServFlorest;
	}

	public Integer getIdImovelMomentaneo() {
		return idImovelMomentaneo;
	}

	public void setIdImovelMomentaneo(Integer idImovelMomentaneo) {
		this.idImovelMomentaneo = idImovelMomentaneo;
	}

	public RequerimentoImovel getRequerimentoImovelAExcluir() {
		return requerimentoImovelAExcluir;
	}

	public void setRequerimentoImovelAExcluir(RequerimentoImovel requerimentoImovelAExcluir) {
		this.requerimentoImovelAExcluir = requerimentoImovelAExcluir;
	}

	public PerguntaRequerimento getPerguntaReqTemp() {
		return perguntaReqTemp;
	}

	public void setPerguntaReqTemp(PerguntaRequerimento perguntaReqTemp) {
		this.perguntaReqTemp = perguntaReqTemp;
	}

	public List<ObjetivoLimpezaArea> getCollObjetivoLimpezaArea() {
		return collObjetivoLimpezaArea;
	}

	public void setCollObjetivoLimpezaArea(
			List<ObjetivoLimpezaArea> collObjetivoLimpezaArea) {
		this.collObjetivoLimpezaArea = collObjetivoLimpezaArea;
	}

	public List<ObjetivoRequerimentoLimpezaArea> getListaDeObjRequerimentoLimpAreaAExcluir() {
		return listaDeObjRequerimentoLimpAreaAExcluir;
	}

	public void setListaDeObjRequerimentoLimpAreaAExcluir(
			List<ObjetivoRequerimentoLimpezaArea> listaDeObjRequerimentoLimpAreaAExcluir) {
		this.listaDeObjRequerimentoLimpAreaAExcluir = listaDeObjRequerimentoLimpAreaAExcluir;
	}

	public ObjetivoRequerimentoLimpezaArea getObjRequerimentoLimpAreaAExcluir() {
		return objRequerimentoLimpAreaAExcluir;
	}

	public void setObjRequerimentoLimpAreaAExcluir(
			ObjetivoRequerimentoLimpezaArea objRequerimentoLimpAreaAExcluir) {
		this.objRequerimentoLimpAreaAExcluir = objRequerimentoLimpAreaAExcluir;
	}

	public boolean isBtnCancelar() {
		return btnCancelar;
	}

	public void setBtnCancelar(boolean btnCancelar) {
		this.btnCancelar = btnCancelar;
	}

	public Pergunta getPerguntaAprovRelocServidFlorest() {
		return perguntaAprovRelocServidFlorest;
	}

	public void setPerguntaAprovRelocServidFlorest(
			Pergunta perguntaAprovRelocServidFlorest) {
		this.perguntaAprovRelocServidFlorest = perguntaAprovRelocServidFlorest;
	}

	public Pergunta getPerguntaAprovFututaRelocServidFlorest() {
		return perguntaAprovFututaRelocServidFlorest;
	}

	public void setPerguntaAprovFututaRelocServidFlorest(
			Pergunta perguntaAprovFututaRelocServidFlorest) {
		this.perguntaAprovFututaRelocServidFlorest = perguntaAprovFututaRelocServidFlorest;
	}

	public List<ImovelRural> getListaImovelRuralDoEmpreend() {
		return listaImovelRuralDoEmpreend;
	}

	public boolean getListaImovelRuralDoEmpreendIsNotNull() {
		return !Util.isNullOuVazio(listaImovelRuralDoEmpreend);
	}

	public void setListaImovelRuralDoEmpreend(
			List<ImovelRural> listaImovelRuralDoEmpreend) {
		this.listaImovelRuralDoEmpreend = listaImovelRuralDoEmpreend;
	}

	public void selectedImovelRuralChanged(ValueChangeEvent event){
		Exception erro = null;
		try {
			if (event.getNewValue() != null) {
				imovelRuralSelecionado = (ImovelRural) event.getNewValue();
				imovelRuralSelecionado = listaImovelRuralDoEmpreend.get(listaImovelRuralDoEmpreend.indexOf(imovelRuralSelecionado)).clone();
			}
			((RequerimentoImovel) ContextoUtil.getContexto().getObjectToLocGeo()).setImovel(imovelRuralSelecionado.getImovel().clone());
		} catch (CloneNotSupportedException e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}

	}

	public void setImovelRuralSelecionado(ImovelRural imovelRuralSelecionado) {
		Exception erro =null;
		try {
			if(!Util.isNull(imovelRuralSelecionado)) {
				this.imovelRuralSelecionado = imovelRuralSelecionado.clone();
			} else {
				this.imovelRuralSelecionado = imovelRuralSelecionado;
			}
		} catch (CloneNotSupportedException e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	public ImovelRural getImovelRuralSelecionado() {
		return imovelRuralSelecionado;
	}

	public Integer getNumeroDeImoveisDoEmpreendimento() {
		return numeroDeImoveisDoEmpreendimento;
	}

	public void setNumeroDeImoveisDoEmpreendimento(
			Integer numeroDeImoveisDoEmpreendimento) {
		this.numeroDeImoveisDoEmpreendimento = numeroDeImoveisDoEmpreendimento;
	}

	public LocalizacaoGeografica getLocalizacaoGeograficaASerExcluida() {
		return localizacaoGeograficaASerExcluida;
	}

	public void setLocalizacaoGeograficaASerExcluida(LocalizacaoGeografica localizacaoGeograficaASerExcluida) {
		this.localizacaoGeograficaASerExcluida = localizacaoGeograficaASerExcluida;
	}

	public Pergunta getPerguntaDalocalizacaoGeograficaASerExcluida() {
		return perguntaDalocalizacaoGeograficaASerExcluida;
	}

	public void setPerguntaDalocalizacaoGeograficaASerExcluida(Pergunta perguntaDalocalizacaoGeograficaASerExcluida) {
		this.perguntaDalocalizacaoGeograficaASerExcluida = perguntaDalocalizacaoGeograficaASerExcluida;
	}

	public Pergunta getPerguntaTemp() {
		return perguntaTemp;
	}

	public void setPerguntaTemp(Pergunta perguntaTemp) {
		this.perguntaTemp = perguntaTemp;
	}

	public Wizard getWizard() {
		return wizard;
	}

	public void setWizard(Wizard wizard) {
		this.wizard = wizard;
	}

	public String getHintAjuda() {
		return hintAjuda;
	}

	public void setHintAjuda(String hintAjuda) {
		this.hintAjuda = hintAjuda;
	}

	public Boolean getIsAtoRloRlu() {
		return isAtoRloRlu;
	}

	public void setIsAtoRloRlu(Boolean isAtoRloRlu) {
		this.isAtoRloRlu = isAtoRloRlu;
	}

	public Requerimento getRequerimento() {
		if(Util.isNull(requerimento)) {
			try {
				requerimento = requerimentoService.carregar(new Requerimento(requerimentoUnico.getIdeRequerimentoUnico()));
			} 
			catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

}

class  pageCount {
	static int firtPage;
	static int fPageSize;
}