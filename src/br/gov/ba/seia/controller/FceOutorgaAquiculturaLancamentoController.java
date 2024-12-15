package br.gov.ba.seia.controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.controller.abstracts.BaseFceLancamentoEfluentesController;
import br.gov.ba.seia.entity.AnaliseTecnica;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.DocumentoObrigatorioRequerimento;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceAquicultura;
import br.gov.ba.seia.entity.FceLancamentoEfluente;
import br.gov.ba.seia.entity.FceLancamentoEfluenteCaracteristica;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoAquicultura;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.TipoPeriodoDerivacao;
import br.gov.ba.seia.enumerator.DiretorioArquivoEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.TipoAquiculturaEnum;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.enumerator.TipoPeriodoDerivacaoEnum;
import br.gov.ba.seia.facade.FceLancamentoEfluentesServiceFacade;
import br.gov.ba.seia.facade.FceOutorgaAquiculturaServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.util.FileUploadUtil;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * Controller da Aba <i>Viveiro Escavado - Lançamento</i>
 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
 * @since 16/04/2015
 */
@Named("fceOutorgaAquiculturaLancamentoController")
@ViewScoped
public class FceOutorgaAquiculturaLancamentoController extends BaseFceLancamentoEfluentesController {

	@Inject
	protected FceOutorgaAquiculturaNavegacaoController navegacaoController;

	@EJB
	private FceOutorgaAquiculturaServiceFacade aquiculturaServiceFacade;

	@EJB
	private FceLancamentoEfluentesServiceFacade fceLancamentoEfluentesServiceFacade;

