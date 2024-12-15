package br.gov.ba.seia.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.DestinoSocioeconomicoProduto;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.EspecieSupressaoAutorizacao;
import br.gov.ba.seia.entity.FceAsv;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.JustificativaSupressao;
import br.gov.ba.seia.entity.ObjetivoSupressao;
import br.gov.ba.seia.entity.OcorrenciaArea;
import br.gov.ba.seia.entity.Produto;
import br.gov.ba.seia.entity.ProdutoSupressao;
import br.gov.ba.seia.enumerator.ObjetivoSupressaoEnum;
import br.gov.ba.seia.enumerator.OcorrenciaAreaEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AsvSupressaoService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.FceService;
import br.gov.ba.seia.service.ProdutoService;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.MetodoUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author eduardo.fernandes
 * @date 15/08/2013
 */

@Named("asvSupressaoController")
@ViewScoped
public class AsvSupressaoController extends EspecieSupressaoController {

	@Inject
	private AsvDadosGeraisController asvDadosGeraisController;
	@EJB
	private AsvSupressaoService asvSupressaoService;
	@EJB
	private FceService fceService;
	@EJB
	private ProdutoService produtoService;
	@EJB
	private EmpreendimentoService empreendimentoService;

	// Constantes
	private static final int IDE_TIPO_PRODUTO_SUPRESSAO = 3;

	// Constantes para conversão de medidas do volume total.
	private static final Double MDC = 0.5;
	private static final Double ESTEREO = 1.5;

	// Somatório das áraeas de APP dos imóveis rurais associados aquele empreendimento.
	private Double areaTotalAppEmpreendimento;

	private List<JustificativaSupressao> listaJustificativaSupressao;
	private List<JustificativaSupressao> listaJustificativaSupressaoSelecionadas;
	private List<OcorrenciaArea> listaOcorrenciaArea;
	private List<OcorrenciaArea> listaOcorrenciaAreaSelecionadas;

	/**
	 * @Comentário lista que retorna todos os objetivo_supressao com ide_pai NULL no BD
	 */
	private List<ObjetivoSupressao> listaObjetivoSupressao;
	/**
	 * @Comentário  lista que recebe TODOS os objetivo_supresao
	 */
	private List<ObjetivoSupressao> listaObjetivoSupressaoSelecionadas;
	/**
	 * @Comentário lista que recebe os objetivo_supressao_pai
	 */
	private List<ObjetivoSupressao> listaObjetivoSupressaoPaiSelecionados;
	/**
	 * @Comentário  lista que vai receber todos os objetivo_supressao com ide_pai != NULL do BD e que vai ser usada para redistribuir os objetos em sub-listas.
	 */
	private List<ObjetivoSupressao> listaObjetivoSupressaoFilhas;

	private Boolean indAtividadeAgro = false;
	private List<ObjetivoSupressao> listObjSupAgro;
	private List<ObjetivoSupressao> listObjSupAgroSelecionados;

	private Boolean indAtividadeMiner = false;

	private Boolean indOleoGas = false;
	private List<ObjetivoSupressao> listObjSupOleoGas;
	private List<ObjetivoSupressao> listObjSupOleoGasSelecionados;

	private Boolean indMiner = false;
	private List<ObjetivoSupressao> listObjSupMiner;
	private List<ObjetivoSupressao> listObjSupMinerSelecionados;

	private Boolean indEmpreendIndust = false;
	private List<ObjetivoSupressao> listObjSupEmpInd;
	private List<ObjetivoSupressao> listObjSupEmpIndSelecionados;

	private Boolean indEmpreendInfraEner = false;
	private List<ObjetivoSupressao> listObjSupEmpInfra;
	private List<ObjetivoSupressao> listObjSupEmpInfraSelecionados;

	private Boolean indEmpreendInterSocial = false;
	private List<ObjetivoSupressao> listObjSupEmpInterSocial;
	private List<ObjetivoSupressao> listObjSupEmpInterSocialSelecionados;

	private Boolean indConstruCivil = false;
	private List<ObjetivoSupressao> listObjSupConstrucao;
	private List<ObjetivoSupressao> listObjSupConstrucaoSelecionados;

	private Boolean indEmpreendUrbanTuristicoLazer = false;
	private List<ObjetivoSupressao> listObjSupEmpUrban;
	private List<ObjetivoSupressao> listObjSupEmpUrbanSelecionados;

	private Boolean indOutroEmpreendimento = false;
	private String outroEmpreendimento;

	private Boolean indAbandonada = false;
	private Double areaAbandonada;

	private Boolean indInviavel = false;
	private Double areaInviavel;

	private List<Produto> listProduto;
	private Produto produtoSelecionado;
	private Produto produtoSelecionadoSupressao;

	private ProdutoSupressao produtoSupressao;
	private ProdutoSupressao produtoSupressaoSelecionado;
	private List<ProdutoSupressao> listProdutoSupressaoSelecionados;
	private List<ProdutoSupressao> listProdutoTemp;

	private String stringVolumeTotalForaApp;
	private String stringVolumeTotalEmApp;
	private String stringVolumeTotal;

	private Double volumeTotalForaApp;
	private Double volumeTotalEmApp;
	private Double volumeTotal;
	private Double conversaoEstereo;
	private Double conversaoMDC;
	
	private MetodoUtil metodoRetornoEspecie;

	/**
	 * Variável temporária para armazenar asvDadosGeraisController.getFceAsv().getNumAreaSupressao();
	 * Área total da Supressão
	 */
	private Double supressaoTEMP;

	/**
	 * Variável temporária para armazenar asvDadosGeraisController.getFceAsv().getNumAreaApp();
	 * Área total de APP a ser Suprimida
	 */
	private Double suprimidaTEMP;

	private boolean imovelUrbano;
	protected boolean blockPorcentagemApp;
	private Empreendimento empreendimento;
	private List<Imovel> imovel;
	private boolean isLenha;
	
	public void init() {
		produtoSelecionadoSupressao = null;
		produtoSelecionado = null;
		super.init();
		iniciarDoubles();
		calcularAreasAppDoRequerimento();
		blockPorcentagemApp = true;
		verificarBlockPorcentagemAPP();
		preencherListas();
		metodoRetornoEspecie = new MetodoUtil(this, "retornoEspecie");
		if(asvDadosGeraisController.isFceSalvo()){
			carregarListasDoBanco();
		}
		carregarEspecieSupressao();
		isLenha = false;
	}
	
	/**
	 * Método chamado para startar as variáveis Double do sistema.
	 * @author eduardo.fernandes
	 */
	private void iniciarDoubles(){
		supressaoTEMP = 0.0;
		suprimidaTEMP = 0.0;
		volumeTotal = 0.0;
		volumeTotalEmApp = 0.0;
		volumeTotalForaApp = 0.0;
	}

