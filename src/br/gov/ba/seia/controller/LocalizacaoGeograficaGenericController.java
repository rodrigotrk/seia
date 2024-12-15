package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import com.vividsolutions.jts.geom.Point;

import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.CaepogDefinicaoCampo;
import br.gov.ba.seia.entity.CaepogLocacao;
import br.gov.ba.seia.entity.CaepogPoco;
import br.gov.ba.seia.entity.CerhLocalizacaoGeografica;
import br.gov.ba.seia.entity.ClassificacaoSecaoGeometrica;
import br.gov.ba.seia.entity.CoordenadaGeografica;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.DeclaracaoInexigibilidadeInfoAbastecimento;
import br.gov.ba.seia.entity.DeclaracaoIntervencaoApp;
import br.gov.ba.seia.entity.DeclaracaoQueimaControladaImovel;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.FceAquiculturaLicencaLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceAquiculturaLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceEnergia;
import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.FceLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceProspeccao;
import br.gov.ba.seia.entity.FceSaaLocalizacaoGeograficaDadosConcedidos;
import br.gov.ba.seia.entity.FceSaaLocalizacaoGeograficaElevatoriaBruta;
import br.gov.ba.seia.entity.FceSaaLocalizacaoGeograficaElevatoriaTratada;
import br.gov.ba.seia.entity.FceSaaLocalizacaoGeograficaEta;
import br.gov.ba.seia.entity.FceSaaLocalizacaoGeograficaReservatorio;
import br.gov.ba.seia.entity.FceTurismoLocalizacaoGeografica;
import br.gov.ba.seia.entity.GeoReferenciavel;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.LacTransporte;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.ParamPersistDadoGeo;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.ProcessoDnpm;
import br.gov.ba.seia.entity.ProspecaoDetalhamento;
import br.gov.ba.seia.entity.RegistroFlorestaProducaoImovelPlantio;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.SistemaCoordenada;
import br.gov.ba.seia.entity.TipoProrrogacaoPrazoValidade;
import br.gov.ba.seia.entity.TorreAnemometrica;
import br.gov.ba.seia.entity.generic.AbstractEntityLocalizacaoGeografica;
import br.gov.ba.seia.entity.generic.GenericLocalizacaoGeograficaClass;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.ConfigEnum;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.SistemaCoordenadaEnum;
import br.gov.ba.seia.enumerator.TipoLocalizacaoGeograficaPerguntaEnum;
import br.gov.ba.seia.enumerator.URLEnum;
import br.gov.ba.seia.enumerator.ValidacaoShapeEnum;
import br.gov.ba.seia.exception.SeiaException;
import br.gov.ba.seia.facade.AdicionarMunicipiosServiceFacade;
import br.gov.ba.seia.facade.DeclaracaoQueimaControladaFacade;
import br.gov.ba.seia.facade.LocalizacaoGeograficaServiceFacade;
import br.gov.ba.seia.facade.PesquisaMineralFacade;
import br.gov.ba.seia.facade.RFPFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.CaepogDefinicaoCampoService;
import br.gov.ba.seia.service.CaepogLocacaoService;
import br.gov.ba.seia.service.CaepogPocoService;
import br.gov.ba.seia.service.ClassificacaoSecaoGeometricaService;
import br.gov.ba.seia.service.DatumService;
import br.gov.ba.seia.service.DeclaracaoInexigibilidadeInfoAbastecimentoService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.ImovelService;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.service.OutorgaLocalizacaoGeograficaService;
import br.gov.ba.seia.service.ParamPersistDadoGeoService;
import br.gov.ba.seia.service.PerguntaRequerimentoService;
import br.gov.ba.seia.service.UsuarioAutorizacaoGeobahiaService;
import br.gov.ba.seia.service.ValidacaoGeoSeiaService;
import br.gov.ba.seia.service.VerticeService;
import br.gov.ba.seia.util.BigDecimalUtil;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.GeoBahiaUtil;
import br.gov.ba.seia.util.GeoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemLacFceUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.PostgisUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

@Named("localizacaoGeograficaGenericController")
@ViewScoped
public class LocalizacaoGeograficaGenericController extends SeiaControllerAb {

	@Inject
	private FceTurismoController fceTurismoController;
	@Inject
	private FceSesController sesController;
	
	@EJB
	private VerticeService verticeService;
	@EJB
	private ImovelService imovelService;
	@EJB
	private EmpreendimentoService empreendimentoService;
	@EJB
	private ParamPersistDadoGeoService paramPersistDadoGeoService;
	@EJB
	private ValidacaoGeoSeiaService validacaoGeoSeiaService;
	@EJB
	private DatumService serviceDatum;
	@EJB
	private ClassificacaoSecaoGeometricaService serviceClassifSecGeometrica;
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	@EJB
	private PerguntaRequerimentoService perguntaRequerimentoService;
	@EJB
	private OutorgaLocalizacaoGeograficaService outorgaLocalizacaoGeograficaService;
	@EJB
	private LocalizacaoGeograficaServiceFacade facadeLocalizacaoGeografica;
	@EJB
	private CaepogDefinicaoCampoService caepogDefinicaoCampoService;
	@EJB
	private CaepogLocacaoService caepogLocacaoService;
	@EJB
	private CaepogPocoService caepogPocoService;
	@EJB
	private AdicionarMunicipiosServiceFacade adicionarMunicipiosServiceFacade;
	@EJB
	private PesquisaMineralFacade pesquisaMineralFacade;
	@EJB
	private DeclaracaoQueimaControladaFacade declaracaoQueimaControladaFacade;
	@EJB
	private RFPFacade rfpFacade;
	@EJB
	private DeclaracaoInexigibilidadeInfoAbastecimentoService declaracaoInexigibilidadeInfoAbastecimentoService;
	@EJB
	private UsuarioAutorizacaoGeobahiaService usuarioAutorizacaoGeobahiaService;
	
	private boolean editandoVertice;
	private boolean analiseTecnicaDeFce;
	private boolean desabilitarVertices = true;
	private boolean disableParaVisualizar;
	private boolean firstTime;
	private boolean shapeTipoPonto = false;
	private boolean disableParaExcluir;
	
	private GeoReferenciavel geoReferenciavel;
	private Collection<SistemaCoordenada> datums;
	private SistemaCoordenada datumSelecionado;
	private List<ClassificacaoSecaoGeometrica> listaSecaoGeomerica;
	private ClassificacaoSecaoGeometrica secaoGeometricaSelecionada;
	private LocalizacaoGeografica localizacaoGeograficaSelecionada = new LocalizacaoGeografica();
	private Imovel imovel;
	private Empreendimento empreendimento;
	private DadoGeografico vertice;
	private DadoGeografico verticeExclusao;
	private StreamedContent arquivoBaixar;
	
	private List<String> listaArquivo;
	private String coordenadaPontoxy;
	private String selectedModoCoordenada = "1";
	private String grausLatitude;
	private String minutosLatitude;
	private String segundosLatitude;
	private String grausLongitude;
	private String minutosLongitude;
	private String segundosLongitude;
	private String fracaoGrauLatitude;
	private String fracaoGrauLongitude;
	private String latitudeUTM;
	private String longitudeUTM;
	private String caminhoArquivo;
	private String arqDeExclusao;
	private String fracaoGrauLatitudeDecimal;
	private String fracaoGrauLongitudeDecimal;
	/**
	 * Variável que vai ser preenchida com o id do componente que deve ser atualizado na página xhtml.
	 * @see editarLocGeoComActionEvent(event);
	 */
	private String idDoComponenteParaSerAtualizado;
	private MetodoUtil metodoAtualizarExterno;
	
	private Boolean isPnlVerticesHabilitado;
	private Boolean isUplShapefile;
	private Boolean existeTheGeom;
	private Boolean modoVisualizacao;
	private Boolean insereVertices;
	private Boolean desabilitaIncluirVerticeLocGeoImovel;
	private Boolean isPerfuracaoPocoOuCaptacaoOuAquicuturaEmRede;
	private Boolean isLicenciamentoAquicutura;
	private Boolean isASV;
	private Boolean isNaoValidaMunicipio;
	private Boolean isIntervencao;
	private Boolean habilitaIncluirLoc = false;
	private Boolean temArquivo;
	private Boolean mostraLista = false;
	private Boolean showIncluirVertice;
	private Boolean pontoUnico = false;
	
	private Object objetoLocalizacao;
	
	private Integer tipoSecaoGeometrica = 0;
	private Integer requerimento;
	private Integer zone;
	private BigDecimal areaSupressao;
	private Integer tipoSecaoGeometricaValidacao = 0;
	
	private boolean indPct; 

	@PostConstruct
	public void init() {
		iniciarVariaveisDeTela();
		if (!Util.isNullOuVazio(ContextoUtil.getContexto().getVisualizandoImovel()) && ContextoUtil.getContexto().getVisualizandoImovel()) {
			this.modoVisualizacao = true;
		} else {
			this.modoVisualizacao = false;
		}
	}

	
	/**
	 * Exibir localização geografica no Geo Bahia
	 * 
	 * */
	public String visualizarLocalizacao(LocalizacaoGeografica locGeo) {
		if(isLocalizacaoGeograficaSalva(locGeo)){
			return criarURLToVisualizarShape(locGeo);
		}
		return "";
	}

	protected boolean isLocalizacaoGeograficaSalva(LocalizacaoGeografica locGeo) {
		try{
		 	return !Util.isNullOuVazio(locGeo);
		 	//&& !Util.isNull(getMineracaoFacade().retornarGeometriaShapeByLocalizacaoGeografica(locGeo)
		}catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("")); 
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public static String criarURLToVisualizarShape(Object obj) {
		StringBuilder lUrl = new StringBuilder();
		StringBuilder lReturn = new StringBuilder();
		lUrl.append(GeoBahiaUtil.obterUrlGeoBahia(paraVisualizarEmTela(obj)));

		lReturn
			.append("window.open('")
			.append(lUrl.toString())
			.append("');");
		
		return lReturn.toString();
	}
	
	private static String paraVisualizarEmTela(Object object) {
		
		String param = "";
		String paramId = "";
		
		if(object instanceof LocalizacaoGeografica) {
			LocalizacaoGeografica loc = (LocalizacaoGeografica) object;
			paramId = "idloc="+loc.getIdeLocalizacaoGeografica().toString()+"&tipo=1";
		}
		
		return "index_seia" + param +".php?acao=view&" + paramId;
	}
	
	public void buscaLocalizacaoInserida(){
		if(objetoLocalizacao instanceof ImovelRural){
			((ImovelRural) objetoLocalizacao).setIdeLocalizacaoGeograficaPct(localizacaoGeograficaSelecionada);
			atualizarPagina();
		}else if(objetoLocalizacao instanceof PerguntaRequerimento){
			((PerguntaRequerimento) objetoLocalizacao).setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
		} 
		else if(objetoLocalizacao instanceof LacTransporte){
			((LacTransporte) objetoLocalizacao).setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
		}
		else if(objetoLocalizacao instanceof TipoProrrogacaoPrazoValidade){
			((TipoProrrogacaoPrazoValidade) objetoLocalizacao).setLocalizacaoGeograficaTransient(localizacaoGeograficaSelecionada);
		}
		else if(objetoLocalizacao instanceof OutorgaLocalizacaoGeografica){
			OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica = (OutorgaLocalizacaoGeografica) objetoLocalizacao;
			if(outorgaLocalizacaoGeografica.isLocalizacaoFinal()){
				outorgaLocalizacaoGeografica.setIdeLocalizacaoGeograficaFinal(localizacaoGeograficaSelecionada);
			}
			else{
				outorgaLocalizacaoGeografica.setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
			}
		}
		else if(objetoLocalizacao instanceof FceAquiculturaLocalizacaoGeografica){
			((FceAquiculturaLocalizacaoGeografica) objetoLocalizacao).setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
		}
		else if(objetoLocalizacao instanceof FceTurismoLocalizacaoGeografica){
			((FceTurismoLocalizacaoGeografica) objetoLocalizacao).setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
			if(fceTurismoController.isZonaUrbana()){
				fceTurismoController.atualizarAreaShapeApp();
			}
		}
		else if(objetoLocalizacao instanceof ProcessoDnpm){
			((ProcessoDnpm) objetoLocalizacao).setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
		}
		else if(objetoLocalizacao instanceof FceProspeccao){
			((FceProspeccao) objetoLocalizacao).setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
		}
		else if(objetoLocalizacao instanceof DeclaracaoIntervencaoApp){
			if(Util.isNullOuVazio(localizacaoGeograficaSelecionada.getDadoGeograficoCollection())) {
				localizacaoGeograficaSelecionada = new LocalizacaoGeografica();
			}
			((DeclaracaoIntervencaoApp) objetoLocalizacao).setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
			atualizarPagina();
		}
		else if(objetoLocalizacao instanceof RegistroFlorestaProducaoImovelPlantio){
			((RegistroFlorestaProducaoImovelPlantio) objetoLocalizacao).setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
		}
		else if(objetoLocalizacao instanceof FceEnergia && temDadoGeografico()){
			((FceEnergia) objetoLocalizacao).setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		}
		objetoLocalizacao = null;
		localizacaoGeograficaSelecionada = new LocalizacaoGeografica();
		limparLocalizacaoGeografSelecionada();
	}
	
	public void fecharDialog() {
		Html.esconder("dlgLocalizacaoGeograficaGeneric");
	}
	
