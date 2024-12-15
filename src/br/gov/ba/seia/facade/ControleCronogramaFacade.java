package br.gov.ba.seia.facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.entity.Cronograma;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.service.CronogramaService;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ControleCronogramaFacade {

	@EJB
	private CronogramaService cronogramaService;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Cronograma obterCronogramaPorProcesso(Processo processo) throws Exception {
		Cronograma cronograma = cronogramaService.obterCronogramaPorProcesso(processo);
		
		if(Util.isNull(cronograma)) {
			cronograma = new Cronograma();
			cronograma.setIdeProcesso(processo);
		}
		
		return cronograma;
	}
	
}