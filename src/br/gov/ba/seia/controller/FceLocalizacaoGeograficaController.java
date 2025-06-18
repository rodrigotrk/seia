package br.gov.ba.seia.controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.EnquadramentoDocumentoAto;
import br.gov.ba.seia.entity.FceLocalizacaoGeografica;
import br.gov.ba.seia.entity.FceLocalizacaoGeograficaPK;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.ObjetivoAtividadeManejo;
import br.gov.ba.seia.entity.Outorga;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.ModalidadeOutorgaEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.FceNavegacaoInterface;
import br.gov.ba.seia.service.DocumentoObrigatorioRequerimentoService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.EnquadramentoService;
import br.gov.ba.seia.service.FceLocalizacaoGeograficaService;
import br.gov.ba.seia.service.LocalizacaoGeograficaService;
import br.gov.ba.seia.service.OutorgaLocalizacaoGeograficaService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * FCE - <b>APPO</b> [<i>Perfuração de Poço</i>]
 */
@Named("fceLocalizacaoGeograficaController")
@ViewScoped
public class FceLocalizacaoGeograficaController extends FceComDocumentoAdicionalController implements FceNavegacaoInterface {

	@Inject
	private LocalizacaoGeograficaGenericController locGeoController;
	@EJB
	private DocumentoObrigatorioRequerimentoService docObrigatorioRequerimentoService;
	@EJB
	private EmpreendimentoService empreendimentoService;
	@EJB
	private FceLocalizacaoGeograficaService fceLocalizacaoGeograficaService;
	@EJB
	private LocalizacaoGeograficaService localizacaoGeograficaService;
	@EJB
	private OutorgaLocalizacaoGeograficaService outorgaLocalizacaoGeograficaService;
	@EJB
	private EnquadramentoService enquadramentoService;	
	
	

