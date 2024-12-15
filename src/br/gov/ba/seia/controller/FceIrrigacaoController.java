package br.gov.ba.seia.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.controller.abstracts.BaseFceOutorgaSemDocumentoAdicionalController;
import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.Fce;
import br.gov.ba.seia.entity.FceAtividadeArea;
import br.gov.ba.seia.entity.FceIrrigacao;
import br.gov.ba.seia.entity.FceOutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.MetodoIrrigacao;
import br.gov.ba.seia.entity.OutorgaLocalizacaoGeografica;
import br.gov.ba.seia.entity.Requerimento;
import br.gov.ba.seia.entity.TipoFinalidadeUsoAgua;
import br.gov.ba.seia.entity.TipoPeriodoDerivacao;
import br.gov.ba.seia.entity.TipologiaAtividade;
import br.gov.ba.seia.entity.TipologiaIrrigacao;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.MetodoIrrigacaoEnum;
import br.gov.ba.seia.enumerator.TipoFinalidadeUsoAguaEnum;
import br.gov.ba.seia.enumerator.TipoPeriodoDerivacaoEnum;
import br.gov.ba.seia.enumerator.TipologiaEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.FceNavegacaoInterface;
import br.gov.ba.seia.service.FceIrrigacaoService;
import br.gov.ba.seia.service.MetodoIrrigacaoService;
import br.gov.ba.seia.service.TipoPeriodoDerivacaoService;
import br.gov.ba.seia.service.TipologiaIrrigacaoService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

/**
 * 04/02/14
 * @author eduardo.fernandes
 */
@Named("fceIrrigacaoController")
@ViewScoped
public class FceIrrigacaoController extends BaseFceOutorgaSemDocumentoAdicionalController implements FceNavegacaoInterface {

	@Inject
	private FceAtividadeAreaController fceAtividadeAreaController;

	@EJB
	private TipoPeriodoDerivacaoService tipoPeriodoDerivacaoService;
	@EJB
	private TipologiaIrrigacaoService tipologiaIrrigacaoService;
	@EJB
	private MetodoIrrigacaoService metodoIrrigacaoService;
	@EJB
	private FceIrrigacaoService fceIrrigacaoService;

	private static final DocumentoObrigatorio DOC_IRRIGACAO = new DocumentoObrigatorio(DocumentoObrigatorioEnum.FCE_IRRIGACAO.getId());
	private static final MetodoIrrigacao OUTROS_METODOS = new MetodoIrrigacao(-1, "Outros");

	private int activeTab;

	// Aba Irrigação
	private FceIrrigacao fceIrrigacao;
	private List<TipologiaIrrigacao> listaTipologiaIrrigacao;
	private List<TipoPeriodoDerivacao> listaTipoPeriodoDerivacao;

	private FceAtividadeArea fceAtividadeArea;
	private TipologiaAtividade tipologiaAtividade;
	private FceAtividadeArea fceAtividadeAreaTemp;

	private String metodoPesquisado;
	private List<MetodoIrrigacao> listaMetodoIrrigacao;

	private StringBuilder somatorioAreaTotalMetodo;
	private StringBuilder stringAreaTotalMetodoDoNR;
	private BigDecimal areaTotalMetodoDoNR;

	/**
	 * Método chamado na abertura da tela FCE - Irrigação
	 * @param fceIrrigacao
	 * @author eduardo.fernandes
	 */
	@Override
	public void init(){
		verificarEdicao();
		carregarAba();
		fceAtividadeAreaController.setIrrigacao(true);
		if(!isFceSalvo()){
			iniciarFce(DOC_IRRIGACAO);
			fceIrrigacao = new FceIrrigacao(super.fce);
		}
		else {
			carregarFceIrrigacao();
			carregarListaFceAtividadeArea();
		}
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
		super.carregarFceDoRequerente(DOC_IRRIGACAO);
	}

