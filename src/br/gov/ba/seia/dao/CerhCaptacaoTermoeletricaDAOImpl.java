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

import br.gov.ba.seia.entity.CerhCaptacaoCaracterizacaoFinalidade;
import br.gov.ba.seia.entity.CerhCaptacaoTermoeletrica;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author eduardo.fernandes 
 * @since 13/04/2017
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhCaptacaoTermoeletricaDAOImpl {

	@Inject
	private IDAO<CerhCaptacaoTermoeletrica> dao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhCaptacaoTermoeletrica carregar(Integer ideCerhCaptacaoCaracterizacaoFinalidade) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhCaptacaoTermoeletrica.class)
				.add(Restrictions.eq("ideCerhCaptacaoCaracterizacaoFinalidade.ideCerhCaptacaoCaracterizacaoFinalidade", ideCerhCaptacaoCaracterizacaoFinalidade))
				.setProjection(
						Projections.projectionList()
						.add(Projections.property("ideCerhCaptacaoTermoeletrica"), "ideCerhCaptacaoTermoeletrica")
						.add(Projections.property("nomCombustivelPrincipal"), "nomCombustivelPrincipal")
						.add(Projections.property("valEstimativaConsumoAgua"), "valEstimativaConsumoAgua")
						.add(Projections.property("valPotenciaInstalada"), "valPotenciaInstalada")
						.add(Projections.property("valProducaoMensalEnergia"), "valProducaoMensalEnergia")
						.add(Projections.property("ideCerhCaptacaoCaracterizacaoFinalidade.ideCerhCaptacaoCaracterizacaoFinalidade"), "ideCerhCaptacaoCaracterizacaoFinalidade.ideCerhCaptacaoCaracterizacaoFinalidade")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhCaptacaoTermoeletrica.class))
				;
		return dao.buscarPorCriteria(criteria );
	}

	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(CerhCaptacaoCaracterizacaoFinalidade cerhCaptacaoCaracterizacaoFinalidade) {
		StringBuilder hql = new StringBuilder();
		
		hql.append("DELETE FROM CerhCaptacaoTermoeletrica c WHERE c.ideCerhCaptacaoCaracterizacaoFinalidade.ideCerhCaptacaoCaracterizacaoFinalidade = :ide");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ide", cerhCaptacaoCaracterizacaoFinalidade.getIdeCerhCaptacaoCaracterizacaoFinalidade());
		
		
		dao.executarQuery(hql.toString(), params);
	}

}
