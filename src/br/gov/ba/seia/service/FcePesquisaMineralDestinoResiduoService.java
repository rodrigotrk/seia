package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.FcePesquisaMineralDestinoResiduoDAOImpl;
import br.gov.ba.seia.entity.FcePesquisaMineral;
import br.gov.ba.seia.entity.FcePesquisaMineralDestinoResiduo;
import br.gov.ba.seia.entity.SubstanciaMineral;

/**
 * Service de {@link SubstanciaMineral}
 * 
 * @author eduardo.fernandes, alexandre.queiroz
 * @since 10/06/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7702">#7702</a>
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FcePesquisaMineralDestinoResiduoService {

	@Inject
	private FcePesquisaMineralDestinoResiduoDAOImpl fcePesquisaMineralDestinoResiduoDAO;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FcePesquisaMineralDestinoResiduo> listarFcePesquisaMineralDestinoResiduoBy(FcePesquisaMineral ideFcePesquisaMineral)  {
		return fcePesquisaMineralDestinoResiduoDAO.listarFcePesquisaMineralDestinoResiduoBy(ideFcePesquisaMineral);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirFcePesquisaMineralDestinoResiduoBy(FcePesquisaMineral ideFcePesquisaMineral)  {
		fcePesquisaMineralDestinoResiduoDAO.excluirFcePesquisaMineralDestinoResiduoBy(ideFcePesquisaMineral);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaFcePesquisaMineralDestinoResiduo(List<FcePesquisaMineralDestinoResiduo> listaFcePesquisaMineralDestinoResiduo)  {
		fcePesquisaMineralDestinoResiduoDAO.salvarListaFcePesquisaMineralDestinoResiduo(listaFcePesquisaMineralDestinoResiduo);
	}
	
}
