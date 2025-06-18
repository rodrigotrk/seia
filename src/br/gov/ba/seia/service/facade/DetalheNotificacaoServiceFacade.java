package br.gov.ba.seia.service.facade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.gov.ba.seia.dao.NotificacaoDAOImpl;
import br.gov.ba.seia.dao.NotificacaoMotivoNotificacaoDAOImpl;
import br.gov.ba.seia.dao.NotificacaoParcialDAOImpl;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.JustificativaRejeicaoService;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DetalheNotificacaoServiceFacade {
	
	@EJB
	private NotificacaoDAOImpl notificacaoDAOImpl;
	@EJB
	private NotificacaoParcialDAOImpl notificacaoParcialDAOImpl;
	@EJB
	private NotificacaoMotivoNotificacaoDAOImpl notificacaoMotivoNotificacaoDAOImpl;
	@EJB
	private EmpreendimentoService empreendimentoService;
	@EJB
	private JustificativaRejeicaoService justificativaRejeicaoService;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Notificacao carregarNotificacao(Notificacao notificacao) throws Exception {
		Integer ideNotificacao = notificacao.getIdeNotificacao();
		notificacao = notificacaoDAOImpl.carregarById(ideNotificacao);
		notificacao.setNotificacaoParcialCollection(notificacaoParcialDAOImpl.listarNotificacaoParcialPorNotificacaoFinal(ideNotificacao));
		notificacao.setNotificacaoMotivoNotificacaoCollection(notificacaoMotivoNotificacaoDAOImpl.listarNotificacaoMotivoNotificacaoPor(notificacao));
		notificacao.setJustificativaRejeicaoCollection(justificativaRejeicaoService.listarByIdeNotificacao(ideNotificacao));
		return notificacao;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Empreendimento carregarEmpreendimento(Notificacao notificacao) throws Exception {
		Integer ideRequerimento = notificacao.getIdeProcesso().getIdeRequerimento().getIdeRequerimento();
		return empreendimentoService.carregarByIdeRequerimento(ideRequerimento);
	}
	
}