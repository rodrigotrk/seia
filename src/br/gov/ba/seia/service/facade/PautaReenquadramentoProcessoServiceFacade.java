package br.gov.ba.seia.service.facade;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.dao.ProcessoReenquadramentoDAOImpl;
import br.gov.ba.seia.dao.StatusReenquadramentoDAOImpl;
import br.gov.ba.seia.dao.TramitacaoReenquadramentoProcessoDAOImpl;
import br.gov.ba.seia.dto.ProcessoReenquadramentoDTO;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.StatusReenquadramento;
import br.gov.ba.seia.entity.TramitacaoReenquadramentoProcesso;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.AreaEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.StatusReenquadramentoEnum;
import br.gov.ba.seia.facade.ReaberturaProcessoFacade;
import br.gov.ba.seia.service.PermissaoPerfilService;
import br.gov.ba.seia.service.RelatoriosRequerimentoService;
import br.gov.ba.seia.service.VwConsultaProcessoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PautaReenquadramentoProcessoServiceFacade {
	
	@EJB
	private VwConsultaProcessoService vwConsultaProcessoService;
	@EJB
	private RelatoriosRequerimentoService relatoriosRequerimentoService;
	@EJB
	private ProcessoReenquadramentoDAOImpl processoReenquadramentoDAOImpl;
	@EJB
	private StatusReenquadramentoDAOImpl statusReenquadramentoDAOImpl;
	@EJB
	private PermissaoPerfilService permissaoPerfilService;
	@EJB
	private ReaberturaProcessoFacade reaberturaProcessoFacade;
	
	@EJB
	private ComunicacaoServiceFacade comunicacaoFacade;
	
	@EJB
	private TramitacaoReenquadramentoProcessoDAOImpl tramitacaoReenquadramentoProcessoDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ProcessoReenquadramentoDTO> listarProcessoReenquadramentoDTO(Map<String, Object> params, Integer first, Integer pageSize) {
		return processoReenquadramentoDAOImpl.listarProcessoReenquadramentoDTO(params, first, pageSize);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Integer countProcessoReenquadramentoDTO(Map<String, Object> params) {
		return processoReenquadramentoDAOImpl.countProcessoReenquadramentoDTO(params);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<StatusReenquadramento> listarStatusReenquadramento() {
		return statusReenquadramentoDAOImpl.listarStatusReenquadramento();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public StreamedContent gerarResumoRequerimento(Integer ideRequerimento) throws Exception {
		return relatoriosRequerimentoService.gerarResumoRequerimento(ideRequerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public VwConsultaProcesso buscarVwConsultaProcesso(Processo processo) throws Exception {
		return vwConsultaProcessoService.buscarVwConsultaProcessoPorIdeProcesso(processo.getIdeProcesso(), false);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Integer> listarPessoasQuePodemSerConsultadasPeloUsuario() {
		try {
			return permissaoPerfilService.listarIdesPessoasAptas(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString(""));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		} 
	}
	
	public String getTextoNaoReenquadrar(ProcessoReenquadramentoDTO prc) {
		StringBuilder dsJustificativa = new StringBuilder();
		
		dsJustificativa.append("Prezado(a),\n\n")
		.append("Informamos que reenquadramento do processo n° "+ prc.getProcessoReenquadramento().getIdeProcesso().getNumProcessoTabela()+" foi cancelado.\n\n")
		.append("Atenciosamente,\n")
		.append("Central de Atendimento/INEMA");
		
		return dsJustificativa.toString();
	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void finalizarNaoReenquadramento(ProcessoReenquadramentoDTO processoReenquadramentoDTO,String dsJustificativa) {
		TramitacaoReenquadramentoProcesso tramitacaoReenquadramentoProcessoA = new TramitacaoReenquadramentoProcesso(
				processoReenquadramentoDTO.getProcessoReenquadramento(),
				StatusReenquadramentoEnum.NAO_REENQUADRADO);
		TramitacaoReenquadramentoProcesso tramitacaoReenquadramentoProcessoB = new TramitacaoReenquadramentoProcesso(
				processoReenquadramentoDTO.getProcessoReenquadramento(),
				StatusReenquadramentoEnum.ENCAMINHADO_PARA_O_GESTOR);
		try {
			salvarTramitacaoReenquadramento(tramitacaoReenquadramentoProcessoA);
			salvarTramitacaoReenquadramento(tramitacaoReenquadramentoProcessoB);
			salvarNaoReenquadramentoProcesso(processoReenquadramentoDTO);

			enviarEmailNaoReenquadrar("SEIA", dsJustificativa, processoReenquadramentoDTO);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Não foi possível realizar a solicitação");
		}
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarNaoReenquadramentoProcesso(ProcessoReenquadramentoDTO prDTO) {
		try {

			reaberturaProcessoFacade.atualizarTramitacaoAnterior(prDTO.getProcessoReenquadramento().getIdeProcesso().getId());
			reaberturaProcessoFacade.salvarControleTramitacao(prDTO.getProcessoReenquadramento().getIdeProcesso().getId(), false, null, null,
					StatusFluxoEnum.NAO_REENQUADRADO.getStatus(), AreaEnum.DIRRE.getId());
			reaberturaProcessoFacade.salvarControleTramitacaoPautaGestor(prDTO.getProcessoReenquadramento().getIdeProcesso().getId(), true, null, null,
					StatusFluxoEnum.ENCAMINHADO_PARA_O_GESTOR.getStatus(), AreaEnum.DIRRE.getId());

		} catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void enviarEmailNaoReenquadrar(String pAssunto,
			String email,
			ProcessoReenquadramentoDTO processoReenquadramentoDTO) throws Exception {
		Map<String, String> mapEmail = new HashMap<String, String>(); 


		
		mapEmail.put("assunto", "SEIA - Comunicado - Processo não reenquadrado");
		mapEmail.put("mensagem", email);
		this.comunicacaoFacade.gerarComunicacao(processoReenquadramentoDTO.getProcessoReenquadramento(), mapEmail);
		
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarTramitacaoReenquadramento(
			TramitacaoReenquadramentoProcesso tramitacaoReenquadramentoProcesso) throws Exception {
		tramitacaoReenquadramentoProcessoDAOImpl.salvar(tramitacaoReenquadramentoProcesso);
	}
	
}