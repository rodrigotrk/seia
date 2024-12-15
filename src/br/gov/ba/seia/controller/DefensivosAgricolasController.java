package br.gov.ba.seia.controller;


import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.AgroClassificacaoDefensivo;
import br.gov.ba.seia.entity.AgroClassificacaoDefensivoPK;
import br.gov.ba.seia.entity.AgroClassificacaoToxicologica;
import br.gov.ba.seia.entity.AgroClassificacaoToxicologicaPK;
import br.gov.ba.seia.entity.AgroDefensivoAgricola;
import br.gov.ba.seia.entity.AgroDefensivoAgricolaPK;
import br.gov.ba.seia.entity.ClassificacaoDefensivoAgricola;
import br.gov.ba.seia.entity.ClassificacaoToxicologica;
import br.gov.ba.seia.entity.TipoDefensivoAgricola;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.service.AgrossilvopastorilService;
import br.gov.ba.seia.service.DefensivoAgricolaService;
import br.gov.ba.seia.util.JsfUtil;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;


@Named("defensivosAgricolasController")
@ViewScoped
public class DefensivosAgricolasController {

	private static final int SINTETICOS = 2;
	@Inject
	private AgrossilvopastorilController agrossilvopastorilController;
	@EJB
	private AgrossilvopastorilService agrossilvopastorilService;
	@EJB
	private DefensivoAgricolaService defensivoAgricolaService;

	private Boolean pergunta_1;
	private List<ClassificacaoDefensivoAgricola> listaClassificacaoDefensivosAgricolas;
	private List<ClassificacaoDefensivoAgricola> listaClassificacaoDefensivosAgricolasSelecionada;
	private List<TipoDefensivoAgricola> listaDefensivosAgricolas;
	private List<TipoDefensivoAgricola> listaDefensivosAgricolasSelecionada;
	private List<ClassificacaoToxicologica> listaClassificacaoToxicologica;
	private List<ClassificacaoToxicologica> listaClassificacaoToxicologicaSelecionada;

	public void init(){
		carregarListaDefensivosAgricolas();
		carregarListaClassificacaoDefensivosAgricolas();
		carregarListaClassificacaoToxicologica();

		if(agrossilvopastorilController.isFceSalvo()){
			carregarAbaDefensivosAgricolas();
		}
	}

	/**
	 * método para carregar os elementos da aba Defensivos caso exista FCE Agro
	 */
	public void carregarAbaDefensivosAgricolas(){
		if(!Util.isNullOuVazio(agrossilvopastorilController.getFceAgrossilvopastoril())){
			pergunta_1 = agrossilvopastorilController.getFceAgrossilvopastoril().getIndDefensivoAgricola();
			carregarListaClassificacaoToxicologicaSelecionada();
			carregarListaDefensivosAgricolasSelecionada();
			carregarListaClassificacaoDefensivosAgricolasSelecionada();
		}
	}