	private static final DocumentoObrigatorio DOCUMENTO_OBRIGATORIO = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_OUTORGA_AQUICULTURA.getId());

	private FceAquicultura fceAquiculturaLancamento;

	private List<FceOutorgaLocalizacaoAquicultura> listaFceOutorgaLocalizacaoAquicultura;

	private List<String> arquivosAdicionaisToDelete;

	@Override
	@PostConstruct
	public void init() {
		if(isExisteRequerimento()) {
			carregarAba();
			if(isTemLancamento()){
				verificarEdicao();
				if(isFceSalvo()){
					carregarFceAquiculturaLancamento();
					if(isExisteFceAquiculturaLancamento()){
						prepararEdicao();
					}
					verificarFceLicenciamentoAquicultura();
				}
				else {
					navegacaoController.iniciarFce(super.requerimento);
					prepararAbaLancamentoEfluente();
					iniciarFceAquicultura();
					montarListaFceOutorgaLocalizacaoGeograficaAquicultura();
				}
			}
		}
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @throws Exception
	 * @since 01/07/2015
	 */
	private void verificarFceLicenciamentoAquicultura() {
		try {
			if(Util.isNull(navegacaoController.getFceLicenciamentoAquicolaPreenchido())){
				navegacaoController.setFceLicenciamentoAquicolaPreenchido(aquiculturaServiceFacade.isFceLicenciamentoAquiculturaPreenchido(super.requerimento));
				if(navegacaoController.getFceLicenciamentoAquicolaPreenchido()){
					JsfUtil.addWarnMessage("FCE LICENCIEMANTO AQUICULTURA PREENCHIDO.");
				}
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do FCE - Licenciamento para Aquicultura.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private boolean isExisteRequerimento() {
		super.requerimento = navegacaoController.getRequerimento();
		return !Util.isNull(super.requerimento);
	}

	private boolean isExisteFceAquiculturaLancamento() {
		return !Util.isNull(fceAquiculturaLancamento) && !Util.isNullOuVazio(fceAquiculturaLancamento.getIdeFceLancamentoEfluente());
	}

	private void carregarFceAquiculturaLancamento() {
		try {
			fceAquiculturaLancamento = aquiculturaServiceFacade.buscarFceAquiculturaBy(super.fce, TipoAquiculturaEnum.LANCAMENTO);
			
			if(Util.isNullOuVazio(fceAquiculturaLancamento)) {
				navegacaoController.setFce(super.fce);
				prepararAbaLancamentoEfluente();
				iniciarFceAquicultura();
				montarListaFceOutorgaLocalizacaoGeograficaAquicultura();
			}
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do FCE - Aquicultura.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private boolean existeFceAquiculturaLancamento(){
		try {
			return !Util.isNull(aquiculturaServiceFacade.buscarFceAquiculturaBy(super.fce, TipoAquiculturaEnum.LANCAMENTO));
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do FCE - Aquicultura.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @throws Exception
	 * @since 28/04/2015
	 */
	private void prepararEdicao() {
		try {
			super.fceLancamentoEfluente = fceAquiculturaLancamento.getIdeFceLancamentoEfluente();
			listaFceOutorgaLocalizacaoAquicultura = aquiculturaServiceFacade.listarFceOutorgaLocalizacaoAquiculturaBy(fceAquiculturaLancamento);
			montarListaFceOutorgaLocalizacaoGeografica();
			if(!Util.isNullOuVazio(super.fceLancamentoEfluente.getIdeFceLancamentoEfluente())) {
				super.listarFceCaracteristicaEfluentesByFceLancamentoEfluentes();
			}
			if(navegacaoController.isAnaliseTecnica()){
				carregarListaPontoLancamentoByLancamentoEfluente();
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do FCE - Aquicultura.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 28/04/2015
	 */
	private void montarListaFceOutorgaLocalizacaoGeografica() {
		try {
			super.listaFceOutorgaLocalizacaoGeograficaLancamento = new ArrayList<FceOutorgaLocalizacaoGeografica>();
			for(FceOutorgaLocalizacaoAquicultura fceOutorgaLocalizacaoAquicultura : listaFceOutorgaLocalizacaoAquicultura){
				FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica = fceOutorgaLocalizacaoAquicultura.getIdeFceOutorgaLocalizacaoGeografica().clone();
				fceOutorgaLocalizacaoGeografica.setIdeFceOutorgaLocalizacaoGeografica(null);
				super.listaFceOutorgaLocalizacaoGeograficaLancamento.add(fceOutorgaLocalizacaoGeografica);
				for(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : listaOutorgaLocalizacaoGeografica){
					if(fceOutorgaLocalizacaoGeografica.getIdeOutorgaLocalizacaoGeografica().equals(outorgaLocalizacaoGeografica)){
						fceOutorgaLocalizacaoGeografica.setIdeOutorgaLocalizacaoGeografica(outorgaLocalizacaoGeografica);
						fceOutorgaLocalizacaoGeografica.setConfirmado(true);
						fceOutorgaLocalizacaoGeografica.setEdicao(true);
					}
				}
			}
		} catch (CloneNotSupportedException e) {
			Log4jUtil.log(this.getClass().getSimpleName(),Level.ERROR, e);
		}
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 28/04/2015
	 */
	private void prepararAbaLancamentoEfluente() {
		montarFce();
		super.prepararListaFceOutorgaLocalizacaoGeografica();
		montarFceLancamentoEfluente();
		super.prepararListaFceLancamentoEfluenteCaracteristicas();
	}

	private void montarListaFceOutorgaLocalizacaoGeograficaAquicultura(){
		if(Util.isNullOuVazio(listaFceOutorgaLocalizacaoAquicultura)){
			listaFceOutorgaLocalizacaoAquicultura = new ArrayList<FceOutorgaLocalizacaoAquicultura>();
		}
		for(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica : super.listaFceOutorgaLocalizacaoGeograficaLancamento){
			listaFceOutorgaLocalizacaoAquicultura.add(new FceOutorgaLocalizacaoAquicultura(fceOutorgaLocalizacaoGeografica, fceAquiculturaLancamento));
		}
	}

	private void montarFce() {
		super.fce = navegacaoController.getFce();
	}

	private void montarFceLancamentoEfluente() {
		super.fceLancamentoEfluente = new FceLancamentoEfluente(super.listaFceOutorgaLocalizacaoGeograficaLancamento.get(0));
	}

	private void iniciarFceAquicultura(){
		fceAquiculturaLancamento = new FceAquicultura(super.fce, super.fceLancamentoEfluente, TipoAquiculturaEnum.LANCAMENTO);
	}

	public void changePeriodoDerivacao(ValueChangeEvent event) {
		TipoPeriodoDerivacao antigoPeriodoDerivacao = (TipoPeriodoDerivacao) event.getOldValue();
		if(!Util.isNullOuVazio(antigoPeriodoDerivacao)){
			if(antigoPeriodoDerivacao.getIdeTipoPeriodoDerivacao().equals(TipoPeriodoDerivacaoEnum.INTERMITENTE.getId())){
				fceAquiculturaLancamento.setNumTempoCaptacao(null);
			}
		}
	}

	public void avancarAba(){
		if(validarAba()){
			navegacaoController.avancarAba();
		}
	}

	@Override
	public void carregarAba() {
		carregarDadosRequerimento();
		if(listaOutorgaLocGeoNotNull()){
			habilitarAbaViveiroEscavadoLancamento();
			listarCaracteristicaEfluentes();
			super.listarTipoPeriodoDerivacao();
		}
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 22/04/2015
	 */
	private void carregarDadosRequerimento() {
		super.listarOutorgaLocGeoBy(new TipoFinalidadeUsoAgua(TipoFinalidadeUsoAguaEnum.AQUICULTURA_VIVEIRO_ESCAVADO.getId()));
	}

	public void confirmarCoordenada() {
		if(super.validarCoordenadas()){
			super.fceOutorgaLocalizacaoGeograficaSelecionada.setConfirmado(true);
			super.fceOutorgaLocalizacaoGeograficaSelecionada.setIdeFce(navegacaoController.getFce());
			if(super.fceOutorgaLocalizacaoGeograficaSelecionada.isEdicao()){
				super.exibirMensagem002();
			} else {
				super.exibirMensagem001();
			}
		}
	}

	@Override
	public void finalizar() {
		if(validarAba()){
			try {
				aquiculturaServiceFacade.finalizarLancamento(this);
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("lac_dadosGerais_mensagens_erro002") + " a aba Viveiro Escavado - Lançamento.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
		else {
			navegacaoController.setActiveTab(2);
		}
	}
	
	public void prepararParaFinalizar() throws Exception{
		aquiculturaServiceFacade.finalizarFceLancamentoEfluenteToFceAquicultura(montarMapComParametros());
		salvarDocumentoAdicional();
	}

	private Map<String, Object> montarMapComParametros() {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("fce", super.fce);
		parametros.put("fceLancamentoEfluente", super.fceLancamentoEfluente);
		parametros.put("listaFceOutorgaLocalizacaoGeografica", super.listaFceOutorgaLocalizacaoGeograficaLancamento);
		parametros.put("listaFceLancamentoEfluenteCaracteristica", super.listaFceLancamentoEfluenteCaracteristicas);
		parametros.put("listaFceOutorgaLocalizacaoAquicultura", listaFceOutorgaLocalizacaoAquicultura);
		parametros.put("fceAquicultura", fceAquiculturaLancamento);
		return parametros;
	}

	private void habilitarAbaViveiroEscavadoLancamento() {
		navegacaoController.setTemLancamento(true);
	}

	public boolean isTemLancamento(){
		return navegacaoController.isTemLancamento();
	}

	public boolean isTipoPeriodoDerivacaoIntermitente(){
		return fceAquiculturaLancamento.getIdeTipoPeriodoDerivacao().isIntermitente();
	}

	@Override
	public void limpar() {
		fceAquiculturaLancamento = null;
		super.limparDadosOutorga();
	}

	/**
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @return
	 * @since 22/04/2015
	 */
	private boolean listaOutorgaLocGeoNotNull() {
		return !Util.isNullOuVazio(super.listaOutorgaLocalizacaoGeografica);
	}

	private void listarCaracteristicaEfluentes(){
		try {
			boolean COM_FOSFORO = true;
			super.listarCaracteristicaEfluente(COM_FOSFORO);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista de caraterísticas do efluente.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	public boolean validarAba(){
		boolean valido = true;
		if(!super.validarCoordenadasLancamento()){
			valido = false;
		}
		if(!super.validarDadosGenericFceLancamentoEfluente()){
			valido = false;
		}
		return valido;
	}

	public boolean isDocAdicionalLancamentoUpado() {
		return !Util.isNullOuVazio(fceAquiculturaLancamento.getUploadCaminhoArquivo());
	}

	private boolean validarDocumentoAdicional(){
		return !Util.isNull(fceAquiculturaLancamento.getFileUploadEvent()) && !Util.isNull(fceAquiculturaLancamento.getUploadCaminhoArquivo());
	}

	/**
	 * Método para realizar o Downolad do Documento Adicional de Viveiro Escavado - Lançamento
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 13/11/2014
	 * @return "nome_do_arquivo".doc
	 * @throws FileNotFoundException
	 */
	public StreamedContent getDadosAdicionaisViveiroLancamento() {
		try {
			return super.getDadosAdicionais("Informacoes_Adicionais_FCE_Outorga__Aquicultura_Viveiro_Escavado_Lancamento.doc", "Informações Adicionais - Viveiro Escavado – Lancamento.doc");
		} catch (FileNotFoundException e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_documento_adicional"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void salvarDocumentoAdicional() throws Exception {
		if(validarDocumentoAdicional()){
			super.inicarDocumentoAdicional();
			super.getDocumentoUpado().setDscCaminhoArquivo(fceAquiculturaLancamento.getUploadCaminhoArquivo().get(0));
			super.getDocumentoUpado().setFileUpload(fceAquiculturaLancamento.getFileUploadEvent());
			super.salvarDocumentoAdicional(super.requerimento, new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_OUTORGA_AQUICULTURA_ADICIONAIS.getId()));
			fceAquiculturaLancamento.setIdeDocumentoObrigatorioRequerimento(super.getDocumentoUpado());
			atualizarFceAquicultura(fceAquiculturaLancamento);
		}
	}

	protected void atualizarFceAquicultura(FceAquicultura fceAquicultura) throws Exception {
		aquiculturaServiceFacade.salvarFceAquicultura(fceAquicultura);
	}

	public void tratarUploadLancamento(FileUploadEvent event) {
		fceAquiculturaLancamento.setFileUploadEvent(event);
		fceAquiculturaLancamento.setUploadCaminhoArquivo(new ArrayList<String>());
		fceAquiculturaLancamento.getUploadCaminhoArquivo().add(getCaminhoArquivoUpado(event));
	}

	private void adicionarArquivoToDelete(String caminho) {
		if(Util.isNull(arquivosAdicionaisToDelete)) {
			arquivosAdicionaisToDelete = new ArrayList<String>();
		}
		arquivosAdicionaisToDelete.add(caminho);
	}

	public void limparCaminhoDocumentoAdicional() {
		adicionarArquivoToDelete(fceAquiculturaLancamento.getUploadCaminhoArquivo().get(0));
		fceAquiculturaLancamento.getUploadCaminhoArquivo().clear();
		fceAquiculturaLancamento.setFileUploadEvent(null);
	}

	/**
	 * Método que retornar o caminho da pasta que o {@link DocumentoObrigatorioRequerimento} vai ser salvo.
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @since 13/11/2014
	 * @param event
	 * @return String
	 */
	protected String getCaminhoArquivoUpado(FileUploadEvent event) {
		return FileUploadUtil.Enviar(event, DiretorioArquivoEnum.DOCUMENTOOBRIGATORIO.toString());
	}

	@Override
	public void verificarEdicao() {
		if(navegacaoController.isAnaliseTecnica()){
			super.carregarFceDoTecnico(DOCUMENTO_OBRIGATORIO);
		} else {
			super.carregarFceDoRequerente(DOCUMENTO_OBRIGATORIO);
		}
	}

	public void voltarAba(){
		navegacaoController.voltarAba();
	}

	public FceAquicultura getFceAquiculturaLancamento() {
		return fceAquiculturaLancamento;
	}

	public List<FceOutorgaLocalizacaoAquicultura> getListaFceOutorgaLocalizacaoAquicultura() {
		return listaFceOutorgaLocalizacaoAquicultura;
	}

	@Override
	public void abrirDialog() {
		RequestContext.getCurrentInstance().execute("outorgaAquicultura.show();");
		RequestContext.getCurrentInstance().addPartialUpdateTarget("pnlFceOutorgaAquicultura");
	}

	public void prepararAnaliseTecnica(Fce fce, AnaliseTecnica analiseTecnica){
		if(navegacaoController.isTemLancamento()){
			navegacaoController.setAnaliseTecnica(true);
			super.carregarFceDoTecnico(DOCUMENTO_OBRIGATORIO);
			if(!super.isFceSalvo()){
				navegacaoController.iniciarFceTecnico(fce, analiseTecnica);
				carregarListaPontoLancamentoByLancamentoEfluente();
				prepararDuplicacao();
				duplicarFce();
			}
			else {
				if(!existeFceAquiculturaLancamento()){
					prepararDuplicacao();
					duplicarFce();
				} 
				carregarFceTecnico();
			}
		}
	}

	/**
	 * @author eduardo (eduardo.fernandes@avansys.com.br)
	 * @param analiseTecnica
	 * @since 18/03/2016
	 * @see <a href="http://10.105.12.26/redmine/issues/">#</a> #ticket
	 */
	private void carregarListaPontoLancamentoByLancamentoEfluente() {
		try {
			super.listaFceOutorgaLocalizacaoGeograficaLancamento = super.fceOutorgaServiceFacade.listarFceOutorgaLocalizacaoGeograficaAnaliseTecnicaToOutorgaAquiculturaByIdeFce(super.fce);
			super.fceOutorgaServiceFacade.tratarPontosListaFceOutorgaLocalizacaoGeografica(listaFceOutorgaLocalizacaoGeograficaLancamento);
		}
		catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("") + ""); // inserir mensagem
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	protected void prepararDuplicacao() {
		try {
			super.fceLancamentoEfluente = super.fceLancamentoEfluente.clone();
			super.fceLancamentoEfluente.setIdeFceLancamentoEfluente(null);
			fceAquiculturaLancamento.setIdeFceLancamentoEfluente(super.fceLancamentoEfluente);
			
			fceAquiculturaLancamento = fceAquiculturaLancamento.clone();
			fceAquiculturaLancamento.setIdeFceAquicultura(null);
			fceAquiculturaLancamento.setIdeFce(navegacaoController.getFce());
			for(FceLancamentoEfluenteCaracteristica fceLancamentoEfluenteCaracteristica : super.listaFceLancamentoEfluenteCaracteristicas){
				fceLancamentoEfluenteCaracteristica.setIdeFceLancamentoEfluenteCaracteristica(null);
				fceLancamentoEfluenteCaracteristica.setIdeFceLancamentoEfluente(super.fceLancamentoEfluente);
			}
		} catch (CloneNotSupportedException e) {
			
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	@Override
	protected void duplicarFce() {
		try {
			aquiculturaServiceFacade.finalizarFceLancamentoEfluenteToFceAquicultura(montarMapComParametros());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " o FCE - Outorga para Aquicultura.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	protected void carregarFceTecnico() {
		try {
			carregarFceAquiculturaLancamento();
			if(isExisteFceAquiculturaLancamento()){
				habilitarAbaViveiroEscavadoLancamento();
				listarCaracteristicaEfluentes();
				super.listarTipoPeriodoDerivacao();
				super.fceLancamentoEfluente = fceAquiculturaLancamento.getIdeFceLancamentoEfluente();
				carregarListaPontoLancamentoByLancamentoEfluente();
				super.listarFceCaracteristicaEfluentesByFceLancamentoEfluentes();
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o FCE - Outorga para Aquicultura.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	@Override
	public boolean isDesabilitarTudo(){
		return navegacaoController.isDesabilitarTudo();
	}
}