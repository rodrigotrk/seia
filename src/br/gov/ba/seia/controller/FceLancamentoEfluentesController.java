package br.gov.ba.seia.controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.controller.abstracts.BaseFceLancamentoEfluentesController;
import br.gov.ba.seia.entity.CaracteristicaEfluente;
import br.gov.ba.seia.entity.CaracterizacaoEfluente;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceLancamentoEfluente;
import br.gov.ba.seia.entity.FceLancamentoEfluenteCaracteristica;
import br.gov.ba.seia.entity.FceLancamentoEfluenteCaracterizacao;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeograficaFinalidade;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaConcedida;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeograficaFinalidade;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.TipoPeriodoDerivacao;
import br.gov.ba.seia.entity.Tipologia;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.TipoPeriodoDerivacaoEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.FceNavegacaoInterface;
import br.gov.ba.seia.service.FceLancamentoEfluentesCaracterizacaoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;



/**
 * 02/04/14
 * @author eduardo.fernandes
 */
@Named("fceLancamentoEfluentesController")
@ViewScoped
public class FceLancamentoEfluentesController extends BaseFceLancamentoEfluentesController implements FceNavegacaoInterface {

	@EJB
	private FceLancamentoEfluentesCaracterizacaoService fceLancamentoEfluentesCaracterizacaoService;