	private boolean temDadoGeografico(){
		return !existeTheGeom && !getLocalizacaoGeografSelecTemDadoGeografico();
	}
	private void iniciarVariaveisDeTela() {
		tipoSecaoGeometrica = 0;
		disableParaVisualizar = false;
		datumSelecionado = null;
		isPnlVerticesHabilitado = false;
		isUplShapefile = false;
		vertice = new DadoGeografico();
		insereVertices = false;
		desabilitaIncluirVerticeLocGeoImovel = true;
		temArquivo = false;
		listaArquivo = new ArrayList<String>();
		showIncluirVertice = true;
		firstTime = true;
		pontoUnico = false;
		isPerfuracaoPocoOuCaptacaoOuAquicuturaEmRede = false;
		isASV = false;
		isNaoValidaMunicipio = false;
		analiseTecnicaDeFce = false;
		editandoVertice = false;
		areaSupressao = null;
		geoReferenciavel = null;
		existeTheGeom = null;
		habilitaIncluirLoc = false;
		idDoComponenteParaSerAtualizado = null;
		disableParaExcluir = true;
		tipoSecaoGeometricaValidacao =0;
		isLicenciamentoAquicutura = false;
	}

	public void cancelar(){
		if(objetoLocalizacao instanceof PerguntaRequerimento){
			((PerguntaRequerimento) objetoLocalizacao).setIdeLocalizacaoGeografica(null);
		}
		else if(objetoLocalizacao instanceof OutorgaLocalizacaoGeografica){
			((OutorgaLocalizacaoGeografica) objetoLocalizacao).setIdeLocalizacaoGeografica(null);
		}
		else if(objetoLocalizacao instanceof FceTurismoLocalizacaoGeografica){
			((FceTurismoLocalizacaoGeografica) objetoLocalizacao).setIdeLocalizacaoGeografica(null);
		}
		else if(objetoLocalizacao instanceof FceLocalizacaoGeografica){
			((FceLocalizacaoGeografica) objetoLocalizacao).setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		}
		else if(objetoLocalizacao instanceof FceAquiculturaLocalizacaoGeografica){
			((FceAquiculturaLocalizacaoGeografica) objetoLocalizacao).setIdeLocalizacaoGeografica(null);
		}
		else if(objetoLocalizacao instanceof FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica){
			((FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica) objetoLocalizacao).setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		}
		else if(objetoLocalizacao instanceof FceAquiculturaLicencaLocalizacaoGeografica){
			((FceAquiculturaLicencaLocalizacaoGeografica) objetoLocalizacao).setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		}
		else if(objetoLocalizacao instanceof CaepogDefinicaoCampo){
			((CaepogDefinicaoCampo) objetoLocalizacao).setIdeLocalizacaoGeografica(null);
		}
		else if(objetoLocalizacao instanceof CaepogLocacao){
			((CaepogLocacao) objetoLocalizacao).setIdeLocalizacaoGeografica(null);
		}
		else if(objetoLocalizacao instanceof CaepogPoco){
			((CaepogPoco) objetoLocalizacao).setIdeLocalizacaoGeografica(null);
		}
		else if(objetoLocalizacao instanceof GenericLocalizacaoGeograficaClass){
			((AbstractEntityLocalizacaoGeografica) objetoLocalizacao).limparLocalizacaoGeografica();
		}
		
		else if(objetoLocalizacao instanceof DeclaracaoIntervencaoApp){
			((DeclaracaoIntervencaoApp) objetoLocalizacao).setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		}
		else if(objetoLocalizacao instanceof CerhLocalizacaoGeografica){
			((CerhLocalizacaoGeografica) objetoLocalizacao).setIdeLocalizacaoGeografica(null);
		}
		else if(objetoLocalizacao instanceof FceSaaLocalizacaoGeograficaDadosConcedidos){
			((FceSaaLocalizacaoGeograficaDadosConcedidos) objetoLocalizacao).setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		}
		else if(objetoLocalizacao instanceof FceSaaLocalizacaoGeograficaElevatoriaBruta){
			((FceSaaLocalizacaoGeograficaElevatoriaBruta) objetoLocalizacao).setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		}
		else if(objetoLocalizacao instanceof FceSaaLocalizacaoGeograficaEta){
			((FceSaaLocalizacaoGeograficaEta) objetoLocalizacao).setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		}
		else if(objetoLocalizacao instanceof FceSaaLocalizacaoGeograficaElevatoriaTratada){
			((FceSaaLocalizacaoGeograficaElevatoriaTratada) objetoLocalizacao).setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		}
		else if(objetoLocalizacao instanceof FceSaaLocalizacaoGeograficaReservatorio){
			((FceSaaLocalizacaoGeograficaReservatorio) objetoLocalizacao).setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		}
		
		else if(objetoLocalizacao instanceof LocalizacaoGeografica){
			if(sesController.getFceSesElevatoriaDTO() != null && sesController.getFceSesElevatoriaDTO().getLocalizacaoElevatoria() != null){
				if(sesController.getFceSesElevatoriaDTO().getLocalizacaoElevatoria().equals(objetoLocalizacao)){
					sesController.getFceSesElevatoriaDTO().setLocalizacaoElevatoria(new LocalizacaoGeografica());
				}
			}
			if(sesController.getFceSesElevatoriaDTO() != null && sesController.getFceSesElevatoriaDTO().getLocalizacaoExtravazamento() != null){
				if(sesController.getFceSesElevatoriaDTO().getLocalizacaoExtravazamento().equals(objetoLocalizacao)){
					sesController.getFceSesElevatoriaDTO().setLocalizacaoExtravazamento(new LocalizacaoGeografica());
				}
			}
			if(sesController.getFceSesDadosEstacaoTratamentoEsgoto() != null && sesController.getFceSesDadosEstacaoTratamentoEsgoto().getIdeLocalizacaoGeografica() != null){
				if(sesController.getFceSesDadosEstacaoTratamentoEsgoto().getIdeLocalizacaoGeografica().equals(objetoLocalizacao)){
					sesController.getFceSesDadosEstacaoTratamentoEsgoto().setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
				}
			}
			objetoLocalizacao = new LocalizacaoGeografica();
		}
		excluirLocGeoSelecionada();
		atualizarPagina();
		limparLocalizacaoGeografSelecionada();
	}

