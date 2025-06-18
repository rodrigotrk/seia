package br.gov.ba.seia.controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.controller.abstracts.BaseFceFinalidadeUsoAguaController;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.FceAbastecimentoIndustrial;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeograficaFinalidade;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.TipoPeriodoDerivacao;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.enumerator.TipoPeriodoDerivacaoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.FceNavegacaoInterface;
import br.gov.ba.seia.service.FceAbastecimentoIndustrialService;
import br.gov.ba.seia.service.TipoPeriodoDerivacaoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("fceAbastecimentoIndustrialController")
@ViewScoped
public class FceAbastecimentoIndustrialController extends BaseFceFinalidadeUsoAguaController implements FceNavegacaoInterface {

	@EJB
	private TipoPeriodoDerivacaoService tipoPeriodoDerivacaoService;
	@EJB
	private FceAbastecimentoIndustrialService fceAbastecimentoIndustrialService;

	private static final DocumentoObrigatorio DOC_OBRIGATORIO_ABS_INDUSTRIAL = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_ABASTECIMENTO_INDUSTRIAL.getId());
	private static final DocumentoObrigatorio DOC_OBRIGATORIO_ABS_INDUSTRIAL_ADICIONAIS = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_ABASTECIMENTO_INDUSTRIAL_ADICIONAIS.getId());

	private int activeTab;

	private List<OutorgaLocalizacaoGeografica> listaCaptacaoSubterranea;
	private List<OutorgaLocalizacaoGeografica> listaCaptacaoSuperficial;

	private OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica;
	private LocalizacaoGeografica localizacaoGeografica;
	private List<OutorgaLocalizacaoGeograficaFinalidade> listaOutorgaLocalizacaoGeograficaFinalidade;

	private List<TipoPeriodoDerivacao> listaTipoPeriodoDerivacao;
	private FceAbastecimentoIndustrial fceAbastecimentoIndustrial;

	@Override
	public void init(){
		verificarEdicao();
		if(!isFceSalvo()){
			prepararFce();
		} else {
			carregarFceAbastecimentoIndustrial();
		}
		carregarAba();
	}

	@Override
	public void carregarAba() {
		carregarAbaDadosRequerimento();
		carregarTipoPeriodoDerivacao();
		carregarAbaDadosAdicionais();
	}

	/**
	 * Método que verifica se já existe um FCE para aquele requerimento
	 * @param requerimento
	 * @param DOC_OBRIGATORIO_ABS_INDUSTRIAL
	 * @author marcelo.deus
	 *
	 */
	@Override
	public void verificarEdicao(){
		if(!Util.isNullOuVazio(super.requerimento)){
			carregarFceDoRequerente(DOC_OBRIGATORIO_ABS_INDUSTRIAL);
		}
	}

	/**
	 * Método que inicia o objeto do FCE com o requerimento e o DOC_OBRIGATORIO_ABS_INDUSTRIAL
	 * @param requerimento
	 * @param DOC_OBRIGATORIO_ABS_INDUSTRIAL
	 * @author marcelo.deus
	 *
	 */
	public void prepararFce(){
		if(!Util.isNullOuVazio(requerimento)){
			iniciarFce(DOC_OBRIGATORIO_ABS_INDUSTRIAL);
			fceAbastecimentoIndustrial = new FceAbastecimentoIndustrial(super.fce);
		}
	}

	/**
	 * Método que busca um FCE existente no banco caso seja Edição
	 * @param fce
	 * @author marcelo.deus
	 *
	 */
	public void carregarFceAbastecimentoIndustrial (){
		if(!Util.isNullOuVazio(super.fce)){
			try {
				fceAbastecimentoIndustrial = fceAbastecimentoIndustrialService.buscarFceAbsIndustrialByIdeFce(fce);
			} catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}

	/**
	 * Método que carrega as listas da aba de Dados do Requerimento
	 * @param requerimento
	 * @param TipoFinalidadeUsoAguaEnum
	 * @author marcelo.deus
	 */
	public void carregarAbaDadosRequerimento(){
		super.carregarListaOutorgaLocGeoByTipoFinalidadeUsoAgua(new TipoFinalidadeUsoAgua(TipoFinalidadeUsoAguaEnum.ABASTECIMENTO_INDUSTRIAL.getId()));
		separarCaptacoes();
	}

	@SuppressWarnings("unchecked")
	private void separarCaptacoes(){
		Map<String, Object> captacoes = super.separarCaptacoes(super.listaOutorgaLocalizacaoGeografica);
		listaCaptacaoSubterranea = (List<OutorgaLocalizacaoGeografica>) captacoes.get("captacoesSubterranea");
		listaCaptacaoSuperficial = (List<OutorgaLocalizacaoGeografica>) captacoes.get("captacoesSuperficial");
	}

	/**
	 * Método que carrega o documento adicionado na aba Adicionais
	 * @author marcelo.deus
	 */
	public void carregarAbaDadosAdicionais(){
		try{
			carregarDocumentoAdicionalByDocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_ABASTECIMENTO_INDUSTRIAL_ADICIONAIS);
		}
		catch(Exception e){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o Documento Adicional do FCE - Abastecimento Industrial.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que busca a lista de TipoPeriodoDerivacao com base na lista de ENUM criada no método criarListaTipoPeriodoDerivacaoEnumParBusca()
	 * @param criarListaTipoPeriodoDerivacaoEnumParBusca()
	 * @author marcelo.deus
	 */
	public void carregarTipoPeriodoDerivacao(){
		if(Util.isNullOuVazio(listaTipoPeriodoDerivacao)){
			listaTipoPeriodoDerivacao = new ArrayList<TipoPeriodoDerivacao>();
		}
		try {
			listaTipoPeriodoDerivacao = tipoPeriodoDerivacaoService.listarTipoPeriodoDerivacaoPorListaTipoPeriodoDerivacaoEnum(criarListaTipoPeriodoDerivacaoEnumParBusca());
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " a lista de Tipo Período Derivação.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que cria uma lista contendo os TipoPeriodoDerivacao utilizados no FCE, retornando uma lista que será utilizada na busca do método carregarTipoPeriodoDerivacao()
	 * @author marcelo.deus
	 * @return listaTemp
	 */
	private List<TipoPeriodoDerivacaoEnum> criarListaTipoPeriodoDerivacaoEnumParBusca(){
		List<TipoPeriodoDerivacaoEnum> listaTemp = new ArrayList<TipoPeriodoDerivacaoEnum>();
		listaTemp.add(TipoPeriodoDerivacaoEnum.CONTINUO);
		listaTemp.add(TipoPeriodoDerivacaoEnum.INTERMITENTE);
		return listaTemp;
	}

	/**
	 * Método que anula todas as variáveis presentes
	 * @author marcelo.deus
	 */
	@Override
	public void limpar(){
		if(!Util.isNullOuVazio(listaOutorgaLocalizacaoGeograficaFinalidade)){
			listaOutorgaLocalizacaoGeograficaFinalidade.clear();
		}
		if(!Util.isNullOuVazio(listaTipoPeriodoDerivacao)){
			listaTipoPeriodoDerivacao.clear();
		}
		fceAbastecimentoIndustrial = null;
		super.limparDadosOutorga();
		super.limparDocumentoUpado();
		activeTab = 0;
	}

	/**
	 * Método que imprime o relatório
	 * @param fce
	 * @param requerimento
	 * @param DOC_OBRIGATORIO_ABS_INDUSTRIAL
	 * @param String
	 * @return StreamedContent
	 * @throws Exception
	 * @author marcelo.deus
	 */
	public StreamedContent getImprimirRelatorio() {
		try {
			return getImprimirRelatorio(super.fce, DOC_OBRIGATORIO_ABS_INDUSTRIAL);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_imprimir_relatorio") + " do FCE - Abastecimento Industrial.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que imprime o documento de Informações Adicionais
	 * @param String
	 * @return StreamedContent
	 * @throws FileNotFoundException
	 * @author marcelo.deus
	 */
	public StreamedContent getDadosAdicionais() {
		try {
			return getDadosAdicionais("Informacoes_Adicionais_FCE__Abastecimento_Industrial.doc", "Informações Adicionais - FCE Captação Para Fins de Abastecimento Industrial.doc");
		} catch (FileNotFoundException e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_documento_adicional"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que valida o FCE
	 * @return boolean
	 * @author marcelo.deus
	 */
	@Override
	public boolean validarAba(){
		if(!validarAbaAbastecimento()){
			activeTab = 1;
			return false;
		}
		if(!validarAbaDadosAdicionais()){
			return false;
		}
		return true;
	}

	/**
	 * Método que valida a Aba Abastecimento Industrial
	 * @return boolean
	 * @author marcelo.deus
	 */
	public boolean validarAbaAbastecimento(){
		boolean valido=true;
		if(Util.isNullOuVazio(fceAbastecimentoIndustrial.getNumConsumoAguaIndustrializado())){
			JsfUtil.addErrorMessage("O consumo de água por unidade do produto industrializado " + Util.getString("msg_generica_null_ou_vazio"));
			valido = false;
		}

		if(Util.isNullOuVazio(fceAbastecimentoIndustrial.getIdeTipoPeriodoDerivacao())){
			JsfUtil.addErrorMessage("O período de derivação " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido=false;
		} else if(fceAbastecimentoIndustrial.getIdeTipoPeriodoDerivacao().equals(new TipoPeriodoDerivacao(TipoPeriodoDerivacaoEnum.INTERMITENTE.getId()))){
			if (Util.isNullOuVazio(fceAbastecimentoIndustrial.getNumTempoCaptacao())){
				JsfUtil.addErrorMessage("O tempo de captação " + Util.getString("msg_generica_null_ou_vazio"));
				valido = false;
			} else if (fceAbastecimentoIndustrial.getNumTempoCaptacao() > 24){
				JsfUtil.addErrorMessage(Util.getString("fce_captacao_subteranea_tempo_captacao_info_0034"));
				valido=false;
			}
		}

		return valido;
	}

	/**
	 * Método que valida a aba Adicionais
	 * @return boolean
	 * @author marcelo.deus
	 */
	public boolean validarAbaDadosAdicionais(){
		boolean valido=true;
		if(!isArquivoUpado()){
			JsfUtil.addErrorMessage(Util.getString("fce_outorga_obrigatorio_upload"));
			valido=false;
		}
		return valido;
	}

	/**
	 * Método que finaliza o FCE Abastecimento Industrial
	 * @author marcelo.deus
	 */
	@Override
	public void finalizar(){
		if(validarAba()){
			try {
				fceAbastecimentoIndustrialService.finalizar(this);
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " FCE Abastecimento Industrial.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}
	
	public void prepararParaFinalizar() throws Exception{
		super.concluirFce();
		salvarAbastecimentoIndustrial();
		salvarAbaAdicionais();
		RequestContext.getCurrentInstance().execute("abs_industrial.hide()");
		limpar();
		if(isFceSalvo()){
			super.exibirMensagem002();
		} else {
			super.exibirMensagem001();
		}
		RequestContext.getCurrentInstance().execute("rel_fce_abs_industrial.show()");
		activeTab = 0;
	}

	/**
	 * Método que salva as informações do FCE Abastecimento Industrial no banco
	 * @return String
	 * @throws Exception
	 * @author marcelo.deus
	 */
	public String salvarAbastecimentoIndustrial() throws Exception{
		String msg = null;
		if(!Util.isNullOuVazio(fceAbastecimentoIndustrial)){
			super.salvarFce();
			changeDerivacao();
			if(Util.isNullOuVazio(fceAbastecimentoIndustrial.getIdeFceAbastecimentoIndustrial())){
				msg=Util.getString("msg_generica_inclusao_efetuada");
			}
			else {
				msg=Util.getString("msg_generica_alteracao_efetuada");
			}
			fceAbastecimentoIndustrialService.salvarFceAbastecimentoIndustrial(fceAbastecimentoIndustrial);
		} else {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " FCE Abastecimento Industrial.");
		}
		return msg;
	}

	/**
	 * Método que valida e salva apenas a aba Abastecimento Industrial
	 * @author marcelo.deus
	 */
	public void validarESalvarAbaAbastecimentoIndustrial(){
		if(validarAbaAbastecimento()){
			try {
				avancarAba();
				JsfUtil.addSuccessMessage(salvarAbastecimentoIndustrial());
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " FCE Abastecimento Industrial.");
			}
		}
	}

	/**
	 * Método que salva apenas a aba Adicionais
	 * @author marcelo.deus
	 */
	private void salvarAbaAdicionais(){
		try {
			salvarDocumentoAdicional(requerimento, DOC_OBRIGATORIO_ABS_INDUSTRIAL_ADICIONAIS);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar_documento_adicional"));
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);

		}
	}

	/**
	 * Método que verifica a existência de TipoPeriodoDerivacao "Intermitente"
	 * @return boolean
	 * @author marcelo.deus
	 */
	public boolean isContemDerivacaoIntermitente(){
		return !Util.isNull(fceAbastecimentoIndustrial) && fceAbastecimentoIndustrial.getIdeTipoPeriodoDerivacao().isIntermitente();
	}

	/**
	 * Método que seta a Aba ativa de acordo com o clique do usuário na tela
	 * @param activeTab
	 * @param event
	 * @author marcelo.deus
	 */
	@Override
	public void controlarAbas(TabChangeEvent event) {
		if("dadosRequerimento".equals(event.getTab().getId())){
			activeTab = 0;
		}
		else if("abastecimento".equals(event.getTab().getId())){
			activeTab = 1;
		}
		else if("dadosAdicionais".equals(event.getTab().getId())){
			activeTab = 2;
		}
	}

	/**
	 * Método que avança uma aba através do clique do botão "Avançar"
	 * @param activeTab
	 * @author marcelo.deus
	 */
	@Override
	public void avancarAba(){
		activeTab++;
	}

	/**
	 * Método que retorna uma aba através do clique do botão "Voltar"
	 * @param activeTab
	 * @author marcelo.deus
	 */
	@Override
	public void voltarAba(){
		activeTab--;
	}

	/**
	 * Método utilizado para renderizar o campo "Tempo de Captação" na tela
	 * @author marcelo.deus
	 */
	public void changeDerivacao (){
		if(!Util.isNullOuVazio(fceAbastecimentoIndustrial.getIdeTipoPeriodoDerivacao()) && !fceAbastecimentoIndustrial.getIdeTipoPeriodoDerivacao().getIdeTipoPeriodoDerivacao().equals(TipoPeriodoDerivacaoEnum.INTERMITENTE.getId())){
			this.fceAbastecimentoIndustrial.setNumTempoCaptacao(null);
		}
	}

	// GETS & SETS
	public int getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}

	public OutorgaLocalizacaoGeografica getOutorgaLocalizacaoGeografica() {
		return outorgaLocalizacaoGeografica;
	}

	public void setOutorgaLocalizacaoGeografica(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica) {
		this.outorgaLocalizacaoGeografica = outorgaLocalizacaoGeografica;
	}

	public LocalizacaoGeografica getLocalizacaoGeografica() {
		return localizacaoGeografica;
	}

	public void setLocalizacaoGeografica(LocalizacaoGeografica localizacaoGeografica) {
		this.localizacaoGeografica = localizacaoGeografica;
	}

	public List<OutorgaLocalizacaoGeograficaFinalidade> getListaOutorgaLocalizacaoGeograficaFinalidade() {
		return listaOutorgaLocalizacaoGeograficaFinalidade;
	}

	public void setListaOutorgaLocalizacaoGeograficaFinalidade(List<OutorgaLocalizacaoGeograficaFinalidade> listaOutorgaLocalizacaoGeograficaFinalidade) {
		this.listaOutorgaLocalizacaoGeograficaFinalidade = listaOutorgaLocalizacaoGeograficaFinalidade;
	}

	public List<TipoPeriodoDerivacao> getListaTipoPeriodoDerivacao() {
		return listaTipoPeriodoDerivacao;
	}

	public void setListaTipoPeriodoDerivacao(List<TipoPeriodoDerivacao> listaTipoPeriodoDerivacao) {
		this.listaTipoPeriodoDerivacao = listaTipoPeriodoDerivacao;
	}

	public FceAbastecimentoIndustrial getFceAbastecimentoIndustrial() {
		return fceAbastecimentoIndustrial;
	}

	public void setFceAbastecimentoIndustrial(FceAbastecimentoIndustrial fceAbastecimentoIndustrial) {
		this.fceAbastecimentoIndustrial = fceAbastecimentoIndustrial;
	}

	public List<OutorgaLocalizacaoGeografica> getListaCaptacaoSubterranea() {
		return listaCaptacaoSubterranea;
	}

	public List<OutorgaLocalizacaoGeografica> getListaCaptacaoSuperficial() {
		return listaCaptacaoSuperficial;
	}

	@Override
	public void abrirDialog() {
		RequestContext.getCurrentInstance().addPartialUpdateTarget("pnlFceAbastecimentoIndustrial");
		RequestContext.getCurrentInstance().addPartialUpdateTarget("dlgImprimirRelatorioAbastecimentoIndustrial");
		RequestContext.getCurrentInstance().execute("abs_industrial.show();");
	}

	@Override
	protected void prepararDuplicacao() {
		fceAbastecimentoIndustrial.setIdeFceAbastecimentoIndustrial(null);
		fceAbastecimentoIndustrial.setIdeFce(super.fce);
	}

	@Override
	protected void duplicarFce() {
		try {
			salvarAbastecimentoIndustrial();
			super.carregarListaFceOutorgaLocalizacaoGeografica(TipoFinalidadeUsoAguaEnum.ABASTECIMENTO_INDUSTRIAL);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " o FCE - Abastecimento Industrial.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	protected void carregarFceTecnico() {
		try {
			carregarFceAbastecimentoIndustrial();
			carregarTipoPeriodoDerivacao();
			carregarAbaDadosAdicionais();
			super.carregarListaFceOutorgaLocalizacaoGeografica(TipoFinalidadeUsoAguaEnum.ABASTECIMENTO_INDUSTRIAL);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o FCE - Abastecimento Industrial.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
}