package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.hibernate.LazyInitializationException;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import com.vividsolutions.jts.geom.Point;

import br.gov.ba.seia.entity.ClassificacaoSecaoGeometrica;
import br.gov.ba.seia.entity.CoordenadaGeografica;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.ObjetivoLimpezaArea;
import br.gov.ba.seia.entity.ObjetivoRequerimentoLimpezaArea;
import br.gov.ba.seia.entity.ParamPersistDadoGeo;
import br.gov.ba.seia.entity.Pergunta;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.RequerimentoImovel;
import br.gov.ba.seia.entity.SistemaCoordenada;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.ConfigEnum;
import br.gov.ba.seia.enumerator.PerguntaEnum;
import br.gov.ba.seia.enumerator.TipoLocalizacaoGeograficaPerguntaEnum;
import br.gov.ba.seia.enumerator.URLEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.ClassificacaoSecaoGeometricaService;
import br.gov.ba.seia.service.DatumService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.ImovelRuralService;
import br.gov.ba.seia.service.ImovelService;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.service.ParamPersistDadoGeoService;
import br.gov.ba.seia.service.RequerimentoUnicoService;
import br.gov.ba.seia.service.VerticeService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.GeoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.PostgisUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

@Named("localizacaoGeograficaReqUnicoController")
@ViewScoped
public class LocalizacaoGeograficaReqUnicoController extends SeiaControllerAb {

	private ImovelRural imovelRural;
	private Collection<SistemaCoordenada> datums;
	private SistemaCoordenada datumSelecionado;
	private ClassificacaoSecaoGeometrica secaoGeometricaSelecionada;
	private List<ClassificacaoSecaoGeometrica> itemsClassifSecGeometrica;
	private Boolean pnlVerticesHabilitado;
	private Boolean uplShapefile;
	private LocalizacaoGeografica localizacaoGeograficaSelecionada;
	private Empreendimento empreendimento;
	
	private Boolean existeTheGeom = new Boolean(false);

	private String selectedModoCoordenada = "1";
	private String grausLatitude;
	private String minutosLatitude;
	private String segundosLatitude;
	private String grausLongitude;
	private String minutosLongitude;
	private String segundosLongitude;
	private String fracaoGrauLatitude;
	private String fracaoGrauLongitude;
	private String fracaoGrauLatitudeDecimal;
	private String fracaoGrauLongitudeDecimal;
	private DadoGeografico vertice;
	private DadoGeografico verticeExclusao;
	private String caminhoArquivo;
	private List<String> listaArquivo;
	private StreamedContent arquivoBaixar;
	private String arqDeExclusao;
	private Boolean disableClassificacoes = false;
	private ObjetivoRequerimentoLimpezaArea objReqLimpezaAreaSelecionado;
	private ObjetivoLimpezaArea objLimpezaAreaSelecionado;
	private List<ObjetivoLimpezaArea> listObjLimpezaArea;
	private Imovel imovel;
	private RequerimentoImovel reqImovelRural;
	
	private BigDecimal valorDaAreaTemp;
	
	public BigDecimal getValorDaAreaTemp() {
		return valorDaAreaTemp;
	}

	public void setValorDaAreaTemp(BigDecimal valorDaAreaTemp) {
		this.valorDaAreaTemp = valorDaAreaTemp;
	}

	@EJB
	private RequerimentoUnicoService requerimentoUnicoService;
	
	private boolean editandoVertice = false;
	private boolean desabilitarVertices = true;
	private Boolean mostraLista = false;
	private Boolean temArquivo;
	private List<ObjetivoRequerimentoLimpezaArea> listObjReqLimpArea;
	private List<LocalizacaoGeografica> listLocGeo;
	private PerguntaRequerimento perguntaRequerimentoLocGeoReqUnico;
	private Pergunta perguntaSalvar;
	
	private Boolean habilitaLinkVisualizaShapeFile;
	
	public Boolean getHabilitaLinkVisualizaShapeFile() {
		return habilitaLinkVisualizaShapeFile;
	}

	public void setHabilitaLinkVisualizaShapeFile(Boolean habilitaLinkVisualizaShapeFile) {
		this.habilitaLinkVisualizaShapeFile = habilitaLinkVisualizaShapeFile;
	}
	
	@EJB
	private ImovelService imovelService;
	@EJB
	private VerticeService verticeService;
	@EJB
	private DatumService serviceDatum;
	@EJB
	private ClassificacaoSecaoGeometricaService serviceClassifSecGeometrica;
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	@EJB
	private EmpreendimentoService empreendimentoService;
	@EJB
	private ParamPersistDadoGeoService paramPersistDadoGeoService;
	@EJB
	private ImovelRuralService imovelRuralService;
	private boolean disableParaVisualizar;

