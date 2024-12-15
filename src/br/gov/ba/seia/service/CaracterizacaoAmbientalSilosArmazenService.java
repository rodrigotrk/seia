package br.gov.ba.seia.service;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.CaracterizacaoAmbientalSilosArmazen;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CaracterizacaoAmbientalSilosArmazenService {

	@Inject
	private IDAO<CaracterizacaoAmbientalSilosArmazen> idao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCaracterizacaoAmbientalSilosArmazen(CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen) {
		idao.salvarOuAtualizar(caracterizacaoAmbientalSilosArmazen);
	}
}
