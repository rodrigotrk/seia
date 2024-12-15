package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.FceEnergia;
import br.gov.ba.seia.entity.FceEnergiaEolica;
import br.gov.ba.seia.entity.FceEnergiaEolicaLicencaPrevia;
import br.gov.ba.seia.entity.FceEnergiaEolicaParque;
import br.gov.ba.seia.entity.FceEnergiaEolicaParqueAerogerador;
import br.gov.ba.seia.entity.FceEnergiaFinalidade;
import br.gov.ba.seia.entity.FceEnergiaSolar;
import br.gov.ba.seia.entity.FceEnergiaSolarUsina;
import br.gov.ba.seia.entity.FceEnergiaTermoEletrica;
import br.gov.ba.seia.entity.FceEnergiaTermoeletricaUnidade;
import br.gov.ba.seia.entity.FceEnergiaTermoeletricaUnidadeCombustivel;
import br.gov.ba.seia.entity.FceEnergiaTermoeletricaUnidadeGerador;
import br.gov.ba.seia.entity.FinalidadeGeracaoEnergia;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.RequerimentoTipologia;
import br.gov.ba.seia.entity.TipoCombustivel;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.facade.LocalizacaoGeograficaServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AtoAmbientalService;
import br.gov.ba.seia.service.EnquadramentoService;
import br.gov.ba.seia.service.FceEnergiaEolicaLicencaPreviaService;
import br.gov.ba.seia.service.FceEnergiaEolicaParqueAerogeradorService;
import br.gov.ba.seia.service.FceEnergiaEolicaParqueService;
import br.gov.ba.seia.service.FceEnergiaEolicaService;
import br.gov.ba.seia.service.FceEnergiaFinalidadeService;
import br.gov.ba.seia.service.FceEnergiaService;
import br.gov.ba.seia.service.FceEnergiaSolarService;
import br.gov.ba.seia.service.FceEnergiaSolarUsinaService;
import br.gov.ba.seia.service.FceEnergiaTermoeletricaService;
import br.gov.ba.seia.service.FceEnergiaTermoeletricaUnidadeCombustivelService;
import br.gov.ba.seia.service.FceEnergiaTermoeletricaUnidadeGeradorService;
import br.gov.ba.seia.service.FceEnergiaTermoeletricaUnidadeService;
import br.gov.ba.seia.service.FceService;
import br.gov.ba.seia.service.FinalidadeGeracaoEnergiaService;
import br.gov.ba.seia.service.RequerimentoTipologiaService;
import br.gov.ba.seia.service.TipoCombustivelService;
import br.gov.ba.seia.service.ValidacaoGeoSeiaService;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.Util;

/**
 * Controller responsável pelo <b>FCE - Geração Energia</b>
 *
 * @author anderson.silva
 * @since 09/02/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8497">#8497</a>
 */

@Named("fceGeracaoEnergiaController")
@ViewScoped
public class FceGeracaoEnergiaController extends FceController {

	@EJB
	private FceService fceService;
	
	@EJB
	private FceEnergiaService fceEnergiaService;
	 
	@EJB
	private FinalidadeGeracaoEnergiaService finalidadeGeracaoEnergiaService;
	
	@EJB
	private FceEnergiaFinalidadeService fceEnergiaFinalidadeService;
	
	@EJB
	private AtoAmbientalService atoAmbientalService;
	
	@EJB
	private FceEnergiaEolicaService fceEnergiaEolicaService;
	
	@EJB
	private FceEnergiaEolicaParqueService fceEnergiaEolicaParqueService;
	
	@EJB
	private FceEnergiaEolicaParqueAerogeradorService fceEnergiaEolicaParqueAerogeradorService;
	
	@EJB
	private FceEnergiaEolicaLicencaPreviaService fceEnergiaEolicaLicencaPreviaService;
	
	@EJB
	private FceEnergiaSolarService fceEnergiaSolarService;
	
	@EJB
	private FceEnergiaSolarUsinaService fceEnergiaSolarUsinaService;
	
	@EJB
	private TipoCombustivelService tipoCombustivelService;
	
	@EJB
	private FceEnergiaTermoeletricaService fceEnergiaTermoeletricaService;
	
	@EJB
	private FceEnergiaTermoeletricaUnidadeService fceEnergiaTermoeletricaUnidadeService;
	
	@EJB
	private FceEnergiaTermoeletricaUnidadeGeradorService fceEnergiaTermoeletricaUnidadeGeradorService;
	
	@EJB
	private FceEnergiaTermoeletricaUnidadeCombustivelService fceEnergiaTermoeletricaUnidadeCombustivelService;
	
	@EJB
	private LocalizacaoGeograficaServiceFacade locGeoService;
	
	@EJB
	private ValidacaoGeoSeiaService validacaoGeoSeiaService;
	
	@EJB
	private RequerimentoTipologiaService requerimentoTipologiaService;
	
	@EJB
	private EnquadramentoService enquadramentoService;
	
