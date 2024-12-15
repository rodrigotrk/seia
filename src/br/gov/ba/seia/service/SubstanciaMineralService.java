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
import br.gov.ba.seia.entity.FcePesquisaMineral;
import br.gov.ba.seia.entity.SubstanciaMineral;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * Service de {@link SubstanciaMineral}
 * 
 * @author eduardo.fernandes, alexandre.queiroz
 * @since 10/06/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SubstanciaMineralService {

	@Inject
	private IDAO<SubstanciaMineral> substanciaMineralIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<SubstanciaMineral> listarSubstanciaMineral() throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(SubstanciaMineral.class)
	       .createAlias("ideTipologia", "ideTipologia")
		   .setProjection(Projections.projectionList()
			   .add(Projections.property("ideSubstanciaMineral"),"ideSubstanciaMineral")
			   .add(Projections.property("nomSubstanciaMineral"),"nomSubstanciaMineral")
			   .add(Projections.property("ideTipologia.ideTipologia"),"ideTipologia.ideTipologia")
			   .add(Projections.property("ideTipologia.desTipologia"),"ideTipologia.desTipologia")
		).setResultTransformer(new AliasToNestedBeanResultTransformer(SubstanciaMineral.class));
		return substanciaMineralIDAO.listarPorCriteria(criteria,Order.asc("ideTipologia"));
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<SubstanciaMineral> listarSubstanciaMineralBy(FcePesquisaMineral fcePesquisaMineral) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(SubstanciaMineral.class)
	       .createAlias("listaFcePesquisaMineralSubstanciaMineral", "fcePesquisaMineral")
	       
	       .add(Restrictions.eq("fcePesquisaMineral.ideFcePesquisaMineral", fcePesquisaMineral.getIdeFcePesquisaMineral()))
		   .setProjection(Projections.projectionList()
			   .add(Projections.property("ideSubstanciaMineral"),"ideSubstanciaMineral")
			   .add(Projections.property("nomSubstanciaMineral"),"nomSubstanciaMineral")
			   .add(Projections.property("ideTipologia.ideTipologia"),"ideTipologia.ideTipologia")
			   .add(Projections.property("ideTipologia.desTipologia"),"ideTipologia.desTipologia")
		).setResultTransformer(new AliasToNestedBeanResultTransformer(SubstanciaMineral.class)); 
		return substanciaMineralIDAO.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<SubstanciaMineral> listarSubstanciaMineralBy(Tipologia tipologia) throws Exception {
		return substanciaMineralIDAO.listarPorCriteria(getCriteria(tipologia));

	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public SubstanciaMineral buscarOutrosBy(Tipologia tipologia) throws Exception{
		DetachedCriteria criteria = getCriteria(tipologia);
		criteria.add(Restrictions.like("nomSubstanciaMineral", "Outros"));
		return substanciaMineralIDAO.buscarPorCriteria(criteria);
	}

	private DetachedCriteria getCriteria(Tipologia tipologia) {
		DetachedCriteria criteria = DetachedCriteria.forClass(SubstanciaMineral.class)
				.createAlias("tipologiaCollection", "t")
				.createAlias("t.ideTipologiaPai", "tp")
				.add(Restrictions.eq("t.ideTipologia", tipologia.getIdeTipologia()))
				.addOrder(Order.asc("nomSubstanciaMineral"))
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideSubstanciaMineral"), "ideSubstanciaMineral")
						.add(Projections.property("nomSubstanciaMineral"), "nomSubstanciaMineral")
						.add(Projections.property("t.ideTipologia"), "ideTipologia.ideTipologia")
						.add(Projections.property("t.desTipologia"), "ideTipologia.desTipologia")
						.add(Projections.property("t.codTipologia"), "ideTipologia.codTipologia")
						.add(Projections.property("tp.ideTipologia"), "ideTipologia.ideTipologiaPai.ideTipologia")
						.add(Projections.property("tp.desTipologia"), "ideTipologia.ideTipologiaPai.desTipologia")
						.add(Projections.property("tp.codTipologia"), "ideTipologia.ideTipologiaPai.codTipologia")
						)
						.setResultTransformer(new AliasToNestedBeanResultTransformer(SubstanciaMineral.class));
		return criteria;
	}

}
