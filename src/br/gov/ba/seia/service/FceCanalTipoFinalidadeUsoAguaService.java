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
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.FceCanal;
import br.gov.ba.seia.entity.FceCanalTipoFinalidadeUsoAgua;

/**
 * @author lesantos
 *
 */

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceCanalTipoFinalidadeUsoAguaService {
	
	@Inject
	IDAO<FceCanalTipoFinalidadeUsoAgua> idao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceCanalTipoFinalidadeUsoAgua> listarFceCanalTipoFinalidadeUsoAguasPorCanal(FceCanal fceCanal) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceCanalTipoFinalidadeUsoAgua.class)
				.createAlias("ideTipoFinalidadeUsoAgua", "ideTipoFinalidadeUsoAgua", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("ideFceCanal", fceCanal));
		return idao.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceCanalTipoFinalidadeUsoAguasPorCanal(FceCanalTipoFinalidadeUsoAgua fceCanalTipoFinalidadeUsoAgua) {
		idao.salvar(fceCanalTipoFinalidadeUsoAgua);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletarFceCanalTipoFinalidadeUsoAguasPorCanal(FceCanalTipoFinalidadeUsoAgua fceCanalTipoFinalidadeUsoAgua) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("ideFceCanalTipoFinalidadeUsoAgua", fceCanalTipoFinalidadeUsoAgua.getIdeFceCanalTipoFinalidadeUsoAgua());
		idao.executarNamedQuery("FceCanalTipoFinalidadeUsoAgua.removeByIdFceCanalTipoFinalidadeUsoAgua", params);
	}
}
