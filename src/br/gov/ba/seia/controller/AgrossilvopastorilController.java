package br.gov.ba.seia.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;

import br.gov.ba.seia.entity.DocumentoObrigatorio;
import br.gov.ba.seia.entity.EmpreendimentoRequerimento;
import br.gov.ba.seia.entity.FceAgroUsoSolo;
import br.gov.ba.seia.entity.FceAgroUsoSoloPK;
import br.gov.ba.seia.entity.FceAgrossilvopastoril;
import br.gov.ba.seia.entity.TipoCaracterizacaoProjeto;
import br.gov.ba.seia.entity.TipoUsoSolo;
import br.gov.ba.seia.enumerator.DocumentoObrigatorioEnum;
import br.gov.ba.seia.enumerator.FaseEmpreendimentoEnum;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AgrossilvopastorilService;
import br.gov.ba.seia.service.EmpreendimentoService;
import br.gov.ba.seia.service.FceAgroUsoSoloService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("agrossilvopastorilController")
@ViewScoped
public class AgrossilvopastorilController extends FceController {
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("/Bundle");

	@EJB
	private EmpreendimentoService empreendimentoService;
	@EJB
	private AgrossilvopastorilService agrossilvopastorilService;
	@EJB
	private FceAgroUsoSoloService fceAgroUsoSoloService;
	@Inject
	private SilviculturaController silviculturaController;

	private static final int EXISTENTE = 0;
	private static final int PLANEJADA = 1;
	public static final int DADOS_GERAIS = 0;
	public static final int CRIACAO_ANIMAIS = 1;
	public static final int SILVICULTURA = 2;
	public static final int DEFENSIVOS_AGRICULAS = 3;
	public static final int ADUBACAO = 4;
	private static DocumentoObrigatorio DOCOBRIGATORIOAGRO = new DocumentoObrigatorio(DocumentoObrigatorioEnum.AGROSSILVOPASTORIL.getId());

	private int activeTab;
	private EmpreendimentoRequerimento empreendimentoRequerimento;
	private FceAgrossilvopastoril fceAgrossilvopastoril;
	private TipoUsoSolo usoSolo;
	private String nomUsoSolo;
	private FceAgroUsoSolo fceAgroUsoSolo;
	private List<FceAgroUsoSolo> listaFceAgroUsoSolo;
	private List<TipoCaracterizacaoProjeto> listaTipoCaracterizacaoProjeto;
	private List<TipoUsoSolo> listaTipoUsoSolo;
	private List<TipoUsoSolo> listaTipoUsoSoloAll;
	private Double areaTotalImoveis;
	private boolean isEdicao;

	@Override
	public void init() {
		carregarEmpreendimentoRequerimento();
		carregarTipoCaracterizacaoProjeto();
		carregarUsoSolo();
		carregarAbaDadosGerais();
		carregarAreaTotalImoveis();
		silviculturaController.init();
	}

