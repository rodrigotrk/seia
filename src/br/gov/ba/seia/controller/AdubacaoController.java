package br.gov.ba.seia.controller;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.entity.AgroTipoAdubacao;
import br.gov.ba.seia.entity.AgroTipoAdubacaoPK;
import br.gov.ba.seia.entity.ClassificacaoAdubacao;
import br.gov.ba.seia.entity.TipoAdubacao;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AdubacaoService;
import br.gov.ba.seia.service.AgrossilvopastorilService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;

@Named("adubacaoController")
@ViewScoped
public class AdubacaoController {

	@Inject
	private AgrossilvopastorilController agrossilvopastorilController;
	@Inject
	private CriacaoAnimaisController criacaoAnimaisController;
	@Inject
	private SilviculturaController silviculturaController;
	@Inject
	private DefensivosAgricolasController defensivosAgricolasController;
	@EJB
	private AgrossilvopastorilService agrossilvopastorilService;
	@EJB
	private AdubacaoService adubacaoService;

	private Boolean pergunta_1;
	private List<TipoAdubacao> listaTipoAdubacaoSelecionada;
	private List<TipoAdubacao> listaTipoAdubacao;
	private List<ClassificacaoAdubacao> listaClassificacaoAdubacao;

	public void init(){
		carregarListaTipoAdubacao();
		carregarListaClassificacaoAdubacao();
		if(agrossilvopastorilController.isFceSalvo()){
			if(verificaSeIndAdubacaoNaoENuloOuFalso()){
				carregarAbaAdubacao();
			}
			carregaIndAdubacao();
		}
	}

	/**
	 * método que retorna se o indAdubacao não é nulo, ou false,
	 * @return true, caso seja edição e indAdubacao true
	 */
	public boolean verificaSeIndAdubacaoNaoENuloOuFalso(){
		return !Util.isNullOuVazio(agrossilvopastorilController.getFceAgrossilvopastoril().getIndAdubacao()) && agrossilvopastorilController.getFceAgrossilvopastoril().getIndAdubacao();
	}


	/**
	 * método para carregar o indAdubacao da FCE Agro buscada do banco
	 */
	public void carregaIndAdubacao(){
		if(!Util.isNullOuVazio(agrossilvopastorilController.getFceAgrossilvopastoril())){
			pergunta_1 = agrossilvopastorilController.getFceAgrossilvopastoril().getIndAdubacao();
		}
	}

	/**
	 * método para carregar os elementos que compõe a aba Adubação
	 */
	public void carregarAbaAdubacao(){
		if(!Util.isNullOuVazio(agrossilvopastorilController.getFceAgrossilvopastoril())){
			carregaIndAdubacao();
			if(verificaSeIndAdubacaoNaoENuloOuFalso()){
				carregarListaTipoAdubacaoSelecionada();
			}
		}
	}

