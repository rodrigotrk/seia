package br.gov.ba.seia.service;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.facade.ReaberturaProcessoFacade;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TramitacaoProcessoService {

	@EJB
	private ReaberturaProcessoFacade reaberturaProcessoFacade;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void tramitarProcessoAtualizandoAnterior(Integer ideProcesso,Integer ideStatusFluxo, Integer ideArea) throws Exception {

		reaberturaProcessoFacade.atualizarTramitacaoAnterior(ideProcesso);
		reaberturaProcessoFacade.salvarControleTramitacao(ideProcesso, true, null, null, ideStatusFluxo,
				ideArea);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void tramitarProcessoAtualizandoAnterior(Integer ideProcesso,Integer ideStatusFluxo, Integer ideArea, String comentarioExterior,String comentarioInterior) throws Exception {
		reaberturaProcessoFacade.atualizarTramitacaoAnterior(ideProcesso);
		reaberturaProcessoFacade.salvarControleTramitacao(ideProcesso, true, comentarioExterior, comentarioInterior, ideStatusFluxo,
				ideArea);
	}
}
