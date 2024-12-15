package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.DeclaracaoIntervencaoAppCaracteristcaDAOImpl;
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
public class DeclaracaoIntervencaoAppCaracteristcaService {

	@Inject
	private DeclaracaoIntervencaoAppCaracteristcaDAOImpl dao;
	
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
	public void salvar(DeclaracaoIntervencaoApp declaracaoIntervencaoApp, List<DeclaracaoIntervencaoAppCaracteristca> lista)  {
		excluirBy(declaracaoIntervencaoApp);
		dao.salvar(lista);
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
	private void excluirBy(DeclaracaoIntervencaoApp declaracaoIntervencaoApp) {
		dao.excluir(declaracaoIntervencaoApp);
	}

	/**
	 * ADICIONAR COMENTÁRIO
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
		return dao.listar(diap);
	}
	
	public List<DeclaracaoIntervencaoAppCaracteristca> listarDeclaracoes(Integer ideRequerimento) {
		return dao.listarDeclaracoes(ideRequerimento);
	}
	
	
}
