package br.gov.ba.seia.controller;

import java.io.InputStream; 
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dto.ColunasExibirImovelRuralRelatorioDTO;
import br.gov.ba.seia.dto.ColunasExibirRelatorioQuantitativoImoveisRuraisDTO;
import br.gov.ba.seia.dto.FiltroImovelRuralRelatorioDTO;
import br.gov.ba.seia.dto.ImovelRuralRelatorioDTO;
import br.gov.ba.seia.entity.ContratoConvenioCefir;
import br.gov.ba.seia.entity.GeoBahia;
import br.gov.ba.seia.entity.GestorFinanceiroCefir;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaImovel;
import br.gov.ba.seia.enumerator.StatusCadastroImovelRuralEnum;
import br.gov.ba.seia.enumerator.TipoArlEnum;
import br.gov.ba.seia.enumerator.TipoCertificadoEnum;
import br.gov.ba.seia.facade.ImovelRuralFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.ContratoConvenioCefirServiceImpl;
import br.gov.ba.seia.service.GestorFinanceiroCefirService;
import br.gov.ba.seia.service.ImovelRuralRelatorioService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.RelatorioQuantitativoImovelRuralService;
import br.gov.ba.seia.service.RelatorioQuantitativoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;
import br.gov.ba.seia.util.security.SecurityController;

@Named("relatorioImovelRuralController")
@ViewScoped
public class RelatorioImovelRuralController extends SeiaControllerAb {
	
	@EJB
	private MunicipioService municipioService;
	@EJB
	private ContratoConvenioCefirServiceImpl contratoConvenioService;
	@EJB
	private GestorFinanceiroCefirService gestorFinanceiroService;
	@EJB
	private ImovelRuralRelatorioService imovelRuralRelatorioService;
	@EJB
	private ImovelRuralFacade imovelRuralFacade;
	@EJB
	private RelatorioQuantitativoService relatorioQuantitativoService;
	@EJB
	private RelatorioQuantitativoImovelRuralService relatorioQuantitativoImovelRuralService;
	
	//Opções filtro Questionário
	private static Integer PREENCHIDO = 1;
	private static Integer NAO_PREENCHIDO = 2;
	
	//Opções Módulos fiscais
	private static Integer ATE_4_MODULOS = 1;
	private static Integer ACIMA_4_MODULOS = 2;
	
	//Opções Documento final
	private static Integer TERMO_COMPROMISSO = 3;
	private static Integer CERTIFICADO = 2;
	private static Integer AVISO = 4;
	
	private FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO;
	private ColunasExibirImovelRuralRelatorioDTO colunasExibirImovelRuralRelatorioDTO;
	private ColunasExibirRelatorioQuantitativoImoveisRuraisDTO colunasExibirRelatorioQuantitativoImoveisRuraisDTO;
	private List<Municipio> listaMunicipios;
	private List<GestorFinanceiroCefir> listaGestorFinanceiro;
	private List<ContratoConvenioCefir> listaContratoConvenio;
	private List<ImovelRuralRelatorioDTO> listaImoveis;
	private LazyDataModel<ImovelRuralRelatorioDTO> listaImoveisDT;
	private LazyDataModel<Pessoa> listaPessoa;
	private boolean registroIncompleto;
	private boolean registrado;
	private boolean cadastrado;
	private boolean pendente;
	private boolean buscaAvancada;		
	private SimpleDateFormat sdfdmy = new SimpleDateFormat("dd/MM/yyyy");
	private Pessoa proprietario;
	private PessoaFisica procurador; 
	private boolean mostrarTabela;
	private boolean tipoDeImpressaoPdf = true;
	
	private List<GeoBahia> listaTerritorio;
	private List<GeoBahia> listaUnidade;
	private List<GeoBahia> listaBioma;
	private List<GeoBahia> listaRpga;
	private List<GeoBahia> listaBacia;
	private List<GeoBahia> listaFiltro;
	
	//Início Disables
	private boolean disablePeriodoFinalizacao;
	private boolean disablePeriodoSincronizacao;
	private boolean disableGestorFinanceiro;
	private boolean disableContratoConvenio;
	private boolean disableAte4Modulos;
	private boolean disableAcima4Modulos;
	private boolean disableNoProprioImovel;
	private boolean disableEmCondominio;
	private boolean disableEmCompensacaoPorServidao;
	private boolean disableEmcompensacaoEntreImoveisMesmoProprietario;
	private boolean disableAviso;
	private boolean disableTermoCompromisso;
	private boolean disableCertificado;
	private boolean disableQuestionarioPreenchido;
	private boolean disableQuestionarioNaoPreenchido;
	private boolean disableBndes;
	private boolean disableCda;
	private boolean disableIncra;
	private boolean disableImovelRural;
	private boolean disableAssentamento;
	private boolean disablePCT;
	private boolean disablePraCadastrado;
	private boolean disableNumeroCar;
	private boolean disableRlAprovada;
	private boolean disableAreaRlMenorQue20PorCento;
	private boolean disableSobreposicaoRlApp;
	private boolean disableOutroPassivoAmbiental;
	private boolean disableLimiteBloqueado;
	private boolean disablePeriodoReservaLegal;
	
	private boolean habilitaDownloadAvisoCarLote;
	
	
	private MetodoUtil metodoSelecionarSolicitante;
	
	@PostConstruct
	public void init() {
		
		try {
			metodoSelecionarSolicitante = new MetodoUtil(this, this.getClass().getDeclaredMethod("receberSolicitante", Pessoa.class));
		
			listaImoveisDT = (LazyDataModel<ImovelRuralRelatorioDTO>) getAtributoSession("IMOVEIS");
			
			createDynamicColumns();
			
			if(filtroImovelRuralRelatorioDTO == null){
				filtroImovelRuralRelatorioDTO = new FiltroImovelRuralRelatorioDTO();
				filtroImovelRuralRelatorioDTO.setListMunicipio(new ArrayList<Integer>());			
				filtroImovelRuralRelatorioDTO.setListGestorFinanceiro(new ArrayList<Integer>());
				filtroImovelRuralRelatorioDTO.setListContratoConvenio(new ArrayList<Integer>());			
			}
			if(Util.isNullOuVazio(this.listaFiltro)){
				montarListaFiltro();
			}
			
			if(Util.isNullOuVazio(this.listaGestorFinanceiro)) {
				this.listaGestorFinanceiro = new ArrayList<GestorFinanceiroCefir>();
				carregarListaGestorFinanceiro();
			}
			
			if(Util.isNullOuVazio(this.listaContratoConvenio)) {
				this.listaContratoConvenio = new ArrayList<ContratoConvenioCefir>();
				carregarListaContratoConvenio();
			}
			
			if(Util.isNull(colunasExibirImovelRuralRelatorioDTO)){
				colunasExibirImovelRuralRelatorioDTO = new ColunasExibirImovelRuralRelatorioDTO();
			}
			
			if(Util.isNull(colunasExibirRelatorioQuantitativoImoveisRuraisDTO)){
				colunasExibirRelatorioQuantitativoImoveisRuraisDTO = new ColunasExibirRelatorioQuantitativoImoveisRuraisDTO();
			}
			
			if(Util.isNullOuVazio(proprietario)){
				this.proprietario = new Pessoa();
			}
			
			if(Util.isNullOuVazio(procurador)){
				this.procurador = new PessoaFisica();
			}
			
			colunasExibirImovelRuralRelatorioDTO.preparaMapColunas();
			filtroImovelRuralRelatorioDTO.setTipoFiltroSelecionado(1);
			
		} catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void receberSolicitante(Pessoa pessoa){
		filtroImovelRuralRelatorioDTO.setResponsavelTecnico(pessoa);
		Html.atualizar("formRelatorioImovelRural:panelFiltros");
	}
	
	/**
	 * Método responsável por montar as opções do select referente ao campo "Filtrar por:"
	 */
	private void montarListaFiltro() {
		int id = 0;
		listaFiltro = new ArrayList<GeoBahia>();
		listaFiltro.add(new GeoBahia(++id, "Selecione..."));
		listaFiltro.add(new GeoBahia(++id, "Bacia"));
		listaFiltro.add(new GeoBahia(++id, "Bioma"));
		listaFiltro.add(new GeoBahia(++id, "Município*"));
		listaFiltro.add(new GeoBahia(++id, "RPGA"));
		listaFiltro.add(new GeoBahia(++id, "Território de identidade"));
		listaFiltro.add(new GeoBahia(++id, "Unidade de conservação"));
	}
	
	/**
	 * Método responsável por carregar a lista de Gestores Financeiros"
	 */
	private void carregarListaGestorFinanceiro() {
		try {			
			this.listaGestorFinanceiro = gestorFinanceiroService.listGestorFinanceiro();
			//Adicionando a opção todos
			if(!Util.isNullOuVazio(this.listaGestorFinanceiro)) {
				GestorFinanceiroCefir e = new GestorFinanceiroCefir(-1, "TODOS");
				this.listaGestorFinanceiro.add(0, e);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	/**
	 * Método responsável por carregar a lista de Contratos/Convênios"
	 */
	private void carregarListaContratoConvenio() {
		try {
			this.listaContratoConvenio = contratoConvenioService.listContratoConvenio();
			if(!Util.isNullOuVazio(this.listaContratoConvenio)) {
				ContratoConvenioCefir c = new ContratoConvenioCefir(-1, "TODOS");
				this.listaContratoConvenio.add(0, c);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}		
	}
	
	/**
	 * Método responsável por limpar as opções que foram selecionadas pelo campo "Filtrar por:"
	 */
	public void limparFiltroSelecionado() {		
		filtroImovelRuralRelatorioDTO.setListMunicipio(new ArrayList<Integer>());
		filtroImovelRuralRelatorioDTO.setIdBiomaSelecionado(new GeoBahia());
		filtroImovelRuralRelatorioDTO.setIdRPGASelecionado(new GeoBahia());
		filtroImovelRuralRelatorioDTO.setIdUnidadeSelecionado(new GeoBahia());
		filtroImovelRuralRelatorioDTO.setIdTerritorioSelecionado(new GeoBahia());
		filtroImovelRuralRelatorioDTO.setIdBaciaSelecionado(new GeoBahia());
	}
	
	/**
	 * Método responsável por limpar os filtros ao selecionar ou deselecionar algum campo do "Status do Cadastro"
	 */
	public void limpaFiltrosBuscaAvancada() {
		
		//Período da primeira finalização
		filtroImovelRuralRelatorioDTO.setDtaInicioPeriodoPrimeiraFinalizacao(null);
		filtroImovelRuralRelatorioDTO.setDtaFimPeriodoPrimeiraFinalizacao(null);
		
		//Período da primeira sincronização
		filtroImovelRuralRelatorioDTO.setDtaInicioPeriodoPrimeiraSincronizacao(null);
		filtroImovelRuralRelatorioDTO.setDtaFimPeriodoPrimeiraSincronizacao(null);
		
		//Gestor financeiro
		filtroImovelRuralRelatorioDTO.setListGestorFinanceiro(new ArrayList<Integer>());
		
		//Contrato / convênio
		filtroImovelRuralRelatorioDTO.setListContratoConvenio(new ArrayList<Integer>());
		
		//Módulos fiscais
		filtroImovelRuralRelatorioDTO.setModulosFiscais(null);
		
		//Tipo de reserva legal
		filtroImovelRuralRelatorioDTO.setTipoArl(null);

		//Documento final
		filtroImovelRuralRelatorioDTO.setDocumentoFinal(null);
		
		//Questionário
		if (this.cadastrado && !this.registroIncompleto && !this.registrado) {
			filtroImovelRuralRelatorioDTO.setQuestionario(PREENCHIDO);
		} else {
			filtroImovelRuralRelatorioDTO.setQuestionario(null);
		}
		
		//Tipos de cadastros
		filtroImovelRuralRelatorioDTO.setCadastroBndes(false);
		filtroImovelRuralRelatorioDTO.setCadastroCda(false);
		filtroImovelRuralRelatorioDTO.setCadastroIncra(false);
		
		//Módulos de cadastro
		filtroImovelRuralRelatorioDTO.setModuloImovelRural(false);
		filtroImovelRuralRelatorioDTO.setModuloAssentamento(false);
		filtroImovelRuralRelatorioDTO.setModuloPCT(false);
		
		//Imóveis com
		filtroImovelRuralRelatorioDTO.setPraCadastrado(false);
		filtroImovelRuralRelatorioDTO.setNumeroCar(false);
		filtroImovelRuralRelatorioDTO.setRlAprovada(false);
		filtroImovelRuralRelatorioDTO.setRlMenorQueVintePorCento(false);
		filtroImovelRuralRelatorioDTO.setSobreposicaoRLAPP(false);
		filtroImovelRuralRelatorioDTO.setOutroPassivoAmbiental(false);
		filtroImovelRuralRelatorioDTO.setLimiteBloqueado(false);
				
		//Disables
		setDisablePeriodoFinalizacao(false);
		setDisablePeriodoSincronizacao(false);
		setDisableGestorFinanceiro(false);
		setDisableContratoConvenio(false);
		setDisableAte4Modulos(false);
		setDisableAcima4Modulos(false);
		setDisableNoProprioImovel(false);
		setDisableEmCondominio(false);
		setDisableEmCompensacaoPorServidao(false);
		setDisableEmcompensacaoEntreImoveisMesmoProprietario(false);
		setDisableAviso(false);
		setDisableTermoCompromisso(false);
		setDisableCertificado(false);
		setDisableQuestionarioPreenchido(false);
		setDisableQuestionarioNaoPreenchido(false);
		setDisableBndes(false);
		setDisableCda(false);
		setDisableIncra(false);
		setDisableImovelRural(false);
		setDisableAssentamento(false);
		setDisablePCT(false);
		setDisablePraCadastrado(false);
		setDisableNumeroCar(false);
		setDisableRlAprovada(false);
		setDisableAreaRlMenorQue20PorCento(false);
		setDisableSobreposicaoRlApp(false);
		setDisableOutroPassivoAmbiental(false);
		setDisableLimiteBloqueado(false);
		setDisablePeriodoReservaLegal(false);
		
		habilitaDownloadAvisoCarLote = false;
		
		validaVisualizacaoCamposBuscaAvancada();
	}
	
	public void changeDataInicioPrimeiraFinalizacao(ValueChangeEvent event) {
		if(!Util.isNullOuVazio(event.getNewValue())){
			filtroImovelRuralRelatorioDTO.setDtaInicioPeriodoPrimeiraFinalizacao((Date) event.getNewValue());
		}	
		validaVisualizacaoCamposBuscaAvancada();
	}
	
	public void changeDataFimPrimeiraFinalizacao(ValueChangeEvent event) {
		if(!Util.isNullOuVazio(event.getNewValue())){
			filtroImovelRuralRelatorioDTO.setDtaFimPeriodoPrimeiraFinalizacao((Date) event.getNewValue());
		}
		validaVisualizacaoCamposBuscaAvancada();
	}
	
	public void changeDataInicioPrimeiraSincronizacao(ValueChangeEvent event) {
		if(!Util.isNullOuVazio(event.getNewValue())){
			filtroImovelRuralRelatorioDTO.setDtaInicioPeriodoPrimeiraSincronizacao((Date) event.getNewValue());
		}	
		validaVisualizacaoCamposBuscaAvancada();
	}
	
	public void changeDataFimPrimeiraSincronizacao(ValueChangeEvent event) {
		if(!Util.isNullOuVazio(event.getNewValue())){
			filtroImovelRuralRelatorioDTO.setDtaFimPeriodoPrimeiraSincronizacao((Date) event.getNewValue());
		}
		validaVisualizacaoCamposBuscaAvancada();
	}
	
	public void changeDataInicioPeriodoReservaLegal(ValueChangeEvent event) {
		if(!Util.isNullOuVazio(event.getNewValue())){
			filtroImovelRuralRelatorioDTO.setDtaInicioPeriodoReservaLegal((Date) event.getNewValue());
		}	
		validaVisualizacaoCamposBuscaAvancada();
	}
	
	public void changeDataFimPeriodoReservaLegal(ValueChangeEvent event) {
		if(!Util.isNullOuVazio(event.getNewValue())){
			filtroImovelRuralRelatorioDTO.setDtaFimPeriodoReservaLegal((Date) event.getNewValue());
		}
		validaVisualizacaoCamposBuscaAvancada();
	}
	
	public void changeModulosFiscais(ValueChangeEvent event) {
		if(!Util.isNullOuVazio(event.getNewValue())){
			filtroImovelRuralRelatorioDTO.setModulosFiscais((Integer.parseInt(event.getNewValue().toString())));
		}
		validaVisualizacaoCamposBuscaAvancada();
	}
	
	public void changeTipoRl(ValueChangeEvent event) {
		if(!Util.isNullOuVazio(event.getNewValue())){
			filtroImovelRuralRelatorioDTO.setTipoArl((Integer.parseInt(event.getNewValue().toString())));
		}
		validaVisualizacaoCamposBuscaAvancada();
	}
	
	public void changeDocumentoFinal(ValueChangeEvent event) {
		if(!Util.isNullOuVazio(event.getNewValue())){
			filtroImovelRuralRelatorioDTO.setDocumentoFinal((Integer.parseInt(event.getNewValue().toString())));
		}
		validaVisualizacaoCamposBuscaAvancada();
	}
	
	public void changeQuestionario(ValueChangeEvent event) {
		if(!Util.isNullOuVazio(event.getNewValue())){
			filtroImovelRuralRelatorioDTO.setQuestionario((Integer.parseInt(event.getNewValue().toString())));
		}
		validaVisualizacaoCamposBuscaAvancada();
	}
	
	
	//Início Validação dos Campos de Busca Avançada
	
	/**
	 * Método responsável por validar a visualizacao dos campos da busca avançada
	 */
	private void validaVisualizacaoCamposBuscaAvancada() {
		//Status Registro Incompleto Selecionado
		if(this.isRegistroIncompleto() && !this.isRegistrado() && !this.isCadastrado()) {
			validaCamposRegistroIncompleto();			
		}
		
		//Status Registrado Selecionado
		if(!this.isRegistroIncompleto() && this.isRegistrado() && !this.isCadastrado()) {
			validaCamposRegistrado();			
		}
		
		//Status Cadastrado Selecionado
		if(!this.isRegistroIncompleto() && !this.isRegistrado() && this.isCadastrado()) {
			validaCamposCadastrado();			
		}
		
		//Nenhum Status Selecionado
		if(!this.isRegistroIncompleto() && !this.isRegistrado() && !this.isCadastrado()) {
			validaCamposNenhumStatus();			
		}
		
		//Todos Status Selecionados
		if(this.isRegistroIncompleto() && this.isRegistrado() && this.isCadastrado()) {
			validaCamposTodosStatus();			
		}
		
		//Status Registro Incompleto e Registrado Selecionados
		if(this.isRegistroIncompleto() && this.isRegistrado() && !this.isCadastrado()) {
			validaCamposRegistroIncompletoRegistrado();			
		}
		
		//Status Registro Incompleto e Cadastrado Selecionados
		if(this.isRegistroIncompleto() && !this.isRegistrado() && this.isCadastrado()) {
			validaCamposRegistroIncompletoCadastrado();			
		}
		
		//Status Registrado e Cadastrado Selecionados
		if(!this.isRegistroIncompleto() && this.isRegistrado() && this.isCadastrado()) {
			validaCamposRegistradoCadastrado();			
		}
	}
	
	/**
	 * Método responsável por validar a visualizacao dos campos da busca avançada
	 * com o filtro Status Registro Incompleto selecionados
	 */
	private void validaCamposRegistroIncompleto() {
		
		//Período da primeira finalização
		setDisablePeriodoFinalizacao(true);
		
		//Período da sincronização
		if(getQuestionarioNaoPreenchido()) {			
			setDisablePeriodoSincronizacao(true);			
			filtroImovelRuralRelatorioDTO.setDtaInicioPeriodoPrimeiraSincronizacao(null);
			filtroImovelRuralRelatorioDTO.setDtaFimPeriodoPrimeiraSincronizacao(null);
		} else {			
			setDisablePeriodoSincronizacao(false);			
		}
		
		//Período de aprovação da Reserva Legal
		setDisablePeriodoReservaLegal(true);
		
		//Gestor financeiro Contrato/Convênio
		if(filtroImovelRuralRelatorioDTO.isCadastroIncra()) {			
			setDisableGestorFinanceiro(true);
			setDisableContratoConvenio(true);			
			filtroImovelRuralRelatorioDTO.setListGestorFinanceiro(new ArrayList<Integer>());
			filtroImovelRuralRelatorioDTO.setListContratoConvenio(new ArrayList<Integer>());
		} else {			
			setDisableGestorFinanceiro(false);
			setDisableContratoConvenio(false);			
		}
		
		//Até 4 módulos
		if(filtroImovelRuralRelatorioDTO.isCadastroBndes()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(AVISO))
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()) {			
			filtroImovelRuralRelatorioDTO.setModulosFiscais(ATE_4_MODULOS);			
		} 
		
		//Acima de 4 módulos
		if(filtroImovelRuralRelatorioDTO.isCadastroBndes()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(AVISO))
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()) {			
			setDisableAcima4Modulos(true);			
			if(filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS)) {
				filtroImovelRuralRelatorioDTO.setModulosFiscais(null);
			}			
		} else {			
			setDisableAcima4Modulos(false);			
		}
		
		//No próprio imóvel
		setDisableNoProprioImovel(false);
		
		//Em compensação entre imóveis de mesmo proprietário
		setDisableEmcompensacaoEntreImoveisMesmoProprietario(false);
		
		//Em condomínio e Em compensação por servidão ambiental
		if(filtroImovelRuralRelatorioDTO.isSobreposicaoRLAPP()
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()
			|| filtroImovelRuralRelatorioDTO.isRlAprovada()) {
			setDisableEmCondominio(true);
			setDisableEmCompensacaoPorServidao(true);
			if((!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
						&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
							|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
				filtroImovelRuralRelatorioDTO.setTipoArl(null);
			}
		} else {			
			setDisableEmCondominio(false);
			setDisableEmCompensacaoPorServidao(false);			
		}
		
		//Aviso
		setDisableAviso(true);
				
		//Termo decompromisso
		setDisableTermoCompromisso(true);
				
		//Certificado de Inscrição
		setDisableCertificado(true);
		
		filtroImovelRuralRelatorioDTO.setDocumentoFinal(null);
				
		//Questionário Preenchido e Questionário Não Preenchido
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl())
			|| filtroImovelRuralRelatorioDTO.isPraCadastrado()
			|| filtroImovelRuralRelatorioDTO.isOutroPassivoAmbiental()
			|| filtroImovelRuralRelatorioDTO.isSobreposicaoRLAPP()
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()
			|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao())
			|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao())
			|| filtroImovelRuralRelatorioDTO.isRlAprovada()
			|| filtroImovelRuralRelatorioDTO.isNumeroCar()) {
			filtroImovelRuralRelatorioDTO.setQuestionario(PREENCHIDO);
			setDisableQuestionarioNaoPreenchido(true);
		} else {
			setDisableQuestionarioNaoPreenchido(false);			
		}
		
