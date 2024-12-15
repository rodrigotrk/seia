package br.gov.ba.seia.facade;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.primefaces.event.FileUploadEvent;
import org.springframework.expression.ExpressionException;
import org.apache.log4j.Level;

import br.gov.ba.seia.controller.FluxoAlternativoController;
import br.gov.ba.seia.dao.ControleProcessoAtoDAOImpl;
import br.gov.ba.seia.dao.EnquadramentoAtoAmbientalDAOImpl;
import br.gov.ba.seia.dao.ProcessoAtoDAOImpl;
import br.gov.ba.seia.dao.ReenquadramentoProcessoAtoDAOImpl;
import br.gov.ba.seia.dao.ReservaLegalDAOImpl;
import br.gov.ba.seia.dto.ProcessoReenquadramentoDTO;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.CategoriaDocumento;
import br.gov.ba.seia.entity.ControleProcessoAto;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Enquadramento;
import br.gov.ba.seia.entity.EnquadramentoAtoAmbiental;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.Imovel;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.Pessoa;
import br.gov.ba.seia.entity.PessoaFisica;
import br.gov.ba.seia.entity.PessoaJuridica;
import br.gov.ba.seia.entity.PessoaJuridicaHistorico;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.ProcessoAto;
import br.gov.ba.seia.entity.ProcessoReenquadramento;
import br.gov.ba.seia.entity.ProcessoReenquadramentoHistAto;
import br.gov.ba.seia.entity.ReenquadramentoProcessoAto;
import br.gov.ba.seia.entity.ReservaLegal;
import br.gov.ba.seia.entity.SolicitacaoAdministrativo;
import br.gov.ba.seia.entity.StatusFluxo;
import br.gov.ba.seia.entity.StatusProcessoAto;
import br.gov.ba.seia.entity.StatusReenquadramento;
import br.gov.ba.seia.entity.StatusReservaLegal;
import br.gov.ba.seia.enumerator.AtoAmbientalEnum;
import br.gov.ba.seia.enumerator.CategoriaDocumentoEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.StatusProcessoAtoEnum;
import br.gov.ba.seia.enumerator.StatusReenquadramentoEnum;
import br.gov.ba.seia.enumerator.StatusReservaLegalEnum;
import br.gov.ba.seia.enumerator.TipoPautaEnum;
import br.gov.ba.seia.service.AreaService;
import br.gov.ba.seia.service.ArquivoProcessoService;
import br.gov.ba.seia.service.AuditoriaService;
import br.gov.ba.seia.service.ControleProcessoAtoService;
import br.gov.ba.seia.service.ControleTramitacaoService;
import br.gov.ba.seia.service.DocumentoObrigatorioService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.EnquadramentoService;
import br.gov.ba.seia.service.GerenciaArquivoService;
import br.gov.ba.seia.service.PautaService;
import br.gov.ba.seia.service.PessoaJuridicaHistoricoService;
import br.gov.ba.seia.service.PessoaJuridicaService;
import br.gov.ba.seia.service.ProcessoAtoService;
import br.gov.ba.seia.service.ProcessoService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.SolicitacaoAdministrativoService;
import br.gov.ba.seia.service.TransferenciaAmbientalService;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.RelatorioUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FluxoAlternativoFacade {

	private ControleTramitacao tramitacao;
	private ArquivoProcesso arquivo =null;
	private Pauta pautaArea;
	private ControleTramitacao tramitacaoAnterior;
	private ControleTramitacao controleTramitacao;
	
	
	private ProcessoReenquadramentoDTO processoReenquadramentoDTO;
	private String nomeFile;
	private FileUploadEvent event;
	
	@EJB
	private GerenciaArquivoService gerenciaArquivoService;
	
	@EJB
	private EmpreendimentoService empreendimentoService;
	
	@EJB
	private RequerimentoService requerimentoService;
	
	@EJB
	private ControleTramitacaoService controleTramitacaoService;
	
	@EJB 
	private PautaService pautaService;
	
	@EJB
	private AreaService areaService;
	
	@EJB
	private AuditoriaService auditoriaService;
	
	@EJB
	private ProcessoService processoService;
	
	@EJB
	private PessoaJuridicaService pessoaJuridicaService;
	
	@EJB
	private SolicitacaoAdministrativoService solicitacaoAdministrativoService;
	
	@EJB
	private TransferenciaAmbientalService transferenciaAmbientalService;
	
	@EJB
	private ProcessoAtoService processoAtoService;
	
	@EJB
	private ControleProcessoAtoService controleProcessoAtoService;
	
	@EJB
	private ControleProcessoAtoDAOImpl controleProcessoAtoDAOImpl;
	
	@EJB
	private ProcessoAtoDAOImpl processoAtoDAOImpl;
	
	@EJB
	private ReservaLegalDAOImpl reservaLegalDAOImpl;
	
	@EJB
	private EnquadramentoAtoAmbientalDAOImpl enquadramentoAtoAmbientalDAOImpl;
	
	@EJB
	private ReenquadramentoProcessoAtoDAOImpl reenquadramentoProcessoAtoDAOImpl;
	
	@EJB
	private TramitacaoReenquadramentoProcessoServiceFacade tramitacaoReenquadramentoProcessoServiceFacade;
	
	@EJB
	private DocumentoObrigatorioService documentoObrigatorioService;
	
	@EJB
	private EnquadramentoService enquadramentoService;
	
	@EJB
	private ProcessoReenquadramentoHistAtoServiceFacade processoReenquadramentoHistAtoServiceFacade;
	
	@EJB
	private ArquivoProcessoService arquivoProcessoService;
	
	@EJB
	private PessoaJuridicaHistoricoService pessoaJuridicaHistoricoService;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar (FluxoAlternativoController ctrl){

		ArquivoProcesso arquivo = null;

		if(Util.isNullOuVazio(ctrl.getOrgao()) && ctrl.getTipoFluxo().equals("3")) {
			JsfUtil.addErrorMessage("O campo orgão é de preenchimento obrigatório");
			
		} else if (Util.isNullOuVazio(ctrl.getArea()) && ctrl.getTipoFluxo().equals("3")) {
			JsfUtil.addErrorMessage("O campo Área Destino é de preenchimento obrigatório");
			
		} else {
			
			if(ctrl.getTemArquivo()) {
				arquivo = ctrl.montarArquivo();
			}
			
			if(ctrl.getPodeConcluir() && !ctrl.getTipoFluxo().equals("3")){
				
				try{
					
					Area area = null;
					ControleTramitacao tramitacaoAnterior = this.controleTramitacaoService.buscarUltimoPorProcesso(ctrl.getProcesso());
					area = tramitacaoAnterior.getIdeArea();
					
					tramitacaoAnterior.setIndFimDaFila(false);					
					
					StatusFluxo statusFluxo = null;			
					
					if(ctrl.getTipoFluxo().equals("1")){
						statusFluxo = new StatusFluxo(StatusFluxoEnum.CONCLUIDO.getStatus());
						atualizarStatusReservaLegal(ctrl);
					}
					else if (ctrl.getTipoFluxo().equals("2")) {
						statusFluxo = new StatusFluxo(StatusFluxoEnum.ARQUIVADO.getStatus());
					}
					
					Integer idePessoaFisicaLogada = ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica();
					
					ControleTramitacao ct = new ControleTramitacao();
					ct.setIdeProcesso(ctrl.getProcesso());
					ct.setIdeStatusFluxo(statusFluxo);
					ct.setIndFimDaFila(true);
					ct.setDtcTramitacao(new Date());
					ct.setIdeArea(area);
					ct.setDscComentarioInterno(ctrl.getObs());
					ct.setIndResponsavel(true);
					ct.setIdePessoaFisica(new PessoaFisica(idePessoaFisicaLogada));
					
					ct.setIdePauta(pautaService.obtemPautaCoordenadorArea(area.getIdeArea(), TipoPautaEnum.PAUTA_COORDENADOR_AREA));				
					if(ct.getIdePauta()==null){
						ct.setIdePauta(pautaService.obtemPautaCoordenadorArea(area.getIdeArea(), TipoPautaEnum.PAUTA_DIRETOR_AREA));
					}
					
					controleTramitacaoService.salvar(ct);
					controleTramitacaoService.atualizar(tramitacaoAnterior);
					
					ctrl.getProcesso().setAtosAmbientais(processoService.obterListaAtosAmbientais(ctrl.getProcesso().getIdeProcesso()));
				
					if (!Util.isNullOuVazio(ctrl.getProcesso().getAtosAmbientais())&& ctrl.getProcesso().getAtosAmbientais().iterator().next().getIdeAtoAmbiental().equals(AtoAmbientalEnum.ARLS.getId())) {
						
						Pessoa p = pessoaJuridicaService.carregarDadosRequerente(ctrl.getProcesso().getIdeRequerimento().getIdeRequerimento(), null);
						PessoaJuridica pj = pessoaJuridicaService.buscarPessoaJuridicaByIde(p.getIdePessoa());
						PessoaJuridicaHistorico pessoaJuridicaHistorico = new PessoaJuridicaHistorico(pj, ctrl.getProcesso());
						pessoaJuridicaHistorico.setIdeUsuarioAlteracao(ContextoUtil.getContexto().getUsuarioLogado());
						pessoaJuridicaHistoricoService.salvarPessoaJuridicaHistorico(pessoaJuridicaHistorico);
						SolicitacaoAdministrativo solicitAdm = solicitacaoAdministrativoService.buscarARLS(p, ctrl.getProcesso().getIdeRequerimento().getIdeRequerimento());
						pj.setNomRazaoSocial(solicitAdm.getNomRazaoSocialNova());
						pessoaJuridicaService.salvarOuAtualizarPessoaJuridica(pj);
					}else if(!Util.isNullOuVazio(ctrl.getProcesso().getAtosAmbientais()) && ctrl.getProcesso().getAtosAmbientais().iterator().next().getIdeAtoAmbiental().equals(AtoAmbientalEnum.TLA.getId())){
						atualizarProcessoCedente(ctrl.getProcesso());
					}
					
					
					salvarControleProcessoAto(ctrl.getProcesso());
					
					JsfUtil.addSuccessMessage("Processo concluído com sucesso!");
					
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					throw Util.capturarException(e,Util.SEIA_EXCEPTION);
				}
				
			} else {
				
				controleTramitacao = ctrl.montarObjetoTramitacao();
				
				try {
					
					if (!validaTramitacao(controleTramitacao, ctrl.isPautaArea())) {
						JsfUtil.addErrorMessage("Não é possível enviar para a área de origem");
						return;
					}
					
					if (ctrl.isReenquadramento()){
						salvarAtos(ctrl);
						
						Area areaSelecionada = areaService.carregar(controleTramitacao.getIdeArea().getIdeArea());
						Pauta pautaGestor = pautaService.obtemPautaPorIdeFuncionario(areaSelecionada.getIdePessoaFisica().getIdePessoaFisica());
						controleTramitacao.setIdePauta(pautaGestor);
						ControleTramitacao controleTramitacaoReenquadrado = controleTramitacao.clone();
						controleTramitacaoReenquadrado.setDscComentarioInterno(null);
						
						salvarTramitacaoAux(controleTramitacaoReenquadrado,new StatusFluxo(StatusFluxoEnum.PROCESSO_REENQUADRADO.getStatus()),false);
						tramitarReenquadramentoProcesso(ctrl, StatusReenquadramentoEnum.ENCAMINHADO_PARA_O_GESTOR);

						salvar(controleTramitacao, arquivo,ctrl.isPautaArea());	
						
						gerarResumoRequerimentoReenquadrado();
						
					}
					else {
						salvar(controleTramitacao, arquivo,ctrl.isPautaArea());					 
					}
					
					JsfUtil.addSuccessMessage("Salvo com Sucesso.");
					
					ctrl.init();
					ctrl.load(ctrl.isPautaArea(), ctrl.isPautaTecnico(),ctrl.getVwConsultaProcesso());
					
				} catch (Exception e) {
					JsfUtil.addErrorMessage(e.getMessage());
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
				} 
			}
		}			
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarAtos(FluxoAlternativoController ctrl) throws Exception {
		ProcessoReenquadramento processoReenquadramento = ctrl.getProcessoReenquadramentoDTO().getProcessoReenquadramento();
		
		//Pega o enquadramento do processo
		Enquadramento enquadramentoProcesso = enquadramentoService.buscarUltimoEnquadramentoPorProcessoReenquadramento(processoReenquadramento);
		
		//Pega os EnquadramentoAtoAmbiental do processo
		List<EnquadramentoAtoAmbiental> listEnquadramentoAtoAmbientalProcesso = enquadramentoAtoAmbientalDAOImpl.buscarEnquadramentoAtoAmbientalPorEnquadramento(enquadramentoProcesso);
		
		for (EnquadramentoAtoAmbiental enquadramentoAtoAmbiental : listEnquadramentoAtoAmbientalProcesso) {
			//cerrega o historico para verificar se é alteração
			ProcessoReenquadramentoHistAto processoReenquadramentoHistAto = processoReenquadramentoHistAtoServiceFacade.obterProcessoReenquadramentoHistAtoReenquadrado(processoReenquadramento, enquadramentoAtoAmbiental);
			enquadramentoAtoAmbiental.setProcessoReenquadramentoHistAtoTransient(processoReenquadramentoHistAto);
			
			ReenquadramentoProcessoAto reenquadramentoProcessoAto = enquadramentoService.isAlteracaoTipoAto(enquadramentoAtoAmbiental, processoReenquadramento.getIdeProcesso().getIdeProcesso());
			//caso retorne um valor o item é uma alteração
			ProcessoAto novoProcessoAto = null;
			if (!Util.isNullOuVazio(reenquadramentoProcessoAto)){
				ProcessoAto processoAto = reenquadramentoProcessoAto.getIdeProcessoAto();
				processoAto.setIndExcluido(true);
				processoAtoService.salvarOuAtualizar(processoAto);
			}else {
				novoProcessoAto = processoAtoService.getProcessoAtoByProcessoByAtoAmbiental(processoReenquadramento.getIdeProcesso(),enquadramentoAtoAmbiental.getAtoAmbiental(),null);
			}
			
			salvarProcessoAto(processoReenquadramento, enquadramentoAtoAmbiental, novoProcessoAto);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public ReenquadramentoProcessoAto obterReenquadramentoProcessoAtoOriginal(ProcessoReenquadramento processoReenquadramento, EnquadramentoAtoAmbiental enquadramentoAtoAmbiental) throws Exception{
		//Pega o enquadramento do processo
		Enquadramento enquadramentoProcesso = enquadramentoService.buscarUltimoEnquadramentoPorProcessoReenquadramento(processoReenquadramento);
		ReenquadramentoProcessoAto reenquadramentoProcessoAtoOriginal = null;
		
		if (!Util.isNullOuVazio(enquadramentoProcesso)){
			//Pega o enquadramento do requerimento
			Collection<ReenquadramentoProcessoAto> listaReenquadramentoProcessoAto = reenquadramentoProcessoAtoDAOImpl.listarReenquadramentoProcessoAtoPorEnquadramento(enquadramentoProcesso);
			
			for (ReenquadramentoProcessoAto reenquadramentoProcessoAto : listaReenquadramentoProcessoAto) {
				//Alteração
				if (!Util.isNullOuVazio(reenquadramentoProcessoAto.getIdeProcessoAto())){
					if (!Util.isNullOuVazio(reenquadramentoProcessoAto.getIdeProcessoAto().getTipologia())){
						if (reenquadramentoProcessoAto.getIdeProcessoAto().getTipologia().equals(enquadramentoAtoAmbiental.getTipologia()) &&
							reenquadramentoProcessoAto.getIdeProcessoAto().getAtoAmbiental().equals(enquadramentoAtoAmbiental.getAtoAmbiental())){
							reenquadramentoProcessoAtoOriginal = reenquadramentoProcessoAto;
						}
					} else {
						
						if (Util.isNullOuVazio(enquadramentoAtoAmbiental.getTipologia())){
							if (reenquadramentoProcessoAto.getIdeProcessoAto().getAtoAmbiental().equals(enquadramentoAtoAmbiental.getAtoAmbiental())){
								reenquadramentoProcessoAtoOriginal = reenquadramentoProcessoAto;
							}
						}
					}
				}
			}
		}
		
		return reenquadramentoProcessoAtoOriginal;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarProcessoAto(ProcessoReenquadramento processoReenquadramento, EnquadramentoAtoAmbiental enquadramentoAtoAmbiental, ProcessoAto pAto) throws Exception{
		
		if(Util.isNullOuVazio(pAto)){
			pAto = new ProcessoAto();
			pAto.setProcesso(processoReenquadramento.getIdeProcesso());
			pAto.setAtoAmbiental(enquadramentoAtoAmbiental.getAtoAmbiental());
		}
		
		pAto.setIndExcluido(false);
		pAto.setIdeProcessoReenquadramento(processoReenquadramento);
		
		if (!Util.isNullOuVazio(enquadramentoAtoAmbiental.getTipologia())){
			pAto.setTipologia(enquadramentoAtoAmbiental.getTipologia());
		}
		
		salvarProcessoAto(pAto, processoReenquadramento.getIdeProcesso(), processoReenquadramento);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private ProcessoAto buscarProcessoAtoLista(List<ProcessoAto> listaProcessoAto, Integer ideTipologia, Integer ideAtoAmbiental){
		for (ProcessoAto processoAto : listaProcessoAto) {
			if (!Util.isNullOuVazio(processoAto.getTipologia())){
				if (processoAto.getTipologia().getIdeTipologia().toString().equals(String.valueOf(ideTipologia)) && 
						processoAto.getAtoAmbiental().getIdeAtoAmbiental().toString().equals(String.valueOf(ideAtoAmbiental))){
					return processoAto;
				}
			} else {
				if (processoAto.getAtoAmbiental().getIdeAtoAmbiental().toString().equals(String.valueOf(ideAtoAmbiental))){
					return processoAto;
				}
			}
			
		}
		return null;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarProcessoAto(ProcessoAto processoAto, Processo processo, ProcessoReenquadramento ideProcessoReenquadramento) throws Exception{
		Integer ideProcessoAto = processoAto.getIdeProcessoAto();
		processoAtoService.salvarOuAtualizar(processoAto);
		
		if (Util.isNullOuVazio(ideProcessoAto)){
			ControleProcessoAto controleProcessoAto = gerarControleProcessoAto(processoAto);
			controleProcessoAtoService.salvarOuAtualizar(controleProcessoAto);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private ControleProcessoAto gerarControleProcessoAto(ProcessoAto processoAto){
		ControleProcessoAto controleProcessoAto = new ControleProcessoAto();
		
		controleProcessoAto.setDtcControleProcessoAto(new Date());
		controleProcessoAto.setIndAprovado(true);
		controleProcessoAto.setIdeStatusProcessoAto(new StatusProcessoAto(StatusProcessoAtoEnum.AGUARDANDO_ANALISE.getId()));
		controleProcessoAto.setIdePessoaFisica(new Funcionario(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
		
		controleProcessoAto.setIdeProcessoAto(processoAto);
		return controleProcessoAto;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void tramitarReenquadramentoProcesso(FluxoAlternativoController ctrl, StatusReenquadramentoEnum status) throws Exception{
		tramitacaoReenquadramentoProcessoServiceFacade.criarTramitacaoReenquadramentoProcessoSemFlush(
				ctrl.getProcessoReenquadramentoDTO().getProcessoReenquadramento(), 
				new StatusReenquadramento(status), 
				ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa()
		);
	}
		
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void atualizarStatusReservaLegal(FluxoAlternativoController ctrl) throws Exception {
		Collection<ProcessoAto> listaProcessoAto = processoAtoDAOImpl.listarProcessoAtoPor(ctrl.getProcesso().getIdeProcesso(), AtoAmbientalEnum.ARRL.getId(), null);
		Integer ideReservaLegalPaiAnterior = 0;
		List<Integer> listReservaLegal = new ArrayList<Integer>();
		boolean isBrothers = false;
		if(!Util.isNullOuVazio(listaProcessoAto)) {
			for(ProcessoAto pa : listaProcessoAto) {
				Collection<ReservaLegal> listaReservaLegalConcedida = reservaLegalDAOImpl.listarReservaLegalConcedida(pa.getIdeProcessoAto());
				for(ReservaLegal rlc :listaReservaLegalConcedida) {
			
					Integer ideReservaLegalPai = rlc.getIdeReservaLegalPai().getIdeReservaLegal();
					ReservaLegal rlp = reservaLegalDAOImpl.buscarReservaLegal(ideReservaLegalPai);
					//auditoriaService.salvar(rlp);
					
					if(ideReservaLegalPaiAnterior.equals(ideReservaLegalPai)) {
						isBrothers = true;
					}else {
						ideReservaLegalPaiAnterior = ideReservaLegalPai;
						isBrothers = false;
					}
					
					reservaLegalDAOImpl.removerTudo(rlc);
					
					rlc.setIdeReservaLegal(ideReservaLegalPai);
					rlc.setIdeImovelRural(rlp.getIdeImovelRural());
					rlc.setIdeReservaLegalPai(null);
					rlc.setIdeStatus(new StatusReservaLegal(StatusReservaLegalEnum.RELOCADA.getId()));
					auditoriaService.atualizar(rlp,rlc);
					
					//Se as reservas não tem o mesmo pai, então não precisa atualizar duas vezes a mesma reserva pai
					if(!isBrothers && !listReservaLegal.contains(ideReservaLegalPai)) {
						reservaLegalDAOImpl.salvarOuAtualizar(rlc);
						listReservaLegal.add(ideReservaLegalPai);
					}
				}
			}
		}			
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public  void atualizarProcessoCedente(Processo processo){
		 
		try{
			List<ProcessoAto> processoCedente = transferenciaAmbientalService.getProcessoAtoCedente(processo);
			
			for (ProcessoAto processoaAto : processoCedente) {
				ControleProcessoAto controleProcessoAto = new ControleProcessoAto();
				controleProcessoAto.setIdeStatusProcessoAto(new StatusProcessoAto(StatusProcessoAtoEnum.TRANSFERIDO.getId()));
				controleProcessoAto.setIdeProcessoAto(processoaAto);
				controleProcessoAto.setDtcControleProcessoAto(new Date());
				controleProcessoAto.setIndAprovado(true);
				controleProcessoAto.setIdePessoaFisica(new Funcionario(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
				
				controleProcessoAtoService.salvarOuAtualizar(controleProcessoAto);
			}
			
		}catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarControleProcessoAto(Processo processo){

		try {
			for (ProcessoAto processoAto : processoAtoService.listarAtosPorProcesso(processo.getIdeProcesso())) {
				ControleProcessoAto controleProcessoAto = controleProcessoAtoDAOImpl.buscarUltimoDeferidoOuIndeferidoPorProcessoAto(processoAto.getIdeProcessoAto());		
				
				if(!Util.isNull(controleProcessoAto)){
					
					controleProcessoAto.setIdeControleProcessoAto(null);
					
					if(controleProcessoAto.getIdeStatusProcessoAto().getIdeStatusProcessoAto().equals(StatusProcessoAtoEnum.INDEFERIDO_PELO_TECNICO.getId())){
						controleProcessoAto.setIdeStatusProcessoAto(new StatusProcessoAto(StatusProcessoAtoEnum.INDEFERIDO.getId()));
						
					} else if(controleProcessoAto.getIdeStatusProcessoAto().getIdeStatusProcessoAto().equals(StatusProcessoAtoEnum.DEFERIDO_PELO_TECNICO.getId())) {
						controleProcessoAto.setIdeStatusProcessoAto(new StatusProcessoAto(StatusProcessoAtoEnum.DEFERIDO.getId()));
					}
					
					controleProcessoAto.setIdeProcessoAto(processoAto);
					controleProcessoAto.setDtcControleProcessoAto(new Date());
					controleProcessoAto.setIdePessoaFisica(new Funcionario(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica()));
					controleProcessoAto.setIndAprovado(true);
					controleProcessoAtoService.salvarOuAtualizar(controleProcessoAto);
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}

	}
	
	
	//Se a origem do Processo for a mesma do destino, criar duas tramita��es.	
	//Se o processo encaminhado para a �rea s� possu�a uma tramita��o, e o status desta tramita��o � Formado,
	//o sistema deve tramitar novamente o processo para o status de Formado e o manter na �rea de destino previamente selecionada.
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvar(ControleTramitacao controleTramitacao,ArquivoProcesso arquivo,Boolean isPautaArea) throws Exception {
		this.tramitacao = controleTramitacao;
		this.pautaArea = getPautadaArea(this.tramitacao.getIdeArea());
		this.tramitacao.setIdeProcesso(controleTramitacao.getIdeProcesso());
		if(Util.isNullOuVazio(pautaArea)){
			throw new Exception("Pauta para área"+ this.tramitacao.getIdeArea().getNomArea() +" não encontrada.");
		}	
		pautaArea = getPautadaArea(pautaArea.getIdeArea());
		if(Util.isNullOuVazio(pautaArea.getIdeArea().getIdePessoaFisica())){
			throw new RuntimeException("A Pauta da Área selecionada não possui um responsável. Favor entrar em contato com a Central de Atendimento.");
			//throw new Exception(" A Pauta da Área selecionada não possui um responsável. Favor entrar em contato com a Central de Atendimento.");
		}
		montarTramitacao(arquivo);		
		//buscando tramitação no fim da fila
		ControleTramitacao tramitacaoAnterior = controleTramitacaoService.buscarUltimoPorProcesso(controleTramitacao.getIdeProcesso());
		List<ControleTramitacao> tramitacoesEmFimDeFila = controleTramitacaoService.retornarFinaisDeFilaPorProcessoPorArea(controleTramitacao.getIdeProcesso(), tramitacaoAnterior.getIdeArea());
		
		if(verificaTramitacaoProcesso(tramitacao.getIdeProcesso()) == 1 ){						
			//salvando uma nova tramitação
			salvarTramitacaoAux(tramitacao,new StatusFluxo(StatusFluxoEnum.ENCAMINHADO_PARA_AREA.getStatus()),true);						
			//removendo as transações antigas do fim da fila
			for(ControleTramitacao ct : tramitacoesEmFimDeFila){
				ct.setIndFimDaFila(false);
				
				controleTramitacaoService.atualizar(ct);
			}
		}
		else{
			Area areaSelecionada = areaService.carregar(tramitacao.getIdeArea().getIdeArea());
			Pauta pautaGestor = pautaService.obtemPautaPorIdeFuncionario(areaSelecionada.getIdePessoaFisica().getIdePessoaFisica());
//			if(pautaCoordenador!=null){
//				this.tramitacao.setIdePauta(pautaCoordenador);
//				//salvando um nova tramitação
//				salvarTramitacaoAux(this.tramitacao,new StatusFluxo(StatusFluxoEnum.ENCAMINHADO_PARA_O_GESTOR.getStatus()),true);
//			}
//			else{
//				Pauta pautaDiretor = pautaService.obtemPautaCoordenadorArea(this.tramitacao.getIdeArea().getIdeArea(), TipoPautaEnum.PAUTA_DIRETOR_AREA);
//				this.tramitacao.setIdePauta(pautaDiretor);
//				//salvando um nova tramitação			
//				salvarTramitacaoAux(this.tramitacao,new StatusFluxo(StatusFluxoEnum.ENCAMINHADO_PARA_O_GESTOR.getStatus()),true);	
//			}
			this.tramitacao.setIdePauta(pautaGestor);
			salvarTramitacaoAux(this.tramitacao,new StatusFluxo(StatusFluxoEnum.ENCAMINHADO_PARA_O_GESTOR.getStatus()),true);

			//removendo as transações antigas do fim da fila
			for(ControleTramitacao ct : tramitacoesEmFimDeFila){
				ct.setIndFimDaFila(false);
				controleTramitacaoService.atualizar(ct);
			}

		}
	}
	
	public void gerarResumoRequerimentoReenquadrado() throws Exception {
	
		Empreendimento empreendimento = empreendimentoService.buscarEmpreendimentoPorRequerimentoRetornandoImoveis(tramitacao.getIdeProcesso().getIdeRequerimento().getIdeRequerimento());
		List<Imovel> listaImovel = (List<Imovel>) empreendimento.getImovelCollection();
		ArquivoProcesso arquivoP= new ArquivoProcesso();
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ide_requerimento", tramitacao.getIdeProcesso().getIdeRequerimento().getIdeRequerimento());
		params.put("nom_tipo_imovel", listaImovel.isEmpty() ? "Não Identificado" : listaImovel.get(0).getIdeTipoImovel().getNomTipoImovel());
		
		RelatorioUtil lRelatorio = new RelatorioUtil("resumo_requerimento.jasper", params);
		
		String caminho = lRelatorio.salvar(
				RelatorioUtil.RELATORIO_PDF, 
				true, 
				this.requerimentoService.isRequerimentoAntigo(tramitacao.getIdeProcesso().getIdeRequerimento().getIdeRequerimento()), 
				"/opt/ARQUIVOS/PROCESSO/"+tramitacao.getIdeProcesso().getIdeProcesso()+"/",
				new SimpleDateFormat("yyyyMMddHHmm").format(new Date()) + ".pdf");
		
				
		String nomeArquivo = "resumo requerimento reenquadrado.pdf";
				
		Pessoa pessoa = ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica().getPessoa();
		 arquivoP.setDscArquivoProcesso("Resumo do requerimento gerado pelo reenquadramento");
		 arquivoP.setDscCaminhoArquivo(caminho);
		 arquivoP.setFileName(nomeArquivo);
		 arquivoP.setCategoriaDocumento(new CategoriaDocumento(CategoriaDocumentoEnum.RESUMO_DE_REQUERIMENTO_DE_REENQUADRAMENTO_DE_PROCESSO.getId()));
		 arquivoP.setDtcCriacao(new Date());
		 arquivoP.setIdePessoaUpload(pessoa);
		 arquivoP.setIdeProcesso(tramitacao.getIdeProcesso());
		 
		 arquivoProcessoService.salvar(arquivoP);
	}
		
	
	
	private int verificaTramitacaoProcesso(Processo ideProcesso) throws Exception {
		 Collection<ControleTramitacao> lista = controleTramitacaoService.listarControleTramitacaoPorIdeProcesso(ideProcesso.getIdeProcesso());
		 if(Util.isNullOuVazio(lista)){
			 return 0;
		 }
		 
		return lista.size();
	}

	private void montarTramitacao(ArquivoProcesso arquivo) {
		this.tramitacao.setIndResponsavel(false);	
		this.tramitacao.setIdePauta(pautaArea);
		this.tramitacao.setIndFimDaFila(true);
		this.arquivo  = arquivo;

		if(Util.isNullOuVazio(this.tramitacao.getDtcTramitacao( )) ) {
			this.tramitacao.setDtcTramitacao(new Date());
		}
	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private Area getAreaTramitacaoDoProcesso(Processo processo) {
		Exception erro =null;
		try {
			carregarUltimaTramitacao(processo);
			return tramitacaoAnterior.getIdeArea();
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return null;
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
	}
	
	private void carregarUltimaTramitacao(Processo processo) throws Exception {
		tramitacaoAnterior = controleTramitacaoService.buscarUltimoPorProcesso(processo);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private Pauta getPautadaArea(Area area) {
		Exception erro = null;
         try {
			return pautaService.obtemPautaArea(area);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
			return null;
		}finally{
			if(erro != null) throw Util.capturarException(erro,Util.SEIA_EXCEPTION); 
		}
		
	}
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void salvarTramitacaoAux(ControleTramitacao ctrlTramitacao,StatusFluxo status,Boolean fimDeFila) throws Exception {
		ControleTramitacao aux = new ControleTramitacao();
		aux.setDtcTramitacao(new Date());
		aux.setIdeProcesso(ctrlTramitacao.getIdeProcesso());
		aux.setIdeArea(ctrlTramitacao.getIdeArea());
		aux.setIndResponsavel(false);
		
		aux.setIndFimDaFila(fimDeFila);
		
		aux.setIdePauta(ctrlTramitacao.getIdePauta());
		aux.setIdeStatusFluxo(status);
		aux.setDscComentarioInterno(ctrlTramitacao.getDscComentarioInterno());
		aux.setIdePessoaFisica(ctrlTramitacao.getIdePessoaFisica());
		controleTramitacaoService.salvar(aux);
		
	}
	
	
	public Boolean validaTramitacao(ControleTramitacao ct,Boolean isPautaArea) throws Exception {
	   ControleTramitacao ctAnterior = controleTramitacaoService.buscarUltimoPorProcesso(ct.getIdeProcesso()); 	
	   
	   if(!Util.isNullOuVazio(ctAnterior) && isPautaArea && ctAnterior.getIdeArea().getIdeArea() == ct.getIdeArea().getIdeArea()){
		   return false;
	   }
	   
	   return true;
	}


	public ArquivoProcesso getArquivo() {
		return arquivo;
	}


	public void setArquivo(ArquivoProcesso arquivo) {
		this.arquivo = arquivo;
	}

	public FileUploadEvent getEvent() {
		return event;
	}

	public void setEvent(FileUploadEvent event) {
		this.event = event;
	}

	public String getNomeFile() {
		return nomeFile;
	}

	public void setNomeFile(String nomeFile) {
		this.nomeFile = nomeFile;
	}

	public ProcessoReenquadramentoDTO getProcessoReenquadramentoDTO() {
		return processoReenquadramentoDTO;
	}

	public void setProcessoReenquadramentoDTO(ProcessoReenquadramentoDTO processoReenquadramentoDTO) {
		this.processoReenquadramentoDTO = processoReenquadramentoDTO;
	}
}
