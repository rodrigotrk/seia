package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.Legislacao;

/***
 * 
 * @author luis
 *
 */
public class LegislacaoDAOImpl {

	@Inject
	private IDAO<Legislacao> daoTipoModalidadeErb;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Legislacao> listarByIdeTipo(Integer ideTipo)  {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Legislacao.class);
		detachedCriteria.add(Restrictions.eq("ideTipoLegislacao.ideTipoLegislacao", ideTipo));
		return daoTipoModalidadeErb.listarPorCriteria(detachedCriteria, Order.asc("dscLegislacao"));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Legislacao carregar(Integer pIde){
		return daoTipoModalidadeErb.carregarGet(pIde);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Legislacao carregarByTipo(Integer pIde)  {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideTipoLegislacao", pIde);
		params.put("indExcluido", false);
		return daoTipoModalidadeErb.buscarEntidadePorNamedQuery("Legislacao.findByLegislacaoTipo", params);
	}
}
