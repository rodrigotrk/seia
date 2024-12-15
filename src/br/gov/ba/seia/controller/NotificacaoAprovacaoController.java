package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.dto.NotificacaoAprovacaoDTO;
import br.gov.ba.seia.entity.Area;
import br.gov.ba.seia.entity.ControleTramitacao;
import br.gov.ba.seia.entity.JustificativaRejeicao;
import br.gov.ba.seia.entity.MotivoEdicaoNotificacao;
import br.gov.ba.seia.entity.Notificacao;
import br.gov.ba.seia.entity.NotificacaoEmpreendimento;
import br.gov.ba.seia.entity.NotificacaoMotivoNotificacao;
import br.gov.ba.seia.entity.Pauta;
import br.gov.ba.seia.entity.StatusFluxo;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.enumerator.JustificativaRejeicaoEnum;
import br.gov.ba.seia.enumerator.MotivoNotificacaoEnum;
import br.gov.ba.seia.enumerator.StatusFluxoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.facade.NotificacaoAprovacaoServiceFacade;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.SessaoUtil;
import br.gov.ba.seia.util.Util;

@Named("notificacaoAprovacaoController")
@ViewScoped
public class NotificacaoAprovacaoController {

	@EJB
	private NotificacaoAprovacaoServiceFacade notificacaoAprovacaoServiceFacade;	
	
	private final Integer SELECIONE = 0;
	private final Integer APROVADO = 1;
	private final Integer NAO_APROVADO_E_CANCELADO = 2;
	private final Integer NAO_APROVADO_E_REJEITADO = 3;
	private final Integer EDITAR = 4;

	private NotificacaoAprovacaoDTO dto;
	private List<NotificacaoEmpreendimento> listaNotificacaoEmpreendimento;
	
	@PostConstruct
	public void init() {
		
	}
	
