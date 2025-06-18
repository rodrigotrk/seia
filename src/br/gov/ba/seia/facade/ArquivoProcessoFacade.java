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

import br.gov.ba.seia.dao.ControleTramitacaoDAOImpl;
import br.gov.ba.seia.dao.NotificacaoDAOImpl;
import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.StatusFluxo;
import br.gov.ba.seia.enumerator.ParametroEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.TipoPautaEnum;
import br.gov.ba.seia.exception.SeiaRuntimeException;
import br.gov.ba.seia.service.ArquivoProcessoService;
import br.gov.ba.seia.service.ControleTramitacaoService;
import br.gov.ba.seia.service.NotificacaoServiceFacade;
import br.gov.ba.seia.service.ParametroService;
import br.gov.ba.seia.service.PautaService;
import br.gov.ba.seia.util.ContextoUtil;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ArquivoProcessoFacade {
	
	@EJB
	private ArquivoProcessoService arquivoProcessoService;
	
	@EJB
	private NotificacaoServiceFacade notificacaoServiceFacade;
	
	@EJB
	private NotificacaoDAOImpl notificacaoDAOImpl;
	
	@EJB
	private ControleTramitacaoService controleTramitacaoService;
	
	@EJB
	private PautaService pautaService;

	@EJB
	private ParametroService parametroService;
	
	@EJB
	private ControleTramitacaoDAOImpl controleTramitacaoDAOImpl;
	
	private Notificacao notificacao;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void salvarComNotificacao(List<ArquivoProcesso>listaArquivos,Notificacao notificacao) throws Exception{
		try {
			
			if(listaArquivos!=null && !listaArquivos.isEmpty()){
				List<ArquivoProcesso> listaDeArquivosAindaNaoSalvos = new ArrayList<ArquivoProcesso>();
				
				for(ArquivoProcesso arq : listaArquivos){
					if(arq.getIdeArquivoProcesso()==null) listaDeArquivosAindaNaoSalvos.add(arq);
				}
				
				if(!listaDeArquivosAindaNaoSalvos.isEmpty()) arquivoProcessoService.salvarListaArquivos(listaDeArquivosAindaNaoSalvos);
			}
			
			this.notificacao = notificacao;
			this.notificacao.setIndRetorno(true);
			
			notificacao.setDtcResposta(new Date());
			notificacaoDAOImpl.atualizar(notificacao);
			
			tramitarNotificacao();
		} catch (Exception e) {
			throw new SeiaRuntimeException("Erro ao enviar o arquivo");
		}
	}
	
	//e tramitar o processo para o status Notificacao Respondida e na Pauta da area.
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void tramitarNotificacao() throws Exception {
		if(!membroTemLimiteProcessos(this.notificacao.getIdeProcesso())) {
			//O processo devera retornar para Pauta de todos os Tecnicos e do Lider.
			//O status de tramitacao do Processo devera mudar de 'Notificacao Respondida', para 'Analise Tecnica'.
			processaNotificacaoRequerente(this.notificacao.getIdeProcesso());
		} else {
			ControleTramitacao ctAnterior =  controleTramitacaoService.buscarUltimoPorProcessoComTresStatus(this.notificacao.getIdeProcesso());
			
			ControleTramitacao ct = new ControleTramitacao();
			
			ct.setIdeProcesso(this.notificacao.getIdeProcesso());
			ct.setIdeArea(ctAnterior.getIdeArea());
			ct.setIdePauta(ctAnterior.getIdePauta());
			ct.setIdeStatusFluxo(new StatusFluxo(StatusFluxoEnum.NOTIFICACAO_RESPONDIDA.getStatus()));
			ct.setIdePessoaFisica(ContextoUtil.getContexto().getUsuarioLogado().getPessoaFisica());
			ct.setDtcTramitacao(new Date());
			ct.setIndFimDaFila(true);
			
			ctAnterior.setIndFimDaFila(false);
			
			controleTramitacaoService.salvar(ct);
			controleTramitacaoService.atualizar(ctAnterior);
		}		
	}
	
	//Percorre os membos da equipe e verifica se alguem tem mais que o maximo de processos permitidos associados
	//para saber o processo associado basta pegar no controle_tramitacao ind_fim_da_fila = true AND ide_pauta = pauta_funcioinario
	public Boolean membroTemLimiteProcessos(Processo processo) {
		List<ControleTramitacao>lista;
		ControleTramitacao ctAnterior =  controleTramitacaoService.buscarUltimoPorProcesso(processo);
		List<Funcionario> funcionarios = notificacaoServiceFacade.listarFuncionariosEquipeProcesso(processo, ctAnterior.getIdeArea());
		
		for (Funcionario funcionario : funcionarios) {
			ControleTramitacao ct= new ControleTramitacao();
			ct.setIndFimDaFila(true);
			ct.setIdePauta(pautaService.obtemPautaPorIdeFuncionario(funcionario.getIdePessoaFisica(),TipoPautaEnum.PAUTA_TECNICA.getTipo()));
			lista = controleTramitacaoService.listarPorExemplo(ct);
			if(lista.size() >= getValorMaxProcessosPautaTecnica()) {
				return true;
			}
		}
		
		return false;
	}
	
	private Integer getValorMaxProcessosPautaTecnica()  {
		String valor = parametroService.obterPorId(ParametroEnum.MAX_PROCESSOS_PAUTA_TECNICA.getIdeParametro()).getDscValor();
		return Integer.parseInt(valor);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void processaNotificacaoRequerente(Processo pProcesso) throws Exception {
		ControleTramitacao ctAnterior =  controleTramitacaoService.buscarUltimoPorProcessoComTresStatus(pProcesso);
		//tramitação notificacao respondida
		ControleTramitacao ctNotificacaoRespondida = new ControleTramitacao();
		ctNotificacaoRespondida.setIdeProcesso(pProcesso);
		ctNotificacaoRespondida.setIdeStatusFluxo(new StatusFluxo(StatusFluxoEnum.NOTIFICACAO_RESPONDIDA.getStatus()));
		ctNotificacaoRespondida.setIndFimDaFila(true);
		ctNotificacaoRespondida.setIdeArea(ctAnterior.getIdeArea());
		ctNotificacaoRespondida.setDtcTramitacao(new Date());
		ctNotificacaoRespondida.setIdePauta(ctAnterior.getIdePauta());
		ctNotificacaoRespondida.setIdePessoaFisica(null);
		
		controleTramitacaoService.salvar(ctNotificacaoRespondida);
		
		Integer ideProcesso = pProcesso.getIdeProcesso();
		//ControleTramitacao ctAnteriorAnaliseTecnica =  controleTramitacaoService.buscarUltimoPorProcessoAnaliseTecnica(pProcesso);
		
		Collection<ControleTramitacao> listaControleTramitacaoTecnicos = controleTramitacaoDAOImpl.listarControleTramitacaoTecnicosArea(ideProcesso,ctAnterior.getIdeArea().getIdeArea());
		controleTramitacaoDAOImpl.resetarTramitacaoPorProcesso(ideProcesso);
		Date dtcTramitacao = new Date();
		for(ControleTramitacao tramitacao : listaControleTramitacaoTecnicos) {
			ControleTramitacao novaTramitacao = tramitacao.clone();
			novaTramitacao.setIdeControleTramitacao(null);
			novaTramitacao.setIdePessoaFisica(null);
			novaTramitacao.setDtcTramitacao(dtcTramitacao);
			novaTramitacao.setDscComentarioExterno(null);
			novaTramitacao.setDscComentarioInterno(null);
			novaTramitacao.setIndFimDaFila(true);
			controleTramitacaoDAOImpl.salvar(novaTramitacao);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private Pauta obterPautaTecnicaPorFuncionario(Funcionario funcionario) throws Exception{
		return pautaService.obtemPautaPorIdeFuncionario(funcionario.getIdePessoaFisica(),TipoPautaEnum.PAUTA_TECNICA.getTipo());
	}
	
}
