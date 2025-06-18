package br.gov.ba.seia.service;
/**
 * 		09/05/14
 * @author marcelo.brito
 */

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
import br.gov.ba.seia.entity.DestinoResiduo;
import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.FceLicenciamentoMineralDestinoResiduo;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DestinoResiduoService {

	@Inject
	private IDAO<DestinoResiduo> destinoResiduoIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DestinoResiduo> buscarListaDestinoResiduo(){
		DetachedCriteria criteria = DetachedCriteria.forClass(DestinoResiduo.class)
				.add(Restrictions.eq("indAtivo", true))
				.addOrder(Order.asc("dataCadastro"));
		
		return destinoResiduoIDAO.listarPorCriteria(criteria, Order.asc("ideDestinoResiduo"));
	}

	/**
	 * Método que vai retornar os {@link DestinoResiduo} que foram associados ao
	 * {@link FceLicenciamentoMineral}.
	 * 
	 * @author eduardo.fernandes
	 * @since 11/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineral
	 * @return
	 * @throws Exception 
	 */
	public List<DestinoResiduo> listarDestinoResiduoBy(FceLicenciamentoMineral fceLicenciamentoMineral){
		DetachedCriteria criteria = DetachedCriteria.forClass(FceLicenciamentoMineralDestinoResiduo.class)
				.createAlias("ideDestinoResiduo", "dr")
				.add(Restrictions.eq("fceLicenciamentoMineral.ideFceLicenciamentoMineral", fceLicenciamentoMineral.getIdeFceLicenciamentoMineral()))
				.setProjection(Projections.projectionList()
						.add(Projections.property("dr.ideDestinoResiduo"),"ideDestinoResiduo")
						.add(Projections.property("dr.dscDestinoResiduo"), "dscDestinoResiduo")
						.add(Projections.property("dr.indAtivo"), "indAtivo")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(DestinoResiduo.class));
		return destinoResiduoIDAO.listarPorCriteria(criteria);

	}
}
