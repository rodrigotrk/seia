package br.gov.ba.seia.controller;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.App;
import br.gov.ba.seia.entity.AreaProdutiva;
import br.gov.ba.seia.entity.AreaProdutivaTipologiaAtividade;
import br.gov.ba.seia.entity.AreaProdutivaTipologiaAtividadeAgricultura;
import br.gov.ba.seia.entity.AreaProdutivaTipologiaAtividadeAnimal;
import br.gov.ba.seia.entity.AreaProdutivaTipologiaAtividadePiscicultura;
import br.gov.ba.seia.entity.Certificado;
import br.gov.ba.seia.entity.ClassificacaoSecaoGeometrica;
import br.gov.ba.seia.entity.CronogramaEtapa;
import br.gov.ba.seia.entity.CronogramaRecuperacao;
import br.gov.ba.seia.entity.GeoJsonSicar;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.ImovelRuralRppn;
import br.gov.ba.seia.entity.ImovelRuralSicar;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.SistemaCoordenada;
import br.gov.ba.seia.entity.TipoApp;
import br.gov.ba.seia.entity.TipoEstadoConservacao;
import br.gov.ba.seia.entity.VegetacaoNativaFinalidade;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.TemaGeoseiaEnum;
import br.gov.ba.seia.enumerator.TipoCertificadoEnum;
import br.gov.ba.seia.enumerator.TipoEstadoConservacaoEnum;
import br.gov.ba.seia.enumerator.TipologiaCefirEnum;
import br.gov.ba.seia.enumerator.URLEnum;
import br.gov.ba.seia.exception.AreaDeclaradaInvalidaException;
import br.gov.ba.seia.facade.EnderecoFacade;
import br.gov.ba.seia.facade.ImovelRuralFacade;
import br.gov.ba.seia.facade.auditoria.AuditoriaFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.ArquivoProcessoService;
import br.gov.ba.seia.service.ImovelRuralService;
import br.gov.ba.seia.service.ParamPersistDadoGeoService;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.json.JsonUtil;

/**
 * Controller de Atualização Shapes de Imoveis Rurais - CEFIR
 * @author danilos.santos
 */
@Named("atualizarShapesController")
@ViewScoped
public class AtualizarShapesController implements Serializable{
	
	private static final long serialVersionUID = 4636891446568432547L;
	
	/**
	 * Controllers
	 */
	@Inject
	@Named("imovelRuralControllerNew")
	private ImovelRuralControllerNew imovelRuralControllerNew;
	
	/**
	 * Services
	 */
	@EJB
	private ImovelRuralFacade imovelRuralServiceFacade;
	
	@EJB
	protected AuditoriaFacade auditoria;
	
	@EJB
	private ImovelRuralService imovelRuralService;
	
	@EJB
	private EnderecoFacade enderecoFacade;
	
	@EJB
	private ParamPersistDadoGeoService paramPersistDadoGeoService;
	
	@EJB
	private ArquivoProcessoService arquivoProcessoService;
	
	/**
	 * Variaveis
	 */
	private int activeIndex= 0;
	private int activeIndexAnterior= 0;
	private boolean exibirBtnVoltar = false;
	private boolean exibirBtnFinalizar = false;
	private boolean exibirBtnSalvarOuAvancar = false;
	private String  observacoes;
	private String abaAtual = "abaLimitePropriedade";
	private boolean exibirAbaVisualizar = false;
	private ImovelRural imovelRural = new ImovelRural();
	private ArrayList<ImovelRural> listaImoveis;
	private ImovelRuralRppn imovelRuralRppn;
	private boolean tab0;
	private boolean tab1;
	private boolean tab2;
	private boolean tab3;
	private boolean tab4;
	private boolean tab5;
	private boolean tab6;
	private String caminhoDesenharGeoBahia  = URLEnum.CAMINHO_GEOBAHIA.toString();
	private ImovelRural imovelRuralOld = new ImovelRural();
	private ArrayList<ImovelRural> listaShapeUpldImoveis;
	private List<SistemaCoordenada> listaDatums;
	private List<ClassificacaoSecaoGeometrica> listaSecaoGeometrica;
	private List<String> listaLocalizacaoGeo;
	private boolean incluirShape;
	private boolean atualizarImovel = true;
	private boolean atualizarApp = true;
	private boolean atualizarRppn = true;
	private boolean atualizarAd = true;
	private Collection<App> appCollection;
	private String geometriaLoc;
	private String ideLocalizacaoGeograficaSelect;
	private Collection<AreaProdutiva>areaProdutivaCollection;
	private Collection<ImovelRuralRppn>listaImovelRuralRppn;
	private Collection<ImovelRuralRppn>listaImovelRuralRppnAlterados;
	private boolean incluirShapeRppn;
	/*Armazena o valor da area do shape temporario*/
	private Double valAreaLim;
	private Double valAreaRL;
	private Double valAreaVN;
	private Double valAreaAPP;
	private Double valAreaRPPN;
	private Double valAreaAtvDesen;
	/*Strings que geram os nomes no poup-up de Downloand de Shapes*/
	private String nomeSHP;
	private String nomeSHX;
	private String nomeDBF;
	private LocalizacaoGeografica localizacaoGeograficaSelecionada = new LocalizacaoGeografica();
	
	/**
	 * Variaveis Shapes selecionados
	 */
	private ClassificacaoSecaoGeometrica secaoGeoSeleccionada = new ClassificacaoSecaoGeometrica();
	private SistemaCoordenada sistemaCoordSelecionado = new SistemaCoordenada();
	private LocalizacaoGeografica localizacaoSelecionada = new LocalizacaoGeografica();
	private ImovelRural limitePropSelecionado = new ImovelRural();
	private ImovelRural reservaLegalSelecionado = new ImovelRural();
	private ImovelRural vegetacaoNativaSelecionado = new ImovelRural();
	private App appSelecionada;
	private AreaProdutiva areaProdutivaSelecionada;	
	private ImovelRuralRppn rppnSelecionado = new ImovelRuralRppn();
	private String observacaoLP;
	private String observacaoRL;
	private String observacaoVN;
	private String observacaoAPP;
	private String observacaoRPPN;
	private String observacaoAD;
	
	/**
	 * Objetos alterados
	 */
	private ImovelRural limitePropAlterado = new ImovelRural();
	private ImovelRural reservaLegalAlterado = new ImovelRural();
	private ImovelRural vegetacaoNativaAlterado = new ImovelRural();
	private Collection<App>listaAppsAlterados;
	private ImovelRuralRppn rppnAlterado = new ImovelRuralRppn();
	private Collection<AreaProdutiva>listaAreasProdutivasAlterados;
	
	//Objetos Olds
	private ImovelRural limitePOld = new ImovelRural();
	private ImovelRural imovel = new ImovelRural();
	private ImovelRural reservaLegalOld = new ImovelRural();
	private ImovelRural vegetacaoNativaOld = new ImovelRural();
	private Collection<App>listaAppsOld;
	private ImovelRuralRppn rppnOld = new ImovelRuralRppn();
	private Collection<AreaProdutiva>listaAreasProdutivasOld;

	
	
