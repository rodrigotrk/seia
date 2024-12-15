package br.gov.ba.seia.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.dao.abstracts.AbstractDAO;
import br.gov.ba.seia.entity.CerhCaptacaoMineracaoExtracaoAreia;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author eduardo.fernandes 
 * @since 13/04/2017
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhCaptacaoMineracaoExtracaoAreiaDAOImpl extends AbstractDAO<CerhCaptacaoMineracaoExtracaoAreia>{
	private static final long serialVersionUID = 1L;

	@Inject
	private IDAO<CerhCaptacaoMineracaoExtracaoAreia> dao;
	
	@Override
	protected IDAO<CerhCaptacaoMineracaoExtracaoAreia> getDAO() {
		return dao;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CerhCaptacaoMineracaoExtracaoAreia buscar(Integer ideCerhCaptacaoMineracaoExtracaoAreia) {
		DetachedCriteria criteria = DetachedCriteria.forClass(CerhCaptacaoMineracaoExtracaoAreia.class)
			.add(Restrictions.eq("ideCerhCaptacaoCaracterizacaoFinalidade.ideCerhCaptacaoCaracterizacaoFinalidade", ideCerhCaptacaoMineracaoExtracaoAreia))
				.setProjection(
					Projections.projectionList()
						.add(Projections.property("ideObjetoPai"), "ideObjetoPai")
						.add(Projections.property("ideCerhCaptacaoMineracaoExtracaoAreia"), "ideCerhCaptacaoMineracaoExtracaoAreia")
						.add(Projections.property("valProducaoMaximaMensal"), "valProducaoMaximaMensal")
						.add(Projections.property("valProducaoMaximaAnual"), "valProducaoMaximaAnual")
						.add(Projections.property("valProporcaoAguaPolpa"), "valProporcaoAguaPolpa")
						.add(Projections.property("valTeorUmidade"), "valTeorUmidade")
						.add(Projections.property("valVolumeMedioAgua"), "valVolumeMedioAgua")
						.add(Projections.property("ideCerhCaptacaoCaracterizacaoFinalidade.ideCerhCaptacaoCaracterizacaoFinalidade"), "ideCerhCaptacaoCaracterizacaoFinalidade.ideCerhCaptacaoCaracterizacaoFinalidade")
					).setResultTransformer(new AliasToNestedBeanResultTransformer(CerhCaptacaoMineracaoExtracaoAreia.class));
		return dao.buscarPorCriteria(criteria);
	}

	
	
}
