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
import br.gov.ba.seia.entity.FuncionalidadeAcaoPessoaFisicaPauta;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FuncionalidadeAcaoPessoaFisicaPautaService {

	@Inject
	IDAO<FuncionalidadeAcaoPessoaFisicaPauta> funcionalidadeAcaoPessoaFisicaPautaDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(FuncionalidadeAcaoPessoaFisicaPauta funcionalidadeAcaoPessoaFisicaPauta) {
		funcionalidadeAcaoPessoaFisicaPautaDAO.salvar(funcionalidadeAcaoPessoaFisicaPauta);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remover(FuncionalidadeAcaoPessoaFisicaPauta funcionalidadeAcaoPessoaFisicaPauta) {
		Map<String, Object> params = new HashMap<String,Object>();		
		params.put("ideFuncionalidadeAcaoPessoaFisicaPauta", funcionalidadeAcaoPessoaFisicaPauta);
		
		StringBuilder sql = new StringBuilder();
		sql.append("delete from FuncionalidadeAcaoPessoaFisicaPauta fapfPauta ");
		sql.append("where fapfPauta = :ideFuncionalidadeAcaoPessoaFisicaPauta ");
		
		funcionalidadeAcaoPessoaFisicaPautaDAO.executarQuery(sql.toString(), params);
	}
	

}