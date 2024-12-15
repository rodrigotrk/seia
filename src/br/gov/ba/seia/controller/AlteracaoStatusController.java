package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.BoletoDaeRequerimento;
import br.gov.ba.seia.entity.BoletoPagamentoRequerimento;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.RequerimentoPessoa;
import br.gov.ba.seia.entity.StatusRequerimento;
import br.gov.ba.seia.entity.TramitacaoRequerimento;
import br.gov.ba.seia.enumerator.StatusRequerimentoEnum;
import br.gov.ba.seia.enumerator.TipoPessoaRequerimentoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AlteracaoStatusService;
import br.gov.ba.seia.service.BoletoDaeRequerimentoService;
import br.gov.ba.seia.service.BoletoPagamentoRequerimentoService;
import br.gov.ba.seia.service.FceService;
import br.gov.ba.seia.service.LacService;
import br.gov.ba.seia.service.RequerimentoService;
import br.gov.ba.seia.service.StatusRequerimentoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * Controller da tela "Alteração de Status"
 *
 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
 * @since 17/03/2014
 */
@Named("alteracaoStatusController")
@ViewScoped
public class AlteracaoStatusController {

	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");

	//SERVICES
	@EJB
	private RequerimentoService requerimentoService;

	@EJB
	private StatusRequerimentoService statusRequerimentoService;

	@EJB
	private BoletoPagamentoRequerimentoService boletoPagamentoRequerimentoService;

	@EJB
	private BoletoDaeRequerimentoService boletoDaeRequerimentoService;

	@EJB
	private AlteracaoStatusService alteracaoStatusService;

	@EJB
	private FceService fceService;
	@EJB
	private LacService lacService;

	//OBJETOS
	private Requerimento requerimento;

	private boolean renderizaPanelDados;

	private boolean existeBoleto;

	private String mensagemErroStatus;

	private List<SelectItem> status;

	private StatusRequerimento statusRequerimento;

	private String tituloPopup;

