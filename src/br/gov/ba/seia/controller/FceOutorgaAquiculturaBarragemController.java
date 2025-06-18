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
 * Controller da Aba <i>Tanque Rede - Barragem</i>
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 16/04/2015
 */
@Named("fceOutorgaAquiculturaBarragemController")
@ViewScoped
public class FceOutorgaAquiculturaBarragemController extends BaseFceOutorgaAquiculturaTanqueRedeController {

	private FceAquicultura fceAquiculturaBarragem;

	@Override
	@PostConstruct
	public void init() {
		
		if(super.isExisteRequerimento()) {
			super.carregarFce();

			if(super.isFceSalvo()){
				carregarFceAquiculturaBarragem();
				if(isExisteBarragem()) {
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

	private void carregarFceAquiculturaBarragem() {
		fceAquiculturaBarragem = super.buscarFceAquiculturaBy(super.fce, TipoAquiculturaEnum.BARRAGEM);
	}
	
	private boolean isExisteBarragem() {
		return !Util.isNull(fceAquiculturaBarragem);
	}

	@Override
	protected void iniciarFceAquicultura(){
		fceAquiculturaBarragem = new FceAquicultura(navegacaoController.getFce(), TipoAquiculturaEnum.BARRAGEM);
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 28/04/2015
	 */
	private void prepararEdicao() {
		super.preparEdicao(fceAquiculturaBarragem);
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
				aquiculturaServiceFacade.finalizarBarragem(this);
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro002") + " a aba Tanque Rede - Barragem.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		else {
			super.navegacaoController.setActiveTab(4);
		}
	}
	
	public void prepararParaFinalizar() throws Exception{
		super.finalizar(fceAquiculturaBarragem, null);
	}

	private void habilitarAbaTanqueRedeBarragem() {
		navegacaoController.setTemBarragem(true);
	}

	public boolean isTemBarragem(){
		return navegacaoController.isTemBarragem();
	}

	public boolean isTipoPeriodoDerivacaoIntermitente(){
		return fceAquiculturaBarragem.getIdeTipoPeriodoDerivacao().isIntermitente();
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
		super.navegacaoController.setOutrosBarragem(true);
	}

	private void desmarcarOutros(){
		super.navegacaoController.setOutrosBarragem(false);
	}

	@Override
	public void limpar() {
		fceAquiculturaBarragem = null;
		desmarcarOutros();
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 16/04/2015
	 */
	@Override
	protected void separarIntervencao() {
		List<OutorgaLocalizacaoGeografica> listaIntervencaoBarragem = super.separarIntervencaoToBarragem();
		if(!Util.isNullOuVazio(listaIntervencaoBarragem)){
			habilitarAbaTanqueRedeBarragem();
			super.criarListaFceOutorgaLocalizacaoGeograficaBy(listaIntervencaoBarragem, fceAquiculturaBarragem);
		}

	}

	@Override
	public boolean validarAba() {
		return super.validarAba(fceAquiculturaBarragem);
	}

	@Override
	public void verificarEdicao() {
		super.carregarFce();
	}

	@Override
	public void voltarAba() {
		navegacaoController.voltarAba();
	}

	public void tratarUploadBarragem(FileUploadEvent event) {
		super.tratarUpload(event, fceAquiculturaBarragem);
	}

	public boolean isDocAdicionalBarragemUpado() {
		return !Util.isNullOuVazio(fceAquiculturaBarragem.getUploadCaminhoArquivo());
	}

	/**
	 * Método para realizar o Downolad do Documento Adicional de Tanque Rede - Barragem
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 13/11/2014
	 * @return "nome_do_arquivo".doc
	 * @throws FileNotFoundException
	 */
	public StreamedContent getDadosAdicionaisTanqueBarragem() {
		try {
			return super.getDadosAdicionais("Informacoes_Adicionais_FCE_Outorga__Aquicultura_Tanque_Rede_Barragem.doc", "Informações Adicionais - Tanque Rede – Barragem.doc");
		} catch (FileNotFoundException e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_documento_adicional"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/*
	 * getters & setters
	 */
	public FceAquicultura getFceAquiculturaBarragem() {
		return fceAquiculturaBarragem;
	}
	
	@Override
	public void prepararAnaliseTecnica(Fce fce, AnaliseTecnica analiseTecnica){
		if(navegacaoController.isTemBarragem()){
			navegacaoController.setAnaliseTecnica(true);
			super.carregarFce();
			if(!super.isFceSalvo()){
				navegacaoController.iniciarFceTecnico(super.fce, analiseTecnica);
			} 
			if(!super.existeFceAquicultura(TipoAquiculturaEnum.BARRAGEM)){
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
			fceAquiculturaBarragem = super.buscarFceAquiculturaTanqueRedeBy(TipoAquiculturaEnum.BARRAGEM);
//			super.prepararFceOutorgaLocalizacaoGeograficaAquiculturaToDuplicacao(TipoAquiculturaEnum.BARRAGEM, fceAquiculturaBarragem); //
			fceAquiculturaBarragem = fceAquiculturaBarragem.clone();
			super.prepararDuplicacaoFce(fceAquiculturaBarragem);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " o FCE - Outorga para Aquicultura.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	protected void duplicarFce() {
		try {
			super.duplicar(fceAquiculturaBarragem, null);
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