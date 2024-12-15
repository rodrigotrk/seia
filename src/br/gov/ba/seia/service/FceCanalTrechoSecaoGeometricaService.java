/**
 * 
 */
package br.gov.ba.seia.service;

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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceCanalTrecho;
import br.gov.ba.seia.entity.FceCanalTrechoSecaoGeometrica;

/**
 * @author lesantos
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceCanalTrechoSecaoGeometricaService {

	@Inject
	IDAO<FceCanalTrechoSecaoGeometrica> idao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceCanalTrechoSecaoGeometrica> getFceCanalTrechoSecaoGeometrica(FceCanalTrecho trecho) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceCanalTrechoSecaoGeometrica.class)
				.createAlias("fceCanalTrecho", "fceCanalTrecho", JoinType.LEFT_OUTER_JOIN)
				.createAlias("secaoGeometrica","secaoGeometrica", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("fceCanalTrecho", trecho));
		return idao.listarPorCriteria(criteria, Order.asc("ideFceCanalTrechoSecaoGeometrica"));
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removerFceCanalTrechoSecaoGeometrica(FceCanalTrechoSecaoGeometrica trechoSecaoGeometrica)  {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideFceCanalTrechoSecaoGeometrica", trechoSecaoGeometrica.getIdeFceCanalTrechoSecaoGeometrica());
		idao.executarNamedQuery("FceCanalTrechoSecaoGeometrica.removeByIdTrechoSecaoGeometrica", params);
	}
}
