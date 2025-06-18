package br.gov.ba.seia.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Estado;
import br.gov.ba.seia.entity.GeoBahia;
import br.gov.ba.seia.entity.Municipio;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RelatorioQuantitativoService {

	@Inject
	IDAO<Municipio> daoMunicipio;
	
	@Inject
	IDAO<GeoBahia> daoGeobahia;

	@Inject
	IDAO<Object> daoObject;

	public Collection<Municipio> filtrarListaMunicipiosPorEstado(Estado estado) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideEstado", estado);
		return daoMunicipio.buscarPorNamedQuery("Municipio.findByIdeEstado", params);
	}

	public Collection<GeoBahia> listaDadosGeoBahia(String table)  {
		List<GeoBahia> listGeobahia = new ArrayList<GeoBahia>();
		String sql = "SELECT gid, nom_"+table+" FROM geo_"+table+" ORDER BY "+"nom_"+table;
		List<GeoBahia> buscarPorNativeQuery = daoGeobahia.buscarPorNativeQuery(sql, null);
		for (Object geoBahia : buscarPorNativeQuery) {
			Object[] obj = ((Object[])geoBahia);
			GeoBahia objGeoBahia = new GeoBahia();
			objGeoBahia.setGid(Integer.parseInt(obj[0].toString()));
			objGeoBahia.setNome(obj[1].toString());
			
			listGeobahia.add(objGeoBahia);
		}
		return listGeobahia;
	}

	public List<Object> filtrarObjetosGeoBahia(String tipoArea, String ides, String temas, String idesTemas)  {
		String sql = "SELECT sp_locgeo_temas('"+tipoArea+"', '"+ides+"', ARRAY"+temas+", ARRAY"+idesTemas+")";
		return daoObject.buscarPorNativeQuery(sql, null);
	}

	public List<Object> getListaObjeto(String sql)  {
		return daoObject.buscarPorNativeQuery(sql, null);
	}

	public List<Object> getTotalArea(String table, String field, String ides)  {
		String sql = "SELECT SUM(val_area) FROM "+table+" WHERE "+field+" IN ("+ides+")";
		return daoObject.buscarPorNativeQuery(sql, null);
	}
	
	public List<Object> getTotalAreaGeorreferenciada(String table, String alias, String field, String ides)  {
		String sql = "SELECT SUM((ST_AREA(SP_TRANSFORMUTM(the_geom))/10000)) as the_geom FROM (SELECT "+alias+".ide_localizacao_geografica, ST_UNION(dg.the_geom) as the_geom FROM "+table+" "+alias+" JOIN dado_geografico dg ON dg.ide_localizacao_geografica="+alias+".ide_localizacao_geografica WHERE "+field+" IN ("+ides+") GROUP BY "+alias+".ide_localizacao_geografica) AS tmp";
		return daoObject.buscarPorNativeQuery(sql, null);
	}
}
