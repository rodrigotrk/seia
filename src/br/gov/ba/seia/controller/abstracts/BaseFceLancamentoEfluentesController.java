package br.gov.ba.seia.controller.abstracts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.CaracteristicaEfluente;
import br.gov.ba.seia.entity.FceLancamentoEfluente;
import br.gov.ba.seia.entity.FceLancamentoEfluenteCaracteristica;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.TipoPeriodoDerivacao;
import br.gov.ba.seia.facade.FceLancamentoEfluentesServiceFacade;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;


public abstract class BaseFceLancamentoEfluentesController extends BaseFceOutorgaComDocumentoAdicionalController {

	@EJB
	protected FceLancamentoEfluentesServiceFacade efluentesServiceFacade;

	protected List<CaracteristicaEfluente> listaCaracteristicaEfluente;

	protected List<TipoPeriodoDerivacao> listaTipoPeriodoDerivacao;

	protected FceLancamentoEfluente fceLancamentoEfluente;

	protected FceLancamentoEfluenteCaracteristica fceLancamentoEfluenteCaracteristicaSelecionado;

	protected List<FceLancamentoEfluenteCaracteristica> listaFceLancamentoEfluenteCaracteristicas;

	protected FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeograficaSelecionada;

	protected List<FceOutorgaLocalizacaoGeografica> listaFceOutorgaLocalizacaoGeograficaLancamento;

