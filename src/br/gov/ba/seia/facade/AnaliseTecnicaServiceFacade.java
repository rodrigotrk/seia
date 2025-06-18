package br.gov.ba.seia.facade;

import java.util.ArrayList;
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

import br.gov.ba.seia.controller.AprovarAnaliseTecnicaController;
import br.gov.ba.seia.controller.ProcessoAnaliseTecnicaController;
import br.gov.ba.seia.controller.SuperProcessoAnaliseTecnicaController;
import br.gov.ba.seia.dao.AnaliseTecnicaDAOImpl;
import br.gov.ba.seia.dao.AreaDAOImpl;
import br.gov.ba.seia.dao.ControleProcessoAtoDAOImpl;
import br.gov.ba.seia.dao.ControleTramitacaoDAOImpl;
import br.gov.ba.seia.dao.EquipeDAOImpl;
import br.gov.ba.seia.dao.FuncionarioDAOImpl;
import br.gov.ba.seia.dao.PautaDAOImpl;
import br.gov.ba.seia.dao.ProcessoAtoConcedidoDAOImpl;
import br.gov.ba.seia.dao.ProcessoAtoDAOImpl;
import br.gov.ba.seia.dao.ProcessoReenquadramentoDAOImpl;
import br.gov.ba.seia.dao.ReservaLegalDAOImpl;
import br.gov.ba.seia.dao.StatusProcessoAtoDAOImpl;
import br.gov.ba.seia.dto.AnaliseAtoDTO;
import br.gov.ba.seia.dto.AprovacaoAnaliseAtoDTO;
import br.gov.ba.seia.entity.AnaliseTecnica;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.AtoAmbiental;
import br.gov.ba.seia.entity.ControleProcessoAto;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.DocumentoAto;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Enquadramento;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.Parametro;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.PerguntaRequerimento;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.ProcessoAtoConcedido;
import br.gov.ba.seia.entity.ProcessoReenquadramento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.ReservaLegal;
import br.gov.ba.seia.entity.StatusProcessoAto;
import br.gov.ba.seia.entity.StatusReservaLegal;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.AreaEnum;
import br.gov.ba.seia.enumerator.DadoOrigemEnum;
import br.gov.ba.seia.enumerator.ParametroEnum;
import br.gov.ba.seia.enumerator.PerguntaEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.StatusProcessoAtoEnum;
import br.gov.ba.seia.enumerator.StatusReservaLegalEnum;
import br.gov.ba.seia.exception.SeiaRuntimeException;
import br.gov.ba.seia.exception.SeiaValidacaoRuntimeException;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.ControleTramitacaoService;
import br.gov.ba.seia.service.DocumentoAtoAmbientalService;
import br.gov.ba.seia.service.DocumentoObrigatorioService;
import br.gov.ba.seia.service.EnquadramentoDocumentoAtoService;
import br.gov.ba.seia.service.EnquadramentoService;
import br.gov.ba.seia.service.FuncionarioService;
import br.gov.ba.seia.service.ParametroService;
import br.gov.ba.seia.service.PerguntaRequerimentoService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AnaliseTecnicaServiceFacade {
	
	@Inject
	private AreaDAOImpl areaDAOImpl;
	@EJB
	private AnaliseTecnicaDAOImpl analiseTecnicaDAOImpl;
	@EJB
	private EquipeDAOImpl equipeDAOImpl;
	@EJB
	private PautaDAOImpl pautaDAOImpl;
	@EJB
	private ProcessoAtoDAOImpl processoAtoDAOImpl;
	@EJB
	private ReservaLegalDAOImpl reservaLegalDAOImpl;
	@EJB
	private ControleProcessoAtoDAOImpl controleProcessoAtoDAOImpl;
	@EJB
	private ControleTramitacaoDAOImpl controleTramitacaoDAOImpl;
	@EJB
	private StatusProcessoAtoDAOImpl statusProcessoAtoDAOImpl;
	@EJB
	private ControleTramitacaoService controleTramitacaoService;
	@EJB
	private FceServiceFacade fceServiceFacade;
	@EJB
	private ParametroService parametroService;
	@EJB
	private DocumentoObrigatorioService documentoObrigatorioService;
	@EJB
	private PerguntaRequerimentoService perguntaRequerimentoService;
	@EJB
	private ProcessoAtoConcedidoDAOImpl processoAtoConcedidoDAOImpl;
	@EJB
	private EnquadramentoDocumentoAtoService enquadramentoDocumentoAtoService;
	@EJB
	private DocumentoAtoAmbientalService documentoAtoAmbientalService;
	@EJB
	private ProcessoReenquadramentoDAOImpl processoReenquadramentoDAOImpl; 
	@EJB
	private FuncionarioService funcionarioService;
	@EJB
	protected EnquadramentoService enquadramentoService;
	@EJB
	private AreaService areaService;
	
	private FuncionarioDAOImpl funcionarioDAOImpl;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Fce> listarFce(Integer ideProcesso, DadoOrigemEnum...listDadoOrigem) throws Exception {
		return fceServiceFacade.listarFce(ideProcesso, listDadoOrigem);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Fce> listarFceDoProcessoReenquadramento(Integer ideProcesso) throws Exception {
		return fceServiceFacade.listarFceDoProcessoReenquadramento(ideProcesso);
	}
	
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Collection<Fce> listarFce(AnaliseTecnica analiseTecnica) throws Exception {
		return fceServiceFacade.listarFce(analiseTecnica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Fce> listarFceParaASV(AnaliseTecnica analiseTecnica, Boolean indAprovado) throws Exception {
		return fceServiceFacade.listarFceParaASV(analiseTecnica, null);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<Fce> listarFceComExcluido(AnaliseTecnica analiseTecnica) throws Exception {
		return fceServiceFacade.listarFceComExcluido(analiseTecnica);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<StatusProcessoAto> listarStatusProcessoAto() throws Exception {
		return statusProcessoAtoDAOImpl.listaParaAnaliseTecnica();
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ProcessoAto> listarProcessoAtoPor(VwConsultaProcesso vwProcesso, SuperProcessoAnaliseTecnicaController controller) throws Exception {
		if(controller instanceof AprovarAnaliseTecnicaController) {
			return processoAtoDAOImpl.listarProcessoAtoPor(vwProcesso.getIdeProcesso(), null);
		} else if (controller instanceof ProcessoAnaliseTecnicaController){
			return processoAtoDAOImpl.listarProcessoAtoPor(vwProcesso.getIdeProcesso(), ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());
		}
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<PerguntaRequerimento> buscarPerguntaRequerimento(VwConsultaProcesso vwProcesso, PerguntaEnum perguntaEnum) throws Exception {
		return perguntaRequerimentoService.listarPerguntaRequerimentoByCodPerguntaAndIdeRequerimento(perguntaEnum.getCod(), vwProcesso.getIdeRequerimento());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<ReservaLegal> listarReservaLegalConcedida(ProcessoAto processoAto) throws Exception {
		return reservaLegalDAOImpl.listarReservaLegalConcedida(processoAto.getIdeProcessoAto()); 
	}
		
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAnaliseTecnica(AnaliseTecnica analiseTecnica) {
		analiseTecnicaDAOImpl.salvarAnaliseTecnica(analiseTecnica);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAnaliseTecnica(Collection<AnaliseAtoDTO> listaAnaliseAto) {
		boolean nenhumaSalva = true;
		for(AnaliseAtoDTO analise : listaAnaliseAto) {
			if(isAnaliseVinculadaAoTecnicoLogado(analise)) {
				ControleProcessoAto controleProcessoAto = analise.getControleProcessoAto();
				if(validarParecer(controleProcessoAto)){
					if(Util.isNullOuVazio(controleProcessoAto.getIdeProcessoAto())){
						controleProcessoAto.setIdeProcessoAto(analise.getProcessoAto());
					}
					if(isDeferido(controleProcessoAto)) {
						controleProcessoAto.setDscJustificativaStatus(null);
					}
					else {
						controleProcessoAto.setNumPrazoValidade(null);
						controleProcessoAto.setIndPrazoIndeterminado(null);
					}
					controleProcessoAto.setIdePessoaFisica(analise.getTecnico());
					try{
						controleProcessoAtoDAOImpl.salvar(controleProcessoAto);
						nenhumaSalva = false;
					}
					catch(Exception e) {
						Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
						throw Util.capturarException(e, Util.SEIA_EXCEPTION);
					}
				}
			}
		}
		if(nenhumaSalva){
			throw new SeiaValidacaoRuntimeException(Util.getString("analise_tecnica_msg_analise_sem_parecer"));
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void enviarAprovacaoAnaliseTecnica(AnaliseTecnica analiseTecnica, Collection<AprovacaoAnaliseAtoDTO> listaAprovacaoAnaliseAto, Integer ideProcesso, Integer idePessoaFisica) throws Exception {
		
		Area areaGestor = areaDAOImpl.buscarAreaCoordenadorAtual(ideProcesso);
		Pauta pautaGestor = pautaDAOImpl.buscarPautaGestorAtualProcesso(ideProcesso);
		
		if(analiseTecnica.getIndAprovado()) {
			if(isFluxoNOUT(ideProcesso) && isAreaAtualDiferenteAreaFormouEquipe(ideProcesso)) {
				
				controleTramitacaoService.salvarNovaTramitacao(
					ideProcesso,
					areaGestor.getIdeArea(),
					pautaGestor.getIdePauta(),
					StatusFluxoEnum.AGUARDANDO_APROVACAO_ANALISE_TECNICA,
					idePessoaFisica,
					null
				);
				analiseTecnica.setIndAprovado(null);
				analiseTecnicaDAOImpl.atualizarAnaliseTecnica(analiseTecnica);
			}
			else {
				salvarAprovacaoAnaliseTecnica(analiseTecnica, listaAprovacaoAnaliseAto, ideProcesso, idePessoaFisica);
				analiseTecnica.setDtcFimAnaliseProcesso(new Date());
				analiseTecnicaDAOImpl.atualizarAnaliseTecnica(analiseTecnica);
				controleTramitacaoService.salvarNovaTramitacao(
					ideProcesso,
					areaGestor.getIdeArea(),
					pautaGestor.getIdePauta(),
					StatusFluxoEnum.ANALISE_TECNICA_APROVADA,
					idePessoaFisica,
					null
				);
			}
			
		}
		else {
			salvarAprovacaoAnaliseTecnica(analiseTecnica, listaAprovacaoAnaliseAto, ideProcesso, idePessoaFisica);
			Collection<ControleTramitacao> listaControleTramitacaoTecnicos = controleTramitacaoDAOImpl.listarControleTramitacaoTecnicos(ideProcesso);
			controleTramitacaoDAOImpl.resetarTramitacaoPorProcesso(ideProcesso);
			Date dtcTramitacao = new Date();
			for(ControleTramitacao tramitacao : listaControleTramitacaoTecnicos) {
				ControleTramitacao novaTramitacao = tramitacao.clone();
				novaTramitacao.setIdeControleTramitacao(null);
				novaTramitacao.setIdePessoaFisica(new PessoaFisica(idePessoaFisica));
				novaTramitacao.setDtcTramitacao(dtcTramitacao);
				novaTramitacao.setDscComentarioExterno(null);
				novaTramitacao.setDscComentarioInterno(analiseTecnica.getObservacao());
				novaTramitacao.setIndFimDaFila(true);
				controleTramitacaoDAOImpl.salvar(novaTramitacao);
			}
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarAprovacaoAnaliseTecnica(AnaliseTecnica analiseTecnica, Collection<AprovacaoAnaliseAtoDTO> listaAprovacaoAnaliseAto, Integer ideProcesso, Integer idePessoaFisica) throws Exception {
		
		if(analiseTecnica.getIndAprovado()) {
			aprovarAnaliseTecnica(listaAprovacaoAnaliseAto, ideProcesso, idePessoaFisica);
		}
		else {
			reprovarAnaliseTecnica(listaAprovacaoAnaliseAto, ideProcesso, idePessoaFisica);
		}
		
		analiseTecnicaDAOImpl.atualizarAnaliseTecnica(analiseTecnica);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void aprovarAnaliseTecnica(Collection<AprovacaoAnaliseAtoDTO> listaAprovacaoAnaliseAto, Integer ideProcesso, Integer idePessoaFisica) {
		try{
			AprovacaoAnaliseAtoDTO analiseTecnico = carregarAnaliseTecnico(listaAprovacaoAnaliseAto);
			marcarDecisaoDoGestorNaAnaliseDoTecnico(analiseTecnico, true);
			atualizarDadosConcedidos(listaAprovacaoAnaliseAto);
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void atualizarDadosConcedidos(Collection<AprovacaoAnaliseAtoDTO> listaAprovacaoAnaliseAto) {
		try{
			for(AprovacaoAnaliseAtoDTO analiseAtoDTO : listaAprovacaoAnaliseAto) {
				Collection<ReservaLegal> listaReservaLegalConcedida = reservaLegalDAOImpl.listarReservaLegalConcedida(analiseAtoDTO.getProcessoAto().getIdeProcessoAto());
				for(ReservaLegal rl : listaReservaLegalConcedida) {
					rl.setIdeStatus(new StatusReservaLegal(StatusReservaLegalEnum.APROVADO_AGUARDANDO_CONCLUSAO_PROCESSO.getId()));
					reservaLegalDAOImpl.atualizar(rl);
				}
			}
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
		
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void reprovarAnaliseTecnica(Collection<AprovacaoAnaliseAtoDTO> listaAprovacaoAnaliseAto, Integer ideProcesso, Integer idePessoaFisica) {
		try{
			AprovacaoAnaliseAtoDTO analiseTecnico = carregarAnaliseTecnico(listaAprovacaoAnaliseAto);
			marcarDecisaoDoGestorNaAnaliseDoTecnico(analiseTecnico, false);
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private AprovacaoAnaliseAtoDTO carregarAnaliseTecnico(Collection<AprovacaoAnaliseAtoDTO> listaAprovacaoAnaliseAto) {
		
		AprovacaoAnaliseAtoDTO aprovacaoAnaliseAtoDTO = new AprovacaoAnaliseAtoDTO();
		aprovacaoAnaliseAtoDTO.setListaControleProcessoAto(new ArrayList<ControleProcessoAto>());
		for(AprovacaoAnaliseAtoDTO dto : listaAprovacaoAnaliseAto) {
			Collection<ControleProcessoAto> lista = dto.getListaControleProcessoAto();
			aprovacaoAnaliseAtoDTO.getListaControleProcessoAto().addAll(lista);
		}
		return aprovacaoAnaliseAtoDTO;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private AprovacaoAnaliseAtoDTO carregarAnaliseGestor(Integer ideProcesso) throws Exception {
		
		Collection<ControleProcessoAto> listaControleProcessoAto = controleProcessoAtoDAOImpl.listarControleProcessoAtoAprovacao(ideProcesso);
		
		AprovacaoAnaliseAtoDTO aprovacaoAnaliseAtoDTO = new AprovacaoAnaliseAtoDTO();
		aprovacaoAnaliseAtoDTO.setListaControleProcessoAto(listaControleProcessoAto);
		
		return aprovacaoAnaliseAtoDTO;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void marcarDecisaoDoGestorNaAnaliseDoTecnico(AprovacaoAnaliseAtoDTO analise, Boolean indAprovado) {
        for(ControleProcessoAto controleProcessoAto : analise.getListaControleProcessoAto()) {
            controleProcessoAto.setIndAprovado(indAprovado);
            
            if(Util.isNullOuVazio(controleProcessoAto.getDtcControleProcessoAto())) {
                controleProcessoAto.setIdeControleProcessoAto(null);
            }
            controleProcessoAtoDAOImpl.salvar(controleProcessoAto);
        }
    }
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void enviarAnaliseTecnica(Integer ideProcesso, Collection<AnaliseAtoDTO> listaAnaliseAto) {
		validarAnaliseTecnica(listaAnaliseAto);
		tramitarProcesso(ideProcesso,listaAnaliseAto);
	}
	
	private boolean isAnaliseVinculadaAoTecnicoLogado(AnaliseAtoDTO analise) {
		return analise.isDisabled() == false;
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void tramitarProcesso(Integer ideProcesso, Collection<AnaliseAtoDTO> listaAnaliseAto) {
		try {
			Area areaGestor = null;
			Pauta pautaGestor = null;
			Funcionario tecnico = retornarTecnico(listaAnaliseAto);
			if(isFluxoNOUT(ideProcesso)) {
				areaGestor = new Area(AreaEnum.NOUT.getId());
				pautaGestor = pautaDAOImpl.buscarPautaGestorPor(areaGestor.getIdeArea());
			}
			else {
				areaGestor = areaService.buscarUltimaAreaPreAnalise(ideProcesso);
				pautaGestor = pautaDAOImpl.buscarPautaGestorPor(areaGestor.getIdeArea());

				/* Retorna para o coordenador da área do técnico
				 * areaGestor =
				 * funcionarioService.buscarAreaFuncionario(tecnico.getIdePessoaFisica());
				 * pautaGestor = pautaDAOImpl.buscarPautaGestorPor(areaGestor.getIdeArea());
				 */
			}
			controleTramitacaoService.salvarNovaTramitacao(
				ideProcesso,
				areaGestor.getIdeArea(),
				pautaGestor.getIdePauta(),
				StatusFluxoEnum.AGUARDANDO_APROVACAO_ANALISE_TECNICA,
				tecnico.getIdePessoaFisica(),
				null
			);
		}
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private boolean isFluxoNOUT(Integer ideProcesso) throws Exception {
		
		boolean contains = false;
		
		Collection<ProcessoAto> listarProcessoAto = processoAtoDAOImpl.listarProcessoAtoPor(ideProcesso,null);
		
		if(listarProcessoAto.size() > 1) {
			Collection<AtoAmbiental> listaIdeAtoAmbientalFluxoNOUT = carregarListaIdeAtoAmbientalFluxoNOUT();
			
			for(ProcessoAto pa : listarProcessoAto) {
				AtoAmbiental atoAmbiental = pa.getAtoAmbiental();
				contains = listaIdeAtoAmbientalFluxoNOUT.contains(atoAmbiental);
				if(contains) {
					break;
				}
			}
		}
		
		return contains;
	}
	

	private boolean isAreaAtualDiferenteAreaFormouEquipe(Integer ideProcesso) throws Exception {
		Area areaAtual = controleTramitacaoDAOImpl.buscarUltimoPorProcesso(ideProcesso).getIdeArea();
		Area areaFormouEquipe = areaDAOImpl.buscarAreaCoordenadorAtual(ideProcesso);
		return !areaFormouEquipe.equals(areaAtual);
	}

	private Collection<AtoAmbiental> carregarListaIdeAtoAmbientalFluxoNOUT() throws Exception {
		Parametro parametro = parametroService.obterPorEnum(ParametroEnum.IDE_ATO_FLUXO_NOUT_ANALISE_TECNICA);
		String relacaoIdeAtoAmbientalFluxoNOUT = parametro.getDscValor().replace(" ","");
		
		Collection<AtoAmbiental> listaAtoAmbientalFluxoNOUT = new ArrayList<AtoAmbiental>();
		
		boolean contains = relacaoIdeAtoAmbientalFluxoNOUT.contains(",");
		
		if(contains) {
			int inicio = 0, fim;
			String ide;
			do {
				fim = relacaoIdeAtoAmbientalFluxoNOUT.indexOf(",", inicio);
				ide = relacaoIdeAtoAmbientalFluxoNOUT.substring(inicio, fim);
				listaAtoAmbientalFluxoNOUT.add(new AtoAmbiental(Integer.valueOf(ide)));
				inicio = fim+1;
			} while (inicio < relacaoIdeAtoAmbientalFluxoNOUT.lastIndexOf(","));
			ide = relacaoIdeAtoAmbientalFluxoNOUT.substring(inicio,relacaoIdeAtoAmbientalFluxoNOUT.length());
		}
		else {
			listaAtoAmbientalFluxoNOUT.add(new AtoAmbiental(Integer.valueOf(relacaoIdeAtoAmbientalFluxoNOUT)));
		}
		
		return listaAtoAmbientalFluxoNOUT;
	}

	private Funcionario retornarTecnico(Collection<AnaliseAtoDTO> listaAnaliseAto) {
		for(AnaliseAtoDTO analise : listaAnaliseAto) {
			if(isAnaliseVinculadaAoTecnicoLogado(analise)) {
				return analise.getTecnico();
			}
		}
		throw new SeiaRuntimeException(Util.getString("analise_tecnica_msg_erro_carregar_tecnico"));
	}
	
	private void validarAnaliseTecnica(Collection<AnaliseAtoDTO> listaAnaliseAto) {
		for(AnaliseAtoDTO analise : listaAnaliseAto) {
			ControleProcessoAto controleProcessoAto = analise.getControleProcessoAto();
			if(!validarParecer(controleProcessoAto)){
				throw new SeiaValidacaoRuntimeException(Util.getString("analise_tecnica_msg_analise_sem_parecer"));
			} else if(((isCampoPrazoNaoPreenchido(controleProcessoAto)
					|| isCampoPrazoIgualZero(controleProcessoAto)) && (controleProcessoAto.getIndPrazoIndeterminado() == null || !controleProcessoAto.getIndPrazoIndeterminado()))
					|| isCampoJustificativaNaoPreenchido(controleProcessoAto)) {
				throw new SeiaValidacaoRuntimeException(Util.getString("analise_tecnica_msg_analise_incompleta"));
			}
		} 
	}

	private boolean validarParecer(ControleProcessoAto controleProcessoAto){
		return !Util.isNull(controleProcessoAto) && !Util.isNull(controleProcessoAto.getIdeStatusProcessoAto());
	}

	private boolean isCampoJustificativaNaoPreenchido(ControleProcessoAto controleProcessoAto) {
		return isIndeferido(controleProcessoAto) && Util.isNullOuVazio(controleProcessoAto.getDscJustificativaStatus());
	}

	private boolean isCampoPrazoNaoPreenchido(ControleProcessoAto controleProcessoAto) {
		return isDeferido(controleProcessoAto) && Util.isNull(controleProcessoAto.getNumPrazoValidade());
	}
	
	private boolean isCampoPrazoIgualZero(ControleProcessoAto controleProcessoAto) {
		return isDeferido(controleProcessoAto) && Integer.valueOf(controleProcessoAto.getNumPrazoValidade()).equals(0);
	}

	private boolean isDeferido(ControleProcessoAto controleProcessoAto) {
		return new StatusProcessoAto(StatusProcessoAtoEnum.DEFERIDO_PELO_TECNICO.getId()).equals(controleProcessoAto.getIdeStatusProcessoAto());
	}
	
	private boolean isIndeferido(ControleProcessoAto controleProcessoAto) {
		return new StatusProcessoAto(StatusProcessoAtoEnum.INDEFERIDO_PELO_TECNICO.getId()).equals(controleProcessoAto.getIdeStatusProcessoAto());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AnaliseAtoDTO> listarAnaliseAtoDTO(Integer ideProcesso, Integer idePessoaFisica) throws Exception {
		
		Collection<ProcessoAto> listaProcessoAtoCompleto = processoAtoDAOImpl.listarProcessoAtoPor(ideProcesso,null);
		Collection<ProcessoAto> listaProcessoAtoPorTecnico = processoAtoDAOImpl.listarProcessoAtoPor(ideProcesso,idePessoaFisica);
		
		Collection<AnaliseAtoDTO> listaAnaliseAtoDTO = new ArrayList<AnaliseAtoDTO>();
		for(ProcessoAto pa : listaProcessoAtoCompleto) {
			Boolean funcionarioPodeAnalisarAto = listaProcessoAtoPorTecnico.contains(pa);
			ControleProcessoAto controleProcessoAto = null; 
					
			controleProcessoAto = controleProcessoAtoDAOImpl.buscarUltimoDeferidoOuIndeferidoPorProcessoAto(pa.getIdeProcessoAto());
			
			if(Util.isNull(controleProcessoAto)) {
				controleProcessoAto = new ControleProcessoAto();
				controleProcessoAto.setNumPrazoValidade(0);
			}
			
			AnaliseAtoDTO analiseAtoDTO = new AnaliseAtoDTO();
			analiseAtoDTO.setProcessoAto(pa);
			analiseAtoDTO.setControleProcessoAto(controleProcessoAto);
			analiseAtoDTO.setTecnico(new Funcionario(idePessoaFisica));
			analiseAtoDTO.setDisabled(funcionarioPodeAnalisarAto ? false : true);
			
			listaAnaliseAtoDTO.add(analiseAtoDTO);
		}
		
		return listaAnaliseAtoDTO;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean isAnaliseAprovadaPeloGestor(Integer ideProcesso) {
		try{
			AnaliseTecnica analiseTecnica = analiseTecnicaDAOImpl.buscarAnaliseTecnica(ideProcesso);
			
			if(!Util.isNull(analiseTecnica)) {
				return analiseTecnica.getIndAprovado() == null || analiseTecnica.getDtcFimAnaliseProcesso() == null ? false : analiseTecnica.getIndAprovado();
			}
			return false;
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Collection<AprovacaoAnaliseAtoDTO> listarAprovacaoAnaliseAtoDTO(Integer ideProcesso) throws Exception {
		
		Collection<ProcessoAto> listaProcessoAtoCompleto = processoAtoDAOImpl.listarProcessoAtoPor(ideProcesso,null);
		Collection<AprovacaoAnaliseAtoDTO> listaAprovacaoAnaliseAtoDTO = new ArrayList<AprovacaoAnaliseAtoDTO>();
		
		for(ProcessoAto pa : listaProcessoAtoCompleto) {
			Collection<ControleProcessoAto> listaControleProcessoAto = controleProcessoAtoDAOImpl.listarControleProcessoAtoAtualPor(pa.getIdeProcessoAto());
			AprovacaoAnaliseAtoDTO aprovacaoAnaliseAtoDTO = new AprovacaoAnaliseAtoDTO();
			aprovacaoAnaliseAtoDTO.setProcessoAto(pa);
			aprovacaoAnaliseAtoDTO.setListaControleProcessoAto(listaControleProcessoAto);
			listaAprovacaoAnaliseAtoDTO.add(aprovacaoAnaliseAtoDTO);
		}
		
		return listaAprovacaoAnaliseAtoDTO;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public AnaliseTecnica buscarAnaliseTecnica(Integer ideProcesso) throws Exception {
		return analiseTecnicaDAOImpl.buscarAnaliseTecnica(ideProcesso);
	}

	/**
	 * Método que vai separar os {@link ProcessoAto} que podem ser analisados por aquele TÃ©cnico.
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @param atos
	 * @return
	 * @throws Exception
	 * @since 14/03/2016
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DocumentoObrigatorio> listarDocumentoObrigatorioByProcessoAto(Collection<AnaliseAtoDTO> atos) throws Exception{
		List<Integer> listaProcessoAto = new ArrayList<Integer>();
		Funcionario tecnico = null;
		if (!Util.isNullOuVazio(atos)){
			for(AnaliseAtoDTO analiseAtoDTO : atos){
				if(!analiseAtoDTO.isDisabled()){
					listaProcessoAto.add(analiseAtoDTO.getProcessoAto().getIdeProcessoAto());
					if(Util.isNull(tecnico)){
						tecnico = analiseAtoDTO.getTecnico();
					}
				}
			}
		}
		return documentoObrigatorioService.listarDocumentoObrigatoriosByTecnicoProcessoAto(tecnico, listaProcessoAto);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<ProcessoAtoConcedido> listarProcessoAtoConcedido(ProcessoAto processoAto) throws Exception {
		return processoAtoConcedidoDAOImpl.listarProcessoAMC();
		
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public boolean existeProcessoAtoConcedido(ProcessoAto processoAto) throws Exception {
		return processoAtoConcedidoDAOImpl.existeProcessoAtoConcedido(processoAto.getIdeProcessoAto());
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void criarFceParaAnliseTecnica(ProcessoAnaliseTecnicaController processoAnaliseTecnicaController) throws Exception {
		processoAnaliseTecnicaController.duplicarFceParaAnliseTecnica();
	}
	
	public void carregarDocumentoAtoFce(List<Fce> fces) throws Exception{
		Enquadramento enquadramento = getEnquadramento(fces.get(0).getIdeRequerimento());
		for(Fce fce : fces){
			List<Enquadramento> enquadramentos = new ArrayList<Enquadramento>();
			ProcessoReenquadramento processoReenquadramento = processoReenquadramentoDAOImpl.obterProcessoReenquadramentoPorProcesso(fce.getIdeAnaliseTecnica().getIdeProcesso().getIdeProcesso());
			if(!Util.isNullOuVazio(processoReenquadramento)){
				Enquadramento pEnquadramento = enquadramentoService.buscarUltimoEnquadramentoPorProcessoReenquadramento(processoReenquadramento);
				if(!Util.isNullOuVazio(processoReenquadramento)){
					enquadramentos.add(pEnquadramento);
				}
			}
			enquadramentos.add(enquadramento);
			
			DocumentoAto documentoAto = enquadramentoDocumentoAtoService.buscarDocAtoByEnquadramentoAndDocumentoObrigatorio(enquadramentos, fce);
			fce.setDocumentoAto(documentoAto);
		}
	}
	
	private Enquadramento getEnquadramento(Requerimento requerimento) throws Exception{
		return enquadramentoService.buscarUltimoEnquadramentoRequerimento(requerimento);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void atualizarAnaliseTecnica(AnaliseTecnica analiseTecnica) {
		analiseTecnicaDAOImpl.atualizarAnaliseTecnica(analiseTecnica);
	}
}