	private int activeTab;
	private static final DocumentoObrigatorio DOC_LANCAMENTO_EFLUENTES = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_LANCAMENTO_EFLUENTES.getId());

	private List<FceLancamentoEfluente> listaFceLancamentoEfluentes;

	// Aba Lançamento
	private List<CaracterizacaoEfluente> listaCaracterizacaoEfluentes;
	private List<CaracterizacaoEfluente> listaCaracterizacaoEfluentesEscolhidos;

	// Aba Adicionais
	private static final DocumentoObrigatorio DOC_LANCAMENTO_EFLUENTES_UPLOAD = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_LANCAMENTO_EFLUENTES_ADICIONAIS.getId());
	
	// #7550
	private List<OutorgaConcedida> listaOutorgaConcedida;

	/**
	 * Método chamado na abertura da tela FCE - Indústria
	 * @param super.fceLancamentoEfluente
	 * @param listaFceLancamentoEfluentes
	 * @author eduardo.fernandes
	 */
	@Override
	public void init(){
		carregarAba();
		verificarEdicao();
		listaFceLancamentoEfluentes = new ArrayList<FceLancamentoEfluente>();
		if(!isFceSalvo()){
			iniciarFce(DOC_LANCAMENTO_EFLUENTES);
			super.prepararListaFceOutorgaLocalizacaoGeografica();
		}
		else {
			super.listarFceOutorgaLocalizacaoGeografica();
			super.prepararListaFceOutorgaLocalizacaoGeograficaInEdicao();
			listarFceLancamentoEfluentes();
		}
	}

	// Métodos Automáticos
	/**
	 * Método que preenche as listas de {@link OutorgaLocalizacaoGeografica} da Aba Dados do Requerimento
	 * @author eduardo.fernandes
	 */
	private void carregarDadosDoRequerimento(){
		super.listarOutorgaLocGeoBy(null);
	}

	/**
	 * Método que verifica se já existe um {@link Fce} salvo para aquele {@link Requerimento}.
	 * @param requerimento
	 * @param DOC_IRRIGACAO
	 * @param isEdicao
	 * @return boolean
	 * @author eduardo.fernandes
	 */
	@Override
	public void verificarEdicao(){
		carregarFceDoRequerente(DOC_LANCAMENTO_EFLUENTES);
	}

	/**
	 * Método que preenche as listas exibidas em cada aba
	 * @author eduardo.fernandes
	 */
	@Override
	public void carregarAba(){
		carregarDadosDoRequerimento();
		carregarListaCaracterizacaoElfuentes();
		carregarListaCaracteristicaElfuentes();
		carregarListaTipoPeriodoDerivacao();
	}

	/**
	 * Método que carrega a lista de {@link CaracterizacaoEfluente}
	 * @param listaCaracterizacaoEfluentes
	 * @author eduardo.fernandes
	 */
	public void carregarListaCaracterizacaoElfuentes(){
		try {
			listaCaracterizacaoEfluentes = efluentesServiceFacade.listarCaracterizacaoElfuentes();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista de caracterização do elfuente.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que carrega a lista de {@link CaracteristicaEfluente}
	 * @param listaCaracteristicaEfluentes
	 * @author eduardo.fernandes
	 */
	private void carregarListaCaracteristicaElfuentes(){
		try {
			boolean SEM_FOSFORO = false;
			super.listarCaracteristicaEfluente(SEM_FOSFORO);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista de caraterísticas do efluente.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que carrega a lista de {@link TipoPeriodoDerivacao}
	 * @param listaTipoPeriodoDerivacao
	 * @author eduardo.fernandes
	 */
	public void carregarListaTipoPeriodoDerivacao(){
		super.listarTipoPeriodoDerivacao();
	}

	public void listarFceLancamentoEfluentes(){
		if(!Util.isNullOuVazio(super.listaFceOutorgaLocalizacaoGeograficaLancamento)){
			try {
				for(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica : super.listaFceOutorgaLocalizacaoGeograficaLancamento){
					if(!Util.isNullOuVazio(fceOutorgaLocalizacaoGeografica.getIdeFceOutorgaLocalizacaoGeografica())){
						fceOutorgaLocalizacaoGeografica.setConfirmado(true);
						fceOutorgaLocalizacaoGeografica.setEdicao(true);
						super.fceOutorgaLocalizacaoGeograficaSelecionada = fceOutorgaLocalizacaoGeografica;
						super.carregarFceLancamentoEfluenteBy();
						if(!Util.isNullOuVazio(super.fceLancamentoEfluente)){
							if(Util.isNull(listaFceLancamentoEfluentes)){
								listaFceLancamentoEfluentes = new ArrayList<FceLancamentoEfluente>();
							}
							listaFceLancamentoEfluentes.add(super.fceLancamentoEfluente);
						}
						super.fceOutorgaLocalizacaoGeograficaSelecionada =  null;
					}
				}
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o FCE - Capta");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}

	/**
	 * Método que seta a Aba ativa de acordo com o clique do usuário na tela
	 * @param activeTab
	 * @param event
	 * @author eduardo.fernandes
	 */
	@Override
	public void controlarAbas(TabChangeEvent event) {
		if ("abaLancamento".equals(event.getTab().getId())) {
			setActiveTab(0);
		}
		else if ("abaAdicionais".equals(event.getTab().getId())) {
			setActiveTab(1);
		}
	}

	/**
	 * Método que permite, ou não, avançar uma aba através do clique do botão "Avançar"
	 * @param activeTab
	 * @author eduardo.fernandes
	 */
	@Override
	public void avancarAba() {
		if(validarAbaLancamento()){
			salvarAbaLancamento();
			if(super.fceLancamentoEfluente.isEdicao()){
				super.exibirMensagem002();
			}
			else {
				super.exibirMensagem001();
			}
			activeTab++;
		}
	}

	public void confirmarCoordenada() {
		if(super.validarCoordenadas()){
			try {
				fceOutorgaLocalizacaoGeograficaSelecionada.setConfirmado(true);
				super.salvarFceOutorgaLocalizacaoGeografica();
				if(fceOutorgaLocalizacaoGeograficaSelecionada.isEdicao()){
					super.exibirMensagem002();
				} else {
					super.exibirMensagem001();
				}
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o FCE - Lançamento de Efluentes.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}

	/**
	 * Método que permite voltar uma aba através do clique do botão "Voltar"
	 * @param activeTab
	 * @author eduardo.fernandes
	 */
	@Override
	public void voltarAba() {
		activeTab--;
	}

	@Override
	public void finalizar(){
		if(validarAba()){
			try {
				fceLancamentoEfluentesCaracterizacaoService.finalizar(this);
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o FCE - Lançamento de Efluentes.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}
	
	public void prepararParaFinalizar() throws Exception{
		super.concluirFce();
		if(super.isFceTecnico()){
			JsfUtil.addWarnMessage(Util.getString("analise_tecnica_msg_verificar_finalidade"));	
		}
		RequestContext.getCurrentInstance().execute("efluentes.hide()");
		RequestContext.getCurrentInstance().execute("rel_lancEfluentes.show()");
		JsfUtil.addSuccessMessage(Util.getString("msg_generica_sucesso_finalizar"));
		limpar();
	}

	public void finalizarDialog(){
		if(validarAbaLancamento()){
			if(validarAbaAdicionais()){
				try {
					salvarAbaAdicionais();
					salvarAbaLancamento();
					RequestContext.getCurrentInstance().execute("efluentes_dialog.hide()");
					JsfUtil.addSuccessMessage(Util.getString("msg_generica_sucesso_finalizar"));
					limparDialog();
				} catch (Exception e) {
					JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o FCE - Lançamento de Efluentes.");
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					throw Util.capturarException(e,Util.SEIA_EXCEPTION);
				}
			}
		} else {
			activeTab = 0;
		}
	}

	@Override
	public boolean validarAba(){
		boolean valido = true;
		if(!super.validarCoordenadasLancamento()){
			valido = false;
		}
		if(!super.isFceTecnico() && valido && isListaFceLocGeoMaiorQueListaFceLancEflu()){
			JsfUtil.addErrorMessage(Util.getString("fce_lancamento_efluente_erro"));
			valido = false;
		}
		return valido;
	}

	private boolean isListaFceLocGeoMaiorQueListaFceLancEflu(){
		return super.listaFceOutorgaLocalizacaoGeograficaLancamento.size() > listaFceLancamentoEfluentes.size();
	}

	private boolean validarAbaLancamento(){
		boolean valido = true;
		if(Util.isNullOuVazio(listaCaracterizacaoEfluentesEscolhidos)){
			JsfUtil.addErrorMessage(Util.getString("fce_lancamento_efluente_caracterizacao_efluente"));
			valido = false;
		}
		if(!super.validarDadosGenericFceLancamentoEfluente()){
			valido = false;
		}
		return valido;
	}

	private boolean validarAbaAdicionais(){
		boolean valido = true;
		if(!super.isArquivoUpado()){
			valido = false;
			JsfUtil.addErrorMessage(Util.getString("fce_outorga_obrigatorio_upload"));
		}
		return valido;
	}

	public void prepararFceLancamentoEfluentes(){
		try {
			super.limparDocumentoUpado();
			super.carregarFceLancamentoEfluenteBy();
			if(Util.isNullOuVazio(super.fceLancamentoEfluente)){
				super.fceLancamentoEfluente = new FceLancamentoEfluente(fceOutorgaLocalizacaoGeograficaSelecionada);
				super.prepararListaFceLancamentoEfluenteCaracteristicas();
			}
			else {
				super.fceLancamentoEfluente.setEdicao(true);
				carregarListaCaracterizacaoEscolhidaByFceLancamentoEfluentes();
				super.listarFceCaracteristicaEfluentesByFceLancamentoEfluentes();
				if(!Util.isNullOuVazio(super.fceLancamentoEfluente.getIdeDocumentoObrigatorioRequerimento())){
					carregarDocumentoAdicionalByDocumentoObrigatorioRequerimento(super.fceLancamentoEfluente.getIdeDocumentoObrigatorioRequerimento());
				}
			}
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void carregarListaCaracterizacaoEscolhidaByFceLancamentoEfluentes() {
		List<FceLancamentoEfluenteCaracterizacao> listaTemp = listarFceLancamentoEfluenteCaracterizacao();
		listaCaracterizacaoEfluentesEscolhidos = new ArrayList<CaracterizacaoEfluente>();
		for(FceLancamentoEfluenteCaracterizacao lancamentoEfluenteCaracterizacao : listaTemp){
			listaCaracterizacaoEfluentesEscolhidos.add(lancamentoEfluenteCaracterizacao.getIdeCaracterizacaoEfluente());
		}
	}

	public void changePeriodoDerivacao(ValueChangeEvent event){
		if(!Util.isNullOuVazio(event.getOldValue()) && event.getOldValue().equals(new TipoPeriodoDerivacao(TipoPeriodoDerivacaoEnum.INTERMITENTE.getId()))){
			super.fceLancamentoEfluente.setNumTempoLancamento(null);
		}
	}

	// INI - Salvar
	protected void salvarFceLancamentoEfluente() throws Exception{
		super.salvarFceLancamentoEfluentes();
		if(!Util.isNull(listaFceLancamentoEfluentes) && !listaFceLancamentoEfluentes.contains(super.fceLancamentoEfluente)){
			listaFceLancamentoEfluentes.add(super.fceLancamentoEfluente);
		}
	}

	private void salvarAbaLancamento(){
		try {
			salvarFceLancamentoEfluente();
			salvarFceLancamentoCaracteristica();
			salvarFceLancamentoElfluentesCaracterizacao();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void salvarAbaAdicionais(){
		if(validarAbaAdicionais()){
			try {
				super.salvarDocumentoAdicional(requerimento, DOC_LANCAMENTO_EFLUENTES_UPLOAD);
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar_documento_adicional"));
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);

			}
		}
	}

	private void salvarFceLancamentoCaracteristica() throws Exception{
		super.salvarFceLancamentoEfluentesCaracteristicas();
	}

	private void salvarFceLancamentoElfluentesCaracterizacao(){
		fceLancamentoEfluentesCaracterizacaoService.prepararAndSalvarFceLancamentoEfluenteCaracterizacao(super.fceLancamentoEfluente, listaCaracterizacaoEfluentesEscolhidos);
	}
	// FIM - Salvar

	// INI - STREAMs
	public StreamedContent getDadosAdicionais() {
		try {
			return getDadosAdicionais("Informacoes_Adicionais_FCE_Lancamento_Efluentes.doc", "Informações Adicionais - FCE Lançamento de Efluentes.doc");
		} catch (FileNotFoundException e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_documento_adicional"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public StreamedContent getImprimirRelatorio() {
		try {
			return getImprimirRelatorio(super.fce, DOC_LANCAMENTO_EFLUENTES);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_imprimir_relatorio") + " do FCE - Lançamento de Efluentes.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	// FIM - STREAMs

	// INI - Close
	@Override
	public void limpar(){
		activeTab = 0;
		if(!Util.isNullOuVazio(super.listaFceOutorgaLocalizacaoGeograficaLancamento)){
			super.listaFceOutorgaLocalizacaoGeograficaLancamento.clear();
		}
		super.limparDadosOutorga();
	}

	public void limparDialog(){
		activeTab = 0;
		super.limparLancamentoEfluente();
		if(!Util.isNullOuVazio(listaCaracterizacaoEfluentesEscolhidos)){
			listaCaracterizacaoEfluentesEscolhidos.clear();
		}
		super.limparDocumentoUpado();
	}
	// FIM - Close

	// INI - Rendered's
	public boolean isPeriodoDerivacaoIntermitente(){
		return !Util.isNullOuVazio(super.fceLancamentoEfluente.getIdeTipoPeriodoDerivacao()) && super.fceLancamentoEfluente.getIdeTipoPeriodoDerivacao().isIntermitente();
	}
	// FIM - Rendered's

	public int getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}

	public List<CaracterizacaoEfluente> getListaCaracterizacaoEfluentes() {
		return listaCaracterizacaoEfluentes;
	}

	public void setListaCaracterizacaoEfluentes(List<CaracterizacaoEfluente> listaCaracterizacaoEfluentes) {
		this.listaCaracterizacaoEfluentes = listaCaracterizacaoEfluentes;
	}

	public List<CaracterizacaoEfluente> getListaCaracterizacaoEfluentesEscolhidos() {
		return listaCaracterizacaoEfluentesEscolhidos;
	}

	public void setListaCaracterizacaoEfluentesEscolhidos(List<CaracterizacaoEfluente> listaCaracterizacaoEfluentesEscolhidos) {
		this.listaCaracterizacaoEfluentesEscolhidos = listaCaracterizacaoEfluentesEscolhidos;
	}

	@Override
	public List<TipoPeriodoDerivacao> getListaTipoPeriodoDerivacao() {
		return listaTipoPeriodoDerivacao;
	}

	public void setListaTipoPeriodoDerivacao(List<TipoPeriodoDerivacao> listaTipoPeriodoDerivacao) {
		this.listaTipoPeriodoDerivacao = listaTipoPeriodoDerivacao;
	}

	@Override
	public void abrirDialog() {
		RequestContext.getCurrentInstance().addPartialUpdateTarget("dlgImprimirRelatorioLancamentoEfluentes");
		RequestContext.getCurrentInstance().addPartialUpdateTarget("efluentes");
		RequestContext.getCurrentInstance().execute("efluentes.show();");
	}

	private List<FceLancamentoEfluenteCaracterizacao> listarFceLancamentoEfluenteCaracterizacao() {
		return fceLancamentoEfluentesCaracterizacaoService.buscarFceLancamentoEfluenteCaracterizacaoByFceLancEflu(super.fceLancamentoEfluente);
	}
	
	@Override
	protected void prepararDuplicacao() {
		listaOutorgaConcedida = new ArrayList<OutorgaConcedida>();
		for(FceLancamentoEfluente fceLancamentoEfluente : listaFceLancamentoEfluentes){
			try {
				FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica = fceLancamentoEfluente.getIdeFceOutorgaLocalizacaoGeografica();
				OutorgaLocalizacaoGeografica outorgaLocGeoOriginal = fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica();
				LocalizacaoGeografica locGeoDuplicada = super.fceOutorgaServiceFacade.duplicarLocalizacaoGeografica(outorgaLocGeoOriginal.getIdeLocalizacaoGeografica());
				prepararListaFceOutorgaLocGeoFinalidade(outorgaLocGeoOriginal, fceOutorgaLocalizacaoGeografica);
				fceOutorgaLocalizacaoGeografica.setIdeLocalizacaoGeografica(locGeoDuplicada);
				fceOutorgaLocalizacaoGeografica.setIdeTipologia(new Tipologia(TipologiaEnum.LANCAMENTO_EFLUENTES.getId()));
				for(FceOutorgaLocalizacaoGeografica fceOutorgaLocGeo: super.listaFceOutorgaLocalizacaoGeograficaLancamento){
					if(fceOutorgaLocalizacaoGeografica.equals(fceOutorgaLocGeo)){
						fceOutorgaLocGeo.setIdeLocalizacaoGeografica(fceOutorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica());
					}
				}
				fceOutorgaLocalizacaoGeografica.setIdeFceOutorgaLocalizacaoGeografica(null);
				criarOutorgaConcedida(fceOutorgaLocalizacaoGeografica, outorgaLocGeoOriginal);
				fceOutorgaLocalizacaoGeografica.setIdeOutorgaLocalizacaoGeografica(null);
			}
			catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("") + ""); // inserir mensagem
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}
	
	private void criarOutorgaConcedida(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica, OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica){
		listaOutorgaConcedida.add(new OutorgaConcedida(fceOutorgaLocalizacaoGeografica, outorgaLocalizacaoGeografica));
	}

	@Override
	protected void duplicarFce() {
	    try {
	        super.salvarFce();
	       
	        for (FceLancamentoEfluente fceLancamentoEfluente : listaFceLancamentoEfluentes) {
	            FceLancamentoEfluente fceOriginal = super.fceLancamentoEfluente;
	            FceOutorgaLocalizacaoGeografica outorgaOriginal = super.fceOutorgaLocalizacaoGeograficaSelecionada;
	           
	            super.fceLancamentoEfluente = fceLancamentoEfluente;
	            super.fceOutorgaLocalizacaoGeograficaSelecionada = fceLancamentoEfluente.getIdeFceOutorgaLocalizacaoGeografica();
	           
	            super.listarFceCaracteristicaEfluentesByFceLancamentoEfluentes();
	           
	            carregarListaCaracterizacaoEscolhidaByFceLancamentoEfluentes();
	           
	            super.fceLancamentoEfluente.setIdeFceLancamentoEfluente(null);
	            super.fceOutorgaLocalizacaoGeograficaSelecionada.setIdeFce(super.fce);
	           
	            super.salvarFceOutorgaLocalizacaoGeografica();
	            salvarFceOutorgaLocalizacaoGeograficaFinalidade(fceOutorgaLocalizacaoGeograficaSelecionada);
	           
	            super.salvarFceLancamentoEfluentes();
	            for (FceLancamentoEfluenteCaracteristica fceLancamentoEfluenteCaracteristica : super.listaFceLancamentoEfluenteCaracteristicas) {
	                fceLancamentoEfluenteCaracteristica.setIdeFceLancamentoEfluente(super.fceLancamentoEfluente);
	            }
	            super.salvarFceLancamentoEfluentesCaracteristicas();
	            salvarFceLancamentoElfluentesCaracterizacao();
	           
	            super.fceLancamentoEfluente = fceOriginal;
	            super.fceOutorgaLocalizacaoGeograficaSelecionada = outorgaOriginal;
	        }
	       
	        super.salvarListaOutorgaConcedida(listaOutorgaConcedida);
	        super.fceLancamentoEfluente = null;
	        super.fceOutorgaLocalizacaoGeograficaSelecionada = null;
	       
	    } catch (Exception e) {
	        JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " o FCE - Lançamento de Efluentes.");
	        Log4jUtil.log(this.getClass().getName(), Level.ERROR, e);
	        throw Util.capturarException(e, Util.SEIA_EXCEPTION);
	    }
	}
	
	protected void prepararListaFceOutorgaLocGeoFinalidade(OutorgaLocalizacaoGeografica outorgaLocGeoOriginal, FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeograficaDuplicada) throws Exception {
		// Preparar lista de FceOutorgaLocalizacaoGeograficaFinalidade.
		List<OutorgaLocalizacaoGeograficaFinalidade> listaOutorgaLocGeoFinalidade = super.fceOutorgaServiceFacade.listarOutorgaLocalizacaoGeograficaFinalidadeBy(outorgaLocGeoOriginal); 
		if(!Util.isNullOuVazio(listaOutorgaLocGeoFinalidade)){
			fceOutorgaLocalizacaoGeograficaDuplicada.setListaFinalidade(new ArrayList<FceOutorgaLocalizacaoGeograficaFinalidade>());
			for(OutorgaLocalizacaoGeograficaFinalidade outorgaLocalizacaoGeograficaFinalidade : listaOutorgaLocGeoFinalidade){
				TipoFinalidadeUsoAgua tipoFinalidadeUsoAgua = outorgaLocalizacaoGeograficaFinalidade.getIdeTipoFinalidadeUsoAgua();
				boolean indCaptacao = outorgaLocalizacaoGeograficaFinalidade.getIndCaptacao();
				fceOutorgaLocalizacaoGeograficaDuplicada.getListaFinalidade().add(new FceOutorgaLocalizacaoGeograficaFinalidade(fceOutorgaLocalizacaoGeograficaDuplicada, tipoFinalidadeUsoAgua, indCaptacao));
			}
		}
	}
	
	/**
	 * Método para salvar a lista de {@link FceOutorgaLocalizacaoGeograficaFinalidade}
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @throws Exception 
	 * @since 06/01/2016
	 * @see adicionar o #ticket
	 */
	private void salvarFceOutorgaLocalizacaoGeograficaFinalidade(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica) throws Exception{
		if(!Util.isNullOuVazio(fceOutorgaLocalizacaoGeografica.getListaFinalidade())){
			super.fceOutorgaServiceFacade.salvarListaFceOutorgaLocGeoFinalidade(fceOutorgaLocalizacaoGeografica.getListaFinalidade());
		}
	}

	@Override
	protected void carregarFceTecnico() {
		try {
			carregarListaCaracterizacaoElfuentes();
			carregarListaCaracteristicaElfuentes();
			carregarListaTipoPeriodoDerivacao();
			super.listarFceOutorgaLocalizacaoGeograficaDAnaliseTecnica();
			List<FceOutorgaLocalizacaoGeografica> list = super.listaFceOutorgaLocalizacaoGeograficaLancamento;
			if(!Util.isNullOuVazio(list)){
				listarFceLancamentoEfluentes();
			}else{
				init();				
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o FCE - Lançamento de Efluentes.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
}