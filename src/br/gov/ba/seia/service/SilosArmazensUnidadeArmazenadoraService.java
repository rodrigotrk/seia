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
import br.gov.ba.seia.entity.SilosArmazen;
import br.gov.ba.seia.entity.SilosArmazensUnidadeArmazenadora;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SilosArmazensUnidadeArmazenadoraService {

	@Inject
	private IDAO<SilosArmazensUnidadeArmazenadora> idao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarSilosArmazensUnidadeArmazenadora(SilosArmazensUnidadeArmazenadora unidadeArmazenadora) throws Exception{
		idao.salvarOuAtualizar(unidadeArmazenadora);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirUnidadeArmazenadora(SilosArmazen ideSilosArmazens) throws Exception{
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideSilosArmazen", ideSilosArmazens);
		idao.executarNamedQuery("SilosArmazensUnidadeArmazenadora.removerByIdeSilos", params);
	}
}
