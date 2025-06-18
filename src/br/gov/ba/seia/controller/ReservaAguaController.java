package br.gov.ba.seia.controller;

import java.util.List;

import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.AnaliseTecnica;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.Funcionario;
import br.gov.ba.seia.entity.MotivoReservaAgua;
import br.gov.ba.seia.entity.ReservaAgua;
import br.gov.ba.seia.entity.VwConsultaProcesso;
import br.gov.ba.seia.facade.ReservaAguaServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.ContextoUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * <i>Controller</i> responsável por realizar as {@link ReservaAgua} na {@link AnaliseTecnica}. 
 * @author eduardo (eduardo.fernandes@zcr.com.br)
 * @since 19/02/2016
 * @see <a href="http://10.105.12.26/redmine/issues/7442">#7442</a>
 */
@Named("reservaAguaController")
@ViewScoped
public class ReservaAguaController {

	@EJB
	private ReservaAguaServiceFacade facade;

	// Reserva Àgua
	private FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica;
	private String ideToUpdate;

	// Cancelar Reserva Àgua
	private VwConsultaProcesso vwProcesso;
	private List<MotivoReservaAgua> listaMotivoReservaAgua;
	private MotivoReservaAgua motivoReservaAgua;
	private String justificativa;
	
	/**
	 * 
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 19/02/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7442">#7442</a>
	 */
	public void reservar(){
		try {
			if(!isReservado()){
				facade.reservarAgua(fceOutorgaLocalizacaoGeografica, getFuncionarioLogado());
				atualizarView();
			}
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " a Reserva de Água.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	/**
	 * 
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 19/02/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7442">#7442</a>
	 */
	public void cancelarReservaAgua(){
		try {
			if(isReservado()){
				facade.cancelarReservaAgua(fceOutorgaLocalizacaoGeografica, getFuncionarioLogado());
				atualizarView();
			}
		} 
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o cancelamento da Reserva de Água.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	public void carregarCancelarReservaAgua(VwConsultaProcesso vwProcesso){
		try {
			this.vwProcesso = vwProcesso;
			carregarListaMotivoReservaAgua();
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("") + ""); // inserir mensagem
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @throws Exception
	 * @since 24/02/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7442">#7442</a>
	 */
	private void carregarListaMotivoReservaAgua() throws Exception {
		listaMotivoReservaAgua = facade.listarMotivoReservaAguaByStatusCancelado();
	}

	public void voltar(){
		this.vwProcesso = null;
	}
	
	/**
	 * <b>[RN 00249] O sistema deverá permitir que o usuário coordenador do NOUT ou diretor da DIRRE possa cancelar a reserva de água definida pelo técnico de Outorga.</b>
	 * 
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 24/02/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7442">#7442</a>
	 * 
	 */
	public void cancelarReservaAguaDoProcesso(){
		try {
			if(isPossivelCancelar()){
				facade.cancelarReservarAgua(vwProcesso.getIdeProcesso(), getFuncionarioLogado(), motivoReservaAgua, justificativa);
				JsfUtil.addSuccessMessage(Util.getString("reserva_agua_cancelada"));
			}
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("") + ""); // inserir mensagem
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @return
	 * @since 24/02/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7442">#7442</a>
	 */
	private boolean isPossivelCancelar() {
		boolean valido = true;
		if(Util.isNull(motivoReservaAgua)){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " motivo.");
			valido = false;
		}
		if(Util.isNullOuVazio(justificativa)){
			JsfUtil.addErrorMessage("A justificativa " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		return valido;
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @return
	 * @since 19/02/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7442">#7442</a>
	 */
	private boolean isReservado() {
		return fceOutorgaLocalizacaoGeografica.isAguaReservada();
	}
	
	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param event
	 * @since 22/02/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/7442">#7442</a>
	 */
	private void atualizarView() { 
		RequestContext.getCurrentInstance().addPartialUpdateTarget(ideToUpdate);
	}

	private Funcionario getFuncionarioLogado(){
		return new Funcionario(ContextoUtil.getContexto().getUsuarioLogado().getIdePessoaFisica());
	}

	public FceOutorgaLocalizacaoGeografica getFceOutorgaLocalizacaoGeografica() {
		return fceOutorgaLocalizacaoGeografica;
	}

	public void setFceOutorgaLocalizacaoGeografica(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica) {
		this.fceOutorgaLocalizacaoGeografica = fceOutorgaLocalizacaoGeografica;
	}

	public String getIdeToUpdate() {
		return ideToUpdate;
	}

	public void setIdeToUpdate(String ideToUpdate) {
		this.ideToUpdate = ideToUpdate;
	}

	public VwConsultaProcesso getVwProcesso() {
		return vwProcesso;
	}

	public void setVwProcesso(VwConsultaProcesso vwProcesso) {
		this.vwProcesso = vwProcesso;
	}

	public List<MotivoReservaAgua> getListaMotivoReservaAgua() {
		return listaMotivoReservaAgua;
	}

	public MotivoReservaAgua getMotivoReservaAgua() {
		return motivoReservaAgua;
	}

	public void setMotivoReservaAgua(MotivoReservaAgua motivoReservaAgua) {
		this.motivoReservaAgua = motivoReservaAgua;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

}