	private static final DocumentoObrigatorio DOC_OBRIGATORIO_GERACAO_ENERGIA = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_GERACAO_ENERGIA.getId(),"Formulário de Caracterização do Empreendimento - Geração de Energia");
	
	private FceEnergia fceEnergia;

	private FceEnergiaEolica fceEnergiaEolica;
	
	private FceEnergiaEolicaLicencaPrevia fceEnergiaEolicaLicencaPrevia; 
	
	private FceEnergiaEolicaParque fceEnergiaEolicaParque;
	
	private FceEnergiaEolicaParqueAerogerador fceEnergiaEolicaParqueAerogerador;
	
	private FceEnergiaSolar fceEnergiaSolar;
	
	private FceEnergiaSolarUsina fceEnergiaSolarUsina;
	
	private FceEnergiaTermoEletrica fceEnergiaTermoeletrica;
	
	private FceEnergiaTermoeletricaUnidade fceEnergiaTermoeletricaUnidade;
	
	private List<AtoAmbiental> listaAtoAmbiental;
	
	private List<FinalidadeGeracaoEnergia> listaFinalidadeGeracaoEnergiaSelecionado;
	
	private List<FinalidadeGeracaoEnergia> listaFinalidadeGeracaoEnergiaAux;
	
	private List<FceEnergiaEolicaParqueAerogerador> listaEnergiaEolicaParqueAerogerador;
	
	private List<FceEnergiaEolicaParque> listaEnergiaEolicaParque;
	
	private List<FceEnergiaSolarUsina> listaEnergiaSolarUsina;
	
	private List<FceEnergiaTermoeletricaUnidade> listaEnergiaTermoeletricaUnidade;
	
	private List<TipoCombustivel> listaTipoCombustivelSelecionado;
	
	private FceEnergiaEolicaParqueAerogerador fceEnergiaEolicaParqueAerogeradorAux;
	
	private FceEnergiaEolicaParque fceEnergiaEolicaParqueAux;
	
	private List<FceEnergiaEolicaParque> listaFceEnergiaEolicaParqueAuxExcluido;
	
	private FceEnergiaSolarUsina fceEnergiaSolarUsinaAux;
	
	private FceEnergiaTermoeletricaUnidade fceEnergiaTermoeletricaUnidadeAux;
	
	private FceEnergiaTermoeletricaUnidadeCombustivel fceEnergiaTermoeletricaUnidadeCombustivel;
	
	private FceEnergiaTermoeletricaUnidadeGerador fceEnergiaTermoeletricaUnidadeGerador;
	
	private FceEnergiaTermoeletricaUnidadeGerador fceEnergiaTermoeletricaUnidadeGeradorAux;
	
	private List<FinalidadeGeracaoEnergia> listFinalidadeGeracaoEnergia;
	
	private LocalizacaoGeografica locGeoAux;
	
	private String municipioAbaGeral;
	
	private String municipioAbaEolica;
	
	private String municipioAbaSolar;
	
	private String municipioAbaTermoeletrica;
	
	private int activeIndex; 
	
	private int numeroAba;
	
	private Integer numeroTotalParque;
	
	private Integer numeroTotalAerogerador;
	
	private Integer numeroTotalUsina;
	
	private Integer numeroTotalUnidade;
	
	private BigDecimal numeroTotalPotencia;
	
	private BigDecimal areaTotalUsina;
	
	private Integer totalAerogeradores;
	
	private BigDecimal totalPotenciaAerogeradores;
	
	private Boolean desabilitaAbaEnergiaEolica = Boolean.FALSE;
	
	private Boolean desabilitaAbaEnergiaSolar = Boolean.FALSE;
	
	private Boolean desabilitaAbaTermoeletrica = Boolean.FALSE;
	
	private Boolean desabilitaCaptacaoAgua = Boolean.FALSE;
	
	private Boolean desabilitaASV = Boolean.FALSE;
	
	private Boolean renderedEnergiaEolica = Boolean.TRUE;
	
	private Boolean alterarAerogerador = Boolean.FALSE;
	
	private Boolean alterarParque = Boolean.FALSE;
	
	private Boolean visualizarParque = Boolean.FALSE;
	
	private Boolean visualizarUsina = Boolean.FALSE;
	
	private Boolean visualizarUnidadeTermo = Boolean.FALSE;
	
	private Boolean alterarUnidadeTermo = Boolean.FALSE;
	
	private Boolean alterarUsina = Boolean.FALSE;
	
	private Boolean alterarGerador = Boolean.FALSE;
	
	private Boolean desabilitaAbas = Boolean.TRUE;
	
	private Boolean flagFinalizar = Boolean.FALSE;
	
	private Integer totalNumeroGeradoresParque;
	
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");
	
	private MetodoUtil metodoAtualizarExterno;
	
	private boolean prepararDublicacao = false;
	
	private String msgImprimirRelatorio;
	
	private Integer quantidadeGeralAerogerador;
	
	private Double quantidadePotenciaTermoeletrica;
	
	private Double quantidadeAreaSolarFotovoltaica;
	
	@Override
	public void init() {
		try {
			iniciarFceEnergia();
			iniciarFceEnergiaEolica();
			iniciarEnergiaEolicaLicencaPrevia();
			iniciarFceEnergiaEolicaParque();
			iniciarFceEnergiaEolicaParqueAerogerador();
			iniciarEnergiaSolar();
			iniciarEnergiaSolarUsina();
			iniciarEnergiaTermoeletrica();
			iniciarEnergiaTermoeletricaCombustivel();
			iniciarEnergiaTermoeletricaGerador();
			iniciarListas();
			verificarEdicao();
			this.numeroAba = 1;
			this.activeIndex = 0;
			this.totalAerogeradores = 0;
			this.totalPotenciaAerogeradores = new BigDecimal(0);
			if(!isFceSalvo()){
				super.iniciarFce(DOC_OBRIGATORIO_GERACAO_ENERGIA);
				super.fce.getIdeRequerimento().setIdeRequerimento(super.requerimento.getIdeRequerimento());
				super.fce.getIdeDocumentoObrigatorio().setNomDocumentoObrigatorio("Formulário de Caracterização do Empreendimento - Geração de Energia");
			}
			metodoAtualizarExterno = new MetodoUtil(this, this.getClass().getMethod("atualizarArea", null));
			abrirModalGeracaoEnergia();
			obterQuantidadesInformadaNoRequerimento();
			gerarMsgImprimirRelatorio("Geração de Energia");
		} catch (SecurityException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		} catch (NoSuchMethodException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void verificarEdicao(){
		super.carregarFceDoRequerente(DOC_OBRIGATORIO_GERACAO_ENERGIA);
	}
	
	public void atualizarArea(){
		try {
			if(!Util.isNull(fceEnergia.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())){
				this.fceEnergia.getIdeLocalizacaoGeografica().setVlrArea(BigDecimal.valueOf(this.locGeoService.retornarAreaShape(this.fceEnergia.getIdeLocalizacaoGeografica())));
			}

			if(!Util.isNull(fceEnergiaEolicaParque.getLocalizacaoGeografica().getIdeLocalizacaoGeografica())){
				this.fceEnergiaEolicaParque.getLocalizacaoGeografica().setVlrArea(BigDecimal.valueOf(this.locGeoService.retornarAreaShape(this.fceEnergiaEolicaParque.getLocalizacaoGeografica())));
			}
			
			if(!Util.isNull(fceEnergiaSolarUsina.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())){
				this.fceEnergiaSolarUsina.getIdeLocalizacaoGeografica().setVlrArea(BigDecimal.valueOf(this.locGeoService.retornarAreaShape(this.fceEnergiaSolarUsina.getIdeLocalizacaoGeografica())));
				fceEnergiaSolarUsina.setValorAreaUsina(fceEnergiaSolarUsina.getIdeLocalizacaoGeografica().getVlrArea());
			}
			
			if(!Util.isNull(fceEnergiaTermoeletricaUnidade.getLocalizacaoGeografica().getIdeLocalizacaoGeografica())){
				this.fceEnergiaTermoeletricaUnidade.getLocalizacaoGeografica().setVlrArea(BigDecimal.valueOf(this.locGeoService.retornarAreaShape(this.fceEnergiaTermoeletricaUnidade.getLocalizacaoGeografica())));
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	private void iniciarListas() {
		this.listaAtoAmbiental = new ArrayList<AtoAmbiental>();
		this.listaFinalidadeGeracaoEnergiaSelecionado = new ArrayList<FinalidadeGeracaoEnergia>();
		this.listaEnergiaEolicaParque = new ArrayList<FceEnergiaEolicaParque>();
		this.listaEnergiaEolicaParqueAerogerador = new ArrayList<FceEnergiaEolicaParqueAerogerador>();
		this.listaEnergiaSolarUsina = new ArrayList<FceEnergiaSolarUsina>();
		this.listaEnergiaTermoeletricaUnidade = new ArrayList<FceEnergiaTermoeletricaUnidade>();
	}

	private void iniciarEnergiaEolicaLicencaPrevia() {
		this.fceEnergiaEolicaLicencaPrevia = new FceEnergiaEolicaLicencaPrevia();
		this.fceEnergiaEolicaLicencaPrevia.setFceEnergiaEolica(new FceEnergiaEolica());
	}

	private void iniciarFceEnergiaEolica() {
		this.renderedEnergiaEolica = Boolean.TRUE;
		this.fceEnergiaEolica = new FceEnergiaEolica();
		this.fceEnergiaEolica.setIdeFceEnergia(new FceEnergia());
	}

	private void iniciarFceEnergia() {
		this.fceEnergia = new FceEnergia(super.fce);
		this.fceEnergia.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		this.fceEnergia.setIndASV(Boolean.FALSE);
		this.fceEnergia.setIndCaptacao(Boolean.FALSE);
		this.fceEnergia.setListaFinalidadeGeracaoEnergia(new ArrayList<FinalidadeGeracaoEnergia>());
		listFinalidadeGeracaoEnergia = new ArrayList<FinalidadeGeracaoEnergia>();
	}

	private void iniciarFceEnergiaEolicaParque(){
		this.visualizarParque = Boolean.FALSE;
		this.alterarParque = Boolean.FALSE;
		this.municipioAbaEolica = null;
		this.fceEnergiaEolicaParque = new FceEnergiaEolicaParque();
		this.fceEnergiaEolicaParque.setFceEnergiaEolica(new FceEnergiaEolica());
		this.fceEnergiaEolicaParque.setLocalizacaoGeografica(new LocalizacaoGeografica());
		this.fceEnergiaEolicaParque.setListaAerogerador(new ArrayList<FceEnergiaEolicaParqueAerogerador>());
		this.fceEnergiaEolicaParqueAux = new FceEnergiaEolicaParque();
		this.listaEnergiaEolicaParqueAerogerador = new ArrayList<FceEnergiaEolicaParqueAerogerador>();
		this.totalAerogeradores = 0;
		this.totalPotenciaAerogeradores = new BigDecimal(0);
	}
	
	public void iniciarFceEnergiaEolicaParqueAerogerador(){
		this.alterarAerogerador = Boolean.FALSE;
		this.fceEnergiaEolicaParqueAerogerador = new FceEnergiaEolicaParqueAerogerador();
		this.fceEnergiaEolicaParqueAerogerador.setFceEnergiaEolicaParque(new FceEnergiaEolicaParque());
		this.fceEnergiaEolicaParqueAerogeradorAux = new FceEnergiaEolicaParqueAerogerador();
	}
	
	public void iniciarEnergiaSolar(){
		
		this.fceEnergiaSolar = new FceEnergiaSolar();
		this.fceEnergiaSolar.setIdeFceEnergia(new FceEnergia());
		
	}
	
	public void iniciarEnergiaSolarUsina(){
		
		this.visualizarUsina = Boolean.FALSE;
		this.alterarUsina = Boolean.FALSE;
		this.municipioAbaSolar = null;
		this.fceEnergiaSolarUsina = new FceEnergiaSolarUsina();
		this.fceEnergiaSolarUsina.setIdeFceEnergiaSolar(new FceEnergiaSolar());
		this.fceEnergiaSolarUsina.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		this.fceEnergiaSolarUsinaAux = new FceEnergiaSolarUsina();
		
	}
	
	public void iniciarEnergiaTermoeletrica(){
		
		this.visualizarUnidadeTermo = Boolean.FALSE;
		this.alterarUnidadeTermo = Boolean.FALSE;
		this.municipioAbaTermoeletrica = null;
		this.fceEnergiaTermoeletrica = new FceEnergiaTermoEletrica();
		this.fceEnergiaTermoeletrica.setIdeFceEnergia(new FceEnergia());
		this.fceEnergiaTermoeletricaUnidade = new FceEnergiaTermoeletricaUnidade();
		this.fceEnergiaTermoeletricaUnidade.setFceEnergiaTermoeletrica(new FceEnergiaTermoEletrica());
		this.fceEnergiaTermoeletricaUnidade.setLocalizacaoGeografica(new LocalizacaoGeografica());
		this.fceEnergiaTermoeletricaUnidade.setIndSistemaControleEmissao(Boolean.FALSE);
		this.fceEnergiaTermoeletricaUnidade.setListaCombustivel(new ArrayList<FceEnergiaTermoeletricaUnidadeCombustivel>());
		this.fceEnergiaTermoeletricaUnidade.setListaGerador(new ArrayList<FceEnergiaTermoeletricaUnidadeGerador>());
		this.fceEnergiaTermoeletricaUnidade.setListaTipoCombustivel(new ArrayList<TipoCombustivel>());
		if(!Util.isNullOuVazio(this.tipoCombustivelService)){
			try {
				
				this.fceEnergiaTermoeletricaUnidade.getListaTipoCombustivel().addAll(this.tipoCombustivelService.listarTodos());
				
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
		}
		this.listaTipoCombustivelSelecionado = new ArrayList<TipoCombustivel>();
		this.fceEnergiaTermoeletricaUnidadeAux = new FceEnergiaTermoeletricaUnidade();
		
	}
	
	public void iniciarEnergiaTermoeletricaCombustivel(){
		
		this.fceEnergiaTermoeletricaUnidadeCombustivel = new FceEnergiaTermoeletricaUnidadeCombustivel();
		this.fceEnergiaTermoeletricaUnidadeCombustivel.setFceEnergiaTermoeletricaUnidade(new FceEnergiaTermoeletricaUnidade());
		this.fceEnergiaTermoeletricaUnidadeCombustivel.setTipoCombustivel(new TipoCombustivel());
		
	}

	public void iniciarEnergiaTermoeletricaGerador(){
	
		this.alterarGerador = Boolean.FALSE;
		this.fceEnergiaTermoeletricaUnidadeGerador = new FceEnergiaTermoeletricaUnidadeGerador();
		this.fceEnergiaTermoeletricaUnidadeGerador.setFceEnergiaTermoeletricaUnidade(new FceEnergiaTermoeletricaUnidade());
		this.fceEnergiaTermoeletricaUnidadeGeradorAux = new FceEnergiaTermoeletricaUnidadeGerador();
		this.fceEnergiaTermoeletricaUnidadeGeradorAux.setFceEnergiaTermoeletricaUnidade(new FceEnergiaTermoeletricaUnidade());
		
	}
	
	public int getClassificacaoShape(){
		return ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId().intValue();
	}
	
	private void obterQuantidadesInformadaNoRequerimento() {
		quantidadeGeralAerogerador = null;
		quantidadePotenciaTermoeletrica = null;
		quantidadeAreaSolarFotovoltaica = null;
		
		try {
			
			Collection<Tipologia> listaTipologia = requerimentoTipologiaService.buscarTipologiasLicenca(super.requerimento, true, true);
			for (Tipologia tipologia : listaTipologia) {
				if (String.valueOf(TipologiaEnum.GERACAO_ENERGIA_ELETRICA_FONTE_EOLICA.getId()).equals(String.valueOf(tipologia.getIdeTipologia()))){
					quantidadeGeralAerogerador = Double.valueOf(tipologia.getValAtividade()).intValue();
				}
				
				if (String.valueOf(TipologiaEnum.TERMOELETRICA_GRUPO_GERADORES.getId()).equals(String.valueOf(tipologia.getIdeTipologia()))){
					quantidadePotenciaTermoeletrica = Double.valueOf(tipologia.getValAtividade());
				}
				
				if (String.valueOf(TipologiaEnum.GERACAO_ENERGIA_SOLAR_FOTOVOLTAICA.getId()).equals(String.valueOf(tipologia.getIdeTipologia()))){
					quantidadeAreaSolarFotovoltaica = Double.valueOf(tipologia.getValAtividade());
				}
			}
			
			if (!renderedEnergiaEolica){
				this.fceEnergiaEolicaLicencaPrevia.setQuantidadeGeralAerogerador(quantidadeGeralAerogerador);	
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void abrirModalGeracaoEnergia(){
		
		try {
			
			this.numeroAba = 1;
			this.activeIndex = 0;
			
			if(isFceTecnico()){
				super.fce = this.fceService.buscarFceByIdReqAndDoc(super.getRequerimento(), new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_GERACAO_ENERGIA.getId()), DadoOrigemEnum.TECNICO);
			}else{
				super.fce = this.fceService.buscarFceByIdReqAndDoc(super.getRequerimento(), new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_GERACAO_ENERGIA.getId()), DadoOrigemEnum.REQUERIMENTO);
			}
			
			salvarFCE();
			
			if(!Util.isNullOuVazio(super.fce)){
				
				this.desabilitaAbaEnergiaEolica = Boolean.TRUE;
				this.desabilitaAbaEnergiaSolar = Boolean.TRUE;
				this.desabilitaAbaTermoeletrica = Boolean.TRUE;
				this.desabilitaAbas = Boolean.FALSE;
				
				List<RequerimentoTipologia> requerimentoTipologia = (List<RequerimentoTipologia>) requerimentoTipologiaService.buscarRequerimentoTipologiaByRequerimento(super.fce.getIdeRequerimento());
				
				if(!Util.isNullOuVazio(requerimentoTipologia)){
					for(RequerimentoTipologia rt: requerimentoTipologia){
						if(rt.getIdeUnidadeMedidaTipologiaGrupo().getIdeTipologiaGrupo().getIdeTipologia().getIdeTipologia().equals(TipologiaEnum.TERMOELETRICA_GRUPO_GERADORES.getId())){
							this.desabilitaAbaTermoeletrica = Boolean.FALSE;
						}
						
						if(rt.getIdeUnidadeMedidaTipologiaGrupo().getIdeTipologiaGrupo().getIdeTipologia().getIdeTipologia().equals(TipologiaEnum.GERACAO_ENERGIA_ELETRICA_FONTE_EOLICA.getId())){
							this.desabilitaAbaEnergiaEolica = Boolean.FALSE;
						}
						
						if(rt.getIdeUnidadeMedidaTipologiaGrupo().getIdeTipologiaGrupo().getIdeTipologia().getIdeTipologia().equals(TipologiaEnum.GERACAO_ENERGIA_SOLAR_FOTOVOLTAICA.getId())){
							this.desabilitaAbaEnergiaSolar = Boolean.FALSE;
						}
					}
				}
				
				this.fceEnergia = this.fceEnergiaService.buscarByIdFce(super.getFce());
				if(!Util.isNullOuVazio(this.fceEnergia)){
					
					if(Util.isNullOuVazio(this.fceEnergia.getIdeLocalizacaoGeografica())){
						
						this.fceEnergia.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
						
					}else{
						
						this.fceEnergia.getIdeLocalizacaoGeografica().setVlrArea(BigDecimal.valueOf(this.locGeoService.retornarAreaShape(this.fceEnergia.getIdeLocalizacaoGeografica()))); 
						
					}
					
					this.fceEnergia.setListaFinalidadeGeracaoEnergia(new ArrayList<FinalidadeGeracaoEnergia>());
					this.fceEnergia.setListaFinalidadeGeracaoEnergiaSelecionado(new ArrayList<FinalidadeGeracaoEnergia>());
					List<FceEnergiaFinalidade> finalidades = this.fceEnergiaFinalidadeService.listarFinalidadesByFceEnergia(this.fceEnergia);
					
					this.listaFinalidadeGeracaoEnergiaSelecionado = new ArrayList<FinalidadeGeracaoEnergia>();
					if(!Util.isNullOuVazio(finalidades)){
						
						for(FceEnergiaFinalidade fin: finalidades){
							
							this.listaFinalidadeGeracaoEnergiaSelecionado.add(fin.getFinalidadeGeracaoEnergia());
							this.fceEnergia.getListaFinalidadeGeracaoEnergiaSelecionado().add(fin.getFinalidadeGeracaoEnergia());
							
						}
					}
					
					if(!desabilitaAbaEnergiaEolica){
						this.fceEnergiaEolica = this.fceEnergiaEolicaService.buscarFceEnergiaEolicaByFceEnergia(this.fceEnergia);
						
						if(!Util.isNullOuVazio(this.fceEnergiaEolica)){
							FceEnergiaEolicaLicencaPrevia fceEolicaLicencaPrevicaCarregado = fceEnergiaEolicaLicencaPreviaService.buscarFceEnergiaEolicaLicencaPrevia(fceEnergiaEolica.getIdeFceEnergiaEolica());
							
							if(!Util.isNullOuVazio(fceEolicaLicencaPrevicaCarregado)){
								fceEnergiaEolicaLicencaPrevia = fceEolicaLicencaPrevicaCarregado;
							}
							
							this.listaEnergiaEolicaParque = this.fceEnergiaEolicaParqueService.listarEolicaParqueByFceEnergiaEolica(this.fceEnergiaEolica);
							
							if(!Util.isNullOuVazio(this.listaEnergiaEolicaParque)){
								for(FceEnergiaEolicaParque parque: this.listaEnergiaEolicaParque){
									if(!Util.isNullOuVazio(parque.getLocalizacaoGeografica())){
										parque.getLocalizacaoGeografica().setVlrArea(new BigDecimal(this.locGeoService.retornarAreaShape(parque.getLocalizacaoGeografica())));
									}
									parque.setListaAerogerador(new ArrayList<FceEnergiaEolicaParqueAerogerador>());
									parque.setListaAerogerador(this.fceEnergiaEolicaParqueAerogeradorService.listarAerogeradorByParque(parque));
									parque.setNumeroAerogeradores(parque.getListaAerogerador().size());
								}
							}
						}else{
							
							this.iniciarFceEnergiaEolica();
							
						}
					}
					
					if(!desabilitaAbaEnergiaSolar){
					
						this.fceEnergiaSolar = this.fceEnergiaSolarService.buscarFceEnergiaSolarByFceEnergia(this.fceEnergia);
						
						if(!Util.isNullOuVazio(this.fceEnergiaSolar)){
							
							this.listaEnergiaSolarUsina = this.fceEnergiaSolarUsinaService.listarUsinaBySolar(this.fceEnergiaSolar);
							if(!Util.isNullOuVazio(this.listaEnergiaSolarUsina)){
								for(FceEnergiaSolarUsina usina: this.listaEnergiaSolarUsina){
									if(Util.isNullOuVazio(usina.getIdeLocalizacaoGeografica())){
										usina.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
									}else{
										usina.getIdeLocalizacaoGeografica().setVlrArea(new BigDecimal(this.locGeoService.retornarAreaShape(usina.getIdeLocalizacaoGeografica())));
									}
								}
							}
						}else{
							
							this.iniciarEnergiaSolar();
							this.iniciarEnergiaSolarUsina();
							
						}
						
					}
					
					if(!desabilitaAbaTermoeletrica){
						
						this.fceEnergiaTermoeletrica = this.fceEnergiaTermoeletricaService.buscarTermoeletricaByFceEnergia(this.fceEnergia);
						
						if(!Util.isNullOuVazio(this.fceEnergiaTermoeletrica)){
							
							this.listaEnergiaTermoeletricaUnidade = this.fceEnergiaTermoeletricaUnidadeService.listarUnidadeByTermoeletrica(this.fceEnergiaTermoeletrica);
							if(!Util.isNullOuVazio(this.listaEnergiaTermoeletricaUnidade)){
								for(FceEnergiaTermoeletricaUnidade termoeletrica: this.listaEnergiaTermoeletricaUnidade){
									if(!Util.isNullOuVazio(termoeletrica.getLocalizacaoGeografica())){
										termoeletrica.getLocalizacaoGeografica().setVlrArea(BigDecimal.valueOf(this.locGeoService.retornarAreaShape(termoeletrica.getLocalizacaoGeografica())));
									}
									termoeletrica.setListaGerador(this.fceEnergiaTermoeletricaUnidadeGeradorService.listarGeradorByUnidade(termoeletrica));
									termoeletrica.setListaCombustivel(this.fceEnergiaTermoeletricaUnidadeCombustivelService.listarCombustivelByIdEnergiaUnidade(termoeletrica));
								}
							}
						}else{
							
							this.iniciarEnergiaTermoeletrica();
							
						}
						
					}
					
					
				}else{
					
					this.iniciarFceEnergia();
				}
				
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}	
		
		this.carregarListas();
		
		Html.atualizar("tabAbasGeracaoEnergia:formAbaGeral:listaFinalidade");
	}
	
	public void carregarListas(){
		
		try {
		
			listarFinalidadeGeracaoEnergia();
			
			this.fceEnergiaTermoeletricaUnidade.getListaTipoCombustivel().addAll(this.tipoCombustivelService.listarTodos());
			
			this.listaAtoAmbiental = (List<AtoAmbiental>) this.atoAmbientalService.filtrarListaAtoAmbientalPorEnquadramentoRequerimento(super.requerimento.getIdeRequerimento());
			
			if(!Util.isNullOuVazio(this.listaAtoAmbiental)){
				for(AtoAmbiental ato: this.listaAtoAmbiental){
					if(ato.getIdeAtoAmbiental().equals(AtoAmbientalEnum.OUTORGA.getId())){
						this.desabilitaCaptacaoAgua = Boolean.TRUE;
						this.fceEnergia.setIndCaptacao(Boolean.TRUE);
					}
					if(ato.getIdeAtoAmbiental().equals(AtoAmbientalEnum.RLO.getId()) || ato.getIdeAtoAmbiental().equals(AtoAmbientalEnum.ASV.getId())){
						this.desabilitaASV = Boolean.TRUE;
						this.fceEnergia.setIndASV(Boolean.FALSE);
					}
					if(ato.getIdeAtoAmbiental().equals(AtoAmbientalEnum.LP.getId())){
						this.renderedEnergiaEolica = Boolean.FALSE;
					}
				}
			}
			
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
		
	}

	private void listarFinalidadeGeracaoEnergia() {
		try {
			
			this.listFinalidadeGeracaoEnergia = this.finalidadeGeracaoEnergiaService.listarFinalidades();
			
		} catch (Exception e) {

			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}

	public void avancar(){

		if(this.validaCampos()){
			
			switch (this.numeroAba) {
			
			case 1:
				
				if(!desabilitaAbaEnergiaEolica){
					this.flagFinalizar = Boolean.FALSE;
					this.activeIndex = 1;
					this.salvar();
					break;
				}else{
					if(!desabilitaAbaEnergiaSolar){
						this.flagFinalizar = Boolean.FALSE;
						this.activeIndex = 2;
						this.salvar();
						break;
					}else{
						if(!desabilitaAbaTermoeletrica){
							this.flagFinalizar = Boolean.FALSE;
							this.activeIndex = 3;
							this.salvar();
							break;
						}else{
							this.flagFinalizar = Boolean.FALSE;
							this.activeIndex = 4;
							this.salvar();
							break;
						}
					}
				}

			case 2:
				
				if(!desabilitaAbaEnergiaSolar){
					this.flagFinalizar = Boolean.FALSE;
					this.activeIndex = 2;
					this.salvar();
					break;
				}else{
					if(!desabilitaAbaTermoeletrica){
						this.flagFinalizar = Boolean.FALSE;
						this.activeIndex = 3;
						this.salvar();
						break;
					}else{
						this.flagFinalizar = Boolean.FALSE;
						this.activeIndex = 4;
						this.salvar();
						break;
					}
				}
				
			case 3:
				
				if(!desabilitaAbaTermoeletrica){
					this.flagFinalizar = Boolean.FALSE;
					this.activeIndex = 3;
					this.salvar();
					break;
				}else{
					this.flagFinalizar = Boolean.FALSE;
					this.activeIndex = 4;
					this.salvar();
					break;
				}
				
			case 4:
				this.flagFinalizar = Boolean.FALSE;
				this.activeIndex = 4;
				this.salvar();
				break;
			
			}
			
		}
		
	}
	
	public void voltar(){
		
		switch (this.numeroAba) {
		
		case 2:
			this.activeIndex = 0;
			break;

		case 3:
			if(!desabilitaAbaEnergiaEolica){
				this.activeIndex = 1;
				break;
			}else{
				this.activeIndex = 0;
				break;
			}
			
		case 4:
			if(!desabilitaAbaEnergiaSolar){
				this.activeIndex = 2;
				break;
			}else{
				if(!desabilitaAbaEnergiaEolica){
					this.activeIndex = 1;
					break;
				}else{
					this.activeIndex = 0;
					break;
				}
			}
			
		case 5:
			
			if(!desabilitaAbaTermoeletrica){
				this.activeIndex = 3;
				break;
			}else{
				if(!desabilitaAbaEnergiaSolar){
					this.activeIndex = 2;
					break;
				}else{
					if(!desabilitaAbaEnergiaEolica){
						this.activeIndex = 1;
						break;
					}else{
						this.activeIndex = 0;
						break;
					}
				}
			}
		
		}

	}
	
	private Boolean validaCampos(){
		
		Boolean retorno = Boolean.TRUE;
		
		switch (this.numeroAba) {
		
		case 1:
			
			retorno = validaCamposDadosGerais(retorno);
			
			break;

		case 2:
			
			retorno = validaCamposEnergiaEolica(retorno);
			
			break;
			
		case 3:
			
			retorno = validaCamposEnergiaSolar(retorno);
			
			break;
			
		case 4:
			
			retorno = validaCamposTermoeletrica(retorno);
			
			break;
		
		}
		
		return retorno;
	}

	private Boolean validaCamposTermoeletrica(Boolean retorno) {
		
		if(!this.desabilitaAbaTermoeletrica){
			if(Util.isNullOuVazio(this.listaEnergiaTermoeletricaUnidade)){
				JsfUtil.addErrorMessage("Campo Obrigatório. Favor informar ao menos uma Unidade Termoelétrica.");
				retorno = Boolean.FALSE;
			}
		}
		
		if(numeroTotalPotencia.doubleValue() != quantidadePotenciaTermoeletrica.doubleValue()){
			JsfUtil.addErrorMessage("A potência total declarada no FCE deve coincidir com a potência informada no requerimento.");
			retorno = Boolean.FALSE;
		}
		
		return retorno;
	}

	private Boolean validaCamposEnergiaSolar(Boolean retorno) {
		try {
			if(!this.desabilitaAbaEnergiaSolar){
				if(Util.isNullOuVazio(this.listaEnergiaSolarUsina)){
					JsfUtil.addErrorMessage("Campo Obrigatório. Favor informar ao menos uma Usina Fotovoltaica.");
					retorno = Boolean.FALSE;
				}
			}
			
			
			Double percentual = (areaTotalUsina.doubleValue() / quantidadeAreaSolarFotovoltaica)*100;
			
			//Arredonda o percentual para apenas uma casa decimal
			percentual = Util.converterDoubleUmaCasa(percentual);
			
			if(percentual < 90 || percentual > 110) {
				JsfUtil.addErrorMessage("A área do empreendimento declarada no FCE deve coincidir com a área informada no requerimento.");
				retorno = Boolean.FALSE;
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		return retorno;
	}

	private Boolean validaCamposEnergiaEolica(Boolean retorno) {
		
		if(!this.desabilitaAbaEnergiaEolica){
			
			if(this.renderedEnergiaEolica){
				if(Util.isNullOuVazio(this.listaEnergiaEolicaParque)){
					JsfUtil.addErrorMessage("Campo Obrigatório. Favor informar ao menos um parque Eólico.");
					retorno = Boolean.FALSE;
				}
			}
			
			if(!this.renderedEnergiaEolica){
				if(Util.isNullOuVazio(this.fceEnergiaEolicaLicencaPrevia.getNumParques())){
					JsfUtil.addErrorMessage("Campo Obrigatório. Favor informar o Número de parques.");
					retorno = Boolean.FALSE;
				}
				if(Util.isNullOuVazio(this.fceEnergiaEolicaLicencaPrevia.getQuantidadeGeralAerogerador())){
					JsfUtil.addErrorMessage("Campo Obrigatório. Favor informar a Quantidade geral de aerogeradores.");
					retorno = Boolean.FALSE;
				}
				if(Util.isNullOuVazio(this.fceEnergiaEolicaLicencaPrevia.getValPotenciaAerogerador())){
					JsfUtil.addErrorMessage("Campo Obrigatório. Favor informar a Potência dos aerogeradores.");
					retorno = Boolean.FALSE;
				}
			}
		}
		
		return retorno;
	}

	private Boolean validaCamposDadosGerais(Boolean retorno) {
		boolean isRLOorRLU = false;
		if(Util.isNullOuVazio(this.fceEnergia.getDesIdentificacaoEmpreendimento())){
			JsfUtil.addErrorMessage("Campo Obrigatório. Favor informar a identificação do empreendimento.");
			retorno = Boolean.FALSE;
		}
		
		if(Util.isNullOuVazio(this.fceEnergia.getIdeLocalizacaoGeografica())){
			JsfUtil.addErrorMessage("Campo Obrigatório. Favor informar o poligonal do objeto de licença.");
			retorno = Boolean.FALSE;
		}
		
		if(!this.desabilitaCaptacaoAgua){
			if(!Util.isNullOuVazio(this.listaAtoAmbiental)){
				for(AtoAmbiental ato: this.listaAtoAmbiental){
					if(ato.getIdeAtoAmbiental().equals(AtoAmbientalEnum.RLO.getId()) || 
							ato.getIdeAtoAmbiental().equals(AtoAmbientalEnum.RLU.getId())){
						isRLOorRLU = true;
						if(this.fceEnergia.getIndCaptacao()){
							if(Util.isNullOuVazio(this.fceEnergia.getNumProcessoOutorga())){
								JsfUtil.addErrorMessage("Campo Obrigatório. Favor informar o Número do Processo de Outorga.");
								retorno = Boolean.FALSE;
							}
						}else{
							if(Util.isNullOuVazio(fceEnergia.getIndCaptacao())){
								JsfUtil.addErrorMessage("Campo Obrigatório. Favor informar se o empreendimento faz captação de água.");
							}
						}
					}
				}
			}
		}
		
		if(this.fceEnergia.getIndASV()){
			if(Util.isNullOuVazio(this.fceEnergia.getNumProcessoASV())){
				JsfUtil.addErrorMessage("Campo Obrigatório. Favor informar o Número do Processo de ASV.");
				retorno = Boolean.FALSE;
			}
			
			if(Util.isNullOuVazio(this.listaFinalidadeGeracaoEnergiaSelecionado)) {
				JsfUtil.addErrorMessage("Campo Obrigatório. Informar as finalidades quando a pergunta 'Existe processo de Autorização de Supressão de Vegetação (ASV)?' está marcado como SIM.");
				retorno = Boolean.FALSE;
			}
			
		}
		
		if(fceEnergia.getIndCaptacao()){
			if(Util.isNullOuVazio(fceEnergia.getNumProcessoOutorga()) &&  !isRLOorRLU){
				JsfUtil.addErrorMessage("Campo Obrigatório. Informar número do processo de Outorga 'O empreendimento faz captação de água?' está marcado como SIM.");
				retorno = Boolean.FALSE;
			}
		}
	
		return retorno;
	}

	private Boolean validaCamposFinalizar(Boolean retorno){
		
		return this.validaCamposDadosGerais(retorno) && validaCamposEnergiaEolica(retorno) 
				&& validaCamposEnergiaSolar(retorno) && validaCamposTermoeletrica(retorno);
	}
	
	public void salvar(){
		
		if(this.validaCampos()){
			try {
				
				switch (this.numeroAba) {
				
					case 1:
						
						this.flagFinalizar = Boolean.FALSE;
						salvarDadosGerais();
						
						break;
						
					case 2:
						
						this.flagFinalizar = Boolean.FALSE;
						salvarEnergiaEolica();
						
						break;
						
					case 3:
						
						this.flagFinalizar = Boolean.FALSE;
						salvarEnergiaSolar();
												
						break;
						
					case 4:
						
						this.flagFinalizar = Boolean.FALSE;
						salvarTermoeletrica();
						
						break;
					
				}
				
			} catch (Exception e) {

				JsfUtil.addErrorMessage("Erro ao salvar.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
				
			}
		}
		
	}

	private void salvarTermoeletrica() throws Exception {
		if(!Util.isNullOuVazio(this.fceEnergia.getIdeFceEnergia())){
			
			if(!desabilitaAbaTermoeletrica){
				
				this.fceEnergiaTermoeletrica = this.fceEnergiaTermoeletricaService.buscarTermoeletricaByFceEnergia(this.fceEnergia);
				
				if(Util.isNullOuVazio(this.fceEnergiaTermoeletrica)){
					this.fceEnergiaTermoeletrica = new FceEnergiaTermoEletrica();
					this.fceEnergiaTermoeletrica.setIdeFceEnergia(new FceEnergia());
				}
				
				this.fceEnergiaTermoeletrica.setIdeFceEnergia(this.fceEnergia);
				
				if(Util.isNullOuVazio(this.fceEnergiaTermoeletrica.getIdeFceEnergiaTermoEletrica())){
					this.fceEnergiaTermoeletricaService.salvar(this.fceEnergiaTermoeletrica);
				}
				
				if(!Util.isNullOuVazio(this.listaEnergiaTermoeletricaUnidade)){
					for(FceEnergiaTermoeletricaUnidade unidade: this.listaEnergiaTermoeletricaUnidade){
						unidade.setFceEnergiaTermoeletrica(this.fceEnergiaTermoeletrica);
						if(!Util.isNullOuVazio(unidade.getLocalizacaoGeografica())){
							String theGeom = validacaoGeoSeiaService.buscarGeometriaShape(unidade.getLocalizacaoGeografica().getIdeLocalizacaoGeografica());
							unidade.setValAreaUnidade((new BigDecimal(validacaoGeoSeiaService.retonarAreaDoShapeByGeometria(theGeom).doubleValue())));
						}
						
						this.fceEnergiaTermoeletricaUnidadeService.salvar(unidade);
						if(!Util.isNullOuVazio(unidade.getListaGerador())){
							for(FceEnergiaTermoeletricaUnidadeGerador gerador: unidade.getListaGerador()){
								gerador.setFceEnergiaTermoeletricaUnidade(unidade);
								this.fceEnergiaTermoeletricaUnidadeGeradorService.salvar(gerador);
							}
						}
						if(!Util.isNullOuVazio(unidade.getListaCombustivel())){
							this.fceEnergiaTermoeletricaUnidadeCombustivelService.removerByUnidade(unidade);
							for(FceEnergiaTermoeletricaUnidadeCombustivel combustivel: unidade.getListaCombustivel()){
								combustivel.setFceEnergiaTermoeletricaUnidade(unidade);
								combustivel.setIdeFceEnergiaTermoeletricaUnidadeCombustivel(null);
								this.fceEnergiaTermoeletricaUnidadeCombustivelService.salvar(combustivel);
							}
						}
					}
				}
				
				if(!this.flagFinalizar && !prepararDublicacao){
					JsfUtil.addSuccessMessage("Etapa de Energia Termoelétrica / Grupo Geradores cadastrada com sucesso.");
				}	
			}
			
		}else{
			
			JsfUtil.addErrorMessage("Etapa de Energia Termoelétrica/Grupo Geradores não pode ser cadastrada.Favor cadastrar a Etapa Dados Gerais antes.");
			this.activeIndex = 0;
		}
	}

	private void salvarEnergiaSolar() throws Exception {
		if(!Util.isNullOuVazio(this.fceEnergia.getIdeFceEnergia())){
			
			if(!desabilitaAbaEnergiaSolar){
				
				this.fceEnergiaSolar.setIdeFceEnergia(this.fceEnergia);
				this.fceEnergiaSolarService.salvar(this.fceEnergiaSolar);
				
				if(!Util.isNullOuVazio(this.listaEnergiaSolarUsina)){
					for(FceEnergiaSolarUsina usina: this.listaEnergiaSolarUsina){
						if(!Util.isNullOuVazio(usina.getIdeLocalizacaoGeografica())){
							String theGeom = validacaoGeoSeiaService.buscarGeometriaShape(usina.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
							usina.setValorAreaUsina((BigDecimal.valueOf(validacaoGeoSeiaService.retonarAreaDoShapeByGeometria(theGeom).doubleValue())));
						}
						usina.setIdeFceEnergiaSolar(this.fceEnergiaSolar);
						this.fceEnergiaSolarUsinaService.salvar(usina);
					}
				}
				
				if(!this.flagFinalizar  && !prepararDublicacao){
					JsfUtil.addSuccessMessage("Etapa de Energia Solar cadastrada com sucesso.");
				}
				
				
			}
			
		}else{
			
			JsfUtil.addErrorMessage("Etapa de Energia Solar não pode ser cadastrada.Favor cadastrar a Etapa Dados Gerais antes.");
			this.activeIndex = 0;
		}
	}

	private void salvarEnergiaEolica() throws Exception {
		if(!Util.isNullOuVazio(this.fceEnergia.getIdeFceEnergia())){
			
			if(renderedEnergiaEolica){
				if (validarEnergiaEolica()){
					this.fceEnergiaEolica.setIdeFceEnergia(this.fceEnergia);
					this.fceEnergiaEolica.setIndLicencaPrevia(Boolean.FALSE); // Verificar valor a ser inserido
					this.fceEnergiaEolicaService.salvar(this.fceEnergiaEolica);
					
					if(!Util.isNullOuVazio(this.listaEnergiaEolicaParque)){
						for(FceEnergiaEolicaParque parque: this.listaEnergiaEolicaParque){
							
							if(!Util.isNullOuVazio(parque.getLocalizacaoGeografica())){
								String theGeom = validacaoGeoSeiaService.buscarGeometriaShape(parque.getLocalizacaoGeografica().getIdeLocalizacaoGeografica());
								parque.setValorAreaParque((new BigDecimal(validacaoGeoSeiaService.retonarAreaDoShapeByGeometria(theGeom).doubleValue())));
							}
							parque.setFceEnergiaEolica(this.fceEnergiaEolica);
							this.fceEnergiaEolicaParqueService.salvar(parque);
							
							if(!Util.isNullOuVazio(parque.getListaAerogerador())){
	
								for(FceEnergiaEolicaParqueAerogerador eolicaAerogerador: parque.getListaAerogerador()){
									eolicaAerogerador.setFceEnergiaEolicaParque(parque);
									this.fceEnergiaEolicaParqueAerogeradorService.salvar(eolicaAerogerador);
								}
							}
						}
					}
					
					if (!Util.isNullOuVazio(listaFceEnergiaEolicaParqueAuxExcluido)){
						for (FceEnergiaEolicaParque fceEnergiaEolicaParque : listaFceEnergiaEolicaParqueAuxExcluido) {
							if (!Util.isNullOuVazio(fceEnergiaEolicaParque.getIdeFceEnergiaEolicaParque())){
								this.fceEnergiaEolicaParqueService.excluir(fceEnergiaEolicaParque);
							}
						}
					}
					
					if(!this.flagFinalizar  && !prepararDublicacao){
						JsfUtil.addSuccessMessage("Etapa de Energia Eólica cadastrada com sucesso.");
					}
				}
				
			}else{
				this.fceEnergiaEolica.setIdeFceEnergia(this.fceEnergia);
				this.fceEnergiaEolica.setIndLicencaPrevia(Boolean.TRUE);
				this.fceEnergiaEolicaService.salvar(this.fceEnergiaEolica);
				this.fceEnergiaEolicaLicencaPrevia.setFceEnergiaEolica(this.fceEnergiaEolica);
				this.fceEnergiaEolicaLicencaPreviaService.salvar(this.fceEnergiaEolicaLicencaPrevia);
				
				if(!this.flagFinalizar){
					JsfUtil.addSuccessMessage("Etapa de Energia Eólica cadastrada com sucesso.");
				}
				
			}
		}else{
			
			JsfUtil.addErrorMessage("Etapa de Energia Eólica não pode ser cadastrada.Favor cadastrar a Etapa Dados Gerais antes.");
			this.activeIndex = 0;
		}
	}

	private void salvarDadosGerais() throws Exception {
		
		this.fceEnergia.setIdeFce(super.fce);
	
		if(!Util.isNullOuVazio(this.fceEnergia.getIdeLocalizacaoGeografica())){
			String theGeom = validacaoGeoSeiaService.buscarGeometriaShape(this.fceEnergia.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			this.fceEnergia.setValorArea((BigDecimal.valueOf(validacaoGeoSeiaService.retonarAreaDoShapeByGeometria(theGeom).doubleValue())));
		}
		
		if(!Util.isNullOuVazio(this.listaFinalidadeGeracaoEnergiaSelecionado)){
			this.fceEnergia.setListaFinalidadeGeracaoEnergia(new ArrayList<FinalidadeGeracaoEnergia>());
			this.fceEnergia.getListaFinalidadeGeracaoEnergia().addAll(this.listaFinalidadeGeracaoEnergiaSelecionado);
		}else{
			this.fceEnergia.setListaFinalidadeGeracaoEnergia(new ArrayList<FinalidadeGeracaoEnergia>());
		}
				
		this.fceEnergiaService.salvar(this.fceEnergia);
		
		this.fceEnergiaEolica.setIdeFceEnergia(this.fceEnergia);
		
		if(!this.flagFinalizar && !prepararDublicacao){
			JsfUtil.addSuccessMessage("Etapa de Dados Gerais cadastrada com sucesso.");
		}
		
		this.desabilitaAbas = Boolean.FALSE;
	}

	private void salvarFCE() throws Exception {
		
		if(Util.isNullOuVazio(super.fce)){
			
			super.iniciarFce(DOC_OBRIGATORIO_GERACAO_ENERGIA);
			
			super.fce.getIdeRequerimento().setIdeRequerimento(super.requerimento.getIdeRequerimento());
			
			salvarFce();
		}
		
	}
	
	public Boolean validarEnergiaEolica() {
		Boolean validacao = Boolean.TRUE;
		
		if (preValidarEnergiaEolica()){
			JsfUtil.addErrorMessage("O número de aerogeradores declarados no FCE deve coincidir com o número informado no requerimento.");
			validacao = Boolean.FALSE;
			this.activeIndex = 1;
		}
		
		return validacao;
	}
	
	public Boolean preValidarEnergiaEolica() {
		getNumeroTotalAerogerador();
		return (quantidadeGeralAerogerador.intValue() != numeroTotalAerogerador && renderedEnergiaEolica);
	}
	
	public void validaCheckOutros(){
        
		if(!Util.isNullOuVazio(this.listaFinalidadeGeracaoEnergiaSelecionado)){
			for(FinalidadeGeracaoEnergia selecionado: this.listaFinalidadeGeracaoEnergiaSelecionado){
				if(selecionado.getIdeFinalidadeGeracaoEnergia().equals(5)){
					super.exibirInformacao001();
				}
			}
			this.fceEnergia.setListaFinalidadeGeracaoEnergiaSelecionado(new ArrayList<FinalidadeGeracaoEnergia>());
			this.fceEnergia.getListaFinalidadeGeracaoEnergiaSelecionado().addAll(this.listaFinalidadeGeracaoEnergiaSelecionado);
		}
    }
	
	public void validaCheckCombustivelOutros(){
        
		if(!Util.isNullOuVazio(this.listaTipoCombustivelSelecionado)){
			for(TipoCombustivel selecionado: this.listaTipoCombustivelSelecionado){
				if(selecionado.getIdeTipoCombustivel().equals(4)){
					super.exibirInformacao001();
				}
			}
		}
    }

	public void visualizarParque(){
		this.visualizarParque = Boolean.TRUE;
		this.alterarParque = Boolean.FALSE;
		this.fceEnergiaEolicaParque = this.fceEnergiaEolicaParqueAux;
		this.listaEnergiaEolicaParqueAerogerador = this.fceEnergiaEolicaParque.getListaAerogerador();
		this.fceEnergiaEolicaParqueAux = new FceEnergiaEolicaParque();
		
		calcularQuantidadeEPotencia();
	}
	
	public void visualizarUsina(){
		this.visualizarUsina = Boolean.TRUE;
		this.alterarUsina = Boolean.FALSE;
		this.fceEnergiaSolarUsina = this.fceEnergiaSolarUsinaAux;
		this.fceEnergiaSolarUsinaAux = new FceEnergiaSolarUsina();
	}
	
	public void visualizarUnidadeTermo(){
		this.visualizarUnidadeTermo = Boolean.TRUE;
		this.alterarUnidadeTermo = Boolean.FALSE;
		this.fceEnergiaTermoeletricaUnidade = this.fceEnergiaTermoeletricaUnidadeAux;
		List<FceEnergiaTermoeletricaUnidadeCombustivel> listaCombustivelAux = new ArrayList<FceEnergiaTermoeletricaUnidadeCombustivel>();
		try {
			listaCombustivelAux = this.fceEnergiaTermoeletricaUnidadeCombustivelService.listarCombustivelByIdEnergiaUnidade(this.fceEnergiaTermoeletricaUnidade);
			this.fceEnergiaTermoeletricaUnidade.setListaTipoCombustivel(this.tipoCombustivelService.listarTodos());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		if(!Util.isNullOuVazio(listaCombustivelAux)){
			this.listaTipoCombustivelSelecionado = new ArrayList<TipoCombustivel>();
			for(FceEnergiaTermoeletricaUnidadeCombustivel combustivel: listaCombustivelAux){
				this.listaTipoCombustivelSelecionado.add(combustivel.getTipoCombustivel());
			}
		}
		
		this.fceEnergiaTermoeletricaUnidadeAux = new FceEnergiaTermoeletricaUnidade();
		
		Html.atualizar("formUnidadeTermoeletrica:listaTipoCombustivel");
		
	}
	
	public void configurarEdicaoParque(){
		
		this.visualizarParque = Boolean.FALSE;
		this.alterarParque = Boolean.TRUE;
		this.fceEnergiaEolicaParque = this.fceEnergiaEolicaParqueAux;
		this.listaEnergiaEolicaParqueAerogerador = new ArrayList<FceEnergiaEolicaParqueAerogerador>();
		this.listaEnergiaEolicaParqueAerogerador = this.fceEnergiaEolicaParqueAux.getListaAerogerador();
		this.fceEnergiaEolicaParqueAux = new FceEnergiaEolicaParque();
		
		calcularQuantidadeEPotencia();
		
	}

	private void calcularQuantidadeEPotencia() {
		this.totalAerogeradores = 0;
		this.totalPotenciaAerogeradores = new BigDecimal(0);
		for(FceEnergiaEolicaParqueAerogerador item : listaEnergiaEolicaParqueAerogerador){
			item.setTotalPotencia(item.getValorPotenciaAerogerador().multiply(new BigDecimal(item.getQuantidadeAerogeradores())));
			totalAerogeradores += item.getQuantidadeAerogeradores();
			totalPotenciaAerogeradores = totalPotenciaAerogeradores.add(item.getTotalPotencia());
		}
	}
	
	public void configurarEdicaoAerogerador(){
		
		this.alterarAerogerador = Boolean.TRUE;
		this.fceEnergiaEolicaParque.setListaAerogerador(new ArrayList<FceEnergiaEolicaParqueAerogerador>());
		this.fceEnergiaEolicaParque.getListaAerogerador().addAll(this.listaEnergiaEolicaParqueAerogerador);
		this.fceEnergiaEolicaParqueAerogerador = this.fceEnergiaEolicaParqueAerogeradorAux;
		this.fceEnergiaEolicaParqueAerogeradorAux = new FceEnergiaEolicaParqueAerogerador();
		
	}
	
	public void configurarEdicaoGerador(){

		this.alterarGerador = Boolean.TRUE;
		this.fceEnergiaTermoeletricaUnidade.getListaGerador().remove(this.fceEnergiaTermoeletricaUnidadeGeradorAux);
		this.fceEnergiaTermoeletricaUnidadeGerador = this.fceEnergiaTermoeletricaUnidadeGeradorAux;
		this.fceEnergiaTermoeletricaUnidadeGeradorAux = new FceEnergiaTermoeletricaUnidadeGerador();
		
	}
	
	public void configurarEdicaoUsina(){
		this.visualizarUsina = Boolean.FALSE;
		this.alterarUsina = Boolean.TRUE;
		this.fceEnergiaSolarUsina = this.fceEnergiaSolarUsinaAux;
	}
	
	public void configurarEdicaoUnidadeTermo(){
		
		this.visualizarUnidadeTermo = Boolean.FALSE;
		this.alterarUnidadeTermo = Boolean.TRUE;
		
		this.fceEnergiaTermoeletricaUnidade = this.fceEnergiaTermoeletricaUnidadeAux;
		if(Util.isNullOuVazio(this.fceEnergiaTermoeletricaUnidade.getLocalizacaoGeografica())){
			this.fceEnergiaTermoeletricaUnidade.setLocalizacaoGeografica(new LocalizacaoGeografica());
		}
		
		List<FceEnergiaTermoeletricaUnidadeCombustivel> listaCombustivelAux = new ArrayList<FceEnergiaTermoeletricaUnidadeCombustivel>();
		try {
			listaCombustivelAux = this.fceEnergiaTermoeletricaUnidadeCombustivelService.listarCombustivelByIdEnergiaUnidade(this.fceEnergiaTermoeletricaUnidade);
			this.fceEnergiaTermoeletricaUnidade.setListaTipoCombustivel(this.tipoCombustivelService.listarTodos());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
		if(!Util.isNullOuVazio(listaCombustivelAux)){
			this.listaTipoCombustivelSelecionado = new ArrayList<TipoCombustivel>();
			for(FceEnergiaTermoeletricaUnidadeCombustivel combustivel: listaCombustivelAux){
				this.listaTipoCombustivelSelecionado.add(combustivel.getTipoCombustivel());
			}
		}
		
		this.fceEnergiaTermoeletricaUnidadeAux = new FceEnergiaTermoeletricaUnidade();
		
		Html.atualizar("formUnidadeTermoeletrica:listaTipoCombustivel");
		
	}

	public void editarUnidade(){
		try {
			if(validaCamposUnidade()){
				boolean podeAdicionar = false;
				this.listaEnergiaTermoeletricaUnidade.remove(this.fceEnergiaTermoeletricaUnidadeAux);
				this.fceEnergiaTermoeletricaUnidadeAux = new FceEnergiaTermoeletricaUnidade();
				
				if(!Util.isNullOuVazio(this.listaTipoCombustivelSelecionado)){
					this.fceEnergiaTermoeletricaUnidade.setListaCombustivel(new ArrayList<FceEnergiaTermoeletricaUnidadeCombustivel>());
					for(TipoCombustivel tipo: this.listaTipoCombustivelSelecionado){
						FceEnergiaTermoeletricaUnidadeCombustivel combustivel = new FceEnergiaTermoeletricaUnidadeCombustivel();
						combustivel.setTipoCombustivel(tipo);
						this.fceEnergiaTermoeletricaUnidade.getListaCombustivel().add(combustivel);
					}
				}
				
				if(!Util.isNullOuVazio(fceEnergiaTermoeletricaUnidade)){
					
					if(!Util.isNullOuVazio(fceEnergiaTermoeletricaUnidade.getIdeFceEnergiaTermoeletricaUnidade())){
						this.fceEnergiaTermoeletricaUnidadeCombustivelService.removerByUnidade(fceEnergiaTermoeletricaUnidade);
						for(FceEnergiaTermoeletricaUnidadeCombustivel combustivel: fceEnergiaTermoeletricaUnidade.getListaCombustivel()){
							combustivel.setFceEnergiaTermoeletricaUnidade(fceEnergiaTermoeletricaUnidade);
							this.fceEnergiaTermoeletricaUnidadeCombustivelService.salvar(combustivel);
						}
					}
				}
				
				if(!Util.isNull(listaEnergiaTermoeletricaUnidade)){
					
					if(listaEnergiaTermoeletricaUnidade.size() >=1){
						for(FceEnergiaTermoeletricaUnidade unidade : listaEnergiaTermoeletricaUnidade){
							String geometriaA = validacaoGeoSeiaService.buscarGeometriaShape(fceEnergiaTermoeletricaUnidade.getLocalizacaoGeografica().getIdeLocalizacaoGeografica());
							String geometriaB = validacaoGeoSeiaService.buscarGeometriaShape(unidade.getLocalizacaoGeografica().getIdeLocalizacaoGeografica());
							
							if(!validacaoGeoSeiaService.validaPercentualSobreposicao(geometriaA, geometriaB)){
								podeAdicionar = true;
							}else{
								JsfUtil.addErrorMessage("O shape informado sobrepõe uma locação cadastrada. Verifique o shapefile informado e tente novamente.");
							}
						}
						
						if(podeAdicionar){
							this.listaEnergiaTermoeletricaUnidade.add(this.fceEnergiaTermoeletricaUnidade);
						}
						
					}else{
						this.listaEnergiaTermoeletricaUnidade.add(this.fceEnergiaTermoeletricaUnidade);
					}
				}
				
				Html.atualizar("tabAbasGeracaoEnergia:formAbaEnergiaTermoeletrica:listaEnergiaTermoeletrica");
				RequestContext.getCurrentInstance().execute("incluirUnidadeTermoeletrica.hide();");
				this.alterarUnidadeTermo = Boolean.FALSE;
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public void adicionarAerogerador(){
		
		if(validaCamposAerogerador()){
			this.fceEnergiaEolicaParqueAerogerador.setFceEnergiaEolicaParque(this.fceEnergiaEolicaParque);
			somarDadosGeradores();
			this.listaEnergiaEolicaParqueAerogerador.add(this.fceEnergiaEolicaParqueAerogerador);
			this.fceEnergiaEolicaParque.setListaAerogerador(new ArrayList<FceEnergiaEolicaParqueAerogerador>());
			this.fceEnergiaEolicaParque.getListaAerogerador().addAll(this.listaEnergiaEolicaParqueAerogerador);
			Html.atualizar("formIncParqueEolico:listaAerogeradorEolico");
			RequestContext.getCurrentInstance().execute("incluirAerogerador.hide();");
		}
		
	}

	private void somarDadosGeradores() {
		this.fceEnergiaEolicaParqueAerogerador.setTotalPotencia(fceEnergiaEolicaParqueAerogerador.getValorPotenciaAerogerador().multiply(new BigDecimal(fceEnergiaEolicaParqueAerogerador.getQuantidadeAerogeradores())));
		this.totalAerogeradores += fceEnergiaEolicaParqueAerogerador.getQuantidadeAerogeradores();
		this.totalPotenciaAerogeradores = totalPotenciaAerogeradores.add(fceEnergiaEolicaParqueAerogerador.getTotalPotencia());
	}
	
	public void editarAerogerador(){
		
		Integer index = listaEnergiaEolicaParqueAerogerador.indexOf(this.fceEnergiaEolicaParqueAerogerador);
		listaEnergiaEolicaParqueAerogerador.set(index, this.fceEnergiaEolicaParqueAerogerador);
		this.fceEnergiaEolicaParque.setListaAerogerador(new ArrayList<FceEnergiaEolicaParqueAerogerador>());
		this.fceEnergiaEolicaParque.getListaAerogerador().addAll(this.listaEnergiaEolicaParqueAerogerador);
		calcularQuantidadeEPotencia();
		Html.atualizar("formIncParqueEolico:listaAerogeradorEolico");
		RequestContext.getCurrentInstance().execute("incluirAerogerador.hide();");
		this.alterarAerogerador = Boolean.FALSE;
		
	}
	
	public void removerAerogerador(){
		try {
			if(!Util.isNullOuVazio(this.fceEnergiaEolicaParqueAerogeradorAux)){
				if(!Util.isNullOuVazio(fceEnergiaEolicaParqueAerogeradorAux.getIdeFceEnergiaEolicaParqueAerogerador())){
					fceEnergiaEolicaParqueAerogeradorService.excluir(fceEnergiaEolicaParqueAerogeradorAux);
				}
				this.listaEnergiaEolicaParqueAerogerador.remove(this.fceEnergiaEolicaParqueAerogeradorAux);
				this.fceEnergiaEolicaParque.getListaAerogerador().remove(this.fceEnergiaEolicaParqueAerogeradorAux);
				calcularQuantidadeEPotencia();
				Html.atualizar("formIncParqueEolico:listaAerogeradorEolico");
				this.fceEnergiaEolicaParqueAerogeradorAux = new FceEnergiaEolicaParqueAerogerador();
				JsfUtil.addSuccessMessage("Aerogerador removido com sucesso.");
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	public void editarParqueEolico(){
		try {
			this.fceEnergiaEolicaParque.setNumeroAerogeradores(this.fceEnergiaEolicaParque.getListaAerogerador().size());
			Integer index = listaEnergiaEolicaParque.indexOf(this.fceEnergiaEolicaParque);
			
			boolean podeEditar = false;
			
			if(!Util.isNull(listaEnergiaEolicaParque)){
				
				if(listaEnergiaEolicaParque.size() > 1){
					for(FceEnergiaEolicaParque eolica : listaEnergiaEolicaParque){
						String geometriaA = validacaoGeoSeiaService.buscarGeometriaShape(fceEnergiaEolicaParque.getLocalizacaoGeografica().getIdeLocalizacaoGeografica());
						String geometriaB = validacaoGeoSeiaService.buscarGeometriaShape(eolica.getLocalizacaoGeografica().getIdeLocalizacaoGeografica());
						
						if(!eolica.equals(fceEnergiaEolicaParque)){
							if(!validacaoGeoSeiaService.validaPercentualSobreposicao(geometriaA, geometriaB)){
								podeEditar = true;
							}else{
								JsfUtil.addErrorMessage("O shape informado sobrepõe uma locação cadastrada. Verifique o shapefile informado e tente novamente.");
							}
						}
					}
					if(podeEditar){
						listaEnergiaEolicaParque.set(index, this.fceEnergiaEolicaParque);
						calcularQuantidadeEPotencia();
						Html.atualizar("formAbaEnergiaEolica:listaEnergiaEolicaParque");
						Html.atualizar("tabAbasGeracaoEnergia:formAbaEnergiaEolica:listaEnergiaEolicaParque");
						RequestContext.getCurrentInstance().execute("incluirParqueEolico.hide();");
						this.alterarParque = Boolean.FALSE;
					}
				}else{
					listaEnergiaEolicaParque.set(index, this.fceEnergiaEolicaParque);	
					calcularQuantidadeEPotencia();
					Html.atualizar("formAbaEnergiaEolica:listaEnergiaEolicaParque");
					Html.atualizar("tabAbasGeracaoEnergia:formAbaEnergiaEolica:listaEnergiaEolicaParque");
					RequestContext.getCurrentInstance().execute("incluirParqueEolico.hide();");
					this.alterarParque = Boolean.FALSE;
				}
			}
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public void adicionarParqueEolico() throws Exception{
		
		this.visualizarParque = Boolean.FALSE;
		this.totalAerogeradores = 0;
		this.totalPotenciaAerogeradores = new BigDecimal(0);
		
		boolean podeAdicionar = false;
		
		if(validaCamposParqueEolico()){
			this.fceEnergiaEolicaParque.setNumeroAerogeradores(this.fceEnergiaEolicaParque.getListaAerogerador().size());
			if(!Util.isNullOuVazio(this.fceEnergiaEolicaParque.getLocalizacaoGeografica())){
				String theGeom = validacaoGeoSeiaService.buscarGeometriaShape(this.fceEnergiaEolicaParque.getLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				this.fceEnergiaEolicaParque.setValorAreaParque((new BigDecimal(validacaoGeoSeiaService.retonarAreaDoShapeByGeometria(theGeom).doubleValue())));
			}
			
			if(!Util.isNull(listaEnergiaEolicaParque)){
				
				if(listaEnergiaEolicaParque.size() >=1){
					for(FceEnergiaEolicaParque eolica : listaEnergiaEolicaParque){
						String geometriaA = validacaoGeoSeiaService.buscarGeometriaShape(fceEnergiaEolicaParque.getLocalizacaoGeografica().getIdeLocalizacaoGeografica());
						String geometriaB = validacaoGeoSeiaService.buscarGeometriaShape(eolica.getLocalizacaoGeografica().getIdeLocalizacaoGeografica());
						
						if(!validacaoGeoSeiaService.validaPercentualSobreposicao(geometriaA, geometriaB)){
							podeAdicionar = true;
						}else{
							JsfUtil.addErrorMessage("O shape informado sobrepõe uma locação cadastrada. Verifique o shapefile informado e tente novamente.");
						}
					}
					if(podeAdicionar){
						listaEnergiaEolicaParque.add(fceEnergiaEolicaParque);
						Html.atualizar("tabAbasGeracaoEnergia:formAbaEnergiaEolica:listaEnergiaEolicaParque");
						RequestContext.getCurrentInstance().execute("incluirParqueEolico.hide();");
					}
					
				}else{
					this.listaEnergiaEolicaParque.add(this.fceEnergiaEolicaParque);
					Html.atualizar("tabAbasGeracaoEnergia:formAbaEnergiaEolica:listaEnergiaEolicaParque");
					RequestContext.getCurrentInstance().execute("incluirParqueEolico.hide();");
				}
			}
			
		}
		
	}
	
	public void excluirPoligonalParque(){
		fceEnergiaEolicaParque.setLocalizacaoGeografica(new LocalizacaoGeografica());
	}
	
	public void excluirPoligonalDadosGerais(){
		fceEnergia.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
	}
	
	public void excluirPoligonalUsinaFotoVoltaica(){
		locGeoAux = fceEnergiaSolarUsina.getIdeLocalizacaoGeografica();
		fceEnergiaSolarUsina.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
	}
	
	public void excluirPoligonalUnidade(){
		fceEnergiaTermoeletricaUnidade.setLocalizacaoGeografica(new LocalizacaoGeografica());
	}
	
	
	public void removerParqueEolico(){
		if(!Util.isNullOuVazio(this.fceEnergiaEolicaParqueAux)){
			try {
				if (Util.isNullOuVazio(listaFceEnergiaEolicaParqueAuxExcluido)) {
					listaFceEnergiaEolicaParqueAuxExcluido = new ArrayList<FceEnergiaEolicaParque>();
				}
				
				listaFceEnergiaEolicaParqueAuxExcluido.add(this.fceEnergiaEolicaParqueAux);
				this.listaEnergiaEolicaParque.remove(this.fceEnergiaEolicaParqueAux);
				Html.atualizar("tabAbasGeracaoEnergia:formAbaEnergiaEolica:listaEnergiaEolicaParque");
				this.fceEnergiaEolicaParqueAerogeradorAux = new FceEnergiaEolicaParqueAerogerador();
				JsfUtil.addSuccessMessage("Parque Eólico removido com sucesso.");
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
		}
	}
	
	
	public void adicionarUsina() throws Exception{
		this.visualizarUsina = Boolean.FALSE;
		if(validaCamposUsina()){
			
			if(!Util.isNullOuVazio(this.fceEnergiaSolarUsina.getIdeLocalizacaoGeografica())){
				String theGeom = validacaoGeoSeiaService.buscarGeometriaShape(this.fceEnergiaSolarUsina.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				this.fceEnergiaSolarUsina.setValorAreaUsina((new BigDecimal(validacaoGeoSeiaService.retonarAreaDoShapeByGeometria(theGeom).doubleValue())));
				this.fceEnergiaSolarUsina.getIdeLocalizacaoGeografica().setVlrArea(this.fceEnergiaSolarUsina.getValorAreaUsina());
			}
			
			if(!Util.isNull(listaEnergiaSolarUsina)){
				
				boolean podeAdicionar = false;
				
				if(listaEnergiaSolarUsina.size() >=1){
					for(FceEnergiaSolarUsina solar : listaEnergiaSolarUsina){
						String geometriaA = validacaoGeoSeiaService.buscarGeometriaShape(fceEnergiaSolarUsina.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
						String geometriaB = validacaoGeoSeiaService.buscarGeometriaShape(solar.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
						
						if(!validacaoGeoSeiaService.validaPercentualSobreposicao(geometriaA, geometriaB)){
							podeAdicionar = true;
						}else{
							JsfUtil.addErrorMessage("O shape informado sobrepõe uma locação cadastrada. Verifique o shapefile informado e tente novamente.");
						}
					}
					if(podeAdicionar){
						listaEnergiaSolarUsina.add(fceEnergiaSolarUsina);
						Html.atualizar("tabAbasGeracaoEnergia:formAbaEnergiaSolar:listaEnergiaSolarUsina");
						RequestContext.getCurrentInstance().execute("incluirFotovoltaica.hide();");
					}
				}else{
					this.listaEnergiaSolarUsina.add(this.fceEnergiaSolarUsina);
					Html.atualizar("tabAbasGeracaoEnergia:formAbaEnergiaSolar:listaEnergiaSolarUsina");
					RequestContext.getCurrentInstance().execute("incluirFotovoltaica.hide();");
				}
			}

		}
	}
	
	public void editarUsina() throws Exception{
		
		if(validaCamposUsina()){
			boolean podeAdicionar = false;
			this.listaEnergiaSolarUsina.remove(this.fceEnergiaSolarUsinaAux);
			
			if(!Util.isNullOuVazio(this.fceEnergiaSolarUsina.getIdeLocalizacaoGeografica())){
				String theGeom = validacaoGeoSeiaService.buscarGeometriaShape(this.fceEnergiaSolarUsina.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				this.fceEnergiaSolarUsina.getIdeLocalizacaoGeografica().setVlrArea((new BigDecimal(validacaoGeoSeiaService.retonarAreaDoShapeByGeometria(theGeom).doubleValue())));
			}
			
			this.fceEnergiaSolarUsina.setValorAreaUsina(this.fceEnergiaSolarUsina.getIdeLocalizacaoGeografica().getVlrArea());
			
			
			if(!Util.isNull(listaEnergiaSolarUsina)){
				
				if(listaEnergiaSolarUsina.size() >=1){
					
					for(FceEnergiaSolarUsina solar : listaEnergiaSolarUsina){
						String geometriaA = validacaoGeoSeiaService.buscarGeometriaShape(fceEnergiaSolarUsina.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
						String geometriaB = validacaoGeoSeiaService.buscarGeometriaShape(solar.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
						
						if(!validacaoGeoSeiaService.validaPercentualSobreposicao(geometriaA, geometriaB)){
							podeAdicionar = true;
							this.alterarUsina = Boolean.FALSE;
						}else{
							JsfUtil.addErrorMessage("O shape informado sobrepõe uma locação cadastrada. Verifique o shapefile informado e tente novamente.");
						}							
					}
					
					if(podeAdicionar){
						listaEnergiaSolarUsina.add(fceEnergiaSolarUsina);
					}
					
				}else{
					this.listaEnergiaSolarUsina.add(this.fceEnergiaSolarUsina);
				}
			}
			Html.atualizar("tabAbasGeracaoEnergia:formAbaEnergiaSolar:listaEnergiaSolarUsina");
			RequestContext.getCurrentInstance().execute("incluirFotovoltaica.hide();");
			this.alterarUsina = Boolean.FALSE;
		}
	}
	
	public void fecharUsina(){
		if(Util.isNullOuVazio(fceEnergiaSolarUsina.getIdeLocalizacaoGeografica())){
			fceEnergiaSolarUsina.setIdeLocalizacaoGeografica(locGeoAux);
		}else{
			locGeoAux = new LocalizacaoGeografica();
		}
	}
	
	public void removerUsina(){
		if(!Util.isNullOuVazio(this.fceEnergiaSolarUsinaAux)){
			
			this.listaEnergiaSolarUsina.remove(this.fceEnergiaSolarUsinaAux);
			Html.atualizar("tabAbasGeracaoEnergia:formAbaEnergiaSolar:listaEnergiaSolarUsina");
			JsfUtil.addSuccessMessage("Usina removida com sucesso.");
			try {
				if(!Util.isNullOuVazio(this.fceEnergiaSolar.getIdeFceEnergiaSolar())){
					this.fceEnergiaSolarUsinaService.remover(this.fceEnergiaSolarUsinaAux);
					this.fceEnergiaSolarUsinaAux = new FceEnergiaSolarUsina();
				}
				
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
			
		}
	}
	
	public void adicionarUnidade(){
		try {
			if(validaCamposUnidade()){
				boolean podeAdicionar = false;
				if(!Util.isNullOuVazio(this.listaTipoCombustivelSelecionado)){
					
					this.fceEnergiaTermoeletricaUnidade.setListaCombustivel(new ArrayList<FceEnergiaTermoeletricaUnidadeCombustivel>());
					for(TipoCombustivel tipo: this.listaTipoCombustivelSelecionado){
						FceEnergiaTermoeletricaUnidadeCombustivel combustivel = new FceEnergiaTermoeletricaUnidadeCombustivel();
						combustivel.setTipoCombustivel(tipo);
						this.fceEnergiaTermoeletricaUnidade.getListaCombustivel().add(combustivel);
					}
					
					if(!Util.isNullOuVazio(this.fceEnergiaTermoeletricaUnidade.getLocalizacaoGeografica())){
						String theGeom;
						
							theGeom = validacaoGeoSeiaService.buscarGeometriaShape(this.fceEnergiaTermoeletricaUnidade.getLocalizacaoGeografica().getIdeLocalizacaoGeografica());
	
						this.fceEnergiaEolicaParque.setValorAreaParque((new BigDecimal(validacaoGeoSeiaService.retonarAreaDoShapeByGeometria(theGeom).doubleValue())));
					}
					
				}
				
				if(!Util.isNull(listaEnergiaTermoeletricaUnidade)){
					
					if(listaEnergiaTermoeletricaUnidade.size() >=1){
						for(FceEnergiaTermoeletricaUnidade unidade : listaEnergiaTermoeletricaUnidade){
							String geometriaA = validacaoGeoSeiaService.buscarGeometriaShape(fceEnergiaTermoeletricaUnidade.getLocalizacaoGeografica().getIdeLocalizacaoGeografica());
							String geometriaB = validacaoGeoSeiaService.buscarGeometriaShape(unidade.getLocalizacaoGeografica().getIdeLocalizacaoGeografica());
							
							if(!validacaoGeoSeiaService.validaPercentualSobreposicao(geometriaA, geometriaB)){
								podeAdicionar = true;
							}else{
								JsfUtil.addErrorMessage("O shape informado sobrepõe uma locação cadastrada. Verifique o shapefile informado e tente novamente.");
							}
						}
						if(podeAdicionar){
							listaEnergiaTermoeletricaUnidade.add(fceEnergiaTermoeletricaUnidade);
							Html.atualizar("tabAbasGeracaoEnergia:formAbaEnergiaTermoeletrica:listaEnergiaTermoeletrica");
							RequestContext.getCurrentInstance().execute("incluirUnidadeTermoeletrica.hide();");
						}
					}else{
						this.listaEnergiaTermoeletricaUnidade.add(this.fceEnergiaTermoeletricaUnidade);
						Html.atualizar("tabAbasGeracaoEnergia:formAbaEnergiaTermoeletrica:listaEnergiaTermoeletrica");
						RequestContext.getCurrentInstance().execute("incluirUnidadeTermoeletrica.hide();");
					}
				}
				
			}
		} catch (Exception e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public void removerUnidade(){
		if(!Util.isNullOuVazio(this.fceEnergiaTermoeletricaUnidadeAux)){
			if(!Util.isNullOuVazio(this.fceEnergiaTermoeletricaUnidadeAux.getIdeFceEnergiaTermoeletricaUnidade())){
				try {
					this.fceEnergiaTermoeletricaUnidadeAux.setListaCombustivel(this.fceEnergiaTermoeletricaUnidadeCombustivelService.listarCombustivelByIdEnergiaUnidade(this.fceEnergiaTermoeletricaUnidadeAux));
					if(!Util.isNullOuVazio(this.fceEnergiaTermoeletricaUnidadeAux.getListaCombustivel())){
						for(FceEnergiaTermoeletricaUnidadeCombustivel combustivel: this.fceEnergiaTermoeletricaUnidadeAux.getListaCombustivel()){
							this.fceEnergiaTermoeletricaUnidadeCombustivelService.remover(combustivel);
						}
					}
					this.fceEnergiaTermoeletricaUnidadeAux.setListaGerador(this.fceEnergiaTermoeletricaUnidadeGeradorService.listarGeradorByUnidade(this.fceEnergiaTermoeletricaUnidadeAux));
					if(!Util.isNullOuVazio(this.fceEnergiaTermoeletricaUnidadeAux.getListaGerador())){
						for(FceEnergiaTermoeletricaUnidadeGerador gerador: this.fceEnergiaTermoeletricaUnidadeAux.getListaGerador()){
							this.fceEnergiaTermoeletricaUnidadeGeradorService.remover(gerador);
						}
					}
					
					this.fceEnergiaTermoeletricaUnidadeService.remover(fceEnergiaTermoeletricaUnidadeAux);
					JsfUtil.addSuccessMessage("Unidade Termoelétrica removida com sucesso.");
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				}
			}
			this.listaEnergiaTermoeletricaUnidade.remove(this.fceEnergiaTermoeletricaUnidadeAux);
			Html.atualizar("tabAbasGeracaoEnergia:formAbaEnergiaTermoeletrica:listaEnergiaTermoeletrica");
		}
	}
	
	public void adicionarGerador(){
		
		if(validaCamposGerador()){
			this.alterarGerador = Boolean.FALSE;
			
			if(fceEnergiaTermoeletricaUnidade.getListaGerador().contains(this.fceEnergiaTermoeletricaUnidadeGerador)){
				Integer index = fceEnergiaTermoeletricaUnidade.getListaGerador().indexOf(this.fceEnergiaTermoeletricaUnidadeGerador);
				fceEnergiaTermoeletricaUnidade.getListaGerador().set(index, this.fceEnergiaTermoeletricaUnidadeGerador);
			}else{
				fceEnergiaTermoeletricaUnidade.getListaGerador().add(this.fceEnergiaTermoeletricaUnidadeGerador);
			}
			Html.atualizar("formUnidadeTermoeletrica:listaGerador");
			RequestContext.getCurrentInstance().execute("incluirGerador.hide();");
		}
		
	}

	public void removerGerador(){
		try {
			if(!Util.isNullOuVazio(this.fceEnergiaTermoeletricaUnidadeGeradorAux)){
				this.fceEnergiaTermoeletricaUnidade.getListaGerador().remove(this.fceEnergiaTermoeletricaUnidadeGeradorAux);
				
				fceEnergiaTermoeletricaUnidadeGeradorService.remover(this.fceEnergiaTermoeletricaUnidadeGeradorAux);
	
				Html.atualizar("formUnidadeTermoeletrica:listaGerador");
				this.fceEnergiaTermoeletricaUnidadeGeradorAux = new FceEnergiaTermoeletricaUnidadeGerador();
				JsfUtil.addSuccessMessage("Gerador removido com sucesso.");
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	private Boolean validaCamposAerogerador(){
		Boolean retorno = Boolean.TRUE;
		
		
		if(Util.isNullOuVazio(this.fceEnergiaEolicaParqueAerogerador.getValorPotenciaAerogerador())){
			JsfUtil.addErrorMessage("Campo Obrigatório. Favor informar a Potência.");
			retorno = Boolean.FALSE;
		}
		
		if(Util.isNullOuVazio(this.fceEnergiaEolicaParqueAerogerador.getQuantidadeAerogeradores())){
			JsfUtil.addErrorMessage("Campo Obrigatório. Favor informar a quantidade de aerogeradores.");
			retorno = Boolean.FALSE;
		}
		
		return retorno;
	}
	
	private Boolean validaCamposParqueEolico(){
		Boolean retorno = Boolean.TRUE;
		
		if(Util.isNullOuVazio(this.fceEnergiaEolicaParque.getDesIdentificadorParque())){
			JsfUtil.addErrorMessage("Campo Obrigatório. Favor informar a Identificação do parque.");
			retorno = Boolean.FALSE;
		}
		
		if(Util.isNullOuVazio(this.fceEnergiaEolicaParque.getLocalizacaoGeografica())){
			JsfUtil.addErrorMessage("Campo Obrigatório. Favor informar a Localização Geográfica do parque.");
			retorno = Boolean.FALSE;
		}
		
		return retorno;
	}
	
	private Boolean validaCamposUsina(){
		Boolean retorno = Boolean.TRUE;
		
		if(Util.isNullOuVazio(this.fceEnergiaSolarUsina.getDesIdentificacaoUsina())){
			JsfUtil.addErrorMessage("Campo Obrigatório. Favor informar a Identificação da Usina.");
			retorno = Boolean.FALSE;
		}
		
		if(Util.isNullOuVazio(this.fceEnergiaSolarUsina.getValorPotenciaUsina())){
			JsfUtil.addErrorMessage("Campo Obrigatório. Favor informar a Potência da Usina.");
			retorno = Boolean.FALSE;
		}
		
		if(Util.isNullOuVazio(fceEnergiaSolarUsina.getIdeLocalizacaoGeografica()) && Util.isNullOuVazio(fceEnergiaSolarUsina.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())){
			JsfUtil.addErrorMessage("Campo Obrigatório. Favor informar a Poligonal da Usina.");
			retorno = Boolean.FALSE;
		}
		
		return retorno;
	}
	
	private Boolean validaCamposGerador(){
		Boolean retorno = Boolean.TRUE;
		
		if(Util.isNullOuVazio(this.fceEnergiaTermoeletricaUnidadeGerador.getDesIdentificacaoGerador())){
			JsfUtil.addErrorMessage("Campo Obrigatório. Favor informar a Identificação do Gerador.");
			retorno = Boolean.FALSE;
		}
		
		if(Util.isNullOuVazio(this.fceEnergiaTermoeletricaUnidadeGerador.getValPotenciaGerador())){
			JsfUtil.addErrorMessage("Campo Obrigatório. Favor informar a Potência do Gerador.");
			retorno = Boolean.FALSE;
		}
		
		return retorno;
	}
	
	private Boolean validaCamposUnidade(){
		Boolean retorno = Boolean.TRUE;
		
		if(Util.isNullOuVazio(this.fceEnergiaTermoeletricaUnidade.getDesIdentificacaoUnidade())){
			JsfUtil.addErrorMessage("Campo Obrigatório. Favor informar a Identificação da Unidade Termoelétrica.");
			retorno = Boolean.FALSE;
		}
		
		if(this.fceEnergiaTermoeletricaUnidade.getIndSistemaControleEmissao()){
			if(Util.isNullOuVazio(this.fceEnergiaTermoeletricaUnidade.getValEficienciaControleEmissao())){
				JsfUtil.addErrorMessage("Campo Obrigatório. Favor informar o percentual de eficiência do sistema de controle.");
				retorno = Boolean.FALSE;
			}
		}
		
		if(Util.isNullOuVazio(this.listaTipoCombustivelSelecionado)){
			JsfUtil.addErrorMessage("Campo Obrigatório. Favor informar ao menos 1 Tipo de Combustível.");
			retorno = Boolean.FALSE;
		}
		
		
		return retorno;
	}
	
	public void habilitaPercentualEficiencia(){
		if(!this.fceEnergiaTermoeletricaUnidade.getIndSistemaControleEmissao()){
			
			this.fceEnergiaTermoeletricaUnidade.setValEficienciaControleEmissao(null);
			
		}
	}
	
	@Override
	public void finalizar() {
		
		if(this.validaCamposFinalizar(Boolean.TRUE)){
		
			try {
				fceEnergiaService.finalizar(this);
			} catch (Exception e) {

				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
				
			}
			
		}else{
			
			JsfUtil.addErrorMessage("Erro ao finalizar a FCE - Geração de Energia. Favor cadastrar todos os campos obrigatórios.");
		}
		
	}
	
	@Override
	public void prepararParaFinalizar() throws Exception {

		if (!preValidarEnergiaEolica()){
			if(verificarMarcacaoOutrosFinalidade() && verificarMarcacaoOutrosTipoCombustivel()){
				super.concluirFce();
				RequestContext.getCurrentInstance().execute("rel_fce_geracao_energia.show()");
				Html.esconder("geracaoEnergia");
				Html.atualizar("formImprimirRelatorioGeracaoEnergia");
				JsfUtil.addSuccessMessage("FCE - Geração de Energia finalizado com sucesso.");
			}else{
				fce.setIndConcluido(false);
				super.salvarFce();
				super.exibirInformacao001();
			}
		}
		
		this.flagFinalizar = Boolean.TRUE;
		this.salvarDadosGerais();
		
		if(!desabilitaAbaEnergiaEolica){
			this.salvarEnergiaEolica();
		}
		
		if(!desabilitaAbaEnergiaSolar){
			this.salvarEnergiaSolar();
		}
		
		if(!desabilitaAbaTermoeletrica){
			this.salvarTermoeletrica();
		}
		
		this.activeIndex = 4;
	
		if(isFceTecnico()){
			Html.executarJS("atualizarDadoConcedido();");
		}
	}

	
	private boolean verificarMarcacaoOutrosFinalidade() {
		
		Boolean retorno = Boolean.TRUE;
		if(!Util.isNullOuVazio(listaFinalidadeGeracaoEnergiaSelecionado)){
			for(FinalidadeGeracaoEnergia finalidae : listaFinalidadeGeracaoEnergiaSelecionado){
				 if(!finalidae.getDesFinalidadeGeracaoEnergia().equalsIgnoreCase("outros")){
					 retorno = Boolean.TRUE;
				 }else{
					 return false;
				 }
			}
		}
		return retorno;
	}
	
	private boolean verificarMarcacaoOutrosTipoCombustivel() {
		
		Boolean retorno = Boolean.TRUE;
		
		for(FceEnergiaTermoeletricaUnidade unidade : listaEnergiaTermoeletricaUnidade){

			for(FceEnergiaTermoeletricaUnidadeCombustivel combustivel : unidade.getListaCombustivel()){
				if(!(combustivel.getTipoCombustivel().getIdeTipoCombustivel() == 4)){
					retorno = Boolean.TRUE;
				}else{
					return false;
				}
			}
		}
		
		return retorno;
		
	}
	
	@Override
	protected void prepararDuplicacao() {
		
		fceEnergia.setIdeFceEnergia(null);
			fce.setIdeFce(null);
			fceEnergiaEolica.setIdeFceEnergiaEolica(null);
			
			for(FceEnergiaEolicaParque parque : listaEnergiaEolicaParque){
				parque.setFceEnergiaEolica(null);
				parque.setIdeFceEnergiaEolicaParque(null);
				
				for(FceEnergiaEolicaParqueAerogerador aerogerador : parque.getListaAerogerador()){
					aerogerador.setFceEnergiaEolicaParque(null);
					aerogerador.setIdeFceEnergiaEolicaParqueAerogerador(null);
				}
			}
			
				fceEnergiaEolicaLicencaPrevia.setIdeFceEnergiaEolicaLicencaPrevia(null);        
				fceEnergiaEolicaParque.setIdeFceEnergiaEolicaParque(null);                      
				fceEnergiaEolicaParqueAerogerador.setIdeFceEnergiaEolicaParqueAerogerador(null);
			fceEnergiaSolar.setIdeFceEnergiaSolar(null);      
				fceEnergiaSolarUsina.setIdeFceEnergiaSolarUsina(null);

				for(FceEnergiaSolarUsina usina : listaEnergiaSolarUsina){
					usina.setIdeFceEnergiaSolarUsina(null);
					usina.setIdeFceEnergiaSolar(null);
				}
				
			fceEnergiaTermoeletrica.setIdeFceEnergiaTermoEletrica(null);                    
				fceEnergiaTermoeletrica.setIdeFceEnergia(null);
				fceEnergiaTermoeletricaUnidade.setIdeFceEnergiaTermoeletricaUnidade(null);
					fceEnergiaTermoeletricaUnidadeCombustivel.setIdeFceEnergiaTermoeletricaUnidadeCombustivel(null);
					fceEnergiaTermoeletricaUnidadeGerador.setIdeFceEnergiaTermoeletricaUnidadeGerador(null);
					
					for(FceEnergiaTermoeletricaUnidade unidade : listaEnergiaTermoeletricaUnidade){
						unidade.setFceEnergiaTermoeletrica(null);
						unidade.setIdeFceEnergiaTermoeletricaUnidade(null);
						
						for(FceEnergiaTermoeletricaUnidadeCombustivel combustivel : unidade.getListaCombustivel()){
							combustivel.setFceEnergiaTermoeletricaUnidade(null);
							combustivel.setIdeFceEnergiaTermoeletricaUnidadeCombustivel(null);
						}
						
						for(FceEnergiaTermoeletricaUnidadeGerador gerador : unidade.getListaGerador()){
							gerador.setFceEnergiaTermoeletricaUnidade(null);
							gerador.setIdeFceEnergiaTermoeletricaUnidadeGerador(null);
						}
					}
	}

	@Override
	protected void duplicarFce() {
		
		try {
			prepararDublicacao = true;
			super.salvarFce();
			salvarDadosGerais();
			salvarEnergiaEolica();
			salvarEnergiaSolar();
			salvarTermoeletrica();
			prepararDublicacao = false;
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " Geração de Energia.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	protected void carregarFceTecnico(){
		try {
			
			//Inicializar objetos e listas
			iniciarFceEnergia();
			iniciarFceEnergiaEolica();
			iniciarEnergiaEolicaLicencaPrevia();
			iniciarFceEnergiaEolicaParque();
			iniciarFceEnergiaEolicaParqueAerogerador();
			iniciarEnergiaSolar();
			iniciarEnergiaSolarUsina();
			iniciarEnergiaTermoeletrica();
			iniciarEnergiaTermoeletricaCombustivel();
			iniciarEnergiaTermoeletricaGerador();
			iniciarListas();

			// carregar dados do fce do tecnico
			abrirModalGeracaoEnergia();
			obterQuantidadesInformadaNoRequerimento();
			carregarListas();
			gerarMsgImprimirRelatorio("Geração de Energia");
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do FCE - Sistema de Abastecimento de Água.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void verificarLocalizacao(){
		if(!Util.isNullOuVazio(fceEnergia.getIdeLocalizacaoGeografica())){
			if(Util.isNullOuVazio(fceEnergia.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica())){
				fceEnergia.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
			}
		}else{
			fceEnergia.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		}
	}

	public void gerarMsgImprimirRelatorio(String nomFce) {
		if(isFceTecnico()){
			msgImprimirRelatorio = Util.getString("geral_msg_imprimir_relatorio_dados_concedidos_fce");
		}else{
			msgImprimirRelatorio = Util.getString("geral_msg_imprimir_relatorio_fce") + " " + nomFce + "?";
		}
	}

	public String getMsgExducluir() {
		return Util.getString("msg_confirma_exclusao");
	}
	
	public StreamedContent getImprimirRelatorio() throws Exception {
		return getImprimirRelatorio(super.fce, DOC_OBRIGATORIO_GERACAO_ENERGIA);
	}
	
	public void validaASV(){
		if(!this.fceEnergia.getIndASV()){
			this.fceEnergia.setNumProcessoASV(null);
			this.listaFinalidadeGeracaoEnergiaSelecionado = new ArrayList<FinalidadeGeracaoEnergia>();
		}
	}
	
	public void validaCaptacao(){
		if(!this.fceEnergia.getIndCaptacao()){
			this.fceEnergia.setNumProcessoOutorga(null);
		}
	}
	
	@Override
	public void abrirDialog() {
		RequestContext.getCurrentInstance().addPartialUpdateTarget("pnlFceGeracaoEnergia");
		RequestContext.getCurrentInstance().execute("geracaoEnergia.show();");
	}
	
	@Override
	public void limpar() {
		
	}
	
	@Override
	public boolean validarAba() {
		return false;
	}
	
	
	@Override
	public void carregarAba() {
	
	}
	
	
	/*
	 * GETTER / SETTER
	 */
	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	public Boolean getDesabilitaAbaEnergiaEolica() {
		return desabilitaAbaEnergiaEolica;
	}

	public void setDesabilitaAbaEnergiaEolica(Boolean desabilitaAbaEnergiaEolica) {
		this.desabilitaAbaEnergiaEolica = desabilitaAbaEnergiaEolica;
	}

	public Boolean getDesabilitaAbaEnergiaSolar() {
		return desabilitaAbaEnergiaSolar;
	}

	public void setDesabilitaAbaEnergiaSolar(Boolean desabilitaAbaEnergiaSolar) {
		this.desabilitaAbaEnergiaSolar = desabilitaAbaEnergiaSolar;
	}

	public Boolean getDesabilitaAbaTermoeletrica() {
		return desabilitaAbaTermoeletrica;
	}

	public void setDesabilitaAbaTermoeletrica(Boolean desabilitaAbaTermoeletrica) {
		this.desabilitaAbaTermoeletrica = desabilitaAbaTermoeletrica;
	}

	public FceEnergia getFceEnergia() {
		return fceEnergia;
	}

	public void setFceEnergia(FceEnergia fceEnergia) {
		this.fceEnergia = fceEnergia;
	}

	public List<AtoAmbiental> getListaAtoAmbiental() {
		return listaAtoAmbiental;
	}

	public void setListaAtoAmbiental(List<AtoAmbiental> listaAtoAmbiental) {
		this.listaAtoAmbiental = listaAtoAmbiental;
	}

	public Boolean getDesabilitaCaptacaoAgua() {
		return desabilitaCaptacaoAgua;
	}

	public void setDesabilitaCaptacaoAgua(Boolean desabilitaCaptacaoAgua) {
		this.desabilitaCaptacaoAgua = desabilitaCaptacaoAgua;
	}

	public Boolean getDesabilitaASV() {
		return desabilitaASV;
	}

	public void setDesabilitaASV(Boolean desabilitaASV) {
		this.desabilitaASV = desabilitaASV;
	}
	
	public int getNumeroAba() {
		return numeroAba;
	}

	public void setNumeroAba(int numeroAba) {
		this.numeroAba = numeroAba;
	}

	public Boolean getRenderedEnergiaEolica() {
		return renderedEnergiaEolica;
	}

	public void setRenderedEnergiaEolica(Boolean renderedEnergiaEolica) {
		this.renderedEnergiaEolica = renderedEnergiaEolica;
	}

	public FceEnergiaEolica getFceEnergiaEolica() {
		return fceEnergiaEolica;
	}

	public void setFceEnergiaEolica(FceEnergiaEolica fceEnergiaEolica) {
		this.fceEnergiaEolica = fceEnergiaEolica;
	}

	public FceEnergiaEolicaLicencaPrevia getFceEnergiaEolicaLicencaPrevia() {
		return fceEnergiaEolicaLicencaPrevia;
	}

	public void setFceEnergiaEolicaLicencaPrevia(
			FceEnergiaEolicaLicencaPrevia fceEnergiaEolicaLicencaPrevia) {
		this.fceEnergiaEolicaLicencaPrevia = fceEnergiaEolicaLicencaPrevia;
	}

	public FceEnergiaEolicaParque getFceEnergiaEolicaParque() {
		return fceEnergiaEolicaParque;
	}

	public void setFceEnergiaEolicaParque(
			FceEnergiaEolicaParque fceEnergiaEolicaParque) {
		this.fceEnergiaEolicaParque = fceEnergiaEolicaParque;
	}

	public FceEnergiaEolicaParqueAerogerador getFceEnergiaEolicaParqueAerogerador() {
		return fceEnergiaEolicaParqueAerogerador;
	}

	public void setFceEnergiaEolicaParqueAerogerador(
			FceEnergiaEolicaParqueAerogerador fceEnergiaEolicaParqueAerogerador) {
		this.fceEnergiaEolicaParqueAerogerador = fceEnergiaEolicaParqueAerogerador;
	}

	public List<FceEnergiaEolicaParqueAerogerador> getListaEnergiaEolicaParqueAerogerador() {
		return listaEnergiaEolicaParqueAerogerador;
	}

	public void setListaEnergiaEolicaParqueAerogerador(
			List<FceEnergiaEolicaParqueAerogerador> listaEnergiaEolicaParqueAerogerador) {
		this.listaEnergiaEolicaParqueAerogerador = listaEnergiaEolicaParqueAerogerador;
	}

	public List<FceEnergiaEolicaParque> getListaEnergiaEolicaParque() {
		return listaEnergiaEolicaParque;
	}

	public void setListaEnergiaEolicaParque(
			List<FceEnergiaEolicaParque> listaEnergiaEolicaParque) {
		this.listaEnergiaEolicaParque = listaEnergiaEolicaParque;
	}

	public FceEnergiaEolicaParqueAerogerador getFceEnergiaEolicaParqueAerogeradorAux() {
		return fceEnergiaEolicaParqueAerogeradorAux;
	}

	public void setFceEnergiaEolicaParqueAerogeradorAux(
			FceEnergiaEolicaParqueAerogerador fceEnergiaEolicaParqueAerogeradorAux) {
		this.fceEnergiaEolicaParqueAerogeradorAux = fceEnergiaEolicaParqueAerogeradorAux;
	}

	public FceEnergiaEolicaParque getFceEnergiaEolicaParqueAux() {
		return fceEnergiaEolicaParqueAux;
	}

	public void setFceEnergiaEolicaParqueAux(
			FceEnergiaEolicaParque fceEnergiaEolicaParqueAux) {
		this.fceEnergiaEolicaParqueAux = fceEnergiaEolicaParqueAux;
	}

	public Integer getNumeroTotalParque() {
		this.numeroTotalParque = 0;
		if(!Util.isNullOuVazio(this.listaEnergiaEolicaParque)){
			this.numeroTotalParque = this.listaEnergiaEolicaParque.size();
		}
		return numeroTotalParque;
	}

	public void setNumeroTotalParque(Integer numeroTotalParque) {
		this.numeroTotalParque = numeroTotalParque;
	}

	public Integer getNumeroTotalAerogerador() {
		this.numeroTotalAerogerador = 0;
		if(!Util.isNullOuVazio(this.listaEnergiaEolicaParque)){
			for(FceEnergiaEolicaParque parque : this.listaEnergiaEolicaParque){
				for(FceEnergiaEolicaParqueAerogerador e : parque.getListaAerogerador()) {
					this.numeroTotalAerogerador = this.numeroTotalAerogerador + e.getQuantidadeAerogeradores();
				}
			}
			
		}
		return numeroTotalAerogerador;
	}
	
	public Integer totalNumeroAerogeradorParque(FceEnergiaEolicaParque parque) {
		this.totalNumeroGeradoresParque = 0;
		if(!Util.isNullOuVazio(this.listaEnergiaEolicaParque)){
			Integer index = listaEnergiaEolicaParque.indexOf(parque);
				for(FceEnergiaEolicaParqueAerogerador e : listaEnergiaEolicaParque.get(index).getListaAerogerador()) {
					this.totalNumeroGeradoresParque = this.totalNumeroGeradoresParque + e.getQuantidadeAerogeradores();
				}
		}
		return totalNumeroGeradoresParque;
	}

	public void setNumeroTotalAerogerador(Integer numeroTotalAerogerador) {
		this.numeroTotalAerogerador = numeroTotalAerogerador;
	}

	public Boolean getAlterarAerogerador() {
		return alterarAerogerador;
	}

	public void setAlterarAerogerador(Boolean alterarAerogerador) {
		this.alterarAerogerador = alterarAerogerador;
	}

	public Boolean getAlterarParque() {
		return alterarParque;
	}

	public void setAlterarParque(Boolean alterarParque) {
		this.alterarParque = alterarParque;
	}

	public FceEnergiaSolar getFceEnergiaSolar() {
		return fceEnergiaSolar;
	}

	public void setFceEnergiaSolar(FceEnergiaSolar fceEnergiaSolar) {
		this.fceEnergiaSolar = fceEnergiaSolar;
	}

	public FceEnergiaSolarUsina getFceEnergiaSolarUsina() {
		return fceEnergiaSolarUsina;
	}

	public void setFceEnergiaSolarUsina(FceEnergiaSolarUsina fceEnergiaSolarUsina) {
		this.fceEnergiaSolarUsina = fceEnergiaSolarUsina;
	}

	public List<FceEnergiaSolarUsina> getListaEnergiaSolarUsina() {
		return listaEnergiaSolarUsina;
	}

	public void setListaEnergiaSolarUsina(
			List<FceEnergiaSolarUsina> listaEnergiaSolarUsina) {
		this.listaEnergiaSolarUsina = listaEnergiaSolarUsina;
	}

	public Integer getNumeroTotalUsina() {
		
		this.numeroTotalUsina = 0;
		if(!Util.isNullOuVazio(this.listaEnergiaSolarUsina)){
			this.numeroTotalUsina = this.listaEnergiaSolarUsina.size();
		}
		
		return numeroTotalUsina;
	}

	public void setNumeroTotalUsina(Integer numeroTotalUsina) {
		this.numeroTotalUsina = numeroTotalUsina;
	}

	public BigDecimal getAreaTotalUsina() {
		this.areaTotalUsina = BigDecimal.ZERO;
		if(!Util.isNullOuVazio(this.listaEnergiaSolarUsina)){
			for(FceEnergiaSolarUsina usina : this.listaEnergiaSolarUsina){
				this.areaTotalUsina = this.areaTotalUsina.add(usina.getValorAreaUsina());
			}
		}
		return areaTotalUsina;
	}

	public void setAreaTotalUsina(BigDecimal areaTotalUsina) {
		this.areaTotalUsina = areaTotalUsina;
	}

	public FceEnergiaSolarUsina getFceEnergiaSolarUsinaAux() {
		return fceEnergiaSolarUsinaAux;
	}

	public void setFceEnergiaSolarUsinaAux(
			FceEnergiaSolarUsina fceEnergiaSolarUsinaAux) {
		this.fceEnergiaSolarUsinaAux = fceEnergiaSolarUsinaAux;
	}

	public Boolean getAlterarUsina() {
		return alterarUsina;
	}

	public void setAlterarUsina(Boolean alterarUsina) {
		this.alterarUsina = alterarUsina;
	}

	public Boolean getVisualizarParque() {
		return visualizarParque;
	}

	public void setVisualizarParque(Boolean visualizarParque) {
		this.visualizarParque = visualizarParque;
	}

	public Boolean getVisualizarUsina() {
		return visualizarUsina;
	}

	public void setVisualizarUsina(Boolean visualizarUsina) {
		this.visualizarUsina = visualizarUsina;
	}

	public FceEnergiaTermoEletrica getFceEnergiaTermoeletrica() {
		return fceEnergiaTermoeletrica;
	}

	public void setFceEnergiaTermoeletrica(
			FceEnergiaTermoEletrica fceEnergiaTermoeletrica) {
		this.fceEnergiaTermoeletrica = fceEnergiaTermoeletrica;
	}

	public FceEnergiaTermoeletricaUnidade getFceEnergiaTermoeletricaUnidade() {
		return fceEnergiaTermoeletricaUnidade;
	}

	public void setFceEnergiaTermoeletricaUnidade(
			FceEnergiaTermoeletricaUnidade fceEnergiaTermoeletricaUnidade) {
		this.fceEnergiaTermoeletricaUnidade = fceEnergiaTermoeletricaUnidade;
	}

	public List<FceEnergiaTermoeletricaUnidade> getListaEnergiaTermoeletricaUnidade() {
		return listaEnergiaTermoeletricaUnidade;
	}

	public void setListaEnergiaTermoeletricaUnidade(
			List<FceEnergiaTermoeletricaUnidade> listaEnergiaTermoeletricaUnidade) {
		this.listaEnergiaTermoeletricaUnidade = listaEnergiaTermoeletricaUnidade;
	}

	public FceEnergiaTermoeletricaUnidade getFceEnergiaTermoeletricaUnidadeAux() {
		return fceEnergiaTermoeletricaUnidadeAux;
	}

	public void setFceEnergiaTermoeletricaUnidadeAux(
			FceEnergiaTermoeletricaUnidade fceEnergiaTermoeletricaUnidadeAux) {
		this.fceEnergiaTermoeletricaUnidadeAux = fceEnergiaTermoeletricaUnidadeAux;
	}

	public Integer getNumeroTotalUnidade() {
		this.numeroTotalUnidade = 0;
		if(!Util.isNullOuVazio(this.listaEnergiaTermoeletricaUnidade)){
			this.numeroTotalUnidade = this.listaEnergiaTermoeletricaUnidade.size();
		}
		return numeroTotalUnidade;
	}

	public void setNumeroTotalUnidade(Integer numeroTotalUnidade) {
		this.numeroTotalUnidade = numeroTotalUnidade;
	}

	public BigDecimal getNumeroTotalPotencia() {
		this.numeroTotalPotencia = BigDecimal.ZERO;
		if(!Util.isNullOuVazio(this.listaEnergiaTermoeletricaUnidade)){
			for(FceEnergiaTermoeletricaUnidade unidade : this.listaEnergiaTermoeletricaUnidade){
				if(!Util.isNullOuVazio(unidade.getListaGerador())){
					for(FceEnergiaTermoeletricaUnidadeGerador gerador: unidade.getListaGerador()){
						if(Util.isNullOuVazio(gerador.getValPotenciaGerador())){
							gerador.setValPotenciaGerador(BigDecimal.ZERO);
						}
						this.numeroTotalPotencia = this.numeroTotalPotencia.add(gerador.getValPotenciaGerador());
					}
				}
			}
		}
		return numeroTotalPotencia;
	}

	public void setNumeroTotalPotencia(BigDecimal numeroTotalPotencia) {
		this.numeroTotalPotencia = numeroTotalPotencia;
	}

	public FceEnergiaTermoeletricaUnidadeCombustivel getFceEnergiaTermoeletricaUnidadeCombustivel() {
		return fceEnergiaTermoeletricaUnidadeCombustivel;
	}

	public void setFceEnergiaTermoeletricaUnidadeCombustivel(
			FceEnergiaTermoeletricaUnidadeCombustivel fceEnergiaTermoeletricaUnidadeCombustivel) {
		this.fceEnergiaTermoeletricaUnidadeCombustivel = fceEnergiaTermoeletricaUnidadeCombustivel;
	}

	public FceEnergiaTermoeletricaUnidadeGerador getFceEnergiaTermoeletricaUnidadeGerador() {
		return fceEnergiaTermoeletricaUnidadeGerador;
	}

	public void setFceEnergiaTermoeletricaUnidadeGerador(
			FceEnergiaTermoeletricaUnidadeGerador fceEnergiaTermoeletricaUnidadeGerador) {
		this.fceEnergiaTermoeletricaUnidadeGerador = fceEnergiaTermoeletricaUnidadeGerador;
	}

	public FceEnergiaTermoeletricaUnidadeGerador getFceEnergiaTermoeletricaUnidadeGeradorAux() {
		return fceEnergiaTermoeletricaUnidadeGeradorAux;
	}

	public void setFceEnergiaTermoeletricaUnidadeGeradorAux(
			FceEnergiaTermoeletricaUnidadeGerador fceEnergiaTermoeletricaUnidadeGeradorAux) {
		this.fceEnergiaTermoeletricaUnidadeGeradorAux = fceEnergiaTermoeletricaUnidadeGeradorAux;
	}

	public Boolean getAlterarGerador() {
		return alterarGerador;
	}

	public void setAlterarGerador(Boolean alterarGerador) {
		this.alterarGerador = alterarGerador;
	}

	public Boolean getDesabilitaAbas() {
		return desabilitaAbas;
	}

	public void setDesabilitaAbas(Boolean desabilitaAbas) {
		this.desabilitaAbas = desabilitaAbas;
	}

	public List<FinalidadeGeracaoEnergia> getListaFinalidadeGeracaoEnergiaSelecionado() {
		return listaFinalidadeGeracaoEnergiaSelecionado;
	}

	public void setListaFinalidadeGeracaoEnergiaSelecionado(
			List<FinalidadeGeracaoEnergia> listaFinalidadeGeracaoEnergiaSelecionado) {
		this.listaFinalidadeGeracaoEnergiaSelecionado = listaFinalidadeGeracaoEnergiaSelecionado;
	}

	public List<FinalidadeGeracaoEnergia> getListaFinalidadeGeracaoEnergiaAux() {
		return listaFinalidadeGeracaoEnergiaAux;
	}

	public void setListaFinalidadeGeracaoEnergiaAux(
			List<FinalidadeGeracaoEnergia> listaFinalidadeGeracaoEnergiaAux) {
		this.listaFinalidadeGeracaoEnergiaAux = listaFinalidadeGeracaoEnergiaAux;
	}

	public LocalizacaoGeografica getLocGeoAux() {
		return locGeoAux;
	}

	public void setLocGeoAux(LocalizacaoGeografica locGeoAux) {
		this.locGeoAux = locGeoAux;
	}

	public Boolean getVisualizarUnidadeTermo() {
		return visualizarUnidadeTermo;
	}

	public void setVisualizarUnidadeTermo(Boolean visualizarUnidadeTermo) {
		this.visualizarUnidadeTermo = visualizarUnidadeTermo;
	}

	public Boolean getAlterarUnidadeTermo() {
		return alterarUnidadeTermo;
	}

	public void setAlterarUnidadeTermo(Boolean alterarUnidadeTermo) {
		this.alterarUnidadeTermo = alterarUnidadeTermo;
	}

	public List<TipoCombustivel> getListaTipoCombustivelSelecionado() {
		return listaTipoCombustivelSelecionado;
	}

	public void setListaTipoCombustivelSelecionado(
			List<TipoCombustivel> listaTipoCombustivelSelecionado) {
		this.listaTipoCombustivelSelecionado = listaTipoCombustivelSelecionado;
	}

	public String getMunicipioAbaGeral() {
		
		if(!Util.isNullOuVazio(this.fceEnergia) && !Util.isNullOuVazio(this.fceEnergia.getIdeLocalizacaoGeografica())){
			try {
				
				municipioAbaGeral = this.fceEnergiaService.obterMunicipioByLocalizacao(this.fceEnergia.getIdeLocalizacaoGeografica());
				
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
		}else{
			
			municipioAbaGeral = null;
					
		}
		
		return municipioAbaGeral;
	}

	public void setMunicipioAbaGeral(String municipioAbaGeral) {
		this.municipioAbaGeral = municipioAbaGeral;
	}

	public String getMunicipioAbaEolica() {
		
		if(!Util.isNullOuVazio(this.fceEnergiaEolicaParque) && !Util.isNullOuVazio(this.fceEnergiaEolicaParque.getLocalizacaoGeografica())){
			try {
				
				municipioAbaEolica = this.fceEnergiaService.obterMunicipioByLocalizacao(this.fceEnergiaEolicaParque.getLocalizacaoGeografica());
				
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
		}else{
			
			municipioAbaEolica = null;
			
		}
		
		return municipioAbaEolica;
	}

	public void setMunicipioAbaEolica(String municipioAbaEolica) {
		this.municipioAbaEolica = municipioAbaEolica;
	}

	public String getMunicipioAbaSolar() {
		
		if(!Util.isNullOuVazio(this.fceEnergiaSolarUsina) && !Util.isNullOuVazio(this.fceEnergiaSolarUsina.getIdeLocalizacaoGeografica())){
			try {
				
				municipioAbaSolar = this.fceEnergiaService.obterMunicipioByLocalizacao(this.fceEnergiaSolarUsina.getIdeLocalizacaoGeografica());
				
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
		}else{
			
			municipioAbaSolar = null;
		}
		
		return municipioAbaSolar;
	}

	public void setMunicipioAbaSolar(String municipioAbaSolar) {
		this.municipioAbaSolar = municipioAbaSolar;
	}

	public String getMunicipioAbaTermoeletrica() {
		
		if(!Util.isNullOuVazio(this.fceEnergiaTermoeletricaUnidade) && !Util.isNullOuVazio(this.fceEnergiaTermoeletricaUnidade.getLocalizacaoGeografica())){
			try {
				
				municipioAbaTermoeletrica = this.fceEnergiaService.obterMunicipioByLocalizacao(this.fceEnergiaTermoeletricaUnidade.getLocalizacaoGeografica());
				
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			}
		}else{
			
			municipioAbaTermoeletrica = null;
		}
		
		return municipioAbaTermoeletrica;
	}

	public void setMunicipioAbaTermoeletrica(String municipioAbaTermoeletrica) {
		this.municipioAbaTermoeletrica = municipioAbaTermoeletrica;
	}

	public Integer getTotalAerogeradores() {
		return totalAerogeradores;
	}

	public BigDecimal getTotalPotenciaAerogeradores() {
		return totalPotenciaAerogeradores;
	}

	public void setTotalPotenciaAerogeradores(BigDecimal totalPotenciaAerogeradores) {
		this.totalPotenciaAerogeradores = totalPotenciaAerogeradores;
	}

	public void setTotalAerogeradores(Integer totalAerogeradores) {
		this.totalAerogeradores = totalAerogeradores;
	}

	public MetodoUtil getMetodoAtualizarExterno() {
		return metodoAtualizarExterno;
	}

	public void setMetodoAtualizarExterno(MetodoUtil metodoAtualizarExterno) {
		this.metodoAtualizarExterno = metodoAtualizarExterno;
	}

	public List<FinalidadeGeracaoEnergia> getListFinalidadeGeracaoEnergia() {
		return listFinalidadeGeracaoEnergia;
	}

	public void setListFinalidadeGeracaoEnergia(
			List<FinalidadeGeracaoEnergia> listFinalidadeGeracaoEnergia) {
		this.listFinalidadeGeracaoEnergia = listFinalidadeGeracaoEnergia;
	}

	public String getMsgImprimirRelatorio() {
		return msgImprimirRelatorio;
	}

	public void setMsgImprimirRelatorio(String msgImprimirRelatorio) {
		this.msgImprimirRelatorio = msgImprimirRelatorio;
	}

}