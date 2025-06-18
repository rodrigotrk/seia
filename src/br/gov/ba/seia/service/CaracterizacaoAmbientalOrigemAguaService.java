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
import br.gov.ba.seia.entity.CaracterizacaoAmbientalOrigemAgua;
import br.gov.ba.seia.entity.CaracterizacaoAmbientalSilosArmazen;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class CaracterizacaoAmbientalOrigemAguaService {

	@Inject
	private IDAO<CaracterizacaoAmbientalOrigemAgua> idao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCaracterizacaoAmbientalOrigemAgua(CaracterizacaoAmbientalOrigemAgua origemAgua) {
		
		idao.salvarOuAtualizar(origemAgua);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirCaracterizacaoOrigemAgua(CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideCaracterizacaoAmbientalSilosArmazen", caracterizacaoAmbientalSilosArmazen);
		idao.executarNamedQuery("CaracterizacaoAmbientalOrigemAgua.removerOrigemAgua", params);
	}
}