	@PostConstruct
	public void init() {
		this.pnlVerticesHabilitado = false;
		this.uplShapefile = false;
		this.empreendimento = (Empreendimento) getAtributoSession("EMPREENDIMENTO_SESSAO");
		this.imovelRural = (ImovelRural) getAtributoSession("IMOVEL_RURAL");
		
		Exception erro = null;
		
		try {
			listObjLimpezaArea = requerimentoUnicoService.listTodosObjetivoLimpezaArea();
			if (!Util.isNull(empreendimento)) {
				empreendimento = empreendimentoService.carregarComLocalizacaoGeografica(empreendimento);
				if (!Util.isNull(empreendimento.getIdeLocalizacaoGeografica())) {
					this.localizacaoGeograficaSelecionada = empreendimento.getIdeLocalizacaoGeografica();
					if (Util.isNull(this.localizacaoGeograficaSelecionada)) {
						this.localizacaoGeograficaSelecionada = new LocalizacaoGeografica();
					}
					desabilitarVertices = false;
					pnlVerticesHabilitado = empreendimento.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao().equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId());
					uplShapefile = empreendimento.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao().equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId());
					
					if(localizacaoGeograficaSelecionada.getIdeClassificacaoSecao().getIdeClassificacaoSecao().equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId())
							&& getExisteTheGeom()
					){
						habilitaLinkVisualizaShapeFile = true;
					}else{
						habilitaLinkVisualizaShapeFile = false;
					}
				} else {
					this.localizacaoGeograficaSelecionada = new LocalizacaoGeografica();
					empreendimento.setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
					desabilitarVertices = true;
				}
			} else {
				this.localizacaoGeograficaSelecionada = new LocalizacaoGeografica();
				desabilitarVertices = true;
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		this.obterDatums();
		this.obterClassificacoes();
		vertice = new DadoGeografico();
		temArquivo = false;
		listaArquivo = new ArrayList<String>();
		listLocGeo = new ArrayList<LocalizacaoGeografica>();
	}

	public void carregarTela() {

	}
	/**
	 * @author micael.coutinho
	 * Executa os comandos necessários para salvar uma localização geografica de requerimento Imóvel.
	 */
	public void salvarLocalizacaoGeografica() {
		
		Exception erro = null;
		
		try { 
			System.out.println();
			this.localizacaoGeograficaSelecionada.setDtcCriacao(new Date());
			this.localizacaoGeograficaSelecionada.setDtcExclusao(null);
			this.localizacaoGeograficaSelecionada.setIndExcluido(Boolean.FALSE);
			RequerimentoImovel reqImovelSalvo;
			reqImovelSalvo = (RequerimentoImovel) localizacaoGeograficaService.salvarOuAtualizarLocalizacaoGeografica(localizacaoGeograficaSelecionada, ContextoUtil.getContexto().getObjectToLocGeo(), objLimpezaAreaSelecionado);
			desabilitarVertices = false;
			ContextoUtil.getContexto().setPergunta(perguntaSalvar);
			atualizarListadeObjetivoReqLimpArea(reqImovelSalvo);
			atualizaCondicionantesDeRenders();
			reqImovelRural = (RequerimentoImovel) reqImovelSalvo.clone();
			if(Util.isNullOuVazio(ContextoUtil.getContexto().getPergunta().getListRequerimentoImovel()))
				ContextoUtil.getContexto().getPergunta().setListRequerimentoImovel(new ArrayList<RequerimentoImovel>());
//			listLocGeo.add(localizacaoGeograficaSelecionada);
			listaArquivo.clear();
			if(!Util.isNull(reqImovelRural.getPerguntaRequerimento().getIdePergunta().getListObjReqLimpArea()) && !Util.isNull(ContextoUtil.getContexto().getObjReqLimpArea())){
				if(!reqImovelRural.getPerguntaRequerimento().getIdePergunta().getListObjReqLimpArea().contains(ContextoUtil.getContexto().getObjReqLimpArea()))
					reqImovelRural.getPerguntaRequerimento().getIdePergunta().getListObjReqLimpArea().add((ObjetivoRequerimentoLimpezaArea) ContextoUtil.getContexto().getObjReqLimpArea().clone());
			}
			JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso.");
			ContextoUtil.getContexto().getPergunta().getListRequerimentoImovel().add(reqImovelRural);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}
	
	public void desabilitarCamposParaVisualizar(){
		this.disableParaVisualizar = true;
		Exception erro = null;
		
		try {
			listObjLimpezaArea = requerimentoUnicoService.listTodosObjetivoLimpezaArea();
			if(!Util.isNullOuVazio(localizacaoGeograficaSelecionada) && !Util.isNullOuVazio(localizacaoGeograficaSelecionada.getIdeSistemaCoordenada()) && !Util.isNullOuVazio(localizacaoGeograficaSelecionada.getDadoGeograficoCollection())){
				if (localizacaoGeograficaSelecionada.getIdeClassificacaoSecao().getIdeClassificacaoSecao().equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId())) {
					pnlVerticesHabilitado = true;
					uplShapefile = false;
				}else{
					pnlVerticesHabilitado = false;
					uplShapefile = true;
				}
			}
		} catch (LazyInitializationException e) {
			if (localizacaoGeograficaSelecionada.getIdeClassificacaoSecao().getIdeClassificacaoSecao().equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId())) {
				pnlVerticesHabilitado = true;
				uplShapefile = false;
			}else{
				pnlVerticesHabilitado = false;
				uplShapefile = true;
			}
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			JsfUtil.addErrorMessage("Não foi possível carregar os Dados geográficos.");
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}
	
	public void limparLocalizacaoGeografSelecionada(){
		if(!Util.isNullOuVazio(perguntaRequerimentoLocGeoReqUnico))
			perguntaRequerimentoLocGeoReqUnico.setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
		
		ContextoUtil.getContexto().setPerguntaReq(perguntaRequerimentoLocGeoReqUnico);
		localizacaoGeograficaSelecionada = null;
		localizacaoGeograficaSelecionada = new LocalizacaoGeografica();
		this.disableParaVisualizar = false;
	}
	
	public void repoeObjetivoLimpezaAreaNaLista(){
		if(!Util.isNullOuVazio(objLimpezaAreaSelecionado)){
//			if(!listObjLimpezaArea.contains(objLimpezaAreaSelecionado)){
//				listObjLimpezaArea.add(objLimpezaAreaSelecionado);
//			}
			objLimpezaAreaSelecionado = null;
		}
		localizacaoGeograficaSelecionada.defineIdeLocalizacaoGeograficaComoNula();
	}
	
	public String getNomeSistemaCoordenadas(SistemaCoordenada sc){
		if(Util.isNullOuVazio(sc)){
			return "";
		}else{
			if(sc.getIdeSistemaCoordenada() == 1)
				return "Geográfica SAD69";
			else if(sc.getIdeSistemaCoordenada() == 4)
				return "Geográfica SIRGAS2000";
			else
				return "";
		}
	}

	public Boolean getLocalizacaoGeografSelecionadaNaoVazio(){
		if(Util.isNullOuVazio(localizacaoGeograficaSelecionada))
			return false;
		else
			return true;
	}
	
	public Boolean getLocalizacaoGeografSelecTemDadoGeografico(){
		
		Exception erro = null;
		try {
			if(!Util.isNullOuVazio(localizacaoGeograficaSelecionada) && !Util.isNullOuVazio(localizacaoGeograficaSelecionada.getDadoGeograficoCollection()) || (Util.isNullOuVazio(habilitaLinkVisualizaShapeFile) && habilitaLinkVisualizaShapeFile))
				return true;
			else
				return false;
		}
		catch (LazyInitializationException e) {
			if(!Util.isNullOuVazio(habilitaLinkVisualizaShapeFile) && habilitaLinkVisualizaShapeFile){
				return true;
			}else{
				return false;
			}
		}catch (NullPointerException e) {
			if(!Util.isNullOuVazio(habilitaLinkVisualizaShapeFile) && habilitaLinkVisualizaShapeFile){
				return true;
			}else{
				return false;
			}
		}catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return false;
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}
	
	/**
	 * 
	 */
	private void atualizaCondicionantesDeRenders() {
		if(!Util.isNullOuVazio(localizacaoGeograficaSelecionada) && !Util.isNullOuVazio(localizacaoGeograficaSelecionada.getIdeClassificacaoSecao()) && 
				localizacaoGeograficaSelecionada.getIdeClassificacaoSecao().getIdeClassificacaoSecao() == ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId()){
			pnlVerticesHabilitado = true;
			uplShapefile = false;
		}else{
			pnlVerticesHabilitado = false;
			uplShapefile = true;
		}
	}

	public void verificarObjetivosParaImovel(ImovelRural imovel,Pergunta pergunta){
		
		Exception erro = null;
		
		try {
			listObjLimpezaArea = requerimentoUnicoService.listTodosObjetivoLimpezaArea();
		
			for (ObjetivoRequerimentoLimpezaArea objetivoRequerimentoLimpezaArea : pergunta.getListObjReqLimpArea() ) {
				if(this.imovelService.getImovelByObjetivoRequerimentoLimpezaArea(objetivoRequerimentoLimpezaArea,imovel.getIdeImovelRural()).getIdeImovel().equals(imovel.getIdeImovelRural())){					
					this.listObjLimpezaArea.remove(objetivoRequerimentoLimpezaArea.getIdeObjetivoLimpezaArea());
				}
			}
			
		} catch (Exception e) {
			erro=e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}
	
	/**
	 * @param reqImovelSalvo
	 * @throws CloneNotSupportedException 
	 */
	private void atualizarListadeObjetivoReqLimpArea(RequerimentoImovel reqImovelSalvo) throws CloneNotSupportedException {
		
		if(Util.isNullOuVazio(listObjReqLimpArea))
			listObjReqLimpArea = new ArrayList<ObjetivoRequerimentoLimpezaArea>();
		if(!Util.isNull(reqImovelSalvo.getObjetivoRequerimentoLimpezaAreaCollection()) && !reqImovelSalvo.getObjetivoRequerimentoLimpezaAreaCollection().isEmpty() ){
			if(perguntaSalvar.getCodPergunta().equals(PerguntaEnum.PERGUNTA_PRODUCAO_VOLUMETRICA_DE_MADEIRA.getCod()) && !perguntaSalvar.getResposta()){
				if(!listObjReqLimpArea.containsAll(reqImovelSalvo.getObjetivoRequerimentoLimpezaAreaCollection())){
					Boolean teste = new Boolean(true);
					for (ObjetivoRequerimentoLimpezaArea objReq : reqImovelSalvo.getObjetivoRequerimentoLimpezaAreaCollection()) {
						teste = true;
						if(!Util.isNullOuVazio(this.perguntaSalvar.getListObjReqLimpArea())){
							for (ObjetivoRequerimentoLimpezaArea objReqLimp2 : this.perguntaSalvar.getListObjReqLimpArea()) {
								if(objReq.getIdeObjetivoLimpezaArea().getIdeObjetivoLimpezaArea().equals(objReqLimp2.getIdeObjetivoLimpezaArea().getIdeObjetivoLimpezaArea())
										&& objReq.getIdeRequerimentoImovel().getImovel().equals(objReqLimp2.getIdeRequerimentoImovel().getImovel())){
									teste = false;
									break;
								}
							}
						}
						if(teste){
							listObjReqLimpArea.add((ObjetivoRequerimentoLimpezaArea)objReq.clone());
						}
					}
				}	
			}
		}
		ContextoUtil.getContexto().setListaObjetReqLimpArea(listObjReqLimpArea);
	}
	
	public String criarUrlShape(String nulo) {
    	StringBuilder lUrl = new StringBuilder();
    	if(!Util.isNullOuVazio(localizacaoGeograficaSelecionada) && !Util.isNullOuVazio(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica()))
    		lUrl.append(URLEnum.CAMINHO_GEOBAHIA + "?acao=view&idloc=" + localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica() + "&tipo=1");

		StringBuilder lReturn = new StringBuilder();
		lReturn.append("window.open('");
		lReturn.append(lUrl.toString());
		lReturn.append("');");
		return lReturn.toString();
    }

	public void salvarVertice() {
		calculaFracaoGrauLatitude(null);
		calculaFracaoGrauLongitude(null);
		
		Exception erro = null;
		try {
			try {
				if(!localizacaoGeograficaService.validaVerticeMunicipio(this.fracaoGrauLatitude, this.fracaoGrauLongitude, empreendimento, null)){
					JsfUtil.addWarnMessage("Coordenada informada está fora dos limites da Localidade do empreendimento.");
				}
			}catch (Exception e) {
				System.out.println("LocalizacaoGeograficaController.salvarVertice() - Não foi possível validar coordenada. CAUSA DO ERRO: "+e+" VERIFIQUE A CAUSA DO PROBLEMA.");
			}
			if(Util.isNullOuVazio(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica())){
				this.localizacaoGeograficaSelecionada.setDtcCriacao(new Date());
				this.localizacaoGeograficaService.salvarlocalizacao(localizacaoGeograficaSelecionada);
			}
			if(selectedModoCoordenada.equalsIgnoreCase("2")){
				if(!Util.isNullOuVazio(this.fracaoGrauLatitudeDecimal) && !Util.isNullOuVazio(this.fracaoGrauLongitudeDecimal)) {
					persisteVertice("-"+this.fracaoGrauLatitudeDecimal, "-"+this.fracaoGrauLongitudeDecimal);
				} else {
					RequestContext.getCurrentInstance().addCallbackParam("validationFailed", true);
				}
			}else if(!Util.isNullOuVazio(this.fracaoGrauLatitude) && !Util.isNullOuVazio(this.fracaoGrauLongitude)) {
				persisteVertice(this.fracaoGrauLatitude, this.fracaoGrauLongitude);
				ContextoUtil.getContexto().setLocalizacaoSalvaNoReq(localizacaoGeograficaSelecionada);
				ContextoUtil.getContexto().setPergunta(perguntaSalvar);
			} else {
				RequestContext.getCurrentInstance().addCallbackParam("validationFailed", true);
			}
		} catch (NumberFormatException e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			System.out.println("LocalizacaoGeograficaController.salvarVertice() if(IBGE) Problemas no coordGeobahiaMunicipio");
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	/**
	 * 
	 */
	private void persisteVertice(String fracaoGrauLatitude, String fracaoGrauLongitude) {
		if (editandoVertice == true) {
			
			try {
				Point ponto = PostgisUtil.criarPonto(Double.parseDouble(fracaoGrauLatitude.replace(',', '.')),
						Double.parseDouble(fracaoGrauLongitude.replace(',', '.')));
				vertice.setCoordGeoNumerica(ponto.toString());
				vertice.setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
				verticeService.salvarOuAtualizarVertice(vertice);
				Integer idDosParametros = paramPersistDadoGeoService.salvarParamsEPersistindoVerticeTheGeom(vertice, localizacaoGeograficaSelecionada.getIdeSistemaCoordenada());
				if(Util.isNullOuVazio(idDosParametros))
					System.out.println("ERRO AO SALVAR PARAMETROS");
				else
					System.out.println("PARAMETROS persistido com sucesso!");
				JsfUtil.addSuccessMessage("Alteração realizada com sucesso!");
				editandoVertice = false;
				limparVertice();
				RequestContext.getCurrentInstance().execute("dialogoIncluirVertice.hide()");
			} catch (Exception e) {
				JsfUtil.addErrorMessage(e.getMessage());
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		} else {
			try {
				Point ponto = PostgisUtil.criarPonto(Double.parseDouble(fracaoGrauLatitude.replace(',', '.')),
						Double.parseDouble(fracaoGrauLongitude.replace(',', '.')));
				vertice.setCoordGeoNumerica(ponto.toString());
				vertice.setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
				verticeService.salvarVertice(vertice);
				
				Integer idDosParametros = paramPersistDadoGeoService.salvarParamsEPersistindoVerticeTheGeom(vertice, localizacaoGeograficaSelecionada.getIdeSistemaCoordenada());
				if(Util.isNullOuVazio(idDosParametros))
					System.out.println("ERRO AO SALVAR PARAMETROS");
				else
					System.out.println("PARAMETROS persistido com sucesso!");
				
				if (Util.isNull(localizacaoGeograficaSelecionada.getDadoGeograficoCollection())) {
					localizacaoGeograficaSelecionada.setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
				}
				localizacaoGeograficaSelecionada.getDadoGeograficoCollection().add(vertice);
				JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso!");
				limparVertice();
				RequestContext.getCurrentInstance().execute("dialogoIncluirVertice.hide()");
			} catch (Exception e) {
				JsfUtil.addErrorMessage(e.getMessage());
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}

	public void excluirVertice() {
		try {
			verticeService.excluir(verticeExclusao);
			localizacaoGeograficaSelecionada.getDadoGeograficoCollection().remove(verticeExclusao);
			JsfUtil.addSuccessMessage("Exclusão efetuada com sucesso!");
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Carrega o vertice para edi��o, imprescind�vel que a vari�vel vertice
	 * esteja preenchida.
	 * 
	 * @author micael.coutinho
	 */
	public void carregarVertice() {
		selectedModoCoordenada = "1";
		
		try {
			CoordenadaGeografica coordenadaGeografica = GeoUtil.converterPointParaCoordenadaGeografica(vertice.getCoordGeoNumerica());
			
			grausLatitude = coordenadaGeografica.getLatitude().getGrau().toString();
			minutosLatitude = coordenadaGeografica.getLatitude().getMinuto().toString();
			segundosLatitude = coordenadaGeografica.getLatitude().getSegundo().toString();
			
			grausLongitude = coordenadaGeografica.getLongitude().getGrau().toString();
			minutosLongitude = coordenadaGeografica.getLongitude().getMinuto().toString();
			segundosLongitude = coordenadaGeografica.getLongitude().getSegundo().toString();
			
			fracaoGrauLatitude = coordenadaGeografica.getLatitude().getAsGD();
			fracaoGrauLatitudeDecimal = coordenadaGeografica.getLongitude().getAsGD();
			
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void limparVertice() {
		selectedModoCoordenada = "1";
		vertice = new DadoGeografico();
		grausLatitude = "";
		minutosLatitude = "";
		segundosLatitude = "";
		grausLongitude = "";
		minutosLongitude = "";
		segundosLongitude = "";
		fracaoGrauLatitude = "";
		fracaoGrauLongitude = "";
		fracaoGrauLatitudeDecimal = "";
		fracaoGrauLongitudeDecimal= "";
	}

	public double getLongitude(DadoGeografico pVertice) {
		if (pVertice.getCoordGeoNumerica().length() > 0 && pVertice.getCoordGeoNumerica().indexOf("(") > 0
				&& pVertice.getCoordGeoNumerica().lastIndexOf(")") > 0) {
			String value = pVertice.getCoordGeoNumerica().substring(pVertice.getCoordGeoNumerica().indexOf("(") + 1,
					pVertice.getCoordGeoNumerica().lastIndexOf(")"));
			String[] v = value.split(" ");
			if (v.length == 2) {
				try {
					return Double.parseDouble(v[1]);
				} 
				catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					throw Util.capturarException(e,Util.SEIA_EXCEPTION);
				}
			}
		}
		return 0;
	}

	public float getLatitude(DadoGeografico pVertice) {
		if (pVertice.getCoordGeoNumerica().length() > 0 && pVertice.getCoordGeoNumerica().indexOf("(") > 0
				&& pVertice.getCoordGeoNumerica().lastIndexOf(")") > 0) {
			String value = pVertice.getCoordGeoNumerica().substring(pVertice.getCoordGeoNumerica().indexOf("(") + 1,
					pVertice.getCoordGeoNumerica().lastIndexOf(")"));
			String[] v = value.split(" ");
			if (v.length == 2) {
				try {
					return Float.parseFloat(v[0]);
				} 
				catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
				}
			}
		}
		return 0;
	}

	public String getUrl(DadoGeografico pVertice) {
		String lUrl = new String();
		lUrl = "window.open('" + URLEnum.CAMINHO_GEOBAHIA + "?acao=view&idloc=" + pVertice.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica() + "&tipo=1');";
		
		return lUrl;
	}

	public void calculaFracaoGrauLatitude(ActionEvent event) {
		if (grausLatitude != null) {
			fracaoGrauLatitude = GeoUtil.converterGMSParaDecimal(GeoUtil.criarCoordenada(grausLatitude, minutosLatitude, segundosLatitude));
		}
	}

	public void calculaFracaoGrauLongitude(ActionEvent event) {
		if (grausLongitude != null) {
			fracaoGrauLongitude = GeoUtil.converterGMSParaDecimal(GeoUtil.criarCoordenada(grausLongitude, minutosLongitude, segundosLongitude));
		}
	}

	public void selectedSecGeometricaChanged(ValueChangeEvent event) throws AbortProcessingException {
		try {
			String value = null;
			if (event.getNewValue() != null) {
				value = event.getNewValue().toString();
			} else {
				pnlVerticesHabilitado = false;
				uplShapefile = false;
			}
			if (value != null && !value.isEmpty()) {
				for (ClassificacaoSecaoGeometrica itemClassifSecGeometrica : itemsClassifSecGeometrica) {
					String newValue = itemClassifSecGeometrica.getIdeClassificacaoSecao().toString();
					if (value.equals(newValue) && itemClassifSecGeometrica.getNomClassificacaoSecao().toLowerCase().contains("shapefile")) {
						uplShapefile = true;
						pnlVerticesHabilitado = false;
						break;
					} else if (value.equals(newValue) && !itemClassifSecGeometrica.getNomClassificacaoSecao().equals("Selecione...")) {
						pnlVerticesHabilitado = false;
						uplShapefile = false;
						break;
					}
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(e.getMessage());
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}

	public void obterDatums() {
		
		try {
			datums = serviceDatum.listarDatum();
			String quaisNaoAparecerao = ContextoUtil.getContexto().getQuaisSecGeometricas();
			if(!Util.isNullOuVazio(quaisNaoAparecerao) && quaisNaoAparecerao.equalsIgnoreCase("somentePonto")){
				datums.remove(new SistemaCoordenada(2, "UTM 23 SAD69", "29193"));
				datums.remove(new SistemaCoordenada(3, "UTM 24 SAD69", "29194"));
				datums.remove(new SistemaCoordenada(5, "UTM 23 SIRGAS 2000", "31983"));
				datums.remove(new SistemaCoordenada(6, "UTM 24 SIRGAS 2000", "31984"));
			}
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void obterDatums(String quaisNaoAparecerao) {
		
		try {
			datums = serviceDatum.listarDatum();
			if(!Util.isNullOuVazio(quaisNaoAparecerao) && quaisNaoAparecerao.equalsIgnoreCase("somentePonto")){
				datums.remove(new SistemaCoordenada(2, "UTM 23 SAD69", "29193"));
				datums.remove(new SistemaCoordenada(3, "UTM 24 SAD69", "29194"));
				datums.remove(new SistemaCoordenada(5, "UTM 23 SIRGAS 2000", "31983"));
				datums.remove(new SistemaCoordenada(6, "UTM 24 SIRGAS 2000", "31984"));
			}
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void obterClassificacoes() {
		try {
			listObjLimpezaArea = requerimentoUnicoService.listTodosObjetivoLimpezaArea();
			this.listObjReqLimpArea = ContextoUtil.getContexto().getListaObjetReqLimpArea();
			this.localizacaoGeograficaSelecionada.setIdeLocalizacaoGeografica(null);
			this.localizacaoGeograficaSelecionada.setIdeClassificacaoSecao(new ClassificacaoSecaoGeometrica());
			this.localizacaoGeograficaSelecionada.setIdeSistemaCoordenada(null);
			this.localizacaoGeograficaSelecionada.setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
			this.localizacaoGeograficaSelecionada.setParamPersistDadoGeoCollection(new ArrayList<ParamPersistDadoGeo>());
			existeTheGeom = null;			
			pnlVerticesHabilitado = false;
			uplShapefile = false;
			listaArquivo = null;
			listaArquivo = new ArrayList<String>();
			limparInformacoesDeLocGeografica();
			itemsClassifSecGeometrica = serviceClassifSecGeometrica.listarClassificacaoSecaoGeometrica();
			//removendo a opção de desenho
			itemsClassifSecGeometrica.remove(2);
			selecionarExibicaoDaSecaoGeografica();
			//setar o imovel do requerimentoImovel no caso de limpeza de área, pois não é possível fazer isso no controler de requerimento unico devido esse metodo ter que ser chamado no action que vem depois dos setParams
			if(!Util.isNullOuVazio(ContextoUtil.getContexto().getObjectToLocGeo()) && ContextoUtil.getContexto().getObjectToLocGeo().getClass() == RequerimentoImovel.class){
				RequerimentoImovel reqTemp = (RequerimentoImovel) ContextoUtil.getContexto().getObjectToLocGeo();
				if(Util.isNullOuVazio(imovelRural)){
					if(!Util.isNullOuVazio(imovel)){
						reqTemp.setImovel(new Imovel(this.imovel.getIdeImovel()));
					}else if(!Util.isNullOuVazio(empreendimento) && !Util.isNullOuVazio(empreendimento.getImovelCollection())){
						reqTemp.setImovel(this.empreendimento.getImovelCollection().iterator().next());
					}
				}else{
					reqTemp.setImovel(new Imovel(imovelRural.getIdeImovelRural()));
					reqTemp.getImovel().setImovelRural(imovelRural);
				}
				if(!Util.isNullOuVazio(objReqLimpezaAreaSelecionado)){
					objReqLimpezaAreaSelecionado.setIdeRequerimentoImovel(null);
					objReqLimpezaAreaSelecionado.setIdeRequerimento(reqTemp.getRequerimento());
					ContextoUtil.getContexto().setObjReqLimpArea(objReqLimpezaAreaSelecionado);
					reqTemp.getObjetivoRequerimentoLimpezaAreaCollection().add(objReqLimpezaAreaSelecionado);
				}else{
					if(!Util.isNullOuVazio(ContextoUtil.getContexto().getObjReqLimpArea()))
						ContextoUtil.getContexto().getObjReqLimpArea().setIdeObjetivoLimpezaArea(null);
					reqTemp.setObjetivoRequerimentoLimpezaAreaCollection(null);
				}
				
				if(!Util.isNullOuVazio(perguntaSalvar) && !Util.isNull(perguntaSalvar.getListObjReqLimpArea()) && perguntaSalvar.getCodPergunta().equals(PerguntaEnum.PERGUNTA_PRODUCAO_VOLUMETRICA_DE_MADEIRA.getCod())){
					listObjReqLimpArea = perguntaSalvar.getListObjReqLimpArea();
					ContextoUtil.getContexto().setListaObjetReqLimpArea(listObjReqLimpArea);
				}
				
				reqTemp.getPerguntaRequerimento().setIdePergunta(perguntaSalvar);
				if(!Util.isNullOuVazio(perguntaSalvar)){
					reqTemp.getPerguntaRequerimento().setIndResposta(perguntaSalvar.getResposta());
					if(perguntaSalvar.getCodPergunta().equals(PerguntaEnum.PERGUNTA_PRODUCAO_VOLUMETRICA_DE_MADEIRA.getCod()) && !perguntaSalvar.getResposta() && Util.isNull(listObjLimpezaArea)){//se não haverá produç vol de madeira, carregar lista de objetivos de area
						listObjLimpezaArea = requerimentoUnicoService.listTodosObjetivoLimpezaArea();
						localizacaoGeograficaSelecionada.setVlrArea(null);
						localizacaoGeograficaSelecionada.setIdeSistemaCoordenada(null);
						localizacaoGeograficaSelecionada.setDesLocalizacaoGeografica(null);
						localizacaoGeograficaSelecionada.setIdeLocalizacaoGeografica(null);
					}
					localizacaoGeograficaSelecionada.setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
				}
				if(disableParaVisualizar){
					listObjLimpezaArea = requerimentoUnicoService.listTodosObjetivoLimpezaArea();
				}
				ContextoUtil.getContexto().setObjectToLocGeo(reqTemp);
//				atualizarListaObjetivoLimpezaArea();
				ContextoUtil.getContexto().setObjReqLimpArea(null);
				
			}
			
			
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@SuppressWarnings("unused")
	private void atualizarListaObjetivoLimpezaArea() {
		if(!Util.isNullOuVazio(perguntaSalvar) && !Util.isNullOuVazio(perguntaSalvar.getCodPergunta()) && perguntaSalvar.getCodPergunta().equals(PerguntaEnum.PERGUNTA_PRODUCAO_VOLUMETRICA_DE_MADEIRA.getCod()) && !perguntaSalvar.getResposta()){
			if(!Util.isNullOuVazio(listObjLimpezaArea)){
				try {
					
					for (ObjetivoRequerimentoLimpezaArea objRLimpArea : Util.clonaListaObjReqLimpArea(perguntaSalvar.getListObjReqLimpArea())) {
							listObjLimpezaArea.remove(objRLimpArea.getIdeObjetivoLimpezaArea());
					}
				} catch (CloneNotSupportedException e) {
					
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				}
			}
		}
	}

	/**
	 * 
	 */
	private void selecionarExibicaoDaSecaoGeografica() {
		if(!Util.isNullOuVazio(perguntaSalvar) && !Util.isNullOuVazio(perguntaSalvar.getIdePergunta()) ){
			if(perguntaSalvar.getIndLocalizacaoGeografica() == TipoLocalizacaoGeograficaPerguntaEnum.SOMENTE_PONTO.getIde().intValue() ){
				definirItemsDaClassifSecGeometrica(1);
				obterDatums("somentePonto");
			}else if(perguntaSalvar.getIndLocalizacaoGeografica() == TipoLocalizacaoGeograficaPerguntaEnum.SOMENTE_SHAPE.getIde().intValue()){
				definirItemsDaClassifSecGeometrica(0);
				obterDatums();
			}else if(perguntaSalvar.getIndLocalizacaoGeografica() == TipoLocalizacaoGeograficaPerguntaEnum.SE_SIM_SHAPE_SE_NAO_PONTOS.getIde().intValue()){
				if(perguntaSalvar.getResposta()){
					definirItemsDaClassifSecGeometrica(0);
					obterDatums();
				}else{
					definirItemsDaClassifSecGeometrica(1);
					obterDatums("somentePonto");
				}
			}else if(perguntaSalvar.getIndLocalizacaoGeografica() == TipoLocalizacaoGeograficaPerguntaEnum.COM_LOCALIZACAO_QUALQUER.getIde().intValue()){
				definirItemsDaClassifSecGeometrica(null);
				obterDatums();
			}else{
				this.localizacaoGeograficaSelecionada.setIdeClassificacaoSecao(null);
				disableClassificacoes = false;
				obterDatums();
			}
			ContextoUtil.getContexto().setQuaisSecGeometricas(null);
		}
	}

	/**
	 * 
	 */
	private void definirItemsDaClassifSecGeometrica(Integer indiceSerRemovido) {
		if(!Util.isNull(indiceSerRemovido)){
			if(indiceSerRemovido==1)
				this.localizacaoGeograficaSelecionada.setIdeClassificacaoSecao(itemsClassifSecGeometrica.get(0));
			else
				this.localizacaoGeograficaSelecionada.setIdeClassificacaoSecao(itemsClassifSecGeometrica.get(1));
			disableClassificacoes = true;
		}else{
			disableClassificacoes = false;
		}
	}

	/**
	 * 
	 */
	private void limparInformacoesDeLocGeografica() {
		localizacaoGeograficaSelecionada.setVlrArea(null);
		localizacaoGeograficaSelecionada.setDesLocalizacaoGeografica(null);
		localizacaoGeograficaSelecionada.setIdeSistemaCoordenada(null);
	}

	public void trataArquivo(FileUploadEvent event) {
		if(listaArquivo.size()<3){
			if(!Util.isNullOuVazio(localizacaoGeograficaSelecionada) && !Util.isNullOuVazio(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica())){  
				
				if((!Util.isNullOuVazio(localizacaoGeograficaSelecionada.getIdeSistemaCoordenada()) && Util.isNullOuVazio(localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().getSrid())) || 
						Util.isNullOuVazio(localizacaoGeograficaSelecionada.getIdeSistemaCoordenada())){
					Exception erro = null;
					try {
						localizacaoGeograficaSelecionada = localizacaoGeograficaService.carregarSomenteComSistemaCoordenada(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica());
					} catch (Exception e) {
						erro =e;
						Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
					}finally{
						if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
					}
					
				}
				
				caminhoArquivo = FileUploadUtil.EnviarShape(event, DiretorioArquivoEnum.SHAPEFILES.toString()+FileUploadUtil.getFileSeparator()+localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica().toString(),
						localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica().toString()+"_"+localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().getSrid());
				listaArquivo.add(FileUploadUtil.getFileName(caminhoArquivo));
				temArquivo = true;
				JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
			}else
				JsfUtil.addWarnMessage("Para inserir os shapes é preciso salvar a Localização Geográfica!");
		}
		else
			JsfUtil.addWarnMessage("Não é possível enviar mais de 3 arquivos.");
	}
	
	public Boolean getDesabilitaSalvarShape(){
		boolean fShp, fDbf, fShx;
		fShp = false;	fDbf = false;	fShx = false;
		
		if(!listaArquivo.isEmpty()){
			for (String arquivo : listaArquivo) {
				String teste[] = new String[2];
				teste = arquivo.split("\\.");
				if(teste[1].equals("shp") || teste[1].equals("SHP")){
					fShp = true;
				}else if(teste[1].equals("dbf") || teste[1].equals("DBF")){
					fDbf = true;
				}else if(teste[1].equals("shx") || teste[1].equals("SHX")){
					fShx = true;
				}else{
					break;
				}
			}
		}
		
		if (fShp && fDbf && fShx) {
			return false;
		}else
			return true;
		
	}
	
	public void persistirShapes(){
		String[] retorno = new String[0];
		Double codIBGE = new Double(0);
		this.imovel = this.reqImovelRural.getImovel();
		
		try {
			localizacaoGeograficaSelecionada = localizacaoGeograficaService.carregarSomenteComSistemaCoordenada(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica());
			localizacaoGeograficaSelecionada.setVlrArea(ContextoUtil.getContexto().getLocalizacaoSalvaNoReq().getVlrArea());
			try {
				if(!Util.isNullOuVazio(this.imovelRural) && !Util.isNullOuVazio(this.imovelRural.getImovel()))
					codIBGE = this.imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio();
				else
					codIBGE = this.imovel.getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio();
				retorno = paramPersistDadoGeoService.salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, false, codIBGE, false,1);
			} catch (LazyInitializationException e) {
				
				System.out.println("INFO - Imovel Sem Endereço - devido a LazyInitializationException - e agora tentando carrregar endereço pelo imóvel");
				codIBGE = carregandoEnderecoByIdeImovel(codIBGE);
				retorno = paramPersistDadoGeoService.salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, false, codIBGE, false,1);					
			}catch (NullPointerException e) {
				
				System.out.println("INFO - Imovel Sem Endereço - devido a NullPointerException - e agora tentando carrregar endereço pelo imóvel");
				codIBGE = carregandoEnderecoByIdeImovel(codIBGE);
				retorno = paramPersistDadoGeoService.salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, false, codIBGE, false,1);					
			}
			if (retorno.length > 0){
				String comp = null;
				if(codIBGE.isNaN())
					codIBGE = 0.0;
				if(retorno[0].contains("OK") || retorno[0].contains(Integer.toString(codIBGE.intValue()))){
					comp = "Importação realizada com sucesso!";
					System.out.println(comp);
					habilitaLinkVisualizaShapeFile = true;
					ContextoUtil.getContexto().setLocalizacaoSalvaNoReq(localizacaoGeograficaSelecionada);
					ContextoUtil.getContexto().setPergunta(perguntaSalvar);
					JsfUtil.addSuccessMessage(comp);
					temArquivo = false;
				}else{
					comp = "Não foi possível persistir o Shape Informado. ";
					for (int i = 0; i < retorno.length; i++) {
						comp += retorno[i]+" - ";	
					}
					System.out.println(comp);
					JsfUtil.addErrorMessage(comp);
					temArquivo = true;
					habilitaLinkVisualizaShapeFile = false;
				}
			}
			
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			System.out.println("ERRO AO SALVAR PARAMETROS");
			JsfUtil.addWarnMessage("Erro ao persistir os shape no banco!\n Apenas foi realizado o Upload.");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * @param codIBGE
	 * @return
	 * @throws Exception
	 */
	private Double carregandoEnderecoByIdeImovel(Double codIBGE)
			throws Exception {
		if(!Util.isNullOuVazio(this.imovelRural) && !Util.isNullOuVazio(this.imovelRural.getImovel())){
			this.imovelRural = imovelRuralService.carregar(this.imovelRural.getIdeImovelRural());
			System.out.println("INFO -*-*-*- Endereço do Imóvel Carragado com Sucesso!");
			codIBGE = this.imovelRural.getImovel().getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio();
		}else{
			this.imovel = imovelService.buscarImovelPorIdeSemImRural(imovel);
			if(!Util.isNullOuVazio(this.imovel.getIdeEndereco().getIdeLogradouro().getIdeMunicipio()) && !Util.isNullOuVazio(this.imovel.getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio()))
				codIBGE = this.imovel.getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio();
			else
				JsfUtil.addErrorMessage("Não foi Possível Carregar o Código IBGE da Localidade");
		}
		return codIBGE;
	}
	
	public void excluirArquivo() {
		if(listaArquivo.size()>0){
			listaArquivo.remove(arqDeExclusao);
			JsfUtil.addSuccessMessage("Arquivo Removido com Sucesso.");
		}
		else{
			temArquivo = false;
			JsfUtil.addWarnMessage("Não é possível excluir este arquivo.");
		}
		if(listaArquivo.isEmpty())
			temArquivo = false;
	}
	
	public Boolean getExisteTheGeom(){
		
		Exception erro = null;
		
		try {
			if(Util.isNullOuVazio(existeTheGeom)){
				existeTheGeom = localizacaoGeograficaService.verificaSeExisteTheGeomValido(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica());
				return existeTheGeom;
			}
			else{
				if(!existeTheGeom){
					existeTheGeom = localizacaoGeograficaService.verificaSeExisteTheGeomValido(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica());
					return existeTheGeom;
				}
				else
					return existeTheGeom;
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			existeTheGeom = null;
			return false;
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}
	
	public Collection<SistemaCoordenada> getDatums() {
//		ArrayList<SistemaCoordenada> temp = new ArrayList<SistemaCoordenada>();
//		for(SistemaCoordenada sistema : datums){
//			if(sistema.getNomSistemaCoordenada().contains("UTM")){
//				temp.add(sistema);
//			}
//		}
//		datums.removeAll(temp);
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

	public List<ClassificacaoSecaoGeometrica> getItemsClassifSecGeometrica() {
		return itemsClassifSecGeometrica;
	}

	public void setItemsClassifSecGeometrica(List<ClassificacaoSecaoGeometrica> itemsClassifSecGeometrica) {
		this.itemsClassifSecGeometrica = itemsClassifSecGeometrica;
	}

	public Boolean getPnlVerticesHabilitado() {
		return pnlVerticesHabilitado;
	}

	public void setPnlVerticesHabilitado(Boolean pnlVerticesHabilitado) { 
		this.pnlVerticesHabilitado = pnlVerticesHabilitado;
	}

	public ClassificacaoSecaoGeometricaService getServiceClassifSecGeometrica() {
		return serviceClassifSecGeometrica;
	}

	public void setServiceClassifSecGeometrica(ClassificacaoSecaoGeometricaService serviceClassifSecGeometrica) {
		this.serviceClassifSecGeometrica = serviceClassifSecGeometrica;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
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

	public boolean getEditandoVertice() {
		return editandoVertice;
	}

	public void setEditandoVertice(boolean editandoVertice) {
		this.editandoVertice = editandoVertice;
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

		try {
			if (!Util.isNullOuVazio(localizacaoGeograficaSelecionada.getDadoGeograficoCollection()) && !localizacaoGeograficaSelecionada.getDadoGeograficoCollection().isEmpty()) {
				mostraLista = true;
			} else {
				mostraLista = false;
			}
		} catch (LazyInitializationException e) {
			try {
				localizacaoGeograficaSelecionada = localizacaoGeograficaService.carregar(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica());
			} catch (Exception e2) {
				JsfUtil.addErrorMessage("Não foi possível carregar os dados geográficos dessa localização.");
			}
			if (!Util.isNullOuVazio(localizacaoGeograficaSelecionada.getDadoGeograficoCollection()) && !localizacaoGeograficaSelecionada.getDadoGeograficoCollection().isEmpty()) {
				mostraLista = true;
			} else {
				mostraLista = false;
			}
		}
		return mostraLista;
	}

	public void setMostraLista(Boolean mostraLista) {
		this.mostraLista = mostraLista;
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

	public Boolean getUplShapefile() {
		return uplShapefile;
	}

	public void setUplShapefile(Boolean uplShapefile) {
		this.uplShapefile = uplShapefile;
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

	/**
	 * @return the arqDeExclusao
	 */
	public String getArqDeExclusao() {
		return arqDeExclusao;
	}

	/**
	 * @param arqDeExclusao the arqDeExclusao to set
	 */
	public void setArqDeExclusao(String arqDeExclusao) {
		this.arqDeExclusao = arqDeExclusao;
	}

	/**
	 * @param existeTheGeom the existeTheGeom to set
	 */
	public void setExisteTheGeom(Boolean existeTheGeom) {
		this.existeTheGeom = existeTheGeom;
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

	public ImovelRural getImovelRural() {
		return imovelRural;
	}

	public void setImovelRural(ImovelRural imovelRural) {
		this.imovelRural = imovelRural;
	}

	public List<ObjetivoRequerimentoLimpezaArea> getListObjReqLimpArea() {
		return listObjReqLimpArea;
	}

	public void setListObjReqLimpArea(
			List<ObjetivoRequerimentoLimpezaArea> listObjReqLimpArea) {
		this.listObjReqLimpArea = listObjReqLimpArea;
	}

	public Boolean getDisableClassificacoes() {
		return disableClassificacoes;
	}

	public void setDisableClassificacoes(Boolean disableClassificacoes) {
		this.disableClassificacoes = disableClassificacoes;
	}

	public List<LocalizacaoGeografica> getListLocGeo() {
		return listLocGeo;
	}

	public void setListLocGeo(List<LocalizacaoGeografica> listLocGeo) {
		this.listLocGeo = listLocGeo;
	}

	public PerguntaRequerimento getPerguntaRequerimentoLocGeoReqUnico() {
		return perguntaRequerimentoLocGeoReqUnico;
	}

	public void setPerguntaRequerimentoLocGeoReqUnico(
			PerguntaRequerimento perguntaRequerimentoLocGeoReqUnico) {
		this.perguntaRequerimentoLocGeoReqUnico = perguntaRequerimentoLocGeoReqUnico;
	}

	public boolean isDisableParaVisualizar() {
		return disableParaVisualizar;
	}

	public void setDisableParaVisualizar(boolean disableParaVisualizar) {
		this.disableParaVisualizar = disableParaVisualizar;
	}

	public Imovel getImovel() {
		return imovel;
	}

	public void setImovel(Imovel imovel) {
		this.imovel = imovel;
	}

	public RequerimentoImovel getReqImovelRural() {
		return reqImovelRural;
	}

	public void setReqImovelRural(RequerimentoImovel reqImovelRural) {
		if(!Util.isNull(reqImovelRural)){
			try {
				this.reqImovelRural = (RequerimentoImovel) reqImovelRural.clone();
			} catch (CloneNotSupportedException e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			}
		}else
			this.reqImovelRural = reqImovelRural;
	}

	public ObjetivoRequerimentoLimpezaArea getObjReqLimpezaAreaSelecionado() {
		return objReqLimpezaAreaSelecionado;
	}

	public void setObjReqLimpezaAreaSelecionado(
			ObjetivoRequerimentoLimpezaArea objReqLimpezaAreaSelecionado) {
		this.objReqLimpezaAreaSelecionado = objReqLimpezaAreaSelecionado;
	}

	public Pergunta getPerguntaSalvar() {
		return perguntaSalvar;
	}

	public void setPerguntaSalvar(Pergunta perguntaSalvar) {
		this.perguntaSalvar = perguntaSalvar;
	}

	public ObjetivoLimpezaArea getObjLimpezaAreaSelecionado() {
		return objLimpezaAreaSelecionado;
	}

	public void setObjLimpezaAreaSelecionado(
			ObjetivoLimpezaArea objLimpezaAreaSelecionado) {
		this.objLimpezaAreaSelecionado = objLimpezaAreaSelecionado;
	}

	public List<ObjetivoLimpezaArea> getListObjLimpezaArea() {
		return listObjLimpezaArea;
	}

	public void setListObjLimpezaArea(List<ObjetivoLimpezaArea> listObjLimpezaArea) {
		this.listObjLimpezaArea = listObjLimpezaArea;
	}

}