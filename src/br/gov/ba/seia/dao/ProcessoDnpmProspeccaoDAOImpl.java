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

import br.gov.ba.seia.entity.ProcessoDnpm;
import br.gov.ba.seia.entity.ProcessoDnpmProspecao;

/**
 * 
 * @author eduardo.fernandes
 * @since 05/12/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProcessoDnpmProspeccaoDAOImpl {

	@Inject
	private IDAO<ProcessoDnpmProspecao> dao;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Object obj)  {
		if (obj instanceof ProcessoDnpmProspecao) {
			dao.salvarOuAtualizar((ProcessoDnpmProspecao) obj);
		}
		else if (obj instanceof ProcessoDnpm) {
			dao.salvarEmLote(((ProcessoDnpm) obj).getListaProcessoDnpmProspecao());
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoDnpmProspecao> listar(ProcessoDnpm processoDnpm)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProcessoDnpmProspecao.class)
				.createAlias("ideMetodoProspeccao", "met")
				.createAlias("processoDnpm", "proc")
				.add(Restrictions.eq("proc.ideProcessoDnpm", processoDnpm.getIdeProcessoDnpm()));
		return dao.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(ProcessoDnpm processoDnpm)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideProcessoDnpm", processoDnpm.getIdeProcessoDnpm());
		dao.executarNamedQuery("ProcessoDnpmProspecao.removeByIdeProcessoDnpm", parameters);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(ProcessoDnpmProspecao prospeccao)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ideProcessoDnpmProspecao", prospeccao.getIdeProcessoDnpmProspecao());
		dao.executarNamedQuery("ProcessoDnpmProspecao.removeByIde", parameters);
	}

}
