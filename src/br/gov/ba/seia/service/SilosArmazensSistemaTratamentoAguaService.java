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
import br.gov.ba.seia.entity.CaracterizacaoAmbientalSilosArmazen;
import br.gov.ba.seia.entity.SilosArmazensSistemaTratamentoAgua;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SilosArmazensSistemaTratamentoAguaService {

	@Inject
	private IDAO<SilosArmazensSistemaTratamentoAgua> idao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarSilosArmazensSistemaTratamentoAgua(SilosArmazensSistemaTratamentoAgua silosArmazensSistemaTratamentoAgua) throws Exception{
		idao.salvarOuAtualizar(silosArmazensSistemaTratamentoAgua);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirSilosArmazensTratamentoAgua(CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen) throws Exception{
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideCaracterizacaoAmbientalSilosArmazen", caracterizacaoAmbientalSilosArmazen);
		idao.executarNamedQuery("SilosArmazensSistemaTratamentoAgua.removerTratamentoAgua", params);
	}
}
