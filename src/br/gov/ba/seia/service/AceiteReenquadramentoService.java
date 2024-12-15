package br.gov.ba.seia.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.ProcessoReenquadramentoDAOImpl;
import br.gov.ba.seia.dao.TramitacaoReenquadramentoProcessoDAOImpl;
import br.gov.ba.seia.dto.ProcessoReenquadramentoDTO;
import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.TramitacaoReenquadramentoProcesso;
import br.gov.ba.seia.enumerator.AreaEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.StatusReenquadramentoEnum;
import br.gov.ba.seia.facade.ReaberturaProcessoFacade;
import br.gov.ba.seia.service.facade.ComunicacaoServiceFacade;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AceiteReenquadramentoService{
	
	@EJB
	private ReaberturaProcessoFacade reaberturaProcessoFacade;
	
	@EJB
	private TramitacaoReenquadramentoProcessoDAOImpl tramitacaoReenquadramentoProcessoDAOImpl;
	
	@EJB
	private ArquivoProcessoService arquivoProcessoService;
	
	@EJB
	private ComunicacaoServiceFacade comunicacaoFacade;
	
	@EJB
	private ProcessoReenquadramentoDAOImpl processoReenquadramentoDAOImpl;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarDiscordarReenquadramento(ProcessoReenquadramentoDTO pr) {
		
		try {
			String observacao ="Verificar justificativa da não concordância de reenquadramento na aba \"Docs Apensados\"";
			processoReenquadramentoDAOImpl.atualizar(pr.getProcessoReenquadramento());
			reaberturaProcessoFacade.atualizarTramitacaoAnterior(pr.getProcessoReenquadramento().getIdeProcesso().getId());
			reaberturaProcessoFacade.salvarControleTramitacao(pr.getProcessoReenquadramento().getIdeProcesso().getId(), false, observacao, observacao, StatusFluxoEnum.NAO_REENQUADRADO.getStatus(), AreaEnum.DIRRE.getId());
			reaberturaProcessoFacade.salvarControleTramitacaoPautaDiretorArea(pr.getProcessoReenquadramento().getIdeProcesso().getId(), true, null, null, StatusFluxoEnum.ENCAMINHADO_PARA_O_GESTOR.getStatus(), AreaEnum.DIRRE.getId());
		}
		catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarTramitacaoDiscordarReenquadramento(TramitacaoReenquadramentoProcesso tramitacaoReenquadramentoProcesso) {
		tramitacaoReenquadramentoProcessoDAOImpl.salvar(tramitacaoReenquadramentoProcesso);
	}
	
	/**
	 * <p>Discordar do Reenquadramento de Processo
	 * Tramitação do Reenquadramento NAO_REENQUADRADO > ENCAMINHADO_PARA_O_GESTOR
	 * Tramitação do Processo  ENCAMINHADO_PARA_O_GESTOR
	 * </p>
	 * 
	 * @param ProcessoReenquadramentoDTO
	 * @author Antoniony
	 * @since 09/08/2019
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void discordarReenquadramento(ProcessoReenquadramentoDTO pr,ArquivoProcesso arquivoProcesso){
		try {
			
			TramitacaoReenquadramentoProcesso t1 = new TramitacaoReenquadramentoProcesso(pr.getProcessoReenquadramento(), StatusReenquadramentoEnum.NAO_REENQUADRADO);
			salvarTramitacaoDiscordarReenquadramento(t1);
			
			TramitacaoReenquadramentoProcesso t2 = new TramitacaoReenquadramentoProcesso(pr.getProcessoReenquadramento(), StatusReenquadramentoEnum.ENCAMINHADO_PARA_O_GESTOR);
			salvarTramitacaoDiscordarReenquadramento(t2);
			
			pr.getProcessoReenquadramento().setIndAceiteRequerente(false);
			pr.getProcessoReenquadramento().setDtcAceiteRequerente(new Date());
			salvarDiscordarReenquadramento(pr);
			
			arquivoProcessoService.salvar(arquivoProcesso);
			
			enviarEmailDiscordarReenquadramento(pr.getRequerente().getDesEmail(), "SEIA",pr);
		}
		catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void concordarReenquadramento(ProcessoReenquadramentoDTO pr)	{
		try {
			pr.getProcessoReenquadramento().setIndAceiteRequerente(true);
			pr.getProcessoReenquadramento().setDtcAceiteRequerente(new Date());
			processoReenquadramentoDAOImpl.atualizar(pr.getProcessoReenquadramento());
		}
		catch (Exception e) {
			Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void enviarEmailDiscordarReenquadramento(String pDestinatario,String pAssunto,ProcessoReenquadramentoDTO processoReenquadramentoDTO) {
		Map<String, String> mapEmail = new HashMap<String, String>(); 
		StringBuilder emailDiscordar = new StringBuilder();
		emailDiscordar
				.append("Prezado(a),\n\n")
				.append("Recebemos sua discordância quanto ao reenquadramento do processo ")
				.append(processoReenquadramentoDTO.getProcessoReenquadramento().getIdeProcesso().getNumProcesso())
				.append(", a qual foi encaminhada para análise técnica.")
				.append("\n\n")
				.append("Aguarde novas orientações.")
				.append("\n\n")
				.append("Atenciosamente, \n Central de Atendimento/INEMA ");
		mapEmail.put("assunto", "SEIA - Comunicado - Processo não reenquadrado");
		mapEmail.put("mensagem", emailDiscordar.toString());
		this.comunicacaoFacade.gerarComunicacao(processoReenquadramentoDTO.getProcessoReenquadramento(), mapEmail);
		
	}

}