	private void carregarEmpreendimentoRequerimento(){
		try {
			empreendimentoRequerimento = empreendimentoService.buscarEmpreendimentoRequerimento(requerimento);
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}

	public void salvarAgrossilvopastoril() {
		fceAgrossilvopastoril.setIdeFce(super.fce);
		agrossilvopastorilService.salvarAtualizarAgrossilvopastoril(fceAgrossilvopastoril);
	}

	public void salvarfceAgroUsoSolo(){
		fceAgroUsoSoloService.excluirFceAgroUsoSoloByIdeFceAgrossilvo(fceAgrossilvopastoril);
		for (FceAgroUsoSolo fceAUS : listaFceAgroUsoSolo) {
			fceAUS.setIdeFceAgrossilvopastoril(fceAgrossilvopastoril);
			fceAUS.setIdeFceAgroUsoSoloPK(new FceAgroUsoSoloPK(fceAgrossilvopastoril.getIdeFceAgrossilvopastoril(), fceAUS.getIdeTipoUsoSolo().getIdeTipoUsoSolo()));
			fceAgroUsoSoloService.salvarFceAgroUsoSolo(fceAUS);
		}
	}

	public boolean salvarAbaDadosGerais(Boolean isSalvarFinal){
		if(validarAbaDadosGerais()){
			Exception erro = null;
			try {
				super.salvarFce();
				salvarAgrossilvopastoril();
				if(!isDisableTblSelectFceAgroSolo()){
					salvarfceAgroUsoSolo();
				}
				if(!isSalvarFinal){
					JsfUtil.addSuccessMessage("Salvo com sucesso.");
					avancarAba();
				}
				return true;
			}catch (Exception e) {
				erro =e;
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
				return false;
			}finally{
				if(erro != null) {
					throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
				}
			}

		}else{
			return false;
		}
	}
	/**
	 * @INFO carrega os dados já inseridos (caso existam) pelo usuário na Aba Dados Gerais
	 */
	public void carregarAbaDadosGerais(){
		Exception erro = null;
		try {
			carregarFceDoRequerente(DOCOBRIGATORIOAGRO);
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}
		erro =null;
		try {
			if(Util.isNull(super.fce)) {
				iniciarFce(DOCOBRIGATORIOAGRO);
				fceAgrossilvopastoril = new FceAgrossilvopastoril(new TipoCaracterizacaoProjeto());
			}else{
				fceAgrossilvopastoril = agrossilvopastorilService.buscarFceAgrossilvopastorilByIdFce(super.fce);
				fceAgrossilvopastoril.setIdeFce(super.fce);
				listaFceAgroUsoSolo = fceAgroUsoSoloService.listarFceAgroUsoSoloByIdeFceAgrossilvo(fceAgrossilvopastoril);
				for (FceAgroUsoSolo fceAUS : listaFceAgroUsoSolo) {
					listaTipoUsoSolo.remove(fceAUS.getIdeTipoUsoSolo());
					listaTipoUsoSoloAll.remove(fceAUS.getIdeTipoUsoSolo());
					fceAUS.setDesabilitado(true);
				}
				isEdicao = true;
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}
	}
	public void carregarAreaTotalImoveis(){
		Exception erro = null;
		try {
			areaTotalImoveis = fceAgroUsoSoloService.buscarValAreaImovelRuralByIdeRequerimento(requerimento);
			if(isDisableTblSelectFceAgroSolo()){
				msgSemAreaTotalImoveis();
			}
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}
	}
	/**
	 * @INFO Carrega o tipo de caracterização do projeto. Misto, Publico e Privado.
	 */
	public void carregarTipoCaracterizacaoProjeto(){
		Exception erro = null;
		try {
			listaTipoCaracterizacaoProjeto = agrossilvopastorilService.listarTipoCaracterizacaoProjeto();
		}catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}
	}
	/**
	 * @INFO Carrega todos os tipos de solo disponiveis
	 */
	public void carregarUsoSolo(){
		Exception erro = null;
		try {
			listaTipoUsoSolo = fceAgroUsoSoloService.listarTipoUsoSolo();
			listaTipoUsoSoloAll = new ArrayList<TipoUsoSolo>();
			listaTipoUsoSoloAll.addAll(listaTipoUsoSolo);
			listaFceAgroUsoSolo = new ArrayList<FceAgroUsoSolo>();
		} catch (Exception e) {
			erro =e;
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);//log
		}finally{
			if(erro != null) {
				throw Util.capturarException(erro,Util.SEIA_EXCEPTION);
			}
		}
	}
	/**
	 * @INFO calcula o percentual das areas planejadas e existentes em relação a area Total dos imoveis
	 */
	public void calcularPercentualSobAreaTotalImovel(FceAgroUsoSolo fceAgroUsoSolo,int tipo){
		//0 => Existente
		//1 => Planejada
		Double area = 0.0;
		if(tipo == EXISTENTE){
			if(fceAgroUsoSolo.getNumAreaImovelExistente() <= areaTotalImoveis){
				area = fceAgroUsoSolo.getNumAreaImovelExistente()*100 / areaTotalImoveis;
				fceAgroUsoSolo.setNumPercentualAreaImovelExistente(area);
			}else{
				fceAgroUsoSolo.setNumPercentualAreaImovelExistente(null);
				fceAgroUsoSolo.setNumAreaImovelExistente(null);
				msgErroAreaExistentePlanejada();
			}
		}else if(tipo == PLANEJADA){
			if(fceAgroUsoSolo.getNumAreaImovelPlanejada() <= areaTotalImoveis){
				area = fceAgroUsoSolo.getNumAreaImovelPlanejada()*100 / areaTotalImoveis ;
				fceAgroUsoSolo.setNumPercentualAreaPlanejada(area);
			}else{
				fceAgroUsoSolo.setNumAreaImovelPlanejada(null);
				fceAgroUsoSolo.setNumPercentualAreaPlanejada(null);
				msgErroAreaExistentePlanejada();
			}

		}
	}

