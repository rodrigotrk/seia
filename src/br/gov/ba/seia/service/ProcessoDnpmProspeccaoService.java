package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.ProcessoDnpmProspeccaoDAOImpl;
import br.gov.ba.seia.entity.ProcessoDnpm;
import br.gov.ba.seia.entity.ProcessoDnpmProspecao;

/**
 * Serviço da classe {@link ProcessoDnpmProspeccao}
 * 
 * @author eduardo.fernandes
 * @since 05/12/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProcessoDnpmProspeccaoService {

	@Inject
	private ProcessoDnpmProspeccaoDAOImpl dao;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Object object)  {
		dao.salvar(object);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoDnpmProspecao> listar(ProcessoDnpm processoDnpm)  {
		return dao.listar(processoDnpm);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(ProcessoDnpm processoDnpm)  {
		dao.excluir(processoDnpm);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(ProcessoDnpmProspecao prospeccao)  {
		dao.excluir(prospeccao);
	}
}
