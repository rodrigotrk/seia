package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.PesquisaMineralResponsavelTecnicoDAOImpl;
import br.gov.ba.seia.entity.PesquisaMineral;
import br.gov.ba.seia.entity.PesquisaMineralResponsavelTecnico;
import br.gov.ba.seia.util.Util;

/**
 * Serviço da classe {@link PesquisaMineralResponsavelTecnico}
 * 
 * @author eduardo.fernandes
 * @since 05/12/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PesquisaMineralResponsavelTecnicoService {

	@Inject
	private PesquisaMineralResponsavelTecnicoDAOImpl dao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(PesquisaMineralResponsavelTecnico pesquisaMineralResponsavelTecnico)  {
		dao.salvar(pesquisaMineralResponsavelTecnico);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(List<PesquisaMineralResponsavelTecnico> listaPesquisaMineralResponsavelTecnico)  {
		dao.salvarLista(listaPesquisaMineralResponsavelTecnico);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PesquisaMineralResponsavelTecnico> listar(PesquisaMineral pesquisaMineral)  {
		return dao.listar(pesquisaMineral);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(PesquisaMineral pesquisaMineral)  {
		dao.excluir(pesquisaMineral);
	}

	/**
	 * ADICIONAR COMENTÁRIO
	 * 
	 * @author eduardo.fernandes
	 * @since 05/12/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a> ADICIONAR TICKET
	 * @param respTecnico
	 */
	public void excluir(PesquisaMineralResponsavelTecnico respTecnico)  {
		if(!Util.isNull(respTecnico.getIdePesquisaMineralResponsavelTecnico())){
			dao.excluir(respTecnico);
		}
	}
}