	public void pesquisarUsoSolo(){
		boolean achou = false;
		listaTipoUsoSolo.clear();
		for (TipoUsoSolo solo : listaTipoUsoSoloAll) {
			if(solo.getDscTipoUsoSolo().toLowerCase().indexOf(nomUsoSolo.toLowerCase()) != -1){
				listaTipoUsoSolo.add(solo);
				achou = true;
			}
		}
		if(!achou && nomUsoSolo.equals("")) {
			listaTipoUsoSolo.addAll(listaTipoUsoSoloAll);
		}
	}
	/**
	 * @INFO Adiciona a(s) atividade(s) de uso de solo a lista de edição
	 */
	public void adicionarUsoSolo(){
		if(Util.isNull(listaFceAgroUsoSolo)){
			listaFceAgroUsoSolo = new ArrayList<FceAgroUsoSolo>();
		}
		listaTipoUsoSolo.remove(usoSolo);
		listaTipoUsoSoloAll.remove(usoSolo);
		Collections.sort(listaTipoUsoSoloAll);
		Collections.sort(listaTipoUsoSolo);
		listaFceAgroUsoSolo.add(new FceAgroUsoSolo(usoSolo,false));
	}
	/**
	 * @INFO Remove a(s) atividade(s) de uso de solo a lista de edição
	 */
	public void excluirUsoSolo(){
		listaFceAgroUsoSolo.remove(fceAgroUsoSolo);
		listaTipoUsoSolo.add(fceAgroUsoSolo.getIdeTipoUsoSolo());
		listaTipoUsoSoloAll.add(fceAgroUsoSolo.getIdeTipoUsoSolo());
		Collections.sort(listaTipoUsoSoloAll);
		Collections.sort(listaTipoUsoSolo);
		msgExclusao();
	}

	public void confirmarUsoSolo(){
		if(validaFceAgroUsoSolo(fceAgroUsoSolo)){
			fceAgroUsoSolo.setDesabilitado(true);
			calcularPercentualSobAreaTotalImovel(fceAgroUsoSolo, 1);
			calcularPercentualSobAreaTotalImovel(fceAgroUsoSolo, 2);
		}
	}

	public void editarUsoSolo(){
		fceAgroUsoSolo.setDesabilitado(false);
	}

	private boolean validaFceAgroUsoSolo(FceAgroUsoSolo agroUsoSolo){
		boolean valido = true;
		if(Util.isNull(agroUsoSolo.getNumAreaImovelExistente())){
			valido = false;
			campoObrigatorio("Área total do imóvel existente, de "+agroUsoSolo.getIdeTipoUsoSolo().getDscTipoUsoSolo());
		}else if(agroUsoSolo.getNumAreaImovelExistente() == 0){
			valido = false;
			msgMaiorQueZero("Área total do imóvel planejada, de "+agroUsoSolo.getIdeTipoUsoSolo().getDscTipoUsoSolo());
		}
		if(Util.isNull(agroUsoSolo.getNumAreaImovelPlanejada())){
			valido = false;
			campoObrigatorio("Área total do imóvel planejada, de "+agroUsoSolo.getIdeTipoUsoSolo().getDscTipoUsoSolo());
		}else if(agroUsoSolo.getNumAreaImovelPlanejada() == 0){
			msgMaiorQueZero("Área total do imóvel planejada, de "+agroUsoSolo.getIdeTipoUsoSolo().getDscTipoUsoSolo());
		}
		return valido;
	}

	/**
	 * @INFO valida os campos da aba Dados Gerais respeitando as Regras de Negócio referentes a cada campo.
	 * @return
	 */
	public boolean validarAbaDadosGerais(){
		boolean valido = true;
		if(Util.isNull(fceAgrossilvopastoril.getIdeTipoCaracterizacaoProjeto().getIdeTipoCaracterizacaoProjeto())){
			valido = false;
			campoObrigatorio("Caracterização do projeto");
		}
		if((isFaseImplantacao() || isFaseOperacao()) && Util.isNull(fceAgrossilvopastoril.getDtcPrevista())){
			valido = false;
			campoObrigatorio("Data prevista para início da "+empreendimentoRequerimento.getIdeFaseEmpreendimento().getNomFaseEmpreendimento());
		}
		if(Util.isNull(fceAgrossilvopastoril.getNumAreaConstruida())){
			valido = false;
			campoObrigatorio("Área construída e/ou a ser construída");
		}else if(fceAgrossilvopastoril.getNumAreaConstruida() == 0){
			valido = false;
			msgMaiorQueZero("Área construída e/ou a ser construída");
		}
		if(isFaseAlteracao()){
			if(Util.isNull(fceAgrossilvopastoril.getNumAreaAmpliacao())){
				valido = false;
				campoObrigatorio("Área prevista para ampliação");
			}else if(fceAgrossilvopastoril.getNumAreaAmpliacao() == 0){
				valido = false;
				msgMaiorQueZero("Área prevista para ampliação");
			}
		}
		if(Util.isNullOuVazio(listaFceAgroUsoSolo) && !isDisableTblSelectFceAgroSolo()){
			valido = false;
			JsfUtil.addErrorMessage("Selecione ao menos 1 (hum) tipo de uso do solo.");
		}
		return valido;
	}
	/**
	 * @param event
	 * @INFO Método responsável pelo controle de identificação da aba atual
	 */
	public void controlarAbas(TabChangeEvent event) {
		if("abaDadosGerais".equals(event.getTab().getId())){
			activeTab = 0;
		}
		else if("abaCriacaoAnimais".equals(event.getTab().getId())){
			activeTab = 1;
		}
		else if("abaSilvicultura".equals(event.getTab().getId())){
			activeTab = 2;
		}
		else if("abaDefensivosAgricolas".equals(event.getTab().getId())){
			activeTab = 3;
		}
		else if("abaAdubacao".equals(event.getTab().getId())){
			activeTab = 4;
		}
	}

