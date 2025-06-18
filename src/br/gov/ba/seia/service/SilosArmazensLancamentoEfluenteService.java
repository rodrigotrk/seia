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
import br.gov.ba.seia.entity.SilosArmazensLancamentoEfluente;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SilosArmazensLancamentoEfluenteService {

	@Inject
	private IDAO<SilosArmazensLancamentoEfluente> idao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarSilosArmazensLancamentoEfluente(SilosArmazensLancamentoEfluente silosArmazensLancamentoEfluente) throws Exception{
		idao.salvarOuAtualizar(silosArmazensLancamentoEfluente);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirSilosArmazensLancamentoEfluente(CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen) throws Exception{
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideCaracterizacaoAmbientalSilosArmazen", caracterizacaoAmbientalSilosArmazen);
		idao.executarNamedQuery("SilosArmazensLancamentoEfluente.removerLancamentoEfluente", params);
	}
}
