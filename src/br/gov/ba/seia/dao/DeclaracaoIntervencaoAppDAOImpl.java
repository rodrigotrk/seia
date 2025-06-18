package br.gov.ba.seia.dao;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.DeclaracaoIntervencaoApp;
import br.gov.ba.seia.entity.Requerimento;

/**
 * @author eduardo.fernandes 
 * @since 11/01/2017
 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DeclaracaoIntervencaoAppDAOImpl {

	@Inject
	private IDAO<DeclaracaoIntervencaoApp> dao;

	/**
	 * Método para salvar/atualizar a {@link DeclaracaoIntervencaoApp}
	 * 
	 * @author eduardo.fernandes 
	 * @since 11/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
	 * @param declaracaoIntervencaoApp
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(DeclaracaoIntervencaoApp declaracaoIntervencaoApp) {
		dao.salvarOuAtualizar(declaracaoIntervencaoApp);
	}

	/**
	 * Método para buscar a {@link DeclaracaoIntervencaoApp} de acordo com o parâmetro.
	 * 
	 * @author eduardo.fernandes 
	 * @since 12/01/2017
	 * @see <a href="http://10.105.17.77/redmine/issues/8263">#8263</a>
	 * @param requerimento
	 * @return
	 * @throws Exception 
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DeclaracaoIntervencaoApp buscar(Requerimento requerimento) {
		DetachedCriteria criteria = DetachedCriteria.forClass(DeclaracaoIntervencaoApp.class)
				.createAlias("ideAtoDeclaratorio", "ad")
				.createAlias("ad.ideRequerimento", "req")
				.createAlias("ad.ideDocumentoObrigatorio", "doc")
				.createAlias("ideLocalizacaoGeografica", "lg", JoinType.LEFT_OUTER_JOIN)
				.createAlias("lg.ideSistemaCoordenada", "sc", JoinType.LEFT_OUTER_JOIN)
				.createAlias("lg.dadoGeograficoCollection", "dg", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.eq("req.ideRequerimento", requerimento.getIdeRequerimento()))
				;
		return dao.buscarPorCriteria(criteria);
	}
	
}
