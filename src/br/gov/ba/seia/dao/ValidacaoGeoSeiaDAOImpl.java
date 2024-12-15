package br.gov.ba.seia.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.gov.ba.seia.entity.GeoJsonSicar;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.enumerator.URLEnum;
import br.gov.ba.seia.exception.CampoObrigatorioException;
import br.gov.ba.seia.util.AcessarURL;
import br.gov.ba.seia.util.Util;

/**
 * @author carlos.duarte
 *
 *Class DAO responsável por acessar as funções de validações geográficas do SEIA.
 *O parâmetro Tipo usado nos métodos são representados por:
 *1 - IMÓVEL
 *2 - RESERVA LEGAL
 *3 - APP
 *4 - ÁREA PRODUTIVA
 *5 - VEGETAÇÃO NATIVA
 */
public class ValidacaoGeoSeiaDAOImpl implements ValidacaoGeoSeiaDAOIf {
	
	@Inject
	private IDAO<Object> daoObject;
	
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public String buscarGeometriaShape(Integer ideLocalizacao)  {
		List<String> listaObj = null;
		
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery("SELECT CAST(ST_Union(the_geom) AS varchar) FROM dado_geografico WHERE ide_localizacao_geografica = " + ideLocalizacao);
		
		listaObj = lQuery.getResultList();
		
		return listaObj.get(0);
	}
	
	@Override
	public String[] buscarGeometriaShapeTemporarioCEFIR(Integer ideImovel, Integer tipo, String identificador) throws CampoObrigatorioException  {
		Map<String, String> parametros = null;
		
		parametros = new HashMap<String, String>();
		parametros.put("idimov", ideImovel.toString());
		parametros.put("tipotema", tipo.toString());
		if(!Util.isNullOuVazio(identificador)){
			parametros.put("identificador", identificador);
		}
		
		String[] retorno = AcessarURL.callAppShape2Geom(URLEnum.CAMINHO_GEOSEIA_CARREGAR_GEOMETRIA.getUrl(parametros));
		if(Util.isNullOuVazio(retorno[0])){
			 retorno = new String[]{"ERRO","9995","Erro interno. Contacte o Suporte técnico!"};
		}
		return retorno;
	}
	
	@Override
	public String[] buscarGeometriaShapeTemporario(LocalizacaoGeografica localizacaoGeografica) throws Exception  {
		Map<String, String> parametros = null;
		
		parametros = new HashMap<String, String>();
		parametros.put("idelocalizacao", localizacaoGeografica.getIdeLocalizacaoGeografica().toString());
		parametros.put("sistemacoordenada", localizacaoGeografica.getIdeSistemaCoordenada().getSrid());
		
		String[] retorno = AcessarURL.callAppShape2Geom(URLEnum.CAMINHO_GEOSEIA_CARREGAR_GEOMETRIA.getUrl(parametros));
		if(Util.isNullOuVazio(retorno[0])){
			retorno = new String[]{"ERRO","9995","Erro interno. Contacte o Suporte técnico!"};
		}
		return retorno;
	}
	
