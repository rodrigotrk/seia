package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import com.vividsolutions.jts.geom.Point;

import br.gov.ba.seia.entity.ClassificacaoSecaoGeometrica;
import br.gov.ba.seia.entity.CoordenadaGeografica;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.GeoReferenciavel;
import br.gov.ba.seia.entity.ImovelRural;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Municipio;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.SistemaCoordenada;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.URLEnum;
import br.gov.ba.seia.enumerator.ConfigEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.ClassificacaoSecaoGeometricaService;
import br.gov.ba.seia.service.DatumService;
import br.gov.ba.seia.service.ImovelRuralService;
import br.gov.ba.seia.service.ImovelService;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.service.MunicipioService;
import br.gov.ba.seia.service.ParamPersistDadoGeoService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.VerticeService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.GeoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.PostgisUtil;
import br.gov.ba.seia.util.SeiaControllerAb;
import br.gov.ba.seia.util.Util;

@Named("localizacaoGeograficaAllController")
@ViewScoped
public class LocalizacaoGeograficaAllController extends SeiaControllerAb {

	@SuppressWarnings("unused")
	private Boolean existeTheGeom;

	private Collection<SistemaCoordenada> datums;
	private SistemaCoordenada datumSelecionado;
	private ClassificacaoSecaoGeometrica secaoGeometricaSelecionada;
	private List<SelectItem> itemsClassifSecGeometrica;
	private Boolean isPnlVerticesHabilitado;
	private Boolean isUplShapefile;
	private LocalizacaoGeografica localizacaoGeograficaSelecionada = new LocalizacaoGeografica();
	private ImovelRural imovelRural;
	private Boolean modoVisualizacao;

	private Boolean opcaoDesenho = false;

	private String selectedModoCoordenada = "1";
	private String grausLatitude;
	private String minutosLatitude;
	private String segundosLatitude;
	private String grausLongitude;
	private String minutosLongitude;
	private String segundosLongitude;
	private String fracaoGrauLatitude;
	private String fracaoGrauLongitude;
	private DadoGeografico vertice;
	private DadoGeografico verticeExclusao;
	private Boolean insereVertices;
	private Boolean desabilitaIncluirVerticeLocGeoImovel;
	private Boolean habilitaLinkVisualizaShapeFile;

	private String coordenadaPontoxy;
	private List<DadoGeografico> listaDadosGeo;
	private Boolean locGeoIsPonto = false;

	private GeoReferenciavel geoReferenciavel;
	
	public Boolean getHabilitaLinkVisualizaShapeFile() {
		return habilitaLinkVisualizaShapeFile;
	}

	public void setHabilitaLinkVisualizaShapeFile(Boolean habilitaLinkVisualizaShapeFile) {
		this.habilitaLinkVisualizaShapeFile = habilitaLinkVisualizaShapeFile;
	}

	private String caminhoArquivo;
	private List<String> listaArquivo;
	private StreamedContent arquivoBaixar;
	private boolean temLocGeoShape = false;
	private boolean temLocGeoDesenho = false;
	private Boolean temArquivo;

	private boolean desabilitarVertices = true;

	private boolean shape = false;

	private Boolean mostraLista = false;
	private Boolean showIncluirVertice;

	private String arqDeExclusao;

	public Boolean getShowIncluirVertice() {
		return showIncluirVertice;
	}

	public void setShowIncluirVertice(Boolean showIncluirVertice) {
		this.showIncluirVertice = showIncluirVertice;
	}

	private List<ClassificacaoSecaoGeometrica> listaSecaoGeomerica;

	private String caminhoDesenharGeoBahia = URLEnum.CAMINHO_GEOBAHIA.toString();
	
	@EJB
	private ImovelRuralService imovelRuralService;
	
	@EJB
	private ImovelService imovelService;

	@EJB
	private VerticeService verticeService;
	@EJB
	private ParamPersistDadoGeoService paramPersistDadoGeoService;
	@EJB
	private DatumService serviceDatum;
	@EJB
	private ClassificacaoSecaoGeometricaService serviceClassifSecGeometrica;
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	@EJB
	private ProcessoService processoService;
	@EJB
	private MunicipioService municipioService;

