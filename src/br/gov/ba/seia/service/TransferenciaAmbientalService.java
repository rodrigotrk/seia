/**
 * 
 */
package br.gov.ba.seia.service;

import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import br.gov.ba.seia.dao.TransferenciaAmbientalDAO;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.StatusProcessoAto;
import br.gov.ba.seia.entity.TransferenciaAmbiental;

/**
 * @author Alexandre Queiroz
 * 
 *
 */

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class TransferenciaAmbientalService {

	@Inject 
	private TransferenciaAmbientalDAO transferenciaAmbientalDAO;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(TransferenciaAmbiental transferenciaAmbiental) throws Exception{
	 transferenciaAmbientalDAO.salvar(transferenciaAmbiental);
	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(List<TransferenciaAmbiental> transferenciaAmbiental) throws Exception{
	 transferenciaAmbientalDAO.salvar(transferenciaAmbiental);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TransferenciaAmbiental getTransferenciaAmbientalByStatusProcessoCedente(int ideStatusProcessoAto, int ideProcesso) throws Exception{
	 return transferenciaAmbientalDAO.getTransferenciaAmbientalByStatusProcessoCedente(ideStatusProcessoAto, ideProcesso);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoAto> getProcessoAtoCedente(Processo ideProcesso) throws Exception{
	 return transferenciaAmbientalDAO.getProcessoAtoCedente(ideProcesso);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pessoa getPessoaCedente(int ideProcesso) throws Exception{
		 return transferenciaAmbientalDAO.getPessoaCedente(ideProcesso);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pessoa getPessoaReceptor(int ideProcesso) throws Exception{
		return transferenciaAmbientalDAO.getPessoaReceptor(ideProcesso);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer getIdeStatusProcessoAtoMaisRecente(int ideProcessoAto) throws Exception{
		return transferenciaAmbientalDAO.getIdeStatusProcessoAtoMaisRecente(ideProcessoAto);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoAto> getListProcessoAtoRecebidos(Processo processo) throws Exception{
		return transferenciaAmbientalDAO.getListProcessoAtoRecebidos(processo);	
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Processo getNumProcessoByIdeProcesso(Integer ideProcessoAto) throws Exception{
		return transferenciaAmbientalDAO.getNumProcessoByIdeProcesso(ideProcessoAto);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TransferenciaAmbiental> isProcessoAtoTransferenciaAmbiental(ProcessoAto pa) throws Exception{
		return transferenciaAmbientalDAO.isProcessoAtoTransferenciaAmbiental(pa);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public StatusProcessoAto carregarStatusProcessoAto(Integer ideStatusProcessoAto)  throws Exception{
		return transferenciaAmbientalDAO.carregarStatusProcessoAto(ideStatusProcessoAto);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public StatusProcessoAto carregarStatusProcessoAto(StatusProcessoAto statusProcessoAto)throws Exception {
		return transferenciaAmbientalDAO.carregarStatusProcessoAto(statusProcessoAto);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public StatusProcessoAto getIdeStatusProcessoAtoByStatus(Integer ideStatus) throws Exception{
		return transferenciaAmbientalDAO.getIdeStatusProcessoAtoByStatus(ideStatus);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isProcessoPossuiSolicitacaoAmbiental(Integer ideProcesso, Integer ideTipoSolicitacao) throws Exception{
		return transferenciaAmbientalDAO.isProcessoPossuiSolicitacaoAmbiental(ideProcesso, ideTipoSolicitacao);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Processo> getHistNumProcesso(ProcessoAto pa) throws Exception{
		return transferenciaAmbientalDAO.getHistNumProcesso(pa);
	}
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isTransferenciaComOrigem(Processo processo) throws Exception {
		return transferenciaAmbientalDAO.isTransferenciaComOrigem(processo);
	}

}