	/**
	 * Método que percorre a lista de {@link OutorgaLocalizacaoGeografica} informadas no {@link Requerimento} e faz um somatório das áreas de agricultura irrigada.
	 * Depois formata esse valor BigDecimal para String e exibe no rodapé da tabela presente na Aba Irrigação
	 * @param areaTotalMetodoDoNR
	 * @param stringAreaTotalMetodoDoNR
	 * @author eduardo.fernandes
	 */
	private void calcularAreaDoMetodoIrrigacao(){
		areaTotalMetodoDoNR = new BigDecimal(0);
		if(!super.isFceTecnico()){
			for(OutorgaLocalizacaoGeografica outorgaLocalizacaoGeografica : super.listaOutorgaLocalizacaoGeografica){
				areaTotalMetodoDoNR = areaTotalMetodoDoNR.add(outorgaLocalizacaoGeografica.getNumAreaIrrigadaCaptacao());
			}
		} else {
			if(!Util.isNullOuVazio(listaFceOutorgaLocalizacaoGeograficaCapSup)){
				for(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica : listaFceOutorgaLocalizacaoGeograficaCapSup){
					areaTotalMetodoDoNR = areaTotalMetodoDoNR.add(fceOutorgaLocalizacaoGeografica.getNumAreaIrrigadaCaptacao());
				}
			}
			if(!Util.isNullOuVazio(listaFceOutorgaLocalizacaoGeograficaCapSub)){
				for(FceOutorgaLocalizacaoGeografica fceOutorgaLocalizacaoGeografica : listaFceOutorgaLocalizacaoGeograficaCapSub){
					areaTotalMetodoDoNR = areaTotalMetodoDoNR.add(fceOutorgaLocalizacaoGeografica.getNumAreaIrrigadaCaptacao());
				}
			}
		}
		stringAreaTotalMetodoDoNR = new StringBuilder();
		formatarAreaToString(stringAreaTotalMetodoDoNR, areaTotalMetodoDoNR);
		fceAtividadeAreaController.setAreaTotalCulturaDoNR(areaTotalMetodoDoNR);
		fceAtividadeAreaController.setStringAreaTotalCulturaDoNR(stringAreaTotalMetodoDoNR);
	}

	// Carregar listas da tela
	/**
	 * Método que preenche as listas exibidas em cada aba
	 * @author eduardo.fernandes
	 */
	@Override
	public void carregarAba(){
		carregarAbaIrrigacao();
		carregarAbaDadosRequerimento();
	}

	/**
	 * Método que preenche as listas da Aba Irrigação
	 * @author eduardo.fernandes
	 */
	private void carregarAbaIrrigacao(){
		carregarListaTipologiaIrrigacao();
		carregarListaMetodoIrrigacao();
		fceAtividadeAreaController.carregarListaTipologiaAtividade(criarListaTipologiaEnumParaBusca());
		carregarListaTipoPeriodoDerivacao();
	}

