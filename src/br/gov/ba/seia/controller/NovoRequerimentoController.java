package br.gov.ba.seia.controller;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

import com.vividsolutions.jts.geom.Point;

import br.gov.ba.seia.dao.ControleTramitacaoDAOImpl;
import br.gov.ba.seia.dto.ProcessoReenquadramentoDTO;
import br.gov.ba.seia.dto.RequerimentoDTO;
import br.gov.ba.seia.dto.RequerimentoUnicoDTO;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.ComunicacaoReenquadramentoProcesso;
import br.gov.ba.seia.entity.ControleProcessoAto;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.DeclaracaoInexigibilidade;
import br.gov.ba.seia.entity.DocumentoAto;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.Enquadramento;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.EnquadramentoDocumentoAto;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.Licenca;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Orgao;
import br.gov.ba.seia.entity.Outorga;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeograficaImovel;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeograficaImovelPK;
import br.gov.ba.seia.entity.PagamentoReposicaoFlorestal;
import br.gov.ba.seia.entity.Pergunta;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.Porte;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.ProcessoExterno;
import br.gov.ba.seia.entity.ProcessoTramite;
import br.gov.ba.seia.entity.ProcuradorPessoaFisica;
import br.gov.ba.seia.entity.ProcuradorRepresentante;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.entity.RequerimentoUnico;
import br.gov.ba.seia.entity.Sistema;
import br.gov.ba.seia.entity.SistemaCoordenada;
import br.gov.ba.seia.entity.SolicitacaoAdministrativo;
import br.gov.ba.seia.entity.StatusFluxo;
import br.gov.ba.seia.entity.StatusReenquadramento;
import br.gov.ba.seia.entity.StatusRequerimento;
import br.gov.ba.seia.entity.Telefone;
import br.gov.ba.seia.entity.TipoAto;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.TipoPessoaRequerimento;
import br.gov.ba.seia.entity.TipoProrrogacaoPrazoValidade;
import br.gov.ba.seia.entity.TipoRequerimento;
import br.gov.ba.seia.entity.TipoSolicitacao;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.AcaoEnum;
import br.gov.ba.seia.enumerator.AreaEnum;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.FuncionalidadeEnum;
import br.gov.ba.seia.enumerator.PerfilEnum;
import br.gov.ba.seia.enumerator.PerguntaEnum;
import br.gov.ba.seia.enumerator.SecaoEnum;
import br.gov.ba.seia.enumerator.SistemaEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.StatusProcessoAtoEnum;
import br.gov.ba.seia.enumerator.StatusReenquadramentoEnum;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoCertificadoEnum;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoSolicitacaoEnum;
import br.gov.ba.seia.enumerator.TipoTelefoneEnum;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.facade.DeclaracaoIntervencaoAppFacade;
import br.gov.ba.seia.facade.NovoRequerimentoServiceFacade;
import br.gov.ba.seia.facade.ProcessoRequerimentoServiceFacade;
import br.gov.ba.seia.facade.TramitacaoReenquadramentoProcessoServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.ImpressoraAtoDeclaratorioDIAP;
import br.gov.ba.seia.interfaces.ImpressoraAtoDeclaratorioIF;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.AtoAmbientalService;
import br.gov.ba.seia.service.CertificadoService;
import br.gov.ba.seia.service.ComunicacaoRequerimentoService;
import br.gov.ba.seia.service.DeclaracaoInexigibilidadeService;
import br.gov.ba.seia.service.DiscordarReenquadramentoService;
import br.gov.ba.seia.service.DocumentoAtoAmbientalService;
import br.gov.ba.seia.service.DocumentoObrigatorioRequerimentoService;
import br.gov.ba.seia.service.EmailService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.EnderecoService;
import br.gov.ba.seia.service.EnquadramentoService;
import br.gov.ba.seia.service.FuncionarioService;
import br.gov.ba.seia.service.ImovelService;
import br.gov.ba.seia.service.LacTransporteService;
import br.gov.ba.seia.service.LicencaService;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.NovoRequerimentoService;
import br.gov.ba.seia.service.OutorgaLocalizacaoGeograficaService;
import br.gov.ba.seia.service.PagamentoReposicaoFlorestalService;
import br.gov.ba.seia.service.PerguntaRequerimentoService;
import br.gov.ba.seia.service.PerguntaService;
import br.gov.ba.seia.service.PermissaoPerfilService;
import br.gov.ba.seia.service.PessoaService;
import br.gov.ba.seia.service.PorteService;
import br.gov.ba.seia.service.ProcessoExternoService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.RelatoriosRequerimentoService;
import br.gov.ba.seia.service.RepresentanteLegalService;
import br.gov.ba.seia.service.RequerimentoPessoaService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.RequerimentoUnicoService;
import br.gov.ba.seia.service.SolicitacaoAdministrativoService;
import br.gov.ba.seia.service.StatusRequerimentoService;
import br.gov.ba.seia.service.TelefoneService;
import br.gov.ba.seia.service.TipoAtoService;
import br.gov.ba.seia.service.TipologiaService;
import br.gov.ba.seia.service.TramitacaoProcessoService;
import br.gov.ba.seia.service.TramitacaoRequerimentoService;
import br.gov.ba.seia.service.VwConsultaProcessoService;
import br.gov.ba.seia.service.facade.ComunicacaoReenquadramentoProcessoServiceFacade;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.PostgisUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.ReportUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.security.SecurityController;

