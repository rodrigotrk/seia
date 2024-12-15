package br.gov.ba.seia.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.FceAquicultura;
import br.gov.ba.seia.entity.FceAquiculturaLicenca;
import br.gov.ba.seia.enumerator.TipoAquiculturaEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.FceAquiculturaService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * Controller responsável pela <i>Aba Dados do Requerimento</i> do <b>FCE - Licenciamento para Aquicultura</b>.
 * <pre><b> #6934 </b></pre>
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 27/05/2015
 */
@Named("fceLicenciamentoAquiculturaDadosRequerimentoController")
@ViewScoped
public class FceLicenciamentoAquiculturaDadosRequerimentoController {

	@Inject
	private FceLicenciamentoAquiculturaController licenciamentoAquiculturaController;

	@EJB
	private FceAquiculturaService fceAquiculturaService;

	private boolean viveiroEscavadoPreenchidoEmOutorga;
	private boolean tanqueRedePreenchidoEmOutorga;

	@PostConstruct
	public void init(){
		prepararAba();
	}

	private void prepararAba(){
		List<FceAquicultura> listaFceAquicultura = listarFceAquiculturaByRequerimento();
		if(!Util.isNullOuVazio(listaFceAquicultura)){
			for(FceAquicultura fceAquicultura : listaFceAquicultura){
				if(isViveiroEscavado(fceAquicultura)){
					if(isFceAquiculturaCaptacao(fceAquicultura)){
						licenciamentoAquiculturaController.getFceAquiculturaLicenca().setNumVazaoCaptacao(fceAquicultura.getNumVazaoRequerida());
					}
					if(isFceAquiculturaLancamento(fceAquicultura)){
						licenciamentoAquiculturaController.getFceAquiculturaLicenca().setNumVazaoLancamento(fceAquicultura.getIdeFceLancamentoEfluente().getNumVazaoEfluente());
					}
					licenciamentoAquiculturaController.setComFceOutorgaAquiculturaViveiroEscavado(true);
					viveiroEscavadoPreenchidoEmOutorga = true;
				}
				if(isTanqueRede(fceAquicultura)){
					licenciamentoAquiculturaController.setComFceOutorgaAquiculturaTanqueRede(true);
					tanqueRedePreenchidoEmOutorga = true;
				}
			}
		}
		else {
			licenciamentoAquiculturaController.setComFceOutorgaAquiculturaViveiroEscavado(false);
			licenciamentoAquiculturaController.setComFceOutorgaAquiculturaTanqueRede(false);
		}
		if(!licenciamentoAquiculturaController.isFceSalvo()){
			getFceAquiculturaLicenca().setIndAquiculturaViveiroEscavado(licenciamentoAquiculturaController.isComFceOutorgaAquiculturaViveiroEscavado());
			getFceAquiculturaLicenca().setIndAquiculturaTanqueRede(licenciamentoAquiculturaController.isComFceOutorgaAquiculturaTanqueRede());
		}
		if(getFceAquiculturaLicenca().getIndAquiculturaViveiroEscavado()){
			licenciamentoAquiculturaController.renderizarAbaViveiro();
		}
		if(getFceAquiculturaLicenca().getIndAquiculturaTanqueRede()){
			licenciamentoAquiculturaController.renderizarAbaTanque();
		}
		
	}

	public void prepararAbaViveiroEscavadoParaCadastro(){
		if(licenciamentoAquiculturaController.cadastrarViveiro()){
			licenciamentoAquiculturaController.renderizarAbaViveiro();
		}
	}

	public void prepararAbaTanqueRedeParaCadastro(){
		if(licenciamentoAquiculturaController.cadastrarTanque()){
			licenciamentoAquiculturaController.renderizarAbaTanque();
		}
	}

	private List<FceAquicultura> listarFceAquiculturaByRequerimento(){
		try {
			return fceAquiculturaService.listarFceAquiculturaByIdeRequerimento(licenciamentoAquiculturaController.getRequerimento());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do FCE - Aquicultura.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @return
	 * @since 29/06/2015
	 */
	public boolean validarAba(){
		if(isSemAquiculturaEmViveiroEscavado() && isSemAquiculturaEmTanqueRede()){
			JsfUtil.addErrorMessage(Util.getString("fce_lic_aqui_dados_requerimento_invalido"));
			return false;
		}
		return true;
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param fceAquicultura
	 * @return
	 * @since 01/06/2015
	 */
	private boolean isTanqueRede(FceAquicultura fceAquicultura) {
		return fceAquicultura.getIdeTipoAquicultura().getIdeTipoAquicultura().equals(TipoAquiculturaEnum.BARRAGEM.getId()) || fceAquicultura.getIdeTipoAquicultura().getIdeTipoAquicultura().equals(TipoAquiculturaEnum.RIO.getId());
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param fceAquicultura
	 * @return
	 * @since 01/06/2015
	 */
	private boolean isViveiroEscavado(FceAquicultura fceAquicultura) {
		return isFceAquiculturaCaptacao(fceAquicultura) || isFceAquiculturaLancamento(fceAquicultura);
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param fceAquicultura
	 * @return
	 * @since 09/06/2015
	 */
	private boolean isFceAquiculturaLancamento(FceAquicultura fceAquicultura) {
		return fceAquicultura.getIdeTipoAquicultura().getIdeTipoAquicultura().equals(TipoAquiculturaEnum.LANCAMENTO.getId());
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param fceAquicultura
	 * @return
	 * @since 09/06/2015
	 */
	private boolean isFceAquiculturaCaptacao(FceAquicultura fceAquicultura) {
		return fceAquicultura.getIdeTipoAquicultura().getIdeTipoAquicultura().equals(TipoAquiculturaEnum.CAPTACAO.getId());
	}

	public boolean isSemAquiculturaEmViveiroEscavado(){
		return !licenciamentoAquiculturaController.isAquiculturaEmViveiroEscavado();
	}

	public boolean isSemAquiculturaEmTanqueRede(){
		return !licenciamentoAquiculturaController.isAquiculturaEmTanqueRede();
	}

	public FceAquiculturaLicenca getFceAquiculturaLicenca(){
		return licenciamentoAquiculturaController.getFceAquiculturaLicenca();
	}

	public void avancarAba(){
		licenciamentoAquiculturaController.avancarAba();
	}

	public void voltarAba(){
		licenciamentoAquiculturaController.voltarAba();
	}

	public boolean isViveiroEscavadoPreenchidoEmOutorga() {
		return viveiroEscavadoPreenchidoEmOutorga;
	}

	public boolean isTanqueRedePreenchidoEmOutorga() {
		return tanqueRedePreenchidoEmOutorga;
	}
}