	/**
	 * Método que carrega a lista de {@link MetodoIrrigacao} e adiciona "Outros" à ela
	 * @param listaMetodoIrrigacao
	 * @author eduardo.fernandes
	 */
	private void carregarListaMetodoIrrigacao(){
		try {
			listaMetodoIrrigacao = (List<MetodoIrrigacao>) metodoIrrigacaoService.listarMetodoIrrigacao();
			listaMetodoIrrigacao.add(OUTROS_METODOS);
			removerMetodosQueNaoDevemSerListados();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista de métodos de irrigação.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Para atender a especificação do RF2, item 2, alínea "a", os métodos de irrigação abaixo devem ser removidos da lista.
	 * @see \projeto_seia_lacfce\seialacfce_analise\1-Requisitos\FCE\FCE_Outorga\Irrigaçao\RM_Requisitos_SEIA - FCEOutorga_Irrigaçao.doc
	 * @author eduardo.fernandes
	 */
	private void removerMetodosQueNaoDevemSerListados(){
		listaMetodoIrrigacao.remove(new MetodoIrrigacao(MetodoIrrigacaoEnum.ASPERSAO_COM_MANGUEIRA.getId()));
		listaMetodoIrrigacao.remove(new MetodoIrrigacao(MetodoIrrigacaoEnum.ASPERSAO_COM_PIVO_LATERAL.getId()));
		listaMetodoIrrigacao.remove(new MetodoIrrigacao(MetodoIrrigacaoEnum.ASPERSAO_POR_MALHA.getId()));
		listaMetodoIrrigacao.remove(new MetodoIrrigacao(MetodoIrrigacaoEnum.INUNDACAO.getId()));
		listaMetodoIrrigacao.remove(new MetodoIrrigacao(MetodoIrrigacaoEnum.SULCO.getId()));
		listaMetodoIrrigacao.remove(new MetodoIrrigacao(MetodoIrrigacaoEnum.XIQUE_XIQUE.getId()));
	}

	/**
	 * Lista com as Tipologia necessárias para buscar as TipologiaAtividade
	 * @return List {@link TipologiaEnum}
	 */
	private List<TipologiaEnum> criarListaTipologiaEnumParaBusca(){
		List<TipologiaEnum> listaTemp = new ArrayList<TipologiaEnum>();
		listaTemp.add(TipologiaEnum.AGRICULTURA_IRRIGADA_OUT);
		listaTemp.add(TipologiaEnum.SILVICULTURA);
		return listaTemp;
	}

	/**
	 * Método que carrega a lista de {@link TipologiaIrrigacao}
	 * @param listaTipologiaIrrigacao
	 * @author eduardo.fernandes
	 */
	private void carregarListaTipologiaIrrigacao(){
		try {
			listaTipologiaIrrigacao = tipologiaIrrigacaoService.listarTipologiaIrrigacaoByIndAtivo();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista de tipologia da irrigação.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que carrega a lista de {@link TipoPeriodoDerivacao}
	 * @param listaTipoPeriodoDerivacao
	 * @author eduardo.fernandes
	 */
	private void carregarListaTipoPeriodoDerivacao(){
		try {
			listaTipoPeriodoDerivacao = tipoPeriodoDerivacaoService.listarTipoPeriodoDerivacao();
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar")+" a lista do período de derivação.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que preenche as listas de {@link OutorgaLocalizacaoGeografica} da Aba Dados do Requerimento
	 * @param listaOutorgaLocalizacaoGeograficaSubterranea
	 * @param listaOutorgaLocalizacaoGeograficaSuperficial
	 * @author eduardo.fernandes
	 */
	private void carregarAbaDadosRequerimento(){
		super.carregarListaOutorgaLocGeoByTipoFinalidadeUsoAgua(new TipoFinalidadeUsoAgua(TipoFinalidadeUsoAguaEnum.IRRIGACAO.getId()));
		calcularAreaDoMetodoIrrigacao();
		super.separarCaptacoes();
	}

	// isEdicao
	/**
	 * Método que busca o {@link FceIrrigacao} associado aquele {@link Fce} salvo.
	 * @param fceIrrigacao
	 * @author eduardo.fernandes
	 */
	private void carregarFceIrrigacao(){
		try {
			fceIrrigacao = fceIrrigacaoService.buscarFceIrrigacaoByIdeFce(super.fce);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " o FCE - Irrigação.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * Método que carrega a lista de {@link FceAtividadeArea} pelo {@link Fce} salvo no BD
	 * @author eduardo.fernandes
	 */
	private void carregarListaFceAtividadeArea(){
		fceAtividadeAreaController.carregarListaFceAtividadeArea(super.fce);
		if(isFceIrrigacaoIndOutrasCulturas()){
			fceAtividadeAreaController.simularOutrosNaLista(true);
		}
		if(fceAtividadeAreaController.isCulturaAdicionada()){
			fceAtividadeAreaController.tratarListaAtividadesAdicionadas("Irrigação");
		}
	}

	// Métodos do usuário
	/**
	 * Método que seta a Aba ativa de acordo com o clique do usuário na tela
	 * @param activeTab
	 * @param event
	 * @author eduardo.fernandes
	 */
	@Override
	public void controlarAbas(TabChangeEvent event) {
		if ("abaDadosRequerimento".equals(event.getTab().getId())) {
			activeTab = 0;
		} else if ("abaIrrigacao".equals(event.getTab().getId())) {
			activeTab = 1;
		}
	}

	/**
	 * Método que permite avançar uma aba através do clique do botão "Avançar"
	 * @param activeTab
	 * @author eduardo.fernandes
	 */
	@Override
	public void avancarAba() {
		activeTab++;
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

	public void changePeriodoDerivacao(ValueChangeEvent event){
		if(!Util.isNullOuVazio(event.getOldValue()) && event.getOldValue().equals(new TipoPeriodoDerivacao(TipoPeriodoDerivacaoEnum.INTERMITENTE.getId()))){
			fceIrrigacao.setNumTempoCaptacao(null);
		}
	}

	public void changePeriodoDerivacaodaGrid(ValueChangeEvent event){
		if(!Util.isNullOuVazio(event.getOldValue()) && event.getOldValue().equals(new TipoPeriodoDerivacao(TipoPeriodoDerivacaoEnum.INTERMITENTE.getId()))){
			fceAtividadeAreaTemp.setNumTempoCaptacao(null);
		}
	}

	/**
	 * Método que armazena o {@link FceAtividadeArea} selecionado pelo usuário na tela
	 * @param fceAtividadeArea
	 * @author eduardo.fernandes
	 */
	public void armazenarCulturaIrrigada(FceAtividadeArea fceAtividadeArea){
		fceAtividadeAreaTemp = new FceAtividadeArea();
		fceAtividadeAreaTemp = fceAtividadeArea;
	}

	/**
	 * Método para finalizar o {@link FceIrrigacao}, e se ele for preenchido corretamente, fechar a tela. Do contrário, exibir as mensagens de validação
	 * @author eduardo.fernandes
	 */
	@Override
	public void finalizar(){
		if(validarAba()){
			try {
				fceOutorgaServiceFacade.finalizar(this);
			} catch (Exception e) {
				JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_salvar") + " o FCE - Irrigação.");
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
				throw Util.capturarException(e,Util.SEIA_EXCEPTION);
			}
		}
	}
	
	public void prepararParaFinalizar() throws Exception{
		boolean permiteFinalizar = true;
		if(temOutrasCulturasAdd()){
			JsfUtil.addWarnMessage(Util.getString("fce_irrigacao_finalizar_outros_cultura"));
			permiteFinalizar = false;
		}
		if(temOutrosMetodosAdd()) {
			JsfUtil.addWarnMessage(Util.getString("fce_irrigacao_finalizar_outros_metodo"));
			permiteFinalizar = false;
		}
		
		if(permiteFinalizar) {
			RequestContext.getCurrentInstance().execute("rel_irrigacao.show()");
			super.concluirFce();
			salvarFceIrrigacao();
			salvarFceAtividadeArea();
			if(!isFceSalvo()){
				super.exibirMensagem001();
			}
			else {
				super.exibirMensagem002();
			}	
			
			RequestContext.getCurrentInstance().execute("irrigacao.hide()");
			limpar();
		}
	}
	
	/**
	 * Método que anula todas as variáveis presentes na tela
	 * @author eduardo.fernandes
	 */
	@Override
	public void limpar(){
		activeTab = 0;
		fceAtividadeAreaController.limpar();
		super.limparDadosOutorga();
		areaTotalMetodoDoNR = null;
		stringAreaTotalMetodoDoNR = null;
		somatorioAreaTotalMetodo = null;
		metodoPesquisado = null;
		listaMetodoIrrigacao = null;
		fceIrrigacao = null;
		fceAtividadeArea = null;
	}
	/**
	 * Método que salva o {@link FceIrrigacao} com o ide do {@link Fce} previamente salvo
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	private void salvarFceIrrigacao() {
		fceIrrigacao.setIdeFce(super.fce);
		fceIrrigacao.setIndOutrosCultura(temOutrasCulturasAdd());
		fceIrrigacaoService.salvarFceIrrigacao(fceIrrigacao);
	}

	/**
	 * Método que salva o {@link FceAtividadeArea} com o ide do {@link Fce} previamente salvo
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	private void salvarFceAtividadeArea() {
		fceAtividadeAreaController.salvarFceAtividadeArea(super.fce);
	}

	/**
	 * Adiciona a cultura selecionada na listaTipologiaAtividadeAdicionadas
	 * @param tipologiaAtividade
	 * @author eduardo.fernandes
	 */
	public void adicionarCultura(){
		fceAtividadeAreaController.adicionarCultura(tipologiaAtividade, false);
	}

	/**
	 * Confirma o {@link FceAtividadeArea} (cultura selecionada) e a adiciona na listaFceAtividadeArea
	 * @param fceAtividadeArea
	 * @author eduardo.fernandes
	 */
	public void confirmarCultura(){
		fceAtividadeAreaController.confirmarCultura(fceAtividadeArea, "Irrigação");
		fceAtividadeAreaTemp = null;
	}

	/**
	 * Método acionado quando o usuário clica em "Editar" e habilita os campos "Área Irrigada", "Volume a Derivar" e "Período de Derivação"
	 * @param fceAtividadeArea
	 * @author eduardo.fernandes
	 */
	public void editarCultura(){
		fceAtividadeAreaController.editarCultura(fceAtividadeArea);
	}

	/**
	 * Método acionado quando o usuário clica em "Excluir", remove da listaTipologiaAtividadeAdicionadas o {@link FceAtividadeArea} escolhido
	 * e o adiciona o {@link TipologiaAtividade} na listaTipologiaAtividade
	 * @param fceAtividadeArea
	 * @author eduardo.fernandes
	 */
	public void excluirCultura(){
		fceAtividadeAreaController.excluirCultura(fceAtividadeArea);
	}

	/**
	 * Método que formata o BigDecimal do somatório das áreas para uma String que será usada na aba Irrigação
	 * @param stringBuilder
	 * @param areaTotal
	 * @author eduardo.fernandes
	 */
	private void formatarAreaToString(StringBuilder stringBuilder, BigDecimal areaTotal){
		DecimalFormat format = new DecimalFormat("##,###,###.##");
		format.setMinimumFractionDigits(2);
		stringBuilder.append(format.format(areaTotal));
	}
	// FIM dos Métodos para trabalhar com CULTURA IRRIGADA

	// Métodos de validação
	/**
	 * Mètodo que verifica as RNs da aba Irrigação
	 * @return boolean
	 * @author eduardo.fernandes
	 */
	@Override
	public boolean validarAba(){
		boolean valido = true;
		boolean culturasConfirmadas = true;
		if(Util.isNullOuVazio(fceIrrigacao.getIdeTipologiaIrrigacao())){
			JsfUtil.addErrorMessage("A tipologia da irrigação " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		}
		if(!fceAtividadeAreaController.isCulturaAdicionada()){
			JsfUtil.addErrorMessage(Util.getString("msg_generica_selecione_um") + " uma cultura irrigada.");
			valido = false;
		}
		else if(validarCulturaConfirmada()) {
			if(!super.isFceTecnico() && !temOutrasCulturasAdd() && fceAtividadeAreaController.isAreaTotalCalculada() && !fceAtividadeAreaController.isAreaTotalDoNRequalsSomatorioArea(fceAtividadeAreaController.getStringAreaTotalCulturaDoNR(), fceAtividadeAreaController.getSomatorioAreaTotalCultura())){
				JsfUtil.addErrorMessage("A área total da cultura irrigada tem que ser igual a "+ fceAtividadeAreaController.getStringAreaTotalCulturaDoNR() +" (ha), conforme informado no requerimento. Favor corrigir.");
				valido = false;
			}
		}
		else {
			culturasConfirmadas = false;
		}

		if(Util.isNullOuVazio(fceIrrigacao.getIdeTipoPeriodoDerivacao())){
			JsfUtil.addErrorMessage("O período de derivação " + Util.getString("msg_generica_preenchimento_obrigatorio"));
			valido = false;
		} else if(isPeriodoDerivacaoIntermitente()){
			if(Util.isNullOuVazio(fceIrrigacao.getNumTempoCaptacao())){
				JsfUtil.addErrorMessage("O tempo de captação " + Util.getString("msg_generica_null_ou_vazio"));
				valido = false;
			} else if(fceIrrigacao.getNumTempoCaptacao() > 24){
				JsfUtil.addErrorMessage(Util.getString("fce_captacao_subteranea_tempo_captacao_info_0034"));
				valido = false;
			}
		}
		if(Util.isNullOuVazio(fceIrrigacao.getNumVolumeDerivar())){
			JsfUtil.addErrorMessage("O volume a derivar " + Util.getString("msg_generica_null_ou_vazio"));
			valido = false;
		}
		if(!culturasConfirmadas){
			valido = false;
		}
		return valido;
	}

	/**
	 * Método que verifica se os {@link FceAtividadeArea} estão todos confirmados passando a String que será exibida caso seja necessário mandar uma mensagem ao usuário
	 * @return boolean
	 * @author eduardo.fernandes
	 */
	private boolean validarCulturaConfirmada(){
		return fceAtividadeAreaController.verificarConfirmado(" Irrigada.");
	}
	// FIM Métodos de validação

	// Render's
	/**
	 * Método que verifica a existência do {@link FceIrrigacao} no banco
	 * @param fceIrrigacao
	 * @return boolean
	 * @author eduardo.fernandes
	 */
	private boolean isFceIrrigacaoSalva(){
		return !Util.isNullOuVazio(fceIrrigacao.getIdeFceIrrigacao());
	}

	/**
	 * Método que verifica se o {@link FceIrrigacao} tem o {@link TipoDerivacao} "Intermitente"
	 * @param fceIrrigacao
	 * @return boolean
	 * @author eduardo.fernandes
	 */
	public boolean isPeriodoDerivacaoIntermitente(){
		return !Util.isNullOuVazio(fceIrrigacao.getIdeTipoPeriodoDerivacao()) && fceIrrigacao.getIdeTipoPeriodoDerivacao().isIntermitente();
	}

	/**
	 * Método que diz se o {@link FceIrrigacao} tem o indOutrosCultura == TRUE
	 * @return boolean
	 * @author eduardo.fernandes
	 */
	private boolean isFceIrrigacaoIndOutrasCulturas(){
		return isFceIrrigacaoSalva() && fceIrrigacao.getIndOutrosCultura();
	}

	/**
	 * Método que diz se o Somatório das Áreas é igual à Área Total
	 * @param areaTotalNR
	 * @param somatorioArea
	 * @return boolean
	 * @author eduardo.fernandes
	 */
	public boolean isAreaTotalDoNRequalsSomatorioArea(StringBuilder areaTotalNR, StringBuilder somatorioArea){
		return areaTotalNR.toString().compareTo(somatorioArea.toString()) == 0;
	}

	/**
	 * Método que verifica se existe "Outros" adicionado a lista de {@link TipologiaAtividade}
	 * @return boolean
	 * @author eduardo.fernandes
	 */
	private boolean temOutrasCulturasAdd(){
		return fceAtividadeAreaController.existeOutros();
	}

	/**
	 * Método que verifica se existe "Outros" adicionado a lista de {@link FceAtividadeArea}
	 * @return boolean
	 * @author eduardo.fernandes
	 */
	private boolean temOutrosMetodosAdd(){
		return fceAtividadeAreaController.existeOutrosMetodoParaCultura();
	}

	/**
	 * Método que verifica se o somatório da Área Total foi calculado
	 * @param somatorioAreaTotalMetodo
	 * @return boolean
	 * @author eduardo.fernandes
	 */
	public boolean isAreaTotalMetodoIrrigacaoCalculada(){
		return !Util.isNullOuVazio(somatorioAreaTotalMetodo);
	}

	/**
	 * Método que retorna "Irrigação"
	 * @return String
	 * @author eduardo.fernandes
	 */
	public String isIrrigacao(){
		return "Irrigação";
	}

	// STREAMs
	/**
	 * Método que imprime o relatório do {@link FceIrrigacao}
	 * @param requerimento
	 * @param DOC_IRRIGACAO
	 * @return StreamedContent
	 * @throws Exception
	 * @author eduardo.fernandes
	 */
	public StreamedContent getImprimirRelatorio() {
		try {
			return getImprimirRelatorio(super.fce, DOC_IRRIGACAO);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_imprimir_relatorio") + " do FCE - Irrigação.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	// Getters and Setters
	public int getActiveTab() {
		return activeTab;
	}
	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}

	public FceIrrigacao getFceIrrigacao() {
		return fceIrrigacao;
	}

	public void setFceIrrigacao(FceIrrigacao fceIrrigacao) {
		this.fceIrrigacao = fceIrrigacao;
	}

	public List<TipologiaIrrigacao> getListaTipologiaIrrigacao() {
		return listaTipologiaIrrigacao;
	}

	public void setListaTipologiaIrrigacao(List<TipologiaIrrigacao> listaTipologiaIrrigacao) {
		this.listaTipologiaIrrigacao = listaTipologiaIrrigacao;
	}

	public List<TipoPeriodoDerivacao> getListaTipoPeriodoDerivacao() {
		return listaTipoPeriodoDerivacao;
	}

	public void setListaTipoPeriodoDerivacao(List<TipoPeriodoDerivacao> listaTipoPeriodoDerivacao) {
		this.listaTipoPeriodoDerivacao = listaTipoPeriodoDerivacao;
	}

	public List<MetodoIrrigacao> getListaMetodoIrrigacao() {
		return listaMetodoIrrigacao;
	}

	public void setListaMetodoIrrigacao(List<MetodoIrrigacao> listaMetodoIrrigacao) {
		this.listaMetodoIrrigacao = listaMetodoIrrigacao;
	}

	public FceAtividadeArea getFceAtividadeArea() {
		return fceAtividadeArea;
	}

	public void setFceAtividadeArea(FceAtividadeArea fceAtividadeArea) {
		this.fceAtividadeArea = fceAtividadeArea;
	}

	public String getMetodoPesquisado() {
		return metodoPesquisado;
	}

	public void setMetodoPesquisado(String metodoPesquisado) {
		this.metodoPesquisado = metodoPesquisado;
	}

	public TipologiaAtividade getTipologiaAtividade() {
		return tipologiaAtividade;
	}

	public void setTipologiaAtividade(TipologiaAtividade tipologiaAtividade) {
		this.tipologiaAtividade = tipologiaAtividade;
	}

	public StringBuilder getSomatorioAreaTotalMetodo() {
		return somatorioAreaTotalMetodo;
	}

	public void setSomatorioAreaTotalMetodo(StringBuilder somatorioAreaTotalMetodo) {
		this.somatorioAreaTotalMetodo = somatorioAreaTotalMetodo;
	}

	public FceAtividadeArea getFceAtividadeAreaTemp() {
		return fceAtividadeAreaTemp;
	}

	public void setFceAtividadeAreaTemp(FceAtividadeArea fceAtividadeAreaTemp) {
		this.fceAtividadeAreaTemp = fceAtividadeAreaTemp;
	}

	public List<OutorgaLocalizacaoGeografica> getListaCaptacaoSubterranea(){
		return super.listaCaptacaoSubterranea;
	}

	public List<OutorgaLocalizacaoGeografica> getListaCaptacaoSuperficial(){
		return super.listaCaptacaoSuperficial;
	}
	
	public List<FceAtividadeArea> getListaFceAtividadeArea(){
		return fceAtividadeAreaController.getListaFceAtividadeArea();
	}

	@Override
	public void abrirDialog() {
		RequestContext.getCurrentInstance().addPartialUpdateTarget("pnlFceIrrigacao");
		RequestContext.getCurrentInstance().addPartialUpdateTarget("dlgImprimirRelatorioIrrigacao");
		RequestContext.getCurrentInstance().execute("irrigacao.show();");
	}
	
	@Override
	protected void prepararDuplicacao() {
		fceIrrigacao.setIdeFceIrrigacao(null);
		fceIrrigacao.setIdeFce(null);
		if (!Util.isNullOuVazio(getListaFceAtividadeArea())){
			for(FceAtividadeArea fceAtividadeAreaObj : getListaFceAtividadeArea()){
				fceAtividadeAreaObj.setIdeFceAtividadeArea(null);
			}
		}
	}

	@Override
	protected void duplicarFce() {
		try {
			super.salvarFce();
			salvarFceIrrigacao();
			salvarFceAtividadeArea();
			super.carregarListaFceOutorgaLocalizacaoGeografica(TipoFinalidadeUsoAguaEnum.IRRIGACAO);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_duplicar") + " Irrigação.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	
	@Override
	protected void carregarFceTecnico(){
		try {
			carregarAbaIrrigacao();
			super.carregarListaFceOutorgaLocalizacaoGeografica(TipoFinalidadeUsoAguaEnum.IRRIGACAO);
			carregarFceIrrigacao();
			calcularAreaDoMetodoIrrigacao();
			carregarListaFceAtividadeArea();		
		} catch (Exception e) {
			JsfUtil.addErrorMessage(Util.getString("msg_generica_erro_ao_carregar") + " as informações do FCE - Irrigação.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
}