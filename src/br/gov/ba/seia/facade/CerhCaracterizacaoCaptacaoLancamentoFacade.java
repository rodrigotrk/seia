package br.gov.ba.seia.facade;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.CerhOutrosUsosDAOImpl;
import br.gov.ba.seia.entity.CerhOutrosUsos;
import br.gov.ba.seia.entity.CerhTipoPrestadorServico;
import br.gov.ba.seia.service.CerhTipoPrestadorServicoService;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CerhCaracterizacaoCaptacaoLancamentoFacade extends CerhFinalidadeFacade {

	@EJB
	protected CerhOutrosUsosDAOImpl outrosUsosDAO;

	@EJB
	private CerhTipoPrestadorServicoService tipoPrestadorService;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhOutrosUsos> listarOutrosUsos(){
		return outrosUsosDAO.listarTodos();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<CerhTipoPrestadorServico> listarTipoPrestadorServicoCollection() {
		return tipoPrestadorService.listarTodos();
	}
	
	
}