	public void listarOutorgaLocGeoBy(TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua){
		try {
			if(Util.isNull(tipoFinalidadeUsoAgua)){
				super.listaOutorgaLocalizacaoGeografica = super.obterInformacoesGeoBahia(super.fceOutorgaServiceFacade.listarOutorgaLocalizacaoGeograficaLancamentoEfluenteBy(super.requerimento));
			}
			else {
				super.listaOutorgaLocalizacaoGeografica = super.obterInformacoesGeoBahia(super.fceOutorgaServiceFacade.listarOutorgaLocalizacaoGeograficaLancamentoEfluenteBy(tipoFinalidadeUsoAgua, super.requerimento));
			}
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Outorga Localização Geográfica.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void listarCaracteristicaEfluente(boolean isComFosforo){
		try {
			if(isComFosforo){
				listaCaracteristicaEfluente = efluentesServiceFacade.listarCaracteristicaEfluentes();
			} else {
				listaCaracteristicaEfluente = efluentesServiceFacade.listarCaracteristicaEfluentesSemFosforo();
			}
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista de caraterísticas do efluente.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	protected void listarFceCaracteristicaEfluentesByFceLancamentoEfluentes(){
		try {
			listaFceLancamentoEfluenteCaracteristicas = efluentesServiceFacade.listarFceLancamentoEfluenteCaracteristicaBy(fceLancamentoEfluente);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista de caraterísticas do efluente.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	protected void listarTipoPeriodoDerivacao(){
		try {
			listaTipoPeriodoDerivacao = efluentesServiceFacade.listarTipoPeriodoDerivacao();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista do período de derivação.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	protected void listarFceOutorgaLocalizacaoGeograficaDAnaliseTecnica() throws Exception{
		listaFceOutorgaLocalizacaoGeograficaLancamento = super.listarFceOutorgaLocalizacaoGeograficaDaAnaliseTecnica();
		super.fceOutorgaServiceFacade.tratarPontosListaFceOutorgaLocalizacaoGeografica(listaFceOutorgaLocalizacaoGeograficaLancamento);
	}
	
	protected void listarFceOutorgaLocalizacaoGeografica(){
		try {
			listaFceOutorgaLocalizacaoGeograficaLancamento = efluentesServiceFacade.listarFceOutorgaLocalizacaoGeograficaByIdeFce(super.fce);
			for(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica : listaFceOutorgaLocalizacaoGeograficaLancamento){
				for(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : super.listaOutorgaLocalizacaoGeografica){
					if(fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().equals(outorgaLocalizacaoGeografica)){
						fceOutorgaLocalizacaoGeografica.setIdeOutorgaLocalizacaoGeografica(outorgaLocalizacaoGeografica);
					}
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " os dados do requerimento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	protected FceLancamentoEfluente carregarFceLancamentoEfluente(FceLancamentoEfluente fceLancamentoEfluente) throws Exception {
		return efluentesServiceFacade.carregarFceLancamentoEfluenteBy(fceLancamentoEfluente);
	}

	protected void carregarFceLancamentoEfluenteBy() throws Exception{
		fceLancamentoEfluente =  efluentesServiceFacade.carregarFceLancamentoEfluenteBy(fceOutorgaLocalizacaoGeograficaSelecionada);
	}

	public void editarCoordenada(){
		fceOutorgaLocalizacaoGeograficaSelecionada.setConfirmado(false);
		fceOutorgaLocalizacaoGeograficaSelecionada.setEdicao(true);
	}

	/**
	 * Confirma o {@link FceLancamentoEfluenteCaracteristica}
	 * @param fceLancamentoEfluenteCaracteristicaSelecionado
	 * @author eduardo.fernandes
	 */
	public void confirmarEfluente(){
		if(validarFceLancamentoEfluenteCaracteristica()){
			fceLancamentoEfluenteCaracteristicaSelecionado.setConfirmado(true);
			fceLancamentoEfluenteCaracteristicaSelecionado.setNumEficienciaRemocao(calcularEficienciaRemocao());
		}
	}

	public void editarEfluente(){
		fceLancamentoEfluenteCaracteristicaSelecionado.setConfirmado(false);
	}

	protected boolean validarDadosGenericFceLancamentoEfluente(){
		boolean valido = true;
		if(!validarFceLancamentoEfluente()){
			valido = false;
		}
		if(!validarListaCaracteristicasEfluentes()){
			valido = false;
		}
		return valido;
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param fceLancamentoEfluente
	 * @param valido
	 * @return
	 * @since 22/04/2015
	 */
	private boolean validarFceLancamentoEfluente() {
		boolean valido = true;
		if(Util.isNullOuVazio(fceLancamentoEfluente.getNumVazaoEfluente())){
			JsfUtil.addErrorMessage("A vazão do efluente " + Util.getString("msg_generica_null_ou_vazio"));
			valido = false;
		}
		if(Util.isNullOuVazio(fceLancamentoEfluente.getIdeTipoPeriodoDerivacao())){
			JsfUtil.addErrorMessage("O período de derivação " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		else if(fceLancamentoEfluente.getIdeTipoPeriodoDerivacao().isIntermitente()){
			if(Util.isNullOuVazio(fceLancamentoEfluente.getNumTempoLancamento())){
				JsfUtil.addErrorMessage("O tempo de lançamento " + Util.getString("msg_generica_null_ou_vazio"));
				valido = false;
			} else if(fceLancamentoEfluente.getNumTempoLancamento() > 24){
				JsfUtil.addErrorMessage(Util.getString("fce_captacao_subteranea_tempo_captacao_info_0034"));
				valido = false;
			}
		}
		return valido;
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param listaFceLancamentoEfluenteCaracteristica
	 * @return
	 * @since 22/04/2015
	 */
	private boolean validarListaCaracteristicasEfluentes(){
		for(FceLancamentoEfluenteCaracteristica fceLancamentoEfluenteCaracteristica : listaFceLancamentoEfluenteCaracteristicas){
			if(!fceLancamentoEfluenteCaracteristica.isConfirmado()){
				JsfUtil.addErrorMessage(Util.getString("fce_lancamento_efluente_confirmar_caracteristica"));
				return false;
			}
		}
		return true;
	}

	/**
	 * Método que verifica se aquele {@link FceLancamentoEfluenteCaracteristica} é válido
	 * @param fceLancamentoEfluenteCaracteristicaSelecionado
	 * @return boolean
	 * @author eduardo.fernandes
	 */
	private boolean validarFceLancamentoEfluenteCaracteristica(){
		boolean valido = true;
		if(Util.isNullOuVazio(fceLancamentoEfluenteCaracteristicaSelecionado.getNumBruto())){
			JsfUtil.addErrorMessage(Util.getString("fce_inf042"));
			valido = false;
		}
		if(Util.isNullOuVazio(fceLancamentoEfluenteCaracteristicaSelecionado.getNumTratado())){
			JsfUtil.addErrorMessage(Util.getString("fce_inf043"));
			valido = false;
		}
		if(valido && fceLancamentoEfluenteCaracteristicaSelecionado.getNumBruto().compareTo(fceLancamentoEfluenteCaracteristicaSelecionado.getNumTratado()) < 0){
			JsfUtil.addErrorMessage(Util.getString("fce_lancamento_efluente_bruto_menor_tratado"));
			valido = false;
		}
		return valido;
	}

	protected boolean validarCoordenadas(){
		if(Util.isNullOuVazio(fceOutorgaLocalizacaoGeograficaSelecionada.getNomRio())){
			JsfUtil.addErrorMessage("O nome do rio " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			return false;
		} else {
			return true;
		}
	}

	protected boolean validarCoordenadasLancamento(){
		for(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica : listaFceOutorgaLocalizacaoGeograficaLancamento){
			if(!fceOutorgaLocalizacaoGeografica.isConfirmado()){
				JsfUtil.addErrorMessage(Util.getString("fce_lancamento_efluente_confirmar"));
				return false;
			}
		}
		return true;
	}

	/**
	 * Método que cria uma lista de FceOutorgaLocalizacaoGeografica com cada OutorgaLocalizacaoGeografica daquele Lançamento de Efluente.
	 * @author eduardo.fernandes
	 */
	protected void prepararListaFceOutorgaLocalizacaoGeografica(){
		if(isListaPontoLancamentoNulaOuVazia()){
			listaFceOutorgaLocalizacaoGeograficaLancamento = new ArrayList<FceOutorgaLocalizacaoGeografica>();
		}
		for(OutorgaLocalizacaoGeografica olg : super.listaOutorgaLocalizacaoGeografica){
			listaFceOutorgaLocalizacaoGeograficaLancamento.add(new FceOutorgaLocalizacaoGeografica(super.fce, olg));
		}
	}

	/**
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @return
	 * @since 22/03/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/">#</a>  #ticket
	 */
	protected boolean isListaPontoLancamentoNulaOuVazia() {
		return Util.isNullOuVazio(listaFceOutorgaLocalizacaoGeograficaLancamento);
	}

	protected void prepararListaFceOutorgaLocalizacaoGeograficaInEdicao(){
		if(isListaOutorgaLocGeoMaiorQueFceLocGeo()){
			List<OutorgaLocalizacaoGeografica> listaTEMP = new ArrayList<OutorgaLocalizacaoGeografica>();
			listaTEMP.addAll(super.listaOutorgaLocalizacaoGeografica);
			for(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica : listaFceOutorgaLocalizacaoGeograficaLancamento){
				if(listaTEMP.contains(fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica())){
					super.listaOutorgaLocalizacaoGeografica.remove(fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica());
				}
			}
			prepararListaFceOutorgaLocalizacaoGeografica();
		}
	}

	protected void prepararListaFceLancamentoEfluenteCaracteristicas(){
		listaFceLancamentoEfluenteCaracteristicas = new ArrayList<FceLancamentoEfluenteCaracteristica>();
		for(CaracteristicaEfluente caracteristicaEfluente : listaCaracteristicaEfluente){
			listaFceLancamentoEfluenteCaracteristicas.add(new FceLancamentoEfluenteCaracteristica(caracteristicaEfluente, fceLancamentoEfluente));
		}
	}

	public void salvarFceOutorgaLocalizacaoGeografica() throws Exception {
		efluentesServiceFacade.salvarFceOutorgaLocalizacaoGeografica(fceOutorgaLocalizacaoGeograficaSelecionada);
	}

	public void salvarFceLancamentoEfluentes() throws Exception {
		if(!Util.isNull(super.getDocumentoUpado())){
			fceLancamentoEfluente.setIdeDocumentoObrigatorioRequerimento(super.getDocumentoUpado());
		}
		efluentesServiceFacade.salvarFceLancamentoEfluentes(fceLancamentoEfluente);
	}

	public void salvarFceLancamentoEfluentesCaracteristicas() throws Exception{
		efluentesServiceFacade.salvarListaFceLancamentoEfluentesCaracteristica(listaFceLancamentoEfluenteCaracteristicas);
	}

	private BigDecimal calcularEficienciaRemocao(){
		return ((fceLancamentoEfluenteCaracteristicaSelecionado.getNumBruto().subtract(fceLancamentoEfluenteCaracteristicaSelecionado.getNumTratado())).multiply(new BigDecimal(100))).divide(fceLancamentoEfluenteCaracteristicaSelecionado.getNumBruto(), RoundingMode.HALF_UP);
	}

	@Override
	public void limparDadosOutorga() {
		limparLancamentoEfluente();
		listaCaracteristicaEfluente = null;
		listaFceOutorgaLocalizacaoGeograficaLancamento = null;
		super.limparDadosOutorga();
	}

	protected void limparLancamentoEfluente(){
		fceLancamentoEfluente = null;
		listaFceLancamentoEfluenteCaracteristicas = null;
		fceLancamentoEfluenteCaracteristicaSelecionado = null;
		fceOutorgaLocalizacaoGeograficaSelecionada = null;
	}

	/*
	 * flags
	 */
	private boolean isListaOutorgaLocGeoMaiorQueFceLocGeo(){
		return super.listaOutorgaLocalizacaoGeografica.size() > listaFceOutorgaLocalizacaoGeograficaLancamento.size();
	}

	/*
	 * getters & setters
	 */
	public List<CaracteristicaEfluente> getListaCaracteristicaEfluente() {
		return listaCaracteristicaEfluente;
	}

	public List<FceLancamentoEfluenteCaracteristica> getListaFceLancamentoEfluenteCaracteristicas() {
		return listaFceLancamentoEfluenteCaracteristicas;
	}

	public FceLancamentoEfluente getFceLancamentoEfluente() {
		return fceLancamentoEfluente;
	}

	public void setFceLancamentoEfluente(FceLancamentoEfluente fceLancamentoEfluente) {
		this.fceLancamentoEfluente = fceLancamentoEfluente;
	}

	public List<FceOutorgaLocalizacaoGeografica> getListaFceOutorgaLocalizacaoGeograficaLancamento() {
		return listaFceOutorgaLocalizacaoGeograficaLancamento;
	}

	public List<TipoPeriodoDerivacao> getListaTipoPeriodoDerivacao() {
		return listaTipoPeriodoDerivacao;
	}

	public FceOutorgaLocalizacaoGeografica getFceOutorgaLocalizacaoGeograficaSelecionada() {
		return fceOutorgaLocalizacaoGeograficaSelecionada;
	}

	public void setFceOutorgaLocalizacaoGeograficaSelecionada(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeograficaSelecionada) {
		this.fceOutorgaLocalizacaoGeograficaSelecionada = fceOutorgaLocalizacaoGeograficaSelecionada;
	}

	public FceLancamentoEfluenteCaracteristica getFceLancamentoEfluenteCaracteristicaSelecionado() {
		return fceLancamentoEfluenteCaracteristicaSelecionado;
	}

	public void setFceLancamentoEfluenteCaracteristicaSelecionado(FceLancamentoEfluenteCaracteristica fceLancamentoEfluenteCaracteristicaSelecionado) {
		this.fceLancamentoEfluenteCaracteristicaSelecionado = fceLancamentoEfluenteCaracteristicaSelecionado;
	}
}