	/**
	 * @author carlos.duarte
	 *
	 *Método que retorna em um vetor a área declarada no cadastro do SEIA e 
	 *a área geométrica calculada a partir do Shape.
	 *RETORNO: vetor[0] = área declarada / vetor[1] = área geométrica calculada a partir do Shape
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Double[] buscarAreaDeclaradaEGeorreferenciada(Integer ideLocalizacao)  {
		List<Object[]> retorno = null;
		Double[] vetorAreas = new Double[2];
		BigDecimal areaDeclarada = null;
		BigDecimal areaGeo = null;
		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery("SELECT * FROM sp_validacao_geom_tema(" + ideLocalizacao + ")");
		
		retorno  = lQuery.getResultList();
		
		if (!Util.isNullOuVazio(retorno)){
			for (Object[] resultElement : retorno) {
				areaDeclarada = (BigDecimal)resultElement[0];
				areaGeo = (BigDecimal)resultElement[1];
				vetorAreas[0] = areaDeclarada.doubleValue();
				vetorAreas[1] = areaGeo.doubleValue();
			}
		}
		
		return vetorAreas;
	}
	
	@Override
	public Double buscarAreaGeorreferenciadaShapeTemporario(String geometria)  {
		Object retorno = null;
		BigDecimal areaGeo = null;
		
		if(geometria == null)
			return 0.00;
		
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery("SELECT * FROM sp_validacao_geom_tema_temporario1('" + geometria + "')");
		
		retorno = lQuery.getSingleResult();		
		if (!Util.isNullOuVazio(retorno)){
			areaGeo = (BigDecimal)retorno;
		}
		
		if(Util.isNull(areaGeo)){
			areaGeo = new BigDecimal(0);
		}
		
		return areaGeo.doubleValue();
	}
	
	@Override
	public List<Integer> buscarCodigoIbgeMunicipioShape(String geometria) {
		List<Integer> listaCodIbgeMunicipio = new ArrayList<Integer>();
		List<Object> lista = daoObject.buscarPorNativeQuery("SELECT sp_get_municipio(cast('" + geometria + "' as geometry))", null);
		for(Object codigo : lista){
			listaCodIbgeMunicipio.add(Integer.parseInt((codigo).toString()));
		}
		
		return listaCodIbgeMunicipio;		
	}
	
	/**
	 * @author micael.coutinho
	 *
	 *Método que retorna um boolean validando a área informada como parametro e 
	 *a área geométrica calculada a partir do Shape da localizacao geografica do parametro.
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Boolean validaAreaComAreaDaLocalizacaoGeografica(Integer ideLocalizacao, Double area)  {
		List<Object> listaObj = null;
		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery("SELECT sp_valida_area_com_loc_geo(" + ideLocalizacao + ", " + area +")");
								
		listaObj  = lQuery.getResultList();		 
		if (!Util.isNullOuVazio(listaObj)){
			return ((Boolean)listaObj.get(0));					 
		}
		return null;
	}
	
	/**
	 * @author kelson.borges
	 *
	 * Método que retorna uma lista de todas as localizações geográficas que intersectam a localização geográfica
	 * informada a partir do seu identificador ou da geometria em formato PostGIS
	 * 
	 * RETORNO: Lista de identificadores de localização geográfica
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> listarLocalizacaoGeograficaComSobreposicao(Integer ideLocalizacao, String geometria, Integer tipo)  {
		String geom = ideLocalizacao != null ? "(SELECT ST_Union(the_geom) FROM dado_geografico WHERE ide_localizacao_geografica = " + ideLocalizacao + ")" : "'" + geometria + "'";
		List<Integer> listaObj = null;
		
		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery("SELECT * FROM sp_validacao_geom_tema_temporario2(" + geom + ", " + tipo + ")");
		listaObj = lQuery.getResultList();
		
		return listaObj;
	}
	
	/**
	 * @author kelson.borges
	 *
	 * Método que retorna um vetor contendo as áreas georreferenciadas totais e a área de sobreposição de duas 
	 * localizações geográficas informadas a partir dos seus identificadores e/ou das suas geometrias em formato PostGIS
	 * 
	 * RETORNO: 
	 * 	vetor[0] = área total da localização geográfica A
	 * 	vetor[1] = área total da localização geográfica B
	 * 	vetor[2] = área sobreposta (comum as duas localizações geográficas)
	 *
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Double[] buscarAreaTotalAreaSobreposicaoLocalizacoesGeograficas(Integer ideLocalizacaoA, String geometriaA, Integer ideLocalizacaoB, String geometriaB)  {		
		String geomA = ideLocalizacaoA != null ? "(SELECT ST_Union(the_geom) FROM dado_geografico WHERE ide_localizacao_geografica = " + ideLocalizacaoA + ")" : "'" + geometriaA + "'";
		String geomB = ideLocalizacaoB != null ? "(SELECT ST_Union(the_geom) FROM dado_geografico WHERE ide_localizacao_geografica = " + ideLocalizacaoB + ")" : "'" + geometriaB + "'";
		List<Object[]> listaObj = null;
		Double[] vetorAreas = new Double[3];
		BigDecimal areaTotalGeomA = null;
		BigDecimal areaTotalGeomB = null;
		BigDecimal areaSobreposicao = null;
		
		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery("SELECT * FROM sp_validacao_geom_tema_temporario3(" + geomA + ", " + geomB + ")");
		listaObj = lQuery.getResultList();
		
		if (!Util.isNullOuVazio(listaObj)){
			for (Object[] resultElement : listaObj) {
				areaTotalGeomA = (BigDecimal)resultElement[0];
				areaTotalGeomB = (BigDecimal)resultElement[1];
				areaSobreposicao = (BigDecimal)resultElement[2];
				vetorAreas[0] = areaTotalGeomA.doubleValue();
				vetorAreas[1] = areaTotalGeomB.doubleValue();
				vetorAreas[2] = areaSobreposicao.doubleValue();
			}
		}
		return vetorAreas;
	}
			
	/**
	 * @author carlos.duarte
	 *
	 *Método que retorna em um vetor com a área total e a 
	 *área sobreposta de uma localização informada a partir de um tipo e ide de uma área (Imovel, Reserva Legal, APP, Área Produtiva e Vegetação Nativa)
	 *RETORNO: vetor[0] = área total da localização informada / vetor[1] = área sobreposta da localização informada
	 *
	 */
	@Override
	public Boolean validaShapeRlCerberus(String numeroProcesso, Integer ideLocalizacao)  {
		Boolean isShapeValido = false;
		Object obj = null;		
		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery("SELECT * FROM sp_validacao_geom_rl_cerberus('" + numeroProcesso + "'," + ideLocalizacao + ")");
								
		obj  = lQuery.getSingleResult();		 
		
		if (!Util.isNullOuVazio(obj)){
			isShapeValido = (Boolean)obj;					 
		}
		return isShapeValido;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean validaPercentualSobreposicao(String geometriaA, String geometriaB) throws Exception  {
		List<Object[]> listaObj = null;
		BigDecimal areaGeomA = null;
		BigDecimal areaGeomB = null;
		BigDecimal areaSobreposta = null;
		
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery("SELECT * FROM sp_verifica_sobreposicao_multigeom('" + geometriaA + "','" + geometriaB + "')");
		
		listaObj  = lQuery.getResultList();
		
		if (!Util.isNullOuVazio(listaObj)){
			for (Object[] resultElement : listaObj) {
				areaGeomA = (BigDecimal)resultElement[0];
				areaGeomB = (BigDecimal)resultElement[1];
				areaSobreposta = (BigDecimal)resultElement[2];
				
				if(areaGeomA.doubleValue() > 0 && areaGeomB.doubleValue() > 0) {
					Double percentualSobreposicaoA = (areaSobreposta.doubleValue()/areaGeomA.doubleValue())*100;
					Double percentualSobreposicaoB = (areaSobreposta.doubleValue()/areaGeomB.doubleValue())*100;
					
					//Arredonda o percentual para apenas uma casa decimal
					percentualSobreposicaoA = Util.converterDoubleUmaCasa(percentualSobreposicaoA);
					percentualSobreposicaoB = Util.converterDoubleUmaCasa(percentualSobreposicaoB);
					
					if (percentualSobreposicaoA > 10 || percentualSobreposicaoB > 10) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> validarAssentadoLote(Integer ideImovel, String listaDePontos, String geometriaImovel)  {
		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery("SELECT * FROM sp_validacao_assentado_lote(" + ideImovel + ", '" + listaDePontos + "' ," + geometriaImovel + ")");
		return lQuery.getResultList();		 
	}
	
	@Override
	public GeoJsonSicar buscarGeoJsonSicar(List<Integer> localizacoes)  {
		Object[] obj = null;
		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery("SELECT * FROM sp_geojson_sicar(ARRAY" + localizacoes + ")");
								
		obj  = (Object[])lQuery.getSingleResult();		 
		
		if (!Util.isNullOuVazio(obj) && !Util.isNullOuVazio(obj[0]) && obj[1]!=null)
			return new GeoJsonSicar((String)obj[0], ((BigDecimal)obj[1]).doubleValue());
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String validaTipoGeometria(Integer ideLocalizacao, String geometria)  {
		String parametro = ideLocalizacao != null ? "(SELECT ST_Union(the_geom) FROM dado_geografico WHERE ide_localizacao_geografica = " + ideLocalizacao + ")" : "'"+geometria+"'";
		StringBuilder sql = new StringBuilder();
		List<String> listaObj = null;

		sql.append("SELECT * FROM sp_validacao_tipogeom(" + parametro + ")");
		
		Query lQuery = executeQuery(sql);
		listaObj  = lQuery.getResultList();
		return listaObj.get(0);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> listarImoveisCreditoRl(Integer ideImovelRural, String geometriaRl, Boolean proprioImovel)  {
		StringBuilder sql = new StringBuilder();
		String condicaoImovel = ""; 
		
		if(Util.isNullOuVazio(geometriaRl)){
			geometriaRl = "GEOMETRYCOLLECTION(EMPTY)";
		}
		if(Util.isNullOuVazio(proprioImovel) || proprioImovel.equals(false)){			
			condicaoImovel = "WHERE ic.ide_imovel_rural <> " + ideImovelRural; 
		}
		sql.append("SELECT * FROM sp_get_imovelcredito(" + ideImovelRural + ", '" + geometriaRl + "') AS ic " + condicaoImovel + " ORDER BY ic.ide_imovel_rural");
		
		Query lQuery = executeQuery(sql);
    	List<Object[]> list = lQuery.getResultList();
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> listarImoveisDebitoRl(Integer ideImovelRural, String geometriaIm)  {
		StringBuilder sql = new StringBuilder();
		
		if(Util.isNullOuVazio(geometriaIm)){
			geometriaIm = "GEOMETRYCOLLECTION(EMPTY)";
		}
		sql.append("SELECT * FROM sp_get_imoveldebito(" + ideImovelRural + ", '" + geometriaIm + "') AS id ORDER BY id.ide_imovel_rural");
		
		Query lQuery = executeQuery(sql);
    	List<Object[]> list = lQuery.getResultList();
		return list;
	}

	@Override
	public Integer buscarQuantidadeTalhoesDeclarados(String geometria) throws Exception  {
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT ST_NumGeometries('"+geometria+"')");
			Query lQuery = executeQuery(sql);
			return (Integer) lQuery.getSingleResult();
		} catch(Exception e) {
			throw new Exception("Erro de validação do shape da atividade desenvolvida do tipo Manejo Sustentável.");
		}
	}
	
	private Query executeQuery(StringBuilder sql) {
		EntityManager lEntityManager = DAOFactory.getEntityManager();
    	lEntityManager.joinTransaction();

    	return lEntityManager.createNativeQuery(sql.toString());

	}
	
	/**
	 * @author carlos.duarte
	 * 
	 * Método que retorna um boolean validando a sobreposição
	 * da área da geometria informada por parâmetro com outros imóveis ignorando o id do imóvel passado 
	 * por parâmetro que está sendo validado, seguindo o padrão de validação de sobreposição de imóveis do CAR.
	 * 
	 * @param geometria
	 * @param ideImovelRural
	 * @return Boolean - TRUE se não houver sobreposição e FALSE se houver sobreposição e não poder proseguir com o cadastro.
	 * @
	 */
	public boolean validaSobreposicaoImovel(String geometria, Integer ideImovelRural)  {
		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery("SELECT sp_valida_imovel_sobreposicao('" + geometria + "', " + ideImovelRural +", true, null)");
								
		return (Boolean)lQuery.getSingleResult();					 
	}
	
	/**
	 * 
	 * Método que retorna um boolean validando a sobreposição no PCT
	 * da área da geometria informada por parâmetro com outros imóveis ignorando o id do imóvel passado 
	 * por parâmetro que está sendo validado, seguindo o padrão de validação de sobreposição de imóveis do CAR.
	 * 
	 * @param geometria
	 * @param ideImovelRural
	 * @return Boolean - TRUE se não houver sobreposição e FALSE se houver sobreposição e não poder proseguir com o cadastro.
	 * @throws Exception
	 */
	public boolean validaSobreposicaoImovelPCT(String geometria, Integer ideImovelRural) throws Exception {
		EntityManager lEntityManager = (EntityManager) DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery("SELECT sp_valida_imovel_sobreposicao_pct('" + geometria + "', " + ideImovelRural +", true, null)");
								
		return (Boolean)lQuery.getSingleResult();					 
	}
	
	/**
	 * @author carlos.duarte
	 * 
	 * Método que verifica se o imóvel que está tentando compensar reserva legal em mais de um imóvel
	 * é uma exceção da regra.
	 * 
	 * @param ideImovelRural
	 * @return Boolean - TRUE se existir o registro na tabela de exceção da regra, FALSE se não existir o registro na tabela de exceção da regra.
	 * @
	 */
	@SuppressWarnings("unchecked")
	public boolean isExcecaoRegraCompensacaoRlMaisDeUmImovel(Integer ideImovelRural)  {
		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery("SELECT * FROM excecao_regra_compensacao_reserva_legal WHERE ide_imovel_rural = "  + ideImovelRural + " ");
		List<Object[]> listaObj = lQuery.getResultList();
		if (!Util.isNullOuVazio(listaObj)){
			for (Object resultElement : listaObj) {		
				if((Integer)resultElement > 0) {
					return true;					
				}
			}
		}
		return false;
	}

	/**
	 * Método que vai verificar se a {@link LocalizacaoGeografica} cai apenas
	 * dentro do Estado da Bahia.
	 * 
	 * @author eduardo.fernandes
	 * @since 19/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param localizacaoGeografica
	 * @return [true] - se a poligonal estiver apenas dentro do Estado da Bahia <br/>
	 *         [false] - se parte da poligonal estiver fora do Estado da Bahia.
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isLocalizacaoGeograficaDentroDaBahia(LocalizacaoGeografica locGeo)  {
		String geomA = "(SELECT the_geom FROM dado_geografico WHERE ide_localizacao_geografica = " + locGeo.getIdeLocalizacaoGeografica() + ")";
		List<Object[]> listaObj = null;
		BigDecimal areaShape = null;
		BigDecimal areaSobreposicao = null;

		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery("SELECT * FROM sp_checar_localizacao_geografica_na_bahia(" + geomA + ")");
		listaObj = lQuery.getResultList();

		if(!Util.isNullOuVazio(listaObj)){
			for(Object[] resultElement : listaObj){
				areaShape = (BigDecimal) resultElement[0]; // Área da Localização Geográfica enviada pelo usuário
				areaSobreposicao = (BigDecimal) resultElement[1]; // Área sobrepostapoligonal no Estado da Bahia
			}
		}
		return areaShape.equals(areaSobreposicao);
	}

	public boolean verificaSobreposicaoShapeUsandoContains(String geometriaA, String geometriaB) {
		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery("SELECT _ST_Contains('" + geometriaA + "', '" + geometriaB + "')");
								
		return (Boolean)lQuery.getSingleResult();	
	}
	
	public String converterPonto(String coordGeoNumerica, Integer sridCoord, Integer sridNovo) throws Exception {
		EntityManager lEntityManager = (EntityManager) DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery("SELECT CAST(st_transform(SetSRID(GeomFromText('"+ coordGeoNumerica +"', "+sridCoord+"), "+sridCoord+"), "+sridNovo+") AS text)");
		
		return (String)lQuery.getSingleResult();	
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
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Integer> getCollectionIdeLocalizacaoGeograficaSobrepostaBy(String theGeom, Integer ideImovel)  {
		String  strLista = (String) daoObject.obterPorNativeQuery("select sp_get_lista_imovel_sobreposto_acima_tolerancia('"+ theGeom + "'," + ideImovel + ", null)", null);
		Collection<Integer> ides = new ArrayList<Integer>();
		if(!Util.isNullOuVazio(strLista)){
			for (String ide : strLista.split(",")) {
				ides.add(Integer.parseInt(ide));
			}
		}
		return ides;
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
	 * @ 
	 */
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isTheGeomIgual(LocalizacaoGeografica locGeoA, LocalizacaoGeografica locGeoB) {
		String geomA = "(SELECT the_geom FROM dado_geografico WHERE ide_localizacao_geografica = " + locGeoA.getIdeLocalizacaoGeografica() + ")";		
		String geomB = "(SELECT the_geom FROM dado_geografico WHERE ide_localizacao_geografica = " + locGeoB.getIdeLocalizacaoGeografica() + ")";		
		List<Object> listaObj = daoObject.buscarPorNativeQuery("SELECT * FROM st_intersects(" + geomA + "," + geomB + ")", null);
		for(Object obj : listaObj){
			return (Boolean) obj;
		}
		return false;
	}
	
	public boolean validaSobreposicaoImovelIncra(String geometria, Integer ideImovelRural)  {
		EntityManager lEntityManager =  DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery("SELECT sp_valida_imovel_sobreposicao_assentamento('" + geometria + "', " + ideImovelRural +", true, null)");
								
		return (Boolean)lQuery.getSingleResult();					 
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean validaPercentualSobreposicaoPCT(String geometriaA, String geometriaB) throws Exception  {
		List<Object[]> listaObj = null;
		BigDecimal areaGeomA = null;
		BigDecimal areaGeomB = null;
		BigDecimal areaSobreposta = null;
		
		EntityManager lEntityManager = DAOFactory.getEntityManager();
		lEntityManager.joinTransaction();
		Query lQuery = lEntityManager.createNativeQuery("SELECT * FROM sp_verifica_sobreposicao_multigeom('" + geometriaA + "','" + geometriaB + "')");
		
		listaObj  = lQuery.getResultList();
		
		if (!Util.isNullOuVazio(listaObj)){
			for (Object[] resultElement : listaObj) {
				areaGeomA = (BigDecimal)resultElement[0];
				areaGeomB = (BigDecimal)resultElement[1];
				areaSobreposta = (BigDecimal)resultElement[2];
				
				if(areaGeomA.doubleValue() > 0 && areaGeomB.doubleValue() > 0) {
					Double percentualSobreposicaoA = (areaSobreposta.doubleValue()/areaGeomA.doubleValue())*100;
					Double percentualSobreposicaoB = (areaSobreposta.doubleValue()/areaGeomB.doubleValue())*100;
					
					//Arredonda o percentual para apenas uma casa decimal
					percentualSobreposicaoA = Util.converterDoubleUmaCasa(percentualSobreposicaoA);
					percentualSobreposicaoB = Util.converterDoubleUmaCasa(percentualSobreposicaoB);
					
					if ((percentualSobreposicaoA > 90 && percentualSobreposicaoA < 110) || (percentualSobreposicaoB > 90 && percentualSobreposicaoB < 110)) {
						return true;
					}
				}
			}
		}
		return false;
	}
}

