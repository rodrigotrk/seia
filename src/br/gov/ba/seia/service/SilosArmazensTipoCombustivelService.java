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
import br.gov.ba.seia.entity.SilosArmazensTipoCombustivel;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SilosArmazensTipoCombustivelService {

	@Inject
	private IDAO<SilosArmazensTipoCombustivel> idao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarSilosArmazensTipoCombustivel(SilosArmazensTipoCombustivel silosArmazensTipoCombustivel) throws Exception{
		idao.salvarOuAtualizar(silosArmazensTipoCombustivel);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirTipoCombustivel(SilosArmazen ideSilosArmazens) throws Exception{
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideSilosArmazen", ideSilosArmazens);
		idao.executarNamedQuery("SilosArmazensTipoCombustivel.removerByIdeSilos", params);
	}
}
