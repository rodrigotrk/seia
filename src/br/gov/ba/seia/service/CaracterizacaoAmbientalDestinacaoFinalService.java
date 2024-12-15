package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.CaracterizacaoAmbientalDestinacaoFinal;
import br.gov.ba.seia.entity.CaracterizacaoAmbientalSilosArmazen;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CaracterizacaoAmbientalDestinacaoFinalService {

	@Inject
	private IDAO<CaracterizacaoAmbientalDestinacaoFinal> idao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCaracterizacaoAmbientalDestinacaoFinal(CaracterizacaoAmbientalDestinacaoFinal caracterizacaoAmbientalDestinacaoFinal){
		
		idao.salvarOuAtualizar(caracterizacaoAmbientalDestinacaoFinal);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirCaracterizacaoDestinacaoFinal(CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideCaracterizacaoAmbientalSilosArmazen", caracterizacaoAmbientalSilosArmazen);
		idao.executarNamedQuery("CaracterizacaoAmbientalDestinacaoFinal.removerDestinacaoFinal", params);
	}
}
