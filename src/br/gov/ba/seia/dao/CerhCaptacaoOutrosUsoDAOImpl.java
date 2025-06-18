package br.gov.ba.seia.dao;

import java.util.HashMap;
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

import br.gov.ba.seia.entity.CerhCaptacaoOutrosUsos;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author eduardo.fernandes 
 * @since 13/04/2017
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhCaptacaoOutrosUsoDAOImpl {

	@Inject
	private IDAO<CerhCaptacaoOutrosUsos> dao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhCaptacaoOutrosUsos carregar(Integer ideCerhCaptacaoCaracterizacaoFinalidade) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhCaptacaoOutrosUsos.class)
				.createAlias("ideCerhOutrosUsos", "outros")
				.add(Restrictions.eq("ideCerhCaptacaoCaracterizacaoFinalidade.ideCerhCaptacaoCaracterizacaoFinalidade", ideCerhCaptacaoCaracterizacaoFinalidade))
				.setProjection(
					Projections.projectionList()
						.add(Projections.property("ideObjetoPai"), "ideObjetoPai")
						.add(Projections.property("ideCerhCaptacaoOutrosUsos"), "ideCerhCaptacaoOutrosUsos")
						.add(Projections.property("outros.ideCerhOutrosUsos"), "ideCerhOutrosUsos.ideCerhOutrosUsos")
						.add(Projections.property("outros.dscOutrosUsos"), "ideCerhOutrosUsos.dscOutrosUsos")
						.add(Projections.property("ideCerhCaptacaoCaracterizacaoFinalidade.ideCerhCaptacaoCaracterizacaoFinalidade"), "ideCerhCaptacaoCaracterizacaoFinalidade.ideCerhCaptacaoCaracterizacaoFinalidade")
					).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhCaptacaoOutrosUsos.class))
				;
		return dao.buscarPorCriteria(criteria );
	}


	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(CerhCaptacaoOutrosUsos cerhCaptacaoOutrosUsos)  {
	
		StringBuilder hql = new StringBuilder();
		
		String entity = "CerhCaptacaoOutrosUsos";
		
		hql.append("DELETE FROM "+entity+" c WHERE c.ide"+entity+" = :ide"+entity);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideCerhCaptacaoOutrosUsos", cerhCaptacaoOutrosUsos.getIdeCerhCaptacaoOutrosUsos());
		
		dao.executarQuery(hql.toString(), params);
		
	}

}