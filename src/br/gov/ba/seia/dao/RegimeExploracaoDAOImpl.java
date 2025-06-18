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

import br.gov.ba.seia.entity.FceLicenciamentoExploracaoRegimeExploracao;
import br.gov.ba.seia.entity.FceLicenciamentoMineral;
import br.gov.ba.seia.entity.RegimeExploracao;
import br.gov.ba.seia.util.AliasToNestedBeanResultTransformer;

/**
 * @author eduardo.fernandes
 * @since 08/07/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class RegimeExploracaoDAOImpl {

	@Inject
	private IDAO<RegimeExploracao> regimeExploracaoIDAO;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RegimeExploracao> listarRegimeExploracao() {
		return regimeExploracaoIDAO.listarTodos();
	}

	/**
	 * Método que vai retornar os {@link RegimeExploracao} que foram associados
	 * ao {@link FceLicenciamentoMineral}.
	 *
	 * @author eduardo.fernandes
	 * @since 11/07/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
	 * @param fceLicenciamentoMineral
	 * @return
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<RegimeExploracao> listarRegimeExploracao(FceLicenciamentoMineral fceLicenciamentoMineral)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(FceLicenciamentoExploracaoRegimeExploracao.class)
				.createAlias("ideRegimeExploracao", "re")
				.add(Restrictions.eq("fceLicenciamentoMineral.ideFceLicenciamentoMineral", fceLicenciamentoMineral.getIdeFceLicenciamentoMineral()))
				.setProjection(Projections.projectionList()
						.add(Projections.property("re.ideRegimeExploracao"),"ideRegimeExploracao")
						.add(Projections.property("re.nomRegimeExploracao"), "nomRegimeExploracao"))
						.setResultTransformer(new AliasToNestedBeanResultTransformer(RegimeExploracao.class));
		return regimeExploracaoIDAO.listarPorCriteria(criteria);
	}

}