		//BNDES
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(AVISO)) {
			filtroImovelRuralRelatorioDTO.setCadastroBndes(true);
		}
		
		if((!Util.isNull(filtroImovelRuralRelatorioDTO.getModulosFiscais()) && filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS))
			|| filtroImovelRuralRelatorioDTO.isCadastroIncra()
			|| filtroImovelRuralRelatorioDTO.isCadastroCda()) {
			setDisableBndes(true);
			filtroImovelRuralRelatorioDTO.setCadastroBndes(false);			
		} else {
			setDisableBndes(false);
		}
		
		//CDA
		if(filtroImovelRuralRelatorioDTO.isCadastroBndes() || filtroImovelRuralRelatorioDTO.isCadastroIncra()) {
			setDisableCda(true);
			filtroImovelRuralRelatorioDTO.setCadastroCda(false);			
		} else {
			setDisableCda(false);
		}
		
		//INCRA
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListGestorFinanceiro())
			|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListContratoConvenio())
			|| filtroImovelRuralRelatorioDTO.isCadastroBndes()
			|| filtroImovelRuralRelatorioDTO.isCadastroCda()) {
			setDisableIncra(true);
			filtroImovelRuralRelatorioDTO.setCadastroIncra(false);			
		} else {
			setDisableIncra(false);
		}
		
		//Imóvel Rural
		if(filtroImovelRuralRelatorioDTO.isModuloAssentamento() || filtroImovelRuralRelatorioDTO.isModuloPCT()) {
			setDisableImovelRural(true);
			filtroImovelRuralRelatorioDTO.setModuloImovelRural(false);
		} else {
			setDisableImovelRural(false);
		}
		
		//Assentamento
		if(filtroImovelRuralRelatorioDTO.isModuloImovelRural() || filtroImovelRuralRelatorioDTO.isModuloPCT()) {
			setDisableAssentamento(true);
			filtroImovelRuralRelatorioDTO.setModuloAssentamento(false);
		} else {
			setDisableAssentamento(false);
		}
		
		//PCT
		if(filtroImovelRuralRelatorioDTO.isModuloAssentamento() || filtroImovelRuralRelatorioDTO.isModuloImovelRural()) {
			setDisablePCT(true);
			filtroImovelRuralRelatorioDTO.setModuloPCT(false);
		} else {
			setDisablePCT(false);
		}
		
		//PRA Cadastrado
		if(getQuestionarioNaoPreenchido()) {
			setDisablePraCadastrado(true);
			filtroImovelRuralRelatorioDTO.setPraCadastrado(false);
		} else {
			setDisablePraCadastrado(false);
		}
		
		//Número CAR
		if(getQuestionarioNaoPreenchido()) {
			setDisableNumeroCar(true);
			filtroImovelRuralRelatorioDTO.setNumeroCar(false);
		} else {
			if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao())
				|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao())) {
				filtroImovelRuralRelatorioDTO.setNumeroCar(true);				
			}
			setDisableNumeroCar(false);
		}		
		
		//RL Aprovada
		if(getQuestionarioNaoPreenchido()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
						&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
							|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
			setDisableRlAprovada(true);
			filtroImovelRuralRelatorioDTO.setRlAprovada(false);
		} else {
			setDisableRlAprovada(false);
		}
		
		//Área de RL menor que 20% da área do imóvel rural
		if(getQuestionarioNaoPreenchido()
			||(!Util.isNull(filtroImovelRuralRelatorioDTO.getModulosFiscais()) && filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS))
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
						&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
							|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
			setDisableAreaRlMenorQue20PorCento(true);
			filtroImovelRuralRelatorioDTO.setRlMenorQueVintePorCento(false);
		} else {
			setDisableAreaRlMenorQue20PorCento(false);
		}

		//Sobreposição de RL e APP
		if(getQuestionarioNaoPreenchido()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
						&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
							|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
			setDisableSobreposicaoRlApp(true);
			filtroImovelRuralRelatorioDTO.setSobreposicaoRLAPP(false);
		} else {
			setDisableSobreposicaoRlApp(false);
		}	
				
		//Outro passivo ambiental fora de APP e RL
		if(getQuestionarioNaoPreenchido()) {
			setDisableOutroPassivoAmbiental(true);
			filtroImovelRuralRelatorioDTO.setOutroPassivoAmbiental(false);
		} else {
			setDisableOutroPassivoAmbiental(false);
		}
		
		// Limite Bloqueado
		if (getQuestionarioNaoPreenchido()) {
			setDisableLimiteBloqueado(true);
			filtroImovelRuralRelatorioDTO.setLimiteBloqueado(false);
		} else {
			setDisableLimiteBloqueado(false);
		}
	}

	/**
	 * Método responsável por validar a visualizacao dos campos da busca avançada
	 * com o filtro Status Registrado selecionado
	 */
	private void validaCamposRegistrado() {
		
		//Período da primeira finalização
		setDisablePeriodoFinalizacao(false);
		
		//Período da sincronização
		if(getQuestionarioNaoPreenchido()) {			
			setDisablePeriodoSincronizacao(true);
			filtroImovelRuralRelatorioDTO.setDtaInicioPeriodoPrimeiraSincronizacao(null);
			filtroImovelRuralRelatorioDTO.setDtaFimPeriodoPrimeiraSincronizacao(null);
		} else {			
			setDisablePeriodoSincronizacao(false);			
		}
		
		//Período de aprovação da Reserva Legal
		setDisablePeriodoReservaLegal(true);
		
		//Gestor financeiro Contrato/Convênio
		if(filtroImovelRuralRelatorioDTO.isCadastroIncra()) {			
			setDisableGestorFinanceiro(true);
			setDisableContratoConvenio(true);			
			filtroImovelRuralRelatorioDTO.setListGestorFinanceiro(new ArrayList<Integer>());
			filtroImovelRuralRelatorioDTO.setListContratoConvenio(new ArrayList<Integer>());
		} else {			
			setDisableGestorFinanceiro(false);
			setDisableContratoConvenio(false);			
		}
		
		//Até 4 módulos
		if(filtroImovelRuralRelatorioDTO.isCadastroBndes()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(AVISO))
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()
			|| getQuestionarioNaoPreenchido()) {			
			filtroImovelRuralRelatorioDTO.setModulosFiscais(ATE_4_MODULOS);			
		} 
		
		//Acima de 4 módulos
		if(filtroImovelRuralRelatorioDTO.isCadastroBndes()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(AVISO))
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()
			|| getQuestionarioNaoPreenchido()) {
			setDisableAcima4Modulos(true);			
			if(filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS)) {
				filtroImovelRuralRelatorioDTO.setModulosFiscais(null);
			}			
		} else {			
			setDisableAcima4Modulos(false);			
		}
		
		//No próprio imóvel
		setDisableNoProprioImovel(false);
		
		//Em compensação entre imóveis de mesmo proprietário
		setDisableEmcompensacaoEntreImoveisMesmoProprietario(false);
		
		//Em condomínio e Em compensação por servidão ambiental
		if(filtroImovelRuralRelatorioDTO.isSobreposicaoRLAPP()
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()
			|| filtroImovelRuralRelatorioDTO.isRlAprovada()) {
			setDisableEmCondominio(true);
			setDisableEmCompensacaoPorServidao(true);			
			if((!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
					&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
						|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
				filtroImovelRuralRelatorioDTO.setTipoArl(null);
			}			
		} else {			
			setDisableEmCondominio(false);
			setDisableEmCompensacaoPorServidao(false);			
		}
		
		//Aviso
		if((!Util.isNull(filtroImovelRuralRelatorioDTO.getModulosFiscais()) 
				&& filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS))
			|| getQuestionarioNaoPreenchido()) {			
			setDisableAviso(true);			
			if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal())
				&& filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(AVISO)) {				
				filtroImovelRuralRelatorioDTO.setDocumentoFinal(null);				
			}
		} else {			
			setDisableAviso(false);			
		}
		
		//Termo decompromisso
		setDisableTermoCompromisso(true);			
		
		//Certificado de Inscrição
		setDisableCertificado(true);
	
		//Questionário Preenchido e Questionário Não Preenchido
		if((!Util.isNull(filtroImovelRuralRelatorioDTO.getModulosFiscais()) 
				&& filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS))
			|| !Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl())
			|| filtroImovelRuralRelatorioDTO.isPraCadastrado()
			|| filtroImovelRuralRelatorioDTO.isOutroPassivoAmbiental()
			|| filtroImovelRuralRelatorioDTO.isSobreposicaoRLAPP()
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()
			|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao())
			|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao())
			|| filtroImovelRuralRelatorioDTO.isRlAprovada()
			|| filtroImovelRuralRelatorioDTO.isNumeroCar()) {
			filtroImovelRuralRelatorioDTO.setQuestionario(PREENCHIDO);
			setDisableQuestionarioNaoPreenchido(true);
		} else {
			setDisableQuestionarioNaoPreenchido(false);			
		}
		
		//BNDES
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(AVISO)) {
			filtroImovelRuralRelatorioDTO.setCadastroBndes(true);
		}
		
		if((!Util.isNull(filtroImovelRuralRelatorioDTO.getModulosFiscais()) && filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS))
			|| filtroImovelRuralRelatorioDTO.isCadastroIncra()
			|| filtroImovelRuralRelatorioDTO.isCadastroCda()) {
			setDisableBndes(true);
			filtroImovelRuralRelatorioDTO.setCadastroBndes(false);			
		} else {
			setDisableBndes(false);
		}
		
		//CDA
		if(filtroImovelRuralRelatorioDTO.isCadastroBndes() || filtroImovelRuralRelatorioDTO.isCadastroIncra()) {
			setDisableCda(true);
			filtroImovelRuralRelatorioDTO.setCadastroCda(false);			
		} else {
			setDisableCda(false);
		}
		
		//INCRA
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListGestorFinanceiro())
			|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListContratoConvenio())
			|| filtroImovelRuralRelatorioDTO.isCadastroBndes()
			|| filtroImovelRuralRelatorioDTO.isCadastroCda()) {
			setDisableIncra(true);
			filtroImovelRuralRelatorioDTO.setCadastroIncra(false);			
		} else {
			setDisableIncra(false);
		}
		
		//Imóvel Rural
		if(filtroImovelRuralRelatorioDTO.isModuloAssentamento() || filtroImovelRuralRelatorioDTO.isModuloPCT()) {
			setDisableImovelRural(true);
			filtroImovelRuralRelatorioDTO.setModuloImovelRural(false);
		} else {
			setDisableImovelRural(false);
		}
		
		//Assentamento
		if(filtroImovelRuralRelatorioDTO.isModuloImovelRural() || filtroImovelRuralRelatorioDTO.isModuloPCT()) {
			setDisableAssentamento(true);
			filtroImovelRuralRelatorioDTO.setModuloAssentamento(false);
		} else {
			setDisableAssentamento(false);
		}
		
		//PCT
		if(filtroImovelRuralRelatorioDTO.isModuloAssentamento() || filtroImovelRuralRelatorioDTO.isModuloImovelRural()) {
			setDisablePCT(true);
			filtroImovelRuralRelatorioDTO.setModuloPCT(false);
		} else {
			setDisablePCT(false);
		}
		
		//PRA Cadastrado
		if(getQuestionarioNaoPreenchido()) {
			setDisablePraCadastrado(true);
			filtroImovelRuralRelatorioDTO.setPraCadastrado(false);
		} else {
			setDisablePraCadastrado(false);
		}
		
		//Número CAR
		if(getQuestionarioNaoPreenchido()) {
			setDisableNumeroCar(true);
			filtroImovelRuralRelatorioDTO.setNumeroCar(false);
		} else {
			if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao())
				|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao())) {
				filtroImovelRuralRelatorioDTO.setNumeroCar(true);				
			}
			setDisableNumeroCar(false);
		}		
		
		//RL Aprovada
		if(getQuestionarioNaoPreenchido()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
						&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
							|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
			setDisableRlAprovada(true);
			filtroImovelRuralRelatorioDTO.setRlAprovada(false);
		} else {
			setDisableRlAprovada(false);
		}
		
		//Área de RL menor que 20% da área do imóvel rural
		if(getQuestionarioNaoPreenchido()
			||(!Util.isNull(filtroImovelRuralRelatorioDTO.getModulosFiscais()) && filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS))
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
						&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
							|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
			setDisableAreaRlMenorQue20PorCento(true);
			filtroImovelRuralRelatorioDTO.setRlMenorQueVintePorCento(false);
		} else {
			setDisableAreaRlMenorQue20PorCento(false);
		}

		//Sobreposição de RL e APP
		if(getQuestionarioNaoPreenchido()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
						&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
							|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
			setDisableSobreposicaoRlApp(true);
			filtroImovelRuralRelatorioDTO.setSobreposicaoRLAPP(false);
		} else {
			setDisableSobreposicaoRlApp(false);
		}	
				
		//Outro passivo ambiental fora de APP e RL
		if(getQuestionarioNaoPreenchido()) {
			setDisableOutroPassivoAmbiental(true);
			filtroImovelRuralRelatorioDTO.setOutroPassivoAmbiental(false);
		} else {
			setDisableOutroPassivoAmbiental(false);
		}
		
		// Limite Bloqueado
		if (getQuestionarioNaoPreenchido()) {
			setDisableLimiteBloqueado(true);
			filtroImovelRuralRelatorioDTO.setLimiteBloqueado(false);
		} else {
			setDisableLimiteBloqueado(false);
		}
		
	}

	/**
	 * Método responsável por validar a visualizacao dos campos da busca avançada
	 * com o filtro Status Cadastrado selecionados
	 */
	private void validaCamposCadastrado() {
		
		//Período da primeira finalização
		setDisablePeriodoFinalizacao(false);
		
		//Período da sincronização
		setDisablePeriodoSincronizacao(false);			
		
		//Gestor financeiro Contrato/Convênio
		if(filtroImovelRuralRelatorioDTO.isCadastroIncra()) {			
			setDisableGestorFinanceiro(true);
			setDisableContratoConvenio(true);			
			filtroImovelRuralRelatorioDTO.setListGestorFinanceiro(new ArrayList<Integer>());
			filtroImovelRuralRelatorioDTO.setListContratoConvenio(new ArrayList<Integer>());
		} else {			
			setDisableGestorFinanceiro(false);
			setDisableContratoConvenio(false);			
		}
		
		//Período de aprovação da Reserva Legal
		setDisablePeriodoReservaLegal(false);
		
		//Até 4 módulos
		if(filtroImovelRuralRelatorioDTO.isCadastroBndes()
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()) {			
			filtroImovelRuralRelatorioDTO.setModulosFiscais(ATE_4_MODULOS);			
		} 
		
		//Acima de 4 módulos
		if(filtroImovelRuralRelatorioDTO.isCadastroBndes()
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()) {			
			setDisableAcima4Modulos(true);			
			if(filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS)) {
				filtroImovelRuralRelatorioDTO.setModulosFiscais(null);
			}			
		} else {			
			setDisableAcima4Modulos(false);			
		}
		
		//No próprio imóvel
		setDisableNoProprioImovel(false);
		
		//Em compensação entre imóveis de mesmo proprietário
		setDisableEmcompensacaoEntreImoveisMesmoProprietario(false);
		
		//Em condomínio e Em compensação por servidão ambiental
		setDisableEmCondominio(true);
		setDisableEmCompensacaoPorServidao(true);		
		
		//Aviso
		setDisableAviso(true);			
		
		//Termo decompromisso
		setDisableTermoCompromisso(false);			
		
		if(filtroImovelRuralRelatorioDTO.isPraCadastrado()
			|| filtroImovelRuralRelatorioDTO.isOutroPassivoAmbiental()) {			
			filtroImovelRuralRelatorioDTO.setDocumentoFinal(TERMO_COMPROMISSO);			
		}
		
		//Certificado de Inscrição
		if(filtroImovelRuralRelatorioDTO.isPraCadastrado()
			|| filtroImovelRuralRelatorioDTO.isOutroPassivoAmbiental()) {
			setDisableCertificado(true);
			if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) 
				&& filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(CERTIFICADO)) {				
				filtroImovelRuralRelatorioDTO.setDocumentoFinal(null);				
			}
		} else {
			setDisableCertificado(false);
		}
		
		//Questionário Preenchido e Questionário Não Preenchido
		setDisableQuestionarioPreenchido(false);
		filtroImovelRuralRelatorioDTO.setQuestionario(PREENCHIDO);
		setDisableQuestionarioNaoPreenchido(true);
		
		
		//BNDES
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(AVISO)) {
			filtroImovelRuralRelatorioDTO.setCadastroBndes(true);
		}
		
		if((!Util.isNull(filtroImovelRuralRelatorioDTO.getModulosFiscais()) && filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS))
			|| filtroImovelRuralRelatorioDTO.isCadastroIncra()
			|| filtroImovelRuralRelatorioDTO.isCadastroCda()) {
			setDisableBndes(true);
			filtroImovelRuralRelatorioDTO.setCadastroBndes(false);			
		} else {
			setDisableBndes(false);
		}
		
		//CDA
		if(filtroImovelRuralRelatorioDTO.isCadastroBndes() || filtroImovelRuralRelatorioDTO.isCadastroIncra()) {
			setDisableCda(true);
			filtroImovelRuralRelatorioDTO.setCadastroCda(false);			
		} else {
			setDisableCda(false);
		}
		
		//INCRA
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListGestorFinanceiro())
			|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListContratoConvenio())
			|| filtroImovelRuralRelatorioDTO.isCadastroBndes()
			|| filtroImovelRuralRelatorioDTO.isCadastroCda()) {
			setDisableIncra(true);
			filtroImovelRuralRelatorioDTO.setCadastroIncra(false);			
		} else {
			setDisableIncra(false);
		}
		
		//Imóvel Rural
		if(filtroImovelRuralRelatorioDTO.isModuloAssentamento() || filtroImovelRuralRelatorioDTO.isModuloPCT()) {
			setDisableImovelRural(true);
			filtroImovelRuralRelatorioDTO.setModuloImovelRural(false);
		} else {
			setDisableImovelRural(false);
		}
		
		//Assentamento
		if(filtroImovelRuralRelatorioDTO.isModuloImovelRural() || filtroImovelRuralRelatorioDTO.isModuloPCT()) {
			setDisableAssentamento(true);
			filtroImovelRuralRelatorioDTO.setModuloAssentamento(false);
		} else {
			setDisableAssentamento(false);
		}
		
		//PCT
		if(filtroImovelRuralRelatorioDTO.isModuloAssentamento() || filtroImovelRuralRelatorioDTO.isModuloImovelRural()) {
			setDisablePCT(true);
			filtroImovelRuralRelatorioDTO.setModuloPCT(false);
		} else {
			setDisablePCT(false);
		}
		
		//PRA Cadastrado
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(CERTIFICADO)) {
			setDisablePraCadastrado(true);
			filtroImovelRuralRelatorioDTO.setPraCadastrado(false);
		} else {
			setDisablePraCadastrado(false);
		}
		
		//Número CAR
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao())
			|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao())) {
			filtroImovelRuralRelatorioDTO.setNumeroCar(true);				
			setDisableNumeroCar(true);
		} else {
			setDisableNumeroCar(false);
		}
		
		//RL Aprovada
		setDisableRlAprovada(false);
		
		//Área de RL menor que 20% da área do imóvel rural
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getModulosFiscais()) && filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS)) {
			setDisableAreaRlMenorQue20PorCento(true);
			filtroImovelRuralRelatorioDTO.setRlMenorQueVintePorCento(false);
		} else {
			setDisableAreaRlMenorQue20PorCento(false);
		}

		//Sobreposição de RL e APP
		setDisableSobreposicaoRlApp(false);
				
		//Outro passivo ambiental fora de APP e RL
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(CERTIFICADO)) {
			setDisableOutroPassivoAmbiental(true);
			filtroImovelRuralRelatorioDTO.setOutroPassivoAmbiental(false);
		} else {
			setDisableOutroPassivoAmbiental(false);
		}
		
	}

	/**
	 * Método responsável por validar a visualizacao dos campos da busca avançada
	 * com nenhum o filtro Status selecionado
	 */
	private void validaCamposNenhumStatus() {
		
		//Período da primeira finalização
		setDisablePeriodoFinalizacao(false);
		
		//Período da sincronização
		if(getQuestionarioNaoPreenchido()) {			
			setDisablePeriodoSincronizacao(true);
			filtroImovelRuralRelatorioDTO.setDtaInicioPeriodoPrimeiraSincronizacao(null);
			filtroImovelRuralRelatorioDTO.setDtaFimPeriodoPrimeiraSincronizacao(null);
		} else {			
			setDisablePeriodoSincronizacao(false);			
		}
		
		//Período de aprovação da Reserva Legal
		setDisablePeriodoReservaLegal(false);
		
		//Gestor financeiro Contrato/Convênio
		if(filtroImovelRuralRelatorioDTO.isCadastroIncra()) {			
			setDisableGestorFinanceiro(true);
			setDisableContratoConvenio(true);			
			filtroImovelRuralRelatorioDTO.setListGestorFinanceiro(new ArrayList<Integer>());
			filtroImovelRuralRelatorioDTO.setListContratoConvenio(new ArrayList<Integer>());
		} else {			
			setDisableGestorFinanceiro(false);
			setDisableContratoConvenio(false);			
		}
		
		//Até 4 módulos
		if(filtroImovelRuralRelatorioDTO.isCadastroBndes()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(AVISO))
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()) {			
			filtroImovelRuralRelatorioDTO.setModulosFiscais(ATE_4_MODULOS);			
		} 
		
		//Acima de 4 módulos
		if(filtroImovelRuralRelatorioDTO.isCadastroBndes()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(AVISO))
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()) {			
			setDisableAcima4Modulos(true);			
			if(filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS)) {
				filtroImovelRuralRelatorioDTO.setModulosFiscais(null);
			}			
		} else {			
			setDisableAcima4Modulos(false);			
		}
		
		//No próprio imóvel
		setDisableNoProprioImovel(false);
		
		//Em compensação entre imóveis de mesmo proprietário
		setDisableEmcompensacaoEntreImoveisMesmoProprietario(false);
		
		//Em condomínio e Em compensação por servidão ambiental
		if(filtroImovelRuralRelatorioDTO.isSobreposicaoRLAPP()
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()
			|| filtroImovelRuralRelatorioDTO.isRlAprovada()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(TERMO_COMPROMISSO))
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(CERTIFICADO))) {
			setDisableEmCondominio(true);
			setDisableEmCompensacaoPorServidao(true);			
			if((!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
					&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
						|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
				filtroImovelRuralRelatorioDTO.setTipoArl(null);
			}			
		} else {			
			setDisableEmCondominio(false);
			setDisableEmCompensacaoPorServidao(false);			
		}
		
		//Aviso
		if((!Util.isNull(filtroImovelRuralRelatorioDTO.getModulosFiscais()) 
				&& filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS))
			|| getQuestionarioNaoPreenchido()) {			
			setDisableAviso(true);			
			if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal())
				&& filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(AVISO)) {				
				filtroImovelRuralRelatorioDTO.setDocumentoFinal(null);				
			}
		} else {			
			setDisableAviso(false);			
		}
		
		//Termo decompromisso
		if(getQuestionarioNaoPreenchido()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
					&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
						|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {			
			setDisableTermoCompromisso(true);			
			if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) 
				&& filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(TERMO_COMPROMISSO)) {				
				filtroImovelRuralRelatorioDTO.setDocumentoFinal(null);				
			}
		} else {			
			setDisableTermoCompromisso(false);			
		}
		
		if(filtroImovelRuralRelatorioDTO.isPraCadastrado()
			|| filtroImovelRuralRelatorioDTO.isOutroPassivoAmbiental()) {			
			filtroImovelRuralRelatorioDTO.setDocumentoFinal(TERMO_COMPROMISSO);			
		}
		
		//Certificado de Inscrição
		if(getQuestionarioNaoPreenchido()
				|| filtroImovelRuralRelatorioDTO.isPraCadastrado()
				|| filtroImovelRuralRelatorioDTO.isOutroPassivoAmbiental()				
				|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
						&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
							|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
			setDisableCertificado(true);
			if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) 
				&& filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(CERTIFICADO)) {				
				filtroImovelRuralRelatorioDTO.setDocumentoFinal(null);				
			}
		} else {
			setDisableCertificado(false);
		}
		
		//Questionário Preenchido e Questionário Não Preenchido
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl())
			|| filtroImovelRuralRelatorioDTO.isPraCadastrado()
			|| filtroImovelRuralRelatorioDTO.isOutroPassivoAmbiental()
			|| filtroImovelRuralRelatorioDTO.isSobreposicaoRLAPP()
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()
			|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao())
			|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao())
			|| filtroImovelRuralRelatorioDTO.isRlAprovada()
			|| filtroImovelRuralRelatorioDTO.isNumeroCar()) {
			filtroImovelRuralRelatorioDTO.setQuestionario(PREENCHIDO);
			setDisableQuestionarioNaoPreenchido(true);
		} else {
			setDisableQuestionarioNaoPreenchido(false);			
		}
		
		//BNDES
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(AVISO)) {
			filtroImovelRuralRelatorioDTO.setCadastroBndes(true);
		}
		
		if((!Util.isNull(filtroImovelRuralRelatorioDTO.getModulosFiscais()) && filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS))
			|| filtroImovelRuralRelatorioDTO.isCadastroIncra()
			|| filtroImovelRuralRelatorioDTO.isCadastroCda()) {
			setDisableBndes(true);
			filtroImovelRuralRelatorioDTO.setCadastroBndes(false);			
		} else {
			setDisableBndes(false);
		}
		
		//CDA
		if(filtroImovelRuralRelatorioDTO.isCadastroBndes() || filtroImovelRuralRelatorioDTO.isCadastroIncra()) {
			setDisableCda(true);
			filtroImovelRuralRelatorioDTO.setCadastroCda(false);			
		} else {
			setDisableCda(false);
		}
		
		//INCRA
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListGestorFinanceiro())
			|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListContratoConvenio())
			|| filtroImovelRuralRelatorioDTO.isCadastroBndes()
			|| filtroImovelRuralRelatorioDTO.isCadastroCda()) {
			setDisableIncra(true);
			filtroImovelRuralRelatorioDTO.setCadastroIncra(false);			
		} else {
			setDisableIncra(false);
		}
		
		//Imóvel Rural
		if(filtroImovelRuralRelatorioDTO.isModuloAssentamento() || filtroImovelRuralRelatorioDTO.isModuloPCT()) {
			setDisableImovelRural(true);
			filtroImovelRuralRelatorioDTO.setModuloImovelRural(false);
		} else {
			setDisableImovelRural(false);
		}
		
		//Assentamento
		if(filtroImovelRuralRelatorioDTO.isModuloImovelRural() || filtroImovelRuralRelatorioDTO.isModuloPCT()) {
			setDisableAssentamento(true);
			filtroImovelRuralRelatorioDTO.setModuloAssentamento(false);
		} else {
			setDisableAssentamento(false);
		}
		
		//PCT
		if(filtroImovelRuralRelatorioDTO.isModuloAssentamento() || filtroImovelRuralRelatorioDTO.isModuloImovelRural()) {
			setDisablePCT(true);
			filtroImovelRuralRelatorioDTO.setModuloPCT(false);
		} else {
			setDisablePCT(false);
		}
		
		//PRA Cadastrado
		if(getQuestionarioNaoPreenchido()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(CERTIFICADO))) {
			setDisablePraCadastrado(true);
			filtroImovelRuralRelatorioDTO.setPraCadastrado(false);
		} else {
			setDisablePraCadastrado(false);
		}
		
		//Número CAR
		if(getQuestionarioNaoPreenchido()) {
			setDisableNumeroCar(true);
			filtroImovelRuralRelatorioDTO.setNumeroCar(false);
		} else {
			if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao())
				|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao())) {
				filtroImovelRuralRelatorioDTO.setNumeroCar(true);				
			}
			setDisableNumeroCar(false);
		}		
		
		//RL Aprovada
		if(getQuestionarioNaoPreenchido()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
						&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
							|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
			setDisableRlAprovada(true);
			filtroImovelRuralRelatorioDTO.setRlAprovada(false);
		} else {
			setDisableRlAprovada(false);
		}
		
		//Área de RL menor que 20% da área do imóvel rural
		if(getQuestionarioNaoPreenchido()
			||(!Util.isNull(filtroImovelRuralRelatorioDTO.getModulosFiscais()) && filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS))
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
						&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
							|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
			setDisableAreaRlMenorQue20PorCento(true);
			filtroImovelRuralRelatorioDTO.setRlMenorQueVintePorCento(false);
		} else {
			setDisableAreaRlMenorQue20PorCento(false);
		}

		//Sobreposição de RL e APP
		if(getQuestionarioNaoPreenchido()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
						&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
							|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
			setDisableSobreposicaoRlApp(true);
			filtroImovelRuralRelatorioDTO.setSobreposicaoRLAPP(false);
		} else {
			setDisableSobreposicaoRlApp(false);
		}	
				
		//Outro passivo ambiental fora de APP e RL
		if(getQuestionarioNaoPreenchido()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(CERTIFICADO))) {
			setDisableOutroPassivoAmbiental(true);
			filtroImovelRuralRelatorioDTO.setOutroPassivoAmbiental(false);
		} else {
			setDisableOutroPassivoAmbiental(false);
		}

		// Limite Bloqueado
		if (getQuestionarioNaoPreenchido()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(CERTIFICADO))){
			setDisableLimiteBloqueado(true);
			filtroImovelRuralRelatorioDTO.setLimiteBloqueado(false);
		} else {
			setDisableLimiteBloqueado(false);
		}

	}

	/**
	 * Método responsável por validar a visualizacao dos campos da busca avançada
	 * com todos filtro Status selecionados
	 */
	private void validaCamposTodosStatus() {
		
		//Período da primeira finalização
		setDisablePeriodoFinalizacao(false);
		
		//Período da sincronização
		if(getQuestionarioNaoPreenchido()) {			
			setDisablePeriodoSincronizacao(true);
			filtroImovelRuralRelatorioDTO.setDtaInicioPeriodoPrimeiraSincronizacao(null);
			filtroImovelRuralRelatorioDTO.setDtaFimPeriodoPrimeiraSincronizacao(null);
		} else {			
			setDisablePeriodoSincronizacao(false);			
		}
		
		//Período de aprovação da Reserva Legal
		setDisablePeriodoReservaLegal(false);
		
		//Gestor financeiro Contrato/Convênio
		if(filtroImovelRuralRelatorioDTO.isCadastroIncra()) {			
			setDisableGestorFinanceiro(true);
			setDisableContratoConvenio(true);			
			filtroImovelRuralRelatorioDTO.setListGestorFinanceiro(new ArrayList<Integer>());
			filtroImovelRuralRelatorioDTO.setListContratoConvenio(new ArrayList<Integer>());
		} else {			
			setDisableGestorFinanceiro(false);
			setDisableContratoConvenio(false);			
		}
		
		//Até 4 módulos
		if(filtroImovelRuralRelatorioDTO.isCadastroBndes()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(AVISO))
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()) {			
			filtroImovelRuralRelatorioDTO.setModulosFiscais(ATE_4_MODULOS);			
		} 
		
		//Acima de 4 módulos
		if(filtroImovelRuralRelatorioDTO.isCadastroBndes()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(AVISO))
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()) {			
			setDisableAcima4Modulos(true);			
			if(filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS)) {
				filtroImovelRuralRelatorioDTO.setModulosFiscais(null);
			}			
		} else {			
			setDisableAcima4Modulos(false);			
		}
		
		//No próprio imóvel
		setDisableNoProprioImovel(false);
		
		//Em compensação entre imóveis de mesmo proprietário
		setDisableEmcompensacaoEntreImoveisMesmoProprietario(false);
		
		//Em condomínio e Em compensação por servidão ambiental
		if(filtroImovelRuralRelatorioDTO.isSobreposicaoRLAPP()
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()
			|| filtroImovelRuralRelatorioDTO.isRlAprovada()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(TERMO_COMPROMISSO))
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(CERTIFICADO))) {
			setDisableEmCondominio(true);
			setDisableEmCompensacaoPorServidao(true);			
			if((!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
					&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
						|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
				filtroImovelRuralRelatorioDTO.setTipoArl(null);
			}			
		} else {			
			setDisableEmCondominio(false);
			setDisableEmCompensacaoPorServidao(false);			
		}
		
		//Aviso
		if((!Util.isNull(filtroImovelRuralRelatorioDTO.getModulosFiscais()) 
				&& filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS))
			|| getQuestionarioNaoPreenchido()) {			
			setDisableAviso(true);			
			if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal())
				&& filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(AVISO)) {				
				filtroImovelRuralRelatorioDTO.setDocumentoFinal(null);				
			}
		} else {			
			setDisableAviso(false);			
		}
		
		//Termo decompromisso
		if(getQuestionarioNaoPreenchido()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
					&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
						|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {			
			setDisableTermoCompromisso(true);			
			if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) 
				&& filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(TERMO_COMPROMISSO)) {				
				filtroImovelRuralRelatorioDTO.setDocumentoFinal(null);				
			}
		} else {			
			setDisableTermoCompromisso(false);			
		}
		
		if(filtroImovelRuralRelatorioDTO.isPraCadastrado()
			|| filtroImovelRuralRelatorioDTO.isOutroPassivoAmbiental()) {			
			filtroImovelRuralRelatorioDTO.setDocumentoFinal(TERMO_COMPROMISSO);			
		}
		
		//Certificado de Inscrição
		if(getQuestionarioNaoPreenchido()
				|| filtroImovelRuralRelatorioDTO.isPraCadastrado()
				|| filtroImovelRuralRelatorioDTO.isOutroPassivoAmbiental()				
				|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
						&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
							|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
			setDisableCertificado(true);
			if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) 
				&& filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(CERTIFICADO)) {				
				filtroImovelRuralRelatorioDTO.setDocumentoFinal(null);				
			}
		} else {
			setDisableCertificado(false);
		}
		
		//Questionário Preenchido e Questionário Não Preenchido
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl())
			|| filtroImovelRuralRelatorioDTO.isPraCadastrado()
			|| filtroImovelRuralRelatorioDTO.isOutroPassivoAmbiental()
			|| filtroImovelRuralRelatorioDTO.isSobreposicaoRLAPP()
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()
			|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao())
			|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao())
			|| filtroImovelRuralRelatorioDTO.isRlAprovada()
			|| filtroImovelRuralRelatorioDTO.isNumeroCar()) {
			filtroImovelRuralRelatorioDTO.setQuestionario(PREENCHIDO);
			setDisableQuestionarioNaoPreenchido(true);
		} else {
			setDisableQuestionarioNaoPreenchido(false);			
		}
		
		//BNDES
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(AVISO)) {
			filtroImovelRuralRelatorioDTO.setCadastroBndes(true);
		}
		
		if((!Util.isNull(filtroImovelRuralRelatorioDTO.getModulosFiscais()) && filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS))
			|| filtroImovelRuralRelatorioDTO.isCadastroIncra()
			|| filtroImovelRuralRelatorioDTO.isCadastroCda()) {
			setDisableBndes(true);
			filtroImovelRuralRelatorioDTO.setCadastroBndes(false);			
		} else {
			setDisableBndes(false);
		}
		
		//CDA
		if(filtroImovelRuralRelatorioDTO.isCadastroBndes() || filtroImovelRuralRelatorioDTO.isCadastroIncra()) {
			setDisableCda(true);
			filtroImovelRuralRelatorioDTO.setCadastroCda(false);			
		} else {
			setDisableCda(false);
		}
		
		//INCRA
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListGestorFinanceiro())
			|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListContratoConvenio())
			|| filtroImovelRuralRelatorioDTO.isCadastroBndes()
			|| filtroImovelRuralRelatorioDTO.isCadastroCda()) {
			setDisableIncra(true);
			filtroImovelRuralRelatorioDTO.setCadastroIncra(false);			
		} else {
			setDisableIncra(false);
		}
		
		//Imóvel Rural
		if(filtroImovelRuralRelatorioDTO.isModuloAssentamento() || filtroImovelRuralRelatorioDTO.isModuloPCT()) {
			setDisableImovelRural(true);
			filtroImovelRuralRelatorioDTO.setModuloImovelRural(false);
		} else {
			setDisableImovelRural(false);
		}
		
		//Assentamento
		if(filtroImovelRuralRelatorioDTO.isModuloImovelRural() || filtroImovelRuralRelatorioDTO.isModuloPCT()) {
			setDisableAssentamento(true);
			filtroImovelRuralRelatorioDTO.setModuloAssentamento(false);
		} else {
			setDisableAssentamento(false);
		}
		
		//PCT
		if(filtroImovelRuralRelatorioDTO.isModuloAssentamento() || filtroImovelRuralRelatorioDTO.isModuloImovelRural()) {
			setDisablePCT(true);
			filtroImovelRuralRelatorioDTO.setModuloPCT(false);
		} else {
			setDisablePCT(false);
		}
		
		//PRA Cadastrado
		if(getQuestionarioNaoPreenchido()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(CERTIFICADO))) {
			setDisablePraCadastrado(true);
			filtroImovelRuralRelatorioDTO.setPraCadastrado(false);
		} else {
			setDisablePraCadastrado(false);
		}
		
		//Número CAR
		if(getQuestionarioNaoPreenchido()) {
			setDisableNumeroCar(true);
			filtroImovelRuralRelatorioDTO.setNumeroCar(false);
		} else {
			if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao())
				|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao())) {
				filtroImovelRuralRelatorioDTO.setNumeroCar(true);				
			}
			setDisableNumeroCar(false);
		}		
		
		//RL Aprovada
		if(getQuestionarioNaoPreenchido()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
						&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
							|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
			setDisableRlAprovada(true);
			filtroImovelRuralRelatorioDTO.setRlAprovada(false);
		} else {
			setDisableRlAprovada(false);
		}
		
		//Área de RL menor que 20% da área do imóvel rural
		if(getQuestionarioNaoPreenchido()
			||(!Util.isNull(filtroImovelRuralRelatorioDTO.getModulosFiscais()) && filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS))
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
						&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
							|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
			setDisableAreaRlMenorQue20PorCento(true);
			filtroImovelRuralRelatorioDTO.setRlMenorQueVintePorCento(false);
		} else {
			setDisableAreaRlMenorQue20PorCento(false);
		}

		//Sobreposição de RL e APP
		if(getQuestionarioNaoPreenchido()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
						&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
							|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
			setDisableSobreposicaoRlApp(true);
			filtroImovelRuralRelatorioDTO.setSobreposicaoRLAPP(false);
		} else {
			setDisableSobreposicaoRlApp(false);
		}	
				
		//Outro passivo ambiental fora de APP e RL
		if(getQuestionarioNaoPreenchido()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(CERTIFICADO))) {
			setDisableOutroPassivoAmbiental(true);
			filtroImovelRuralRelatorioDTO.setOutroPassivoAmbiental(false);
		} else {
			setDisableOutroPassivoAmbiental(false);
		}

		// Limite Bloqueado
		if (getQuestionarioNaoPreenchido() 
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(CERTIFICADO))){
			setDisableLimiteBloqueado(true);
			filtroImovelRuralRelatorioDTO.setLimiteBloqueado(false);
		} else {
			setDisableLimiteBloqueado(false);
		}
		
	}

	/**
	 * Método responsável por validar a visualizacao dos campos da busca avançada
	 * com o filtro Status Registro Incompleto e Registrado selecionados
	 */
	private void validaCamposRegistroIncompletoRegistrado() {
		
		//Período da primeira finalização
		setDisablePeriodoFinalizacao(false);
		
		//Período da sincronização
		if(getQuestionarioNaoPreenchido()) {			
			setDisablePeriodoSincronizacao(true);
			filtroImovelRuralRelatorioDTO.setDtaInicioPeriodoPrimeiraSincronizacao(null);
			filtroImovelRuralRelatorioDTO.setDtaFimPeriodoPrimeiraSincronizacao(null);
		} else {			
			setDisablePeriodoSincronizacao(false);			
		}
		
		//Período de aprovação da Reserva Legal
		setDisablePeriodoReservaLegal(true);
		
		//Gestor financeiro Contrato/Convênio
		if(filtroImovelRuralRelatorioDTO.isCadastroIncra()) {			
			setDisableGestorFinanceiro(true);
			setDisableContratoConvenio(true);			
			filtroImovelRuralRelatorioDTO.setListGestorFinanceiro(new ArrayList<Integer>());
			filtroImovelRuralRelatorioDTO.setListContratoConvenio(new ArrayList<Integer>());
		} else {			
			setDisableGestorFinanceiro(false);
			setDisableContratoConvenio(false);			
		}
		
		//Até 4 módulos
		if(filtroImovelRuralRelatorioDTO.isCadastroBndes()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(AVISO))
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()
			|| getQuestionarioNaoPreenchido()) {			
			filtroImovelRuralRelatorioDTO.setModulosFiscais(ATE_4_MODULOS);			
		} 
		
		//Acima de 4 módulos
		if(filtroImovelRuralRelatorioDTO.isCadastroBndes()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(AVISO))
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()
			|| getQuestionarioNaoPreenchido()) {
			setDisableAcima4Modulos(true);			
			if(filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS)) {
				filtroImovelRuralRelatorioDTO.setModulosFiscais(null);
			}			
		} else {			
			setDisableAcima4Modulos(false);			
		}
		
		//No próprio imóvel
		setDisableNoProprioImovel(false);
		
		//Em compensação entre imóveis de mesmo proprietário
		setDisableEmcompensacaoEntreImoveisMesmoProprietario(false);
		
		//Em condomínio e Em compensação por servidão ambiental
		if(filtroImovelRuralRelatorioDTO.isSobreposicaoRLAPP()
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()
			|| filtroImovelRuralRelatorioDTO.isRlAprovada()) {
			setDisableEmCondominio(true);
			setDisableEmCompensacaoPorServidao(true);			
			if((!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
					&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
						|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
				filtroImovelRuralRelatorioDTO.setTipoArl(null);
			}			
		} else {			
			setDisableEmCondominio(false);
			setDisableEmCompensacaoPorServidao(false);			
		}
		
		//Aviso
		if((!Util.isNull(filtroImovelRuralRelatorioDTO.getModulosFiscais()) 
				&& filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS))
			|| getQuestionarioNaoPreenchido()) {			
			setDisableAviso(true);			
			if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal())
				&& filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(AVISO)) {				
				filtroImovelRuralRelatorioDTO.setDocumentoFinal(null);				
			}
		} else {			
			setDisableAviso(false);			
		}
		
		//Termo decompromisso
		setDisableTermoCompromisso(true);			
		
		//Certificado de Inscrição
		setDisableCertificado(true);
	
		//Questionário Preenchido e Questionário Não Preenchido
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl())
			|| filtroImovelRuralRelatorioDTO.isPraCadastrado()
			|| filtroImovelRuralRelatorioDTO.isOutroPassivoAmbiental()
			|| filtroImovelRuralRelatorioDTO.isSobreposicaoRLAPP()
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()
			|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao())
			|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao())
			|| filtroImovelRuralRelatorioDTO.isRlAprovada()
			|| filtroImovelRuralRelatorioDTO.isNumeroCar()) {
			filtroImovelRuralRelatorioDTO.setQuestionario(PREENCHIDO);
			setDisableQuestionarioNaoPreenchido(true);
		} else {
			setDisableQuestionarioNaoPreenchido(false);			
		}
		
		//BNDES
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(AVISO)) {
			filtroImovelRuralRelatorioDTO.setCadastroBndes(true);
		}
		
		if((!Util.isNull(filtroImovelRuralRelatorioDTO.getModulosFiscais()) && filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS))
			|| filtroImovelRuralRelatorioDTO.isCadastroIncra()
			|| filtroImovelRuralRelatorioDTO.isCadastroCda()) {
			setDisableBndes(true);
			filtroImovelRuralRelatorioDTO.setCadastroBndes(false);			
		} else {
			setDisableBndes(false);
		}
		
		//CDA
		if(filtroImovelRuralRelatorioDTO.isCadastroBndes() || filtroImovelRuralRelatorioDTO.isCadastroIncra()) {
			setDisableCda(true);
			filtroImovelRuralRelatorioDTO.setCadastroCda(false);			
		} else {
			setDisableCda(false);
		}
		
		//INCRA
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListGestorFinanceiro())
			|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListContratoConvenio())
			|| filtroImovelRuralRelatorioDTO.isCadastroBndes()
			|| filtroImovelRuralRelatorioDTO.isCadastroCda()) {
			setDisableIncra(true);
			filtroImovelRuralRelatorioDTO.setCadastroIncra(false);			
		} else {
			setDisableIncra(false);
		}
		
		//Imóvel Rural
		if(filtroImovelRuralRelatorioDTO.isModuloAssentamento() || filtroImovelRuralRelatorioDTO.isModuloPCT()) {
			setDisableImovelRural(true);
			filtroImovelRuralRelatorioDTO.setModuloImovelRural(false);
		} else {
			setDisableImovelRural(false);
		}
		
		//Assentamento
		if(filtroImovelRuralRelatorioDTO.isModuloImovelRural() || filtroImovelRuralRelatorioDTO.isModuloPCT()) {
			setDisableAssentamento(true);
			filtroImovelRuralRelatorioDTO.setModuloAssentamento(false);
		} else {
			setDisableAssentamento(false);
		}
		
		//PCT
		if(filtroImovelRuralRelatorioDTO.isModuloAssentamento() || filtroImovelRuralRelatorioDTO.isModuloImovelRural()) {
			setDisablePCT(true);
			filtroImovelRuralRelatorioDTO.setModuloPCT(false);
		} else {
			setDisablePCT(false);
		}
		
		//PRA Cadastrado
		if(getQuestionarioNaoPreenchido()) {
			setDisablePraCadastrado(true);
			filtroImovelRuralRelatorioDTO.setPraCadastrado(false);
		} else {
			setDisablePraCadastrado(false);
		}
		
		//Número CAR
		if(getQuestionarioNaoPreenchido()) {
			setDisableNumeroCar(true);
			filtroImovelRuralRelatorioDTO.setNumeroCar(false);
		} else {
			if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao())
				|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao())) {
				filtroImovelRuralRelatorioDTO.setNumeroCar(true);				
			}
			setDisableNumeroCar(false);
		}		
		
		//RL Aprovada
		if(getQuestionarioNaoPreenchido()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
						&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
							|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
			setDisableRlAprovada(true);
			filtroImovelRuralRelatorioDTO.setRlAprovada(false);
		} else {
			setDisableRlAprovada(false);
		}
		
		//Área de RL menor que 20% da área do imóvel rural
		if(getQuestionarioNaoPreenchido()
			||(!Util.isNull(filtroImovelRuralRelatorioDTO.getModulosFiscais()) && filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS))
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
						&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
							|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
			setDisableAreaRlMenorQue20PorCento(true);
			filtroImovelRuralRelatorioDTO.setRlMenorQueVintePorCento(false);
		} else {
			setDisableAreaRlMenorQue20PorCento(false);
		}

		//Sobreposição de RL e APP
		if(getQuestionarioNaoPreenchido()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
						&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
							|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
			setDisableSobreposicaoRlApp(true);
			filtroImovelRuralRelatorioDTO.setSobreposicaoRLAPP(false);
		} else {
			setDisableSobreposicaoRlApp(false);
		}	
				
		//Outro passivo ambiental fora de APP e RL
		if(getQuestionarioNaoPreenchido()) {
			setDisableOutroPassivoAmbiental(true);
			filtroImovelRuralRelatorioDTO.setOutroPassivoAmbiental(false);
		} else {
			setDisableOutroPassivoAmbiental(false);
		}
		// Limite Bloqueado
		if (getQuestionarioNaoPreenchido()) {
			setDisableLimiteBloqueado(true);
			filtroImovelRuralRelatorioDTO.setLimiteBloqueado(false);
		} else {
			setDisableLimiteBloqueado(false);
		}

	}

	/**
	 * Método responsável por validar a visualizacao dos campos da busca avançada
	 * com o filtro Status Registro Incompleto e Cadastrado selecionados
	 */
	private void validaCamposRegistroIncompletoCadastrado() {
		
		//Período da primeira finalização
		setDisablePeriodoFinalizacao(false);
		
		//Período da sincronização
		if(getQuestionarioNaoPreenchido()) {			
			setDisablePeriodoSincronizacao(true);
			filtroImovelRuralRelatorioDTO.setDtaInicioPeriodoPrimeiraSincronizacao(null);
			filtroImovelRuralRelatorioDTO.setDtaFimPeriodoPrimeiraSincronizacao(null);
		} else {			
			setDisablePeriodoSincronizacao(false);			
		}
		
		//Período de aprovação da Reserva Legal
		setDisablePeriodoReservaLegal(false);
		
		//Gestor financeiro Contrato/Convênio
		if(filtroImovelRuralRelatorioDTO.isCadastroIncra()) {			
			setDisableGestorFinanceiro(true);
			setDisableContratoConvenio(true);			
			filtroImovelRuralRelatorioDTO.setListGestorFinanceiro(new ArrayList<Integer>());
			filtroImovelRuralRelatorioDTO.setListContratoConvenio(new ArrayList<Integer>());
		} else {			
			setDisableGestorFinanceiro(false);
			setDisableContratoConvenio(false);			
		}
		
		//Até 4 módulos
		if(filtroImovelRuralRelatorioDTO.isCadastroBndes()
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()) {			
			filtroImovelRuralRelatorioDTO.setModulosFiscais(ATE_4_MODULOS);			
		} 
		
		//Acima de 4 módulos
		if(filtroImovelRuralRelatorioDTO.isCadastroBndes()
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()) {			
			setDisableAcima4Modulos(true);			
			if(filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS)) {
				filtroImovelRuralRelatorioDTO.setModulosFiscais(null);
			}			
		} else {			
			setDisableAcima4Modulos(false);			
		}
		
		//No próprio imóvel
		setDisableNoProprioImovel(false);
		
		//Em compensação entre imóveis de mesmo proprietário
		setDisableEmcompensacaoEntreImoveisMesmoProprietario(false);
		
		//Em condomínio e Em compensação por servidão ambiental
		if(filtroImovelRuralRelatorioDTO.isSobreposicaoRLAPP()
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()
			|| filtroImovelRuralRelatorioDTO.isRlAprovada()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) 
					&& (filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(CERTIFICADO)
							|| filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(TERMO_COMPROMISSO)))){
			setDisableEmCondominio(true);
			setDisableEmCompensacaoPorServidao(true);
			if((!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
						&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
							|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
				filtroImovelRuralRelatorioDTO.setTipoArl(null);
			}
		} else {			
			setDisableEmCondominio(false);
			setDisableEmCompensacaoPorServidao(false);			
		}		
		
		//Aviso
		if((!Util.isNull(filtroImovelRuralRelatorioDTO.getModulosFiscais()) 
				&& filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS))
			|| getQuestionarioNaoPreenchido()) {			
			setDisableAviso(true);			
			if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal())
				&& filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(AVISO)) {				
				filtroImovelRuralRelatorioDTO.setDocumentoFinal(null);				
			}
		} else {			
			setDisableAviso(false);			
		}		
		
		//Termo decompromisso
		if(getQuestionarioNaoPreenchido()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
					&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
						|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {			
			setDisableTermoCompromisso(true);			
			if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) 
				&& filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(TERMO_COMPROMISSO)) {				
				filtroImovelRuralRelatorioDTO.setDocumentoFinal(null);				
			}
		} else {			
			setDisableTermoCompromisso(false);			
		}
		
		if(filtroImovelRuralRelatorioDTO.isPraCadastrado()
			|| filtroImovelRuralRelatorioDTO.isOutroPassivoAmbiental()) {			
			filtroImovelRuralRelatorioDTO.setDocumentoFinal(TERMO_COMPROMISSO);			
		}
		
		//Certificado de Inscrição
		if(getQuestionarioNaoPreenchido()
				|| filtroImovelRuralRelatorioDTO.isPraCadastrado()
				|| filtroImovelRuralRelatorioDTO.isOutroPassivoAmbiental()				
				|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
						&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
							|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
			setDisableCertificado(true);
			if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) 
				&& filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(CERTIFICADO)) {				
				filtroImovelRuralRelatorioDTO.setDocumentoFinal(null);				
			}
		} else {
			setDisableCertificado(false);
		}
		
		//Questionário Preenchido e Questionário Não Preenchido
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl())
			|| filtroImovelRuralRelatorioDTO.isPraCadastrado()
			|| filtroImovelRuralRelatorioDTO.isOutroPassivoAmbiental()
			|| filtroImovelRuralRelatorioDTO.isSobreposicaoRLAPP()
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()
			|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao())
			|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao())
			|| filtroImovelRuralRelatorioDTO.isRlAprovada()
			|| filtroImovelRuralRelatorioDTO.isNumeroCar()) {
			filtroImovelRuralRelatorioDTO.setQuestionario(PREENCHIDO);
			setDisableQuestionarioNaoPreenchido(true);
		} else {
			setDisableQuestionarioNaoPreenchido(false);			
		}
		
		
		//BNDES
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(AVISO)) {
			filtroImovelRuralRelatorioDTO.setCadastroBndes(true);
		}
		
		if((!Util.isNull(filtroImovelRuralRelatorioDTO.getModulosFiscais()) && filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS))
			|| filtroImovelRuralRelatorioDTO.isCadastroIncra()
			|| filtroImovelRuralRelatorioDTO.isCadastroCda()) {
			setDisableBndes(true);
			filtroImovelRuralRelatorioDTO.setCadastroBndes(false);			
		} else {
			setDisableBndes(false);
		}
		
		//CDA
		if(filtroImovelRuralRelatorioDTO.isCadastroBndes() || filtroImovelRuralRelatorioDTO.isCadastroIncra()) {
			setDisableCda(true);
			filtroImovelRuralRelatorioDTO.setCadastroCda(false);			
		} else {
			setDisableCda(false);
		}
		
		//INCRA
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListGestorFinanceiro())
			|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListContratoConvenio())
			|| filtroImovelRuralRelatorioDTO.isCadastroBndes()
			|| filtroImovelRuralRelatorioDTO.isCadastroCda()) {
			setDisableIncra(true);
			filtroImovelRuralRelatorioDTO.setCadastroIncra(false);			
		} else {
			setDisableIncra(false);
		}
		
		//Imóvel Rural
		if(filtroImovelRuralRelatorioDTO.isModuloAssentamento() || filtroImovelRuralRelatorioDTO.isModuloPCT()) {
			setDisableImovelRural(true);
			filtroImovelRuralRelatorioDTO.setModuloImovelRural(false);
		} else {
			setDisableImovelRural(false);
		}
		
		//Assentamento
		if(filtroImovelRuralRelatorioDTO.isModuloImovelRural() || filtroImovelRuralRelatorioDTO.isModuloPCT()) {
			setDisableAssentamento(true);
			filtroImovelRuralRelatorioDTO.setModuloAssentamento(false);
		} else {
			setDisableAssentamento(false);
		}
		
		//PCT
		if(filtroImovelRuralRelatorioDTO.isModuloAssentamento() || filtroImovelRuralRelatorioDTO.isModuloImovelRural()) {
			setDisablePCT(true);
			filtroImovelRuralRelatorioDTO.setModuloPCT(false);
		} else {
			setDisablePCT(false);
		}
		
		//PRA Cadastrado
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(CERTIFICADO)
			|| getQuestionarioNaoPreenchido()) {
			setDisablePraCadastrado(true);
			filtroImovelRuralRelatorioDTO.setPraCadastrado(false);
		} else {
			setDisablePraCadastrado(false);
		}
		
		//Número CAR
		if(getQuestionarioNaoPreenchido()) {
			filtroImovelRuralRelatorioDTO.setNumeroCar(false);				
			setDisableNumeroCar(true);
		} else {
			if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao())
				|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao())) {
				filtroImovelRuralRelatorioDTO.setNumeroCar(true);				
				setDisableNumeroCar(true);
			} else {
				setDisableNumeroCar(false);
			}
		}
		
		//RL Aprovada
		if(getQuestionarioNaoPreenchido()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
						&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
							|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
			setDisableRlAprovada(true);
			filtroImovelRuralRelatorioDTO.setRlAprovada(false);
		} else {
			setDisableRlAprovada(false);
		}
		
		//Área de RL menor que 20% da área do imóvel rural
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getModulosFiscais()) && filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS)
			|| getQuestionarioNaoPreenchido()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
					&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
						|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
			setDisableAreaRlMenorQue20PorCento(true);
			filtroImovelRuralRelatorioDTO.setRlMenorQueVintePorCento(false);
		} else {
			setDisableAreaRlMenorQue20PorCento(false);
		}

		//Sobreposição de RL e APP
		if(getQuestionarioNaoPreenchido()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
						&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
							|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
			setDisableSobreposicaoRlApp(true);
			filtroImovelRuralRelatorioDTO.setSobreposicaoRLAPP(false);
		} else {
			setDisableSobreposicaoRlApp(false);
		}
				
		// Outro passivo ambiental fora de APP e RL
		if (!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal())
				&& filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(CERTIFICADO)
				|| getQuestionarioNaoPreenchido()) {
			setDisableOutroPassivoAmbiental(true);
			filtroImovelRuralRelatorioDTO.setOutroPassivoAmbiental(false);
		} else {
			setDisableOutroPassivoAmbiental(false);
		}
		
		// Limite Bloqueado
		if (!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal())
				&& filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(CERTIFICADO)
				|| getQuestionarioNaoPreenchido()) {
			setDisableLimiteBloqueado(true);
			filtroImovelRuralRelatorioDTO.setLimiteBloqueado(false);
		} else {
			setDisableLimiteBloqueado(false);
		}
	}

	/**
	 * Método responsável por validar a visualizacao dos campos da busca avançada
	 * com o filtro Status Registrado e Cadastrado selecionados
	 */
	private void validaCamposRegistradoCadastrado() {
		
		//Período da primeira finalização
		setDisablePeriodoFinalizacao(false);
		
		//Período da sincronização
		if(getQuestionarioNaoPreenchido()) {			
			setDisablePeriodoSincronizacao(true);
			filtroImovelRuralRelatorioDTO.setDtaInicioPeriodoPrimeiraSincronizacao(null);
			filtroImovelRuralRelatorioDTO.setDtaFimPeriodoPrimeiraSincronizacao(null);
		} else {			
			setDisablePeriodoSincronizacao(false);			
		}
		
		//Período de aprovação da Reserva Legal
		setDisablePeriodoReservaLegal(false);
		
		//Gestor financeiro Contrato/Convênio
		if(filtroImovelRuralRelatorioDTO.isCadastroIncra()) {			
			setDisableGestorFinanceiro(true);
			setDisableContratoConvenio(true);			
			filtroImovelRuralRelatorioDTO.setListGestorFinanceiro(new ArrayList<Integer>());
			filtroImovelRuralRelatorioDTO.setListContratoConvenio(new ArrayList<Integer>());
		} else {			
			setDisableGestorFinanceiro(false);
			setDisableContratoConvenio(false);			
		}
		
		//Até 4 módulos
		if(filtroImovelRuralRelatorioDTO.isCadastroBndes()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(AVISO))
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()
			|| getQuestionarioNaoPreenchido()) {			
			filtroImovelRuralRelatorioDTO.setModulosFiscais(ATE_4_MODULOS);			
		} 
		
		//Acima de 4 módulos
		if(filtroImovelRuralRelatorioDTO.isCadastroBndes()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(AVISO))
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()) {
			setDisableAcima4Modulos(true);			
			if(filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS)) {
				filtroImovelRuralRelatorioDTO.setModulosFiscais(null);
			}			
		} else {			
			setDisableAcima4Modulos(false);			
		}
		
		//No próprio imóvel
		setDisableNoProprioImovel(false);
		
		//Em compensação entre imóveis de mesmo proprietário
		setDisableEmcompensacaoEntreImoveisMesmoProprietario(false);
		
		//Em condomínio e Em compensação por servidão ambiental
		if(filtroImovelRuralRelatorioDTO.isSobreposicaoRLAPP()
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()
			|| filtroImovelRuralRelatorioDTO.isRlAprovada()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) 
					&& (filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(CERTIFICADO)
							|| filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(TERMO_COMPROMISSO)))){
			setDisableEmCondominio(true);
			setDisableEmCompensacaoPorServidao(true);
			if((!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
						&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
							|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
				filtroImovelRuralRelatorioDTO.setTipoArl(null);
			}
		} else {			
			setDisableEmCondominio(false);
			setDisableEmCompensacaoPorServidao(false);			
		}		
		
		//Aviso
		if((!Util.isNull(filtroImovelRuralRelatorioDTO.getModulosFiscais()) 
				&& filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS))
			|| getQuestionarioNaoPreenchido()) {			
			setDisableAviso(true);			
			if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal())
				&& filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(AVISO)) {				
				filtroImovelRuralRelatorioDTO.setDocumentoFinal(null);				
			}
		} else {			
			setDisableAviso(false);			
		}		
		
		//Termo decompromisso
		if(getQuestionarioNaoPreenchido()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
					&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
						|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {			
			setDisableTermoCompromisso(true);			
			if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) 
				&& filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(TERMO_COMPROMISSO)) {				
				filtroImovelRuralRelatorioDTO.setDocumentoFinal(null);				
			}
		} else {			
			setDisableTermoCompromisso(false);			
		}
		
		if(filtroImovelRuralRelatorioDTO.isPraCadastrado()
			|| filtroImovelRuralRelatorioDTO.isOutroPassivoAmbiental()) {			
			filtroImovelRuralRelatorioDTO.setDocumentoFinal(TERMO_COMPROMISSO);			
		}
		
		//Certificado de Inscrição
		if(getQuestionarioNaoPreenchido()
				|| filtroImovelRuralRelatorioDTO.isPraCadastrado()
				|| filtroImovelRuralRelatorioDTO.isOutroPassivoAmbiental()				
				|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
						&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
							|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
			setDisableCertificado(true);
			if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) 
				&& filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(CERTIFICADO)) {				
				filtroImovelRuralRelatorioDTO.setDocumentoFinal(null);				
			}
		} else {
			setDisableCertificado(false);
		}
		
		//Questionário Preenchido e Questionário Não Preenchido
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl())
			|| filtroImovelRuralRelatorioDTO.isPraCadastrado()
			|| filtroImovelRuralRelatorioDTO.isOutroPassivoAmbiental()
			|| filtroImovelRuralRelatorioDTO.isSobreposicaoRLAPP()
			|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()
			|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao())
			|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao())
			|| filtroImovelRuralRelatorioDTO.isRlAprovada()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getModulosFiscais()) 
					&& filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS))
			|| filtroImovelRuralRelatorioDTO.isNumeroCar()) {
			filtroImovelRuralRelatorioDTO.setQuestionario(PREENCHIDO);
			setDisableQuestionarioNaoPreenchido(true);
		} else {
			setDisableQuestionarioNaoPreenchido(false);			
		}
		
		
		//BNDES
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(AVISO)) {
			filtroImovelRuralRelatorioDTO.setCadastroBndes(true);
		}
		
		if((!Util.isNull(filtroImovelRuralRelatorioDTO.getModulosFiscais()) && filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS))
			|| filtroImovelRuralRelatorioDTO.isCadastroIncra()
			|| filtroImovelRuralRelatorioDTO.isCadastroCda()) {
			setDisableBndes(true);
			filtroImovelRuralRelatorioDTO.setCadastroBndes(false);			
		} else {
			setDisableBndes(false);
		}
		
		//CDA
		if(filtroImovelRuralRelatorioDTO.isCadastroBndes() || filtroImovelRuralRelatorioDTO.isCadastroIncra()) {
			setDisableCda(true);
			filtroImovelRuralRelatorioDTO.setCadastroCda(false);			
		} else {
			setDisableCda(false);
		}
		
		//INCRA
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListGestorFinanceiro())
			|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListContratoConvenio())
			|| filtroImovelRuralRelatorioDTO.isCadastroBndes()
			|| filtroImovelRuralRelatorioDTO.isCadastroCda()) {
			setDisableIncra(true);
			filtroImovelRuralRelatorioDTO.setCadastroIncra(false);			
		} else {
			setDisableIncra(false);
		}
		
		//Imóvel Rural
		if(filtroImovelRuralRelatorioDTO.isModuloAssentamento() || filtroImovelRuralRelatorioDTO.isModuloPCT()) {
			setDisableImovelRural(true);
			filtroImovelRuralRelatorioDTO.setModuloImovelRural(false);
		} else {
			setDisableImovelRural(false);
		}
		
		//Assentamento
		if(filtroImovelRuralRelatorioDTO.isModuloImovelRural() || filtroImovelRuralRelatorioDTO.isModuloPCT()) {
			setDisableAssentamento(true);
			filtroImovelRuralRelatorioDTO.setModuloAssentamento(false);
		} else {
			setDisableAssentamento(false);
		}
		
		//PCT
		if(filtroImovelRuralRelatorioDTO.isModuloAssentamento() || filtroImovelRuralRelatorioDTO.isModuloImovelRural()) {
			setDisablePCT(true);
			filtroImovelRuralRelatorioDTO.setModuloPCT(false);
		} else {
			setDisablePCT(false);
		}
		
		//PRA Cadastrado
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(CERTIFICADO)
			|| getQuestionarioNaoPreenchido()) {
			setDisablePraCadastrado(true);
			filtroImovelRuralRelatorioDTO.setPraCadastrado(false);
		} else {
			setDisablePraCadastrado(false);
		}
		
		//Número CAR
		if(getQuestionarioNaoPreenchido()) {
			filtroImovelRuralRelatorioDTO.setNumeroCar(false);				
			setDisableNumeroCar(true);
		} else {
			if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao())
				|| !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao())) {
				filtroImovelRuralRelatorioDTO.setNumeroCar(true);				
				setDisableNumeroCar(true);
			} else {
				setDisableNumeroCar(false);
			}
		}
		
		//RL Aprovada
		if(getQuestionarioNaoPreenchido()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
						&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
							|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
			setDisableRlAprovada(true);
			filtroImovelRuralRelatorioDTO.setRlAprovada(false);
		} else {
			setDisableRlAprovada(false);
		}
		
		//Área de RL menor que 20% da área do imóvel rural
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getModulosFiscais()) && filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(ACIMA_4_MODULOS)
			|| getQuestionarioNaoPreenchido()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
					&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
						|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
			setDisableAreaRlMenorQue20PorCento(true);
			filtroImovelRuralRelatorioDTO.setRlMenorQueVintePorCento(false);
		} else {
			setDisableAreaRlMenorQue20PorCento(false);
		}

		//Sobreposição de RL e APP
		if(getQuestionarioNaoPreenchido()
			|| (!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) 
						&& (filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())
							|| filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())))) {
			setDisableSobreposicaoRlApp(true);
			filtroImovelRuralRelatorioDTO.setSobreposicaoRLAPP(false);
		} else {
			setDisableSobreposicaoRlApp(false);
		}
				
		//Outro passivo ambiental fora de APP e RL
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(CERTIFICADO)
			|| getQuestionarioNaoPreenchido()) {
			setDisableOutroPassivoAmbiental(true);
			filtroImovelRuralRelatorioDTO.setOutroPassivoAmbiental(false);
		} else {
			setDisableOutroPassivoAmbiental(false);
		}
		
		// Limite Bloqueado
		if (!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) && filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(CERTIFICADO)
				|| getQuestionarioNaoPreenchido()){
			setDisableLimiteBloqueado(true);
			filtroImovelRuralRelatorioDTO.setLimiteBloqueado(false);
		} else {
			setDisableLimiteBloqueado(false);
		}
	}
	
	//Fim Validação dos Campos de Busca Avançada
	
	
	// Início das FLAGs
	public boolean isRequerenteVisivel(){
		SecurityController security = (SecurityController) SessaoUtil.recuperarManagedBean("#{security}", SecurityController.class);
		return !security.temAcessoPorPerfil("2") || (security.temAcessoPorPerfil("2") && isUsuarioResponsavelTecnico());   
	}
	
	private boolean isUsuarioResponsavelTecnico() {
		try {
			return !Util.isNullOuVazio(imovelRuralFacade.listarResponsavelImovelRuralById(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return false;
		}
	}

	// Fim das FLAGs
	
	//Início Getters e Setters

	public boolean isDisablePeriodoFinalizacao() {
		return disablePeriodoFinalizacao;
	}

	public void setDisablePeriodoFinalizacao(boolean disablePeriodoFinalizacao) {
		this.disablePeriodoFinalizacao = disablePeriodoFinalizacao;
	}

	public boolean isDisablePeriodoSincronizacao() {
		return disablePeriodoSincronizacao;
	}

	public void setDisablePeriodoSincronizacao(boolean disablePeriodoSincronizacao) {
		this.disablePeriodoSincronizacao = disablePeriodoSincronizacao;
	}

	public boolean isDisableGestorFinanceiro() {
		return disableGestorFinanceiro;
	}

	public void setDisableGestorFinanceiro(boolean disableGestorFinanceiro) {
		this.disableGestorFinanceiro = disableGestorFinanceiro;
	}

	public boolean isDisableContratoConvenio() {
		return disableContratoConvenio;
	}

	public void setDisableContratoConvenio(boolean disableContratoConvenio) {
		this.disableContratoConvenio = disableContratoConvenio;
	}

	public boolean isDisableAte4Modulos() {
		return disableAte4Modulos;
	}

	public void setDisableAte4Modulos(boolean disableAte4Modulos) {
		this.disableAte4Modulos = disableAte4Modulos;
	}

	public boolean isDisableAcima4Modulos() {
		return disableAcima4Modulos;
	}

	public void setDisableAcima4Modulos(boolean disableAcima4Modulos) {
		this.disableAcima4Modulos = disableAcima4Modulos;
	}

	public boolean isDisableNoProprioImovel() {
		return disableNoProprioImovel;
	}

	public void setDisableNoProprioImovel(boolean disableNoProprioImovel) {
		this.disableNoProprioImovel = disableNoProprioImovel;
	}

	public boolean isDisableEmCondominio() {
		return disableEmCondominio;
	}

	public void setDisableEmCondominio(boolean disableEmCondominio) {
		this.disableEmCondominio = disableEmCondominio;
	}

	public boolean isDisableEmCompensacaoPorServidao() {
		return disableEmCompensacaoPorServidao;
	}

	public void setDisableEmCompensacaoPorServidao(boolean disableEmCompensacaoPorServidao) {
		this.disableEmCompensacaoPorServidao = disableEmCompensacaoPorServidao;
	}

	public boolean isDisableEmcompensacaoEntreImoveisMesmoProprietario() {
		return disableEmcompensacaoEntreImoveisMesmoProprietario;
	}

	public void setDisableEmcompensacaoEntreImoveisMesmoProprietario(
			boolean disableEmcompensacaoEntreImoveisMesmoProprietario) {
		this.disableEmcompensacaoEntreImoveisMesmoProprietario = disableEmcompensacaoEntreImoveisMesmoProprietario;
	}

	public boolean isDisableAviso() {
		return disableAviso;
	}

	public void setDisableAviso(boolean disableAviso) {
		this.disableAviso = disableAviso;
	}

	public boolean isDisableTermoCompromisso() {
		return disableTermoCompromisso;
	}

	public void setDisableTermoCompromisso(boolean disableTermoCompromisso) {
		this.disableTermoCompromisso = disableTermoCompromisso;
	}

	public boolean isDisableCertificado() {
		return disableCertificado;
	}

	public void setDisableCertificado(boolean disableCertificado) {
		this.disableCertificado = disableCertificado;
	}

	public boolean isDisableQuestionarioPreenchido() {
		return disableQuestionarioPreenchido;
	}

	public void setDisableQuestionarioPreenchido(boolean disableQuestionarioPreenchido) {
		this.disableQuestionarioPreenchido = disableQuestionarioPreenchido;
	}

	public boolean isDisableQuestionarioNaoPreenchido() {
		return disableQuestionarioNaoPreenchido;
	}

	public void setDisableQuestionarioNaoPreenchido(boolean disableQuestionarioNaoPreenchido) {
		this.disableQuestionarioNaoPreenchido = disableQuestionarioNaoPreenchido;
	}

	public boolean isDisableBndes() {
		return disableBndes;
	}

	public void setDisableBndes(boolean disableBndes) {
		this.disableBndes = disableBndes;
	}

	public boolean isDisableCda() {
		return disableCda;
	}

	public void setDisableCda(boolean disableCda) {
		this.disableCda = disableCda;
	}

	public boolean isDisableIncra() {
		return disableIncra;
	}

	public void setDisableIncra(boolean disableIncra) {
		this.disableIncra = disableIncra;
	}

	public boolean isDisablePraCadastrado() {
		return disablePraCadastrado;
	}

	public void setDisablePraCadastrado(boolean disablePraCadastrado) {
		this.disablePraCadastrado = disablePraCadastrado;
	}

	public boolean isDisableNumeroCar() {
		return disableNumeroCar;
	}

	public void setDisableNumeroCar(boolean disableNumeroCar) {
		this.disableNumeroCar = disableNumeroCar;
	}

	public boolean isDisableRlAprovada() {
		return disableRlAprovada;
	}

	public void setDisableRlAprovada(boolean disableRlAprovada) {
		this.disableRlAprovada = disableRlAprovada;
	}

	public boolean isDisableAreaRlMenorQue20PorCento() {
		return disableAreaRlMenorQue20PorCento;
	}

	public void setDisableAreaRlMenorQue20PorCento(boolean disableAreaRlMenorQue20PorCento) {
		this.disableAreaRlMenorQue20PorCento = disableAreaRlMenorQue20PorCento;
	}

	public boolean isDisableSobreposicaoRlApp() {
		return disableSobreposicaoRlApp;
	}

	public void setDisableSobreposicaoRlApp(boolean disableSobreposicaoRlApp) {
		this.disableSobreposicaoRlApp = disableSobreposicaoRlApp;
	}

	public boolean isDisableOutroPassivoAmbiental() {
		return disableOutroPassivoAmbiental;
	}

	public void setDisableOutroPassivoAmbiental(boolean disableOutroPassivoAmbiental) {
		this.disableOutroPassivoAmbiental = disableOutroPassivoAmbiental;
	}
	
	public boolean getTermoOuCertificadoselecionaStatusCadastroSelecionado() {
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getDocumentoFinal()) 
			&& (filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(TERMO_COMPROMISSO)
					|| filtroImovelRuralRelatorioDTO.getDocumentoFinal().equals(CERTIFICADO))) {
			return true;
		}
		return false;
	}
	
	public String getNomProprietario() {
		String nomProprietario = "";
		if(!Util.isNullOuVazio(this.proprietario)){						
			if(!Util.isNullOuVazio(this.proprietario.getPessoaFisica())){
				nomProprietario = this.proprietario.getPessoaFisica().getNomPessoa();
			} else if (!Util.isNullOuVazio(this.proprietario.getPessoaJuridica())){
				nomProprietario = this.proprietario.getPessoaJuridica().getNomRazaoSocial();
			}
		}
		return nomProprietario;
	}

	public Pessoa getProprietario() {
		return proprietario;
	}


	public void setProprietario(Pessoa proprietario) {
		this.proprietario = proprietario;
	}

	public PessoaFisica getProcurador() {
		return procurador;
	}
	
	public void setProcurador(PessoaFisica procurador) {
		this.procurador = procurador;
	}
	
	public FiltroImovelRuralRelatorioDTO getFiltroImovelRuralRelatorioDTO() {
		return filtroImovelRuralRelatorioDTO;
	}

	public void setFiltroImovelRuralRelatorioDTO(FiltroImovelRuralRelatorioDTO filtroImovelRuralRelatorioDTO) {
		this.filtroImovelRuralRelatorioDTO = filtroImovelRuralRelatorioDTO;
	}
	
	public List<GeoBahia> getListaFiltro() {		
		return listaFiltro;
	}
	
	/**
	 * Método responsável por verificar a seleção do campo "Filtrar por -> Selecione (Não Filtrado)" 
	 * 
	 * @return retorna {@code true} se o campo selecionado for Selecione (Não Filtrado)
	 */
	public boolean isTipoFiltroSelecionadoSelecione() {
		return filtroImovelRuralRelatorioDTO.getTipoFiltroSelecionado() == 1;
	}
	
	/**
	 * Método responsável por verificar a seleção do campo "Filtrar por -> Bacia" 
	 * 
	 * @return retorna {@code true} se o campo selecionado for Filtrar por -> Bacia
	 */
	public boolean isTipoFiltroSelecionadoBacia() {
		return filtroImovelRuralRelatorioDTO.getTipoFiltroSelecionado() == 2;
	}	
	
	
	/**
	 * Método responsável por verificar a seleção do campo "Filtrar por -> Bioma" 
	 * 
	 * @return retorna {@code true} se o campo selecionado for Filtrar por -> Bioma 
	 */
	public boolean isTipoFiltroSelecionadoBioma() {
		return filtroImovelRuralRelatorioDTO.getTipoFiltroSelecionado() == 3;
	}
	
	/**
	 * Método responsável por verificar a seleção do campo "Filtrar por -> Municipio" 
	 * 
	 * @return retorna {@code true} se o campo selecionado for Filtrar por -> Municipio
	 */
	public boolean isTipoFiltroSelecionadoMunicipio() {
		return filtroImovelRuralRelatorioDTO.getTipoFiltroSelecionado() == 4;
	}
	 
	/**
	 * Método responsável por verificar a seleção do campo "Filtrar por -> RPGA" 
	 * 
	 * @return retorna {@code true} se o campo selecionado for Filtrar por -> RPGA  
	 */
	public boolean isTipoFiltroSelecionadoRPGA() {
		return filtroImovelRuralRelatorioDTO.getTipoFiltroSelecionado() == 5;
	}
	
	/**
	 * Método responsável por verificar a seleção do campo "Filtrar por -> Território de Identidade" 
	 * 
	 * @return retorna {@code true} se o campo selecionado for Filtrar por -> Território de Identidade
	 */
	public boolean isTipoFiltroSelecionadoTerritorioIdentidade() {
		return filtroImovelRuralRelatorioDTO.getTipoFiltroSelecionado() == 6;
	}
	
	/**
	 * Método responsável por verificar a seleção do campo "Filtrar por -> Unidade de Conservação" 
	 * 
	 * @return retorna {@code true} se o campo selecionado for Filtrar por -> Unidade de Conservação 
	 */
	public boolean isTipoFiltroSelecionadoUnidadeConservacao() {
		return filtroImovelRuralRelatorioDTO.getTipoFiltroSelecionado() == 7;
	}
	
	public List<Municipio> getListaMunicipios() {
		if(Util.isNullOuVazio(this.listaMunicipios)){
			try {
				this.listaMunicipios = (List<Municipio>) municipioService.filtrarListaMunicipiosDaBahia();
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}
		return listaMunicipios;
	}
	
	public Collection<GeoBahia> getListaBioma() {
		if(Util.isNull(this.listaBioma)) {
			try {
				this.listaBioma = new ArrayList<GeoBahia>(relatorioQuantitativoService.listaDadosGeoBahia("bioma"));
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}
		return this.listaBioma;
	}
	
	public Collection<GeoBahia> getListaRpga() {
		if(Util.isNull(this.listaRpga)) {
			try {
				this.listaRpga = new ArrayList<GeoBahia>(relatorioQuantitativoService.listaDadosGeoBahia("rpga"));
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}
		return this.listaRpga;
	}

	public Collection<GeoBahia> getListaUnidadeConservacao() {
		if(Util.isNull(this.listaUnidade)) {
			try {
				this.listaUnidade = new ArrayList<GeoBahia>(relatorioQuantitativoService.listaDadosGeoBahia("unidade_conservacao"));
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}
		return this.listaUnidade;
	}
	
	public Collection<GeoBahia> getListaTerritorioIdentidade() {
		if(Util.isNull(this.listaTerritorio)) {
			try {
				this.listaTerritorio = new ArrayList<GeoBahia>(relatorioQuantitativoService.listaDadosGeoBahia("territorio_identidade"));
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}
		return this.listaTerritorio;
	}
	
	public Collection<GeoBahia> getListaBacia() {
		if(Util.isNull(this.listaBacia)) {
			try {
				this.listaBacia = new ArrayList<GeoBahia>(relatorioQuantitativoService.listaDadosGeoBahia("bacia"));
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
		}
		return this.listaBacia;
	}
	
	public boolean isRegistroIncompleto() {		
		return registroIncompleto;
	}

	public void setRegistroIncompleto(boolean registroIncompleto) {
		this.registroIncompleto = registroIncompleto;
	}
	
	public boolean isRegistrado() {
		return registrado;
	}

	public void setRegistrado(boolean registrado) {
		this.registrado = registrado;
	}

	public boolean isCadastrado() {
		return cadastrado;
	}

	public void setCadastrado(boolean cadastrado) {
		this.cadastrado = cadastrado;
	}
	
	public boolean isPendente() {
		return pendente;
	}

	public void setPendente(boolean pendente) {
		this.pendente = pendente;
	}
	
	public String getMaxDateInicioPrimeiraFinalizacao() {
		Date data; 
		if(!Util.isNullOuVazio(this.filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraFinalizacao())) {
			data = this.filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraFinalizacao();
		} else {
			data = new Date(System.currentTimeMillis());
		}
		SimpleDateFormat formatarDate = new SimpleDateFormat("dd/MM/yyyy");
		return formatarDate.format(data);
	}
	
	public String getMaxDateInicioPrimeiraSincronizacao() {
		Date data; 
		if(!Util.isNullOuVazio(this.filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao())) {
			data = this.filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao();
		} else {
			data = new Date(System.currentTimeMillis());
		}
		SimpleDateFormat formatarDate = new SimpleDateFormat("dd/MM/yyyy");
		return formatarDate.format(data);
	}
	
	public String getMaxDateInicioPeriodoReservaLegal() {
		Date data; 
		if(!Util.isNullOuVazio(this.filtroImovelRuralRelatorioDTO.getDtaFimPeriodoReservaLegal())) {
			data = this.filtroImovelRuralRelatorioDTO.getDtaFimPeriodoReservaLegal();
		} else {
			data = new Date(System.currentTimeMillis());
		}
		SimpleDateFormat formatarDate = new SimpleDateFormat("dd/MM/yyyy");
		return formatarDate.format(data);
	}
	
	public String getMinDateFimPrimeiraFinalizacao() {
		try {
			SimpleDateFormat formatarDate = new SimpleDateFormat("dd/MM/yyyy");
			Date data; 
		if(!Util.isNullOuVazio(this.filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraFinalizacao())) {
			data = this.filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraFinalizacao();
		} else {
				data = formatarDate.parse("01/01/2000");
		}
			return formatarDate.format(data);
		} catch (ParseException e) {			
			JsfUtil.addErrorMessage(e.getMessage());
			return null;
		}
	}
	
	public String getMinDateFimPrimeiraSincronizacao() {
		try {
			SimpleDateFormat formatarDate = new SimpleDateFormat("dd/MM/yyyy");
			Date data; 
			if(!Util.isNullOuVazio(this.filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao())) {
				data = this.filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao();
			} else {
				data = formatarDate.parse("01/01/2000");
			}
			return formatarDate.format(data);
		} catch (ParseException e) {			
			JsfUtil.addErrorMessage(e.getMessage());
			return null;
		}
	}
	
	public String getMinDateFimPeriodoReservaLegal() {
		try {
			SimpleDateFormat formatarDate = new SimpleDateFormat("dd/MM/yyyy");
			Date data; 
			if(!Util.isNullOuVazio(this.filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoReservaLegal())) {
				data = this.filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoReservaLegal();
			} else {
				data = formatarDate.parse("01/01/2000");
			}
			return formatarDate.format(data);
		} catch (ParseException e) {			
			JsfUtil.addErrorMessage(e.getMessage());
			return null;
		}
	}
	
	
	
	public boolean getQuestionarioPreenchido() {
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getQuestionario()) && filtroImovelRuralRelatorioDTO.getQuestionario().equals(PREENCHIDO)) {
			return true;
		}
		return false;
	}
	
	public boolean getQuestionarioNaoPreenchido() {
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getQuestionario()) && filtroImovelRuralRelatorioDTO.getQuestionario().equals(NAO_PREENCHIDO)) {
			return true;
		}
		return false;
	}
	
	public List<GestorFinanceiroCefir> getListaGestorFinanceiro() {
		return listaGestorFinanceiro;
	}

	public void setListaGestorFinanceiro(List<GestorFinanceiroCefir> listaGestorFinanceiro) {
		this.listaGestorFinanceiro = listaGestorFinanceiro;
	}
	
	public List<ContratoConvenioCefir> getListaContratoConvenio() {
		
			carregarListaContratoConvenio();
			
		List<Integer> listGestorFinanceiro = filtroImovelRuralRelatorioDTO.getListGestorFinanceiro();
		if(!Util.isNullOuVazio(listGestorFinanceiro) && listGestorFinanceiro.get(0) != -1) {
			List<ContratoConvenioCefir> listContratoConvenioFiltro = new ArrayList<ContratoConvenioCefir>();
			
			for (ContratoConvenioCefir c : listaContratoConvenio) {
				if (!Util.isNull(c.getIdeGestorFinanceiroCefir()) && listGestorFinanceiro.contains(c.getIdeGestorFinanceiroCefir().getIdeGestorFinanceiroCefir())) {
					listContratoConvenioFiltro.add(c);
				}
			}
			
			if(!Util.isNullOuVazio(listContratoConvenioFiltro)) {
				listContratoConvenioFiltro.add(0, new ContratoConvenioCefir(-1, "TODOS"));
			}
			
			listaContratoConvenio = listContratoConvenioFiltro;
		}
		
		return listaContratoConvenio;
	}
	
	public void setListaContratoConvenio(
			List<ContratoConvenioCefir> listaContratoConvenio) {
		this.listaContratoConvenio = listaContratoConvenio;
	}
	
	// Fim Getters e Setters
	
	/**
	 * Método responsável por verificar o campo "Filtrar por"
	 * 
	 * @return retorna {@code true} se o campo "Filtrar por" não foi selecionado
	 * @return retorna {@code false} se o campo "Filtrar por" foi selecionado e não foi escolhido uma opção 
	 */
	public boolean getFiltroSelecionado() {
		if (!isTipoFiltroSelecionadoSelecione()) {
			if (isTipoFiltroSelecionadoBacia() && Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdBaciaSelecionado())) {
				JsfUtil.addErrorMessage("Selecione ao menos uma Bacia!");
				return false;
			}
			if (isTipoFiltroSelecionadoBioma() && Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdBiomaSelecionado())) {
				JsfUtil.addErrorMessage("Selecione ao menos um Bioma!");
				return false;
			}
			if (isTipoFiltroSelecionadoMunicipio() && Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListMunicipio())) {
				JsfUtil.addErrorMessage("Selecione ao menos um Município!");
				return false;
			}
			if (isTipoFiltroSelecionadoRPGA() && Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdRPGASelecionado())) {
				JsfUtil.addErrorMessage("Selecione ao menos uma RPGA!");
				return false;
			}
			if (isTipoFiltroSelecionadoUnidadeConservacao() && Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdUnidadeSelecionado())) {
				JsfUtil.addErrorMessage("Selecione ao menos um Unidade de Identidade!");
				return false;
			}
			if (isTipoFiltroSelecionadoTerritorioIdentidade() && Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdTerritorioSelecionado())) {
				JsfUtil.addErrorMessage("Selecione ao menos uma Território de Conservação!");
				return false;
			} 
		} 
		return true;
	}
	
	public String consultarImoveisRuraisRelatorio() {
		try {
			// Verificando se o combo "Filtrar por" foi selecionado
			if(verificaItemSelecionadoDaColuna()) {
				
				if(getFiltroSelecionado()) {
					carregarFiltroImovelRuralRelatorioDTO();
					
					listaImoveisDT = new LazyDataModel<ImovelRuralRelatorioDTO>() {
						
						private static final long serialVersionUID = 251097493915855087L;
						
						@Override
						public List<ImovelRuralRelatorioDTO> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> fields) {
							List<ImovelRuralRelatorioDTO> lista = null;
							
							Map<String, Object> params = new HashMap<String, Object>();
							params.put("first", first);
							params.put("pageSize", pageSize);
							params.put("isPagination", true);
							
							try {
								lista = imovelRuralRelatorioService.listarImovelRuralRelatorio(filtroImovelRuralRelatorioDTO, params, false);
							} catch(Exception e) {
								Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
							}
							return lista;
						}
					};
					
					listaImoveis = imovelRuralRelatorioService.listarImovelRuralRelatorio(filtroImovelRuralRelatorioDTO, null, true);
					listaImoveisDT.setRowCount(listaImoveis.size());
					
					mostrarTabela = true;
				}
			} else {
				MensagemUtil.erro("Selecione no mínimo uma coluna para exibição.");
			}
		} catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Houve erro ao carregar a lista de imóveis.");
		}
		
		return null;
	}
	
	private boolean verificaItemSelecionadoDaColuna() {
		boolean retorno = false;
		
		if(colunasExibirImovelRuralRelatorioDTO.isNomImovelRural() || colunasExibirImovelRuralRelatorioDTO.isNomMunicipio()
				|| colunasExibirImovelRuralRelatorioDTO.isNomStatusCadastro() || colunasExibirImovelRuralRelatorioDTO.isDtaPrimeiraFinalizacao()
				|| colunasExibirImovelRuralRelatorioDTO.isNomRequerente() || colunasExibirImovelRuralRelatorioDTO.isValArea()
				|| colunasExibirImovelRuralRelatorioDTO.isQtdModuloFiscal() || colunasExibirImovelRuralRelatorioDTO.isNomStatusReservaLegal()
				|| colunasExibirImovelRuralRelatorioDTO.isNumContratoConvenio() || colunasExibirImovelRuralRelatorioDTO.isNomTipoDocumento()
				|| colunasExibirImovelRuralRelatorioDTO.isNumCpfCnpjRequerente() || colunasExibirImovelRuralRelatorioDTO.isDtaFinalizacao()
				|| colunasExibirImovelRuralRelatorioDTO.isDtaSincronizacao() || colunasExibirImovelRuralRelatorioDTO.isProprietario() 
				|| colunasExibirImovelRuralRelatorioDTO.isResponsavelTecnico() || colunasExibirImovelRuralRelatorioDTO.isCpfCadastrante() || 
				colunasExibirImovelRuralRelatorioDTO.isNomeCadastrante()){
			
			retorno = true;
		}
		
		return retorno;	
	}
	
	private void carregarFiltroImovelRuralRelatorioDTO() {	
		filtroImovelRuralRelatorioDTO.setListStatusCadastro(new ArrayList<Integer>());
		
		//Filtro de gestor caso opção todos
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListGestorFinanceiro()) && filtroImovelRuralRelatorioDTO.getListGestorFinanceiro().get(0) == -1) {
			filtroImovelRuralRelatorioDTO.getListGestorFinanceiro().clear();
			for (GestorFinanceiroCefir g: listaGestorFinanceiro) {
				if(g.getIdeGestorFinanceiroCefir() != -1) {
					filtroImovelRuralRelatorioDTO.getListGestorFinanceiro().add(g.getIdeGestorFinanceiroCefir());
				}
			}
		}

		//Filtro de contrato/convenio caso opção todos
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListContratoConvenio()) && filtroImovelRuralRelatorioDTO.getListContratoConvenio().get(0) == -1) {
			filtroImovelRuralRelatorioDTO.getListContratoConvenio().clear();
			for (ContratoConvenioCefir c: listaContratoConvenio) {
				if(c.getIdeContratoConvenioCefir() != -1) {
					filtroImovelRuralRelatorioDTO.getListContratoConvenio().add(c.getIdeContratoConvenioCefir());
				}
			}
		}
		
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getTipoFiltroSelecionado())){
			filtroImovelRuralRelatorioDTO.setTipoFiltroSelecionado(filtroImovelRuralRelatorioDTO.getTipoFiltroSelecionado());
		}
		
		filtroImovelRuralRelatorioDTO.setStatusPendente(null);
		
		if(this.registroIncompleto) {
			filtroImovelRuralRelatorioDTO.getListStatusCadastro().add(StatusCadastroImovelRuralEnum.REGISTRO_INCOMPLETO.getId());
			filtroImovelRuralRelatorioDTO.setStatusPendente(false);
		}
		
		if(this.registrado) {
			filtroImovelRuralRelatorioDTO.getListStatusCadastro().add(StatusCadastroImovelRuralEnum.REGISTRADO.getId());
			filtroImovelRuralRelatorioDTO.setStatusPendente(false);
		}
		
		if(this.cadastrado) {
			filtroImovelRuralRelatorioDTO.getListStatusCadastro().add(StatusCadastroImovelRuralEnum.CADASTRADO.getId());
			filtroImovelRuralRelatorioDTO.setStatusPendente(false);
		}
		
		if(this.pendente) {
			filtroImovelRuralRelatorioDTO.setStatusPendente(true);
		}
		
		if(!Util.isNullOuVazio(this.proprietario)){
			filtroImovelRuralRelatorioDTO.setIdeProprietario(this.proprietario.getIdePessoa());
		}
		if(!Util.isNullOuVazio(this.procurador)){
			filtroImovelRuralRelatorioDTO.setIdeProcurador(this.procurador.getIdePessoaFisica());
		}
	}
	
	public void limparFiltrosImovelRuralRelatorio() {
		this.filtroImovelRuralRelatorioDTO = new FiltroImovelRuralRelatorioDTO();
		this.colunasExibirImovelRuralRelatorioDTO = new ColunasExibirImovelRuralRelatorioDTO();
		this.registroIncompleto = false;
		this.registrado = false;
		this.cadastrado = false;		
		this.proprietario = new Pessoa();
		this.procurador = new PessoaFisica();
		this.mostrarTabela = false;
		this.listaImoveis = new ArrayList<ImovelRuralRelatorioDTO>();	
		this.pendente = false;
		//Limpando os filtros referente a busca avançada
		limpaFiltrosBuscaAvancada();
		filtroImovelRuralRelatorioDTO.setTipoFiltroSelecionado(1);
	}

	public StreamedContent getImpRelatorioImoveisRurais() {
		try {
			carregarFiltroImovelRuralRelatorioDTO();
			
			if(tipoDeImpressaoPdf) {
				tipoDeImpressaoPdf = true;
				return imovelRuralRelatorioService.imprimirRelatorioImovelRural(
						filtroImovelRuralRelatorioDTO, colunasExibirImovelRuralRelatorioDTO, montarDscFiltrosRelatorio(true));
			} else {
				tipoDeImpressaoPdf = true;
				return imovelRuralRelatorioService.imprimirRelatorioImovelRuralXLS(
						filtroImovelRuralRelatorioDTO, colunasExibirImovelRuralRelatorioDTO, montarDscFiltrosRelatorio(true));
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		} 
	}

	public void changeTodosTemas(ValueChangeEvent event){
		this.colunasExibirRelatorioQuantitativoImoveisRuraisDTO = new ColunasExibirRelatorioQuantitativoImoveisRuraisDTO(((Boolean) event.getNewValue())); 
	}
	
	public void limparSelecaoTemas(){
		this.colunasExibirRelatorioQuantitativoImoveisRuraisDTO = new ColunasExibirRelatorioQuantitativoImoveisRuraisDTO(false);
	}
	
	private Map<String,String> montarDscFiltrosRelatorio(boolean exibeDscTotalImoveis) {
		@SuppressWarnings("unused")
		String filtros = "";
		Map<String, String> mapFiltros = new HashMap<String,String>();
		
		
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getResponsavelTecnico()))
		{
			mapFiltros.put("nomeResponsavelTecnico", filtroImovelRuralRelatorioDTO.getResponsavelTecnico().getPessoaFisica().getNomPessoa());
		}

		
		if(!Util.isNullOuVazio(proprietario) && !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdeProprietario())) {						
			if(!Util.isNullOuVazio(proprietario.getPessoaFisica())){
				mapFiltros.put("nomeProprietario", proprietario.getPessoaFisica().getNomPessoa());
			} else if (!Util.isNullOuVazio(proprietario.getPessoaJuridica())){
				mapFiltros.put("nomeProprietario", proprietario.getPessoaJuridica().getNomRazaoSocial());
			}
		}
		
		if(!Util.isNullOuVazio(procurador) && !Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdeProcurador())) {						
			mapFiltros.put("nomeProcurador", procurador.getNomPessoa());
		}
		
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getNomImovelRural())){
			mapFiltros.put("nomeImovel", filtroImovelRuralRelatorioDTO.getNomImovelRural());
		}
		
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getTipoFiltroSelecionado())) {
			String filtroPorTipo = "";
			String filtroPorSelecao = "";
			
			if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListMunicipio())) {
				filtroPorTipo = "Município(s) -";
				for (Integer ideMunicipio : filtroImovelRuralRelatorioDTO.getListMunicipio()) {
					filtroPorSelecao += retornaNomMunicipio(ideMunicipio) + ", ";
				}
				filtroPorSelecao = filtroPorSelecao.substring(0, filtroPorSelecao.length() - 2);				
			}			
			if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdBiomaSelecionado())) {
				filtroPorTipo = "Bioma -";
				filtroPorSelecao = filtroImovelRuralRelatorioDTO.getIdBiomaSelecionado().getNome();
			}
			if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdRPGASelecionado())) {
				filtroPorTipo = "RPGA -";
				filtroPorSelecao = filtroImovelRuralRelatorioDTO.getIdRPGASelecionado().getNome();
			}
			if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdUnidadeSelecionado())) {
				filtroPorTipo = "Unidade de conservação -";
				filtroPorSelecao = filtroImovelRuralRelatorioDTO.getIdUnidadeSelecionado().getNome();
			}
			if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdTerritorioSelecionado())) {
				filtroPorTipo = "Território de identidade -";
				filtroPorSelecao = filtroImovelRuralRelatorioDTO.getIdTerritorioSelecionado().getNome();
			}
			if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getIdBaciaSelecionado())) {
				filtroPorTipo = "Bacia -";
				filtroPorSelecao = filtroImovelRuralRelatorioDTO.getIdBaciaSelecionado().getNome();
			}			
			mapFiltros.put("filtroPorTipo", filtroPorTipo);
			mapFiltros.put("filtroPorSelecao", filtroPorSelecao);
		}
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListStatusCadastro())) {
			String statusCadastro = "";			
			for (Integer ideStatusCadastro : filtroImovelRuralRelatorioDTO.getListStatusCadastro()) {
				statusCadastro += retornaNomStatusCadastro(ideStatusCadastro) + ", ";
			}
			statusCadastro = statusCadastro.substring(0, statusCadastro.length() - 2);
			mapFiltros.put("statusCadastro", statusCadastro);
		}
		
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraFinalizacao())){
			mapFiltros.put("dtFinalizacao", sdfdmy.format(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraFinalizacao()) + " a " + sdfdmy.format(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraFinalizacao() != null ? filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraFinalizacao() : new Date()));					
		} else if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraFinalizacao())) {
			mapFiltros.put("dtFinalizacao", "até " + sdfdmy.format(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraFinalizacao()));
		}
		
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao())){
			mapFiltros.put("dtSincronizacao", sdfdmy.format(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoPrimeiraSincronizacao()) + " a " + sdfdmy.format(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao() != null ? filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao() : new Date()));					
		} else if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao())){
			mapFiltros.put("dtSincronizacao", "até " + sdfdmy.format(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoPrimeiraSincronizacao()));
		}
		
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoReservaLegal())){
			mapFiltros.put("dtAprovacao", sdfdmy.format(filtroImovelRuralRelatorioDTO.getDtaInicioPeriodoReservaLegal()) + " a " + sdfdmy.format(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoReservaLegal() != null ? filtroImovelRuralRelatorioDTO.getDtaFimPeriodoReservaLegal() : new Date()));					
		} else if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoReservaLegal())){
			mapFiltros.put("dtAprovacao", "até " + sdfdmy.format(filtroImovelRuralRelatorioDTO.getDtaFimPeriodoReservaLegal()));
		}
		
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListGestorFinanceiro())){
			String gestorFinanceiro = "";	
			for (Integer ideGestor : filtroImovelRuralRelatorioDTO.getListGestorFinanceiro()) {
				gestorFinanceiro += retornaNomGestorFinanceiro(ideGestor) + ", ";
			}
			gestorFinanceiro = gestorFinanceiro.substring(0, gestorFinanceiro.length() - 2);			
			mapFiltros.put("gestorFinanceiro", gestorFinanceiro);
		}
		
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getListContratoConvenio())){
			String  contratoConvenio = "";
			for (Integer ideContrato : filtroImovelRuralRelatorioDTO.getListContratoConvenio()) {
				contratoConvenio += retornaNomContratoConvenio(ideContrato) + ", ";
			}
			contratoConvenio = contratoConvenio.substring(0, contratoConvenio.length() - 2);
			mapFiltros.put("contratoConvenio", contratoConvenio);
		}
		
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getModulosFiscais()) && filtroImovelRuralRelatorioDTO.getModulosFiscais() > 0) {
			String  modulosFiscais = "";
			filtros += "&nbsp;&nbsp;<b>Módulos fiscais: </b><br>";
			if(filtroImovelRuralRelatorioDTO.getModulosFiscais().equals(1)) {
				modulosFiscais = "Até 04 módulos";
			} else {
				modulosFiscais = "Acima de 04 módulos";
			}
			mapFiltros.put("modulosFiscais", modulosFiscais);
		}
		
		if(!Util.isNull(filtroImovelRuralRelatorioDTO.getTipoArl()) && filtroImovelRuralRelatorioDTO.getTipoArl() > 0) {
			String  tipoReservalegal = "";
			if(filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.NPI.getId())) {
				tipoReservalegal = "No próprio imóvel";
			}
			
			if(filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECIP.getId())) {
				tipoReservalegal = "Em compensação entre imóveis de mesmo proprietário";
			}
			
			if(filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECSF.getId())) {
				tipoReservalegal = "Em compensação por Servidão Ambiental";
			}
			
			if(filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.ECOND.getId()) || filtroImovelRuralRelatorioDTO.getTipoArl().equals(TipoArlEnum.CDAUC.getId())) {			
				tipoReservalegal = "Em condomínio";
			}
			mapFiltros.put("tipoReservalegal", tipoReservalegal);
		}
		
		
		if(filtroImovelRuralRelatorioDTO.isCadastroCda() 
				|| filtroImovelRuralRelatorioDTO.isNumeroCar() 
				|| filtroImovelRuralRelatorioDTO.isPraCadastrado()
				|| filtroImovelRuralRelatorioDTO.isOutroPassivoAmbiental()
				|| filtroImovelRuralRelatorioDTO.isLimiteBloqueado()
				|| filtroImovelRuralRelatorioDTO.isRlAprovada()
				|| filtroImovelRuralRelatorioDTO.isSobreposicaoRLAPP()
				|| filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()
				|| filtroImovelRuralRelatorioDTO.isCadastroIncra()) {
			
			String  imoveisCom = "";
			if(filtroImovelRuralRelatorioDTO.isNumeroCar()) {
				imoveisCom += "Número CAR, ";
			}
			
			if(filtroImovelRuralRelatorioDTO.isPraCadastrado()) {			
				imoveisCom += "PRA cadastrado, ";
			}
			
			if(filtroImovelRuralRelatorioDTO.isLimiteBloqueado()) {			
				imoveisCom += "Limite Bloqueado, ";
			}
			
			if(filtroImovelRuralRelatorioDTO.isRlAprovada()) {
				imoveisCom += "RL aprovada, ";
			}
			
			if(filtroImovelRuralRelatorioDTO.isOutroPassivoAmbiental()) {
				imoveisCom += "Outro passivo ambiental fora de APP e RL, ";
			}			
			
			if(filtroImovelRuralRelatorioDTO.isSobreposicaoRLAPP()) {
				imoveisCom += "Sobreposição de RL e APP, ";
			}
			
			if(filtroImovelRuralRelatorioDTO.isRlMenorQueVintePorCento()) {
				imoveisCom += "Área de RL menor que 20% da área do imóvel rural, ";
			}
			
			if(imoveisCom.length()>=2){
				imoveisCom = imoveisCom.substring(0, imoveisCom.length() - 2);
			}
			
			mapFiltros.put("imoveisCom", imoveisCom);
		}
		
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getDocumentoFinal())) {
			String  documentoFinal = retornaNomTipoDocumentoFinal(filtroImovelRuralRelatorioDTO.getDocumentoFinal());
			mapFiltros.put("documentoFinal", documentoFinal);
		}
		
		if(!Util.isNullOuVazio(filtroImovelRuralRelatorioDTO.getNumCertificado())) {
			String numeroDocumentoFinal = "Nº " + mapFiltros.get("documentoFinal")+ " " + filtroImovelRuralRelatorioDTO.getNumCertificado();
			mapFiltros.put("numeroDocumentoFinal", numeroDocumentoFinal);
		}		
		
		if(filtroImovelRuralRelatorioDTO.isCadastroBndes()) {
			mapFiltros.put("tipoCadastro", "Imóvel Rural Projeto BNDES");
		} else if(filtroImovelRuralRelatorioDTO.isCadastroIncra()) {
			mapFiltros.put("tipoCadastro", "Assentamento INCRA");
		} else if(filtroImovelRuralRelatorioDTO.isCadastroCda()) {
			mapFiltros.put("tipoCadastro", "Imóvel Rural CDA");
		}
		
		if(filtroImovelRuralRelatorioDTO.isModuloImovelRural()) {
			mapFiltros.put("moduloCadastro", "Imóvel Rural");
		} else if(filtroImovelRuralRelatorioDTO.isModuloAssentamento()) {
			mapFiltros.put("moduloCadastro", "Assentamento");
		} else if(filtroImovelRuralRelatorioDTO.isModuloPCT()) {
			mapFiltros.put("moduloCadastro", "Povos e Comunidades Tradicionais");
		}
		
		if(this.getQuestionarioPreenchido()) {
			mapFiltros.put("questionario", "Preenchido");
		} else if(this.getQuestionarioNaoPreenchido()) {
			mapFiltros.put("questionario", "Não Preenchido");
		}
		
		if(exibeDscTotalImoveis)		
					
			mapFiltros.put("dscTotalImoveis", getDscTotalImoveisPdf());
		
		return mapFiltros;
	}
	
	public String getDscTotalImoveisPdf(){
		String dscTotalImoveis = "";
		DecimalFormat df = new DecimalFormat("###,###,##0", new DecimalFormatSymbols (new Locale ("pt", "BR")));
		if(!Util.isNullOuVazio(listaImoveis)){
			dscTotalImoveis = "Total de <b>" + df.format(listaImoveis.size()) + "</b> imóveis";
			if(colunasExibirImovelRuralRelatorioDTO.isValArea()){
				dscTotalImoveis += " somando uma área total de <b>" + retornaSomaAreaImoveis() + " ha</b>.";
			}
		}
		return dscTotalImoveis;	
	}

	private String retornaNomContratoConvenio(Integer ideContrato) {
		for (ContratoConvenioCefir contrato : this.listaContratoConvenio) {
			if(contrato.getIdeContratoConvenioCefir().equals(ideContrato)){
				return contrato.getNomContratoConvenioCefir();
			}
		}
		return null;
	}

	private String retornaNomGestorFinanceiro(Integer ideGestor) {
		for (GestorFinanceiroCefir gestor : this.listaGestorFinanceiro) {
			if(gestor.getIdeGestorFinanceiroCefir().equals(ideGestor)){
				return gestor.getNomGestorFinanceiroCefir();
			}
		}
		return null;
	}
	
	public void createDynamicColumns() {        
        if(!Util.isNull(colunasExibirImovelRuralRelatorioDTO)) {
        	colunasExibirImovelRuralRelatorioDTO.preparaMapColunas();
        }
    }

	private String retornaNomTipoDocumentoFinal(Integer ideTipoDocumentoFinal) {
		if(ideTipoDocumentoFinal.equals(TipoCertificadoEnum.TERMO_DE_COMPROMISSO.getId()))
			return "Termo de Compromisso";
		else if(ideTipoDocumentoFinal.equals(TipoCertificadoEnum.CEFIR.getId()))
			return "Certificado de Inscrição";
		else if(ideTipoDocumentoFinal.equals(TipoCertificadoEnum.AVISO_BNDES.getId()))
			return "Aviso";
		
		return "";
	}

	private String retornaNomStatusCadastro(Integer ideStatusCadastro) {		
		if(ideStatusCadastro.equals(StatusCadastroImovelRuralEnum.REGISTRO_INCOMPLETO.getId()))
			return "Registro incompleto";
		else if(ideStatusCadastro.equals(StatusCadastroImovelRuralEnum.REGISTRADO.getId()))
			return "Registrado";
		else if(ideStatusCadastro.equals(StatusCadastroImovelRuralEnum.CADASTRADO.getId()))
			return "Cadastrado";
		
		return "";
	}

	private String retornaNomMunicipio(Integer ideMunicipio) {
		for (Municipio municipio : listaMunicipios) {
			if(municipio.getIdeMunicipio().equals(ideMunicipio)){
				return municipio.getNomMunicipio();
			}
		}
		return null;
	}
	
	public Pessoa obterProprietarioImovel(Integer ideImovelRural) {
		Collection<PessoaImovel> listProprietarios;
		try {
			listProprietarios = imovelRuralFacade.filtrarPROPRIETARIOImovel(new Imovel(ideImovelRural));
			if(!Util.isNullOuVazio(listProprietarios)) {
				return listProprietarios.iterator().next().getIdePessoa();
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return new Pessoa();
	}

	public ColunasExibirImovelRuralRelatorioDTO getColunasExibirImovelRuralRelatorioDTO() {
		return colunasExibirImovelRuralRelatorioDTO;
	}

	public void setColunasExibirImovelRuralRelatorioDTO(
			ColunasExibirImovelRuralRelatorioDTO colunasExibirImovelRuralRelatorioDTO) {
		this.colunasExibirImovelRuralRelatorioDTO = colunasExibirImovelRuralRelatorioDTO;
	}
	
	public void setColunasExibirRelatorioQuantitativoImoveisRuraisDTO(
			ColunasExibirRelatorioQuantitativoImoveisRuraisDTO colunasExibirRelatorioQuantitativoImoveisRuraisDTO) {
		this.colunasExibirRelatorioQuantitativoImoveisRuraisDTO = colunasExibirRelatorioQuantitativoImoveisRuraisDTO;
	}	
	
	public ColunasExibirRelatorioQuantitativoImoveisRuraisDTO getColunasExibirRelatorioQuantitativoImoveisRuraisDTO() {
		return colunasExibirRelatorioQuantitativoImoveisRuraisDTO;
	}

	public List<ImovelRuralRelatorioDTO> getListaImoveis() {
		return listaImoveis;
	}

	public void setListaImoveis(List<ImovelRuralRelatorioDTO> listaImoveis) {
		this.listaImoveis = listaImoveis;
	}
	
	public boolean isBuscaAvancada() {
		return buscaAvancada;
	}

	public void setBuscaAvancada(boolean buscaAvancada) {
		this.buscaAvancada = buscaAvancada;
	}

	
	public String getDscTotalImoveis(){
		String dscTotalImoveis = "";
		DecimalFormat df = new DecimalFormat("###,###,##0", new DecimalFormatSymbols (new Locale ("pt", "BR")));
		if(!Util.isNullOuVazio(listaImoveis)){
			dscTotalImoveis = df.format(listaImoveis.size());			
		}
		return dscTotalImoveis;	
	}
	
	public String getDscAreaTotalImoveis(){
		String dscTotalImoveis = "";
		if(!Util.isNullOuVazio(listaImoveis)){
			dscTotalImoveis += retornaSomaAreaImoveis();
		}
		return dscTotalImoveis;	
	}

	private String retornaSomaAreaImoveis() {
		DecimalFormat df = new DecimalFormat("#,##0.0000", new DecimalFormatSymbols (new Locale ("pt", "BR")));
		Double valAreaTotal = 0.0;
		for (ImovelRuralRelatorioDTO imovel : listaImoveis) {
			valAreaTotal += imovel.getValArea();
		}
		return df.format(valAreaTotal);
	}
	
	
	/**
	 * Método responsável por carregar as variaveis da busca avancada
	 */
	public void selecionaBuscaAvancada() {
		limpaFiltrosBuscaAvancada();
		
		if(this.buscaAvancada) {
			this.buscaAvancada = false;
		} else {
			this.buscaAvancada = true;
		}
	}
	
	public String getDataAtual() {
		Date data = new Date(System.currentTimeMillis());
		SimpleDateFormat formatarDate = new SimpleDateFormat("dd/MM/yyyy");
		return formatarDate.format(data);
	}
	
	public boolean getMostrarTabela() {
		return mostrarTabela;
	}
	
	//FIM NOVO
	
	
	// Relatório quantitativo
	public StreamedContent getImpRelatorioQuantitativoImovelRural(){
		try {
			String listaIdImovel = obterListaIdRelatorio();
			carregarFiltroImovelRuralRelatorioDTO();
 			return relatorioQuantitativoImovelRuralService.imprimirRelatorioQuantitativoImoveisRurais(filtroImovelRuralRelatorioDTO, colunasExibirRelatorioQuantitativoImoveisRuraisDTO, montarDscFiltrosRelatorio(false), listaIdImovel, listaImoveis.size());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	// Método que pega todos os ids dos imoveis da listaImoveis e retorna uma string com os id separado por ',' para ser utilizado na consulta 
	// do relatório quantitativo imovel rural.
	private String obterListaIdRelatorio() {
		StringBuilder lista = new StringBuilder();
	
		for (ImovelRuralRelatorioDTO imovel : listaImoveis) {
			lista.append(imovel.getIdeImovelRural()+",");
		}
		
		lista.replace(lista.lastIndexOf(","),lista.lastIndexOf(",")+1,"");
		
		return lista.toString();
	}
	
		
	public boolean getDesabilitaBtnImprimir(){
		return !colunasExibirRelatorioQuantitativoImoveisRuraisDTO.isAlgumTemaSelecionado();
	}	
	
	public boolean isTipoDeImpressaoPdf() {
		return tipoDeImpressaoPdf;
	}

	public void setTipoDeImpressaoPdf(boolean tipoDeImpressaoPdf) {
		this.tipoDeImpressaoPdf = tipoDeImpressaoPdf;
	}
	
	public boolean isRenderedBtnDownloadAvisoCarLote(){
		return habilitaDownloadAvisoCarLote
				&& this.isTipoFiltroSelecionadoMunicipio() 
				&& this.buscaAvancada 
				&& !this.disableContratoConvenio
				&& !Util.isNullOuVazio(this.listaContratoConvenio) 
				&& !Util.isNullOuVazio(this.filtroImovelRuralRelatorioDTO)
				&& !Util.isNullOuVazio(this.filtroImovelRuralRelatorioDTO.getListContratoConvenio())
				&& this.mostrarTabela 
				&& this.colunasExibirImovelRuralRelatorioDTO.getQtdColunasExibir() > 0;
 	}
	
	public StreamedContent getImprimirAvisoBndes(Integer ideImovelRural){
		return imovelRuralFacade.getImprimirAvisoBndes(ideImovelRural, false);
	}
	
	/**
	 * Método responsável por fazer o download do arquivo zip contendo os 'Avisos Projeto CAR/BNDES/INEMA' da listaImoveis.
	 * 
	 * @author ivanildo.souza
	 * @since 06/10/16
	 */
	public void getDownloadAvisoCarBndesLote(){
		try {
			List<StreamedContent> listAvisoCarBndesInema = new ArrayList<StreamedContent>();  
			for (ImovelRuralRelatorioDTO imovelRuralRelatorioDTO : this.listaImoveis) {

				if(imovelRuralRelatorioDTO.isAvisoBndes()){
					listAvisoCarBndesInema.add(imovelRuralFacade.getImprimirAvisoBndes(imovelRuralRelatorioDTO.getIdeImovelRural(), false));
				}
			}
			InputStream arquivo = Util.comprimirListaStreamedContentParaZip(listAvisoCarBndesInema);
			SessaoUtil.adicionarObjetoSessao("arquivo", arquivo);
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			String url =  request.getScheme() + "://" + request.getServerName();
			 
			if (request.getServerPort()!=80){
				url += ":" + request.getServerPort();
			}
			
			RequestContext.getCurrentInstance().execute("window.open('"+url+"/aviso-car-lote/')");
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Erro ao fazer o download do arquivo");
			
		}
	}
	
	public boolean habilitarDownloadAvisoCarLoteMethod(){
		List<ImovelRuralRelatorioDTO> listImovel = (List<ImovelRuralRelatorioDTO>) listaImoveisDT;
		for (ImovelRuralRelatorioDTO imovel : listImovel) {
			if(!habilitaDownloadAvisoCarLote && imovel.isAvisoBndes()){
				habilitaDownloadAvisoCarLote = true;
			}
		}
		return habilitaDownloadAvisoCarLote;
	}
	
	public void carregarProprietarios(final ImovelRuralRelatorioDTO imovel){
		
		try {
			
			listaPessoa = new LazyDataModel<Pessoa>() {
				

				/**
				 * 
				 */
				private static final long serialVersionUID = 1757432349270279372L;

				@Override
				public List<Pessoa> load(int first, int pageSize, String arg2, SortOrder arg3, Map<String, String> arg4) {
					
					
					try {
						List<Pessoa> lista = (List<Pessoa>) imovelRuralFacade.listarProprietariosJustoPossuidoresImovelPorDemanda(imovel.getIdeImovelRural(), first, pageSize);
						return lista;
					} catch (Exception e) {
						Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					}
					
					return null;
				}
			}; 
					
			this.listaPessoa.setRowCount(imovelRuralFacade.listarProprietariosJustoPossuidoresImovelCount(imovel.getIdeImovelRural()));
			
			Html.exibir("dialogProprietarios");
			Html.atualizar("formProprietarios:dataTablePessoas");
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		System.out.println(imovel);
	}
	

	public LazyDataModel<ImovelRuralRelatorioDTO> getListaImoveisDT() {
		return listaImoveisDT;
	}

	public void setListaImoveisDT(
			LazyDataModel<ImovelRuralRelatorioDTO> listaImoveisDT) {
		this.listaImoveisDT = listaImoveisDT;
	}

	public MetodoUtil getMetodoSelecionarSolicitante() {
		return metodoSelecionarSolicitante;
	}

	public void setMetodoSelecionarSolicitante(MetodoUtil metodoSelecionarSolicitante) {
		this.metodoSelecionarSolicitante = metodoSelecionarSolicitante;
	}

	public LazyDataModel<Pessoa> getListaPessoa() {
		return listaPessoa;
	}

	public void setListaPessoa(LazyDataModel<Pessoa> listaPessoa) {
		this.listaPessoa = listaPessoa;
	}

	public boolean isDisableLimiteBloqueado() {
		return disableLimiteBloqueado;
	}

	public void setDisableLimiteBloqueado(boolean disableLimiteBloqueado) {
		this.disableLimiteBloqueado = disableLimiteBloqueado;
	}

	public boolean isDisableImovelRural() {
		return disableImovelRural;
	}

	public void setDisableImovelRural(boolean disableImovelRural) {
		this.disableImovelRural = disableImovelRural;
	}

	public boolean isDisableAssentamento() {
		return disableAssentamento;
	}

	public void setDisableAssentamento(boolean disableAssentamento) {
		this.disableAssentamento = disableAssentamento;
	}
	
	public void setDisablePeriodoReservaLegal(boolean disablePeriodoReservaLegal) {
		this.disablePeriodoReservaLegal = disablePeriodoReservaLegal;
	}
	
	public boolean isDisablePCT() {
		return disablePCT;
	}

	public void setDisablePCT(boolean disablePCT) {
		this.disablePCT = disablePCT;
	}	
}