	/**
	 * Método usado para carregar as o somátorio de áreas de APPs do requerimento.
	 * @author eduardo.fernandes
	 */
	public void calcularAreasAppDoRequerimento(){
		try {
			areaTotalAppEmpreendimento = 0.0;
			areaTotalAppEmpreendimento = asvSupressaoService.buscarAppByIdeRequerimento(asvDadosGeraisController.getRequerimento());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro001") + "a Área de APP do Empreendimento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Quando o imóvel for urbano, cessionário ou Rural-*inferior a 4 módulos fiscais* e houver supressão em APP,
	 * a porcentagem da área de APP a ser suprimida deverá ser informada pelo requerente
	 * @author eduardo.fernandes
	 */
	public void verificarBlockPorcentagemAPP(){
		try {
			List<Empreendimento> coll = (List<Empreendimento>) empreendimentoService.buscarEmpreendimentoPorRequerimento(asvDadosGeraisController.getRequerimento());
			empreendimento = empreendimentoService.carregarById(coll.get(0).getIdeEmpreendimento());
			if(!Util.isNullOuVazio(empreendimento)){
				imovel = (List<Imovel>) getEmpreendimento().getImovelCollection();
				if(empreendimento.getIndCessionario()){
					blockPorcentagemApp = false;
				} else {
					if(!Util.isNullOuVazio(imovel)){
						for(Imovel im : imovel){
							if(!Util.isNullOuVazio(im.getImovelRural()) && im.getImovelRural().getQtdModuloFiscal() < 4){
								blockPorcentagemApp = false;
							}
							else if (!Util.isNullOuVazio(im.getImovelUrbano())){
								blockPorcentagemApp = false;
								imovelUrbano = true;
							}
						}
					}
				}
			}
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage("Ocorreu um erro ao verificar os imóveis do Empreendimento." + e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}


	/**
	 * Métodos para preenchimento das listas que serão exibidas na tela.
	 * @author eduardo.fernandes
	 */
	public void preencherListas(){
		preencherJustificativas();
		preencherOcorrenciaAreas();
		preencherObjetivosSupressao();
		preencherProdutos();
	}

	/**
	 * Dados da Table(name="justificativa_supressao")
	 * @author eduardo.fernandes
	 */
	private void preencherJustificativas(){
		try {
			if(Util.isNullOuVazio(listaJustificativaSupressao)){
				listaJustificativaSupressao = asvSupressaoService.listarJustificativaSupressao();
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro001") + "Justificativas da Supressão.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Dados da Table(name="ocorrencia_area")
	 * @author eduardo.fernandes
	 */
	private void preencherOcorrenciaAreas(){
		try {
			if(Util.isNullOuVazio(getListaOcorrenciaArea())){
				listaOcorrenciaAreaSelecionadas = new ArrayList<OcorrenciaArea>();
				listaOcorrenciaArea = asvSupressaoService.listarOccorrenciaAreas();
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro001") + "Ocorrência de Áreas.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Dados da Table(name="objetivo_supressao")
	 * @author eduardo.fernandes
	 */
	private void preencherObjetivosSupressao(){
		try {
			if(Util.isNullOuVazio(listaObjetivoSupressao)){
				listaObjetivoSupressaoFilhas = new ArrayList<ObjetivoSupressao>();
				listaObjetivoSupressaoSelecionadas = new ArrayList<ObjetivoSupressao>();
				listaObjetivoSupressaoPaiSelecionados = new ArrayList<ObjetivoSupressao>();
				// Listaremos os ObjetivoSupressao "Pai"
				listaObjetivoSupressao = asvSupressaoService.listarObjetivoSupressao();
			}
			// Listaremos os ObjetivoSupressao "Filhas" de acordo com o ide_objetivo_supressao dos seus pais.
			for(ObjetivoSupressao supressao : listaObjetivoSupressao){
				// Essa lista é genérica, ela recebe TODOS os ObjetivoSupressao "Filhas" para redistribuir os objetos em outras listas.
				listaObjetivoSupressaoFilhas.addAll(asvSupressaoService.listarObjetivoSupressaoFilhaByIdeObjPai(supressao.getIdeObjetoSupressao()));
			}
			preencherListasFilhas();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro001") + "Objetivos da Supressão.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * A 'listaObjetivoSupressaoFilhas' devidamente preenchida que será desmanchada em outras listas menores.
	 * @author eduardo.fernandes
	 */
	private void preencherListasFilhas(){
		iniciarListasFilhas();
		// Lista temporária para permitir a manipulação dos dados.
		List<ObjetivoSupressao> listtemp = new ArrayList<ObjetivoSupressao>();
		listtemp.addAll(listaObjetivoSupressaoFilhas);
		// Corremos a listtemp e buscamos pelos ide_objetivo_supressao_pai de cada ObjetivoSupressao
		for(ObjetivoSupressao objetivoSupressao : listtemp){
			if(objetivoSupressao.getObjetivoSupressao().equals(ObjetivoSupressaoEnum.ATIVIDADE_AGROSSILVOPASTORIL.getId())){
				listObjSupAgro.add(objetivoSupressao);
			}
			else if(objetivoSupressao.getObjetivoSupressao().equals(ObjetivoSupressaoEnum.EMP_INDUSTRIAL.getId())){
				listObjSupEmpInd.add(objetivoSupressao);
			}
			else if(objetivoSupressao.getObjetivoSupressao().equals(ObjetivoSupressaoEnum.EMP_DE_INFRA_E_ENERGIA.getId())){
				listObjSupEmpInfra.add(objetivoSupressao);
			}
			else if(objetivoSupressao.getObjetivoSupressao().equals(ObjetivoSupressaoEnum.EMP_DE_INTERESSE_SOCIAL.getId())){
				listObjSupEmpInterSocial.add(objetivoSupressao);
			}
			else if(objetivoSupressao.getObjetivoSupressao().equals(ObjetivoSupressaoEnum.CONSTRUÇÃO_CIVIL.getId())){
				listObjSupConstrucao.add(objetivoSupressao);
			}
			else if(objetivoSupressao.getObjetivoSupressao().equals(ObjetivoSupressaoEnum.EMP_URBAN_TUR_E_LAZER.getId())){
				listObjSupEmpUrban.add(objetivoSupressao);
			}
			listaObjetivoSupressaoFilhas.remove(objetivoSupressao);
		}
		preencherListaOleoGas();
		preencherListaMineracao();
	}

	/**
	 * Método para startar os ArrayList<ObjetivoSupressao>()
	 * @author eduardo.fernandes
	 */
	private void iniciarListasFilhas(){
		listObjSupAgro = new ArrayList<ObjetivoSupressao>();
		listObjSupOleoGas = new ArrayList<ObjetivoSupressao>();
		listObjSupMiner = new ArrayList<ObjetivoSupressao>();
		listObjSupEmpInd = new ArrayList<ObjetivoSupressao>();
		listObjSupEmpInfra = new ArrayList<ObjetivoSupressao>();
		listObjSupEmpInterSocial = new ArrayList<ObjetivoSupressao>();
		listObjSupConstrucao = new ArrayList<ObjetivoSupressao>();
		listObjSupEmpUrban = new ArrayList<ObjetivoSupressao>();
	}

	/**
	 * Método para carregar o ObjetivoSupressao 'Oléo e Gás' e seus ObjetivoSupressao "Filhas".
	 * @author eduardo.fernandes
	 */
	private void preencherListaOleoGas(){
		try{
			// Precisamos desse objeto pois caso indOleoGas = true precisamos armazená-lo no BD
			listObjSupOleoGas = asvSupressaoService.listarObjetivoSupressaoFilhaByIdeObjPai(ObjetivoSupressaoEnum.OLEO_E_GAS.getId());
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro001") + "Óleo e Gás.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método para carregar o ObjetivoSupressao 'Mineração' e seus ObjetivoSupressao "Filhas".
	 * @author eduardo.fernandes
	 */
	private void preencherListaMineracao(){
		try {
			// Precisamos desse objeto pois caso indMiner = true precisamos armazená-lo no BD
			listObjSupMiner = asvSupressaoService.listarObjetivoSupressaoFilhaByIdeObjPai(ObjetivoSupressaoEnum.MINERACAO.getId());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro001") + "Mineração.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que retorna os Produtos do tipo Supressão
	 * @author eduardo.fernandes
	 */
	private void preencherProdutos() {
		try {
			listProduto = produtoService.carregarListaProdutosByTipoProduto(IDE_TIPO_PRODUTO_SUPRESSAO);
			
			//MODIFICAÇÃO NECESSÁRIA PARA SINAFLOR
			if(!Util.isNullOuVazio(listProduto)) {
				List<Produto> tmp = new ArrayList<Produto>();
				
				for (Produto p : listProduto) {
					if(!p.getDscProduto().equals("Palmito in natura(estirpe)") && !p.getDscProduto().equals("Raízes")) {
						tmp.add(p);
					}
				}
				
				listProduto = tmp;
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro001") + "Produtos da Supressão.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método usado para chamar as informações armazenadas no BD para aquela FCE ASV.
	 * @author eduardo.fernandes
	 */
	public void carregarListasDoBanco(){
		if(!Util.isNullOuVazio(asvDadosGeraisController.getFceAsv()) && !Util.isNullOuVazio(asvDadosGeraisController.getFceAsv().getIdeFceAsv())){
			if(!Util.isNullOuVazio(asvDadosGeraisController.getFceAsv().getIndApp())) {
				supressaoTEMP = asvDadosGeraisController.getFceAsv().getNumAreaSupressao();
				if(asvDadosGeraisController.getFceAsv().getIndApp()){
					// caso indApp, buscaremos as justificativas e preenchemos a variável temporária.
					carregarListaFceAsvJustificativa();
					suprimidaTEMP = asvDadosGeraisController.getFceAsv().getNumAreaApp();
				}
				carregarListaFceAsvObjetivo();
				carregarListaFceAsvOcorrencia();
				if(!Util.isNullOuVazio(asvDadosGeraisController.getFceAsv().getIndMaterialLenhoso()) && asvDadosGeraisController.getFceAsv().getIndMaterialLenhoso()){
					carregarListaProdutoSupressao();
					listProdutoTemp = new ArrayList<ProdutoSupressao>();
					listProdutoTemp.addAll(listProdutoSupressaoSelecionados);
				}
			}

		}
	}

	/**
	 * Método que traz as JustificativaSupressao armazenadas no BD
	 * @author eduardo.fernandes
	 */
	private void carregarListaFceAsvJustificativa(){
		try {
			listaJustificativaSupressaoSelecionadas = asvSupressaoService.buscarListaJustificativaSupressaoByIdeFceAsv(asvDadosGeraisController.getFceAsv());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro001") + " Justificativa da Supressão.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que traz os ObjetivoSupressao armazenados no BD
	 * @author eduardo.fernandes
	 */
	private void carregarListaFceAsvObjetivo(){
		try {
			listaObjetivoSupressaoSelecionadas = asvSupressaoService.buscarListaObjetivoSupressaoByIdeFceAsv(asvDadosGeraisController.getFceAsv());
			tratarObjetivo();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro001") + " Objetivos da Supressão.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}

	}

	/**
	 * Método que vai marcar os como true os booleanos que controlam as listas.
	 * @param lista
	 * @author eduardo.fernandes
	 */
	private void tratarObjetivo() {
		if (!Util.isNullOuVazio(listaObjetivoSupressaoSelecionadas)) {
			for (ObjetivoSupressao objetivoSupressao : listaObjetivoSupressaoSelecionadas) {
				if(Util.isNullOuVazio(objetivoSupressao.getObjetivoSupressao())){
					adicionarNaListaPai(objetivoSupressao);
				} else {
					adicionarNaListaFilha(objetivoSupressao);
				}
			}
			/* 'listaObjetivoSupressaoSelecionadas' recebe todas as lista de ObjetivoSupressao selecionados pelo usuários, tanto a "Pai" quanto as "Filhas".
			 * Depois de redistribuir os ObjetivoSupressao, nós limpamos 'listaObjetivoSupressaoSelecionadas' para evitar duplicação de objetos dentro dela.
			 */
			listaObjetivoSupressaoSelecionadas.clear();
		}
	}

	/**
	 * Método que vai marcar como true os booleanos que renderizam as listas de ObjetivoSupressao "Filhas" e adicionar na 'listaObjetivoSupressaoPaiSelecionados' aquele ObjetivoSupressao passado.
	 * Essa 'listaObjetivoSupressaoPaiSelecionados' é a que será manipulada e salva pelo usuário.
	 * @param objetivoSupressao
	 * @author eduardo.fernandes
	 */
	private void adicionarNaListaPai(ObjetivoSupressao objetivoSupressao){
		if(isNaoContemObjetivo(objetivoSupressao)){
			if(objetivoSupressao.getIdeObjetoSupressao().equals(ObjetivoSupressaoEnum.ATIVIDADE_AGROSSILVOPASTORIL.getId())){
				indAtividadeAgro = true;
			}
			if(objetivoSupressao.getIdeObjetoSupressao().equals(ObjetivoSupressaoEnum.ATIVIDADE_DE_MINERAÇÃO.getId())){
				indAtividadeMiner = true;
			}
			if(objetivoSupressao.getIdeObjetoSupressao().equals(ObjetivoSupressaoEnum.EMP_INDUSTRIAL.getId())){
				indEmpreendIndust = true;
			}
			if(objetivoSupressao.getIdeObjetoSupressao().equals(ObjetivoSupressaoEnum.EMP_DE_INFRA_E_ENERGIA.getId())){
				indEmpreendInfraEner = true;
			}
			if(objetivoSupressao.getIdeObjetoSupressao().equals(ObjetivoSupressaoEnum.EMP_DE_INTERESSE_SOCIAL.getId())){
				indEmpreendInterSocial = true;
			}
			if(objetivoSupressao.getIdeObjetoSupressao().equals(ObjetivoSupressaoEnum.CONSTRUÇÃO_CIVIL.getId())){
				indConstruCivil = true;
			}
			if(objetivoSupressao.getIdeObjetoSupressao().equals(ObjetivoSupressaoEnum.EMP_URBAN_TUR_E_LAZER.getId())){
				indEmpreendUrbanTuristicoLazer = true;
			}
			if(objetivoSupressao.getIdeObjetoSupressao().equals(ObjetivoSupressaoEnum.OUTRO.getId())){
				indOutroEmpreendimento = true;
			}
			listaObjetivoSupressaoPaiSelecionados.add(objetivoSupressao);
		}
	}

	/**
	 * Método que diz se aquele ObjetivoSupressao já está inserido na listaObjetivoSupressaoPaiSelecionados
	 * @param objetivoSupressao
	 * @return true or false
	 * @author eduardo.fernandes
	 */
	private boolean isNaoContemObjetivo(ObjetivoSupressao objetivoSupressao){
		return !listaObjetivoSupressaoPaiSelecionados.contains(objetivoSupressao);
	}

	/**
	 * Quando o ObjetivoSupressao tem ide_objetivo_supressao_pai != null nós precisamos colocá-lo na sua devida sub-lista.
	 * Startamos a sub-lista e adcionamos o ObjetivoSupressa à ela.
	 * @param objetivoSupressao
	 * @author eduardo.fernandes
	 */
	private void adicionarNaListaFilha(ObjetivoSupressao objetivoSupressao){
		if(objetivoSupressao.getObjetivoSupressao().equals(ObjetivoSupressaoEnum.ATIVIDADE_AGROSSILVOPASTORIL.getId())) {
			
			if(Util.isNullOuVazio(listObjSupAgroSelecionados)) {
				listObjSupAgroSelecionados = new ArrayList<ObjetivoSupressao>();
				listObjSupAgroSelecionados.add(objetivoSupressao);
				
			} else if(!listObjSupAgroSelecionados.contains(objetivoSupressao)) {
				listObjSupAgroSelecionados.add(objetivoSupressao);
			}
		} else if (objetivoSupressao.getObjetivoSupressao().equals(ObjetivoSupressaoEnum.OLEO_E_GAS.getId()) 
				|| objetivoSupressao.getIdeObjetoSupressao().equals(ObjetivoSupressaoEnum.OLEO_E_GAS.getId())) {
			
			indOleoGas = true;
			
			if (Util.isNullOuVazio(listObjSupOleoGasSelecionados)) {
				listObjSupOleoGasSelecionados = new ArrayList<ObjetivoSupressao>();
				listObjSupOleoGasSelecionados.add(objetivoSupressao);
				
			} else if(!listObjSupOleoGasSelecionados.contains(objetivoSupressao)) {
				listObjSupOleoGasSelecionados.add(objetivoSupressao);
			}
		} else if (objetivoSupressao.getObjetivoSupressao().equals(ObjetivoSupressaoEnum.MINERACAO.getId()) 
				|| objetivoSupressao.getIdeObjetoSupressao().equals(ObjetivoSupressaoEnum.MINERACAO.getId())) {
			
			indMiner = true;
			
			if (Util.isNullOuVazio(listObjSupMinerSelecionados)) {
				listObjSupMinerSelecionados = new ArrayList<ObjetivoSupressao>();
				listObjSupMinerSelecionados.add(objetivoSupressao);
				
			} else if(!listObjSupMinerSelecionados.contains(objetivoSupressao)) {
				listObjSupMinerSelecionados.add(objetivoSupressao);
			}			
		} else if (objetivoSupressao.getObjetivoSupressao().equals(ObjetivoSupressaoEnum.EMP_INDUSTRIAL.getId())) {
			
			if(Util.isNullOuVazio(listObjSupEmpIndSelecionados)){
				listObjSupEmpIndSelecionados = new ArrayList<ObjetivoSupressao>();
				listObjSupEmpIndSelecionados.add(objetivoSupressao);
				
			} else if(!listObjSupEmpIndSelecionados.contains(objetivoSupressao)) {
				listObjSupEmpIndSelecionados.add(objetivoSupressao);
			}
		} else if (objetivoSupressao.getObjetivoSupressao().equals(ObjetivoSupressaoEnum.EMP_DE_INFRA_E_ENERGIA.getId())) {
			
			if(Util.isNullOuVazio(listObjSupEmpInfraSelecionados)){
				listObjSupEmpInfraSelecionados = new ArrayList<ObjetivoSupressao>();
				listObjSupEmpInfraSelecionados.add(objetivoSupressao);
				
			} else if(!listObjSupEmpInfraSelecionados.contains(objetivoSupressao)) {
				listObjSupEmpInfraSelecionados.add(objetivoSupressao);
			}
		} else if (objetivoSupressao.getObjetivoSupressao().equals(ObjetivoSupressaoEnum.EMP_DE_INTERESSE_SOCIAL.getId())) {
			
			if(Util.isNullOuVazio(listObjSupEmpInterSocialSelecionados)){
				listObjSupEmpInterSocialSelecionados = new ArrayList<ObjetivoSupressao>();
				listObjSupEmpInterSocialSelecionados.add(objetivoSupressao);
				
			} else if(!listObjSupEmpInterSocialSelecionados.contains(objetivoSupressao)) {
				listObjSupEmpInterSocialSelecionados.add(objetivoSupressao);
			}			
		} else if (objetivoSupressao.getObjetivoSupressao().equals(ObjetivoSupressaoEnum.CONSTRUÇÃO_CIVIL.getId())) {
			
			if(Util.isNullOuVazio(listObjSupConstrucaoSelecionados)){
				listObjSupConstrucaoSelecionados = new ArrayList<ObjetivoSupressao>();
				listObjSupConstrucaoSelecionados.add(objetivoSupressao);
				
			} else if(!listObjSupConstrucaoSelecionados.contains(objetivoSupressao)) {
				listObjSupConstrucaoSelecionados.add(objetivoSupressao);
			}			
		} else if (objetivoSupressao.getObjetivoSupressao().equals(ObjetivoSupressaoEnum.EMP_URBAN_TUR_E_LAZER.getId())) {
			
			if(Util.isNullOuVazio(listObjSupEmpUrbanSelecionados)){
				listObjSupEmpUrbanSelecionados = new ArrayList<ObjetivoSupressao>();
				listObjSupEmpUrbanSelecionados.add(objetivoSupressao);
				
			} else if(!listObjSupEmpUrbanSelecionados.contains(objetivoSupressao)) {
				listObjSupEmpUrbanSelecionados.add(objetivoSupressao);
			}			
		} else if (objetivoSupressao.getObjetivoSupressao().equals(ObjetivoSupressaoEnum.OUTRO.getId())) {
			indOutroEmpreendimento = true;
			outroEmpreendimento = objetivoSupressao.getDscObjetoSupressao();
		}
	}

	/**
	 * Método que traz as OcorrenciaArea armazenadas no BD
	 * @author eduardo.fernandes
	 */
	private void carregarListaFceAsvOcorrencia(){
		try {
			listaOcorrenciaAreaSelecionadas = asvSupressaoService.buscarListaOcorrenciaAreaByIdeFceAsv(asvDadosGeraisController.getFceAsv());
			// Como 'listaOcorrenciaAreaSelecionadas' não é um campo obrigatório para 'asvDadosGeraisController.getFceAsv()' devemos verificar se existe algum retorno na consulta ao BD.
			if(!Util.isNullOuVazio(listaOcorrenciaAreaSelecionadas)){
				// 	Precisamos distinguir o tipo de OcorrenciaArea selecionada pelo usuário, por isso temos essa varredura.
				for(OcorrenciaArea ocorrenciaArea : listaOcorrenciaAreaSelecionadas){
					if(ocorrenciaArea.getIdeOcorrenciaArea().equals(OcorrenciaAreaEnum.ABANDONADA.getId())){
						indAbandonada = true;
						areaAbandonada = ocorrenciaArea.getNumArea();
					}
					if(ocorrenciaArea.getIdeOcorrenciaArea().equals(OcorrenciaAreaEnum.INVIAVEL.getId())){
						indInviavel = true;
						areaInviavel = ocorrenciaArea.getNumArea();
					}
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro001") + " Ocorrência de Áreas.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método chamado apenas se asvDadosGeraisController.getFceAsv().getIndMaterialLenhoso() == true para exibir a grid de ProdutoSupressao salva pelo usuário.
	 * @author eduardo.fernandes
	 */
	private void carregarListaProdutoSupressao(){
		try{
			listProdutoSupressaoSelecionados = asvSupressaoService.buscarProdutoSupressao(asvDadosGeraisController.getFceAsv());
			for(ProdutoSupressao produtoSupressao : listProdutoSupressaoSelecionados){
				removerProdutoCombo(produtoSupressao.getProduto());
				if(!Util.isNullOuVazio(asvDadosGeraisController.getFceAsv().getNumAreaSupressao())){
					calcularVolumetTotal(produtoSupressao);
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro001") + " Produto Supressão.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que remove os produtos da 'listProduto' e, consequentemente, do selectOneMenu da asvAbaSupressao.
	 * @param produto
	 * @author eduardo.fernandes
	 */
	public void removerProdutoCombo(Produto produto){
		if(!Util.isNullOuVazio(listProduto)){
			List<Produto> list = new ArrayList<Produto>();
			list.addAll(listProduto);
			if(list.contains(produto)){
				listProduto.remove(produto);
			}
		}
	}

	public void changeIndAPP(ValueChangeEvent event){
		if(!(Boolean) event.getNewValue()){
			asvDadosGeraisController.getFceAsv().setIndApp(false);
			asvDadosGeraisController.getFceAsv().setNumAreaApp(null);
			suprimidaTEMP = 0.0;
			asvDadosGeraisController.getFceAsv().setNumAreaSuprimida(null);
			if(!Util.isNullOuVazio(listaJustificativaSupressaoSelecionadas)){
				listaJustificativaSupressaoSelecionadas.clear();
			}
			if(!listaDeProdutosDesbloqueada()){
				JsfUtil.addWarnMessage(Util.getString("fce_asv_inf017"));
			}
		} else {
			asvDadosGeraisController.getFceAsv().setIndApp(true);
			if(isSemAreaAppNoEmpreendimento() && blockPorcentagemApp){
				JsfUtil.addWarnMessage(Util.getString("fce_asv_inf014"));
				asvDadosGeraisController.getFceAsv().setIndMaterialLenhoso(null);
				anularListaEaddCombo();
			}
		}
		desabilitarGridProdutoSupressao();
	}

	public void limparVolumesConversoes(){
		volumeTotal = 0.0;
		stringVolumeTotal = "";
		volumeTotalEmApp = 0.0;
		stringVolumeTotalEmApp = "";
		volumeTotalForaApp = 0.0;
		stringVolumeTotalForaApp = "";
		conversaoEstereo = 0.0;
		conversaoMDC = 0.0;
	}

	// Métodos do change de OBJETIVO SUPRESSAO
	private void limparListaFilha(List<ObjetivoSupressao> list){
		if(!Util.isNullOuVazio(list)){
			list.clear();
		}
	}

	private void removerPaiSelecionado(ObjetivoSupressao objetivoSupressao){
		if(!Util.isNullOuVazio(listaObjetivoSupressaoPaiSelecionados)){
			listaObjetivoSupressaoPaiSelecionados.remove(objetivoSupressao);
		}
	}

	public void changeAtividadeAgro(){
		ObjetivoSupressao objetivoSupressao = listaObjetivoSupressao.get(ObjetivoSupressaoEnum.ATIVIDADE_AGROSSILVOPASTORIL.getId() - 1);
		if(!indAtividadeAgro){
			indAtividadeAgro = true;
			listaObjetivoSupressaoPaiSelecionados.add(objetivoSupressao);
		}
		else {
			indAtividadeAgro = false;
			limparListaFilha(listObjSupAgroSelecionados);
			removerPaiSelecionado(objetivoSupressao);
		}
	}

	public void changeAtividadeMiner(){
		ObjetivoSupressao objetivoSupressao = listaObjetivoSupressao.get(ObjetivoSupressaoEnum.ATIVIDADE_DE_MINERAÇÃO.getId() - 1);
		if(!indAtividadeMiner){
			indAtividadeMiner = true;
			listaObjetivoSupressaoPaiSelecionados.add(objetivoSupressao);
		}
		else {
			indAtividadeMiner = false;
			indOleoGas = false;
			limparListaFilha(listObjSupOleoGasSelecionados);
			indMiner = false;
			limparListaFilha(listObjSupMinerSelecionados);
			removerPaiSelecionado(objetivoSupressao);
		}
	}

	public void changeOleoGas(){
		if(!indOleoGas){
			indOleoGas = true;
		}
		else {
			indOleoGas = false;
			limparListaFilha(listObjSupOleoGasSelecionados);
		}
	}

	public void changeMiner(){
		if(!indMiner){
			indMiner = true;
		}
		else {
			indMiner = false;
			limparListaFilha(listObjSupMinerSelecionados);
		}
	}

	public void changeEmprendimentoIndustrial(){
		ObjetivoSupressao objetivoSupressao = listaObjetivoSupressao.get(ObjetivoSupressaoEnum.EMP_INDUSTRIAL.getId() - 1);
		if(!indEmpreendIndust){
			indEmpreendIndust = true;
			listaObjetivoSupressaoPaiSelecionados.add(objetivoSupressao);
		}
		else {
			indEmpreendIndust = false;
			limparListaFilha(listObjSupEmpIndSelecionados);
			removerPaiSelecionado(objetivoSupressao);
		}
	}

	public void changeEmprendimentoInfraestrutura(){
		ObjetivoSupressao objetivoSupressao = listaObjetivoSupressao.get(ObjetivoSupressaoEnum.EMP_DE_INFRA_E_ENERGIA.getId() - 1);
		if(!indEmpreendInfraEner){
			indEmpreendInfraEner = true;
			listaObjetivoSupressaoPaiSelecionados.add(objetivoSupressao);
		} else {
			indEmpreendInfraEner = false;
			limparListaFilha(listObjSupEmpInfraSelecionados);
			removerPaiSelecionado(objetivoSupressao);
		}
	}

	public void changeEmprendimentoInteresseSocial(){
		ObjetivoSupressao objetivoSupressao = listaObjetivoSupressao.get(ObjetivoSupressaoEnum.EMP_DE_INTERESSE_SOCIAL.getId() - 1);
		if(!indEmpreendInterSocial){
			indEmpreendInterSocial = true;
			listaObjetivoSupressaoPaiSelecionados.add(objetivoSupressao);
		} else {
			indEmpreendInterSocial = false;
			limparListaFilha(listObjSupEmpInterSocialSelecionados);
			removerPaiSelecionado(objetivoSupressao);
		}
	}

	public void changeConstrucaoCivil(){
		ObjetivoSupressao objetivoSupressao = listaObjetivoSupressao.get(ObjetivoSupressaoEnum.CONSTRUÇÃO_CIVIL.getId() - 1);
		if(indConstruCivil){
			indConstruCivil = false;
			limparListaFilha(listObjSupConstrucaoSelecionados);
			removerPaiSelecionado(objetivoSupressao);
		} else {
			indConstruCivil = true;
			listaObjetivoSupressaoPaiSelecionados.add(objetivoSupressao);
		}
	}

	public void changeEmpreendUrbanTuristicoLazer(){
		ObjetivoSupressao objetivoSupressao = listaObjetivoSupressao.get(ObjetivoSupressaoEnum.EMP_URBAN_TUR_E_LAZER.getId() - 1);
		if(indEmpreendUrbanTuristicoLazer){
			indEmpreendUrbanTuristicoLazer = false;
			limparListaFilha(listObjSupEmpUrbanSelecionados);
			removerPaiSelecionado(objetivoSupressao);
		} else {
			indEmpreendUrbanTuristicoLazer = true;
			listaObjetivoSupressaoPaiSelecionados.add(objetivoSupressao);
		}
	}

	public void changeOutroObjetivo(){
		ObjetivoSupressao objetivoSupressao = listaObjetivoSupressao.get(ObjetivoSupressaoEnum.OUTRO.getId() - 1);
		if(indOutroEmpreendimento){
			indOutroEmpreendimento = false;
			outroEmpreendimento = null;
			removerPaiSelecionado(objetivoSupressao);
		} else {
			indOutroEmpreendimento = true;
		}
	}
	// FIM Métodos do change de OBJETIVO SUPRESSAO

	public void changeAreaAbandonada(ValueChangeEvent event){
		OcorrenciaArea abandonada = listaOcorrenciaArea.get(OcorrenciaAreaEnum.ABANDONADA.getId() - 1);
		if(!(Boolean) event.getNewValue()){
			areaAbandonada = null;
			if(!Util.isNullOuVazio(listaOcorrenciaAreaSelecionadas) && listaOcorrenciaAreaSelecionadas.contains(abandonada)){
				listaOcorrenciaAreaSelecionadas.remove(abandonada);
			}
		} else {
			abandonada.setNumArea(areaAbandonada);
			listaOcorrenciaAreaSelecionadas.add(abandonada);
		}
	}

	public void changeAreaInviavel(ValueChangeEvent event){
		OcorrenciaArea inviavel = listaOcorrenciaArea.get(OcorrenciaAreaEnum.INVIAVEL.getId() - 1);
		if(!(Boolean) event.getNewValue()){
			areaInviavel = null;
			if(!Util.isNullOuVazio(listaOcorrenciaAreaSelecionadas) && listaOcorrenciaAreaSelecionadas.contains(inviavel)){
				listaOcorrenciaAreaSelecionadas.remove(inviavel);
			}
		} else {
			inviavel.setNumArea(areaInviavel);
			listaOcorrenciaAreaSelecionadas.add(inviavel);
		}
	}

	public void changeMaterialLenhoso(ValueChangeEvent event){
		if(!(Boolean) event.getNewValue()){
			anularListaEaddCombo();
		}
	}

	private void anularListaEaddCombo(){
		produtoSelecionado = null;
		if(!Util.isNullOuVazio(listProdutoSupressaoSelecionados)){
			for(ProdutoSupressao produtoSupressao : listProdutoSupressaoSelecionados){
				produtoSupressao.setDestinoSocioeconomicoProdutosSelecionados(new ArrayList<DestinoSocioeconomicoProduto>());
				produtoSupressao.setDesabilitaQtd(false);
				produtoSupressao.setNumVolumeTotal(null);
				adicionarPodutoCombo(produtoSupressao.getProduto());
			}
			listProdutoSupressaoSelecionados.clear();
		}
		limparVolumesConversoes();
	}

	private Collection<DestinoSocioeconomicoProduto> preencherDestinoSocioDosProduto(Produto produto){
		List<DestinoSocioeconomicoProduto> listDestSocioEcoProd = new ArrayList<DestinoSocioeconomicoProduto>();
		try {
			listDestSocioEcoProd = asvSupressaoService.listarDestinoSocioEconomicoProdutoByIdeProduto(produto);
			for(DestinoSocioeconomicoProduto destinoSocioeconomicoProduto : listDestSocioEcoProd){
				destinoSocioeconomicoProduto.setIdeProduto(produto);
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro001") + " Destino Sócio Econômico.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		return listDestSocioEcoProd;
	}

	public void adicionarProdutoSupressao(){
		if(!Util.isNullOuVazio(produtoSelecionado)){
			if(Util.isNullOuVazio(listProdutoSupressaoSelecionados)){
				listProdutoSupressaoSelecionados = new ArrayList<ProdutoSupressao>();
			}
			produtoSupressao = new ProdutoSupressao();
			produtoSupressao.setFceAsv(asvDadosGeraisController.getFceAsv());
			produtoSupressao.setProduto(produtoSelecionado);
			produtoSupressao.setDestinoSocioeconomicoProdutosBD((List<DestinoSocioeconomicoProduto>) preencherDestinoSocioDosProduto(produtoSupressao.getProduto()));
			listProdutoSupressaoSelecionados.add(produtoSupressao);
			removerProdutoCombo(produtoSelecionado);
			produtoSelecionado = null;
		}
		else {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_msg010"));
		}
	}

	public void adicionarPodutoCombo(Produto produto){
		listProduto.add(produto);
	}

	public void removerProdutoSupressaoSelecionado(){
		if(!Util.isNullOuVazio(listProdutoSupressaoSelecionados)){
			List<ProdutoSupressao> list = new ArrayList<ProdutoSupressao>();
			list.addAll(listProdutoSupressaoSelecionados);
			if(list.contains(produtoSupressaoSelecionado)){
				listProdutoSupressaoSelecionados.remove(produtoSupressaoSelecionado);
				JsfUtil.addSuccessMessage(Util.getString("lac_dadosGerais_msg005"));
				volumeTotal = 0.0;
				volumeTotalEmApp = 0.0;
				volumeTotalForaApp = 0.0;
			}
			adicionarPodutoCombo(produtoSupressaoSelecionado.getProduto());
			if(!Util.isNullOuVazio(listProdutoSupressaoSelecionados)){
				for(ProdutoSupressao produto: listProdutoSupressaoSelecionados){
					if(produto.isDesabilitaQtd()){
						calcularVolumetTotal(produto);
					}
				}
			} else {
				limparVolumesConversoes();
			}
		}
		Html.atualizar("asvFormAbaSupressao");
	}

	/**
	 * Método que cálcula o volume total baseado nas informações passadas pelo usuário.
	 * @param produto
	 * @author eduardo.fernandes
	 */
	public void calcularVolumetTotal(ProdutoSupressao produto){
		// Caso tenha área de APP no requerimento, calule.
		if(!isSemAreaAppNoEmpreendimento()){
			calculo(produto);
		}
		// Caso NÃO tenha área de APP no requerimento e o usuário indique isso, calule.
		else if (isSemAreaAppNoEmpreendimento() && (!isSupressaoAPP() || !blockPorcentagemApp)){
			calculo(produto);
		}
	}

	public void modificaStatusProduto(ProdutoSupressao supressao){
		supressao.setDesabilitaQtd(false);
		supressao.setEdicao(true);
	}

	/**
	 *
	 * @param produto
	 * @author eduardo.fernandes
	 */
	private void calculo(ProdutoSupressao produto){
		if(confirmaProduto(produto)){
			if(!isSupressaoAPP()){
				// Caso o usuário tenha selecionado "Não" precisamos setar os valores como 0.0 para evitar NullPointer
				asvDadosGeraisController.getFceAsv().setNumAreaApp(0.0);
				produto.setNumEmApp(0.0);
			}
			Double double1 = 0.0;
			// Calculos realizados de acordo com a RN0037
			if(!Util.isNullOuVazio(produto.getNumForaApp())){
				double1 = (asvDadosGeraisController.getFceAsv().getNumAreaSupressao() - asvDadosGeraisController.getFceAsv().getNumAreaApp()) * produto.getNumForaApp();
			}
			Double double2 = asvDadosGeraisController.getFceAsv().getNumAreaApp() * produto.getNumEmApp();

			// Quando houver alteração de informação na grid deve-se exibir a mensagem abaixo.
			if(!Util.isNullOuVazio(produto.getNumVolumeTotal()) && produto.isEdicao()){
				JsfUtil.addSuccessMessage(Util.getString("lac_dadosGerais_msg002"));
			}
			produto.setNumVolumeTotal(double1 + double2);
			produto.setDesabilitaQtd(true);
			// Caso os produtos daqueala grid estejam "Confirmados" pelo usuário, o sistema deve exibir os somatórios no rodapé das colunas.
			if(!listaDeProdutosDesbloqueada()){
				volumesFooters();
			}
			conversoes();
			double1 = double2 = null;
		} else {
			produto.setNumVolumeTotal(null);
			limparVolumesConversoes();
		}
	}

	/**
	 * Caso os produtos adicionados a grid estejam todos confirmados o método retorna false;
	 * Caso pelo menos UM dos produtos na grid não tenha tido confirmação o método retorna true;
	 * @return true or false
	 * @author eduardo.fernandes
	 */
	private boolean listaDeProdutosDesbloqueada(){
		if(isTemProdutoAdicionado()){
			boolean desbloqueada = false;
			for(ProdutoSupressao produtoSupressao : listProdutoSupressaoSelecionados){
				if(Util.isNullOuVazio(produtoSupressao.getNumVolumeTotal())){
					desbloqueada  = true;
				}
			}
			return desbloqueada;
		} else {
			return true;
		}

	}

	/**
	 * Método que realiza o somatório dos volumes de cada coluna.
	 * @author eduardo.fernandes
	 */
	private void volumesFooters(){
		volumeTotal = 0.0;
		volumeTotalForaApp = 0.0;
		volumeTotalEmApp = 0.0;
		for(ProdutoSupressao produto : listProdutoSupressaoSelecionados){
			volumeTotal  += produto.getNumVolumeTotal();
			volumeTotalEmApp += produto.getNumEmApp();
			if(!Util.isNullOuVazio(produto.getNumForaApp())){
				volumeTotalForaApp += produto.getNumForaApp();
			}
		}
		transformaVolumesString();
	}

	private void conversoes(){
		// Calculos realizados de acordo com a RN0038
		conversaoEstereo = volumeTotal * ESTEREO;
		conversaoMDC = volumeTotal * MDC;
	}

	/**
	 * Método que verifica se o usuário preencheu devidamente as informações na grid.
	 * @param produto
	 * @return true or false
	 */
	private boolean confirmaProduto(ProdutoSupressao produto){
		boolean valido = true;

		if(Util.isNullOuVazio(produto.getNumForaApp()) && !isBlockColunaForaApp()){
			JsfUtil.addWarnMessage("O Volume Médio Fora de APP "+Util.getString("fce_asv_msg011"));
			stringVolumeTotalForaApp = "";
			valido = false;
		} else {
			if(!isTemAreaTotalSupressao()){
				valido = false;
				JsfUtil.addWarnMessage(Util.getString("fce_asv_inf013_volumeForaApp"));
			}
		}

		if(isSupressaoAPP()){
			/*
			 * #6933
			 *
			 if(Util.isNullOuVazio(produto.getNumEmApp())){
				JsfUtil.addWarnMessage("O Volume Médio Em APP "+Util.getString("fce_asv_msg011"));
				stringVolumeTotalEmApp = "";
				valido = false;
			} else*/
			if(!isTemAreaSerSuprimida()){
				JsfUtil.addWarnMessage(Util.getString("fce_asv_inf013_volumeEmApp"));
				valido = false;
			}
		}

		if(Util.isNullOuVazio(produto.getDestinoSocioeconomicoProdutosSelecionados()) || produto.getDestinoSocioeconomicoProdutosSelecionados().isEmpty()){
			JsfUtil.addWarnMessage("O Destino Socioeconômico do Produto " + Util.getString("lac_dadosGerais_msg003"));
			stringVolumeTotal = "";
			valido = false;
		}
		return valido;
	}

	/**
	 * Para exibir o valor no rodapé das colunas da dataTable em asvAbaSupressao precisamos transformar o valor (double) em String.
	 * @author eduardo.fernandes
	 */
	private void transformaVolumesString(){
		DecimalFormat format = new DecimalFormat("##,###,###.##");
		format.setMinimumFractionDigits(2);
		stringVolumeTotal = format.format(volumeTotal);
		stringVolumeTotalEmApp = format.format(volumeTotalEmApp);
		if(!isBlockColunaForaApp()) {
			stringVolumeTotalForaApp = format.format(volumeTotalForaApp);
		}
	}

	private void desabilitarGridProdutoSupressao(){
		if(isTemProdutoAdicionado()){
			limparVolumesConversoes();
			for(ProdutoSupressao produtoSupressao : listProdutoSupressaoSelecionados){
				produtoSupressao.setDesabilitaQtd(false);
				produtoSupressao.setNumVolumeTotal(null);
			}
		}
	}

	public void calcularPorcentagemBySupressao(){
		if(isAreaSupressaoDiferente()){
			if(podeCalcularPorcentagem()){
				if(!listaDeProdutosDesbloqueada()){
					JsfUtil.addWarnMessage(Util.getString("fce_asv_inf017"));
				}
				calcularPocentagem();
			}
			else {
				desabilitarGridProdutoSupressao();
			}
		}
	}

	private boolean podeCalcularPorcentagem(){
		boolean valido = false;
		if(!isSemAreaAppNoEmpreendimento()){
			if(isAreasPreenchidas()) {
				if((asvDadosGeraisController.getFceAsv().getNumAreaApp() <= asvDadosGeraisController.getFceAsv().getNumAreaSupressao()) && (asvDadosGeraisController.getFceAsv().getNumAreaApp() <= areaTotalAppEmpreendimento)){
					valido = true;
					supressaoTEMP = asvDadosGeraisController.getFceAsv().getNumAreaSupressao();
					suprimidaTEMP = asvDadosGeraisController.getFceAsv().getNumAreaApp();
					if(isAreasIguais()){
						anulaColunaForaApp();
					}
				} else if (asvDadosGeraisController.getFceAsv().getNumAreaApp() > asvDadosGeraisController.getFceAsv().getNumAreaSupressao()){
					asvDadosGeraisController.getFceAsv().setNumAreaSuprimida(null);
					JsfUtil.addWarnMessage(Util.getString("fce_asv_inf011"));
				} else if (asvDadosGeraisController.getFceAsv().getNumAreaApp() > areaTotalAppEmpreendimento){
					asvDadosGeraisController.getFceAsv().setNumAreaSuprimida(null);
					JsfUtil.addWarnMessage(Util.getString("fce_asv_inf011_emp"));
				}
			}
			else if(!isTemAreaTotalSupressao()){
				asvDadosGeraisController.getFceAsv().setNumAreaSuprimida(null);
				JsfUtil.addWarnMessage("A área total da supressão "+Util.getString("fce_asv_mg011"));
			}
			else if(isSupressaoAPP() && !isTemAreaSerSuprimida()){
				asvDadosGeraisController.getFceAsv().setNumAreaSuprimida(null);
				JsfUtil.addWarnMessage("A área total de APP a ser suprimida "+Util.getString("fce_asv_mg011"));
			}
		}
		return valido;
	}

	private void anulaColunaForaApp(){
		if(isTemProdutoAdicionado() && isBlockColunaForaApp()){
			for(ProdutoSupressao produto : listProdutoSupressaoSelecionados){
				produto.setNumForaApp(0.0);
			}
		}
	}

	/**
	 * @comentário Cálculos realizados de acordo com RN0032
	 * @author eduardo.fernandes
	 */
	private void calcularPocentagem(){
		asvDadosGeraisController.getFceAsv().setNumAreaSuprimida((asvDadosGeraisController.getFceAsv().getNumAreaApp() * 100) / areaTotalAppEmpreendimento);
		desabilitarGridProdutoSupressao();
	}

	public void calcularPorcentagemBySuprimida(){
		if(isAreaSuprimidaDiferente()){
			if(isTemAreaTotalSupressao()){
				if(!isSemAreaAppNoEmpreendimento() && blockPorcentagemApp) {
					if(podeCalcularPorcentagem()){
						if(!listaDeProdutosDesbloqueada()){
							JsfUtil.addWarnMessage(Util.getString("fce_asv_inf017"));
						}
						calcularPocentagem();
					}
				}
				else if(blockPorcentagemApp){
					asvDadosGeraisController.getFceAsv().setNumAreaSuprimida(null);
					JsfUtil.addWarnMessage(Util.getString("fce_asv_inf014"));
				} else {
					if (asvDadosGeraisController.getFceAsv().getNumAreaApp() > asvDadosGeraisController.getFceAsv().getNumAreaSupressao()){
						asvDadosGeraisController.getFceAsv().setNumAreaSuprimida(null);
						JsfUtil.addWarnMessage(Util.getString("fce_asv_inf011"));
					}
				}
			}
			else {
				asvDadosGeraisController.getFceAsv().setNumAreaSuprimida(null);
				JsfUtil.addWarnMessage(Util.getString("fce_asv_inf012"));
			}
		}
	}

	public void finalizarAbaSupressao() {
		if(asvDadosGeraisController.validarAba()){
			if(validarAbaSupressao()){
				if(asvDadosGeraisController.isFceSalvo()){
					excluirEntidadesAntesDeSalvar();
				}
				if(asvDadosGeraisController.getFceAsv().getIndApp() && (!isSemAreaAppNoEmpreendimento() || !blockPorcentagemApp)){
					salvarFceAsvJustificativaSupressao();
				}
				salvarFceAsvObjetivoSupressao();
				salvarFceAsvOcorrenciaArea();
				salvarFceProdutoSupressao();
				salvarFceEspecie();
				salvarEfecharFceAsv();
			}
		} else {
			asvDadosGeraisController.voltarAba();
		}
	}

	public void salvarFceEspecie() {
		try {
			if(!Util.isNullOuVazio(getListaEspecieSupressaoAutorizacao())){
				asvSupressaoService.salvarListaEspecieSupressaoAutorizacao(getListaEspecieSupressaoAutorizacao());
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro002") + " a lista de especies." + e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private void salvarEfecharFceAsv(){
		try {
			boolean exibeMSGsucess = true;
			asvSupressaoService.salvarFceAsv(asvDadosGeraisController.getFceAsv());
			if(isFinalizarComAviso()){
				JsfUtil.addWarnMessage(Util.getString("fce_asv_inf015"));
				exibeMSGsucess = false;
			}
			if((!asvDadosGeraisController.getFceAsv().getIdeFce().getIdeDadoOrigem().getIdeDadoOrigem().equals(2)) && asvDadosGeraisController.existeOutros()){
				JsfUtil.addWarnMessage(Util.getString("fce_asv_inf016"));
				exibeMSGsucess = false;
			}
			if(exibeMSGsucess){
				JsfUtil.addSuccessMessage(Util.getString("lac_dadosGerais_msg001"));
				
				if(!asvDadosGeraisController.getIsNotFceASV()){
					
					RequestContext.getCurrentInstance().execute("rel_fce_asv.show()");
				}
			}
			RequestContext.getCurrentInstance().execute("fce_asv.hide()");
			asvDadosGeraisController.limpar();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro002") + " a FCE ASV");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public StreamedContent getImprimirRelatorio() throws Exception {
		return asvDadosGeraisController.getImprimirRelatorio();
	}

	protected void excluirEntidadesAntesDeSalvar() {

		asvSupressaoService.excluirObjetivoSupressao(asvDadosGeraisController.getFceAsv());
		if(!Util.isNullOuVazio(listProdutoTemp)){
			for(ProdutoSupressao produtoSupressao : listProdutoTemp){
				excluirProdutoSupressaoDestinoByIdeProdutoSupressao(produtoSupressao);
			}
			excluirProdutoSupressaoByIdeFceAsv();
		}
	}


	private void excluirProdutoSupressaoByIdeFceAsv(){
		try {
			asvSupressaoService.excluirProdutosSupressaoByIdeFceAsv(asvDadosGeraisController.getFceAsv());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro003") + " a lista de Produto Supressão.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void excluirProdutoSupressaoDestinoByIdeProdutoSupressao(ProdutoSupressao produtoSupressao){
		try {
			asvSupressaoService.excluirProdutosSupressaoDestinoByIdeProdutoSupressao(produtoSupressao);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro003") + " a lista de Produto Supressão Destino Socioeconômico.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	protected void salvarFceAsvJustificativaSupressao() {
		try{
			asvSupressaoService.salvarListasComFceAsv(asvDadosGeraisController.getFceAsv(), listaJustificativaSupressaoSelecionadas);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro002") + " a lista de Justificativas da Supressão.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	protected void salvarFceAsvObjetivoSupressao() {
		try{
			if(asvDadosGeraisController.isFceSalvo()){
				listaObjetivoSupressaoSelecionadas.clear();
			}
			preparaSalvarListObjetivoSupressao();
			asvSupressaoService.salvarListasComFceAsv(asvDadosGeraisController.getFceAsv(), listaObjetivoSupressaoSelecionadas);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro002") + " a lista de Objetivos da Supressão.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void preparaSalvarListObjetivoSupressao(){
		try {
			listaObjetivoSupressaoSelecionadas.addAll(listaObjetivoSupressaoPaiSelecionados);
			if(indAtividadeAgro){
				if(!listaObjetivoSupressaoSelecionadas.containsAll(listObjSupAgroSelecionados)) {
					listaObjetivoSupressaoSelecionadas.addAll(listObjSupAgroSelecionados);
				}
			}
			if(indAtividadeMiner){
				if(!listaObjetivoSupressaoSelecionadas.contains(new ObjetivoSupressao(ObjetivoSupressaoEnum.ATIVIDADE_DE_MINERAÇÃO.getId()))){
					listaObjetivoSupressaoSelecionadas.add(listaObjetivoSupressao.get(ObjetivoSupressaoEnum.ATIVIDADE_DE_MINERAÇÃO.getId() - 1));
				}
				if(indOleoGas && !Util.isNullOuVazio(listObjSupOleoGasSelecionados)){
					if(!listaObjetivoSupressaoSelecionadas.containsAll(listObjSupOleoGasSelecionados)) {
						listaObjetivoSupressaoSelecionadas.addAll(listObjSupOleoGasSelecionados);
					}
				}
				if(indMiner && !Util.isNullOuVazio(listObjSupMinerSelecionados)){
					if(!listaObjetivoSupressaoSelecionadas.containsAll(listObjSupMinerSelecionados)) {
						listaObjetivoSupressaoSelecionadas.addAll(listObjSupMinerSelecionados);
					}
				}
			}
			if(indConstruCivil){
				listaObjetivoSupressaoSelecionadas.addAll(listObjSupConstrucaoSelecionados);
			}
			if(indEmpreendIndust){
				listaObjetivoSupressaoSelecionadas.addAll(listObjSupEmpIndSelecionados);
			}
			if(indEmpreendInfraEner){
				listaObjetivoSupressaoSelecionadas.addAll(listObjSupEmpInfraSelecionados);
			}
			if(indEmpreendInterSocial){
				listaObjetivoSupressaoSelecionadas.addAll(listObjSupEmpInterSocialSelecionados);
			}
			if(indEmpreendUrbanTuristicoLazer){
				listaObjetivoSupressaoSelecionadas.addAll(listObjSupEmpUrbanSelecionados);
			}
			if(indOutroEmpreendimento){
				ObjetivoSupressao outro = new ObjetivoSupressao(outroEmpreendimento, ObjetivoSupressaoEnum.OUTRO.getId(), true);
				try{
					asvSupressaoService.salvarObjetivoSupressao(outro);
				} catch (Exception e) {
					JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro002") + "o Objetivo Supressão.");
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				}
				listaObjetivoSupressaoSelecionadas.add(outro);
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro002") + " a lista de Objetivos da Supressão.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}

	}

	protected void salvarFceAsvOcorrenciaArea() {
		try{
			for(OcorrenciaArea ocorrenciaArea : listaOcorrenciaAreaSelecionadas){
				if (ocorrenciaArea.getIdeOcorrenciaArea().equals(OcorrenciaAreaEnum.INVIAVEL.getId())){
					ocorrenciaArea.setNumArea(areaInviavel);
				} else if(ocorrenciaArea.getIdeOcorrenciaArea().equals(OcorrenciaAreaEnum.ABANDONADA.getId())){
					ocorrenciaArea.setNumArea(areaAbandonada);
				}
			}
			asvSupressaoService.salvarListasComFceAsv(asvDadosGeraisController.getFceAsv(), listaOcorrenciaAreaSelecionadas);
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro002") + " a lista de Ocorrência de Áreas.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	protected void salvarFceProdutoSupressao() {
		try {
			if(!Util.isNullOuVazio(listProdutoSupressaoSelecionados)){
				asvSupressaoService.salvarListaProdutoSupressao(listProdutoSupressaoSelecionados, asvDadosGeraisController.getFceAsv());
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro002") + " a lista de Produtos Supressão." + e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}


	public boolean validarAbaSupressao(){
		boolean valido = true;
		if(Util.isNullOuVazio(asvDadosGeraisController.getFceAsv().getNumAreaSupressao())){
			valido = false;
			JsfUtil.addErrorMessage("A área total da supressão " + Util.getString("lac_dadosGerais_msg003"));
		}
		if(Util.isNullOuVazio(isSupressaoAPP())){
			valido = false;
			JsfUtil.addErrorMessage("A declaração da Supressão em Área de Preservação Permanente " + Util.getString("lac_dadosGerais_msg003"));
		} else if(isSupressaoAPP()){
			if(!isSemAreaAppNoEmpreendimento() || !blockPorcentagemApp){
				if(!isTemAreaSerSuprimida()){
					valido = false;
					JsfUtil.addErrorMessage("A área total de APP a ser suprimida " + Util.getString("lac_dadosGerais_msg003"));
				}
				if(Util.isNullOuVazio(asvDadosGeraisController.getFceAsv().getNumAreaSuprimida())){
					valido = false;
					JsfUtil.addErrorMessage("A % de APP a ser suprimida deve ser calculada.");
				} else if(asvDadosGeraisController.getFceAsv().getNumAreaSuprimida() > 100){
					valido = false;
					JsfUtil.addErrorMessage("% de APP a ser suprimida não pode ser superior a 100%.");
				}
				if(Util.isNullOuVazio(listaJustificativaSupressaoSelecionadas)){
					valido = false;
					JsfUtil.addErrorMessage("A seleção da justificativa da supressão em APP " + Util.getString("lac_dadosGerais_msg003"));
				}
			}
		}

		if(!verificaObjetivos()){
			valido = false;
		} else {
			if(indAtividadeAgro){
				if(Util.isNullOuVazio(listObjSupAgroSelecionados)){
					valido = false;
					JsfUtil.addErrorMessage("A seleção do tipo de atividade agrossilvopastoril " + Util.getString("lac_dadosGerais_msg003"));
				}
			}
			if(indAtividadeMiner){
				if(!indMiner && !indOleoGas){
					valido = false;
					JsfUtil.addErrorMessage("A seleção do tipo de atividade de mineração " + Util.getString("lac_dadosGerais_msg003"));
				} else{
					if(indOleoGas){
						if(Util.isNullOuVazio(listObjSupOleoGasSelecionados)){
							valido = false;
							JsfUtil.addErrorMessage(Util.getString("fce_asv_inf020"));
						}
					}
					if (indMiner){
						if(Util.isNullOuVazio(listObjSupMinerSelecionados)){
							valido = false;
							JsfUtil.addErrorMessage(Util.getString("fce_asv_inf021"));
						}
					}
				}
			}
			if(indEmpreendIndust){
				if(Util.isNullOuVazio(listObjSupEmpIndSelecionados)){
					valido = false;
					JsfUtil.addErrorMessage("A seleção do tipo de empeendimento industrial " + Util.getString("lac_dadosGerais_msg003"));
				}
			}
			if(indEmpreendInfraEner){
				if(Util.isNullOuVazio(listObjSupEmpInfraSelecionados)){
					valido = false;
					JsfUtil.addErrorMessage("A seleção do tipo de empeendimento de infraestrutura e energia " + Util.getString("lac_dadosGerais_msg003"));
				}
			}
			if(indEmpreendInterSocial){
				if(Util.isNullOuVazio(getListObjSupEmpInterSocialSelecionados())){
					valido = false;
					JsfUtil.addErrorMessage("A seleção do tipo de empeendimento de interesse social " + Util.getString("lac_dadosGerais_msg003"));
				}
			}
			if(indConstruCivil){
				if(Util.isNullOuVazio(listObjSupConstrucaoSelecionados)){
					valido = false;
					JsfUtil.addErrorMessage("A seleção do tipo de construção civil " + Util.getString("lac_dadosGerais_msg003"));
				}
			}
			if(indEmpreendUrbanTuristicoLazer){
				if(Util.isNullOuVazio(listObjSupEmpUrbanSelecionados)){
					valido = false;
					JsfUtil.addErrorMessage("A seleção do tipo de empreendimento urbanístico, turístico e de lazer " + Util.getString("lac_dadosGerais_msg003"));
				}
			}
			if(indOutroEmpreendimento){
				if(Util.isNullOuVazio(outroEmpreendimento)){
					valido = false;
					JsfUtil.addErrorMessage("O objetivo não especificado " + Util.getString("lac_dadosGerais_msg003"));
				} else if(!validaTamanhoString(outroEmpreendimento, 50)){
					valido = false;
					JsfUtil.addErrorMessage("O nome do objetivo supressão " + Util.getString("fce_asv_mg012"));
				}
			}
		}

		if(indAbandonada && Util.isNullOuVazio(areaAbandonada)){
			valido = false;
			JsfUtil.addErrorMessage("A área abandonada/subutilizada/utilizada de forma inadequada "+Util.getString("fce_asv_msg011"));
		}
		if(indInviavel && Util.isNullOuVazio(areaInviavel)){
			valido = false;
			JsfUtil.addErrorMessage("A área agronomicamente inviável "+Util.getString("fce_asv_msg011"));
		}
		if(!isFinalizarComAviso()){
			if(Util.isNullOuVazio(asvDadosGeraisController.getFceAsv().getIndMaterialLenhoso())){
				if (!Util.isNull(asvDadosGeraisController.getFceAsv().getIndMaterialLenhoso())){
					valido = false;
					JsfUtil.addErrorMessage("A seleção do rendimento de material lenhoso " + Util.getString("lac_dadosGerais_msg003"));
				} else {
					//#9789
					if (Util.isNullOuVazio(getListaEspecieSupressaoAutorizacao())){
						valido = false;
						JsfUtil.addErrorMessage("A seleção de produto / especie " + Util.getString("lac_dadosGerais_msg003"));
					} else {
						for (EspecieSupressaoAutorizacao especieSupressaoAutorizacao : getListaEspecieSupressaoAutorizacao()) {
							if (especieSupressaoAutorizacao.getEdicao()){
								JsfUtil.addErrorMessage("A confirmação da especie " + Util.getString("lac_dadosGerais_msg003"));
								valido = false;
								break;
							}
						}
						
						if(Util.isNullOuVazio(asvDadosGeraisController.getFceAsv().getIndAceite()) || !asvDadosGeraisController.getFceAsv().getIndAceite()){
							valido = false;
							JsfUtil.addErrorMessage("O aceite da especie " + Util.getString("lac_dadosGerais_msg003"));
						}
					}
				}
			} else if(asvDadosGeraisController.getFceAsv().getIndMaterialLenhoso()){
				if(Util.isNullOuVazio(getListProdutoSupressaoSelecionados())){
					valido = false;
					JsfUtil.addErrorMessage("A seleção do produto da supressão " + Util.getString("lac_dadosGerais_msg003"));
				} else {
					if(!verificaProduto(getListProdutoSupressaoSelecionados())){
						valido = false;
					}
					if(Util.isNullOuVazio(asvDadosGeraisController.getFceAsv().getIndAceite()) || !asvDadosGeraisController.getFceAsv().getIndAceite()){
						valido = false;
						JsfUtil.addErrorMessage("O aceite do inventário florestal " + Util.getString("lac_dadosGerais_msg003"));
					}
				}
			} 
		}
		return valido;
	}

	/**
	 * @return boolean
	 * @author eduardo.fernandes
	 * @Commentarios Retorna false se o tamanho da variavel (String) exceder o tamanho especificado
	 */
	public boolean validaTamanhoString(String string, int tamanho){
		boolean valido = true;
		if(string.trim().length() > tamanho){
			valido = false;
		}
		return valido;
	}

	public boolean verificaObjetivos(){
		boolean valido = true;
		if(!indAtividadeAgro && !indAtividadeMiner && !indEmpreendIndust && !indEmpreendInfraEner
				&& !indEmpreendInterSocial && !indEmpreendUrbanTuristicoLazer && !indOutroEmpreendimento && !indConstruCivil){
			valido = false;
			JsfUtil.addErrorMessage("A seleção do objetivo da supressão " + Util.getString("lac_dadosGerais_msg003"));
		}
		return valido;
	}
	
	public String msgImprimirRelatorio(String nomFce) {
		return asvDadosGeraisController.msgImprimirRelatorio(nomFce);
	}

	public boolean verificaProduto(List<ProdutoSupressao> listaProdutoGerado){
		boolean valido = true;

		// #6933
		boolean aceitaZero = listaProdutoGerado.size() > 1;

		for(ProdutoSupressao produto : listaProdutoGerado){
			if(Util.isNullOuVazio(produto.getNumForaApp()) && !isBlockColunaForaApp()){
				valido = false;
				JsfUtil.addErrorMessage("A área fora de app " + Util.getString("lac_dadosGerais_msg003"));
			}

			if(isSupressaoAPP()){
				// #6933
				if(aceitaZero){
					if(Util.isNullOuVazio(volumeTotalEmApp)){
						valido = false;
						JsfUtil.addErrorMessage("O volume total em app " + Util.getString("lac_dadosGerais_msg003"));
					}
				}
				else {
					if(Util.isNullOuVazio(produto.getNumEmApp())){
						valido = false;
						JsfUtil.addErrorMessage("O volume em app " + Util.getString("lac_dadosGerais_msg003"));
					}
				}
			}

			if(Util.isNullOuVazio(produto.getDestinoSocioeconomicoProdutosSelecionados())){
				valido = false;
				JsfUtil.addErrorMessage("A seleção do destino socioeconômico do produto " + Util.getString("lac_dadosGerais_msg003"));
			}
			if(!produto.isDesabilitaQtd()){
				valido = false;
				JsfUtil.addErrorMessage("A confirmação do produto supressão " + Util.getString("lac_dadosGerais_msg003"));
			}
		}
		return valido;
	}

	public void limparAbaAsvSupressao(){
		if(!Util.isNullOuVazio(listaJustificativaSupressaoSelecionadas)){
			listaJustificativaSupressaoSelecionadas.clear();
		}
		if(!Util.isNullOuVazio(listaOcorrenciaAreaSelecionadas)){
			listaOcorrenciaAreaSelecionadas.clear();
		}
		if(!Util.isNullOuVazio(listaObjetivoSupressaoSelecionadas)){
			listaObjetivoSupressaoSelecionadas.clear();
		}
		if(!Util.isNullOuVazio(listaObjetivoSupressaoPaiSelecionados)){
			listaObjetivoSupressaoPaiSelecionados.clear();
		}
		if(!Util.isNullOuVazio(listObjSupAgroSelecionados)){
			listObjSupAgroSelecionados.clear();
		}
		if(!Util.isNullOuVazio(listObjSupOleoGasSelecionados)){
			listObjSupOleoGasSelecionados.clear();
		}
		if(!Util.isNullOuVazio(listObjSupMinerSelecionados)){
			listObjSupMinerSelecionados.clear();
		}
		if(!Util.isNullOuVazio(listObjSupEmpIndSelecionados)){
			listObjSupEmpIndSelecionados.clear();
		}
		if(!Util.isNullOuVazio(listObjSupEmpInfraSelecionados)){
			listObjSupEmpInfraSelecionados.clear();
		}
		if(!Util.isNullOuVazio(listObjSupEmpInterSocialSelecionados)){
			listObjSupEmpInterSocialSelecionados.clear();
		}
		if(!Util.isNullOuVazio(listObjSupConstrucaoSelecionados)){
			listObjSupConstrucaoSelecionados.clear();
		}
		if(!Util.isNullOuVazio(listObjSupEmpUrbanSelecionados)){
			listObjSupEmpUrbanSelecionados.clear();
		}
		if(!Util.isNullOuVazio(asvDadosGeraisController.getListaClassificacaoSelecionada())){
			asvDadosGeraisController.getListaClassificacaoSelecionada().clear();
		}
		indAtividadeAgro = false;
		indAtividadeMiner = false;
		indOleoGas = false;
		indMiner = false;
		indEmpreendIndust = false;
		indEmpreendInfraEner = false;
		indEmpreendInterSocial = false;
		indConstruCivil = false;
		indEmpreendUrbanTuristicoLazer = false;
		indOutroEmpreendimento = false;
		indAbandonada = false;
		areaAbandonada = null;
		indInviavel = false;
		areaInviavel = null;
		produtoSelecionado = null;
		produtoSelecionadoSupressao = null;
		produtoSupressaoSelecionado = null;
		supressaoTEMP = null;
		suprimidaTEMP = null;
		if(isTemProdutoAdicionado()){
			listProdutoSupressaoSelecionados.clear();
		}
		limparVolumesConversoes();
	}
	
	public void carregarEspecieSupressao() {
		try {
			carregarEspecieSupressaoAutorizacao();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void carregarEspecieSupressaoAutorizacao() throws Exception {
		if (!Util.isNullOuVazio(getFceAsv())){
			setListaEspecieSupressaoAutorizacao(asvSupressaoService.listaEspecieSupressaoAutorizacao(getFceAsv()));
			asvSupressaoService.removerListaEspecieSupressaoEdicao(this);
		}
	}

	public void retornoEspecie()  {
		if(verificarSeExisteEspeficacaoLenha() && !Util.isNullOuVazio(getProdutoSelecionadoSupressao()) 
				&& getProdutoSelecionadoSupressao().getDscProduto().equalsIgnoreCase("Lenha")){
			
			JsfUtil.addWarnMessage("O produto selecionado já foi adicionado!");
		}
		if(!verificarSeExisteEspeficacaoLenha() && !Util.isNullOuVazio(getProdutoSelecionadoSupressao()) 
				&& getProdutoSelecionadoSupressao().getDscProduto().equalsIgnoreCase("Lenha")){
			
			if (asvSupressaoService.ajustarRetornoEspecie(this)) {
				Html.atualizar(new String[]{ "tabAbasASVid:asvFormAbaSupressao:gridTableEspecieSupressao", "tabAbasASVid:asvFormAbaSupressao:gridTableEspecie", "tabAbasASVid:asvFormAbaSupressao:produtosSupressao", "asvFormAbaSupressao:gridTableEspecieSupressao", "asvFormAbaSupressao:gridTableEspecie","asvFormAbaSupressao:produtosSupressao"});
			}
		}else if(!Util.isNullOuVazio(getProdutoSelecionadoSupressao()) 
				&& !getProdutoSelecionadoSupressao().getDscProduto().equalsIgnoreCase("Lenha")){
			if (asvSupressaoService.ajustarRetornoEspecie(this)) {
				Html.atualizar(new String[]{ "tabAbasASVid:asvFormAbaSupressao:gridTableEspecieSupressao", "tabAbasASVid:asvFormAbaSupressao:gridTableEspecie", "tabAbasASVid:asvFormAbaSupressao:produtosSupressao", "asvFormAbaSupressao:gridTableEspecieSupressao", "asvFormAbaSupressao:gridTableEspecie","asvFormAbaSupressao:produtosSupressao"});
			}
		}
		
	}
	
	public boolean verificarSeExisteEspeficacaoLenha(){
		
		if(!Util.isNullOuVazio(getListaEspecieSupressaoAutorizacao())){
			
			for(EspecieSupressaoAutorizacao espSupAut :  getListaEspecieSupressaoAutorizacao()){
				
				if(!Util.isNullOuVazio(espSupAut.getIdeProduto())){
					
					if(espSupAut.getIdeProduto().getDscProduto().equalsIgnoreCase("Lenha")){
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public void validarEspecie(EspecieSupressaoAutorizacao especieSupressao) {
		if (Util.isNullOuVazio(especieSupressao.getListaDestinoSocioeconomicoSelecionado())){
			MensagemUtil.msg0003("Destino socioeconêmico do Produto");
			return;
		}
		
		especieSupressao.setEdicao(false);
	}
	
	public void changeProduto(){
		if(!Util.isNullOuVazio(produtoSelecionadoSupressao) && produtoSelecionadoSupressao.getDscProduto().equalsIgnoreCase("Lenha")){
			isLenha = true;
		}else{
			isLenha = false;
		}
	}
	
	// INI booleans controladores
	/**
	 * Método que verifica se há área de APP para aquele empreendimento.
	 * @return true or false
	 * @author eduardo.fernandes
	 */
	public boolean isSemAreaAppNoEmpreendimento(){
		return Util.isNullOuVazio(areaTotalAppEmpreendimento);
	}


	/**
	 *
	 * @return true or false
	 * @author eduardo.fernandes
	 */
	public boolean isFinalizarComAviso(){
		return isSupressaoAPP() && isSemAreaAppNoEmpreendimento() && blockPorcentagemApp && !imovelUrbano;
	}

	/**
	 *
	 * @return true or false
	 * @author eduardo.fernandes
	 */
	public boolean isPodeAdicionar(){
		return !isListProdutoEmpty() && !Util.isNullOuVazio(produtoSelecionado);
	}

	/**
	 *
	 * @return true or false
	 * @author eduardo.fernandes
	 */
	public boolean isListProdutoEmpty(){
		return listProduto.isEmpty();
	}

	/**
	 * Verifica se existe produto adicionado na lista.
	 * @return true or false
	 * @author eduardo.fernandes
	 */
	public boolean isTemProdutoAdicionado(){
		return !Util.isNullOuVazio(listProdutoSupressaoSelecionados);
	}

	/**
	 * Método para verificar o preenchimento da áreas
	 * @return numAreaApp && numAreaSupressao
	 */
	public boolean isAreasPreenchidas(){
		return isTemAreaSerSuprimida() && isTemAreaTotalSupressao();
	}

	/**
	 *
	 * @return true or false
	 * @author eduardo.fernandes
	 */
	public boolean isTemVolumeTotal(){
		return !Util.isNullOuVazio(volumeTotal);
	}

	/**
	 * Verifica se há "Área total de APP a ser suprimida (ha)"
	 *  @return !Util.isNullOuVazio(asvDadosGeraisController.getFceAsv().getNumAreaApp())
	 */
	public boolean isTemAreaSerSuprimida(){
		return !Util.isNullOuVazio(asvDadosGeraisController.getFceAsv().getNumAreaApp());
	}

	/**
	 * Verifica se há "Área total da supressão (ha)"
	 * @return !Util.isNullOuVazio(asvDadosGeraisController.getFceAsv().getNumAreaSupressao())
	 */
	public boolean isTemAreaTotalSupressao(){
		return !Util.isNullOuVazio(asvDadosGeraisController.getFceAsv().getNumAreaSupressao());
	}

	/**
	 * Método para verificar se o usuário marcou a pergunta "Haverá Supressão em Área de Preservação Permanente (APP)?"
	 * @return asvDadosGeraisController.getFceAsv().getIndApp()
	 */
	public boolean isSupressaoAPP(){
		return !Util.isNullOuVazio(asvDadosGeraisController.getFceAsv().getIndApp()) && asvDadosGeraisController.getFceAsv().getIndApp();
	}

	/**
	 *
	 * @return true or false
	 * @author eduardo.fernandes
	 */
	public boolean isOcorrenciaAbandonada(){
		if(!Util.isNullOuVazio(listaOcorrenciaAreaSelecionadas)){
			return listaOcorrenciaAreaSelecionadas.contains(new OcorrenciaArea(1));
		} else {
			return false;
		}
	}

	/**
	 *
	 * @return true or false
	 * @author eduardo.fernandes
	 */
	public boolean isOcorrenciaInviavel(){
		if(!Util.isNullOuVazio(listaOcorrenciaAreaSelecionadas)){
			return listaOcorrenciaAreaSelecionadas.contains(new OcorrenciaArea(2));
		} else {
			return false;
		}
	}

	/**
	 * Verifica se a coluna "Volume Médio Fora de APP" está bloqueada.
	 * @return true or false
	 * @author eduardo.fernandes
	 */
	public boolean isBlockColunaForaApp(){
		return isSupressaoAPP() && isAreasPreenchidas() && isAreasIguais();
	}

	/**
	 * Método que verifica se a "Área Total da Supressão" é igual a "Área de APP a ser Suprimida"
	 * @return true or false
	 * @author eduardo.fernandes
	 */
	private boolean isAreasIguais(){
		return asvDadosGeraisController.getFceAsv().getNumAreaSupressao().equals(asvDadosGeraisController.getFceAsv().getNumAreaApp());
	}

	/**
	 *
	 * @return true or false
	 * @author eduardo.fernandes
	 */
	private boolean isAreaSupressaoDiferente(){
		return !asvDadosGeraisController.getFceAsv().getNumAreaSupressao().equals(supressaoTEMP);
	}

	/**
	 *
	 * @return true or false
	 * @author eduardo.fernandes
	 */
	private boolean isAreaSuprimidaDiferente(){
		return !asvDadosGeraisController.getFceAsv().getNumAreaApp().equals(suprimidaTEMP);
	}

	/**
	 *
	 * FIM booleans controladores
	 **/

	// Getters e Setters
	public Boolean getIndAtividadeAgro() {
		return indAtividadeAgro;
	}

	public void setIndAtividadeAgro(Boolean indAtividadeAgro) {
		this.indAtividadeAgro = indAtividadeAgro;
	}

	public Boolean getIndAtividadeMiner() {
		return indAtividadeMiner;
	}

	public void setIndAtividadeMiner(Boolean indAtividadeMiner) {
		this.indAtividadeMiner = indAtividadeMiner;
	}

	public Boolean getIndEmpreendIndust() {
		return indEmpreendIndust;
	}

	public void setIndEmpreendIndust(Boolean indEmpreendIndust) {
		this.indEmpreendIndust = indEmpreendIndust;
	}

	public Boolean getIndEmpreendInfraEner() {
		return indEmpreendInfraEner;
	}

	public void setIndEmpreendInfraEner(Boolean indEmpreendInfraEner) {
		this.indEmpreendInfraEner = indEmpreendInfraEner;
	}

	public Boolean getIndEmpreendInterSocial() {
		return indEmpreendInterSocial;
	}

	public void setIndEmpreendInterSocial(Boolean indEmpreendInterSocial) {
		this.indEmpreendInterSocial = indEmpreendInterSocial;
	}

	public Boolean getIndConstruCivil() {
		return indConstruCivil;
	}

	public void setIndConstruCivil(Boolean indConstruCivil) {
		this.indConstruCivil = indConstruCivil;
	}

	public Boolean getIndEmpreendUrbanTuristicoLazer() {
		return indEmpreendUrbanTuristicoLazer;
	}

	public void setIndEmpreendUrbanTuristicoLazer(Boolean indEmpreendUrbanTuristicoLazer) {
		this.indEmpreendUrbanTuristicoLazer = indEmpreendUrbanTuristicoLazer;
	}

	public Boolean getIndOutroEmpreendimento() {
		return indOutroEmpreendimento;
	}

	public void setIndOutroEmpreendimento(Boolean indOutroEmpreendimento) {
		this.indOutroEmpreendimento = indOutroEmpreendimento;
	}

	public Boolean getIndOleoGas() {
		return indOleoGas;
	}

	public void setIndOleoGas(Boolean indOleoGas) {
		this.indOleoGas = indOleoGas;
	}

	public Boolean getIndMiner() {
		return indMiner;
	}

	public void setIndMiner(Boolean indMiner) {
		this.indMiner = indMiner;
	}

	public String getOutroEmpreendimento() {
		return outroEmpreendimento;
	}

	public void setOutroEmpreendimento(String outroEmpreendimento) {
		this.outroEmpreendimento = outroEmpreendimento;
	}

	public Double getConversaoEstereo() {
		return conversaoEstereo;
	}

	public void setConversaoEstereo(Double conversaoEstereo) {
		this.conversaoEstereo = conversaoEstereo;
	}

	public Double getConversaoMDC() {
		return conversaoMDC;
	}

	public void setConversaoMDC(Double conversaoMDC) {
		this.conversaoMDC = conversaoMDC;
	}

	public Double getVolumeTotal() {
		return volumeTotal;
	}

	public void setVolumeTotal(Double volumeTotal) {
		this.volumeTotal = volumeTotal;
	}

	public Double getVolumeTotalForaApp() {
		return volumeTotalForaApp;
	}

	public void setVolumeTotalForaApp(Double volumeTotalForaApp) {
		this.volumeTotalForaApp = volumeTotalForaApp;
	}

	public Double getVolumeTotalEmApp() {
		return volumeTotalEmApp;
	}

	public void setVolumeTotalEmApp(Double volumeTotalEmApp) {
		this.volumeTotalEmApp = volumeTotalEmApp;
	}

	public String getStringVolumeTotalForaApp() {
		return stringVolumeTotalForaApp;
	}

	public void setStringVolumeTotalForaApp(String stringVolumeTotalForaApp) {
		this.stringVolumeTotalForaApp = stringVolumeTotalForaApp;
	}

	public String getStringVolumeTotalEmApp() {
		return stringVolumeTotalEmApp;
	}

	public void setStringVolumeTotalEmApp(String stringVolumeTotalEmApp) {
		this.stringVolumeTotalEmApp = stringVolumeTotalEmApp;
	}

	public String getStringVolumeTotal() {
		return stringVolumeTotal;
	}

	public void setStringVolumeTotal(String stringVolumeTotal) {
		this.stringVolumeTotal = stringVolumeTotal;
	}

	// codificação 30/08/13
	// getters and setters
	public FceAsv getFceAsv() {
		return asvDadosGeraisController.getFceAsv();
	}

	public List<JustificativaSupressao> getListaJustificativaSupressao() {
		return listaJustificativaSupressao;
	}

	public void setListaJustificativaSupressao(List<JustificativaSupressao> listaJustificativaSupressao) {
		this.listaJustificativaSupressao = listaJustificativaSupressao;
	}

	public List<JustificativaSupressao> getListaJustificativaSupressaoSelecionadas() {
		return listaJustificativaSupressaoSelecionadas;
	}

	public void setListaJustificativaSupressaoSelecionadas(List<JustificativaSupressao> listaJustificativaSupressaoSelecionadas) {
		this.listaJustificativaSupressaoSelecionadas = listaJustificativaSupressaoSelecionadas;
	}

	public List<OcorrenciaArea> getListaOcorrenciaArea() {
		return listaOcorrenciaArea;
	}

	public void setListaOcorrenciaArea(List<OcorrenciaArea> listaOcorrenciaArea) {
		this.listaOcorrenciaArea = listaOcorrenciaArea;
	}

	public List<OcorrenciaArea> getListaOcorrenciaAreaSelecionadas() {
		return listaOcorrenciaAreaSelecionadas;
	}

	public void setListaOcorrenciaAreaSelecionadas(List<OcorrenciaArea> listaOcorrenciaAreaSelecionadas) {
		this.listaOcorrenciaAreaSelecionadas = listaOcorrenciaAreaSelecionadas;
	}

	public List<ObjetivoSupressao> getListaObjetivoSupressao() {
		return listaObjetivoSupressao;
	}

	public void setListaObjetivoSupressao(List<ObjetivoSupressao> listaObjetivoSupressao) {
		this.listaObjetivoSupressao = listaObjetivoSupressao;
	}

	public List<ObjetivoSupressao> getListaObjetivoSupressaoSelecionadas() {
		return listaObjetivoSupressaoSelecionadas;
	}

	public void setListaObjetivoSupressaoSelecionadas(List<ObjetivoSupressao> listaObjetivoSupressaoSelecionadas) {
		this.listaObjetivoSupressaoSelecionadas = listaObjetivoSupressaoSelecionadas;
	}

	public List<ObjetivoSupressao> getListaObjetivoSupressaoPaiSelecionados() {
		return listaObjetivoSupressaoPaiSelecionados;
	}

	public void setListaObjetivoSupressaoPaiSelecionados(List<ObjetivoSupressao> listaObjetivoSupressaoPaiSelecionados) {
		this.listaObjetivoSupressaoPaiSelecionados = listaObjetivoSupressaoPaiSelecionados;
	}

	public List<ObjetivoSupressao> getListaObjetivoSupressaoFilhas() {
		return listaObjetivoSupressaoFilhas;
	}

	public void setListaObjetivoSupressaoFilhas(List<ObjetivoSupressao> listaObjetivoSupressaoFilhas) {
		this.listaObjetivoSupressaoFilhas = listaObjetivoSupressaoFilhas;
	}

	public Double getAreaAbandonada() {
		return areaAbandonada;
	}

	public void setAreaAbandonada(Double areaAbandonada) {
		this.areaAbandonada = areaAbandonada;
	}

	public Double getAreaInviavel() {
		return areaInviavel;
	}

	public void setAreaInviavel(Double areaInviavel) {
		this.areaInviavel = areaInviavel;
	}

	public Boolean getIndAbandonada() {
		return indAbandonada;
	}

	public void setIndAbandonada(Boolean indAbandonada) {
		this.indAbandonada = indAbandonada;
	}

	public Boolean getIndInviavel() {
		return indInviavel;
	}

	public void setIndInviavel(Boolean indInviavel) {
		this.indInviavel = indInviavel;
	}

	public List<ObjetivoSupressao> getListObjSupAgro() {
		return listObjSupAgro;
	}

	public void setListObjSupAgro(List<ObjetivoSupressao> listObjSupAgro) {
		this.listObjSupAgro = listObjSupAgro;
	}

	public List<ObjetivoSupressao> getListObjSupAgroSelecionados() {
		return listObjSupAgroSelecionados;
	}

	public void setListObjSupAgroSelecionados(List<ObjetivoSupressao> listObjSupAgroSelecionados) {
		this.listObjSupAgroSelecionados = listObjSupAgroSelecionados;
	}

	public List<ObjetivoSupressao> getListObjSupMiner() {
		return listObjSupMiner;
	}

	public void setListObjSupMiner(List<ObjetivoSupressao> listObjSupMiner) {
		this.listObjSupMiner = listObjSupMiner;
	}

	public List<ObjetivoSupressao> getListObjSupOleoGas() {
		return listObjSupOleoGas;
	}

	public void setListObjSupOleoGas(List<ObjetivoSupressao> listObjSupOleoGas) {
		this.listObjSupOleoGas = listObjSupOleoGas;
	}

	public List<ObjetivoSupressao> getListObjSupOleoGasSelecionados() {
		return listObjSupOleoGasSelecionados;
	}

	public void setListObjSupOleoGasSelecionados(List<ObjetivoSupressao> listObjSupOleoGasSelecionados) {
		this.listObjSupOleoGasSelecionados = listObjSupOleoGasSelecionados;
	}

	public List<ObjetivoSupressao> getListObjSupMinerSelecionados() {
		return listObjSupMinerSelecionados;
	}

	public void setListObjSupMinerSelecionados(List<ObjetivoSupressao> listObjSupMinerSelecionados) {
		this.listObjSupMinerSelecionados = listObjSupMinerSelecionados;
	}

	public List<ObjetivoSupressao> getListObjSupEmpInd() {
		return listObjSupEmpInd;
	}

	public void setListObjSupEmpInd(List<ObjetivoSupressao> listObjSupEmpInd) {
		this.listObjSupEmpInd = listObjSupEmpInd;
	}

	public List<ObjetivoSupressao> getListObjSupEmpIndSelecionados() {
		return listObjSupEmpIndSelecionados;
	}

	public void setListObjSupEmpIndSelecionados(List<ObjetivoSupressao> listObjSupEmpIndSelecionados) {
		this.listObjSupEmpIndSelecionados = listObjSupEmpIndSelecionados;
	}

	public List<ObjetivoSupressao> getListObjSupEmpInfra() {
		return listObjSupEmpInfra;
	}

	public void setListObjSupEmpInfra(List<ObjetivoSupressao> listObjSupEmpInfra) {
		this.listObjSupEmpInfra = listObjSupEmpInfra;
	}

	public List<ObjetivoSupressao> getListObjSupEmpInfraSelecionados() {
		return listObjSupEmpInfraSelecionados;
	}

	public void setListObjSupEmpInfraSelecionados(List<ObjetivoSupressao> listObjSupEmpInfraSelecionados) {
		this.listObjSupEmpInfraSelecionados = listObjSupEmpInfraSelecionados;
	}

	public List<ObjetivoSupressao> getListObjSupEmpInterSocial() {
		return listObjSupEmpInterSocial;
	}

	public void setListObjSupEmpInterSocial(List<ObjetivoSupressao> listObjSupEmpInterSocial) {
		this.listObjSupEmpInterSocial = listObjSupEmpInterSocial;
	}

	public List<ObjetivoSupressao> getListObjSupConstrucaoSelecionados() {
		return listObjSupConstrucaoSelecionados;
	}

	public void setListObjSupConstrucaoSelecionados(List<ObjetivoSupressao> listObjSupConstrucaoSelecionados) {
		this.listObjSupConstrucaoSelecionados = listObjSupConstrucaoSelecionados;
	}

	public List<ObjetivoSupressao> getListObjSupConstrucao() {
		return listObjSupConstrucao;
	}

	public void setListObjSupConstrucao(List<ObjetivoSupressao> listObjSupConstrucao) {
		this.listObjSupConstrucao = listObjSupConstrucao;
	}

	public List<ObjetivoSupressao> getListObjSupEmpInterSocialSelecionados() {
		return listObjSupEmpInterSocialSelecionados;
	}

	public void setListObjSupEmpInterSocialSelecionados(List<ObjetivoSupressao> listObjSupEmpInterSocialSelecionados) {
		this.listObjSupEmpInterSocialSelecionados = listObjSupEmpInterSocialSelecionados;
	}

	public List<ObjetivoSupressao> getListObjSupEmpUrban() {
		return listObjSupEmpUrban;
	}

	public void setListObjSupEmpUrban(List<ObjetivoSupressao> listObjSupEmpUrban) {
		this.listObjSupEmpUrban = listObjSupEmpUrban;
	}

	public List<ObjetivoSupressao> getListObjSupEmpUrbanSelecionados() {
		return listObjSupEmpUrbanSelecionados;
	}

	public void setListObjSupEmpUrbanSelecionados(List<ObjetivoSupressao> listObjSupEmpUrbanSelecionados) {
		this.listObjSupEmpUrbanSelecionados = listObjSupEmpUrbanSelecionados;
	}

	public List<Produto> getListProduto() {
		return listProduto;
	}

	public void setListProduto(List<Produto> listProduto) {
		this.listProduto = listProduto;
	}

	public Produto getProdutoSelecionado() {
		return produtoSelecionado;
	}

	public void setProdutoSelecionado(Produto produtoSelecionado) {
		this.produtoSelecionado = produtoSelecionado;
	}

	public ProdutoSupressao getProdutoSupressao() {
		return produtoSupressao;
	}

	public void setProdutoSupressao(ProdutoSupressao produtoSupressao) {
		this.produtoSupressao = produtoSupressao;
	}

	public List<ProdutoSupressao> getListProdutoSupressaoSelecionados() {
		return listProdutoSupressaoSelecionados;
	}

	public void setListProdutoSupressaoSelecionados(List<ProdutoSupressao> listProdutoSupressaoSelecionados) {
		this.listProdutoSupressaoSelecionados = listProdutoSupressaoSelecionados;
	}

	public ProdutoSupressao getProdutoSupressaoSelecionado() {
		return produtoSupressaoSelecionado;
	}

	public void setProdutoSupressaoSelecionado(ProdutoSupressao produtoSupressaoSelecionado) {
		this.produtoSupressaoSelecionado = produtoSupressaoSelecionado;
	}

	public List<ProdutoSupressao> getListProdutoTemp() {
		return listProdutoTemp;
	}

	public void setListProdutoTemp(List<ProdutoSupressao> listProdutoTemp) {
		this.listProdutoTemp = listProdutoTemp;
	}

	public boolean isBlockPorcentagemApp() {
		return blockPorcentagemApp;
	}

	public void setBlockPorcentagemApp(boolean blockPorcentagemApp) {
		this.blockPorcentagemApp = blockPorcentagemApp;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}

	public List<Imovel> getImovel() {
		return imovel;
	}

	public void setImovel(List<Imovel> imovel) {
		this.imovel = imovel;
	}

	public MetodoUtil getMetodoRetornoEspecie() {
		return metodoRetornoEspecie;
	}

	public void setMetodoRetornoEspecie(MetodoUtil metodoRetornoEspecie) {
		this.metodoRetornoEspecie = metodoRetornoEspecie;
	}

	public Produto getProdutoSelecionadoSupressao() {
		return produtoSelecionadoSupressao;
	}

	public void setProdutoSelecionadoSupressao(Produto produtoSelecionadoSupressao) {
		this.produtoSelecionadoSupressao = produtoSelecionadoSupressao;
	}

	public boolean isLenha() {
		return isLenha;
	}

	public void setLenha(boolean isLenha) {
		this.isLenha = isLenha;
	}
	
}