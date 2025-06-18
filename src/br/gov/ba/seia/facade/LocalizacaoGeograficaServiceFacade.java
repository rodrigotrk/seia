package br.gov.ba.seia.facade;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;

import com.vividsolutions.jts.geom.Point;

import br.gov.ba.seia.dao.DAOFactory;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.ParamPersistDadoGeo;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.enumerator.SistemaCoordenadaEnum;
import br.gov.ba.seia.enumerator.TipoIntervencaoEnum;
import br.gov.ba.seia.exception.SeiaLocalizacaoGeograficaException;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.service.ParamPersistDadoGeoService;
import br.gov.ba.seia.service.ValidacaoGeoSeiaService;
import br.gov.ba.seia.service.VerticeService;
import br.gov.ba.seia.util.BigDecimalUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.PostgisUtil;
import br.gov.ba.seia.util.Util;
	
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LocalizacaoGeograficaServiceFacade {

	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	@EJB
	private EmpreendimentoService empreendimentoService;
	@EJB
	private VerticeService verticeService;
	@EJB
	private ParamPersistDadoGeoService paramPersistDadoGeoService;
	@EJB
	private ValidacaoGeoSeiaService validacaoGeoSeiaService;


	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarVerticeUTMComUnidadeConservacao(Map<String,Object> params) throws NumberFormatException, Exception  {

		String latitudeUTM = (String) params.get("latitudeUTM");
		String longitudeUTM = (String) params.get("longitudeUTM");
		LocalizacaoGeografica localizacaoGeograficaSelecionada = (LocalizacaoGeografica) params.get("localizacaoGeograficaSelecionada");
		DadoGeografico vertice = (DadoGeografico) params.get("dadoGeografico");
		Empreendimento empreendimento = empreendimentoService.buscarEmpreendimentoPorIde(((Empreendimento) params.get("empreendimento")).getIdeEmpreendimento());

		salvarVerticeUTM(latitudeUTM, longitudeUTM, localizacaoGeograficaSelecionada, vertice, empreendimento);

		if(localizacaoGeograficaService.isUnidadeConservacao(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica())){
			empreendimento.setIndUnidadeConservacao(true);
		}
		else {
			empreendimento.setIndUnidadeConservacao(false);
		}
		empreendimentoService.atualizar(empreendimento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarVerticeUTM(String latitudeUTM, String longitudeUTM, LocalizacaoGeografica localizacaoGeograficaSelecionada, DadoGeografico vertice, Empreendimento empreendimento) throws NumberFormatException, Exception  {

		validarVerticeUTM(latitudeUTM, longitudeUTM);

		validarLocalizacao(null, null, null, null, latitudeUTM, longitudeUTM, localizacaoGeograficaSelecionada, null, empreendimento);

		Point ponto = PostgisUtil.criarPonto(Double.parseDouble(latitudeUTM.toString().replace(',', '.')), Double.parseDouble(longitudeUTM.toString().replace(',', '.')) );

		vertice.setCoordGeoNumerica(ponto.toString());
		vertice.setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);

		localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().setSrid(SistemaCoordenadaEnum.getEnum(localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().getIdeSistemaCoordenada()).getSrid());
		verticeService.salvarVertice(vertice, localizacaoGeograficaSelecionada.getIdeSistemaCoordenada());

	}

	public void validarVerticeUTM(String longitudeUTM, String latitudeUTM) throws SeiaLocalizacaoGeograficaException {
		if(latitudeUTM != null && !latitudeUTM.isEmpty() && latitudeUTM.length() < 7){
			throw new SeiaLocalizacaoGeograficaException("A latitude em UTM, na Bahia, não tem valores menores que 1.000.000.");
		}
		if(longitudeUTM != null && !longitudeUTM.isEmpty() && longitudeUTM.length() < 6){
			throw new SeiaLocalizacaoGeograficaException("A longitude em UTM, na Bahia, não tem valores menores que 100.000.");
		}
	}


	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void salvarVerticeComUnidadeConservacao(Map<String, Object> params) throws Exception  {

		String fracaoGrauLatitudeDecimal = (String) params.get("fracaoGrauLatitudeDecimal");
		String fracaoGrauLongitudeDecimal = (String) params.get("fracaoGrauLongitudeDecimal");
		String fracaoGrauLatitude = (String) params.get("fracaoGrauLatitude");
		String fracaoGrauLongitude = (String) params.get("fracaoGrauLongitude");
		String modoCoordenada = (String) params.get("modoCoordenada");
		LocalizacaoGeografica localizacaoGeograficaSelecionada = (LocalizacaoGeografica) params.get("localizacaoGeograficaSelecionada");
		DadoGeografico vertice = (DadoGeografico) params.get("dadoGeografico");
		Empreendimento empreendimento = empreendimentoService.buscarEmpreendimentoPorIde(((Empreendimento) params.get("empreendimento")).getIdeEmpreendimento());

		localizacaoGeograficaService.salvarOuAtualizarLocalizacaoGeografica(localizacaoGeograficaSelecionada);

		salvarVertice(fracaoGrauLatitudeDecimal, fracaoGrauLongitudeDecimal, fracaoGrauLatitude, fracaoGrauLongitude, modoCoordenada, empreendimento,
				localizacaoGeograficaSelecionada, vertice);

		if (localizacaoGeograficaService.isUnidadeConservacao(localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica())) {
			empreendimento.setIndUnidadeConservacao(true);
		} else {
			empreendimento.setIndUnidadeConservacao(false);
		}

		empreendimentoService.atualizar(empreendimento);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarVertice(String fracaoGrauLatitudeDecimal, String fracaoGrauLongitudeDecimal, String fracaoGrauLatitude, String fracaoGrauLongitude, String modoCoordenada,
			Empreendimento empreendimento, LocalizacaoGeografica localizacaoGeograficaSelecionada, DadoGeografico vertice) throws NumberFormatException, Exception  {

		validarLocalizacao(fracaoGrauLatitudeDecimal, fracaoGrauLongitudeDecimal, fracaoGrauLatitude, fracaoGrauLongitude, null, null, localizacaoGeograficaSelecionada,
				modoCoordenada, empreendimento);

		Point ponto = PostgisUtil.criarPonto(Double.parseDouble(fracaoGrauLatitude.replace(',', '.')), Double.parseDouble(fracaoGrauLongitude.replace(',', '.')));
		vertice.setCoordGeoNumerica(ponto.toString());
		vertice.setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);

		if (vertice.getIdeDadoGeografico() == null) {
			verticeService.salvarOuAtualizarVertice(vertice);
		} else {
			verticeService.mergeVertice(vertice);
		}

		paramPersistDadoGeoService.salvarParamsEPersistindoVerticeTheGeomJava(vertice, localizacaoGeograficaSelecionada.getIdeSistemaCoordenada());
	}

	private void validarLocalizacao(String fracaoGrauLatitudeDecimal, String fracaoGrauLongitudeDecimal,
			String fracaoGrauLatitude,String fracaoGrauLongitude, String longitudeUTM , String latitudeUTM,
			LocalizacaoGeografica localizacaoGeograficaSelecionada,String modoCoordenada, Empreendimento empreendimento) throws SeiaLocalizacaoGeograficaException  {

		if(Util.isNull(latitudeUTM) && Util.isNull(longitudeUTM)) {

			if(modoCoordenada.equalsIgnoreCase("2")){
				if(Util.isNullOuVazio(fracaoGrauLatitudeDecimal) || Util.isNullOuVazio(fracaoGrauLongitudeDecimal)) {
					throw new SeiaLocalizacaoGeograficaException("Coordenada Decimal não pode ser vazia!!");
				}
				if(!localizacaoGeograficaService.validaVerticeMunicipio("-"+fracaoGrauLatitudeDecimal, "-"+fracaoGrauLongitudeDecimal, empreendimento, null)){
					throw new SeiaLocalizacaoGeograficaException("Coordenada informada está fora dos limites da Localidade do empreendimento.");
				}
			}
			else{
				if(!localizacaoGeograficaService.validaVerticeMunicipio(fracaoGrauLatitude, fracaoGrauLongitude, empreendimento, null)){
					throw new SeiaLocalizacaoGeograficaException("Coordenada informada está fora dos limites da Localidade do empreendimento.");
				}
			}
		}
		else {
			if (!localizacaoGeograficaService.validaVerticeMunicipio(latitudeUTM,longitudeUTM, empreendimento,SistemaCoordenadaEnum.getEnum(localizacaoGeograficaSelecionada.getIdeSistemaCoordenada().getIdeSistemaCoordenada()).getSrid())) {
				throw new SeiaLocalizacaoGeograficaException("Coordenada informada está fora dos limites da Localidade do empreendimento.");
			}
		}

	}

	public List<DadoGeografico> listarDadoGeografico(LocalizacaoGeografica localizacaoGeografica, Integer ideClassificacaoSecao) {
		return localizacaoGeograficaService.listarDadoGeografico(localizacaoGeografica, ideClassificacaoSecao);
	}

	public void excluirDadoGeografico(LocalizacaoGeografica localizacaoGeografica) {
		localizacaoGeograficaService.excluirDadosPersistidos(localizacaoGeografica.getIdeLocalizacaoGeografica());
	}
	// GEOBAHIA
	/**
	 * Método que vai tratar os pontos de {@link LocalizacaoGeografica} daquela lista de {@link OutorgaLocalizacaoGeografica}
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param listaOutorgaLocGeo
	 * @
	 * @since 08/04/2015
	 */
	public void tratarPontosOutorgaLocalizacaoGeografica(List<OutorgaLocalizacaoGeografica> listaOutorgaLocGeo) {
		for(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : listaOutorgaLocGeo){
			tratarPontoLocalizacaoGeografica(outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica());
		}
	}

	/**
	 * Método que vai tratar os pontos da {@link LocalizacaoGeografica} daquela lista de {@link FceOutorgaLocalizacaoGeografica}
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param listaFceOutorgaLocGeo
	 * @
	 * @since 12/01/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/">#</a> #ticket
	 */
	public void tratarPontosFceOutorgaLocalizacaoGeografica(List<FceOutorgaLocalizacaoGeografica> listaFceOutorgaLocGeo) {
		for(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica : listaFceOutorgaLocGeo){
			if(Util.isNullOuVazio(fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica())){
				tratarPontoLocalizacaoGeografica(fceOutorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica());
			}
			else {
				tratarPontoLocalizacaoGeografica(fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica());
			}
			fceOutorgaLocalizacaoGeografica.setConfirmado(true);
		}
	}

	protected boolean isPonto(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica) {
		return !Util.isNullOuVazio(fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica().getIdeClassificacaoSecao()) && fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao().equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId());
	}
	
	public void tratarPontoLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) {
		tratarPonto(localizacaoGeografica);
		tratarBaciaAndSubBaciaFromCefir(localizacaoGeografica);
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param locGeo
	 * @since 13/04/2015
	 */
	public void tratarPonto(LocalizacaoGeografica locGeo) {
		
		if(Util.isNullOuVazio(locGeo.getDadoGeograficoCollection())){
			locGeo.setDadoGeograficoCollection(getDadoGeograficoCollection(locGeo));
		}
		
		for(DadoGeografico dadoGeografico : locGeo.getDadoGeograficoCollection()){
			Map<String, String> pontos = new HashMap<String, String>();
			if(dadoGeografico.getIdeLocalizacaoGeografica().getIdeClassificacaoSecao().getIdeClassificacaoSecao().equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId())){
				pontos = getPonto(dadoGeografico);
				locGeo.setLatitudeInicial(pontos.get("latitude"));
				locGeo.setLongitudeInicial(pontos.get("longitude"));
			}
		}
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param locGeo
	 * @return
	 * @
	 * @since 15/02/2016
	 */
	public List<DadoGeografico> getDadoGeograficoCollection(LocalizacaoGeografica locGeo){
		try {
			return localizacaoGeograficaService.listarDadoGeografico(locGeo, ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " os dados geográficos.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public Map<String, String> getPonto(DadoGeografico ponto) {
		if (isDadoGeograficoPonto(ponto)) {
			String[] latitudeAndLongitude = separarParDePonto(ponto.getCoordGeoNumerica().substring(separarLatitude(ponto), separarLongitude(ponto)));
			if (temDoisPontos(latitudeAndLongitude)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("latitude", latitudeAndLongitude[0]);
				map.put("longitude", latitudeAndLongitude[1]);
				return map;
			}
		}
		return null;
	}

	private boolean temDoisPontos(String[] v) {
		return v.length == 2;
	}

	private String[] separarParDePonto(String value) {
		return value.split(" ");
	}

	private int separarLongitude(DadoGeografico pVertice) {
		return pVertice.getCoordGeoNumerica().lastIndexOf(")");
	}

	private int separarLatitude(DadoGeografico pVertice) {
		return pVertice.getCoordGeoNumerica().indexOf("(") + 1;
	}

	private boolean isDadoGeograficoPonto(DadoGeografico pVertice) {
		return pVertice.getCoordGeoNumerica().length() > 0 && pVertice.getCoordGeoNumerica().indexOf("(") > 0 && separarLongitude(pVertice) > 0;
	}

	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void tratarBaciaAndSubBaciaFromCefir(LocalizacaoGeografica locGeo)  {
		String latitude = "";
		String longitude = "";
		String srid = locGeo.getIdeSistemaCoordenada().getSrid();
		
		/*// O par de ponto é salvo ao contrário quando o Sistema de Coordenada escolhido é UTM23 ou UTM24 (SAD 69 e SIRGAS 2000)
		if(srid.equals(SistemaCoordenadaEnum.GEOGRAFICA_SAD69.getSrid().toString())
				|| srid.equals(SistemaCoordenadaEnum.GEOGRAFICA_SIRGAS_2000.getSrid().toString())){
			latitude = locGeo.getLatitudeInicial();
			longitude = locGeo.getLongitudeInicial();
		}
		else {
			// Invertem-se os valores para fazer a busca pela função sp_info_outorga
			latitude = locGeo.getLongitudeInicial();
			longitude = locGeo.getLatitudeInicial();
		}*/
		
		latitude = locGeo.getLatitudeInicial();
		longitude = locGeo.getLongitudeInicial();
		
		List<Object[]> listaObj = null;
		listaObj = DAOFactory.getEntityManager().createNativeQuery("SELECT * FROM sp_info_outorga(" + latitude + "," + longitude + "," + srid +")").getResultList();
		if (!Util.isNullOuVazio(listaObj)) {
			for (Object[] resultElement : listaObj) {
				locGeo.setBaciaHidrografica((String) resultElement[0]);
				locGeo.setSubBacia((String) resultElement[1]);
				locGeo.setRpga((String) resultElement[3]);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public String getBacia(LocalizacaoGeografica localizacaoGeografica){
		List<Object[]> listaObj = null;
		listaObj = DAOFactory.getEntityManager().createNativeQuery("SELECT * FROM sp_get_bacia(" + localizacaoGeografica.getIdeLocalizacaoGeografica() +")").getResultList();
		if (!Util.isNullOuVazio(listaObj)) {
			return concatenarString(listaObj);
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	public String getSubBacia(LocalizacaoGeografica localizacaoGeografica){
		List<Object[]> listaObj = DAOFactory.getEntityManager().createNativeQuery("SELECT * FROM sp_get_subbacia(" + localizacaoGeografica.getIdeLocalizacaoGeografica() +")").getResultList();
		if (!Util.isNullOuVazio(listaObj)) {
			return concatenarString(listaObj);
		}
		return "--";

	}

	@SuppressWarnings("unchecked")
	public String getRPGA(LocalizacaoGeografica localizacaoGeografica){
		List<Object[]> listaObj = DAOFactory.getEntityManager().createNativeQuery("SELECT * FROM sp_get_rpga(" + localizacaoGeografica.getIdeLocalizacaoGeografica() +")").getResultList();
		if (!Util.isNullOuVazio(listaObj)) {
			return concatenarString(listaObj);
		}
		return "--";
	}
	
	@SuppressWarnings("unchecked")
	public String getSistemaAquifero(LocalizacaoGeografica localizacaoGeografica){
		List<Object[]> listaObj = DAOFactory.getEntityManager().createNativeQuery("SELECT * FROM sp_get_hidrogeologia(" + localizacaoGeografica.getIdeLocalizacaoGeografica() +")").getResultList();
		if (!Util.isNullOuVazio(listaObj)) {
			return concatenarString(listaObj);
		}
		return "--";
	}
	
	private String concatenarString(List<Object[]> listaObj){
		String informacaoOutorga = "";
		for (Object dado : listaObj) {
			if(informacaoOutorga.equals("")){
				informacaoOutorga = (String) dado;
			}
			else {
				informacaoOutorga.concat(", " + (String) dado);
			}
		}
		return informacaoOutorga;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Double retornarAreaShape(LocalizacaoGeografica locGeo) throws Exception {
		return validacaoGeoSeiaService.retonarAreaDoShapeByGeometria(retornarGeometriaShapeByLocalizacaoGeografica(locGeo));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String retornarGeometriaShapeByLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) throws Exception  {
		return validacaoGeoSeiaService.buscarGeometriaShape(localizacaoGeografica.getIdeLocalizacaoGeografica());
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isTipoGeometriaPonto(Integer ideLocalizacao, String geometria) throws Exception {
		return validacaoGeoSeiaService.validaTipoGeometriaPonto(ideLocalizacao, geometria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isSobrePosicaoCompleta(LocalizacaoGeografica locGeoPrincipal, LocalizacaoGeografica locGeoContida, Double porcentagemContida) throws Exception {
		return validacaoGeoSeiaService.verificarSobreposicaoCompleta(locGeoPrincipal, locGeoContida, porcentagemContida); 
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Double retornaPercentualSobreposicao(LocalizacaoGeografica locGeoPrincipal, LocalizacaoGeografica locGeoContida) throws Exception  {
		return validacaoGeoSeiaService.retornaPercentualSobreposicao(locGeoPrincipal, locGeoContida);
	}

	/**
	 * Método que verifica se a área informada tem o mesmo valor que a área do Shape persistido.
	 *
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @param areaInformada
	 * @param shapeInserido
	 * @return <i>true</i> se valor da 'areaInformada' for igual a área do 'shapeInserido'
	 * @throws Exception 
	 * @
	 * @since 04/03/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7479">#7479</a>
	 */
	public boolean isAreaInformadaDeMesmoTamanhoDoShapeInserido(BigDecimal areaInformada, LocalizacaoGeografica shapeInserido) throws Exception {
		BigDecimal areaShape = BigDecimalUtil.aproximar(retornarAreaShape(shapeInserido), 4);
		return (areaShape.compareTo(areaInformada) == 0);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public LocalizacaoGeografica duplicarLocalizacaoGeografica(LocalizacaoGeografica locGeo) {
		return localizacaoGeograficaService.duplicarLocalizacaoGeografica(locGeo);
	}
	
	/**
	 * 
	 * Método que retorna os ide_localizacao_geografica sobrepostos pela theGeom. 
	 *
	 * @author ivanildo.souza
	 *
	 * @param theGeom
	 * @return Collection
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Integer> getCollectionLocalizacaoGeograficaSobrepostaBy(String theGeom, Integer ideImovel) {
		return validacaoGeoSeiaService.getCollectionIdeLocalizacaoGeograficaSobrepostaBy(theGeom, ideImovel);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean naoExisteSobreposicao(LocalizacaoGeografica locGeoA, LocalizacaoGeografica locGeoB) throws Exception  {
		return !validacaoGeoSeiaService.isExisteIntersecao(locGeoA, locGeoB);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isLocalizacaoGeograficaSomenteNaBahia(LocalizacaoGeografica locGeo)  {
		return validacaoGeoSeiaService.isLocalizacaoGeograficaSomenteNaBahia(locGeo);
	}
	
	/**
	 * Método para verificar se o the_geom da primeira {@link LocalizacaoGeografica} intercepta o the_geom da segunda.
	 * 
	 * @author eduardo.fernandes 
	 * @since 23/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8434">#8434</a>
	 * @param locGeoA
	 * @param locGeoB
	 * @return
	 * @throws Exception 
	 * @ 
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isTheGeomIgual(LocalizacaoGeografica locGeoA, LocalizacaoGeografica locGeoB) throws Exception  {
		return !locGeoA.getIdeLocalizacaoGeografica().equals(locGeoB.getIdeLocalizacaoGeografica()) 
				&& validacaoGeoSeiaService.isTheGeomIgual(locGeoA, locGeoB);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirTudoPorLocalizacaoGeografica(LocalizacaoGeografica locGeo) {
		try {
			localizacaoGeograficaService.excluirTudoPorLocalizacaoGeografica(locGeo);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public List<ParamPersistDadoGeo> listarParamPersistDadoGeoPorLocalizacaoGeografica(LocalizacaoGeografica locGeo) {
		return paramPersistDadoGeoService.listarPorLocalizacaoGeografica(locGeo);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Double> listarCodIBGE(Integer ideLocalizacaoGeografica) {
		return localizacaoGeograficaService.listarCodIBGE(ideLocalizacaoGeografica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<LocalizacaoGeografica> listarLocalizacaoGeograficaBy(Processo ideProcesso, ModalidadeOutorgaEnum modalidadeOutorgaEnum, TipoIntervencaoEnum tipoIntervencaoEnum)  {
		return localizacaoGeograficaService.listarLocalizacaoGeograficaBy(ideProcesso, modalidadeOutorgaEnum, tipoIntervencaoEnum);
	}
	
	public BigDecimal getDistanciaEntrePontos(LocalizacaoGeografica locGeoA, LocalizacaoGeografica locGeoB)  {
		return localizacaoGeograficaService.getDistanciaEntrePontos(locGeoA, locGeoB);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public LocalizacaoGeografica carregarPorIdeLocalizacao(Integer ideLocGeo) {
		return localizacaoGeograficaService.carregar(ideLocGeo);
	}
}