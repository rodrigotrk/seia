package br.gov.ba.seia.controller;

import static br.gov.ba.seia.util.Html.telaInvalida;
import static br.gov.ba.seia.util.Html.telaValida;

import java.util.ArrayList;
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
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.ClassificacaoSecaoGeometrica;
import br.gov.ba.seia.entity.CoordenadaGeografica;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.SistemaCoordenada;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.URLEnum;
import br.gov.ba.seia.enumerator.ValidacaoShapeEnum;
import br.gov.ba.seia.exception.SeiaLocalizacaoGeograficaException;
import br.gov.ba.seia.facade.AdicionarMunicipiosServiceFacade;
import br.gov.ba.seia.facade.LocalizacaoGeograficaServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.ClassificacaoSecaoGeometricaService;
import br.gov.ba.seia.service.DatumService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.ParamPersistDadoGeoService;
import br.gov.ba.seia.service.ValidacaoGeoSeiaService;
import br.gov.ba.seia.service.VerticeService;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.GeoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Named("localizacaoGeograficaController")
@ViewScoped
public class LocalizacaoGeograficaController extends SeiaControllerAb {

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
	private MunicipioService municipioService;
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	@EJB
	private LocalizacaoGeograficaServiceFacade localizacaoGeograficaServiceFacade;
	@EJB
	private AdicionarMunicipiosServiceFacade adicionarMunicipiosServiceFacade;
	@EJB
	private ValidacaoGeoSeiaService validacaoGeoSeiaService;
	
	@SuppressWarnings("unused")
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
	private List<String> listaCaminhoArquivo;
	private StreamedContent arquivoBaixar;
	private String arqDeExclusao;

	private boolean editandoVertice = false;
	private boolean desabilitarVertices = true;
	private Boolean mostraLista = false;
	private Boolean temArquivo;

	private Boolean habilitaIncluirLoc = false;

	private Boolean habilitaLinkVisualizaShapeFile;

	// Variaveis UTM
	private boolean locGeoIsUTM = false;
	private Integer zone;

	private String latitudeUTM;
	private String longitudeUTM;
	private Integer tipoSecaoGeometrica = 0;

	@PostConstruct
	public void init() {

		this.pnlVerticesHabilitado = false;
		this.uplShapefile = false;
		this.locGeoIsUTM = false;
		this.empreendimento = (Empreendimento) getAtributoSession("EMPREENDIMENTO_SESSAO");
		this.imovelRural = (ImovelRural) getAtributoSession("IMOVEL_RURAL");

		carregarLocalizacao();

		this.obterDatums();
		this.obterClassificacoes();
		vertice = new DadoGeografico();
		temArquivo = false;
		listaArquivo = new ArrayList<String>();
		listaCaminhoArquivo = new ArrayList<String>();
		tipoSecaoGeometrica = 0;
	}

