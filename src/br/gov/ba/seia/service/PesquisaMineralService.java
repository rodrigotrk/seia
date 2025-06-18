package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.PesquisaMineralDAOImpl;
import br.gov.ba.seia.entity.CadastroAtividadeNaoSujeitaLic;
import br.gov.ba.seia.entity.PesquisaMineral;

/**
 * Serviço da classe {@link PesquisaMineral}
 * 
 * @author eduardo.fernandes
 * @since 01/12/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PesquisaMineralService {

	@Inject
	private PesquisaMineralDAOImpl dao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(PesquisaMineral pesquisaMineral)  {
		dao.salvar(pesquisaMineral);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PesquisaMineral buscar(CadastroAtividadeNaoSujeitaLic cadastro)  {
		return dao.buscar(cadastro);
	}
}