	public void voltarAba(){
		activeTab--;
	}

	public void avancarAba(){
		activeTab++;
	}

	public void closeFCEAgro(){
		activeTab = 0;
	}

	public StreamedContent getImprimirRelatorio() {
		try {
			return getImprimirRelatorio(super.fce, DOCOBRIGATORIOAGRO);
		} catch (Exception e) {
			JsfUtil.addErrorMessage(BUNDLE.getString("msg_generica_erro_imprimir_relatorio") + " do FCE - Agrossilvopastoril.");
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * @param str
	 * @return String concatenada
	 * @INFO Adiciona ao campo passado a informação de que o mesmo é obrigatório, conforme especificado nas Regras de Negócio SEIA - LAC's e FCE'S
	 *  atualizadas até a presente data(29/07/2013).
	 */
	public static void campoObrigatorio(String nomCampo){
		JsfUtil.addErrorMessage(nomCampo+" é de preenchimento obrigatório.");
	}
	/**
	 * @Mensagem Exclusão realizada com sucesso.
	 */
	public static void msgExclusao(){
		JsfUtil.addSuccessMessage("Exclusão realizada com sucesso.");
	}
	/**
	 * @Mensagem Inclusão realizada com sucesso.
	 */
	public static void msgInclusao(){
		JsfUtil.addSuccessMessage("Inclusão realizada com sucesso.");
	}
	/**
	 * @Mensagem Erro ao realizar inclusão.
	 */
	public static void msgErroInclusao(){
		JsfUtil.addErrorMessage("Erro ao realizar inclusão.");
	}
	/**
	 * @Mensagem Área Existente/Planejada não pode ser maior que Área Total. ( Tamanho_da_Area ha)
	 */
	public static void msgErroAreaExistentePlanejada(){
		JsfUtil.addErrorMessage("A Área informada não pode ser maior que a área total dos imóveis cadastrados no CEFIR.");
	}
	/**
	 * @Mensagem [nome do campo] deve ser maior que zero.
	 */
	public static void msgMaiorQueZero(String nomCampo){
		JsfUtil.addErrorMessage(nomCampo+" deve ser maior que zero.");
	}
	/**
	 * @Mensagem O imóvel não possui cadastro no CEFIR. Favor realizar o cadastro para que possa finalizar o FCE de agrossilvopastoril.
	 */
	public static void msgSemAreaTotalImoveis(){
		JsfUtil.addErrorMessage("O imóvel não possui cadastro no CEFIR. Favor realizar o cadastro para que possa finalizar o FCE de agrossilvopastoril.");
	}
	/**
	 * @INFO Oculta a grid de FceAgroUso Selecionados caso o usuário não tenha selecionado nenhum tipo de uso do solo ainda.
	 */
	public boolean isExibirTblFceAgroSolo() {
		return !Util.isNullOuVazio(listaFceAgroUsoSolo);
	}
	/**
	 * @INFO Desabilita a grid de Seleção de FceAgroUso caso o requerimento não tenha imovel rural cadastrado.
	 */
	public boolean isDisableTblSelectFceAgroSolo() {
		return Util.isNull(areaTotalImoveis);
	}
	/**
	 * @return boolean
	 * @INFO Desabilita todas as abas, exceto agrossilvopastoril, enquanto o usuário não tiver salvo uma FCE e FCE Agrossilvopastoril
	 * já que as demais abas dependem destes dois objetos.
	 */
	public boolean isDesabilitarAbas(){
		if(Util.isNull(fceAgrossilvopastoril) || Util.isNull(fceAgrossilvopastoril.getIdeFceAgrossilvopastoril())){
			return true;
		}
		return false;
	}

	//LOCALIZACAO(1), IMPLANTACAO(2),OPERACAO(3), ALTERACAO(4);
	public boolean isFaseLocalizacao(){
		return empreendimentoRequerimento.getIdeFaseEmpreendimento().getIdeFaseEmpreendimento() == FaseEmpreendimentoEnum.LOCALIZACAO.getId();
	}
	public boolean isFaseImplantacao(){
		return empreendimentoRequerimento.getIdeFaseEmpreendimento().getIdeFaseEmpreendimento() == FaseEmpreendimentoEnum.IMPLANTACAO.getId();
	}
	public boolean isFaseOperacao(){
		return empreendimentoRequerimento.getIdeFaseEmpreendimento().getIdeFaseEmpreendimento() == FaseEmpreendimentoEnum.OPERACAO.getId();
	}
	public boolean isFaseAlteracao(){
		return empreendimentoRequerimento.getIdeFaseEmpreendimento().getIdeFaseEmpreendimento() == FaseEmpreendimentoEnum.ALTERACAO.getId();
	}


	public TipoUsoSolo getUsoSolo() {
		return usoSolo;
	}

	public void setUsoSolo(TipoUsoSolo usoSolo) {
		this.usoSolo = usoSolo;
	}

	public FceAgroUsoSolo getFceAgroUsoSolo() {
		return fceAgroUsoSolo;
	}

	public void setFceAgroUsoSolo(FceAgroUsoSolo fceAgroUsoSolo) {
		this.fceAgroUsoSolo = fceAgroUsoSolo;
	}

	public List<TipoCaracterizacaoProjeto> getListaTipoCaracterizacaoProjeto() {
		return listaTipoCaracterizacaoProjeto;
	}

	public void setListaTipoCaracterizacaoProjeto(List<TipoCaracterizacaoProjeto> listaTipoCaracterizacaoProjeto) {
		this.listaTipoCaracterizacaoProjeto = listaTipoCaracterizacaoProjeto;
	}

	public int getActiveTab() {
		return activeTab;
	}

	public void setActiveTab(int activeTab) {
		this.activeTab = activeTab;
	}

	public List<TipoUsoSolo> getListaTipoUsoSolo() {
		return listaTipoUsoSolo;
	}

	public void setListaTipoUsoSolo(List<TipoUsoSolo> listaTipoUsoSolo) {
		this.listaTipoUsoSolo = listaTipoUsoSolo;
	}

	public List<FceAgroUsoSolo> getListaFceAgroUsoSolo() {
		return listaFceAgroUsoSolo;
	}

	public void setListaFceAgroUsoSolo(List<FceAgroUsoSolo> listaFceAgroUsoSolo) {
		this.listaFceAgroUsoSolo = listaFceAgroUsoSolo;
	}

	public String getNomUsoSolo() {
		return nomUsoSolo;
	}

	public void setNomUsoSolo(String nomUsoSolo) {
		this.nomUsoSolo = nomUsoSolo;
	}

	public FceAgrossilvopastoril getFceAgrossilvopastoril() {
		return fceAgrossilvopastoril;
	}

	public void setFceAgrossilvopastoril(FceAgrossilvopastoril fceAgrossilvopastoril) {
		this.fceAgrossilvopastoril = fceAgrossilvopastoril;
	}

	@Override
	public boolean isFceSalvo() {
		return isEdicao;
	}

	public void setEdicao(boolean isEdicao) {
		this.isEdicao = isEdicao;
	}

	public EmpreendimentoRequerimento getEmpreendimentoRequerimento() {
		return empreendimentoRequerimento;
	}

	public void setEmpreendimentoRequerimento(EmpreendimentoRequerimento empreendimentoRequerimento) {
		this.empreendimentoRequerimento = empreendimentoRequerimento;
	}

	@Override
	public void verificarEdicao() {
		

	}

	@Override
	public void carregarAba() {
		

	}

	@Override
	public void finalizar() {
		
		// super.concluirFce(); 
	}
	
	public void prepararParaFinalizar() throws RuntimeException{
		
	}

	@Override
	public void limpar() {
		

	}

	@Override
	public boolean validarAba() {
		
		return false;
	}

	@Override
	public void abrirDialog() {
		
		
	}

	@Override
	protected void prepararDuplicacao() {
		
		
	}

	@Override
	protected void duplicarFce() {
		
		
	}

	@Override
	public void carregarFceTecnico() {
		
		
	}

}