	private StatusRequerimento statusRequerimentoAntigo;

	
	/**
	 * Consulta o requerimento e prepara a tela para ser exibida.
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 */
	public void consultar() {
		try {

			requerimento = requerimentoService.buscarEntidadeCarregadaPorId(requerimento);

			if(!Util.isNullOuVazio(requerimento) && !Util.isNullOuVazio(requerimento.getTramitacaoRequerimentoCollection())) {

				if(validaStatus(requerimento)) {
					requerimento = setaRequerente(requerimento);
					existeBoleto = verificaExistenciaBoleto(requerimento);
					statusRequerimento = new StatusRequerimento();
					renderizaPanelDados = true;
				} else {
					renderizaPanelDados = false;
					JsfUtil.addErrorMessage(BUNDLE.getString("alteracao_status_msg_erro_status") + "\"" + mensagemErroStatus + "\".");
				}
			} else {
				renderizaPanelDados = false;
				JsfUtil.addErrorMessage(BUNDLE.getString("geral_msg_requerimento_nao_encontrado"));
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(BUNDLE.getString("alteracao_status_msg_erro_requerimento"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	/**
	 * Seta o requerente o obtendo da lista de {@link RequerimentoPessoa}
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 */
	private Requerimento setaRequerente(Requerimento req) {
		if(!Util.isNullOuVazio(req) && !Util.isNullOuVazio(req.getRequerimentoPessoaCollection())) {
			for (RequerimentoPessoa pessoa : req.getRequerimentoPessoaCollection()) {
				if (pessoa.getIdeTipoPessoaRequerimento().getIdeTipoPessoaRequerimento().equals(TipoPessoaRequerimentoEnum.REQUERENTE.getId())) {

					req.setRequerente(pessoa.getPessoa());

					if(!Util.isNull(pessoa.getPessoa().getPessoaFisica())) {
						String cpf = Util.format("###.###.###-##", pessoa.getPessoa().getPessoaFisica().getNumCpf());
						req.getRequerente().getPessoaFisica().setNumCpf(cpf);
					} else if(!Util.isNull(pessoa.getPessoa().getPessoaJuridica())) {
						String cnpj = Util.format("##.###.###/####-##", pessoa.getPessoa().getPessoaJuridica().getNumCnpj());
						req.getRequerente().getPessoaJuridica().setNumCnpj(cnpj);
					}
				}
			}
		}

		return req;
	}

	/**
	 * Método que verifica se o status do requerimento é igual a: AGUARDANDO ENQUADRAMENTO, CANCELADO ou PROCESSO FORMADO.
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param req - {@link Requerimento} com a collection de {@link TramitacaoRequerimento} já carregada
	 * @return {@link Boolean} FALSE caso o status seja igual a um dos citados acima.
	 */
	public boolean validaStatus(Requerimento req) {

		int x=0;
		boolean retorno = true;

		//OBTEM O ULTIMO STATUS
		for (TramitacaoRequerimento tram : req.getTramitacaoRequerimentoCollection()) {
			if(x < tram.getIdeTramitacaoRequerimento()) {
				x = tram.getIdeTramitacaoRequerimento();
				statusRequerimentoAntigo = tram.getIdeStatusRequerimento();
			}
		}
		// Correção #6554
		if(isImpossivelVoltarStatus(statusRequerimentoAntigo)){
			mensagemErroStatus = statusRequerimentoAntigo.getNomStatusRequerimento();
			retorno = false;
		} else {
			carregaComboStatus();
		}
		return retorno;
	}

	/**
	 * Carrega o combo de {@link StatusRequerimento}.
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 */
	private void carregaComboStatus() {

		status = new ArrayList<SelectItem>();
		StatusRequerimento st = new StatusRequerimento();

		try {
			status.add(new SelectItem(StringUtils.EMPTY, BUNDLE.getString("geral_lbl_selecione")));

			st = statusRequerimentoService.carregarGet(StatusRequerimentoEnum.AGUARDANDO_ENQUADRAMENTO.getStatus());
			status.add(new SelectItem(st, st.getNomStatusRequerimento()));
			// Correção #6554
			if(!isStatusAindaNaoValidado(statusRequerimentoAntigo) && !isStatusValidado(statusRequerimentoAntigo)) {
				st = statusRequerimentoService.carregarGet(StatusRequerimentoEnum.VALIDADO.getStatus());
				status.add(new SelectItem(st, st.getNomStatusRequerimento()));
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível carregar a lista de status.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	/**
	 * Verifica se o {@link Requerimento} possui algum {@link BoletoPagamentoRequerimento} ou {@link BoletoDaeRequerimento}
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 * @param req
	 * @return
	 */
	private boolean verificaExistenciaBoleto(Requerimento req) {
		try {

			if(!Util.isNullOuVazio(boletoPagamentoRequerimentoService.carregarByRequerimento(req.getIdeRequerimento(), false))
					|| !Util.isNullOuVazio(boletoDaeRequerimentoService.listarByRequerimento(req.getIdeRequerimento()))) {

				tituloPopup = "O requerimento já tem um boleto gerado. Deseja remover o boleto e confirmar a alteração deste requerimento?";
				return true;
			} else {
				tituloPopup = "Deseja confirmar a alteração de status deste requerimento?";
				return false;
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Não foi possível carregar a lista de boletos.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			return false;
		}
	}

	/**
	 * Salva o {@link Requerimento} executando a query para alterar o status.
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 */
	public void salvar() {
		try {
			alteracaoStatusService.alterarStatus(requerimento, statusRequerimento);
			RequestContext.getCurrentInstance().execute("dialogAlteracaoStatus.hide()");
			JsfUtil.addSuccessMessage("O status do requerimento foi alterado com sucesso!");
			limpar();
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Ocorreu um erro ao tentar alterar o status do requerimento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	/**
	 * Método para limpar as variáveis que não serão mais utilizadas.
	 * @author eduardo.fernandes
	 * @since 28/01/2015
	 * @see Correção #6554
	 */
	private void limpar(){
		requerimento = null;
		statusRequerimento = null;
		statusRequerimentoAntigo = null;
		renderizaPanelDados = false;
	}


	/**
	 * Valida se o combo do status foi preenchido.
	 *
	 * @author Vitor Alexis de Almeida Leitao (vitor.leitao@zcr.com.br)
	 */
	public void validaObrigatorio() {

		if(Util.isNullOuVazio(statusRequerimento)) {
			JsfUtil.addErrorMessage("Favor selecionar o novo status!");
			RequestContext.getCurrentInstance().addCallbackParam("validado", false);
		} else {
			RequestContext.getCurrentInstance().addCallbackParam("validado", true);
		}
	}



	/**
	 *  {@link Requerimento} não podem ser alterados com os seguintes {@link StatusRequerimento}:
	 * 	<li>1 - Aguardando Enquadramento</li>
	 *  <li>8 - Formado</li>
	 *  <li>9 - Cancelado</li>
	 * @author eduardo.fernandes
	 * @since 28/01/2015
	 * @param status
	 * @return boolean
	 * @see Correção #6554
	 */
	public boolean isImpossivelVoltarStatus(StatusRequerimento status){
		return isStatusAguardandoEnquadramento(status) || isStatusFormado(status) || isStatusCancelado(status);
	}

	/**
	 * {@link Requerimento} com os seguintes {@link StatusRequerimento}:
	 * 	<li>2 - Sendo Enquadrado</li>
	 *  <li>3 - Enquadrado</li>
	 *  <li>4 - Validação Prévia</li>
	 *  <li>11 - Pendência de Validação</li>
	 *  <li>10 - Pendência Enquadramento</li>
	 * @author eduardo.fernandes
	 * @since 28/01/2015
	 * @param status
	 * @return boolean
	 * @see Correção #6554
	 */
	private boolean isStatusAindaNaoValidado(StatusRequerimento status) {
		return isStatusSendoEnquadrado(status) || isStatusEnquadrado(status) || isStatusPendenciaEnquadramento(status) || isStatusValidacaoPrevia(status) || isStatusPendenciaValidacao(status);
	}

	private boolean isStatusAguardandoEnquadramento(StatusRequerimento status) {
		return status.getIdeStatusRequerimento().equals(StatusRequerimentoEnum.AGUARDANDO_ENQUADRAMENTO.getStatus());
	}
	private boolean isStatusSendoEnquadrado(StatusRequerimento status) {
		return status.getIdeStatusRequerimento().equals(StatusRequerimentoEnum.SENDO_ENQUADRADO.getStatus());
	}
	private boolean isStatusEnquadrado(StatusRequerimento status) {
		return status.getIdeStatusRequerimento() == StatusRequerimentoEnum.ENQUADRADO.getStatus();
	}
	private boolean isStatusPendenciaEnquadramento(StatusRequerimento status) {
		return status.getIdeStatusRequerimento() == StatusRequerimentoEnum.PENDENCIA_ENQUADRAMENTO.getStatus();
	}
	private boolean isStatusValidacaoPrevia(StatusRequerimento status) {
		return status.getIdeStatusRequerimento() == StatusRequerimentoEnum.EM_VALIDACAO_PREVIA.getStatus();
	}
	private boolean isStatusValidado(StatusRequerimento status) {
		return status.getIdeStatusRequerimento() == StatusRequerimentoEnum.VALIDADO.getStatus();
	}
	private boolean isStatusPendenciaValidacao(StatusRequerimento status) {
		return status.getIdeStatusRequerimento() == StatusRequerimentoEnum.PENDENCIA_VALIDACAO.getStatus();
	}
	private boolean isStatusFormado(StatusRequerimento status) {
		return status.getIdeStatusRequerimento().equals(StatusRequerimentoEnum.FORMADO.getStatus());
	}
	private boolean isStatusCancelado(StatusRequerimento status) {
		return status.getIdeStatusRequerimento().equals(StatusRequerimentoEnum.CANCELADO.getStatus());
	}
	/*
	 *
	 *
	 * GET'S AND SET'S
	 *
	 *
	 */

	/**
	 * @return the requerimento
	 */
	public Requerimento getRequerimento() {

		return requerimento;
	}

	/**
	 * @param requerimento the requerimento to set
	 */
	public void setRequerimento(Requerimento requerimento) {

		this.requerimento = requerimento;
	}

	/**
	 * @return the renderizaPanelDados
	 */
	public boolean isRenderizaPanelDados() {

		return renderizaPanelDados;
	}

	/**
	 * @param renderizaPanelDados the renderizaPanelDados to set
	 */
	public void setRenderizaPanelDados(boolean renderizaPanelDados) {

		this.renderizaPanelDados = renderizaPanelDados;
	}

	/**
	 * @return the mensagemErroStatus
	 */
	public String getMensagemErroStatus() {

		return mensagemErroStatus;
	}

	/**
	 * @param mensagemErroStatus the mensagemErroStatus to set
	 */
	public void setMensagemErroStatus(String mensagemErroStatus) {

		this.mensagemErroStatus = mensagemErroStatus;
	}

	/**
	 * @return the statusRequerimento
	 */
	public StatusRequerimento getStatusRequerimento() {

		return statusRequerimento;
	}

	/**
	 * @param statusRequerimento the statusRequerimento to set
	 */
	public void setStatusRequerimento(StatusRequerimento statusRequerimento) {

		this.statusRequerimento = statusRequerimento;
	}

	/**
	 * @return the status
	 */
	public List<SelectItem> getStatus() {

		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(List<SelectItem> status) {

		this.status = status;
	}

	/**
	 * @return the existeBoleto
	 */
	public boolean isExisteBoleto() {

		return existeBoleto;
	}

	/**
	 * @param existeBoleto the existeBoleto to set
	 */
	public void setExisteBoleto(boolean existeBoleto) {

		this.existeBoleto = existeBoleto;
	}

	/**
	 * @return the tituloPopup
	 */
	public String getTituloPopup() {

		return tituloPopup;
	}

	/**
	 * @param tituloPopup the tituloPopup to set
	 */
	public void setTituloPopup(String tituloPopup) {

		this.tituloPopup = tituloPopup;
	}
}