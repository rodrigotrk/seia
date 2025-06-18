package br.gov.ba.seia.service.facade;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.ProcessoAtoConcedidoDAOImpl;
import br.gov.ba.seia.entity.AnaliseTecnica;
import br.gov.ba.seia.entity.ProcessoAtoConcedido;
import br.gov.ba.seia.facade.AnaliseTecnicaServiceFacade;
import br.gov.ba.seia.util.Util;


@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DetalharProcessoFacade {

	@EJB
	private AnaliseTecnicaServiceFacade  analiseTecnicaServiceFacade; 
	
	@EJB
	private ProcessoAtoConcedidoDAOImpl processoAtoConcedidoDaoImpl;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoAtoConcedido> listaProcessoConcedidoAprovadoNaAnaliseTecnicaAmcAsvBy(Integer ideProcesso) throws Exception {
		AnaliseTecnica analiseTecnica = analiseTecnicaServiceFacade.buscarAnaliseTecnica(ideProcesso);
		
		if(!Util.isNull(analiseTecnica)){
			return processoAtoConcedidoDaoImpl.listarProcessoConcedidoPorAmcAsv(analiseTecnica, true);
		}
		return null;
	}
	
	
	
	
}
