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

import br.gov.ba.seia.entity.PesquisaMineral;
import br.gov.ba.seia.entity.PesquisaMineralSubstanciaMineral;

/**
 * ADICIONAR COMENTÁRIO
 * 
 * @author eduardo.fernandes
 * @since 05/12/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PesquisaMineralSubstanciaMineralDAOImpl {

	@Inject
	private IDAO<PesquisaMineralSubstanciaMineral> dao;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(PesquisaMineralSubstanciaMineral pesquisaMineralSubstanciaMineral)  {
		dao.salvarOuAtualizar(pesquisaMineralSubstanciaMineral);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(List<PesquisaMineralSubstanciaMineral> listaPesquisaMineralSubstanciaMineral)  {
		dao.salvarEmLote(listaPesquisaMineralSubstanciaMineral);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PesquisaMineralSubstanciaMineral> listar(PesquisaMineral pesquisaMineral)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(PesquisaMineralSubstanciaMineral.class)
				.createAlias("ideSubstanciaMineral", "sub")
				.createAlias("pesquisaMineral", "pesq")
				.add(Restrictions.eq("pesq.idePesquisaMineral", pesquisaMineral.getIdePesquisaMineral()));
		return dao.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(PesquisaMineral pesquisaMineral)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idePesquisaMineral", pesquisaMineral.getIdePesquisaMineral());
		dao.executarNamedQuery("PesquisaMineralSubstanciaMineral.removeByIdePesquisaMineral", parameters);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(PesquisaMineralSubstanciaMineral substancia)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idePesquisaMineralSubstanciaMineral", substancia.getIdePesquisaMineralSubstanciaMineral());
		dao.executarNamedQuery("PesquisaMineralSubstanciaMineral.removeByIde", parameters);
	}

}