	private void carregarLocalizacao() {
		try {
			if (!Util.isNull(empreendimento)) {
				empreendimento = empreendimentoService.carregarComLocalizacaoGeografica(empreendimento);
				if (!Util.isNull(empreendimento.getIdeLocalizacaoGeografica())) {
					this.localizacaoGeograficaSelecionada = empreendimento.getIdeLocalizacaoGeografica();
					if (Util.isNull(this.localizacaoGeograficaSelecionada)) {
						this.localizacaoGeograficaSelecionada = new LocalizacaoGeografica();
					}
					datumSelecionado = localizacaoGeograficaSelecionada.getIdeSistemaCoordenada();
					desabilitarVertices = false;
					if(empreendimento.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao().equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId())){
						defineExibicaoVerticesGeogUTM();
					}else{
						uplShapefile = empreendimento.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao().equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId());
					}

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
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
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

	public void salvarLocalizacaoGeografica() {
		try {
			if (Util.isNull(this.empreendimento)) {
				this.empreendimento = (Empreendimento) getAtributoSession("EMPREENDIMENTO_SESSAO");
			}

			if(validarLocalizacaoGeografica()){
				if(!Util.isNull(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica())) {
					this.excluirLocGeo(this.localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica());
				}

				this.localizacaoGeograficaSelecionada.setDtcCriacao(new Date());
				this.localizacaoGeograficaSelecionada.setDtcExclusao(null);
				this.localizacaoGeograficaSelecionada.setIndExcluido(Boolean.FALSE);

				localizacaoGeograficaService.salvarOuAtualizarLocalizacaoGeografica(this.localizacaoGeograficaSelecionada);

				this.empreendimento.setIdeLocalizacaoGeografica(this.localizacaoGeograficaSelecionada);

				empreendimentoService.atualizarLocalizacaoGeograficaEmpreendimento(empreendimento);
				desabilitarVertices = false;
				defineExibicaoVerticesGeogUTM();
				JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso.");
				setHabilitaIncluirLoc(true);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private boolean validarLocalizacaoGeografica(){
		boolean valido = true;
		if(Util.isNull(localizacaoGeograficaSelecionada.getIdeClassificacaoSecao())){
			valido = false;
			JsfUtil.addErrorMessage("O campo Tipo de Inserção é de preenchimento obrigatório.");
		}
		if(Util.isNull(localizacaoGeograficaSelecionada.getIdeSistemaCoordenada())){
			valido = false;
			JsfUtil.addErrorMessage("O campo Sistema de Coordenada é de preenchimento obrigatório.");
		}
		return valido;
	}

	public void excluirLocGeo(Integer pIdeLocalizacaoGeografica) {
		Exception erro = null;
		try {
			localizacaoGeograficaService.excluirDadosPersistidos(pIdeLocalizacaoGeografica);
			existeTheGeom = false;
			this.localizacaoGeograficaSelecionada.setDadoGeograficoCollection(new ArrayList<DadoGeografico>());

			listaArquivo.clear();
			listaArquivo = new ArrayList<String>();
			listaCaminhoArquivo.clear();
			listaCaminhoArquivo = new ArrayList<String>();
		} catch (Exception e) {
			erro =e;
			JsfUtil.addErrorMessage(e.getMessage());
		}finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}

	}

	public String criarUrlShape(String nulo) {
		StringBuilder lUrl = new StringBuilder();
		if(!Util.isNullOuVazio(localizacaoGeograficaSelecionada) && !Util.isNullOuVazio(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica())) {
			Map<String, String> parametros = new HashMap<String, String>();
			parametros.put("acao", "view");
			parametros.put("idloc", localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica().toString());
			parametros.put("tipo", "1");
			try {
				lUrl.append(URLEnum.CAMINHO_GEOBAHIA.getUrl(parametros));
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}

		StringBuilder lReturn = new StringBuilder();
		lReturn.append(lUrl.toString());
		return lReturn.toString();
	}

	public void salvarVertice() {

		if ((Util.isNullOuVazio(this.fracaoGrauLatitude) || editandoVertice) && selectedModoCoordenada.equals("1")) {
			calculaFracaoGrauLatitude(null);
		}

		if ((Util.isNullOuVazio(this.fracaoGrauLongitude) || editandoVertice) && selectedModoCoordenada.equals("1")) {
			calculaFracaoGrauLongitude(null);
		}

		if (selectedModoCoordenada.equals("2")) {
			fracaoGrauLatitude = "-" + fracaoGrauLatitudeDecimal;
			fracaoGrauLongitude = "-" + fracaoGrauLongitudeDecimal;
			/*
			 * fracaoGrauLatitudeDecimal = "-"+fracaoGrauLatitudeDecimal; fracaoGrauLongitudeDecimal = "-"+fracaoGrauLongitudeDecimal;
			 */
		}

		Map<String, Object> params = carregarParametrosParaSalvarVertice();

		try {
			localizacaoGeograficaServiceFacade.salvarVerticeComUnidadeConservacao(params);
			localizacaoGeograficaSelecionada = localizacaoGeograficaService.carregar(localizacaoGeograficaSelecionada);
			carregarLocalizacao();
			habilitaIncluirLoc = false;
			limparVertice();
			JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso!");
			telaValida();
		} catch (SeiaLocalizacaoGeograficaException e) {
			JsfUtil.addWarnMessage(e.getMessage());
			telaInvalida();
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível executar a solicitação.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			telaInvalida();
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private Map<String,Object> carregarParametrosParaSalvarVertice() {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("dadoGeografico", vertice);
		params.put("editandoVertice", editandoVertice);
		params.put("fracaoGrauLatitudeDecimal", fracaoGrauLatitudeDecimal);
		params.put("fracaoGrauLongitudeDecimal", fracaoGrauLongitudeDecimal);
		params.put("fracaoGrauLatitude", fracaoGrauLatitude);
		params.put("fracaoGrauLongitude", fracaoGrauLongitude);
		params.put("modoCoordenada", selectedModoCoordenada);
		params.put("empreendimento", empreendimento);
		params.put("localizacaoGeograficaSelecionada", localizacaoGeograficaSelecionada);
		return params;
	}

	private Map<String,Object> carregarParametrosParaSalvarVerticeUTM() {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("latitudeUTM",latitudeUTM);
		params.put("longitudeUTM",longitudeUTM);
		params.put("localizacaoGeograficaSelecionada",localizacaoGeograficaSelecionada);
		params.put("dadoGeografico",vertice);
		params.put("empreendimento",empreendimento);
		return params;
	}
	/*
	private void persisteVertice(String fracaoGrauLatitude, String fracaoGrauLongitude) {
		if (editandoVertice == true) {
			Exception erro = null;
			try {
				Point ponto = PostgisUtil.criarPonto(Double.parseDouble(fracaoGrauLatitude.replace(',', '.')),
						Double.parseDouble(fracaoGrauLongitude.replace(',', '.')));
				vertice.setCoordGeoNumerica(ponto.toString());
				vertice.setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
				verticeService.salvarOuAtualizarVertice(vertice);
				Integer idDosParametros = paramPersistDadoGeoService.salvarParamsEPersistindoVerticeTheGeom(vertice, localizacaoGeograficaSelecionada.getIdeSistemaCoordenada());
				if(Util.isNullOuVazio(idDosParametros)) {
					System.out.println("ERRO AO SALVAR PARAMETROS");
				} else {
					System.out.println("PARAMETROS persistido com sucesso!");
				}
				JsfUtil.addSuccessMessage("Alteração realizada com sucesso!");
				editandoVertice = false;
				limparVertice();
				RequestContext.getCurrentInstance().execute("dialogoIncluirVertice.hide()");
			} catch (Exception e) {
				erro =e;
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				JsfUtil.addErrorMessage(e.getMessage());
			}finally{
				if(erro != null) {
					throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
				}
			}

		} else {
			Exception erro = null;
			try {
				Point ponto = PostgisUtil.criarPonto(Double.parseDouble(fracaoGrauLatitude.replace(',', '.')),
						Double.parseDouble(fracaoGrauLongitude.replace(',', '.')));
				vertice.setCoordGeoNumerica(ponto.toString());
				vertice.setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
				verticeService.salvarVertice(vertice);

				Integer idDosParametros = paramPersistDadoGeoService.salvarParamsEPersistindoVerticeTheGeom(vertice, localizacaoGeograficaSelecionada.getIdeSistemaCoordenada());
				if(Util.isNullOuVazio(idDosParametros)) {
					System.out.println("ERRO AO SALVAR PARAMETROS");
				} else {
					System.out.println("PARAMETROS persistido com sucesso!");
				}

				if (Util.isNull(localizacaoGeograficaSelecionada.getDadoGeograficoCollection())) {
					localizacaoGeograficaSelecionada.setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
				}
				localizacaoGeograficaSelecionada.getDadoGeograficoCollection().add(vertice);
				JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso!");
				limparVertice();
				RequestContext.getCurrentInstance().execute("dialogoIncluirVertice.hide()");
			} catch (Exception e) {
				erro =e;
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				JsfUtil.addErrorMessage(e.getMessage());
			}finally{
				if(erro != null) {
					throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
				}
			}

		}
	}
	**/

	public void excluirVertice() {

		Exception erro = null;
		try {
			verticeService.excluir(verticeExclusao);
			localizacaoGeograficaSelecionada.getDadoGeograficoCollection().remove(verticeExclusao);
			JsfUtil.addSuccessMessage("Exclusão efetuada com sucesso!");
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage(e.getMessage());
		}finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}

	}

	public void carregarVertice() {
		selectedModoCoordenada = "1";
		Exception erro = null;
		try {
			CoordenadaGeografica coordenadaGeografica = GeoUtil.converterPointParaCoordenadaGeografica(vertice.getCoordGeoNumerica());
			
			
			grausLatitude = coordenadaGeografica.getLatitude().getGrau().toString();
			minutosLatitude = coordenadaGeografica.getLatitude().getMinuto().toString();
			segundosLatitude = coordenadaGeografica.getLatitude().getSegundo().toString();
			
			grausLongitude = coordenadaGeografica.getLongitude().getGrau().toString();
			minutosLongitude = coordenadaGeografica.getLongitude().getMinuto().toString();
			segundosLongitude = coordenadaGeografica.getLongitude().getSegundo().toString();

			
			fracaoGrauLatitude = coordenadaGeografica.getLatitude().getAsGD();
			fracaoGrauLatitudeDecimal = fracaoGrauLatitude;
			
			fracaoGrauLongitude = coordenadaGeografica.getLongitude().getAsGD();
			fracaoGrauLongitudeDecimal = fracaoGrauLongitude;
			
		} catch (Exception e) {
			erro = null;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
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
		latitudeUTM = "";
		longitudeUTM = "";
	}

	public double getLongitude(DadoGeografico pVertice) {
		if(Util.isNull(pVertice.getCoordGeoNumerica())){
			return 0;
		}

		if (pVertice.getCoordGeoNumerica().length() > 0 && pVertice.getCoordGeoNumerica().indexOf("(") > 0
				&& pVertice.getCoordGeoNumerica().lastIndexOf(")") > 0) {
			String value = pVertice.getCoordGeoNumerica().substring(pVertice.getCoordGeoNumerica().indexOf("(") + 1,
					pVertice.getCoordGeoNumerica().lastIndexOf(")"));
			String[] v = value.split(" ");
			if (v.length == 2) {
				try {
					return Double.parseDouble(v[1]);
				} catch (Exception e) {
				}
			}
		}
		return 0;
	}

	public float getLatitude(DadoGeografico pVertice) {

		if(Util.isNull(pVertice.getCoordGeoNumerica())){
			return 0;
		}

		if (pVertice.getCoordGeoNumerica().length() > 0 && pVertice.getCoordGeoNumerica().indexOf("(") > 0
				&& pVertice.getCoordGeoNumerica().lastIndexOf(")") > 0) {
			String value = pVertice.getCoordGeoNumerica().substring(pVertice.getCoordGeoNumerica().indexOf("(") + 1,
					pVertice.getCoordGeoNumerica().lastIndexOf(")"));
			String[] v = value.split(" ");
			if (v.length == 2) {
				Exception erro = null;
				try {
					return Float.parseFloat(v[0]);
				} catch (Exception e) {
					erro = e;
				}finally{
					if(erro != null) {
						throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
					}
				}
			}
		}
		return 0;
	}

	public String getUrl(DadoGeografico pVertice) {
		
		String lUrl = new String();
		Map<String, String> parametros = new HashMap<String, String>();
		parametros.put("acao", "view");
		parametros.put("idloc", pVertice.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica().toString());
		parametros.put("tipo", "1");
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
		Exception erro = null;

		try {
			String value = null;
			if (event.getNewValue() != null) {
				value = event.getNewValue().toString();
			} else {
				pnlVerticesHabilitado = false;
				uplShapefile = false;
				locGeoIsUTM = false;
			}
			if (value != null && !value.isEmpty()) {
				for (ClassificacaoSecaoGeometrica itemClassifSecGeometrica : itemsClassifSecGeometrica) {
					String newValue = itemClassifSecGeometrica.getIdeClassificacaoSecao().toString();
					if (value.equals(newValue) && itemClassifSecGeometrica.getNomClassificacaoSecao().toLowerCase().contains("shapefile")) {
						obterDatums();
						uplShapefile = true;
						pnlVerticesHabilitado = false;
						locGeoIsUTM = false;
						break;
					} else if (value.equals(newValue) && !itemClassifSecGeometrica.getNomClassificacaoSecao().equals("Selecione...")) {
						this.pnlVerticesHabilitado = false;
						this.locGeoIsUTM = false;
						obterDatums();
						uplShapefile = false;
						existeTheGeom = false;
						break;
					}
				}
			}
			setHabilitaIncluirLoc(false);
		} catch (Exception e) {
			erro =e;
			JsfUtil.addErrorMessage(e.getMessage());
		}finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}
	}

	public void selectedDatumChanged(ValueChangeEvent event) throws AbortProcessingException {
		this.datumSelecionado = (SistemaCoordenada) event.getNewValue();
		defineExibicaoVerticesGeogUTM();
		habilitaIncluirLoc = false;
	}

	private void defineExibicaoVerticesGeogUTM() {
		if (!Util.isNullOuVazio(this.localizacaoGeograficaSelecionada.getIdeClassificacaoSecao()) && !Util.isNullOuVazio(this.localizacaoGeograficaSelecionada.getIdeClassificacaoSecao().getIdeClassificacaoSecao())
				&& this.localizacaoGeograficaSelecionada.getIdeClassificacaoSecao().getIdeClassificacaoSecao().equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId())){
			this.pnlVerticesHabilitado = false;
			this.locGeoIsUTM = false;
			uplShapefile = true;
		}else if (this.datumSelecionado.getIdeSistemaCoordenada() != 1 && this.datumSelecionado.getIdeSistemaCoordenada() != 4){
			this.pnlVerticesHabilitado = false;
			this.locGeoIsUTM = true;
			uplShapefile = false;
		}else{
			this.pnlVerticesHabilitado = true;
			this.locGeoIsUTM = false;
			uplShapefile = false;
		}
	}

	public void obterDatums() {
		Exception erro = null;
		try {
			datums = serviceDatum.listarDatum();
		} catch (Exception e) {
			erro=e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}
	}

	/**
	 * Carrega o vertice para edicao, imprescindivel que a variavel vertice esteja preenchida.
	 * @Data 08/07/2014
	 * @author micael.coutinho
	 */
	public void carregarVerticeUTM() {
		latitudeUTM = vertice.getCoordGeoNumerica().replaceAll("POINT ", "").replace("(", "").replace(")", "").split(" ")[0];
		longitudeUTM = vertice.getCoordGeoNumerica().replaceAll("POINT ", "").replace("(", "").replace(")", "").split(" ")[1];
		carregarZona();
	}

	public String getLatitudeUTM(DadoGeografico pVertice) {
		try{
			return pVertice.getCoordGeoNumerica().replaceAll("POINT ", "").replace("(", "").replace(")", "").split(" ")[0];
		}catch(Exception e){
			return StringUtils.EMPTY;
		}
	}

	public String getLongitudeUTM(DadoGeografico pVertice) {
		try{
			return pVertice.getCoordGeoNumerica().replaceAll("POINT ", "").replace("(", "").replace(")", "").split(" ")[1];
		}catch(Exception e){
			return StringUtils.EMPTY;
		}
	}

	public void carregarZona() {
		if(localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().getIdeSistemaCoordenada().equals(2) || localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().getIdeSistemaCoordenada().equals(5)){
			zone = 23;
		}else if(localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().getIdeSistemaCoordenada().equals(3) || localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().getIdeSistemaCoordenada().equals(6)){
			zone = 24;
		}
	}

	public void salvarVerticeUTM() {
		try {
			localizacaoGeograficaServiceFacade.salvarVerticeUTMComUnidadeConservacao(carregarParametrosParaSalvarVerticeUTM());
			RequestContext.getCurrentInstance().execute("dialogIncluirVerticeUtm.hide();");
			carregarLocalizacao();
			habilitaIncluirLoc = false;
			limparVertice();
			JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso!");
		}
		catch(SeiaLocalizacaoGeograficaException e) {
			JsfUtil.addWarnMessage(e.getMessage());
		}
		catch(Exception e) {
			JsfUtil.addErrorMessage("Não foi possível executar a solicitação.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
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

	public void obterClassificacoes() {
		Exception erro = null;
		try {
			itemsClassifSecGeometrica = serviceClassifSecGeometrica.listarClassificacaoSecaoGeometrica();
			//removendo a opção de desenho
			itemsClassifSecGeometrica.remove(2);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}
	}

	public void trataArquivo(FileUploadEvent event) {
		
		if(listaArquivo.size()<3){
			if(!Util.isNullOuVazio(localizacaoGeograficaSelecionada) && !Util.isNullOuVazio(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica())){

				if((!Util.isNullOuVazio(localizacaoGeograficaSelecionada.getIdeSistemaCoordenada())
						&& Util.isNullOuVazio(localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().getSrid()))
						|| Util.isNullOuVazio(localizacaoGeograficaSelecionada.getIdeSistemaCoordenada())){

					try {
						localizacaoGeograficaSelecionada = localizacaoGeograficaService.carregarSomenteComSistemaCoordenada(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica());
					} catch (Exception e) {
						throw Util.capturarException(e, Util.SEIA_EXCEPTION);
					}
				}

				caminhoArquivo = FileUploadUtil.EnviarShape(event, DiretorioArquivoEnum.SHAPEFILES.toString()+FileUploadUtil.getFileSeparator()+localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica().toString(),
						localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica().toString()+"_"+localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().getSrid());

				listaArquivo.add(FileUploadUtil.getFileName(caminhoArquivo));
				listaCaminhoArquivo.add(caminhoArquivo);

				temArquivo = true;

				JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");

			} else {
				JsfUtil.addWarnMessage("Para inserir os shapes é preciso salvar a Localização Geográfica!");
			}

		} else {
			JsfUtil.addWarnMessage("Não é possível enviar mais de 3 arquivos.");
		}
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
		} else {
			return true;
		}
	}

	public void persistirShapes(){
		String[] retorno = new String[0];
		Exception erro = null;
		String comp = new String();
		try {
			
			localizacaoGeograficaSelecionada = localizacaoGeograficaService.carregarSomenteComSistemaCoordenada(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica());

			Collection<Municipio> listaMunicipio = adicionarMunicipiosServiceFacade.listarTodosMunicipiosEnvolvidos(empreendimento.getIdeEmpreendimento());
				if(validarTipoShape()){
					if(!Util.isNullOuVazio(listaMunicipio)) {
						retorno = paramPersistDadoGeoService.salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, false, null, false,1);
						Map<String, Object> retornoValidarMunicipios = adicionarMunicipiosServiceFacade.validarMunicipios(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica(), listaMunicipio);
						Boolean isShapeValido = (Boolean) retornoValidarMunicipios.get("isShapeValido");
						if(Boolean.FALSE.equals(isShapeValido)) {
							retornoValidarMunicipios.get("isShapeValido");
							@SuppressWarnings("unchecked")
							Collection<Municipio> listaMunicipioFaltandoShape = (Collection<Municipio>) retornoValidarMunicipios.get("listaMunicipioFaltando");
							@SuppressWarnings("unchecked")
							Collection<Municipio> listaMunicipioSobrandoShape = (Collection<Municipio>) retornoValidarMunicipios.get("listaMunicipioSobrando");
		
							if(!Util.isNullOuVazio(listaMunicipioSobrandoShape) && Util.isNullOuVazio(listaMunicipioFaltandoShape)) {
								AdicionarMunicipioController adicionarMunicipiosController = (AdicionarMunicipioController) SessaoUtil.recuperarManagedBean("#{adicionarMunicipioController}", AdicionarMunicipioController.class);
								adicionarMunicipiosController.setLocalizacaoGeografica(localizacaoGeograficaSelecionada);
								adicionarMunicipiosController.setListaMunicipioSelecionado(listaMunicipioSobrandoShape);
								RequestContext.getCurrentInstance().execute("dlgConfirmarAdicaoAutomaticaMunicipio.show()");
							}
		
							if(!Util.isNullOuVazio(listaMunicipioFaltandoShape)) {
								adicionarMunicipiosServiceFacade.removerDadoGeografico(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica());
								JsfUtil.addWarnMessage("Caro(a) Usuário(a), o shape inserido não contempla todos os municípios cadastrados no empreendimento. Por favor, revise a lista de municípios.");
							}
						}
						retornarMgsErro(retorno, comp);
					} else {
						Municipio municipio = this.municipioService.buscarCoordGeoMunicipioPorEmpreendimento(empreendimento.getIdeEmpreendimento());
						retorno = paramPersistDadoGeoService.salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, true, municipio.getCoordGeobahiaMunicipio(), false,1);
						retornarMgsErro(retorno, comp);
					}
			}
		}
		catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			System.out.println("ERRO AO SALVAR PARAMETROS");
			JsfUtil.addWarnMessage("Erro ao persistir os shape no banco!\n Apenas foi realizado o Upload.");
		}finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}
	}

	private void retornarMgsErro(String[] retorno, String comp) {
		if (retorno.length > 0){
			if (retorno[0].equalsIgnoreCase("OK")){
				comp = "Importação realizada com sucesso!";
			} else if (isCodErro(retorno, "1000")){
				comp = "Erro interno. Favor contactar o suporte do sistema.";
			} else if (isCodErro(retorno, "1001")){
				comp = "Erro interno. Favor contactar o suporte do sistema.";
			} else if (isCodErro(retorno, "1002")){
				comp = "Erro interno. Favor contactar o suporte do sistema.";
			} else if (isCodErro(retorno, "1003")){
				comp = "Erro interno. Favor contactar o suporte do sistema.";
			} else if (isCodErro(retorno, "2000")){
				comp = "Erro: Importação do shapefile interrompida. Favor contactar o suporte do sistema.";
			} else if (isCodErro(retorno, "2001")){
				comp = "Erro durante importação do shapefile. Arquivos inválidos ou corrompidos.";
			} else if (isCodErro(retorno, "3000")){
				comp = "Erro: A geometria do arquivo carregado é desconhecida/inválida. Para importar é necessário um Shapefile do tipo 'POLÍGONO'.";
			} else if (isCodErro(retorno, "3001")){
				comp = "Erro: Shapefile com geometria inválida. <br><br>Verifique os erros de topologia do shape.";
			} else if (isCodErro(retorno, "3002")){
				String loc = new String (retorno[2].substring(retornaIndiceInicioCoord(retorno), retornaIndiceFinalCoord(retorno)));
				comp = "Erro: Sistema de Coordenadas incorreto ou Extensão geográfica inconsistente "+ loc +"].<br><br> Verifique o Sistema de Coordenadas escolhido e o georreferenciamento do arquivo shape.";
			} else if (isCodErro(retorno, "3003")){
				comp = "Erro: Município da localização geográfica não confere com o endereço cadastrado.";
			} else if (isCodErro(retorno, "3004")){
				String loc = new String (retorno[2].substring(retornaIndiceInicioCoord(retorno), retornaIndiceFinalCoord(retorno)));
				comp = "Erro: Extensão Geográfica fora dos limites do estado " + loc +"].<br><br> Verifique o Sistema de Coordenadas escolhido e o georreferenciamento do arquivo shape.";
			} else if (isCodErro(retorno, "3005")){
				comp = "Erro: Arquivo shapefile vazio. Não existem registros de geometrias.";
			} else if (isCodErro(retorno, "3006")){
				comp = "Erro: Poligonal importada está fora do limite da poligonal do imóvel, ou poligonal do imóvel inexistente.";
			} else if (isCodErro(retorno, "0001")
					|| isCodErro(retorno, "0002")
					|| isCodErro(retorno, "0003")
					|| isCodErro(retorno, "0004")
					|| isCodErro(retorno, "0005")
					|| isCodErro(retorno, "0006")){
				comp = "Erro interno. Favor contactar o suporte do sistema.";
			} else {
				comp = "Erro interno. Favor contactar o suporte do sistema.";
			}
			//	String comp = retorno[0].equalsIgnoreCase("ERRO")?retorno[2]+" ["+retorno[1]+"]":"Importação realizada com sucesso!";

			System.out.println(comp);
			if(retorno[0].equalsIgnoreCase("OK")){
				JsfUtil.addSuccessMessage(comp);
				habilitaLinkVisualizaShapeFile = true;
			}
			else if (retorno[0].equalsIgnoreCase("ERRO")) {
				JsfUtil.addErrorMessage(comp);
			}
		}
	}

	public boolean isCodErro(String[] retorno, String str){
		return retorno[1].contains(str);
	}

	public int retornaIndiceInicioCoord(String[] str){
		return str[2].indexOf("[");
	}

	public int retornaIndiceFinalCoord(String[] str){
		return str[2].indexOf("]");
	}

	public void excluirArquivo() {
		if(listaArquivo.size()>0){

			int index = listaArquivo.lastIndexOf(arqDeExclusao);

			listaArquivo.remove(index);
			listaCaminhoArquivo.remove(index);

			JsfUtil.addSuccessMessage("Arquivo Removido com Sucesso.");
		}
		else{
			temArquivo = false;
			JsfUtil.addWarnMessage("Não é possível excluir este arquivo.");
		}
		if(listaArquivo.isEmpty()) {
			temArquivo = false;
		}
	}

	public Boolean getExisteTheGeom(){
		try {
			existeTheGeom = localizacaoGeograficaService.verificaSeExisteTheGeomValido(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica());
			return existeTheGeom;
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return false;
		}
	}

	public StreamedContent getArquivoBaixar(String arquivo) {
		if(!listaArquivo.isEmpty()) {
			try {
				int index = listaArquivo.indexOf(arquivo);
				arquivoBaixar = gerenciaArquivoService.getContentFile(listaCaminhoArquivo.get(index));
			} catch (Exception e) {
				JsfUtil.addErrorMessage("Arquivo não encontrado.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}

		return arquivoBaixar;
	}
	
	/**
	 * Validar Tipo do Shape que será persistido
	 * @return
	 * @throws Exception
	 * @author rodrigo.santos
	 */
	private boolean validarTipoShape() throws Exception {
		
		String geometria = validacaoGeoSeiaService.buscarGeometriaShapeTemporario(localizacaoGeograficaSelecionada);
		
		boolean tipoPoligono =validacaoGeoSeiaService.validaTipoGeometriaPoligono(null,geometria);
		boolean tipoLinha = validacaoGeoSeiaService.validaTipoGeometriaLinha(null,geometria);
		boolean tipoPonto = validacaoGeoSeiaService.validaTipoGeometriaPonto(null,geometria);
		String tipoFeicao = "";
		
		if(tipoSecaoGeometrica == ValidacaoShapeEnum.POLIGONO.getId().intValue()){
			tipoFeicao = "Polígono";
			if(tipoPoligono){
				return true;
			}
			
		}
		if(tipoSecaoGeometrica == ValidacaoShapeEnum.POLIGONO_OU_LINHA.getId().intValue()){
			tipoFeicao = "Polígono ou Linha";
			if(tipoPoligono || tipoLinha){
				return true;
			}
			
		}
		if(tipoSecaoGeometrica == ValidacaoShapeEnum.PONTO.getId().intValue()){
			tipoFeicao = "Ponto";
			if(tipoPonto){
				return true;
			}
			
		}
		if(tipoSecaoGeometrica == ValidacaoShapeEnum.LINHA.getId().intValue()){
			tipoFeicao = "Linha";
			if(tipoLinha){
				return true;
			}
			
		}
		if(tipoSecaoGeometrica == ValidacaoShapeEnum.DESENHO.getId().intValue()){
			tipoFeicao = "Desenho";
			return true;
		}	
		
		JsfUtil.addErrorMessage("A feição do shape informado não é permitida. Insira um shape na feição " + tipoFeicao + ".");
		return false;
	}

	public Collection<SistemaCoordenada> getDatums() {
		if(datums != null && datums.size() > 1){} else {
			obterDatums();
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
		if (!Util.isNullOuVazio(localizacaoGeograficaSelecionada.getDadoGeograficoCollection()) && !localizacaoGeograficaSelecionada.getDadoGeograficoCollection().isEmpty()) {
			mostraLista = true;
		} else {
			mostraLista = false;
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

	public Boolean getHabilitaIncluirLoc() {
		return habilitaIncluirLoc;
	}

	public void setHabilitaIncluirLoc(Boolean habilitaIncluirLoc) {
		this.habilitaIncluirLoc = habilitaIncluirLoc;
	}

	/**
	 * @return the habilitaLinkVisualizaShapeFile
	 */
	public Boolean getHabilitaLinkVisualizaShapeFile() {
		return habilitaLinkVisualizaShapeFile;
	}

	/**
	 * @param habilitaLinkVisualizaShapeFile the habilitaLinkVisualizaShapeFile to set
	 */
	public void setHabilitaLinkVisualizaShapeFile(Boolean habilitaLinkVisualizaShapeFile) {
		this.habilitaLinkVisualizaShapeFile = habilitaLinkVisualizaShapeFile;
	}

	/**
	 * @return the locGeoIsUTM
	 */
	public boolean isLocGeoIsUTM() {
		return locGeoIsUTM;
	}

	/**
	 * @param locGeoIsUTM the locGeoIsUTM to set
	 */
	public void setLocGeoIsUTM(boolean locGeoIsUTM) {
		this.locGeoIsUTM = locGeoIsUTM;
	}

	/**
	 * @return the zone
	 */
	public Integer getZone() {
		return zone;
	}

	/**
	 * @param zone the zone to set
	 */
	public void setZone(Integer zone) {
		this.zone = zone;
	}

	/**
	 * @return the latitudeUTM
	 */
	public String getLatitudeUTM() {
		return latitudeUTM;
	}

	/**
	 * @param latitudeUTM the latitudeUTM to set
	 */
	public void setLatitudeUTM(String latitudeUTM) {
		this.latitudeUTM = latitudeUTM;
	}

	/**
	 * @return the longitudeUTM
	 */
	public String getLongitudeUTM() {
		return longitudeUTM;
	}

	/**
	 * @param longitudeUTM the longitudeUTM to set
	 */
	public void setLongitudeUTM(String longitudeUTM) {
		this.longitudeUTM = longitudeUTM;
	}


	public List<String> getListaCaminhoArquivo() {

		return listaCaminhoArquivo;
	}


	public void setListaCaminhoArquivo(List<String> listaCaminhoArquivo) {

		this.listaCaminhoArquivo = listaCaminhoArquivo;
	}

	public Integer getTipoSecaoGeometrica() {
		return tipoSecaoGeometrica;
	}

	public void setTipoSecaoGeometrica(Integer tipoSecaoGeometrica) {
		this.tipoSecaoGeometrica = tipoSecaoGeometrica;
	}
	
}