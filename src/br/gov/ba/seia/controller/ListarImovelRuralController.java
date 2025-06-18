package br.gov.ba.seia.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.management.RuntimeErrorException;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

import com.vividsolutions.jts.geom.Point;

import br.gov.ba.seia.dao.ImovelRuralDAOImpl;
import br.gov.ba.seia.dto.ImovelRuralDTO;
import br.gov.ba.seia.entity.Arquivo;
import br.gov.ba.seia.entity.AssentadoIncraImovelRural;
import br.gov.ba.seia.entity.Bairro;
import br.gov.ba.seia.entity.ClassificacaoSecaoGeometrica;
import br.gov.ba.seia.entity.CoordenadaGeografica;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.DocumentoImovelRural;
import br.gov.ba.seia.entity.Endereco;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Logradouro;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaImovel;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.RepresentanteLegal;
import br.gov.ba.seia.entity.ReservaLegal;
import br.gov.ba.seia.entity.StatusReservaLegal;
import br.gov.ba.seia.entity.TipoLogradouro;
import br.gov.ba.seia.entity.TipoRequerimento;
import br.gov.ba.seia.entity.TipoVinculoImovel;
import br.gov.ba.seia.entity.auditoria.FiltroAuditoria;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.ConfigEnum;
import br.gov.ba.seia.enumerator.TipoArlEnum;
import br.gov.ba.seia.enumerator.TipoCertificadoEnum;
import br.gov.ba.seia.enumerator.TipoPessoaEnum;
import br.gov.ba.seia.enumerator.TipoRequerimentoEnum;
import br.gov.ba.seia.enumerator.URLEnum;
import br.gov.ba.seia.exception.CampoObrigatorioException;
import br.gov.ba.seia.facade.EnderecoFacade;
import br.gov.ba.seia.facade.ImovelRuralFacade;
import br.gov.ba.seia.facade.PessoaFacade;
import br.gov.ba.seia.facade.auditoria.AuditoriaFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AppService;
import br.gov.ba.seia.service.AreaProdutivaService;
import br.gov.ba.seia.service.ArquivoService;
import br.gov.ba.seia.service.ClassificacaoSecaoGeometricaService;
import br.gov.ba.seia.service.CronogramaEtapaService;
import br.gov.ba.seia.service.CronogramaRecuperacaoService;
import br.gov.ba.seia.service.DatumService;
import br.gov.ba.seia.service.DocumentoImovelRuralService;
import br.gov.ba.seia.service.EnderecoPessoaService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.ImovelRuralService;
import br.gov.ba.seia.service.ImovelRuralUsoAguaIntervencaoService;
import br.gov.ba.seia.service.ImovelRuralUsoAguaService;
import br.gov.ba.seia.service.ImovelService;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.service.ManejoService;
import br.gov.ba.seia.service.MetodoIrrigacaoService;
import br.gov.ba.seia.service.ModuloFiscalService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.ParamPersistDadoGeoService;
import br.gov.ba.seia.service.PessoaFisicaService;
import br.gov.ba.seia.service.PessoaJuridicaService;
import br.gov.ba.seia.service.ProcessoUsoAguaService;
import br.gov.ba.seia.service.RepresentanteLegalService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.ReservaLegalService;
import br.gov.ba.seia.service.ResponsavelImovelRuralService;
import br.gov.ba.seia.service.SupressaoVegetacaoService;
import br.gov.ba.seia.service.TipoAppService;
import br.gov.ba.seia.service.TipoArlService;
import br.gov.ba.seia.service.TipoEstadoConservacaoService;
import br.gov.ba.seia.service.TipoIntervencaoService;
import br.gov.ba.seia.service.TipoOrigemCertificadoService;
import br.gov.ba.seia.service.TipoRecuperacaoService;
import br.gov.ba.seia.service.TipoVinculoImovelService;
import br.gov.ba.seia.service.TipologiaService;
import br.gov.ba.seia.service.VegetacaoNativaService;
import br.gov.ba.seia.service.VerticeService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.GeoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.PostgisUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.auditoria.ZipFiles;

@Named("listarImovelRuralController")
@ViewScoped
public class ListarImovelRuralController extends SeiaControllerAb {
	
	//Início Ponto Pesquisa
	private LocalizacaoGeografica localizacaoGeograficaSelecionada = new LocalizacaoGeografica();

	private String selectedModoCoordenada = "1";

	private String grausLatitudeLoc;

	private String minutosLatitudeLoc;

	private String segundosLatitudeLoc;

	private String grausLongitudeLoc;

	private String minutosLongitudeLoc;

	private String segundosLongitudeLoc;

	private String fracaoGrauLatitudeLoc;

	private String fracaoGrauLongitudeLoc;

	private DadoGeografico vertice;
	
	private boolean habilitaDownloadAvisoCarLote;
	
	private List<ClassificacaoSecaoGeometrica> listaSecaoGeomerica;

	private boolean usuarioVinculadoProjetoBndes;
	private Pessoa procurador=null; 

	//Fim Ponto Pesquisa
	
	private Boolean showPessoaJuridica;
	private Boolean showProprioRequerente;
	private Boolean showProcuradorPessoaFisica;
	private Boolean showProcuradorPessoaJuridica;
	
	private String nomeImovel = null ;
	private String numeroMatricula =null;
	private String descNome = null;
	private String nomeResponsavelTecnico =null;
	private String nomeProcurador = null;
	private String certificado = null;
	private String cefirOuTermo = "cefir";
	private String token = null;
	private Municipio municipio = null;
	private StatusReservaLegal statusRLFiltro = null;
	private Boolean bloqueioAsvFiltro = null;
	private List<LocalizacaoGeografica> listaPontoPesquisa = null;
	private List<Municipio> listaMunicipios = null;
	private List<StatusReservaLegal> listaStatusRL = null;
	private LazyDataModel<Imovel> listaImoveis = null;
	private Pessoa requerente = null;
	private String documento = null;
	private String lblNome = null;
	private String lblDoc = null;
	private String tipoPessoa = null;
	private String mascara = null;
	private String nome; //Nome ou Razao
	private List<SelectItem> itens;
	private boolean renderizaJuridica;
	private boolean renderizaFisica;
	private LazyDataModel<PessoaFisica> pessoasFisicaModel;
	private LazyDataModel<PessoaJuridica> pessoasJuridicaModel;
	private Pessoa solicitante = null;
	private PessoaFisica usuarioLogado = null;
	private TipoRequerimento tipoRequerimento = new TipoRequerimento();
	private boolean rendererPoll;
	private boolean iniciarConsulta;
	private Boolean imovelCDA = null;
	private Boolean imovelBndes = null;
	private Boolean isImovelRegistroIncompleto = false;
//	private boolean executarValidacaoRegistroIncompleto = false;
	private Pessoa responsavelTecnico = null;	
	private boolean editMode = false;
	private boolean adicionaProprietario;
	
	//	Imovel
	private ImovelRural imovelRural = new ImovelRural();
	private ImovelRural imovelSelecionado;
	private Imovel imovelExcluir;
	private Imovel inativarImovel;
	private boolean temImovelSelecionado;
	private Boolean visualizandoImovel = false;
	private Boolean imprimindoImovel = false;
	private Boolean disableImovelRural = false;
	private	ImovelRuralDTO dadosParaBusca = new ImovelRuralDTO();
	private String imoveisAValidar; 
	private boolean habilitaEndereco = false;
	private boolean habilitaLocalizacao = false;
	private boolean habilitaCadastroAmbiental = false;
	private List<Arquivo> arquivosAValidar;
	private String nomContato;
	private String numTelefone;
	private String desEmail;
	private Endereco endereco;
	private Logradouro logradouro;
	private Bairro bairro;
	private Estado estado;
	private TipoLogradouro tipoLogradouro;	
	
	private String caminhoDesenharGeoBahia = URLEnum.CAMINHO_GEOBAHIA.toString();
	private String caminhoArquivo;
	
	private boolean telaCpf;
	private boolean telaCnpj;
	private Pessoa pessoaPersist;
	private boolean editModeProprietario;
	private boolean enableBotaoPesquisa;
	private PessoaFisica pessoaFisicaPersist;
	private PessoaJuridica pessoaJuridicaPersist;
	private PessoaJuridica pessoaJuridica;
	private UIComponent formularioASerLimpo;
	private boolean enableFormPessoaJuridica;
	private boolean enableFormPessoaFisica;
	private PessoaImovel pessoaiMovel;
	private Boolean isProprietarioImovel = null;
	private Boolean isProcPfPjRepLegal = false;
	
	private String urlVoltar;
	private String numSicar;
	private FiltroAuditoria filtroAuditoria;
	private List<FiltroAuditoria> listAuditoria;
	private String msgCadastroConcluido;
	private String tipoCadastro;

	private MetodoUtil metodoSelecionarSolicitante;
	private MetodoUtil metodoSelecionarResponsavelTecnico;	
	private MetodoUtil metodoSelecionarProcurador;
	
	@EJB
	ImovelService imovelService;
		
	@EJB
	ImovelRuralService imovelRuralService;
	
	@EJB
	RepresentanteLegalService representanteLegalService;
	
	@EJB
	ImovelRuralFacade imovelRuralServiceFacade;
	
	@EJB
	PessoaFisicaService pessoaFisicaService;
	
	@EJB
	PessoaJuridicaService pessoaJuridicaService;
	
	@EJB
	MunicipioService municipioService;
	
	@EJB
	EnderecoPessoaService enderecoPessoaService;
	
	@EJB
	TipoVinculoImovelService tipoVinculoImovelService;
	
	@EJB
	TipoArlService tipoArlService;
	
	@EJB
	PessoaFacade pessoaFacade;
	
	@EJB
	ArquivoService arquivoService;
	
	@EJB
	RequerimentoService requerimentoService;
	
	@EJB
	TipoOrigemCertificadoService tipoOrigemCertificadoService;
	
	@EJB
	DatumService serviceDatum;
	
	@EJB
	DocumentoImovelRuralService documentoObrigatorioService;
	
	@EJB
	private RepresentanteLegalService repreLegalService;
	
	@EJB
	private EnderecoFacade enderecoService;
	
	@EJB
	private SupressaoVegetacaoService supressaoVegetacaoService;
	
	@EJB
	private ModuloFiscalService moduloFiscalService;
	
	@EJB
	private ReservaLegalService reservaLegalService;
	
	@EJB
	private AppService appService;
	
	@EJB
	private AreaProdutivaService areaProdutivaService;
	
	@EJB
	private VegetacaoNativaService vegetacaoNativaService;
	
	@EJB
	private TipologiaService tipologiaService;
	
	@EJB
	private MetodoIrrigacaoService metodoIrrigacaoService;
	
	@EJB
	private ManejoService manejoService;
	
	@EJB
	private ClassificacaoSecaoGeometricaService secaoGeometricaService;
	
	@EJB
	private ParamPersistDadoGeoService paramPersistDadoGeoService;
	
	@EJB
	private TipoEstadoConservacaoService tipoEstadoConservacaoService;
	
	@EJB
	private TipoAppService tipoAppService;
	
	@EJB
	private TipoRecuperacaoService tipoRecuperacaoService;
	
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	
	@EJB
	private CronogramaEtapaService cronogramaEtapaService;
	
	@EJB
	private CronogramaRecuperacaoService cronogramaRecuperacaoService;
	
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	
	@EJB
	private ResponsavelImovelRuralService responsavelImovelRuralService;
	
	@EJB
	ImovelRuralUsoAguaService imovelRuralUsoAguaService;
	
	@EJB
	ProcessoUsoAguaService processoUsoAguaService;
	
	@EJB
	TipoIntervencaoService tipoIntervencaoService;
	
	@EJB
	ImovelRuralUsoAguaIntervencaoService imovelRuralUsoAguaIntervencaoService;
	
	@EJB
	VerticeService verticeService;

