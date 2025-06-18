package br.gov.ba.seia.dao;

import java.util.Collection;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.CerhCaptacaoTransposicao;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author eduardo.fernandes 
 * @since 13/04/2017
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhCaptacaoTransposicaoDAOImpl {

	@Inject
	private IDAO<CerhCaptacaoTransposicao> dao;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhCaptacaoTransposicao> listar(Integer ideCerhCaptacaoCaracterizacaoFinalidade) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhCaptacaoTransposicao.class)
				.createAlias("ideCerhFinalidadeTransposicao", "cft")
				.add(Restrictions.eq("ideCerhCaptacaoCaracterizacaoFinalidade.ideCerhCaptacaoCaracterizacaoFinalidade", ideCerhCaptacaoCaracterizacaoFinalidade)
				).setProjection(
					Projections.projectionList()
						.add(Projections.property("ideObjetoPai"), "ideObjetoPai")
						.add(Projections.property("ideCerhCaptacaoTransposicao"), "ideCerhCaptacaoTransposicao")
						.add(Projections.property("cft.ideCerhFinalidadeTransposicao"), "ideCerhFinalidadeTransposicao.ideCerhFinalidadeTransposicao")
						.add(Projections.property("cft.dscFinalidadeTransposicao"), "ideCerhFinalidadeTransposicao.dscFinalidadeTransposicao")
						.add(Projections.property("ideCerhCaptacaoCaracterizacaoFinalidade.ideCerhCaptacaoCaracterizacaoFinalidade"), "ideCerhCaptacaoCaracterizacaoFinalidade.ideCerhCaptacaoCaracterizacaoFinalidade")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhCaptacaoTransposicao.class))
				;
		return dao.listarPorCriteria(criteria);
	}

}
