package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.List;
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
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.FceLicenciamentoMineralProducaoProduto;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author eduardo.fernandes
 * @since 25/07/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceLicenciamentoMineralProducaoProdutoDAOImpl {

	@Inject
	private IDAO<FceLicenciamentoMineralProducaoProduto> iDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFceLicenciamentoMineralProducaoProduto(List<FceLicenciamentoMineralProducaoProduto> listProducaoProduto)  {
		iDAO.salvarEmLote(listProducaoProduto);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceLicenciamentoMineralProducaoProduto> listarFceLicenciamentoMineralProducaoProdutoBy(FceLicenciamentoMineral fceLicenciamentoMineral)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceLicenciamentoMineralProducaoProduto.class)
				.createAlias("fceLicenciamentoMineral", "flm")
				.createAlias("producaoProduto", "pp")
				.createAlias("pp.ideUnidadeMedida", "un", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("flm.ideFceLicenciamentoMineral", fceLicenciamentoMineral.getIdeFceLicenciamentoMineral())
						).setProjection(Projections.projectionList()
								.add(Projections.property("valProducao"), "valProducao")
								.add(Projections.property("flm.ideFceLicenciamentoMineral"), "fceLicenciamentoMineral.ideFceLicenciamentoMineral")
								.add(Projections.property("pp.ideProducaoProduto"), "producaoProduto.ideProducaoProduto")
								.add(Projections.property("pp.nomProducaoProduto"), "producaoProduto.nomProducaoProduto")
								.add(Projections.property("un.ideUnidadeMedida"), "producaoProduto.ideUnidadeMedida.ideUnidadeMedida")
								.add(Projections.property("un.codUnidadeMedida"), "producaoProduto.ideUnidadeMedida.codUnidadeMedida")
								).setResultTransformer(new AliasToNestedBeanResultTransformer(FceLicenciamentoMineralProducaoProduto.class)
										);
		return iDAO.listarPorCriteria(criteria, Order.asc("producaoProduto.nomProducaoProduto"));
	}

	/**
	 *
	 * @author eduardo.fernandes
	 * @since 27/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/">#</a> 
	 * @param fceLicenciamentoMineral
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(FceLicenciamentoMineral fceLicenciamentoMineral) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceLicenciamentoMineral", fceLicenciamentoMineral.getIdeFceLicenciamentoMineral());
		iDAO.executarNamedQuery("FceLicenciamentoMineralProducaoProduto.removeByIdeFceLicenciamentoMineral", parameters);
	}

}