	public void load(VwConsultaProcesso vwProcesso, Pauta pautaGestor) {
		try{
			dto = notificacaoAprovacaoServiceFacade.carregarNotificacaoAprovacao(vwProcesso);
			dto.setSituacaoNotificacao(SELECIONE);
			this.listaNotificacaoEmpreendimento = new ArrayList<NotificacaoEmpreendimento>();
			listarNotificacaoEmpreendimento();
			carregarDocumentosNotificacao();
			carregarListaSituacaoNotificacao();
			carregarDadosGestor(pautaGestor);
			verificacoes(pautaGestor);
		}
		catch(Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private void listarNotificacaoEmpreendimento() {
		if(!Util.isNullOuVazio(this.dto)){
			if(!Util.isNullOuVazio(this.dto.getNotificacao().getNotificacaoMotivoNotificacaoCollection())){
				for(NotificacaoMotivoNotificacao motivo: this.dto.getNotificacao().getNotificacaoMotivoNotificacaoCollection()){
					if(!Util.isNullOuVazio(motivo.getIdeMotivoNotificacao())){
						if(motivo.getIdeMotivoNotificacao().getIdeMotivoNotificacao().equals(MotivoNotificacaoEnum.SHAPE.getId()) || motivo.getIdeMotivoNotificacao().getIdeMotivoNotificacao().equals(MotivoNotificacaoEnum.RELOCACAO_RESERVA_LEGAL.getId())){
							NotificacaoEmpreendimento ne = new NotificacaoEmpreendimento(motivo, this.dto.getVwProcesso());
							this.listaNotificacaoEmpreendimento.add(ne);
						}
					}
				}
			}
		}
	}
	
	public boolean isRenderedCorrecaoNotificacao() {
		return dto.getSituacaoNotificacao().equals(EDITAR); 
	}
	
	public boolean isRenderedJustificativas() {
		return (dto.getSituacaoNotificacao().equals(NAO_APROVADO_E_CANCELADO) || dto.getSituacaoNotificacao().equals(NAO_APROVADO_E_REJEITADO)); 
	}
	
	public boolean isRenderedOutraJustificativa() {
		if(!Util.isNullOuVazio(dto.getNotificacao().getJustificativaRejeicaoCollection())) {
			JustificativaRejeicao outros = new JustificativaRejeicao(JustificativaRejeicaoEnum.OUTROS.getCodigo());
			return dto.getNotificacao().getJustificativaRejeicaoCollection().contains(outros);
		}
		return false; 
	}
	
	public boolean isDisabledCorrecaoNotificacao() {
		if(Util.isNullOuVazio(dto.getNotificacao().getMotivoEdicaoNotificacaoCollection())) {
			return true;
		}
		return false; 
	}
	
	public boolean isDisabledJustificativas() {
		return dto.getSituacaoNotificacao().equals(NAO_APROVADO_E_CANCELADO); 
	}
	
	public boolean isDisabledBtnEnviar() {
		return dto.getSituacaoNotificacao().equals(EDITAR); 
	}
	
	private void carregarListaSituacaoNotificacao() {
		List<SelectItem> lSituacaoNotificacao = new ArrayList<SelectItem>();
		lSituacaoNotificacao.add(new SelectItem(APROVADO, "Aprovado"));
		lSituacaoNotificacao.add(new SelectItem(NAO_APROVADO_E_CANCELADO, "Cancelado"));
		lSituacaoNotificacao.add(new SelectItem(NAO_APROVADO_E_REJEITADO, "Para Revisão"));
		lSituacaoNotificacao.add(new SelectItem(EDITAR, "Editar notificação"));
		dto.setlSituacaoNotificacao(lSituacaoNotificacao);
	}

	private void carregarDadosGestor(Pauta pautaGestor) throws Exception {
		if(Util.isNull(pautaGestor)) {
			pautaGestor = carregarPautaGestor();
			dto.setAreaGestor(carregarAreaGestor());
		}
		else{
			dto.setAreaGestor(pautaGestor.getIdePessoaFisica().getIdeArea());
		}
		dto.setPautaGestor(pautaGestor);
	}

	private void verificacoes(Pauta pautaGestor) throws Exception {
		boolean pauta = false;
		
		if(!Util.isNullOuVazio(dto.getNotificacao().getIdePautaCriacao()) || !Util.isNullOuVazio(pautaGestor)){
			if(dto.getNotificacao().getIdePautaCriacao().equals(pautaGestor)){
				pauta = true;
			}
		}
		
		ControleTramitacao ultimaTramitacao = notificacaoAprovacaoServiceFacade.buscarUltimaTramitacao(dto.getVwProcesso());
		if(new StatusFluxo(StatusFluxoEnum.NOTIFICACAO_ENVIADA_PARA_REVISAO.getStatus()).equals(ultimaTramitacao.getIdeStatusFluxo()) || pauta) {
			dto.getlSituacaoNotificacao().remove(SELECIONE);
			dto.getlSituacaoNotificacao().remove(NAO_APROVADO_E_CANCELADO);
		}
		if(dto.getNotificacao().getMotivoEdicaoNotificacaoCollection().isEmpty()){
			dto.setSituacaoNotificacao(SELECIONE);
		}
		else{
			if(ultimaTramitacao.getIdeStatusFluxo().getIdeStatusFluxo()==StatusFluxoEnum.NOTIFICACAO_ALTERADA.getStatus()){
				dto.setSituacaoNotificacao(EDITAR);
			}
			else{
				dto.setSituacaoNotificacao(SELECIONE);
				dto.getNotificacao().setMotivoEdicaoNotificacaoCollection(new ArrayList<MotivoEdicaoNotificacao>());
			}
		}
	}

	private Pauta carregarPautaGestor() throws Exception {
		Integer ideFuncionarioLogado = ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica();
		Pauta pautaGestor = notificacaoAprovacaoServiceFacade.obterPautaPorIdeFuncionario(ideFuncionarioLogado);
		return pautaGestor;
	}
	
	private Area carregarAreaGestor() throws Exception {
		return notificacaoAprovacaoServiceFacade.obterAreaFuncionarioCoordenadorPorIdeFuncionario
				(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());
	}
	
	public void carregarDocumentosNotificacao() {
		LstDocumentosDaNotificacaoController lstDocumentosDaNotificacaoController = (LstDocumentosDaNotificacaoController) 
				SessaoUtil.recuperarManagedBean("#{lstDocumentosDaNotificacaoController}", LstDocumentosDaNotificacaoController.class);
		lstDocumentosDaNotificacaoController.setNotificacao(dto.getNotificacao());
		lstDocumentosDaNotificacaoController.carregarDocumentosDaNotificacao();
	}
	
	public void enviarNotificacaoAprovacao() {
		try {
			if(validarNotificacaoAprovacao()) {
				boolean isProcessoComAcompanhamento = isProcessoComAcompanhamento();
				montarNotificacao(dto.getNotificacao());
				
				if (dto.getSituacaoNotificacao().equals(APROVADO)) {
					notificacaoAprovacaoServiceFacade.aprovarNotificacao(dto.getNotificacao(), dto.getAreaGestor(), dto.getPautaGestor(), isProcessoComAcompanhamento, !Util.isNullOuVazio(dto.getVwProcesso().getDesEmail()) ? dto.getVwProcesso().getDesEmail() :dto.getVwProcesso().getDesEmailRequerimento());
					JsfUtil.addSuccessMessage("Notificação aprovada com sucesso.");
				}
				else if(dto.getSituacaoNotificacao().equals(NAO_APROVADO_E_CANCELADO) || dto.getSituacaoNotificacao().equals(NAO_APROVADO_E_REJEITADO)) {
					String msg = "";
					StatusFluxoEnum statusFluxoEnum = null;
					if(dto.getSituacaoNotificacao().equals(NAO_APROVADO_E_REJEITADO)){
						msg = Util.getString("notificao_msg_enviada_revisao");
						statusFluxoEnum = StatusFluxoEnum.NOTIFICACAO_ENVIADA_PARA_REVISAO;
					}
					else{
						msg = Util.getString("notificao_msg_cancelada");
						statusFluxoEnum  = StatusFluxoEnum.NOTIFICACAO_CANCELADA;
					}
					dto.getNotificacao().setDtcEnvio(null);
					notificacaoAprovacaoServiceFacade.reprovarOuCancelarNotificacao(dto.getNotificacao(), dto.getAreaGestor(), dto.getPautaGestor(), statusFluxoEnum, isProcessoComAcompanhamento);
					JsfUtil.addSuccessMessage(msg);
				}
			}
			RequestContext.getCurrentInstance().execute("atualizarPauta();");
			RequestContext.getCurrentInstance().execute("dialogAprovarNotificacao.hide();");
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			JsfUtil.addErrorMessage("Não foi possí­vel realizar a solicitação.");
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private boolean isProcessoComAcompanhamento(){
		try {
			Area areaDoGestor = dto.getVwProcesso().getArea();
			Area areaSecundaria = notificacaoAprovacaoServiceFacade.carregarAreaSecundaria(dto.getVwProcesso());	
			if(Util.isNull(areaSecundaria)) {
				return false;
			}
			else{
				return areaDoGestor.equals(areaSecundaria);
			}
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	private boolean validarNotificacaoAprovacao(){
		if(Util.isNullOuVazio(dto.getSituacaoNotificacao())|| !dto.getSituacaoNotificacao().equals(APROVADO) && !dto.getSituacaoNotificacao().equals(NAO_APROVADO_E_CANCELADO) &&!dto.getSituacaoNotificacao().equals(NAO_APROVADO_E_REJEITADO) ){
			JsfUtil.addErrorMessage("Campo obrigatÃ³rio: DecisÃ£o a ser tomada.");
			 return false;
		}
		if(isRenderedJustificativas()){
			if(Util.isNullOuVazio(dto.getNotificacao().getJustificativaRejeicaoCollection())){
				JsfUtil.addErrorMessage("Favor informar a Justificativa para a Reprovação da Notificação.");
				return false;
			}
		}
		if(isRenderedOutraJustificativa()){
			if(Util.isNullOuVazio(dto.getNotificacao().getDscOutroJustificativa())){
				JsfUtil.addErrorMessage("Favor informar a Justificativa para a Reprovação da Notificação");
				return false;
			}
		}
		return true;
	}
	
	private void montarNotificacao(Notificacao notificacao) throws Exception{
		
		if(dto.getSituacaoNotificacao().equals(APROVADO) ) {
			notificacao.setIndAprovado(true);			
			notificacao.setIndCancelado(false);
			notificacao.setIndRejeitado(false);
			notificacao.setDtcEnvio(new Date());
			if(!Util.isNull(notificacao.getQtdDiasValidade())) {
				notificacao.setDtcValidade(calculaValidadeDtcEnvio(notificacao));
			}
			notificacao.setIdePautaAprovacao(notificacaoAprovacaoServiceFacade.carregarPautaPorIdeProcessoFimFila(dto.getVwProcesso()));
		}
		else{
			dto.getNotificacao().setIndAprovado(false);
			dto.getNotificacao().setIndEnviadoAprovacao(false);
			dto.getNotificacao().setDtcReprovacao(new Date());
			if(dto.getSituacaoNotificacao() == NAO_APROVADO_E_CANCELADO) {
				dto.getNotificacao().setIndCancelado(true);
				dto.getNotificacao().setIndRejeitado(false);
			}
			else if(dto.getSituacaoNotificacao() == NAO_APROVADO_E_REJEITADO) {
				dto.getNotificacao().setIndCancelado(false);
				dto.getNotificacao().setIndRejeitado(true);
			}
		}
	}
	
	public Date calculaValidadeDtcEnvio(Notificacao notificacao) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(notificacao.getDtcEnvio());
		calendar.add(Calendar.DAY_OF_MONTH, notificacao.getQtdDiasValidade());
		return calendar.getTime();
	}
	
	public void selecionaSituacaoNotificacao(ValueChangeEvent event){
		Integer itemSelected = (Integer)event.getNewValue();
		if(itemSelected.equals(NAO_APROVADO_E_CANCELADO)){
			ArrayList<JustificativaRejeicao> ar = new ArrayList<JustificativaRejeicao>();
			ar.add(new JustificativaRejeicao(JustificativaRejeicaoEnum.OUTROS.getCodigo()));
			dto.getNotificacao().setJustificativaRejeicaoCollection(ar);
		}
		else {
			dto.getNotificacao().setJustificativaRejeicaoCollection(new ArrayList<JustificativaRejeicao>());
		}
    }
	
	public void salvarCorrecaoNotificacao() {
		try {
			notificacaoAprovacaoServiceFacade.salvarCorrecaoNotificacao(dto);
			dto.setSituacaoNotificacao(APROVADO);
			JsfUtil.addSuccessMessage("A correção foi salva com sucesso.");
		} 
		catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}		
	}

	public NotificacaoAprovacaoDTO getDto() {
		return dto;
	}

	public void setDto(NotificacaoAprovacaoDTO dto) {
		this.dto = dto;
	}

	public List<NotificacaoEmpreendimento> getListaNotificacaoEmpreendimento() {
		return listaNotificacaoEmpreendimento;
	}

	public void setListaNotificacaoEmpreendimento(
			List<NotificacaoEmpreendimento> listaNotificacaoEmpreendimento) {
		this.listaNotificacaoEmpreendimento = listaNotificacaoEmpreendimento;
	}
}