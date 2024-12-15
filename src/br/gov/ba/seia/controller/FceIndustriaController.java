package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.DestinoResiduo;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceIndustria;
import br.gov.ba.seia.entity.FceIndustriaDestinoResiduo;
import br.gov.ba.seia.entity.FceIndustriaEmissaoAtmosferica;
import br.gov.ba.seia.entity.FceIndustriaOrigemEnergia;
import br.gov.ba.seia.entity.FceIndustriaResiduoCivil;
import br.gov.ba.seia.entity.FceIndustriaResiduoGerado;
import br.gov.ba.seia.entity.FceIndustriaSistemaTratamento;
import br.gov.ba.seia.entity.FceIndustriaSubstancia;
import br.gov.ba.seia.entity.FceIndustriaTipoApp;
import br.gov.ba.seia.entity.ModalidadeOutorga;
import br.gov.ba.seia.entity.Outorga;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaTipoCaptacao;
import br.gov.ba.seia.entity.Pergunta;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoApp;
import br.gov.ba.seia.entity.TipoCaptacao;
import br.gov.ba.seia.entity.TipoEmissaoAtmosferica;
import br.gov.ba.seia.entity.TipoOrigemEnergia;
import br.gov.ba.seia.entity.TipoResiduoConsCivil;
import br.gov.ba.seia.entity.TipoResiduoGerado;
import br.gov.ba.seia.entity.TipoSistemaTratamento;
import br.gov.ba.seia.entity.TipoSolicitacao;
import br.gov.ba.seia.entity.TipoSubstancia;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.FaseEmpreendimentoEnum;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.enumerator.PerguntaEnum;
import br.gov.ba.seia.enumerator.TipoCaptacaoEnum;
import br.gov.ba.seia.enumerator.TipoSolicitacaoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.FceNavegacaoInterface;
import br.gov.ba.seia.service.AsvSupressaoService;
import br.gov.ba.seia.service.DestinoResiduoService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.FceIndustriaDestinoResiduoService;
import br.gov.ba.seia.service.FceIndustriaEmissaoAtmosfericaService;
import br.gov.ba.seia.service.FceIndustriaOrigemEnergiaService;
import br.gov.ba.seia.service.FceIndustriaResiduoCivilService;
import br.gov.ba.seia.service.FceIndustriaResiduoGeradoService;
import br.gov.ba.seia.service.FceIndustriaService;
import br.gov.ba.seia.service.FceIndustriaSistemaTratamentoService;
import br.gov.ba.seia.service.FceIndustriaSubstanciaService;
import br.gov.ba.seia.service.FceIndustriaTipoAppService;
import br.gov.ba.seia.service.OutorgaLocalizacaoGeograficaService;
import br.gov.ba.seia.service.OutorgaService;
import br.gov.ba.seia.service.PerguntaRequerimentoService;
import br.gov.ba.seia.service.PerguntaService;
import br.gov.ba.seia.service.TipoAppService;
import br.gov.ba.seia.service.TipoCaptacaoService;
import br.gov.ba.seia.service.TipoEmissaoAtmosfericaService;
import br.gov.ba.seia.service.TipoOrigemEnergiaService;
import br.gov.ba.seia.service.TipoResiduoConsCivilService;
import br.gov.ba.seia.service.TipoResiduoGeradoService;
import br.gov.ba.seia.service.TipoSistemaTratamentoService;
import br.gov.ba.seia.service.TipoSubstanciaService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * 09/05/14
 * @author eduardo.fernandes
 */
@Named("fceIndustriaController")
@ViewScoped
public class FceIndustriaController extends FceController implements FceNavegacaoInterface {

	@EJB
	private PerguntaRequerimentoService perguntaRequerimentoService;

	@EJB
	private PerguntaService perguntaService;

	@EJB
	private OutorgaService outorgaService;

	@EJB
	private OutorgaLocalizacaoGeograficaService outorgaLocalizacaoGeograficaService;

	@EJB
	private FceIndustriaService fceIndustriaService;

	@EJB
	private TipoOrigemEnergiaService origemEnergiaService;
	@EJB
	private FceIndustriaOrigemEnergiaService fceIndustriaOrigemEnergiaService;

	@EJB
	private TipoSubstanciaService substanciaService;
	@EJB
	private FceIndustriaSubstanciaService fceIndustriaSubstanciaService;

	@EJB
	private TipoCaptacaoService tipoCaptacaoService;
	//COMENTADO PARA O TICKET "" DO RESULTADO DA HOMOLOGAÇÃO DE 11/06

	@EJB
	private TipoEmissaoAtmosfericaService emissaoAtmosfericaService;
	@EJB
	private FceIndustriaEmissaoAtmosfericaService fceIndustriaEmissaoAtmosfericaService;

	@EJB
	private TipoSistemaTratamentoService sistemaTratamentoService;
	@EJB
	private FceIndustriaSistemaTratamentoService fceIndustriaSistemaTratamentoService;

	@EJB
	private TipoResiduoConsCivilService residuoConsCivilService;
	@EJB
	private FceIndustriaResiduoCivilService fceIndustriaResiduoCivilService;

	@EJB
	private TipoResiduoGeradoService residuoGeradoService;
	@EJB
	private FceIndustriaResiduoGeradoService fceIndustriaResiduoGeradoService;

	@EJB
	private DestinoResiduoService destinoResiduoService;
	@EJB
	private FceIndustriaDestinoResiduoService fceIndustriaDestinoResiduoService;

	@EJB
	private TipoAppService tipoAppService;
	@EJB
	private FceIndustriaTipoAppService fceIndustriaTipoAppService;

	@EJB
	private EmpreendimentoService empreendimentoService;

	@EJB
	private AsvSupressaoService asvSupressaoService;

