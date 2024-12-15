package br.gov.ba.seia.controller;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.controller.abstracts.BaseFceOutorgaAquiculturaViveiroEscavadoController;
import br.gov.ba.seia.entity.AnaliseTecnica;
import br.gov.ba.seia.entity.AquiculturaTipoAtividade;
import br.gov.ba.seia.entity.Empreendimento;
import br.gov.ba.seia.entity.Especie;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceAquicultura;
import br.gov.ba.seia.entity.FceAquiculturaLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.TipoPeriodoDerivacao;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.TipoAquiculturaEnum;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.enumerator.TipoPeriodoDerivacaoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;


/**
 * Controller da Aba <i>Viveiro Escavado - Captação</i>
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 16/04/2015
 */
@Named("fceOutorgaAquiculturaCaptacaoController")
@ViewScoped
public class FceOutorgaAquiculturaCaptacaoController extends BaseFceOutorgaAquiculturaViveiroEscavadoController {

	@Inject
	protected LocalizacaoGeograficaGenericController locGeoController;

	private Empreendimento empreendimento;

	private FceAquicultura fceAquiculturaCaptacao;

	private List<OutorgaLocalizacaoGeografica> listaCaptacaoSubterranea;

	private List<FceAquiculturaLocalizacaoGeografica> listaFceAquiculturaLocalizacaoGeografica;

	private FceAquiculturaLocalizacaoGeografica poligonalSelecionado;

	private BigDecimal numVazaoRequeridaTotal;

	@Override
	@PostConstruct
	public void init(){

		if(super.isExisteRequerimento()) {
			
			super.carregarFce();
			
			if(super.isFceSalvo()) {
				carregarFceAquiculturaCaptacao();
				if(isExisteCaptacao()) {
					prepararEdicao();
				}
				super.verificarFceLicenciamentoAquicultura();
			}
			else {
				navegacaoController.iniciarFce(super.requerimento);
				iniciarFceAquicultura();
			}

			if(super.isFceTecnico()){
				carregarFceTecnico();
			} 
			else {
				carregarAba();
			}
		}
	}

	private void carregarFceAquiculturaCaptacao() {
		fceAquiculturaCaptacao = super.buscarFceAquiculturaBy(super.fce, TipoAquiculturaEnum.CAPTACAO);
	}

