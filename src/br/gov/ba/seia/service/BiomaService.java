/**
 * 
 */
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

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.Bioma;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.enumerator.BiomaEnum;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;
import br.gov.ba.seia.util.Util;

/**
 * @author lesantos
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class BiomaService {
	
	@Inject
	IDAO<Object> idao;
	
	@Inject
	IDAO<Bioma> biomaDAO;

	public List<Bioma> listarBiomaPor(LocalizacaoGeografica localizacaoGeografica) {
		List<Bioma> listaBioma = null;
		String sql = "SELECT * FROM sp_get_bioma(:ideLocalizacaoGeografica)";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideLocalizacaoGeografica", localizacaoGeografica.getIdeLocalizacaoGeografica());
		List<Object> lista = idao.buscarPorNativeQuery(sql, params);
		if(!Util.isNullOuVazio(lista)){
			listaBioma = new ArrayList<Bioma>();
			for(Object obj : lista) {
				listaBioma.add(new Bioma((Object[]) obj));
			}
		}
		return listaBioma;
	}
	
	public String getBiomaByLocalizacaoGeo(LocalizacaoGeografica localizacaoGeografica) {
		String sql = "SELECT * FROM sp_get_bioma(:ideLocalizacaoGeografica)";
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideLocalizacaoGeografica", localizacaoGeografica.getIdeLocalizacaoGeografica());
		List<Object> bioma = idao.buscarPorNativeQuery(sql, params);
		if(!Util.isNullOuVazio(bioma)){
			return ((Object[])bioma.get(0))[0].toString();
		}
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Bioma> obterBiomaPorTipo(Integer ideTipoBioma) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Bioma.class);
		
		criteria.add(Restrictions.eq("ideTipoBioma.ideTipoBioma", ideTipoBioma));
		
		criteria.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideBioma"), "ideBioma")
					.add(Projections.property("nomBioma"), "nomBioma")
					.add(Projections.property("metrosCubicos"), "metrosCubicos")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(Bioma.class))
				;
		return biomaDAO.listarPorCriteria(criteria);
	}
}
