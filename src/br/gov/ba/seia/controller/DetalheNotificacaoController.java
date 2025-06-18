package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.NotificacaoEmpreendimento;
import br.gov.ba.seia.entity.NotificacaoMotivoNotificacao;
import br.gov.ba.seia.enumerator.MotivoNotificacaoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.facade.DetalheNotificacaoServiceFacade;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("detalheNotificacaoController")
@ViewScoped
public class DetalheNotificacaoController {
	
	@EJB
	private DetalheNotificacaoServiceFacade detalheNotificacaoServiceFacade;

	private Notificacao notificacao;
	private Empreendimento empreendimento;
	private List<NotificacaoEmpreendimento> listaNotificacaoEmpreendimento; 
	

	public void visualizar(Notificacao notificacao) {
		try {
			this.listaNotificacaoEmpreendimento = new ArrayList<NotificacaoEmpreendimento>();
			this.notificacao = notificacao = detalheNotificacaoServiceFacade.carregarNotificacao(notificacao);
			this.empreendimento = detalheNotificacaoServiceFacade.carregarEmpreendimento(notificacao);
			listarNotificacaoEmpreendimento();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Não foi possí­vel realizar a sua solicitação.");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}

	private void listarNotificacaoEmpreendimento() {
		if(!Util.isNullOuVazio(this.notificacao)){
			if(!Util.isNullOuVazio(this.notificacao.getNotificacaoMotivoNotificacaoCollection())){
				for(NotificacaoMotivoNotificacao motivo: this.notificacao.getNotificacaoMotivoNotificacaoCollection()){
					if(!Util.isNullOuVazio(motivo.getIdeMotivoNotificacao())){
						if(motivo.getIdeMotivoNotificacao().getIdeMotivoNotificacao().equals(MotivoNotificacaoEnum.SHAPE.getId()) || motivo.getIdeMotivoNotificacao().getIdeMotivoNotificacao().equals(MotivoNotificacaoEnum.RELOCACAO_RESERVA_LEGAL.getId())){
							NotificacaoEmpreendimento ne = new NotificacaoEmpreendimento(motivo, empreendimento);
							this.listaNotificacaoEmpreendimento.add(ne);
						}
					}
				}
			}
		}
	}
	
	public boolean isExistePeloMenosUmImovel() {
		
		if(!Util.isNullOuVazio(notificacao) && !Util.isNullOuVazio(notificacao.getNotificacaoMotivoNotificacaoCollectionComShape())) {
			for (NotificacaoMotivoNotificacao nmn : notificacao.getNotificacaoMotivoNotificacaoCollectionComShape()) {
				if(!Util.isNullOuVazio(nmn.getMotivoNotificacaoImovelCollection())) return true; 
			}
		}
		
		return false;
	}

	public Notificacao getNotificacao() {
		return notificacao;
	}

	public void setNotificacao(Notificacao notificacao) {
		this.notificacao = notificacao;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}

	public List<NotificacaoEmpreendimento> getListaNotificacaoEmpreendimento() {
		return listaNotificacaoEmpreendimento;
	}

	public void setListaNotificacaoEmpreendimento(
			List<NotificacaoEmpreendimento> listaNotificacaoEmpreendimento) {
		this.listaNotificacaoEmpreendimento = listaNotificacaoEmpreendimento;
	}
}