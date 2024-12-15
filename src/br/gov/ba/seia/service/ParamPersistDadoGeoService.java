package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.LazyInitializationException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import com.vividsolutions.jts.geom.Point;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.CoordenadaGeografica;
import br.gov.ba.seia.entity.DadoGeografico;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.ParamPersistDadoGeo;
import br.gov.ba.seia.entity.SistemaCoordenada;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.SistemaCoordenadaEnum;
import br.gov.ba.seia.enumerator.URLEnum;
import br.gov.ba.seia.exception.CampoObrigatorioException;
import br.gov.ba.seia.util.AcessarURL;
import br.gov.ba.seia.util.GeoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.PostgisUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ParamPersistDadoGeoService {
	@Inject
	IDAO<ParamPersistDadoGeo> persisteTheGeomDAO;
	@Inject
	IDAO<LocalizacaoGeografica> localizacaoGeoDAO;
	@Inject
	IDAO<SistemaCoordenada> siscoordDAO;
	@Inject
	IDAO<DadoGeografico> dadoGeograficoDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizarParamPersistDadoGeo(ParamPersistDadoGeo paramPersistDadoGeo) {
		persisteTheGeomDAO.salvarOuAtualizar(paramPersistDadoGeo);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarParamPersistDadoGeo(ParamPersistDadoGeo paramPersistDadoGeo) {
		persisteTheGeomDAO.salvar(paramPersistDadoGeo);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(ParamPersistDadoGeo paramPersistDadoGeo)  {
		persisteTheGeomDAO.remover(paramPersistDadoGeo);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public ParamPersistDadoGeo carregar(ParamPersistDadoGeo paramPersistDadoGeo){
		return persisteTheGeomDAO.carregarLoad(paramPersistDadoGeo.getIdeParamPersistDadoGeo());
	}

	/**
	 * @author micael.coutinho
	 * @param vertice
	 * @param sisCoord
	 * @return Integer
	 * @throws Exception 
	 * @throws NumberFormatException 
	 * @
	 */

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer salvarParamsEPersistindoVerticeTheGeom(DadoGeografico vertice, SistemaCoordenada sisCoord) throws NumberFormatException, Exception  {
		ParamPersistDadoGeo params = new ParamPersistDadoGeo();
 
		if(isUTM(sisCoord)) {
			// Separa latitude de logitude, onde latitude é o primeiro valor é longitude o segundo valor
		
			String tmp = vertice.getCoordGeoNumerica().replaceAll("POINT ", "");
			tmp = tmp.replace("(", "").replace(")", "");
			
			String latitudeUTM = tmp.split(" ")[0];
			latitudeUTM = latitudeUTM.toString().replace(',', '.');
			
			String longitudeUTM = tmp.split(" ")[1];
			longitudeUTM = longitudeUTM.toString().replace(',', '.');
			
			Point ponto = PostgisUtil.criarPonto(Double.parseDouble(latitudeUTM), Double.parseDouble(longitudeUTM));
			vertice.setCoordGeoNumerica(ponto.toString());
			
			params.setLatitude(latitudeUTM);
			params.setLongitude(longitudeUTM);
		} else {
			CoordenadaGeografica coordenadaGeografica = GeoUtil.converterPointParaCoordenadaGeografica(vertice.getCoordGeoNumerica());
			params.setLatitude(coordenadaGeografica.getLatitude().getAsGD());
			params.setLongitude(coordenadaGeografica.getLongitude().getAsGD());
		}

		params.setIdeDadoGeografico(vertice.getIdeDadoGeografico());
		
		if(Util.isNullOuVazio(sisCoord.getSrid())) {
			sisCoord = siscoordDAO.carregarGet(sisCoord.getIdeSistemaCoordenada());
		}
		
		params.setSrid(sisCoord.getSrid());
		
		try {
			salvarParamPersistDadoGeo(params);
			
			//Chama a url que executará o sistema php para persistir a latitudo e longitude na coluna the_geom
			Map<String, String> parametros = new HashMap<String, String>();
			parametros.put("id", params.getIdeParamPersistDadoGeo().toString());
			
			if(AcessarURL.callAppGeoSeia(URLEnum.CAMINHO_GEOSEIA_IMPORTAR.getUrl(parametros))) {
				System.out.println("EXECUÇÃO DO GEOSEIA RETORNOU OK!");
			} else {
				System.out.println("EXECUÇÃO DO GEOSEIA MAL SUCESSEDIDA!*****");
			}

			return params.getIdeParamPersistDadoGeo();
		} catch (Exception e) {
			System.out.println("*************INICIO DO ERRO AO SALVAR LOCALIZAÇÃO!");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			System.out.println("*************FIM DO ERRO AO SALVAR LOCALIZAÇÃO!");
			
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarParamsEPersistindoVerticeTheGeomJava(DadoGeografico vertice, SistemaCoordenada sisCoord)  {
		ParamPersistDadoGeo params = new ParamPersistDadoGeo();
		
		CoordenadaGeografica coordenadaGeografica = GeoUtil.converterPointParaCoordenadaGeografica(vertice.getCoordGeoNumerica());
		params.setLatitude(coordenadaGeografica.getLatitude().getAsGD());
		params.setLongitude(coordenadaGeografica.getLongitude().getAsGD());
		params.setIdeDadoGeografico(vertice.getIdeDadoGeografico());

		if(Util.isNullOuVazio(sisCoord.getSrid())) {
			sisCoord = siscoordDAO.carregarGet(sisCoord.getIdeSistemaCoordenada());
		}
		params.setSrid(sisCoord.getSrid());

		salvarParamPersistDadoGeo(params);

		gravarTheGeom(params);

	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void gravarTheGeom(ParamPersistDadoGeo params)  {
		
		//Inverte-se a posição nesse momento, coloca-se a longitude na frente e a latitude depois para a função do postgis interpretar corretamente
		String ponto = ("POINT("+params.getLongitude()+" "+params.getLatitude()+")").replaceAll(",", ".");
		
		Integer srid = Integer.parseInt(params.getSrid());
		Integer default_srid = Integer.parseInt(SistemaCoordenadaEnum.GEOGRAFICA_SIRGAS_2000.getSrid());
		Integer ideDadoGeografico = params.getIdeDadoGeografico();

		StringBuilder sql = new StringBuilder();

		sql.append("UPDATE dado_geografico ");
		if(srid.equals(default_srid)) {
			sql.append("SET the_geom = GeomFromText(:ponto , :srid) ");
		}
		else {
			sql.append("SET the_geom = st_transform(SetSRID(GeomFromText(:ponto, :srid), :srid), :default_srid) ");
		}
		sql.append("WHERE ide_dado_geografico = :ideDadoGeografico");

		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("ponto", ponto);
		parametros.put("srid", srid);
		if(!srid.equals(default_srid)) {
			parametros.put("default_srid", default_srid);
		}
		parametros.put("ideDadoGeografico", ideDadoGeografico);

		localizacaoGeoDAO.executarNativeQuery(sql.toString(), parametros);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String[] salvarParamsEPersistirShapeTheGeom(LocalizacaoGeografica locGeo, boolean validaMunicipio, Double codigoMunicipio, boolean shapeTipoPonto, Integer valida_territorio) throws Exception {
		try {
			return persistenciaCompleta(locGeo, validaMunicipio, codigoMunicipio, shapeTipoPonto, valida_territorio);
		} catch (LazyInitializationException e) {
			return persistenciaCompleta(locGeo, validaMunicipio, codigoMunicipio, shapeTipoPonto, valida_territorio);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return new String[]{"ERRO","9995","Erro interno. Contacte o Suporte técnico!"};
		}
	}

	private String[] persistenciaCompleta(LocalizacaoGeografica locGeo, boolean validaMunicipio, Double codigoMunicipio, boolean shapeTipoPonto, Integer valida_territorio) throws Exception {
		ParamPersistDadoGeo params = new ParamPersistDadoGeo();
		params.setIdeLocalizacaoGeografica(locGeo);
		params.setSrid(locGeo.getIdeSistemaCoordenada().getSrid());
		params.setPastaDosArquivos(DiretorioArquivoEnum.SHAPEFILES+locGeo.getIdeLocalizacaoGeografica().toString());
		salvarParamPersistDadoGeo(params);
		
		Map<String, String> parametros = new HashMap<String, String>();
		parametros.put("id", params.getIdeParamPersistDadoGeo().toString());
		parametros.put("valida_territorio", valida_territorio.toString());
		
		if (!Util.isNullOuVazio(codigoMunicipio) && validaMunicipio) {
			parametros.put("codibge", tratarCodigoMunicipio(codigoMunicipio));
		}
		
		//Chama a url que executará o sistema php para persistir a latitudo e longitude na coluna the_geom
		if(shapeTipoPonto) {
			return AcessarURL.callAppGeoSeia2(URLEnum.CAMINHO_GEOSEIA_IMPORTAR_SHAPE_TIPO_PONTO.getUrl(parametros));
		} else {
			return AcessarURL.callAppGeoSeia2(URLEnum.CAMINHO_GEOSEIA_IMPORTAR.getUrl(parametros));
		}
	}

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public String[] persistirShapeTheGeomImportacaoImovelFinalizado(Integer ideImovel, String nomePastaArquivos, String srid, Double codigoMunicipio) throws Exception {
		ParamPersistDadoGeo params = new ParamPersistDadoGeo();
		Map<String, String> parametros = null;
		
		try {
			params.setIdeLocalizacaoGeografica(null);
			params.setIdeImovelRural(ideImovel);
			params.setSrid(srid);
			params.setPastaDosArquivos(DiretorioArquivoEnum.SHAPEFILESTEMP+nomePastaArquivos);
			salvarParamPersistDadoGeo(params);
			
			//Chama a url que executará o sistema php para persistir a latitudo e longitude na coluna the_geom
			parametros = new HashMap<String, String>();
			parametros.put("id", params.getIdeParamPersistDadoGeo().toString());
			
			if(!Util.isNullOuVazio(codigoMunicipio)) {
				parametros.put("codibge", tratarCodigoMunicipio(codigoMunicipio));
			}
			
			String[] retorno = AcessarURL.callAppGeoSeia2(URLEnum.CAMINHO_GEOSEIA_IMPORTAR.getUrl(parametros));
			return retorno;
		} catch (Exception e) {
			return new String[]{"ERRO","9995","Erro interno. Contacte o Suporte técnico!"};
		}
	}

	public String[] persistirShapeTheGeomDesenhoImovelFinalizado(Integer ideParam, Double codigoMunicipio)  {
		Map<String, String> parametros = null;
		
		try {
			//Chama a url que executará o sistema php para persistir a latitudo e longitude na coluna the_geom
			parametros = new HashMap<String, String>();
			parametros.put("id", ideParam.toString());
			
			if(!Util.isNullOuVazio(codigoMunicipio)) {
				parametros.put("codibge", tratarCodigoMunicipio(codigoMunicipio));
			}
			
			String[] retorno = AcessarURL.callAppGeoSeia2(URLEnum.CAMINHO_GEOSEIA_IMPORTAR.getUrl(parametros));
			return retorno;
		} catch (Exception e) {
			return new String[]{"ERRO","9995","Erro interno. Contacte o Suporte técnico!"};
		}
	}

	public Integer carregarSomenteComParamPersistDadoGeo(Integer pIdeLocalizacaoGeografica) {
		List<ParamPersistDadoGeo> listParamPersistDadoGeo = new ArrayList<ParamPersistDadoGeo>();
		String sql = new String("SELECT ide_param_persist_dado_geo FROM param_persist_dado_geo"+
				" WHERE ide_localizacao_geografica = "+pIdeLocalizacaoGeografica+
				" AND srid = '4674' AND dtc_persistido IS NULL"+
				" LIMIT 1;");

		listParamPersistDadoGeo = persisteTheGeomDAO.buscarPorNativeQuery(sql, null);

		if (Util.isNullOuVazio(listParamPersistDadoGeo)) {
			return null;
		} else {
			Object obj = listParamPersistDadoGeo.get(0);
			return (Integer) obj;
		}
	}

	public Integer carregarParamPersistDadoGeoPorPasta(String pPastaArquivos) {
		List<ParamPersistDadoGeo> listParamPersistDadoGeo = new ArrayList<ParamPersistDadoGeo>();
		String sql = new String("SELECT ide_param_persist_dado_geo FROM param_persist_dado_geo"+
				" WHERE pasta_dos_arquivos = '"+pPastaArquivos+"'" +
				" AND srid = '4674' AND dtc_persistido IS NULL"+
				" LIMIT 1;");

		listParamPersistDadoGeo = persisteTheGeomDAO.buscarPorNativeQuery(sql, null);

		if (Util.isNullOuVazio(listParamPersistDadoGeo)) {
			return null;
		} else {
			Object obj = listParamPersistDadoGeo.get(0);
			return (Integer) obj;
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public LocalizacaoGeografica carregar(Integer ide) {
		LocalizacaoGeografica localizacao = null;
		DetachedCriteria criteria = DetachedCriteria.forClass(LocalizacaoGeografica.class, "localizacaoGeografica");
		criteria.createAlias("ideSistemaCoordenada", "sistemaCoordenada", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("ideClassificacaoSecao", "classificacaoSecao", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("ideLocalizacaoGeografica", ide));
		localizacao = localizacaoGeoDAO.buscarPorCriteria(criteria);
		if(Util.isNull(localizacao.getIdeClassificacaoSecao()) || localizacao.getIdeClassificacaoSecao().getIdeClassificacaoSecao().equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId())){
			localizacao.setDadoGeograficoCollection(listarDadoGeografico(localizacao, ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId()));
		}else if(localizacao.getIdeClassificacaoSecao().getIdeClassificacaoSecao().equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId())){
			localizacao.setDadoGeograficoCollection(listarDadoGeografico(localizacao, ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId()));
		}
		return localizacao;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DadoGeografico> listarDadoGeografico(LocalizacaoGeografica locGeo, Integer classifSec) {
		List<DadoGeografico> listDadoGeo = null;
		List<DadoGeografico> tempDadoGeo = null;
		String sql = new String("SELECT ide_dado_geografico, coord_geo_numerica, ide_localizacao_geografica  FROM dado_geografico "+
				"WHERE ide_localizacao_geografica = "+locGeo.getIdeLocalizacaoGeografica());
		if(classifSec.equals(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId())){
			sql = sql+ " AND the_geom is not null " +
					"AND coord_geo_numerica is null " +
					"LIMIT 10;";
		}else{
			sql = sql + " AND coord_geo_numerica is not null;";
		}
		tempDadoGeo = dadoGeograficoDAO.buscarPorNativeQuery(sql, null);
		listDadoGeo = new ArrayList<DadoGeografico>();
		for (Object tempObj : tempDadoGeo.toArray()) {
			Object[] resultElement = (Object[]) tempObj;
			DadoGeografico tempDado = new DadoGeografico((Integer)resultElement[0]);
			if(resultElement[1]!=null) {
				tempDado.setCoordGeoNumerica(resultElement[1].toString());
			}
			if(resultElement[2]!=null) {
				tempDado.setIdeLocalizacaoGeografica(new LocalizacaoGeografica((Integer)resultElement[2]));
			}
			listDadoGeo.add(tempDado);
		}

		return listDadoGeo;
	}

	public String[] salvarParamsEPersistindoDesenhoTheGeom(Integer pIdeParamPersistDadoGeo, Double codigoMunicipio) {
		try {
			//Chama a url que executará o sistema php para persistir a latitudo e longitude na coluna the_geom
			Map<String, String> parametros = new HashMap<String, String>();
			parametros.put("id", pIdeParamPersistDadoGeo.toString());
			if (!Util.isNullOuVazio(codigoMunicipio)) {
				parametros.put("codibge", tratarCodigoMunicipio(codigoMunicipio));
			}
			String[] retorno = AcessarURL.callAppGeoSeia2(URLEnum.CAMINHO_GEOSEIA_IMPORTAR.getUrl(parametros));
			return retorno;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return new String[]{"ERRO","9994","Erro interno. Contacte o Suporte técnico!"};
	}

	private String tratarCodigoMunicipio(Double codigoMunicipio) {
		return codigoMunicipio.toString().substring(0,7);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void persistirDadoGeografico(Integer ideLocalizacaoGeografica, String geometria)  {
		String insertSQL = "INSERT INTO dado_geografico (ide_localizacao_geografica, the_geom) VALUES (" + ideLocalizacaoGeografica + ", '" + geometria + "')";
		persisteTheGeomDAO.executarNativeQuery(insertSQL, null);
	}
	
	public boolean isUTM(LocalizacaoGeografica lg) {
		return !lg.getIdeSistemaCoordenada().getIdeSistemaCoordenada().equals(SistemaCoordenadaEnum.GEOGRAFICA_SAD69.getId())
				&& !lg.getIdeSistemaCoordenada().getIdeSistemaCoordenada().equals(SistemaCoordenadaEnum.GEOGRAFICA_SIRGAS_2000.getId());
	}
	
	public boolean isUTM(SistemaCoordenada sc) {
		return !sc.getIdeSistemaCoordenada().equals(SistemaCoordenadaEnum.GEOGRAFICA_SAD69.getId())
				&& !sc.getIdeSistemaCoordenada().equals(SistemaCoordenadaEnum.GEOGRAFICA_SIRGAS_2000.getId());
	}
	
	public List<ParamPersistDadoGeo> listarPorLocalizacaoGeografica(LocalizacaoGeografica locGeo) {
		
		try {
			DetachedCriteria criteria = DetachedCriteria.forClass(ParamPersistDadoGeo.class)
				.add(Restrictions.eq("ideLocalizacaoGeografica.ideLocalizacaoGeografica", locGeo.getIdeLocalizacaoGeografica()));
		
			return persisteTheGeomDAO.listarPorCriteria(criteria);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirPorLocalizacaoGeografica(Integer ideLocGeo) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("ideLocalizacaoGeografica", ideLocGeo);
			
			String delSQL = "DELETE FROM param_persist_dado_geo WHERE ide_localizacao_geografica = :ideLocalizacaoGeografica";
			persisteTheGeomDAO.executarNativeQuery(delSQL, params);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
}
