package br.gov.ba.seia.service;

import java.util.List;

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

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.SubstanciaMineralTipologia;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * Service de {@link SubstanciaMineralTipologia}
 * 
 * @author eduardo.fernandes
 * @since 14/09/2016
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SubstanciaMineralTipologiaService {

	@Inject
	private IDAO<SubstanciaMineralTipologia> SubstanciaMineralTipologiaIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<SubstanciaMineralTipologia> listarSubstanciaMineralTipologiaBy(Tipologia tipologia) throws Exception {
		DetachedCriteria criteria = getCriteria(tipologia);
		return SubstanciaMineralTipologiaIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public SubstanciaMineralTipologia buscarOutrosBy(Tipologia tipologia) throws Exception{
		DetachedCriteria criteria = getCriteria(tipologia);
		criteria.add(Restrictions.like("sm.nomSubstanciaMineral", "Outros"));
		return SubstanciaMineralTipologiaIDAO.buscarPorCriteria(criteria);
	}

	private DetachedCriteria getCriteria(Tipologia tipologia) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SubstanciaMineralTipologia.class)
				.createAlias("ideSubstanciaMineral", "sm")
				.createAlias("ideTipologia", "t")
				.createAlias("t.ideTipologiaPai", "tp")
				.add(Restrictions.eq("t.ideTipologia", tipologia.getIdeTipologia()))
				.addOrder(Order.asc("sm.nomSubstanciaMineral"))
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideSubstanciaMineralTipologia"), "ideSubstanciaMineralTipologia")
						.add(Projections.property("sm.ideSubstanciaMineral"), "ideSubstanciaMineral.ideSubstanciaMineral")
						.add(Projections.property("sm.nomSubstanciaMineral"), "ideSubstanciaMineral.nomSubstanciaMineral")
						.add(Projections.property("t.ideTipologia"), "ideTipologia.ideTipologia")
						.add(Projections.property("t.desTipologia"), "ideTipologia.desTipologia")
						.add(Projections.property("t.codTipologia"), "ideTipologia.codTipologia")
						.add(Projections.property("tp.ideTipologia"), "ideTipologia.ideTipologiaPai.ideTipologia")
						.add(Projections.property("tp.desTipologia"), "ideTipologia.ideTipologiaPai.desTipologia")
						.add(Projections.property("tp.codTipologia"), "ideTipologia.ideTipologiaPai.codTipologia")
						)
						.setResultTransformer(new AliasToNestedBeanResultTransformer(SubstanciaMineralTipologia.class));
		return criteria;
	}
}
