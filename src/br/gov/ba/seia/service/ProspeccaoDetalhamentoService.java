package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.ProspeccaoDetalhamentoDAOImpl;
import br.gov.ba.seia.entity.ProcessoDnpmProspecao;
import br.gov.ba.seia.entity.ProspecaoDetalhamento;

/**
 * Serviço da classe {@link ProcessoDnpmProspeccao}
 * 
 * @author eduardo.fernandes
 * @since 05/12/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProspeccaoDetalhamentoService {

	@Inject
	private ProspeccaoDetalhamentoDAOImpl dao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Object obj)  {
		dao.salvar(obj);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProspecaoDetalhamento> listar(ProcessoDnpmProspecao processoDnpmProspeccao)  {
		return dao.listar(processoDnpmProspeccao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(Object object)  {
		dao.excluir(object);
	}

}