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
import br.gov.ba.seia.entity.PesquisaMineralResponsavelTecnico;

/**
 * 
 * @author eduardo.fernandes
 * @since 05/12/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PesquisaMineralResponsavelTecnicoDAOImpl {

	@Inject
	private IDAO<PesquisaMineralResponsavelTecnico> dao;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(PesquisaMineralResponsavelTecnico pesquisaMineralResponsavelTecnico)  {
		dao.salvarOuAtualizar(pesquisaMineralResponsavelTecnico);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarLista(List<PesquisaMineralResponsavelTecnico> listaPesquisaMineralResponsavelTecnico)  {
		dao.salvarEmLote(listaPesquisaMineralResponsavelTecnico);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PesquisaMineralResponsavelTecnico> listar(PesquisaMineral pesquisaMineral)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(PesquisaMineralResponsavelTecnico.class)
				.createAlias("idePessoaFisicaResponsavelTecnico", "pf")
				.createAlias("pesquisaMineral", "pesq")
				.createAlias("formacaoProfissional", "for")
				.add(Restrictions.eq("pesq.idePesquisaMineral", pesquisaMineral.getIdePesquisaMineral()));
		return dao.listarPorCriteria(criteria);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(PesquisaMineral pesquisaMineral)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idePesquisaMineral", pesquisaMineral.getIdePesquisaMineral());
		dao.executarNamedQuery("PesquisaMineralResponsavelTecnico.removeByIdePesquisaMineral", parameters);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(PesquisaMineralResponsavelTecnico respTecnico)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("idePesquisaMineralResponsavelTecnico", respTecnico.getIdePesquisaMineralResponsavelTecnico());
		dao.executarNamedQuery("PesquisaMineralResponsavelTecnico.removeByIde", parameters);
	}

}
