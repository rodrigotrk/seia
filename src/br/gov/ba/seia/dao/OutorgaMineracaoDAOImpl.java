package br.gov.ba.seia.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.FcePesquisaMineral;
import br.gov.ba.seia.entity.OutorgaMineracao;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author eduardo.fernandes
 * @since 15/07/2016
 *
 */
public class OutorgaMineracaoDAOImpl {

	@Inject
	private IDAO<OutorgaMineracao> IDAO;

	/**
	 * ADICIONAR COMENTÁRIO
	 *
	 * @author eduardo.fernandes
	 * @since 15/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param lista
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaOutorgaMineracao(List<OutorgaMineracao> lista) throws Exception {
		for (OutorgaMineracao outorgaMineracao : lista) {
			IDAO.salvarOuAtualizar(outorgaMineracao);
		}
	}

	/**
	 * ADICIONAR COMENTÁRIO
	 *
	 * @author eduardo.fernandes
	 * @since 15/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param object
	 * @return
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<OutorgaMineracao> listarOutorgaMineracaoBy(Object object) throws Exception {
		if(object instanceof FcePesquisaMineral){
			return listarOutorgaMineracaoByFcePesquisaMineral((FcePesquisaMineral) object);
		}
		else if(object instanceof FceLicenciamentoMineral){
			return listarOutorgaMineracaoByFceLicenciamentoMineral((FceLicenciamentoMineral) object);
		}
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<OutorgaMineracao> listarOutorgaMineracaoByFcePesquisaMineral(FcePesquisaMineral fcePesquisaMineral) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(OutorgaMineracao.class)
				.createAlias("fcePesquisaMineral", "fpm")
				.add(Restrictions.eq("fpm.ideFcePesquisaMineral", fcePesquisaMineral.getIdeFcePesquisaMineral()))
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideOutorgaMineracao"), "ideOutorgaMineracao")
						.add(Projections.property("numPortariaOutorga"), "numPortariaOutorga")
						.add(Projections.property("numProcessoOutorga"), "numProcessoOutorga")
						.add(Projections.property("fpm.ideFcePesquisaMineral"), "fcePesquisaMineral.ideFcePesquisaMineral")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(OutorgaMineracao.class));
		return IDAO.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private List<OutorgaMineracao> listarOutorgaMineracaoByFceLicenciamentoMineral(FceLicenciamentoMineral fceLicenciamentoMineral) throws Exception {
		DetachedCriteria criteria = DetachedCriteria.forClass(OutorgaMineracao.class)
				.createAlias("fceLicenciamentoMineral", "flm")
				.add(Restrictions.eq("flm.ideFceLicenciamentoMineral", fceLicenciamentoMineral.getIdeFceLicenciamentoMineral()))
				.setProjection(Projections.projectionList()
						.add(Projections.property("ideOutorgaMineracao"), "ideOutorgaMineracao")
						.add(Projections.property("numPortariaOutorga"), "numPortariaOutorga")
						.add(Projections.property("numProcessoOutorga"), "numProcessoOutorga")
						.add(Projections.property("flm.ideFceLicenciamentoMineral"), "fceLicenciamentoMineral.ideFceLicenciamentoMineral")
						).setResultTransformer(new AliasToNestedBeanResultTransformer(OutorgaMineracao.class));
		return IDAO.listarPorCriteria(criteria);
	}

	/**
	 * ADICIONAR COMENTÁRIO
	 *
	 * @author eduardo.fernandes
	 * @since 15/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineral
	 * @throws Exception
	 */
	@Deprecated
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirListaOutorgaMineracao(FceLicenciamentoMineral fceLicenciamentoMineral) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideFceLicenciamentoMineral", fceLicenciamentoMineral.getIdeFceLicenciamentoMineral());
		IDAO.executarNamedQuery("ProcessoDnpm.removeByIdeFceLicenciamentoMineral", parameters);
	}

}