	@PostConstruct
	public void init() {
		locGeoIsPonto = false;
		if (!Util.isNullOuVazio(ContextoUtil.getContexto().getVisualizandoImovel()) && ContextoUtil.getContexto().getVisualizandoImovel())
			this.modoVisualizacao = true;
		else
			this.modoVisualizacao = false;

		obterImovelDaSessao();
		carregarLocalizacao();

		this.obterDatums();
		this.obterClassificacoes();
		carregarSecaoGeometrica();
		isPnlVerticesHabilitado = Boolean.FALSE;
		isUplShapefile = Boolean.FALSE;
		vertice = new DadoGeografico();
		insereVertices = Boolean.FALSE;
		desabilitaIncluirVerticeLocGeoImovel = Boolean.TRUE;

		temArquivo = Boolean.FALSE;
		listaArquivo = new ArrayList<String>();
		showIncluirVertice = Boolean.TRUE;
		habilitaLinkVisualizaShapeFile = false;
	}

	public void setImovel(ImovelRural imovelRural) {
		this.imovelRural = imovelRural;
		try {
			carregarLocalizacao();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
		
	}

	public void carregarSecaoGeometrica() {
		try {
			listaSecaoGeomerica = serviceClassifSecGeometrica.listarClassificacaoSecaoGeometrica();
			// removendo a opção de ponto
			listaSecaoGeomerica.remove(0);
			if (!Util.isNullOuVazio(imovelRural) && !imovelRural.isMenorQueQuatroModulosFiscais()) {
				// removendo a opção de desenho
				listaSecaoGeomerica.remove(1);
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
		
	}

	private void carregarLocalizacao() {
		// ler imovel da sess�o e criar uma nova Localiza��o Geogr�fica caso o
		// im�vel n�o possua uma localiza��o associada.
		try {
			if (!Util.isNullOuVazio(this.imovelRural) && !Util.isNullOuVazio(this.imovelRural.getIdeLocalizacaoGeografica())) {
				this.localizacaoGeograficaSelecionada = localizacaoGeograficaService.carregarSomenteComSistemaCoordenada(this.imovelRural
						.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
				datumSelecionado = this.localizacaoGeograficaSelecionada.getIdeSistemaCoordenada();

				if (this.localizacaoGeograficaSelecionada.getIdeClassificacaoSecao().getIdeClassificacaoSecao().equals(2)) {
					temLocGeoShape = true;
					this.locGeoIsPonto = false;
				} else if (this.localizacaoGeograficaSelecionada.getIdeClassificacaoSecao().getIdeClassificacaoSecao().equals(3)) {
					temLocGeoDesenho = true;
					this.opcaoDesenho = true;
					this.locGeoIsPonto = false;
				} else if (this.localizacaoGeograficaSelecionada.getIdeClassificacaoSecao().getIdeClassificacaoSecao().equals(1)) {
					this.locGeoIsPonto = true;
					listaDadosGeo = localizacaoGeograficaService.listarDadoGeografico(this.localizacaoGeograficaSelecionada,
							this.localizacaoGeograficaSelecionada.getIdeClassificacaoSecao().getIdeClassificacaoSecao());
					this.coordenadaPontoxy = reorganizarCoordenadaPonto(this.listaDadosGeo.get(0).getCoordGeoNumerica());
				}

				existeTheGeom = getExisteTheGeom();
				/*
				 * if(localizacaoGeograficaSelecionada.getIdeClassificacaoSecao()
				 * .getIdeClassificacaoSecao().equals(ClassificacaoSecaoEnum.
				 * CLASSIFICACAO_SECAO_SHAPEFILE.getId()) && getExisteTheGeom()
				 * ){ habilitaLinkVisualizaShapeFile = true; }else{
				 * habilitaLinkVisualizaShapeFile = false; }
				 */
			} else {
				this.localizacaoGeograficaSelecionada = new LocalizacaoGeografica();
			}
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
		
	}

	private String reorganizarCoordenadaPonto(String ponto) {
		String aux = ponto.substring(7, ponto.length() - 1);
		String[] c = aux.split(" ");
		aux = c[1] + " " + c[0];
		return aux;
	}

	public void excluirLocGeo(Integer pIdeLocalizacaoGeografica) {
		try {
			localizacaoGeograficaService.excluirDadosPersistidos(pIdeLocalizacaoGeografica);
			this.locGeoIsPonto = false;
			JsfUtil.addSuccessMessage("Exclusão efetuada com Sucesso.");
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível excluir a localização.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void salvarLocalizacaoGeografica() {
		try {

			montarLocalizacao();
			obterImovelDaSessao();
			
			if (!Util.isNullOuVazio(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica())) {
				localizacaoGeograficaService.salvarOuAtualizarLocalizacaoGeografica(this.localizacaoGeograficaSelecionada);
			} else {

				imovelRural = ContextoUtil.getContexto().getImovelRural();
				if (!Util.isNullOuVazio(imovelRural)){

					imovelRural.setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
					localizacaoGeograficaService.salvarlocalizacaoComImovel(localizacaoGeograficaSelecionada, imovelRural);
				}else{
					localizacaoGeograficaService.salvarlocalizacao(localizacaoGeograficaSelecionada);
					salvarGeoReferenciavel();
				}
			}

			if (this.localizacaoGeograficaSelecionada.getIdeClassificacaoSecao().getIdeClassificacaoSecao().equals(2)) {
				temLocGeoShape = true;
				temLocGeoDesenho = false;
				this.opcaoDesenho = false;
			} else if (this.localizacaoGeograficaSelecionada.getIdeClassificacaoSecao().getIdeClassificacaoSecao().equals(3)) {
				temLocGeoShape = false;
				temLocGeoDesenho = true;
				this.opcaoDesenho = true;
			}

			ContextoUtil.getContexto().setLocalizacaoSalva(true);
			desabilitarVertices = false;
			JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso.");
			this.listaArquivo.clear();

		} catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível salvar a localização.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void montarLocalizacao() {
		try {
			this.localizacaoGeograficaSelecionada.setDtcCriacao(new Date());
			this.localizacaoGeograficaSelecionada.setDtcExclusao(null);
			this.localizacaoGeograficaSelecionada.setIndExcluido(Boolean.FALSE);
			this.datumSelecionado = serviceDatum.carregar(this.datumSelecionado.getIdeSistemaCoordenada());
			this.localizacaoGeograficaSelecionada.setIdeSistemaCoordenada(this.datumSelecionado);
			this.secaoGeometricaSelecionada = serviceClassifSecGeometrica.carregar(this.localizacaoGeograficaSelecionada.getIdeClassificacaoSecao()
					.getIdeClassificacaoSecao());
			this.localizacaoGeograficaSelecionada.setIdeClassificacaoSecao(this.secaoGeometricaSelecionada);
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao obter Sistema de Coordenadas");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void obterImovelDaSessao() {
		this.imovelRural = (ImovelRural) getAtributoSession("IMOVEL_RURAL");
		addAtributoSessao("IMOVEL_RURAL", null);
	}

	public void salvarVertice() {
		if (Util.isNullOuVazio(this.fracaoGrauLatitude))
			calculaFracaoGrauLatitude(null);
		if (Util.isNullOuVazio(this.fracaoGrauLongitude))
			calculaFracaoGrauLongitude(null);
		
		if (!Util.isNullOuVazio(this.fracaoGrauLatitude) && !Util.isNullOuVazio(this.fracaoGrauLongitude)) {
			try {
				try {
					if (!localizacaoGeograficaService.validaVerticeMunicipio(this.fracaoGrauLatitude, this.fracaoGrauLongitude, (Empreendimento) getAtributoSession("EMPREENDIMENTO_SESSAO"), null)) {
						JsfUtil.addWarnMessage("Coordenada informada está fora dos limites da Localidade do empreendimento.");
						return;
					}
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				}
				Point ponto = PostgisUtil.criarPonto(Double.parseDouble(this.fracaoGrauLatitude.replace(',', '.')),
						Double.parseDouble(this.fracaoGrauLongitude.replace(',', '.')));
				this.vertice.setCoordGeoNumerica(ponto.toString());
				this.vertice.setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
				verticeService.salvarOuAtualizarVertice(this.vertice);
				if (Util.isNull(localizacaoGeograficaSelecionada.getDadoGeograficoCollection())) {
					localizacaoGeograficaSelecionada.setDadoGeograficoCollection(new ArrayList<DadoGeografico>());
				}
				final List<DadoGeografico> verticeCollection = (List<DadoGeografico>) localizacaoGeograficaSelecionada.getDadoGeograficoCollection();

				this.atualizaVerticeNaLista(verticeCollection);

				JsfUtil.addSuccessMessage("Inclusão efetuada com sucesso!");
				limparVertice();
			} 
			catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				JsfUtil.addErrorMessage(e.getMessage());
				throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
			}
			
		} else {
			RequestContext.getCurrentInstance().addCallbackParam("validationFailed", true);
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

	public void excluirVertice() {
		try {
			verticeService.excluir(verticeExclusao);
			localizacaoGeograficaSelecionada.getDadoGeograficoCollection().remove(verticeExclusao);
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível excluir o vértice.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void carregarVertice() {
		CoordenadaGeografica coordenadaGeografica = GeoUtil.converterPointParaCoordenadaGeografica(vertice.getCoordGeoNumerica());
		selectedModoCoordenada = "1";
		try {
			
			grausLatitude = coordenadaGeografica.getLatitude().getGrau().toString();
			grausLongitude =coordenadaGeografica.getLongitude().getGrau().toString();
			
			minutosLatitude = coordenadaGeografica.getLatitude().getMinuto().toString();
			minutosLongitude = coordenadaGeografica.getLongitude().getMinuto().toString();

			segundosLatitude = coordenadaGeografica.getLatitude().getSegundo().toString();
			segundosLongitude =coordenadaGeografica.getLongitude().getSegundo().toString();
			
			fracaoGrauLatitude = coordenadaGeografica.getLatitude().getAsGD();
			fracaoGrauLongitude = coordenadaGeografica.getLongitude().getAsGD();
		} 
		catch (Exception e) {
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
	}

	public void limparLocGeo() {
		this.localizacaoGeograficaSelecionada = new LocalizacaoGeografica();
		this.imovelRural = new ImovelRural();
	}

	public float getLongitude(DadoGeografico pVertice) {
		if (pVertice.getCoordGeoNumerica().length() > 0 && pVertice.getCoordGeoNumerica().indexOf("(") > 0
				&& pVertice.getCoordGeoNumerica().lastIndexOf(")") > 0) {
			String value = pVertice.getCoordGeoNumerica().substring(pVertice.getCoordGeoNumerica().indexOf("(") + 1,
					pVertice.getCoordGeoNumerica().lastIndexOf(")"));
			String[] v = value.split(" ");
			if (v.length == 2) {
				try {
					return Float.parseFloat(v[1]);
				} catch (Exception e) {
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
				} catch (Exception e) {
				}
			}
		}
		return 0;
	}

	public String getUrl(DadoGeografico pVertice) {
		float longitude = getLongitude(pVertice);
		float latitude = getLatitude(pVertice);
		float a = 0.1f;
		float xmin = longitude - a;
		float xmax = longitude + a;
		float ymin = latitude - a;
		float ymax = latitude + a;
		StringBuilder lUrl = new StringBuilder();
		lUrl.append("http://geobahia.inema.ba.gov.br/i3geo/ms_criamapa.php?");
		lUrl.append("base=../geobahia3/camadasl.map&interface=../geobahia3/interface.htm");
		lUrl.append("&temasa=limiteba C0084 C0083 C0094 C0093 C0372 C0375 C0378 C0376 C0374 C0377");
		lUrl.append("&layers=C0093&pontos="); // Par de coordenadas
		lUrl.append(longitude); // longitude (X)
		lUrl.append(" ");
		lUrl.append(latitude); // latitude (Y)
		lUrl.append("&mapext="); // Valores para a extensÃ£o do mapa
		lUrl.append(xmin); // Longitude mÃ­nima
		lUrl.append(" ");
		lUrl.append(ymin); // Latitude mÃ­nima
		lUrl.append(" ");
		lUrl.append(xmax); // Longitude mÃ¡xima
		lUrl.append(" ");
		lUrl.append(ymax); // Latitude mÃ¡xima

		StringBuilder lReturn = new StringBuilder();
		lReturn.append("window.open('");
		lReturn.append(lUrl.toString());
		lReturn.append("', \"popup\");");
		return lReturn.toString();
	}

	public String criarUrlShape(String nulo) {
		StringBuilder lUrl = new StringBuilder();
		if (!Util.isNullOuVazio(localizacaoGeograficaSelecionada) && !Util.isNullOuVazio(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica()))
			lUrl.append(URLEnum.CAMINHO_GEOBAHIA + "?acao=view&idloc=" + localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica() + "&tipo=1");

		StringBuilder lReturn = new StringBuilder();
		lReturn.append("window.open('");
		lReturn.append(lUrl.toString());
		lReturn.append("');");
		return lReturn.toString();
	}

	public String criarUrlShapeVisualizacao(LocalizacaoGeografica localizacaoGeografica) {
		StringBuilder lUrl = new StringBuilder();
		if(!Util.isNullOuVazio(localizacaoGeografica) && !Util.isNullOuVazio(localizacaoGeografica.getIdeLocalizacaoGeografica()))
			lUrl.append(URLEnum.CAMINHO_GEOBAHIA + "?acao=view&idloc=" + localizacaoGeografica.getIdeLocalizacaoGeografica() + "&tipo=1");
		
		StringBuilder lReturn = new StringBuilder();
		lReturn.append("window.open('");
		lReturn.append(lUrl.toString());
		lReturn.append("');");
		return lReturn.toString();
	}
	
	public void calculaFracaoGrauLatitude(ActionEvent event) {
		if (grausLatitude != null) {
			fracaoGrauLatitude = GeoUtil.converterGMSParaDecimal(GeoUtil.criarCoordenada(grausLatitude,minutosLatitude,segundosLatitude));
		}
	}

	public void calculaFracaoGrauLongitude(ActionEvent event) {
		if (grausLongitude != null) {
			fracaoGrauLongitude  = GeoUtil.converterGMSParaDecimal(GeoUtil.criarCoordenada(grausLongitude,minutosLongitude,segundosLongitude ));
		}
	}

	
	public void selectedSecGeometricaChanged(ValueChangeEvent event) throws AbortProcessingException {
		try {
			String value = null;
			if (event.getNewValue() != null) {
				value = this.extractValue(event.getNewValue().toString());
			} else {
				isPnlVerticesHabilitado = false;
				isUplShapefile = false;
				showIncluirVertice = true;
			}
			if (value != null && !value.isEmpty()) {
				for (SelectItem itemClassifSecGeometrica : itemsClassifSecGeometrica) {
					String newValue = this.extractValue(itemClassifSecGeometrica.getValue().toString());
					if (value.equals(newValue) && itemClassifSecGeometrica.getLabel().equals("Shapefile")) {
						isUplShapefile = true;
						isPnlVerticesHabilitado = false;
						showIncluirVertice = false;
						break;
					} else if (value.equals(newValue) && !itemClassifSecGeometrica.getLabel().equals("Selecione...")) {
						isPnlVerticesHabilitado = true;
						isUplShapefile = false;
						showIncluirVertice = true;
						break;
					}
				}
			}
			secaoGeometricaSelecionada = (ClassificacaoSecaoGeometrica) event.getNewValue();
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}

	public void selectedDatumChanged(ValueChangeEvent event) throws AbortProcessingException {
		this.datumSelecionado = (SistemaCoordenada) event.getNewValue();
	}

	private String extractValue(String newValue) {
		String value = null;
		if (newValue != null && !newValue.toString().trim().isEmpty()) {
			value = newValue.toString().substring(newValue.toString().lastIndexOf("=") + 1, newValue.toString().lastIndexOf(" ")).trim();
		}
		return value;
	}

	public void obterDatums() {
		try {
			datums = serviceDatum.listarDatum();
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
		
	}

	public void obterClassificacoes() {
		try {
			itemsClassifSecGeometrica = new ArrayList<SelectItem>();
			itemsClassifSecGeometrica.add(new SelectItem("", ResourceBundle.getBundle("/Bundle").getString("geral_lbl_selecione")));
			for (ClassificacaoSecaoGeometrica classifSecGeometrica : serviceClassifSecGeometrica.listarClassificacaoSecaoGeometrica()) {
				itemsClassifSecGeometrica.add(new SelectItem(classifSecGeometrica, classifSecGeometrica.getNomClassificacaoSecao()));
			}
		} 
		catch (Exception e) {
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
					} 
					catch (Exception e) {
						Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
						throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
					}
				}
				caminhoArquivo = FileUploadUtil.EnviarShape(event, DiretorioArquivoEnum.SHAPEFILES.toString() + FileUploadUtil.getFileSeparator()
						+ localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica().toString(), localizacaoGeograficaSelecionada
						.getIdeLocalizacaoGeografica().toString() + "_" + localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().getSrid());
				listaArquivo.add(FileUploadUtil.getFileName(caminhoArquivo));
				temArquivo = true;
				JsfUtil.addSuccessMessage("Arquivo Enviado com Sucesso.");
			} else
				JsfUtil.addWarnMessage("Para inserir os shapes é preciso salvar a Localização Geográfica!");
		} else
			JsfUtil.addWarnMessage("Não é possível enviar mais de 3 arquivos.");
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
		} else
			return true;
	}

	public void excluirArquivo() {
		if (listaArquivo.size() > 0) {
			listaArquivo.remove(arqDeExclusao);
			JsfUtil.addSuccessMessage("Arquivo Removido com Sucesso.");
		} else {
			temArquivo = false;
			JsfUtil.addWarnMessage("Não é possível excluir este arquivo.");
		}
		if (listaArquivo.isEmpty())
			temArquivo = false;
	}

	public void persistirShapes(Integer pTipoPersitencia) {
		try {
			atualizarGeoReferenciavel();
			
			Integer idDosParametros;
			String[] retorno;

			if (Util.isNullOuVazio(imovelRural))
				imovelRural = ContextoUtil.getContexto().getImovelRural();

			if (pTipoPersitencia.equals(1)) {
				// Persistência por upload
				localizacaoGeograficaSelecionada = localizacaoGeograficaService.carregarSomenteComSistemaCoordenada(localizacaoGeograficaSelecionada
						.getIdeLocalizacaoGeografica());
				
				if(!Util.isNull(imovelRural)){
				
					retorno = paramPersistDadoGeoService.salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, false, imovelRural.getImovel()
						.getIdeEndereco().getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio(), false,1);
				
				}else{
					Processo processo = processoService.carregarProcesso(getGeoReferenciavel().getIdeProcesso().getIdeProcesso());
					Municipio municipio = municipioService.buscarCoordGeoMunicipioPorRequerimento(processo.getIdeRequerimento().getIdeRequerimento());
					retorno = paramPersistDadoGeoService.salvarParamsEPersistirShapeTheGeom(localizacaoGeograficaSelecionada, false, municipio.getCoordGeobahiaMunicipio(), false,1);
					
				}
				
			} else {
				// Persistência por desenho
				idDosParametros = paramPersistDadoGeoService.carregarSomenteComParamPersistDadoGeo(localizacaoGeograficaSelecionada
						.getIdeLocalizacaoGeografica());
				if (!Util.isNullOuVazio(idDosParametros)) {
					retorno = paramPersistDadoGeoService.salvarParamsEPersistindoDesenhoTheGeom(idDosParametros, imovelRural.getImovel().getIdeEndereco()
							.getIdeLogradouro().getIdeMunicipio().getCoordGeobahiaMunicipio());
				} else
					retorno = new String[] { "ERRO", "9999", "Erro interno. Contacte o Suporte técnico!" };
			}

			if (retorno.length > 0) {
				this.existeTheGeom = retorno[0].equalsIgnoreCase("OK") ? true : false;
				String comp = retorno[0].equalsIgnoreCase("ERRO") ? retorno[2] + " [" + retorno[1] + "]" : "Importação realizada com sucesso!";
				System.out.println(comp);
				JsfUtil.addSuccessMessage(comp);
			}

			/*
			 * localizacaoGeograficaSelecionada =
			 * localizacaoGeograficaService.carregarSomenteComSistemaCoordenada
			 * (localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica());
			 * Integer idDosParametros =
			 * paramPersistDadoGeoService.salvarParamsEPersistindoShapeTheGeom
			 * (localizacaoGeograficaSelecionada);
			 * if(Util.isNullOuVazio(idDosParametros)){
			 * System.out.println("ERRO AO SALVAR PARAMETROS");
			 * JsfUtil.addWarnMessage(
			 * "Erro ao persistir os shape no banco!\n Apenas foi realizado o Upload."
			 * ); } else{
			 * System.out.println("PARAMETROS persistido com sucesso!");
			 * JsfUtil.addSuccessMessage("Shape persistido com sucesso!");
			 * habilitaLinkVisualizaShapeFile = true; }
			 */
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			System.out.println("ERRO AO SALVAR PARAMETROS");
			JsfUtil.addWarnMessage("Erro ao persistir os shape no banco!\n Apenas foi realizado o Upload.");
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
		
	}

	public void salvarGeoReferenciavel() throws  Exception {
		GeoReferenciavel geo = getGeoReferenciavel();
		geo.setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
		localizacaoGeograficaService.salvarGeoReferenciavel(geo);
	}
	
	public void atualizarGeoReferenciavel() {
		try {
			GeoReferenciavel geo = getGeoReferenciavel();
			geo.setDtcCriacao(new Date());
			geo.setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
			localizacaoGeograficaService.atualizarGeoReferenciavel(geo);
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}		
	}

	public void limpaFormLocGeo() {
		listaArquivo = new ArrayList<String>();
		existeTheGeom = false;
	}

	public void excluirShape(Integer pIdeLocalizacaoGeografica) {
		try {
			this.localizacaoGeograficaSelecionada.setDadoGeograficoCollection(null);
			localizacaoGeograficaService.excluirDadosPersistidos(pIdeLocalizacaoGeografica);
			JsfUtil.addSuccessMessage("Exclusão efetuada com Sucesso.");
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao tentar excluir o shape.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}
	}

	public void verificaTipoSecaoGeometricaShape() throws Exception {
		if (this.localizacaoGeograficaSelecionada.getIdeClassificacaoSecao().getIdeClassificacaoSecao() == 3) {
			this.opcaoDesenho = true;
			this.datumSelecionado = this.serviceDatum.carregar(4);
		} else {
			this.opcaoDesenho = false;
			this.datumSelecionado = this.serviceDatum.carregar(0);
		}
	}

	public Collection<SistemaCoordenada> getDatums() {
		// Remoção do código que retirava as opções de UTM
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

	public List<SelectItem> getItemsClassifSecGeometrica() {
		return itemsClassifSecGeometrica;
	}

	public void setItemsClassifSecGeometrica(List<SelectItem> itemsClassifSecGeometrica) {
		this.itemsClassifSecGeometrica = itemsClassifSecGeometrica;
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
		Exception erro = null;
		try {
			if (!Util.isNullOuVazio(localizacaoGeograficaSelecionada.getDadoGeograficoCollection())
					&& !localizacaoGeograficaSelecionada.getDadoGeograficoCollection().isEmpty()) {
				if (localizacaoGeograficaSelecionada.getIdeClassificacaoSecao().getIdeClassificacaoSecao()
						.equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId())) {
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
			}
		} catch (Exception e) {
			erro =e;
			try {
				localizacaoGeograficaSelecionada = localizacaoGeograficaService.carregar(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica());
				if (!Util.isNullOuVazio(localizacaoGeograficaSelecionada.getDadoGeograficoCollection())
						&& !localizacaoGeograficaSelecionada.getDadoGeograficoCollection().isEmpty()) {
					if (localizacaoGeograficaSelecionada.getIdeClassificacaoSecao().getIdeClassificacaoSecao()
							.equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId())) {
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
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e1);
			}
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
		return mostraLista;
	}

	public void setMostraLista(Boolean mostraLista) {
		this.mostraLista = mostraLista;
	}

	public ImovelRural getImovelRural() {
		return imovelRural;
	}

	public void setImovelRural(ImovelRural imovelRural) {
		this.imovelRural = imovelRural;
		try {
			carregarLocalizacao();
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			throw Util.capturarException(e,Util.SEIA_EXCEPTION); 
		}		
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
			} else
				desabilitaIncluirVerticeLocGeoImovel = true;
		} else
			desabilitaIncluirVerticeLocGeoImovel = true;

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
		Exception erro = null;
		try {
			return localizacaoGeograficaService.verificaSeExisteTheGeomValido(getLocalizacaoGeograficaSelecionada().getIdeLocalizacaoGeografica());
		} catch (Exception e) {
			erro = e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return false;
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}

	/**
	 * @param existeTheGeom
	 *            the existeTheGeom to set
	 */
	public void setExisteTheGeom(Boolean existeTheGeom) {
		this.existeTheGeom = existeTheGeom;
	}

	public boolean isTemLocGeoShape() {
		return temLocGeoShape;
	}

	public void setTemLocGeoShape(boolean temLocGeoShape) {
		this.temLocGeoShape = temLocGeoShape;
	}

	public boolean isTemLocGeoDesenho() {
		return temLocGeoDesenho;
	}

	public void setTemLocGeoDesenho(boolean temLocGeoDesenho) {
		this.temLocGeoDesenho = temLocGeoDesenho;
	}

	public Boolean getOpcaoDesenho() {
		return opcaoDesenho;
	}

	public void setOpcaoDesenho(Boolean opcaoDesenho) {
		this.opcaoDesenho = opcaoDesenho;
	}

	public String getCoordenadaPontoxy() {
		return coordenadaPontoxy;
	}

	public void setCoordenadaPontoxy(String coordenadaPontoxy) {
		this.coordenadaPontoxy = coordenadaPontoxy;
	}

	public Boolean getLocGeoIsPonto() {
		return locGeoIsPonto;
	}

	public void setLocGeoIsPonto(Boolean locGeoIsPonto) {
		this.locGeoIsPonto = locGeoIsPonto;
	}

	public List<ClassificacaoSecaoGeometrica> getListaSecaoGeomerica() {
		return listaSecaoGeomerica;
	}

	public void setListaSecaoGeomerica(List<ClassificacaoSecaoGeometrica> listaSecaoGeomerica) {
		this.listaSecaoGeomerica = listaSecaoGeomerica;
	}

	public String getCaminhoDesenharGeoBahia() {
		return caminhoDesenharGeoBahia;
	}

	public void setCaminhoDesenharGeoBahia(String caminhoDesenharGeoBahia) {
		this.caminhoDesenharGeoBahia = caminhoDesenharGeoBahia;
	}

	public boolean isShape() {
		return shape;
	}

	public void setShape(boolean shape) {
		if(shape)
			this.localizacaoGeograficaSelecionada.setIdeClassificacaoSecao(new ClassificacaoSecaoGeometrica(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId()));
		this.shape = shape;
	}

	public GeoReferenciavel getGeoReferenciavel() {
		return geoReferenciavel;
	}

	public void setGeoReferenciavel(GeoReferenciavel geoReferenciavel) {
		this.geoReferenciavel = geoReferenciavel;
		this.localizacaoGeograficaSelecionada = geoReferenciavel.getIdeLocalizacaoGeografica();
		if(Util.isNull(this.localizacaoGeograficaSelecionada.getDadoGeograficoCollection())){
			this.temLocGeoShape =false;
		}else{
			this.temLocGeoShape =true;
		}
		this.listaArquivo = new ArrayList<String>();
		this.temArquivo = false;
		this.getExisteTheGeom();
		this.datumSelecionado = localizacaoGeograficaSelecionada.getIdeSistemaCoordenada();
	}

}