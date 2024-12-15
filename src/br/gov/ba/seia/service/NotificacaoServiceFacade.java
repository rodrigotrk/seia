package br.gov.ba.seia.service;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import org.apache.log4j.Level;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import br.gov.ba.seia.dao.ControleTramitacaoDAOImpl;
import br.gov.ba.seia.dao.EquipeDAOImpl;
import br.gov.ba.seia.dao.IDAO;
import br.gov.ba.seia.dao.NotificacaoDAOImpl;
import br.gov.ba.seia.dao.NotificacaoParcialDAOImpl;
import br.gov.ba.seia.dao.PautaDAOImpl;
import br.gov.ba.seia.dao.ProcessoReenquadramentoDAOImpl;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.ComunicacaoProcesso;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Equipe;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.JustificativaRejeicao;
import br.gov.ba.seia.entity.MotivoNotificacao;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.NotificacaoMotivoNotificacao;
import br.gov.ba.seia.entity.NotificacaoParcial;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoReenquadramento;
import br.gov.ba.seia.entity.Requerente;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.entity.StatusFluxo;
import br.gov.ba.seia.entity.TipoPauta;
import br.gov.ba.seia.entity.TramitacaoReenquadramentoProcesso;
import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.enumerator.AreaEnum;
import br.gov.ba.seia.enumerator.JustificativaRejeicaoEnum;
import br.gov.ba.seia.enumerator.MotivoNotificacaoEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.StatusReenquadramentoEnum;
import br.gov.ba.seia.enumerator.TipoNotificacaoEnum;
import br.gov.ba.seia.enumerator.TipoPautaEnum;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.EmailUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.PerfilUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NotificacaoServiceFacade {

	@Inject
	private IDAO<NotificacaoParcial> notificacaoParcialDAO;
	@Inject
	private IDAO<ControleTramitacao> controleTramitacaoDAO;
	@Inject
	private IDAO<Funcionario> funcionarioDAO;
	
	@EJB
	private AreaService areaService;
	@EJB
	private AlertaService alertaService;
	@EJB
	private UsuarioService usuarioService;
	@EJB
	private ControleTramitacaoService controleTramitacaoService;
	@EJB
	private ComunicacaoProcessoService comunicacaoProcessoService;	
	@EJB
	private PautaDAOImpl pautaDAOImpl;
	@EJB
	private EquipeDAOImpl equipeDAOImpl;
	@EJB
	private NotificacaoDAOImpl notificacaoDAOImpl;
	@EJB
	private NotificacaoParcialDAOImpl notificacaoParcialDAOImpl;
	@EJB
	private ControleTramitacaoDAOImpl controleTramitacaoDAOImpl;
	@EJB
	private ProcessoReenquadramentoDAOImpl processoReenquadramentoDAOImpl;
	@EJB
	private JustificativaRejeicaoService justificativaRejeicaoService;
	@EJB
	private NotificacaoService notificacaoService;
	@EJB
	private ArquivoProcessoService arquivoProcessoService;
	@EJB
	private MotivoNotificacaoService motivoNotificacaoService;
	@EJB
	private ReenquadramentoProcessoService reenquadramentoProcessoService;
	@EJB
	private RequerimentoPessoaService requerimentoPessoaService;
	@EJB
	private EmpreendimentoService empreendimentoService;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private String tratarJustificativa(Notificacao notificacao)  {
		
		StringBuilder justificativa = new StringBuilder("");
		
		for(JustificativaRejeicao jus : notificacao.getJustificativaRejeicaoCollection()) {
			
			jus = justificativaRejeicaoService.carregar(jus);
			
			if(justificativa.toString().equals("")) {
				justificativa.append( jus.getNomJustificativaRejeicao());
			}
			else {
				justificativa.append( ", " + jus.getNomJustificativaRejeicao());					
			}
			
			if(jus.getIdeJustificativaRejeicao()==JustificativaRejeicaoEnum.OUTROS.getCodigo()){
				justificativa.append(" - " + notificacao.getDscOutroJustificativa());
			}
		}
		
		return justificativa.toString();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<NotificacaoParcial> listarNotificacaoParcialPorNotificacaoFinal(Integer ideNotificacao)  {
		return notificacaoParcialDAOImpl.listarNotificacaoParcialPorNotificacaoFinal(ideNotificacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void reprovarOuCancelarNotificacao(Notificacao notificacao, Area areaGestor, Pauta pautaGestor, StatusFluxoEnum statusFluxoEnum, boolean isProcessoComAcompanhamento) throws Exception  {
		
		Processo processo = notificacao.getIdeProcesso();
		String justificativa = tratarJustificativa(notificacao);
		
		notificacaoDAOImpl.atualizar(notificacao);
		
		if(isCoordenador(pautaGestor)) {
			
			atualizarNotificacaoParcial(notificacao);
			controleTramitacaoService.salvarNovaTramitacao(
				processo.getIdeProcesso(),
				areaGestor.getIdeArea(),
				pautaGestor.getIdePauta(),
				statusFluxoEnum,
				getIdeUsuarioLogado(),
				justificativa
			);
			tramitarParaTecnicos(processo);

		}
		else if(isDiretor(pautaGestor)) {
			if(isProcessoComAcompanhamento){
				tramitarParaTecnicos(processo);
			} 
			else {
				
				Area areaCoordenador = areaService.retornarCoordenacaoDaAprovacaoNotificacao(processo);
				Pauta pautaCoordenador = pautaDAOImpl.obtemPautaCoordenadorArea(areaCoordenador.getIdeArea(), TipoPautaEnum.PAUTA_COORDENADOR_AREA);
				controleTramitacaoService.salvarNovaTramitacao(
					processo.getIdeProcesso(),
					areaCoordenador.getIdeArea(),
					pautaCoordenador.getIdePauta(),
					statusFluxoEnum,
					getIdeUsuarioLogado(),
					justificativa
				);
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void atualizarNotificacaoParcial(Notificacao notificacao)
			 {
		if (notificacao.getIdeNotificacao() != null 
			&& !notificacao.getIndCancelado()
			&& !notificacao.getIndAprovado()
			&& notificacao.getIndRejeitado()) {
			
			List<NotificacaoParcial> nParciais = notificacaoParcialDAOImpl.listarNotificacaoParcialPorNotificacaoFinal(notificacao.getIdeNotificacao());
			if (!Util.isNullOuVazio(nParciais)) {
				for (NotificacaoParcial nParcial : nParciais) {
					if (notificacao.getTipo() == nParcial.getTipo()) {
						nParcial.setIndEmissao(false);
						notificacaoParcialDAO.salvar(nParcial);
					}
				}
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void tramitarParaTecnicos(Processo processo) throws Exception  {
		Collection<ControleTramitacao> listaControleTramitacaoTecnicos = controleTramitacaoDAOImpl.listarControleTramitacaoTecnicos(processo.getIdeProcesso());
		controleTramitacaoDAOImpl.resetarTramitacaoPorProcesso(processo.getIdeProcesso());
		
		Date dtcTramitacao = new Date();
		
		for(ControleTramitacao tramitacao : listaControleTramitacaoTecnicos) {
			ControleTramitacao novaTramitacao = tramitacao.clone();
			novaTramitacao.setIdeControleTramitacao(null);
			novaTramitacao.setIdePessoaFisica(new PessoaFisica(getIdeUsuarioLogado()));
			novaTramitacao.setDtcTramitacao(dtcTramitacao);
			novaTramitacao.setDscComentarioExterno(null);
			novaTramitacao.setDscComentarioInterno(null);
			novaTramitacao.setIndFimDaFila(true);
			controleTramitacaoDAOImpl.salvar(novaTramitacao);
		}
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Pauta carregarPautaDestino(Area areaGestor)  {
		Area areaDestino = areaService.buscarAreaPaiDoProcesso(areaGestor.getIdeArea());
		return carregarPautaByArea(areaDestino);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Pauta carregarPautaByArea(Area areaDestino)  {
		return pautaDAOImpl.buscarPautaGestorPor(areaDestino.getIdeArea());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Area carregarAreaDestino(Area areaGestor)  {
		return areaService.buscarAreaPaiDoProcesso(areaGestor.getIdeArea());
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void aprovarNotificacao(Notificacao notificacao, Area areaGestor, Pauta pautaGestor, boolean isProcessoComAcompanhamento, String email)  {
		
		if(notificacao.isNotificacaoPrazo()){
			fluxoNotificacaoPrazo(notificacao, areaGestor, pautaGestor, isProcessoComAcompanhamento, email, notificacao.getIdeProcesso());
		} 
		else if(notificacao.isNotificacaoComunicacao()) {
			
			fluxoAprovacaoNotificacaoComunicacao(notificacao, areaGestor, pautaGestor, isProcessoComAcompanhamento, email, notificacao.getIdeProcesso());
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void fluxoAprovacaoNotificacaoComunicacao(Notificacao notificacao, Area areaGestor, Pauta pautaGestor, boolean isProcessoComAcompanhamento, String email, Processo processo) {
		try {
			MotivoNotificacaoEnum motivoNotificacaoEnum = retornarMotivoNotificacaoEnum(notificacao);
			
			if(isCoordenador(pautaGestor)) {
				
				if(motivoNotificacaoEnum.equals(MotivoNotificacaoEnum.REENQUADRAMENTO_PROCESSO)) {
					if (isCoordenadorUR()) {
						enviarParaAprovacaoDaArea(processo, AreaEnum.COGED);
					}
					else {
						if (isCoordenadorNOUT() || isCoordenadorCOGED()) {
							enviarParaAprovacaoDaArea(processo, AreaEnum.DIRRE);
						}
						else {
							enviarParaAprovacaoDiretor(areaGestor, processo);
						}
					}
				}
				else{
					enviarParaAprovacaoDiretor(areaGestor, processo);
				}
				
			}
			else if(isDiretor(pautaGestor)) {
				if(isProcessoComAcompanhamento && isUsuarioLogadoResponsavelDaAreaSecundaria(processo.getIdeProcesso())) {
					devolverTramitacaoParaAreaPrincipal(processo);
				}  
				else {
					notificacao.setNumNotificacao(gerarNumNotificacaoComunicacao(processo));
					expedirNotificacao(notificacao, pautaGestor, areaGestor, processo);
					
					
					switch (motivoNotificacaoEnum) {
						case INDEFERIR :
							controleTramitacaoService.salvarNovaTramitacao(
								processo.getIdeProcesso(),
								areaGestor.getIdeArea(),
								pautaGestor.getIdePauta(),
								StatusFluxoEnum.CONCLUIDO,
								getIdeUsuarioLogado(),
								null
							);
							break;
						case ARQUIVAR :
							controleTramitacaoService.salvarNovaTramitacao(
								processo.getIdeProcesso(),
								areaGestor.getIdeArea(),
								pautaGestor.getIdePauta(),
								StatusFluxoEnum.ARQUIVADO,
								getIdeUsuarioLogado(),
								null
							);
							break;
						case CANCELAR :
							controleTramitacaoService.salvarNovaTramitacao(
								processo.getIdeProcesso(),
								areaGestor.getIdeArea(),
								pautaGestor.getIdePauta(),
								StatusFluxoEnum.CANCELADO,
								getIdeUsuarioLogado(),
								null
							);
							break;
						case REENQUADRAMENTO_PROCESSO :
							Pauta pautaDestino = pautaDAOImpl.obtemPautaArea(new Area(AreaEnum.ATEND.getId()));
							controleTramitacaoService.salvarNovaTramitacao(
									processo.getIdeProcesso(),
									AreaEnum.ATEND.getId(),
									pautaDestino.getIdePauta(),
									StatusFluxoEnum.AGUARDANDO_REENQUADRAMENTO_PROCESSO,
									getIdeUsuarioLogado(),
									null
								);
							gerarProcessoReenquadramento(processo);
							break;
						default :
							break;
					}
					enviarEmail(notificacao, email, processo);
				}
			}
		}
		catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void gerarProcessoReenquadramento(Processo processo) throws Exception  {
		ProcessoReenquadramento processoReenquadramento = new ProcessoReenquadramento(processo);
		processoReenquadramento.setTramitacaoReenquadramentoProcessoCollection(new ArrayList<TramitacaoReenquadramentoProcesso>());
		processoReenquadramento.getTramitacaoReenquadramentoProcessoCollection().add(new TramitacaoReenquadramentoProcesso(processoReenquadramento,StatusReenquadramentoEnum.AGUARDANDO_EDICAO_REENQUADRAMENTO));
		reenquadramentoProcessoService.gerarNumeroProcessoReenquadramento(processoReenquadramento);
		processoReenquadramentoDAOImpl.salvar(processoReenquadramento);
	}

	private MotivoNotificacaoEnum retornarMotivoNotificacaoEnum(Notificacao notificacao) {
		MotivoNotificacao motivo;
		try {
			motivo = recuperarMotivo(notificacao);
			return MotivoNotificacaoEnum.getEnum(motivo.getIdeMotivoNotificacao());
		}
		catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método para enviar e-mail ao {@link Requerente} comunicando sobre a {@link Notificacao}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 10/08/2016
	 * @param notificacao
	 * @param email
	 * @param processo
	 * @throws Exception 
	 * @
	 */
	private void enviarEmail(Notificacao notificacao, String email, Processo processo) throws Exception  {
		String msgNotificacaoComunicacao = montarMsgNotificacaoComunicacao(notificacao);
		salvarComunicacaoProcesso(processo,	msgNotificacaoComunicacao);
		new EmailUtil().enviarEmail(email, "SEIA - Comunicado - Notificação de comunicação nº "+notificacao.getNumNotificacao(), msgNotificacaoComunicacao);
	}

	/**
	 * ComplementaÃ§Ã£o do ticket <a href="http://10.105.12.26/redmine/issues/7690">#7690</a>
	 * 
	 * <p>
	 * 1 - Quando um tÃ©cnico da UR emitir a terceira notificaÃ§Ã£o (e as seguintes), apÃ³s a aprovaÃ§Ã£o do coordenador da UR, a mesma deverÃ¡ ser encaminhada para o coordenador da COGED.
	 * </p>
	 * <p>
	 * 2 - Quando um tÃ©cnico do NOUT emitir a terceira notificaÃ§Ã£o (e as seguintes), apÃ³s a aprovaÃ§Ã£o do coordenador, a mesma deverÃ¡ ser encaminhada para aprovaÃ§Ã£o do diretor da DIRRE.
	 * </p>
	 * 
	 * @author eduardo.fernandes 
	 * @since 10/08/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7886">#7886</a>
	 * @param processo
	 */
	private void enviarParaAprovacaoDaArea(Processo processo, AreaEnum areaEnum)  {
		Area area = new Area(areaEnum.getId());
		Pauta pauta = carregarPautaByArea(area);
		controleTramitacaoService.salvarNovaTramitacao(
			processo.getIdeProcesso(),
			area.getIdeArea(),
			pauta.getIdePauta(),
			StatusFluxoEnum.AGUARDANDO_APROVACAO_NOTIFICACAO,
			getIdeUsuarioLogado(),
			null
		);
	}

	private void enviarParaAprovacaoDiretor(Area areaGestor, Processo processo)  {
		Area areaDestino = carregarAreaDestino(areaGestor);
		Pauta pautaDestino = carregarPautaDestino(areaGestor);
		controleTramitacaoService.salvarNovaTramitacao(
			processo.getIdeProcesso(),
			areaDestino.getIdeArea(),
			pautaDestino.getIdePauta(),
			StatusFluxoEnum.AGUARDANDO_APROVACAO_NOTIFICACAO,
			getIdeUsuarioLogado(),
			null
		);
	}

	private boolean isDiretor(Pauta pautaGestor) {
		return new TipoPauta(TipoPautaEnum.PAUTA_DIRETOR_AREA.getTipo()).equals(pautaGestor.getIdeTipoPauta());
	}

	private boolean isCoordenador(Pauta pautaGestor) {
		return new TipoPauta(TipoPautaEnum.PAUTA_COORDENADOR_AREA.getTipo()).equals(pautaGestor.getIdeTipoPauta());
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void fluxoNotificacaoPrazo(Notificacao notificacao, Area areaGestor, Pauta pautaGestor, boolean isProcessoComAcompanhamento, String email, Processo processo) {
		try {
			if(isProcessoComAcompanhamento && isUsuarioLogadoResponsavelDaAreaSecundaria(processo.getIdeProcesso())){
				devolverTramitacaoParaAreaPrincipal(processo);
			} 
			else {
				
				Integer qtdNotificacoesEmitidas = notificacaoDAOImpl.bucarQtdNotificacoesEmitidas(processo.getIdeProcesso(), TipoNotificacaoEnum.NOTIFICACAO_PRAZO);
				
				if(isCoordenador(pautaGestor) && qtdNotificacoesEmitidas >= 2) {
					// #7886
					if (isProcessoDeUR(processo)) {
						if (isCoordenadorUR()) {
							enviarParaAprovacaoDaArea(processo, AreaEnum.COGED);
						}
						else if (isCoordenadorCOGED()) {
							notificacao.setNumNotificacao(gerarNumNotificacao(processo));
							expedirNotificacao(notificacao, null, areaGestor, processo);
							enviarEmail(notificacao, email, processo);
						}
						else if (isCoordenadorCOASP())  {
							enviarParaAprovacaoDaArea(processo, AreaEnum.DIRRE);
						}
					}
					else {
						if (isCoordenadorNOUT()) {
							enviarParaAprovacaoDaArea(processo, AreaEnum.DIRRE);
						}
						else {
							enviarParaAprovacaoDiretor(areaGestor, processo);
						}
					}
				}
				else {
					notificacao.setNumNotificacao(gerarNumNotificacao(processo));
					expedirNotificacao(notificacao, null, areaGestor, processo);
					enviarEmail(notificacao, email, processo);
				}				
			}
		}
		catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que verifica se o {@link Usuario} logado Ã© o coordenador da COGED.
	 * 
	 * @author eduardo.fernandes 
	 * @since 10/08/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7886">#7886</a>
	 * @return
	 */
	private boolean isCoordenadorCOGED() {
		Area area = buscarAreaByPessoaFisica(getIdeUsuarioLogado());
		return !Util.isNull(area) && area.getIdeArea().equals(AreaEnum.COGED.getId());
	}
	
	/**
	 * Método que verifica se o {@link Usuario} logado Ã© o coordenador da COASP.
	 * 
	 * @author dorgival.fernando
	 * @since 25/04/2024
	 * @see <a href="http://10.105.12.26/redmine/issues/36218">#36218</a>
	 * @return
	 */
	private boolean isCoordenadorCOASP() {
		Area area = buscarAreaByPessoaFisica(getIdeUsuarioLogado());
		return !Util.isNull(area) && area.getIdeArea().equals(AreaEnum.COASP.getId());
	}
	
	/**
	 * Método que verifica se a penÃºltima tramitaÃ§Ã£o do {@link Processo} foi feita por alguma Unidade Regional <b>(UR)</b>.
	 * 
	 * @author eduardo.fernandes 
	 * @since 10/08/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7886">#7886</a>
	 * @return
	 * @ 
	 */
	private boolean isProcessoDeUR(Processo processo)  {
		ControleTramitacao penultimoControleTramitacao = controleTramitacaoService.buscarPenultimoPorProcesso(processo);
		Area area = penultimoControleTramitacao.getIdeArea();
		return listarUnidadesRegionais().contains(area);
	}
	
	/**
	 * Método para realizar a expediÃ§Ã£o da {@link Notificacao} e enviar o {@link EmailUtil} para o requerente.
	 * 
	 * @author eduardo.fernandes 
	 * @since 10/08/2016
	 * @param notificacao
	 * @param areaDestino
	 * @param pautaDestino
	 * @param email
	 * @param processo
	 * @
	 */
	private void expedirNotificacao(Notificacao notificacao, Pauta pautaDestino, Area areaDestino,Processo processo)  {
		notificacaoDAOImpl.atualizar(notificacao);
		
		if(Util.isNull(pautaDestino)){
			pautaDestino = pautaDAOImpl.obtemPautaArea(areaDestino.getIdeArea());
		}
		
		controleTramitacaoService.salvarNovaTramitacao(
			processo.getIdeProcesso(),
			areaDestino.getIdeArea(),
			pautaDestino.getIdePauta(),
			StatusFluxoEnum.NOTIFICACAO_EXPEDIDA,
			getIdeUsuarioLogado(),
			null
		);
	}

	/**
	 * Método que retorna o usuÃ¡rio logado na sessÃ£o. 
	 * 
	 * @author eduardo.fernandes 
	 * @since 10/08/2016
	 * @return
	 */
	private Integer getIdeUsuarioLogado() {
		return ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica();
	}

	/**
	 * 
	 * Método que verifica se o {@link Usuario} logado Ã© o coordenador da NOUT.
	 * 
	 * @author eduardo.fernandes 
	 * @since 10/08/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7886">#7886</a>
	 * @return
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean isCoordenadorNOUT() {
		Area area = buscarAreaByPessoaFisica(getIdeUsuarioLogado());
		return !Util.isNull(area) && area.getIdeArea().equals(AreaEnum.NOUT.getId());
	}

	/**
	 * 
	 * Método que verifica se o {@link Usuario} logado Ã© o coordenador de alguma Unidade Regional.
	 * 
	 * @author eduardo.fernandes 
	 * @since 10/08/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7886">#7886</a>
	 * @return
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean isCoordenadorUR() {
		Area area = buscarAreaByPessoaFisica(getIdeUsuarioLogado());
		List<Area> listaURs = listarUnidadesRegionais();
		return !Util.isNull(area) && listaURs.contains(area);
	}

	/**
	 * Método que lista as {@link Area} do tipo Unidade Regional <b>(UR)</b>.
	 * 
	 * @author eduardo.fernandes 
	 * @since 10/08/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7786">#7786</a>
	 * @return
	 * @
	 */
	private List<Area> listarUnidadesRegionais() {
		try {
			return areaService.listarUnidadesRegionais();
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Unidades Regionais.");
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}

	/**
	 * 
	 * Método que busca {@link Area} do {@link Usuario}.
	 * 
	 * @author eduardo.fernandes 
	 * @since 10/08/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7886">#7886</a>
	 * @param ideUsuarioLogado
	 * @return
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Area buscarAreaByPessoaFisica(Integer ideUsuarioLogado) {
		try {
			return areaService.buscarAreaPorPessoaFisica(ideUsuarioLogado);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a Área do funcionÃ¡rio logado.");
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarComunicacaoProcesso(Processo processo, String msgNotificacaoPrazo)  {
		ComunicacaoProcesso comunicacaoComRequerente = new ComunicacaoProcesso(new Date(),msgNotificacaoPrazo,processo);
		comunicacaoProcessoService.salvar(comunicacaoComRequerente);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void devolverTramitacaoParaAreaPrincipal(Processo processo) throws Exception  {
		Equipe equipe = equipeDAOImpl.buscarEquipeAtual(processo.getIdeProcesso());
		Area areaPrincipal = equipe.getIdeArea(); 
		Pauta pautaCoordenador = pautaDAOImpl.obtemPautaCoordenadorArea(areaPrincipal.getIdeArea(), TipoPautaEnum.PAUTA_COORDENADOR_AREA);
		controleTramitacaoService.salvarNovaTramitacao(
			processo.getIdeProcesso(),
			areaPrincipal.getIdeArea(),
			pautaCoordenador.getIdePauta(),
			StatusFluxoEnum.NOTIFICACAO_EXPEDIDA,
			getIdeUsuarioLogado(),
			null
		);
	}

	private MotivoNotificacao recuperarMotivo(Notificacao notificacao)  {
		
		List<MotivoNotificacao> motivos = (List<MotivoNotificacao>) motivoNotificacaoService.listarMotivoNotificacao(notificacao);
		return motivos.get(0);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean isUsuarioLogadoResponsavelDaAreaSecundaria(Integer ideProcesso) {
		Usuario usuarioLogado = ContextoUtil.getContexto().getUsuarioLogado();
		Area areaSecundaria = null;
		
		if(PerfilUtil.isDiretor()){
			areaSecundaria = areaService.buscarAreaDoProcessoByPauta(new Processo(ideProcesso), true);
			
			if(!Util.isNullOuVazio(areaSecundaria)) {
				areaSecundaria = areaSecundaria.getIdeAreaPai();
			}
		}
		else if(PerfilUtil.isCoordenador()){
			areaSecundaria = areaService.buscarAreaDoProcessoByPauta(new Processo(ideProcesso), true);
		}
		
		if(Util.isNull(areaSecundaria)){
			return false;
		}
		
		return usuarioLogado.getIdePessoaFisica().equals(areaSecundaria.getIdePessoaFisica().getIdePessoaFisica());
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarNotificacaoParcial(NotificacaoParcial notificacaoParcial)  {
		notificacaoParcialDAO.salvarOuAtualizar(notificacaoParcial);
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarNotificacaoFinal(Notificacao notificacaoFinal)  {
		notificacaoDAOImpl.salvarOuAtualizar(notificacaoFinal) ;
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void enviarNotificacaoParcial(NotificacaoParcial notificacaoParcial)  {
		notificacaoParcialDAO.salvar(notificacaoParcial);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Funcionario> listarFuncionariosEquipeProcesso(Processo ideProcesso, Area area)  {
		
		DetachedCriteria criteria = DetachedCriteria.forClass(Funcionario.class);
		criteria
				.setFetchMode("pautaCollection", FetchMode.EAGER)
				.setFetchMode("integranteEquipeCollection", FetchMode.LAZY)
				.setFetchMode("portariaCollection", FetchMode.LAZY)
				.setFetchMode("reservaAguaCollection", FetchMode.LAZY)
				.setFetchMode("controleProcessoAtoCollection", FetchMode.LAZY)
				.setFetchMode("areaCollection", FetchMode.LAZY)
				.add(Property.forName("idePessoaFisica")
				.in(DetachedCriteria.forClass(Equipe.class)
						.createAlias("ideProcesso","p", JoinType.INNER_JOIN)
						.createAlias("ideArea","a", JoinType.INNER_JOIN)
						.createAlias("integranteEquipeCollection","ie", JoinType.INNER_JOIN)
						.createAlias("ie.idePessoaFisica","fun", JoinType.INNER_JOIN)
						.add(Restrictions.eq("p.ideProcesso", ideProcesso.getIdeProcesso()))
						.add(Restrictions.eq("a.ideArea", area.getIdeArea()))
						.setProjection(Projections.property("fun.idePessoaFisica"))
						)
				)
;
		
		return  funcionarioDAO.listarPorCriteria(criteria);
	}
	
	public String statusNumNotificacao(Notificacao notificacao){
		if (isNotificacaoRejeitada(notificacao)) {
			return "NOTIFICAÇÃO REJEITADA";
		}
		if (isNotificacaoCancelada(notificacao)) {
			return "NOTIFICAÇÃO CANCELADA";
		}
		
		if (isNotificacaoAguardandoAprovacao(notificacao)) {
			return "AGUARDANDO APROVAÇÃO";
		}
		
		return notificacao.getNumNotificacao();
	}
	
	private boolean isNotificacaoAguardandoAprovacao(Notificacao notificacao) {
		return (Util.assertEquals(notificacao.getIndCancelado(), false) 
				&& Util.assertEquals(notificacao.getIndAprovado(), false)
				&& Util.assertEquals(notificacao.getIndRejeitado(), false));		
	}

	private boolean isNotificacaoRejeitada(Notificacao notificacao) {
		return (Util.assertEquals(notificacao.getIndCancelado(), false) 
				&& Util.assertEquals(notificacao.getIndAprovado(), false)   
				&& Util.assertEquals(notificacao.getIndRejeitado(), true) );
	}
	
	private boolean isNotificacaoCancelada(Notificacao notificacao) {		
		return (Util.assertEquals(notificacao.getIndCancelado(), true) 
				&& Util.assertEquals(notificacao.getIndAprovado(), false)   
				&& Util.assertEquals(notificacao.getIndRejeitado(), false) );
	}

	private Boolean isNotificacaoEmitida(Notificacao notificacao) {
		return (Util.assertEquals(notificacao.getIndCancelado(), false) 
				&& Util.assertEquals(notificacao.getIndAprovado(), true)   
				&& Util.assertEquals(notificacao.getIndRejeitado(), false) );
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarTramitacao(ControleTramitacao tramitacao, StatusFluxo status ) throws Exception  {
		ControleTramitacao ctAnterior = controleTramitacaoService.buscarUltimoPorProcesso(tramitacao.getIdeProcesso());
		ctAnterior.setIndFimDaFila(false);
		ControleTramitacao ct = tramitacao;
		ct.setIndFimDaFila(true);
		ct.setDtcTramitacao(new Date());
		ct.setIdeArea(ctAnterior.getIdeArea());
		ct.setIdeStatusFluxo(status);
		ct.setIndResponsavel(false);
		ct.setIdePessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica());
		ct.setIdePauta(pautaDAOImpl.obtemPautaArea(ctAnterior.getIdeArea()));		
		//salvando nova tramitacao
		controleTramitacaoService.salvar(ct);
		//removendo a tramitacao anterior do fim da fila
		controleTramitacaoService.atualizar(ctAnterior);
	}	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarListaTramitacaoArea(ControleTramitacao tramitacao, StatusFluxo status, TipoPautaEnum pauta) throws Exception  {
		
		Equipe equipe = equipeDAOImpl.buscarEquipeAtual(tramitacao.getIdeProcesso().getIdeProcesso());
		Area areaPrincipal = equipe.getIdeArea(); 
		Area areaSecundaria = areaService.buscarAreaDoProcessoByPauta(tramitacao.getIdeProcesso(), true);
		
		if(!Util.isNullOuVazio(areaSecundaria)){
			areaPrincipal = areaSecundaria;
		}		
		
		controleTramitacaoService.resetarTramitacoes(tramitacao.getIdeProcesso());
		
		ControleTramitacao ct = tramitacao;
		ct.setIndFimDaFila(true);
		ct.setDtcTramitacao(new Date());
		ct.setIdeArea(areaPrincipal);
		ct.setIdeStatusFluxo(status);
		ct.setIndResponsavel(true);
		ct.setIdePauta(pautaDAOImpl.obtemPautaCoordenadorArea(areaPrincipal.getIdeArea(), pauta));
		ct.setIdePessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica());
		
		controleTramitacaoService.salvar(ct);
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarListaTramitacao(List<ControleTramitacao> listaTramitacao)  {
		
		Date date = new Date();
		ControleTramitacao ctAnterior = controleTramitacaoService.buscarUltimoPorProcesso(listaTramitacao.get(0).getIdeProcesso());
		ctAnterior.setIndFimDaFila(false);
		
		List<ControleTramitacao> listaPersist = new ArrayList<ControleTramitacao>();
		for(ControleTramitacao controle:listaTramitacao) {
			ControleTramitacao ct = new ControleTramitacao();
			ct.setIndFimDaFila(true);
			ct.setDtcTramitacao(date);
			ct.setIdePauta(controle.getIdePauta());
			ct.setIdeProcesso(controle.getIdeProcesso());
			ct.setIndResponsavel(controle.getIndResponsavel());
			ct.setIdeStatusFluxo(controle.getIdeStatusFluxo());
			
			ct.setIdeArea(ctAnterior.getIdeArea());
			listaPersist.add(ct);
			
		}
		
		controleTramitacaoService.salvarLote(listaPersist);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void enviarNotificacaoFinal(Notificacao notificacaoFinal,StatusFluxo ideStatusFluxo) throws Exception {
		
		ControleTramitacao ct = new ControleTramitacao();
		ct.setIdeProcesso(notificacaoFinal.getIdeProcesso());
		ct.setIdeStatusFluxo(ideStatusFluxo);
		
		if(!Util.isNullOuVazio(notificacaoFinal) && notificacaoFinal.getTipo()==1){
			notificacaoFinal.setDtcValidade(calculaValidade(notificacaoFinal.getQtdDiasValidade()));
		}
		
		salvarNotificacaoFinal(notificacaoFinal);
		
		salvarListaTramitacaoArea(ct, ideStatusFluxo, TipoPautaEnum.PAUTA_COORDENADOR_AREA);	
	}
	
	public Date calculaValidade(Integer qtdDiasValidade) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, qtdDiasValidade);
		return calendar.getTime();
	}
	
	

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void desaprovarNotificacao(Notificacao notificacao,List<ControleTramitacao> listaTramitacao) {
		notificacao.setDtcReprovacao(new Date());
		controleTramitacaoDAO.salvarEmLote(listaTramitacao);
		salvarNotificacaoFinal(notificacao);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public Notificacao carregarById(Integer ideNotificacao)  {
		return this.notificacaoDAOImpl.carregarById(ideNotificacao);		
	}
	
	private String montarMsgNotificacaoPrazo(Notificacao notificacao){
		return "Prezado requerente,\n\nInformamos a emissão da notificação nº "+notificacao.getNumNotificacao() + ",\n" +
				"referente ao processo nº " + notificacao.getIdeProcesso().getNumProcesso() + ".\n" +
				"Acesse o conteÃºdo da notificação no site www.seia.ba.gov.br, utilizando sua senha e login.\n" +
				"Caso a citada notificação não seja acessada, o prazo para atendimento da mesma será contado a partir do dÃ©cimo dia de sua emissão.";
	}
	
	private String montarMsgNotificacaoComunicacao(Notificacao notificacao){
		
		String mensagem =null; 
		
		if(isMotivoReenquadramento(notificacao)) {
			mensagem = notificacao.getDscNotificacao();
		}
		else {
			mensagem = "Prezado requerente,\n\nInformamos a emissão da notificação de comunicação nº "+notificacao.getNumNotificacao() + ",\n" +
					   "referente ao processo nº " + notificacao.getIdeProcesso().getNumProcesso() + ".\n" +
					   "Acesse o conteúdo da notificação no site www.seia.ba.gov.br, utilizando sua senha e login.\n";
		}
		
		return mensagem;
	}

	private boolean isMotivoReenquadramento(Notificacao notificacao) {
		for(NotificacaoMotivoNotificacao nmn : notificacao.getNotificacaoMotivoNotificacaoCollection()) {
			if(MotivoNotificacaoEnum.REENQUADRAMENTO_PROCESSO.equals(nmn)) {
				return true;
			}
		}
		return false;
	}
	
	private String gerarNumNotificacaoComunicacao(Processo processo) {

		if(processo.getNumProcesso().indexOf("/") > 0) {
			String NNN ="";
			List<Notificacao> notificacoes = notificacaoDAOImpl.obterNotificacaoProcesso(processo,2);
			if(!Util.isNullOuVazio(notificacoes)) {
				Notificacao p = notificacoes.get(0);
				String aux = p.getNumNotificacao();

				if(!Util.isNullOuVazio(aux)) {
					aux = aux.substring(aux.lastIndexOf('-')+1);
					int i = Integer.parseInt(aux) + 1;
					NNN = "" + i;
				}
			}

			if(Util.isNullOuVazio(NNN)) {
				NNN = "1";
			}

			NNN = Util.lpad(NNN, '0', 3);

			StringBuilder temp = new StringBuilder();
			temp.append(processo.getNumProcesso().substring(0, processo.getNumProcesso().indexOf("/")));
			temp.append("/");
			temp.append("NOTC");
			temp.append("-");
			temp.append(NNN);

			return temp.toString();
		}		 
		return null;
	}
	
	private String gerarNumNotificacao(Processo processo) {

		if(processo.getNumProcesso().indexOf("/") > 0) {
			String NNN ="";
			List<Notificacao> notificacoes = notificacaoDAOImpl.obterNotificacaoProcesso(processo,1);
			if(!Util.isNullOuVazio(notificacoes)) {
				Notificacao p = notificacoes.get(0);
				String aux = p.getNumNotificacao();

				if(!Util.isNullOuVazio(aux)) {
					aux = aux.substring(aux.lastIndexOf('-')+1);
					int i = Integer.parseInt(aux) + 1;
					NNN = "" + i;
				}
			}

			if(Util.isNullOuVazio(NNN)) {
				NNN = "1";
			}

			NNN = Util.lpad(NNN, '0', 3);

			StringBuilder temp = new StringBuilder();
			temp.append(processo.getNumProcesso().substring(0, processo.getNumProcesso().indexOf("/")));
			temp.append("/");
			temp.append("NOT");
			temp.append("-");
			temp.append(NNN);

			return temp.toString();
		}		 
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void aprovarNotificacao(ControleTramitacao tramitacao, StatusFluxo status,Notificacao notificacaoFinal) throws Exception {
		notificacaoDAOImpl.atualizar(notificacaoFinal);
		salvarTramitacao(tramitacao, status);
		alertaService.criarAlerta(tramitacao,notificacaoFinal);
	}
	
	public List<Notificacao> listarPorProcesso(Processo proc) {
		try {
			return notificacaoDAOImpl.listarPorProcesso(proc);
		} catch (Exception e) {
			Log4jUtil.log( this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public void enviarEmailsAoRequerente(Requerimento requerimento, String assunto, String mensagemEmail) throws Exception {
		new EmailUtil().enviarEmail(null, null, listarEmailsRequerente(requerimento), assunto, mensagemEmail);
	}

	public List<String> listarEmailsRequerente(Requerimento requerimento) throws Exception {
		
		List<String> emails = new ArrayList<String>();
		
		if(!Util.isNullOuVazio(requerimento.getDesEmail())) {
			emails.add(requerimento.getDesEmail());
		}
		
		List<RequerimentoPessoa> listaRequerimentoPessoa = (List<RequerimentoPessoa>) requerimentoPessoaService.listarClientesDoRequerimento(requerimento.getIdeRequerimento());
		
		for (RequerimentoPessoa rp : listaRequerimentoPessoa) {
			if (!Util.isNullOuVazio(rp.getPessoa()) && !Util.isNullOuVazio(rp.getPessoa().getDesEmail())) {
				emails.add(rp.getPessoa().getDesEmail());
			}
		}
		
		Collection<Empreendimento> empreendimentos = empreendimentoService.buscarEmpreendimentoPorRequerimento(requerimento);
		
		for (Empreendimento emp : empreendimentos) {
			if(!Util.isNullOuVazio(emp.getDesEmail())) {
				emails.add(emp.getDesEmail());
			}
		}
		
		return emails;
	}
}