	/**
	 * método para carregar do banco a lista de Tipo Adubação, 3º elemento da aba
	 */
	public void carregarListaTipoAdubacao(){
		if(Util.isNullOuVazio(listaTipoAdubacao)){
			listaTipoAdubacao = new ArrayList<TipoAdubacao>();
		}
		try {
			listaTipoAdubacao = adubacaoService.listarTipoAdubacao();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * método para carregar do banco a lista de Classificação Adubação, 2º elemento da aba
	 */
	public void carregarListaClassificacaoAdubacao (){
		if(Util.isNullOuVazio(listaClassificacaoAdubacao)){
			listaClassificacaoAdubacao = new ArrayList<ClassificacaoAdubacao>();
		}
		try {
			listaClassificacaoAdubacao = adubacaoService.listarClassificacaoAdubacao();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * método para carregar do banco a listaTipoAdubacaoSelecionada, que é salva ao concluir a aba
	 */
	public void carregarListaTipoAdubacaoSelecionada(){
		if(Util.isNullOuVazio(listaTipoAdubacaoSelecionada)){
			listaTipoAdubacaoSelecionada = new ArrayList<TipoAdubacao>();
		}
		try {
			listaTipoAdubacaoSelecionada = adubacaoService.listarTipoAdubacaoByIdeFceAgro(agrossilvopastorilController.getFceAgrossilvopastoril());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * método para salvar a listaTipoAdubacaoSelecionada,
	 * ele salva um objeto composto por IdeFceAgrossilvopastoril e ideTipoAdubacao em uma tabela de ligação.
	 */
	public void salvarTipoAdubacao (){
		if(!Util.isNullOuVazio(listaTipoAdubacaoSelecionada)){
			for(TipoAdubacao tipoAdubacao : listaTipoAdubacaoSelecionada){
				AgroTipoAdubacao agroTipoAdubacao = new AgroTipoAdubacao(new AgroTipoAdubacaoPK(agrossilvopastorilController.getFceAgrossilvopastoril().getIdeFceAgrossilvopastoril(), tipoAdubacao.getIdeTipoAdubacao()));
				agroTipoAdubacao.setIdeFceAgrossilvopastoril(agrossilvopastorilController.getFceAgrossilvopastoril());
				agroTipoAdubacao.setIdeTipoAdubacao(tipoAdubacao);
				try {
					adubacaoService.salvarAgroTipoAdubacao(agroTipoAdubacao);
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					throw Util.capturarException(e,Util.SEIA_EXCEPTION);
				}
			}
		}
	}

	/**
	 * método para excluir da tabela de ligação os registros que contém aquela ideFceAgro através deu ma namedQuery
	 */
	public void excluirLigacoesAdubacao(){
		try {
			adubacaoService.excluirLigacaoTipoAdubacao(agrossilvopastorilController.getFceAgrossilvopastoril());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * método para salvar todos os elementos da aba Adubação caso a validação retorne true
	 */
	public void salvarAbAdubacao(){
		if(!salvarAbas()) {
			return;
		}
		if(validarAbaAdubacao()){
			excluirLigacoesAdubacao();
			if(!pergunta_1){
				agrossilvopastorilController.getFceAgrossilvopastoril().setIndAdubacao(getPergunta_1());
				try {
					agrossilvopastorilService.salvarAtualizarAgrossilvopastoril(agrossilvopastorilController.getFceAgrossilvopastoril());
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
					throw Util.capturarException(e,Util.SEIA_EXCEPTION);
				}

			} else {
				salvarFceAgroAdubacao();
			}
			agrossilvopastorilController.setActiveTab(0);
			RequestContext.getCurrentInstance().execute("imprimirRelatorio.show()");
			RequestContext.getCurrentInstance().execute("fce_agrossilvo.hide()");
			JsfUtil.addSuccessMessage("Salvo com sucesso.");
			agrossilvopastorilController.closeFCEAgro();
		}
	}

	/**
	 * método criado para reduzir a repetição de código dentro do método salvarAbAdubacao()
	 */
	public void salvarFceAgroAdubacao (){
		agrossilvopastorilController.getFceAgrossilvopastoril().setIndAdubacao(pergunta_1);
		salvarTipoAdubacao();
		try {
			agrossilvopastorilService.salvarAtualizarAgrossilvopastoril(agrossilvopastorilController.getFceAgrossilvopastoril());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * @return
	 * @INFO Valida e salva todas as abas anteriores, retornando à aba e parando a validação na primeira aba que tiver problemas de validação
	 */
	public boolean salvarAbas(){
		boolean valido;
		valido = agrossilvopastorilController.salvarAbaDadosGerais(true);
		if(!valido){
			agrossilvopastorilController.setActiveTab(AgrossilvopastorilController.DADOS_GERAIS);
			return false;
		}
		valido = criacaoAnimaisController.salvarAbaCriacaoAnimais(true);
		if(!valido){
			agrossilvopastorilController.setActiveTab(AgrossilvopastorilController.CRIACAO_ANIMAIS);
			return false;
		}
		valido = silviculturaController.salvarAbaSilvicultura(true);
		if(!valido){
			agrossilvopastorilController.setActiveTab(AgrossilvopastorilController.SILVICULTURA);
			return false;
		}
		valido = defensivosAgricolasController.salvarAbaDefensivosAgricolas(true);
		if(!valido){
			agrossilvopastorilController.setActiveTab(AgrossilvopastorilController.DEFENSIVOS_AGRICULAS);
			return false;
		}
		return true;
	}



	/**
	 * método para validar os elementos da aba Adubação
	 * @return true se todos os elementos forem válidos
	 */
	public boolean validarAbaAdubacao(){
		boolean valido = true;

		if(Util.isNullOuVazio(pergunta_1)){
			JsfUtil.addErrorMessage(campoObrigatorio("O campo utiliza adubação"));
			valido=false;
		}
		else if(pergunta_1){
			if (Util.isNullOuVazio(agrossilvopastorilController.getFceAgrossilvopastoril().getIdeClassificacaoAdubacao())){
				JsfUtil.addErrorMessage(campoObrigatorio("O campo de classificação"));
				valido=false;
			} else if(Util.isNullOuVazio(listaTipoAdubacaoSelecionada)){
				JsfUtil.addErrorMessage(campoObrigatorio("O campo de tipo"));
				valido=false;
			}
		}
		return valido;
	}

	/**
	 * método chamado para limpar os elementos da aba
	 * quando o radioButton (Utiliza Adubação) for "Não"
	 * @param event
	 */
	public void changeUtilizaAdubacao (ValueChangeEvent event){
		if(!(Boolean) event.getNewValue()){
			agrossilvopastorilController.getFceAgrossilvopastoril().setIdeClassificacaoAdubacao(null);
			this.listaTipoAdubacaoSelecionada.clear();
		}
	}

	public String campoObrigatorio(String str){
		str+=" é de preenchimento obrigatório.";
		return str;
	}

	/**
	 * getter e setters da classe
	 */
	public AgrossilvopastorilController getAgrossilvopastorilController() {
		return agrossilvopastorilController;
	}

	public void setAgrossilvopastorilController(AgrossilvopastorilController agrossilvopastorilController) {
		this.agrossilvopastorilController = agrossilvopastorilController;
	}

	public Boolean getPergunta_1() {
		return pergunta_1;
	}

	public void setPergunta_1(Boolean pergunta1) {
		this.pergunta_1 = pergunta1;
	}

	public List<TipoAdubacao> getListaTipoAdubacaoSelecionada() {
		return listaTipoAdubacaoSelecionada;
	}

	public void setListaTipoAdubacaoSelecionada(List<TipoAdubacao> listaTipoAdubacaoSelecionada) {
		this.listaTipoAdubacaoSelecionada = listaTipoAdubacaoSelecionada;
	}

	public List<TipoAdubacao> getListaTipoAdubacao() {
		return listaTipoAdubacao;
	}

	public void setListaTipoAdubacao(List<TipoAdubacao> listaTipoAdubacao) {
		this.listaTipoAdubacao = listaTipoAdubacao;
	}

	public List<ClassificacaoAdubacao> getListaClassificacaoAdubacao() {
		return listaClassificacaoAdubacao;
	}

	public void setListaClassificacaoAdubacao(List<ClassificacaoAdubacao> listaClassificacaoAdubacao) {
		this.listaClassificacaoAdubacao = listaClassificacaoAdubacao;
	}

	public AdubacaoService getAdubacaoService() {
		return adubacaoService;
	}

	public void setAdubacaoService(AdubacaoService adubacaoService) {
		this.adubacaoService = adubacaoService;
	}

	public AgrossilvopastorilService getAgrossilvopastorilService() {
		return agrossilvopastorilService;
	}

	public void setAgrossilvopastorilService(AgrossilvopastorilService agrossilvopastorilService) {
		this.agrossilvopastorilService = agrossilvopastorilService;
	}

}