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
import br.gov.ba.seia.entity.ClassificacaoResiduoCaracterizacaoAmbiental;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ClassificacaoResiduoCaracterizacaoAmbientalService {

	@Inject
	private IDAO<ClassificacaoResiduoCaracterizacaoAmbiental> idao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarClassificacaoResiduoCaracterizacaoAmbiental(ClassificacaoResiduoCaracterizacaoAmbiental classificacaoResiduoCaracterizacaoAmbiental) {
		idao.salvarOuAtualizar(classificacaoResiduoCaracterizacaoAmbiental);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirCaracterizacaoClassificacaoResiduo(CaracterizacaoAmbientalSilosArmazen caracterizacaoAmbientalSilosArmazen) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideCaracterizacaoAmbientalSilosArmazen", caracterizacaoAmbientalSilosArmazen);
		idao.executarNamedQuery("ClassificacaoResiduoCaracterizacaoAmbiental.removerClassificacaoResiduo", params);
	}
}
