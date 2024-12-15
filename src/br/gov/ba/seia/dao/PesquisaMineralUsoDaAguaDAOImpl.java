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
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.entity.PesquisaMineral;
import br.gov.ba.seia.entity.PesquisaMineralUsoDaAgua;

/**
 * 
 * @author eduardo.fernandes
 * @since 05/12/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PesquisaMineralUsoDaAguaDAOImpl {

	@Inject
	private IDAO<PesquisaMineralUsoDaAgua> dao;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(PesquisaMineralUsoDaAgua pesquisaMineralUsoDaAgua)  {
		dao.salvarOuAtualizar(pesquisaMineralUsoDaAgua);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(List<PesquisaMineralUsoDaAgua> listaPesquisaMineralUsoDaAgua)  {
		dao.salvarEmLote(listaPesquisaMineralUsoDaAgua);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PesquisaMineralUsoDaAgua> listar(PesquisaMineral pesquisaMineral)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(PesquisaMineralUsoDaAgua.class)
				.createAlias("tipoCaptacao", "tc")
				.createAlias("pesquisaMineralDocumentoCaptacao", "pmDoc", JoinType.LEFT_OUTER_JOIN)
				.createAlias("pesquisaMineral", "pesq")
				.add(Restrictions.eq("pesq.idePesquisaMineral", pesquisaMineral.getIdePesquisaMineral()));
		return dao.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(PesquisaMineral pesquisaMineral)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idePesquisaMineral", pesquisaMineral.getIdePesquisaMineral());
		dao.executarNamedQuery("PesquisaMineralUsoDaAgua.removeByIdePesquisaMineral", parameters);
	}

	/**
	 * 
	 * @author eduardo.fernandes
	 * @since 05/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @param usoAgua
	 * @
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(PesquisaMineralUsoDaAgua usoAgua)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idePesquisaMineralUsoDaAgua", usoAgua.getIdePesquisaMineralUsoDaAgua());
		dao.executarNamedQuery("PesquisaMineralUsoDaAgua.removeByIde", parameters);
	}

}