	public void carregarDados(){
		try {
			limpar();
			abaLimitePropriedade();
			abaReservaLegal();
			abaVegetacaoNativa();
			abaAreaPreservacaoPermanente();
			abaRPPN();
			abaAtividadeDesenvolvida();
			habilitarAbas();
			mudarAbas(abaAtual);
			setImovelRuralOld(this.imovelRural.clone());
			atualizarObservacaoAba();
		} catch (CloneNotSupportedException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void limpar(){
		controlarBotoes(false,false,false);
		estadoInicalAbas();
		observacoes = null;
		abaAtual = "abaLimitePropriedade";
		activeIndex= 0;
		activeIndexAnterior = 0;
		atualizarImovel = true;
		atualizarApp = true;
		atualizarRppn = true;
		atualizarAd = true;
		limparShapesSelecionados();
	}
	
	/**
	 * Chamada de dos metodos que possuem as regras de cada aba. 
	 */
	public void controlarAbas(TabChangeEvent event) {
		guardarObservacaoAba();
		mudarAbas(event.getTab().getId());
		activeIndexAnterior = activeIndex;
		atualizarObservacaoAba();
	}

	/**
	 * Metodo que faz a mudança de aba levando em consideração a aba atual e qual proxima aba habilitada
	 */
	private void mudarAbas(String aba) {
		abaAtual = aba;
		incluirShape = false;
		incluirShapeRppn = false;
		atualizarImovel = true;
		atualizarApp = true;
		atualizarRppn = true;
		atualizarAd  = true;
		activeIndexAnterior = activeIndex;
		if ("abaLimitePropriedade".equals(aba) && !tab0) {
			abaLimitePropriedade();
		}else if ("abaReservaLegal".equals(aba) && !tab1) {
			abaReservaLegal();
		} else if ("abaVegetacaoNativa".equals(aba) && !tab2) {
			abaVegetacaoNativa();
		} else if ("abaAPP".equals(aba) && !tab3) {
			abaAreaPreservacaoPermanente();
		} else if ("abaRPPN".equals(aba) && !tab4) {
			abaRPPN();
		} else if ("abaAtividadeDesenvolvida".equals(aba) && !tab5) {
			abaAtividadeDesenvolvida();
		} else if ("abaVisualizacao".equals(aba) && !tab6) {
			abaVisualizacao();
		}else{
			avancarAbas();
		}
	}
	
	/**
	 * Metodo que faz a mudança de abas atraves do botão avançar
	 */
	public void avancarAbas() {
		boolean podeMudar = false;
		guardarObservacaoAba();
		if ("abaLimitePropriedade".equals(abaAtual)) {
			abaAtual = "abaReservaLegal";
			if(!tab1){
				podeMudar = true;
			}
		} else if ("abaReservaLegal".equals(abaAtual)) {
			abaAtual = "abaVegetacaoNativa";
			if(!tab2){
				podeMudar = true;
			}
		} else if ("abaVegetacaoNativa".equals(abaAtual)) {
			abaAtual = "abaAPP";
			if(!tab3){
				podeMudar = true;
			}
		} else if ("abaAPP".equals(abaAtual)) {
			abaAtual = "abaRPPN";
			if(!tab4){
				podeMudar = true;
			}
		} else if ("abaRPPN".equals(abaAtual)) {
			abaAtual = "abaAtividadeDesenvolvida";
			if(!tab5){
				podeMudar = true;
			}else{
				salvar();
				abaAtual = "abaVisualizacao";
				podeMudar = true;
			}
		} else if ("abaAtividadeDesenvolvida".equals(abaAtual)) {
			abaAtual = "abaVisualizacao";
			if(!tab6){
				podeMudar = true;
			}
		} 
		activeIndex ++ ;
		atualizarObservacaoAba();
		
		if(podeMudar){
			mudarAbas(abaAtual);
		}else{
			avancarAbas();
		}
	}
	
	/**
	 * Metodo que faz a mudança de abas atraves do botão voltar
	 */
	public void voltarAbas() {
		boolean podeMudar = false;
		guardarObservacaoAba();
		if ("abaReservaLegal".equals(abaAtual)) {
			abaAtual = "abaLimitePropriedade";
			if(!tab0){
				podeMudar = true;
			}
		} else if ("abaVegetacaoNativa".equals(abaAtual)) {
			abaAtual = "abaReservaLegal";
			if(!tab1){
				podeMudar = true;
			}
		} else if ("abaAPP".equals(abaAtual)) {
			abaAtual = "abaVegetacaoNativa";
			if(!tab2){
				podeMudar = true;
			}
		} else if ("abaRPPN".equals(abaAtual)) {
			abaAtual = "abaAPP";
			if(!tab3){
				podeMudar = true;
			}
		} else if ("abaAtividadeDesenvolvida".equals(abaAtual)) {
			abaAtual = "abaRPPN";
			if(!tab4){
				podeMudar = true;
			}
		} else if ("abaVisualizacao".equals(abaAtual)) {
			abaAtual = "abaAtividadeDesenvolvida";
			if(!tab5){
				podeMudar = true;
			}
		}
		activeIndex -- ;
		atualizarObservacaoAba();
		if(podeMudar){
			mudarAbas(abaAtual);
		}else{
			voltarAbas();
		}
	}
	
	/**
	 * Metodo Salvar atualizações do Shapes
	 */
	public void salvar(){
		guardarObservacaoAba();
		if(validarObservacoes() && validarShapesatualizados()){
			try {
				visualizarShapesTemporarios();	
				tab6 = false;
				if ("abaAtividadeDesenvolvida".equals(abaAtual)) {
					abaAtual = "abaVisualizacao";
				} 
				activeIndex ++ ;
				mudarAbas(abaAtual);
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}
	}
	
	public void prepararParaFinalizar() {
		try {
			imovelRuralServiceFacade.finalizarAtualizacaoShapes(this);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void finalizar(){
		try {
			carregarTudoOld();
			atualizarImovelRural();
			atualizarEauditar();
			
			//Persistir Imovel
			persistirImovelRural();
			
			//Persistir APP
			if (!Util.isNull(this.imovelRural.getIndApp()) && this.imovelRural.getIndApp()) {
				for (App app : this.imovelRural.getAppCollection()) {
					persistirAppCefir(app);
				}
			}
			
			//Persistir RPPN
			persistirImovelRppnCefir(this.imovelRural);
			
			//Persistir Atividade Desenvolvida
			if (!Util.isNullOuVazio(imovelRural.getAreaProdutivaCollection())) {
				for (AreaProdutiva area : this.imovelRural.getAreaProdutivaCollection()) {
					persistirAreaProdutivaCefir(area);
				}
			}
			
			//Sincronizar SICAR
			this.imovelRuralServiceFacade.gerarCertificado(this.imovelRural);
			
			if (!Util.isNullOuVazio(imovelRural.getImovelRuralSicar()) && !Util.isNullOuVazio(imovelRural.getImovelRuralSicar().getIdeImovelRuralSicar())) {
				montarImovelRuralSicar();
				
				ImovelRuralSicar irs = imovelRural.getImovelRuralSicar().clone();
				irs.setIdeImovelRural(new ImovelRural(imovelRural.getIdeImovelRural()));
				imovelRuralServiceFacade.atualizarImovelRuralSicarComMerge(irs);
				
			} else {
				imovelRural.setImovelRuralSicar(new ImovelRuralSicar());
				imovelRural.getImovelRuralSicar().setDtcCriacao(new Date());
				montarImovelRuralSicar();
				imovelRuralServiceFacade.salvarImovelRuralSicar(imovelRural.getImovelRuralSicar());
			}
			
			limparShapesSelecionados();
			Html.atualizar("filtroImoveis");
			Html.esconder("dialogAtualizarShape");
        	JsfUtil.addSuccessMessage("Dados salvos com sucesso!");
        	
		} catch (Exception e) {
			MensagemUtil.erro("Houve um erro ao salvar.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private void montarImovelRuralSicar() throws Exception {
		try {
			carregarDadosGeoJsonSicar();
			String numToken = "";
			if (imovelRural.isTermoCompromisso()) {
				List<Certificado> lCefir = imovelRuralServiceFacade.obterCertificadoPorImovelAndTipo(this.imovelRural.getIdeImovelRural(), TipoCertificadoEnum.TERMO_DE_COMPROMISSO.getId());
				numToken = "-" + lCefir.get(0).getNumToken();
				imovelRural.getImovelRuralSicar().setNumCertificado(lCefir.get(0).getNumCertificado());
			} else {
				List<Certificado> lCefir = imovelRuralServiceFacade.obterCertificadoPorImovelAndTipo(this.imovelRural.getIdeImovelRural(), TipoCertificadoEnum.CEFIR.getId());
				if (!Util.isNullOuVazio(lCefir)) {
					numToken = "-" + lCefir.get(0).getNumToken();
					imovelRural.getImovelRuralSicar().setNumCertificado(lCefir.get(0).getNumCertificado());
				}
			}
			imovelRural.getImovelRuralSicar().setIdeImovelRural(imovelRural);
			imovelRural.getImovelRuralSicar().setNumProtocolo("BA-" + imovelRural.getIdeImovelRural() + "-" + new SimpleDateFormat("yyyyMMdd").format(new Date())+ numToken);
			imovelRural.getImovelRuralSicar().setIndSicronia(false);
			imovelRural.getImovelRuralSicar().setDtcIniSicronia(null);
			imovelRural.getImovelRuralSicar().setDtcFimSicronia(null);
			imovelRural.getImovelRuralSicar().setToken(null);
			imovelRural.getImovelRuralSicar().setMsgRetornoSincronia(null);
			imovelRural.getImovelRuralSicar().setCodRetornoSincronia(null);
			imovelRural.getImovelRuralSicar().setUrlReciboInscricao(null);
			imovelRural.getImovelRuralSicar().setJson(JsonUtil.montarJsonImovelRuralSicar(this.imovelRural, this.imovelRural.getIdeImovelRuralRppn()).toString());
			
		} catch (Exception e) {
			MensagemUtil.erro("Erro ao montar o imóvel rural.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private void carregarDadosGeoJsonSicar() throws Exception {
		List<Integer> localizacoes = new ArrayList<Integer>();
		GeoJsonSicar geoJsonSicar = new GeoJsonSicar();
		localizacoes.add(imovelRural.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
		geoJsonSicar = imovelRuralServiceFacade.buscarGeoJsonSicar(localizacoes);
		
		if(geoJsonSicar != null){
			imovelRural.setGeoJsonSicar(geoJsonSicar);
			geoJsonSicar = new GeoJsonSicar();
		}
		localizacoes.clear();

		if (!Util.isNullOuVazio(imovelRural.getReservaLegal()) && !Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica())) {
			localizacoes.add(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			geoJsonSicar = imovelRuralServiceFacade.buscarGeoJsonSicar(localizacoes);
			if(geoJsonSicar != null){
				imovelRural.getReservaLegal().setGeoJsonSicar(geoJsonSicar);
				geoJsonSicar = new GeoJsonSicar();
			}
		}

		localizacoes.clear();

		if (imovelRural.getIndVegetacaoNativa() && !Util.isNullOuVazio(imovelRural.getVegetacaoNativa())) {
			localizacoes.add(imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			geoJsonSicar = imovelRuralServiceFacade.buscarGeoJsonSicar(localizacoes);
			if(geoJsonSicar != null){
				imovelRural.getVegetacaoNativa().setGeoJsonSicar(geoJsonSicar);
				geoJsonSicar = new GeoJsonSicar();
			}
		}

		localizacoes.clear();

		if (!Util.isNullOuVazio(this.imovelRural.getIndAreaRuralConsolidada())
				&& this.imovelRural.getIndAreaRuralConsolidada()) {
			localizacoes.add(imovelRural.getIdeAreaRuralConsolidada().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			geoJsonSicar = imovelRuralServiceFacade.buscarGeoJsonSicar(localizacoes);
			if(geoJsonSicar != null){
				imovelRural.getIdeAreaRuralConsolidada().setGeoJsonSicar(geoJsonSicar);
				geoJsonSicar = new GeoJsonSicar();
			}
		}

		if (imovelRural.getIndApp() && !Util.isNullOuVazio(imovelRural.getAppCollection())) {
			List<Integer> localizacoesApp = new ArrayList<Integer>();
			for (App app : imovelRural.getAppCollection()) {
				localizacoes.clear();
				localizacoes.add(app.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				geoJsonSicar = imovelRuralServiceFacade.buscarGeoJsonSicar(localizacoes);
				
				if(geoJsonSicar != null){
					app.setGeoJsonSicar(geoJsonSicar);
					geoJsonSicar = new GeoJsonSicar();
				}
				
				localizacoesApp.add(app.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			}
			geoJsonSicar = imovelRuralServiceFacade.buscarGeoJsonSicar(localizacoesApp);
			if(geoJsonSicar != null){
				imovelRural.setGeoJsonSicarAppTotal(geoJsonSicar);
				geoJsonSicar = new GeoJsonSicar();
			}
		}
	}
	
	private void persistirImovelRural() throws Exception {
		if (imovelRural.getIdeLocalizacaoGeografica().getListArquivosShape().size() == 3){
			
			if (imovelRuralControllerNew.temShapeTemporario(
					imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.LIMITE_PROPRIEDADE.getId(), 3)) {
				
				FileUploadUtil.moverArquivoShapePastaTemporariaParaPastaOriginalCefir(imovelRural.getIdeLocalizacaoGeografica(), "1", imovelRural.getIdeImovelRural().toString());
				imovelRuralServiceFacade.persistirShapes(imovelRural.getIdeLocalizacaoGeografica(), null);
				imovelRural.getIdeLocalizacaoGeografica().setNovosArquivosShapeImportados(false);
			}
			
			atualizarLocalizacaoGeografica(imovelRural.getIdeLocalizacaoGeografica());
		}
		
		if(imovelRural.getReservaLegal() != null && imovelRural.getReservaLegal().getIdeLocalizacaoGeografica() != null 
				&& imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getListArquivosShape().size() == 3) {
			
			if (imovelRuralControllerNew.temShapeTemporario(
					imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.RESERVA_LEGAL.getId(), 3)) {
				
				FileUploadUtil.moverArquivoShapePastaTemporariaParaPastaOriginalCefir(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica()  , "2", imovelRural.getIdeImovelRural().toString());
				imovelRuralServiceFacade.persistirShapes(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica(), null);
				imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().setNovosArquivosShapeImportados(false);
			}
			
			atualizarLocalizacaoGeografica(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica());
		}
		
		if (imovelRural.getVegetacaoNativa() != null && imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica() != null 
				&& imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getListArquivosShape().size() == 3) {
			
			if (imovelRuralControllerNew.temShapeTemporario(
					imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.VEGETACAO_NATIVA.getId(), 3)) {
				
				FileUploadUtil.moverArquivoShapePastaTemporariaParaPastaOriginalCefir(imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica(), "5", imovelRural.getIdeImovelRural().toString());
				imovelRuralServiceFacade.persistirShapes(imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica(), null);
				imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().setNovosArquivosShapeImportados(false);
			}
			
			atualizarLocalizacaoGeografica(imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica());
		}
	}

	/**
	 * Atualiza os shapes e audita as mudanças
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void atualizarEauditar() throws Exception {
		if(this.imovelRural != null && this.imovelRural.getIdeImovelRural() != null){
			excluirDadosGeografico(imovelRural.getIdeLocalizacaoGeografica().clone());
			imovelRuralServiceFacade.atualizarImovelRural(imovelRural.clone());
			auditoria.atualizar(limitePOld, this.imovelRural);
		}
		
		if(imovelRural.getReservaLegal() != null){
			excluirDadosGeografico(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().clone());
			imovelRuralServiceFacade.atualizarReservaLegal(this.imovelRural.getReservaLegal());
			auditoria.atualizar(reservaLegalOld.getReservaLegal(), this.imovelRural.getReservaLegal());
		}
		
		if(imovelRural.getVegetacaoNativa() != null){
			imovelRural.getVegetacaoNativa().setVegetacaoNativaFinalidadeCollection(new ArrayList<VegetacaoNativaFinalidade>());
			excluirDadosGeografico(imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().clone());
			imovelRuralServiceFacade.atualizarVegetacaoNativa(imovelRural.getVegetacaoNativa());
			auditoria.atualizar(vegetacaoNativaOld.getVegetacaoNativa(), this.imovelRural.getVegetacaoNativa());
		}
		
		if(this.imovelRural.getIdeImovelRuralRppn() != null){
			excluirDadosGeografico(imovelRural.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().clone());
			imovelRuralServiceFacade.atualizarImovelRuralRppn(this.imovelRural.getIdeImovelRuralRppn());
			auditoria.atualizar(rppnOld, this.imovelRural.getIdeImovelRuralRppn());
		}
		
		for (App appNew : listaAppsAlterados) {
			for (App appOld : listaAppsOld) {
				if(appNew.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica().equals(appOld.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())){
					excluirDadosGeografico(appNew.getIdeLocalizacaoGeografica().clone());
					imovelRuralServiceFacade.atualizarApp(appNew);
					auditoria.atualizar(appOld, appNew);
				}
			}
		}
		
		for (AreaProdutiva areaNew : listaAreasProdutivasAlterados) {
			for (AreaProdutiva areaOld : listaAreasProdutivasOld) {
				if(areaNew.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica().equals(areaOld.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())){
					excluirDadosGeografico(areaNew.getIdeLocalizacaoGeografica().clone());
					imovelRuralServiceFacade.atualizarAreaProdutiva(areaNew);
					auditoria.atualizar(areaOld, areaNew);
				}
			}
		}
	}
	
	private void controlarBotoes(boolean exibirBtnVoltar,boolean exibirBtnSalvarOuAvancar,boolean exibirBtnFinalizar){
		this.exibirBtnVoltar = exibirBtnVoltar;
		this.exibirBtnFinalizar = exibirBtnFinalizar;
		this.exibirBtnSalvarOuAvancar = exibirBtnSalvarOuAvancar;
	}
	
	private void habilitarAbas(){
		try {
			tab6 = true;
			if(!Util.isNullOuVazio(imovelRural)){
				tab0 = !existeTheGeom (imovelRural, 0);
				tab1 = !existeTheGeom (imovelRural, 1);
				tab2 = !existeTheGeom (imovelRural, 2);
			}else{
				tab0 = true;
				tab1 = true; 
				tab2 = true;
			}
			if(Util.isNullOuVazio(appCollection)){
				tab3 = true;
			}
			if(Util.isNullOuVazio(imovelRuralRppn)){
				tab4 = true;
			}
			if(Util.isNullOuVazio(areaProdutivaCollection)){
				tab5 = true;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	private boolean existeTheGeom (ImovelRural imovelRural, int tipo) throws Exception{
		if(tipo == 0){
			if(imovelRural.getIdeLocalizacaoGeografica() != null){
				return imovelRuralServiceFacade.existeTheGeom(imovelRural.getIdeLocalizacaoGeografica());
			}else{
				return false;
			}
		}else if(tipo == 1){
			if(imovelRural.getReservaLegal() != null && imovelRural.getReservaLegal().getIdeLocalizacaoGeografica() != null){
				return imovelRuralServiceFacade.existeTheGeom(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica());
			}else{
				return false;
			}
		}else if(tipo == 2){
			if(imovelRural.getVegetacaoNativa() != null && imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica() != null){
				return imovelRuralServiceFacade.existeTheGeom(imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica());
			}else{
				return false;
			}
		}
		return false;
	}
	
	private void estadoInicalAbas(){
		tab0 = false;
		tab1 = false; 
		tab2 = false;
		tab3 = false;
		tab4 = false;
		tab5 = false;
		tab6 = true;
		observacaoLP = null;
		observacaoRL = null;
		observacaoVN = null;
		observacaoAPP = null;
		observacaoRPPN = null;
		observacaoAD = null;
	}

	private void limparShapesSelecionados() {
		incluirShape = false;
		incluirShapeRppn = false;
		limitePropSelecionado = new ImovelRural();
		reservaLegalSelecionado = new ImovelRural();
		vegetacaoNativaSelecionado = new ImovelRural();
		appSelecionada = new App();
		rppnSelecionado = new ImovelRuralRppn();
		areaProdutivaSelecionada = new AreaProdutiva();
		listaAppsAlterados = new ArrayList<App>();
		listaShapeUpldImoveis = new ArrayList<ImovelRural>();
		listaImovelRuralRppnAlterados = new ArrayList<ImovelRuralRppn>();
		listaAreasProdutivasAlterados = new ArrayList<AreaProdutiva>();
	}
	
	/**
	 * Carrega todos os objetos originais
	 */
	private void carregarTudoOld()throws Exception{
		List<App> apps = new ArrayList<App>();
		List<AreaProdutiva> aProds = new ArrayList<AreaProdutiva>();
		listaAppsOld = new ArrayList<App>();
		listaAreasProdutivasOld = new ArrayList<AreaProdutiva>();
		
		try {
			if (!Util.isNullOuVazio(imovelRuralOld)){ 
				limitePOld = imovelRuralServiceFacade.carregarTudo(imovelRuralOld.getIdeImovelRural());
				rppnOld = imovelRuralServiceFacade.carregarImovelRuralRppnByIdeImovelRural(imovelRural);
			}
			
			if (!Util.isNullOuVazio(imovelRuralOld.getReservaLegal())) 
				reservaLegalOld.setReservaLegal(imovelRuralServiceFacade.carregarTudo(imovelRuralOld.getReservaLegal()));
			
			if (!Util.isNullOuVazio(imovelRuralOld.getVegetacaoNativa())) 
				vegetacaoNativaOld.setVegetacaoNativa(imovelRuralServiceFacade.carregarTudoVegetacaoNativa(imovelRuralOld.getVegetacaoNativa()));

			apps.addAll(imovelRuralServiceFacade.listarAppByImovelRural(imovelRuralOld));
			
			for (App app : apps) {
				app = imovelRuralServiceFacade.carregarTudoApp(app);
				listaAppsOld.add(app);
			}
			
			aProds.addAll(imovelRuralServiceFacade.listarAreaProdutivaByImovelRural(imovelRuralOld));
			
			for (AreaProdutiva aprod : aProds) {
				aprod = imovelRuralServiceFacade.carregarTudoAreaProdutiva(aprod);
				listaAreasProdutivasOld.add(aprod);
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao carregar dados do Imóvel, contate o administrador do sistema.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private void atualizarImovelRural() throws CloneNotSupportedException {
		
		atualizarDescricaoImovelRural();
		
		if(limitePropSelecionado != null && limitePropSelecionado.getIdeImovelRural() != null)
			this.imovelRural = limitePropSelecionado.clone();
		if(reservaLegalAlterado != null && reservaLegalAlterado.getReservaLegal() != null)
			this.imovelRural.setReservaLegal(reservaLegalAlterado.getReservaLegal());
		if(vegetacaoNativaAlterado != null && vegetacaoNativaAlterado.getVegetacaoNativa() != null)
			this.imovelRural.setVegetacaoNativa(vegetacaoNativaAlterado.getVegetacaoNativa());
		if(rppnAlterado != null && rppnAlterado.getIdeImovelRuralRppn() != null)
			this.imovelRural.setIdeImovelRuralRppn(rppnAlterado);
		if(listaAppsAlterados != null && !listaAppsAlterados.isEmpty())
			this.imovelRural.setAppCollection(listaAppsAlterados);
		if(listaAreasProdutivasAlterados != null && !listaAreasProdutivasAlterados.isEmpty())
			this.imovelRural.setAreaProdutivaCollection(listaAreasProdutivasAlterados);
	}
	
	private void visualizarShapesTemporarios() throws CloneNotSupportedException{
		if(limitePropSelecionado != null && limitePropSelecionado.getIdeImovelRural() != null)
			imovel = limitePropSelecionado.clone();
		if(reservaLegalAlterado != null && reservaLegalAlterado.getReservaLegal() != null)
			imovel.setReservaLegal(reservaLegalAlterado.getReservaLegal().clone());
		if(vegetacaoNativaSelecionado != null && vegetacaoNativaSelecionado.getVegetacaoNativa() != null)
			imovel.setVegetacaoNativa(vegetacaoNativaSelecionado.getVegetacaoNativa().clone());
		if(rppnAlterado != null && rppnAlterado.getIdeImovelRuralRppn() != null)
			imovel.setIdeImovelRuralRppn(rppnAlterado.clone());
		if(listaAppsAlterados != null && !listaAppsAlterados.isEmpty()){
			Collection<App> appCollection  = new ArrayList<App>();
			for (App app : listaAppsAlterados) {
				appCollection.add(app.clone());
			}
			imovel.setAppCollection(appCollection);
		}
		if(listaAreasProdutivasAlterados != null && !listaAreasProdutivasAlterados.isEmpty()){
			Collection<AreaProdutiva> areaCollection = new ArrayList<AreaProdutiva>();
			for (AreaProdutiva area : listaAreasProdutivasAlterados) {
				areaCollection.add(area.clone());
			}
			imovel.setAreaProdutivaCollection(areaCollection);
		}
	}
	
	private void atualizarDescricaoImovelRural(){
		
		if(limitePropSelecionado != null){
			limitePropSelecionado.setDscObservacaoAlteracaoShape(observacaoLP);
		}
		
		if(reservaLegalAlterado != null && reservaLegalAlterado.getReservaLegal() != null){
			reservaLegalAlterado.getReservaLegal().setDscObservacaoAlteracaoShape(observacaoRL);
		}
		
		if(vegetacaoNativaSelecionado != null && vegetacaoNativaSelecionado.getVegetacaoNativa() != null){
			vegetacaoNativaSelecionado.getVegetacaoNativa().setDscObservacaoAlteracaoShape(observacaoVN);
		}
		
		if(rppnAlterado != null){
			rppnAlterado.setDscObservacaoAlteracaoShape(observacaoRPPN);
		}
		
		for (App app : listaAppsAlterados) {
			app.setDscObservacaoAlteracaoShape(observacaoAPP);
		}
		for (AreaProdutiva area : listaAreasProdutivasAlterados) {
			area.setDscObservacaoAlteracaoShape(observacaoAD);
		}
	}
	
	public void persistirShapesRlParamPersistDadoGeo() throws Exception{		
		String retorno[] = null;
		String geometriaRl = null;
		
		retorno = imovelRuralServiceFacade.persistirShapeTheGeomImportacaoImovelFinalizado(imovelRural.getIdeImovelRural(), imovelRural.getIdeImovelRural().toString()+"_"+TemaGeoseiaEnum.RESERVA_LEGAL.getId(), reservaLegalSelecionado.getReservaLegal().getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid(), imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio());
		if (retorno != null) {
			if (retorno.length > 0){
				if(retorno[0].equalsIgnoreCase("ERRO")){
					throw new Exception(retorno[2]+" ["+retorno[1]+"]");
				} else {
					geometriaRl = imovelRuralServiceFacade.buscarGeometriaShapeTemporario(imovelRural.getIdeImovelRural(), TemaGeoseiaEnum.RESERVA_LEGAL.getId(), null);
					
					if(!imovelRuralServiceFacade.validaTipoGeometriaPoligono(null, geometriaRl)){
						throw new Exception("Geometria do shapefile carregado não é do tipo POLÍGONO.");
					}
					reservaLegalSelecionado.getReservaLegal().getIdeLocalizacaoGeografica().setNovosArquivosShapeImportados(true);
					reservaLegalSelecionado.getReservaLegal().getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
				}
			}
		}			
		
		if(!Util.isNullOuVazio(reservaLegalSelecionado.getReservaLegal().getIdeStatus()) && reservaLegalSelecionado.getReservaLegal().getIdeStatus().getIdeStatusReservaLegal().equals(9))
			reservaLegalSelecionado.getReservaLegal().setAlterarStatusRl(true);
		else
			reservaLegalSelecionado.getReservaLegal().setAlterarStatusRl(false);
	}
	
	public void persistirShapesVnParamPersistDadoGeo() throws Exception {
		try {
			String retorno[] = null;
			String geometriaVn = null;

			retorno = imovelRuralServiceFacade.persistirShapeTheGeomImportacaoImovelFinalizado(vegetacaoNativaSelecionado.getIdeImovelRural(), vegetacaoNativaSelecionado.getIdeImovelRural().toString() + "_"	+ TemaGeoseiaEnum.VEGETACAO_NATIVA.getId(), 
						vegetacaoNativaSelecionado.getVegetacaoNativa().getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid(),vegetacaoNativaSelecionado.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio());
			if (retorno != null) {
				if (retorno.length > 0) {
					if (retorno[0].equalsIgnoreCase("ERRO")) {
						throw new Exception(retorno[2] + " [" + retorno[1] + "]");
					} else {
						geometriaVn = imovelRuralServiceFacade.buscarGeometriaShapeTemporario(vegetacaoNativaSelecionado.getIdeImovelRural(), TemaGeoseiaEnum.VEGETACAO_NATIVA.getId(), null);

						if (!imovelRuralServiceFacade.validaTipoGeometriaPoligono(null, geometriaVn)) {
							throw new Exception("Geometria do shapefile carregado não é do tipo POLÍGONO.");
						}

						vegetacaoNativaSelecionado.getVegetacaoNativa().getIdeLocalizacaoGeografica().setNovosArquivosShapeImportados(true);
						vegetacaoNativaSelecionado.getVegetacaoNativa().getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
					}
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public void persistirShapesLimiteImovelRuralParamPersistDadoGeo() throws Exception {
		String retorno[] = null;
		String geometriaImovel = null;

		retorno = imovelRuralServiceFacade.persistirShapeTheGeomImportacaoImovelFinalizado(limitePropSelecionado.getIdeImovelRural(), limitePropSelecionado.getIdeImovelRural().toString() + "_"+ TemaGeoseiaEnum.LIMITE_PROPRIEDADE.getId(), 
				limitePropSelecionado.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid(), limitePropSelecionado.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio());
		if (retorno != null) {
			if (retorno.length > 0) {
				if (retorno[0].equalsIgnoreCase("ERRO")) {
					throw new Exception(retorno[2] + " [" + retorno[1] + "]");
				} else {
					geometriaImovel = imovelRuralServiceFacade.buscarGeometriaShapeTemporario(limitePropSelecionado.getIdeImovelRural(), TemaGeoseiaEnum.LIMITE_PROPRIEDADE.getId(), null);

					if (!imovelRuralServiceFacade.validaTipoGeometriaPoligono(null, geometriaImovel)) {
						throw new Exception("Geometria do shapefile carregado não é do tipo POLÍGONO.");
					}

					imovelRural.getIdeLocalizacaoGeografica().setNovosArquivosShapeImportados(true);
					imovelRural.getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
				}
			}
		}
	}
	
	public void persistirShapesAppParamPersistDadoGeo() throws Exception {
		try {
			String retorno[] = null;
			String geometriaApp = null;

			retorno = imovelRuralServiceFacade.persistirShapeTheGeomImportacaoImovelFinalizado(imovelRural.getIdeImovelRural(), imovelRural.getIdeImovelRural().toString() + "_"+ TemaGeoseiaEnum.APP.getId() + "_"+ appSelecionada.getCodigoPersistirShape().toString(), 
						appSelecionada.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid(), imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio());
			if (retorno != null) {
				if (retorno.length > 0) {
					if (retorno[0].equalsIgnoreCase("ERRO")) {
						throw new Exception(retorno[2] + " [" + retorno[1] + "]");
					} else {
						geometriaApp = imovelRuralServiceFacade.buscarGeometriaShapeTemporario(imovelRural.getIdeImovelRural(), TemaGeoseiaEnum.APP.getId(), appSelecionada.getCodigoPersistirShape().toString());

						if (!imovelRuralServiceFacade.validaTipoGeometriaPoligono(null, geometriaApp)) {
							throw new Exception("Geometria do shapefile carregado não é do tipo POLÍGONO.");
						}

						appSelecionada.getIdeLocalizacaoGeografica().setNovosArquivosShapeImportados(true);
						appSelecionada.getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
					}
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public void persistirShapesRppnParamPersistDadoGeo() throws Exception {
		try {
			String retorno[] = null;
			String geometriaRppn = null;

			retorno = imovelRuralServiceFacade.persistirShapeTheGeomImportacaoImovelFinalizado(imovelRural.getIdeImovelRural(), imovelRural.getIdeImovelRural().toString() + "_"+ TemaGeoseiaEnum.RPPN.getId(), 
					rppnSelecionado.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid(), imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio());
			if (retorno != null) {
				if (retorno.length > 0) {
					if (retorno[0].equalsIgnoreCase("ERRO")) {
						throw new Exception(retorno[2] + " [" + retorno[1] + "]");
					} else {
						geometriaRppn = imovelRuralServiceFacade.buscarGeometriaShapeTemporario(imovelRural.getIdeImovelRural(), TemaGeoseiaEnum.RPPN.getId(), null);

						if (!imovelRuralServiceFacade.validaTipoGeometriaPoligono(null, geometriaRppn)) {
							throw new Exception("Geometria do shapefile carregado não é do tipo POLÍGONO.");
						}
						rppnSelecionado.getIdeLocalizacaoGeografica().setNovosArquivosShapeImportados(true);
						rppnSelecionado.getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
					}
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	public void persistirShapesApParamPersistDadoGeo() throws Exception {
		try {
			String retorno[] = null;
			String geometriaAp = null;
			
			retorno = imovelRuralServiceFacade.persistirShapeTheGeomImportacaoImovelFinalizado(
					imovelRural.getIdeImovelRural(),
					imovelRural.getIdeImovelRural().toString() + "_"+ TemaGeoseiaEnum.ATIVIDADE_DESENVOLVIDA.getId() + "_"+ areaProdutivaSelecionada.getCodigoPersistirShape().toString(), 
					areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid(), 
					imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio());
			
			if (retorno != null) {
				if (retorno.length > 0) {
					if (retorno[0].equalsIgnoreCase("ERRO")) {
						String erro = retorno[2] + " [" + retorno[1] + "]";
						MensagemUtil.erro(erro);
						throw new Exception(erro);
					} else {
						geometriaAp = imovelRuralServiceFacade.buscarGeometriaShapeTemporario(
								imovelRural.getIdeImovelRural(), 
								TemaGeoseiaEnum.ATIVIDADE_DESENVOLVIDA.getId(),
								areaProdutivaSelecionada.getCodigoPersistirShape().toString());

						if (!imovelRuralServiceFacade.validaTipoGeometriaPoligono(null, geometriaAp)) {
							throw new Exception("Geometria do shapefile carregado não é do tipo POLÍGONO.");
						}
						areaProdutivaSelecionada.getIdeLocalizacaoGeografica().setNovosArquivosShapeImportados(true);
						areaProdutivaSelecionada.getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
					}
				}
			}
		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * Aba Limite da Propriedade
	 */
	private void abaLimitePropriedade() {
		listaImoveis = new ArrayList<ImovelRural>();
		listaShapeUpldImoveis = new ArrayList<ImovelRural>();
		if(limitePropSelecionado != null && limitePropSelecionado.getIdeLocalizacaoGeografica() != null && limitePropSelecionado.getIdeLocalizacaoGeografica().getListArquivosShape() != null && limitePropSelecionado.getIdeLocalizacaoGeografica().getListArquivosShape().size() > 0){
			listaShapeUpldImoveis.add(limitePropSelecionado);
			incluirShape = true;
		}
		try {
			if (!Util.isNullOuVazio(imovelRural)) {
				imovelRural = imovelRuralServiceFacade.carregarTudo(imovelRural.getIdeImovelRural());
			}
			controlarBotoes(false, false, false);
			listaImoveis.add(imovelRural);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
		
	/**
	 * Aba Reserva Legal
	 */
	private void abaReservaLegal() {
		try {
			listaShapeUpldImoveis = new ArrayList<ImovelRural>();
			if(reservaLegalSelecionado != null && reservaLegalSelecionado.getIdeLocalizacaoGeografica() != null && reservaLegalSelecionado.getIdeLocalizacaoGeografica().getListArquivosShape() != null && !reservaLegalSelecionado.getIdeLocalizacaoGeografica().getListArquivosShape().isEmpty()){
				listaShapeUpldImoveis.add(reservaLegalSelecionado);
				incluirShape = true;
			}
			if (!Util.isNullOuVazio(imovelRural.getReservaLegal())) {
				listaImoveis = new ArrayList<ImovelRural>();
				imovelRural.setReservaLegal(imovelRuralServiceFacade.carregarTudo(imovelRural.getReservaLegal()));
				listaImoveis.add(imovelRural);
			}
			controlarBotoes(true, false, false);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	/**
	 * Aba Vegetação Nativa
	 */
	private void abaVegetacaoNativa(){
		try {
			listaShapeUpldImoveis = new ArrayList<ImovelRural>();
			if(vegetacaoNativaSelecionado != null && vegetacaoNativaSelecionado.getIdeLocalizacaoGeografica() != null && vegetacaoNativaSelecionado.getIdeLocalizacaoGeografica().getListArquivosShape() != null && vegetacaoNativaSelecionado.getIdeLocalizacaoGeografica().getListArquivosShape().size() > 0){
				listaShapeUpldImoveis.add(vegetacaoNativaSelecionado);
				incluirShape = true;
			}
			if (!Util.isNullOuVazio(imovelRural.getVegetacaoNativa())) {
				listaImoveis = new ArrayList<ImovelRural>();
				imovelRural.setVegetacaoNativa(imovelRuralServiceFacade.carregarTudoVegetacaoNativa(imovelRural.getVegetacaoNativa()));
				listaImoveis.add(imovelRural);
			}
			controlarBotoes(true,false,false);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	/**
	 * Aba Área de Preservação Permanente
	 */
	private void abaAreaPreservacaoPermanente(){
		try {
			appCollection = new ArrayList<App>();
			List<App> apps = new ArrayList<App>();
			
			if (Util.isNullOuVazio(appSelecionada)) {
				appSelecionada = new App();
				appSelecionada.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
				appSelecionada.setIdeTipoApp(new TipoApp());
				appSelecionada.setIdeTipoEstadoConservacao(new TipoEstadoConservacao());
			}
			
			apps.addAll(imovelRuralServiceFacade.listarAppByImovelRural(imovelRural));
			
			for (App app : apps) {
				app = imovelRuralServiceFacade.carregarTudoApp(app);
				
				if (Util.isNullOuVazio(app.getCodigoPersistirShape())) {
					String codigo = Util.getStringAlfanumAleatoria(5);
					
					if (!Util.isNullOuVazio(app.getIdeLocalizacaoGeografica())) {
						codigo += "_" + app.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica().toString();
					}
					
					app.setCodigoPersistirShape(codigo);
				}
				
				appCollection.add(app);
			}
			
			controlarBotoes(true,false,false);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	/**
	 * Aba RPPN
	 */
	private void abaRPPN(){
		try {
			listaImovelRuralRppn = new ArrayList<ImovelRuralRppn>();
			imovelRuralRppn = imovelRuralServiceFacade.carregarImovelRuralRppnByIdeImovelRural(imovelRural);
			listaImovelRuralRppn.add(imovelRuralRppn);
			if(rppnSelecionado != null && rppnSelecionado.getIdeLocalizacaoGeografica() != null){
				listaImovelRuralRppnAlterados = new ArrayList<ImovelRuralRppn>();
				listaImovelRuralRppnAlterados.add(rppnSelecionado);
				incluirShapeRppn = true;
			}
			controlarBotoes(true,false,false);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
 	
	/**
	 * Aba Atividade Desenvolvida
	 */
	private void abaAtividadeDesenvolvida(){
		try {
			areaProdutivaCollection = new ArrayList<AreaProdutiva>();
			List<AreaProdutiva> aProds = new ArrayList<AreaProdutiva>();
			
			if (areaProdutivaSelecionada == null) {
				areaProdutivaSelecionada = new AreaProdutiva();
				areaProdutivaSelecionada.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
			}
			
			aProds.addAll(imovelRuralServiceFacade.listarAreaProdutivaByImovelRural(imovelRural));
			
			for (AreaProdutiva aprod : aProds) {
				aprod = imovelRuralServiceFacade.carregarTudoAreaProdutiva(aprod);
				
				if (Util.isNullOuVazio(aprod.getCodigoPersistirShape())) {
					String codigo = Util.getStringAlfanumAleatoria(5);
					
					if (!Util.isNullOuVazio(aprod.getIdeLocalizacaoGeografica())) {
						codigo += "_" + aprod.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica().toString();
					}
					
					aprod.setCodigoPersistirShape(codigo);
				}
				
				areaProdutivaCollection.add(aprod);
			}
			controlarBotoes(true,true,false);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	/**
	 * Aba Visualização
	 */
	private void abaVisualizacao(){
		controlarBotoes(true,false,true);
	}
	
	public void importarShape(FileUploadEvent event) {
		Boolean salvouShape = false;
		String caminhoArquivo = null;
		if(validarCoordenadas()){
			try {
				switch (activeIndex) {
				case 0:
						if(atualizarImovel)
							atualizarCoordenadasdoImovel();
						salvouShape = importarShapeLimitePropriedade(event,caminhoArquivo);
						atualizarImovel = false;
						break;
				case 1:
						if(atualizarImovel)
							atualizarCoordenadasdoImovel();
						salvouShape = importarShapeReservaLegal(event,caminhoArquivo);
						atualizarImovel = false;
						break;
				case 2:
						if(atualizarImovel)
							atualizarCoordenadasdoImovel();
						salvouShape = importarShapeVegetacaoNativa(event,caminhoArquivo);
						atualizarImovel = false;
						break;
				case 3:
						if(atualizarApp)
							atualizarCoordenadasApp();
						salvouShape = importarShapeAPP(event,caminhoArquivo);
						break;
				case 4:
						if(atualizarRppn)
							atualizarCoordenadasRppn();
						salvouShape = importarShapeRPPN(event,caminhoArquivo);
						atualizarRppn = false;
						break;
				case 5:
						if(atualizarAd) atualizarCoordenadasAtividadesDesenvolvidas();
						salvouShape = importarShapeAtividadeDesenvolvida(event, caminhoArquivo);
						atualizarAd = false;
						break;
				default:
					JsfUtil.addWarnMessage("Erro de importação, contate o administrador do sistema!");
				}
				
				if (salvouShape!=null && salvouShape){
					listaShapeUpldImoveis = new ArrayList<ImovelRural>();
					if(listaAppsAlterados == null || listaAppsAlterados.isEmpty()){
						listaAppsAlterados = new ArrayList<App>();
					}
					if(listaAreasProdutivasAlterados == null || listaAreasProdutivasAlterados.isEmpty()){
						listaAreasProdutivasAlterados = new ArrayList<AreaProdutiva>();
					}
					
					if(listaImovelRuralRppnAlterados == null || listaImovelRuralRppnAlterados.isEmpty()){
						listaImovelRuralRppnAlterados = new ArrayList<ImovelRuralRppn>();
					}
					
					try {
						if(activeIndex == 3){
							App app = guardarShapeAppAtualizado();
							if(app != null && app.getIdeApp() != null){
								listaAppsAlterados.add(app.clone());
								persistirShapesAppParamPersistDadoGeo();
								appSelecionada = new App();
								atualizarApp = true;
							}else{
								atualizarApp = false;
								if(listaAppsAlterados == null || listaAppsAlterados.isEmpty()){
									listaAppsAlterados = new ArrayList<App>();
									appSelecionada.getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
								}
							}
							
						}else if(activeIndex == 5){
							AreaProdutiva aP = guardarShapeAreasProdutivasAtualizado();
							
							atualizarAd = true;
							
							if(aP != null && aP.getIdeAreaProdutiva() != null){
								try {
									persistirShapesApParamPersistDadoGeo();
									
								} catch (Exception e) {
									areaProdutivaSelecionada = new AreaProdutiva();
									Html.esconder("dlgUploadShapeAjustadoCefir");
									FileUploadUtil.removerArquivos(new File(excluirPasta()));
									return;
								}
								
								aP = guardarShapeAreasProdutivasAtualizado();
								listaAreasProdutivasAlterados.add(aP.clone());
								areaProdutivaSelecionada = new AreaProdutiva();
								
							} else if(listaAreasProdutivasAlterados == null || listaAreasProdutivasAlterados.isEmpty()){
								listaAreasProdutivasAlterados = new ArrayList<AreaProdutiva>();
								areaProdutivaSelecionada.getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
							}
						}else if(activeIndex == 4){
							ImovelRuralRppn rppn = guardarShapeRppnAtualizado();
							
							if(rppn != null && rppn.getIdeImovelRuralRppn() != null){
								listaImovelRuralRppnAlterados.add(rppn);
								persistirShapesRppnParamPersistDadoGeo();
								incluirShapeRppn= true;
							}else{
								incluirShapeRppn = false;
								listaImovelRuralRppnAlterados = new ArrayList<ImovelRuralRppn>();
								rppnSelecionado.getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
								
							}
							
						}else{
							ImovelRural iR = guardarShapeAtualizado();
							
							if(iR != null && iR.getIdeImovelRural() != null){
								if(activeIndex == 0){
									persistirShapesLimiteImovelRuralParamPersistDadoGeo();
								}else if(activeIndex == 1){
									persistirShapesRlParamPersistDadoGeo();
								}else if(activeIndex == 2){
									persistirShapesVnParamPersistDadoGeo();
								}
								listaShapeUpldImoveis.add(iR);
								incluirShape = true;
								atualizarImovel = false;
								
							}else{
								atualizarImovel = true;
								incluirShape = false;
								FileUploadUtil.removerArquivos(new File(excluirPasta()));
								listaShapeUpldImoveis = new ArrayList<ImovelRural>();
								if(activeIndex == 0){
									limitePropSelecionado.getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
									limitePropSelecionado.setIdeLocalizacaoGeografica(null);
								}else if(activeIndex == 1){
									reservaLegalSelecionado.getReservaLegal().getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
									reservaLegalSelecionado.setIdeLocalizacaoGeografica(null);
									
								}else if(activeIndex == 2){
									vegetacaoNativaSelecionado.getVegetacaoNativa().getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
									vegetacaoNativaSelecionado.setIdeLocalizacaoGeografica(null);
								}
							}
						}
			            RequestContext.getCurrentInstance().execute("dlgUploadShapeAjustadoCefir.hide()");
			        	if(!listaShapeUpldImoveis.isEmpty())
			        		JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");										
		        	
					} catch (Exception e) {
						Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					}
		    	}else if(salvouShape == null){
		                JsfUtil.addWarnMessage("Não é possível enviar mais de 3 arquivos.");
		    	}
			
			} catch (Exception e1) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e1);
			}

		}
		
	}

	private void atualizarCoordenadasdoImovel() throws Exception {
		switch (activeIndex) {
		case 0:
			if(limitePropSelecionado.getIdeLocalizacaoGeografica().getListArquivosShape() == null || limitePropSelecionado.getIdeLocalizacaoGeografica().getListArquivosShape().isEmpty()){
				limitePropSelecionado.setIdeLocalizacaoGeografica(imovelRuralServiceFacade.carregarLocalizacaoGeografica(localizacaoSelecionada.getIdeLocalizacaoGeografica()));
				limitePropSelecionado.getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(imovelRuralServiceFacade.carregarClassificacaoSecaoGeometricaPorId(secaoGeoSeleccionada.getIdeClassificacaoSecao()));
				limitePropSelecionado.getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(sistemaCoordSelecionado.getIdeSistemaCoordenada()));
			}
			break;
		case 1:
			if(reservaLegalSelecionado.getReservaLegal().getIdeLocalizacaoGeografica().getListArquivosShape() == null || reservaLegalSelecionado.getReservaLegal().getIdeLocalizacaoGeografica().getListArquivosShape().isEmpty()){
				reservaLegalSelecionado.getReservaLegal().setIdeLocalizacaoGeografica(imovelRuralServiceFacade.carregarLocalizacaoGeografica(localizacaoSelecionada.getIdeLocalizacaoGeografica()));
				reservaLegalSelecionado.getReservaLegal().getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(imovelRuralServiceFacade.carregarClassificacaoSecaoGeometricaPorId(secaoGeoSeleccionada.getIdeClassificacaoSecao()));
				reservaLegalSelecionado.getReservaLegal().getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(sistemaCoordSelecionado.getIdeSistemaCoordenada()));
			}
			break;
		case 2:
			if(vegetacaoNativaSelecionado.getVegetacaoNativa().getIdeLocalizacaoGeografica().getListArquivosShape() == null || vegetacaoNativaSelecionado.getVegetacaoNativa().getIdeLocalizacaoGeografica().getListArquivosShape().isEmpty()){
				vegetacaoNativaSelecionado.getVegetacaoNativa().setIdeLocalizacaoGeografica(imovelRuralServiceFacade.carregarLocalizacaoGeografica(localizacaoSelecionada.getIdeLocalizacaoGeografica()));
				vegetacaoNativaSelecionado.getVegetacaoNativa().getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(imovelRuralServiceFacade.carregarClassificacaoSecaoGeometricaPorId(secaoGeoSeleccionada.getIdeClassificacaoSecao()));
				vegetacaoNativaSelecionado.getVegetacaoNativa().getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(sistemaCoordSelecionado.getIdeSistemaCoordenada()));
			}
			break;
		default:
		}
	}

	/**
	 * Guardar os dados do shapes para salvar depois
	 */
	private ImovelRural guardarShapeAtualizado() throws Exception{
		LocalizacaoGeografica local = new LocalizacaoGeografica();
		limparValArea();
		
		switch (activeIndex) {
		case 0:
			local = imovelRuralServiceFacade.carregarLocalizacaoGeografica(localizacaoSelecionada.getIdeLocalizacaoGeografica());
			local.setListArquivosShape(new ArrayList<String>());
			local.getListArquivosShape().addAll(limitePropSelecionado.getIdeLocalizacaoGeografica().getListArquivosShape());
			
			limitePropSelecionado.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
			limitePropSelecionado.setIdeLocalizacaoGeografica(local);
			limitePropSelecionado.getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(sistemaCoordSelecionado.getIdeSistemaCoordenada()));
			
			valAreaLim = buscarAreaShapeTemporario(TemaGeoseiaEnum.LIMITE_PROPRIEDADE.getId(),null);
			
			if(!validarDezPorcentoAreaDeclarada(getBuscarAreaShape(imovelRural.getIdeLocalizacaoGeografica()),valAreaLim)){
				return null;
			}
			
			limitePropSelecionado.setValArea(valAreaLim);
			
			limitePropAlterado = limitePropSelecionado.clone();
			limitePropAlterado.setIdeLocalizacaoGeografica(limitePropSelecionado.getIdeLocalizacaoGeografica().clone());
			limitePropAlterado.getIdeLocalizacaoGeografica().setListArquivosShape(Util.clonaListStrings(limitePropSelecionado.getIdeLocalizacaoGeografica().getListArquivosShape()));
			return limitePropSelecionado;
			
		case 1:
			local = imovelRuralServiceFacade.carregarLocalizacaoGeografica(localizacaoSelecionada.getIdeLocalizacaoGeografica());
			local.setListArquivosShape(new ArrayList<String>());
			local.getListArquivosShape().addAll(reservaLegalSelecionado.getReservaLegal().getIdeLocalizacaoGeografica().getListArquivosShape());
			
			reservaLegalSelecionado.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
			reservaLegalSelecionado.setIdeLocalizacaoGeografica(local);
			reservaLegalSelecionado.getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(imovelRuralServiceFacade.carregarClassificacaoSecaoGeometricaPorId(secaoGeoSeleccionada.getIdeClassificacaoSecao()));
			reservaLegalSelecionado.getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(sistemaCoordSelecionado.getIdeSistemaCoordenada()));
			
			valAreaRL = buscarAreaShapeTemporario(TemaGeoseiaEnum.RESERVA_LEGAL.getId(),null);
			
			if(!validarReservaDezPorcentoAreaDeclarada(getBuscarAreaShape(reservaLegalSelecionado.getReservaLegal().getIdeLocalizacaoGeografica()), valAreaRL)){
				return null;
			}
			
			reservaLegalSelecionado.setValArea(valAreaRL);
			
			reservaLegalAlterado = reservaLegalSelecionado.clone();
			reservaLegalAlterado.setReservaLegal(reservaLegalSelecionado.getReservaLegal().clone());
			reservaLegalAlterado.getReservaLegal().setIdeLocalizacaoGeografica(reservaLegalSelecionado.getReservaLegal().getIdeLocalizacaoGeografica().clone());
			reservaLegalAlterado.getReservaLegal().getIdeLocalizacaoGeografica().setListArquivosShape(Util.clonaListStrings(reservaLegalSelecionado.getReservaLegal().getIdeLocalizacaoGeografica().getListArquivosShape()));
			return reservaLegalSelecionado;
			
		case 2:
			local = imovelRuralServiceFacade.carregarLocalizacaoGeografica(localizacaoSelecionada.getIdeLocalizacaoGeografica());
			local.setListArquivosShape(new ArrayList<String>());
			local.getListArquivosShape().addAll(vegetacaoNativaSelecionado.getVegetacaoNativa().getIdeLocalizacaoGeografica().getListArquivosShape());
			
			vegetacaoNativaSelecionado.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
			vegetacaoNativaSelecionado.setIdeLocalizacaoGeografica(local);
			vegetacaoNativaSelecionado.getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(imovelRuralServiceFacade.carregarClassificacaoSecaoGeometricaPorId(secaoGeoSeleccionada.getIdeClassificacaoSecao()));
			vegetacaoNativaSelecionado.getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(sistemaCoordSelecionado.getIdeSistemaCoordenada()));
			
			valAreaVN = buscarAreaShapeTemporario(TemaGeoseiaEnum.VEGETACAO_NATIVA.getId(),null);
			
			if(!validarDezPorcentoAreaDeclarada(getBuscarAreaShape(imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica()),valAreaVN)){
				return null;
			}
			
			vegetacaoNativaSelecionado.setValArea(valAreaVN);
			
			vegetacaoNativaAlterado = vegetacaoNativaSelecionado.clone();
			vegetacaoNativaAlterado.setVegetacaoNativa(vegetacaoNativaSelecionado.getVegetacaoNativa().clone());
			vegetacaoNativaAlterado.getVegetacaoNativa().setIdeLocalizacaoGeografica(vegetacaoNativaSelecionado.getVegetacaoNativa().getIdeLocalizacaoGeografica().clone());
			vegetacaoNativaAlterado.getVegetacaoNativa().getIdeLocalizacaoGeografica().setListArquivosShape(Util.clonaListStrings(vegetacaoNativaSelecionado.getVegetacaoNativa().getIdeLocalizacaoGeografica().getListArquivosShape()));
			return vegetacaoNativaSelecionado;
			
		default:
			return null;
		}
	}
	
	private App guardarShapeAppAtualizado() throws Exception{
		limparValArea();
		switch (activeIndex) {
		case 3:
			App appTemp = appSelecionada.clone();
			appTemp.setIdeLocalizacaoGeografica(appSelecionada.getIdeLocalizacaoGeografica().clone());
			appTemp.getIdeLocalizacaoGeografica().setListArquivosShape(Util.clonaListStrings(appSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape()));
			
			valAreaAPP = buscarAreaShapeTemporario(TemaGeoseiaEnum.APP.getId(), appSelecionada.getCodigoPersistirShape());
			
			if(!validarDezPorcentoAreaDeclarada(getBuscarAreaShape(appTemp.getIdeLocalizacaoGeografica()),valAreaAPP)){
				return null;
			}
			
			appTemp.setValArea(valAreaAPP);
			
			return appTemp.clone();
		default:
			return null;
		}
	}

	private ImovelRuralRppn guardarShapeRppnAtualizado() throws Exception{
		limparValArea();
		switch (activeIndex) {
		case 4:
			ImovelRuralRppn rppnTemp = rppnSelecionado.clone();
			rppnTemp.setIdeLocalizacaoGeografica(rppnSelecionado.getIdeLocalizacaoGeografica().clone());
			rppnTemp.getIdeLocalizacaoGeografica().setListArquivosShape(Util.clonaListStrings(rppnSelecionado.getIdeLocalizacaoGeografica().getListArquivosShape()));
			
			valAreaRPPN = buscarAreaShapeTemporario(TemaGeoseiaEnum.RPPN.getId(), null);
			
			if(!validarDezPorcentoAreaDeclarada(getBuscarAreaShape(rppnTemp.getIdeLocalizacaoGeografica()),valAreaRPPN)){
				return null;
			}
			
			rppnSelecionado.setValArea(valAreaRPPN);
			
			setRppnAlterado(rppnSelecionado);
			
			return rppnSelecionado;
		default:
			return null;
		}
	}
	
	private AreaProdutiva guardarShapeAreasProdutivasAtualizado() throws Exception{
		limparValArea();
		switch (activeIndex) {
		case 5:
			AreaProdutiva areaProdutivaTemp = areaProdutivaSelecionada.clone();
			areaProdutivaTemp.setIdeLocalizacaoGeografica(areaProdutivaSelecionada.getIdeLocalizacaoGeografica().clone());
			areaProdutivaTemp.getIdeLocalizacaoGeografica().setListArquivosShape(Util.clonaListStrings(areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape()));
			
			valAreaAtvDesen = buscarAreaShapeTemporario(TemaGeoseiaEnum.ATIVIDADE_DESENVOLVIDA.getId(),areaProdutivaSelecionada.getCodigoPersistirShape());
			
			if(!validarDezPorcentoAreaDeclarada(getBuscarAreaShape(areaProdutivaTemp.getIdeLocalizacaoGeografica()),valAreaAtvDesen)){
				return null;
			}
			
			areaProdutivaSelecionada.setValArea(valAreaAtvDesen);
			
			return areaProdutivaSelecionada.clone();
		default:
			return null;
		}
	}
	
	private void atualizarCoordenadasApp() throws Exception {
		appSelecionada.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		appSelecionada.setIdeLocalizacaoGeografica(imovelRuralServiceFacade.carregarLocalizacaoGeografica(localizacaoSelecionada.getIdeLocalizacaoGeografica()));
		appSelecionada.getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(imovelRuralServiceFacade.carregarClassificacaoSecaoGeometricaPorId(secaoGeoSeleccionada.getIdeClassificacaoSecao()));
		appSelecionada.getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(sistemaCoordSelecionado.getIdeSistemaCoordenada()));
		atualizarApp = false;
	}

	private void atualizarCoordenadasAtividadesDesenvolvidas() throws Exception {
		areaProdutivaSelecionada.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		areaProdutivaSelecionada.setIdeLocalizacaoGeografica(imovelRuralServiceFacade.carregarLocalizacaoGeografica(localizacaoSelecionada.getIdeLocalizacaoGeografica()));
		areaProdutivaSelecionada.getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(imovelRuralServiceFacade.carregarClassificacaoSecaoGeometricaPorId(secaoGeoSeleccionada.getIdeClassificacaoSecao()));
		areaProdutivaSelecionada.getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(sistemaCoordSelecionado.getIdeSistemaCoordenada()));
	}

	private void atualizarCoordenadasRppn() throws Exception {
		rppnSelecionado.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		rppnSelecionado.setIdeLocalizacaoGeografica(imovelRuralServiceFacade.carregarLocalizacaoGeografica(localizacaoSelecionada.getIdeLocalizacaoGeografica()));
		rppnSelecionado.getIdeLocalizacaoGeografica().setIdeClassificacaoSecao(imovelRuralServiceFacade.carregarClassificacaoSecaoGeometricaPorId(secaoGeoSeleccionada.getIdeClassificacaoSecao()));
		rppnSelecionado.getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(imovelRuralServiceFacade.carregarDatum(sistemaCoordSelecionado.getIdeSistemaCoordenada()));
	}
	
	private Boolean importarShapeLimitePropriedade(FileUploadEvent event,String caminhoArquivo) {
		boolean retorno = false;

		if (Util.isNullOuVazio(limitePropSelecionado.getIdeLocalizacaoGeografica().getListArquivosShape())){
			limitePropSelecionado.getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
			//Excluir pasta do tem para eliminar lixo 
			FileUploadUtil.removerArquivos(new File(excluirPasta()));
		}
		
        if (limitePropSelecionado.getIdeLocalizacaoGeografica().getListArquivosShape().size() < 3) {
			if ((!Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada()) && Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid()))
					|| Util.isNullOuVazio(imovelRural.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada())) {
				limitePropSelecionado.getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(selecionarSistemaCoordenada(imovelRural.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada()));
			}

			caminhoArquivo = FileUploadUtil.EnviarShape(event, DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.LIMITE_PROPRIEDADE.getId(),
					imovelRural.getIdeImovelRural().toString() + "_" + limitePropSelecionado.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid());

			if (limitePropSelecionado.getIdeLocalizacaoGeografica().getListArquivosShape().contains(FileUploadUtil.getFileName(caminhoArquivo))){
				FileUploadUtil.removerArquivos(new File(excluirPasta()));
				JsfUtil.addWarnMessage("Não é possível enviar mais de um arquivo com a mesma extensão.");
			}
			else {
				limitePropSelecionado.getIdeLocalizacaoGeografica().getListArquivosShape().add(FileUploadUtil.getFileName(caminhoArquivo));
				if (limitePropSelecionado.getIdeLocalizacaoGeografica().getListArquivosShape().size() == 3)
					retorno = true;
			}
		}else{
			return null;
		}
		return retorno;
	}
	
	private Boolean importarShapeReservaLegal(FileUploadEvent event, String caminhoArquivo) {
		boolean retorno = false;
		
		if(Util.isNullOuVazio(reservaLegalSelecionado.getReservaLegal().getIdeLocalizacaoGeografica().getListArquivosShape())){
			reservaLegalSelecionado.getReservaLegal().getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
			FileUploadUtil.removerArquivos(new File(excluirPasta()));
		}
		if(reservaLegalSelecionado.getReservaLegal().getIdeLocalizacaoGeografica().getListArquivosShape().size() < 3){
			if((!Util.isNullOuVazio(reservaLegalSelecionado.getReservaLegal().getIdeLocalizacaoGeografica().getIdeSistemaCoordenada()) && Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid())) || Util.isNullOuVazio(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeSistemaCoordenada())){
				reservaLegalSelecionado.getReservaLegal().getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(selecionarSistemaCoordenada(imovelRural.getReservaLegal().getIdeLocalizacaoGeografica().getIdeSistemaCoordenada()));
			}

			caminhoArquivo = FileUploadUtil.EnviarShape(event, DiretorioArquivoEnum.SHAPEFILESTEMP.toString()+imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.RESERVA_LEGAL.getId(),
				imovelRural.getIdeImovelRural().toString()+"_"+ reservaLegalSelecionado.getReservaLegal().getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid());

			if(reservaLegalSelecionado.getReservaLegal().getIdeLocalizacaoGeografica().getListArquivosShape().contains(FileUploadUtil.getFileName(caminhoArquivo))){
				FileUploadUtil.removerArquivos(new File(excluirPasta()));
				JsfUtil.addWarnMessage("Não é possível enviar mais de um arquivo com a mesma extensão.");
			}
			else{
				reservaLegalSelecionado.getReservaLegal().getIdeLocalizacaoGeografica().getListArquivosShape().add(FileUploadUtil.getFileName(caminhoArquivo));
				if(reservaLegalSelecionado.getReservaLegal().getIdeLocalizacaoGeografica().getListArquivosShape().size() == 3)
					retorno = true;
			}
		} else{
			return null;
		}
		return retorno;
	}
	
	private Boolean importarShapeVegetacaoNativa(FileUploadEvent event, String caminhoArquivo) {
		boolean retorno = false;
		
		if (Util.isNullOuVazio(vegetacaoNativaSelecionado.getVegetacaoNativa().getIdeLocalizacaoGeografica().getListArquivosShape())){
			vegetacaoNativaSelecionado.getVegetacaoNativa().getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
			FileUploadUtil.removerArquivos(new File(excluirPasta()));
		}
		if (vegetacaoNativaSelecionado.getVegetacaoNativa().getIdeLocalizacaoGeografica().getListArquivosShape().size() < 3) {
			if ((!Util.isNullOuVazio(vegetacaoNativaSelecionado.getVegetacaoNativa().getIdeLocalizacaoGeografica().getIdeSistemaCoordenada()) && Util.isNullOuVazio(imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid())) || Util.isNullOuVazio(imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getIdeSistemaCoordenada())) {
				vegetacaoNativaSelecionado.getVegetacaoNativa().getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(selecionarSistemaCoordenada(imovelRural.getVegetacaoNativa().getIdeLocalizacaoGeografica().getIdeSistemaCoordenada()));
			}
			caminhoArquivo = FileUploadUtil.EnviarShape(event, DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.VEGETACAO_NATIVA.getId(),
					imovelRural.getIdeImovelRural().toString() + "_" + vegetacaoNativaSelecionado.getVegetacaoNativa().getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid());

			if (vegetacaoNativaSelecionado.getVegetacaoNativa().getIdeLocalizacaoGeografica().getListArquivosShape().contains(FileUploadUtil.getFileName(caminhoArquivo))){
				FileUploadUtil.removerArquivos(new File(excluirPasta()));
				JsfUtil.addWarnMessage("Não é possível enviar mais de um arquivo com a mesma extensão.");
			}
			else {
				vegetacaoNativaSelecionado.getVegetacaoNativa().getIdeLocalizacaoGeografica().getListArquivosShape().add(FileUploadUtil.getFileName(caminhoArquivo));
				if (vegetacaoNativaSelecionado.getVegetacaoNativa().getIdeLocalizacaoGeografica().getListArquivosShape().size() == 3)
					retorno = true;
			}
		}else{
			return null;
		}
	
		return retorno;
	}

	private Boolean importarShapeAPP(FileUploadEvent event, String caminhoArquivo) {
		boolean retorno = false;
		
		if (Util.isNullOuVazio(appSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape())){
			appSelecionada.getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
			//Excluir pasta do tem para eliminar lixo 
			FileUploadUtil.removerArquivos(new File(excluirPasta()));
		}
		
		if (appSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape().size() < 3) {
			
			if ((!Util.isNullOuVazio(appSelecionada.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada()) && Util.isNullOuVazio(appSelecionada.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid()))
					|| Util.isNullOuVazio(appSelecionada.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada())) {
				appSelecionada.getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(selecionarSistemaCoordenada(appSelecionada.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada()));
			}
			caminhoArquivo = FileUploadUtil.EnviarShape(event, DiretorioArquivoEnum.SHAPEFILESTEMP.toString() 
					+ imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.APP.getId() + "_"
							+ appSelecionada.getCodigoPersistirShape().toString(), imovelRural.getIdeImovelRural().toString()
							+ "_" + appSelecionada.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid());
			
			if (appSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape().contains(FileUploadUtil.getFileName(caminhoArquivo))){
				FileUploadUtil.removerArquivos(new File(excluirPasta()));
				JsfUtil.addWarnMessage("Não é possível enviar mais de um arquivo com a mesma extensão.");
			}
			else {
				appSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape().add(FileUploadUtil.getFileName(caminhoArquivo));
				if (appSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape().size() == 3){
					retorno = true;
					appSelecionada.getIdeLocalizacaoGeografica().setNovosArquivosShapeImportados(true);
				}
			}
		} else{
			return null;
		}
	return retorno;
	}
	
	private Boolean importarShapeRPPN(FileUploadEvent event, String caminhoArquivo) {
		boolean retorno = false;

		if (Util.isNullOuVazio(rppnSelecionado.getIdeLocalizacaoGeografica().getListArquivosShape())){
			rppnSelecionado.getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
			//Excluir pasta do tem para eliminar lixo 
			FileUploadUtil.removerArquivos(new File(excluirPasta()));
		}
		
		if (rppnSelecionado.getIdeLocalizacaoGeografica().getListArquivosShape().size() < 3) {
			if ((!Util.isNullOuVazio(rppnSelecionado.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada()) && Util.isNullOuVazio(rppnSelecionado.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid())) || Util.isNullOuVazio(rppnSelecionado.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada())) {
				rppnSelecionado.getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(selecionarSistemaCoordenada(rppnSelecionado.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada()));
			}

			caminhoArquivo = FileUploadUtil.EnviarShape(event, DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.RPPN.getId(), imovelRural.getIdeImovelRural().toString() + "_" + rppnSelecionado.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid());

			if (rppnSelecionado.getIdeLocalizacaoGeografica().getListArquivosShape().contains(FileUploadUtil.getFileName(caminhoArquivo))){
				FileUploadUtil.removerArquivos(new File(excluirPasta()));
				JsfUtil.addWarnMessage("Não é possível enviar mais de um arquivo com a mesma extensão.");
			}
			else {
				rppnSelecionado.getIdeLocalizacaoGeografica().getListArquivosShape().add(FileUploadUtil.getFileName(caminhoArquivo));
				if (rppnSelecionado.getIdeLocalizacaoGeografica().getListArquivosShape().size() == 3)
				retorno = true;
			}
		}else{
			return null;
		}
		return retorno;
	}
	
	private Boolean importarShapeAtividadeDesenvolvida(FileUploadEvent event, String caminhoArquivo) {
		Boolean retorno = false;

		if (Util.isNullOuVazio(areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape())){
			areaProdutivaSelecionada.getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
			//Excluir pasta do tem para eliminar lixo 
			FileUploadUtil.removerArquivos(new File(excluirPasta()));
		}
		if (areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape().size() < 3) {
			
			if ((!Util.isNullOuVazio(areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada()) 
					&& Util.isNullOuVazio(areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid()))
					|| Util.isNullOuVazio(areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada())) {
				
				areaProdutivaSelecionada.getIdeLocalizacaoGeografica().setIdeSistemaCoordenada(
						selecionarSistemaCoordenada(areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada()));
			}

			caminhoArquivo = FileUploadUtil.EnviarShape(event, DiretorioArquivoEnum.SHAPEFILESTEMP.toString()
					+ imovelRural.getIdeImovelRural().toString() + "_" + TemaGeoseiaEnum.ATIVIDADE_DESENVOLVIDA.getId()
					+ "_" + areaProdutivaSelecionada.getCodigoPersistirShape().toString(), imovelRural.getIdeImovelRural().toString()
					+ "_" + areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getIdeSistemaCoordenada().getSrid());

			if (areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape().contains(FileUploadUtil.getFileName(caminhoArquivo))){
				FileUploadUtil.removerArquivos(new File(excluirPasta()));
				JsfUtil.addWarnMessage("Não é possível enviar mais de um arquivo com a mesma extensão.");
			}
			else {
				areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape().add(FileUploadUtil.getFileName(caminhoArquivo));
				if (areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getListArquivosShape().size() == 3){
					retorno = true;
					areaProdutivaSelecionada.getIdeLocalizacaoGeografica().setNovosArquivosShapeImportados(true);
				}
			}
		} else{
			return null;
		}
		return retorno;
	}
	
	public SistemaCoordenada selecionarSistemaCoordenada(SistemaCoordenada sistemaCoordenada) {
		for (SistemaCoordenada sistema : listaDatums) {
			if (sistemaCoordenada != null && sistemaCoordenada.getIdeSistemaCoordenada().equals(sistema.getIdeSistemaCoordenada()))
				return sistema;
		}
		return new SistemaCoordenada();
	}
	
	public void carregarSecaoGeometrica() {
		try {
			listaSecaoGeometrica = imovelRuralServiceFacade.listarClassificacaoSecaoGeometrica();
			// removendo a opção de ponto
			listaSecaoGeometrica.remove(0);
			// removendo a opção de desenho
			listaSecaoGeometrica.remove(1);
			//seta Shape como principal
			secaoGeoSeleccionada = listaSecaoGeometrica.get(0);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void carregarDatums() {
		try {
			sistemaCoordSelecionado = null;
			listaDatums = (List<SistemaCoordenada>) imovelRuralServiceFacade.listarDatum();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void atualizarShapes(){
		carregarSecaoGeometrica();
		carregarDatums();
		carregarListaGeo();
		Html.atualizar("formUploadShapeAjustadoCefir");
		Html.exibir("dlgUploadShapeAjustadoCefir");
	}
	
	public void setarImovelSelcionado(ImovelRural obj){
		try {
			if(activeIndex == 0){
				limitePropSelecionado = obj.clone();
			}
			
			if(activeIndex == 1){
				reservaLegalSelecionado = obj.clone();
				reservaLegalSelecionado.setReservaLegal(obj.getReservaLegal().clone());
			}
			
			if(activeIndex == 2){
				vegetacaoNativaSelecionado = obj.clone();
				vegetacaoNativaSelecionado.setVegetacaoNativa(obj.getVegetacaoNativa().clone());
			}
		} catch (CloneNotSupportedException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void setarAppSelcionado(App obj){
		try {
			appSelecionada = obj.clone();
		} catch (CloneNotSupportedException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void setarRppnSelecionado(ImovelRuralRppn obj){
		try {
			rppnSelecionado = obj.clone();
		} catch (CloneNotSupportedException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void setarAtividadeDesenvolvidaSelecionado(AreaProdutiva obj){
		try {
			areaProdutivaSelecionada = obj.clone();
		} catch (CloneNotSupportedException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	private void carregarListaGeo(){
		ideLocalizacaoGeograficaSelect = null;
		listaLocalizacaoGeo = new ArrayList<String>();
		
		switch (activeIndex) {
		case 0:
				listaLocalizacaoGeo.add(limitePropSelecionado.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica().toString());
				break;
		case 1:
				listaLocalizacaoGeo.add(reservaLegalSelecionado.getReservaLegal().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica().toString());
				break;
		case 2:
				listaLocalizacaoGeo.add(vegetacaoNativaSelecionado.getVegetacaoNativa().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica().toString());
				break;
		case 3:
				listaLocalizacaoGeo.add(appSelecionada.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica().toString());
				break;
		case 4:
				listaLocalizacaoGeo.add(rppnSelecionado.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica().toString());
				break;
		case 5:
				listaLocalizacaoGeo.add(areaProdutivaSelecionada.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica().toString());
				break;
		default:
				break;
		}
		setIdeLocalizacaoGeograficaSelect(listaLocalizacaoGeo.get(0));
	}

	private boolean validarObservacoes(){
		boolean retorno = true;
		if(Util.isNullOuVazio(observacaoLP) && !tab0){
			JsfUtil.addWarnMessage("A Observação da aba Limite da Propriedade " + Util.getString("messagem_003"));
			retorno = false;
		}else if(Util.isNullOuVazio(observacaoRL)&& !tab1){
			JsfUtil.addWarnMessage("A Observação da aba Reserva Legal " + Util.getString("messagem_003"));
			retorno = false;
		}else if(Util.isNullOuVazio(observacaoVN)&& !tab2){
			JsfUtil.addWarnMessage("A Observação da aba Vegetação Nativa " + Util.getString("messagem_003"));
			retorno = false;
		}else if(Util.isNullOuVazio(observacaoAPP)&& !tab3){
			JsfUtil.addWarnMessage("A Observação da aba Área de Preservação Permanente " + Util.getString("messagem_003"));
			retorno = false;
		}else if(Util.isNullOuVazio(observacaoRPPN)&& !tab4){
			JsfUtil.addWarnMessage("A Observação da aba RPPN " + Util.getString("messagem_003"));
			retorno = false;
		}else if(Util.isNullOuVazio(observacaoAD)&& !tab5){
			JsfUtil.addWarnMessage("A Observação da aba Atividade Desenvolvida " + Util.getString("messagem_003"));
			retorno = false;
		} 
		return retorno;
	}
	
	private boolean validarCoordenadas(){
		if(this.ideLocalizacaoGeograficaSelect.isEmpty() || this.ideLocalizacaoGeograficaSelect.length() == 0){
			JsfUtil.addErrorMessage("O campo Identificador é de preenchimento obrigatório.");
			return false;
		}else if(this.secaoGeoSeleccionada == null || this.secaoGeoSeleccionada.getIdeClassificacaoSecao() == null){
			JsfUtil.addErrorMessage("O campo Tipo é de preenchimento obrigatório.");
			return false;
		}else if(this.sistemaCoordSelecionado == null || this.sistemaCoordSelecionado.getIdeSistemaCoordenada() == null){
			JsfUtil.addErrorMessage("O campo Sistema de Coordenadas é de preenchimento obrigatório.");
			return false;
		}
		return true;
	}
	
	/**
	 * Guarda as observações digitadas
	 */
	private void guardarObservacaoAba(){
		switch (activeIndexAnterior) {
		case 0:
				if(!Util.isNullOuVazio(observacoes) && Util.isNullOuVazio(observacaoLP)){
					setObservacaoLP(null);
					setObservacaoLP(observacoes);
					setObservacoes(null);
				}
				break;
		case 1:
				if(!Util.isNullOuVazio(observacoes) && Util.isNullOuVazio(observacaoRL)){
					setObservacaoRL(null);
					setObservacaoRL(observacoes);
					setObservacoes(null);
				}
				break;
		case 2:
				if(!Util.isNullOuVazio(observacoes) && Util.isNullOuVazio(observacaoVN)){
					setObservacaoVN(null);
					setObservacaoVN(observacoes);
					setObservacoes(null);
				}
				break;
		case 3:
			if(!Util.isNullOuVazio(observacoes) && Util.isNullOuVazio(observacaoAPP)){
				setObservacaoAPP(null);
				setObservacaoAPP(observacoes);
				setObservacoes(null);
			}
			break;
		case 4:
			if(!Util.isNullOuVazio(observacoes) && Util.isNullOuVazio(observacaoRPPN)){
				setObservacaoRPPN(null);
				setObservacaoRPPN(observacoes);
				setObservacoes(null);
			}
			break;
		case 5:
			if(!Util.isNullOuVazio(observacoes) && Util.isNullOuVazio(observacaoAD)){
				setObservacaoAD(null);
				setObservacaoAD(observacoes);
				setObservacoes(null);
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * Carregar ultima observações preenchidas
	 */
	private void atualizarObservacaoAba(){
		setObservacoes(null);
		switch (activeIndex) {
		case 0:
				if(!Util.isNullOuVazio(observacaoLP))
					setObservacoes(observacaoLP);
				break;
		case 1:
				if(!Util.isNullOuVazio(observacaoRL))
					setObservacoes(observacaoRL);
				break;
		case 2:
				if(!Util.isNullOuVazio(observacaoVN))
					setObservacoes(observacaoVN);
				break;
		case 3:
				if(!Util.isNullOuVazio(observacaoAPP))
					setObservacoes(observacaoAPP);
				break;
		case 4:
				if(!Util.isNullOuVazio(observacaoRPPN))
					setObservacoes(observacaoRPPN);
				break;											
		case 5:
				if(!Util.isNullOuVazio(observacaoAD))
					setObservacoes(observacaoAD);
				break;
		default:
			break;
		}
	}
	
	public double buscarAreaShapeTemporario(Integer tipo, String identificador){
		geometriaLoc = null;
		try {
			geometriaLoc = imovelRuralServiceFacade.buscarGeometriaShapeTemporario(imovelRural.getIdeImovelRural(),tipo , identificador);
			return imovelRuralServiceFacade.getAreaShapeTemporario(geometriaLoc);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return 0.0;
	}
	
	public double getBuscarAreaShape(LocalizacaoGeografica localizacaoGeografica){
		geometriaLoc = null;
		try {
			if(localizacaoGeografica != null && localizacaoGeografica.getIdeLocalizacaoGeografica() != null){
				geometriaLoc = imovelRuralServiceFacade.buscarGeometriaShape(localizacaoGeografica.getIdeLocalizacaoGeografica());
				return imovelRuralServiceFacade.getAreaShape(geometriaLoc);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return 0.0;
	}
	
	public boolean validarDezPorcentoAreaDeclarada(Double areaDeclarada, Double areaShape)throws Exception{
		String nomeAba = nomeAba();
		boolean retorno = true;
		try {
			if(areaDeclarada != null){
				imovelRuralServiceFacade.validarValorAreaDeclaradaShapeTemporario(areaDeclarada,areaShape);
			}else{
				excluirShapes(false,null);
				JsfUtil.addErrorMessage("Erro na validação de geometria" + nomeAba + ", contate o administrador do sistema.");
				return false;
			}
		} catch(AreaDeclaradaInvalidaException a) {
			retorno = false;
			JsfUtil.addErrorMessage("A área informada" + nomeAba + " (" + a.getAreaDeclarada() + " ha) não confere com a área do shapefile importado ("+a.getAreaCalculada()+" ha).");
			return false;
		} catch (Exception e) {
			retorno = false;
			JsfUtil.addErrorMessage("Erro na validação de geometria" + nomeAba + ", contate o administrador do sistema.");
			return false;
		}
		return retorno;
	}
	
	public boolean validarReservaDezPorcentoAreaDeclarada(Double areaDeclarada, Double areaShape)throws Exception{
		String nomeAba = nomeAba();
		boolean retorno = true;
		try {
			if(areaDeclarada != null){
				imovelRuralServiceFacade.validarReservaAreaDeclaradaShapeTemporario(areaDeclarada,areaShape);
			}else{
				excluirShapes(false,null);
				JsfUtil.addErrorMessage("Erro na validação de geometria" + nomeAba + ", contate o administrador do sistema.");
				return false;
			}
		} catch(AreaDeclaradaInvalidaException a) {
			retorno = false;
			JsfUtil.addErrorMessage("A área" + nomeAba + " importado no shapefile(" + a.getAreaCalculada() + " ha) não pode ser menor ou ultrapassar em 10% o valor da área informada ("+a.getAreaDeclarada()+" ha).");
			return false;
		} catch (Exception e) {
			retorno = false;
			JsfUtil.addErrorMessage("Erro na validação de geometria" + nomeAba + ", contate o administrador do sistema.");
			return false;
		}
		return retorno;
	}
	
	private String nomeAba(){
		if ("abaLimitePropriedade".equals(abaAtual)) {
			return " do Limite da Propriedade";
		}else if ("abaReservaLegal".equals(abaAtual)) {
			return " da Reserva Legal";
		} else if ("abaVegetacaoNativa".equals(abaAtual)) {
			return " da Vegetação Nativa";
		} else if ("abaAPP".equals(abaAtual)) {
			return " da Área de Preservação Permanente";
		} else if ("abaRPPN".equals(abaAtual)) {
			return " do RPPN";	
		} else if ("abaAtividadeDesenvolvida".equals(abaAtual)) {
			return " da Atividade Desenvolvida";
		} else {
			return "";
		}
	}
	
	public boolean incluirShapeApp(App item){
		if(Util.isNullOuVazio(listaAppsAlterados)){
			return false;
		}
		return listaAppsAlterados.contains(item);
	}
	
	public boolean incluirShapeAd(AreaProdutiva item){
		if(Util.isNullOuVazio(listaAreasProdutivasAlterados)){
			return false;
		}
		return listaAreasProdutivasAlterados.contains(item);
			
	}
	
	private boolean validarShapesatualizados(){
		boolean retorno = true;
		if((limitePropAlterado == null || limitePropAlterado.getIdeImovelRural() == null) && !tab0){
			retorno = false;
		}else if((reservaLegalAlterado == null || reservaLegalAlterado.getIdeImovelRural() == null) && !tab1){ 
			retorno = false;
		}else if((vegetacaoNativaAlterado == null || vegetacaoNativaAlterado.getIdeImovelRural() == null) && !tab2){
			retorno = false;
		}else if((listaAppsAlterados == null || listaAppsAlterados.isEmpty()) && !tab3){
			retorno = false;
		}else if((rppnAlterado == null || rppnAlterado.getIdeImovelRural() == null) && !tab4){
			retorno = false;
		}else if((listaAreasProdutivasAlterados == null || listaAreasProdutivasAlterados.isEmpty())&& !tab5){
			retorno = false;
		}
		if(!retorno){
			JsfUtil.addWarnMessage("Todos os arquivos devem ser alterados.");
		}
		return retorno;
	}
		
	public void excluirShapes(boolean variant, AreaProdutiva areaProdutivaTela) {
		if(areaProdutivaTela != null)
			try {
				areaProdutivaSelecionada = areaProdutivaTela.clone();
			} catch (CloneNotSupportedException e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		
		String diretorioExcluir = excluirPasta();
		
		if (!Util.isNullOuVazio(diretorioExcluir)){
			FileUploadUtil.removerArquivos(new File(diretorioExcluir));
			if(variant) {
				JsfUtil.addSuccessMessage("Arquivo Removido com Sucesso.");
			}
			limparListas();
			tab6 = true;
			Html.atualizar("formAtualizacaoShape:abasAtualizacaoShapes");
			Html.atualizar("formAcao");
		} else {
			JsfUtil.addWarnMessage("Não é possível excluir este arquivo.");
		}
	}

	private String excluirPasta() {
		String diretorioExcluir = "";
		switch (activeIndex) {
			case 0:
				diretorioExcluir = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.LIMITE_PROPRIEDADE.getId();
				break;
			case 1:
				diretorioExcluir = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.RESERVA_LEGAL.getId();
				break;
			case 2:
				diretorioExcluir = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.VEGETACAO_NATIVA.getId();
				break;
			case 3:
				diretorioExcluir = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.APP.getId() + "_" + appSelecionada.getCodigoPersistirShape();
				break;
			case 4:
				diretorioExcluir = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.RPPN.getId();
				break;
			case 5:
				diretorioExcluir = DiretorioArquivoEnum.SHAPEFILESTEMP.toString() + imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.ATIVIDADE_DESENVOLVIDA.getId() + "_" + areaProdutivaSelecionada.getCodigoPersistirShape();
				break;
			
			default:
				break;
		}
		return diretorioExcluir;
	}
	
	private void limparListas(){
		switch (activeIndex) {
		case 3:
			listaAppsAlterados.remove(appSelecionada);
			appSelecionada = null;
			atualizarApp = true;
			break;
		case 4:
			incluirShapeRppn = false;
			listaImovelRuralRppnAlterados = null;
			listaImovelRuralRppnAlterados = new ArrayList<ImovelRuralRppn>();
			rppnSelecionado = null;
			rppnAlterado = new ImovelRuralRppn();
			atualizarRppn = true;
			break;
		case 5:
			atualizarAd = true;
			listaAreasProdutivasAlterados.remove(areaProdutivaSelecionada);
			areaProdutivaSelecionada = null;
			break;
		default:
			if(activeIndex == 0){
				limitePropSelecionado.getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
				limitePropAlterado = new ImovelRural();
			}else if(activeIndex == 1){
				reservaLegalSelecionado.getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
				reservaLegalAlterado = new ImovelRural();
			}else if(activeIndex == 2){
				vegetacaoNativaSelecionado.getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
				vegetacaoNativaAlterado = new ImovelRural();
				
			}
			atualizarImovel = true;
			incluirShape = false;
			listaShapeUpldImoveis = null;
			listaShapeUpldImoveis = new ArrayList<ImovelRural>();
			break;
		}
	}
	
	public Integer idlocImovelRural(ImovelRural obj){
		if(obj != null){
			if(activeIndex == 0){
				return obj.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica();
			}
			if(activeIndex == 1){
				return obj.getReservaLegal().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica();
			}
			if(activeIndex == 2){
				return obj.getVegetacaoNativa().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica();
			}
		}
		
		return 0;
	}
	
	public Integer getIdlocImovelRuralAlterado(){
		return imovelRural.getIdeImovelRural();
	}
	
	public Integer idlocApp(App obj){
		if(obj != null){
			return obj.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica();
		}
		return 0;
	}
	
	public Integer idlocRppn(ImovelRuralRppn obj){
		if(obj != null){
			return obj.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica();
		}
		return 0;
	}
	
	public Integer idlocAd(AreaProdutiva obj){
		if(obj != null){
			return obj.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica();
		}
		return 0;
	}
	
	public Integer getTipoImovelRural() {
		switch (activeIndex) {
			case 0:
				return TemaGeoseiaEnum.LIMITE_PROPRIEDADE.getId();
			case 1:
				return TemaGeoseiaEnum.RESERVA_LEGAL.getId();
			case 2:
				return TemaGeoseiaEnum.VEGETACAO_NATIVA.getId();
			case 3:
				return TemaGeoseiaEnum.APP.getId();
			case 4:
				return TemaGeoseiaEnum.RPPN.getId();
			case 5:
				return TemaGeoseiaEnum.ATIVIDADE_DESENVOLVIDA.getId();
			default:
				return 0;
		}
	}
		
	public void excluirDadosGeografico(LocalizacaoGeografica obj){
		try {
			
			arquivoProcessoService.excluirPorLocalizacaoGeografica(obj.getIdeLocalizacaoGeografica());
			paramPersistDadoGeoService.excluirPorLocalizacaoGeografica(obj.getIdeLocalizacaoGeografica());
			
			obj.setDadoGeograficoCollection(null);
			obj.setLocalizacaoExcluida(false);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void persistirImovelRppnCefir(ImovelRural imovel) throws Exception {
		if (imovel != null && imovel.getIdeImovelRuralRppn() != null && imovel.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica() != null){
			if (imovel.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().getLocalizacaoExcluida()) {
				if (imovel.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica() != null) {
					imovelRuralServiceFacade.excluirDadosPersistidosLocalizacao(imovel.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
					imovel.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().setDadoGeograficoCollection(null);
				}
				imovel.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().setLocalizacaoExcluida(false);
			}

			LocalizacaoGeografica temp = null;
			if (!Util.isNull(imovel.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().getIdeClassificacaoSecao())) {
				imovelRuralServiceFacade.salvarOuAtualizarLocalizacaoGeografica(imovel.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica());
			} else {
				temp = imovel.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica();
				imovel.getIdeImovelRuralRppn().setIdeLocalizacaoGeografica(null);
			}
			
			if (!imovelRuralControllerNew.isEdicaoImoveRuralRppn(imovel)) {
				imovel.getIdeImovelRuralRppn().setIdeImovelRural(imovel);
				imovelRuralServiceFacade.salvarImovelRuralRppn(imovel.getIdeImovelRuralRppn());
				auditoria.salvar(imovel.getIdeImovelRuralRppn());
			} else {
				ImovelRuralRppn imovelRuralRppnAtual = imovelRuralControllerNew.carregarImovelRuralRppn(imovel);
				imovelRuralServiceFacade.atualizarImovelRuralRppn(imovel.getIdeImovelRuralRppn());
				auditoria.atualizar(imovelRuralRppnAtual, imovel.getIdeImovelRuralRppn());
			}
			
			if (temp != null) {
				imovel.getIdeImovelRuralRppn().setIdeLocalizacaoGeografica(temp);
			}
			
			if (imovelRuralControllerNew.temShapeTemporario(imovel.getIdeImovelRural() + "_" + TemaGeoseiaEnum.RPPN.getId(), 3)) {
				FileUploadUtil.moverArquivoShapePastaTemporariaParaPastaOriginalCefir(imovel.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica(), "13", imovel.getIdeImovelRural().toString());
				imovelRuralServiceFacade.persistirShapes(imovel.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica(), null);
				imovel.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica().setNovosArquivosShapeImportados(false);
			}
			
			atualizarLocalizacaoGeografica(imovel.getIdeImovelRuralRppn().getIdeLocalizacaoGeografica());
		}
	}
	
	public void persistirAppCefir(App pApp) throws Exception {
		
		if (pApp.getIdeLocalizacaoGeografica().getLocalizacaoExcluida()) {
			if (pApp.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica() != null) {
				imovelRuralServiceFacade.excluirDadosPersistidosLocalizacao(pApp.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				pApp.getIdeLocalizacaoGeografica().setDadoGeograficoCollection(null);
			}
			pApp.getIdeLocalizacaoGeografica().setLocalizacaoExcluida(false);
		}
		if (Util.isNullOuVazio(pApp.getIdeApp())) {
			imovelRuralServiceFacade.salvarOuAtualizarLocalizacaoGeografica(pApp.getIdeLocalizacaoGeografica());
			imovelRuralServiceFacade.salvarApp(pApp);
			auditoria.salvar(pApp);
			
		} else {
			App appAntiga = imovelRuralServiceFacade.carregarTudoApp(pApp);
			if (!Util.isNull(pApp.getIdeTipoEstadoConservacao()) && pApp.getIdeTipoEstadoConservacao().getIdeTipoEstadoConservacao().equals(TipoEstadoConservacaoEnum.PRESERVADA.getId())) {
				if (!Util.isNullOuVazio(appAntiga.getCronogramaRecuperacao()) && !Util.isNullOuVazio(appAntiga.getCronogramaRecuperacao().getIdeCronogramaRecuperacao())) {
					pApp.setIndProcessoRecuperacao(null);
					pApp.setCronogramaRecuperacao(null);
				}
			}
			if (!Util.isNullOuVazio(pApp.getCronogramaRecuperacao()) && !Util.isNullOuVazio(pApp.getCronogramaRecuperacao().getIdeCronogramaRecuperacao())) {
				imovelRuralServiceFacade.excluirAllEtapasByIdeCronogramaRecuperacao(pApp.getCronogramaRecuperacao().getIdeCronogramaRecuperacao());
				
				if (!Util.isNullOuVazio(pApp.getCronogramaRecuperacao().getCronogramaEtapaCollection())) {
					for (CronogramaEtapa etapa : pApp.getCronogramaRecuperacao().getCronogramaEtapaCollection())
						etapa.setIdeCronogramaEtapa(null);
				}
			}
			imovelRuralServiceFacade.salvarOuAtualizarApp(pApp);
			auditoria.atualizar(appAntiga, pApp);
		}
		if (imovelRuralControllerNew.temShapeTemporario(
				imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.APP.getId() + "_" + pApp.getCodigoPersistirShape(), 3)) {
			FileUploadUtil.moverArquivoShapePastaTemporariaParaPastaOriginalCefir(pApp.getIdeLocalizacaoGeografica(), "3_" + pApp.getCodigoPersistirShape(), this.imovelRural.getIdeImovelRural().toString());
			imovelRuralServiceFacade.persistirShapes(pApp.getIdeLocalizacaoGeografica(), null);
			pApp.getIdeLocalizacaoGeografica().setNovosArquivosShapeImportados(false);
		}
		atualizarLocalizacaoGeografica(pApp.getIdeLocalizacaoGeografica());
		pApp.getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
	}
	
	public void persistirAreaProdutivaCefir(AreaProdutiva pAreaProdutiva) throws Exception {
		List<AreaProdutivaTipologiaAtividade> listaAtividade = new ArrayList<AreaProdutivaTipologiaAtividade>();
		if(pAreaProdutiva.possuiTipologiaAtividadeCadastrada()) {
			listaAtividade.addAll(pAreaProdutiva.getAreaProdutivaTipologiaAtividadeCollection());
		}

		if (pAreaProdutiva.getIdeLocalizacaoGeografica().getLocalizacaoExcluida()) {
			if (pAreaProdutiva.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica() != null) {
				imovelRuralServiceFacade.excluirDadosPersistidosLocalizacao(pAreaProdutiva.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				pAreaProdutiva.getIdeLocalizacaoGeografica().setDadoGeograficoCollection(null);
			}
			pAreaProdutiva.getIdeLocalizacaoGeografica().setLocalizacaoExcluida(false);
		}
		
		if (Util.isNullOuVazio(pAreaProdutiva.getIdeAreaProdutiva())) {
			imovelRuralServiceFacade.salvarOuAtualizarLocalizacaoGeografica(pAreaProdutiva.getIdeLocalizacaoGeografica());

			pAreaProdutiva.setIdeImovelRural(imovelRural);
			pAreaProdutiva.setAreaProdutivaTipologiaAtividadeCollection(null);
			if(!Util.isNull(pAreaProdutiva.getIdeDocumentoAutorizacaoManejo())) {
				imovelRuralServiceFacade.salvarDocumentoObrigatorio(pAreaProdutiva.getIdeDocumentoAutorizacaoManejo());
			}
			imovelRuralServiceFacade.salvarAreaProdutiva(pAreaProdutiva);
			auditoria.salvar(pAreaProdutiva);

		} else {
			if(!Util.isNull(pAreaProdutiva.getIdeDocumentoAutorizacaoManejo())) {
				imovelRuralServiceFacade.salvarDocumentoObrigatorio(pAreaProdutiva.getIdeDocumentoAutorizacaoManejo());
			}
			
			AreaProdutiva lAreaProdutivaAntigo = imovelRuralServiceFacade.carregarTudoAreaProdutiva(pAreaProdutiva);
			pAreaProdutiva.setAreaProdutivaTipologiaAtividadeCollection(null);
			
			imovelRuralServiceFacade.salvarOuAtualizarAreaProdutiva(pAreaProdutiva);
			
			auditoria.atualizar(lAreaProdutivaAntigo, pAreaProdutiva);
		}

		imovelRuralServiceFacade.excluirAllAptaByIdeAreaProdutiva(pAreaProdutiva);

		if(!Util.isNullOuVazio(pAreaProdutiva.getIdeTipologiaSubgrupo())) {
			
			List<AreaProdutivaTipologiaAtividade> listaTempClones = new ArrayList<AreaProdutivaTipologiaAtividade>();

			if (pAreaProdutiva.getIdeTipologiaSubgrupo().getIdeTipologia().equals(TipologiaCefirEnum.AGRICULTURA_IRRIGADA.getId())) {
				for (AreaProdutivaTipologiaAtividade apta : listaAtividade) {
					
					AreaProdutivaTipologiaAtividade aptaClonada = apta.clone();
					
					aptaClonada.setIdeAreaProdutivaTipologiaAtividade(null);
					aptaClonada.setIdeAreaProdutiva(pAreaProdutiva);
					aptaClonada.setIndExcluido(Boolean.FALSE);
					aptaClonada.setDtcCriacao(new Date());
					aptaClonada.setDtcExclusao(null);
					aptaClonada.getIdeAreaProdutivaTipologiaAtividadeAgricultura().setIdeAreaProdutivaTipologiaAtividadeAgricultura(null);
					
					AreaProdutivaTipologiaAtividadeAgricultura aptaAgricultura = aptaClonada.getIdeAreaProdutivaTipologiaAtividadeAgricultura();
					aptaClonada.setIdeAreaProdutivaTipologiaAtividadeAgricultura(null);
					
					imovelRuralServiceFacade.salvarOuAtualizarAreaProdutivaTipologiaAtividade(aptaClonada);
					
					aptaAgricultura.setIdeAreaProdutivaTipologiaAtividade(aptaClonada);
					imovelRuralServiceFacade.salvarAreaProdutivaTipologiaAtividadeAgricultura(aptaAgricultura);
					
					aptaClonada.setIdeAreaProdutivaTipologiaAtividadeAgricultura(aptaAgricultura);
					
					listaTempClones.add(aptaClonada);
				}
			} else if (pAreaProdutiva.getIdeTipologiaSubgrupo().getIdeTipologia().equals(TipologiaCefirEnum.PECUARIA.getId())
					|| pAreaProdutiva.getIdeTipologiaSubgrupo().getIdeTipologia().equals(TipologiaCefirEnum.CRIACOES_CONFINADAS.getId())) {
				
				for (AreaProdutivaTipologiaAtividade apta : listaAtividade) {
					
					AreaProdutivaTipologiaAtividade aptaClonada = apta.clone();
					
					aptaClonada.setIdeAreaProdutivaTipologiaAtividade(null);
					aptaClonada.setIdeAreaProdutiva(pAreaProdutiva);
					aptaClonada.setIndExcluido(Boolean.FALSE);
					aptaClonada.setDtcCriacao(new Date());
					aptaClonada.setDtcExclusao(null);
					
					AreaProdutivaTipologiaAtividadeAnimal aptaAnimal = aptaClonada.getIdeAreaProdutivaTipologiaAtividadeAnimal();
					aptaClonada.setIdeAreaProdutivaTipologiaAtividadeAnimal(null);
					
					imovelRuralServiceFacade.salvarOuAtualizarAreaProdutivaTipologiaAtividade(aptaClonada);
					
					aptaAnimal.setIdeAreaProdutivaTipologiaAtividade(aptaClonada);
					imovelRuralServiceFacade.salvarAreaProdutivaTipologiaAtividadeAnimal(aptaAnimal);
					
					aptaClonada.setIdeAreaProdutivaTipologiaAtividadeAnimal(aptaAnimal);
					
					listaTempClones.add(aptaClonada);
				}
			} else if (pAreaProdutiva.getIdeTipologiaSubgrupo().getIdeTipologia().equals(TipologiaCefirEnum.AQUICULTURA.getId())) {
				for (AreaProdutivaTipologiaAtividade apta : listaAtividade) {
					
					AreaProdutivaTipologiaAtividade aptaClonada = apta.clone();
					
					aptaClonada.setIdeAreaProdutivaTipologiaAtividade(null);
					aptaClonada.setIdeAreaProdutiva(pAreaProdutiva);
					aptaClonada.setIndExcluido(Boolean.FALSE);
					aptaClonada.setDtcCriacao(new Date());
					aptaClonada.setDtcExclusao(null);
					
					AreaProdutivaTipologiaAtividadePiscicultura aptaPisicultura = aptaClonada.getIdeAreaProdutivaTipologiaAtividadePiscicultura();
					aptaClonada.setIdeAreaProdutivaTipologiaAtividadePiscicultura(null);
					
					imovelRuralServiceFacade.salvarOuAtualizarAreaProdutivaTipologiaAtividade(aptaClonada);

					aptaPisicultura.setIdeAreaProdutivaTipologiaAtividade(aptaClonada);
					imovelRuralServiceFacade.salvarAreaProdutivaTipologiaAtividadePiscicultura(aptaPisicultura);

					aptaClonada.setIdeAreaProdutivaTipologiaAtividadePiscicultura(aptaPisicultura);
					
					listaTempClones.add(aptaClonada);
				}
			} else {
				for (AreaProdutivaTipologiaAtividade apta : listaAtividade) {
					
					AreaProdutivaTipologiaAtividade aptaClonada = apta.clone();
					
					aptaClonada.setIdeAreaProdutivaTipologiaAtividade(null);
					aptaClonada.setIdeAreaProdutiva(pAreaProdutiva);
					aptaClonada.setIndExcluido(Boolean.FALSE);
					aptaClonada.setDtcCriacao(new Date());
					aptaClonada.setDtcExclusao(null);
					imovelRuralServiceFacade.salvarOuAtualizarAreaProdutivaTipologiaAtividade(aptaClonada);
					
					listaTempClones.add(aptaClonada);
				}
			}
			
			pAreaProdutiva.setAreaProdutivaTipologiaAtividadeCollection(listaTempClones);
		}

		if (imovelRuralControllerNew.temShapeTemporario(
				imovelRural.getIdeImovelRural() + "_" + TemaGeoseiaEnum.ATIVIDADE_DESENVOLVIDA.getId() + "_" + pAreaProdutiva.getCodigoPersistirShape(), 3)) {
			
			FileUploadUtil.moverArquivoShapePastaTemporariaParaPastaOriginalCefir(pAreaProdutiva.getIdeLocalizacaoGeografica(), "4_" + pAreaProdutiva.getCodigoPersistirShape(), this.imovelRural.getIdeImovelRural().toString());
			imovelRuralServiceFacade.persistirShapes(pAreaProdutiva.getIdeLocalizacaoGeografica(), null);
			pAreaProdutiva.getIdeLocalizacaoGeografica().setNovosArquivosShapeImportados(false);
			
		}
		
		atualizarLocalizacaoGeografica(pAreaProdutiva.getIdeLocalizacaoGeografica());
		
		pAreaProdutiva.getIdeLocalizacaoGeografica().setListArquivosShape(new ArrayList<String>());
	}
	
	public void validarObservacao(){ //Caso o usuário apague a observação, a aba visualizar deve ser desabilitada e o campo apagado.
		if(StringUtils.isEmpty(observacoes)){
			tab6 = true;
		
			switch (activeIndex) {
				case 0:
					setObservacaoLP(null);
					break;
				case 1:
					setObservacaoRL(null);
					break;
				case 2:
					setObservacaoVN(null);
					break;
				case 3:
					setObservacaoAPP(null);
					break;
				case 4:
					setObservacaoRPPN(null);
					break;											
				case 5:
					setObservacaoAD(null);
					break;
				default:
					break;
			}
			
			Html.atualizar("formAtualizacaoShape:abasAtualizacaoShapes");
			Html.atualizar("formAcao");
		}
	}
	
	private void limparValArea(){ 
		switch (activeIndex) {
				case 0:
					setValAreaLim(0.00);
					break;
				case 1:
					setValAreaRL(0.00);
					break;
				case 2:
					setValAreaVN(0.00);
					break;
				case 3:
					setValAreaAPP(0.00);
					break;
				case 4:
					setValAreaRPPN(0.00);
					break;											
				case 5:
					setValAreaAtvDesen(0.00);
					break;
				default:
					break;
			}
	}
	
	/** implementar downloand arquivos*/
	
	public void carregarDownloadShapes(){
		
		if(getFileDownloadDBF() != null && getFileDownloadSHP() != null && getFileDownloadSHX() != null) {
			Html.atualizar("dlgDownloadShapesCefir");
			Html.exibir("dlgDownloadShapesCefir");
		} else {
			MensagemUtil.erro("Arquivo não encontrado, favor entrar em contato com a administração do sistema.");
		}
	}
	
	public void carregarDownloadArquivosSelecionado(Object obj){
		if(activeIndex == 0){
			ImovelRural imovel = (ImovelRural) obj;
			localizacaoGeograficaSelecionada = imovel.getIdeLocalizacaoGeografica();
			this.nomeSHP = "Limite_de_Propriedade_("+imovel.getIdeImovelRural().toString()+").shp";
			this.nomeSHX = "Limite_de_Propriedade_("+imovel.getIdeImovelRural().toString()+").shx";
			this.nomeDBF = "Limite_de_Propriedade_("+imovel.getIdeImovelRural().toString()+").dbf";
		}else if(activeIndex == 1){
			ImovelRural imovel = (ImovelRural) obj;
			localizacaoGeograficaSelecionada = imovel.getReservaLegal().getIdeLocalizacaoGeografica();
			this.nomeSHP = "Reserva_Legal_("+imovel.getIdeImovelRural().toString()+").shp";
			this.nomeSHX = "Reserva_Legal_("+imovel.getIdeImovelRural().toString()+").shx";
			this.nomeDBF = "Reserva_Legal_("+imovel.getIdeImovelRural().toString()+").dbf";
		}else if(activeIndex == 2){
			ImovelRural imovel = (ImovelRural) obj;
			localizacaoGeograficaSelecionada = imovel.getVegetacaoNativa().getIdeLocalizacaoGeografica();
			this.nomeSHP = "Vegetação_Nativa_("+imovel.getIdeImovelRural().toString()+").shp";
			this.nomeSHX = "Vegetação_Nativa_("+imovel.getIdeImovelRural().toString()+").shx";
			this.nomeDBF = "Vegetação_Nativa_("+imovel.getIdeImovelRural().toString()+").dbf";
		}else if(activeIndex == 3){
			App app = (App) obj;
			localizacaoGeograficaSelecionada = app.getIdeLocalizacaoGeografica();
			this.nomeSHP = "APP_("+app.getIdeImovelRural().getIdeImovelRural().toString()+").shp";
			this.nomeSHX = "APP_("+app.getIdeImovelRural().getIdeImovelRural().toString()+").shx";
			this.nomeDBF = "APP_("+app.getIdeImovelRural().getIdeImovelRural().toString()+").dbf";
		}else if(activeIndex == 4){
			ImovelRuralRppn rppn = (ImovelRuralRppn) obj;
			localizacaoGeograficaSelecionada = rppn.getIdeLocalizacaoGeografica();
			this.nomeSHP = "RPPN_("+rppn.getIdeImovelRural().getIdeImovelRural().toString()+").shp";
			this.nomeSHX = "RPPN_("+rppn.getIdeImovelRural().getIdeImovelRural().toString()+").shx";
			this.nomeDBF = "RPPN_("+rppn.getIdeImovelRural().getIdeImovelRural().toString()+").dbf";
		}else if(activeIndex == 5){
			AreaProdutiva area = (AreaProdutiva) obj;
			localizacaoGeograficaSelecionada = area.getIdeLocalizacaoGeografica();
			this.nomeSHP = "Atividade_Desenvolvida_("+area.getIdeImovelRural().getIdeImovelRural().toString()+").shp";
			this.nomeSHX = "Atividade_Desenvolvida_("+area.getIdeImovelRural().getIdeImovelRural().toString()+").shx";
			this.nomeDBF = "Atividade_Desenvolvida_("+area.getIdeImovelRural().getIdeImovelRural().toString()+").dbf";
		}
	}
	
	private String caminhoArquivo(LocalizacaoGeografica localizacaoGeo){
		return DiretorioArquivoEnum.SHAPEFILES.toString() + localizacaoGeo.getIdeLocalizacaoGeografica().toString();
	}
		
	public StreamedContent getFileDownloadSHP(){
		String pasta = null;
		String arquivo = null;
		String nomeDoArquivo = null;
		
		pasta = caminhoArquivo(localizacaoGeograficaSelecionada);
		
		nomeDoArquivo = localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica().toString() + "_" + localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().getSrid();
		
		arquivo = imovelRuralServiceFacade.ObterShapeDiretorio(pasta, nomeDoArquivo, ".shp");
		
		return imovelRuralControllerNew.getFileDownload(arquivo);
	}
	
	public StreamedContent getFileDownloadSHX(){
		String pasta = null;
		String arquivo = null;
		String nomeDoArquivo = null;
		
		pasta = caminhoArquivo(localizacaoGeograficaSelecionada);
		nomeDoArquivo = localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica().toString() + "_" + localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().getSrid();
		arquivo = imovelRuralServiceFacade.ObterShapeDiretorio(pasta, nomeDoArquivo, ".shx");
		
		return imovelRuralControllerNew.getFileDownload(arquivo);
	}
	
	public StreamedContent getFileDownloadDBF(){
		String pasta = null;
		String arquivo = null;
		String nomeDoArquivo = null;
		
		pasta = caminhoArquivo(localizacaoGeograficaSelecionada);
		nomeDoArquivo = localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica().toString() + "_" + localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().getSrid();
		arquivo = imovelRuralServiceFacade.ObterShapeDiretorio(pasta, nomeDoArquivo, ".dbf");
		
		return imovelRuralControllerNew.getFileDownload(arquivo);
	}
	
	private void atualizarLocalizacaoGeografica(LocalizacaoGeografica lg) {
		try {
			lg.setDadoGeograficoCollection(null);
			lg.setDtcAtualizacao(new Date());
			imovelRuralServiceFacade.salvarOuAtualizarLocalizacaoGeografica(lg);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * XXX: GETTS E SETTS
	 */

	public boolean isExibirBtnVoltar() {
		return exibirBtnVoltar;
	}

	public void setExibirBtnVoltar(boolean exibirBtnVoltar) {
		this.exibirBtnVoltar = exibirBtnVoltar;
	}

	public boolean isExibirBtnFinalizar() {
		return exibirBtnFinalizar;
	}

	public void setExibirBtnFinalizar(boolean exibirBtnFinalizar) {
		this.exibirBtnFinalizar = exibirBtnFinalizar;
	}

	public boolean isExibirBtnSalvarOuAvancar() {
		return exibirBtnSalvarOuAvancar;
	}

	public void setExibirBtnSalvarOrAvancar(boolean exibirBtnSalvarOuAvancar) {
		this.exibirBtnSalvarOuAvancar = exibirBtnSalvarOuAvancar;
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public String getAbaAtual() {
		return abaAtual;
	}

	public void setAbaAtual(String abaAtual) {
		this.abaAtual = abaAtual;
	}

	public boolean isExibirAbaVisualizar() {
		return exibirAbaVisualizar;
	}

	public void setExibirAbaVisualizar(boolean exibirAbaVisualizar) {
		this.exibirAbaVisualizar = exibirAbaVisualizar;
	}

	public ImovelRural getImovelRural() {
		return imovelRural;
	}

	public void setImovelRural(ImovelRural imovelRural) {
		this.imovelRural = imovelRural;
	}

	public ArrayList<ImovelRural> getListaImoveis() {
		return listaImoveis;
	}

	public void setListaImoveis(ArrayList<ImovelRural> listaImoveis) {
		this.listaImoveis = listaImoveis;
	}

	public ImovelRuralRppn getImovelRuralRppn() {
		return imovelRuralRppn;
	}

	public void setImovelRuralRppn(ImovelRuralRppn imovelRuralRppn) {
		this.imovelRuralRppn = imovelRuralRppn;
	}

	public boolean isTab6() {
		return tab6;
	}

	public void setTab6(boolean tab6) {
		this.tab6 = tab6;
	}

	public boolean isTab5() {
		return tab5;
	}

	public void setTab5(boolean tab5) {
		this.tab5 = tab5;
	}

	public boolean isTab1() {
		return tab1;
	}

	public void setTab1(boolean tab1) {
		this.tab1 = tab1;
	}

	public boolean isTab4() {
		return tab4;
	}

	public void setTab4(boolean tab4) {
		this.tab4 = tab4;
	}

	public boolean isTab2() {
		return tab2;
	}

	public void setTab2(boolean tab2) {
		this.tab2 = tab2;
	}

	public boolean isTab3() {
		return tab3;
	}

	public void setTab3(boolean tab3) {
		this.tab3 = tab3;
	}

	public boolean isTab0() {
		return tab0;
	}

	public void setTab0(boolean tab0) {
		this.tab0 = tab0;
	}

	public String getCaminhoDesenharGeoBahia() {
		return caminhoDesenharGeoBahia;
	}

	public void setCaminhoDesenharGeoBahia(String caminhoDesenharGeoBahia) {
		this.caminhoDesenharGeoBahia = caminhoDesenharGeoBahia;
	}

	public ImovelRural getImovelRuralOld() {
		return imovelRuralOld;
	}

	public void setImovelRuralOld(ImovelRural imovelRuralOld) {
		this.imovelRuralOld = imovelRuralOld;
	}

	public ArrayList<ImovelRural> getListaShapeUpldImoveis() {
		return listaShapeUpldImoveis;
	}

	public void setListaShapeUpldImoveis(ArrayList<ImovelRural> listaShapeUpldImoveis) {
		this.listaShapeUpldImoveis = listaShapeUpldImoveis;
	}

	
	public List<SistemaCoordenada> getListaDatums() {
		return listaDatums;
	}

	
	public void setListaDatums(List<SistemaCoordenada> listaDatums) {
		this.listaDatums = listaDatums;
	}

	public List<ClassificacaoSecaoGeometrica> getListaSecaoGeometrica() {
		return listaSecaoGeometrica;
	}

	public void setListaSecaoGeometrica(
			List<ClassificacaoSecaoGeometrica> listaSecaoGeometrica) {
		this.listaSecaoGeometrica = listaSecaoGeometrica;
	}

	public ClassificacaoSecaoGeometrica getSecaoGeoSeleccionada() {
		return secaoGeoSeleccionada;
	}

	public void setSecaoGeoSeleccionada(ClassificacaoSecaoGeometrica secaoGeoSeleccionada) {
		this.secaoGeoSeleccionada = secaoGeoSeleccionada;
	}

	public SistemaCoordenada getSistemaCoordSelecionado() {
		return sistemaCoordSelecionado;
	}

	public void setSistemaCoordSelecionado(SistemaCoordenada sistemaCoordSelecionado) {
		this.sistemaCoordSelecionado = sistemaCoordSelecionado;
	}

	public ImovelRural getLimitePropSelecionado() {
		return limitePropSelecionado;
	}

	public void setLimitePropSelecionado(ImovelRural limitePropSelecionado) {
		this.limitePropSelecionado = limitePropSelecionado;
	}

	public LocalizacaoGeografica getLocalizacaoSelecionada() {
		return localizacaoSelecionada;
	}

	public void setLocalizacaoSelecionada(LocalizacaoGeografica localizacaoSelecionada) {
		this.localizacaoSelecionada = localizacaoSelecionada;
	}

	public List<String> getListaLocalizacaoGeo() {
		return listaLocalizacaoGeo;
	}

	public void setListaLocalizacaoGeo(List<String> listaLocalizacaoGeo) {
		this.listaLocalizacaoGeo = listaLocalizacaoGeo;
	}

	public boolean isIncluirShape() {
		return incluirShape;
	}

	public void setIncluirShape(boolean incluirShape) {
		this.incluirShape = incluirShape;
	}

	public String getObservacaoLP() {
		return observacaoLP;
	}

	public void setObservacaoLP(String observacaoLP) {
		this.observacaoLP = observacaoLP;
	}

	public String getObservacaoRL() {
		return observacaoRL;
	}

	public void setObservacaoRL(String observacaoRL) {
		this.observacaoRL = observacaoRL;
	}

	public String getObservacaoVN() {
		return observacaoVN;
	}

	public void setObservacaoVN(String observacaoVN) {
		this.observacaoVN = observacaoVN;
	}

	public String getObservacaoAPP() {
		return observacaoAPP;
	}

	public void setObservacaoAPP(String observacaoAPP) {
		this.observacaoAPP = observacaoAPP;
	}

	public String getObservacaoRPPN() {
		return observacaoRPPN;
	}

	public void setObservacaoRPPN(String observacaoRPPN) {
		this.observacaoRPPN = observacaoRPPN;
	}

	public String getObservacaoAD() {
		return observacaoAD;
	}

	public void setObservacaoAD(String observacaoAD) {
		this.observacaoAD = observacaoAD;
	}

	public ImovelRural getReservaLegalSelecionado() {
		return reservaLegalSelecionado;
	}

	public void setReservaLegalSelecionado(ImovelRural reservaLegalSelecionado) {
		this.reservaLegalSelecionado = reservaLegalSelecionado;
	}

	public ImovelRural getVegetacaoNativaSelecionado() {
		return vegetacaoNativaSelecionado;
	}

	public void setVegetacaoNativaSelecionado(ImovelRural vegetacaoNativaSelecionado) {
		this.vegetacaoNativaSelecionado = vegetacaoNativaSelecionado;
	}

	public App getAppSelecionada() {
		return appSelecionada;
	}

	public void setAppSelecionada(App appSelecionada) {
		this.appSelecionada = appSelecionada;
	}

	public Collection<App> getListaAppsAlterados() {
		return listaAppsAlterados;
	}

	public void setListaAppsAlterados(Collection<App> listaAppsAlterados) {
		this.listaAppsAlterados = listaAppsAlterados;
	}

	public Collection<App> getAppCollection() {
		return appCollection;
	}

	public void setAppCollection(Collection<App> appCollection) {
		this.appCollection = appCollection;
	}

	public String getGeometriaLoc() {
		return geometriaLoc;
	}

	public void setGeometriaLoc(String geometriaLoc) {
		this.geometriaLoc = geometriaLoc;
	}

	public String getIdeLocalizacaoGeograficaSelect() {
		return ideLocalizacaoGeograficaSelect;
	}

	public void setIdeLocalizacaoGeograficaSelect(String ideLocalizacaoGeograficaSelect) {
		localizacaoSelecionada = new LocalizacaoGeografica();
		localizacaoSelecionada.setIdeLocalizacaoGeografica(Integer.valueOf(ideLocalizacaoGeograficaSelect));
		this.ideLocalizacaoGeograficaSelect = ideLocalizacaoGeograficaSelect;
	}

	public AreaProdutiva getAreaProdutivaSelecionada() {
		return areaProdutivaSelecionada;
	}

	public void setAreaProdutivaSelecionada(AreaProdutiva areaProdutivaSelecionada) {
		this.areaProdutivaSelecionada = areaProdutivaSelecionada;
	}

	public Collection<AreaProdutiva> getListaAreasProdutivasAlterados() {
		
		return listaAreasProdutivasAlterados;
	}

	public void setListaAreasProdutivasAlterados(Collection<AreaProdutiva> listaAreasProdutivasAlterados) {
		this.listaAreasProdutivasAlterados = listaAreasProdutivasAlterados;
	}

	public Collection<ImovelRuralRppn> getListaImovelRuralRppn() {
		return listaImovelRuralRppn;
	}

	public void setListaImovelRuralRppn(Collection<ImovelRuralRppn> listaImovelRuralRppn) {
		this.listaImovelRuralRppn = listaImovelRuralRppn;
	}

	public boolean isIncluirShapeRppn() {
		return incluirShapeRppn;
	}

	public void setIncluirShapeRppn(boolean incluirShapeRppn) {
		this.incluirShapeRppn = incluirShapeRppn;
	}

	public ImovelRuralRppn getRppnSelecionado() {
		return rppnSelecionado;
	}

	public void setRppnSelecionado(ImovelRuralRppn rppnSelecionado) {
		this.rppnSelecionado = rppnSelecionado;
	}

	public Collection<AreaProdutiva> getAreaProdutivaCollection() {
		return areaProdutivaCollection;
	}

	public void setAreaProdutivaCollection(Collection<AreaProdutiva> areaProdutivaCollection) {
		this.areaProdutivaCollection = areaProdutivaCollection;
	}

	public Collection<ImovelRuralRppn> getListaImovelRuralRppnAlterados() {
		return listaImovelRuralRppnAlterados;
	}

	public void setListaImovelRuralRppnAlterados(
			Collection<ImovelRuralRppn> listaImovelRuralRppnAlterados) {
		this.listaImovelRuralRppnAlterados = listaImovelRuralRppnAlterados;
	}

	public boolean isAtualizarImovel() {
		return atualizarImovel;
	}

	public void setAtualizarImovel(boolean atualizarImovel) {
		this.atualizarImovel = atualizarImovel;
	}

	public boolean isAtualizarApp() {
		return atualizarApp;
	}

	public void setAtualizarApp(boolean atualizarApp) {
		this.atualizarApp = atualizarApp;
	}

	public boolean isAtualizarRppn() {
		return atualizarRppn;
	}

	public void setAtualizarRppn(boolean atualizarRppn) {
		this.atualizarRppn = atualizarRppn;
	}

	public boolean isAtualizarAd() {
		return atualizarAd;
	}

	public void setAtualizarAd(boolean atualizarAd) {
		this.atualizarAd = atualizarAd;
	}

	public ImovelRural getLimitePropAlterado() {
		return limitePropAlterado;
	}

	public void setLimitePropAlterado(ImovelRural limitePropAlterado) {
		this.limitePropAlterado = limitePropAlterado;
	}

	public ImovelRural getReservaLegalAlterado() {
		return reservaLegalAlterado;
	}

	public void setReservaLegalAlterado(ImovelRural reservaLegalAlterado) {
		this.reservaLegalAlterado = reservaLegalAlterado;
	}

	public ImovelRural getVegetacaoNativaAlterado() {
		return vegetacaoNativaAlterado;
	}

	public void setVegetacaoNativaAlterado(ImovelRural vegetacaoNativaAlterado) {
		this.vegetacaoNativaAlterado = vegetacaoNativaAlterado;
	}

	public ImovelRuralRppn getRppnAlterado() {
		return rppnAlterado;
	}

	public void setRppnAlterado(ImovelRuralRppn rppnAlterado) {
		this.rppnAlterado = rppnAlterado;
	}

	public int getActiveIndexAnterior() {
		return activeIndexAnterior;
	}

	public void setActiveIndexAnterior(int activeIndexAnterior) {
		this.activeIndexAnterior = activeIndexAnterior;
	}

	public ImovelRural getLimitePOld() {
		return limitePOld;
	}

	public void setLimitePOld(ImovelRural limitePOld) {
		this.limitePOld = limitePOld;
	}

	public ImovelRural getReservaLegalOld() {
		return reservaLegalOld;
	}

	public void setReservaLegalOld(ImovelRural reservaLegalOld) {
		this.reservaLegalOld = reservaLegalOld;
	}

	public ImovelRural getVegetacaoNativaOld() {
		return vegetacaoNativaOld;
	}

	public void setVegetacaoNativaOld(ImovelRural vegetacaoNativaOld) {
		this.vegetacaoNativaOld = vegetacaoNativaOld;
	}

	public Collection<App> getListaAppsOld() {
		return listaAppsOld;
	}

	public void setListaAppsOld(Collection<App> listaAppsOld) {
		this.listaAppsOld = listaAppsOld;
	}

	public ImovelRuralRppn getRppnOld() {
		return rppnOld;
	}

	public void setRppnOld(ImovelRuralRppn rppnOld) {
		this.rppnOld = rppnOld;
	}

	public Collection<AreaProdutiva> getListaAreasProdutivasOld() {
		return listaAreasProdutivasOld;
	}

	public void setListaAreasProdutivasOld(Collection<AreaProdutiva> listaAreasProdutivasOld) {
		this.listaAreasProdutivasOld = listaAreasProdutivasOld;
	}

	public ImovelRural getImovel() {
		return imovel;
	}

	public void setImovel(ImovelRural imovel) {
		this.imovel = imovel;
	}

	public Double getValAreaLim() {
		return valAreaLim;
	}

	public void setValAreaLim(Double valAreaLim) {
		this.valAreaLim = valAreaLim;
	}

	public Double getValAreaRL() {
		return valAreaRL;
	}

	public void setValAreaRL(Double valAreaRL) {
		this.valAreaRL = valAreaRL;
	}

	public Double getValAreaVN() {
		return valAreaVN;
	}

	public void setValAreaVN(Double valAreaVN) {
		this.valAreaVN = valAreaVN;
	}

	public Double getValAreaAPP() {
		return valAreaAPP;
	}

	public void setValAreaAPP(Double valAreaAPP) {
		this.valAreaAPP = valAreaAPP;
	}

	public Double getValAreaRPPN() {
		return valAreaRPPN;
	}

	public void setValAreaRPPN(Double valAreaRPPN) {
		this.valAreaRPPN = valAreaRPPN;
	}
	
	public Double getValAreaAtvDesen() {
		return valAreaAtvDesen;
	}

	public void setValAreaAtvDesen(Double valAreaAtvDesen) {
		this.valAreaAtvDesen = valAreaAtvDesen;
	}

	public String getNomeSHP() {
		return nomeSHP;
	}

	public void setNomeSHP(String nomeSHP) {
		this.nomeSHP = nomeSHP;
	}

	public String getNomeSHX() {
		return nomeSHX;
	}

	public void setNomeSHX(String nomeSHX) {
		this.nomeSHX = nomeSHX;
	}

	public String getNomeDBF() {
		return nomeDBF;
	}

	public void setNomeDBF(String nomeDBF) {
		this.nomeDBF = nomeDBF;
	}
}
