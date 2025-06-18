package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.entity.ImovelRuralUsoAgua;
import br.gov.ba.seia.entity.ProcessoUsoAgua;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ProcessoUsoAguaService {
	@Inject
	IDAO<ProcessoUsoAgua> processoUsoAguaDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAtualizarProcessoUsoAgua(ProcessoUsoAgua pProcesso) {
		processoUsoAguaDAO.salvarOuAtualizar(pProcesso);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoUsoAgua> obterPorImovelRuralUsoAgua(ImovelRuralUsoAgua pImovelRuralUsoAgua)  {
		StringBuilder sql = new StringBuilder();

		sql.append("select P ");
		sql.append("from ProcessoUsoAgua P ");
		sql.append("where P.indExcluido =false and P.ideImovelRuralUsoAgua=" + pImovelRuralUsoAgua.getIdeImovelRuralUsoAgua());

		return processoUsoAguaDAO.listarPorQuery(sql.toString(), null);
	}
}
