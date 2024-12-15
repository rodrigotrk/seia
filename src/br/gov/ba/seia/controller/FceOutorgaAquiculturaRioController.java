package br.gov.ba.seia.controller;

import java.io.FileNotFoundException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.controller.abstracts.BaseFceOutorgaAquiculturaTanqueRedeController;
import br.gov.ba.seia.entity.AnaliseTecnica;
import br.gov.ba.seia.entity.AquiculturaTipoAtividade;
import br.gov.ba.seia.entity.Especie;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceAquicultura;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.enumerator.TipoAquiculturaEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;


/**
 * Controller da Aba <i>Tanque Rede - Rio</i>
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 16/04/2015
 */
@Named("fceOutorgaAquiculturaRioController")
@ViewScoped
public class FceOutorgaAquiculturaRioController extends BaseFceOutorgaAquiculturaTanqueRedeController {

	private FceAquicultura fceAquiculturaRio;

	@Override
	@PostConstruct
	public void init() {

		if(super.isExisteRequerimento()) {
			super.carregarFce();

			if(super.isFceSalvo()){
				carregarFceAquiculturaRio();
				if(isExisteRio()) {
					prepararEdicao();
				}
				super.verificarFceLicenciamentoAquicultura();
			}
			else {
				navegacaoController.iniciarFce(super.requerimento);
				iniciarFceAquicultura();
			}
			carregarAba();
		}
	}

	private void carregarFceAquiculturaRio() {
		fceAquiculturaRio = super.buscarFceAquiculturaBy(super.fce, TipoAquiculturaEnum.RIO);
	}

	private boolean isExisteRio() {
		return !Util.isNull(fceAquiculturaRio);
	}

	@Override
	protected void iniciarFceAquicultura(){
		fceAquiculturaRio = new FceAquicultura(navegacaoController.getFce(), TipoAquiculturaEnum.RIO);
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 28/04/2015
	 */
	private void prepararEdicao() {
		super.preparEdicao(fceAquiculturaRio);
	}

	/**
	 * Método que adiciona a {@link AquiculturaTipoAtividade} na {@link #listaTipoAtividadeCaptacao} e carrega suas {@link Especie}s
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 12/11/2014
	 * @param tipoAtividade
	 */
	public void adicionarTipoAtividade() {
		super.adicionarTipoAtividade(super.tipoAtividadeSelecionado);
	}

	public void excluirTipoAtividade(){
		super.excluirTipoAtividade(super.tipoAtividadeSelecionado);
	}

	@Override
	public void avancarAba() {
		if(validarAba()){
			navegacaoController.avancarAba();
		}
	}

	/* (non-Javadoc)
	 * @see br.gov.ba.seia.fce.FceInterface#carregarAba()
	 */
	@Override
	public void carregarAba() {
		super.carregarDadosRequerimento();
		if(super.isListaOutorgaLocalizacaoGeograficaNotNull()){
			separarIntervencao();
			if(!Util.isNullOuVazio(super.listaFceOutorgaLocalizacaoGeografica)){
				super.carregarAba();
			}
		}
	}

	@Override
	public void finalizar() {
		if(validarAba()){
			try {
				aquiculturaServiceFacade.finalizarRio(this);
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro002") + " a aba Tanque Rede - Rio.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		else {
			super.navegacaoController.setActiveTab(3);
		}
	}
	
	public void prepararParaFinalizar() throws Exception{
		super.finalizar(fceAquiculturaRio, null);
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 16/04/2015
	 */
	private void habilitarAbaTanqueRedeRio() {
		navegacaoController.setTemRio(true);
	}

	public boolean isTemRio(){
		return navegacaoController.isTemRio();
	}

	@Override
	public void changeEspecies(ValueChangeEvent event){
		super.changeEspecies(event);
		if(super.isOutrosSelecionado()){
			marcarOutros();
		} else {
			desmarcarOutros();
		}
	}

	private void marcarOutros(){
		super.navegacaoController.setOutrosRio(true);
	}

	private void desmarcarOutros(){
		super.navegacaoController.setOutrosRio(false);
	}


	@Override
	public void limpar() {
		fceAquiculturaRio = null;
		desmarcarOutros();
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 16/04/2015
	 */
	@Override
	protected void separarIntervencao() {
		List<OutorgaLocalizacaoGeografica> listaIntervencaoRio = super.separarIntervencaoToRio();
		if(!Util.isNullOuVazio(listaIntervencaoRio)){
			habilitarAbaTanqueRedeRio();
			super.criarListaFceOutorgaLocalizacaoGeograficaBy(listaIntervencaoRio, fceAquiculturaRio);
		}
	}

	@Override
	public boolean validarAba() {
		boolean valido = true;
		if(!super.validarAba(fceAquiculturaRio)){
			valido = false;
		}
		return valido;
	}

	@Override
	public void verificarEdicao() {
		super.carregarFce();
	}

	@Override
	public void voltarAba() {
		navegacaoController.voltarAba();
	}


	/**
	 * Método para realizar o Downolad do Documento Adicional de Tanque Rede - Rio
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 13/11/2014
	 * @return "nome_do_arquivo".doc
	 * @throws FileNotFoundException
	 */
	public StreamedContent getDadosAdicionaisTanqueRio() {
		try {
			return super.getDadosAdicionais("Informacoes_Adicionais_FCE_Outorga__Aquicultura_Tanque_Rede_Rio.doc", "Informações Adicionais - Tanque Rede – Rio.doc");
		} catch (FileNotFoundException e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_documento_adicional"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}


	public void tratarUploadRio(FileUploadEvent event) {
		super.tratarUpload(event, fceAquiculturaRio);
	}

	public boolean isDocAdicionalRioUpado() {
		return !Util.isNullOuVazio(fceAquiculturaRio.getUploadCaminhoArquivo());
	}

	/*
	 * getters & setters
	 */
	public FceAquicultura getFceAquiculturaRio() {
		return fceAquiculturaRio;
	}
	
	@Override
	public void prepararAnaliseTecnica(Fce fce, AnaliseTecnica analiseTecnica){
		if(navegacaoController.isTemRio()){
			navegacaoController.setAnaliseTecnica(true);
			super.carregarFce();
			if(!super.isFceSalvo()){
				navegacaoController.iniciarFceTecnico(fce, analiseTecnica);
			}
			if(!super.existeFceAquicultura(TipoAquiculturaEnum.RIO)){
				super.duplicarFceOutorgaAquicultura();
			} 
			else {
				carregarFceTecnico();
			}
		}
	}

	@Override
	protected void prepararDuplicacao() {
		try {
			fceAquiculturaRio = super.buscarFceAquiculturaTanqueRedeBy(TipoAquiculturaEnum.RIO);
//			super.prepararFceOutorgaLocalizacaoGeograficaAquiculturaToDuplicacao(TipoAquiculturaEnum.RIO, fceAquiculturaRio); //
			super.prepararDuplicacaoFce(fceAquiculturaRio);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " o FCE - Outorga para Aquicultura.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	protected void duplicarFce() {
		try {
			super.duplicar(fceAquiculturaRio, null);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " o FCE - Outorga para Aquicultura.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	protected void carregarFceTecnico() {
		super.carregarAba();		
	}
}