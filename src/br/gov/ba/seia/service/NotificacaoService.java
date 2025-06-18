package br.gov.ba.seia.service;

import java.util.ArrayList;	
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Level;

import br.gov.ba.seia.dao.ClasseDAOImpl;
import br.gov.ba.seia.dao.EquipeDAOImpl;
import br.gov.ba.seia.dao.ImovelDAOImpl;
import br.gov.ba.seia.dao.MotivoNotificacaoImovelDAOImpl;
import br.gov.ba.seia.dao.NotificacaoDAOImpl;
import br.gov.ba.seia.dao.NotificacaoMotivoNotificacaoDAOImpl;
import br.gov.ba.seia.dao.NotificacaoParcialDAOImpl;
import br.gov.ba.seia.dao.PautaDAOImpl;
import br.gov.ba.seia.dao.PessoaFisicaDAOImpl;
import br.gov.ba.seia.dao.PotencialPoluicaoDAOImpl;
import br.gov.ba.seia.dao.StatusProcessoAtoDAOImpl;
import br.gov.ba.seia.dto.NotificacaoAprovacaoDTO;
import br.gov.ba.seia.dto.NotificacaoFinalDTO;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.ComunicacaoProcesso;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.FinalidadeReenquadramentoProcesso;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.MotivoNotificacao;
import br.gov.ba.seia.entity.MotivoNotificacaoImovel;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.NotificacaoMotivoNotificacao;
import br.gov.ba.seia.entity.NotificacaoParcial;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ReenquadramentoProcesso;
import br.gov.ba.seia.entity.ReenquadramentoProcessoAto;
import br.gov.ba.seia.entity.StatusProcessoAto;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.MotivoNotificacaoEnum;
import br.gov.ba.seia.enumerator.PerfilEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.StatusProcessoAtoEnum;
import br.gov.ba.seia.enumerator.TipoNotificacaoEnum;
import br.gov.ba.seia.enumerator.TipoPautaEnum;
import br.gov.ba.seia.exception.SeiaRuntimeException;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.EmailUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.PerfilUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NotificacaoService {
	
	@EJB
	private NotificacaoDAOImpl notificacaoDAOImpl;
	@EJB
	private NotificacaoParcialDAOImpl notificacaoParcialDAOImpl;
	@EJB
	private ArquivoProcessoService arquivoProcessoService;
	@EJB
	private ControleTramitacaoService controleTramitacaoService;
	@EJB
	private EquipeDAOImpl equipeDAOImpl;
	@EJB
	private NotificacaoMotivoNotificacaoDAOImpl notificacaoMotivoNotificacaoDAOImpl;
	@EJB
	private PautaDAOImpl pautaDAOImpl;
	@EJB
	private StatusProcessoAtoDAOImpl statusProcessoAtoDAOImpl; 
	@EJB
	private PessoaFisicaDAOImpl pessoaFisicaDAOImpl; 
	@EJB
	private ImovelDAOImpl imovelListagemDAOImpl;
	@EJB
	private ClasseDAOImpl classeDAOImpl; 
	@EJB
	private PotencialPoluicaoDAOImpl potencialPoluicaoDAOImpl; 
	@EJB
	private AreaService areaService;
	@EJB
	private AlertaService alertaService;
	@EJB
	private ComunicacaoProcessoService comunicacaoProcessoService;
	@EJB
	private MotivoNotificacaoService motivoNotificacaoService;
	@EJB
	private LegislacaoService legislacaoService;
	@EJB
	private ImovelService imovelService;
	@EJB
	private ProcessoService processoService;
	@EJB
	private PerguntaRequerimentoService perguntaRequerimentoService;
	@EJB
	private VwConsultaProcessoService vwConsultaProcessoService;
	@EJB
	private ReenquadramentoProcessoService reenquadramentoProcessoService;
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarNotificacaoFinal(NotificacaoFinalDTO dto) {
		try{
			montaNotificacaoFinal(dto);
			verificarNotificacaoFinal(dto);
			//notificacaoMotivoNotificacaoDAOImpl.removerNaoListados(dto.getNotificacaoFinal());
			//notificacaoMotivoNotificacaoDAOImpl.removerNaoListadosComShape(dto.getNotificacaoFinal());
			notificacaoDAOImpl.salvarOuAtualizar(dto.getNotificacaoFinal());
			notificacaoMotivoNotificacaoDAOImpl.salvarOuAtualizar(dto.getNotificacaoFinal().getNotificacaoMotivoNotificacaoCollection(), dto);
			notificacaoMotivoNotificacaoDAOImpl.salvar(dto.getNotificacaoFinal().getNotificacaoMotivoNotificacaoCollectionComShape());
			for(NotificacaoParcial nParcial: dto.getNotificacaoFinal().getNotificacaoParcialCollection()) {
				if(dto.getNotificacaoFinal().getTipo()==nParcial.getTipo()){
					nParcial.setIdeNotificacao(dto.getNotificacaoFinal());
					notificacaoParcialDAOImpl.salvar(nParcial);
				}
			}
		}
		catch(SeiaValidacaoRuntimeException e) {
			throw e;
		}
		catch(Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private void atualizarChaveAuxiliar(NotificacaoFinalDTO dto) throws CloneNotSupportedException {
		
		for(NotificacaoMotivoNotificacao nmn : dto.getListaNotificacaoMotivoNotificacaoSelecionado()) {
			if(nmn.getIdeNotificacaoMotivoNotificacao() == null){
				nmn.setIdeNotificacaoMotivoNotificacao(-1);
			}
			if(nmn.getIdeNotificacaoMotivoNotificacao() < 0) {
				nmn.setIdeNotificacaoMotivoNotificacao(null);
			}
		}
		
		for(NotificacaoMotivoNotificacao nmn : dto.getListaNotificacaoMotivoNotificacaoEnvioShapeSelecionado()) {
			if(Util.isNullOuVazio(nmn.getIdeNotificacaoMotivoNotificacao()) || nmn.getIdeNotificacaoMotivoNotificacao() < 0) {
				nmn.setIdeNotificacaoMotivoNotificacao(null);
			}
			
			Collection<MotivoNotificacaoImovel> motivoNotificacaoImovelCollectionClone = new ArrayList<MotivoNotificacaoImovel>();
			if(!Util.isNullOuVazio(nmn.getMotivoNotificacaoImovelCollection())) {
				for(MotivoNotificacaoImovel mi : nmn.getMotivoNotificacaoImovelCollection()) {
					MotivoNotificacaoImovel clone = mi.clone();
					if(!Util.isNullOuVazio(mi.getIdeMotivoNotificacaoImovel())) {
						if(mi.getIdeMotivoNotificacaoImovel() < 0) {
							//clone.setIdeMotivoNotificacaoImovel(null);
							clone.setIdeNotificacaoMotivoNotificacao(nmn);
						}
					}
					motivoNotificacaoImovelCollectionClone.add(clone);
				}
			} 
			else {
				MotivoNotificacaoImovel motivoNotificacaoImovel = new MotivoNotificacaoImovel();
				motivoNotificacaoImovel.setIdeNotificacaoMotivoNotificacao(nmn);
				Imovel imovel = null;
				for(Imovel i : dto.getListaImovelFlorestal()) {
					imovel = i;
					break;
				}
				motivoNotificacaoImovel.setIdeImovel(imovel);
				motivoNotificacaoImovelCollectionClone.add(motivoNotificacaoImovel);
			}
			nmn.setMotivoNotificacaoImovelCollection(motivoNotificacaoImovelCollectionClone);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void enviarNotificacaoFinal(NotificacaoFinalDTO dto) {
		enviarNotificacaoAprovacao(dto);
		
		if(dto.getUsuarioCoordenador()){
			aprovarNotificacao(dto);
		}
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void enviarNotificacaoAprovacao(NotificacaoFinalDTO dto) {
		try {
			
			Processo processo = dto.getNotificacaoFinal().getIdeProcesso();
			Area areaPrincipal = areaService.buscarUltimaAreaPreAnalise(processo.getIdeProcesso());
			Area areaSecundaria = areaService.buscarAreaDoProcessoByPauta(processo, true);
		
			if(!Util.isNullOuVazio(areaSecundaria)){
				areaPrincipal = areaSecundaria;
			}
			
			TipoPautaEnum tipoPautaEnum = null;
			
			if (dto.getUsuarioCoordenador()){
				tipoPautaEnum = TipoPautaEnum.PAUTA_DIRETOR_AREA;
			} else {
				tipoPautaEnum = TipoPautaEnum.PAUTA_COORDENADOR_AREA;
			}
			 
			if(Util.isNullOuVazio(areaPrincipal)) {
				System.out.println("areaPrincipal");
				JsfUtil.addWarnMessage("Não foi possível encontrar a área responsável.");

				throw new NullPointerException("Não foi possível encontrar a areaPrincipal.");
			}
			
			if(Util.isNullOuVazio(tipoPautaEnum)) {
				System.out.println("tipoPautaEnum");
				JsfUtil.addWarnMessage("Não foi possível encontrar a área responsável.");
				throw new NullPointerException("Não foi possível encontrar o tipoPautaEnum.");
			}
			
			Pauta pautaCoordenadorArea = pautaDAOImpl.obtemPautaCoordenadorArea(areaPrincipal.getIdeArea(), tipoPautaEnum);
			
			if(Util.isNullOuVazio(pautaCoordenadorArea)){
				if(tipoPautaEnum.equals(TipoPautaEnum.PAUTA_DIRETOR_AREA)){
					tipoPautaEnum = TipoPautaEnum.PAUTA_COORDENADOR_AREA;
				} else {
					tipoPautaEnum = TipoPautaEnum.PAUTA_DIRETOR_AREA;
				}
				pautaCoordenadorArea = pautaDAOImpl.obtemPautaCoordenadorArea(areaPrincipal.getIdeArea(), tipoPautaEnum);
				
				if(Util.isNullOuVazio(pautaCoordenadorArea)){
					tipoPautaEnum = TipoPautaEnum.PAUTA_TECNICA;
					pautaCoordenadorArea = pautaDAOImpl.obtemPautaCoordenadorArea(areaPrincipal.getIdeArea(), tipoPautaEnum);
				}
			}
			
			
			controleTramitacaoService.salvarNovaTramitacao(
				processo.getIdeProcesso(), 
				areaPrincipal.getIdeArea(), 
				pautaCoordenadorArea.getIdePauta(), 
				StatusFluxoEnum.AGUARDANDO_APROVACAO_NOTIFICACAO, 
				ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getIdePessoaFisica()
			);
		}
		catch(Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private Area retornarAreaPrincipal(Processo processo) throws Exception {
		
		//#10703
		List<ControleTramitacao> listTramitacoes = (List<ControleTramitacao>) controleTramitacaoService.listarControleTramitacaoPorIdeProcesso(processo.getIdeProcesso());
		
		Integer statusAnterior= 0;
		Area areaPrincipal = null;
		
		if(!Util.isNullOuVazio(listTramitacoes)){
			for(ControleTramitacao ct : listTramitacoes){
				
				if(ct.getIdeStatusFluxo().getIdeStatusFluxo().equals(StatusFluxoEnum.ANALISE_TECNICA.getStatus()) && ct.getIndFimDaFila()){
					//Alguns status anteriores estão nesta regra porque se fez necessário corrigir algumas notificações
					if (statusAnterior.equals(StatusFluxoEnum.FORMADO.getStatus()) || 
							statusAnterior.equals(StatusFluxoEnum.ENCAMINHADO_PARA_AREA.getStatus()) || statusAnterior.equals(StatusFluxoEnum.ENCAMINHADO_PARA_O_GESTOR.getStatus())
							|| statusAnterior.equals(StatusFluxoEnum.NOTIFICACAO_RESPONDIDA.getStatus()) || statusAnterior.equals(StatusFluxoEnum.NOTIFICACAO_NAO_RESPONDIDA.getStatus())
							|| statusAnterior.equals(StatusFluxoEnum.NOTIFICACAO_ENVIADA_PARA_REVISAO.getStatus())
							|| statusAnterior.equals(StatusFluxoEnum.AGUARDANDO_APROVACAO_ANALISE_TECNICA.getStatus())
							|| statusAnterior.equals(StatusFluxoEnum.ANALISE_TECNICA.getStatus())
							|| statusAnterior.equals(StatusFluxoEnum.NOTIFICACAO_CANCELADA.getStatus()) || statusAnterior.equals(StatusFluxoEnum.NOTIFICACAO_CRIADA.getStatus())){
						areaPrincipal = ct.getIdeArea();
						break;
					}
				}  
				else if((ct.getIdeStatusFluxo().getIdeStatusFluxo().equals(StatusFluxoEnum.ENCAMINHADO_PARA_O_GESTOR.getStatus()) || ct.getIdeStatusFluxo().getIdeStatusFluxo().equals(StatusFluxoEnum.AGUARDANDO_APROVACAO_ANALISE_TECNICA.getStatus())
						|| ct.getIdeStatusFluxo().getIdeStatusFluxo().equals(StatusFluxoEnum.ANALISE_TECNICA_APROVADA.getStatus()) || ct.getIdeStatusFluxo().getIdeStatusFluxo().equals(StatusFluxoEnum.NOTIFICACAO_NAO_RESPONDIDA.getStatus())) && ct.getIndFimDaFila()){
					
					if (statusAnterior.equals(StatusFluxoEnum.FORMADO.getStatus()) 
							|| statusAnterior.equals(StatusFluxoEnum.REABERTO.getStatus()) 
							|| (statusAnterior.equals(StatusFluxoEnum.ENCAMINHADO_PARA_AREA.getStatus()) && isCoordenador()) 
							|| statusAnterior.equals(StatusFluxoEnum.ENCAMINHADO_PARA_O_GESTOR.getStatus())
							|| statusAnterior.equals(StatusFluxoEnum.ANALISE_TECNICA.getStatus())
							|| statusAnterior.equals(StatusFluxoEnum.NOTIFICACAO_ENVIADA_PARA_REVISAO.getStatus())
							|| statusAnterior.equals(StatusFluxoEnum.NOTIFICACAO_EXPEDIDA.getStatus())
							|| statusAnterior.equals(StatusFluxoEnum.AGUARDANDO_APROVACAO_ANALISE_TECNICA.getStatus())
							// Status inserido devido as transferências de processos que estão sendo realizadas através de tickets no SEIA
							|| statusAnterior.equals(StatusFluxoEnum.ANALISE_TECNICA_APROVADA.getStatus())
							|| statusAnterior.equals(StatusFluxoEnum.NOTIFICACAO_NAO_RESPONDIDA.getStatus())){
						areaPrincipal = ct.getIdeArea();
						break;
					}
				} else if(ct.getIdeStatusFluxo().getIdeStatusFluxo().equals(StatusFluxoEnum.NOTIFICACAO_ENVIADA_PARA_REVISAO.getStatus()) && ct.getIndFimDaFila()
						&& statusAnterior.equals(StatusFluxoEnum.AGUARDANDO_APROVACAO_NOTIFICACAO.getStatus())){
						areaPrincipal = ct.getIdeArea();
						break;
				}
				statusAnterior = ct.getIdeStatusFluxo().getIdeStatusFluxo();
			}
		}
		
		return areaPrincipal;
	}
	
	private void montarNotificacao(Notificacao notificacao) {
		try {
			notificacao.setIndCancelado(false);
			notificacao.setIndRejeitado(false);
			notificacao.setIdePautaAprovacao(pautaDAOImpl.carregarPautaPorIdeProcessoFimFila(notificacao.getIdeProcesso().getIdeProcesso()));
		}
		catch(Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * 
	 * Método que vai setar o indAprovado e dtcEnvio para a notificação.
	 *
	 * @param notificacaoFinal
	 *
	 * @author eduardo.fernandes
	 * @since 20/10/2016
	 * @see <a href="http://10.105.17.77/redmine/issues/8163">#8163</a>
	 */
	private void prepararNotificacaoAprovada(Notificacao notificacaoFinal) {
		notificacaoFinal.setIndAprovado(true);			
		notificacaoFinal.setDtcEnvio(new Date());
	}
	
	private void aprovarNotificacao(NotificacaoFinalDTO dto) {
		try {
			
			Notificacao notificacaoFinal = dto.getNotificacaoFinal();
			Processo processo = notificacaoFinal.getIdeProcesso();
			Area areaPrincipal = retornarAreaPrincipal(processo);
			Pauta pautaAreaPrinical = pautaDAOImpl.obtemPautaArea(areaPrincipal.getIdeArea());
			
			montarNotificacao(notificacaoFinal);
			
			if(notificacaoFinal.isNotificacaoPrazo()){
				Integer qtdNotificacoesEmitidas = notificacaoDAOImpl.bucarQtdNotificacoesEmitidas(processo.getIdeProcesso(), TipoNotificacaoEnum.NOTIFICACAO_PRAZO);
				if(PerfilUtil.isCoordenador() && qtdNotificacoesEmitidas >= 2) {
					enviarParaAprovacaoDiretor(processo, areaPrincipal);
				}
				else{
					notificacaoFinal.setNumNotificacao(gerarNumNotificacao(notificacaoFinal));
					prepararNotificacaoAprovada(notificacaoFinal);
					emitirNotificacao(notificacaoFinal, processo, areaPrincipal, pautaAreaPrinical);
					
					String mensagem = msgNotificacao(notificacaoFinal);
					ComunicacaoProcesso comunicacaoComRequerente = new ComunicacaoProcesso(new Date(), mensagem, processo);
					comunicacaoProcessoService.salvar(comunicacaoComRequerente);
					
					EmailUtil enviarEmail = new EmailUtil();
					enviarEmail.enviarEmail(dto.getEmail(), "SEIA - Comunicado - Notificação número "+dto.getNotificacaoFinal().getNumNotificacao(), mensagem);
				}
			} 
			else if(dto.getNotificacaoFinal().isNotificacaoComunicacao()) {

				if(isCoordenador()) {
					enviarParaAprovacaoDiretor(processo, areaPrincipal);
				}
				else if(isDiretor()) {
					
					notificacaoFinal.setNumNotificacao(gerarNumNotificacaoComunicacao(notificacaoFinal));
					prepararNotificacaoAprovada(notificacaoFinal);
					
					emitirNotificacao(notificacaoFinal, processo, areaPrincipal, pautaAreaPrinical);
					
					indeferirOuArquivar(notificacaoFinal, processo,	areaPrincipal, pautaAreaPrinical);
					
					String mensagem = msgNotificacaoComunicacao(notificacaoFinal);
					
					ComunicacaoProcesso comunicacaoComRequerente = new ComunicacaoProcesso(new Date(),mensagem,dto.getNotificacaoFinal().getIdeProcesso());
					comunicacaoProcessoService.salvar(comunicacaoComRequerente);
					
					EmailUtil enviarEmail = new EmailUtil();
					enviarEmail.enviarEmail(dto.getEmail(), "SEIA - Comunicado - Notificação de comunicação nº "+notificacaoFinal.getNumNotificacao(), mensagem);
				}
			}
		}
		catch (Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private void indeferirOuArquivar(Notificacao notificacaoFinal, Processo processo, Area areaPrincipal, Pauta pautaAreaPrinical)  {
		
		MotivoNotificacao motivoNotificacao = null;
		for(NotificacaoMotivoNotificacao nmn : notificacaoFinal.getNotificacaoMotivoNotificacaoCollection()) {
			motivoNotificacao = nmn.getIdeMotivoNotificacao();
		}
		
		controleTramitacaoService.salvarNovaTramitacao(
				processo.getIdeProcesso(), 
				areaPrincipal.getIdeArea(), 
				pautaAreaPrinical.getIdePauta(), 
				StatusFluxoEnum.NOTIFICACAO_EXPEDIDA, 
				ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getIdePessoaFisica()
		);
		
		if(motivoNotificacao.getIdeMotivoNotificacao().equals(MotivoNotificacaoEnum.INDEFERIR.getId())){
			controleTramitacaoService.salvarNovaTramitacao(
				processo.getIdeProcesso(), 
				areaPrincipal.getIdeArea(), 
				pautaAreaPrinical.getIdePauta(), 
				StatusFluxoEnum.CONCLUIDO, 
				ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getIdePessoaFisica()
			);
		}
		else if(motivoNotificacao.getIdeMotivoNotificacao().equals(MotivoNotificacaoEnum.ARQUIVAR.getId())){
			controleTramitacaoService.salvarNovaTramitacao(
				processo.getIdeProcesso(), 
				areaPrincipal.getIdeArea(), 
				pautaAreaPrinical.getIdePauta(), 
				StatusFluxoEnum.ARQUIVADO, 
				ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getIdePessoaFisica()
			);
		}
	}
	
	private void emitirNotificacao(Notificacao notificacaoFinal, Processo processo, Area areaPrincipal, Pauta pautaAreaPrinical) {
		notificacaoDAOImpl.atualizar(notificacaoFinal);
		controleTramitacaoService.salvarNovaTramitacao(
				processo.getIdeProcesso(), 
				areaPrincipal.getIdeArea(), 
				pautaAreaPrinical.getIdePauta(), 
				StatusFluxoEnum.NOTIFICACAO_EXPEDIDA, 
				ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getIdePessoaFisica()
		);
	}
	
	private void enviarParaAprovacaoDiretor(Processo processo, Area areaPrincipal) throws Exception {
		Area areaDiretor = areaPrincipal.getIdeAreaPai();
		Pauta pautaDiretor = pautaDAOImpl.obtemPautaCoordenadorArea(areaDiretor.getIdeArea(), TipoPautaEnum.PAUTA_DIRETOR_AREA);
		controleTramitacaoService.salvarNovaTramitacao(
			processo.getIdeProcesso(), 
			areaDiretor.getIdeArea(), 
			pautaDiretor.getIdePauta(), 
			StatusFluxoEnum.AGUARDANDO_APROVACAO_NOTIFICACAO, 
			ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getIdePessoaFisica()
		);
	}
	
	private boolean isCoordenador(){
		return ContextoUtil.getContexto().getUsuarioLogado().getIdePerfil().getIdePerfil().equals(PerfilEnum.COORDENADOR.getId()) 
				|| ContextoUtil.getContexto().getUsuarioLogado().getIdePerfil().getIdePerfil().equals(PerfilEnum.COORD_CTGA.getId());
	}
	
	private boolean isDiretor(){
		return ContextoUtil.getContexto().getUsuarioLogado().getIdePerfil().getIdePerfil().equals(PerfilEnum.DIRETOR.getId());
	}

	private String msgNotificacaoComunicacao(Notificacao notificacaoFinal) {
		return  "Prezado requerente,\n\nInformamos a emissão da notificação de comunicação nº "+notificacaoFinal.getNumNotificacao() + ",\n" +
				       "referente ao processo nº "+ notificacaoFinal.getIdeProcesso().getNumProcesso()+ ".\n"+
				       "Acesse o conteúdo da notificação no site www.seia.ba.gov.br, utilizando sua senha e login.\n";
		
	}

	private String msgNotificacao(Notificacao notificacaoFinal) {
		return "Prezado requerente,\n\nInformamos a emissão da notificação nº "+notificacaoFinal.getNumNotificacao() + ",\n" +
						 "referente ao processo nº "+ notificacaoFinal.getIdeProcesso().getNumProcesso()+ ".\n"+
						 "Acesse o conteúdo da notificação no site www.seia.ba.gov.br, utilizando sua senha e login.\n" +
						 "Caso a citada notificação não seja acessada, o prazo para atendimento da mesma será contado a partir do décimo dia de sua emissão.";

	}
	
	public Date calculaValidade(Integer qtdDiasValidade) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, qtdDiasValidade);
		return calendar.getTime();
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void prepararEnvioNotificacao(NotificacaoFinalDTO dto) {
		
		dto.getNotificacaoFinal().setIndEnviadoAprovacao(true);
		
		if(dto.getNotificacaoFinal().isNotificacaoPrazo()){
			Date validade = calculaValidade(dto.getNotificacaoFinal().getQtdDiasValidade());
			dto.getNotificacaoFinal().setDtcValidade(validade);
		}
		
		notificacaoDAOImpl.atualizar(dto.getNotificacaoFinal());
		
		for(NotificacaoParcial nParcial: dto.getNotificacaoFinal().getNotificacaoParcialCollection()){
			if(dto.getNotificacaoFinal().getTipo() == nParcial.getTipo()){
				nParcial.setIndEmissao(true);
				notificacaoParcialDAOImpl.atualizar(nParcial);
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void montaNotificacaoFinal(NotificacaoFinalDTO dto) {
		try {
			Notificacao notificacaoFinal = dto.getNotificacaoFinal();
			boolean imovelSetado;
			notificacaoFinal.setIdeProcesso(dto.getProcesso());
			notificacaoFinal.setIdeLegislacao(dto.getLegislacao());
			notificacaoFinal.setDtcCriacao(new Date());		
			notificacaoFinal.setTipo(dto.getTipoNotificacaoEnum().getTipo());
			notificacaoFinal.setIndRejeitado(false);
			notificacaoFinal.setIndCancelado(false);
			notificacaoFinal.setIndAprovado(false);
			
			dto.setEmail(dto.getVwProcesso().getDesEmail());
			dto.setUsuarioCoordenador(dto.getUsuarioCoordenador());
			
			if(dto.getUsuarioCoordenador()) {
				Integer ideCoordenador = ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica();	
				notificacaoFinal.setIdePautaCriacao(pautaDAOImpl.obtemPautaPorIdeFuncionario(ideCoordenador));			
			}
			else{
				if(Util.isNullOuVazio(dto.getPautaCriacao())) {
					Integer ideUsuarioLogado = ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica();	
					notificacaoFinal.setIdePautaCriacao(pautaDAOImpl.obtemPautaPorIdeFuncionario(ideUsuarioLogado));	
				}else {
					notificacaoFinal.setIdePautaCriacao(dto.getPautaCriacao());
				}
			}
			if(!Util.isNullOuVazio(dto.getListaNotificacaoMotivoNotificacaoEnvioShapeSelecionado())) {
				for(NotificacaoMotivoNotificacao notificacaoMotivoNotificacao : dto.getListaNotificacaoMotivoNotificacaoEnvioShapeSelecionado()) {
					if(!notificacaoMotivoNotificacao.isSemImovel()) {
						imovelSetado=false;
						for(MotivoNotificacaoImovel mni : notificacaoMotivoNotificacao.getMotivoNotificacaoImovelCollection()) {
							if(mni.getIdeImovel()!=null){
								imovelSetado=true;
								break;
							}
						}
						if(imovelSetado==false) {
							throw new SeiaValidacaoRuntimeException("Favor selecionar o imóvel referente ao motivo selecionado.");							
						}
					}
				}
			}
			atualizarChaveAuxiliar(dto);
			notificacaoFinal.setNotificacaoMotivoNotificacaoCollection(new ArrayList<NotificacaoMotivoNotificacao>());
			notificacaoFinal.getNotificacaoMotivoNotificacaoCollection().addAll(dto.getListaNotificacaoMotivoNotificacaoSelecionado());
			notificacaoFinal.setNotificacaoMotivoNotificacaoCollectionComShape(new ArrayList<NotificacaoMotivoNotificacao>());
			if(notificacaoFinal.isNotificacaoPrazo()){
				for(NotificacaoMotivoNotificacao nmn : dto.getListaNotificacaoMotivoNotificacaoEnvioShapeSelecionado()) {
					notificacaoFinal.getNotificacaoMotivoNotificacaoCollectionComShape().add(nmn);
				}
				
			}
		}
		catch(Exception e) {
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void verificarNotificacaoFinal(NotificacaoFinalDTO dto) {
		
		if(dto.getNotificacaoFinal().getDscNotificacao() == null || dto.getNotificacaoFinal().getDscNotificacao().equals("")){
			throw new SeiaValidacaoRuntimeException("O campo texto da notificação é de preenchimento obrigatório.");
		}
		
		if(dto.getNotificacaoComArquivosApensados()){
			if(verificarSeListaDocumentosVaziaOuNula(dto)) {
				throw new SeiaValidacaoRuntimeException("Nenhum arquivo foi apensado a notificação.");
			} else if(existeDocumentoSemDescricao(dto)){
				throw new SeiaValidacaoRuntimeException("O campo descrição do documento apensado é obrigatório.");
			}
		}
		
		if(verificarSeMotivosNotificacaoFicouVazio(dto)){
			throw new SeiaValidacaoRuntimeException("Nenhum motivo foi selecionado");
		}
		
		if(verificarSeQuantidadeDeDiasVazioOuMenorQueZero(dto)){
			throw new SeiaValidacaoRuntimeException("O campo quantidade de dias é de preenchimento obrigatório, e o prazo informado deve ser maior ou igual a 1(um) dia.");
		}
		
		if (verificarObrigatoriedadeMotivoReenquadramento(dto)){
			throw new SeiaValidacaoRuntimeException("Selecione ao menos uma Finalidade(s) do reenquadramento.");
		}
		
		if(verificarSeFinalidadeReenquadramentoFicouVazio(dto)){ 
			throw new SeiaValidacaoRuntimeException(dto.getMensagemFinalidadeReenquadramentoVazia());
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean verificarObrigatoriedadeMotivoReenquadramento(NotificacaoFinalDTO dto) { 
		return MotivoNotificacaoEnum.REENQUADRAMENTO_PROCESSO.equals(dto.getNotificacaoMotivoNotificacaoSelecionado()) && verificarSelecaoMotivoReenquadramento(dto);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean verificarSelecaoMotivoReenquadramento(NotificacaoFinalDTO dto){
		return !dto.isAlterarAtoAutorizativo() && !dto.isIncluirNovoAtoAutorizativo() && !dto.isAlterarPotencialPoluidor() && !dto.isAlterarClasseEmpreendimento()
				&& !dto.isAlterarTipologia() && !dto.isCorrigirPorte();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean verificarSeListaDocumentosVaziaOuNula(NotificacaoFinalDTO dto) {
		return dto.getListaDeDocumentosDaNotificacao() == null || dto.getListaDeDocumentosDaNotificacao().isEmpty();
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean verificarSeQuantidadeDeDiasVazioOuMenorQueZero(NotificacaoFinalDTO dto) {
		return dto.getNotificacaoFinal().isNotificacaoPrazo() && (dto.getNotificacaoFinal().getQtdDiasValidade() == null || dto.getNotificacaoFinal().getQtdDiasValidade() <= 0);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean verificarSeMotivosNotificacaoFicouVazio(NotificacaoFinalDTO dto) {
		return dto.getListaNotificacaoMotivoNotificacaoSelecionado().isEmpty();
		
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean verificarSeImovelFicouVazio(NotificacaoFinalDTO dto) {
		boolean retorno = false;
		if(!Util.isNullOuVazio(dto.getListaNotificacaoMotivoNotificacaoEnvioShapeSelecionado())) {
			for(NotificacaoMotivoNotificacao mn : dto.getNotificacaoFinal().getNotificacaoMotivoNotificacaoCollectionComShape()) {				
				if(Util.isNullOuVazio(mn.getMotivoNotificacaoImovelCollection())){
					
				     return true;
				}
				
			}
		}
		
		return retorno;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean verificarSeFinalidadeReenquadramentoFicouVazio(NotificacaoFinalDTO dto){

		boolean retorno= false;

		if (dto.isAlterarAtoAutorizativo()){
			if (Util.isNullOuVazio(dto.getListaAlteracaoReenquadramentoProcessoAto())){
				dto.setMensagemFinalidadeReenquadramentoVazia("Selecione o ato a ser alterado.");
				retorno = true;
			}
			else {
				for (ReenquadramentoProcessoAto rpa : dto.getListaAlteracaoReenquadramentoProcessoAto()) {
					if(Util.isNullOuVazio(rpa.getIdeNovoAtoAmbiental())){
						dto.setMensagemFinalidadeReenquadramentoVazia("Selecione o novo ato a ser alterado.");
						retorno = true;
					}
				}
			
			}
		}
		if (dto.isIncluirNovoAtoAutorizativo() && Util.isNullOuVazio(dto.getListaInclusaoReenquadramentoProcessoAto())){
				dto.setMensagemFinalidadeReenquadramentoVazia("Selecione o ato a ser incluído.");
				retorno = true;
		}
		if (dto.isAlterarPotencialPoluidor() && Util.isNullOuVazio(dto.getListaReenquadramentoPotencialPoluicao())){
				dto.setMensagemFinalidadeReenquadramentoVazia("Selecione o potencial poluidor a ser alterado.");
				retorno = true;
		}
		if (dto.isAlterarClasseEmpreendimento() && Util.isNullOuVazio(dto.getReenquadramentoProcesso().getIdeNovaClasseEmpreendimento())){
				dto.setMensagemFinalidadeReenquadramentoVazia("Selecione a classe a ser alterada.");
				retorno = true;
		}
		if (dto.isAlterarTipologia()&& Util.isNullOuVazio(dto.getListaRequerimentoTipologiaParaReenquadramento())){
				dto.setMensagemFinalidadeReenquadramentoVazia("Selecione a tipologia a ser alterada.");
				retorno = true;			
		}

		return retorno; 
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean existeDocumentoSemDescricao(NotificacaoFinalDTO dto) {
		for(ArquivoProcesso arq : dto.getListaDeDocumentosDaNotificacao()) {
			if(!arq.isDscConfirmada()){
				return true;
			}
		}
		return false;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String gerarNumNotificacao(Notificacao notificacao){
		
		Processo processo = notificacao.getIdeProcesso();
		
		if(processo.getNumProcesso().indexOf("/") > 0) {
			String nNN ="";
			List<Notificacao> notificacoes = notificacaoDAOImpl.obterNotificacaoProcesso(processo,1);
			if(!Util.isNullOuVazio(notificacoes)) {
				Notificacao p = notificacoes.get(0);
				String aux = p.getNumNotificacao();

				if(!Util.isNullOuVazio(aux)) {
					aux = aux.substring(aux.lastIndexOf('-')+1);
					int i = Integer.parseInt(aux) + 1;
					nNN = "" + i;
				}
			}

			if(Util.isNullOuVazio(nNN)) {
				nNN = "1";
			}

			nNN = Util.lpad(nNN, '0', 3);

			StringBuilder temp = new StringBuilder();
			temp.append(processo.getNumProcesso().substring(0, processo.getNumProcesso().indexOf("/")));
			temp.append("/");
			temp.append("NOT");
			temp.append("-");
			temp.append(nNN);

			return temp.toString();
		}		 
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public String gerarNumNotificacaoComunicacao(Notificacao notificacao) throws Exception{
		
		Processo processo = notificacao.getIdeProcesso();
		
		if(processo.getNumProcesso().indexOf("/") > 0) {
			String nNN ="";
			List<Notificacao> notificacoes = notificacaoDAOImpl.obterNotificacaoProcesso(processo,2);
			if(!Util.isNullOuVazio(notificacoes)) {
				Notificacao p = notificacoes.get(0);
				String aux = p.getNumNotificacao();

				if(!Util.isNullOuVazio(aux)) {
					aux = aux.substring(aux.lastIndexOf('-')+1);
					int i = Integer.parseInt(aux) + 1;
					nNN = "" + i;
				}
			}

			if(Util.isNullOuVazio(nNN)) {
				nNN = "1";
			}

			nNN = Util.lpad(nNN, '0', 3);

			StringBuilder temp = new StringBuilder();
			temp.append(processo.getNumProcesso().substring(0, processo.getNumProcesso().indexOf("/")));
			temp.append("/");
			temp.append("NOTC");
			temp.append("-");
			temp.append(nNN);

			return temp.toString();
		}		 
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public NotificacaoFinalDTO carregarNotificacaoFinal(VwConsultaProcesso vwp, TipoNotificacaoEnum tipoNotificacaoEnum, boolean acessoFeitoPorPermissaoConcedida) throws Exception {
		
		NotificacaoFinalDTO dto = new NotificacaoFinalDTO();
		
		dto.setTipoNotificacaoEnum(tipoNotificacaoEnum);
		dto.setAcessoFeitoPorPermissaoConcedida(acessoFeitoPorPermissaoConcedida);
		dto.setLegislacao(legislacaoService.carregarByTipo(2));
		dto.setVwProcesso(vwp);
		dto.setObservacao("");
		dto.setUsuarioLider(dto.getVwProcesso().isIndLiderEquipe());
		dto.setProcesso(processoService.carregarProcesso(dto.getVwProcesso().getIdeProcesso()));
		dto.setPautaCriacao(pautaDAOImpl.retornarPautaDoTecnicoCriadorDaNotificacao(dto.getProcesso().getIdeProcesso(), dto.getVwProcesso().getIdeArea()));
		dto.setListaImovel(imovelService.listarImovelPor(dto.getVwProcesso().getIdeRequerimento()));
		dto.setListaImovelFlorestal(imovelListagemDAOImpl.listarImoveisPor(dto.getVwProcesso().getIdeProcesso()));
		verificarUsuarioCoordenador(dto);
		carregarNotificacao(dto);
		carregarReenquadramentoProcesso(dto);
		
		return dto;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private void carregarReenquadramentoProcesso(NotificacaoFinalDTO dto) throws Exception {
		if(MotivoNotificacaoEnum.REENQUADRAMENTO_PROCESSO.equals(dto.getNotificacaoMotivoNotificacaoSelecionado())) {
			carregarDadosReenquadramentoProcesso(dto);
			reenquadramentoProcessoService.carregarReenquadramentoProcesso(dto);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void carregarReenquadramentoProcesso(NotificacaoAprovacaoDTO dto) throws Exception {
		if(dto.getNotificacao().isNotificacaoComunicacao()) {
			NotificacaoMotivoNotificacao nmn = dto.getNotificacao().getNotificacaoMotivoNotificacaoCollection().iterator().next();
			if(MotivoNotificacaoEnum.REENQUADRAMENTO_PROCESSO.equals(nmn)) {
				reenquadramentoProcessoService.carregarReenquadramentoProcesso(dto);
			}
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public void carregarDadosReenquadramentoProcesso(NotificacaoFinalDTO dto) throws Exception {
		vwConsultaProcessoService.getProcessoComListaAtos(dto.getVwProcesso());
		vwConsultaProcessoService.getProcessoComRequerimentoTipologia(dto.getVwProcesso());
		vwConsultaProcessoService.getProcessoComEmpreendimentoRequerimento(dto.getVwProcesso());
		dto.setListaClasseParaReenquadramento(classeDAOImpl.listarClasse());
		dto.setListaPotencialPoluicaoParaReenquadramento(potencialPoluicaoDAOImpl.listarPotencialPoluicao());
		dto.setReenquadramentoProcesso(new ReenquadramentoProcesso(dto.getNotificacaoFinal()));
		dto.getReenquadramentoProcesso().setFinalidadeReequadramentoProcessoCollection(new ArrayList<FinalidadeReenquadramentoProcesso>());
	}
	
	private void verificarUsuarioCoordenador(NotificacaoFinalDTO dto) {
		try{
			Integer pessoaLogada = ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica();
			ControleTramitacao ultimaTramitacao = controleTramitacaoService.buscarUltimoPorProcesso(dto.getProcesso());
			dto.setUsuarioCoordenador(false);
			if(dto.isAcessoFeitoPorPermissaoConcedida() || pessoaLogada.equals(ultimaTramitacao.getIdeArea().getIdePessoaFisica().getIdePessoaFisica())){
				dto.setUsuarioCoordenador(true);
			} 
			
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private void carregarNotificacao(NotificacaoFinalDTO dto) {
		try {
			
			Area areaCoordenador = new Area(dto.getVwProcesso().getIdeArea());
			dto.setNotificacaoFinal(notificacaoDAOImpl.obterNotificacaoFinal(dto));
			
			atualizarTextoNotificacao(dto);
			
			dto.getNotificacaoFinal().setNotificacaoParcialCollection(notificacaoParcialDAOImpl.listarNotificacaoParcialPorTecnicosTipoNot(dto, areaCoordenador.getIdeArea()));
			
			for(NotificacaoParcial np : dto.getNotificacaoFinal().getNotificacaoParcialCollection()){
				np.setDscTextoParcial(np.getDscTextoParcial().replaceAll("\n", "<br />"));
			}
			
			dto.setListaDeDocumentosDaNotificacao(arquivoProcessoService.listarArquivosProcessoPorNotificacao(dto.getNotificacaoFinal()));
			dto.setNotificacaoComArquivosApensados(true);
			if(Util.isNullOuVazio(dto.getListaDeDocumentosDaNotificacao())){
				dto.setNotificacaoComArquivosApensados(false);
			}
			
			carregarMotivoNotificacao(dto);
			
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void atualizarTextoNotificacao(NotificacaoFinalDTO dto) {
		Notificacao notificacaoFinal = dto.getNotificacaoFinal();
		if(notificacaoFinal.isNotificacaoComunicacao() && Util.isNullOuVazio(notificacaoFinal.getDscNotificacao())) {
			StatusProcessoAto statusARRL = statusProcessoAtoDAOImpl.buscarStatusProcessoAtoPor(dto.getVwProcesso().getIdeProcesso(), AtoAmbientalEnum.ARRL.getId());
			if(dto.getVwProcesso().verificarStatus(StatusFluxoEnum.ANALISE_TECNICA_APROVADA)
			&& 	new StatusProcessoAto(StatusProcessoAtoEnum.DEFERIDO_PELO_TECNICO.getId()).equals(statusARRL)) {
				Collection<Imovel> listaImoveis = imovelListagemDAOImpl.listarImoveisPor(dto.getVwProcesso().getIdeProcesso());
				StringBuilder texto = new StringBuilder();
				texto.append("Foi aprovada a realocação Reserva Legal para a(s) fazenda(s):\n\n");
				for(Imovel imovel : listaImoveis) {
					texto.append("- "+imovel.getNomeImovelRural()+", "+imovel.getImovelRural().getNumMatricula()+" de propriedade de "+retornarProprietarios(imovel)+"\n");
				}
				texto.append("Fica autorizado o cancelamento da Averbação da Reserva Legal anterior, Ã  margem da matrí­cula do imóvel, quando for o caso. O requerente deverá atualizar o cadastro e documentos do imóvel no CEFIR.");
				notificacaoFinal.setDscNotificacao(texto.toString());
			}
		}
	}

	private String retornarProprietarios(Imovel imovel) {
		StringBuilder proprietarios = new StringBuilder();
		Collection<PessoaFisica> listaProprietario = pessoaFisicaDAOImpl.listarProprietarioImovel(imovel.getIdeImovel());
		
		if(Util.isNullOuVazio(listaProprietario)) {
			throw new SeiaRuntimeException("Não foi possível carregar a lista de proprietários.");			
		}
		else {
			for (Iterator<PessoaFisica> iterator = listaProprietario.iterator(); iterator.hasNext();) {
				PessoaFisica pessoaFisica =  iterator.next();
				
				if(listaProprietario.size()==1) {
					proprietarios.append(pessoaFisica.getNomPessoa()+".");
				}
				else{
					if(iterator.hasNext()) {
						proprietarios.append(pessoaFisica.getNomPessoa()+", ");
					}
					else{
						proprietarios.append("e "+pessoaFisica.getNomPessoa()+".");
					}
				}
			}
			return proprietarios.toString().replace(", e ", " e ");
		}
		
	}
	
	public void carregarMotivoNotificacao(NotificacaoFinalDTO dto) throws Exception {
			
		dto.getNotificacaoFinal().setNotificacaoMotivoNotificacaoCollection(
				notificacaoMotivoNotificacaoDAOImpl.listarNotificacaoMotivoNotificacaoPor(dto.getNotificacaoFinal())
		);
		
		Collection<MotivoNotificacao> listaMotivoNotificacao = motivoNotificacaoService.listarMotivoNotificacaoSemEnvioShape(dto.getNotificacaoFinal());
		dto.setListaNotificacaoMotivoNotificacao(
				listarNotificacaoMotivoNotificacao(dto.getNotificacaoFinal(), listaMotivoNotificacao)
		);
		
		Collection<MotivoNotificacao> listaMotivoNotificacaoEnvioShape = 
				motivoNotificacaoService.listarMotivoNotificacaoEnvioShape(dto.getNotificacaoFinal());
		dto.setListaNotificacaoMotivoNotificacaoEnvioShape(
				listarNotificacaoMotivoNotificacao(dto.getNotificacaoFinal(), listaMotivoNotificacaoEnvioShape)
		);
		
		dto.setListaNotificacaoMotivoNotificacaoSelecionado(
			listarNotificacaoMotivoNotificacaoSelecionado(dto.getNotificacaoFinal(), dto.getListaNotificacaoMotivoNotificacao(),dto.getListaImovel())
		);
		
		dto.setListaNotificacaoMotivoNotificacaoEnvioShapeSelecionado(
			listarNotificacaoMotivoNotificacaoSelecionado(dto.getNotificacaoFinal(), dto.getListaNotificacaoMotivoNotificacaoEnvioShape(),dto.getListaImovel())
		);
		
		if(TipoNotificacaoEnum.NOTIFICACAO_COMUNICACAO.equals(dto.getTipoNotificacaoEnum())) {
			List<NotificacaoMotivoNotificacao> lista = (List<NotificacaoMotivoNotificacao>) dto.getListaNotificacaoMotivoNotificacaoSelecionado();
			if(!Util.isNullOuVazio(lista)) {
				dto.setNotificacaoMotivoNotificacaoSelecionado(lista.get(0));
			}
		}
	}

	private Collection<NotificacaoMotivoNotificacao> listarNotificacaoMotivoNotificacaoSelecionado(Notificacao notificacao, Collection<NotificacaoMotivoNotificacao> lista, Collection<Imovel> listaImovel) {
		Collection<NotificacaoMotivoNotificacao> listaNotificacaoMotivoNotificacao = new ArrayList<NotificacaoMotivoNotificacao>();
		for(NotificacaoMotivoNotificacao nmn : lista) {
			if(notificacao.getNotificacaoMotivoNotificacaoCollection().contains(nmn)) {
				nmn.getIdeMotivoNotificacao().setChecked(true);
				if(Util.isNullOuVazio(listaImovel)) {
					nmn.setSemImovel(true);
				}
				listaNotificacaoMotivoNotificacao.add(nmn);
			}
		}
		return listaNotificacaoMotivoNotificacao;
	}

	private Collection<NotificacaoMotivoNotificacao> listarNotificacaoMotivoNotificacao(Notificacao notificacao,Collection<MotivoNotificacao> listaMotivoNotificacao) {
		if(Util.isNullOuVazio(notificacao.getNotificacaoMotivoNotificacaoCollection())) {
			int indexAux = 0;
			Collection<NotificacaoMotivoNotificacao> lista = new ArrayList<NotificacaoMotivoNotificacao>();
			for(MotivoNotificacao mn : listaMotivoNotificacao) {
				NotificacaoMotivoNotificacao nmn = new NotificacaoMotivoNotificacao();
				nmn.setIdeNotificacaoMotivoNotificacao(--indexAux);
				nmn.setIdeMotivoNotificacao(mn);
				nmn.setIdeNotificacao(notificacao);
				lista.add(nmn);
			}
			return lista;
		}
		else {
			int indexAux = 0;
			Collection<NotificacaoMotivoNotificacao> lista = new ArrayList<NotificacaoMotivoNotificacao>();
			for(MotivoNotificacao mn : listaMotivoNotificacao) {
				NotificacaoMotivoNotificacao nmnAux = null;
				for(NotificacaoMotivoNotificacao nmn : notificacao.getNotificacaoMotivoNotificacaoCollection()) {
					if(mn.equals(nmn.getIdeMotivoNotificacao())) {
						nmnAux = nmn;
					}
				}
				if(Util.isNull(nmnAux)) {
					nmnAux = new NotificacaoMotivoNotificacao();
					nmnAux.setIdeNotificacaoMotivoNotificacao(--indexAux);
					nmnAux.setIdeMotivoNotificacao(mn);
					nmnAux.setIdeNotificacao(notificacao);
					lista.add(nmnAux);
				}
				else {
					lista.add(nmnAux);
				}
			}
			return lista;
		}
	}
}