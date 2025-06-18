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
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.FceLicenciamentoMineralTecnicaLavra;
import br.gov.ba.seia.entity.MetodoLavra;
import br.gov.ba.seia.entity.TecnicaLavra;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author eduardo.fernandes
 * @since 08/07/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TecnicaLavraDAOImpl {

	@Inject
	private IDAO<TecnicaLavra> tecnicaLavraIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TecnicaLavra> listarTecnicaLavraBy(MetodoLavra metodoLavra)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(TecnicaLavra.class, "tl");
		criteria.add(Restrictions.or(Restrictions.eq("tl.metodoLavra.ideMetodoLavra", metodoLavra.getIdeMetodoLavra()),
				Restrictions.and(Restrictions.isNull("tl.metodoLavra.ideMetodoLavra"), Restrictions.isNull("tl.ideTecnicaLavraPai.ideTecnicaLavra"))));
		return tecnicaLavraIDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TecnicaLavra> listarTecnicaLavraBy(TecnicaLavra tecnicaLavra)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(TecnicaLavra.class, "tl");
		criteria.add(Restrictions.eq("tl.ideTecnicaLavraPai.ideTecnicaLavra", tecnicaLavra.getIdeTecnicaLavra()));
		return tecnicaLavraIDAO.listarPorCriteria(criteria);
	}

	/**
	 * Método que vai retornar os {@link TecnicaLavra} que foram associados ao
	 * {@link FceLicenciamentoMineral}.
	 *
	 * @author eduardo.fernandes
	 * @since 11/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param licenciamentoMineral
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TecnicaLavra> listarTecnicaLavraBy(FceLicenciamentoMineral licenciamentoMineral)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceLicenciamentoMineralTecnicaLavra.class)
				.createAlias("ideTecnicaLavra", "tl")
				.createAlias("tl.ideTecnicaLavraPai", "tlp", JoinType.LEFT_OUTER_JOIN)
				.createAlias("tl.metodoLavra", "ml", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("fceLicenciamentoMineral.ideFceLicenciamentoMineral", licenciamentoMineral.getIdeFceLicenciamentoMineral()))
				.setProjection(Projections.projectionList()
						.add(Projections.property("tl.ideTecnicaLavra"),"ideTecnicaLavra")
						.add(Projections.property("tl.nomTecnicaLavra"),"nomTecnicaLavra")
						.add(Projections.property("tlp.ideTecnicaLavra"),"ideTecnicaLavraPai.ideTecnicaLavra")
						.add(Projections.property("tlp.nomTecnicaLavra"),"ideTecnicaLavraPai.nomTecnicaLavra")
						.add(Projections.property("ml.ideMetodoLavra"),"metodoLavra.ideMetodoLavra")
						.add(Projections.property("ml.nomMetodoLavra"),"metodoLavra.nomMetodoLavra")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(TecnicaLavra.class));
		return tecnicaLavraIDAO.listarPorCriteria(criteria);
	}
}
