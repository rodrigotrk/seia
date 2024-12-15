package br.gov.ba.seia.service.facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.EnderecoDAOImpl;
import br.gov.ba.seia.dao.MotivoEdicaoNotificacaoDAOImpl;
import br.gov.ba.seia.dao.NotificacaoDAOImpl;
import br.gov.ba.seia.dao.NotificacaoMotivoNotificacaoDAOImpl;
import br.gov.ba.seia.dao.PautaDAOImpl;
import br.gov.ba.seia.dto.NotificacaoAprovacaoDTO;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.StatusFluxo;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.LegislacaoEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.ControleTramitacaoService;
import br.gov.ba.seia.service.JustificativaRejeicaoService;
import br.gov.ba.seia.service.LegislacaoService;
import br.gov.ba.seia.service.NotificacaoServiceFacade;
import br.gov.ba.seia.service.ReenquadramentoProcessoService;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NotificacaoAprovacaoServiceFacade {
	
	@EJB
	private MotivoEdicaoNotificacaoDAOImpl motivoEdicaNotificacaoDAOImpl;
	@EJB
	private NotificacaoMotivoNotificacaoDAOImpl notificacaoMotivoNotificacaoDAOImpl;
	@EJB
	private NotificacaoServiceFacade notificacaoServiceFacade;
	@EJB
	private NotificacaoDAOImpl notificacaoDAOImpl;
	@EJB
	private ControleTramitacaoService controleTramitacaoService;
	@EJB
	private ReenquadramentoProcessoService reenquadramentoProcessoService;
	@EJB
	private PautaDAOImpl pautaDAOImpl;
	@EJB
	private AreaService areaService;
	@EJB
	private LegislacaoService legislacaoService;
	@EJB
	private EnderecoDAOImpl enderecoDAOImpl;
	@EJB
	private JustificativaRejeicaoService justificativaService;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public NotificacaoAprovacaoDTO carregarNotificacaoAprovacao(VwConsultaProcesso vwProcesso) throws Exception {
		NotificacaoAprovacaoDTO notificacaoAprovacaoDTO = new NotificacaoAprovacaoDTO();
		notificacaoAprovacaoDTO.setVwProcesso(vwProcesso);
		notificacaoAprovacaoDTO.setLegislacao(legislacaoService.carregar(LegislacaoEnum.ART180.getId()));
		notificacaoAprovacaoDTO.setNotificacao(carregarNotificacao(vwProcesso));
		notificacaoAprovacaoDTO.setListaJustificativas(justificativaService.listarTodos());
		notificacaoAprovacaoDTO.setEnderecoEmpreendimento(enderecoDAOImpl.carregarEnderecoPor(vwProcesso.getIdeEmpreendimento()));
		notificacaoAprovacaoDTO.setlMotivoEdicaoNotificacao(motivoEdicaNotificacaoDAOImpl.listarMotivosEdicaoNotificacao());
		reenquadramentoProcessoService.carregarReenquadramentoProcesso(notificacaoAprovacaoDTO);
		return notificacaoAprovacaoDTO;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Notificacao carregarNotificacao(VwConsultaProcesso vwProcesso) throws Exception {
		Notificacao notificacao = notificacaoDAOImpl.obterNotificacaoParaAProvacao(new Processo(vwProcesso.getIdeProcesso()), vwProcesso.getStatusFluxo());
		notificacao.setNotificacaoMotivoNotificacaoCollection(notificacaoMotivoNotificacaoDAOImpl.listarNotificacaoMotivoNotificacaoPor(notificacao));
		notificacao.setMotivoEdicaoNotificacaoCollection(motivoEdicaNotificacaoDAOImpl.listarMotivosEdicaoNotificacaoPorNotificacao(notificacao));
		return notificacao;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ControleTramitacao buscarUltimaTramitacao(VwConsultaProcesso vwProcesso) throws Exception {
		return controleTramitacaoService.buscarUltimoPorProcesso(new Processo(vwProcesso.getIdeProcesso()));
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pauta obterPautaPorIdeFuncionario(Integer ideFuncionarioLogado)  {
		return pautaDAOImpl.obtemPautaPorIdeFuncionario(ideFuncionarioLogado);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Area obterAreaFuncionarioCoordenadorPorIdeFuncionario(Integer ideFuncionarioLogado)  {
		return areaService.obterAreaFuncionarioCoordenadorPorIdeFuncionario(ideFuncionarioLogado);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Area carregarAreaSecundaria(VwConsultaProcesso vwProcesso)  {
		return areaService.buscaAreaProcessoIndAreaSecundariaTrue(vwProcesso.getIdeProcesso());
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarCorrecaoNotificacao(NotificacaoAprovacaoDTO dto) throws Exception {
		notificacaoServiceFacade.salvarNotificacaoFinal(dto.getNotificacao());
		controleTramitacaoService.salvarNovaTramitacao(dto.getNotificacao().getIdeProcesso(), dto.getPautaGestor(), dto.getNotificacao().retornarMotivosEdicaoNotificacao(), false, new StatusFluxo(StatusFluxoEnum.NOTIFICACAO_ALTERADA.getStatus()));
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void aprovarNotificacao(Notificacao notificacao, Area areaGestor, Pauta pautaGestor, boolean isProcessoComAcompanhamento, String email) throws Exception {
		notificacaoServiceFacade.aprovarNotificacao(notificacao, areaGestor, pautaGestor, isProcessoComAcompanhamento, email);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void reprovarOuCancelarNotificacao(Notificacao notificacao, Area areaGestor, Pauta pautaGestor, StatusFluxoEnum statusFluxoEnum, boolean isProcessoComAcompanhamento) throws Exception {
		notificacaoServiceFacade.reprovarOuCancelarNotificacao(notificacao, areaGestor, pautaGestor, statusFluxoEnum, isProcessoComAcompanhamento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Pauta carregarPautaPorIdeProcessoFimFila(VwConsultaProcesso vwProcesso) {
		return pautaDAOImpl.carregarPautaPorIdeProcessoFimFila(vwProcesso.getIdeProcesso());
	}
}