	// Geral
	private int activeTab;
	private static DocumentoObrigatorio DOC_INDUSTRIA = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_INDUSTRIA.getId());
	private FceIndustria fceIndustria;
	private EmpreendimentoRequerimento empreendimentoRequerimento;

	// Aba Empreendimento
	private List<TipoOrigemEnergia> listaOrigemEnergias, listaOrigemEnergiasEscolhidos;
	private String substanciaPesquisada;
	private TipoSubstancia tipoSubstancia;
	private FceIndustriaSubstancia fceIndustriaSubstancia;
	private List<TipoSubstancia> listaSubstancias, listaSubstanciasEscolhidos, listaSubstanciasFULL;
	private List<FceIndustriaSubstancia> listaFceIndustriaSubstancias;
	private List<TipoCaptacao> listaOrigemUsoAgua;//, listaOrigemUsoAguaEscolhidos;

	// Aba Efluentes
	private List<TipoSistemaTratamento> listaSistemaTratamentos, listaSistemaTratamentosEscolhidos;
	private List<TipoEmissaoAtmosferica> listaEmissaoAtmosfericas, listaEmissaoAtmosfericasEscolhidos;

	// Aba Resíduos
	private List<TipoResiduoGerado> listaResiduoGerados, listaResiduoGeradosEscolhidos;
	private List<TipoResiduoConsCivil> listaResiduoConsCivils, listaResiduoConsCivilsEscolhidos;
	private List<DestinoResiduo> listaDestinoResiduos, listaDestinoResiduosEscolhidos;

	// Aba Caracterização Ambiental
	private List<TipoApp> listaTipoApps, listaTipoAppsEscolhidos;

	//Pergunta do novo requerimento
	private PerguntaRequerimento perguntaNR_ABA4_1;
	private PerguntaRequerimento perguntaNR_A4P14;
	private PerguntaRequerimento perguntaA4_P14;
	private PerguntaRequerimento perguntaNR_A4_P141;
	private PerguntaRequerimento perguntaNR_A4_P142;
	private PerguntaRequerimento perguntaNR_A4_P1411;
	private PerguntaRequerimento perguntaNR_A4_P1512;
	private PerguntaRequerimento perguntaRequerimento;

	private List<PerguntaRequerimento> listaPerguntaRN;

	private boolean captacaoPrecipitacaoPluvial, captacaoConcessessionarioPublica, captacaoSuperficial, captacaoSubterranea;
	private List<OutorgaLocalizacaoGeografica> captacoesSubterraneas,captacoesSuperficiais, lancamentosEfluentes;

	private boolean alertaExibido1, alertaExibido2, alertaExibido3, alertaExibido4;

	private BigDecimal valorVolumeCaminhaoPipa;
	private Integer qtdTransporteCaminhaoPipa;

	/**
	 * Método chamado na abertura da tela FCE - Indústria
	 * @param fceIndustria
	 * @param listaPerguntaRN
	 * @param listaRespostaRN
	 * @author eduardo.fernandes
	 */
	@Override
	public void init(){
		activeTab = 0;
		verificarEdicao();
		if(!isFceSalvo()){
			iniciarFce(DOC_INDUSTRIA);
			fceIndustria = new FceIndustria(super.fce);
		}
		else {
			carregarFceIndustriaByFce();
		}
		carregarAba();
	}

	/**
	 * Método que anula todas as variáveis presentes na tela
	 * @author eduardo.fernandes
	 */
	@Override
	public void limpar(){
		super.limparFce();
		fceIndustria = null;
		listaOrigemEnergiasEscolhidos = null;
		listaSubstanciasEscolhidos = null;
		listaFceIndustriaSubstancias = null;
		//		listaOrigemUsoAguaEscolhidos = null;
		listaSistemaTratamentosEscolhidos = null;
		listaEmissaoAtmosfericasEscolhidos = null;
		listaResiduoGeradosEscolhidos = null;
		listaResiduoConsCivilsEscolhidos = null;
		listaDestinoResiduosEscolhidos = null;
		captacoesSuperficiais = null;
		captacoesSubterraneas = null;
		lancamentosEfluentes = null;
		captacaoSubterranea = false;
		captacaoConcessessionarioPublica = false;
		captacaoSuperficial = false;
	}

	/**
	 * Método que preenche as listas exibidas em cada aba
	 * @author eduardo.fernandes
	 */
	private void carregarListas(){
		carregarAbaEmpreendimento();
		carregarAbaElfuentes();
		carregarAbaResiduos();
		carregarAbaCaracterizacaoAmbiental();
	}

	/**
	 * Método que preenche as listas da Aba Dados do Empreendimento
	 * @author eduardo.fernandes
	 */
	private void carregarAbaEmpreendimento(){
		carregarListaOrigemEnergia();
		carregarListaSubstancias();
		carregarListaOrigemUsoDaAgua();
		if(isFceIndustriaSalvo()){
			buscarListasAbaEmpreendimento();
		}
	}

	/**
	 * Método que preenche as listas da Aba Efluentes e Emissões
	 * @author eduardo.fernandes
	 */
	private void carregarAbaElfuentes(){
		carregarListaSistemaTratamento();
		carregarListaEmissoesAtmosfericas();
		if(isFceIndustriaSalvo()){
			buscarListasAbaEfluentesEmissoes();
		}
	}

	/**
	 * Método que preenche as listas da Aba Resíduos
	 * @author eduardo.fernandes
	 */
	private void carregarAbaResiduos(){
		carregarListaResiduosGerados();
		carregarListaResiduosConsCivil();
		carregarListaDestinoResiduos();
		if(isFceIndustriaSalvo()){
			buscarListasAbaResiduos();
		}
	}

	/**
	 * Método que carrega a lista de {@link TipoCaptacao}
	 * @param listaOrigemUsoAgua
	 * @author eduardo.fernandes
	 */
	private void carregarListaOrigemUsoDaAgua(){
		listaOrigemUsoAgua = new ArrayList<TipoCaptacao>();
		try {
			listaOrigemUsoAgua = tipoCaptacaoService.listarTipoCaptacaoCompleto();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista de origem e uso água.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que carrega a lista de {@link DestinoResiduo}
	 * @param listaDestinoResiduos
	 * @author eduardo.fernandes
	 */
	private void carregarListaDestinoResiduos(){
		listaDestinoResiduos = new ArrayList<DestinoResiduo>();
		try {
			listaDestinoResiduos = destinoResiduoService.buscarListaDestinoResiduo();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista de destino dos resíduos.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que carrega a lista de {@link TipoEmissaoAtmosferica}
	 * @param listaEmissaoAtmosfericas
	 * @author eduardo.fernandes
	 */
	private void carregarListaEmissoesAtmosfericas(){
		listaEmissaoAtmosfericas = new ArrayList<TipoEmissaoAtmosferica>();
		try {
			listaEmissaoAtmosfericas = emissaoAtmosfericaService.buscarListaTipoEmissaoAtmosferica();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista de emissões atmosféricas.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que carrega a lista de {@link TipoOrigemEnergia}
	 * @param listaOrigemEnergias
	 * @author eduardo.fernandes
	 */
	private void carregarListaOrigemEnergia(){
		listaOrigemEnergias = new ArrayList<TipoOrigemEnergia>();
		try {
			listaOrigemEnergias = origemEnergiaService.buscarListaTipoOrigemEnergia();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista de origem da energia.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que carrega a lista de {@link TipoResiduoConsCivil}
	 * @param listaResiduoConsCivils
	 * @author eduardo.fernandes
	 */
	private void carregarListaResiduosConsCivil(){
		listaResiduoConsCivils = new ArrayList<TipoResiduoConsCivil>();
		try {
			listaResiduoConsCivils = residuoConsCivilService.buscarListaTipoResiduoConsCivil();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista de resíduos da construção civil.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que carrega a lista de {@link TipoResiduoGerado}
	 * @param listaResiduoGerados
	 * @author eduardo.fernandes
	 */
	private void carregarListaResiduosGerados(){
		listaResiduoGerados = new ArrayList<TipoResiduoGerado>();
		try {
			listaResiduoGerados = residuoGeradoService.buscarListaTipoResiduoGerado();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista de resíduos gerados.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que carrega a lista de {@link TipoSistemaTratamento}
	 * @param listaSistemaTratamentos
	 * @author eduardo.fernandes
	 */
	private void carregarListaSistemaTratamento(){
		listaSistemaTratamentos = new ArrayList<TipoSistemaTratamento>();
		try {
			listaSistemaTratamentos = sistemaTratamentoService.buscarListaTipoSistemaTratamento();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista de sistemas de tratamento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que carrega a lista de {@link TipoSubstancia}
	 * @param listaSubstancias
	 * @author eduardo.fernandes
	 */
	private void carregarListaSubstancias(){
		listaSubstancias = new ArrayList<TipoSubstancia>();
		try {
			listaSubstancias = substanciaService.buscarListaTipoSubstancia();
			armazenarListaSubstancia();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista de substâncias.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}


	/**
	 * Método que armazena a listaSubstancia completa para evitar diversas transações futuras com o BD
	 * @param listaSubstanciasFULL
	 * @author eduardo.fernandes
	 */
	private void armazenarListaSubstancia(){
		listaSubstanciasFULL = new ArrayList<TipoSubstancia>();
		listaSubstanciasFULL.addAll(listaSubstancias);
	}


	/**
	 * Método que preenche as listas da Aba Caracterização Ambiental
	 * @author eduardo.fernandes
	 */
	private void carregarAbaCaracterizacaoAmbiental(){
		if(Util.isNull(fceIndustria.getIndApp())){
			fceIndustria.setIndApp(existeAreaApp());
		}
		carregarListaTipoApp();
		if(isFceIndustriaSalvo()){
			buscarListasAbaCaracterizacaoAmbiental();
		}
	}

	private Boolean existeAreaApp(){
		Double area = 0.0;
		try {
			area = asvSupressaoService.buscarAppByIdeRequerimento(requerimento);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a área de APP.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		return !Util.isNullOuVazio(area);
	}

	private void buscarListasAbaCaracterizacaoAmbiental(){
		try {
			buscarListaTipoAppSalva();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a Aba Caracterização Ambiental.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void carregarListaTipoApp(){
		listaTipoApps = new ArrayList<TipoApp>();
		try {
			listaTipoApps = (List<TipoApp>) tipoAppService.listarTipoApp();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista de APPs.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que busca através do {@link FceIndustria} a lista de {@link FceIndustriaOrigemEnergia},
	 * caso ela exista no BD devemos percorrer-la e setar os objetos {@link OrigemEnergia} na listaOrigemEnergiasEscolhidos
	 * @param fceIndustria
	 * @param listaOrigemEnergiasEscolhidos
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	private void buscarListaTipoAppSalva() throws Exception{
		listaTipoAppsEscolhidos = new ArrayList<TipoApp>();
		List<FceIndustriaTipoApp> list = fceIndustriaTipoAppService.buscarFceIndustriaTipoAppByFceIndustria(fceIndustria);
		if(!Util.isNullOuVazio(list)){
			for(FceIndustriaTipoApp fceIndustriaTipoApp : list){
				listaTipoAppsEscolhidos.add(fceIndustriaTipoApp.getIdeTipoApp());
			}
		}
	}

	// Modo Edição
	/**
	 * Método que busca o {@link FceIndustria} associado aquele {@link Fce} salvo.
	 * @param fceIndustria
	 * @author eduardo.fernandes
	 */
	private void carregarFceIndustriaByFce(){
		try {
			fceIndustria = fceIndustriaService.buscarFceIndustriaByIdeFce(super.fce);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que busca as listas salvas e associadas ao {@link FceIndustria} da Aba Dados do Empreendimento
	 * @author eduardo.fernandes
	 */
	private void buscarListasAbaEmpreendimento(){
		try {
			buscarListaOrigemEnergiaSalva();
			buscarListaSubstanciasSalva();
			//COMENTADO PARA O TICKET "" DO RESULTADO DA HOMOLOGAÇÃO DE 11/06
		
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a Aba Dados do Empreendimento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que busca através do {@link FceIndustria} a lista de {@link FceIndustriaOrigemEnergia},
	 * caso ela exista no BD devemos percorrer-la e setar os objetos {@link OrigemEnergia} na listaOrigemEnergiasEscolhidos
	 * @param fceIndustria
	 * @param listaOrigemEnergiasEscolhidos
	 * @author eduardo.fernandes
	 */
	private void buscarListaOrigemEnergiaSalva() {
		listaOrigemEnergiasEscolhidos = new ArrayList<TipoOrigemEnergia>();
		List<FceIndustriaOrigemEnergia> list = fceIndustriaOrigemEnergiaService.buscarFceIndustriaOrigemEnergiaByFceIndustria(fceIndustria);
		if(!Util.isNullOuVazio(list)){
			for(FceIndustriaOrigemEnergia fceIndustriaOrigemEnergia : list){
				listaOrigemEnergiasEscolhidos.add(fceIndustriaOrigemEnergia.getIdeTipoOrigemEnergia());
			}
		}
	}

	/**
	 * Método que busca através do {@link FceIndustria} a lista de {@link FceIndustriaSubstancia}
	 * @param fceIndustria
	 * @param listaFceIndustriaSubstancias
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	private void buscarListaSubstanciasSalva() throws Exception{
		listaFceIndustriaSubstancias = fceIndustriaSubstanciaService.buscarListaFceIndustriaSubstanciaByIdeFceIndustria(fceIndustria);
		if(!Util.isNullOuVazio(listaFceIndustriaSubstancias)){
			removerSubstanciasEscolhidasDaLista();
		}
	}


	/**
	 * Método que busca as listas salvas e associadas ao {@link FceIndustria} da Aba Efluentes e Emissões
	 * @author eduardo.fernandes
	 */
	private void buscarListasAbaEfluentesEmissoes(){
		try {
			buscarListaSistemaTratamentoSalva();
			buscarListaEmissaoAtmosfericaSalva();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a Aba Efluentes e Emissões.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que busca através do {@link FceIndustria} a lista de {@link FceIndustriaSistemaTratamento},
	 * caso ela exista no BD devemos percorrer-la e setar os objetos {@link TipoSistemaTratamento} na listaSistemaTratamentosEscolhidos
	 * @param fceIndustria
	 * @param listaSistemaTratamentosEscolhidos
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	private void buscarListaSistemaTratamentoSalva() {
		listaSistemaTratamentosEscolhidos = new ArrayList<TipoSistemaTratamento>();
		List<FceIndustriaSistemaTratamento> list =  fceIndustriaSistemaTratamentoService.buscarFceIndustriaideSistemaTratamentoByFceIndustria(fceIndustria);
		if(!Util.isNullOuVazio(list)){
			for(FceIndustriaSistemaTratamento fceIndustriaSistemaTratamento : list){
				listaSistemaTratamentosEscolhidos.add(fceIndustriaSistemaTratamento.getIdeTipoSistemaTratamento());
			}
		}
	}

	/**
	 * Método que busca através do {@link FceIndustria} a lista de {@link FceIndustriaEmissaoAtmosferica},
	 * caso ela exista no BD devemos percorrer-la e setar os objetos {@link TipoEmissaoAtmosferica} na listaEmissaoAtmosfericasEscolhidos
	 * @param fceIndustria
	 * @param listaEmissaoAtmosfericasEscolhidos
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	private void buscarListaEmissaoAtmosfericaSalva() throws Exception{
		listaEmissaoAtmosfericasEscolhidos = new ArrayList<TipoEmissaoAtmosferica>();
		List<FceIndustriaEmissaoAtmosferica> list = fceIndustriaEmissaoAtmosfericaService.buscarFceIndustriaEmissaoAtmosfericaByFceIndustria(fceIndustria);
		if(!Util.isNullOuVazio(list)){
			for(FceIndustriaEmissaoAtmosferica fceIndustriaEmissaoAtmosferica : list){
				listaEmissaoAtmosfericasEscolhidos.add(fceIndustriaEmissaoAtmosferica.getIdeTipoEmissaoAtmosferica());
			}
		}
	}

	/**
	 * Método que busca as listas salvas e associadas ao {@link FceIndustria} da Aba Resíduos
	 * @author eduardo.fernandes
	 */
	private void buscarListasAbaResiduos(){
		try {
			buscarListaDestinoResiduoSalva();
			buscarListaResiduoGeradoSalva();
			buscarListaResiduoConsCivilSalva();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a Aba Resíduos Sólidos.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que busca através do {@link FceIndustria} a lista de {@link FceIndustriaResiduoCivil},
	 * caso ela exista no BD devemos percorrer-la e setar os objetos {@link TipoResiduoConsCivil} na listaResiduoConsCivilsEscolhidos
	 * @param fceIndustria
	 * @param listaResiduoConsCivilsEscolhidos
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	private void buscarListaResiduoConsCivilSalva(){
		listaResiduoConsCivilsEscolhidos = new ArrayList<TipoResiduoConsCivil>();
		List<FceIndustriaResiduoCivil> list = fceIndustriaResiduoCivilService.buscarFceIndustriaResiduoCivilByFceIndustria(fceIndustria);
		if(!Util.isNullOuVazio(list)){
			for(FceIndustriaResiduoCivil fceIndustriaResiduoCivil : list){
				listaResiduoConsCivilsEscolhidos.add(fceIndustriaResiduoCivil.getIdeTipoResiduoConsCivil());
			}
		}
	}

	/**
	 * Método que busca através do {@link FceIndustria} a lista de {@link FceIndustriaResiduoGerado},
	 * caso ela exista no BD devemos percorrer-la e setar os objetos {@link TipoResiduoGerado} na listaResiduoGeradosEscolhidos
	 * @param fceIndustria
	 * @param listaResiduoGeradosEscolhidos
	 * @author eduardo.fernandes
	 */
	private void buscarListaResiduoGeradoSalva(){
		listaResiduoGeradosEscolhidos = new ArrayList<TipoResiduoGerado>();
		List<FceIndustriaResiduoGerado> list = fceIndustriaResiduoGeradoService.buscarFceIndustriaResiduoGeradoByFceIndustria(fceIndustria);
		if(!Util.isNullOuVazio(list)){
			for(FceIndustriaResiduoGerado fceIndustriaResiduoGerado : list){
				listaResiduoGeradosEscolhidos.add(fceIndustriaResiduoGerado.getIdeTipoResiduoGerado());
			}
		}
	}

	/**
	 * Método que busca através do {@link FceIndustria} a lista de {@link FceIndustriaDestinoResiduo},
	 * caso ela exista no BD devemos percorrer-la e setar os objetos {@link DestinoResiduo} na listaDestinoResiduosEscolhidos
	 * @param fceIndustria
	 * @param listaDestinoResiduosEscolhidos
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	private void buscarListaDestinoResiduoSalva() throws Exception{
		listaDestinoResiduosEscolhidos = new ArrayList<DestinoResiduo>();
		List<FceIndustriaDestinoResiduo> list = fceIndustriaDestinoResiduoService.buscarFceIndustriaDestinoResiduoByFceIndustria(fceIndustria);
		if(!Util.isNullOuVazio(list)){
			for(FceIndustriaDestinoResiduo fceIndustriaDestinoResiduo : list){
				listaDestinoResiduosEscolhidos.add(fceIndustriaDestinoResiduo.getIdeDestinoResiduo());
			}
		}
	}
	// FIM Modo Edição

	// Métodos do USUÁRIO
	/**
	 * Método que seta a Aba ativa de acordo com o clique do usuário na tela
	 * @param activeTab
	 * @param event
	 * @author eduardo.fernandes
	 */
	@Override
	public void controlarAbas(TabChangeEvent event) {
		if ("abaDadosRequerimento".equals(event.getTab().getId())) {
			activeTab = 0;
		} else if ("abaEfluentes".equals(event.getTab().getId())) {
			activeTab = 1;
		} else if ("abaResiduos".equals(event.getTab().getId())) {
			activeTab = 2;
		} else if ("abaCaracterizacaoAmbiental".equals(event.getTab().getId())) {
			activeTab = 3;
		}
	}

	public void avancarAbaDadosEmpreendimento(){
		if(validarAbaDadosRequerimento()){
			try {
				salvarAbaDadosDoEmpreendimento();
				avancarAba();
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " a Aba Dados do Empreendimento.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}
	
	public void avancarAbaEfluentes(){
		if(validarAbaEfluentes()){
			try {
				salvarAbaEfluentesEmissoes();
				avancarAba();
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " a Aba Efluentes e Emissões.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}
	
	public void avancarAbaResiduos(){
		if(validarAbaResiduos()){
			try {
				salvarAbaResiduos();
				avancarAba();
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " a Aba Resíduos Sólidos.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}
	
	/**
	 * Método que permite, ou não, avançar uma aba através do clique do botão "Avançar"
	 * @param activeTab
	 * @author eduardo.fernandes
	 */
	@Override
	public void avancarAba() {
		activeTab++;
	}

	/**
	 * Método que permite voltar uma aba através do clique do botão "Voltar"
	 * @param activeTab
	 * @author eduardo.fernandes
	 */
	@Override
	public void voltarAba() {
		activeTab--;
	}


	/**
	 * Método que permite pesquisar, através do que foi digitado pelo usuário, os objetos {@link TipoSubstancia}
	 * @param listaSubstancias
	 * @author eduardo.fernandes
	 */
	public void pesquisarSubstancia(){
		atualizarListaSubstancia();
		List<TipoSubstancia> listaTemp = new ArrayList<TipoSubstancia>();
		listaTemp.addAll(listaSubstancias);
		limparListaSubstancia();
		for(TipoSubstancia temp : listaTemp){
			if(temp.getDscSubstancia().toLowerCase().indexOf(substanciaPesquisada.toLowerCase()) != -1){
				listaSubstancias.add(temp);
			}
		}
	}

	/**
	 * Método que limpa e preenche a listaSubstancias, para depois remover os objetos {@link TipoSubstancia} que já foram escolhidas pelo usuário
	 * @param listaSubstancias
	 * @param listaSubstanciasFULL
	 * @author eduardo.fernandes
	 */
	private void atualizarListaSubstancia(){
		limparListaSubstancia();
		listaSubstancias.addAll(listaSubstanciasFULL);
		removerSubstanciasEscolhidasDaLista();
	}

	/**
	 * Método que remove os objetos {@link TipoSubstancia} da listaSubstancias quando eles estiverm adicionados à listaFceIndustriaSubstancias
	 * @param listaSubstancias
	 * @param listaFceIndustriaSubstancias
	 * @author eduardo.fernandes
	 */
	private void removerSubstanciasEscolhidasDaLista(){
		if(isExisteSubstancia()){
			List<TipoSubstancia> listaTemp = new ArrayList<TipoSubstancia>();
			listaTemp.addAll(listaSubstancias);
			for(FceIndustriaSubstancia industriaSubstancia : listaFceIndustriaSubstancias){
				if(listaTemp.contains(industriaSubstancia.getIdeTipoSubstancia())){
					listaSubstancias.remove(industriaSubstancia.getIdeTipoSubstancia());
				}
			}
		}
	}

	/**
	 * Método acionado quando o usuário clicar em "Adicionar Substância"
	 * @param substanciaPesquisada
	 * @see criarFceIndustriaSubstancia();
	 * @see criarFceIndustriaSubstancia();
	 * @author eduardo.fernandes
	 */
	public void adicionarSubstancia(){
		criarFceIndustriaSubstancia();
		substanciaPesquisada = null;
		atualizarListaSubstancia();
	}

	/**
	 * Método acionado quando o usuário clica em "Confirmar Substância" e desabilita o campo "Média de Armazenamento Mensal (t/mês)"
	 * @param fceIndustriaSubstancia
	 * @author eduardo.fernandes
	 */
	public void confirmarSubstancia(){
		if(validarSubstancia()){
			fceIndustriaSubstancia.setConfirmado(true);
			if(fceIndustriaSubstancia.isEdicao()){
				super.exibirMensagem002();
			}
		}
	}


	/**
	 * Método acionado quando o usuário clica em "Editar Substância" e habilita o campo "Média de Armazenamento Mensal (t/mês)"
	 * @author eduardo.fernandes
	 */
	public void editarSubstancia(){
		fceIndustriaSubstancia.setConfirmado(false);
		fceIndustriaSubstancia.setEdicao(true);
	}

	/**
	 * Método acionado quando o usuário clica em "Excluir Substância", remove da listaFceIndustriaSubstancias o {@link FceIndustriaSubstancia} escolhido
	 * e o adiciona o {@link TipoSubstancia} na listaSubstancias
	 * @param listaFceIndustriaSubstancias
	 * @param fceIndustriaSubstancia
	 * @author eduardo.fernandes
	 */
	public void excluirSubstancia(){
		listaFceIndustriaSubstancias.remove(fceIndustriaSubstancia);
		super.exibirMensagem005();
		atualizarListaSubstancia();
	}

	/**
	 * Método para centralizar as ações de criação do {@link FceIndustriaSubstancia}
	 * @see prepararFceIndustriaSubstancia();
	 * @see adicionarFceIndustriaSubstancia();
	 * @see limparFceIndustriaSubstancia();
	 * @author eduardo.fernandes
	 */
	private void criarFceIndustriaSubstancia(){
		prepararFceIndustriaSubstancia();
		adicionarFceIndustriaSubstancia();
		limparFceIndustriaSubstancia();
	}


	/**
	 * Criar o {@link FceIndustriaSubstancia} com o ide do {@link TipoSubstancia} escolhido
	 * @param fceIndustriaSubstancia
	 * @param tipoSubstancia
	 * @author eduardo.fernandes
	 */
	private void prepararFceIndustriaSubstancia(){
		fceIndustriaSubstancia = new FceIndustriaSubstancia(fceIndustria, tipoSubstancia);
		fceIndustriaSubstancia.setIdeTipoSubstancia(tipoSubstancia);
	}

	/**
	 * Adicionar o {@link FceIndustriaSubstancia} na listaFceIndustriaSubstancias
	 * @author eduardo.fernandes
	 */
	private void adicionarFceIndustriaSubstancia(){
		if(!isExisteSubstancia()){
			listaFceIndustriaSubstancias = new ArrayList<FceIndustriaSubstancia>();
		}
		listaFceIndustriaSubstancias.add(fceIndustriaSubstancia);
	}

	/**
	 * Anular o {@link FceIndustriaSubstancia} criado
	 * @author eduardo.fernandes
	 */
	private void limparFceIndustriaSubstancia(){
		fceIndustriaSubstancia = null;
	}

	/**
	 * Método que limpa a listaSubstancias
	 * @param listaSubstancias
	 * @author eduardo.fernandes
	 */
	private void limparListaSubstancia(){
		if(!Util.isNullOuVazio(listaSubstancias)){
			listaSubstancias.clear();
		}
	}

	/**
	 * Método que limpa a lista de Apps quando o usuário diz que não possui APP
	 * @author eduardo.fernandes
	 * @param event
	 */
	public void changeCaracterizacaoAmbiental(ValueChangeEvent event){
		if(event.getNewValue().equals(false)){
			if(!Util.isNullOuVazio(listaTipoAppsEscolhidos)){
				listaTipoAppsEscolhidos.clear();
			}
		}
	}

	
	/**
	 * Método para finalizar o {@link FceIndustria}, e se ele for preenchido corretamente, fechar a tela. Do contrário, exibir as mensagens de validação
	 * @author eduardo.fernandes
	 */
	@Override
	public void finalizar(){
		if(validarAba()){
			try {
				fceIndustriaService.finalizar(this);
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o FCE - Indústria.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}
	// FIM Métodos do USUÁRIO

	public void prepararParaFinalizar() throws Exception{
		super.concluirFce();
		salvarAbaDadosDoEmpreendimento();
		salvarAbaEfluentesEmissoes();
		salvarAbaResiduos();
		salvarAbaCaracterizacaoAmbiental();
		RequestContext.getCurrentInstance().execute("industria.hide()");
		super.exibirMensagem001();
		if(!isAlgumOutrosAdicionados()){
			RequestContext.getCurrentInstance().execute("rel_industria.show()");
		}
		else {
			StringBuilder feminino =  new StringBuilder("a");
			StringBuilder masculino =  new StringBuilder("o");
			if(existeOutrosOrigemEnergia()){
				StringBuilder energia =  new StringBuilder("origem da energia");
				JsfUtil.addWarnMessage(mensagem(energia, feminino).toString());
			}
			if(existeOutrosEfluentesLiquidos()){
				StringBuilder tratamento =  new StringBuilder("sistema de tratamento");
				JsfUtil.addWarnMessage(mensagem(tratamento, masculino).toString());
			}
			if(existeOutrasEmissoesAtmosfericas()){
				StringBuilder atmosferica =  new StringBuilder("emissão atmosférica");
				JsfUtil.addWarnMessage(mensagem(atmosferica, feminino).toString());
			}
			if(existeOutrosDestinoResiduos()){
				StringBuilder destino =  new StringBuilder("destino dos resíduos");
				JsfUtil.addWarnMessage(mensagem(destino, masculino).toString());
			}
		}
		this.limpar();
	}
	
	// Métodos de SALVAR
	/**
	 * Método que salva o {@link FceIndustria} com o ide do {@link Fce} previamente salvo
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	private void salvarFceIndustria() throws Exception{
		super.salvarFce();
		fceIndustriaService.salvarFceIndustria(fceIndustria);
	}

	/**
	 * Método para salvar a Aba Dados do Empreendimento
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	private void salvarAbaDadosDoEmpreendimento() throws Exception{
		salvarFceIndustria();
		if(isFceIndustriaSalvo()){
			salvarFceIndustriaOrigemEnergia();
			salvarFceIndustriaSubstancia();

		}
	}

	/**
	 * Método que exclui e depois salva os {@link FceIndustriaOrigemEnergia}
	 * @param isEdicao()
	 * @param fceIndustria
	 * @param listaOrigemEnergiasEscolhidos
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	private void salvarFceIndustriaOrigemEnergia() throws Exception{
		if(isFceSalvo()){
			fceIndustriaOrigemEnergiaService.excluirAssociativaByIdeFceIndustria(fceIndustria);
		}
		fceIndustriaOrigemEnergiaService.prepararAndSalvarFceIndustriaOrigemEnergia(fceIndustria, listaOrigemEnergiasEscolhidos);
	}

	/**
	 * Método que exclui e depois salva os {@link FceIndustriaOrigemEnergia}
	 * @param isEdicao()
	 * @param fceIndustria
	 * @param listaOrigemEnergiasEscolhidos
	 * @author eduardo.fernandes
	 */
	private void salvarFceIndustriaTipoApp(){
		if(isFceIndustriaComApp()){
			if(isFceSalvo()){
				fceIndustriaTipoAppService.excluirAssociativaByIdeFceIndustria(fceIndustria);
			}
			fceIndustriaTipoAppService.prepararAndSalvarFceIndustriaOrigemEnergia(fceIndustria, listaTipoAppsEscolhidos);
		}
	}

	/**
	 * Método que exclui e depois salva os {@link FceIndustriaSubstancia}
	 * @param isEdicao()
	 * @param fceIndustria
	 * @param listaFceIndustriaSubstancias
	 * @author eduardo.fernandes
	 */
	private void salvarFceIndustriaSubstancia(){
		if(isFceSalvo()){
			fceIndustriaSubstanciaService.excluirAssociativaByIdeFceIndustria(fceIndustria);
		}
		fceIndustriaSubstanciaService.salvarListaFceIndustriaSubstacia(fceIndustria, listaFceIndustriaSubstancias);
	}


	/**
	 * Método para salvar a Aba Efluentes e Emissões
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	private void salvarAbaEfluentesEmissoes() throws Exception{
		salvarFceIndustria();
		if(isFceIndustriaSalvo()){
			salvarFceIndustriaSistemaTratamento();
			salvarFceIndustriaEmissaoAtmosferica();
		}
	}

	/**
	 * Método que exclui e depois salva os {@link FceIndustriaSistemaTratamento}
	 * @param isEdicao()
	 * @param fceIndustria
	 * @param listaSistemaTratamentosEscolhidos
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	private void salvarFceIndustriaSistemaTratamento() throws Exception{
		if(isFceSalvo()){
			fceIndustriaSistemaTratamentoService.excluirAssociativaByIdeFceIndustria(fceIndustria);
		}
		fceIndustriaSistemaTratamentoService.prepararAndSalvarFceIndustriaSistemaTratamento(fceIndustria, listaSistemaTratamentosEscolhidos);
	}

	/**
	 * Método que exclui e depois salva os {@link FceIndustriaEmissaoAtmosferica}
	 * @param isEdicao()
	 * @param fceIndustria
	 * @param listaEmissaoAtmosfericasEscolhidos
	 * @author eduardo.fernandes
	 */
	private void salvarFceIndustriaEmissaoAtmosferica(){
		if(isFceSalvo()){
			fceIndustriaEmissaoAtmosfericaService.excluirAssociativaByIdeFceIndustria(fceIndustria);
		}
		fceIndustriaEmissaoAtmosfericaService.prepararAndSalvarFceIndustriaEmissaoAtmosferica(fceIndustria, listaEmissaoAtmosfericasEscolhidos);
	}

	/**
	 * Método para salvar a Aba Resíduos
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	private void salvarAbaResiduos() throws Exception{
		if(!isFceIndustriaSalvo()){
			salvarFceIndustria();
		}
		salvarFceIndustriaResiduoGerado();
		if(isLIouLA()){
			salvarFceIndustriaResiduoCivil();
		}
		salvarFceIndustriaDestinoResiduo();
	}

	/**
	 * Método que exclui e depois salva os {@link FceIndustriaResiduoGerado}
	 * @param isEdicao()
	 * @param fceIndustria
	 * @param listaResiduoGeradosEscolhidos
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	private void salvarFceIndustriaResiduoGerado() {
		if(isFceSalvo()){
			fceIndustriaResiduoGeradoService.excluirAssociativaByIdeFceIndustria(fceIndustria);
		}
		fceIndustriaResiduoGeradoService.prepararAndSalvarFceIndustriaResiduoGerado(fceIndustria, listaResiduoGeradosEscolhidos);
	}

	/**
	 * Método que exclui e depois salva os {@link FceIndustriaResiduoCivil}
	 * @param isEdicao()
	 * @param fceIndustria
	 * @param listaResiduoConsCivilsEscolhidos
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	private void salvarFceIndustriaResiduoCivil() {
		if(isFceSalvo()){
			fceIndustriaResiduoCivilService.excluirAssociativaByIdeFceIndustria(fceIndustria);
		}
		fceIndustriaResiduoCivilService.prepararAndSalvarFceIndustriaResiduoCivil(fceIndustria, listaResiduoConsCivilsEscolhidos);
	}

	/**
	 * Método que exclui e depois salva os {@link FceIndustriaDestinoResiduo}
	 * @param isEdicao()
	 * @param fceIndustria
	 * @param listaDestinoResiduosEscolhidos
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	private void salvarFceIndustriaDestinoResiduo() {
		if(isFceSalvo()){
			fceIndustriaDestinoResiduoService.excluirAssociativaByIdeFceIndustria(fceIndustria);
		}
		fceIndustriaDestinoResiduoService.prepararAndSalvarFceIndustriaDestinoResiduo(fceIndustria, listaDestinoResiduosEscolhidos);
	}

	private void salvarAbaCaracterizacaoAmbiental() throws Exception{
		salvarFceIndustria();
		salvarFceIndustriaTipoApp();
	}
	// FIM Métodos de SALVAR

	/**
	 * Mètodo para ser exibir uma String quando se finalizar o {@link FceIndustria} e houver a presença de "Outros(as)" em alguma das tabelas associativa
	 * @param tipo
	 * @param genero
	 * @return String
	 * @author eduardo.fernandes
	 */
	private String mensagem(StringBuilder tipo, StringBuilder genero){
		return "O FCE não será finalizado até que o cadastro d"+genero+" "+tipo+" seja realizado. Favor aguardar resposta do atendimento, confirmando o cadastro d"+genero+" "+tipo+". Depois da confirmação, volte a tela de Enviar Documentação Obrigatória e edite o FCE - Indústria selecionando "+genero+" "+tipo+" cadastrad"+genero+".";
	}

	// Métodos de Validacao

	/**
	 * Método que verifica se os {@link FceIndustriaSubstancia} estão todos confirmados
	 * @return boolean
	 * @author eduardo.fernandes
	 */
	private boolean verificarConfirmadoNaLista(){
		boolean valido = true;
		for(FceIndustriaSubstancia fceIndustriaSubstanciaObj : listaFceIndustriaSubstancias){
			if(!fceIndustriaSubstanciaObj.isConfirmado()){
				JsfUtil.addErrorMessage(Util.getString("msg_generica_necessario_confirmar_inf_0040")+" a substância.");
				valido = false;
				break;
			}
		}
		return valido;
	}

	/**
	 * Método que verifica se a Área Construída é maior que a Área Total
	 * @return boolean
	 * @author eduardo.fernandes
	 */
	private boolean isAreaConstruidaMaiorQueTotal(){
		if(fceIndustria.getNumAreaConstruida().compareTo(fceIndustria.getNumAreaTotal()) > 0){
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Método que verifica se a Área a ser Construída é maior que a Área Total
	 * @return boolean
	 * @author eduardo.fernandes
	 */
	private boolean isAreaToContruirMaiorQueTotal(){
		if(fceIndustria.getNumAreaSerConstruida().compareTo(fceIndustria.getNumAreaTotal()) > 0){
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * Método que verifica se o somatório da Área Construída e Área a ser Construída é maior que a Área Total
	 * @return boolean
	 * @author eduardo.fernandes
	 */
	private boolean somatorioDasAreasValido(){
		if(fceIndustria.getNumAreaConstruida().add(fceIndustria.getNumAreaSerConstruida()).compareTo(fceIndustria.getNumAreaTotal()) > 0){
			return false;
		}
		else {
			return true;
		}
	}

	/**
	 * Mètodo que verifica as RNs da aba Dados do Empreendimento
	 * @return boolean
	 * @author eduardo.fernandes
	 */
	private boolean validarAbaDadosRequerimento(){
		boolean valido = true;

		if(Util.isNullOuVazio(fceIndustria.getNumAreaTotal())){
			JsfUtil.addErrorMessage("A área total " + Util.getString("msg_generica_null_ou_vazio"));
			valido = false;
		}
		if(valido){
			if(isAreaConstruidaMaiorQueTotal()){
				JsfUtil.addErrorMessage("A área construída não pode ser maior que a área total.");
				valido = false;
			}
			if(isAreaToContruirMaiorQueTotal()){
				JsfUtil.addErrorMessage("A área a ser construída não pode ser maior que a área total.");
				valido = false;
			}
			if(valido && !somatorioDasAreasValido()){
				JsfUtil.addErrorMessage(Util.getString("fce_industria_inf0048"));
				valido = false;
			}
		}
		if(Util.isNullOuVazio(listaOrigemEnergiasEscolhidos)){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " uma origem de energia.");
			valido = false;
		}
		if(!isExisteSubstancia()){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " uma substância.");
			valido = false;
		} else {
			if(!verificarConfirmadoNaLista()){
				valido = false;
			}
		}
	
		if(!valido){
			activeTab = 0;
		}
		return valido;
	}

	/**
	 * Mètodo que verifica as RNs da aba Emissões e Efluentes
	 * @return boolean
	 * @author eduardo.fernandes
	 */
	private boolean validarAbaEfluentes(){
		boolean valido = true;
		if(Util.isNullOuVazio(listaSistemaTratamentosEscolhidos)){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " um sistema de tratamento.");
			valido = false;
		}

		if(Util.isNullOuVazio(listaEmissaoAtmosfericasEscolhidos)){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " uma emissão atmosférica.");
			valido = false;
		}
		if(!valido){
			activeTab = 1;
		}
		return valido;
	}

	/**
	 * Mètodo que verifica as RNs da aba Resíduos
	 * @return boolean
	 * @author eduardo.fernandes
	 */
	private boolean validarAbaResiduos(){
		boolean valido = true;
		if(Util.isNullOuVazio(listaResiduoGeradosEscolhidos)){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " um resíduo gerado ou a ser gerado.");
			valido = false;
		}
		if(isLIouLA() && Util.isNullOuVazio(listaResiduoConsCivilsEscolhidos)){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " um resíduo da construção civil.");
			valido = false;
		}
		if(Util.isNullOuVazio(listaDestinoResiduosEscolhidos)){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " um destino de resíduo.");
			valido = false;
		}
		if(!valido){
			activeTab = 2;
		}
		return valido;
	}

	private boolean validarAbaCaracterizacaoAmbiental(){
		boolean valido = true;
		if(Util.isNullOuVazio(fceIndustria.getIndApp())){
			JsfUtil.addErrorMessage("O empreendimento possui ÁREAS DE PRESERVAÇÃO PERMANENTE – APP " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		} else if(fceIndustria.getIndApp()){
			if(Util.isNullOuVazio(listaTipoAppsEscolhidos)){
				JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " um APP.");
				valido = false;
			}
		}
		if(!valido){
			activeTab = 3;
		}
		else {
			try {
				salvarAbaCaracterizacaoAmbiental();
			} catch (Exception e) {
				valido = false;
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " a Aba Caracterização Ambiental.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		return valido;
	}

	/**
	 * Mètodo que verifica se todas as abas são válidas
	 * @return boolean
	 * @author eduardo.fernandes
	 */
	@Override
	public boolean validarAba(){
		return validarAbaDadosRequerimento() && validarAbaEfluentes() && validarAbaResiduos() && validarAbaCaracterizacaoAmbiental();
	}

	/**
	 * Método que verifica se aquele {@link FceIndustriaSubstancia} é válido
	 * @param fceIndustriaSubstancia
	 * @return boolean
	 * @author eduardo.fernandes
	 */
	private boolean validarSubstancia(){
		if(Util.isNullOuVazio(fceIndustriaSubstancia.getNumMediaArmazenamento())){
			JsfUtil.addErrorMessage("A média de armazenamento mensal(t/mês) " + Util.getString("msg_generica_null_ou_vazio"));
			return false;
		} else {
			return true;
		}
	}
	// FIM Métodos de Validacao
	/**
	 * Método acionado pela tela quando o usuário escolhe os itens das tabelas associativas.
	 * Caso um deles seja "Outros(as)" deve-se exibir um alerta para o usuário.
	 * @param i
	 * @param alertaExibido1
	 * @param alertaExibido2
	 * @param alertaExibido3
	 * @param alertaExibido4
	 * @author eduardo.fernandes
	 */
	public void exibirAlerta(int i){
		boolean exibe = false;
		switch (i) {
		case 1:
			if(!alertaExibido1 && existeOutrosOrigemEnergia()){
				alertaExibido1 = exibe = true;
			}
			break;
		case 2:
			if(!alertaExibido2 && existeOutrosEfluentesLiquidos()){
				alertaExibido2 = exibe = true;
			}
			break;
		case 3:
			if(!alertaExibido3 && existeOutrasEmissoesAtmosfericas()){
				alertaExibido3 = exibe = true;
			}
			break;
		case 4:
			if(!alertaExibido4 && existeOutrosDestinoResiduos()){
				alertaExibido4 = exibe = true;
			}
			break;
		}
		if(exibe){
			super.exibirInformacao001();
		}
	}

	// Redendered's, Flags, Controladores em geral
	/**
	 * Método que verifica a existência do {@link FceIndustria} no banco
	 * @param fceIndustria
	 * @return boolean
	 * @author eduardo.fernandes
	 */
	private boolean isFceIndustriaSalvo(){
		return !Util.isNullOuVazio(fceIndustria) && !Util.isNullOuVazio(fceIndustria.getIdeFceIndustria());
	}

	/**
	 * Método que verifica se o {@link TipoOrigemEnergia} é igual a "Outros"
	 * @param listaOrigemEnergiasEscolhidos
	 * @return boolean
	 * @author eduardo.fernandes
	 */
	private boolean existeOutrosOrigemEnergia(){
		for(TipoOrigemEnergia origemEnergia : listaOrigemEnergiasEscolhidos){
			if(origemEnergia.isOutros()){
				return true;
			}
		}
		return false;
	}

	/**
	 * Método que verifica se o {@link TipoSistemaTratamento} é igual a "Outros"
	 * @param listaSistemaTratamentosEscolhidos
	 * @return boolean
	 * @author eduardo.fernandes
	 */
	private boolean existeOutrosEfluentesLiquidos(){
		for(TipoSistemaTratamento sistemaTratamento : listaSistemaTratamentosEscolhidos){
			if(sistemaTratamento.isOutros()){
				return true;
			}
		}
		return false;
	}

	/**
	 * Método que verifica se o {@link TipoEmissaoAtmosferica} é igual a "Outros"
	 * @param listaEmissaoAtmosfericasEscolhidos
	 * @return boolean
	 * @author eduardo.fernandes
	 */
	private boolean existeOutrasEmissoesAtmosfericas(){
		for(TipoEmissaoAtmosferica emissaoAtmosferica : listaEmissaoAtmosfericasEscolhidos){
			if(emissaoAtmosferica.isOutros()){
				return true;
			}
		}
		return false;
	}

	/**
	 * Método que verifica se o {@link DestinoResiduo} é igual a "Outros"
	 * @param listaDestinoResiduosEscolhidos
	 * @return boolean
	 * @author eduardo.fernandes
	 */
	private boolean existeOutrosDestinoResiduos(){
		for(DestinoResiduo destinoResiduo : listaDestinoResiduosEscolhidos){
			if(destinoResiduo.isOutros()){
				return true;
			}
		}
		return false;
	}

	/**
	 * Método que verifica se existe algum "Outros(as)" adicionado em alguma lista
	 * @return
	 * @author eduardo.fernandes
	 */
	private boolean isAlgumOutrosAdicionados(){
		if(existeOutrosOrigemEnergia() || existeOutrosEfluentesLiquidos() || existeOutrasEmissoesAtmosfericas() || existeOutrosDestinoResiduos()){
			return true;
		}
		else {
			return false;
		}
	}

	private boolean isFceIndustriaComApp(){
		return !Util.isNullOuVazio(fceIndustria.getIndApp()) && fceIndustria.getIndApp();
	}

	/*
	 * metodos para carregar as perguntas e resposta das perguntas
	 */
	public PerguntaRequerimento perguntaByCodPerguntaEnum(String codPergunta) throws Exception {
		Pergunta pergunta = perguntaService.carregarPerguntabyCodPergunta(codPergunta);
		return new PerguntaRequerimento(pergunta);
	}

	/**
	 * metodo para carregar as perguntas do novo requerimento
	 */
	public void carregarListaPerguntaNovoRequerimento(){
		try {
			perguntaNR_ABA4_1 = perguntaByCodPerguntaEnum(PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA4_1.getCod());
			perguntaNR_A4P14 = perguntaByCodPerguntaEnum(PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA4_14.getCod());
			perguntaNR_A4_P141  = perguntaByCodPerguntaEnum(PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA4_141.getCod());
			perguntaNR_A4_P1411 = perguntaByCodPerguntaEnum(PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA4_1411.getCod());
			perguntaNR_A4_P142 = perguntaByCodPerguntaEnum(PerguntaEnum.PERGUNTA_NOVO_REQUERIMENTO_ABA4_142.getCod());

			listaPerguntaRN = new ArrayList<PerguntaRequerimento>();
			listaPerguntaRN.add(perguntaNR_ABA4_1);
			listaPerguntaRN.add(perguntaNR_A4P14);
			listaPerguntaRN.add(perguntaNR_A4_P141);
			listaPerguntaRN.add(perguntaNR_A4_P1411);
			listaPerguntaRN.add(perguntaNR_A4_P142);

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Metodo para apresentar as perguntas na tela
	 */
	public void  carregarperguntaRespondidaNovoRequerimento(){
		carregarListaPerguntaNovoRequerimento();
		try{
			perguntaRequerimentoService.carregarListaPerguntaRequerimentoRespondida(listaPerguntaRN, requerimento, null, null, null);
			carregarOrigemAgua();
			carregarRespostasVolCaminhaoEMediaTransp();
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void carregarEmpreendimentoRequerimento(){
		try {
			List<Empreendimento> listaEmpreendimento = (List<Empreendimento>) empreendimentoService.buscarEmpreendimentoPorRequerimento(requerimento);
			Empreendimento empreendimento = empreendimentoService.carregarById(listaEmpreendimento.get(0).getIdeEmpreendimento());
			empreendimentoRequerimento = empreendimentoService.buscarEmpreendimentoRequerimento(requerimento, empreendimento);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" as informações do empreendimento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void carregarRespostasVolCaminhaoEMediaTransp(){
		try{
			List<Outorga> outorgas = outorgaService.listarOutorgaByRequerimentoECaptacaoEOuTipoSolicitacao(requerimento,new ModalidadeOutorga(ModalidadeOutorgaEnum.CAPTACAO.getIdModalidade()), new TipoSolicitacao [] {new TipoSolicitacao(TipoSolicitacaoEnum.NOVA_OUTORGA.getId())});
			if(!Util.isNullOuVazio(outorgas)){
				Outorga outorga = outorgas.iterator().next();
				qtdTransporteCaminhaoPipa = outorga.getQtdTransporteCaminhaoPipa();
				valorVolumeCaminhaoPipa = outorga.getValorVolumeCaminhaoPipa();
			}
		}catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}


	/**
	 * metodo para carregar a perguntas Qual(is) origem(ns) da(s) água(s) utilizada ou a ser utilizada?
	 */
	public void carregarOrigemAgua(){

		try {
			List<Outorga> outorgasCaptacao = outorgaService.getOutorgaByModalidadeRequerimento(ModalidadeOutorgaEnum.CAPTACAO, requerimento);

			for(Outorga outorga : outorgasCaptacao){
				OutorgaTipoCaptacao outorgaTipoCaptacao = this.outorgaService.buscarOutorgaTipoCaptacaoByIdeOutorga(outorga);
				if(!Util.isNull(outorgaTipoCaptacao) && Util.isNull(outorgaTipoCaptacao.getIdeTipoCaptacao())){
					if(TipoCaptacaoEnum.CAPTACAO_CONCESSIONARIA_PUBLICA.getId().intValue() == outorgaTipoCaptacao.getIdeTipoCaptacao().getIdeTipoCaptacao()){
						captacaoConcessessionarioPublica = true;
					}else if(TipoCaptacaoEnum.CAPTACAO_PRECIPITACAO_METEOROLOGICA_PLUVIAL.getId().intValue() == outorgaTipoCaptacao.getIdeTipoCaptacao().getIdeTipoCaptacao()){
						captacaoPrecipitacaoPluvial = true;
					}
				}
			}

			captacoesSubterraneas = outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeograficaByTipoCaptacao(TipoCaptacaoEnum.CAPTACAO_SUBTERRANEA, requerimento);
			if(!Util.isNullOuVazio(captacoesSubterraneas)){
				captacaoSubterranea  = true;
			}

			captacoesSuperficiais = outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeograficaByTipoCaptacao(TipoCaptacaoEnum.CAPTACAO_SUPERFICIAL, requerimento);
			if(!Util.isNullOuVazio(captacoesSuperficiais)){
				captacaoSuperficial  = true;
			}

			lancamentosEfluentes = outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeograficaComDadoGeograficoByModalidadeOutorga(ModalidadeOutorgaEnum.LANCAMENTO_EFLUENTES, requerimento);
		}catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/*
	 * 1. O empreendimento ou atividade usa ou usará água, realiza ou realizará lançamento de efluente e/ou possui ou possuirá intervenção em corpo hídrico?
	 */

	public boolean isVerificaPerguntaNR_ABA4_1(){
		if(!Util.isNullOuVazio(perguntaNR_ABA4_1) && !Util.isNullOuVazio(perguntaNR_ABA4_1.getIndResposta()) && perguntaNR_ABA4_1.getIndResposta()){
			return true;
		}
		return false;
	}

	/*
	 * 1.4. Os Processos relativos ao empreendimento atendem a todos os usos passíveis de outorga ou dispensa?
	 */
	public boolean isVerificaPerguntaNR_A4_P14(){
		if(!Util.isNullOuVazio(perguntaNR_A4P14) && !Util.isNullOuVazio(perguntaNR_A4P14.getIndResposta()) && !perguntaNR_A4P14.getIndResposta()){
			return true;
		}
		return false;
	}

	/*
	 * Verifica as pergundas 1 e 1.4 e 1.4.1 da aba 4
	 */
	public boolean isVerificaPergunta_OrigensUsosAgua(){
		if(isVerificaPerguntaNR_ABA4_1() && isVerificaPerguntaNR_A4_P14()){
			return true;
		}
		return false;
	}
	/*
	 * 1.4.1. O empreendimento faz ou fará captação de água?
	 */
	public boolean isVerificaPerguntaNR_A4_P141(){
		if(!Util.isNullOuVazio(perguntaNR_A4_P141) && !Util.isNullOuVazio(perguntaNR_A4_P141.getIndResposta()) && perguntaNR_A4_P141.getIndResposta()){
			return true;
		}
		return false;
	}

	/*
	 * 1.4.1.1.
	 */
	public boolean isVerificaPerguntaNR_A4_P1411(){
		if(!Util.isNullOuVazio(perguntaNR_A4_P1411) && !Util.isNullOuVazio(perguntaNR_A4_P1411.getIndResposta()) && perguntaNR_A4_P1411.getIndResposta()){
			return true;
		}
		return false;
	}
	/*
	 * 1.4.2. Existe ou existirá, no empreendimento,lançamento de efluentes em corpo hídrico?(não se aplica a lançamentos no oceano)
	 */
	public boolean isVerificaPerguntaNR_A4_P142(){
		return !Util.isNullOuVazio(perguntaNR_A4_P142.getIndResposta()) && perguntaNR_A4_P142.getIndResposta();
	}



	/**
	 * Mètodo que diz se o {@link TipoCaptacao} é Subterrânea
	 * @param listaOrigemUsoAguaEscolhidos
	 * @return boolean
	 * @author eduardo.fernandes
	
	// COMENTADO PARA O TICKET "" DO RESULTADO DA HOMOLOGAÇÃO DE 11/06
	/*	private boolean isOrigemAguaCaptacaoSubterranea(){
		return listaOrigemUsoAguaEscolhidos.contains(new TipoCaptacao(TipoCaptacaoEnum.CAPTACAO_SUBTERRANEA.getId()));
	}
	 */
	/**
	 * Mètodo que diz se o {@link TipoCaptacao} é Superficial
	 * @param listaOrigemUsoAguaEscolhidos
	 * @return boolean
	 * @author eduardo.fernandes
	
	// COMENTADO PARA O TICKET "" DO RESULTADO DA HOMOLOGAÇÃO DE 11/06
	/*private boolean isOrigemAguaCaptacaoSuperficial(){
		return listaOrigemUsoAguaEscolhidos.contains(new TipoCaptacao(TipoCaptacaoEnum.CAPTACAO_SUPERFICIAL.getId()));
	}
	 */
	/**
	 * Método que verifica se existe alguma captação escolhida e se as existentes são "Subterrânea" ou "Superficial"
	 * @see isOrigemAguaCaptacaoSubterranea();
	 * @see isOrigemAguaCaptacaoSuperficial();
	 * @return boolean
	 * @author eduardo.fernandes
	
	// COMENTADO PARA O TICKET "" DO RESULTADO DA HOMOLOGAÇÃO DE 11/06
	/*public boolean isOrigemAguaCaptacao(){
		return !Util.isNullOuVazio(listaOrigemUsoAguaEscolhidos) && (isOrigemAguaCaptacaoSubterranea() || isOrigemAguaCaptacaoSuperficial());
	} */

	/**
	 * Método que diz se o Lançamento do Efluente do {@link FceIndustria} é regularizado
	 * @param fceIndustria
	 * @return boolean
	 * @author eduardo.fernandes
	
	// COMENTADO PARA O TICKET "" DO RESULTADO DA HOMOLOGAÇÃO DE 11/06
	/*	public boolean isLancamentoEfluenteRegularizado(){
		return !Util.isNullOuVazio(fceIndustria) && !Util.isNullOuVazio(fceIndustria.getIndLancamentoRegularizado()) && fceIndustria.getIndLancamentoRegularizado();
	}
	 */

	/**
	 * RN 00102
	 * @return boolean
	 * @author eduardo.fernandes
	 */
	public boolean isLIouLA(){
		if(!Util.isNullOuVazio(empreendimentoRequerimento)){
			return empreendimentoRequerimento.getIdeFaseEmpreendimento().getIdeFaseEmpreendimento().equals(FaseEmpreendimentoEnum.ALTERACAO.getId()) || empreendimentoRequerimento.getIdeFaseEmpreendimento().getIdeFaseEmpreendimento().equals(FaseEmpreendimentoEnum.IMPLANTACAO.getId());
		}
		else {
			return false;
		}
	}

	/**
	 * Método que verifica se algum {@link FceIndustriaSubstancia} foi adicionado pelo usuário
	 * @param listaFceIndustriaSubstancias
	 * @return boolean
	 * @author eduardo.fernandes
	 */
	public boolean isExisteSubstancia(){
		return !Util.isNullOuVazio(listaFceIndustriaSubstancias);
	}
	// FIM Redendered's, Flags, Controladores em geral


	// STREAMs
	/**
	 * Método que imprime o relatório do {@link FceIndustria}
	 * @param requerimento
	 * @param DOC_INDUSTRIA
	 * @return StreamedContent
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	public StreamedContent getImprimirRelatorio() {
		try {
			return getImprimirRelatorio(super.fce, DOC_INDUSTRIA);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_imprimir_relatorio") + " do FCE - Indústria.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	// getters & setters
	public int getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}

	public List<DestinoResiduo> getListaDestinoResiduos() {
		return listaDestinoResiduos;
	}

	public void setListaDestinoResiduos(List<DestinoResiduo> listaDestinoResiduos) {
		this.listaDestinoResiduos = listaDestinoResiduos;
	}

	public List<DestinoResiduo> getListaDestinoResiduosEscolhidos() {
		return listaDestinoResiduosEscolhidos;
	}

	public void setListaDestinoResiduosEscolhidos(List<DestinoResiduo> listaDestinoResiduosEscolhidos) {
		this.listaDestinoResiduosEscolhidos = listaDestinoResiduosEscolhidos;
		if(!existeOutrosDestinoResiduos()){
			alertaExibido4 = false;
		}
	}

	public List<TipoEmissaoAtmosferica> getListaEmissaoAtmosfericas() {
		return listaEmissaoAtmosfericas;
	}

	public void setListaEmissaoAtmosfericas(List<TipoEmissaoAtmosferica> listaEmissaoAtmosfericas) {
		this.listaEmissaoAtmosfericas = listaEmissaoAtmosfericas;
	}

	public List<TipoEmissaoAtmosferica> getListaEmissaoAtmosfericasEscolhidos() {
		return listaEmissaoAtmosfericasEscolhidos;
	}

	public void setListaEmissaoAtmosfericasEscolhidos(List<TipoEmissaoAtmosferica> listaEmissaoAtmosfericasEscolhidos) {
		this.listaEmissaoAtmosfericasEscolhidos = listaEmissaoAtmosfericasEscolhidos;
		if(!existeOutrasEmissoesAtmosfericas()){
			alertaExibido3 = false;
		}
	}

	public List<TipoOrigemEnergia> getListaOrigemEnergias() {
		return listaOrigemEnergias;
	}

	public void setListaOrigemEnergias(List<TipoOrigemEnergia> listaOrigemEnergias) {
		this.listaOrigemEnergias = listaOrigemEnergias;
	}

	public List<TipoOrigemEnergia> getListaOrigemEnergiasEscolhidos() {
		return listaOrigemEnergiasEscolhidos;
	}

	public void setListaOrigemEnergiasEscolhidos(List<TipoOrigemEnergia> listaOrigemEnergiasEscolhidos) {
		this.listaOrigemEnergiasEscolhidos = listaOrigemEnergiasEscolhidos;
		if(!existeOutrosOrigemEnergia()){
			alertaExibido1 = false;
		}
	}

	public List<TipoResiduoConsCivil> getListaResiduoConsCivils() {
		return listaResiduoConsCivils;
	}

	public void setListaResiduoConsCivils(List<TipoResiduoConsCivil> listaResiduoConsCivils) {
		this.listaResiduoConsCivils = listaResiduoConsCivils;
	}

	public List<TipoResiduoConsCivil> getListaResiduoConsCivilsEscolhidos() {
		return listaResiduoConsCivilsEscolhidos;
	}

	public void setListaResiduoConsCivilsEscolhidos(List<TipoResiduoConsCivil> listaResiduoConsCivilsEscolhidos) {
		this.listaResiduoConsCivilsEscolhidos = listaResiduoConsCivilsEscolhidos;
	}

	public List<TipoResiduoGerado> getListaResiduoGerados() {
		return listaResiduoGerados;
	}

	public void setListaResiduoGerados(List<TipoResiduoGerado> listaResiduoGerados) {
		this.listaResiduoGerados = listaResiduoGerados;
	}

	public List<TipoResiduoGerado> getListaResiduoGeradosEscolhidos() {
		return listaResiduoGeradosEscolhidos;
	}

	public void setListaResiduoGeradosEscolhidos(List<TipoResiduoGerado> listaResiduoGeradosEscolhidos) {
		this.listaResiduoGeradosEscolhidos = listaResiduoGeradosEscolhidos;
	}

	public List<TipoSistemaTratamento> getListaSistemaTratamentos() {
		return listaSistemaTratamentos;
	}

	public void setListaSistemaTratamentos(List<TipoSistemaTratamento> listaSistemaTratamentos) {
		this.listaSistemaTratamentos = listaSistemaTratamentos;
	}

	public List<TipoSistemaTratamento> getListaSistemaTratamentosEscolhidos() {
		return listaSistemaTratamentosEscolhidos;
	}

	public void setListaSistemaTratamentosEscolhidos(List<TipoSistemaTratamento> listaSistemaTratamentosEscolhidos) {
		this.listaSistemaTratamentosEscolhidos = listaSistemaTratamentosEscolhidos;
		if(!existeOutrosEfluentesLiquidos()){
			alertaExibido2 = false;
		}
	}

	public List<TipoSubstancia> getListaSubstanciasEscolhidos() {
		return listaSubstanciasEscolhidos;
	}

	public void setListaSubstanciasEscolhidos(List<TipoSubstancia> listaSubstanciasEscolhidos) {
		this.listaSubstanciasEscolhidos = listaSubstanciasEscolhidos;
	}

	public List<TipoSubstancia> getListaSubstancias() {
		return listaSubstancias;
	}

	public void setListaSubstancias(List<TipoSubstancia> listaSubstancias) {
		this.listaSubstancias = listaSubstancias;
	}

	@Override
	public Requerimento getRequerimento() {
		return requerimento;
	}

	@Override
	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public FceIndustria getFceIndustria() {
		return fceIndustria;
	}

	public void setFceIndustria(FceIndustria fceIndustria) {
		this.fceIndustria = fceIndustria;
	}

	public String getSubstanciaPesquisada() {
		return substanciaPesquisada;
	}

	public void setSubstanciaPesquisada(String substanciaPesquisada) {
		this.substanciaPesquisada = substanciaPesquisada;
	}

	public TipoSubstancia getTipoSubstancia() {
		return tipoSubstancia;
	}

	public void setTipoSubstancia(TipoSubstancia tipoSubstancia) {
		this.tipoSubstancia = tipoSubstancia;
	}

	public List<FceIndustriaSubstancia> getListaFceIndustriaSubstancias() {
		return listaFceIndustriaSubstancias;
	}

	public void setListaFceIndustriaSubstancias(List<FceIndustriaSubstancia> listaFceIndustriaSubstancias) {
		this.listaFceIndustriaSubstancias = listaFceIndustriaSubstancias;
	}

	public FceIndustriaSubstancia getFceIndustriaSubstancia() {
		return fceIndustriaSubstancia;
	}

	public void setFceIndustriaSubstancia(FceIndustriaSubstancia fceIndustriaSubstancia) {
		this.fceIndustriaSubstancia = fceIndustriaSubstancia;
	}



	public List<TipoCaptacao> getListaOrigemUsoAgua() {
		return listaOrigemUsoAgua;
	}

	public void setListaOrigemUsoAgua(List<TipoCaptacao> listaOrigemUsoAgua) {
		this.listaOrigemUsoAgua = listaOrigemUsoAgua;
	}

	public List<PerguntaRequerimento> getListaPerguntaRN() {
		return listaPerguntaRN;
	}


	public void setListaPerguntaRN(List<PerguntaRequerimento> listaPerguntaRN) {
		this.listaPerguntaRN = listaPerguntaRN;
	}


	public PerguntaRequerimento getPerguntaRequerimento() {
		return perguntaRequerimento;
	}


	public void setPerguntaRequerimento(PerguntaRequerimento perguntaRequerimento) {
		this.perguntaRequerimento = perguntaRequerimento;
	}


	public PerguntaRequerimento getPerguntaNR_A4_P1411() {
		return perguntaNR_A4_P1411;
	}


	public void setPerguntaNR_A4_P1411(PerguntaRequerimento perguntaNR_A4_P1411) {
		this.perguntaNR_A4_P1411 = perguntaNR_A4_P1411;
	}


	public PerguntaRequerimento getPerguntaNR_A4_P1512() {
		return perguntaNR_A4_P1512;
	}


	public void setPerguntaNR_A4_P1512(PerguntaRequerimento perguntaNR_A4_P1512) {
		this.perguntaNR_A4_P1512 = perguntaNR_A4_P1512;
	}


	public PerguntaRequerimento getPerguntaNR_A4_P141() {
		return perguntaNR_A4_P141;
	}


	public void setPerguntaNR_A4_P141(PerguntaRequerimento perguntaNR_A4_P141) {
		this.perguntaNR_A4_P141 = perguntaNR_A4_P141;
	}


	public PerguntaRequerimento getPerguntaNR_A4P14() {
		return perguntaNR_A4P14;
	}


	public void setPerguntaNR_A4P14(PerguntaRequerimento perguntaNR_A4P14) {
		this.perguntaNR_A4P14 = perguntaNR_A4P14;
	}

	public boolean isCaptacaoSuperficial() {
		return captacaoSuperficial;
	}

	public void setCaptacaoSuperficial(boolean captacaoSuperficial) {
		this.captacaoSuperficial = captacaoSuperficial;
	}

	public boolean isCaptacaoSubterranea() {
		return captacaoSubterranea;
	}

	public void setCaptacaoSubterranea(boolean captacaoSubterranea) {
		this.captacaoSubterranea = captacaoSubterranea;
	}

	public boolean isCaptacaoConcessessionarioPublica() {
		return captacaoConcessessionarioPublica;
	}

	public void setCaptacaoConcessessionarioPublica(boolean captacaoConcessessionarioPublica) {
		this.captacaoConcessessionarioPublica = captacaoConcessessionarioPublica;
	}
	public boolean isCaptacaoPrecipitacaoPluvial() {
		return captacaoPrecipitacaoPluvial;
	}

	public void setCaptacaoPrecipitacaoPluvial(boolean captacaoPrecipitacaoPluvial) {
		this.captacaoPrecipitacaoPluvial = captacaoPrecipitacaoPluvial;
	}

	public Collection<OutorgaLocalizacaoGeografica> getCaptacoesSubterraneas() {
		return captacoesSubterraneas;
	}

	public List<OutorgaLocalizacaoGeografica> getCaptacaoesSuperficiais() {
		return captacoesSuperficiais;
	}

	public PerguntaRequerimento getPerguntaNR_ABA4_1() {
		return perguntaNR_ABA4_1;
	}


	public void setPerguntaNR_ABA4_1(PerguntaRequerimento perguntaNR_ABA4_1) {
		this.perguntaNR_ABA4_1 = perguntaNR_ABA4_1;
	}

	public PerguntaRequerimento getPerguntaA4_P14() {
		return perguntaA4_P14;
	}

	public void setPerguntaA4_P14(PerguntaRequerimento perguntaA4_P14) {
		this.perguntaA4_P14 = perguntaA4_P14;
	}

	public PerguntaRequerimento getPerguntaNR_A4_P142() {
		return perguntaNR_A4_P142;
	}

	public void setPerguntaNR_A4_P142(PerguntaRequerimento perguntaNR_A4_P142) {
		this.perguntaNR_A4_P142 = perguntaNR_A4_P142;
	}

	public List<OutorgaLocalizacaoGeografica> getLancamentosEfluentes() {
		return lancamentosEfluentes;
	}

	public void setLancamentosEfluentes(List<OutorgaLocalizacaoGeografica> lancamentosEfluentes) {
		this.lancamentosEfluentes = lancamentosEfluentes;
	}

	public BigDecimal getValorVolumeCaminhaoPipa() {
		return valorVolumeCaminhaoPipa;
	}

	public void setValorVolumeCaminhaoPipa(BigDecimal valorVolumeCaminhaoPipa) {
		this.valorVolumeCaminhaoPipa = valorVolumeCaminhaoPipa;
	}

	public Integer getQtdTransporteCaminhaoPipa() {
		return qtdTransporteCaminhaoPipa;
	}

	public void setQtdTransporteCaminhaoPipa(Integer qtdTransporteCaminhaoPipa) {
		this.qtdTransporteCaminhaoPipa = qtdTransporteCaminhaoPipa;
	}

	public List<TipoApp> getListaTipoApps() {
		return listaTipoApps;
	}

	public void setListaTipoApps(List<TipoApp> listaTipoApps) {
		this.listaTipoApps = listaTipoApps;
	}

	public List<TipoApp> getListaTipoAppsEscolhidos() {
		return listaTipoAppsEscolhidos;
	}

	public void setListaTipoAppsEscolhidos(List<TipoApp> listaTipoAppsEscolhidos) {
		this.listaTipoAppsEscolhidos = listaTipoAppsEscolhidos;
	}

	/**
	 * Método que verifica se já existe um {@link Fce} salvo para aquele {@link Requerimento}.
	 * @author eduardo.fernandes
	 */
	@Override
	public void verificarEdicao() {
		carregarFceDoRequerente(DOC_INDUSTRIA);
	}

	@Override
	public void carregarAba() {
		carregarListas();
		carregarperguntaRespondidaNovoRequerimento();
		carregarEmpreendimentoRequerimento();
	}

	@Override
	public void abrirDialog() {
		RequestContext.getCurrentInstance().addPartialUpdateTarget("pnlFceIndustria");
		RequestContext.getCurrentInstance().addPartialUpdateTarget("dlgImprimirRelatorioIndustria");
		RequestContext.getCurrentInstance().execute("industria.show();");
	}

	@Override
	protected void prepararDuplicacao() {
		fceIndustria.setIdeFceIndustria(null);
		fceIndustria.setIdeFce(super.fce);
	}

	@Override
	protected void duplicarFce() {
		try {
			super.salvarFce();
			salvarFceIndustria();
			salvarAbaDadosDoEmpreendimento();
			salvarAbaEfluentesEmissoes();
			salvarAbaResiduos();
			salvarAbaCaracterizacaoAmbiental();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " o FCE - Indústria.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	public void carregarFceTecnico() {
		activeTab = 0;
		carregarFceIndustriaByFce();
		carregarAba();
	}
}