	/**
	 * método que busca do banco os registros que preenchem a listaClassificacaoDefensivosAgricolasSelecionada
	 */
	public void carregarListaClassificacaoDefensivosAgricolasSelecionada (){
		listaClassificacaoDefensivosAgricolasSelecionada = new ArrayList<ClassificacaoDefensivoAgricola>();
		try {
			listaClassificacaoDefensivosAgricolasSelecionada = defensivoAgricolaService.listarClassificacaoDefensivoAgricolaByIdeFceAgro(agrossilvopastorilController.getFceAgrossilvopastoril());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * método que busca do banco os registros que preenchem a listaDefensivosAgricolasSelecionada
	 */
	public void carregarListaDefensivosAgricolasSelecionada(){
		listaDefensivosAgricolasSelecionada = new ArrayList<TipoDefensivoAgricola>();
		try {
			listaDefensivosAgricolasSelecionada = defensivoAgricolaService.listarTipoDefensivoAgricolaByIdeFceAgro(agrossilvopastorilController.getFceAgrossilvopastoril());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * método que busca do banco os registros que preenchem a listaClassificacaoToxicologicaSelecionada
	 */
	public void carregarListaClassificacaoToxicologicaSelecionada(){
		listaClassificacaoToxicologicaSelecionada = new ArrayList<ClassificacaoToxicologica>();
		try {
			listaClassificacaoToxicologicaSelecionada = defensivoAgricolaService.listarClassificacaoToxicologicaByIdeFceAgro(agrossilvopastorilController.getFceAgrossilvopastoril());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * método que busca do banco a lista de Defensivos Agricolas, 2º elemento da aba
	 */
	public void carregarListaDefensivosAgricolas(){
		listaDefensivosAgricolas = new ArrayList<TipoDefensivoAgricola>();
		try {
			listaDefensivosAgricolas = defensivoAgricolaService.listarDefensivoAgricola();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * método que busca do banco a lista de Classificação Defensivos Agricolas, 3º elemento da aba
	 */
	public void carregarListaClassificacaoDefensivosAgricolas(){
		listaClassificacaoDefensivosAgricolas = new ArrayList<ClassificacaoDefensivoAgricola>();
		try {
			listaClassificacaoDefensivosAgricolas = defensivoAgricolaService.listarClassificacaoDefensivoAgricola();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * método que busca do banco a lista de Classificação Toxicológica, 4º elemento da aba
	 */
	public void carregarListaClassificacaoToxicologica(){
		listaClassificacaoToxicologica = new ArrayList<ClassificacaoToxicologica>();
		try {
			listaClassificacaoToxicologica = defensivoAgricolaService.listarClassificacaoToxicologica();
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * método para salvar a listaDefensivosAgricolasSelecionada,
	 * ele salva um objeto composto por IdeFceAgrossilvopastoril e ideTipoDefensivoAgricola em uma tabela de ligação.
	 */
	public void salvarTipoDefensivoAgricola (){
		if(!Util.isNullOuVazio(listaDefensivosAgricolasSelecionada)){
			for(TipoDefensivoAgricola agricola : listaDefensivosAgricolasSelecionada){
				AgroDefensivoAgricola agroDefensivoAgricola = new AgroDefensivoAgricola(new AgroDefensivoAgricolaPK(agrossilvopastorilController.getFceAgrossilvopastoril().getIdeFceAgrossilvopastoril(), agricola.getIdeTipoDefensivoAgricola()));
				agroDefensivoAgricola.setIdeFceAgrossilvopastoril(agrossilvopastorilController.getFceAgrossilvopastoril());
				agroDefensivoAgricola.setIdeTipoDefensivoAgricola(agricola);
				try {
					defensivoAgricolaService.salvarAgroDefensivoAgricola(agroDefensivoAgricola);
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
				}
			}
		}
	}

	/**
	 * método para salvar a listaDefensivosAgricolasSelecionada,
	 * ele salva um objeto composto por IdeFceAgrossilvopastoril e ideClassificacaoToxicologica em uma tabela de ligação.
	 */
	public void salvarClassificacaoToxicologica (){
		if(!Util.isNullOuVazio(listaClassificacaoToxicologicaSelecionada)){
			for(ClassificacaoToxicologica classificacaoToxicologica : listaClassificacaoToxicologicaSelecionada){
				AgroClassificacaoToxicologica agroClassificacaoToxicologica = new AgroClassificacaoToxicologica(new AgroClassificacaoToxicologicaPK(agrossilvopastorilController.getFceAgrossilvopastoril().getIdeFceAgrossilvopastoril(), classificacaoToxicologica.getIdeClassificacaoToxicologica()));
				agroClassificacaoToxicologica.setIdeFceAgrossilvopastoril(agrossilvopastorilController.getFceAgrossilvopastoril());
				agroClassificacaoToxicologica.setIdeClassificacaoToxicologica(classificacaoToxicologica);
				try {
					defensivoAgricolaService.salvarAgroClassificacaoToxicologica(agroClassificacaoToxicologica);
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
				}
			}
		}
	}

	/**
	 * método para salvar a listaDefensivosAgricolasSelecionada,
	 * ele salva um objeto composto por IdeFceAgrossilvopastoril e ideClassificacaoDefensivoAgricola em uma tabela de ligação.
	 */
	public void salvarClassificacaoDefensivoAgricola (){
		if(!Util.isNullOuVazio(listaClassificacaoDefensivosAgricolasSelecionada)){
			for(ClassificacaoDefensivoAgricola classificacaoDefensivoAgricola : listaClassificacaoDefensivosAgricolasSelecionada){
				AgroClassificacaoDefensivo agroClassificacaoDefensivo = new AgroClassificacaoDefensivo(new AgroClassificacaoDefensivoPK(agrossilvopastorilController.getFceAgrossilvopastoril().getIdeFceAgrossilvopastoril(), classificacaoDefensivoAgricola.getIdeClassificacaoDefensivoAgricola()));
				agroClassificacaoDefensivo.setIdeFceAgrossilvopastoril(agrossilvopastorilController.getFceAgrossilvopastoril());
				agroClassificacaoDefensivo.setIdeClassificacaoDefensivoAgricola(classificacaoDefensivoAgricola);
				try {
					defensivoAgricolaService.salvarAgroClassificacaoDefensivoAgricola(agroClassificacaoDefensivo);
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
				}
			}
		}
	}

	/**
	 * método para validar a aba Defensivos Agrícolas
	 * @return true caso todos os elementos da aba sejam válidos
	 */
	public boolean validarAbaDefensivosAgricolas () {
		boolean valido = true;
		if(Util.isNull(pergunta_1)){
			JsfUtil.addErrorMessage(campoObrigatorio("O campo Utiliza defensivos agrícolas"));
			valido=false;
		} else if(pergunta_1){
			if (Util.isNullOuVazio(listaDefensivosAgricolasSelecionada)){
				JsfUtil.addErrorMessage(campoObrigatorio("O campo de defensivos"));
				valido=false;
			}
			if(pergunta_1 && Util.isNullOuVazio(listaClassificacaoDefensivosAgricolasSelecionada)){
				JsfUtil.addErrorMessage(campoObrigatorio("O campo de classificação"));
				valido=false;
			}else if(!Util.isNullOuVazio(listaClassificacaoDefensivosAgricolasSelecionada) &&
					listaClassificacaoDefensivosAgricolasSelecionada.contains(new ClassificacaoDefensivoAgricola(2)) && //2 = Sintéticos
					Util.isNullOuVazio(listaClassificacaoToxicologicaSelecionada)){
				JsfUtil.addErrorMessage(campoObrigatorio("O campo de classificação toxicológica"));
				valido=false;
			}
		}
		return valido;
	}

	/**
	 * método para salvar os elementos da aba caso a validação retorne true
	 */
	public boolean salvarAbaDefensivosAgricolas(Boolean isSalvarFinal){
		if(validarAbaDefensivosAgricolas()){
			if(!pergunta_1){
				excluirLigacoes();
				agrossilvopastorilController.getFceAgrossilvopastoril().setIndDefensivoAgricola(getPergunta_1());
				try {
					agrossilvopastorilService.salvarAtualizarAgrossilvopastoril(agrossilvopastorilController.getFceAgrossilvopastoril());
				} catch (Exception e) {
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
				}
			} else if (agrossilvopastorilController.isFceSalvo()) {
				excluirLigacoes();
				setarIndSalvarListasESalvarAgro();
			} else {
				setarIndSalvarListasESalvarAgro();
			}
			if(!isSalvarFinal){
				JsfUtil.addSuccessMessage("Salvo com sucesso.");
				agrossilvopastorilController.avancarAba();
			}
			return true;
		}
		return false;
	}

	/**
	 * método que salva todos os elementos da aba Defensivos Agrícolas e atualiza a FCE Agro
	 */
	public void setarIndSalvarListasESalvarAgro(){
		agrossilvopastorilController.getFceAgrossilvopastoril().setIndDefensivoAgricola(getPergunta_1());
		salvarTipoDefensivoAgricola();
		salvarClassificacaoDefensivoAgricola();
		salvarClassificacaoToxicologica();
		try {
			agrossilvopastorilService.salvarAtualizarAgrossilvopastoril(agrossilvopastorilController.getFceAgrossilvopastoril());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * método que verifica se existe a opção Sintéticos marcada na listaClassificacaoDefensivosAgricolasSelecionada
	 * @return true caso exista
	 */
	public boolean isTemSinteticos(){
		return !Util.isNullOuVazio(listaClassificacaoDefensivosAgricolasSelecionada) && listaClassificacaoDefensivosAgricolasSelecionada.contains(new ClassificacaoDefensivoAgricola(SINTETICOS));
	}

	/**
	 * método para excluir da tabela de ligação os registros que contém aquela ideFceAgro através deu ma namedQuery
	 */
	public void excluirLigacoes(){
		try {
			defensivoAgricolaService.excluirLigacaoDefensivosAgricolas(agrossilvopastorilController.getFceAgrossilvopastoril());
			defensivoAgricolaService.excluirLigacaoClassificacao(agrossilvopastorilController.getFceAgrossilvopastoril());
			defensivoAgricolaService.excluirLigacaoClassificacaoToxicologica(agrossilvopastorilController.getFceAgrossilvopastoril());
		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
            	throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}

	/**
	 * método chamado para limpar os elementos da aba
	 * quando o radioButton (Utiliza Defensivos) for "Não"
	 * @param event
	 */
	public void changeUtilizaDefensivos (ValueChangeEvent event){
		if(!(Boolean) event.getNewValue()){
			this.listaClassificacaoDefensivosAgricolasSelecionada.clear();
			this.listaDefensivosAgricolasSelecionada.clear();
			this.listaClassificacaoToxicologicaSelecionada.clear();
		}
	}

	/**
	 * método chamado para limpar a listaClassificacaoToxicologicaSelecionada
	 * quando a opção Sintéticos for desmarcada da lista
	 */
	public void changeSinteticos (){
		if(!isTemSinteticos()){
			this.listaClassificacaoToxicologicaSelecionada.clear();
		}
	}

	public String campoObrigatorio(String str){
		str+=" é de preenchimento obrigatório.";
		return str;
	}

	/**
	 * getters e setters da classe
	 */
	public Boolean getPergunta_1() {
		return pergunta_1;
	}

	public void setPergunta_1(Boolean pergunta_1) {
		this.pergunta_1 = pergunta_1;
	}

	public List<ClassificacaoDefensivoAgricola> getListaClassificacaoDefensivosAgricolas() {
		return listaClassificacaoDefensivosAgricolas;
	}

	public void setListaClassificacaoDefensivosAgricolas(List<ClassificacaoDefensivoAgricola> listaClassificacaoDefensivosAgricolas) {
		this.listaClassificacaoDefensivosAgricolas = listaClassificacaoDefensivosAgricolas;
	}

	public List<ClassificacaoDefensivoAgricola> getListaClassificacaoDefensivosAgricolasSelecionada() {
		return listaClassificacaoDefensivosAgricolasSelecionada;
	}

	public void setListaClassificacaoDefensivosAgricolasSelecionada(List<ClassificacaoDefensivoAgricola> listaClassificacaoDefensivosAgricolasSelecionada) {
		this.listaClassificacaoDefensivosAgricolasSelecionada = listaClassificacaoDefensivosAgricolasSelecionada;
	}

	public List<TipoDefensivoAgricola> getListaDefensivosAgricolas() {
		return listaDefensivosAgricolas;
	}

	public void setListaDefensivosAgricolas(List<TipoDefensivoAgricola> listaDefensivosAgricolas) {
		this.listaDefensivosAgricolas = listaDefensivosAgricolas;
	}

	public List<TipoDefensivoAgricola> getListaDefensivosAgricolasSelecionada() {
		return listaDefensivosAgricolasSelecionada;
	}

	public void setListaDefensivosAgricolasSelecionada(List<TipoDefensivoAgricola> listaDefensivosAgricolasSelecionada) {
		this.listaDefensivosAgricolasSelecionada = listaDefensivosAgricolasSelecionada;
	}

	public List<ClassificacaoToxicologica> getListaClassificacaoToxicologica() {
		return listaClassificacaoToxicologica;
	}

	public void setListaClassificacaoToxicologica(List<ClassificacaoToxicologica> listaClassificacaoToxicologica) {
		this.listaClassificacaoToxicologica = listaClassificacaoToxicologica;
	}

	public List<ClassificacaoToxicologica> getListaClassificacaoToxicologicaSelecionada() {
		return listaClassificacaoToxicologicaSelecionada;
	}

	public void setListaClassificacaoToxicologicaSelecionada(List<ClassificacaoToxicologica> listaClassificacaoToxicologicaSelecionada) {
		this.listaClassificacaoToxicologicaSelecionada = listaClassificacaoToxicologicaSelecionada;
	}

	public DefensivoAgricolaService getDefensivoAgricolaService() {
		return defensivoAgricolaService;
	}

	public void setDefensivoAgricolaService(DefensivoAgricolaService defensivoAgricolaService) {
		this.defensivoAgricolaService = defensivoAgricolaService;
	}

	public AgrossilvopastorilController getAgrossilvopastorilController() {
		return agrossilvopastorilController;
	}

	public void setAgrossilvopastorilController(AgrossilvopastorilController agrossilvopastorilController) {
		this.agrossilvopastorilController = agrossilvopastorilController;
	}

	public AgrossilvopastorilService getAgrossilvopastorilService() {
		return agrossilvopastorilService;
	}

	public void setAgrossilvopastorilService(AgrossilvopastorilService agrossilvopastorilService) {
		this.agrossilvopastorilService = agrossilvopastorilService;
	}
}
