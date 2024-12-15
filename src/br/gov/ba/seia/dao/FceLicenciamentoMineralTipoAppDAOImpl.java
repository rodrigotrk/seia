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
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.FceLicenciamentoMineralTipoApp;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author eduardo.fernandes
 * @since 08/07/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FceLicenciamentoMineralTipoAppDAOImpl {

	@Inject
	private IDAO<FceLicenciamentoMineralTipoApp> iDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarFceLicenciamentoMineralTipoApp(FceLicenciamentoMineralTipoApp tipoApp) {
		iDAO.salvarOuAtualizar(tipoApp);
	}

	/**
	 * Método que vai retornar os {@link FceLicenciamentoMineralTipoApp} que
	 * foram associados ao {@link FceLicenciamentoMineral}.
	 *
	 * @author eduardo.fernandes
	 * @since 11/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineral
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FceLicenciamentoMineralTipoApp> listarFceLicenciamentoMineralTipoAppBy(FceLicenciamentoMineral fceLicenciamentoMineral) {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceLicenciamentoMineralTipoApp.class)
				.createAlias("tipoApp", "app")
				.add(Restrictions.eq("fceLicenciamentoMineral.ideFceLicenciamentoMineral", fceLicenciamentoMineral.getIdeFceLicenciamentoMineral()))
				.setProjection(Projections.projectionList()
						.add(Projections.property("fceLicenciamentoMineral.ideFceLicenciamentoMineral"),"fceLicenciamentoMineral.ideFceLicenciamentoMineral")
						.add(Projections.property("app.ideTipoApp"),"tipoApp.ideTipoApp")
						.add(Projections.property("app.dscTipoApp"), "tipoApp.dscTipoApp"))
						.setResultTransformer(new AliasToNestedBeanResultTransformer(FceLicenciamentoMineralTipoApp.class));
		return iDAO.listarPorCriteria(criteria);
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
	public void excluir(FceLicenciamentoMineral fceLicenciamentoMineral)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceLicenciamentoMineral", fceLicenciamentoMineral.getIdeFceLicenciamentoMineral());
		iDAO.executarNamedQuery("FceLicenciamentoMineralTipoApp.removeByIdeFceLicenciamentoMineral", parameters);
	}
}