	private static final DocumentoObrigatorio DOCUMENTO_OBRIGATORIO_PERFURACAO_POCO = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_LOCALIZACAO_GEOGRAFICA.getId());
	private static final DocumentoObrigatorio DOCUMENTO_OBRIGATORIO_PERFURACAO_POCO_ADICIONAIS = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_LOCALIZACAO_GEOGRAFICA_DADOS_ADICIONAIS.getId());
	
	private int activeTab;
	private List<FceLocalizacaoGeografica> listaFceLocGeo;
	private FceLocalizacaoGeografica fceLocalizacaoGeografica;
	private FceLocalizacaoGeografica fceLocalizacaoGeograficaSelecionada;
	
	boolean blockPocoAdd;
	
	boolean alteracao;
	
	boolean podeFinalizar;

	@Override
	public void init(){
		carregarAba();
		verificarEdicao();
		Collection<EnquadramentoDocumentoAto> atos = enquadramentoService.listarAtosEnquadradosByRequerimento(requerimento.getIdeRequerimento());
		for (EnquadramentoDocumentoAto ato : atos) {
			if(ato.getDocumentoAto().getIdeAtoAmbiental().getIdeAtoAmbiental()==54) {
				blockPocoAdd=true;
			}
		}
		if(!super.isFceSalvo()){
			iniciarFce(DOCUMENTO_OBRIGATORIO_PERFURACAO_POCO);
			criarListaFceOutorgaLocGeoByListaOutorgaLocGeo();
		}
		else{
			carregarlistaFceLocalizacaoGeografica();
			carregarUploadAbaDadosAdicionais();
			alteracao = true;
		}
		tratarPontos();
	}
	
	public void preparaLocalizacaoGeografica(ActionEvent event){
		iniciarFceLocalizacaoGeografica();
		fceLocalizacaoGeografica.setIdeFce(super.fce);
		fceLocalizacaoGeografica.setIdeLocalizacaoGeografica(new LocalizacaoGeografica());
		
		locGeoController.limparLocalizacaoGeografSelecionada();
		locGeoController.setIdDoComponenteParaSerAtualizado(getPanelToUpdate());
		locGeoController.setTipoSecaoGeometrica(getSomentePonto());
		locGeoController.setRequerimento(super.requerimento.getIdeRequerimento());
		locGeoController.setLocalizacaoGeograficaSelecionada(fceLocalizacaoGeografica.getIdeLocalizacaoGeografica());
		locGeoController.setObjetoLocalizacao(fceLocalizacaoGeografica);
		locGeoController.carregarTela();
		
		adicionar();
	}

	public String getPanelToUpdate() {
		return "formTabPerfuracaoPoco:tabAbasId_fcePerfuracaoPoco:pnl2";
	}

	public Integer getSomentePonto() {
		return ClassificacaoSecaoEnum.CLASSIFICACAO_SECAO_PONTO.getId().intValue();
	}

	private void adicionar(){
		listaFceLocGeo.add(fceLocalizacaoGeografica);
	}

	/**
	 * salvar os dados no arraylist ListaFceLocGeo
	 *
	 */
	public void guardaLista(LocalizacaoGeografica localizacaoGeograficaSelecionada){
		if(super.isFceSalvo()){
			fceLocalizacaoGeografica.setIdeFceLocalizacaoGeograficaPK(new FceLocalizacaoGeograficaPK(super.fce.getIdeFce(), localizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica()));
			fceLocalizacaoGeografica.setIdeFce(super.fce);
			fceLocalizacaoGeografica.setIdeLocalizacaoGeografica(localizacaoGeograficaSelecionada);
			iniciarFceLocalizacaoGeografica();
		}
	}

	/** Metodo para salvar dados da fce localizacao geografica,
	 *  os dados do novo requerimento não serão salvos
	 * @throws Exception 
	 */
	public void salvarFceLocalizacaoGeografica() {
		List<OutorgaLocalizacaoGeografica> listaOutorgaLocalizacaoGeografica = listarOutorgaLocalizacaoGeografica();
		Outorga outorga = null;
		podeFinalizar=false;
		for(FceLocalizacaoGeografica fceLocalizacaoGeograficaObj : listaFceLocGeo){		
			for(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : listaOutorgaLocalizacaoGeografica) {
				if(outorga==null) {
					outorga = outorgaLocalizacaoGeografica.getIdeOutorga();
				}
				if(fceLocalizacaoGeograficaObj.getIdeLocalizacaoGeografica().equals(outorgaLocalizacaoGeografica.getIdeLocalizacaoGeografica())) {
					outorgaLocalizacaoGeografica.setNumExpectativaCaptacao(fceLocalizacaoGeograficaObj.getNumExpectativaCaptacao());
					outorgaLocalizacaoGeografica.setIndConcedido(fceLocalizacaoGeograficaObj.isIndConcedido());
					outorgaLocalizacaoGeograficaService.salvarAtualizar(outorgaLocalizacaoGeografica);
				}
			}
			//se isNovoRequerimento for false ira add no banco de dados
			if(!fceLocalizacaoGeograficaObj.isNovoRequerimento()){
				prepararFceLocalizacaoGeograficaPk(fceLocalizacaoGeograficaObj);
				fceLocalizacaoGeograficaService.salvarOuAtualizartFceLocalizacaoGeograficaPerfuracaoPoco(fceLocalizacaoGeograficaObj);
				OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica = new OutorgaLocalizacaoGeografica(fceLocalizacaoGeograficaObj.getIdeLocalizacaoGeografica());
				outorgaLocalizacaoGeografica.setIdeOutorga(outorga);
				outorgaLocalizacaoGeograficaService.salvarAtualizar(outorgaLocalizacaoGeografica);
			}
		}
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @param fceLocalizacaoGeografica
	 * @since 22/01/2016
	 */
	private void prepararFceLocalizacaoGeograficaPk(FceLocalizacaoGeografica fceLocalizacaoGeografica) {
		Integer ideFce = fceLocalizacaoGeografica.getIdeFce().getIdeFce();
		Integer ideLocalizacaoGeografica = fceLocalizacaoGeografica.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica();
		FceLocalizacaoGeograficaPK fceLocalizacaoGeograficaPK = new FceLocalizacaoGeograficaPK(ideFce, ideLocalizacaoGeografica);
		fceLocalizacaoGeografica.setIdeFceLocalizacaoGeograficaPK(fceLocalizacaoGeograficaPK);
	}

	@Override
	public void limpar(){
		fceLocalizacaoGeografica = null;
		fceLocalizacaoGeograficaSelecionada = null;
		listaFceLocGeo = null;
		activeTab = 0;
		alteracao = false;
		super.limparDocumentoUpado();
	}

	private void carregarlistaFceLocalizacaoGeografica(){
		try{
			criarListaFceOutorgaLocGeoByListaOutorgaLocGeo();
			listaFceLocGeo.addAll(fceLocalizacaoGeograficaService.listarFceLocalizacaoGeograficaPerfuracaoPocoByIdeFce(super.fce));
		}catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public void tratarPontos(){
		try {
			for(FceLocalizacaoGeografica fceLocalizacaoGeograficaObj : listaFceLocGeo){
				locGeoController.tratarPonto(fceLocalizacaoGeograficaObj.getIdeLocalizacaoGeografica());
			}
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as Informações do Geobahia.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void iniciarFceLocalizacaoGeografica(){
		fceLocalizacaoGeografica = new FceLocalizacaoGeografica();
	}

	@Override
	public void finalizar(){
		if(isAbasValidas()){
			try {			
				fceLocalizacaoGeograficaService.finalizar(this);
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o FCE - APPO.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}
	
	public void prepararParaFinalizar() throws Exception{
		if(podeFinalizar) {
			super.concluirFce();
		}else {
			if(fce.isIndConcluido()) {
				fce.setIndConcluido(false);
				fceServiceFacade.salvarFce(fce);			
			}
		}
		salvarAbaPoco();
		salvarAbaAdicionais();
		exibirMensagem();
		fecharDialogs();
		limpar();
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 22/01/2016
	 */
	private void exibirMensagem() {
		if(alteracao ){
			super.exibirMensagem002();
		}
		else{
			super.exibirMensagem001();
		}
	}
	
	private boolean isAbasValidas(){
		if(validarAbaPoco() && validarAba()){
			return true;
		} 
		else if(!validarAbaPoco()){
			activeTab = 0;
		}
		return false;
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @since 22/01/2016
	 */
	private void fecharDialogs() {
		RequestContext.getCurrentInstance().execute("relatorio_perfuracao_poco.show()");
		RequestContext.getCurrentInstance().execute("fce_perfucacao_poco.hide()");
	}

	@Override
	public boolean validarAba() {
		if(!super.isArquivoUpado()) {
			JsfUtil.addErrorMessage(Util.getString("fce_localizacao_geografica_perfuracao_dado_obrigatorio"));
			return false;
		}
		return true;
	}

	public boolean validarAbaPoco(){
		if(Util.isNullOuVazio(listaFceLocGeo) && blockPocoAdd==false){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_preenchimento_obrigatorio_localizacao_geografica_inf_32"));
			return false;
		}
		for (FceLocalizacaoGeografica fceLocalizacaoGeografica : listaFceLocGeo) {
			if(fceLocalizacaoGeografica.isIndConcedido()==true) {
				podeFinalizar=true;
			}
			if(!fceLocalizacaoGeografica.isConfirmado()) {
				JsfUtil.addErrorMessage("Os valores das expectativas de vazão precisam ser confirmados");
				return false;
			}
		}
		return true;
	}

	@Override
	public void controlarAbas(TabChangeEvent event) {
		if ("abaPerfuracaoPoco".equals(event.getTab().getId())) {
			activeTab = 0;
		} 
		else if ("abaAdicionais".equals(event.getTab().getId())) {
			activeTab = 1;
		}
	}

	/**
	 * metodo para excluir da tabela Fce Localizacao Geografica e
	 * da Localizacao Geografica e Dado Geografico
	 */
	public void excluirPerfuracaoPoco(){
		try {
			if(!Util.isNull(fceLocalizacaoGeograficaSelecionada.getIdeFceLocalizacaoGeograficaPK())){
				fceLocalizacaoGeograficaService.excluirFceLocalizacaoGeografica(fceLocalizacaoGeograficaSelecionada);
			}
			localizacaoGeograficaService.excluirByIdLocalizacaoGeografica(fceLocalizacaoGeograficaSelecionada.getIdeLocalizacaoGeografica());
			listaFceLocGeo.remove(fceLocalizacaoGeograficaSelecionada);
			super.exibirMensagem005();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}

		iniciarFceLocalizacaoGeografica();
	}

	/**
	 *Verificar se FCE já existe
	 */
	@Override
	public void verificarEdicao() {
		super.carregarFceDoRequerente(DOCUMENTO_OBRIGATORIO_PERFURACAO_POCO);
	}

	private void salvarAbaAdicionais(){
		try {
			super.salvarDocumentoAdicional(requerimento, DOCUMENTO_OBRIGATORIO_PERFURACAO_POCO_ADICIONAIS);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar_documento_adicional"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	private void carregarUploadAbaDadosAdicionais(){
		try{
			super.carregarDocumentoAdicionalByDocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_LOCALIZACAO_GEOGRAFICA_DADOS_ADICIONAIS);
		}catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o Documento Adicional do FCE - APPO.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public List<OutorgaLocalizacaoGeografica> listarOutorgaLocalizacaoGeografica(){
		try{
			return outorgaLocalizacaoGeograficaService.listarOutorgaLocalizacaoGeograficaComDadoGeograficoByModalidadeOutorga(ModalidadeOutorgaEnum.POCO, super.requerimento);
		}catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Pontos cadastrados no Requerimento.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 *lista de outorga Localizacao Geografica add na lista principal listaFceLocGeo
	 */
	private void criarListaFceOutorgaLocGeoByListaOutorgaLocGeo() {
		listaFceLocGeo = new ArrayList<FceLocalizacaoGeografica>();
		for (OutorgaLocalizacaoGeografica outorgaLocGeo : listarOutorgaLocalizacaoGeografica()) {
			if(outorgaLocGeo.getNumExpectativaCaptacao()==null) {
				listaFceLocGeo.add(new FceLocalizacaoGeografica(super.fce, outorgaLocGeo.getIdeLocalizacaoGeografica(), true,true));
			}else {
				listaFceLocGeo.add(new FceLocalizacaoGeografica(super.fce, outorgaLocGeo.getIdeLocalizacaoGeografica(), true,true,outorgaLocGeo.getNumExpectativaCaptacao(),outorgaLocGeo.getIndConcedido()));				
			}
		}
		if (listaFceLocGeo.isEmpty()) {
			blockPocoAdd = false;
		}
	}

	// Download do arquivo
	public StreamedContent getDadosAdicionais() {
		try {
			return super.getDadosAdicionais("Informacoes_Adicionais_FCE_APPO.doc", "Informações Adicionais - FCE Perfuração do Poço.doc");
		} catch (FileNotFoundException e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_documento_adicional"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public StreamedContent getImprimirRelatorio() throws Exception {
		try {
			return super.getImprimirRelatorio(super.fce, DOCUMENTO_OBRIGATORIO_PERFURACAO_POCO);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_imprimir_relatorio") + " do FCE - APPO.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	public boolean isSemPontoAdicionado(){
		return Util.isNullOuVazio(listaFceLocGeo);
	}

	@Override
	public void voltarAba(){
		activeTab--;
	}

	@Override
	public void avancarAba(){
		if(validarAbaPoco()){
			activeTab++;
			salvarAbaPoco();
		}
	}

	/**
	 * @author eduardo (eduardo.fernandes@zcr.com.br)
	 * @throws Exception
	 * @since 22/01/2016
	 */
	private void salvarAbaPoco() {
		try {
			if(!super.isFceSalvo()){
				super.salvarFce();
			}
			salvarFceLocalizacaoGeografica();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " a aba Poço.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
		
	}

	public int getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}

	@Override
	public Requerimento getRequerimento() {
		return requerimento;
	}

	@Override
	public void setRequerimento(Requerimento requerimento) {
		this.requerimento = requerimento;
	}

	public List<FceLocalizacaoGeografica> getListaFceLocGeo() {
		return listaFceLocGeo;
	}

	public void setListaFceLocGeo(List<FceLocalizacaoGeografica> listaFceLocGeo) {
		this.listaFceLocGeo = listaFceLocGeo;
	}

	public FceLocalizacaoGeografica getFceLocalizacaoGeograficaSelecionada() {
		return fceLocalizacaoGeograficaSelecionada;
	}

	public void setFceLocalizacaoGeograficaSelecionada(FceLocalizacaoGeografica fceLocalizacaoGeograficaSelecionada) {
		this.fceLocalizacaoGeograficaSelecionada = fceLocalizacaoGeograficaSelecionada;
	}

	@Override
	public void carregarAba() {
		listarOutorgaLocalizacaoGeografica();
	}

	@Override
	public void abrirDialog() {
		RequestContext.getCurrentInstance().addPartialUpdateTarget("pnlFcePerfuracaoPoco");
		RequestContext.getCurrentInstance().addPartialUpdateTarget("dlgImprimirRelatorioPerfuracaoPoco");
		RequestContext.getCurrentInstance().execute("fce_perfucacao_poco.show();");
	}

	@Override
	protected void prepararDuplicacao() {
		for(FceLocalizacaoGeografica fceLocalizacaoGeografica : listaFceLocGeo){
			fceLocalizacaoGeografica.setIdeFceLocalizacaoGeograficaPK(new FceLocalizacaoGeograficaPK(super.fce.getIdeFce(), fceLocalizacaoGeografica.getIdeLocalizacaoGeografica().getIdeLocalizacaoGeografica()));
			fceLocalizacaoGeografica.setIdeFce(super.fce);
			fceLocalizacaoGeografica.setIdeLocalizacaoGeografica(fceLocalizacaoGeografica.getIdeLocalizacaoGeografica());			
		}
	}

	@Override
	protected void duplicarFce() {
		try {
			super.salvarFce();
			salvarFceLocalizacaoGeografica();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " APPO.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	protected void carregarFceTecnico() {
		carregarAba();
		carregarlistaFceLocalizacaoGeografica();
		carregarUploadAbaDadosAdicionais();
		tratarPontos();
	}
	
	public boolean isBlockPocoAdd() {
		return blockPocoAdd;
	}
	
	public void editarVazao(){
		if(!fceLocalizacaoGeograficaSelecionada.isConfirmado()) {
			fceLocalizacaoGeograficaSelecionada.setConfirmado(true);
			
		}else {
			fceLocalizacaoGeograficaSelecionada.setConfirmado(false);
		}
	}

}