	private boolean isExisteCaptacao() {
		return !Util.isNull(fceAquiculturaCaptacao);
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 28/04/2015
	 */
	private void prepararEdicao() {
		super.preparEdicao(fceAquiculturaCaptacao);
		listarFceAquiculturaLocalizacoGeografica();
	}

	@Override
	protected void iniciarFceAquicultura(){
		fceAquiculturaCaptacao = new FceAquicultura(navegacaoController.getFce(), TipoAquiculturaEnum.CAPTACAO);
		fceAquiculturaCaptacao.setIdeTipoPeriodoDerivacao(new TipoPeriodoDerivacao());
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
	public void avancarAba(){
		if(validarAba()){
			navegacaoController.avancarAba();
		}
	}

	@Override
	public void carregarAba() {
		carregarDadosRequerimento();
		if(super.isListaOutorgaLocalizacaoGeograficaNotNull()){
			habilitarAbasViveiroEscavadoCaptacao();
			separarCaptacoes();
			if(!Util.isNullOuVazio(listaCaptacaoSubterranea) || !Util.isNullOuVazio(super.listaFceOutorgaLocalizacaoGeografica)){
				somatorioDaVazaoRequerida();
			}
		}
		carregarDadosEmpreendimento();
		super.listarTipoPeriodoDerivacao();
		super.listarAquiculturaTipoAtividade();
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 16/04/2015
	 */
	@Override
	public void carregarDadosRequerimento() {
		super.listarOutorgaLocGeoFromRequerimentoBy(new TipoFinalidadeUsoAgua(TipoFinalidadeUsoAguaEnum.AQUICULTURA_VIVEIRO_ESCAVADO.getId()));
		super.listarTipologiaDoRequerimento();
	}

	private void carregarDadosEmpreendimento(){
		try {
			empreendimento = aquiculturaServiceFacade.buscarEmpreendimentoBy(super.requerimento);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do empreendimento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void changePeriodoDerivacao(ValueChangeEvent event) {
		TipoPeriodoDerivacao antigoPeriodoDerivacao = (TipoPeriodoDerivacao) event.getOldValue();
		if(!Util.isNullOuVazio(antigoPeriodoDerivacao.getIdeTipoPeriodoDerivacao())){
			if(antigoPeriodoDerivacao.getIdeTipoPeriodoDerivacao().equals(TipoPeriodoDerivacaoEnum.INTERMITENTE.getId())){
				fceAquiculturaCaptacao.setNumTempoCaptacao(null);
			}
		}
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
		super.navegacaoController.setOutrosCaptacao(true);
	}

	private void desmarcarOutros(){
		super.navegacaoController.setOutrosCaptacao(false);
	}

	private void listarFceAquiculturaLocalizacoGeografica(){
		try {
			listaFceAquiculturaLocalizacaoGeografica = super.aquiculturaServiceFacade.listarFceAquiculturaLocalizacaoGeograficaBy(fceAquiculturaCaptacao);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do FCE - Aquicultura.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}


	@Override
	public void finalizar() {
		if(validarAba()){
			try {
				aquiculturaServiceFacade.finalizarCaptacao(this);
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro002") + " a aba Viveiro Escavado - Captação.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		} else {
			super.navegacaoController.setActiveTab(1);
		}
	}
	
	public void prepararParaFinalizar() throws Exception{
		super.finalizar(fceAquiculturaCaptacao, listaFceAquiculturaLocalizacaoGeografica);
	}

	/**
	 * Método para realizar o Downolad do Documento Adicional de Viveiro Escavado - Captação
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 13/11/2014
	 * @return "nome_do_arquivo".doc
	 * @throws FileNotFoundException
	 */
	public StreamedContent getDadosAdicionaisViveiroCaptacao() {
		try {
			return super.getDadosAdicionais("Informacoes_Adicionais_FCE_Outorga__Aquicultura_Viveiro_Escavado_Captacao.doc", "Informações Adicionais - Viveiro Escavado – Captacao.doc");
		} catch (FileNotFoundException e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_documento_adicional"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void tratarUploadCaptacao(FileUploadEvent event) {
		super.tratarUpload(event, fceAquiculturaCaptacao);
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 16/04/2015
	 */
	private void habilitarAbasViveiroEscavadoCaptacao() {
		navegacaoController.setTemCaptacao(true);
	}

	public boolean isNumVazaoPreenchida(){
		return !Util.isNullOuVazio(numVazaoRequeridaTotal);
	}

	public boolean isPontoCaptacaoSubterranea(){
		return !Util.isNullOuVazio(listaCaptacaoSubterranea);
	}

	public boolean isTemCaptacao(){
		return navegacaoController.isTemCaptacao();
	}

	public boolean isTipoPeriodoDerivacaoIntermitente(){
		return isExisteCaptacao() && fceAquiculturaCaptacao.getIdeTipoPeriodoDerivacao().isIntermitente();
	}

	public boolean isTipoPeriodoDerivacaoSazonal(){
		return isExisteCaptacao() && fceAquiculturaCaptacao.getIdeTipoPeriodoDerivacao().isSazonal();
	}

	/**
	 * Método que verifica se houve inserção da Poligonal da Área do Cultivo da aba ativa.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 21/11/2014
	 * @return
	 */
	public boolean isPoligonalPreenchida() {
		if(!Util.isNullOuVazio(listaFceAquiculturaLocalizacaoGeografica) && !Util.isNull(listaFceAquiculturaLocalizacaoGeografica.get(0))) {
			return !Util.isNullOuVazio(listaFceAquiculturaLocalizacaoGeografica.get(0).getIdeLocalizacaoGeografica());
		}
		return false;
	}

	public boolean isExibirUploadDocumentoAdicionalParaCaptacao(){
		return navegacaoController.isTemCaptacao() && isTipoPeriodoDerivacaoSazonal();
	}

	public boolean isDocAdicionalCaptacaoUpado() {
		return !Util.isNullOuVazio(fceAquiculturaCaptacao.getUploadCaminhoArquivo());
	}


	@Override
	public void limpar() {
		fceAquiculturaCaptacao = null;
		numVazaoRequeridaTotal = null;
		listaCaptacaoSubterranea = null;
		listaFceAquiculturaLocalizacaoGeografica = null;
		empreendimento = null;
		desmarcarOutros();
	}

	public void prepararExclusaoDePoligonal() {
		locGeoController.setLocalizacaoGeograficaSelecionada(poligonalSelecionado.getIdeLocalizacaoGeografica());
	}

	public void excluirPoligonal() {
		locGeoController.excluirShape(poligonalSelecionado.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica());
		listaFceAquiculturaLocalizacaoGeografica.remove(poligonalSelecionado);
	}

	public void prepararInclusaoDePoligonal(){
		FceAquiculturaLocalizacaoGeografica fceAquiculturaLocalizacaoGeografica = new FceAquiculturaLocalizacaoGeografica(fceAquiculturaCaptacao);
		prepararTelaLocalizacaoGeograficaGeneric(fceAquiculturaLocalizacaoGeografica);
		if(Util.isNullOuVazio(listaFceAquiculturaLocalizacaoGeografica)){
			listaFceAquiculturaLocalizacaoGeografica = new ArrayList<FceAquiculturaLocalizacaoGeografica>();
		}
		listaFceAquiculturaLocalizacaoGeografica.add(fceAquiculturaLocalizacaoGeografica);
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param fceAquiculturaLocalizacaoGeografica
	 * @since 24/04/2015
	 */
	private void prepararTelaLocalizacaoGeograficaGeneric(FceAquiculturaLocalizacaoGeografica fceAquiculturaLocalizacaoGeografica) {
		locGeoController.limparLocalizacaoGeografSelecionada();
		locGeoController.setIsLicenciamentoAquicutura(true);
		locGeoController.setTipoSecaoGeometrica(ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_SHAPEFILE.getId().intValue());
		locGeoController.setEmpreendimento(empreendimento);
		locGeoController.setObjetoLocalizacao(fceAquiculturaLocalizacaoGeografica);
		locGeoController.setLocalizacaoGeograficaSelecionada(fceAquiculturaLocalizacaoGeografica.getIdeLocalizacaoGeografica());
		locGeoController.carregarTela();
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 16/04/2015
	 */
	@SuppressWarnings("unchecked")
	public void separarCaptacoes() {
		List<OutorgaLocalizacaoGeografica> listaCaptacaoSuperficial;
		Map<String, Object> captacoes = super.separarCaptacoes(super.listaOutorgaLocalizacaoGeografica);
		listaCaptacaoSubterranea = (List<OutorgaLocalizacaoGeografica>) captacoes.get("captacoesSubterranea");
		listaCaptacaoSuperficial = (List<OutorgaLocalizacaoGeografica>) captacoes.get("captacoesSuperficial");
		if(!Util.isNullOuVazio(listaCaptacaoSubterranea) || !Util.isNullOuVazio(listaCaptacaoSuperficial)){
			navegacaoController.setTemCaptacao(true);
			if(!Util.isNullOuVazio(listaCaptacaoSuperficial)){
				super.criarListaFceOutorgaLocalizacaoGeograficaBy(listaCaptacaoSuperficial, fceAquiculturaCaptacao);
			}
		}
	}

	private void somatorioDaVazaoRequerida() {
		numVazaoRequeridaTotal = new BigDecimal(0);
		if(!super.isFceTecnico()){
			for(OutorgaLocalizacaoGeografica outLocGeo : super.listaOutorgaLocalizacaoGeografica){
				if(!Util.isNullOuVazio(outLocGeo.getNumVazao())) {
					numVazaoRequeridaTotal = numVazaoRequeridaTotal.add(outLocGeo.getNumVazao());
				}
			}
		} 
		else {
			for(FceOutorgaLocalizacaoGeografica fceOutLocGeo : super.listaFceOutorgaLocalizacaoGeografica){
				if(!Util.isNullOuVazio(fceOutLocGeo.getNumVazao())) {
					numVazaoRequeridaTotal = numVazaoRequeridaTotal.add(fceOutLocGeo.getNumVazao());
				}
			}
		}
	}

	/**
	 * Método que verifica se as informações obrigatórios da Aba <i>Viveiro Escavado - Captação</i> foram preenchidas.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 13/11/2014
	 * @return
	 */
	@Override
	public boolean validarAba(){
		boolean valido = true;
		if(isPontoCaptacaoSuperficialPreenchido()){
			valido  = super.validarListaFceOutorgaLocalizacaoGeografica("captacao");
		}
		if(!super.validarTipoAtividade()){
			valido = false;
		}
		if(!super.validarTipoPeriodoDerivacao(fceAquiculturaCaptacao)){
			valido = false;
		}
		if(!super.validarDadosSobreCultivo(fceAquiculturaCaptacao)){
			valido = false;
		}
		if(Util.isNullOuVazio(fceAquiculturaCaptacao.getNumVazaoRequerida())){
			valido = false;
			JsfUtil.addErrorMessage("A vazão requerida para cultivo " + Util.getString("msg_generica_null_ou_vazio"));
		}
		else if(!Util.isNullOuVazio(numVazaoRequeridaTotal) && fceAquiculturaCaptacao.getNumVazaoRequerida().compareTo(numVazaoRequeridaTotal) > 0){
			valido = false;
			JsfUtil.addErrorMessage(Util.getString("fce_out_aqui_cp_vazao_cultivo"));
		}
		/*
		 * Poligonal da Área do Cultivo
		 */
		if(Util.isNullOuVazio(listaFceAquiculturaLocalizacaoGeografica)) {
			valido = false;
			JsfUtil.addErrorMessage(Util.getString("fce_out_aqui_obrigatorio_poligonal"));
		}
		return valido;
	}

	@Override
	public void verificarEdicao() {
		super.carregarFce();
	}

	@Override
	public void voltarAba(){
		navegacaoController.voltarAba();
	}

	/*
	 * getters & setters
	 */
	public List<OutorgaLocalizacaoGeografica> getListaCaptacaoSubterranea() {
		return listaCaptacaoSubterranea;
	}

	public FceAquicultura getFceAquiculturaCaptacao() {
		return fceAquiculturaCaptacao;
	}

	public BigDecimal getNumVazaoRequeridaTotal() {
		return numVazaoRequeridaTotal;
	}

	public FceAquiculturaLocalizacaoGeografica getPoligonalSelecionado() {
		return poligonalSelecionado;
	}

	public void setPoligonalSelecionado(FceAquiculturaLocalizacaoGeografica poligonalSelecionado) {
		this.poligonalSelecionado = poligonalSelecionado;
	}

	public List<FceAquiculturaLocalizacaoGeografica> getListaFceAquiculturaLocalizacaoGeografica() {
		if(!Util.isNullOuVazio(listaFceAquiculturaLocalizacaoGeografica)){
			List<FceAquiculturaLocalizacaoGeografica> listaTemp = new ArrayList<FceAquiculturaLocalizacaoGeografica>();
			listaTemp.addAll(listaFceAquiculturaLocalizacaoGeografica);
			for(FceAquiculturaLocalizacaoGeografica poligonal : listaTemp){
				if(Util.isNull(poligonal.getIdeLocalizacaoGeografica())){
					listaFceAquiculturaLocalizacaoGeografica.remove(poligonal);
				}
			}
		}
		return listaFceAquiculturaLocalizacaoGeografica;
	}

	public Empreendimento getEmpreendimento() {
		return empreendimento;
	}

	public void setEmpreendimento(Empreendimento empreendimento) {
		this.empreendimento = empreendimento;
	}

	
	protected boolean existeFceTecnico(){
		return super.isFceTecnico() && !Util.isNull(fceAquiculturaCaptacao);
	}
	
	@Override
	public void prepararAnaliseTecnica(Fce fce, AnaliseTecnica analiseTecnica){
		if(navegacaoController.isTemCaptacao()){
			navegacaoController.setAnaliseTecnica(true);
			super.carregarFce();
			if(!super.isFceSalvo()){
				navegacaoController.iniciarFceTecnico(fce, analiseTecnica);
				super.duplicarFceOutorgaAquicultura();
			} 
			else {
				carregarFceAquiculturaCaptacao();
				if(!isExisteCaptacao()){
					super.duplicarFceOutorgaAquicultura();
				} 
				else {
					carregarFceTecnico();
				}
			}
		}
	
	}
	
	@Override
	protected void prepararDuplicacao() {
		try {
			super.carregarListaFceOutorgaLocalizacaoGeografica(TipoFinalidadeUsoAguaEnum.AQUICULTURA_VIVEIRO_ESCAVADO);
			//fceAquiculturaCaptacao.getIdeFce().setIndConcluido(false);
			fceAquiculturaCaptacao = fceAquiculturaCaptacao.clone();
			super.prepararDuplicacaoFce(fceAquiculturaCaptacao);
			//super.prepararDuplicacaoFce(fceAquiculturaCaptacao);
			for(FceAquiculturaLocalizacaoGeografica fceAquiculturaLocalizacaoGeografica : listaFceAquiculturaLocalizacaoGeografica){
				fceAquiculturaLocalizacaoGeografica.setIdeFceAquiculturaLocalizacaoGeografica(null);
				LocalizacaoGeografica locGeoDuplicada = super.fceOutorgaServiceFacade.duplicarLocalizacaoGeografica(fceAquiculturaLocalizacaoGeografica.getIdeLocalizacaoGeografica());
				fceAquiculturaLocalizacaoGeografica.setIdeLocalizacaoGeografica(locGeoDuplicada);
				fceAquiculturaLocalizacaoGeografica.setIdeFceAquicultura(fceAquiculturaCaptacao);
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " o FCE - Outorga para Aquicultura.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	protected void duplicarFce() {
		try {
			super.duplicar(fceAquiculturaCaptacao, listaFceAquiculturaLocalizacaoGeografica);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " o FCE - Outorga para Aquicultura.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	protected void carregarFceTecnico() {
		try {
			if(isExisteCaptacao()){
				listarFceOutorgaLocalizacaoGeograficaDasCaptacoes();
				habilitarAbasViveiroEscavadoCaptacao();
				somatorioDaVazaoRequerida();
				carregarDadosEmpreendimento();
				super.listarTipoPeriodoDerivacao();
				super.listarAquiculturaTipoAtividade();
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("") + ""); // inserir mensagem
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private void listarFceOutorgaLocalizacaoGeograficaDasCaptacoes(){
		try {
			super.listaFceOutorgaLocalizacaoGeografica = new ArrayList<FceOutorgaLocalizacaoGeografica>();
			super.carregarListaFceOutorgaLocalizacaoGeografica(TipoFinalidadeUsoAguaEnum.AQUICULTURA_VIVEIRO_ESCAVADO);
			if(isExisteCaptacaoSuperficial()){
				listaCaptacaoSubterranea = new ArrayList<OutorgaLocalizacaoGeografica>();
				for(FceOutorgaLocalizacaoGeografica capSub : super.listaFceOutorgaLocalizacaoGeograficaCapSub){
					listaCaptacaoSubterranea.add(new OutorgaLocalizacaoGeografica(capSub.getIdeLocalizacaoGeografica()));
				}
			}
			if(isExisteCaptacaoSubterranea()){
				super.listaFceOutorgaLocalizacaoGeografica.addAll(super.listaFceOutorgaLocalizacaoGeograficaCapSup);
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("") + ""); // inserir mensagem
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @return
	 * @since 21/03/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/">#</a> #ticket
	 */
	private boolean isExisteCaptacaoSubterranea() {
		return !Util.isNullOuVazio(super.listaFceOutorgaLocalizacaoGeograficaCapSup);
	}

	/**
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @return
	 * @since 21/03/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/">#</a> #ticket
	 */
	private boolean isExisteCaptacaoSuperficial() {
		return !Util.isNullOuVazio(super.listaFceOutorgaLocalizacaoGeograficaCapSub);
	}
}