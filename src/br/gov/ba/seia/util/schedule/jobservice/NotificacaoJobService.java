package br.gov.ba.seia.util.schedule.jobservice;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.NotificacaoDAOImpl;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.Processo;
import br.gov.ba.seia.entity.StatusFluxo;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.enumerator.TipoNotificacaoEnum;
import br.gov.ba.seia.service.AlertaService;
import br.gov.ba.seia.service.CalendarioService;
import br.gov.ba.seia.service.ControleTramitacaoService;
import br.gov.ba.seia.service.NotificacaoServiceFacade;
import br.gov.ba.seia.util.Util;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class NotificacaoJobService {
	
	
	@EJB
	private NotificacaoServiceFacade notificacaoService;
	@EJB
	private NotificacaoDAOImpl notificacaoDAOImpl;
	@EJB
	private ControleTramitacaoService controleTramitacaoService;
	@EJB
	private CalendarioService calendarioService;
	@EJB
	private AlertaService alertaService;
	
		
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void processaNotificacoesNaoRespondidas() throws Exception {
		List<Notificacao> lista = notificacaoDAOImpl.listarNotificacoesNaoRespondidas();
		for(Notificacao notificacao: lista){
			processaNotificacaoNaoRespondida(notificacao);
		}

		alertaService.criarAlertaPrazoNotificacao();
	}


	@TransactionAttribute(TransactionAttributeType.REQUIRED)
  	private void processaNotificacaoNaoRespondida(Notificacao notificacao) throws Exception {
		notificacao.setIndRetorno(false);
		notificacaoDAOImpl.atualizar(notificacao);
		tramitarNotificacaoNaoRespondida(notificacao);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	private void tramitarNotificacaoNaoRespondida(Notificacao notificacao) throws Exception {
		
		Processo processo = notificacao.getIdeProcesso();
		
		controleTramitacaoService.resetarTramitacoes(processo.getIdeProcesso());
		
		Integer qtdNotificacoesEmitidas = notificacaoDAOImpl.bucarQtdNotificacoesEmitidas(processo.getIdeProcesso(), TipoNotificacaoEnum.NOTIFICACAO_PRAZO);
		
		ControleTramitacao ctAnterior = null;
		
		ctAnterior = controleTramitacaoService.buscarUltimoPorProcesso(processo);

		if(!Util.isNullOuVazio(ctAnterior)) {
			ControleTramitacao tramitacao = new ControleTramitacao();
			tramitacao.setIdeArea(ctAnterior.getIdeArea());
			tramitacao.setIdeProcesso(ctAnterior.getIdeProcesso());
			tramitacao.setIdeStatusFluxo(new StatusFluxo(StatusFluxoEnum.NOTIFICACAO_NAO_RESPONDIDA.getStatus()));
			tramitacao.setIndFimDaFila(true);
			tramitacao.setDtcTramitacao(new Date());
			tramitacao.setIdePauta(ctAnterior.getIdePauta());
			
			controleTramitacaoService.salvar(tramitacao);
		}
		
	}

}
