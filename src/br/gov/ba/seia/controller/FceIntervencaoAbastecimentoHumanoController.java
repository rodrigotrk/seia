package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.controller.abstracts.BaseFceOutorgaSemDocumentoAdicionalController;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.FceAbastecimentoHumano;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.entity.Outorga;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeograficaFinalidade;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.TipoPeriodoDerivacao;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.enumerator.TipoPeriodoDerivacaoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.FceNavegacaoInterface;
import br.gov.ba.seia.service.FceIntervencaoAbastecimentoHumanoService;
import br.gov.ba.seia.service.TipoPeriodoDerivacaoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("fceIntervencaoAbastecimentoHumanoController")
@ViewScoped
public class FceIntervencaoAbastecimentoHumanoController extends BaseFceOutorgaSemDocumentoAdicionalController implements FceNavegacaoInterface {

	@EJB
	private FceIntervencaoAbastecimentoHumanoService fceIntervencaoAbastecimentoHumanoService;
	@EJB
	private TipoPeriodoDerivacaoService tipoPeriodoDerivacaoService;

	private static DocumentoObrigatorio DOC_OBRIGATORIO_ABS_HUMANO = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_OUTORGA_INTERVENCAO_ABASTECIMENTO_HUMANO.getId());

	private static final TipoPeriodoDerivacao TIPO_PERIODO_DERIVACAO_INTERMITENTE = new TipoPeriodoDerivacao(TipoPeriodoDerivacaoEnum.INTERMITENTE.getId());

	private static final TipoPeriodoDerivacao TIPO_PERIODO_DERIVACAO_CONTINUO = new TipoPeriodoDerivacao(TipoPeriodoDerivacaoEnum.CONTINUO.getId());

	private Outorga outorga;

	private FceAbastecimentoHumano fceAbastecimentoHumano;

	/**
	 * Aba Dados do Requerimento
	 */
	private int activeTab;

	private OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica;
	private LocalizacaoGeografica localizacaoGeografica;
	private List<OutorgaLocalizacaoGeograficaFinalidade> listaOutorgaLocalizacaoGeograficaFinalidade;
	/**
	 * Fim da Aba Dados Requerimento
	 */
	/**
	 * Aba Abastecimento
	 */
	private List<TipoPeriodoDerivacao> listaTipoPeriodoDerivacao;
	/**
	 * Fim da Aba Abastecimento
	 */
	@Override
	public void init(){
		verificarEdicao();
		if(!isFceSalvo()){
			iniciarFce(DOC_OBRIGATORIO_ABS_HUMANO);
			fceAbastecimentoHumano = new FceAbastecimentoHumano(super.fce);
		} else {
			carregarFceAbsHumano();
		}
		carregarAba();
	}

	@Override
	public void carregarAba() {
		carregarAbaDadosRequerimento();
		carregarAbaAbastecimento();
	}

	/**
	 * Método que verifica se já existe um FCE para aquele requerimento
	 * @param requerimento
	 * @param DOC_OBRIGATORIO_ABS_HUMANO
	 * @author marcelo.deus
	 *
	 */
	@Override
	public void verificarEdicao(){
		if(!Util.isNullOuVazio(requerimento) && !isFceTecnico()){
			carregarFceDoRequerente(DOC_OBRIGATORIO_ABS_HUMANO);
		}
	}

	/**
	 * Método que busca um FCE existente no banco caso seja Edição
	 * @param fce
	 * @author marcelo.deus
	 *
	 */
	public void carregarFceAbsHumano (){
		if(!Util.isNullOuVazio(super.fce)){
			try {
				fceAbastecimentoHumano = fceIntervencaoAbastecimentoHumanoService.buscarFceAbsHumanoByIdeFce(super.fce);
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
		super.carregarListaOutorgaLocGeoByTipoFinalidadeUsoAgua(new TipoFinalidadeUsoAgua(TipoFinalidadeUsoAguaEnum.ABASTECIMENTO_HUMANO.getId()));
		super.separarCaptacoes();
	}

	/**
	 * Método que carrega os elementos da aba Abastecimento
	 * @author marcelo.deus
	 */
	public void carregarAbaAbastecimento(){
		carregarTipoPeriodoDerivacao();
		calculaConsumoAtual();
	}

	/**
	 * Método que busca a lista de TipoPeriodoDerivacao com base na lista de ENUM criada no método criarListaTipoPeriodoDerivacaoEnumParBusca()
	 * @param criarListaTipoPeriodoDerivacaoEnumParBusca()
	 * @author marcelo.deus
	 */
	public void carregarTipoPeriodoDerivacao(){
		listaTipoPeriodoDerivacao = new ArrayList<TipoPeriodoDerivacao>();
		try {
			listaTipoPeriodoDerivacao = tipoPeriodoDerivacaoService.listarTipoPeriodoDerivacaoPorListaTipoPeriodoDerivacaoEnum(criarListaTipoPeriodoDerivacaoEnumParBusca());
		} catch (Exception e) {
			JsfUtil.addErrorMessage("Erro ao carregar a lista de Tipo PerÃ­odo Derivação.");
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
	 * Método que verifica a existÃªncia de TipoPeriodoDerivacao "Intermitente"
	 * @return boolean
	 * @author marcelo.deus
	 */
	public boolean isDerivacaoIntermitente(){
		return isContemDerivacao() && fceAbastecimentoHumano.getIdeTipoPeriodoDerivacao().equals(TIPO_PERIODO_DERIVACAO_INTERMITENTE);
	}

	public boolean isContemDerivacao(){
		return !Util.isNullOuVazio(fceAbastecimentoHumano.getIdeTipoPeriodoDerivacao());
	}

	/**
	 * Método que calcula o valor do Consumo Atual na aba Abastecimento
	 * @author marcelo.deus
	 */
	public void calculaConsumoAtual(){
		if(!Util.isNullOuVazio(fceAbastecimentoHumano) ){
			if(!Util.isNullOuVazio(fceAbastecimentoHumano.getNumPopulacaoAtual()) && !Util.isNullOuVazio(fceAbastecimentoHumano.getNumConsumoPerCapita())){
				BigDecimal tempMult = new BigDecimal(fceAbastecimentoHumano.getNumPopulacaoAtual());
				fceAbastecimentoHumano.setNumConsumoAtual(tempMult.multiply(fceAbastecimentoHumano.getNumConsumoPerCapita()).divide(new BigDecimal(1000)));
			}
			else {
				fceAbastecimentoHumano.setNumConsumoAtual(null);
			}
		}
	}

	/**
	 * Método que salva o FCE Abastecimento Humano no banco
	 * @author marcelo.deus
	 */
	public void salvarFceAbastecimentoHumano() {
		fceIntervencaoAbastecimentoHumanoService.salvarFceAbastecimentoHumano(fceAbastecimentoHumano);
	}

	/**
	 * Método que valida a Aba Abastecimento
	 * @return boolean
	 * @author marcelo.deus
	 */
	@Override
	public boolean validarAba(){
		boolean valido=true;

		if(Util.isNullOuVazio(fceAbastecimentoHumano.getNumPopulacaoAtual())){
			valido=false;
			JsfUtil.addErrorMessage("O campo de população atual é de preenchimento obrigatório.");
		}
		if(Util.isNullOuVazio(fceAbastecimentoHumano.getNumConsumoPerCapita())){
			valido=false;
			JsfUtil.addErrorMessage("O campo de consumo per capta é de preenchimento obrigatório.");
		}
		if(Util.isNullOuVazio(fceAbastecimentoHumano.getNumHorizonteProjeto())){
			valido=false;
			JsfUtil.addErrorMessage("O campo de horizonte do projeto é de preenchimento obrigatório.");
		}
		if(Util.isNullOuVazio(fceAbastecimentoHumano.getNumPopulacaoProjetada())){
			valido=false;
			JsfUtil.addErrorMessage("O campo de população projetada é de preenchimento obrigatório.");
		}
		if(Util.isNullOuVazio(fceAbastecimentoHumano.getNumConsumoProjetado())){
			valido=false;
			JsfUtil.addErrorMessage("O campo de consumo projetado é de preenchimento obrigatório.");
		}
		if(!isContemDerivacao()){
			valido=false;
			JsfUtil.addErrorMessage("A seleção de um perÃ­odo de derivação é obrigatória.");
		} else if(isDerivacaoIntermitente()){
			if(Util.isNullOuVazio(fceAbastecimentoHumano.getNumTempoCaptacao())){
				valido=false;
				JsfUtil.addErrorMessage("O campo de tempo de captação é de preenchimento obrigatório.");
			}
			else if(fceAbastecimentoHumano.getNumTempoCaptacao() >= 25){
				valido=false;
				JsfUtil.addErrorMessage("O tempo de captação não pode ser maior que 24h/dia.");
			}
		}
		return valido;
	}

	/**
	 * Método chamado ao clicar no botão Finalizar do FCE Abastecimento Humano
	 * @author marcelo.deus
	 */
	@Override
	public void finalizar(){
		if(validarAba()){
			try {
				fceIntervencaoAbastecimentoHumanoService.finalizar(this);
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o FCE - Abastecimento Humano.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}
	
	public void prepararParaFinalizar() throws Exception{
		super.concluirFce();
		salvarFceAbastecimentoHumano();
		JsfUtil.addSuccessMessage("Finalizado com sucesso.");
		RequestContext.getCurrentInstance().execute("abs_humano.hide()");
		limpar();
		RequestContext.getCurrentInstance().execute("rel_fce_abs_humano.show()");
	}

	/**
	 * Método que anula todas as variáveis presentes
	 * @author marcelo.deus
	 */
	@Override
	public void limpar(){
		activeTab = 0;
		fceAbastecimentoHumano = null;
		super.limparDadosOutorga();
	}

	/**
	 * Método que imprime o relatório
	 * @param fce
	 * @param requerimento
	 * @param DocumentoObrigatorioEnum
	 * @param String
	 * @return StreamedContent
	 * @throws Exception
	 * @author marcelo.deus
	 */
	public StreamedContent getImprimirRelatorio() {
		try {
			return getImprimirRelatorio(super.fce, DOC_OBRIGATORIO_ABS_HUMANO);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_imprimir_relatorio") + " do FCE - Abastecimento Humano.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método utilizado para renderizar o campo "Tempo de Captação" na tela
	 * @author marcelo.deus
	 */
	public void changeDerivacao(){
		if(fceAbastecimentoHumano.getIdeTipoPeriodoDerivacao().equals(TIPO_PERIODO_DERIVACAO_CONTINUO)){
			fceAbastecimentoHumano.setNumTempoCaptacao(null);
		}
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
			setActiveTab(0);
		}
		else if("abastecimento".equals(event.getTab().getId())){
			setActiveTab(1);
		}
	}

	/**
	 * Método que avançaa uma aba através do clique do botão "Avançar"
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
	 * Método auxiliar que transforma um valor String em um Int
	 * @param tmp
	 * @return int
	 * @author marcelo.deus
	 */
	public int transformaStringEmInt(String tmp){
		tmp=tmp.replace(".", "");
		return Integer.parseInt(tmp);
	}

	/**
	 * Método auxiliar que transforma um valor int em uma String
	 * @param tmp
	 * @return String
	 * @author marcelo.deus
	 */
	public String transformaIntEmString(Integer tmp){
		return tmp.toString();
	}

	//GETS & SETS
	public int getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}

	public Outorga getOutorga() {
		return outorga;
	}

	public void setOutorga(Outorga outorga) {
		this.outorga = outorga;
	}

	public FceAbastecimentoHumano getFceAbastecimentoHumano() {
		return fceAbastecimentoHumano;
	}

	public void setFceAbastecimentoHumano(FceAbastecimentoHumano fceAbastecimentoHumano) {
		this.fceAbastecimentoHumano = fceAbastecimentoHumano;
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

	public List<TipoPeriodoDerivacao> getListaTipoPeriodoDerivacao() {
		return listaTipoPeriodoDerivacao;
	}

	public void setListaTipoPeriodoDerivacao(List<TipoPeriodoDerivacao> listaTipoPeriodoDerivacao) {
		this.listaTipoPeriodoDerivacao = listaTipoPeriodoDerivacao;
	}

	public List<OutorgaLocalizacaoGeograficaFinalidade> getListaOutorgaLocalizacaoGeograficaFinalidade() {
		return listaOutorgaLocalizacaoGeograficaFinalidade;
	}

	public void setListaOutorgaLocalizacaoGeograficaFinalidade(List<OutorgaLocalizacaoGeograficaFinalidade> listaOutorgaLocalizacaoGeograficaFinalidade) {
		this.listaOutorgaLocalizacaoGeograficaFinalidade = listaOutorgaLocalizacaoGeograficaFinalidade;
	}

	public List<OutorgaLocalizacaoGeografica> getListaCaptacaoSubterranea(){
		return super.listaCaptacaoSubterranea;
	}

	public List<OutorgaLocalizacaoGeografica> getListaCaptacaoSuperficial(){
		return super.listaCaptacaoSuperficial;
	}

	@Override
	public void abrirDialog() {
		RequestContext.getCurrentInstance().addPartialUpdateTarget("pnlFceAbastecimentoHumano");
		RequestContext.getCurrentInstance().addPartialUpdateTarget("dlgImprimirRelatorioAbastecimentoHumano");
		RequestContext.getCurrentInstance().execute("abs_humano.show();");
	}

	@Override
	protected void prepararDuplicacao() {
		fceAbastecimentoHumano.setFceIdeAbastecimentoHumano(null);
		fceAbastecimentoHumano.setIdeFce(super.fce);
	}

	@Override
	protected void duplicarFce() {
		try {
			super.salvarFce();
			salvarFceAbastecimentoHumano();
			super.carregarListaFceOutorgaLocalizacaoGeografica(TipoFinalidadeUsoAguaEnum.ABASTECIMENTO_HUMANO);
			
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " o FCE - Abastecimento Humano.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	@Override
	protected void carregarFceTecnico() {
		try {
			verificarEdicao();
			carregarFceAbsHumano();
			carregarAbaAbastecimento();
			carregarAbaDadosRequerimento();
			super.carregarListaFceOutorgaLocalizacaoGeografica(TipoFinalidadeUsoAguaEnum.ABASTECIMENTO_HUMANO);
			construirAbaRequerimento();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o FCE - Abastecimento Humano.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	private void construirAbaRequerimento(){
				
		for(OutorgaLocalizacaoGeografica out:this.listaCaptacaoSubterranea){
			FceOutorgaLocalizacaoGeografica fceOutSub = new FceOutorgaLocalizacaoGeografica(out);
			fceOutSub.setIdeLocalizacaoGeografica(out.getIdeLocalizacaoGeografica());
			this.listaFceOutorgaLocalizacaoGeograficaCapSub.add(fceOutSub);
		}
		
		for(OutorgaLocalizacaoGeografica out:this.listaCaptacaoSuperficial){
			FceOutorgaLocalizacaoGeografica fceOutSup = new FceOutorgaLocalizacaoGeografica(out);
			fceOutSup.setIdeLocalizacaoGeografica(out.getIdeLocalizacaoGeografica());
			this.listaFceOutorgaLocalizacaoGeograficaCapSup.add(fceOutSup);
		}
	}
}