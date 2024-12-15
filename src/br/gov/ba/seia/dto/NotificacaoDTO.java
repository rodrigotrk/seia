package br.gov.ba.seia.dto;

import java.util.List;

import br.gov.ba.seia.entity.ArquivoProcesso;
import br.gov.ba.seia.entity.MotivoNotificacao;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.NotificacaoParcial;

public class NotificacaoDTO {

	private Boolean usuarioCoordenador;
	private Boolean notificacaoComArquivosApensados;
	private String observacao;
	private String email;
	private Notificacao notificacaoFinal;
	private List<MotivoNotificacao> listaMotivosNotificacao;
	private List<NotificacaoParcial> listaNotificacaoParcialtecnico;
	private List<ArquivoProcesso> listaDeDocumentosDaNotificacao;

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Notificacao getNotificacaoFinal() {
		return notificacaoFinal;
	}

	public void setNotificacaoFinal(Notificacao notificacaoFinal) {
		this.notificacaoFinal = notificacaoFinal;
	}

	public List<MotivoNotificacao> getListaMotivosNotificacao() {
		return listaMotivosNotificacao;
	}

	public void setListaMotivosNotificacao(
			List<MotivoNotificacao> listaMotivosNotificacao) {
		this.listaMotivosNotificacao = listaMotivosNotificacao;
	}

	public List<NotificacaoParcial> getListaNotificacaoParcialtecnico() {
		return listaNotificacaoParcialtecnico;
	}

	public void setListaNotificacaoParcialtecnico(
			List<NotificacaoParcial> listaNotificacaoParcialtecnico) {
		this.listaNotificacaoParcialtecnico = listaNotificacaoParcialtecnico;
	}

	public Boolean getNotificacaoComArquivosApensados() {
		return notificacaoComArquivosApensados;
	}

	public void setNotificacaoComArquivosApensados(
			Boolean notificacaoComArquivosApensados) {
		this.notificacaoComArquivosApensados = notificacaoComArquivosApensados;
	}

	public List<ArquivoProcesso> getListaDeDocumentosDaNotificacao() {
		return listaDeDocumentosDaNotificacao;
	}

	public void setListaDeDocumentosDaNotificacao(
			List<ArquivoProcesso> listaDeDocumentosDaNotificacao) {
		this.listaDeDocumentosDaNotificacao = listaDeDocumentosDaNotificacao;
	}

	public Boolean getUsuarioCoordenador() {
		return usuarioCoordenador;
	}

	public void setUsuarioCoordenador(Boolean usuarioCoordenador) {
		this.usuarioCoordenador = usuarioCoordenador;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}