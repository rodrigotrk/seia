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
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.DeclaracaoIntervencaoApp;
import br.gov.ba.seia.entity.DeclaracaoIntervencaoAppCaracteristca;

/**
 * @author eduardo.fernandes 
 * @since 11/01/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DeclaracaoIntervencaoAppCaracteristcaDAOImpl {
	
	@Inject
	private IDAO<DeclaracaoIntervencaoAppCaracteristca> dao;

	/**
	 * Método para persitir uma lista de {@link DeclaracaoIntervencaoAppCaracteristca} 
	 * 
	 * @author eduardo.fernandes 
	 * @since 12/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
	 * @param obj
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(List<DeclaracaoIntervencaoAppCaracteristca> lista) {
		dao.salvarEmLote(lista);
	}

	/**
	 * Método para excluir as {@link DeclaracaoIntervencaoAppCaracteristca} de acordo com a {@link DeclaracaoIntervencaoApp}
	 * 
	 * @author eduardo.fernandes 
	 * @since 12/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
	 * @param declaracaoIntervencaoApp
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(DeclaracaoIntervencaoApp declaracaoIntervencaoApp) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideDeclaracaoIntervencaoApp", declaracaoIntervencaoApp.getIdeDeclaracaoIntervencaoApp());
		dao.executarNamedQuery("DeclaracaoIntervencaoAppCaracteristca.removerByDeclaracaoIntervencaoApp", parameters);		
	}

	/**
	 * 
	 * @author eduardo.fernandes 
	 * @since 13/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
	 * @param diap
	 * @return
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DeclaracaoIntervencaoAppCaracteristca> listar(DeclaracaoIntervencaoApp diap) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DeclaracaoIntervencaoAppCaracteristca.class)
				.add(Restrictions.eq("declaracaoIntervencaoApp.ideDeclaracaoIntervencaoApp", diap.getIdeDeclaracaoIntervencaoApp()));
		return dao.listarPorCriteria(criteria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DeclaracaoIntervencaoAppCaracteristca> listarDeclaracoes(Integer ideRequerimento) {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(DeclaracaoIntervencaoAppCaracteristca.class);
		
		criteria.createAlias("declaracaoIntervencaoApp", "dia", JoinType.INNER_JOIN)
				.createAlias("caracteristicaAtividadeIntervencaoApp", "cia", JoinType.INNER_JOIN)
				.createAlias("cia.caracteristicaIntervencaoApp", "cii", JoinType.INNER_JOIN)
				.createAlias("dia.ideAtoDeclaratorio", "ad", JoinType.INNER_JOIN)
				.createAlias("cia.atividadeIntervencaoApp", "aia", JoinType.INNER_JOIN)
				.add(Restrictions.eq("aia.ideAtividadeIntervencaoApp", 5))
				.add(Restrictions.in("cia.caracteristicaIntervencaoApp.ideCaracteristicaIntervencaoApp", new Integer[] {1, 2}))
				.add(Restrictions.eq("ad.ideRequerimento.ideRequerimento", ideRequerimento));
		
		return dao.listarPorCriteria(criteria, Order.desc("cii.nomCaracteristicaIntervencao"));
	}
	
}
