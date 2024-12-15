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
import br.gov.ba.seia.entity.ProspecaoDetalhamento;

/**
 * 
 * @author eduardo.fernandes
 * @since 05/12/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProspeccaoDetalhamentoDAOImpl {

	@Inject
	private IDAO<ProspecaoDetalhamento> dao;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Object obj)  {
		if (obj instanceof ProspecaoDetalhamento) {
			dao.salvarOuAtualizar((ProspecaoDetalhamento) obj);
		} else if(obj instanceof ProcessoDnpm) {
			ProcessoDnpm dpnm = ((ProcessoDnpm) obj);
			for (ProcessoDnpmProspecao prospeccao : dpnm.getListaProcessoDnpmProspecao()) {
				dao.salvarEmLote(prospeccao.getProspecaoDetalhamentos());
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProspecaoDetalhamento> listar(ProcessoDnpmProspecao processoDnpmProspecao)  {
		DetachedCriteria criteria = DetachedCriteria.forClass(ProspecaoDetalhamento.class)
				.createAlias("ideLocalizacaoGeografica", "loc")
				.createAlias("loc.dadoGeograficoCollection", "dg")
				.createAlias("processoDnpmProspecao", "pro")
				.createAlias("pro.ideMetodoProspeccao", "met")
				.createAlias("pro.processoDnpm", "dnpm")
				.add(Restrictions.eq("pro.ideProcessoDnpmProspecao", processoDnpmProspecao.getIdeProcessoDnpmProspecao()));
		List<ProspecaoDetalhamento> lista = dao.listarPorCriteria(criteria);
		for (ProspecaoDetalhamento det : lista) {
			det.getIdeLocalizacaoGeografica().getDadoGeograficoCollection().iterator().next();
			det.getProcessoDnpmProspecao().getProspecaoDetalhamentos().iterator().next();
		}
		return lista;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(Object object)  {
		Map<String, Object> parameters = new HashMap<String, Object>();
		String namedQuery = "ProspecaoDetalhamento.removeByIde";
		if (object instanceof ProcessoDnpmProspecao) {
			parameters.put("ideProcessoDnpmProspecao", ((ProcessoDnpmProspecao) object).getIdeProcessoDnpmProspecao());
			namedQuery += "ProcessoDnpmProspecao";
		}
		else if (object instanceof ProcessoDnpm) {
			parameters.put("ideProcessoDnpm", ((ProcessoDnpm) object).getIdeProcessoDnpm());
			namedQuery += "ProcessoDnpm";
		}
		else if (object instanceof ProspecaoDetalhamento) {
			parameters.put("ideProspeccaoDetalhamento", ((ProspecaoDetalhamento) object).getIdeProspeccaoDetalhamento());
		}
		dao.executarNamedQuery(namedQuery, parameters);
	}

}
