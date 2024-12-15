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
import br.gov.ba.seia.entity.OrigemAguaTipoConcessionaria;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OrigemAguaTipoConcessionariaService {

	@Inject
	private IDAO<OrigemAguaTipoConcessionaria> idao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarOrigemAguaTipoConcessionaria(OrigemAguaTipoConcessionaria origemAguaTipoConcessionaria ) {
		
		idao.salvarOuAtualizar(origemAguaTipoConcessionaria);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluirCaracterizacaoOrigemAguaTipoConcessionaria(CaracterizacaoAmbientalOrigemAgua caracterizacaoAmbientalOrigemAgua) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ideCaracterizacaoAmbientalOrigemAgua", caracterizacaoAmbientalOrigemAgua);
		idao.executarNamedQuery("OrigemAguaTipoConcessionaria.removerOrigemAguaTipoConcessionaria", params);
	}
}