	@EJB
	private AuditoriaFacade auditoriaFacade;
	
	
		@PostConstruct
	public void init() {
		try {
			
			metodoSelecionarSolicitante = new MetodoUtil(this, this.getClass().getDeclaredMethod("receberSolicitante", Pessoa.class));
			metodoSelecionarResponsavelTecnico = new MetodoUtil(this, this.getClass().getDeclaredMethod("receberResponsavelTecnico", Pessoa.class));
			metodoSelecionarProcurador = new MetodoUtil(this, this.getClass().getDeclaredMethod("receberProcurador", Pessoa.class));

			
			
			usuarioVinculadoProjetoBndes = imovelRuralServiceFacade.isUsuarioVinculadoProjetoBndes(ContextoUtil.getContexto().getUsuarioLogado());
	
			if(!Util.isNullOuVazio(ContextoUtil.getContexto()) && !Util.isNullOuVazio(ContextoUtil.getContexto().getUsuarioLogado())) {
				if(Util.isNullOuVazio(ContextoUtil.getContexto().getIsProcPfPjOuRepLegal()))
					isProcPfPjRepLegal = false;
				else
					isProcPfPjRepLegal = ContextoUtil.getContexto().getIsProcPfPjOuRepLegal();
				
				imoveisAValidar = null;		
				this.requerente = new Pessoa();	
				descNome = new String();
				municipio = new Municipio();
				statusRLFiltro = new StatusReservaLegal();
				bloqueioAsvFiltro = null;
				imovelCDA = null;
				imovelBndes = null;
				listaMunicipios = new ArrayList<Municipio>();
				listaStatusRL = new ArrayList<StatusReservaLegal>();
				listaPontoPesquisa = new ArrayList<LocalizacaoGeografica>();		
				carregarListaMunicipiosBahia();
				carregarListaStatusRL();
				nomeImovel = new String();
				montarProprietarioImovel();
				usuarioLogado = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica();
				numSicar = "";
				
				final Object temp = ContextoUtil.getContexto().getObject();
				if (temp != null && temp instanceof ImovelRuralDTO) {
					urlVoltar = ((ImovelRuralDTO) temp).getUrlVoltar();
				} else urlVoltar = "/paginas/manter-imoveis/cefir/listaImoveisRural.xhtml";
				
				
				verificaSeRequerenteEUsuarioExterno();
				
				if(ContextoUtil.getContexto().getUsuarioLogado().isUsuarioExterno()){
					this.requerente.setIdePessoa(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());//lembrado que o id de pessoa fisica eh o mesmo de pessoa.
					this.requerente.setPessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica());
				}else if (!Util.isNullOuVazio(ContextoUtil.getContexto().getRequerentePesquisa()) && ContextoUtil.getContexto().getImprimindoImovel()) {
					this.requerente.setIdePessoa(ContextoUtil.getContexto().getRequerentePesquisa().getIdePessoa());//lembrado que o id de pessoa fisica eh o mesmo de pessoa.
					this.requerente.setPessoaFisica(ContextoUtil.getContexto().getRequerentePesquisa().getPessoaFisica());
				}
				
				if(!Util.isNullOuVazio(ContextoUtil.getContexto().getListaPontoPesquisa())){
					this.listaPontoPesquisa = ContextoUtil.getContexto().getListaPontoPesquisa();
				}
				if (!Util.isNullOuVazio(ContextoUtil.getContexto().getTipoRequerimento())) {
					tipoRequerimento = ContextoUtil.getContexto().getTipoRequerimento();
				}else{
					tipoRequerimento = new TipoRequerimento(TipoRequerimentoEnum.REGULACAO_AMBIENTAL_DO_EMPREENDIMENTO.getIde(),
															TipoRequerimentoEnum.REGULACAO_AMBIENTAL_DO_EMPREENDIMENTO.getNomeTipoRequerimento());
				}

				//ativa o pollAjax responsável por mostrar a mensagem apos finalizar o cadastro do imóvel
				if (ContextoUtil.getContexto() != null && ContextoUtil.getContexto().getObject() != null && ContextoUtil.getContexto().getObject() instanceof String) {
					rendererPoll = true;
				} 
	
				setFiltroAuditoria(new FiltroAuditoria());
				
				if(ContextoUtil.getContexto().getExisteImovelCadastroPendente()){
					this.imoveisAValidar = "RegPendente";	
					consultarReqImoveis();
					ContextoUtil.getContexto().setExisteImovelCadastroPendente(false);
				} else {
					this.imoveisAValidar = null;
				}
				
				if(!Util.isNullOuVazio(ContextoUtil.getContexto().getImoveisDuplicados())){			
					consultarReqImoveis();			
				}
			}
		}catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
	}

	public void limparObjetosPontoPesquisa() {
		grausLatitudeLoc = "";
		minutosLatitudeLoc = "";
		segundosLatitudeLoc = "";
		grausLongitudeLoc = "";
		minutosLongitudeLoc = "";
		segundosLongitudeLoc = "";
		fracaoGrauLatitudeLoc = "";
		fracaoGrauLongitudeLoc = "";
		selectedModoCoordenada = "1";		
		localizacaoGeograficaSelecionada = new LocalizacaoGeografica();		
	}
	
	public List<LocalizacaoGeografica> getListaPontoPesquisa() {
		return listaPontoPesquisa;
	}
	
	public void setListaPontoPesquisa(List<LocalizacaoGeografica> listaPontoPesquisa) {
		this.listaPontoPesquisa = listaPontoPesquisa;
	}
	
	public String inserirPontoPesquisa(){
		List<String> messages = new ArrayList<String>();
		
		validarCamposPontoPesquisa(messages);
				
		if (!messages.isEmpty()) {
			JsfUtil.addErrorMessages(messages);
			return null;
		}
		try{
			this.localizacaoGeograficaSelecionada.setIdeSistemaCoordenada(serviceDatum.carregar(4));
			localizacaoGeograficaSelecionada.setIdeClassificacaoSecao(new ClassificacaoSecaoGeometrica(1));
			Point ponto = PostgisUtil.criarPonto(Double.parseDouble(getFracaoGrauLatitudeLoc().replace(',', '.')), Double.parseDouble(getFracaoGrauLongitudeLoc().replace(',', '.')));
			vertice = new DadoGeografico();
			vertice.setCoordGeoNumerica(ponto.toString());
			localizacaoGeograficaSelecionada.setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
			localizacaoGeograficaSelecionada.getDadoGeograficoCollection().add(vertice);
			listaPontoPesquisa.add(localizacaoGeograficaSelecionada);
			//FECHAR A MODAL
			RequestContext context = RequestContext.getCurrentInstance();
			context.addCallbackParam("closeModal", true);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		return null;
	}
	
	public String excluirPontoPesquisa(LocalizacaoGeografica pontoPesquisa){
		if(!Util.isNullOuVazio(listaPontoPesquisa)){
			listaPontoPesquisa.remove(pontoPesquisa);
		}
		return null;
	}
	
	public String obterLatitudePontoPesquisa(LocalizacaoGeografica localizacaoGeografica) {
		
		CoordenadaGeografica coordenadaGeografica = GeoUtil.converterPointParaCoordenadaGeografica(localizacaoGeografica.getDadoGeograficoCollection().iterator().next().getCoordGeoNumerica());
		return coordenadaGeografica.getLatitude().getAsGD()+ "S ";
	}
	
	public String obterLongitudePontoPesquisa(LocalizacaoGeografica localizacaoGeografica) {
		CoordenadaGeografica coordenadaGeografica = GeoUtil.converterPointParaCoordenadaGeografica(localizacaoGeografica.getDadoGeograficoCollection().iterator().next().getCoordGeoNumerica());
		return coordenadaGeografica.getLongitude().getAsGD()+ "W ";
	}
	
	public String obterLatLongPontoPesquisa(LocalizacaoGeografica localizacaoGeografica) {
		CoordenadaGeografica coordenadaGeografica = GeoUtil.converterPointParaCoordenadaGeografica(localizacaoGeografica.getDadoGeograficoCollection().iterator().next().getCoordGeoNumerica());
		return coordenadaGeografica.getLatitude().getAsGD()+" "+coordenadaGeografica.getLongitude().getAsGD();
	}
	
	public void validarCamposPontoPesquisa(List<String> messages) {		
		if (Util.isNullOuVazio(fracaoGrauLatitudeLoc)) {
			messages.add("O campo 'Latitude'é de preenchimento obrigatório.");
		}
		if (Util.isNullOuVazio(fracaoGrauLongitudeLoc)) {
			messages.add("O campo 'Longitude' é de preenchimento obrigatório.");
		}
	}
	
	private String montarStringComPosntosParaPesquisa() {
		String pontos = "";
		
		if (!Util.isNullOuVazio(listaPontoPesquisa)){
			pontos = "[";
			for (LocalizacaoGeografica loc  : listaPontoPesquisa){
				CoordenadaGeografica coordenadaGeografica = GeoUtil.converterPointParaCoordenadaGeografica(loc.getDadoGeograficoCollection().iterator().next().getCoordGeoNumerica());
				pontos+= coordenadaGeografica.getLatitude().getAsGD()+";"+coordenadaGeografica.getLongitude().getAsGD()+";"+ loc.getIdeSistemaCoordenada().getSrid() + "',";
			}		
			pontos = pontos.substring(0, pontos.length() - 1) + "]";
		}
		
		return pontos;
	}
	
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
	
	public LocalizacaoGeografica getLocalizacaoGeograficaSelecionada() {
		return localizacaoGeograficaSelecionada;
	}

	public void setLocalizacaoGeograficaSelecionada(LocalizacaoGeografica localizacaoGeograficaSelecionada) {
		this.localizacaoGeograficaSelecionada = localizacaoGeograficaSelecionada;
	}

	public String getSelectedModoCoordenada() {
		return selectedModoCoordenada;
	}

	public void setSelectedModoCoordenada(String selectedModoCoordenada) {
		this.selectedModoCoordenada = selectedModoCoordenada;
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

	public void receberSolicitante(Pessoa pessoa){
		requerente = pessoa;
		if(!Util.isNull(pessoa)){
			if(pessoa.isPF()){
				requerente.setIdePessoa(pessoa.getPessoaFisica().getIdePessoaFisica());
				requerente.setPessoaFisica(pessoa.getPessoaFisica());
				descNome = pessoa.getPessoaFisica().getNomPessoa();
			} 
			else {
				requerente.setIdePessoa(pessoa.getPessoaJuridica().getIdePessoaJuridica());
				requerente.setPessoaJuridica(pessoa.getPessoaJuridica());
				descNome = pessoa.getPessoaJuridica().getNomRazaoSocial();
			}		
			Html.atualizar("filtroImoveis:tblFiltroRequerente");
		}
	}	
	
	public void receberResponsavelTecnico(Pessoa pessoa){
		responsavelTecnico = pessoa;
		if(!Util.isNull(pessoa)){
			responsavelTecnico.setIdePessoa(pessoa.getPessoaFisica().getIdePessoaFisica());
			responsavelTecnico.setPessoaFisica(pessoa.getPessoaFisica());
			nomeResponsavelTecnico = pessoa.getPessoaFisica().getNomPessoa();
		
			Html.atualizar("filtroImoveis:tblFiltroProcuradorResponsavel");
		}
	}	
	
	public void receberProcurador(Pessoa pessoa){
		procurador = pessoa;
		if(!Util.isNull(pessoa)){
			procurador.setIdePessoa(pessoa.getPessoaFisica().getIdePessoaFisica());
			procurador.setPessoaFisica(pessoa.getPessoaFisica());
			nomeProcurador = pessoa.getPessoaFisica().getNomPessoa();
			Html.atualizar("filtroImoveis:tblFiltroProcuradorResponsavel");
		}
	}	
	
	private void verificaSeRequerenteEUsuarioExterno() {
		if(ContextoUtil.getContexto().getUsuarioLogado().isUsuarioExterno()){
			this.requerente.setIdePessoa(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());//lembrado que o id de pessoa fisica eh o mesmo de pessoa.
			this.requerente.setPessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica());
		}else if (!Util.isNullOuVazio(ContextoUtil.getContexto().getRequerentePesquisa()) && ContextoUtil.getContexto().getImprimindoImovel()) {
			this.requerente.setIdePessoa(ContextoUtil.getContexto().getRequerentePesquisa().getIdePessoa());//lembrado que o id de pessoa fisica eh o mesmo de pessoa.
			this.requerente.setPessoaFisica(ContextoUtil.getContexto().getRequerentePesquisa().getPessoaFisica());
		}
	}
	
	/**
	 * CONTROLE PARA HABILITAÇÃO DOS CAMPOS DE IMOVEL RURAL
	 */
	public void montarProprietarioImovel() {
		editModeProprietario = false;
		this.telaCpf = Boolean.TRUE;
		this.telaCnpj = Boolean.FALSE;
		pessoaPersist = new Pessoa();
		pessoaFisicaPersist = new PessoaFisica();
		pessoaJuridica = new PessoaJuridica();
		pessoaJuridicaPersist = new PessoaJuridica();
		adicionaProprietario = false;
	}
	
	protected void controleDeCampos() {
		this.visualizandoImovel = ContextoUtil.getContexto().getVisualizandoImovel();
		if(!Util.isNullOuVazio(this.visualizandoImovel) && this.visualizandoImovel){
			this.disableImovelRural = true;
		}else{
			this.disableImovelRural = false;
		}
	}
	
	public Boolean getIsProcPfPjRepLegal() {
		return isProcPfPjRepLegal;
	}
	
	public void setIsProcPfPjRepLegal(Boolean isProcPfPjRepLegal) {
		this.isProcPfPjRepLegal = isProcPfPjRepLegal;
	}
	
	
	
	public Boolean getTemImovelMaiorQuatroModulos() {
		if(!Util.isNullOuVazio(this.imovelRural.getIdeImovelRural()) && !imovelRural.isMenorQueQuatroModulosFiscais()){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * ESTA SENDO USADO PELO CEFIR PARA CARREGAR A PAGINA DE LISTAR IMOVEIS RURAIS.
	 * @author MICAEL 
	 * @return
	 */
	public String carregarPagListarImoveisRurais() {
		ContextoUtil.getContexto().setTelaParaRedirecionar(null);
		ContextoUtil.getContexto().setIsProcPfPjOuRepLegal(null);
		ContextoUtil.getContexto().setImovelRural(null);
		ContextoUtil.getContexto().setLabelTitutoRequerimento("Cadastro de Imóvel Rural");		
		ContextoUtil.getContexto().setExisteImovelCadastroPendente(false);
		ContextoUtil.getContexto().setExecutarValidacaoRegistroIncompleto(true); 
		return "/paginas/manter-imoveis/cefir/listaImoveisRural.xhtml";
	}
	
	public void validarExistenciaImoveisCadastroPendente() {
		try {
			if(imovelRuralServiceFacade.qtdImoveisPendentesPorRequerente(this.requerente.getIdePessoa()) > 0){
				RequestContext context = RequestContext.getCurrentInstance();
				context.addCallbackParam("existeImoveisPendentes", true);
			} else {
				RequestContext context = RequestContext.getCurrentInstance();				
				context.addCallbackParam("existeImoveisPendentes", false);
			}
		} catch (Exception e) {
			carregarPagListarImoveisRurais();
		}
	}

	public String montarFiltrosPesquisaImoveisPendentes() {
		ContextoUtil.getContexto().setIsProcPfPjOuRepLegal(null);
		ContextoUtil.getContexto().setImovelRural(null);
		ContextoUtil.getContexto().setLabelTitutoRequerimento("Cadastro de Imóvel Rural");
		ContextoUtil.getContexto().setExisteImovelCadastroPendente(true);
		return "/paginas/manter-imoveis/cefir/listaImoveisRural.xhtml";
	}

	public String incluirReqImovel() {
		ContextoUtil.getContexto().setImovelRural(null);
		ContextoUtil.getContexto().setTipoRequerimento(new TipoRequerimento(4, "Requerimento de Cadastro de Imóvel Rural"));
		ContextoUtil.getContexto().setDisableUploadDoc(true);
		ContextoUtil.getContexto().setLabelUpload(null);
		ContextoUtil.getContexto().setArquivoEmEdicao(null);
		ContextoUtil.getContexto().setProcuracaoEmEdicao(null);
		ContextoUtil.getContexto().setIsProcPfPjOuRepLegal(null);
		
		return "/paginas/manter-tipo-cadastro/tipo-cadastro.xhtml";

	}
	
	public String redirecionarTipoCadastroPCT(){
		
		ContextoUtil.getContexto().setTipoSolicitante(5);
		ContextoUtil.getContexto().setPCT(true);
		
		
		return "/paginas/identificar-papel/identificar-papel.xhtml";
	}
	
	public String redirecionarTipoCadastroImovelRuralAssentamento(){
		
		ContextoUtil.getContexto().setTipoSolicitante(1);
		ContextoUtil.getContexto().setPCT(false);
		
		return "/paginas/identificar-papel/identificar-papel.xhtml";
	}
	
	public Boolean getIsAtend() {
		if (usuarioLogado.getUsuario().getIdePerfil().getIdePerfil().intValue() == 5) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
	
	public void carregarListaMunicipiosBahia() {
		try {
			this.listaMunicipios = (List<Municipio>) municipioService.filtrarListaMunicipiosPorEstado(new Estado(5));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void carregarListaStatusRL() {
		try {
			this.listaStatusRL = (List<StatusReservaLegal>) imovelRuralServiceFacade.listarStatusReservaLegal();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void carregarTelaValidarArquivos(ImovelRural imovelRural) {
		try {
			if(!Util.isNullOuVazio(imovelRural) && !Util.isNullOuVazio(imovelRural.getIdeImovelRural())){
				this.imovelRural = imovelRural;
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void listarArquivosAValidarDoImovel() {
		try {
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void consultarReqImoveis() {
		try {
			if(Util.isNullOuVazio(ContextoUtil.getContexto().getImoveisDuplicados())){
				validarCamposObrigatoriosBusca();
			}
			carregarLazyModelReqImoveis();
		} catch (CampoObrigatorioException e) {
			JsfUtil.addErrorMessage(e.getMessage());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	/**Verificar se existe imoveis com cadastro Incompleto
	 * 
	 * */
	public void verificarImovelCadastroIncompleto() {
		
		if(ContextoUtil.getContexto().isExecutarValidacaoRegistroIncompleto()
				&& ContextoUtil.getContexto().isUsuarioExterno() && !getIsImovelRegistroIncompleto()) {	
			validarDadosBusca();
			Integer result = imovelRuralService.countImoveisCadastroIncompleto(dadosParaBusca);		
			if(result>0) {
				setIsImovelRegistroIncompleto(true);
				imoveisAValidar = "RegPendente";
				Html.exibir("dialogImovelRegistroIncompletoRequerente");
			}else {
				return ;
			}
			Html.atualizar("filtroImoveis:dataTableReqImovel"); 
			carregarLazyModelReqImoveis();
			
		}
		ContextoUtil.getContexto().setExecutarValidacaoRegistroIncompleto(false);
	}
	
	public void disableAviso() {
		setIsImovelRegistroIncompleto(false);
	}
	
	/**
	 * Método responsável por validar que ao menos um filtro seja selecionado na consulta de imóveis rurais
	 * @return void
	 * @throws CampoObrigatorioException 
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 14/10/2015
	*/
	private void validarCamposObrigatoriosBusca() throws CampoObrigatorioException  {
		StringBuilder s = new StringBuilder();
		s.append(this.certificado);
		s.append(this.token);
		s.append(this.imoveisAValidar);
		s.append(this.requerente);
		s.append(this.municipio);
		s.append(this.nomeImovel);
		s.append(this.statusRLFiltro);
		s.append(this.bloqueioAsvFiltro);
		s.append(this.imovelCDA);
		s.append(this.imovelBndes);
		s.append(this.listaPontoPesquisa);
		s.append(this.numSicar);
		s.append(this.numeroMatricula);
		s.append(this.responsavelTecnico);
		s.append(this.procurador);
//		s.append(this.tipoCadastro);
		final String campos = s.toString().replaceAll("false", "").replaceAll("null", "").replaceAll("\\[", "").replaceAll("\\]", "");
		if(campos.isEmpty()) {
			throw new CampoObrigatorioException(Util.getString("cefir_msg_A010"));
		} else if (Util.isNullOuVazio(this.municipio) && getSeUsuarioExterno()) { // solicitação de RN por meio do Redmine 36178
			throw new CampoObrigatorioException(Util.getString("cefir_msg_A001", "\"Localidade\""));
	    }
	}
		
	/**
	 * Método responsável por limpar os filtros da consulta de imóveis rurais
	 * @return void
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 14/10/2015
	*/
	public void limparFormConsulta(){
		nomeImovel = null;
		municipio = new Municipio();
		statusRLFiltro = new StatusReservaLegal();
		bloqueioAsvFiltro = null;
		imovelCDA = null;
		imovelBndes = null;
		listaPontoPesquisa = new ArrayList<LocalizacaoGeografica>();
		descNome = null;
		certificado = null;
		token = null;
		imoveisAValidar = "";
		this.listaImoveis = null;
		habilitaDownloadAvisoCarLote = false;
		numSicar="";
		tipoCadastro = "Rural";
		nomeResponsavelTecnico =null;
		responsavelTecnico=null;
		numeroMatricula=null;
		nomeProcurador=null;
		procurador=null;
		
		
		verificaSeRequerenteEUsuarioExterno();
		
		ContextoUtil.getContexto().setRequerentePesquisa(null);
		ContextoUtil.getContexto().setListaPontoPesquisa(null);
		
		this.requerente = new Pessoa();
		if(ContextoUtil.getContexto().getUsuarioLogado().isUsuarioExterno()){
			this.requerente.setIdePessoa(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());
			this.requerente.setPessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica());
		}
	}
	
	public void excluirImovel() {
		try {
			Imovel objAntigo = imovelService.buscarImovelPorIde(imovelExcluir);
			imovelExcluir = objAntigo.clone();
			imovelRuralServiceFacade.excluirImovel(imovelExcluir);
			auditoriaFacade.atualizar(objAntigo , imovelExcluir);
			auditoriaFacade.excluir(imovelExcluir);
			JsfUtil.addSuccessMessage("Exclusão efetuada com sucesso.");
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao Excluir Imóvel");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public void habilitarTela(ImovelRural imovelRural) {
		imovelSelecionado = imovelRural;
		temImovelSelecionado = true;
		habilitaEndereco = true;		
	}

	private void validarDadosBusca() {
		ContextoUtil.getContexto().setRequerentePesquisa(this.requerente);
		ContextoUtil.getContexto().setListaPontoPesquisa(this.listaPontoPesquisa);
		dadosParaBusca = new ImovelRuralDTO();
		dadosParaBusca.setMunicipio(this.municipio);
		dadosParaBusca.setStatusReservaLegal(this.statusRLFiltro);
		dadosParaBusca.setBloqueioAsv(this.bloqueioAsvFiltro);
		dadosParaBusca.setImovelCDA(this.imovelCDA);
		dadosParaBusca.setImovelBNDES(this.imovelBndes);
		dadosParaBusca.setNumSicar(this.numSicar.toUpperCase());
		dadosParaBusca.setResponsavelTecnico(this.responsavelTecnico);
		dadosParaBusca.setProcurador(this.procurador);
		dadosParaBusca.setNumeroMatricula(this.numeroMatricula);
		if (!Util.isNullOuVazio(certificado)) {
			if (this.cefirOuTermo.equals("cefir")) {
				certificado += "%/CEFIR";
			} else {
				certificado += "%/TC";
			}
		}
		dadosParaBusca.setNumCertificado(certificado);
		this.solicitante = new Pessoa();
		if (ContextoUtil.getContexto().getUsuarioLogado().isConveniado()
				&& !ContextoUtil.getContexto().getUsuarioLogado().isMP()) {
			this.solicitante.setIdePessoa(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());// lembrado
																												// que o
																												// id de
																												// pessoa
																												// fisica
																												// eh o
																												// mesmo
																												// de
																												// pessoa.
		}
		dadosParaBusca.setSolicitante(this.solicitante);
		if (ContextoUtil.getContexto().getUsuarioLogado().isUsuarioExterno()) {
			this.requerente.setIdePessoa(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());// lembrado
																												// que o
																												// id de
																												// pessoa
																												// fisica
																												// eh o
																												// mesmo
																												// de
																												// pessoa.
		}
		dadosParaBusca.setProprietarioOuProcurador(this.requerente);

		if ("Pendente".equalsIgnoreCase(imoveisAValidar)) {
			dadosParaBusca.setImoveisAValidar(null);
			dadosParaBusca.setIndSuspensao(true);
		} else {
			dadosParaBusca.setImoveisAValidar(imoveisAValidar);
			dadosParaBusca.setIndSuspensao(false);
		}

		if (!Util.isNullOuVazio(listaPontoPesquisa)) {
			dadosParaBusca.setPontosPesquisa(montarStringComPosntosParaPesquisa());
		}
		// Redmine 8381 - danilos.santos
		if (!Util.isNullOuVazio(certificado) && cefirOuTermo != null) {
			if ("cefir".equalsIgnoreCase(cefirOuTermo)) {
				dadosParaBusca.setStsCertificado(true);
			} else {
				dadosParaBusca.setStsCertificado(false);
			}
		}
	}
	/**
	 * Método responsável por carregar a lista de imóveis rurais na consulta de imóveis rurais 
	 * @return void
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 14/10/2015
	*/
	@SuppressWarnings("serial")
	public void carregarLazyModelReqImoveis() {
		ContextoUtil.getContexto().setRequerentePesquisa(this.requerente);
		ContextoUtil.getContexto().setListaPontoPesquisa(this.listaPontoPesquisa);
		dadosParaBusca = new ImovelRuralDTO();		
		dadosParaBusca.setMunicipio(this.municipio);
		dadosParaBusca.setStatusReservaLegal(this.statusRLFiltro);
		dadosParaBusca.setBloqueioAsv(this.bloqueioAsvFiltro);
		dadosParaBusca.setImovelCDA(this.imovelCDA);
		dadosParaBusca.setImovelBNDES(this.imovelBndes);
		dadosParaBusca.setNumeroMatricula(this.numeroMatricula);
		dadosParaBusca.setResponsavelTecnico(this.responsavelTecnico);
		dadosParaBusca.setNumSicar(this.numSicar.toUpperCase());
		dadosParaBusca.setTipoCadastro(this.tipoCadastro);
        dadosParaBusca.setProcurador(this.procurador);

		if (!Util.isNullOuVazio(certificado)){
			if(this.cefirOuTermo.equals("cefir")){
				certificado += "%/CEFIR"; 
			} else {
				certificado += "%/TC";
			}
		}
		dadosParaBusca.setNumCertificado(certificado);
		this.solicitante = new Pessoa();
		if(ContextoUtil.getContexto().getUsuarioLogado().isConveniado() && !ContextoUtil.getContexto().getUsuarioLogado().isMP()){
			this.solicitante.setIdePessoa(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());//lembrado que o id de pessoa fisica eh o mesmo de pessoa.
		}
		dadosParaBusca.setSolicitante(this.solicitante);
		if(ContextoUtil.getContexto().getUsuarioLogado().isUsuarioExterno()){
			this.requerente.setIdePessoa(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());//lembrado que o id de pessoa fisica eh o mesmo de pessoa.
		}
		dadosParaBusca.setProprietarioOuProcurador(this.requerente);
		
		if("Pendente".equalsIgnoreCase(imoveisAValidar)){
			dadosParaBusca.setImoveisAValidar(null);
			dadosParaBusca.setIndSuspensao(true);
		}else{
			dadosParaBusca.setImoveisAValidar(imoveisAValidar);
			dadosParaBusca.setIndSuspensao(false);
		}
		
		if (!Util.isNullOuVazio(listaPontoPesquisa)){
			dadosParaBusca.setPontosPesquisa(montarStringComPosntosParaPesquisa());
		}
		//Redmine 8381 -  danilos.santos
		if(!Util.isNullOuVazio(certificado) && cefirOuTermo != null){
			if("cefir".equalsIgnoreCase(cefirOuTermo)){
				dadosParaBusca.setStsCertificado(true);
			}else{
				dadosParaBusca.setStsCertificado(false);
			}
		}
		
		listaImoveis = new LazyDataModel<Imovel>() {

			@Override
			public List<Imovel> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
				List<Imovel> reqImoveis = null;
				try {
					setPageSize(pageSize);
					if(Util.isNullOuVazio(ContextoUtil.getContexto().getImoveisDuplicados())){
						reqImoveis = populateListImoveis(first, pageSize);
					} else {
						reqImoveis = populateListImoveisDuplicados(first, pageSize);
						ContextoUtil.getContexto().setImoveisDuplicados(null);
					}

				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					JsfUtil.addErrorMessage(e.getMessage());
				}
				return reqImoveis;
			}
			
		};

		if(Util.isNullOuVazio(ContextoUtil.getContexto().getImoveisDuplicados())){
			listaImoveis.setRowCount(getRowCountEmpreendimento());
		} else {
			listaImoveis.setRowCount(ContextoUtil.getContexto().getImoveisDuplicados().size());
		}
		
	}
	
	protected List<Imovel> populateListImoveis(int first, int pageSize) {
		List<Imovel> reqImoveis = new ArrayList<Imovel>();
		try {
			reqImoveis = imovelRuralService.listarPorCriteriaDemanda(dadosParaBusca, this.nomeImovel, first, pageSize);
			renderedBtnDownloadAvisoCarLote(reqImoveis);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
		reqImoveis = buscaProprietariosDosImoveis(reqImoveis);
		return reqImoveis;
	}

	protected List<Imovel> populateListImoveisDuplicados(int first, int pageSize) {
		List<Imovel> reqImoveis = new ArrayList<Imovel>(ContextoUtil.getContexto().getImoveisDuplicados());
		reqImoveis = buscaProprietariosDosImoveis(reqImoveis);
		renderedBtnDownloadAvisoCarLote(reqImoveis);
		return reqImoveis;
	}
	
	private void renderedBtnDownloadAvisoCarLote(List<Imovel> reqImoveis) {
		if(Util.isNullOuVazio(this.municipio) || Util.isNullOuVazio(this.imoveisAValidar)){
			habilitaDownloadAvisoCarLote = false;
			return;
		}
		
		for (Imovel im : reqImoveis) {
			if(!Util.isNull(im.getImovelRural()) && im.getImovelRural().isAvisoBndes()){
				habilitaDownloadAvisoCarLote = true;
				return;
			}
		}
		habilitaDownloadAvisoCarLote = false;
	}
	
	protected int getRowCountEmpreendimento() {
		int totalRowCount = 0;
		try {
			totalRowCount = imovelRuralService.count(dadosParaBusca, this.nomeImovel);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return totalRowCount;
	}
	
	private List<Imovel> buscaProprietariosDosImoveis(List<Imovel> reqImoveis){
		try {
			for (Imovel imovel : reqImoveis) {
				imovel.setPessoaImovelCollection(imovelRuralService.filtrarPROPRIETARIOImovel(imovel));
				if(Util.isNullOuVazio(imovel.getPessoaImovelCollection())){
					imovel.setPessoaImovelCollection(imovelRuralService.filtrarPessoasProprietariasOuNaoPorImovel(imovel));
					imovel.setProprietario(imovel.getPessoaImovelCollection().iterator().next().getIdePessoa());
					
					if(!Util.isNullOuVazio(imovel.getProprietario()) && !Util.isNullOuVazio(imovel.getProprietario().getPessoaFisica()) && 
																				!Util.isNullOuVazio(imovel.getProprietario().getPessoaFisica().getNomPessoa())){
						imovel.getProprietario().getPessoaFisica().setNomPessoa(imovel.getProprietario().getPessoaFisica().getNomPessoa()+"(Justo Possuidor)");
					}else if(!Util.isNullOuVazio(imovel.getProprietario()) && !Util.isNullOuVazio(imovel.getProprietario().getPessoaJuridica()) && 
																			!Util.isNullOuVazio(imovel.getProprietario().getPessoaJuridica().getNomRazaoSocial())){
						imovel.getProprietario().getPessoaJuridica().setNomRazaoSocial(imovel.getProprietario().getPessoaJuridica().getNomRazaoSocial()+"(Justo Possuidor PJ)");
					}
				}
				else{
					imovel.setProprietario(imovel.getPessoaImovelCollection().iterator().next().getIdePessoa());
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return reqImoveis;
	}
	
	public void changeMunicipio(ValueChangeEvent event) {
		try {
			Municipio municipioSelected = (Municipio)event.getNewValue();
			if (Util.isNullOuVazio(municipioSelected) || municipioSelected.getIdeMunicipio() == -1) {
				municipio = new Municipio();
			}else{
				this.municipio = municipioSelected;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void trocaTipoPessoa(ValueChangeEvent event) {
		//Seta o tipoPessoa selecionado, para se basear nas buscas
		 Integer vlo = Integer.parseInt((String) event.getNewValue());
		 setDocumento("");
		if (vlo.equals(TipoPessoaEnum.FISICA.getId())) {
			lblNome = "Nome";
			lblDoc = "CPF";
			tipoPessoa = TipoPessoaEnum.FISICA.toString();
			mascara = "999.999.999-99";
		} else if(vlo.equals(TipoPessoaEnum.JURIDICA.getId())){
			lblNome = "Razão Social";
			lblDoc = "CNPJ";
			tipoPessoa = TipoPessoaEnum.JURIDICA.toString();
			mascara = "99.999.999/9999-99";
		}
		setRenderizaFisica(false);
		setRenderizaJuridica(false);
	}
	
	public void consultarRequerente() {
		if (tipoPessoa.equals(TipoPessoaEnum.FISICA.getId().toString()) || tipoPessoa.equals(TipoPessoaEnum.FISICA.name())){
			consultarPessoaFisica();
			setRenderizaFisica(true);
		} else if (tipoPessoa.equals(TipoPessoaEnum.JURIDICA.getId().toString()) || tipoPessoa.equals(TipoPessoaEnum.JURIDICA.name())){
			consultarPessoaJuridica();
			setRenderizaJuridica(true);
		}
	}
	
	private void consultarPessoaFisica() {
		PessoaFisica lPessoa = new PessoaFisica();
		lPessoa.setNomPessoa(this.nome);
		lPessoa.setNumCpf(this.documento);
		carregarLazyModelPessoaFisica(lPessoa);		
	}
	
	@SuppressWarnings("serial")
	private void carregarLazyModelPessoaFisica(final PessoaFisica pessoaSelecionada) {
		pessoasFisicaModel = new LazyDataModel<PessoaFisica>() {

			@Override
			public List<PessoaFisica> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
				List<PessoaFisica> pessoasFisicas = null;
				try {
					setPageSize(pageSize);
					pessoasFisicas = populateList(pessoaSelecionada, first, pageSize);
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					JsfUtil.addErrorMessage(e.getMessage());
				}
				return pessoasFisicas;
			}
		};
		pessoasFisicaModel.setRowCount(getRowCount(pessoaSelecionada));
	}
	
	private void consultarPessoaJuridica() {
		try {
			if ((Util.isNull(nome) || Util.isEmptyString(nome)) && (Util.isNull(documento) || Util.isEmptyString(documento))) {
				carregarLazyModelPessoaJuridica(new PessoaJuridica());
			} else {
				PessoaJuridica lPessoa = new PessoaJuridica();
				lPessoa.setNomRazaoSocial(this.nome);
				lPessoa.setNumCnpj(this.documento);
				carregarLazyModelPessoaJuridica(lPessoa);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	@SuppressWarnings("serial")
	private void carregarLazyModelPessoaJuridica(final PessoaJuridica pessoaSelecionada) {
		pessoasJuridicaModel = new LazyDataModel<PessoaJuridica>() {

			@Override
			public List<PessoaJuridica> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
				List<PessoaJuridica> pessoasJuridicas = null;
				try {
					setPageSize(pageSize);
					pessoasJuridicas = populateListJuridica(pessoaSelecionada, first, pageSize);
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					JsfUtil.addErrorMessage(e.getMessage());
				}
				return pessoasJuridicas;
			}
		};
		pessoasJuridicaModel.setRowCount(getRowCountJuridica(pessoaSelecionada));
	}
	
	protected List<PessoaFisica> populateList(PessoaFisica pessoaSelecionada, int first, int pageSize) {
		List<PessoaFisica> pessoasFisicas = new ArrayList<PessoaFisica>();
		try {
			pessoasFisicas = pessoaFisicaService.listarPorCriteriaDemanda(pessoaSelecionada, first, pageSize);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return pessoasFisicas;
	}

	protected int getRowCount(PessoaFisica pessoaSelecionada) {
		int totalRowCount = 0;
		try {
			totalRowCount = pessoaFisicaService.count(pessoaSelecionada);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return totalRowCount;
	}

	protected List<PessoaJuridica> populateListJuridica(PessoaJuridica pessoaJuridicaSelecionada, int first, int pageSize) {
		List<PessoaJuridica> pessoasJuridicas = new ArrayList<PessoaJuridica>();
		try {
			pessoasJuridicas = pessoaJuridicaService.listarPorCriteriaDemanda(pessoaJuridicaSelecionada, first, pageSize);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return pessoasJuridicas;
	}
	
	/**
	 * Retorna verdade quando o Status igual a Zero, o que significa que ele precisa ser validado para poder ser listado.
	 * @param imovel
	 * @return
	 */
	public Boolean isImovelAValidar(Imovel imovel){
		
		if(!Util.isNullOuVazio(imovel.getImovelRural()) 
				&& imovel.getImovelRural().getStatusCadastro() != null 
				&& imovel.getImovelRural().isRegistroIncompleto()){
			
			return true;
		}
		
		return false;
	}

	protected int getRowCountJuridica(PessoaJuridica pessoaJuridicaSelecionada) {
		int totalRowCount = 0;
		try {
			totalRowCount = pessoaJuridicaService.count(pessoaJuridicaSelecionada);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}
		return totalRowCount;
	}
	
	public List<TipoRequerimento> getListaTiposRequerimento() {
		List<TipoRequerimento> lista = new ArrayList<TipoRequerimento>();
        for (int j = 0; j < TipoRequerimentoEnum.values().length; j++) {
        	lista.add( new TipoRequerimento(TipoRequerimentoEnum.values()[j].getIde(), TipoRequerimentoEnum.values()[j].getNomeTipoRequerimento() ) );
        }
        
        return lista;
	}
	
	private void setDocumento(String documento) {
		this.documento = documento;
	}

	public boolean getSeUsuarioExterno() {
		return ContextoUtil.getContexto().isUsuarioExterno();
	}
	
	public String getDescNome() {
		return descNome;
	}
	
	public void setDescNome(String descNome) {
		this.descNome = descNome;
	}
	
	public String getNomeImovel() {
		return nomeImovel;
	}
	public String getCertificado() {
		return certificado;
	}
	
	public void setCertificado(String certificado) {
		this.certificado = certificado;
	}
	
	public String getCefirOuTermo() {
		return cefirOuTermo;
	}
	
	public void setCefirOuTermo(String cefirOuTermo) {
		this.cefirOuTermo = cefirOuTermo;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	/**
	 * Retorna a url do questionário
	 * @return
	 */
	public String getCarregarUrlQuestionario() {
		try {
			this.tipoRequerimento = ContextoUtil.getContexto().getTipoRequerimento();// o valor desse contexto é setado no changeTipoRequerimento
			if(tipoRequerimento.getIdeTipoRequerimento().equals(TipoRequerimentoEnum.REGULACAO_AMBIENTAL_DO_EMPREENDIMENTO.getIde())){
				return "/paginas/requerimentos/formQuestionarioReqUnico.xhtml";
			}else if(tipoRequerimento.getIdeTipoRequerimento().equals(TipoRequerimentoEnum.REQUERIMENTO_ATO_DECLARATORIO.getIde())){
				return "/paginas/requerimentos/formQuestionarioReqUnico.xhtml";
			}else if(tipoRequerimento.getIdeTipoRequerimento().equals(TipoRequerimentoEnum.REQUERIMENTO_DE_ATO_ADMINISTRATIVO.getIde())){
				return "/paginas/requerimentos/formQuestionarioReqUnico.xhtml";
			}else if(tipoRequerimento.getIdeTipoRequerimento().equals(TipoRequerimentoEnum.REQUERIMENTO_DE_CADASTRO_DE_IMOVEL_RURAL.getIde())){
				return "/paginas/requerimentos/formQuestionarioCadImovelRural.xhtml";
			}else{
				return "/paginas/requerimentos/formQuestionarioReqUnico.xhtml";
			}
		} catch (Exception e) {
			System.out.println("ImovelRuralController.getCarregarUrlQuestionario()");
			return "/paginas/requerimentos/formQuestionarioCadImovelRural.xhtml";
		}
	}
	
	public void changeTipoRequerimento(ValueChangeEvent event) {
		try {
			TipoRequerimento tipoRequerimentoSelected = (TipoRequerimento)event.getNewValue();
			if (Util.isNullOuVazio(tipoRequerimentoSelected)) {
				tipoRequerimento = new TipoRequerimento();
				ContextoUtil.getContexto().setTipoRequerimento(this.tipoRequerimento);
				ContextoUtil.getContexto().setLabelTitutoRequerimento("Requerimento Único");
			}else{
				this.tipoRequerimento = tipoRequerimentoSelected;
				ContextoUtil.getContexto().setTipoRequerimento(this.tipoRequerimento);
				ContextoUtil.getContexto().setLabelTitutoRequerimento(TipoRequerimentoEnum.REQUERIMENTO_DE_CADASTRO_DE_IMOVEL_RURAL.getNomeTipoRequerimento(this.tipoRequerimento.getIdeTipoRequerimento()));
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public String retornaNomeDaPessoa(Pessoa p) {
		if(!Util.isNullOuVazio(p) && !Util.isNullOuVazio(p.getPessoaFisica()) && !Util.isNullOuVazio(p.getPessoaFisica().getNomPessoa())){
			return p.getPessoaFisica().getNomPessoa().toString();
		}else if(!Util.isNullOuVazio(p) && !Util.isNullOuVazio(p.getPessoaJuridica()) && !Util.isNullOuVazio(p.getPessoaJuridica().getNomRazaoSocial())){
			return p.getPessoaJuridica().getNomRazaoSocial().toString();
		}else
			return "Esse imóvel só tem Justo Possuidor";
	}
	
	public String retornaNomeDoRequerente(Pessoa pessoa) {
		try {
			Pessoa p = imovelRuralServiceFacade.filtrarRequerenteImovelRural(pessoa);
			
			if(!Util.isNull(p.getPessoaFisica())) {
				return p.getPessoaFisica().getNomPessoa();
			}
			
			return p.getPessoaJuridica().getNomRazaoSocial();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		
		return "";
	}
	
	public String retornaStatusImovel(ImovelRural imovelRural) {
		if(imovelRural.getIndSuspensao() != null && imovelRural.getIndSuspensao()){
			return "Pendente";
			
		} else if (imovelRural.getStatusCadastro() != null){
			switch (imovelRural.getStatusCadastro()) {
				case 0:
					return "Registro incompleto";
				case 1:
					return "Registrado";
				case 2:
					return "Cadastro incompleto";
				case 3:
					return "Cadastrado";
				default:
					return "Status Não Encontrado!";
			}
		}
		
		return "Status Não Encontrado!";
	}
	
	/**
	 * Método para mudar a cor da fonte na coluna Status do Imovel
	 */
	public String mudarCorFonte(ImovelRural imovelRural){
		if(imovelRural.getIndSuspensao() != null && imovelRural.getIndSuspensao()){
			return "color:red";
		} else {
			return "color:#4F4F4F";//cor padrão da gride
		}
    }
	
	public void setNomeImovel(String nomeEmpreendimento) {
		this.nomeImovel = nomeEmpreendimento;
	}

	public Municipio getMunicipio() {
		return municipio;
	}
	
	public void setRenderizaFisica(boolean pRenderizaFisica) {
		this.renderizaFisica = pRenderizaFisica;
		if (pRenderizaFisica) {
			this.setRenderizaJuridica(false);
		}
	}

	public boolean isRenderizaJuridica() {
		return renderizaJuridica;
	}

	public void setRenderizaJuridica(boolean pRenderizaJuridica) {
		this.renderizaJuridica = pRenderizaJuridica;
		if (pRenderizaJuridica) {
			this.renderizaFisica = false;
		}
	}
	
	public List<SelectItem> getItens() {
		return itens;
	}
	
	public void setItens(List<SelectItem> itens) {
		this.itens = itens;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}
	
	public StatusReservaLegal getStatusRLFiltro() {
		return this.statusRLFiltro;
	}
	
	public void setStatusRLFiltro(StatusReservaLegal statusRLFiltro) {
		this.statusRLFiltro = statusRLFiltro;
	}
	
	public Boolean getBloqueioAsvFiltro() {
		return this.bloqueioAsvFiltro;
	}
	
	public void setBloqueioAsvFiltro(Boolean bloqueioAsvFiltro) {
		this.bloqueioAsvFiltro = bloqueioAsvFiltro;
	}

	public List<Municipio> getListaMunicipios() {
		return listaMunicipios;
	}

	public void setListaMunicipios(List<Municipio> listaMunicipios) {
		this.listaMunicipios = listaMunicipios;
	}

	public Pessoa getRequerente() {
		return requerente;
	}

	public void setRequerente(Pessoa requerente) {
		this.requerente = requerente;
		if(!Util.isNullOuVazio(this.requerente) && !Util.isNullOuVazio(this.requerente.getPessoaFisica()) && !Util.isNullOuVazio(this.requerente.getPessoaFisica().getNomPessoa()))
			this.descNome = this.requerente.getPessoaFisica().getNomPessoa();
		else if(!Util.isNullOuVazio(this.requerente) && !Util.isNullOuVazio(this.requerente.getPessoaJuridica()) && !Util.isNullOuVazio(this.requerente.getPessoaJuridica().getNomRazaoSocial())){
			this.descNome = this.requerente.getPessoaJuridica().getNomRazaoSocial();
		}else
			this.descNome = "";
	}	

	public StreamedContent getImprimirCertificado(Integer ideImovel) {
		try {
			String linkGeobahia = "";
			String linkGeobahiaRL = "";
			List<String> linkCompensacaoRL = null;
			Boolean indSobreposicaoApp = false;
			
			ReservaLegal lReserva = reservaLegalService.buscaReservaLegalByImovelRural(new ImovelRural(ideImovel));
			
			try {
				StringBuilder buffer = getURLGeoBahia(ideImovel);
			
				linkGeobahia = ConfigEnum.GEOBAHIA_SERVER + buffer.toString().substring(27).replace("'</script>", "").replace(" ", "%20");

				List<ImovelRural> listImoveisEmCompensacaoDeReserva = imovelRuralService.imoveisEmCompensacaoDeReserva(ideImovel);
				
				if(!Util.isNullOuVazio(listImoveisEmCompensacaoDeReserva)) {
					linkCompensacaoRL = new ArrayList<String>();
					
					for (int i = 0; i < listImoveisEmCompensacaoDeReserva.size(); i++) {
						buffer = getURLGeoBahia("idimov="+ideImovel+"&res=640%20480&idimovc=" + listImoveisEmCompensacaoDeReserva.get(i)); 
						
						linkCompensacaoRL.add(ConfigEnum.GEOBAHIA_SERVER + buffer.toString().substring(27).replace("'</script>", "").replace(" ", "%20"));
					}
				}
			} catch(Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}

			if (!this.imovelRuralServiceFacade.hasCertificado(ideImovel)) {
				this.imovelRuralServiceFacade.gerarCertificado(TipoCertificadoEnum.CEFIR,ideImovel);
			}
			
			if(!Util.isNullOuVazio(lReserva)) {
				indSobreposicaoApp = lReserva.getIndSobreposicaoApp();
			}
			
			return this.impCertificado(ideImovel, indSobreposicaoApp, linkGeobahia,linkGeobahiaRL, linkCompensacaoRL);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return null;
		}
	}
	
	public StreamedContent getImprimirTermoDeCompromisso(Integer ideImovel) {
		try {
			ReservaLegal lReserva = reservaLegalService.buscaReservaLegalByImovelRural(new ImovelRural(ideImovel));
			Boolean indSobreposicaoApp = false;
			String linkGeobahia = "";
			String linkGeobahiaRL = "";
			List<String> linkCompensacaoRL = null;
			
			StringBuilder buffer = getURLGeoBahia(ideImovel);
			
			if(!Util.isNullOuVazio(buffer)) {
			
				linkGeobahia = ConfigEnum.GEOBAHIA_SERVER + buffer.toString().substring(27).replace("'</script>", "").replace(" ", "%20");
				
				List<ImovelRural> listImoveisEmCompensacaoDeReserva = new ArrayList<ImovelRural>();
				
				if(lReserva.getIdeTipoArl().getIdeTipoArl().equals(3)) {
					listImoveisEmCompensacaoDeReserva = imovelRuralService.imoveisEmCompensacaoDeReserva(ideImovel);
				}
				
				if(!Util.isNullOuVazio(listImoveisEmCompensacaoDeReserva)) {
					linkCompensacaoRL = new ArrayList<String>();
					
					for (int i = 0; i < listImoveisEmCompensacaoDeReserva.size(); i++) {
						buffer = getURLGeoBahia("idimov="+ideImovel+"&res=640%20480&idimovc=" + listImoveisEmCompensacaoDeReserva.get(i)); 
						linkCompensacaoRL.add(ConfigEnum.GEOBAHIA_SERVER + buffer.toString().substring(27).replace("'</script>", "").replace(" ", "%20"));
					}
				}
			}
			
			
			if(!Util.isNullOuVazio(lReserva)) {
				indSobreposicaoApp = lReserva.getIndSobreposicaoApp();
			}
			
		    if (!this.imovelRuralServiceFacade.hasTermosDeCompromisso(ideImovel)) {
		    	this.imovelRuralServiceFacade.gerarCertificado(TipoCertificadoEnum.TERMO_DE_COMPROMISSO,ideImovel);
		    }
		    
			return this.impTermoDeCompromisso(ideImovel, indSobreposicaoApp, linkGeobahia, linkGeobahiaRL, linkCompensacaoRL);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return null;
		}
	}
	
	public StreamedContent getImprimirAvisoBndes(Integer ideImovel) {
		return imovelRuralServiceFacade.getImprimirAvisoBndes(ideImovel, false);
	}

	private StringBuilder getURLGeoBahia(Integer ideImovel) throws IOException {
		return criarStreamComUrl(obterStringUrlGeoBahiaPorTipo("idimov="+ideImovel+"&res=640%20480"));
	}
	
	private StringBuilder getURLGeoBahia(String parametros) throws IOException {
		return criarStreamComUrl(obterStringUrlGeoBahiaPorTipo(parametros));
	}


	private StringBuilder criarStreamComUrl(String pUrl) throws IOException  {
		URL url = null;
		StringBuilder buffer = null;
		BufferedReader br = null; 
		InputStreamReader in = null;
		try {
			url = new URL(pUrl);
			in = new InputStreamReader(url.openStream());
			br = new BufferedReader(in);
			
			buffer = new StringBuilder();
			String linha;
			while ((linha = br.readLine()) != null) {
				buffer.append(linha);
			}
		} catch (MalformedURLException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		} catch (IOException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}finally{
			if(!Util.isNull(in)) {
				in.close();
			}
			if(!Util.isNull(br)) {
				br.close();
			}
		}

		return buffer;
	}


	private String obterStringUrlGeoBahiaPorTipo(String parametros) {
		return URLEnum.CAMINHO_GEOBAHIA_CERTIFICADO + parametros;
	}
	
	private StreamedContent impCertificado(Integer ideImovel, Boolean indSobreposicaoApp, String linkGeobahia,String linkGeobahiaRL, List<String> linkCompensacaoRL) throws Exception {
		Map<String, Object> lParametros = new HashMap<String, Object>();
		
		ImovelRural imovelRural = this.imovelRuralService.carregarById(ideImovel);
		imovelRural.setReservaLegal(reservaLegalService.buscaReservaLegalByImovelRural(imovelRural));
		
		lParametros.put("ide_imovel", ideImovel);
		lParametros.put("SHAPE", linkGeobahia);
		lParametros.put("SHAPE_RL", linkGeobahiaRL);
		lParametros.put("ind_sobreposicao_app", indSobreposicaoApp);
		if(!Util.isNullOuVazio(linkCompensacaoRL)) {
			lParametros.put("SHAPE_COMPENSACAO_RL", linkCompensacaoRL);
			lParametros.put("AREA_IMOVEL", imovelRural.getValArea());
		}
		if(!Util.isNullOuVazio(imovelRural.getReservaLegal())) {
			indSobreposicaoApp = imovelRural.getReservaLegal().getIndSobreposicaoApp();
		}
		try{
			lParametros.put("isRlMenorQueVintePorCento", isRlMenorQueVintePorCento(imovelRural));
		} catch (Exception e){
			lParametros.put("isRlMenorQueVintePorCento", false);
		}
		
		if(!Util.isNullOuVazio(imovelRural.getReservaLegal()) && !Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeTipoArl()) && imovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(TipoArlEnum.ECIP.getId())) {
			lParametros.put("isRlEmCompensacao", true);
		} else {
			lParametros.put("isRlEmCompensacao", false);
		}
		
		return new RelatorioUtil("certificado_cefir.jasper", lParametros).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}
	
	public StreamedContent getImprimirHistorico() throws Exception {

		return imovelRuralService.getImprimirHistorico(getFiltroAuditoria().getDataInicio(), getFiltroAuditoria().getDataFim(), imovelRural.getIdeImovelRural(), this.retornaStatusImovel(imovelRural), listAuditoria);
		
	}
	
	
	private StreamedContent impTermoDeCompromisso(Integer ideImovelRural, Boolean indSobreposicaoApp, String linkGeobahia, String linkGeobahiaRL, List<String> linkCompensacaoRL) throws Exception {
		Map<String, Object> lParametros = new HashMap<String, Object>();
		
		ImovelRural imovelRural = this.imovelRuralService.carregarTudo(new ImovelRural(ideImovelRural));
		imovelRural.setAppCollection(imovelRuralServiceFacade.listarAppByImovelRural(imovelRural));					
		imovelRural.setAreaProdutivaCollection(imovelRuralServiceFacade.listarAreaProdutivaByImovelRural(imovelRural));
		imovelRural.setVegetacaoNativa(imovelRuralServiceFacade.listarVegetacaoNativaByImovelRural(imovelRural));
		
		Collection<PessoaImovel> listPessoaImovel = this.imovelRuralService.filtrarPROPRIETARIOImovel(new Imovel(ideImovelRural));
		Collection<Pessoa> listPessoa = this.imovelRuralService.listarProprietariosJustoPossuidoresImovel(ideImovelRural);
		//Pessoa pessoa = this.imovelRuralService.buscarJustoPossuidor(ideImovelRural);
		Collection<RepresentanteLegal> representantesLegais = null;
		TipoVinculoImovel tipoVinculoImovelRural = new TipoVinculoImovel(TipoVinculoImovel.TIPO_VINCULO_PROPRIETARIO);
		
		List<AssentadoIncraImovelRural> assentados = this.imovelRuralServiceFacade.listarAssentadoIncraImovelRuralPorImovelRural(imovelRural);
		List<PessoaFisica> listAssentado = new ArrayList<PessoaFisica>();
		
		if(!Util.isNullOuVazio(assentados)) {
			for (AssentadoIncraImovelRural assentadoIncraImovelRural : assentados) {
				listAssentado.add(assentadoIncraImovelRural.getIdeAssentadoIncra().getIdePessoaFisica());
			}
		}
		
		if(!Util.isNullOuVazio(listPessoaImovel)){
			for (PessoaImovel pessoaImovel : listPessoaImovel) {
				if(pessoaImovel.getIdeTipoVinculoImovel().getIdeTipoVinculoImovel().equals(TipoVinculoImovel.TIPO_VINCULO_JUSTO_POSSUIDOR)){
					tipoVinculoImovelRural.setIdeTipoVinculoImovel(TipoVinculoImovel.TIPO_VINCULO_JUSTO_POSSUIDOR);
				}
			}
		}
		
		if(!Util.isNullOuVazio(listPessoa) && !listPessoa.isEmpty()){
			representantesLegais = this.representanteLegalService.listarRepresentantesLegais(listPessoa);
		}
		
		imovelRural.setReservaLegal(reservaLegalService.buscaReservaLegalByImovelRural(imovelRural));
		
		lParametros.put("TERMO", RelatorioUtil.formatarTermoDeCompromisso(imovelRural, tipoVinculoImovelRural, listPessoa, representantesLegais, listAssentado));
		
		if(tipoVinculoImovelRural.getIdeTipoVinculoImovel().equals(TipoVinculoImovel.TIPO_VINCULO_JUSTO_POSSUIDOR)){
			lParametros.put("IMOVEL", "<i>" + imovelRural.getDesDenominacao() + "</i>" + (!Util.isNullOuVazio(imovelRural.getRegistroMatricula()) ? ", Nº de registro do documento de posse: <i>"+imovelRural.getRegistroMatricula()+ "</i>" : "") + (!Util.isNullOuVazio(imovelRural.getNumItr()) ? ", ITR: <i>"+imovelRural.getNumItr() + "</i>" : ""));
		}else{
			lParametros.put("IMOVEL", "<i>" + imovelRural.getDesDenominacao() + "</i>" + ", Matrícula: <i>"+imovelRural.getRegistroMatricula()+ "</i>" + (!Util.isNullOuVazio(imovelRural.getNumItr()) ? ", ITR: <i>"+imovelRural.getNumItr() + "</i>" : ""));
		}
		
		lParametros.put("ENDERECO", imovelRural.getImovel().getIdeEndereco().getEnderecoCompletoSemPais());
		lParametros.put("ide_imovel", ideImovelRural);
		lParametros.put("ind_sobreposicao_app", indSobreposicaoApp);
		lParametros.put("isRlMenorQueVintePorCento", isRlMenorQueVintePorCento(imovelRural));
		lParametros.put("temMaisDeUmProprietario", temMaisDeUmProprietario(imovelRural, tipoVinculoImovelRural, listPessoa, representantesLegais, listAssentado));
		
		lParametros.put("precisaLicencaAP", imovelRuralService.precisaDeLicencaAreaProdutiva(imovelRural));
		lParametros.put("precisaOutorga", imovelRuralService.precisaDeOutorga(imovelRural));
		lParametros.put("possuiPradAPP", imovelRuralService.possuiPraApp(imovelRural));
		lParametros.put("possuiPradRL", imovelRuralService.possuiPraRl(imovelRural));
		lParametros.put("possuiPradOP", imovelRuralService.possuiPradOP(imovelRural));
		lParametros.put("isJustoPossuidor", imovelRuralService.carregarTipoVinculoImovel(imovelRural).getIdeTipoVinculoImovel().equals(TipoVinculoImovel.TIPO_VINCULO_JUSTO_POSSUIDOR));
		
		//SHAPES
		lParametros.put("SHAPE", linkGeobahia);
		lParametros.put("SHAPE_RL", linkGeobahiaRL);
		
		if(!Util.isNullOuVazio(linkCompensacaoRL)) {
			lParametros.put("SHAPE_COMPENSACAO_RL", linkCompensacaoRL);
			lParametros.put("AREA_IMOVEL", imovelRural.getValArea());
		}
		
		if(imovelRural.getReservaLegal().getIdeTipoArl().getIdeTipoArl().equals(TipoArlEnum.ECIP.getId())) {
			lParametros.put("isRlEmCompensacao", true);
		} else {
			lParametros.put("isRlEmCompensacao", false);
		}
		
		if(imovelRural.isImovelINCRA()){
			return new RelatorioUtil("termo_de_compromisso_cefir_incra.jasper", lParametros).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);			
		}
		
		return new RelatorioUtil("termo_de_compromisso_cefir.jasper", lParametros).gerarRelatorio(RelatorioUtil.RELATORIO_PDF, true);
	}
		
	private boolean temMaisDeUmProprietario(ImovelRural imovelRural, TipoVinculoImovel tipoVinculoImovelRural, Collection<Pessoa> listPessoa, Collection<RepresentanteLegal> representantesLegais, List<PessoaFisica> listAssentado) {
		int qtdProprietarios = 0;
		
		if((!Util.isNullOuVazio(listPessoa) || !listPessoa.isEmpty())){
			qtdProprietarios += listPessoa.size();				
		}
		
		if(!Util.isNullOuVazio(listAssentado)) {
			qtdProprietarios += listAssentado.size();
		}
		
		if(qtdProprietarios > 1){
			return true;
		} else {
			return false;
		}
	}

	public LazyDataModel<Imovel> getListaImoveis() {
		return listaImoveis;
	}

	public void setListaImoveis(LazyDataModel<Imovel> listaImoveis) {
		this.listaImoveis = listaImoveis;
	}

	public String getDocumento() {
		return documento;
	}

	public String getLblNome() {
		return lblNome;
	}

	public void setLblNome(String lblNome) {
		this.lblNome = lblNome;
	}

	public String getLblDoc() {
		return lblDoc;
	}

	public void setLblDoc(String lblDoc) {
		this.lblDoc = lblDoc;
	}

	public String getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public String getMascara() {
		return mascara;
	}

	public void setMascara(String mascara) {
		this.mascara = mascara;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isRenderizaFisica() {
		return renderizaFisica;
	}

	public LazyDataModel<PessoaFisica> getPessoasFisicaModel() {
		return pessoasFisicaModel;
	}

	public void setPessoasFisicaModel(LazyDataModel<PessoaFisica> pessoasFisicaModel) {
		this.pessoasFisicaModel = pessoasFisicaModel;
	}

	public TipoRequerimento getTipoRequerimento() {
		return tipoRequerimento;
	}

	public void setTipoRequerimento(TipoRequerimento tipoRequerimento) {
		this.tipoRequerimento = tipoRequerimento;
	}

	public boolean isEditMode() {
		return editMode;
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	public ImovelRural getImovelRural() {
		return imovelRural;
	}

	public void setImovelRural(ImovelRural imovelRural) {
		this.imovelRural = imovelRural;
	}

	public boolean isAdicionaProprietario() {
		return adicionaProprietario;
	}

	public void setAdicionaProprietario(boolean adicionaProprietario) {
		this.adicionaProprietario = adicionaProprietario;
	}

	public boolean isTemImovelSelecionado() {
		return temImovelSelecionado;
	}

	public void setTemImovelSelecionado(boolean temImovelSelecionado) {
		this.temImovelSelecionado = temImovelSelecionado;
	}

	public Boolean getShowPessoaJuridica() {
		return showPessoaJuridica;
	}

	public void setShowPessoaJuridica(Boolean showPessoaJuridica) {
		this.showPessoaJuridica = showPessoaJuridica;
	}

	public Boolean getShowProprioRequerente() {
		return showProprioRequerente;
	}

	public void setShowProprioRequerente(Boolean showProprioRequerente) {
		this.showProprioRequerente = showProprioRequerente;
	}

	public Boolean getShowProcuradorPessoaFisica() {
		return showProcuradorPessoaFisica;
	}

	public void setShowProcuradorPessoaFisica(Boolean showProcuradorPessoaFisica) {
		this.showProcuradorPessoaFisica = showProcuradorPessoaFisica;
	}

	public Boolean getShowProcuradorPessoaJuridica() {
		return showProcuradorPessoaJuridica;
	}

	public void setShowProcuradorPessoaJuridica(Boolean showProcuradorPessoaJuridica) {
		this.showProcuradorPessoaJuridica = showProcuradorPessoaJuridica;
	}

	public Pessoa getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(Pessoa solicitante) {
		this.solicitante = solicitante;
	}

	public ImovelRural getImovelSelecionado() {
		return imovelSelecionado;
	}

	public void setImovelSelecionado(ImovelRural imovelSelecionado) {
		this.imovelSelecionado = imovelSelecionado;
	}

	public Boolean getVisualizandoImovel() {
		return visualizandoImovel;
	}
	
	public void setVisualizandoImovel(Boolean visualizandoImovel) {
		this.visualizandoImovel = visualizandoImovel;
	}
	
	public Boolean getImprimindoImovel() {
		return imprimindoImovel;
	}
	
	public void setImprimindoImovel(Boolean imprimindoImovel) {
		this.imprimindoImovel = imprimindoImovel;
	}

	public String getCaminhoArquivo() {
		return caminhoArquivo;
	}

	public void setCaminhoArquivo(String caminhoArquivo) {
		this.caminhoArquivo = caminhoArquivo;
	}

	public LocalizacaoGeograficaService getLocalizacaoGeograficaService() {
		return localizacaoGeograficaService;
	}

	public void setLocalizacaoGeograficaService(
			LocalizacaoGeograficaService localizacaoGeograficaService) {
		this.localizacaoGeograficaService = localizacaoGeograficaService;
	}

	public boolean isHabilitaEndereco() {
		return habilitaEndereco;
	}

	public void setHabilitaEndereco(boolean habilitaEndereco) {
		this.habilitaEndereco = habilitaEndereco;
	}

	public boolean isHabilitaLocalizacao() {
		return habilitaLocalizacao;
	}

	public void setHabilitaLocalizacao(boolean habilitaLocalizacao) {
		this.habilitaLocalizacao = habilitaLocalizacao;
	}

	public boolean isHabilitaCadastroAmbiental() {
		return habilitaCadastroAmbiental;
	}

	public void setHabilitaCadastroAmbiental(boolean habilitaCadastroAmbiental) {
		this.habilitaCadastroAmbiental = habilitaCadastroAmbiental;
	}

	protected ImovelRuralDTO getDadosParaBusca() {
		return dadosParaBusca;
	}

	protected void setDadosParaBusca(ImovelRuralDTO dadosParaBusca) {
		this.dadosParaBusca = dadosParaBusca;
	}

	public String getImoveisAValidar() {
		return imoveisAValidar;
	}

	public void setImoveisAValidar(String imoveisAValidar) {
		this.imoveisAValidar = imoveisAValidar;
	}

	public void alterarLayout(ValueChangeEvent event) {
		Boolean value = (Boolean) event.getNewValue();
		if (value) {
			this.telaCnpj = false;
			this.telaCpf = true;
		} else {
			this.telaCpf = false;
			this.telaCnpj = true;
		}
	}

	public String limparObjetosAction() {
		this.telaCpf = Boolean.TRUE;
		this.telaCnpj = Boolean.FALSE;
		pessoaPersist = new Pessoa();
		pessoaFisicaPersist = new PessoaFisica();
		pessoaJuridicaPersist = new PessoaJuridica();
		return null;
	}

	public void limparObjetos() {
		this.telaCpf = Boolean.TRUE;
		this.telaCnpj = Boolean.FALSE;
		pessoaPersist = new Pessoa();
		pessoaFisicaPersist = new PessoaFisica();
		pessoaJuridicaPersist = new PessoaJuridica();
		enableBotaoPesquisa = true;
		enableFormPessoaFisica = false;
		enableFormPessoaJuridica = false;
		limparComponentesFormulario(formularioASerLimpo);
	}

	public void pesquisarPessoa() {
		if (telaCpf) {
			pesquisarPessoaFisica();
		} else {
			pesquisarPessoaJuridica();
		}
	}

	public void pesquisarPessoaFisica() {
		try {
			String cpf = pessoaFisicaPersist.getNumCpf();
			pessoaFisicaPersist = pessoaFisicaService.filtrarPessoaFisicaByCpf(pessoaFisicaPersist);
			if (Util.isNullOuVazio(pessoaFisicaPersist)) {
				pessoaFisicaPersist = new PessoaFisica();
				pessoaFisicaPersist.setNumCpf(cpf);
				pessoaPersist = new Pessoa();
				enableFormPessoaFisica = Boolean.TRUE;
			} else {
				pessoaPersist = pessoaFisicaPersist.getPessoa();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public void pesquisarPessoaJuridica() {
		try {
			String cnpj = pessoaJuridicaPersist.getNumCnpj();
			pessoaJuridicaPersist = pessoaJuridicaService.filtrarPessoaFisicaByCnpj(pessoaJuridicaPersist);
			if (Util.isNullOuVazio(pessoaJuridicaPersist)) { 
				pessoaJuridicaPersist = new PessoaJuridica();
				pessoaJuridicaPersist.setNumCnpj(cnpj);
				pessoaPersist = new Pessoa();
				enableFormPessoaJuridica = Boolean.TRUE;
			} else {
				pessoaPersist = pessoaJuridicaPersist.getPessoa();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public String prepararParaEdicao() {
		setEnableBotaoPesquisa(true);
		return prepararJanelaCompAcionaria();
	}
	
	public String prepararParaVisualizacao() {
		setEnableBotaoPesquisa(false);
		return prepararJanelaCompAcionaria();
	}

	private String prepararJanelaCompAcionaria() {
		pessoaFisicaPersist = new PessoaFisica();
		pessoaJuridicaPersist = new PessoaJuridica();
		limparComponentesFormulario(formularioASerLimpo);
		enableFormPessoaFisica = true;
		if (!Util.isNullOuVazio(pessoaPersist.getPessoaFisica())) {
			pessoaFisicaPersist = pessoaPersist.getPessoaFisica();
			selecionarRadioPessoaFisica();
		} else {
			pessoaJuridicaPersist = pessoaPersist.getPessoaJuridica();
			selecionarRadioPessoaJuridica();
		}
		return null;
	}
		
	private void selecionarRadioPessoaFisica() {
		telaCnpj = Boolean.FALSE;
		telaCpf = Boolean.TRUE;
	}

	private void selecionarRadioPessoaJuridica() {
		telaCnpj = Boolean.TRUE;
		telaCpf = Boolean.FALSE;
	}
	
	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Logradouro getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(Logradouro logradouro) {
		this.logradouro = logradouro;
	}

	public Bairro getBairro() {
		return bairro;
	}

	public void setBairro(Bairro bairro) {
		this.bairro = bairro;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	
	public TipoLogradouro getTipoLogradouro() {
		return tipoLogradouro;
	}

	public void setTipoLogradouro(TipoLogradouro tipoLogradouro) {
		this.tipoLogradouro = tipoLogradouro;
	}
	
	public void setEnableBotaoPesquisa(boolean enableBotaoPesquisa) {
		this.enableBotaoPesquisa = enableBotaoPesquisa;
	}

	public boolean getEnableFormPessoaFisica() {
		return enableFormPessoaFisica;
	}

	public boolean isTelaCpf() {
		return telaCpf;
	}

	public void setTelaCpf(boolean telaCpf) {
		this.telaCpf = telaCpf;
	}

	public boolean isTelaCnpj() {
		return telaCnpj;
	}

	public void setTelaCnpj(boolean telaCnpj) {
		this.telaCnpj = telaCnpj;
	}

	public Pessoa getPessoaPersist() {
		return pessoaPersist;
	}

	public void setPessoaPersist(Pessoa pessoaPersist) {
		this.pessoaPersist = pessoaPersist;
	}

	public boolean isEditModeProprietario() {
		return editModeProprietario;
	}

	public void setEditModeProprietario(boolean editModeProprietario) {
		this.editModeProprietario = editModeProprietario;
	}

	public PessoaFisica getPessoaFisicaPersist() {
		return pessoaFisicaPersist;
	}

	public void setPessoaFisicaPersist(PessoaFisica pessoaFisicaPersist) {
		this.pessoaFisicaPersist = pessoaFisicaPersist;
	}

	public PessoaJuridica getPessoaJuridicaPersist() {
		return pessoaJuridicaPersist;
	}

	public void setPessoaJuridicaPersist(PessoaJuridica pessoaJuridicaPersist) {
		this.pessoaJuridicaPersist = pessoaJuridicaPersist;
	}

	public UIComponent getFormularioASerLimpo() {
		return formularioASerLimpo;
	}

	public void setFormularioASerLimpo(UIComponent formularioASerLimpo) {
		this.formularioASerLimpo = formularioASerLimpo;
	}

	public boolean isEnableFormPessoaJuridica() {
		return enableFormPessoaJuridica;
	}

	public void setEnableFormPessoaJuridica(boolean enableFormPessoaJuridica) {
		this.enableFormPessoaJuridica = enableFormPessoaJuridica;
	}

	public boolean getEnableBotaoPesquisa() {
		return enableBotaoPesquisa;
	}

	public void setEnableFormPessoaFisica(boolean enableFormPessoaFisica) {
		this.enableFormPessoaFisica = enableFormPessoaFisica;
	}

	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}

	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
	}

	public PessoaImovel getPessoaiMovel() {
		return pessoaiMovel;
	}

	public void setPessoaiMovel(PessoaImovel pessoaiMovel) {
		this.pessoaiMovel = pessoaiMovel;
	}

	public List<Arquivo> getArquivosAValidar() {
		return arquivosAValidar;
	}

	public void setArquivosAValidar(List<Arquivo> arquivosAValidar) {
		this.arquivosAValidar = arquivosAValidar;
	}

	public Boolean getDisableImovelRural() {
		return disableImovelRural;
	}

	public void setDisableImovelRural(Boolean disableImovelRural) {
		this.disableImovelRural = disableImovelRural;
	}

	public String getCaminhoDesenharGeoBahia() {
		return caminhoDesenharGeoBahia;
	}

	public void setCaminhoDesenharGeoBahia(String caminhoDesenharGeoBahia) {
		this.caminhoDesenharGeoBahia = caminhoDesenharGeoBahia;
	}
	
	public Imovel getImovelExcluir() {
		return imovelExcluir;
	}

	public void setImovelExcluir(Imovel imovelExcluir) {
		this.imovelExcluir = imovelExcluir;
	}

	public Boolean getIsProprietarioImovel() {
		return isProprietarioImovel;
	}

	public void setIsProprietarioImovel(Boolean isProprietarioImovel) {
		this.isProprietarioImovel = isProprietarioImovel;
	}

	//metodo criado apenas para fazer o poll que carrega as mensagens do growl funcionar
	public void pollAjax(){
		if (ContextoUtil.getContexto() != null && ContextoUtil.getContexto().getObject() != null && ContextoUtil.getContexto().getObject() instanceof String) {
				this.msgCadastroConcluido = (String) ContextoUtil.getContexto().getObject();
				RequestContext.getCurrentInstance().execute("dialogCadastroConcluido.show()");
				ContextoUtil.getContexto().setObject(null);
				rendererPoll=false;
				RequestContext.getCurrentInstance().execute("pollMensagens.stop()");
		}
		return;
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

	public boolean isRendererPoll() {
		return rendererPoll;
	}

	public void setRendererPoll(boolean rendererPoll) {
		this.rendererPoll = rendererPoll;
	}

	public String voltar() {
		return urlVoltar;
	}

	public List<StatusReservaLegal> getListaStatusRL() {
		return listaStatusRL;
	}

	public void setListaStatusRL(List<StatusReservaLegal> listaStatusRL) {
		this.listaStatusRL = listaStatusRL;
	}
	
	public void filtrarHistorico() {
		if(!Util.isNullOuVazio(getFiltroAuditoria().getDataInicio())) {
			try {
				TimeZone timeZone = TimeZone.getTimeZone("America/Sao_Paulo");
		        TimeZone.setDefault(timeZone);
				listAuditoria = imovelRuralServiceFacade.filtrarHistorico(getFiltroAuditoria().getDataInicio(), getFiltroAuditoria().getDataFim(), imovelRural, 0, 0);
				List<FiltroAuditoria> listAuditoriaNova = new ArrayList<FiltroAuditoria>();
				for (FiltroAuditoria filtroAuditoria : listAuditoria) {
					filtroAuditoria.setCampoAlterado(filtroAuditoria.getCampo().getNomeCampo());
					filtroAuditoria.setValorAntigoString(filtroAuditoria.getValorAntigo().getValor());
					filtroAuditoria.setValorNovoString(filtroAuditoria.getValorNovo().getValor());
					if(!Util.isNullOuVazio(filtroAuditoria.getValorAntigo().getValor()) && !Util.isNullOuVazio(filtroAuditoria.getValorNovo().getValor())) {
						if(!filtroAuditoria.getValorAntigo().getValor().trim().equals(filtroAuditoria.getValorNovo().getValor().trim())) {
							listAuditoriaNova.add(filtroAuditoria);
						}
					}
				}
				listAuditoria = listAuditoriaNova;
				listAuditoriaNova = null;
				
				
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}
	}
	
	public void changeStatusReservaLegal(ValueChangeEvent event) {
		try {
			StatusReservaLegal statusReservaLegal = (StatusReservaLegal)event.getNewValue();
			if (Util.isNullOuVazio(statusReservaLegal) || statusReservaLegal.getIdeStatusReservaLegal() == -1) {
				statusRLFiltro = new StatusReservaLegal();
			}else{
				this.statusRLFiltro = statusReservaLegal;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public FiltroAuditoria getFiltroAuditoria() {
		return filtroAuditoria;
	}

	public void setFiltroAuditoria(FiltroAuditoria filtroAuditoria) {
		this.filtroAuditoria = filtroAuditoria;
	}

	public List<FiltroAuditoria> getListFiltroAuditoria() throws Exception {
			return listAuditoria;
	}
	
	public Boolean getReservaLegalAprovadaOuAverbada() {
		return imovelRural.getReservaLegalAprovadaOuAverbada();
	}
	
	public void limparFiltroAuditoria() {
		FiltroAuditoria filtro = new FiltroAuditoria();
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(calendar.get(Calendar.YEAR), Calendar.JANUARY, 1);
		filtro.setDataInicio(calendar.getTime());
		filtro.setDataFim(new Date());
		
		setFiltroAuditoria(filtro);
		listAuditoria = null;
	}		
		
	public Boolean isRlMenorQueVintePorCento(ImovelRural pImovelRural) throws Exception {
		return this.imovelRuralServiceFacade.isRlMenor20PorCento(pImovelRural);
	}
	
	public StreamedContent downloadHistoricoShapeStream(String caminhoArquivo) throws Exception {
		String caminhoDiretorio = caminhoArquivo.split(";")[0];
		caminhoDiretorio = caminhoDiretorio.substring(0, caminhoDiretorio.lastIndexOf(FileUploadUtil.getFileSeparator()));
		ZipFiles.zipIt("shapes", caminhoDiretorio);			
		return gerenciaArquivoService.getContentFile(caminhoDiretorio+FileUploadUtil.getFileSeparator()+"shapes.zip");
	}		
	
	public StreamedContent getDocumentoParaDownload(Integer ideDocumento) throws Exception {
		DocumentoImovelRural documento = documentoObrigatorioService.carregarDocumentoObrigatorio(ideDocumento);
		if(documento != null) {
			return gerenciaArquivoService.getContentFile(documento.getDscCaminhoArquivo());
		}
		return null;
	}		
	
	public boolean isIniciarConsulta() {
		return iniciarConsulta;
	}


	public void setIniciarConsulta(boolean iniciarConsulta) {
		this.iniciarConsulta = iniciarConsulta;
	}

	public String getMsgCadastroConcluido() {
		return msgCadastroConcluido;
	}

	public void setMsgCadastroConcluido(String msgCadastroConcluido) {
		this.msgCadastroConcluido = msgCadastroConcluido;
	}
	
	public String getMensagemErroCar() {
		if(this.imovelRural != null && this.imovelRural.getImovelRuralSicar() != null && this.imovelRural.getImovelRuralSicar().getCodRetornoSincronia() != null) {
			return "Prezado(a) usuário(a), <br /><br />" + Util.getString("cefir_msg_erro_numero_car") + " e informe o protocolo a seguir: <br /><br />" + this.imovelRural.getImovelRuralSicar().getNumProtocolo();
		}else if(this.imovelRural != null && this.imovelRural.getImovelRuralSicar() != null && Util.isNullOuVazio(this.imovelRural.getImovelRuralSicar().getCodRetornoSincronia())) {
			return "Prezado(a) usuário(a), <br /><br /> O imóvel está em processo de geração do número CAR, durante esse processo não será possível realizar alterações no cadastro. Para maiores informações, por favor, entre em contato com o Service Desk do SEIA, através do email atendimento.seia@inema.ba.gov.br e informe o protocolo a seguir: <br /><br />" + this.imovelRural.getImovelRuralSicar().getNumProtocolo();
		}
			
		return "";
	}
	
	public void excluirShape(Integer pIdeLocalizacaoGeografica) {
		try {
			localizacaoGeograficaService.excluirDadosPersistidos(pIdeLocalizacaoGeografica);
			JsfUtil.addSuccessMessage("Exclusão efetuada com Sucesso.");
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}
	
	public void carregarSecaoGeometrica() {
		try {
			listaSecaoGeomerica = secaoGeometricaService.listarClassificacaoSecaoGeometrica();
			//removendo a opção de ponto
			listaSecaoGeomerica.remove(0);
			if(!imovelRural.isMenorQueQuatroModulosFiscais()){
				//removendo a opção de desenho				
				listaSecaoGeomerica.remove(1);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public List<ClassificacaoSecaoGeometrica> getListaSecaoGeomerica() {
		return listaSecaoGeomerica;
	}

	public void setListaSecaoGeomerica(List<ClassificacaoSecaoGeometrica> listaSecaoGeomerica) {
		this.listaSecaoGeomerica = listaSecaoGeomerica;
	}

	public Boolean getImovelCDA() {
		return imovelCDA;
	}

	public void setImovelCDA(Boolean imovelCDA) {
		this.imovelCDA = imovelCDA;
	}
	
	public Boolean getImovelBndes() {
		return imovelBndes;
	}

	public void setImovelBndes(Boolean imovelBndes) {
		this.imovelBndes = imovelBndes;
	}
	
	/**
	 * Método responsável por validar a renderização na tela de consulta de imóveis rurais o ckeckbox Imóveis BNDES
	 * @return boolean
	 * @throws Exception 
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 14/10/2015
	*/
	public boolean getRenderedCheckProjetoBndes() throws Exception {
		return usuarioVinculadoProjetoBndes;
	}
	
	/**
	 * Método responsável por validar a renderização na tela de consulta de imóveis rurais o botão de exclusão de imóvel
	 * @return boolean
	 * @author carlos.duarte (carlos.duarte@inema.ba.gov.br)
	 * @since 28/04/2016
	*/
	public boolean renderedBotaoExclusao(Imovel imovel) {
		if((Util.isNullOuVazio(imovel.getImovelRural().getStsCertificado()) || !imovel.getImovelRural().getStsCertificado()) 
				&& (Util.isNullOuVazio(imovel.getImovelRural().isTermoCompromisso()) ||!imovel.getImovelRural().isTermoCompromisso()) 
				&& (imovel.getImovelRural().getStatusCadastro() == null || imovel.getImovelRural().isRegistroIncompleto()) 
				&& (Util.isNullOuVazio(imovel.getImovelRural().getImovelRuralSicar()) || Util.isNullOuVazio(imovel.getImovelRural().getImovelRuralSicar().getNumSicar()))
				&& (!imovel.getImovelRural().getReservaLegalAprovadaOuAverbada())
				&& !imovel.getImovelRural().isCedeAreaParaCompensacaoRl()) {
			
			try {
				if(imovel.getImovelRural() != null) {
					imovelRuralServiceFacade.validarPossuiVinculoComEmpreendimento(imovel.getImovelRural()); //#7715
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}
		
		return false;
	}
	
	/**
	 * Método responsável por fazer o download do arquivo zip contendo os 'Avisos Projeto CAR/BNDES/INEMA' da {@link listaImoveis}
	 * 
	 * @author ivanildo.souza
	 * @since 12/01/17
	 */
	public String getDownloadAvisoCarBndesLote(){
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		try{
			List<StreamedContent> listAvisoCarBndesInema = new ArrayList<StreamedContent>();
			
			for (Object object : listaImoveis) {
				Imovel imovel = (Imovel) object;

				if(imovel.getImovelRural().isAvisoBndes()){
					listAvisoCarBndesInema.add(getImprimirAvisoBndes(imovel.getImovelRural().getIdeImovelRural()));
				}				
			}
			InputStream arquivo = Util.comprimirListaStreamedContentParaZip(listAvisoCarBndesInema);
			SessaoUtil.adicionarObjetoSessao("arquivo", arquivo);
			String url =  request.getScheme() + "://" + request.getServerName();
			
			if (request.getServerPort()!=80){
				url += ":" + request.getServerPort();
			}
			
			RequestContext.getCurrentInstance().execute("window.open('"+url+"/aviso-car-lote/')");
			return "";
		}
		catch (Exception e) {
			request.getSession().setAttribute("arquvo",null);
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Erro ao fazer o download do arquivo");
			return null;
		}

	}
	
	public boolean isHabilitaDownloadAvisoCarLote() {
		return habilitaDownloadAvisoCarLote;
	}

	public MetodoUtil getMetodoSelecionarSolicitante() {
		return metodoSelecionarSolicitante;
	}

	public void setMetodoSelecionarSolicitante(
			MetodoUtil metodoSelecionarSolicitante) {
		this.metodoSelecionarSolicitante = metodoSelecionarSolicitante;
	}
	
	public boolean isUsuarioExterno() {
		return ContextoUtil.getContexto().isUsuarioExterno();
	}
	
	public boolean validarUsuarioExternoCadastro(ImovelRural imovelRural){
		boolean usuarioExterno = isUsuarioExterno();
		String status = retornaStatusImovel(imovelRural);
		return (usuarioExterno && status.equalsIgnoreCase("Pendente"));
	}
	
	public boolean habilitarBotoesStatusPendenteImovel(ImovelRural imovelRural){
		return !"Pendente".equalsIgnoreCase(retornaStatusImovel(imovelRural));
	}
	
	public boolean habilitarBotoesStatusPendenteIdeImovel(Integer ideImovelRural){
		return habilitarBotoesStatusPendenteImovel(new ImovelRural(ideImovelRural));
	}
	
	public void inativarImovel() {
		try {
			Imovel objAntigo = imovelService.buscarImovelPorIde(inativarImovel);
			inativarImovel = objAntigo.clone();
			imovelRuralServiceFacade.inativarImovel(inativarImovel);
			JsfUtil.addSuccessMessage("Inativação efetuada com sucesso.");
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao Inativar Imóvel");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public String getNumSicar() {
		return numSicar;
	}

	public void setNumSicar(String numSicar) {
		this.numSicar = numSicar;
	}

	public Boolean getIsImovelRegistroIncompleto() {
		return isImovelRegistroIncompleto;
	}

	public void setIsImovelRegistroIncompleto(Boolean isImovelRegistroIncompleto) {
		this.isImovelRegistroIncompleto = isImovelRegistroIncompleto;
	}

	public String getTipoCadastro() {
		return tipoCadastro;
	}

	public void setTipoCadastro(String tipoCadastro) {
		this.tipoCadastro = tipoCadastro;
	}

	public Imovel getinativarImovel() {
		return inativarImovel;
	}

	public void setinativarImovel(Imovel inativarImovel) {
		this.inativarImovel = inativarImovel;
	}

	public Pessoa getProcurador() {
		return procurador;
	}

	public void setProcurador(Pessoa procurador) {
		this.procurador = procurador;
	}

	public String getNumeroMatricula() {
		return numeroMatricula;
	}

	public void setNumeroMatricula(String numeroMatricula) {
		this.numeroMatricula = numeroMatricula;
	}

	public Pessoa getResponsavelTecnico() {
		return responsavelTecnico;
	}

	public void setResponsavelTecnico(Pessoa responsavelTecnico) {
		this.responsavelTecnico = responsavelTecnico;
	}

	public String getNomeResponsavelTecnico() {
		return nomeResponsavelTecnico;
	}

	public void setNomeResponsavelTecnico(String nomeResponsavelTecnico) {
		this.nomeResponsavelTecnico = nomeResponsavelTecnico;
	}

	public MetodoUtil getMetodoSelecionarResponsavelTecnico() {
		return metodoSelecionarResponsavelTecnico;
	}

	public void setMetodoSelecionarResponsavelTecnico(MetodoUtil metodoSelecionarResponsavelTecnico) {
		this.metodoSelecionarResponsavelTecnico = metodoSelecionarResponsavelTecnico;
	}

	public MetodoUtil getMetodoSelecionarProcurador() {
		return metodoSelecionarProcurador;
	}

	public void setMetodoSelecionarProcurador(MetodoUtil metodoSelecionarProcurador) {
		this.metodoSelecionarProcurador = metodoSelecionarProcurador;
	}

	public String getNomeProcurador() {
		return nomeProcurador;
	}

	public void setNomeProcurador(String nomeProcurador) {
		this.nomeProcurador = nomeProcurador;
	}
	
}