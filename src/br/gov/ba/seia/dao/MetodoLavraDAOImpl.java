package br.gov.ba.seia.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.FceLicenciamentoMineralMetodoLavra;
import br.gov.ba.seia.entity.MetodoLavra;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author eduardo.fernandes
 * @since 08/07/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MetodoLavraDAOImpl {

	@Inject
	private IDAO<MetodoLavra> metodoLavraIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MetodoLavra> listarMetodoLavra() {
		return metodoLavraIDAO.listarTodos();
	}

	/**
	 * Método que vai retornar os {@link MetodoLavra} que foram associados
	 * ao {@link FceLicenciamentoMineral}.
	 *
	 * @author eduardo.fernandes
	 * @since 11/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param licenciamentoMineral
	 * @return MetodoLavra
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<MetodoLavra> listarMetodoLavraBy(FceLicenciamentoMineral licenciamentoMineral)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceLicenciamentoMineralMetodoLavra.class)
				.createAlias("ideMetodoLavra", "ml")
				.add(Restrictions.eq("fceLicenciamentoMineral.ideFceLicenciamentoMineral", licenciamentoMineral.getIdeFceLicenciamentoMineral()))
				.setProjection(Projections.projectionList()
						.add(Projections.property("ml.ideMetodoLavra"),"ideMetodoLavra")
						.add(Projections.property("ml.nomMetodoLavra"), "nomMetodoLavra"))
						.setResultTransformer(new AliasToNestedBeanResultTransformer(MetodoLavra.class));
		return metodoLavraIDAO.listarPorCriteria(criteria);
	}

}
