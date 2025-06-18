package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.PesquisaMineralDocumentoCaptacaoDAOImpl;
import br.gov.ba.seia.entity.PesquisaMineralDocumentoCaptacao;
import br.gov.ba.seia.entity.PesquisaMineralUsoDaAgua;

/**
 * Serviço da classe {@link PesquisaMineralDocumentoCaptacao}
 * 
 * @author eduardo.fernandes 
 * @since 18/11/2016
 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PesquisaMineralDocumentoCaptacaoService {

	@Inject
	private PesquisaMineralDocumentoCaptacaoDAOImpl dao;
	
	/**
	 * Método que lista todos os {@link PesquisaMineralDocumentoCaptacao}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 18/11/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8188">#8188</a>
	 * @return List<PesquisaMineralDocumentoCaptacao>
	 */ 
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<PesquisaMineralDocumentoCaptacao> listarPesquisaMineralDocumentoCaptacao() {
		return dao.listarTodosDocumentoCaptacao();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public PesquisaMineralDocumentoCaptacao buscar(PesquisaMineralUsoDaAgua usoAgua) {
		return dao.buscar(usoAgua);
	}
	
}