@Named("novoRequerimentoController")
@ViewScoped
public class NovoRequerimentoController implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");

	@Inject
	private AbaFlorestalController abaFlorestalController;

	@Inject
	private AbaFinalizarRequerimentoController abaFinalizarRequerimentoController;

	@Inject
	private AbaFaunaController abaFaunaController;

	@Inject
	private AbaLicencaAutorizacaoController abaLicencaAutorizacaoController;

	@Inject
	private AbaOutorgaController abaOutorgaController;

	@Inject
	private AbaProcessoController abaProcessoController;

	@Inject
	private AbaRenovacaoAlteracaoProrrogacaoController abaRenovacaoAlteracaoProrrogacaoController;

	@Inject
	private IdentificarPapelController identificarPapelController;

	@Inject
	private EnquadramentoController enquadramentoController;
	
	@Inject
	private AceiteReenquadramentoController aceiteReenquadramentoController;
	
	@EJB
	private OutorgaLocalizacaoGeograficaService outorgaLocalizacaoGeograficaService;

	@EJB
	private ProcessoRequerimentoServiceFacade processoRequerimentoServiceFacade;

	@EJB
	private ImovelService imovelService;

	@EJB
	private RequerimentoUnicoService requerimentoUnicoService;

	@EJB
	private EmpreendimentoService empreendimentoService;

	@EJB
	private RelatoriosRequerimentoService relatorioRequerimentoServiceFacade;

	@EJB
	private ComunicacaoRequerimentoService comunicacaoRequerimentoService;

	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;

	@EJB
	private DocumentoAtoAmbientalService documentoAtoAmbientalService;

	@EJB
	private EnquadramentoService enquadramentoService;

	@EJB
	private TelefoneService telefoneService;

	@EJB
	private NovoRequerimentoService novoRequerimentoService;

	@EJB
	private ProcessoService processoService;

	@EJB
	private VwConsultaProcessoService vwConsultaProcessoService;

	@EJB
	private RequerimentoPessoaService requerimentoPessoaService;

	@EJB
	private ProcessoExternoService processoExternoService;

	@EJB
	private PerguntaService perguntaService;

	@EJB
	private SolicitacaoAdministrativoService solicitacaoAdministrativoService;

	@EJB
	private MunicipioService municipioService;

	@EJB
	private EnderecoService enderecoService;

	@EJB
	private TipoAtoService tipoAtoService;

	@EJB
	private TipologiaService tipologiaService;
	
	@EJB
	private PagamentoReposicaoFlorestalService  pagagementoReposicaoFlorestalService;

	@EJB
	private StatusRequerimentoService statusRequerimentoService;

	@EJB
	private AreaService areaService;

	@EJB
	private CertificadoService certificadoService;

	@EJB
	private PorteService porteService;

	@EJB
	private PessoaService pessoaService;

	@EJB
	private AtoAmbientalService atoAmbientalService;

	@EJB
	private RequerimentoService requerimentoService;

	@EJB
	private DocumentoObrigatorioRequerimentoService documentoObrigatorioReqService;

	@EJB
	private TramitacaoRequerimentoService tramitacaoRequerimentoService;

	@EJB
	private NovoRequerimentoServiceFacade novoRequerimentoServiceFacade;

	@EJB
	private EmailService emailService;

	@EJB
	private LacTransporteService lacTransporteService;

	@EJB
	private LicencaService licencaService;

	@EJB
	private ControleTramitacaoDAOImpl controleTramitacaoDAOImpl;

	@EJB
	private PerguntaRequerimentoService perguntaRequerimentoService;

	@EJB
	private RepresentanteLegalService representanteLegalService;

	@EJB
	private PermissaoPerfilService permissaoPerfilService;

	@EJB
	private FuncionarioService funcionarioService;

	@EJB
	private DeclaracaoIntervencaoAppFacade diapFacade;
	
	@EJB
	private DeclaracaoInexigibilidadeService declaracaoInexigibilidadeService;
	
	private SecurityController securityController;
	
	@EJB
	private TramitacaoProcessoService tramitacaoProcessoService;
	
	@EJB
	private TramitacaoReenquadramentoProcessoServiceFacade tramitacaoReenquadramentoProcessoServiceFacade;
	
	@EJB
	private ComunicacaoReenquadramentoProcessoServiceFacade comunicacaoReenquadramentoProcessoService;
	// Tela Novo Requerimento
	private MetodoUtil metodoExterno;
	private int activeTab;
	private Boolean mostraQuestionarioAposSalvar;

	private String dataHoje;

	private Empreendimento empreendimentoBusca;
	private Empreendimento empreendimento;
	private Pessoa pessoa;

	private Boolean enderecoContato = Boolean.FALSE;
	private boolean dadosContato;

	// Utilizar essas variáveis para MODO DE VISUALIZAÇÃO
	private boolean desabilitarTudo;
	private boolean declaracao = false;
	private boolean declaracaoAutorizacaoEspecial = false;
	private boolean modoEdicao = false;
	private boolean modoEnquadramento;
	private boolean modoEdicaoDialog = false;
	private boolean modoEdicaoSubDialog;
	private Boolean visualizaSelectEmpreendimento = true;
	private boolean isImovelUrbano;

	private String dataMinima;

	private List<SelectItem> collEmpreendimento;

	private boolean perguntasAba0Carregadas;
	private List<Pergunta> listPerguntas;
	private List<PerguntaRequerimento> listPerguntaRequerimentoAba0;
	private PerguntaRequerimento pergunta0_1;
	private String novaRazaoSocial;
	private SolicitacaoAdministrativo solicitacaoAdministrativo;

	// Dialog Incluir Vértice
	private List<LocalizacaoGeografica> listaLocalizacaoGeografica;
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
	private Map<Integer, LocalizacaoGeografica> mapLocGeo;
	private TipoProrrogacaoPrazoValidade tipoProrrogacaoSelecionado;
	private LocalizacaoGeografica[] listLocalizacaoGeo;

	// Requerimento
	private Requerimento requerimento;
	private Requerimento requerimentoSelecionado;

	private Imovel imovelSelecionado;
	private List<Imovel> listaImovel;
	private List<Imovel> listaImovelSelecionado;

	private ProcessoExterno processoExterno;
	private boolean imovelUnico;
	private boolean disabledAbasRequerimento;

	private boolean existeSolicitacao;
	// consulta
	// Bindings
	private Pessoa requerente;
	private Requerimento requerimentoBusca;

	private EmpreendimentoRequerimento empreendimentoRequerimento;

	HashMap<String, String> pontosLocGeoOutorga;

	private TipoAto tipoAto;
	private List<TipoAto> listaTipoAto;
	private Funcionario tecnico;

	private AtoAmbiental atoAmbiental;
	private List<AtoAmbiental> listaAto;
	
	private PagamentoReposicaoFlorestal pagamentoReposicaoFlorestal ;
	private List<PagamentoReposicaoFlorestal> pagamentoReposicaoFlorestalList;

	private Collection<StatusRequerimento> collectionStatusRequerimento;
	private StatusRequerimento statusRequerimento;
	private String numeroRequerimento;

	private Area urSelecionada;
	private List<Area> listaUrs;

	private Municipio municipioSelecionado;
	private ArrayList<Municipio> municipios;
	private List<Municipio> listaMunicipios;

	private List<Porte> listaPortes;
	private Porte idePorte;

	private Date periodoInicio;
	private Date periodoFim;

	private LazyDataModel<RequerimentoDTO> listaRequerimentosDTOModel;
	private DataTable dataTableRequerimentos;

	private RequerimentoDTO requerimentoDTO;
	private RequerimentoUnicoDTO requerimentoUnicoDTO;
	private Tipologia tipologiaDivisao;
	private List<Tipologia> listaTipologiaDivisao;
	private Tipologia tipologiaAtividade;
	private List<Tipologia> listaTipologiaAtividade;

	private EmpreendimentoRequerimento empreendimentoRequerimentoBuscado;

	private OutorgaLocalizacaoGeograficaImovel outorgaLocalizacaoGeograficaImovel;
	private OutorgaLocalizacaoGeograficaImovelPK outorgaLocalizacaoGeograficaImovelPK;
	private Boolean rendererPoll;

	private List<SolicitacaoAdministrativo> listaSolicitacaoAdministrativo;
	private String numProcesso;

	private boolean emailRequerimentoCarregadoPeloEmpreendimento;
	
	// Tipologia:
	private Collection<Tipologia> listaTipologia;
	private Tipologia tipologia;
	
	// Finalidade:
	private Collection<TipoFinalidadeUsoAgua> listaFinalidadeUsoAgua;	
	private TipoFinalidadeUsoAgua finalidadeUsoAgua;
	
	//Relatório
	private boolean consultaRealizada = false;

	@Inject
	private IdentificarTipoSolicitacaoController identificarTipoSolicitacaoController;
	
	private ProcessoReenquadramentoDTO processoReenquadramentoDTO;
	
	
	@EJB
	private DiscordarReenquadramentoService discordarReenquadramentoService;
	
	//Constantes
	public static final String URL_PAUTA_REENQUADRAMENTO = "/paginas/manter-processo/pautaReenquadramentoProcesso.xhtml"; 

	
	private boolean isEnquadramento = false;
	
	private boolean tipoDeImpressaoPdf;
	
	public void limparSolicitacaoAdministrativo() {
		numProcesso = "";
		listaSolicitacaoAdministrativo = new ArrayList<SolicitacaoAdministrativo>();
	}
	
	public String carregarReenquadramento(ProcessoReenquadramentoDTO processoReenquadramentoDTO) throws Exception {
		processoReenquadramentoDTO.setVisualizar(false);
		String retorno =  novoRequerimentoServiceFacade.carregarReenquadramento(identificarTipoSolicitacaoController, processoReenquadramentoDTO);
		SessaoUtil.removerObjetoSessao("isRespostaChange");
		SessaoUtil.adicionarObjetoSessao("isEnquadramento",true);
		if (Util.isNullOuVazio(retorno)){
			this.processoReenquadramentoDTO = processoReenquadramentoDTO;
			RequestContext.getCurrentInstance().execute("dlgRequerimentoUnico.show()");
			return null;
		}
		else {
			return retorno;
		}
	}
	
	public String carregarReenquadramentoVisualizacao(ProcessoReenquadramentoDTO processoReenquadramentoDTO) throws Exception {
		processoReenquadramentoDTO.setVisualizar(true);
		String retorno =  novoRequerimentoServiceFacade.carregarReenquadramento(identificarTipoSolicitacaoController, processoReenquadramentoDTO);
		SessaoUtil.removerObjetoSessao("isRespostaChange");
		if (Util.isNullOuVazio(retorno)){
			this.processoReenquadramentoDTO = processoReenquadramentoDTO;
			RequestContext.getCurrentInstance().execute("dlgRequerimentoUnico.show()");
			return null;
		}
		else {
			return retorno;
		}
	}

	public void adicionarSolicitacaoAdministrativo() {
		try {

			Processo processo = processoService.buscarProcessoPorCriteria(numProcesso);
			ProcessoExterno processoExterno = processoExternoService.buscarProcessoExternoByNumeroProcesso(numProcesso);

			SolicitacaoAdministrativo solicitacaoAdministrativo = new SolicitacaoAdministrativo();
			solicitacaoAdministrativo.setIdeTipoSolicitacao(new TipoSolicitacao(TipoSolicitacaoEnum.ALTERACAO_RAZAO_SOCIAL.getId()));

			if(!Util.isNull(processo)) {

				boolean isProcessoConluido = processoService.isProcessoNoStatus(processo.getIdeProcesso(), StatusFluxoEnum.CONCLUIDO.getStatus());

				if(isProcessoConluido) {
					solicitacaoAdministrativo.setNumProcesso(processo.getNumProcesso());
					solicitacaoAdministrativo.setIdeSistema(getSistema(SistemaEnum.SEIA.getNomSistema()));
				}
				else {
					throw new SeiaValidacaoRuntimeException("O processo informado ainda não está concluído.");
				}
			}
			else if(!Util.isNull(processoExterno)) {
				solicitacaoAdministrativo.setNumProcesso(processoExterno.getProcesso());
				solicitacaoAdministrativo.setIdeSistema(getSistema(processoExterno.getSistema()));
			}
			else {
				solicitacaoAdministrativo.setNumProcesso(numProcesso);
				solicitacaoAdministrativo.setIdeSistema(getSistema(SistemaEnum.DESCONHECIDO.getNomSistema()));
			}

			listaSolicitacaoAdministrativo = new ArrayList<SolicitacaoAdministrativo>();
			listaSolicitacaoAdministrativo.add(solicitacaoAdministrativo);
			RequestContext.getCurrentInstance().execute("dlgInformaProcessoConcluido.hide()");
			JsfUtil.addSuccessMessage("Operação realizada com sucesso.");

		}
		catch (SeiaValidacaoRuntimeException e) {
			JsfUtil.addWarnMessage(e.getMessage());
		}
		catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private Sistema getSistema(String sistema) {
		for (SistemaEnum sistemaEnum : SistemaEnum.values()) {
			if(sistemaEnum.getNomSistema().equalsIgnoreCase(sistema)){
				return  new Sistema(sistemaEnum.getId(), sistemaEnum.getNomSistema());
			}
		}

		return new Sistema(SistemaEnum.DESCONHECIDO.getId(), SistemaEnum.DESCONHECIDO.getNomSistema());
	}
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		System.out.println("INIT");
		carregaPerguntas();
		carregarMunicipios();
		carregarUr();
		carregarPortes();
		carregarDivisao();
		metodoExterno = new MetodoUtil(this, "selecionarRequerente", Pessoa.class);
		FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale("pt", "BR"));
		listaRequerimentosDTOModel = (LazyDataModel<RequerimentoDTO>) getAtributoSession("REQUERIMENTOS");
		// FECHA TELA DE CONSULTA

		mostraQuestionarioAposSalvar = Boolean.FALSE;
		
		if(!Util.isNull(this.identificarTipoSolicitacaoController)){
			this.empreendimento = this.identificarTipoSolicitacaoController.getEmpreendimento();
			this.empreendimentoRequerimento = this.identificarTipoSolicitacaoController.getEmpreendimentoRequerimento();
		}
		

		// Depois de selecionar AVANÇAR na tela de seleção
		if (Util.isNullOuVazio(this.pessoa)) {
			pessoa = ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().getPessoa();
		} else {
			ContextoUtil.getContexto().setRequerenteRequerimento(this.pessoa);
		}
		FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap();

		if (Util.isNullOuVazio(empreendimento)) {
			this.empreendimento = new Empreendimento();
		}

		if (Util.isNullOuVazio(empreendimentoRequerimento)) {
			empreendimentoRequerimento = new EmpreendimentoRequerimento();
			empreendimentoRequerimento.setIdeEmpreendimento(empreendimento);
		}

		addAtributoSessao("EMPREENDIMENTO_SESSAO", null);

		final Object temp = ContextoUtil.getContexto().getObject();
		// Caso o usuário clique em VISUALIZAR ou EDITAR na tela de CONSULTA.
		if (temp != null && temp instanceof RequerimentoDTO) {
			requerimentoDTO = ((RequerimentoDTO) temp);
			if (requerimentoDTO.isVisualizar()) {
				desabilitarTudo = true;
				declaracao = declaracaoAutorizacaoEspecial = true;
			} else {
				modoEdicao = true;
			}
			requerimento = requerimentoDTO.getRequerimento();
			empreendimentoRequerimento.setIdeRequerimento(requerimento);

			empreendimento = requerimentoDTO.getEmpreendimento();

			if(!Util.isNullOuVazio(empreendimento) &&!Util.isNullOuVazio(empreendimento.getIdeEmpreendimento())){

				empreendimentoRequerimento.setIdeRequerimento(requerimento);
				empreendimentoRequerimento.setIdeEmpreendimento(empreendimento);
			}

			try {
				empreendimentoRequerimento = requerimentoService.carregarEmpreendimentoRequerimento(requerimento.getIdeRequerimento());
			} catch (Exception e1) {
				System.out.println("Erro ao CARREGAR EMPREENDIMENTO REQUERIMENTO!!");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e1);
				throw Util.capturarException(e1, Util.SEIA_EXCEPTION);
			}

			addAtributoSessao("EMPREENDIMENTO_SESSAO", empreendimento);
			this.modoEnquadramento = requerimentoDTO.isModoEnquadramento();

			this.carregarEnderecoContato();


			try {
				PerguntaRequerimento pr = 
						perguntaRequerimentoService.buscarPerguntaRequerimentoPorRequerimentoECodPergunta
							(this.requerimento, pergunta0_1.getIdePergunta().getCodPergunta());

				if(pr != null) {
					pergunta0_1 = pr;
				}

				if(pr != null && pr.getIndResposta() != null && pr.getIndResposta()) {

					if(solicitacaoAdministrativo == null) {

						solicitacaoAdministrativo = new SolicitacaoAdministrativo();
						solicitacaoAdministrativo = solicitacaoAdministrativoService.obterSolicitacaoAdministrativa(requerimento, TipoSolicitacaoEnum.ALTERACAO_RAZAO_SOCIAL);

						listaSolicitacaoAdministrativo = new ArrayList<SolicitacaoAdministrativo>();
						listaSolicitacaoAdministrativo.add(solicitacaoAdministrativo);
					}

					if(!Util.isNull(solicitacaoAdministrativo) && !Util.isNull(solicitacaoAdministrativo.getNomRazaoSocialNova())) {
						novaRazaoSocial = solicitacaoAdministrativo.getNomRazaoSocialNova();
						mostraQuestionarioAposSalvar = Boolean.FALSE;
					}
				} else {

					pergunta0_1.setIndResposta(false);
					mostraQuestionarioAposSalvar = Boolean.TRUE;
					if(Util.isNullOuVazio(requerimentoDTO.getEmpreendimento())){
						requerimentoDTO.setEmpreendimento(new Empreendimento(0));
						empreendimento = new Empreendimento(0);

					}
					
					
						empreendimento.setIdeEmpreendimento(requerimentoDTO.getEmpreendimento().getIdeEmpreendimento());
						if (Util.isNullOuVazio(listaImovel)) {
							listaImovel = new ArrayList<Imovel>();
						}
						if(!empreendimento.getIdeEmpreendimento().equals(0)){
							listarImovel(empreendimento); 
						}
						
						
						try {
							
							if (Util.isNullOuVazio(getEmpreendimentoRequerimentoBuscado())) {
								empreendimentoRequerimentoBuscado = new EmpreendimentoRequerimento();
							}
							
							if (!Util.isNullOuVazio(requerimento) && !Util.isNullOuVazio(empreendimento)) {
								
								empreendimentoRequerimentoBuscado = empreendimentoService.buscarEmpreendimentoRequerimento(requerimento, empreendimento);
								
								if (!Util.isNullOuVazio(empreendimentoRequerimento)) {
									
									empreendimentoRequerimento.setDtcFaseEmpreendimento(empreendimentoRequerimentoBuscado.getDtcFaseEmpreendimento());
									empreendimentoRequerimento.setIdeFaseEmpreendimento(empreendimentoRequerimentoBuscado.getIdeFaseEmpreendimento());
									empreendimentoRequerimento.setIdePorte(empreendimentoRequerimentoBuscado.getIdePorte());
								}
							}
						} catch (Exception e) {
							JsfUtil.addErrorMessage("Erro ao buscar o EmpreendimentoRequerimento");
							Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
							throw Util.capturarException(e, Util.SEIA_EXCEPTION);
						}
					
				}

				pessoa = ((RequerimentoDTO) temp).getPessoa();
				requerimentoSelecionado = requerimento;
				
				if(!Util.isNull(requerimentoDTO.getProcessoReenquadramentoDTO())) {
					aceiteReenquadramentoController.carregarProcessoReenquadramento(requerimentoDTO.getProcessoReenquadramentoDTO());
				}

				ContextoUtil.getContexto().setObject(null);
			} catch (Exception e1) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e1);
				throw Util.capturarException(e1, Util.SEIA_EXCEPTION);
			}
		} else {
			if(empreendimento.getIdeEmpreendimento() == null){
				empreendimento.setIdeEmpreendimento(0);
			}
			requerimento = new Requerimento();
			requerimentoSelecionado = requerimento;
			
			if(pergunta0_1 != null) {
				pergunta0_1.setIndResposta(false);
			}
		}

		if (pessoa != null) {

			List<Telefone> listaTel = null;

			try {
				listaTel = telefoneService.buscarTelefonesPorPessoa(pessoa);
			} catch (Exception e) {
				Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			}

			if (!Util.isNullOuVazio(pessoa.getPessoaFisica()) && !Util.isNullOuVazio(pessoa.getPessoaFisica().getNomPessoa()) && Util.isNullOuVazio(requerimento.getNomContato())) {
				requerimento.setNomContato(pessoa.getPessoaFisica().getNomPessoa());

				if (!Util.isNullOuVazio(pessoa.getPessoaFisica().getPessoa().getDesEmail()) && Util.isNullOuVazio(requerimento.getDesEmail())) {
					requerimento.setDesEmail(pessoa.getPessoaFisica().getPessoa().getDesEmail());
				}
			} else if (!Util.isNullOuVazio(pessoa.getPessoaJuridica()) && Util.isNullOuVazio(requerimento.getNomContato())) {

				requerimento.setNomContato(pessoa.getPessoaJuridica().getNomRazaoSocial());

				Integer tipoSolicit = ContextoUtil.getContexto().getTipoSolicitante();
				ContextoUtil.getContexto().setTipoSolicitante(null);

				if (Util.isNullOuVazio(tipoSolicit) || tipoSolicit.equals(1)) {
					PessoaJuridica pJuridica = null;
					if(ContextoUtil.getContexto().getSolicitanteRequerimento() != null){
						pJuridica = ContextoUtil.getContexto().getSolicitanteRequerimento().getPessoaJuridica();
					}

					if(!Util.isNullOuVazio(pJuridica) && !Util.isNullOuVazio(pJuridica.getPessoa()) && !Util.isNullOuVazio(pJuridica.getPessoa().getDesEmail())) {
						requerimento.setDesEmail(pJuridica.getPessoa().getDesEmail());
					}
				} else if (tipoSolicit.equals(4) || tipoSolicit.equals(2)) {

					if (!Util.isNullOuVazio(ContextoUtil.getContexto().getSolicitanteRequerimento().getPessoaFisica())) {
						requerimento.setDesEmail(ContextoUtil.getContexto().getSolicitanteRequerimento().getPessoaFisica().getPessoa().getDesEmail());
					} else {
						requerimento.setDesEmail(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa().getDesEmail());
					}
				}
			}

			if (!listaTel.isEmpty() && Util.isNullOuVazio(requerimento.getNumTelefone())) {
				try {
					if (listaTel.size() > 1) {
						requerimento.setNumTelefone(getTelefoneParaRequerimento(listaTel).getNumTelefone());
					} else {
						requerimento.setNumTelefone(listaTel.get(0).getNumTelefone());
					}
				} catch (Exception e) {
					requerimento.setNumTelefone(listaTel.get(0).getNumTelefone());
					Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
				}
			}
		}

		if (collEmpreendimento == null) {
			this.carregarEmpreendimento();
		}

		carregarTipoAto();

		if (temp != null && temp instanceof String) {
			JsfUtil.addSuccessMessage((String) temp);
			ContextoUtil.getContexto().setObject(null);
		}
		
		securityController = (SecurityController) SessaoUtil.recuperarManagedBean("#{security}", SecurityController.class);
	}

	public Collection<Funcionario> carregarFuncionariosAtend(String nomeFuncionario) {
		try {
			return funcionarioService.listarFuncionarioPor(new Area(AreaEnum.ATEND.getId()),nomeFuncionario);
		}
		catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void carregarEnderecoContato() {

		try {

			if (!Util.isNullOuVazio(requerimento.getIdeEnderecoContato()) && !Util.isNullOuVazio(requerimento.getIdeEnderecoContato().getIdeEndereco())) {

				this.enderecoContato = true;
				Endereco endereco = this.enderecoService.carregar(requerimento.getIdeEnderecoContato().getIdeEndereco());
				ContextoUtil.getContexto().setRequerimentoEndereco(endereco);
			} else {
				requerimento.setIdeEnderecoContato(null);
			}
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
		}
	}

	public boolean isDisabledComboAtividade() {
		return Util.isNull(tipologiaDivisao);
	}

	public boolean isDisabledComboAto() {
		return Util.isNull(tipoAto);
	}

	public void visualizarProcesso(RequerimentoDTO requerimentoDTO){
		try {

			this.requerimentoDTO = requerimentoDTO;
			
			
			if(Util.isNullOuVazio(requerimentoDTO.getProcesso())){
				TramitacaoRequerimento tramitacaoRequerimento = tramitacaoRequerimentoService.buscarUltimaTramitacaoPorRequerimento(requerimentoDTO.getRequerimento().getIdeRequerimento());
				Pessoa operador = tramitacaoRequerimento.getIdePessoa();
				
				Area area = requerimentoDTO.getRequerimento().getIdeArea(); 
				
				if(Util.isNullOuVazio(area)) {
					EnquadramentoAtoAmbiental enquadramentoAto = enquadramentoService.buscarEnquadramentoAtoAmbientalByRequerimento(requerimentoDTO.getRequerimento().getIdeRequerimento()).get(0);
					if(enquadramentoAto.getAtoAmbiental().isDiap()) {
						area = new Area(8);//coins
					}
					
					if(enquadramentoAto.getAtoAmbiental().isAPE()) {
						area = new Area(39);//coasp
					}
					
					
				}
				
				//Se a área veio null e não foi DIAP e nem APE, então não gera o processo
				if(!Util.isNullOuVazio(area)) {
					processoService.gerarProcesso(requerimentoDTO.getRequerimento(), operador, area.getIdeArea(), false);
				}
			}
			
			requerimentoDTO.setProcesso(processoService.buscarPorRequerimento(requerimentoDTO.getRequerimento().getIdeRequerimento()));
			
			VwConsultaProcesso vwProcesso= vwConsultaProcessoService.buscarVwConsultaProcessoPorIdeProcesso(requerimentoDTO.getProcesso().getIdeProcesso(), false);
			DetalharProcessoController detalharProcessoController = (DetalharProcessoController) SessaoUtil.recuperarManagedBean("#{detalharProcessoController}", DetalharProcessoController.class);
			detalharProcessoController.setVwProcesso(vwProcesso);
			detalharProcessoController.visualizarProcesso();
			HistoricoTramitacaoProcessoController historicoTramitacaoProcessoController = (HistoricoTramitacaoProcessoController) SessaoUtil.recuperarManagedBean("#{historicoTramitacaoProcessoController}", HistoricoTramitacaoProcessoController.class);
			historicoTramitacaoProcessoController.setVwProcesso(vwProcesso);

		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void carregarDivisao() {
		try {
			if(Util.isNullOuVazio(listaTipologiaDivisao)) {
				listaTipologiaDivisao = tipologiaService.listarTipologiaDivisao();
			}
		}
		catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void carregarAtividade() {
		try {
			if(!Util.isNull(tipologiaDivisao)) {
				listaTipologiaAtividade = tipologiaService.listarTipologiaAtividade(tipologiaDivisao);
			}
			else{
				listaTipologiaAtividade = null;
			}
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}

	}

	public void carregaPerguntas() {

		if (!perguntasAba0Carregadas) {
			try {
				listPerguntas = new ArrayList<Pergunta>();
				listPerguntas = perguntaService.carregarPerguntasNovoRequerimento(0);

				listPerguntaRequerimentoAba0 = new ArrayList<PerguntaRequerimento>();

				for (Pergunta pergunta : listPerguntas) {
					if (pergunta.getCodPergunta().equals(PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA0_1.getCod())) {
						pergunta0_1 = new PerguntaRequerimento(pergunta);
						listPerguntaRequerimentoAba0.add(getPergunta0_1());
					}
				}

				perguntasAba0Carregadas = true;
			} catch (Exception e) {
				perguntasAba0Carregadas = false;
				JsfUtil.addErrorMessage("Ocorreu um erro ao carregar as perguntas " + e.getMessage());
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
		}
	}

	public void changePergunta0_1(ValueChangeEvent event) {
		Boolean resposta = (Boolean) event.getNewValue();
		this.pergunta0_1.setIndResposta(resposta);
		if (resposta == false) {
			novaRazaoSocial = null;
		} else {
			if (!Util.isNullOuVazio(empreendimento)) {
				empreendimento.setIdeEmpreendimento(0);
			}
		}
	}

	public void incluirEmpreendimento() {
		this.addAtributoSessao("URL_EMPREENDIMENTO_ORIGEM", "/paginas/novo-requerimento/incluirNovoRequerimento.xhtml");
		
		EmpreendimentoController ec = (EmpreendimentoController) SessaoUtil.recuperarManagedBean("#{empreendimentoController}", EmpreendimentoController.class);
		ec.setUrlOrigem("/paginas/novo-requerimento/incluirNovoRequerimento.xhtml");
		
		Html.redirecionarPagina("/paginas/manter-empreendimento/empreendimento.xhtml");
	}

	public void carregarEmpreendimento() {
		try {

			Collection<Empreendimento> collectionsEmpreendimento = inicializaCollections();

			if (!Util.isNull(requerente)) {
				collectionsEmpreendimento = empreendimentoService.listarEmpreendimento(requerente);
			} else if (!Util.isNullOuVazio(pessoa)) {
				collectionsEmpreendimento = requerimentoUnicoService.listarEmpreendimentoPorPessoa(pessoa);
			}

			if (!Util.isNullOuVazio(collectionsEmpreendimento)) {
				for (Empreendimento empreendimento : collectionsEmpreendimento) {
					SelectItem item = new SelectItem(empreendimento.getIdeEmpreendimento(),empreendimento.getNomEmpreendimento());
					this.collEmpreendimento.add(item);
				}
			}
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Não foi possível carregar a lista de empreendimentos - " + e.getMessage());
		}
	}

	private Collection<Empreendimento> inicializaCollections() {

		collEmpreendimento = new ArrayList<SelectItem>();
		Collection<Empreendimento> collectionsEmpreendimento = null;
		return collectionsEmpreendimento;
	}



	public List<Imovel> listarImovel() {
		try {
			listaImovel = imovelService.filtrarListaImovelPorEmpreendimento(this.empreendimento);
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao carregar a lista de imóveis:" + e.getMessage());
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
		}
		if (!Util.isNullOuVazio(listaImovel) && !Util.isNull(listaImovel.iterator().next().getImovelUrbano())) {
			isImovelUrbano = true;
		}
		return listaImovel;
	}

	public List<Imovel> listarImovel(Empreendimento empreendimento) {
		try {
			listaImovel = imovelService.filtrarListaImovelPorEmpreendimento(empreendimento);
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao carregar a lista de imóveis:" + e.getMessage());
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
		}
		// Se o primeiro imovel for Urbano (TipoImovel == 2) -- Se for Urbano só terá um imovel.
		if (!Util.isNullOuVazio(listaImovel) && !Util.isNullOuVazio(listaImovel.iterator().next().getImovelUrbano())) {
			isImovelUrbano = true;
		}

		return listaImovel;
	}

	/**
	 * Verifica a quantidade de imóvel na lista. Caso se tenha apenas 1 (UM)
	 * imóvel seta-se esse como imóvelSelecionado e bloqueia a opção do usuário
	 * selecionar o imóvel no dialog. Faz-se necessário verificar se o dialog é
	 * novo ou edição.
	 *
	 * @author eduardo.fernandes
	 * @return imovelUnico; imovelSelecionado
	 */
	public void verificaImovelNaLista() {
		if (!Util.isNullOuVazio(listaImovel)) {
			if (listaImovel.size() == 1) {
				imovelUnico = true;
			} else if (listaImovel.size() > 1) {
				imovelUnico = false;
			}
		} else {
			imovelUnico = false;
		}
	}



	public boolean isRequerentePF(){
		return (!Util.isNullOuVazio(ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().getPessoa().getPessoaFisica()));
	}

	public boolean isRequerentePJ(){
		return (!Util.isNull(ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().getPessoa().getPessoaJuridica()));
	}

	public void salvarRequerimentoAposEndereco(ActionEvent event) {
		
		try {
			if(!Util.isNullOuVazio(this.empreendimento)){
				novoRequerimentoServiceFacade.salvarRequerimentoAposEndereco(event, this, identificarPapelController);
				// action
				abaProcessoController.init();
				// remoteComand actionListener
				abaRenovacaoAlteracaoProrrogacaoController.init();
				// remoteComand action
				abaOutorgaController.init();
			} 
			else {
				JsfUtil.addWarnMessage("Selecione um Empreendimento já cadastrado!");
			}
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public boolean isDisableBotaoSalvar() {
		return getSalvoParcialmente() || modoEdicao || desabilitarTudo;
	}

	public boolean isRenderedFiltroTecnico() {
		return isAtend();
	}

	private void adicionarTramitacao(StatusRequerimentoEnum status) {
		TramitacaoRequerimento tramitacaoRequerimento = new TramitacaoRequerimento();
		tramitacaoRequerimento.setIdePessoa(pessoa);
		StatusRequerimento statusRequeri = new StatusRequerimento();
		statusRequeri.setIdeStatusRequerimento(status.getStatus());
		tramitacaoRequerimento.setIdeStatusRequerimento(statusRequeri);
		requerimento.setTramitacaoRequerimentoCollection(new ArrayList<TramitacaoRequerimento>());
		requerimento.getTramitacaoRequerimentoCollection().add(tramitacaoRequerimento);

	}

	public void finalizarRequerimentoRazaoSocial() {

		try {

			if(isRequerentePJ()){
				identificarPapelController.setPessoaJuridica(ContextoUtil.getContexto().getReqPapeisDTO().getRequerente().getPessoa().getPessoaJuridica());
			}

			if(isRequerentePF() || (isRequerentePJ() && identificarPapelController.validarPJ())){

				if (validarRazaoSocial()) {

					List<String> emails = emailService.listarEmailsRequerente(this.requerimento);

					if(Util.isNullOuVazio(emails)) {
						JsfUtil.addErrorMessage("É necessário informar pelo menos um endereço de email para que o email seja enviado.");
						return;
					}

					if (Util.isNullOuVazio(requerimento.getIdeOrgao())) {
						// ideOrga = 1 (INEMA)
						requerimento.setIdeOrgao(new Orgao(1));
					}

					RequerimentoPessoa reqPessoaRequerente = ContextoUtil.getContexto().getReqPapeisDTO().getRequerente();

					if (this.novoRequerimentoService.verificarExistenciaARLS(reqPessoaRequerente)) {
						JsfUtil.addErrorMessage("Já existe uma solicitação de alteração de razão social");
						return;
					}

					requerimento.setIdeOrgao(requerimentoUnicoService.recuperarOrgao(requerimento.getIdeOrgao()));

					if (!modoEdicao) {


						for(SolicitacaoAdministrativo sa : listaSolicitacaoAdministrativo) {
							solicitacaoAdministrativo = sa;
						}

						// ideTipoRequerimento = 1 (Regulação Ambiental do
						// Empreendimento)
						TipoRequerimento tipoRequerimento = new TipoRequerimento(1);
						requerimento.setIdeTipoRequerimento(tipoRequerimento);
						requerimento.setDtcCriacao(new Date());
						requerimento.setIndDeclaracao(true);
						requerimento.setIndExcluido(false);

						adicionarTramitacao(StatusRequerimentoEnum.AGUARDANDO_ENQUADRAMENTO);

						pessoasRequerimento();

						solicitacaoAdministrativo.setIdeRequerimento(requerimento);
						solicitacaoAdministrativo.setIdeTipoSolicitacao(new TipoSolicitacao(TipoSolicitacaoEnum.ALTERACAO_RAZAO_SOCIAL.getId()));
					}

					if (Util.isNullOuVazio(getRequerimento().getNumRequerimento())) {
						requerimentoUnicoService.geraNumeroRequerimento(requerimento);
						requerimento.setDtcFinalizacao(new Date());
						novoRequerimentoService.inserirRequerimento(requerimento);
					}

					solicitacaoAdministrativo.setNomRazaoSocialNova(novaRazaoSocial);
					solicitacaoAdministrativoService.salvarOuAtualizarSolicitacaoAdministrativa(solicitacaoAdministrativo);

					pergunta0_1.setDtcResposta(new Date());
					pergunta0_1.setIdeRequerimento(requerimento);
					perguntaRequerimentoService.salvarOuAtualizarPerguntaReq(pergunta0_1);

					this.efetuarEnquadramentoAutomatico(AtoAmbientalEnum.ARLS);

					this.carregaMsgAberturaRequerimento();
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao salvar o requerimento " + e.getMessage());
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void finalizarRequerimentoTransTitularidade() {
		try {
			if (abaProcessoController.validarAba()) {

				abaProcessoController.salvar();

				if (Util.isNullOuVazio(requerimento.getIdeTipoRequerimento())) {
					requerimento.setIdeTipoRequerimento(new TipoRequerimento(1));
				}
				if (Util.isNullOuVazio(requerimento.getIdeOrgao().getDscSiglaOrgao())) {
					requerimento.setIdeOrgao(requerimentoUnicoService.recuperarOrgao(requerimento.getIdeOrgao()));
				}
				if (isModoEdicao()) {
					empreendimentoRequerimento = empreendimentoService.buscarEmpreendimentoRequerimento(requerimento,
							empreendimento);
				} else {


					pessoasRequerimento();
					empreendimentoRequerimento.setIdeEmpreendimento(empreendimento);
					empreendimentoRequerimento.setIdeRequerimento(requerimento);
				}

				adicionarTramitacao(StatusRequerimentoEnum.AGUARDANDO_ENQUADRAMENTO);

				if (Util.isNullOuVazio(requerimento.getNumRequerimento())) {
					requerimentoUnicoService.geraNumeroRequerimento(requerimento);
					requerimento.setDtcFinalizacao(new Date());
					novoRequerimentoService.inserirRequerimento(requerimento);

				}

				if(this.atoAmbientalService.carregarById(AtoAmbientalEnum.TLA.getId()).getIndAutomatico()) {
					this.efetuarEnquadramentoAutomatico(AtoAmbientalEnum.TLA);
				}

				carregaMsgAberturaRequerimento();
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao finalizar requerimento.");
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
		}
	}

	private void efetuarEnquadramentoAutomatico(AtoAmbientalEnum atoAmbientalEnum) throws Exception {
		this.tramitacaoRequerimentoService.tramitarAutomaticamente(requerimento, StatusRequerimentoEnum.SENDO_ENQUADRADO);

		AtoAmbiental atoAmbiental = this.atoAmbientalService.carregarById(atoAmbientalEnum.getId());

		Enquadramento enquadramento = gerarEnquadramento(atoAmbiental);

		this.enquadramentoService.salvar(enquadramento);

		if(!Util.isNullOuVazio(enquadramento.getEnquadramentoAtoAmbientalCollection()) && enquadramento.getEnquadramentoAtoAmbientalCollection().size() == 1) {
			documentoObrigatorioReqService.atualizarEnquadramentoDosDocumentosByRequerimento(enquadramento.getIdeRequerimento(), enquadramento.getEnquadramentoAtoAmbientalCollection().iterator().next());
		}

		this.enquadramentoService.salvarComunicacao(enquadramento);
	}

	private Enquadramento gerarEnquadramento(AtoAmbiental atoAmbiental) throws Exception {
		Enquadramento enquadramento = new Enquadramento();
		enquadramento.setIdeRequerimento(this.requerimento);
		enquadramento.setIndEnquadramentoAprovado(true);
		enquadramento.setIndPassivelEiarima(false);
		enquadramento.setIdePessoa(this.pessoaService.carregarUsuarioSEIA());
		this.gerarEnquadramentoAtoAmbiental(atoAmbiental, enquadramento);
		this.gerarEnquadramentoDocumentoAto(atoAmbiental, enquadramento);

		return enquadramento;
	}

	private void gerarEnquadramentoDocumentoAto(AtoAmbiental atoAmbiental, Enquadramento enquadramento)
			throws Exception {
		enquadramento.setEnquadramentoDocumentoAtoCollection(new ArrayList<EnquadramentoDocumentoAto>());
		Collection<DocumentoAto> documentos = this.documentoAtoAmbientalService
				.listarDocumentoAtoByIdeAtoAmbiental(atoAmbiental.getIdeAtoAmbiental());
		for (DocumentoAto documentoAto : documentos) {
			documentoAto.setChecked(true);
			EnquadramentoDocumentoAto enquadramentoDocumentoAto = new EnquadramentoDocumentoAto();
			enquadramentoDocumentoAto.setEnquadramento(enquadramento);
			enquadramentoDocumentoAto.setDocumentoAto(documentoAto);
			enquadramento.getEnquadramentoDocumentoAtoCollection().add(enquadramentoDocumentoAto);
		}
	}

	private void gerarEnquadramentoAtoAmbiental(AtoAmbiental atoAmbiental, Enquadramento enquadramento) {
		enquadramento.setEnquadramentoAtoAmbientalCollection(new ArrayList<EnquadramentoAtoAmbiental>());

		EnquadramentoAtoAmbiental enquadramentoAtoAmbiental = new EnquadramentoAtoAmbiental(enquadramento,
				atoAmbiental, null);

		enquadramento.getEnquadramentoAtoAmbientalCollection().add(enquadramentoAtoAmbiental);
	}

	private void carregaMsgAberturaRequerimento() {
		try {

			String mensagem = new String("O requerimento foi aberto com sucesso. O número para acompanhamento é: "  + requerimento.getNumRequerimento()
					+ ". ------------------------------------ "
					+ "Você receberá por e-mail a relação de documentos para abertura do processo. Após o envio da documentação você receberá "
					+ "o boleto para pagamento da taxa ambiental.");

			ContextoUtil.getContexto().setObject(mensagem);
			FacesContext.getCurrentInstance().getExternalContext().redirect(prepararParaConsultar());
		} catch (IOException e) {
			JsfUtil.addErrorMessage("Erro ao redirecionar para página de consulta");
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
		}
	}

	public String prepararParaConsultar() {
		return "/paginas/novo-requerimento/consulta.xhtml";
	}

	public String redirecionarVisualizacao(RequerimentoDTO dto) {
		try {
			RequerimentoUnico requerimentoUnico = novoRequerimentoService.obterRequerimentoUnico(dto.getRequerimento());
			
			DeclaracaoInexigibilidade declaracaoInexigibilidade = requerimentoService.buscarRequerimentoDeclaracaoInexigibilidadeFinalizadaPor(dto.getRequerimento());
			
			if(!Util.isNull(declaracaoInexigibilidade)) {
				return "/paginas/manter-declaracao-inexigibilidade/declaracaoInexigibilidade.xhtml";
			}

			if (Util.isNullOuVazio(requerimentoUnico)) {
				return "/paginas/novo-requerimento/incluirNovoRequerimento.xhtml";
			} else {
				pessoa = pessoaService.carregarDadosRequerente(requerimentoUnico.getIdeRequerimentoUnico(), null);
				requerimentoUnicoDTO = new RequerimentoUnicoDTO(requerimentoUnico, pessoa, empreendimento, dto.getStatusRequerimento(), null);

				if (dto.isVisualizar()) {
					requerimentoUnicoDTO.setVisualizar(true);
				} else {
					requerimentoUnicoDTO.setVisualizar(false);
				}
				ContextoUtil.getContexto().setObject(requerimentoUnicoDTO);
				return "/paginas/requerimentos/incluirRequerimentoUnico.xhtml";
			}
		} catch (Exception e) {
			JsfUtil.addWarnMessage("Erro no redirecionamento de página!");
			return null;
		}
	}

	public String redirectToEnquadramento(RequerimentoDTO dto) {
		try {
			RequerimentoUnico requerimentoUnico = novoRequerimentoService.obterRequerimentoUnico(dto.getRequerimento());
			Requerimento requerimento = requerimentoService.carregar(dto.getRequerimento());
			if(requerimento.getNumRequerimento().contains("DTRP")){
				dto.setVisualizar(Boolean.FALSE);
				return "/paginas/manter-dtrp/declaracao-transporte-residuo-perigoso-incluir.xhtml";
			} else if (Util.isNullOuVazio(requerimentoUnico)) {
				return "/paginas/novo-requerimento/incluirNovoRequerimento.xhtml";
			} else {
				requerimentoUnicoDTO = new RequerimentoUnicoDTO(requerimentoUnico, pessoa, empreendimento, dto.getStatusRequerimento(), null);
				requerimentoUnicoDTO.setVisualizar(dto.isVisualizar());
				requerimentoUnicoDTO.setEfetuandoEnquadramento(dto.isModoEnquadramento());
				ContextoUtil.getContexto().setObject(requerimentoUnicoDTO);
				return "/paginas/requerimentos/incluirRequerimentoUnico.xhtml";
			}
		} catch (Exception e) {
			JsfUtil.addWarnMessage("Erro no redirecionamento de página!");
			return null;
		}
	}

	private boolean validarRazaoSocial() throws Exception {

		boolean valido = true;

		if(pergunta0_1 == null) {
			JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO'para a pergunta.");
			valido = false;

		} else if (pergunta0_1.getIndResposta() == null) {
			JsfUtil.addWarnMessage("Por favor, selecione 'SIM' ou 'NÃO'para a pergunta.");
			valido = false;

		} else if (pergunta0_1.getIndResposta()) {

			if (Util.isNullOuVazio(this.novaRazaoSocial)) {
				JsfUtil.addWarnMessage("Por favor, preencha o campo com a Nova razão social.");
				valido = false;
			} else {
				if (!validaTamanhoString(this.novaRazaoSocial, 200)) {
					JsfUtil.addWarnMessage("A nova razão social só aceita 200 caracteres");
					valido = false;
				}
			}
		}

		return valido;
	}

	private void pessoasRequerimento() {
		try {

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

			List<ProcuradorPessoaFisica> collProcuradorPessoaFisica = requerimentoUnicoService.recuperarProcuradorPessoaFisicaAssinaturaObrigatoriaPorRequerenteEmpreendimento(pessoa, empreendimento);
			for (ProcuradorPessoaFisica procuradorPessoaFisica : collProcuradorPessoaFisica) {
				RequerimentoPessoa requerimentoPessoa = new RequerimentoPessoa();
				requerimentoPessoa.setPessoa(procuradorPessoaFisica.getIdeProcurador().getPessoa());
				TipoPessoaRequerimento tipoPessoaRequerimento = new TipoPessoaRequerimento();
				tipoPessoaRequerimento.setIdeTipoPessoaRequerimento(TipoPessoaRequerimentoEnum.PROCURADOR.getId());
				requerimentoPessoa.setIdeTipoPessoaRequerimento(tipoPessoaRequerimento);
				pessoasRequerimentos.add(requerimentoPessoa);
			}

			List<ProcuradorRepresentante> collProcuradorRepresentante = requerimentoUnicoService.recuperarProcuradorRepresentanteAssinaturaObrigatoriaPorRequerenteEmpreendimento(pessoa, empreendimento);
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
				reqPess.setRequerimento(requerimento);
			}

			requerimento.setRequerimentoPessoaCollection(pessoasRequerimentos);
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
		}
	}

	public void carregarInformacoesEmpreendimento() throws Exception {
		this.mostraQuestionarioAposSalvar = false;

		try {
			if (!Util.isNull(this.empreendimento) 
					&& !Util.isNull(this.empreendimento.getIdeEmpreendimento())) {
				
				this.desabilitarTudo = Boolean.FALSE;
				empreendimento = empreendimentoService.carregarById(empreendimento.getIdeEmpreendimento());

				this.listaImovel = new ArrayList<Imovel>();
				this.listarImovel(empreendimento);

				requerimento.setIdeEnderecoContato(new Endereco());
				requerimento.getIdeEnderecoContato().setIdeLogradouro(new Logradouro());

				ContextoUtil.getContexto().setEmpreendimento(empreendimento);

				if((Util.isNullOuVazio(requerimento.getDesEmail()) || emailRequerimentoCarregadoPeloEmpreendimento) && !dadosContato) {
					if(!Util.isNullOuVazio(empreendimento) && !Util.isNullOuVazio(empreendimento.getDesEmail())) {
						requerimento.setDesEmail(empreendimento.getDesEmail());
						emailRequerimentoCarregadoPeloEmpreendimento = true;
					}
				}
				
			}
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
		}

	}

	public void controlarAbas(TabChangeEvent event) {
		if ("abaProcesso".equals(event.getTab().getId())) {
			activeTab = 0;
		} else if ("abaRenovacao".equals(event.getTab().getId())) {
			activeTab = 1;
		} else if ("abaLicenca".equals(event.getTab().getId())) {
			activeTab = 2;
		} else if ("abaOutorga".equals(event.getTab().getId())) {
			activeTab = 3;
		} else if ("abaFlorestal".equals(event.getTab().getId())) {
			activeTab = 4;
		} else if ("abaFauna".equals(event.getTab().getId())) {
			activeTab = 5;
		} else if ("abaFinalizarRequerimento".equals(event.getTab().getId())) {
			abaFinalizarRequerimentoController.init();
			activeTab = 6;
		}
	}
	
	public void avancarAbas() {
		try {
			existeSolicitacao = false;

			List<Integer> abas = Arrays.asList(new Integer[] {1,3,4,5});
			if (!Util.isNullOuVazio(getRequerimentoDTO()) && getRequerimentoDTO().isVisualizar() && !modoEdicao) {
				do {
					activeTab++;
				} while (disabledAbasRequerimento && abas.contains(activeTab));
			}
			else if (validarAbas()) {
				do {
					activeTab++;
				} while (disabledAbasRequerimento && abas.contains(activeTab));
				
				if(activeTab == 6) {
					abaFinalizarRequerimentoController.init();
				}
			}
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
		}
	}

	public void voltarAbas() {
		try {
			existeSolicitacao = false;
			if (!Util.isNullOuVazio(getRequerimentoDTO()) && getRequerimentoDTO().isVisualizar() && !modoEdicao) {
				do {
					activeTab--;
					//desabilitarAbaReenquadramento(activeTab);
				} while (disabledAbasRequerimento && Arrays.asList(new Integer[] {1,3,4,5}).contains(activeTab));
			} else if (validarAbas()) {
				do {
					activeTab--;
					//desabilitarAbaReenquadramento(activeTab);
				} while (disabledAbasRequerimento && Arrays.asList(new Integer[] {1,3,4,5}).contains(activeTab));
			}
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
		}

	}

	/**
	 * @return
	 */
	public EmpreendimentoRequerimento carregarEmpreendRequerimento() {
		this.empreendimentoRequerimento.setIdeRequerimento(getRequerimento());
		this.empreendimentoRequerimento.setIdeEmpreendimento(this.empreendimento);
		return this.empreendimentoRequerimento;
	}

	// Dialog Incluir Vértice. Controle PopUp Localizacao Geografica - Begin
	public void calculaFracaoGrauLatitudeLoc(ActionEvent event) {
		try {
			if (grausLatitudeLoc != null && minutosLatitudeLoc != null && segundosLatitudeLoc != null) {
				this.fracaoGrauLatitudeLoc = PostgisUtil.calculaFracaoGrauLatitude(grausLatitudeLoc,
						minutosLatitudeLoc, segundosLatitudeLoc).toString();
			}
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
		}
	}

	public void calculaFracaoGrauLongitudeLoc(ActionEvent event) {
		try {
			if (grausLongitudeLoc != null && minutosLongitudeLoc != null && segundosLongitudeLoc != null) {
				this.fracaoGrauLongitudeLoc = PostgisUtil.calculaFracaoGrauLongitude(grausLongitudeLoc,
						minutosLongitudeLoc, segundosLongitudeLoc).toString();
			}
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
		}
	}

	public void incluirVertice() {

		if (Util.isNullOuVazio(listaLocalizacaoGeografica)) {
			listaLocalizacaoGeografica = new ArrayList<LocalizacaoGeografica>();
			calculaFracaoGrauLatitudeLoc(null);
			calculaFracaoGrauLongitudeLoc(null);
			if (!Util.isNullOuVazio(this.fracaoGrauLatitudeLoc) && !Util.isNullOuVazio(this.fracaoGrauLongitudeLoc)) {
				try {
					if (!localizacaoGeograficaService.validaVerticeMunicipio(this.fracaoGrauLatitudeLoc,
							this.fracaoGrauLongitudeLoc, empreendimento, null)) {

						JsfUtil.addWarnMessage("Coordenada informada está fora dos limites da Localidade do empreendimento. ");
					}

					Point ponto = PostgisUtil.criarPonto(
							Double.parseDouble("-" + this.fracaoGrauLatitudeLoc.replace(',', '.')),
							Double.parseDouble("-" + this.fracaoGrauLongitudeLoc.replace(',', '.')));
					if (this.verticeLoc == null) {
						this.verticeLoc = new DadoGeografico();
					}
					this.verticeLoc.setCoordGeoNumerica(ponto.toString());
					this.verticeLoc.setIdeLocalizacaoGeografica(getLocalizacaoGeograficaSelecionada());
					if (Util.isNullOuVazio((getLocalizacaoGeograficaSelecionada().getDadoGeograficoCollection()))) {
						getLocalizacaoGeograficaSelecionada().setDadoGeograficoCollection(
								new ArrayList<DadoGeografico>());
					}
					getLocalizacaoGeograficaSelecionada().getDadoGeograficoCollection().add(verticeLoc);
					if (tipoProrrogacaoSelecionado != null) {// Se quem chamou
						// está função
						// foi a dialog
						// prorrogar
						// prazo
						// validade
						if (listLocalizacaoGeo == null) {
							listLocalizacaoGeo = new LocalizacaoGeografica[15];
						}
						listLocalizacaoGeo[tipoProrrogacaoSelecionado.getIdeTipoProrrogacaoPrazoValidade()] = getLocalizacaoGeograficaSelecionada();
					} else {
						this.listaLocalizacaoGeografica.add(getLocalizacaoGeograficaSelecionada());
					}

					setLocalizacaoGeograficaSelecionada(null);
					limparFormLocGeografica();
					JsfUtil.addSuccessMessage("Pontos inseridos com sucesso!");

				} catch (Exception exception) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
					JsfUtil.addErrorMessage(exception.getMessage());
				}
			} else {
				RequestContext.getCurrentInstance().addCallbackParam("validationFailed", true);
			}
		} else {
			for (LocalizacaoGeografica localizacaoGeografica : listaLocalizacaoGeografica) {

				for (DadoGeografico dadoGeografico : localizacaoGeografica.getDadoGeograficoCollection()) {

					try {
						calculaFracaoGrauLatitudeLoc(null);
						calculaFracaoGrauLongitudeLoc(null);
						Point ponto = PostgisUtil.criarPonto(
								Double.parseDouble("-" + this.fracaoGrauLatitudeLoc.replace(',', '.')),
								Double.parseDouble("-" + this.fracaoGrauLongitudeLoc.replace(',', '.')));

						dadoGeografico.setCoordGeoNumerica(ponto.toString());
						dadoGeografico.setIdeLocalizacaoGeografica(localizacaoGeografica);
					} catch (Exception exception) {
						Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
						JsfUtil.addErrorMessage(exception.getMessage());
					}
				}

				limparFormLocGeografica();
				JsfUtil.addSuccessMessage("Pontos inseridos com sucesso!");
			}

			JsfUtil.addSuccessMessage("Pontos inseridos com sucesso!");
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

	public void limparFormLocGeografica() {

		this.grausLatitudeLoc = null;
		this.minutosLatitudeLoc = null;
		this.segundosLatitudeLoc = null;
		this.grausLongitudeLoc = null;
		this.minutosLongitudeLoc = null;
		this.segundosLongitudeLoc = null;
		this.fracaoGrauLatitudeLoc = null;
		this.fracaoGrauLongitudeLoc = null;
		this.verticeLoc = new DadoGeografico();

	}
	
		/**
		 * @param date
		 * @return true se a data for inferior a atual
		 * @Comentarios Verifica se a data passada por parametro é maior que a
		 *              atual.
		 *
		 */
	

	/**
	 * @param date1
	 *            Date
	 * @param date2
	 *            Date
	 * @return true se date1 < date2
	 * @Comentarios Verifica se a segunda data informada é superior a primeira
	 */
	public boolean validarDuasDatas(Date date1, Date date2) {
		if (date1.compareTo(date2) < 0) {
			return true;
		}
		return false;
	}

	public boolean after(Date date1, Date date2) {
		if (date1.compareTo(date2) > 0) {
			return true;
		}
		return false;
	}

	/**
	 *
	 * @return Data de Hoje (atual)
	 */
	public String getDataHoje() {
		Date data = new Date(System.currentTimeMillis());
		SimpleDateFormat formatarDate = new SimpleDateFormat("dd/MM/yyyy");
		dataHoje = formatarDate.format(data);
		return dataHoje;
	}

	/**
	 *
	 * @return Data de validade maxima permitida na dialog prorrogação prazo de
	 *         validade
	 */
	public String getDataMaxima() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, +60);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		dataMinima = sdf.format(calendar.getTime());
		return dataMinima;
	}

	/**
	 *
	 * @param date
	 * @return boolean
	 * @Comentario Checa se a data informata é superior a 60 dias a partir de
	 *             hoje
	 */
	public boolean dataValidadeMaiorQueMaximoPermitido(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 60);
		if ((date.compareTo(calendar.getTime()) <= 0)) {
			return true;
		} else {
			JsfUtil.addWarnMessage("Data inválida!");
			return false;
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

	/**
	 * @return boolean
	 * @author eduardo.fernandes
	 * @Commentarios Retorna false se o tamanho da variavel (String) exceder o
	 *               tamanho especificado
	 */
	public boolean validaTamanhoString(String string, int tamanho) {
		boolean valido = true;
		if (string.trim().length() > tamanho) {
			valido = false;
		}
		return valido;
	}

	/**
	 *
	 * @param numProcesso
	 * @param pergunta
	 * @return boolean
	 * @Comentarios O parametro opcional 'pergunta' especifica qual a pergunta
	 *              está chamando o método a fim de atribuir uma mensagem mais
	 *              intuitiva em caso do processo ser inválido
	 * @Observação Após a chamada do método é retornado falso pois o processo já
	 *             foi salvo, caso contrário o método que o chama poderá tentar
	 *             salvar um novo processo.
	 */
	public SistemaEnum validarProcesso(String numProcesso, String... pergunta) {
		Processo processo;
		if (pergunta.length == 0) {
			pergunta = new String[1];
			pergunta[0] = "";
		} else {
			pergunta[0] += " - ";
		}
		try {
			processo = processoService.buscarProcessoPorCriteria(numProcesso);
			if (Util.isNullOuVazio(processo)) {
				processoExterno = processoExternoService.buscarProcessoExternoByNumeroProcesso(numProcesso);
				if (Util.isNullOuVazio(processoExterno)) {
					JsfUtil.addWarnMessage("Processo não encontrado.");
					return SistemaEnum.DESCONHECIDO;
				} else {
					if (processoExterno.getSistema().equalsIgnoreCase(SistemaEnum.CERBERUS.getNomSistema())) {
						return SistemaEnum.CERBERUS;
					} else {
						return SistemaEnum.PROHIDROS;
					}
				}
			} else {
				return SistemaEnum.SEIA;
			}

		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro em algum processo:" + e.getMessage());
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			return SistemaEnum.DESCONHECIDO;
		}
	}

	/**
	 *
	 * @param str
	 * @return String sem espaços
	 * @Comentarios Método utilizado para remover espaços de certas variaveis
	 *              cujos mesmo estarem sendo ignorados pelo .trim()
	 *              convencional, devido não serem espaços do padrão ancii32 e
	 *              sim 160.
	 */
	public String trim(String str) {
		String string = str.replace(String.valueOf((char) 160), " ").trim();
		return string;
	}

	/**
	 *
	 * @param listProcesso
	 * @param object
	 * @param isEdicaoNumProcesso
	 * @return boolean
	 * @Comentarios Verifica se o processo digitado já não foi inserido na lista
	 *              passada.
	 */
	public boolean verificarProcessoDuplicado(List<?> listProcesso, Object object, String... isEdicaoNumProcesso) {
		if ((!Util.isNullOuVazio(listProcesso))) {
			for (Object p : listProcesso) {
				if (p instanceof ProcessoTramite) {
					ProcessoTramite processoTramite = (ProcessoTramite) p;
					ProcessoTramite processoSelecionado = (ProcessoTramite) object;

					if (processoSelecionado.getNumProcessoTramite().equals(processoTramite.getNumProcessoTramite())
							&& !processoTramite.equals(processoSelecionado)) {
						JsfUtil.addWarnMessage("Processo já está cadastrado");
						return false;
					}
				}
				if (p instanceof Licenca) {
					Licenca processoLicenca = (Licenca) p;
					if (!modoEdicaoDialog) {
						if (processoLicenca.getNumProcessoLicenca().trim().equals(((String) object).trim())) {
							JsfUtil.addWarnMessage("Processo já está cadastrado");
							return false;
						}
					} else if (processoLicenca.getNumProcessoLicenca().trim().equals(((String) object).trim())
							&& !processoLicenca.getNumProcessoLicenca().trim().equals(isEdicaoNumProcesso[0])) {
						JsfUtil.addWarnMessage("Processo já está cadastrado");
						return false;
					}
				}
				if (p instanceof Outorga) {
					Outorga processoOutorga = (Outorga) p;
					if (!modoEdicaoDialog) {
						if (processoOutorga.getNumProcessoOutorga().trim().equals(((String) object).trim())) {
							JsfUtil.addWarnMessage("Processo já está cadastrado");
							return false;
						}
					} else if (processoOutorga.getNumProcessoOutorga().trim().equals(((String) object).trim())
							&& !processoOutorga.getNumProcessoOutorga().trim().equals(isEdicaoNumProcesso[0])) {
						JsfUtil.addWarnMessage("Processo já está cadastrado");
						return false;
					}
				}
				if (p instanceof SolicitacaoAdministrativo) {
					SolicitacaoAdministrativo processoSolicitacao = (SolicitacaoAdministrativo) p;
					if (!modoEdicaoDialog) {
						if (processoSolicitacao.getNumProcesso().trim().equals(((String) object).trim())) {
							JsfUtil.addWarnMessage("Processo já está cadastrado");
							return false;
						}
					} else if (processoSolicitacao.getNumProcesso().trim().equals(((String) object).trim())
							&& !processoSolicitacao.getNumProcesso().trim().equals(isEdicaoNumProcesso[0])) {
						JsfUtil.addWarnMessage("Processo já está cadastrado");
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * .
	 *
	 * @param str
	 * @param arrayStr
	 * @return str
	 * @Comentarios faz um replace em sua String trocando todos caracteres
	 *              passados ''(vazio) ou atributo passado por parametro
	 *              (Opcional)
	 */
	public String replaceString(String str, String[] arrayStr, String... subst) {
		if (Util.isNull(subst) || subst.length < 1) {
			subst = new String[1];
			subst[0] = "";
		}
		for (String var : arrayStr) {
			str = str.replace(var, subst[0]);
		}
		return str;
	}

	/**
	 *
	 * @author eduardo.fernandes
	 * @throws Exception
	 */
	public boolean validarAbas() throws Exception {
		existeSolicitacao = false;
		if (activeTab == 0) {
			if (abaProcessoController.validarAba()) {
				abaProcessoController.salvar();
				existeSolicitacao = true;
			}
		} else if (activeTab == 1) {
			if (abaRenovacaoAlteracaoProrrogacaoController.validarAba()) {
				abaRenovacaoAlteracaoProrrogacaoController.salvar();
				existeSolicitacao = true;
			}
		} else if (activeTab == 2) {
			if (abaLicencaAutorizacaoController.validarAba()) {
				abaLicencaAutorizacaoController.salvar();
				existeSolicitacao = true;
			}
		} else if (activeTab == 3) {
			if (abaOutorgaController.validarAba()) {
				abaOutorgaController.salvar();
				existeSolicitacao = true;
			}
		} else if (activeTab == 4) {
			if (abaFlorestalController.validarAba()) {
				abaFlorestalController.salvar();
				existeSolicitacao = true;
			}
		} else if (activeTab == 5) {
			if (abaFaunaController.validarAba()) {
				abaFaunaController.salvar();
				existeSolicitacao = true;
			}
		} else if (activeTab == 6) {
			int i;
			int temp = 6;
			boolean valido = true;
			for (i = 0; i <= (temp - 1); i++) {
				activeTab = i;
				if(!disabledAbasRequerimento 
				|| (disabledAbasRequerimento && !Arrays.asList(new Integer[] {1,3,4,5}).contains(activeTab))) {
					valido = validarAbas();
					if (!valido) {
						temp = activeTab;
						break;
					}
				}				
			}
			activeTab = temp;
			existeSolicitacao = valido;
		}
		return existeSolicitacao;
	}

	/**
	 *
	 * @author micael.coutinho
	 * @throws Exception
	 */
	public boolean existeSolicitacao() throws Exception {
		existeSolicitacao = false;
		if (abaProcessoController.getPerguntaNR_A1_P2().getIndResposta()) {
			existeSolicitacao = true;
		}else if ((!Util.isNullOuVazio(abaRenovacaoAlteracaoProrrogacaoController.getPerguntaNR_A2_P1().getIndResposta()) && abaRenovacaoAlteracaoProrrogacaoController.getPerguntaNR_A2_P1().getIndResposta()) ||
				(!Util.isNullOuVazio(abaRenovacaoAlteracaoProrrogacaoController.getPerguntaNR_A2_P2().getIndResposta()) && abaRenovacaoAlteracaoProrrogacaoController.getPerguntaNR_A2_P2().getIndResposta()) ||
				(!Util.isNullOuVazio(abaRenovacaoAlteracaoProrrogacaoController.getPerguntaNR_A2_P3().getIndResposta()) && abaRenovacaoAlteracaoProrrogacaoController.getPerguntaNR_A2_P3().getIndResposta()) ||
				(!Util.isNullOuVazio(abaRenovacaoAlteracaoProrrogacaoController.getPerguntaNR_A2_P4().getIndResposta()) && abaRenovacaoAlteracaoProrrogacaoController.getPerguntaNR_A2_P4().getIndResposta())) {
			existeSolicitacao = true;
		}else if (!Util.isNull(abaLicencaAutorizacaoController.getPerguntaNR_A3_P1().getIndResposta()) && abaLicencaAutorizacaoController.getPerguntaNR_A3_P1().getIndResposta()) {
			existeSolicitacao = true;
		}else if (!Util.isNull(abaOutorgaController.getPerguntaNR_A4_P1().getIndResposta()) && abaOutorgaController.getPerguntaNR_A4_P1().getIndResposta()) {
			existeSolicitacao = true;
		}else if (!Util.isNull(abaFlorestalController.getPerguntaNR_A5_P1().getIndResposta()) && abaFlorestalController.getPerguntaNR_A5_P1().getIndResposta()) {
			existeSolicitacao = true;
		}else if (!Util.isNull(abaFaunaController.getPerguntaNR_A6_P1().getIndResposta()) && abaFaunaController.getPerguntaNR_A6_P1().getIndResposta()) {
			existeSolicitacao = true;
		}else if (!Util.isNull(abaLicencaAutorizacaoController.getPerguntaNR_A3_P13().getIndResposta()) && abaLicencaAutorizacaoController.getPerguntaNR_A3_P13().getIndResposta()) {
			existeSolicitacao = true;
		}
		
		return existeSolicitacao;
	}

	/**
	 * Método que lista todas as {@link Pergunta} do {@link Requerimento} que foram respondidas como <i>Sim</i> e se não há mais de uma aba respondida positivamente. 
	 * Portanto entende-se que é um requerimento para um único {@link AtoAmbiental}. 
	 * 
	 * @author eduardo.fernandes 
	 * @since 24/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
	 * @param pergunta [ Pergunta responsável pela Aba ]
	 * @return
	 */
	protected boolean isExisteUnicaSolicitacaoParaRequerimento(PerguntaEnum pergunta){
		try {
			boolean resposta = false;
			boolean isEtapaDoisNenhumaAlternativa = false;
			for(PerguntaRequerimento pr : perguntaRequerimentoService.listarPerguntasRespondidasByIdeRequerimento(requerimento.getIdeRequerimento())){
				if(pr.getIdePergunta().getCodPergunta().equals(pergunta.getCod())){
					resposta = pr.getIndResposta();
				} 
				else if(pr.getIdePergunta().getCodPergunta().equals(PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA2_5.getCod())){
					isEtapaDoisNenhumaAlternativa = pr.getIndResposta();
				}
				else if(!pr.getIdePergunta().getCodPergunta().contains(pergunta.getCod().substring(3, 5))){
					return false;
				}
			}
			return resposta && isEtapaDoisNenhumaAlternativa;
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Ocorreu um erro ao carregar a lista de Perguntas respondidas.");
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public Boolean getSalvoParcialmente() {
		return !Util.isNull(requerimento) && !Util.isNull(requerimento.getIdeRequerimento()) && Util.isNull(requerimento.getNumRequerimento());
	}

	public void consultar() {
		
		SessaoUtil.removerObjetoSessao("isEnquadramento");

		this.dataTableRequerimentos.setFirst(0);
		this.dataTableRequerimentos.setPage(1);
		requerimentoBusca = new Requerimento();
		try{
			if (validar()) {
				carregarParametros();
				carregarPaginacaoRequerimentos();
				consultaRealizada = true;
			}
		}
		catch(Exception e){
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private Map<String, Object> carregarParametros() throws Exception {

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("requerimento", requerimentoBusca);
		
		params.put("requerente", requerente);
		params.put("empreendimento", empreendimentoBusca);
		params.put("PORTE", idePorte);

		if (!Util.isNullOuVazio(numeroRequerimento)) {
			requerimentoBusca.setNumRequerimento(numeroRequerimento.trim());
		} else {
			requerimentoBusca.setNumRequerimento(null);
		}
		
		params.put("numeroRequerimento", numeroRequerimento);
		params.put("statusRequerimento", statusRequerimento);
		params.put("periodoInicio", periodoInicio);
		params.put("periodoFim", periodoFim);
		
		params.put("TIPOATO", tipoAto);
		params.put("ATO", atoAmbiental);
		params.put("tipologia", tipologia);
		params.put("finalidade", finalidadeUsoAgua);
		
		params.put("pagamentoReposicaoFlorestal", pagamentoReposicaoFlorestal);
		params.put("divisao", tipologiaDivisao);
		
		params.put("unidadeRegional", urSelecionada);
		params.put("municipio", municipioSelecionado);

		params.put("tecnico", tecnico);

		params.put("usuarioLogado", ContextoUtil.getContexto().getUsuarioLogado());
		params.put("isAtendente", ContextoUtil.getContexto().getUsuarioLogado().isAtende());
		params.put("isUsuarioExterno",ContextoUtil.getContexto().isUsuarioExterno());
		params.put("ideUsuarioLogado", ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());

		if (ContextoUtil.getContexto().isUsuarioExterno()) {
			List<Integer> listaPessoas = permissaoPerfilService.listarIdesPessoas();
			params.put("listaPessoas", listaPessoas);
		}

		if(!Util.isNull(tipologiaDivisao)) {
			if(!Util.isNull(tipologiaAtividade)) {
				params.put("tipologiaAtividade", tipologiaAtividade);
			}
			else{
				List<Integer> listaIdeTipologiaAtividade = new ArrayList<Integer>();
				for(Tipologia t : listaTipologiaAtividade) {
					listaIdeTipologiaAtividade.add(t.getIdeTipologia());
				}
				params.put("listaIdeTipologiaAtividade", listaIdeTipologiaAtividade);

			}
		}

		if (!Util.isNullOuVazio(tipoAto) && Util.isNullOuVazio(atoAmbiental)) {
			params.put("LISTAATOS", this.listaAto);
		}

		if (urSelecionada != null) {
			municipios = (ArrayList<Municipio>) municipioService.filtrarMunicipiosUR(urSelecionada);
			params.put("municipios", municipios);
		} else {
			municipios = null;
		}

		if(ContextoUtil.getContexto().isUsuarioExterno()){
			List<Integer> requerimentoSemPermissaoByRepLegalInativado = requerimentoService.getRequerimentoNaoVisiveisByRepresentanteLegalInativado(new PessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
			if(!Util.isNullOuVazio(requerimentoSemPermissaoByRepLegalInativado)){
				params.put("listReqSemPermissaoByRepLegal", requerimentoSemPermissaoByRepLegalInativado);
			}
		}

		return params;
	}


	private void carregarPaginacaoRequerimentos() {

		listaRequerimentosDTOModel = new LazyDataModel<RequerimentoDTO>() {
			private static final long serialVersionUID = 1L;

			@Override
			public List<RequerimentoDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {

				List<RequerimentoDTO> lista = null;

				try {
					if (Util.isNullOuVazio(empreendimento.getIdeEmpreendimento()) || empreendimento.getIdeEmpreendimento().equals(0)) {
						empreendimentoBusca = null;
					} else {
						empreendimentoBusca = empreendimento;
					}

					Map<String, Object> params = carregarParametros();
					params.put("first", first);
					params.put("pageSize", pageSize);
					params.put("isPagination", true);

					lista = novoRequerimentoService.consultaRequerimento(params);

				}
				catch (Exception e) {
					Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
					JsfUtil.addErrorMessage("Não foi possível realizar a sua solicitação.");
					throw Util.capturarException(e, Util.SEIA_EXCEPTION);
				}
				return lista;
			}
		};
		listaRequerimentosDTOModel.setRowCount(count());
		addAtributoSessao("REQUERIMENTOS", listaRequerimentosDTOModel);
	}

	private Integer count() {
		try{
			Map<String, Object> params = carregarParametros();
			params.put("isCount", true);
			Integer countRequerimento = novoRequerimentoService.countRequerimento(params);
			return countRequerimento;
		}
		catch(Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private boolean validar() {


		boolean requerenteValido = !Util.isNull(requerente) && !Util.isNull(requerente.getIdePessoa()) && requerente.getIdePessoa() > 0;

		boolean statusValido = !Util.isNull(statusRequerimento) && !Util.isNull(statusRequerimento.getIdeStatusRequerimento()) && statusRequerimento.getIdeStatusRequerimento() > 0;

		boolean municipioValido = !Util.isNull(municipioSelecionado) && !Util.isNull(municipioSelecionado.getIdeMunicipio()) && municipioSelecionado.getIdeMunicipio() > 0;

		if (Util.isNullOuVazio(numeroRequerimento) && !requerenteValido && !statusValido && !municipioValido) {
			JsfUtil.addErrorMessage("Selecione ao menos um dos filtros: Requerente, Nº do Requerimento, Status, Localidade.");
			return false;
		}

		if (!Util.validaPeriodo(periodoInicio, periodoFim)) {
			JsfUtil.addErrorMessage(BUNDLE.getString("geral_msg_periodo_invalido"));
			return false;
		}

		return true;
	}
	
	/**
	 * Método que preenche uma lista de {@link Tipologia} de acordo com o {@link AtoAmbiental} selecionado e limpa os combos dependentes daquele antiga tipologia, quando houver.
	 * @author eduardo.fernandes
	 * @since 20/09/2012
	 * @see <a href="http://10.105.12.26/redmine/issues/7865">#7865</a>
	 */
	public void changeAtoAmbiental(ValueChangeEvent changeEvent) {
		try {
			AtoAmbiental atoAmbiental = (AtoAmbiental) changeEvent.getNewValue();
			if(!Util.isNull(atoAmbiental)){
				listaTipologia = tipologiaService.listarByAto(atoAmbiental);

				if (AtoAmbientalEnum.CRF.getId().equals(
						atoAmbiental.getIdeAtoAmbiental())) {
					pagamentoReposicaoFlorestalList = pagagementoReposicaoFlorestalService
							.listarPagamentoReposicaoFlorestalPai();
				}else {
					limparComboFormaCumprimento();	
				}
			} 
			else {
				limparComboTipologia();
			}
			limparComboFinalidade();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Tipologias.");
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * Método que preenche uma lista de Finalidades de acordo com a {@link Tipologia} selecionada.
	 * @author eduardo.fernandes
	 * @since 20/09/2012
	 * @see <a href="http://10.105.12.26/redmine/issues/7865">#7865</a>
	 */
	public void changeTipologia(ValueChangeEvent changeEvent) {
		try {
			Tipologia tipologia = (Tipologia) changeEvent.getNewValue();
			if(!Util.isNull(tipologia)){
				listaFinalidadeUsoAgua = novoRequerimentoServiceFacade.listarTipoFinalidadeUsoAgua(tipologia, atoAmbiental);
			} else {
				limparComboFinalidade();
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Finalidades.");
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * Método para limpar o combo de seleção de {@link Tipologia}.
	 * @author eduardo.fernandes
	 * @since 20/09/2012
	 * @see <a href="http://10.105.12.26/redmine/issues/7865">#7865</a>
	 */
	private void limparComboTipologia(){
		if(!Util.isNull(listaTipologia)){
			listaTipologia.clear();
		}
		tipologia = null;
	}
	
	/**
	 * Método para limpar o combo de seleção de {@link Forma de cumprimento}.
	 * @author israel.borges
	 * @since 05/08/2019	 
	 */
	private void limparComboFormaCumprimento(){
		if(!Util.isNull(pagamentoReposicaoFlorestalList)){
			pagamentoReposicaoFlorestalList.clear();
			pagamentoReposicaoFlorestal = null;
			Html.atualizar("formConsultarRequerimento:comboFormaCumprimento");
		}	
		
	}
	
	/**
	 * Método para limpar o combo de seleção de Finalidade [{@link TipoFinalidadeUsoAgua}].
	 * @author eduardo.fernandes
	 * @since 20/09/2012
	 * @see <a href="http://10.105.12.26/redmine/issues/7865">#7865</a>
	 */
	private void limparComboFinalidade(){
		if(!Util.isNull(listaFinalidadeUsoAgua)){
			listaFinalidadeUsoAgua.clear();
		}
		finalidadeUsoAgua = null;
	}

	public boolean isComboTipologiaBlocked(){
		return Util.isNull(atoAmbiental) || Util.isNullOuVazio(listaTipologia);
	}
	
	public boolean isComboFinalidadeBlocked(){
		return Util.isNull(tipologia) || Util.isNullOuVazio(listaFinalidadeUsoAgua);
	}
	
	public boolean isDisabledComboCumprimento(){
		return Util.isNullOuVazio(pagamentoReposicaoFlorestalList);
	}
	
	public void setPontosLocGeograficaNaGrid(OutorgaLocalizacaoGeografica outorgaLocGeoTemp) {
		pontosLocGeoOutorga = new HashMap<String, String>();
		pontosLocGeoOutorga.put("pontos", outorgaLocGeoTemp.getIdeLocalizacaoGeografica().getDadoGeograficoCollection()
				.iterator().next().getCoordGeoNumerica());
		outorgaLocGeoTemp.setPontos(pontosLocGeoOutorga);
	}

	/**
	 *
	 * @param outorgaLocGeo
	 * @param listaImovel
	 * @Observação Uma lista de imovel fez-se necessária devido a
	 *             'dialogAlteracao(...)Outorga' ter varias localizações
	 *             geograficas de modo que está não poderia ser resgatada por
	 *             'listaImovelSelecionado' senão ficaria UMA lista de imoveis
	 *             para todas as localizações inseridas.
	 */
	public void salvarImovel(OutorgaLocalizacaoGeografica outorgaLocGeo, List<Imovel> listaImovel) {
		try {
			outorgaLocalizacaoGeograficaImovel = new OutorgaLocalizacaoGeograficaImovel();
			outorgaLocalizacaoGeograficaImovelPK = new OutorgaLocalizacaoGeograficaImovelPK();
			if (listaImovel != null) {
				listaImovelSelecionado = listaImovel;
			}
			for (Imovel imovel : listaImovelSelecionado) {
				outorgaLocalizacaoGeograficaImovelPK.setIdeImovel(imovel.getIdeImovel());
				outorgaLocalizacaoGeograficaImovelPK.setIdeOutorgaLocalizacaoGeografica(outorgaLocGeo
						.getIdeOutorgaLocalizacaoGeografica());
				outorgaLocalizacaoGeograficaImovel.setIdeImovel(imovel);
				outorgaLocalizacaoGeograficaImovel.setIdeOutorgaLocalizacaoGeografica(outorgaLocGeo);
				outorgaLocalizacaoGeograficaImovel
				.setOutorgaLocalizacaoGeograficaImovelPK(outorgaLocalizacaoGeograficaImovelPK);
				outorgaLocalizacaoGeograficaService
				.salvarAtualizarOutorgaLocalizacaoGeograficaImovel(outorgaLocalizacaoGeograficaImovel);
			}
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	public void verificarImoveisEmpreendimento(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) {
		this.prepararEditarCarregarImovel(outorgaLocalizacaoGeografica);
	}

	/**
	 *
	 * @param olg
	 * @Comentario Carrega a lista de imoveis selecionados da outorga
	 *             Localização Geografica passada. Ex.: Captação Subterranea,
	 *             Superficial, Lançamento de Efluentes, Intervenção em corpo
	 *             Hidrico.
	 */
	public void prepararEditarCarregarImovel(OutorgaLocalizacaoGeografica olg) {
		List<Imovel> listaImovel = new ArrayList<Imovel>();
		try {
			if (Util.isNull(olg.getListaImovel())) {
				for (OutorgaLocalizacaoGeograficaImovel olgi : outorgaLocalizacaoGeograficaService
						.buscarOutorgaLocalizacaoGeograficaImovel(olg)) {
					listaImovel.add(olgi.getIdeImovel());
				}
				this.listaImovelSelecionado = listaImovel;
			} else {
				listaImovelSelecionado = olg.getListaImovel();
			}

		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
		}
	}

	// CONSULTA PARA NOVO REQUERIMENTO
	/**
	 * [IMPORTADO] metodo criado apenas para fazer o poll que carrega as mensagens do growl funcionar
	 * @see RequerimentoUnicoController
	 */
	public void pollAjax() {
		if (ContextoUtil.getContexto() != null && ContextoUtil.getContexto().getObject() != null
				&& ContextoUtil.getContexto().getObject() instanceof String) {
			String msg = (String) ContextoUtil.getContexto().getObject();
			JsfUtil.addSuccessMessage(msg);
			ContextoUtil.getContexto().setObject(null);
			setRendererPoll(false);
		}
		return;
	}

	/**
	 * [IMPORTADO]
	 *
	 * @see RequerimentoUnicoController
	 * @param event
	 */
	public void changeTipoAto(ValueChangeEvent event) {
		try {
			TipoAto tipoAtoSelecionado = (TipoAto) event.getNewValue();
			if (!Util.isNull(tipoAtoSelecionado) && tipoAtoSelecionado.getIdeTipoAto() != 0 && tipoAtoSelecionado.getIdeTipoAto() != -1) {
				listaAto = (List<AtoAmbiental>) atoAmbientalService.listarAtoAmbientalByTipoForSearch(tipoAtoSelecionado.getIdeTipoAto());
			} else {
				listaAto = new ArrayList<AtoAmbiental>();
			} 
			limparComboFormaCumprimento();
				
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
		}
	}

	/**
	 * [IMPORTADO]
	 *
	 * @see RequerimentoUnicoController
	 */
	private void carregarTipoAto() {
		try {
			listaTipoAto = (List<TipoAto>) tipoAtoService.listarTiposAtoConsultaveis();
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
		}
	}

	/**
	 * [IMPORTADO]
	 *
	 * @see RequerimentoUnicoController
	 * @return itens
	 */
	public Collection<SelectItem> getStatus() {
		if (collectionStatusRequerimento == null) {
			consultarStatusRequerimentos();
		}

		List<SelectItem> itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem("", BUNDLE.getString("geral_lbl_selecione")));
		if (ContextoUtil.getContexto().getUsuarioLogado().getIdePerfil().getIdePerfil() == PerfilEnum.FINANCEIRO.getId()) {
			for (StatusRequerimento status : collectionStatusRequerimento) {
				if (status.getIdeStatusRequerimento() == StatusRequerimentoEnum.COMPROVANTE_ENVIADO.getStatus()
						|| status.getIdeStatusRequerimento() == StatusRequerimentoEnum.VALIDADO.getStatus()
						|| status.getIdeStatusRequerimento() == StatusRequerimentoEnum.PAGAMENTO_LIBERADO.getStatus()
						|| status.getIdeStatusRequerimento() == StatusRequerimentoEnum.PENDENCIA_VALIDACAO_COMPROVANTE.getStatus()) {

					if(status.getIdeStatusRequerimento() != StatusRequerimentoEnum.BOLETO_VALIDADO.getStatus()) {
						itens.add(new SelectItem(status, status.getNomStatusRequerimento()));
					}
				}
			}
		} else {
			if (collectionStatusRequerimento != null) {
				for (StatusRequerimento status : collectionStatusRequerimento) {
					if(status.getIdeStatusRequerimento() != StatusRequerimentoEnum.BOLETO_VALIDADO.getStatus()) {
						itens.add(new SelectItem(status, status.getNomStatusRequerimento()));
					}
				}
			}
		}

		return itens;
	}

	/**
	 * [IMPORTADO]
	 *
	 * @see RequerimentoUnicoController
	 */
	private void consultarStatusRequerimentos() {
		try {
			collectionStatusRequerimento = statusRequerimentoService.listarStatusRequerimento();
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível carregar a lista de status");
		}
	}

	/**
	 * [IMPORTADO]
	 *
	 * @see RequerimentoUnicoController
	 */
	private void carregarUr() {
		try {
			listaUrs = (List<Area>) areaService.filtrarAreasUR();
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
		}
	}

	/**
	 * [IMPORTADO]
	 *
	 * @see RequerimentoUnicoController
	 */
	private void carregarMunicipios() {
		try {
			listaMunicipios = (List<Municipio>) municipioService.filtrarListaMunicipiosPorEstado(new Estado(5));
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
		}
	}

	/**
	 * [IMPORTADO]
	 *
	 * @see RequerimentoUnicoController
	 */
	private void carregarPortes() {
		try {
			listaPortes = porteService.listarPorte();
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
		}
	}

	/**
	 * [IMPORTADO]
	 *
	 * @see RequerimentoUnicoController
	 */
	public void limparTela() {
		addAtributoSessao("REQUERIMENTOS", null);
		numeroRequerimento = "";
		periodoInicio = null;
		periodoFim = null;
		requerente = null;
		statusRequerimento = null;
		empreendimentoRequerimento = null;
		empreendimento.setIdeEmpreendimento(0);
		tipoAto = null;
		atoAmbiental = null;
		urSelecionada = null;
		municipioSelecionado = null;
		municipios = null;
		tecnico = null;
		tipologiaDivisao = null;
		listaTipologiaDivisao = null;
		tipologiaAtividade = null;
		listaTipologiaAtividade = null;
		idePorte = null;
		consultaRealizada = false;
		limparComboTipologia();
		limparComboFinalidade();
		limparComboFormaCumprimento();
		init();
	}

	/**
	 * [IMPORTADO]
	 *
	 * @see RequerimentoUnicoController
	 */
	protected Object getAtributoSession(String pNomeAtributo) {
		return getAtributosSession().get(pNomeAtributo);
	}

	/**
	 * [IMPORTADO]
	 *
	 * @see SeiaControllerAb
	 */
	@SuppressWarnings("rawtypes")
	protected Map getAtributosSession() {
		return getContextoExterno().getSessionMap();
	}

	/**
	 * [IMPORTADO]
	 *
	 * @see RequerimentoUnicoController
	 */
	public StreamedContent getGerarResumoRequerimentoPdf() {
		try {
			
			Requerimento req = requerimentoService.buscarEntidadeCarregadaPorId(requerimentoDTO.getRequerimento());
			
			if (!Util.isNull(req) && !Util.isNullOuVazio(req.getDesCaminhoResumoOriginal())) {
				return new RelatorioUtil().downloadArquivoRelatorio(req.getDesCaminhoResumoOriginal());
			}
			else {
				List<Imovel> listaImovel = null;
				Map<String, Object> params = new HashMap<String, Object>();
				Empreendimento empreendimento = getRequerimentoDTO().getEmpreendimento();
				
				if(!Util.isNull(empreendimento) && !Util.isNull(empreendimento.getIdeEmpreendimento())) {
					listaImovel = (List<Imovel>) empreendimentoService.obterTipoImovelPorIdeEmpreendimento(empreendimento.getIdeEmpreendimento()).getImovelCollection();
					params.put("nom_tipo_imovel", listaImovel.isEmpty() ? "Não Identificado" : listaImovel.get(0).getIdeTipoImovel().getNomTipoImovel());
				}
				
				Integer ideRequerimento = getRequerimentoDTO().getRequerimento().getIdeRequerimento();
				params.put("ide_requerimento", ideRequerimento);
				
				if(this.requerimentoService.isRequerimentoAntigo(ideRequerimento)) {
					RelatorioUtil lRelatorio = new RelatorioUtil("resumo_requerimento.jasper", params);
					return lRelatorio.gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
				}
				else{
					return new ReportUtil().imprimir("resumo_requerimento.jasper", params); 
				}
			}
			
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return null;
	}

	/**
	 * [IMPORTADO]
	 * @throws Exception 
	 *
	 * @see RequerimentoUnicoController
	 */
	public boolean rendered(RequerimentoDTO requerimentoDTO, int acao) throws Exception {
		switch (acao) {
			
			case 1: // editar
				return isRequerimentoAguardandoEnquadramento(requerimentoDTO)
						|| requerimentoDTO.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.PENDENCIA_ENQUADRAMENTO.getStatus())
						|| requerimentoDTO.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.REQUERIMENTO_INCOMPLETO.getStatus());
			
			case 2: // efetuar enquadramento
				return isRequerimentoAguardandoEnquadramento(requerimentoDTO)
						|| requerimentoDTO.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.SENDO_ENQUADRADO.getStatus())
						|| requerimentoDTO.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.PENDENCIA_ENQUADRAMENTO.getStatus())
						|| requerimentoDTO.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.REQUERIMENTO_INCOMPLETO.getStatus());
			
			case 3: // enviar documento
				return isEnquadrado(requerimentoDTO)
						|| requerimentoDTO.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.PENDENCIA_VALIDACAO.getStatus())
						|| requerimentoDTO.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.PENDENCIA_ENVIO_DOCUMENTACAO.getStatus());
			
			case 4: // efetuar validação prévia
				return requerimentoDTO.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.PENDENCIA_VALIDACAO.getStatus())
						|| requerimentoDTO.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.EM_VALIDACAO_PREVIA.getStatus());
			
			case 5: // gravar taxa boleto
				return requerimentoDTO.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.VALIDADO.getStatus());
			
			case 6: // enviar comprovante ou baixar boleto
				return requerimentoDTO.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.PAGAMENTO_LIBERADO.getStatus())
						|| requerimentoDTO.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.PENDENCIA_VALIDACAO_COMPROVANTE.getStatus());
			
			case 7: // validar comprovante, baixa manual e gerar processo
				return requerimentoDTO.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.COMPROVANTE_ENVIADO.getStatus());
			
			case 8: // gerar relatório de ato declaratório
				return securityController.temAcesso(SecaoEnum.REQUERIMENTOS, FuncionalidadeEnum.RELATORIO_ATO_DECLARATORIO, AcaoEnum.GERAR)
						&& validaRelatorioAtoDeclaratorio(requerimentoDTO)
						&& isProcessoConcluido(requerimentoDTO)
						&& (verificarAtoDQC(requerimentoDTO) || verificarAtoLimpezaArea(requerimentoDTO));
			
			case 9:// visualizar Lac Erb
				return existeLac(requerimentoDTO) && isLacErb(requerimentoDTO) && isEnquadrado(requerimentoDTO) && !isProcessoConcluido(requerimentoDTO);
			
			case 10:// certificado Lac
				return existeLac(requerimentoDTO) && isProcessoConcluido(requerimentoDTO);// && isProcessoConcluido(pRequerimento);
			
			case 11:// relatorio final Lac
				return existeLac(requerimentoDTO) && isProcessoConcluido(requerimentoDTO);
			
			case 12:// visualizar Lac Posto
				return existeLac(requerimentoDTO) && isLacPosto(requerimentoDTO) && isEnquadrado(requerimentoDTO);
			
			case 13:
				return false;// && isLacGenerica(pRequerimento);
			
			case 14:// Gerar Certificado de Corte de Floresta
				return verificarAtoRegistroCorte(requerimentoDTO);
			
			case 15:// Gerar Certificado de Floresta de Produção Plantada
				return verificarAtoRegistroFlorestaProducao(requerimentoDTO) && isProcessoConcluido(requerimentoDTO);
				
			case 16: // Retorno de Status
				return !isRequerimentoAguardandoEnquadramento(requerimentoDTO) && !isRequerimentoCancelado(requerimentoDTO) 
						&& !isStatusRequerimentoProcessoFormado(requerimentoDTO) && isAtend() && !isRepflor(requerimentoDTO)
						&& (!isInexigibilidade(requerimentoDTO) &&
						!requerimentoDTO.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.REQUERIMENTO_INCOMPLETO.getStatus()))
						&& securityController.temAcesso(SecaoEnum.REQUERIMENTOS, FuncionalidadeEnum.RETORNO_DE_STATUS, AcaoEnum.GRAVAR);
			
			case 17: // #7060 -> Imprimir Relatório da LAC Transportadora
				return isLacTransportadora(requerimentoDTO) && !isRequerimentoAguardandoEnquadramento(requerimentoDTO) 
						&& !isRequerimentoCancelado(requerimentoDTO);
			
			case 18: //Gerar Certificado de APE
				return verificarAtoAPE(requerimentoDTO);
			
			case 19: 
				return verificarAtoDIAP(requerimentoDTO);
			
			case 20: 
				//DTRP
				return isProcessoConcluido(requerimentoDTO) && verificarAtoDTRP(requerimentoDTO);
			
			case 21:
				//RESUMO DTRP
				return !requerimentoDTO.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.REQUERIMENTO_INCOMPLETO.getStatus()) 
						&& verificarAtoDTRP(requerimentoDTO);
			
			case 22:
				//RESUMO REQUERIMENTO
				return !verificarAtoDTRP(requerimentoDTO) && !verificarDeclaracaoInexigibilidade(requerimentoDTO.getRequerimento());
				//return verificarAtoAPE(requerimentoDTO) && isProcessoConcluido(requerimentoDTO);
			
			case 23:
				//RESUMO INEXIGIBILIDADE
				return !requerimentoDTO.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.REQUERIMENTO_INCOMPLETO.getStatus()) 
						&& verificarDeclaracaoInexigibilidade(requerimentoDTO.getRequerimento());
			
			case 24:
				//CERTIFICADO INEXIGIBILIDADE
				return !requerimentoDTO.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.REQUERIMENTO_INCOMPLETO.getStatus()) 
						&& isDeclaracaoEmitida(requerimentoDTO);
			
			default:
				return false;
		}
	}

	
	
	private boolean existeLacTransportadora(RequerimentoDTO requerimentoDTO) throws Exception {
		return lacTransporteService.existeLacTransportadora(requerimentoDTO.getLac());
	}
	
	/**
	 * Método que verifica se existe uma {@link Lac} para aquele {@link Requerimento}.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param requerimentoDTO
	 * @return
	 * @since 07/07/2015
	 */
	private boolean existeLac(RequerimentoDTO requerimentoDTO) {
		return !Util.isNull(requerimentoDTO.getLac());
	}

	/**
	 * Método que verifica se o {@link StatusRequerimento} é igual à "Aguardando Enquadramento".
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param requerimentoDTO
	 * @return
	 * @since 07/07/2015
	 */
	private boolean isRequerimentoAguardandoEnquadramento(RequerimentoDTO requerimentoDTO) {
		return requerimentoDTO.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.AGUARDANDO_ENQUADRAMENTO.getStatus());
	}

	/**
	 * Método que verifica se o {@link StatusRequerimento} é igual à "Cancelado".
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param requerimentoDTO
	 * @return
	 * @since 07/07/2015
	 */
	private boolean isRequerimentoCancelado(RequerimentoDTO requerimentoDTO) {
		return requerimentoDTO.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.CANCELADO.getStatus());
	}

	/**
	 * Método que verifica se o {@link StatusRequerimento} é igual à "Processo Formado".
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param requerimentoDTO
	 * @return
	 * @since 07/07/2015
	 */
	private boolean isStatusRequerimentoProcessoFormado(RequerimentoDTO requerimentoDTO) {
		return requerimentoDTO.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.FORMADO.getStatus());
	}

	public boolean isRenderedValidacaoPrevia(RequerimentoDTO requerimentoDTO) {
		if(requerimentoDTO.isValidacaoPrevia()) {
			SecurityController sc = (SecurityController) SessaoUtil.recuperarManagedBean("#{security}", SecurityController.class);
			if(ContextoUtil.getContexto().getUsuarioLogado().isPerfilCoordenador()) {
				return sc.temAcesso("4.26.13") && isAtend();
			}
			else {
				return sc.temAcesso("4.26.13");
			}
		}
		return false;		
	}
	
	public boolean isAtend() {
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
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			return false;
		}
	}

	public StreamedContent gerarDocumentoCRF(Requerimento requerimento) {
		try {
			return relatorioRequerimentoServiceFacade.gerarDocumentoCRF(requerimento);
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível realizar a solicitação.");
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public StreamedContent getGerarDocumentoDLA() {
		try {
			return relatorioRequerimentoServiceFacade.gerarDocumentoDLA(requerimentoDTO.getRequerimento().getIdeRequerimento());
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível realizar a solicitação.");
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public StreamedContent getImprimirRelatorioCorteFloresta(RequerimentoDTO requerimentoDTO) {
		try {
			return relatorioRequerimentoServiceFacade.imprimirRelatorioCorteFloresta(requerimentoDTO.getRequerimento().getIdeRequerimento());
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível realizar a solicitação.");
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public StreamedContent getImprimirCertificadoAPE(RequerimentoDTO requerimentoDTO) {
		try {
			return relatorioRequerimentoServiceFacade.imprimirCertificadoAPE(requerimentoDTO.getRequerimento().getIdeRequerimento());
		} 
		catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível realizar a solicitação.");
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public StreamedContent getGerarDocumentoDeclaracaoPdf() {
		try {
			return relatorioRequerimentoServiceFacade.imprimirDQC(requerimentoDTO.getRequerimento().getIdeRequerimento());
			
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível realizar a solicitação.");
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public StreamedContent getRegistroCorteFlorestaProducaoPlantada(RequerimentoDTO requerimentoDTO) {
		try {
			return relatorioRequerimentoServiceFacade.imprimirRegistroCorteFlorestaProducaoPlantada(requerimentoDTO.getRequerimento().getIdeRequerimento());
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível realizar a solicitação.");
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private StreamedContent getImprimirCertificadoAtoDeclaratorio(ImpressoraAtoDeclaratorioIF imprimirAtoDeclaratorioIF, Integer ideRequerimento, DocumentoObrigatorioEnum docObrigatorioEnum) throws Exception{
		return imprimirAtoDeclaratorioIF.imprimirCertificado(ideRequerimento, docObrigatorioEnum);
	}
	
	public StreamedContent getImprimirCertificadoDIAP(RequerimentoDTO dto){
		try {
			Requerimento req = dto.getRequerimento();
			relatorioRequerimentoServiceFacade.gerarCertificados(req.getIdeRequerimento());
			return getImprimirCertificadoAtoDeclaratorio(new ImpressoraAtoDeclaratorioDIAP(diapFacade.montarTextoRepresentadoPor(req)), req.getIdeRequerimento(), DocumentoObrigatorioEnum.FORMULARIO_DIAP);
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível realizar a solicitação.");
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * [IMPORTADO]
	 *
	 * @see RequerimentoUnicoController
	 */
	private boolean isEnquadrado(RequerimentoDTO pRequerimento) {
		return pRequerimento.getStatusRequerimento().getIdeStatusRequerimento().equals(StatusRequerimentoEnum.ENQUADRADO.getStatus());
	}

	/**
	 * [IMPORTADO]
	 *
	 * @see RequerimentoUnicoController
	 */
	public boolean isProcessoConcluido(RequerimentoDTO pRequerimento) {
		try {
			return !Util.isNullOuVazio(controleTramitacaoDAOImpl.buscarStatusFluxoProcessoConcluido(pRequerimento.getRequerimento().getIdeRequerimento())); 
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * [IMPORTADO] ZCR/RN0031. A opção Gerar relatório de ato declaratório só
	 * será exibida caso exista um processo relacionado ao Requerimento, o
	 * status do processo seja Concluído, e TODOS os atos relacionados ao
	 * processo sejam Declaratórios.
	 *
	 * @see RequerimentoUnicoController
	 */
	private boolean validaRelatorioAtoDeclaratorio(RequerimentoDTO pRequerimentoDTO) {
		return processoRequerimentoServiceFacade.validaRelatorioAtoDeclaratorio(pRequerimentoDTO);
	}

	/**
	 * Método que verifica se o {@link Requerimento} tem o {@link AtoAmbientalEnum} passado no parâmetro.
	 * 
	 * @since 16/01/2017
	 * @author eduardo.fernandes
	 */
	private boolean verificarProcessoComAtoBy(RequerimentoDTO reqDTO, AtoAmbientalEnum atoAmbientalEnum) {
		if (!Util.isNullOuVazio(reqDTO.getRequerimento().getAtosAmbientais())) {
			for (AtoAmbiental atoAmbiental : reqDTO.getRequerimento().getAtosAmbientais()) {
				if (atoAmbiental.getIdeAtoAmbiental().equals(atoAmbientalEnum.getId())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Método que verifica se o {@link StatusRequerimento} é 'Processo Formado' e se o {@link StatusFluxo} é 'Concluido'.  
	 * 
	 * @author eduardo.fernandes 
	 * @param requerimentoDTO
	 * @return
	 */
	private boolean isPossivelGerarCertificado(RequerimentoDTO requerimentoDTO){
		return isProcessoConcluido(requerimentoDTO) && isStatusRequerimentoProcessoFormado(requerimentoDTO);
	}

	private boolean verificarAtoRegistroCorte(RequerimentoDTO pRequerimento) {
		return verificarProcessoComAtoBy(pRequerimento, AtoAmbientalEnum.CORTE_FLORESTA_PRODUCAO) 
				&& isPossivelGerarCertificado(pRequerimento)
				&& verificarAtoEmitido(pRequerimento, AtoAmbientalEnum.CORTE_FLORESTA_PRODUCAO);
	}
	
	private boolean verificarAtoRegistroFlorestaProducao(RequerimentoDTO pRequerimento) {
		return verificarProcessoComAtoBy(pRequerimento, AtoAmbientalEnum.REGISTRO_FLORESTA_PRODUCAO) 
				&& isPossivelGerarCertificado(pRequerimento)
				&& verificarAtoEmitido(pRequerimento, AtoAmbientalEnum.REGISTRO_FLORESTA_PRODUCAO);
	}

	private boolean verificarAtoAPE(RequerimentoDTO pRequerimento) {
		return verificarProcessoComAtoBy(pRequerimento, AtoAmbientalEnum.APE) 
				&& isPossivelGerarCertificado(pRequerimento)
				&& verificarAtoEmitido(pRequerimento, AtoAmbientalEnum.APE);
	}

	private boolean verificarAtoDIAP(RequerimentoDTO dto) {
		return verificarProcessoComAtoBy(dto, AtoAmbientalEnum.DIAP) 
				&& isPossivelGerarCertificado(dto)
				&& verificarAtoEmitido(dto, AtoAmbientalEnum.DIAP);
	}
	
	private boolean verificarAtoDTRP(RequerimentoDTO dto) {
		return verificarProcessoComAtoBy(dto, AtoAmbientalEnum.DTRP);
	}
	
	private boolean verificarAtoDQC(RequerimentoDTO dto) {
		return verificarProcessoComAtoBy(dto, AtoAmbientalEnum.DQC);
	}
	
	private boolean verificarAtoLimpezaArea(RequerimentoDTO dto) {
		return verificarProcessoComAtoBy(dto, AtoAmbientalEnum.LIMPEZA_AREA);
	}
	
	//Os requerimento que possuem DAE
	public boolean isDae(RequerimentoDTO dto) {
		return verificarProcessoComAtoBy(dto, AtoAmbientalEnum.CRF);
	}
	
	public boolean isCRF(RequerimentoDTO dto) {
		return verificarProcessoComAtoBy(dto, AtoAmbientalEnum.CRF);
	}
	
	/**
	 * [IMPORTADO]
	 *
	 * @see RequerimentoUnicoController
	 */
	public boolean isLacPosto(RequerimentoDTO pRequerimento) {
		try {

			if (!Util.isNull(pRequerimento)
					&& !Util.isNull(pRequerimento.getRequerimento())
					&& !Util.isNull(pRequerimento.getRequerimento().getIdeRequerimento())
					&& existeLac(pRequerimento)
					&& !Util.isNull(pRequerimento.getLac().getIdeDocumentoObrigatorio())
					&& !Util.isNull(pRequerimento.getLac().getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())) {

				return isTipoLacDocObrigInformado(pRequerimento, DocumentoObrigatorioEnum.POSTO);
			}

			return false;
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * [IMPORTADO]
	 *
	 * @see RequerimentoUnicoController
	 */
	public boolean isLacTransportadora(RequerimentoDTO pRequerimento) {
		try {

			if (!Util.isNull(pRequerimento)
					&& !Util.isNull(pRequerimento.getRequerimento())
					&& !Util.isNull(pRequerimento.getRequerimento().getIdeRequerimento())
					&& existeLac(pRequerimento)
					&& existeLacTransportadora(pRequerimento)
					&& !Util.isNull(pRequerimento.getLac().getIdeDocumentoObrigatorio())
					&& !Util.isNull(pRequerimento.getLac().getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())) {

				return isTipoLacDocObrigInformado(pRequerimento, DocumentoObrigatorioEnum.TRANSPORTADORA);
			}

			return false;
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * [IMPORTADO]
	 *
	 * @see RequerimentoUnicoController
	 */
	public boolean isLacErb(RequerimentoDTO pRequerimento) {
		try {

			if (!Util.isNull(pRequerimento)
					&& !Util.isNull(pRequerimento.getRequerimento())
					&& !Util.isNull(pRequerimento.getRequerimento().getIdeRequerimento())
					&& existeLac(pRequerimento)
					&& !Util.isNull(pRequerimento.getLac().getIdeDocumentoObrigatorio())
					&& !Util.isNull(pRequerimento.getLac().getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio())) {

				return isTipoLacDocObrigInformado(pRequerimento, DocumentoObrigatorioEnum.ERB);

			}

			return false;
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private boolean isTipoLacDocObrigInformado(RequerimentoDTO pRequerimento, DocumentoObrigatorioEnum docObrigEnum) throws Exception {
		List<AtoAmbiental> listaAtosEnq = (List<AtoAmbiental>) atoAmbientalService.listarAtoAmbientalPorEnquadramentoRequerimento(pRequerimento.getRequerimento().getIdeRequerimento());
		for (AtoAmbiental ato: listaAtosEnq) {
			if(ato.getIdeAtoAmbiental().equals(AtoAmbientalEnum.LAC.getId())) {
				return docObrigEnum.getId().equals(pRequerimento.getLac().getIdeDocumentoObrigatorio().getIdeDocumentoObrigatorio());
			}
		}
		return false;
	}

	/**
	 * [IMPORTADO]
	 *
	 * @see RequerimentoUnicoController
	 */
	public Boolean isConversaoTcraLac(RequerimentoDTO pRequerimento) {
		if(Util.isNull(pRequerimento.getEmpreendimento())) {
			return false;
		}
		return pRequerimento.getEmpreendimento().getIndConversaoTcraLac();
	}

	public Boolean isConversaoTcraLac() {
		return this.empreendimento.getIndConversaoTcraLac();
	}

	public Boolean isCessionario() {
		if(this.empreendimento.getIndCessionario() == null) {
			return false;
		}
		return this.empreendimento.getIndCessionario();
	}

	/**
	 * [IMPORTADO]"/paginas/manter-processo/pautaReenquadramentoProcesso.xhtml"
	 *
	 * @see RequerimentoUnicoController
	 */
	public boolean isLac(RequerimentoDTO pRequerimento) {
		return existeLac(pRequerimento) && !Util.isNull(pRequerimento.getLac().getIdeLac());
	}

	/**
	 * [IMPORTADO]
	 *
	 * @see RequerimentoUnicoController
	 */
	public void excluirRequerimento() {
		try {
			requerimento = (Requerimento) ContextoUtil.getContexto().getObject();
			Pessoa operador = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa();
			this.tramitacaoRequerimentoService.tramitar(requerimento, StatusRequerimentoEnum.CANCELADO, operador);

			this.salvarComunicacaoCancelamento();

			JsfUtil.addSuccessMessage("Requerimento cancelado com sucesso!");
			
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);

			if(e.getMessage().contains("É necessário informar pelo menos um endereço de email para que o email seja enviado.")){
				JsfUtil.addErrorMessage("Não foi possível enviar o e-mail de comunicação.");
			} else {
				JsfUtil.addErrorMessage("Não foi possível excluir o requerimento único selecionado.");
			}
		}
	}

	private void salvarComunicacaoCancelamento() throws Exception {
		String email = "Prezado(a),  \n" + "O Requerimento de número " + requerimento.getNumRequerimento()
				+ " foi cancelado. \n"
				+ "Para maiores esclarecimentos favor entrar o contato com a Central de Atendimento .\n\n"
				+ "Atte., \n" + "Central de Atendimento/INEMA.";
		String assunto = "Cancelamento de Requerimento";
		this.comunicacaoRequerimentoService.salvar(requerimento, assunto , email);
		emailService.enviarEmailsAoRequerente(requerimento, assunto, email);
	}

	public StreamedContent gerarDocumentoDLA(Requerimento requerimento) {
		try {
			obterCertificado(requerimento);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ide_requerimento", requerimento.getIdeRequerimento());
			RelatorioUtil lRelatorio = new RelatorioUtil("dla.jasper", params);
			DefaultStreamedContent relatorio = (DefaultStreamedContent) lRelatorio.gerarRelatorio(
					RelatorioUtil.RELATORIO_PDF, true);
			relatorio.setContentType("application/pdf");
			return relatorio;
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return null;
	}

	/**
	 * Método para gerar o {@link Certificado} de DLA do {@link Requerimento}.
	 * @author eduardo.fernandes
	 * @param requerimento
	 * @throws Exception
	 * @see #7268
	 */
	private void obterCertificado(Requerimento requerimento) throws Exception {
		certificadoService.gerarComprovante(requerimento, TipoCertificadoEnum.DLA);
	}

	public void selecionarRequerente(Pessoa requerente) {
		this.requerente = requerente;
		carregarEmpreendimento();
		Html.atualizar("formConsultarRequerimento");
	}

	public boolean isRLAC(RequerimentoDTO pRequerimento) {
		try {
			if (!Util.isNull(pRequerimento) && !Util.isNull(pRequerimento.getRequerimento()) && !Util.isNull(pRequerimento.getRequerimento().getIdeRequerimento())) {
				List<AtoAmbiental> listaAtosEnq = (List<AtoAmbiental>) atoAmbientalService.listarAtoAmbientalPorEnquadramentoRequerimento(pRequerimento.getRequerimento().getIdeRequerimento());
				for (AtoAmbiental ato: listaAtosEnq) {
					if(ato.getIdeAtoAmbiental().equals(AtoAmbientalEnum.RLAC.getId())) {
						return true;
					}
				}
			}
			return false;
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public StreamedContent imprimirCertificadoRLAC(RequerimentoDTO requerimentoDTO) throws Exception {
		try {
			return relatorioRequerimentoServiceFacade.imprimirCertificadoRLAC(requerimentoDTO.getRequerimento().getIdeRequerimento());
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível realizar a solicitação.");
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void carregarEfetuarReenquadramento(){
		enquadramentoController.loadReenquadramento(requerimentoDTO.getProcessoReenquadramentoDTO());
	}
	
	public boolean verificarDeclaracaoInexigibilidade(Requerimento requerimento) throws Exception{
		DeclaracaoInexigibilidade declaracaoInexigibilidade = this.declaracaoInexigibilidadeService.obterDeclaracaoPorRequerimento(requerimento);
		if(declaracaoInexigibilidade != null){
			return true;
		}
		return false;
	}
	
	public boolean isRepflor(RequerimentoDTO pRequerimento) {
		try {
			if (pRequerimento != null && pRequerimento.getRequerimento() != null && pRequerimento.getRequerimento().getIdeRequerimento() != null) {
				for (AtoAmbiental ato: atoAmbientalService.listarAtoAmbientalPorEnquadramentoRequerimento(pRequerimento.getRequerimento().getIdeRequerimento())) {
					if(AtoAmbientalEnum.CRF.getId().equals(ato.getIdeAtoAmbiental())) {
						return true;
					}
				}
			}
			
			return false;
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public boolean isInexigibilidade(RequerimentoDTO pRequerimento) {
		try {
			if (pRequerimento != null && pRequerimento.getRequerimento() != null && pRequerimento.getRequerimento().getIdeRequerimento() != null) {
				for (AtoAmbiental ato: atoAmbientalService.listarAtoAmbientalPorEnquadramentoRequerimento(pRequerimento.getRequerimento().getIdeRequerimento())) {
					if(AtoAmbientalEnum.INEXIGIBILIDADE.getId().equals(ato.getIdeAtoAmbiental())) {
						return true;
					}
				}
			}
			
			return false;
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void finalizarEdicaoParaReenquadramento() {
		try {
			boolean test = abaFinalizarRequerimentoController.finalizarEdicaoReenquadramento(); 
			if(test) {		
				novoRequerimentoService.finalizarEdicaoParaReenquadramento(requerimentoDTO);
				SessaoUtil.adicionarObjetoSessao("numProcesso", requerimentoDTO.getProcessoReenquadramentoDTO().getProcessoReenquadramento().getIdeProcesso().getNumProcesso());
				FacesContext.getCurrentInstance().getExternalContext().redirect("/paginas/manter-processo/pautaReenquadramentoProcesso.xhtml");
				SessaoUtil.removerObjetoSessao("isEnquadramento");
			}
		}
		catch (Exception e) {
			MensagemUtil.erro("Erro ao realizar a solicitação");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public StreamedContent getGerarRelatorioRequerimento() throws Exception {
		
		try {
			Map<String, Object> paramsRelatorio = carregarParametros();
			paramsRelatorio.put("consulta", novoRequerimentoService.gerarConsultaRelatorioRequerimento(this));
			if(tipoDeImpressaoPdf) {
				return new RelatorioUtil("relatorioRequerimentoPdf.jasper", paramsRelatorio, "logoInemaRelatorio.png", "sema_vertical.png").gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);

			} else {
				return new RelatorioUtil("relatorioRequerimentoXls.jasper", paramsRelatorio, "logoInemaRelatorio.png", "sema_vertical.png").gerarRelatorio(RelatorioUtil.RELATORIO_XLS, true);

			}
					} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
			return null;
		}
		
	}
	
	public void liberarEdicao() {
		this.desabilitarTudo = false;
		this.modoEdicao = true;
	}
	
	public MetodoUtil getMetodoLiberarEdicao() {
		return new MetodoUtil(this, "liberarEdicao");
	}
	
	public void setDataHoje(String dataHoje) {
		this.dataHoje = dataHoje;
	}

	public boolean isDesabilitarTudo() {
		return desabilitarTudo;
	}

	public void setDesabilitarTudo(boolean desabilitarTudo) {
		this.desabilitarTudo = desabilitarTudo;
	}

	public boolean getModoEdicaoDialog() {
		return modoEdicaoDialog;
	}

	public void setModoEdicaoDialog(boolean modoEdicaoDialog) {
		this.modoEdicaoDialog = modoEdicaoDialog;
	}

	public Requerimento getRequerimentoSelecionado() {
		return requerimentoSelecionado;
	}

	public void setRequerimentoSelecionado(Requerimento requerimentoSelecionado) {
		this.requerimentoSelecionado = requerimentoSelecionado;
	}

	public int getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}

	public Boolean getMostraQuestionarioAposSalvar() {
		return mostraQuestionarioAposSalvar;
	}

	public void setMostraQuestionarioAposSalvar(Boolean mostraQuestionarioAposSalvar) {
		this.mostraQuestionarioAposSalvar = mostraQuestionarioAposSalvar;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public boolean isModoEdicao() {
		return modoEdicao;
	}

	public void setModoEdicao(boolean modoEdicao) {
		this.modoEdicao = modoEdicao;
	}

	public Boolean getVisualizaSelectEmpreendimento() {
		return visualizaSelectEmpreendimento;
	}

	public void setVisualizaSelectEmpreendimento(Boolean visualizaSelectEmpreendimento) {
		this.visualizaSelectEmpreendimento = visualizaSelectEmpreendimento;
	}

	public List<SelectItem> getCollEmpreendimento() {
		return collEmpreendimento;
	}

	public void setCollEmpreendimento(List<SelectItem> collEmpreendimento) {
		this.collEmpreendimento = collEmpreendimento;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}

	public Requerimento getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public Boolean getEnderecoContato() {
		return enderecoContato;
	}

	public void setEnderecoContato(Boolean enderecoContato) {
		this.enderecoContato = enderecoContato;
	}

	public List<LocalizacaoGeografica> getListaLocalizacaoGeografica() {
		return listaLocalizacaoGeografica;
	}

	public void setListaLocalizacaoGeografica(List<LocalizacaoGeografica> listaLocalizacaoGeografica) {
		this.listaLocalizacaoGeografica = listaLocalizacaoGeografica;
	}

	public LocalizacaoGeografica getLocalizacaoGeograficaSelecionada() {
		return Util.isNull(localizacaoGeograficaSelecionada) ? (localizacaoGeograficaSelecionada = new LocalizacaoGeografica())
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

	public Map<Integer, LocalizacaoGeografica> getMapLocGeo() {
		return mapLocGeo;
	}

	public void setMapLocGeo(Map<Integer, LocalizacaoGeografica> mapLocGeo) {
		this.mapLocGeo = mapLocGeo;
	}

	public TipoProrrogacaoPrazoValidade getTipoProrrogacaoSelecionado() {
		return tipoProrrogacaoSelecionado;
	}

	public void setTipoProrrogacaoSelecionado(TipoProrrogacaoPrazoValidade tipoProrrogacaoSelecionado) {
		this.tipoProrrogacaoSelecionado = tipoProrrogacaoSelecionado;
	}

	public LocalizacaoGeografica[] getListLocalizacaoGeo() {
		return listLocalizacaoGeo;
	}

	public void setListLocalizacaoGeo(LocalizacaoGeografica[] listLocalizacaoGeo) {
		this.listLocalizacaoGeo = listLocalizacaoGeo;
	}

	public Imovel getImovelSelecionado() {
		return imovelSelecionado;
	}

	public void setImovelSelecionado(Imovel imovelSelecionado) {
		this.imovelSelecionado = imovelSelecionado;
	}

	public List<Imovel> getListaImovel() {
		return listaImovel;
	}

	public void setListaImovel(List<Imovel> listaImovel) {
		this.listaImovel = listaImovel;
	}

	public boolean isModoEdicaoSubDialog() {
		return modoEdicaoSubDialog;
	}

	public void setModoEdicaoSubDialog(boolean modoEdicaoSubDialog) {
		this.modoEdicaoSubDialog = modoEdicaoSubDialog;
	}

	public boolean isImovelUnico() {
		return imovelUnico;
	}

	public void setImovelUnico(boolean imovelUnico) {
		this.imovelUnico = imovelUnico;
	}

	public List<PerguntaRequerimento> getListPerguntaRequerimentoAba0() {
		return listPerguntaRequerimentoAba0;
	}

	public void setListPerguntaRequerimentoAba0(List<PerguntaRequerimento> listPerguntaRequerimentoAba0) {
		this.listPerguntaRequerimentoAba0 = listPerguntaRequerimentoAba0;
	}

	public List<Pergunta> getListPerguntas() {
		return listPerguntas;
	}

	public void setListPerguntas(List<Pergunta> listPerguntas) {
		this.listPerguntas = listPerguntas;
	}

	public boolean isPerguntasAba0Carregadas() {
		return perguntasAba0Carregadas;
	}

	public void setPerguntasAba0Carregadas(boolean perguntasAba0Carregadas) {
		this.perguntasAba0Carregadas = perguntasAba0Carregadas;
	}

	public PerguntaRequerimento getPergunta0_1() {
		return pergunta0_1;
	}

	public void setPergunta0_1(PerguntaRequerimento pergunta0_1) {
		this.pergunta0_1 = pergunta0_1;
	}

	public SolicitacaoAdministrativo getSolicitacaoAdministrativo() {
		return solicitacaoAdministrativo;
	}

	public void setSolicitacaoAdministrativo(SolicitacaoAdministrativo solicitacaoAdministrativo) {
		this.solicitacaoAdministrativo = solicitacaoAdministrativo;
	}

	public String getNovaRazaoSocial() {
		return novaRazaoSocial;
	}

	public void setNovaRazaoSocial(String novaRazaoSocial) {
		this.novaRazaoSocial = novaRazaoSocial;
	}

	public ProcessoExterno getProcessoExterno() {
		return processoExterno;
	}

	public void setProcessoExterno(ProcessoExterno processoExterno) {
		this.processoExterno = processoExterno;
	}

	protected void addAtributoSessao(String pNomeAtributo, Object pValor) {
		getSession().setAttribute(pNomeAtributo, pValor);
	}

	protected HttpSession getSession() {
		return getRequest().getSession();
	}

	protected HttpServletRequest getRequest() {
		return (HttpServletRequest) getContextoExterno().getRequest();
	}

	protected ExternalContext getContextoExterno() {
		return getCurrentInstante().getExternalContext();
	}

	protected FacesContext getCurrentInstante() {
		return FacesContext.getCurrentInstance();
	}

	public Pessoa getRequerente() {
		return requerente;
	}

	public void setRequerente(Pessoa requerente) {
		this.requerente = requerente;
	}

	public TipoAto getTipoAto() {
		return tipoAto;
	}

	public void setTipoAto(TipoAto tipoAto) {
		this.tipoAto = tipoAto;
	}

	public List<TipoAto> getListaTipoAto() {
		return listaTipoAto;
	}

	public void setListaTipoAto(List<TipoAto> listaTipoAto) {
		this.listaTipoAto = listaTipoAto;
	}

	public AtoAmbiental getAtoAmbiental() {
		return atoAmbiental;
	}

	public void setAtoAmbiental(AtoAmbiental atoAmbiental) {
		this.atoAmbiental = atoAmbiental;
	}

	public List<AtoAmbiental> getListaAto() {
		return listaAto;
	}

	public void setListaAto(List<AtoAmbiental> listaAto) {
		this.listaAto = listaAto;
	}

	public String getNumeroRequerimento() {
		return numeroRequerimento;
	}

	public void setNumeroRequerimento(String numeroRequerimento) {
		this.numeroRequerimento = numeroRequerimento;
	}

	public StatusRequerimento getStatusRequerimento() {
		return statusRequerimento;
	}

	public void setStatusRequerimento(StatusRequerimento statusRequerimento) {
		this.statusRequerimento = statusRequerimento;
	}

	public Collection<StatusRequerimento> getCollectionStatusRequerimento() {
		return collectionStatusRequerimento;
	}

	public void setCollectionStatusRequerimento(Collection<StatusRequerimento> collectionStatusRequerimento) {
		this.collectionStatusRequerimento = collectionStatusRequerimento;
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

	public Municipio getMunicipioSelecionado() {
		return municipioSelecionado;
	}

	public void setMunicipioSelecionado(Municipio municipioSelecionado) {
		this.municipioSelecionado = municipioSelecionado;
	}

	public ArrayList<Municipio> getMunicipios() {
		return municipios;
	}

	public void setMunicipios(ArrayList<Municipio> municipios) {
		this.municipios = municipios;
	}

	public List<Municipio> getListaMunicipios() {
		return listaMunicipios;
	}

	public void setListaMunicipios(List<Municipio> listaMunicipios) {
		this.listaMunicipios = listaMunicipios;
	}

	public List<Porte> getListaPortes() {
		return listaPortes;
	}

	public void setListaPortes(List<Porte> listaPortes) {
		this.listaPortes = listaPortes;
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
		this.periodoFim = periodoFim;
	}

	public LazyDataModel<RequerimentoDTO> getListaRequerimentosDTOModel() {
		return listaRequerimentosDTOModel;
	}

	public void setListaRequerimentosDTOModel(LazyDataModel<RequerimentoDTO> listaRequerimentosDTOModel) {
		this.listaRequerimentosDTOModel = listaRequerimentosDTOModel;
	}

	public DataTable getDataTableRequerimentos() {
		return dataTableRequerimentos;
	}

	public void setDataTableRequerimentos(DataTable dataTableRequerimentos) {
		this.dataTableRequerimentos = dataTableRequerimentos;
	}

	public RequerimentoDTO getRequerimentoDTO() {
		return requerimentoDTO;
	}

	public void setRequerimentoDTO(RequerimentoDTO requerimentoDTO) {
		this.requerimentoDTO = requerimentoDTO;
	}

	public EmpreendimentoRequerimento getEmpreendimentoRequerimento() {
		return empreendimentoRequerimento;
	}

	public void setEmpreendimentoRequerimento(EmpreendimentoRequerimento empreendimentoRequerimento) {
		this.empreendimentoRequerimento = empreendimentoRequerimento;
	}

	public EmpreendimentoRequerimento getEmpreendimentoRequerimentoBuscado() {
		return empreendimentoRequerimentoBuscado;
	}

	public void setEmpreendimentoRequerimentoBuscado(EmpreendimentoRequerimento empreendimentoRequerimentoBuscado) {
		this.empreendimentoRequerimentoBuscado = empreendimentoRequerimentoBuscado;
	}

	public RequerimentoUnicoDTO getRequerimentoUnicoDTO() {
		return requerimentoUnicoDTO;
	}

	public void setRequerimentoUnicoDTO(RequerimentoUnicoDTO requerimentoUnicoDTO) {
		this.requerimentoUnicoDTO = requerimentoUnicoDTO;
	}

	public Empreendimento getEmpreendimentoBusca() {
		return empreendimentoBusca;
	}

	public void setEmpreendimentoBusca(Empreendimento empreendimentoBusca) {
		this.empreendimentoBusca = empreendimentoBusca;
	}

	public List<Imovel> getListaImovelSelecionado() {
		return listaImovelSelecionado;
	}

	public void setListaImovelSelecionado(List<Imovel> listaImovelSelecionado) {
		this.listaImovelSelecionado = listaImovelSelecionado;
	}

	public boolean isImovelUrbano() {
		return isImovelUrbano;
	}

	public void setImovelUrbano(boolean isImovelUrbano) {
		this.isImovelUrbano = isImovelUrbano;
	}

	
	public boolean isTransferenciaTitularidade() {
		Boolean indRespostaTransferenciaTitularidade = abaProcessoController.getPerguntaNR_A1_P2().getIndResposta();
		if (!Util.isNullOuVazio(indRespostaTransferenciaTitularidade)) {
			return indRespostaTransferenciaTitularidade;
		}

		return false;
	}
	
	public boolean isDisabledAbaRenovacao() {		
		return isTransferenciaTitularidade() || disabledAbasRequerimento;
	}
	
	public boolean isDisabledAbaLicenca() {
		return isTransferenciaTitularidade();
	}
	
	public boolean isDisabledAbaOutorga() {
		return isTransferenciaTitularidade() || disabledAbasRequerimento;
	}
	
	public boolean isDisabledAbaFlorestal() {
		return isTransferenciaTitularidade() || disabledAbasRequerimento;
	}
	
	public boolean isDisabledAbaFauna() {
		return isTransferenciaTitularidade() || disabledAbasRequerimento;
	}
	
	public boolean isDisabledAbaFinalizarRequerimento() {
		return isTransferenciaTitularidade();
		
	}

	public Boolean getRendererPoll() {
		return rendererPoll;
	}

	public void setRendererPoll(Boolean rendererPoll) {
		this.rendererPoll = rendererPoll;
	}

	public boolean isModoEnquadramento() {
		return modoEnquadramento;
	}

	public void setModoEnquadramento(boolean modoEnquadramento) {
		this.modoEnquadramento = modoEnquadramento;
	}

	public AbaRenovacaoAlteracaoProrrogacaoController getAbaRenovacaoAlteracaoProrrogacaoController() {
		return abaRenovacaoAlteracaoProrrogacaoController;
	}

	public boolean isImovelRural() {

		if(Util.isNull(empreendimento) || Util.isNull(empreendimento.getIdeEmpreendimento()) || empreendimento.getIdeEmpreendimento() == 0){
			return false;
		}

		return !Util.isNull(this.getEmpreendimento().getIdeEmpreendimento())
				&& !isCessionario() && !this.isImovelUrbano;
	}
	
	public boolean isDeclaracaoEmitida(RequerimentoDTO pRequerimento) {
		try {
			TramitacaoRequerimento tramitacaoRequerimento = tramitacaoRequerimentoService.buscarUltimaTramitacaoPorRequerimento(pRequerimento.getRequerimento().getIdeRequerimento());
			
			if(!Util.isNull(tramitacaoRequerimento) && 
					!Util.isNull(tramitacaoRequerimento.getIdeStatusRequerimento()) &&
					!Util.isNull(tramitacaoRequerimento.getIdeStatusRequerimento().getIdeStatusRequerimento()) &&
					StatusRequerimentoEnum.DECLARACAO_EMITIDA.getStatus() == tramitacaoRequerimento.getIdeStatusRequerimento().getIdeStatusRequerimento().intValue()) {
				return true;
			}
			
			return false;
			
		} catch (Exception e) {
			return false;
		}
	}


	public boolean isDeclaracao() {
		return declaracao;
	}

	public void setDeclaracao(boolean declaracao) {
		this.declaracao = declaracao;
		if(this.requerimento != null) {
			this.requerimento.setIndDeclaracao(declaracao);
		}
	}

	public AbaLicencaAutorizacaoController getAbaLicencaAutorizacaoController() {
		return abaLicencaAutorizacaoController;
	}

	/**
	 * @return the abaOutorgaController
	 */
	public AbaOutorgaController getAbaOutorgaController() {
		return abaOutorgaController;
	}

	/**
	 * @param abaOutorgaController the abaOutorgaController to set
	 */
	public void setAbaOutorgaController(AbaOutorgaController abaOutorgaController) {
		this.abaOutorgaController = abaOutorgaController;
	}

	public Porte getIdePorte() {
		return idePorte;
	}

	public void setIdePorte(Porte idePorte) {
		this.idePorte = idePorte;
	}

	public boolean isDadosContato() {
		return dadosContato;
	}

	public void setDadosContato(boolean dadosContato) {
		this.dadosContato = dadosContato;
	}

	public boolean isEmailRequerimentoCarregadoPeloEmpreendimento() {
		return emailRequerimentoCarregadoPeloEmpreendimento;
	}

	public void setEmailRequerimentoCarregadoPeloEmpreendimento(boolean emailRequerimentoCarregadoPeloEmpreendimento) {
		this.emailRequerimentoCarregadoPeloEmpreendimento = emailRequerimentoCarregadoPeloEmpreendimento;
	}

	public Funcionario getTecnico() {
		return tecnico;
	}

	public void setTecnico(Funcionario tecnico) {
		this.tecnico = tecnico;
	}

	public List<Tipologia> getListaTipologiaDivisao() {
		return listaTipologiaDivisao;
	}

	public void setListaTipologiaDivisao(List<Tipologia> listaTipologiaDivisao) {
		this.listaTipologiaDivisao = listaTipologiaDivisao;
	}

	public List<Tipologia> getListaTipologiaAtividade() {
		return listaTipologiaAtividade;
	}

	public void setListaTipologiaAtividade(List<Tipologia> listaTipologiaAtividade) {
		this.listaTipologiaAtividade = listaTipologiaAtividade;
	}

	public Tipologia getTipologiaDivisao() {
		return tipologiaDivisao;
	}

	public void setTipologiaDivisao(Tipologia tipologiaDivisao) {
		this.tipologiaDivisao = tipologiaDivisao;
	}

	public Tipologia getTipologiaAtividade() {
		return tipologiaAtividade;
	}

	public void setTipologiaAtividade(Tipologia tipologiaAtividade) {
		this.tipologiaAtividade = tipologiaAtividade;
	}

	public String getNumProcesso() {
		return numProcesso;
	}

	public void setNumProcesso(String numProcesso) {
		this.numProcesso = numProcesso;
	}

	public List<SolicitacaoAdministrativo> getListaSolicitacaoAdministrativo() {
		return listaSolicitacaoAdministrativo;
	}

	public void setListaSolicitacaoAdministrativo(
			List<SolicitacaoAdministrativo> listaSolicitacaoAdministrativo) {
		this.listaSolicitacaoAdministrativo = listaSolicitacaoAdministrativo;
	}

	public boolean isDisabledAbasRequerimento() {
		return disabledAbasRequerimento;
	}

	public void setDisabledAbasRequerimento(boolean disabledAbasRequerimento) {
		this.disabledAbasRequerimento = disabledAbasRequerimento;
	}

	public boolean isDeclaracaoAutorizacaoEspecial() {
		return declaracaoAutorizacaoEspecial;
	}

	public void setDeclaracaoAutorizacaoEspecial(
			boolean declaracaoAutorizacaoEspecial) {
		this.declaracaoAutorizacaoEspecial = declaracaoAutorizacaoEspecial;
	}

	public Collection<Tipologia> getListaTipologia() {
		return listaTipologia;
	}

	public void setListaTipologia(Collection<Tipologia> listaTipologia) {
		this.listaTipologia = listaTipologia;
	}

	public Tipologia getTipologia() {
		return tipologia;
	}

	public void setTipologia(Tipologia tipologia) {
		this.tipologia = tipologia;
	}

	public Collection<TipoFinalidadeUsoAgua> getListaFinalidadeUsoAgua() {
		return listaFinalidadeUsoAgua;
	}

	public void setListaFinalidadeUsoAgua(
			Collection<TipoFinalidadeUsoAgua> listaFinalidadeUsoAgua) {
		this.listaFinalidadeUsoAgua = listaFinalidadeUsoAgua;
	}

	public TipoFinalidadeUsoAgua getFinalidadeUsoAgua() {
		return finalidadeUsoAgua;
	}

	public void setFinalidadeUsoAgua(TipoFinalidadeUsoAgua finalidadeUsoAgua) {
		this.finalidadeUsoAgua = finalidadeUsoAgua;
	}
	
	public MetodoUtil getMetodoExterno() {
		return metodoExterno;
	}

	public void setMetodoExterno(MetodoUtil metodoExterno) {
		this.metodoExterno = metodoExterno;
	}
	public ProcessoReenquadramentoDTO getProcessoReenquadramentoDTO() {
		return processoReenquadramentoDTO;
	}

	public void setProcessoReenquadramentoDTO(
			ProcessoReenquadramentoDTO processoReenquadramentoDTO) {
		this.processoReenquadramentoDTO = processoReenquadramentoDTO;
	}
	
	public AbaFlorestalController getAbaFlorestalController() {
		return abaFlorestalController;
	}

	public void setAbaFlorestalController(AbaFlorestalController abaFlorestalController) {
		this.abaFlorestalController = abaFlorestalController;
	}

	public AbaFaunaController getAbaFaunaController() {
		return abaFaunaController;
	}

	public void setAbaFaunaController(AbaFaunaController abaFaunaController) {
		this.abaFaunaController = abaFaunaController;
	}

	private boolean verificarAtoEmitido(RequerimentoDTO pRequerimento, AtoAmbientalEnum atoAmbientalEnum) {
		
		if(pRequerimento != null && !Util.isNullOuVazio(pRequerimento.getProcesso())) {
			
			ProcessoAto pa = novoRequerimentoServiceFacade.buscarProcessoAtoPorProcessoEAtoAmbientalETipologia(
					pRequerimento.getProcesso(), new AtoAmbiental(atoAmbientalEnum), null);
			
			if(!Util.isNullOuVazio(pa)) {
				ControleProcessoAto cpa = novoRequerimentoService.buscarControleProcessoAtoAtualPorProcessoAto(pa.getIdeProcessoAto());
				
				if(!Util.isNullOuVazio(cpa)) {
					return cpa.getIdeStatusProcessoAto().getIdeStatusProcessoAto().equals(StatusProcessoAtoEnum.EMITIDO.getId());
				}
			}
		}
		
		return false;
	}

	public PagamentoReposicaoFlorestal getPagamentoReposicaoFlorestal() {
		return pagamentoReposicaoFlorestal;
	}

	public void setPagamentoReposicaoFlorestal(
			PagamentoReposicaoFlorestal pagamentoReposicaoFlorestal) {
		this.pagamentoReposicaoFlorestal = pagamentoReposicaoFlorestal;
	}

	public List<PagamentoReposicaoFlorestal> getPagamentoReposicaoFlorestalList() {
		return pagamentoReposicaoFlorestalList;
	}

	public void setPagamentoReposicaoFlorestalList(
			List<PagamentoReposicaoFlorestal> pagamentoReposicaoFlorestalList) {
		this.pagamentoReposicaoFlorestalList = pagamentoReposicaoFlorestalList;
	}	


	/**<p>Método verifica se houve alteração 
	 * de respostas nas abas pelo requerente</p>
	 * 
	 * */
	public void alteracaoResposta() {
		if(!ContextoUtil.getContexto().getUsuarioLogado().getIndTipoUsuario()) {
			SessaoUtil.adicionarObjetoSessao("isRespostaChange",true);
			
		}
		
	}
	
	public void changePerguntaNR_A3_P11(ValueChangeEvent event) {
		this.getAbaOutorgaController().getPerguntaNR_A4_P1().setIndResposta(false);
		Boolean resposta = (Boolean) event.getNewValue();
		alteracaoResposta();
		if(resposta) {
			this.getAbaRenovacaoAlteracaoProrrogacaoController().valueChangeDesmarcarTodasPerguntas(event);
			this.getAbaRenovacaoAlteracaoProrrogacaoController().getPerguntaNR_A2_P5().setIndResposta(true);
			this.getAbaOutorgaController().getPerguntaNR_A4_P10().setIndResposta(null);
			this.getAbaOutorgaController().getPerguntaNR_A4_P11().setIndResposta(null);
			this.getAbaOutorgaController().getPerguntaNR_A4_P12().setIndResposta(null);
			this.getAbaOutorgaController().getPerguntaNR_A4_P13().setIndResposta(null);
			this.getAbaOutorgaController().getPerguntaNR_A4_P14().setIndResposta(null);
			this.getAbaOutorgaController().getPerguntaNR_A4_P15().setIndResposta(null);
			this.getAbaOutorgaController().getPerguntaNR_A4_P1().setIndResposta(false);
			this.getAbaFaunaController().getPerguntaNR_A6_P1().setIndResposta(false);
			this.getAbaFlorestalController().getPerguntaNR_A5_P1().setIndResposta(false);
			
			this.getAbaRenovacaoAlteracaoProrrogacaoController().salvarAba();
			this.getAbaOutorgaController().salvarAba();
			this.getAbaFaunaController().salvarAba();
			this.getAbaFlorestalController().salvarAba();

			RequestContext.getCurrentInstance().addPartialUpdateTarget("subViewAbas:tabAbasId");
		}
	}
	

	public boolean isEnquadramento() {
		return isEnquadramento;
	}

	public void setEnquadramento(boolean isEnquadramento) {
		this.isEnquadramento = isEnquadramento;
	}

	public boolean isConsultaRealizada() {
		return consultaRealizada;
	}

	public void setConsultaRealizada(boolean consultaRealizada) {
		this.consultaRealizada = consultaRealizada;
	}

	public boolean isTipoDeImpressaoPdf() {
		return tipoDeImpressaoPdf;
	}

	public void setTipoDeImpressaoPdf(boolean tipoDeImpressaoPdf) {
		this.tipoDeImpressaoPdf = tipoDeImpressaoPdf;
	}
	
	
}