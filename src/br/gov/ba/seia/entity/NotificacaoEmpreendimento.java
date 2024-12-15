
package br.gov.ba.seia.entity;

import java.io.Serializable;

public class NotificacaoEmpreendimento implements Serializable {
    

	private static final long serialVersionUID = -1144405524074578032L;
	
	private NotificacaoMotivoNotificacao notificacaoMotivoNotificacao;
	private Empreendimento empreendimento;
	private VwConsultaProcesso consultaProcesso;

	
	public NotificacaoEmpreendimento(NotificacaoMotivoNotificacao notificacaoMotivoNotificacao, Empreendimento empreendimento){
		this.notificacaoMotivoNotificacao = notificacaoMotivoNotificacao;
		this.empreendimento = empreendimento;
	}
	
	public NotificacaoEmpreendimento(NotificacaoMotivoNotificacao notificacaoMotivoNotificacao, VwConsultaProcesso consultaProcesso){
		this.notificacaoMotivoNotificacao = notificacaoMotivoNotificacao;
		this.consultaProcesso = consultaProcesso;
	}
	
	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}
	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}


	public NotificacaoMotivoNotificacao getNotificacaoMotivoNotificacao() {
		return notificacaoMotivoNotificacao;
	}


	public void setNotificacaoMotivoNotificacao(
			NotificacaoMotivoNotificacao notificacaoMotivoNotificacao) {
		this.notificacaoMotivoNotificacao = notificacaoMotivoNotificacao;
	}


	public VwConsultaProcesso getConsultaProcesso() {
		return consultaProcesso;
	}


	public void setConsultaProcesso(VwConsultaProcesso consultaProcesso) {
		this.consultaProcesso = consultaProcesso;
	}
}
