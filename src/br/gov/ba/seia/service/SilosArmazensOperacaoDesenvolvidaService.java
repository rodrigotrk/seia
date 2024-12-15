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
import br.gov.ba.seia.entity.SilosArmazensOperacaoDesenvolvida;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SilosArmazensOperacaoDesenvolvidaService {

	@Inject
	private IDAO<SilosArmazensOperacaoDesenvolvida> idao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarSilosArmazensOperacaoDesenvolvida(SilosArmazensOperacaoDesenvolvida armazensOperacaoDesenvolvida) throws Exception{
		idao.salvarOuAtualizar(armazensOperacaoDesenvolvida);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirOperacaoDesenvolvida(SilosArmazen ideSilosArmazens) throws Exception{
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideSilosArmazen", ideSilosArmazens);
		idao.executarNamedQuery("SilosArmazensOperacaoDesenvolvida.removerByIdeSilos", params);
	}
}
