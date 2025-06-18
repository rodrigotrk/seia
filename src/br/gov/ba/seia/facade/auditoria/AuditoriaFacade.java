package br.gov.ba.seia.facade.auditoria;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.entity.auditoria.HistCampo;
import br.gov.ba.seia.entity.auditoria.HistTabela;
import br.gov.ba.seia.entity.auditoria.HistValor;
import br.gov.ba.seia.service.AuditoriaService;
/**
 * Fachada para auditoria do imovel rural
 * @author 
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AuditoriaFacade {
	
	@Inject
	private AuditoriaService auditoriaService; 
	
	/**
	 * Salva objeto de auditoria
	 * @param obj
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(Object obj)  {
		auditoriaService.salvar(obj);		
	}
	/**
	 * Atualiza objeto de auditoria
	 * @param objAntigo
	 * @param objNovo
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarPCT(Object objAntigo, Object objNovo) {
		auditoriaService.atualizarPCT(objAntigo, objNovo);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizar(Object objAntigo, Object objNovo) {
		auditoriaService.atualizar(objAntigo, objNovo);
	}
	/**Exclui objeto de auditoria
	 * 
	 * @param obj
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void excluir(Object obj) throws Exception {
		auditoriaService.excluir(obj);
	}
	/**
	 * Lista historico de valor da auditoria
	 * @param tabelaFiltro
	 * @param campoTabelaFiltro
	 * @return lista do historico do valor
	 * @throws Exception
	 */
	public List<HistValor> obterHistorico(HistTabela tabelaFiltro, HistCampo campoTabelaFiltro) throws Exception {
		return auditoriaService.obterHistorico(tabelaFiltro, campoTabelaFiltro);
	}
	/**
	 * Lista historico de tabelas de auditoria
	 * @return tabelas
	 */
	public List<HistTabela> obterTabelas()  {
		return auditoriaService.obterTabelas();
	}
	/**
	 * Lista historico dos campos da tabela de auditoria
	 * @param tabela
	 * @return campos da tabela
	 */
	public List<HistCampo> obterCamposTabela(HistTabela tabela){
		return auditoriaService.obterCamposTabela(tabela);
	}
	/**
	 * Salva shapes auditados
	 * @param obj
	 * @param caminhoArquivo
	 * @param nomeCampo
	 * @throws Exception
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarShape(Object obj, String caminhoArquivo, String nomeCampo) throws Exception {
		auditoriaService.salvarShape(obj, caminhoArquivo, nomeCampo);		
	}
	
}
