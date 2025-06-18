package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.PesquisaMineralSubstanciaMineralDAOImpl;
import br.gov.ba.seia.entity.PesquisaMineral;
import br.gov.ba.seia.entity.PesquisaMineralSubstanciaMineral;

/**
 * Serviço da classe {@link PesquisaMineralSubstanciaMineral}
 * 
 * @author eduardo.fernandes
 * @since 05/12/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PesquisaMineralSubstanciaMineralService {

	@Inject
	private PesquisaMineralSubstanciaMineralDAOImpl dao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(PesquisaMineralSubstanciaMineral pesquisaMineralSubstanciaMineral)  {
		dao.salvar(pesquisaMineralSubstanciaMineral);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(List<PesquisaMineralSubstanciaMineral> listaPesquisaMineralSubstanciaMineral)  {
		dao.salvar(listaPesquisaMineralSubstanciaMineral);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PesquisaMineralSubstanciaMineral> listar(PesquisaMineral pesquisaMineral)  {
		return dao.listar(pesquisaMineral);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(PesquisaMineral pesquisaMineral)  {
		dao.excluir(pesquisaMineral);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(PesquisaMineralSubstanciaMineral substancia)  {
		dao.excluir(substancia);
	}
}