	/**
	 *
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 28/11/2014
	 */
	public void excluirLocGeoSelecionada() {
		try {
			if(!editandoVertice && !Util.isNullOuVazio(this.localizacaoGeograficaSelecionada)) {
				localizacaoGeograficaService.excluirByIdLocalizacaoGeografica(this.localizacaoGeograficaSelecionada);
				this.localizacaoGeograficaSelecionada = new LocalizacaoGeografica();
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_excluir") + " a Localização Geográfica.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void listarSecaoGeometrica() {
		try {
			inicializarListaClassificacaoSecaoGeometrica();
			if(isSecaoGeometricaPonto()){
				listaSecaoGeomerica.add(serviceClassifSecGeometrica.carregar(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId()));
			}
			else if(isSecaoGeometricaShape()){
				listaSecaoGeomerica.add(serviceClassifSecGeometrica.carregar(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId()));
			}
			else if(tipoSecaoGeometrica == TipoLocalizacaoGeograficaPerguntaEnum.SHAPE_E_DESENEHO.getIde().intValue() ){
				listaSecaoGeomerica = serviceClassifSecGeometrica.listarClassificacaoSecaoGeometrica();
				listaSecaoGeomerica.remove(1);
			}
			else {
				listaSecaoGeomerica = serviceClassifSecGeometrica.listarClassificacaoSecaoGeometrica();
				listaSecaoGeomerica.remove(2);
			}
			verificarSecaoGeometrica();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @return
	 * @since 01/02/2016
	 */
	private boolean isSecaoGeometricaShape() {
		return tipoSecaoGeometrica == ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId().intValue();
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @return
	 * @since 01/02/2016
	 */
	private boolean isSecaoGeometricaPonto() {
		return tipoSecaoGeometrica == ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId().intValue();
	}

	private void inicializarListaClassificacaoSecaoGeometrica() {
		if(Util.isNull(listaSecaoGeomerica)){
			listaSecaoGeomerica = new ArrayList<ClassificacaoSecaoGeometrica>();
		} else {
			listaSecaoGeomerica.clear();
		}
	}

	public void carregarTela(){
		if(Util.isNull(localizacaoGeograficaSelecionada)) {
			localizacaoGeograficaSelecionada = new LocalizacaoGeografica();
		}
		listarSecaoGeometrica();
		listarSistemaCoordenada();
	}

	public void carregarTelaSistemaCoordenadasSirgas2000(){
	      if(Util.isNull(localizacaoGeograficaSelecionada)) {
	    	  localizacaoGeograficaSelecionada = new LocalizacaoGeografica();
	    	  }
	    	  listarSecaoGeometrica();
	    	  getSistemaCoordenadaSirgas2000();
	    	}

	public void getSistemaCoordenadaSirgas2000() {
		try {
			if(!Util.isNullOuVazio(datums)) {
				datums.removeAll(datums);
			}
			datums = serviceDatum.listarDatumSirgas2000comUTM();
			datumSelecionado = serviceDatum.carregar(4);
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " Sistema de Coordenadas."); 
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
	}
		 

	
	private void verificarSecaoGeometrica() {
		if(isDesabilitaSecaoGeometrica()){
			localizacaoGeograficaSelecionada.setIdeClassificacaoSecao(this.listaSecaoGeomerica.get(0));
		}
	}

	public void limparLocalizacaoGeografSelecionada(){
		try {
			if(!Util.isNull(metodoAtualizarExterno)) {
				metodoAtualizarExterno.executeMethod();
				atualizarPagina();
			}
			disableParaVisualizar = false;
			datumSelecionado = null;
			iniciarVariaveisDeTela();
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}


	}

	public void visualizarLocalizacaoGeografica(){
		try {
			disableParaVisualizar = true;
			abrirDlgLocalizacaoGeograficaGeneric();
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao tentar visualizar.");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void editarLocGeoComActionEvent(ActionEvent event){
		idDoComponenteParaSerAtualizado = (String) event.getComponent().getAttributes().get("ideToUpdate");
		if(Util.isNullOuVazio(idDoComponenteParaSerAtualizado)){
			idDoComponenteParaSerAtualizado = event.getComponent().getParent().getParent().getParent().getParent().getClientId();
		}
		localizacaoGeograficaSelecionada = (LocalizacaoGeografica) event.getComponent().getAttributes().get("ideLocalizacaoGeografica");
		if(tipoSecaoGeometrica != 0 && tipoSecaoGeometricaValidacao == 0){
			tipoSecaoGeometricaValidacao = tipoSecaoGeometrica;
		}
		tipoSecaoGeometrica = (Integer) event.getComponent().getAttributes().get("ideTipoInsercao");
		requerimento = (Integer) event.getComponent().getAttributes().get("ideRequerimento");
		if(Util.isNull((Boolean) event.getComponent().getAttributes().get("analiseTecnica"))){
			analiseTecnicaDeFce = false;
		}
		else {
			analiseTecnicaDeFce = (Boolean) event.getComponent().getAttributes().get("analiseTecnica");
		}
		objetoLocalizacao = event.getComponent().getAttributes().get("objetoLocalizacao");
		editarLocalizacaoGeografica();
	}
	
	public void editarLocalizacaoGeografica(){
		try {
			editandoVertice = true;
			listarSecaoGeometrica();
			abrirDlgLocalizacaoGeograficaGeneric();
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao tentar editar.");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private void abrirDlgLocalizacaoGeograficaGeneric() throws Exception {
		datumSelecionado = localizacaoGeograficaSelecionada.getIdeSistemaCoordenada();
		if(tipoSecaoGeometrica != 0 && tipoSecaoGeometricaValidacao == 0){
			tipoSecaoGeometricaValidacao = tipoSecaoGeometrica;
		}
		tipoSecaoGeometrica = localizacaoGeograficaSelecionada.getIdeClassificacaoSecao().getIdeClassificacaoSecao();
		
		if(Util.isNullOuVazio(localizacaoGeograficaSelecionada.getDadoGeograficoCollection())){
			localizacaoGeograficaSelecionada.setDadoGeograficoCollection(
					localizacaoGeograficaService.listarDadoGeografico(
							localizacaoGeograficaSelecionada, localizacaoGeograficaSelecionada.getIdeClassificacaoSecao().getIdeClassificacaoSecao()));
		}
		defineVariaveisDeExibicao();
	}
	public void excluirGeoPlantiu() {
		this.excluirLocGeo();
		Html.atualizar("formPlantio:dtImovelPlantio");
		Html.esconder("confirmExcluirGeo");
	}
	/**
	 * @author micaelcoutinho
	 * Exclui a localização geográfica do objeto setado na variável objetoLocalizacao, seta no na localização do objeto atualiza o mesmo e exlui a localização.
	 */
	public void excluirLocGeo() {
		try {
			LocalizacaoGeografica locGeoExcluir = new LocalizacaoGeografica();
			//IMPLEMENTAR INSTANCE OF E REMOVER O Q FOR NECESSARIO
			if(objetoLocalizacao instanceof PerguntaRequerimento){
				PerguntaRequerimento pergReq = ((PerguntaRequerimento) objetoLocalizacao);
				locGeoExcluir = pergReq.getIdeLocalizacaoGeografica();
				if(!Util.isNullOuVazio(pergReq.getIdePerguntaRequerimento())){
					Collection<PerguntaRequerimento> perguntas = perguntaRequerimentoService.buscarPerguntaRequerimentoByIdeLocalizacaoGeografica(pergReq.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
					for (PerguntaRequerimento perguntaRequerimento : perguntas) {
						perguntaRequerimento.setIdeLocalizacaoGeografica(null);
						perguntaRequerimentoService.atualizarPerguntaReq(perguntaRequerimento);
					}
				}
				((PerguntaRequerimento) objetoLocalizacao).setIdeLocalizacaoGeografica(null);
			}
			else if(objetoLocalizacao instanceof OutorgaLocalizacaoGeografica){
				OutorgaLocalizacaoGeografica outLocGeo = ((OutorgaLocalizacaoGeografica) objetoLocalizacao);
				locGeoExcluir = outLocGeo.getIdeLocalizacaoGeografica();
				if(!Util.isNullOuVazio(locGeoExcluir)){
					if(!Util.isNullOuVazio(outLocGeo.getIdeOutorga()) && !Util.isNullOuVazio(outLocGeo.getIdeOutorga().getIdeOutorga())){
						outLocGeo.setIdeLocalizacaoGeografica(null);
						outorgaLocalizacaoGeograficaService.salvarAtualizar(outLocGeo);
					}
				}
				((OutorgaLocalizacaoGeografica) objetoLocalizacao).setIdeLocalizacaoGeografica(null);
			}
			else if(objetoLocalizacao instanceof LocalizacaoGeografica){
				locGeoExcluir = localizacaoGeograficaSelecionada;
				localizacaoGeograficaSelecionada.setIdeLocalizacaoGeografica(null);
			}
			else if(objetoLocalizacao instanceof CaepogDefinicaoCampo){
				CaepogDefinicaoCampo cdc = ((CaepogDefinicaoCampo) objetoLocalizacao);
				locGeoExcluir = cdc.getIdeLocalizacaoGeografica();
				
				if(!Util.isNullOuVazio(cdc)) {
					cdc.setIdeLocalizacaoGeografica(null);
					caepogDefinicaoCampoService.atualizarValidando(cdc);
				}
				
				((CaepogDefinicaoCampo) objetoLocalizacao).setIdeLocalizacaoGeografica(null);
			}
			else if(objetoLocalizacao instanceof CaepogLocacao){
				CaepogLocacao cl = ((CaepogLocacao) objetoLocalizacao);
				locGeoExcluir = cl.getIdeLocalizacaoGeografica();
				
				if(!Util.isNullOuVazio(cl)) {
					cl.setIdeLocalizacaoGeografica(null);
					caepogLocacaoService.salvar(cl);
				}
				
				((CaepogLocacao) objetoLocalizacao).setIdeLocalizacaoGeografica(null);
			}
			else if(objetoLocalizacao instanceof CaepogPoco){
				CaepogPoco cp = ((CaepogPoco) objetoLocalizacao);
				locGeoExcluir = cp.getIdeLocalizacaoGeografica();
				
				if(!Util.isNullOuVazio(cp)) {
					cp.setIdeLocalizacaoGeografica(null);
					caepogPocoService.salvar(cp);
				}
				
				((CaepogPoco) objetoLocalizacao).setIdeLocalizacaoGeografica(null);
			}
			else if(objetoLocalizacao instanceof FceProspeccao){
				FceProspeccao fceProspeccao = (FceProspeccao) objetoLocalizacao;
				fceProspeccao.setIdeLocalizacaoGeografica(null);
			}
			else if(objetoLocalizacao instanceof CerhLocalizacaoGeografica){
				CerhLocalizacaoGeografica cerhLocalizacaoGeografica = (CerhLocalizacaoGeografica) objetoLocalizacao;
				cerhLocalizacaoGeografica.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
			}
			
			if(objetoLocalizacao instanceof DeclaracaoQueimaControladaImovel){
				DeclaracaoQueimaControladaImovel dqcI = ((DeclaracaoQueimaControladaImovel) objetoLocalizacao);
				locGeoExcluir = dqcI.getIdeLocalizacaoGeografica();
				
				if(!Util.isNullOuVazio(dqcI)) {
					dqcI.setIdeLocalizacaoGeografica(null);
					declaracaoQueimaControladaFacade.salvarDeclaracaoQueimaControladaImovel(dqcI);
				}
				
				((DeclaracaoQueimaControladaImovel) objetoLocalizacao).setIdeLocalizacaoGeografica(null);
				
				localizacaoGeograficaService.excluirTudoPorLocalizacaoGeografica(locGeoExcluir);
			}
			
			if(objetoLocalizacao instanceof RegistroFlorestaProducaoImovelPlantio){
				
				RegistroFlorestaProducaoImovelPlantio rf = ((RegistroFlorestaProducaoImovelPlantio) objetoLocalizacao);
				locGeoExcluir = rf.getIdeLocalizacaoGeografica();
				
				rf.setValAreaPlantio(BigDecimal.ZERO);
				if(!Util.isNullOuVazio(rf.getIdeRegistroFlorestaImovelPlantio())) {
					rf.setIdeLocalizacaoGeografica(null);
					rfpFacade.salvarRegistroFlorestaProducaoImovelPlantio(rf);
					rfpFacade.salvarAtoDeclaratorio(rf);
				}
				
				((RegistroFlorestaProducaoImovelPlantio) objetoLocalizacao).setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
				
				localizacaoGeograficaService.excluirTudoPorLocalizacaoGeografica(locGeoExcluir);
			
				Html.atualizar("formFlorestaProducaoPlantada:pnlDadosPlantio");
				Html.esconder("confirmExcluirGeo");
				
			}
			
			if (objetoLocalizacao instanceof DeclaracaoInexigibilidadeInfoAbastecimento) {

				DeclaracaoInexigibilidadeInfoAbastecimento ab = (DeclaracaoInexigibilidadeInfoAbastecimento) objetoLocalizacao;

				LocalizacaoGeografica ideLocalizacaoGeografica = ab.getLocalizacaoGeografica();

				ab.setLocalizacaoGeografica(null);
				
				localizacaoGeograficaService.excluirByIdLocalizacaoGeografica(ideLocalizacaoGeografica);
				
				ab.setLocalizacaoGeografica(new LocalizacaoGeografica());
			}
			
			if(objetoLocalizacao instanceof TorreAnemometrica){
				TorreAnemometrica torre = ((TorreAnemometrica)objetoLocalizacao);
				locGeoExcluir = torre.getIdeLocalizacaoGeografica();
				torre.setIdeLocalizacaoGeografica(null);
				localizacaoGeograficaService.excluirTudoPorLocalizacaoGeografica(locGeoExcluir);
			}

			localizacaoGeograficaService.excluirByIdLocalizacaoGeografica(locGeoExcluir);
			JsfUtil.addSuccessMessage("Exclusão efetuada com Sucesso.");
			
			atualizarPagina();
		} catch (Exception exception) {
			JsfUtil.addErrorMessage("Erro ao excluir localização geográfica");
		}
	}

	public void salvarLocalizacaoGeografica() {
		try {
			montarLocalizacao();
			
			if(tipoSecaoGeometrica != 0 && tipoSecaoGeometricaValidacao == 0){
				tipoSecaoGeometricaValidacao = tipoSecaoGeometrica;
			}
			
			if(!isDesabilitaSecaoGeometrica()){
				this.tipoSecaoGeometrica = localizacaoGeograficaSelecionada.getIdeClassificacaoSecao().getIdeClassificacaoSecao();
			}
			
			if (!Util.isNullOuVazio(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica())) {
				localizacaoGeograficaService.salvarOuAtualizarLocalizacaoGeografica(this.localizacaoGeograficaSelecionada);
			}else {
				localizacaoGeograficaService.salvarlocalizacao(localizacaoGeograficaSelecionada);
			}
			defineVariaveisDeExibicao();

			ContextoUtil.getContexto().setLocalizacaoSalva(true);
			desabilitarVertices = false;
			
			JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso.");
			this.listaArquivo.clear();
			habilitaIncluirLoc = true;
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Erro ao salvar localização geográfica");
		}
	}

	public void salvarLocalizacaoGeograficaGenericImovelRural() {
		try {
			montarLocalizacao();
			if (!Util.isNullOuVazio(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica())) {
				localizacaoGeograficaService.salvarOuAtualizarLocalizacaoGeografica(this.localizacaoGeograficaSelecionada);
			}else {
				localizacaoGeograficaService.salvarlocalizacao(localizacaoGeograficaSelecionada);
			}
			ContextoUtil.getContexto().setLocalizacaoSalva(true);
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}
	
	private void defineVariaveisDeExibicao() { 
		if(isTemLocGeoPonto()){
			existeTheGeom = false;
		}
	}

	public void montarLocalizacao() {
		try {
			localizacaoGeograficaSelecionada.setDtcCriacao(new Date());
			localizacaoGeograficaSelecionada.setDtcExclusao(null);
			localizacaoGeograficaSelecionada.setIndExcluido(false);
			localizacaoGeograficaSelecionada.setIdeSistemaCoordenada(serviceDatum.carregar(this.datumSelecionado.getIdeSistemaCoordenada()));
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao obter Sistema de Coordenadas");
		}
	}

	public Empreendimento retonarEmpreendimento() throws Exception{
		if(!Util.isNull(empreendimento)) {
			return empreendimento;
		}
		else {
			empreendimento = (Empreendimento) getAtributoSession("EMPREENDIMENTO_SESSAO");
			if(Util.isNull(empreendimento)){
				empreendimento = empreendimentoService.getEmpreendimentoByRequerimento(new Requerimento(requerimento));	
			}
			return empreendimento;
		}
	}
	
	private boolean validarMunicipios(String latitude, String longitude, String srid) throws Exception{
		Empreendimento emp = retonarEmpreendimento();
		Collection<Municipio> listaMunicipio = adicionarMunicipiosServiceFacade.listarTodosMunicipiosEnvolvidos(emp.getIdeEmpreendimento());
		if(!Util.isNullOuVazio(listaMunicipio) && listaMunicipio.size() > 1) {
			return localizacaoGeograficaService.validaVerticeMunicipio(latitude, longitude, listaMunicipio, srid);
		}
		return localizacaoGeograficaService.validaVerticeMunicipio(latitude, longitude, emp, srid);
	}

	public void salvarVertice() {
		
		if(tipoSecaoGeometrica != 0 && tipoSecaoGeometricaValidacao == 0){
			tipoSecaoGeometricaValidacao = tipoSecaoGeometrica;
		}
		
		if(selectedModoCoordenada.equals("1")){
			calculaFracaoGrauLatitude(null);
			calculaFracaoGrauLongitude(null);	
		} 
		else if(selectedModoCoordenada.equals("2")){
			fracaoGrauLatitude = "-"+fracaoGrauLatitudeDecimal;
			fracaoGrauLongitude = "-"+fracaoGrauLongitudeDecimal;
		}

		if (!Util.isNullOuVazio(this.fracaoGrauLatitude) && !Util.isNullOuVazio(this.fracaoGrauLongitude)) {
			this.localizacaoGeograficaSelecionada.setPontoLatitude(this.fracaoGrauLatitude);
			this.localizacaoGeograficaSelecionada.setPontoLongitude(this.fracaoGrauLongitude);

			this.localizacaoGeograficaSelecionada.setLatitudeInicial(this.fracaoGrauLatitude);
			this.localizacaoGeograficaSelecionada.setLongitudeInicial(this.fracaoGrauLongitude);

			
			if(!indPct){
				
				try {
					if (isRealizarVerificacaoMunicipio() && !validarMunicipios(this.fracaoGrauLatitude, this.fracaoGrauLongitude, null)) {
						this.localizacaoGeograficaSelecionada.setPontoLatitude(null);
						this.localizacaoGeograficaSelecionada.setPontoLongitude(null);
						MensagemUtil.alerta("Coordenada informada está fora dos limites da Localidade do empreendimento.");
						return;
					}
				} catch (Exception e) {
					JsfUtil.addWarnMessage("Não foi possível validar o vértice Informado com a localidade, o sistema não conseguiu encontrar a localidade do empreendimento.");
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					return;
				}
			}

			try {

				Point ponto = PostgisUtil.criarPonto(Double.parseDouble(this.fracaoGrauLatitude.replace(',', '.')),	Double.parseDouble(this.fracaoGrauLongitude.replace(',', '.')));
				this.vertice.setCoordGeoNumerica(ponto.toString());
				this.vertice.setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
				verticeService.salvarOuAtualizarVertice(this.vertice);

				Integer idDosParametros = paramPersistDadoGeoService.salvarParamsEPersistindoVerticeTheGeom(vertice, localizacaoGeograficaSelecionada.getIdeSistemaCoordenada());
				if(Util.isNullOuVazio(idDosParametros)) {
					System.out.println("ERRO AO SALVAR PARAMETROS");
				}
				else {
					System.out.println("PARAMETROS persistido com sucesso!");
					boolean podePersistir = true;

					// #8541
					if(objetoLocalizacao instanceof ProspecaoDetalhamento){
						ProspecaoDetalhamento prospecaoDetalhamento = (ProspecaoDetalhamento) objetoLocalizacao;
						for (ProspecaoDetalhamento detalhamento : prospecaoDetalhamento.getProcessoDnpmProspecao().getProspecaoDetalhamentos()) {
							if(pesquisaMineralFacade.isMesmaCoordenada(prospecaoDetalhamento, detalhamento)){
								podePersistir = false;
								MensagemLacFceUtil.exibirInformacao076();
								break;
							}
						}
					}

					if(podePersistir){
						if (Util.isNull(localizacaoGeograficaSelecionada.getDadoGeograficoCollection())) {
							localizacaoGeograficaSelecionada.setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
						}
						final List<DadoGeografico> verticeCollection = (List<DadoGeografico>) localizacaoGeograficaSelecionada.getDadoGeograficoCollection();

						this.atualizaVerticeNaLista(verticeCollection);
						if(analiseTecnicaDeFce){
							if(objetoLocalizacao instanceof FceLocalizacaoGeografica){
								tratarPonto(localizacaoGeograficaSelecionada);
							}
						}
						if(objetoLocalizacao instanceof DeclaracaoQueimaControladaImovel) {
							tratarPonto(localizacaoGeograficaSelecionada);
							DeclaracaoQueimaControladaImovel dqcI = ((DeclaracaoQueimaControladaImovel) objetoLocalizacao);
							dqcI.setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
							if(!Util.isNullOuVazio(dqcI)) {
								declaracaoQueimaControladaFacade.salvarDeclaracaoQueimaControladaImovel(dqcI);
							}
						}
						
						if(objetoLocalizacao instanceof DeclaracaoInexigibilidadeInfoAbastecimento){
							tratarPonto(localizacaoGeograficaSelecionada);
						}
						JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso!");
					} 
					else {
						System.out.println("PARAMETROS excluídos com sucesso!");
						paramPersistDadoGeoService.excluir(new ParamPersistDadoGeo(idDosParametros));
						localizacaoGeograficaService.excluirByIdLocalizacaoGeografica(localizacaoGeograficaSelecionada);

						limparLocalizacaoGeografSelecionada();
						if(objetoLocalizacao instanceof ProspecaoDetalhamento){
							tipoSecaoGeometrica = ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId();
							((ProspecaoDetalhamento) objetoLocalizacao).setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
							localizacaoGeograficaSelecionada = ((ProspecaoDetalhamento) objetoLocalizacao).getIdeLocalizacaoGeografica();
							idDoComponenteParaSerAtualizado = "formIncluirProspeccao:gridCoordenada";
						}
						carregarTela();
					}
				}
				atualizarPagina();
				limparVertice();
			} catch (Exception exception) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
				JsfUtil.addErrorMessage("Não foi possivel persistir os parâmetros");
			}
		} 
		else {
			RequestContext.getCurrentInstance().addCallbackParam("validationFailed", true);
		}
	}

	private void atualizarPagina() {
		try {
			if(!Util.isNull(metodoAtualizarExterno)) {
					metodoAtualizarExterno.executeMethod();
			}
		if(!Util.isNull(idDoComponenteParaSerAtualizado)){
			Html.atualizar(idDoComponenteParaSerAtualizado);
		}
	}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void tratarPonto(LocalizacaoGeografica localizacaoGeografica) throws Exception {
		facadeLocalizacaoGeografica.tratarPontoLocalizacaoGeografica(localizacaoGeografica);
	}

	private boolean isRealizarVerificacaoMunicipio() {
		
		List<Integer> reqLiberados = Arrays.asList(new Integer[] {35448,39320,40452});
		
		if(isEmpreendimentoLiberado()) {
			return false;
		}
		else if(reqLiberados.contains(requerimento) && (!Util.isNull(isASV) && isASV)) {
			return false;
		}
		else if((new Integer(35448)).equals(requerimento) && (!Util.isNull(isIntervencao) && isIntervencao)) {
			return false;
		}
		else if (Util.isNull(isPerfuracaoPocoOuCaptacaoOuAquicuturaEmRede)) {
			return true;
		}
		else if(objetoLocalizacao instanceof CaepogLocacao) {
			return false;
		}
		else if(objetoLocalizacao instanceof CaepogPoco) {
			return false;
		}
		else if(objetoLocalizacao instanceof DeclaracaoInexigibilidadeInfoAbastecimento) {
			return false;
		}
		else if (indPct) {
			return false;
		}

		return !isPerfuracaoPocoOuCaptacaoOuAquicuturaEmRede;
	}

	private boolean isEmpreendimentoLiberado() {
		try {
			if(Util.isNull(requerimento)) {
				return false;
			}
			else {
				List<Integer> empLiberados = Arrays.asList(new Integer[] {30917});
				Empreendimento empreendimento = empreendimentoService.buscarEmpreendimentoPorRequerimento(requerimento);
			
				if(Util.isNullOuVazio(empreendimento)){
					return false;
				}
				
				return empLiberados.contains(empreendimento.getIdeEmpreendimento());
			}
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public void adicionarRemoverMenos() {
		if(selectedModoCoordenada.equals("1")){
			fracaoGrauLatitudeDecimal = "-" + fracaoGrauLatitudeDecimal;
			fracaoGrauLongitudeDecimal = "-" + fracaoGrauLongitudeDecimal;
		}
		else {
			fracaoGrauLatitudeDecimal = fracaoGrauLatitudeDecimal.replace("-", "");
			fracaoGrauLongitudeDecimal = fracaoGrauLongitudeDecimal.replace("-", "");
		}
	}
	
	private String verificarSistemaCoordenada() {
		String sistemaCoordenada = "";
		if(localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().getIdeSistemaCoordenada().equals(SistemaCoordenadaEnum.UTM_23_SAD69.getId()) || localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().getIdeSistemaCoordenada().equals(SistemaCoordenadaEnum.UTM_24_SAD69.getId())) {
			sistemaCoordenada = "SAD69";
		}
		if(localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().getIdeSistemaCoordenada().equals(SistemaCoordenadaEnum.UTM_23_SIRGAS_2000.getId()) || localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().getIdeSistemaCoordenada().equals(SistemaCoordenadaEnum.UTM_24_SIRGAS_2000.getId())) {
			sistemaCoordenada = "SIRGAS_2000";
		}		
		return sistemaCoordenada;
	}
	
	private void salvarZona() {
		if(zone == 23) {
			if(verificarSistemaCoordenada().equals("SAD69")) {
				localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().setIdeSistemaCoordenada(SistemaCoordenadaEnum.UTM_23_SAD69.getId());
				return;
			}
			if(verificarSistemaCoordenada().equals("SIRGAS_2000")) {
				localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().setIdeSistemaCoordenada(SistemaCoordenadaEnum.UTM_23_SIRGAS_2000.getId());
				return;
			}
		}else {
			if(verificarSistemaCoordenada().equals("SAD69")) {
				localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().setIdeSistemaCoordenada(SistemaCoordenadaEnum.UTM_24_SAD69.getId());
				return;
			}
			if(verificarSistemaCoordenada().equals("SIRGAS_2000")) {
				localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().setIdeSistemaCoordenada(SistemaCoordenadaEnum.UTM_24_SIRGAS_2000.getId());
				return;
			}			
		}
	}

	public void salvarVerticeUTM() {
		try {
			
			if(!PostgisUtil.validaVerticeUTM(latitudeUTM, longitudeUTM)) return;
			
			try {
				if (isRealizarVerificacaoMunicipio() 
						&& !validarMunicipios(latitudeUTM, longitudeUTM,
								SistemaCoordenadaEnum.getEnum(localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().getIdeSistemaCoordenada()).getSrid())) {
					
					JsfUtil.addWarnMessage("Coordenada informada está fora dos limites da Localidade do empreendimento.");
					return;
				}
			} catch (Exception e) {
				JsfUtil.addWarnMessage("Não foi possível validar o vértice informado com a localidade, o sistema não conseguiu encontrar a localidade do empreendimento.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
			
			Point ponto = PostgisUtil.criarPonto(Double.parseDouble(latitudeUTM.toString().replace(',', '.')), Double.parseDouble(longitudeUTM.toString().replace(',', '.')) );

			vertice.setCoordGeoNumerica(ponto.toString());
			vertice.setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);

			localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().setSrid(SistemaCoordenadaEnum.getEnum(localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().getIdeSistemaCoordenada()).getSrid());
			//verticeService.salvarVertice(vertice, localizacaoGeograficaSelecionada.getIdeSistemaCoordenada());
			
			verticeService.salvarOuAtualizarVertice(vertice);
			Integer idDosParametros = paramPersistDadoGeoService.salvarParamsEPersistindoVerticeTheGeom(vertice, localizacaoGeograficaSelecionada.getIdeSistemaCoordenada());
			if(Util.isNullOuVazio(idDosParametros)) {
				System.out.println("ERRO AO SALVAR PARAMETROS");
			}

			if (Util.isNull(localizacaoGeograficaSelecionada.getDadoGeograficoCollection())) {
				localizacaoGeograficaSelecionada.setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
			}
			
			final List<DadoGeografico> verticeCollection = (List<DadoGeografico>) localizacaoGeograficaSelecionada.getDadoGeograficoCollection();

			atualizaVerticeNaLista(verticeCollection);
			
			salvarZona();

			disableParaVisualizar = true;
			JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso!");
			limparVertice();
			
			if (indPct) {
				this.localizacaoGeograficaSelecionada.setLatitudeInicial(String.valueOf(ponto.getCoordinate().x));
				this.localizacaoGeograficaSelecionada.setLongitudeInicial(String.valueOf(ponto.getCoordinate().y));
				atualizarPagina();
			}			
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void atualizaVerticeNaLista(final List<DadoGeografico> verticeCollection) {
		if (verticeCollection.contains(vertice)) {
			int index = verticeCollection.indexOf(vertice);
			verticeCollection.set(index, vertice);
		} else {
			verticeCollection.add(vertice);
		}
	}
	
	public Boolean getLocalizacaoGeografSelecTemDadoGeografico(){
		return !Util.isNullOuVazio(localizacaoGeograficaSelecionada) && !Util.isNullOuVazio(localizacaoGeograficaSelecionada.getDadoGeograficoCollection());
	}
	
	/*
	 * Flag's
	 */
	public boolean isDesabilitaSecaoGeometrica() {
		return !Util.isNullOuVazio(listaSecaoGeomerica) && listaSecaoGeomerica.size() == 1;
	}
	
	private boolean isLocalizacaoGeograficaSelecionadaNotNull() {
		return !Util.isNull(localizacaoGeograficaSelecionada);
	}

	public boolean isLocGeoIsPonto(){
		return isSecaoGeometricaPonto() && isTemSistemaCoordenada() && (sistemaCoordenadaSad69() || sistemaCoordenadasSirgas2000());
	}

	public boolean isLocGeoIsUTM(){
		return isSecaoGeometricaPonto() && isTemSistemaCoordenada() && !sistemaCoordenadaSad69() && !sistemaCoordenadasSirgas2000();
	}
	
	private boolean isTemSistemaCoordenada(){
		return isLocalizacaoGeograficaSelecionadaNotNull() && !Util.isNullOuVazio(localizacaoGeograficaSelecionada.getIdeSistemaCoordenada());
	}
	
	private boolean isTemClassificacaoSecao(){
		return isLocalizacaoGeograficaSelecionadaNotNull() && !Util.isNullOuVazio(localizacaoGeograficaSelecionada.getIdeClassificacaoSecao());
	}

	private boolean isTemLocGeoPonto(){
		return isTemClassificacaoSecao() && localizacaoGeograficaSelecionada.getIdeClassificacaoSecao().getIdeClassificacaoSecao().equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId());
	}
	
	public boolean isTemLocGeoShape() {
		return isTemClassificacaoSecao() && localizacaoGeograficaSelecionada.getIdeClassificacaoSecao().getIdeClassificacaoSecao().equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId());
	}

	public boolean isTemLocGeoDesenho() {
		return isTemClassificacaoSecao() && localizacaoGeograficaSelecionada.getIdeClassificacaoSecao().getIdeClassificacaoSecao().equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_DESENHO.getId());
	}
	
	private boolean sistemaCoordenadasUtm23(){
		return localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().getIdeSistemaCoordenada().equals(SistemaCoordenadaEnum.UTM_23_SAD69.getId()) || localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().getIdeSistemaCoordenada().equals(SistemaCoordenadaEnum.UTM_23_SIRGAS_2000.getId());
	}
	
	private boolean sistemaCoordenadasUtm24(){
		return localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().getIdeSistemaCoordenada().equals(SistemaCoordenadaEnum.UTM_24_SAD69.getId()) || localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().getIdeSistemaCoordenada().equals(SistemaCoordenadaEnum.UTM_24_SIRGAS_2000.getId());
	}

	private boolean sistemaCoordenadasSirgas2000() {
		return localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().getIdeSistemaCoordenada().equals(SistemaCoordenadaEnum.GEOGRAFICA_SIRGAS_2000.getId());
	}

	private boolean sistemaCoordenadaSad69() {
		return localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().getIdeSistemaCoordenada().equals(SistemaCoordenadaEnum.GEOGRAFICA_SAD69.getId());
	}
	/*
	 * Final das Flag's
	 */

	public void excluirVertice() {
		try {
			verticeService.excluir(verticeExclusao);
			localizacaoGeograficaSelecionada.getDadoGeograficoCollection().remove(verticeExclusao);
		} catch (Exception exception) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, exception);
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void carregarVertice() {
		selectedModoCoordenada = "1";
		try {
			
			CoordenadaGeografica coordenadaGeografica = GeoUtil.converterPointParaCoordenadaGeografica(vertice.getCoordGeoNumerica());
			
			grausLatitude = coordenadaGeografica.getLatitude().getGrau().toString();
			minutosLatitude = coordenadaGeografica.getLatitude().getMinuto().toString();
			segundosLatitude = coordenadaGeografica.getLatitude().getSegundo().toString();
			
			grausLongitude =  coordenadaGeografica.getLongitude().getGrau().toString();
			minutosLongitude = coordenadaGeografica.getLongitude().getMinuto().toString();
			segundosLongitude = coordenadaGeografica.getLongitude().getSegundo().toString();
			
			fracaoGrauLatitude = coordenadaGeografica.getLatitude().getAsGD();
			fracaoGrauLongitude = coordenadaGeografica.getLongitude().getAsGD();
			
			fracaoGrauLatitudeDecimal = coordenadaGeografica.getLatitude().getAsGD();
			fracaoGrauLongitudeDecimal = coordenadaGeografica.getLongitude().getAsGD();
			
			latitudeUTM = coordenadaGeografica.getLatitude().getAsGD();
			longitudeUTM = coordenadaGeografica.getLongitude().getAsGD();
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public void carregarVerticeUTM() {
		latitudeUTM = vertice.getCoordGeoNumerica().replaceAll("POINT ", "").replace("(", "").replace(")", "").split(" ")[0];
		longitudeUTM = vertice.getCoordGeoNumerica().replaceAll("POINT ", "").replace("(", "").replace(")", "").split(" ")[1];
		carregarZona();
	}

	public String getLatitudeUTM(DadoGeografico pVertice) {
		try{
			String latitude = pVertice.getCoordGeoNumerica().replaceAll("POINT ", "").replace("(", "").replace(")", "").split(" ")[0];
			localizacaoGeograficaSelecionada.setLatitudeInicial(latitude);
			return latitude;
		}catch(Exception e){
			return StringUtils.EMPTY;
		}
	}

	public String getLongitudeUTM(DadoGeografico pVertice) {
		try{
			String longitude = pVertice.getCoordGeoNumerica().replaceAll("POINT ", "").replace("(", "").replace(")", "").split(" ")[1];
			localizacaoGeograficaSelecionada.setLongitudeInicial(longitude);
			return longitude;
		}catch(Exception e){
			return StringUtils.EMPTY;
		}
	}

	public void limparVertice() {
		selectedModoCoordenada = "1";
		vertice = new DadoGeografico();
		grausLatitude = StringUtils.EMPTY;
		minutosLatitude = StringUtils.EMPTY;
		segundosLatitude = StringUtils.EMPTY;
		grausLongitude = StringUtils.EMPTY;
		minutosLongitude = StringUtils.EMPTY;
		segundosLongitude = StringUtils.EMPTY;
		fracaoGrauLatitude = StringUtils.EMPTY;
		fracaoGrauLongitude = StringUtils.EMPTY;
		fracaoGrauLatitudeDecimal = StringUtils.EMPTY;
		fracaoGrauLongitudeDecimal= StringUtils.EMPTY;
		latitudeUTM = StringUtils.EMPTY;
		longitudeUTM = StringUtils.EMPTY;
	}

	public void carregarZona(){
		if(sistemaCoordenadasUtm23()){
			zone = 23;
		}
		else if(sistemaCoordenadasUtm24()){
			zone = 24;
		}
	}

	public void limparLocGeo() {
		this.localizacaoGeograficaSelecionada = new LocalizacaoGeografica();
		this.imovel = new Imovel();
	}

	public String getLatitude(DadoGeografico pVertice) {
		if (Util.isNullOuVazio(pVertice)){
			return "--";
		}
		Map<String, String> pontos = facadeLocalizacaoGeografica.getPonto(pVertice);
		if (!Util.isNullOuVazio(pontos)){
			return pontos.get("latitude");
		} 
		else {
			return "--";
		}
	}

	public String getLongitude(DadoGeografico pVertice) {
		Map<String, String> pontos = facadeLocalizacaoGeografica.getPonto(pVertice);
		if (!Util.isNullOuVazio(pontos)){
			return pontos.get("longitude");
		} 
		else {
			return "--";
		}
	}

	public String getUrl(DadoGeografico pVertice) {
		return retornarUrl(pVertice.getIdeLocalizacaoGeografica());
	}
	
	public String retornarUrl(LocalizacaoGeografica localizacaoGeografica) {
		String lUrl = new String();
		Map<String, String> parametros = new HashMap<String, String>();
		parametros.put("acao", "view");
		parametros.put("idloc", localizacaoGeografica.getIdeLocalizacaoGeografica().toString());
		parametros.put("tipo", "1");
		
		String token = usuarioAutorizacaoGeobahiaService.carregarUsuarioAutorizacaoGeobahia();
		
		if(!Util.isNullOuVazio(token)) {
			parametros.put("token", token);
		}
		Exception erro = null;
		try {
			lUrl = "window.open('"+URLEnum.CAMINHO_GEOBAHIA.getUrl(parametros)+"');";
		} catch (Exception e) {
			erro =e;
			System.err.println(e.getMessage());
		}finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}
		
		return lUrl;
	}

	public String criarUrlShape(String nulo) {
		StringBuilder lUrl = new StringBuilder();
		if (!Util.isNullOuVazio(localizacaoGeograficaSelecionada) && !Util.isNullOuVazio(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica())) {
			lUrl.append(URLEnum.CAMINHO_GEOBAHIA + "?acao=view&idloc=" + localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica() + "&tipo=1");
			
			String token = usuarioAutorizacaoGeobahiaService.carregarUsuarioAutorizacaoGeobahia();
			
			if(!Util.isNullOuVazio(token)) {
				lUrl.append("&token=");
				lUrl.append(token);
			}
		}

		StringBuilder lReturn = new StringBuilder();
		lReturn.append("window.open('");
		lReturn.append(lUrl.toString());
		lReturn.append("');");
		return lReturn.toString();
	}

	public void calculaFracaoGrauLatitude(ActionEvent event) {
		if (grausLatitude != null) {
			if (!segundosLatitude.contains(",")){
				if(segundosLatitude.contains(".")){
					segundosLatitude = segundosLatitude.replace(".", ",");
				} else {
					if (segundosLatitude.length() > 2){
						segundosLatitude = segundosLatitude.substring(0, 2) + "," + segundosLatitude.substring(2);
					} else {
						segundosLatitude = segundosLatitude + ",00";

					}
				}
			}
			fracaoGrauLatitude = GeoUtil.converterGMSParaDecimal(GeoUtil.criarCoordenada(grausLatitude, minutosLatitude, segundosLatitude));
		}
	}

	public void calculaFracaoGrauLongitude(ActionEvent event) {
		if (grausLongitude != null) {
			if (!segundosLongitude.contains(",")){
				if(segundosLongitude.contains(".")){
					segundosLongitude = segundosLongitude.replace(".", ",");
				} else {
					if(segundosLongitude.length() > 2){
						segundosLongitude = segundosLongitude.substring(0, 2) + "," + segundosLongitude.substring(2);
					} else {
						segundosLongitude = segundosLongitude + ",00";
					}
				}
			}

			fracaoGrauLongitude = GeoUtil.converterGMSParaDecimal(GeoUtil.criarCoordenada(grausLongitude, minutosLongitude, segundosLongitude));
		}
	}

	public void selectedDatumChanged(ValueChangeEvent event) throws AbortProcessingException {
		this.datumSelecionado = (SistemaCoordenada) event.getNewValue();
		habilitaIncluirLoc = false;
	}

	public void listarSistemaCoordenada() {
		try {
			datums = serviceDatum.listarDatum();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Sistema de Coordenadas."); 
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void trataArquivo(FileUploadEvent event) {
		if (listaArquivo.size() < 3) {
			if (!Util.isNullOuVazio(localizacaoGeograficaSelecionada) && !Util.isNullOuVazio(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica())) {

				if ((!Util.isNullOuVazio(localizacaoGeograficaSelecionada.getIdeSistemaCoordenada()) && Util.isNullOuVazio(localizacaoGeograficaSelecionada
						.getIdeSistemaCoordenada().getSrid())) || Util.isNullOuVazio(localizacaoGeograficaSelecionada.getIdeSistemaCoordenada())) {

					try {
						localizacaoGeograficaSelecionada = localizacaoGeograficaService.carregarSomenteComSistemaCoordenada(localizacaoGeograficaSelecionada
								.getIdeLocalizacaoGeografica());
					} catch (Exception e) {
						Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					}

				}

				caminhoArquivo = FileUploadUtil.EnviarShape(event, DiretorioArquivoEnum.SHAPEFILES.toString() + FileUploadUtil.getFileSeparator()
						+ localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica().toString(), localizacaoGeograficaSelecionada
						.getIdeLocalizacaoGeografica().toString() + "_" + localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().getSrid());
				listaArquivo.add(FileUploadUtil.getFileName(caminhoArquivo));
				temArquivo = true;
				JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
			} else {
				JsfUtil.addWarnMessage("Para inserir os shapes é preciso salvar a Localização Geográfica!");
			}
		} else {
			JsfUtil.addWarnMessage("Não é possível enviar mais de 3 arquivos.");
		}
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
				if (teste[1].equals("shp") || teste[1].equals("SHP")) {
					fShp = true;
				} else if (teste[1].equals("dbf") || teste[1].equals("DBF")) {
					fDbf = true;
				} else if (teste[1].equals("shx") || teste[1].equals("SHX")) {
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

	public void excluirArquivo() {
		if (listaArquivo.size() > 0) {
			listaArquivo.remove(arqDeExclusao);
			JsfUtil.addSuccessMessage("Arquivo Removido com Sucesso.");
		} else {
			temArquivo = false;
			JsfUtil.addWarnMessage("Não é possível excluir este arquivo.");
		}
		if (listaArquivo.isEmpty()) {
			temArquivo = false;
		}
	}

	public void persistirShapes(Integer pTipoPersitencia) {
		String[] retorno;
		boolean isErro = false;
		try {
			Integer idDosParametros;
			Collection<Municipio> listaMunicipio = null;

			if(Util.isNullOuVazio(empreendimento)) {
				empreendimento = empreendimentoService.getEmpreendimentoByRequerimento(new Requerimento(requerimento));
			}

			if(validarTipoShape()){
			
			// Persistência por upload
			if (pTipoPersitencia.equals(1)) {

				LocalizacaoGeografica locGeo = null;

				if(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica() != null) {
					locGeo = localizacaoGeograficaService.carregarSomenteComSistemaCoordenada(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica());
				}

				if(locGeo == null) {
					localizacaoGeograficaSelecionada.setIdeLocalizacaoGeografica(null);
					localizacaoGeograficaService.salvarlocalizacao(localizacaoGeograficaSelecionada);
				} else {
					localizacaoGeograficaSelecionada = locGeo;
				}


				if(!Util.isNullOuVazio(imovel)) {
					boolean isEnderecoNull = false;

					if(Util.isNullOuVazio(imovel.getIdeEndereco())) {
						isEnderecoNull = true;
					} else if(Util.isNullOuVazio(imovel.getIdeEndereco().getIdeLogradouro())) {
						isEnderecoNull = true;
					} else if(Util.isNullOuVazio(imovel.getIdeEndereco().getIdeLogradouro().getIdeMunicipio())) {
						isEnderecoNull = true;
					} else if(Util.isNullOuVazio(imovel.getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio())) {
						isEnderecoNull = true;
					}

					if(isEnderecoNull) {
						imovel = imovelService.buscarImovelPorIde(imovel);
					}
				}

				listaMunicipio = adicionarMunicipiosServiceFacade.listarTodosMunicipiosEnvolvidos(empreendimento.getIdeEmpreendimento());

				if(!Util.isNullOuVazio(listaMunicipio) && listaMunicipio.size() > 1) {
					
					if(isLicenciamentoAquicutura) {
						retorno = paramPersistDadoGeoService.salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, false, null, false, 0);
					}else {
						retorno = paramPersistDadoGeoService.salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, false, null, false, 1);
					}

				} else if(!isRealizarVerificacaoMunicipio()) {
					if(objetoLocalizacao instanceof CaepogPoco) { //Shape tipo ponto
						retorno = paramPersistDadoGeoService.salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, false, null, true,1);
					} else {
						if(isLicenciamentoAquicutura) {
							retorno = paramPersistDadoGeoService.salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, false, null, false,0);
						}else {
							retorno = paramPersistDadoGeoService.salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, false, null, false,1);
						}
					}
				} else if(!Util.isNullOuVazio(imovel)
						&& !Util.isNullOuVazio(imovel.getIdeEndereco())
						&& !Util.isNullOuVazio(imovel.getIdeEndereco().getIdeLogradouro())
						&& !Util.isNullOuVazio(imovel.getIdeEndereco().getIdeLogradouro().getIdeMunicipio())
						&& !Util.isNullOuVazio(imovel.getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio())){

					retorno = paramPersistDadoGeoService.salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, validarMunicipio(),
							imovel.getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio(), shapeTipoPonto,1);

				} else if(!Util.isNullOuVazio(empreendimento) && !Util.isNullOuVazio(empreendimento.getEnderecoParaValidacao())){

					if(Util.isNullOuVazio(empreendimento.getEnderecoParaValidacao().getIdeLogradouro().getIdeMunicipio())) {
						
						if(isLicenciamentoAquicutura) {
							retorno = paramPersistDadoGeoService.salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, validarMunicipio(),
									empreendimento.getEnderecoParaValidacao().getIdeLogradouro().getIdeBairro().getIdeMunicipio().getCoordGeobahiaMunicipio(), shapeTipoPonto,0);
						}else {
							retorno = paramPersistDadoGeoService.salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, validarMunicipio(),
									empreendimento.getEnderecoParaValidacao().getIdeLogradouro().getIdeBairro().getIdeMunicipio().getCoordGeobahiaMunicipio(), shapeTipoPonto,1);
						}
						
					} else {
						
						if(isLicenciamentoAquicutura) {
							retorno = paramPersistDadoGeoService.salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, validarMunicipio(),
									empreendimento.getEnderecoParaValidacao().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio(), shapeTipoPonto,0);
						}else {
							retorno = paramPersistDadoGeoService.salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, validarMunicipio(),
									empreendimento.getEnderecoParaValidacao().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio(), shapeTipoPonto,1);
						}
						
					}

				} else if(!Util.isNullOuVazio(empreendimento) && Util.isNullOuVazio(empreendimento.getEnderecoParaValidacao())){

					this.empreendimento = empreendimentoService.carregarPorIdComMunicipio(empreendimento.getIdeEmpreendimento());

					if(isLicenciamentoAquicutura) {
						retorno = paramPersistDadoGeoService.salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, validarMunicipio(),
								empreendimento.getEnderecoParaValidacao().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio(), shapeTipoPonto,0);
					}else {
						retorno = paramPersistDadoGeoService.salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, validarMunicipio(),
								empreendimento.getEnderecoParaValidacao().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio(), shapeTipoPonto,1);
					}
					

				} else {
					JsfUtil.addErrorMessage("Não foi informado nenhum imóvel ou empreendimento para validar a localização geográfica.");
					retorno = new String[]{"Não foi informado nenhum imóvel ou empreendimento para validar a localização geográfica."};
					return;
				}
			}

			else {// Persistência por desenho
				idDosParametros = paramPersistDadoGeoService.carregarSomenteComParamPersistDadoGeo(localizacaoGeograficaSelecionada
						.getIdeLocalizacaoGeografica());
				if (!Util.isNullOuVazio(idDosParametros)) {
					retorno = paramPersistDadoGeoService.salvarParamsEPersistindoDesenhoTheGeom(idDosParametros, imovel.getIdeEndereco()
							.getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio());
				} else {
					retorno = new String[] { "ERRO", "9999", "Erro interno. Contacte o Suporte técnico!" };
				}
			}

			if (retorno.length > 0) {
				this.existeTheGeom = retorno[0].equalsIgnoreCase("OK");
				String comp = StringUtils.EMPTY;
				if(retorno[0].contains("ERRO")){
					comp = retorno[0];
					for (int i = 1; i < retorno.length; i++) {
						comp = comp + " [" + retorno[i] + "]";
					}
					System.out.println(comp);
					JsfUtil.addErrorMessage(comp);
					isErro=true;
				}else{

					if(objetoLocalizacao instanceof PerguntaRequerimento){
						PerguntaRequerimento pReq = (PerguntaRequerimento) objetoLocalizacao;

						if(isPerguntaDialogFlorestal(pReq)){

							if(validacaoGeoSeiaService.validarAreaSupressaoRequerimento(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica(), areaSupressao.doubleValue())){

								if(!Util.isNullOuVazio(listaMunicipio) && listaMunicipio.size() > 1) {

									Map<String, Object> retornoValidarMunicipios = adicionarMunicipiosServiceFacade.validarMunicipiosParaRequerimento(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica(), listaMunicipio);
									boolean isShapeValido = (Boolean) retornoValidarMunicipios.get("isShapeValido");
									if(!isShapeValido) {

										@SuppressWarnings("unchecked")
										Collection<Municipio> listaMunicipioSobrandoShape = (Collection<Municipio>) retornoValidarMunicipios.get("listaMunicipioSobrando");

										adicionarMunicipiosServiceFacade.removerDadoGeografico(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica());
										existeTheGeom = false;

										if(!Util.isNullOuVazio(listaMunicipioSobrandoShape)) {
											JsfUtil.addWarnMessage("Caro(a) Usuário(a),<br /><br />O shape inserido transpassa município não informado.<br />Por favor, corrija a lista de municípios cadastrados no endereço do empreendimento.");
										}					
									} else {
										msgsDePersistenciaComSucesso();
									}
								}
							} else {
								JsfUtil.addWarnMessage(Util.getString("msg_generica_area_diferente_area_shape"));
								throw new SeiaException(comp);
							}
						} else{
							msgsDePersistenciaComSucesso();
						}

					} else if(objetoLocalizacao instanceof FceTurismoLocalizacaoGeografica){
						FceTurismoLocalizacaoGeografica fceTurismoLocalizacaoGeografica = (FceTurismoLocalizacaoGeografica) objetoLocalizacao;
						fceTurismoController.prepararFceParaVisualizacaoGeoBahia(fceTurismoLocalizacaoGeografica);
					}
					// Poligonal do Cultivo
					else if(objetoLocalizacao instanceof FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica){
						FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica poligonalCultivo = (FceAquiculturaLicencaTipoAtividadeLocalizacaoGeografica) objetoLocalizacao;
						String theGeom = validacaoGeoSeiaService.buscarGeometriaShape(poligonalCultivo.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
						poligonalCultivo.setAreaPoligonal(new BigDecimal(validacaoGeoSeiaService.retonarAreaDoShapeByGeometria(theGeom)));

					} else if(objetoLocalizacao instanceof CaepogLocacao) {

						if(!validacaoGeoSeiaService.isLocalizacaoGeograficaContida(
								((CaepogLocacao) objetoLocalizacao).getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica(), null,
								((CaepogLocacao) objetoLocalizacao).getIdeCaepogDefinicaoCampo().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica(), null)) {

							JsfUtil.addErrorMessage("O shape da locação não está dentro do campo cadastrado.");
							excluirShapeSemMensagem(((CaepogLocacao) objetoLocalizacao).getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
							limpaFormLocGeo();
							RequestContext.getCurrentInstance().addPartialUpdateTarget("locGeograficaGeneric");

						} else {
							if(!Util.isNullOuVazio(((CaepogLocacao) objetoLocalizacao).getIdeCaepogDefinicaoCampo().getCaepogLocacaoCollection())) {

								boolean shapeRepetido = false;

								for (CaepogLocacao cl : ((CaepogLocacao) objetoLocalizacao).getIdeCaepogDefinicaoCampo().getCaepogLocacaoCollection()) {

									if(((CaepogLocacao) objetoLocalizacao).getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica() 
											!= cl.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica()) {

										if(validacaoGeoSeiaService.isLocalizacaoGeograficaContida(
												((CaepogLocacao) objetoLocalizacao).getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica(), null,
												cl.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica(), null)) {

											JsfUtil.addErrorMessage("O arquivo shape informado sobrepõe uma locação cadastrada. Verifique o arquivo e tente novamente.");
											excluirShapeSemMensagem(((CaepogLocacao) objetoLocalizacao).getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
											limpaFormLocGeo();
											RequestContext.getCurrentInstance().addPartialUpdateTarget("locGeograficaGeneric");
											shapeRepetido = true;
										}
									}
								}

								if(!shapeRepetido) {
									msgsDePersistenciaComSucesso();
								}
							} else {
								msgsDePersistenciaComSucesso();
							}
						}
					} else if(objetoLocalizacao instanceof CaepogPoco) {

						if(!validacaoGeoSeiaService.verificaSobreposicaoShapeUsandoContains(
								((CaepogPoco) objetoLocalizacao).getIdeCaepogLocacao().getIdeLocalizacaoGeografica(),
								((CaepogPoco) objetoLocalizacao).getIdeLocalizacaoGeografica())) {
							atualizarPagina();
							JsfUtil.addErrorMessage("O shape do poço não está dentro da locação cadastrada.");
							excluirShapeSemMensagem(((CaepogPoco) objetoLocalizacao).getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
							limpaFormLocGeo();
							RequestContext.getCurrentInstance().addPartialUpdateTarget("locGeograficaGeneric");
						} else {
							msgsDePersistenciaComSucesso();
						}
					} 
					// Poligonal requerida ao DNPM
					else if(objetoLocalizacao instanceof ProcessoDnpm){
						ProcessoDnpm poligonalDNPM = (ProcessoDnpm) objetoLocalizacao;
						poligonalDNPM.setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
						String theGeom = validacaoGeoSeiaService.buscarGeometriaShape(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica());
						poligonalDNPM.setAreaProcessoDnpm(new BigDecimal(validacaoGeoSeiaService.retonarAreaDoShapeByGeometria(theGeom).doubleValue()));
					}
					else if(objetoLocalizacao instanceof FceLicenciamentoMineral){
						validarFceLicenciamentoMineracao();
					}
					else if(objetoLocalizacao instanceof DeclaracaoQueimaControladaImovel) {
						DeclaracaoQueimaControladaImovel dqcI = ((DeclaracaoQueimaControladaImovel) objetoLocalizacao);
						dqcI.setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
						
						if(!Util.isNullOuVazio(dqcI)) {
							declaracaoQueimaControladaFacade.salvarDeclaracaoQueimaControladaImovel(dqcI);
						}
						
						localizacaoGeograficaSelecionada.setParamPersistDadoGeoCollection(
								paramPersistDadoGeoService.listarPorLocalizacaoGeografica(localizacaoGeograficaSelecionada));
					}

					else if (objetoLocalizacao instanceof RegistroFlorestaProducaoImovelPlantio) { 
						
						RegistroFlorestaProducaoImovelPlantio registroFlorestaProducaoImovelPlantio = (RegistroFlorestaProducaoImovelPlantio) objetoLocalizacao;
						String theGeom = validacaoGeoSeiaService.buscarGeometriaShape(registroFlorestaProducaoImovelPlantio.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
						registroFlorestaProducaoImovelPlantio.setValAreaPlantio(BigDecimalUtil.aproximar((new BigDecimal(validacaoGeoSeiaService.retonarAreaDoShapeByGeometria(theGeom).doubleValue())) ,6));
						disableParaExcluir = false;
					}
					else {
						msgsDePersistenciaComSucesso();
					}
				}
			}

			if(geoReferenciavel != null && !isErro) {
				ArquivoProcesso arq = (ArquivoProcesso) geoReferenciavel;
				arq.setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
				arq.setDscArquivoProcesso(localizacaoGeograficaSelecionada.getDesLocalizacaoGeografica());
				localizacaoGeograficaService.salvarGeoReferenciavel(geoReferenciavel);
			}
			}

			atualizarPagina();

		} catch (SeiaException e) {
			excluirShapeSemMensagem(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica());
			this.firstTime = true;
			this.localizacaoGeograficaSelecionada.setIdeLocalizacaoGeografica(null);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			System.out.println("ERRO AO SALVAR PARAMETROS");
			retorno = new String[]{"ERRO AO SALVAR PARAMETROS"};
			excluirShape(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica());
			JsfUtil.addWarnMessage("Erro ao persistir os shapes no banco!");
		}
	}
	
	/**
	 * Método que verifica se o shape inserido precisa de validação ou não de limite municipal
	 * @return
	 * @author edson.lacerda
	 */
	public boolean validarMunicipio() {
		
		//Condicional que verifica se o motivo da notificação é igual a 11, 16 ou 23 para não validar o municipio do shape inserido (processo)
		if (geoReferenciavel != null && geoReferenciavel instanceof ArquivoProcesso) {
			ArquivoProcesso arq = (ArquivoProcesso) geoReferenciavel;
		    int motivoNotificacao = arq.getMotivoNotificacao().getIdeMotivoNotificacao();
		    if (motivoNotificacao == 11 || motivoNotificacao == 16 || motivoNotificacao == 23) {
		        return false;
		    }
		}
		
		//Condicional que retira a validação municipal dos shapes para os atos de ARRL, ARSF e ASF (requerimento)
		if (isNaoValidaMunicipio.equals(true)) {
			return false;
		}
		
		return true;
	}

	/**
	 * Validar Tipo do Shape que será persistido
	 * @return
	 * @throws Exception
	 * @author rodrigo.santos
	 */
	private boolean validarTipoShape() throws Exception {
		
		if(!(objetoLocalizacao instanceof CaepogPoco)) { //Shape tipo ponto
		
			if(tipoSecaoGeometricaValidacao.equals(0)) {
				tipoSecaoGeometricaValidacao = tipoSecaoGeometrica;
			}
			String geometria = validacaoGeoSeiaService.buscarGeometriaShapeTemporario(localizacaoGeograficaSelecionada);
			
			boolean tipoPoligono =validacaoGeoSeiaService.validaTipoGeometriaPoligono(null,geometria);
			boolean tipoLinha = validacaoGeoSeiaService.validaTipoGeometriaLinha(null,geometria);
			boolean tipoPonto = validacaoGeoSeiaService.validaTipoGeometriaPonto(null,geometria);
			String tipoFeicao = "";
			
			if(tipoSecaoGeometricaValidacao == ValidacaoShapeEnum.POLIGONO.getId().intValue()){
				tipoFeicao = "Polígono";
				if(tipoPoligono){
					return true;
				}
				
			}
			if(tipoSecaoGeometricaValidacao == ValidacaoShapeEnum.POLIGONO_OU_LINHA.getId().intValue()){
				tipoFeicao = "Polígono ou Linha";
				if(tipoPoligono || tipoLinha){
					return true;
				}
				
			}
			if(tipoSecaoGeometricaValidacao == ValidacaoShapeEnum.PONTO.getId().intValue()){
				tipoFeicao = "Ponto";
				if(tipoPonto){
					return true;
				}
				
			}
			if(tipoSecaoGeometricaValidacao == ValidacaoShapeEnum.LINHA.getId().intValue()){
				tipoFeicao = "Linha";
				if(tipoLinha){
					return true;
				}
				
			}
			if(tipoSecaoGeometricaValidacao == ValidacaoShapeEnum.DESENHO.getId().intValue()){
				tipoFeicao = "Desenho";
				return true;
			}	
			
			if(tipoSecaoGeometricaValidacao == ValidacaoShapeEnum.POLIGONO_OU_PONTO.getId().intValue()){
				tipoFeicao = "Ponto ou Poligono";
				if(tipoPoligono || tipoPonto){
					return true;
				}
			}	
			
			JsfUtil.addErrorMessage("A feição do shape informado não é permitida. Insira um shape na feição " + tipoFeicao + ".");
			return false;
		}
		
		return true;
			
	}

	/**
	 * @author eduardo.fernandes 
	 * @since 06/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @throws Exception
	 */
	private void validarFceLicenciamentoMineracao() throws Exception {
		FceLicenciamentoMineral fceLicenciamentoMineral = (FceLicenciamentoMineral) objetoLocalizacao;
		boolean valido = true;
		String msg = "";
		Double totalAreaSobreposta = 0.0;
		
		if(isLocalizacaoGeograficaNaBahia(localizacaoGeograficaSelecionada)) {
		
			if(Util.isNullOuVazio(fceLicenciamentoMineral.getIdeLocalizacaoGeograficaLavra()) 
					&& Util.isNullOuVazio(fceLicenciamentoMineral.getIdeLocalizacaoGeograficaServidao())){
				
				for(ProcessoDnpm dnpm : fceLicenciamentoMineral.getListaProcessoDNPM()){
						
					if(fceLicenciamentoMineral.getListaProcessoDNPM().size() == 1) {
						
						if(isPoligonalLavraValida(dnpm)){
							fceLicenciamentoMineral.setAreaDeLavra(obterAreaShape());
							fceLicenciamentoMineral.setIdeLocalizacaoGeograficaLavra(localizacaoGeograficaSelecionada);
							RequestContext.getCurrentInstance().addPartialUpdateTarget("tabLicMineracao:minLicQuadroAreas:gridServidao");
							valido = true;
							break;
							
						} else {
							msg = "A(s) poligonal(is) de lavra deve(m) estar dentro de algum dos shapes da DNPM inseridos.";
							valido = false;
						}
					} else {
						totalAreaSobreposta += 
								facadeLocalizacaoGeografica.retornaPercentualSobreposicao(dnpm.getIdeLocalizacaoGeografica(), localizacaoGeograficaSelecionada);
						
						//A área de lavra tem que estar 100% contida ao longo dos shapes dos processos DNPM
						if(Util.converterDoubleQuatroCasas(totalAreaSobreposta).equals(100.0)) {
							fceLicenciamentoMineral.setAreaDeLavra(obterAreaShape());
							fceLicenciamentoMineral.setIdeLocalizacaoGeograficaLavra(localizacaoGeograficaSelecionada);
							RequestContext.getCurrentInstance().addPartialUpdateTarget("tabLicMineracao:minLicQuadroAreas:gridServidao");
							valido = true;
							break;
							
						} else {
							msg = "A(s) poligonal(is) de lavra deve(m) estar dentro de algum dos shapes da DNPM inseridos.";
							valido = false;
						}
					}
				}
				
				if(!valido){
					fceLicenciamentoMineral.setAreaDeLavra(null);
					fceLicenciamentoMineral.setIdeLocalizacaoGeograficaLavra(null);				
				}
			} else {
				if(isPoligonalServidaoValida(fceLicenciamentoMineral)){
					fceLicenciamentoMineral.setAreaDeServidao(obterAreaShape());
					fceLicenciamentoMineral.setIdeLocalizacaoGeograficaServidao(localizacaoGeograficaSelecionada);
					RequestContext.getCurrentInstance().addPartialUpdateTarget("tabLicMineracao:minLicQuadroAreas:gridAreaLavra");
					
				} else {
					fceLicenciamentoMineral.setAreaDeServidao(null);
					fceLicenciamentoMineral.setIdeLocalizacaoGeograficaServidao(null);
					msg = "A(s) poligonal(is) de Servidão não pode(m) sobrepor a(s) poligonal(is) de lavra.";
					valido = false;
				}
			}
		} else {
			msg = "A(s) poligonal(is) deve(m) estar em sua totalidade no estado da Bahia.";
			valido = false;
		}
		
		if(!valido){
			excluirShape(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica());
			fecharDialog();
			MensagemUtil.erro(msg);
		}
	}

	/**
	 * Método que obtém a área do Shape / Poligonal.
	 * 
	 * @author eduardo.fernandes
	 * @since 06/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @return
	 * @throws Exception
	 */
	private Double obterAreaShape() throws Exception {
		return facadeLocalizacaoGeografica.retornarAreaShape(localizacaoGeograficaSelecionada);
	}

	/**
	 * <li><b>RN 00170 - Poligonal da área efetiva de lavra</b></li>
	 * <p>
	 * Cadastro obrigatório de um arquivo shapefile contendo uma ou mais
	 * poligonais da área efetiva de lavra. No caso do shape conter mais de uma
	 * poligonal, elas devem estar em sua totalidade no estado da Bahia e não
	 * podem se sobrepor. A área de lavra deve estar dentro de algum dos shapes
	 * da DNPM inseridos.
	 * </p>
	 * 
	 * @author eduardo.fernandes
	 * @since 30/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param ideLocalizacaoGeograficaLavra
	 * @return
	 * @throws Exception
	 */
	private boolean isPoligonalLavraValida(ProcessoDnpm dnpm) throws Exception {
		return facadeLocalizacaoGeografica.isSobrePosicaoCompleta(dnpm.getIdeLocalizacaoGeografica(), localizacaoGeograficaSelecionada, 100.00);
	}

	/**
	 * <li><b>RN 00171 - Poligonal da área de servidão</b></li>
	 * <p>
	 * Cadastro de um arquivo shapefile contendo uma ou mais poligonais da área
	 * de servidão. No caso do shape conter mais de uma poligonal, elas devem
	 * estar em sua totalidade no estado da Bahia e não podem se sobrepor e nem
	 * podem sobrepor as poligonais de lavra. O cadastro não é obrigatório para
	 * FCE de LP, sendo obrigatório para os demais tipos de licença e renovação
	 * de licença (LI, LO, LU, LR, RLU e RLO).
	 * </p>
	 * 
	 * @author eduardo.fernandes
	 * @since 30/06/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param ideLocalizacaoGeograficaServidao
	 * @return
	 * @throws Exception
	 */
	private boolean isPoligonalServidaoValida(FceLicenciamentoMineral fceLicenciamentoMineral) throws Exception {
		return facadeLocalizacaoGeografica.naoExisteSobreposicao(fceLicenciamentoMineral.getIdeLocalizacaoGeograficaLavra(), localizacaoGeograficaSelecionada); 
	}

	/**
	 * @author eduardo.fernandes
	 * @since 19/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @return
	 * @throws Exception
	 */
	protected boolean isLocalizacaoGeograficaNaBahia(LocalizacaoGeografica localizacaoGeografica) throws Exception {
		return facadeLocalizacaoGeografica.isLocalizacaoGeograficaSomenteNaBahia(localizacaoGeografica);
	}

	/**
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @param pReq
	 * @return
	 * @since 07/03/2016
	 */
	private boolean isPerguntaDialogFlorestal(PerguntaRequerimento pReq) {
		return pReq.getIdePergunta().getCodPergunta().equals("NR_A5_DFLO_P12")
				|| pReq.getIdePergunta().getCodPergunta().equals("NR_A5_DFLO_P13")
				|| pReq.getIdePergunta().getCodPergunta().equals("NR_A5_DFLO_P15")
				|| pReq.getIdePergunta().getCodPergunta().equals("NR_A5_DFLO_P16")
				/*|| pReq.getIdePergunta().getCodPergunta().equals("NR_A5_DFLO_P17")*/
				|| pReq.getIdePergunta().getCodPergunta().equals("NR_A5_DFLO_P18")
				|| pReq.getIdePergunta().getCodPergunta().equals("NR_A5_DFLO_P19");
	}

	/**
	 *
	 */
	public void msgsDePersistenciaComSucesso() {
		String comp;
		comp = "Importação realizada com sucesso!";
		System.out.println(comp);
		JsfUtil.addSuccessMessage(comp);
	}

	public void limpaFormLocGeo() {
		listaArquivo = new ArrayList<String>();
		existeTheGeom = false;
	}

	public void excluirShape(Integer pIdeLocalizacaoGeografica) {
		try {
			localizacaoGeograficaService.excluirDadosPersistidos(pIdeLocalizacaoGeografica);
			/*localizacaoGeograficaService.excluirByIdLocalizacaoGeografica(this.localizacaoGeograficaSelecionada);*/
			
			excluirLocGeo();
			
			this.localizacaoGeograficaSelecionada.setDadoGeograficoCollection(null);
	//		this.localizacaoGeograficaSelecionada.setIdeLocalizacaoGeografica(null);
			habilitaIncluirLoc = false;
			editandoVertice = false;
			
			localizacaoGeograficaService.excluirDadosPersistidos(this.localizacaoGeograficaSelecionada);
			localizacaoGeograficaSelecionada.setDadoGeograficoCollection(null);
			localizacaoGeograficaSelecionada.setIdeLocalizacaoGeografica(null);
			this.localizacaoGeograficaSelecionada.setIdeLocalizacaoGeografica(null);
			if(!Util.isNull(geoReferenciavel)) {
				geoReferenciavel.setIde(null);
			}
			habilitaIncluirLoc = false;
			editandoVertice = false;
			JsfUtil.addSuccessMessage("Exclusão efetuada com Sucesso.");
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}

	public void excluirShapeSemMensagem(Integer pIdeLocalizacaoGeografica) {
		try {
			this.localizacaoGeograficaSelecionada.setDadoGeograficoCollection(null);
			localizacaoGeograficaService.excluirDadosPersistidos(pIdeLocalizacaoGeografica);
			habilitaIncluirLoc = false;
			editandoVertice = false;
		} catch (Exception exception) {
			JsfUtil.addErrorMessage(exception.getMessage());
		}
	}
	
	public void verificaTipoSecaoGeometricaShape() throws Exception {
		if(isTemLocGeoDesenho()) {
			this.datumSelecionado = this.serviceDatum.carregar(4);
		}
		else {
			this.datumSelecionado = this.serviceDatum.carregar(0);
		}
	}

	public boolean getDisabledIncluirPonto() {
		if(disableParaVisualizar) {
			return true;
		}

		if((!Util.isNullOuVazio(localizacaoGeograficaSelecionada)) && (!Util.isNullOuVazio(localizacaoGeograficaSelecionada.getDadoGeograficoCollection()))) {
			if((localizacaoGeograficaSelecionada.getDadoGeograficoCollection().size() > 0) && (pontoUnico)) {
				return true;
			}
		}

		return false;
	}

	public Collection<SistemaCoordenada> getDatums() {
		if(datums != null && datums.size() > 1){} 
		else if (!Util.isNullOuVazio(datums) && datums.size() == 1){}
		else {
			listarSistemaCoordenada();
		}
		return datums;
	}

	public void setDatums(Collection<SistemaCoordenada> datums) {
		this.datums = datums;
	}

	public ClassificacaoSecaoGeometrica getSecaoGeometricaSelecionada() {
		return secaoGeometricaSelecionada;
	}

	public void setSecaoGeometricaSelecionada(ClassificacaoSecaoGeometrica secaoGeometricaSelecionada) {
		this.secaoGeometricaSelecionada = secaoGeometricaSelecionada;
	}

	public boolean isPnlVerticesHabilitado() {
		return isPnlVerticesHabilitado;
	}

	public void setPnlVerticesHabilitado(boolean isPnlVerticesHabilitado) {
		this.isPnlVerticesHabilitado = isPnlVerticesHabilitado;
	}

	public boolean isUplShapefile() {
		getMostraLista();
		return isUplShapefile;
	}

	public boolean getUplShapefile() {
		return isUplShapefile;
	}

	public void setUplShapefile(boolean isUplShapefile) {
		this.isUplShapefile = isUplShapefile;
	}

	public SistemaCoordenada getDatumSelecionado() {
		return datumSelecionado;
	}

	public void setDatumSelecionado(SistemaCoordenada datumSelecionado) {
		this.datumSelecionado = datumSelecionado;
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

	public DadoGeografico getVertice() {
		return vertice;
	}

	public void setVertice(DadoGeografico vertice) {
		this.vertice = vertice;
	}

	public DadoGeografico getVerticeExclusao() {
		return verticeExclusao;
	}

	public void setVerticeExclusao(DadoGeografico pVerticeExclusao) {
		verticeExclusao = pVerticeExclusao;
	}

	public boolean isDesabilitarVertices() {
		return desabilitarVertices;
	}

	public void setDesabilitarVertices(boolean pDesabilitarVertices) {
		desabilitarVertices = pDesabilitarVertices;
	}

	public Boolean getMostraLista() {
		if (Util.isNullOuVazio(localizacaoGeograficaSelecionada)) {
			return false;
		}
		if (ContextoUtil.getContexto().getBloquearLocGeoImovelDepoisDeImovelSalvo()) {
			mostraLista = false;
			isUplShapefile = false;
			this.localizacaoGeograficaSelecionada = null;
			this.datumSelecionado = null;
			ContextoUtil.getContexto().setBloquearLocGeoImovelDepoisDeImovelSalvo(false);
			return mostraLista;
		}

		try {
			if (!Util.isNullOuVazio(localizacaoGeograficaSelecionada.getDadoGeograficoCollection())
					&& !localizacaoGeograficaSelecionada.getDadoGeograficoCollection().isEmpty()) {

				if (isTemLocGeoPonto()) {
					mostraLista = true;
					isUplShapefile = false;
				} else if (isTemLocGeoShape()) {
					mostraLista = false;
					isUplShapefile = true;
				} else {
					mostraLista = false;
					isUplShapefile = false;
				}
			} else {
				mostraLista = false;
			}
		} catch (Exception e) {
			try {
				localizacaoGeograficaSelecionada = localizacaoGeograficaService.carregar(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica());
				if (!Util.isNullOuVazio(localizacaoGeograficaSelecionada.getDadoGeograficoCollection())
						&& !localizacaoGeograficaSelecionada.getDadoGeograficoCollection().isEmpty()) {
					if (isTemLocGeoPonto()) {
						mostraLista = true;
						isUplShapefile = false;
					} else if (localizacaoGeograficaSelecionada.getIdeClassificacaoSecao().getIdeClassificacaoSecao()
							.equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId())) {
						mostraLista = false;
						isUplShapefile = true;
					} else {
						mostraLista = false;
						isUplShapefile = false;
					}
				} else {
					mostraLista = false;
					isUplShapefile = false;
				}
			} catch (Exception e1) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e1);
			}
		}
		return mostraLista;
	}

	public void setMostraLista(Boolean mostraLista) {
		this.mostraLista = mostraLista;
	}

	public Imovel getImovel() {
		return imovel;
	}

	public void setImovel(Imovel imovel) {
		this.imovel = imovel;
	}

	public Boolean getInsereVertices() {
		if (Util.isNullOuVazio(localizacaoGeograficaSelecionada)) {
			return insereVertices = false;
		}
		if (!Util.isNullOuVazio(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica())) {
			insereVertices = true;
		}

		return insereVertices;
	}

	public void setInsereVertices(Boolean insereVertices) {
		this.insereVertices = insereVertices;
	}

	public String getCaminhoArquivo() {
		return caminhoArquivo;
	}

	public void setCaminhoArquivo(String caminhoArquivo) {
		this.caminhoArquivo = caminhoArquivo;
	}

	public List<String> getListaArquivo() {
		return listaArquivo;
	}

	public void setListaArquivo(List<String> listaArquivo) {
		this.listaArquivo = listaArquivo;
	}

	public StreamedContent getArquivoBaixar() {
		return arquivoBaixar;
	}

	public void setArquivoBaixar(StreamedContent arquivoBaixar) {
		this.arquivoBaixar = arquivoBaixar;
	}

	public Boolean getTemArquivo() {
		return temArquivo;
	}

	public void setTemArquivo(Boolean temArquivo) {
		this.temArquivo = temArquivo;
	}

	public Boolean getDesabilitaIncluirVerticeLocGeoImovel() {
		if (!Util.isNullOuVazio(localizacaoGeograficaSelecionada)) {
			if (!Util.isNullOuVazio(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica())) {
				desabilitaIncluirVerticeLocGeoImovel = false;
			} else {
				desabilitaIncluirVerticeLocGeoImovel = true;
			}
		} else {
			desabilitaIncluirVerticeLocGeoImovel = true;
		}

		return desabilitaIncluirVerticeLocGeoImovel;
	}

	/**
	 * @param desabilitaIncluirVerticeLocGeoImovel
	 *            the desabilitaIncluirVerticeLocGeoImovel to set
	 */
	public void setDesabilitaIncluirVerticeLocGeoImovel(Boolean desabilitaIncluirVerticeLocGeoImovel) {
		this.desabilitaIncluirVerticeLocGeoImovel = desabilitaIncluirVerticeLocGeoImovel;
	}

	public Boolean getModoVisualizacao() {
		return modoVisualizacao;
	}

	public void setModoVisualizacao(Boolean modoVisualizacao) {
		this.modoVisualizacao = modoVisualizacao;
	}

	/**
	 * @return the arqDeExclusao
	 */
	public String getArqDeExclusao() {
		return arqDeExclusao;
	}

	/**
	 * @param arqDeExclusao
	 *            the arqDeExclusao to set
	 */
	public void setArqDeExclusao(String arqDeExclusao) {
		this.arqDeExclusao = arqDeExclusao;
	}

	/**
	 * @return the existeTheGeom
	 */
	public Boolean getExisteTheGeom() {
		try {
			
			if(Util.isNullOuVazio(localizacaoGeograficaSelecionada)) {
				return false;
			}
			
			if(!firstTime) {
				return existeTheGeom;
			}

			this.existeTheGeom = localizacaoGeograficaService.verificaSeExisteTheGeomValido(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica());
			firstTime = false;
			
			if(!this.existeTheGeom && !Util.isNullOuVazio(this.localizacaoGeograficaSelecionada) && (Util.isNullOuVazio(this.localizacaoGeograficaSelecionada.getDadoGeograficoCollection()))){
				
				localizacaoGeograficaService.excluirDadosPersistidos(this.localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica());
				this.localizacaoGeograficaSelecionada.setParamPersistDadoGeoCollection(null);
				this.localizacaoGeograficaSelecionada.setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
			}
			return this.existeTheGeom;
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return false;
		}
	}

	public void setExisteTheGeom(Boolean existeTheGeom) {
		this.existeTheGeom = existeTheGeom;
	}

	public String getCoordenadaPontoxy() {
		return coordenadaPontoxy;
	}

	public void setCoordenadaPontoxy(String coordenadaPontoxy) {
		this.coordenadaPontoxy = coordenadaPontoxy;
	}

	public List<ClassificacaoSecaoGeometrica> getListaSecaoGeomerica() {
		return listaSecaoGeomerica;
	}

	public void setListaSecaoGeomerica(List<ClassificacaoSecaoGeometrica> listaSecaoGeomerica) {
		this.listaSecaoGeomerica = listaSecaoGeomerica;
	}

	public String getCaminhoDesenharGeoBahia() {
		return URLEnum.CAMINHO_GEOBAHIA.toString();
	}

	public boolean isDisableParaVisualizar() {
		return disableParaVisualizar;
	}

	public boolean isDisableParaExcluir() {
		return disableParaExcluir;
	}
	
	public Integer getTipoSecaoGeometrica() {
		return tipoSecaoGeometrica;
	}

	public void setTipoSecaoGeometrica(Integer tipoSecaoGeometrica) {
		this.tipoSecaoGeometrica = tipoSecaoGeometrica;
	}

	public Object getObjetoLocalizacao() {
		return objetoLocalizacao;
	}

	public void setObjetoLocalizacao(Object objetoLocalizacao) {
		this.objetoLocalizacao = objetoLocalizacao;
	}

	public String getFracaoGrauLatitudeDecimal() {
		return fracaoGrauLatitudeDecimal;
	}

	public void setFracaoGrauLatitudeDecimal(String fracaoGrauLatitudeDecimal) {
		this.fracaoGrauLatitudeDecimal = fracaoGrauLatitudeDecimal;
	}

	public String getFracaoGrauLongitudeDecimal() {
		return fracaoGrauLongitudeDecimal;
	}

	public void setFracaoGrauLongitudeDecimal(String fracaoGrauLongitudeDecimal) {
		this.fracaoGrauLongitudeDecimal = fracaoGrauLongitudeDecimal;
	}

	public boolean isEditandoVertice() {
		return editandoVertice;
	}

	public void setEditandoVertice(boolean editandoVertice) {
		this.editandoVertice = editandoVertice;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}

	public Boolean getPontoUnico() {
		return pontoUnico;
	}

	public void setPontoUnico(Boolean pontoUnico) {
		this.pontoUnico = pontoUnico;
	}

	public boolean isClassificacaoSelecionada(){
		return !Util.isNullOuVazio(this.localizacaoGeograficaSelecionada.getIdeClassificacaoSecao());
	}

	public BigDecimal getAreaSupressao() {
		return areaSupressao;
	}

	public void setAreaSupressao(BigDecimal areaSupressao) {
		this.areaSupressao = areaSupressao;
	}

	public Integer getZone() {
		return zone;
	}

	public void setZone(Integer zone) {
		this.zone = zone;
	}

	public String getLatitudeUTM() {
		return latitudeUTM;
	}

	public void setLatitudeUTM(String latitudeUTM) {
		this.latitudeUTM = latitudeUTM;
	}

	public String getLongitudeUTM() {
		return longitudeUTM;
	}

	public void setLongitudeUTM(String longitudeUTM) {
		this.longitudeUTM = longitudeUTM;
	}

	public Boolean getHabilitaIncluirLoc() {
		return habilitaIncluirLoc;
	}

	public void setHabilitaIncluirLoc(Boolean habilitaIncluirLoc) {
		this.habilitaIncluirLoc = habilitaIncluirLoc;
	}

	public LocalizacaoGeografica recuperarLocalizacaoGeograficaSomenteComSistemaCoordenada() throws Exception{
		return localizacaoGeograficaService.carregarSomenteComSistemaCoordenada(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica());
	}

	public Boolean getIsPerfuracaoPocoOuCaptacaoOuAquicuturaEmRede() {
		return isPerfuracaoPocoOuCaptacaoOuAquicuturaEmRede;
	}

	public void setIsPerfuracaoPocoOuCaptacaoOuAquicuturaEmRede(Boolean isPerfuracaoPocoOuCaptacaoOuAquicuturaEmRede) {
		this.isPerfuracaoPocoOuCaptacaoOuAquicuturaEmRede = isPerfuracaoPocoOuCaptacaoOuAquicuturaEmRede;
	}

	public Integer getRequerimento() {
		return requerimento;
	}

	public void setRequerimento(Integer requerimento) {
		this.requerimento = requerimento;
	}

	public Boolean getIsASV() {
		return isASV;
	}

	public void setIsASV(Boolean isASV) {
		this.isASV = isASV;
	}

	public Boolean getIsNaoValidaMunicipio() {
		return isNaoValidaMunicipio;
	}

	public void setIsNaoValidaMunicipio(Boolean isNaoValidaMunicipio) {
		this.isNaoValidaMunicipio = isNaoValidaMunicipio;
	}

	public Boolean getIsIntervencao() {
		return isIntervencao;
	}

	public void setIsIntervencao(Boolean isIntervencao) {
		this.isIntervencao = isIntervencao;
	}

	public boolean isExistePonto() {
		return !Util.isNullOuVazio(Util.isNull(localizacaoGeograficaSelecionada)) && !Util.isNullOuVazio(localizacaoGeograficaSelecionada.getDadoGeograficoCollection());
	}

	public boolean isAnaliseTecnicaDeFce() {
		return analiseTecnicaDeFce;
	}

	public void setAnaliseTecnicaDeFce(boolean analiseTecnicaDeFce) {
		this.analiseTecnicaDeFce = analiseTecnicaDeFce;
	}
	
	public Boolean getShowIncluirVertice() {
		return showIncluirVertice;
	}

	public void setShowIncluirVertice(Boolean showIncluirVertice) {
		this.showIncluirVertice = showIncluirVertice;
	}

	public void setIdDoComponenteParaSerAtualizado(String idDoComponenteParaSerAtualizado) {
		this.idDoComponenteParaSerAtualizado = idDoComponenteParaSerAtualizado;
	}
	
	public boolean isShapeTipoPonto() {
		return shapeTipoPonto;
	}

	public void setShapeTipoPonto(boolean shapeTipoPonto) {
		this.shapeTipoPonto = shapeTipoPonto;
	}
	
	public GeoReferenciavel getGeoReferenciavel() {
		return geoReferenciavel;
	}

	public MetodoUtil getMetodoAtualizarExterno() {
		return metodoAtualizarExterno;
	}

	public void setMetodoAtualizarExterno(MetodoUtil metodoAtualizarExterno) {
		this.metodoAtualizarExterno = metodoAtualizarExterno;
	}
	
	public void setGeoReferenciavel(GeoReferenciavel geoReferenciavel) {
		try {
			this.geoReferenciavel = geoReferenciavel;
			localizacaoGeograficaSelecionada = geoReferenciavel.getIdeLocalizacaoGeografica();
			datumSelecionado = localizacaoGeograficaSelecionada.getIdeSistemaCoordenada();
			listaArquivo = new ArrayList<String>();
			temArquivo = false;
			this.existeTheGeom = localizacaoGeograficaService.verificaSeExisteTheGeomValido(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica());
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	public Boolean getIsLicenciamentoAquicutura() {
		return isLicenciamentoAquicutura;
	}


	public void setIsLicenciamentoAquicutura(Boolean isLicenciamentoAquicutura) {
		this.isLicenciamentoAquicutura = isLicenciamentoAquicutura;
	}
	
	public boolean isIndPct() {
		return indPct;
	}


	public void setIndPct(boolean indPct) {
		this.indPct = indPct;
	}
}