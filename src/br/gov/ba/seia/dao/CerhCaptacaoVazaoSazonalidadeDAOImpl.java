package br.gov.ba.seia.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.CerhCaptacaoVazaoSazonalidade;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author eduardo.fernandes 
 * @since 13/04/2017
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhCaptacaoVazaoSazonalidadeDAOImpl extends AbstractDAO<CerhCaptacaoVazaoSazonalidade>{
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<CerhCaptacaoVazaoSazonalidade> dao;
	
	@Override
	protected IDAO<CerhCaptacaoVazaoSazonalidade> getDAO() {
		return dao;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhCaptacaoVazaoSazonalidade> listar(Integer ideCerhCaptacaoCaracterizacao) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhCaptacaoVazaoSazonalidade.class)
			.createAlias("ideMes", "m")
			.add(Restrictions.eq("ideCerhCaptacaoCaracterizacao.ideCerhCaptacaoCaracterizacao", ideCerhCaptacaoCaracterizacao))
			.setProjection(
				Projections.projectionList()
					.add(Projections.property("ideObjetoPai"), "ideObjetoPai")
					.add(Projections.property("ideCerhCaptacaoVazaoSazonalidade"), "ideCerhCaptacaoVazaoSazonalidade")
					.add(Projections.property("valDiaMes"), "valDiaMes")
					.add(Projections.property("valTempoCaptacao"), "valTempoCaptacao")
					.add(Projections.property("valVazaoCaptacao"), "valVazaoCaptacao")
					.add(Projections.property("ideCerhCaptacaoCaracterizacao.ideCerhCaptacaoCaracterizacao"), "ideCerhCaptacaoCaracterizacao.ideCerhCaptacaoCaracterizacao")
					.add(Projections.property("m.ideMes"), "ideMes.ideMes")
					.add(Projections.property("m.codMes"), "ideMes.codMes")
					.add(Projections.property("m.nomMes"), "ideMes.nomMes")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhCaptacaoVazaoSazonalidade.class))
				;
		return dao.listarPorCriteria(criteria, Order.asc("m.ideMes"));
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(Integer ide)  {
		StringBuilder hql = new StringBuilder();
		
		hql.append("DELETE FROM CerhCaptacaoVazaoSazonalidade c WHERE c.ideCerhCaptacaoCaracterizacao.ideCerhCaptacaoCaracterizacao = :ideCerhCaptacaoCaracterizacao");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideCerhCaptacaoCaracterizacao", ide);
		
		dao.executarQuery(hql.toString(), params);
	}

	
}
