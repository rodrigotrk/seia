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
import br.gov.ba.seia.entity.FcePesquisaMineral;
import br.gov.ba.seia.entity.PesquisaMineral;
import br.gov.ba.seia.entity.ProcessoDnpm;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author Alexandre Queiroz
 *
 */

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProcessoDnpmDAOImpl {

	@Inject
	private IDAO<ProcessoDnpm> iDAO;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarProcessoDnpm(List<ProcessoDnpm> pLista)  {
		iDAO.salvarEmLote(pLista);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ProcessoDnpm processoDnpm)  {
		iDAO.salvarOuAtualizar(processoDnpm);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoDnpm> listarProcessoDnpmBy(Object object)  {
		if(object instanceof FcePesquisaMineral){
			return listarProcessoDnpmByFcePesquisaMineral((FcePesquisaMineral) object);
		}
		else if(object instanceof FceLicenciamentoMineral){
			return listarProcessoDnpmByFceLicenciamentoMineral((FceLicenciamentoMineral) object);
		}
		else if (object instanceof PesquisaMineral) {
			return listarProcessoDnpmByPesquisaMineral((PesquisaMineral) object);
		}
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<ProcessoDnpm> listarProcessoDnpmByFcePesquisaMineral(FcePesquisaMineral fcePesquisaMineral)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoDnpm.class)
				.createAlias("ideFcePesquisaMineral", "fpm")
				.add(Restrictions.eq("ideFcePesquisaMineral.ideFcePesquisaMineral", fcePesquisaMineral.getIdeFcePesquisaMineral()))
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideProcessoDnpm"), "ideProcessoDnpm")
						.add(Projections.property("numProcessoDnpm"), "numProcessoDnpm")
						.add(Projections.property("fpm.ideFcePesquisaMineral"), "ideFcePesquisaMineral.ideFcePesquisaMineral")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoDnpm.class));
		return iDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<ProcessoDnpm> listarProcessoDnpmByFceLicenciamentoMineral(FceLicenciamentoMineral fceLicenciamentoMineral)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoDnpm.class)
				.createAlias("ideLocalizacaoGeografica", "lg")
				.createAlias("ideFceLicenciamentoMineral", "flm")
				.add(Restrictions.eq("flm.ideFceLicenciamentoMineral", fceLicenciamentoMineral.getIdeFceLicenciamentoMineral()))
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideProcessoDnpm"), "ideProcessoDnpm")
						.add(Projections.property("areaProcessoDnpm"), "areaProcessoDnpm")
						.add(Projections.property("numProcessoDnpm"), "numProcessoDnpm")
						.add(Projections.property("flm.ideFceLicenciamentoMineral"), "ideFceLicenciamentoMineral.ideFceLicenciamentoMineral")
						.add(Projections.property("lg.ideLocalizacaoGeografica"), "ideLocalizacaoGeografica.ideLocalizacaoGeografica")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoDnpm.class));
		return iDAO.listarPorCriteria(criteria);
	}

	/**
	 * @author eduardo.fernandes
	 * @since 05/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param pesquisaMineral
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<ProcessoDnpm> listarProcessoDnpmByPesquisaMineral(PesquisaMineral pesquisaMineral)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoDnpm.class)
				.createAlias("ideLocalizacaoGeografica", "lg")
				.createAlias("idePesquisaMineral", "pm")
				.add(Restrictions.eq("pm.idePesquisaMineral", pesquisaMineral.getIdePesquisaMineral()))
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideProcessoDnpm"), "ideProcessoDnpm")
						.add(Projections.property("areaProcessoDnpm"), "areaProcessoDnpm")
						.add(Projections.property("numProcessoDnpm"), "numProcessoDnpm")
						.add(Projections.property("pm.idePesquisaMineral"), "idePesquisaMineral.idePesquisaMineral")
						.add(Projections.property("lg.ideLocalizacaoGeografica"), "ideLocalizacaoGeografica.ideLocalizacaoGeografica")
				).setResultTransformer(new AliasToNestedBeanResultTransformer(ProcessoDnpm.class));
		return iDAO.listarPorCriteria(criteria);
	}

	/**
	 * 
	 * @author eduardo.fernandes
	 * @since 13/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineral
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirListaProcessoDnpm(FceLicenciamentoMineral fceLicenciamentoMineral)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceLicenciamentoMineral", fceLicenciamentoMineral.getIdeFceLicenciamentoMineral());
		iDAO.executarNamedQuery("ProcessoDnpm.removeByIdeFceLicenciamentoMineral", parameters);
	}

	/**
	 * 
	 * @author alexandre.queiroz
	 * @since 13/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7703">#7703</a>
	 * @param fcePesquisaMineral
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirListaProcessoDnpm(FcePesquisaMineral fceLicenciamentoMineral)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFcePesquisaMineral", fceLicenciamentoMineral.getIdeFcePesquisaMineral());
		iDAO.executarNamedQuery("ProcessoDnpm.removeByIdeFcePesquisaMineral", parameters);
	}
	
	/**
	 * 
	 * @author eduardo.fernandes
	 * @since 05/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param pesquisaMineral
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirListaProcessoDnpm(PesquisaMineral pesquisaMineral)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idePesquisaMineral", pesquisaMineral.getIdePesquisaMineral());
		iDAO.executarNamedQuery("ProcessoDnpm.removeByIdePesquisaMineral", parameters);
	}

	/**
	 * 
	 * @author alexandre.queiroz
	 * @since 13/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7703">#7703</a>
	 * @param fcePesquisaMineral
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirProcessoDnpmByIdeProcessoDnpm(ProcessoDnpm ideProcessoDnpm)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideProcessoDnpm", ideProcessoDnpm.getIdeProcessoDnpm());
		iDAO.executarNamedQuery("ProcessoDnpm.removeByideProcessoDnpm", parameters);
	}
}
