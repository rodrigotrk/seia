package br.gov.ba.seia.service.facade;

import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.CategoriaDocumentoDAOImpl;
import br.gov.ba.seia.dao.MotivoNotificacaoImovelDAOImpl;
import br.gov.ba.seia.dto.NotificacaoFinalDTO;
import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.CategoriaDocumento;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.MotivoNotificacaoImovel;
import br.gov.ba.seia.entity.StatusFluxo;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.CategoriaDocumentoEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.TipoNotificacaoEnum;
import br.gov.ba.seia.service.ArquivoProcessoService;
import br.gov.ba.seia.service.ControleTramitacaoService;
import br.gov.ba.seia.service.NotificacaoService;
import br.gov.ba.seia.service.ReenquadramentoProcessoService;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NotificacaoFinalServiceFacade {
	
	@EJB
	private NotificacaoService notificacaoService;
	@EJB
	private ControleTramitacaoService controleTramitacaoService;
	@EJB
	private ReenquadramentoProcessoService reenquadramentoProcessoService;
	@EJB
	private ArquivoProcessoService arquivoProcessoService;
	@EJB
	private CategoriaDocumentoDAOImpl categoriaDocumentoDAOImpl;
	@EJB
	private MotivoNotificacaoImovelDAOImpl motivoNotificacaoImovelDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarNotificacaoFinal(NotificacaoFinalDTO dto) {
		notificacaoService.salvarNotificacaoFinal(dto);
		arquivoProcessoService.salvarDocumentosNotificacao(dto);
		reenquadramentoProcessoService.salvarReenquadramento(dto);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void enviarNotificacaoFinal(NotificacaoFinalDTO dto) throws Exception {
		salvarNotificacaoFinal(dto);
		notificacaoService.prepararEnvioNotificacao(dto);
		controleTramitacaoService.salvarNovaTramitacao(
			dto.getNotificacaoFinal().getIdeProcesso(),
			dto.getNotificacaoFinal().getIdePautaCriacao(), 
			dto.getObservacao(),
			false, 
			new StatusFluxo(StatusFluxoEnum.NOTIFICACAO_CRIADA.getStatus())
		);
		notificacaoService.enviarNotificacaoFinal(dto);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Imovel> listarImovel() {
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<MotivoNotificacaoImovel> listarMotivoNotificacaoImovel(Integer ideNotificacaoMotivoNotificacao) {
		return motivoNotificacaoImovelDAOImpl.listarMotivoNotificacaoImovelPor(ideNotificacaoMotivoNotificacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public CategoriaDocumento carregarCategoriaDocumentoNotificacao() {
		return categoriaDocumentoDAOImpl.carregarCategoriaDocumento(CategoriaDocumentoEnum.DOCUMENTO_DA_NOTIFICACAO.getId());
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void deletarArquivo(ArquivoProcesso arquivoProcesso) {
		arquivoProcessoService.deletarArquivo(arquivoProcesso);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public NotificacaoFinalDTO carregarNotificacaoFinal(VwConsultaProcesso vwp, TipoNotificacaoEnum tipoNotificacaoEnum, boolean acessoFeitoPorPermissaoConcedida) throws Exception {
		return notificacaoService.carregarNotificacaoFinal(vwp,tipoNotificacaoEnum,acessoFeitoPorPermissaoConcedida);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void carregarDadosReequadramentoProcesso(NotificacaoFinalDTO dto) throws Exception {
		notificacaoService.carregarDadosReenquadramentoProcesso(dto);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void recarregar(NotificacaoFinalDTO dto) throws Exception {
		notificacaoService.carregarNotificacaoFinal(dto.getVwProcesso(), dto.getTipoNotificacaoEnum(),dto.isAcessoFeitoPorPermissaoConcedida());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String montarTextoNotificacao(NotificacaoFinalDTO dto) {
		return reenquadramentoProcessoService.montarTextoNotificacao(dto);
	}
}