package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.ComunicacaoProcesso;
import br.gov.ba.seia.entity.Processo;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ComunicacaoProcessoService {

	@Inject
	IDAO<ComunicacaoProcesso> comunicacaoProcessoDAO;
	
	public void salvar(ComunicacaoProcesso comunicacaoProcesso) {
		comunicacaoProcessoDAO.salvar(comunicacaoProcesso);
	}
	
	public List<ComunicacaoProcesso> listarTodosPorProcesso(Integer ideProcesso) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("ideProcesso", new Processo(ideProcesso));
		
		StringBuilder sql = new StringBuilder();
		sql.append("select c ");
		sql.append("from ComunicacaoProcesso c ");
		sql.append("where c.ideProcesso = :ideProcesso");
		return comunicacaoProcessoDAO.listarPorQuery(sql.toString(), params);
	}

}