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
import br.gov.ba.seia.entity.SilosArmazensResponsavelTecnico;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SilosArmazensResponsavelTecnicoService {

	@Inject
	private IDAO<SilosArmazensResponsavelTecnico> idao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarSilosArmazensResponsavelTecnico(SilosArmazensResponsavelTecnico armazensResponsavelTecnico) throws Exception{
		
		idao.salvarOuAtualizar(armazensResponsavelTecnico);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirResponsavelTecnico(SilosArmazen ideSilosArmazens) throws Exception{
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideSilosArmazen", ideSilosArmazens);
		idao.executarNamedQuery("SilosArmazensResponsavelTecnico.removerByIdeSilos", params);
	}
}
