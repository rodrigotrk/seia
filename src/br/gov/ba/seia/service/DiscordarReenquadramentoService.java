package br.gov.ba.seia.service;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;

import br.gov.ba.seia.dao.TramitacaoReenquadramentoProcessoDAOImpl;
import br.gov.ba.seia.dto.ProcessoReenquadramentoDTO;
import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.TramitacaoReenquadramentoProcesso;
import br.gov.ba.seia.enumerator.AreaEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.StatusReenquadramentoEnum;
import br.gov.ba.seia.facade.ReaberturaProcessoFacade;
import br.gov.ba.seia.service.facade.ComunicacaoReenquadramentoProcessoServiceFacade;
import br.gov.ba.seia.service.facade.ComunicacaoServiceFacade;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DiscordarReenquadramentoService{
	
	@EJB
	private ControleTramitacaoService controleTramitacaoService;
	@EJB
	private PautaService pautaService;
	@EJB
	private ReaberturaProcessoFacade reaberturaProcessoFacade;
	
	@EJB
	private VwConsultaProcessoService vwConsultaProcessoService;
	
	@EJB
	private TramitacaoReenquadramentoProcessoDAOImpl tramitacaoReenquadramentoProcessoDAOImpl;
	
	@EJB
	private ArquivoProcessoService arquivoProcessoService;
	
	@EJB
	private ComunicacaoReenquadramentoProcessoServiceFacade comunicacaoReenquadramentoProcessoServiceFacade;
	
	@EJB
	private ComunicacaoServiceFacade comunicacaoFacade;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarDiscordarReenquadramentoProcesso(ProcessoReenquadramentoDTO prDTO) {
		try {

			reaberturaProcessoFacade
					.atualizarTramitacaoAnterior(prDTO.getProcessoReenquadramento().getIdeProcesso().getId());
			reaberturaProcessoFacade.salvarControleTramitacaoPautaDiretorArea(
					prDTO.getProcessoReenquadramento().getIdeProcesso().getId(), true, null, null,
					StatusFluxoEnum.ENCAMINHADO_PARA_O_GESTOR.getStatus(), AreaEnum.DIRRE.getId());

		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarTramitacaoDiscordarReenquadramento(
			TramitacaoReenquadramentoProcesso tramitacaoReenquadramentoProcesso)  {
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
	public void discordarReenquadramento(ProcessoReenquadramentoDTO processoReenquadramentoDTO,ArquivoProcesso arquivoProcesso){
		try {
			TramitacaoReenquadramentoProcesso tramitacaoReenquadramentoProcessoA = new TramitacaoReenquadramentoProcesso(
					processoReenquadramentoDTO.getProcessoReenquadramento(),
					StatusReenquadramentoEnum.NAO_REENQUADRADO);
			TramitacaoReenquadramentoProcesso tramitacaoReenquadramentoProcessoB = new TramitacaoReenquadramentoProcesso(
					processoReenquadramentoDTO.getProcessoReenquadramento(),
					StatusReenquadramentoEnum.ENCAMINHADO_PARA_O_GESTOR);
			salvarTramitacaoDiscordarReenquadramento(tramitacaoReenquadramentoProcessoA);
			salvarTramitacaoDiscordarReenquadramento(tramitacaoReenquadramentoProcessoB);
			salvarDiscordarReenquadramentoProcesso(processoReenquadramentoDTO);
			arquivoProcessoService.salvar(arquivoProcesso);
			enviarEmailDiscordarReenquadramento(processoReenquadramentoDTO.getRequerente().getDesEmail(), "SEIA",processoReenquadramentoDTO);
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			

		}
		
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void enviarEmailDiscordarReenquadramento(String pDestinatario,String pAssunto,ProcessoReenquadramentoDTO processoReenquadramentoDTO)  {
		Map<String, String> mapEmail = new HashMap<String, String>(); 
		StringBuilder emailDiscordar = new StringBuilder();
		emailDiscordar
				.append("Prezado(a),\n\n")
				.append("recebemos sua justificativa de não reenquadrar o processo ")
				.append(processoReenquadramentoDTO.getProcessoReenquadramento().getIdeProcesso().getNumProcesso())
				.append(",  a qual foi encaminhada para análise técnica.")
				.append("\n\n")
				.append("Aguarde novas orientações.")
				.append("\n\n\n\n\n")
				.append("Atenciosamente, \n Central de Atendimento/INEMA ");

		
		mapEmail.put("assunto", "SEIA - Comunicado - Processo não reenquadrado");
		mapEmail.put("mensagem", emailDiscordar.toString());
		this.comunicacaoFacade.gerarComunicacao(processoReenquadramentoDTO.getProcessoReenquadramento(), mapEmail);
		
		
	}
	
	
	
	

}
