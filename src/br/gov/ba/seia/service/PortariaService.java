package br.gov.ba.seia.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.PortariaDAOimpl;
import br.gov.ba.seia.entity.Portaria;
import br.gov.ba.seia.entity.Processo;

/**
 * @author Alexandre Queiroz
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PortariaService {

	@EJB
	private PortariaDAOimpl portariaDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOuAtualizar(Portaria portaria){
		portariaDAO.salvarOuAtualizar(portaria);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Portaria buscarPortariaByProcesso(Processo processo){
		return portariaDAO.buscarPortariaByProcesso(processo);
	}
}
