package br.gov.ba.seia.controller;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.inject.Named;

import org.apache.log4j.Level;

import br.gov.ba.seia.entity.LocalizacaoGeografica;
import br.gov.ba.seia.enumerator.ClassificacaoSecaoEnum;
import br.gov.ba.seia.facade.LocalizacaoGeograficaServiceFacade;
import br.gov.ba.seia.faces.ViewScoped;
import br.gov.ba.seia.interfaces.LocalizacaoGeofraficaInterface;
import br.gov.ba.seia.util.GeoBahiaUtil;
import br.gov.ba.seia.util.Html;
import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.MensagemUtil;
import br.gov.ba.seia.util.Util;

/**
 * @author	Alexandre Queiroz
 * @since	01/2017 
 * 
 * Controller feio para gerenciar os componentes a biblioteca "seia:s"
 */

@ViewScoped
@Named("seiaController")
public class SeiaController implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private LocalizacaoGeograficaServiceFacade localizacaoGeograficaFacade;
	
	
	private static final String SEIA_INDEX = "index_seia.php?acao=view&";
	private static final String IDE_LOC = "idloc=";
	private static final String TIPO_PARAMETRO = "&tipo=1";
	
	
	private LocalizacaoGeografica ideLocalizacaoGeografica;
	
	private LocalizacaoGeofraficaInterface objetoLocalizacao;
	private String componente;
	
	
	
	
	 /**
	  *
	  * INICIO VISUALIZAR LOCALIZAÇÂO GEOGRAFICA 
	  **/
	
	public String visualizarLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		if(isLocalizacaoGeograficaSalva(ideLocalizacaoGeografica)){
			return criarURL(ideLocalizacaoGeografica);
		}
		
		return "";
	}
	
	private boolean isLocalizacaoGeograficaSalva(LocalizacaoGeografica ideLocalizacaoGeografica) {
		try{
		
			return 
				!Util.isNullOuVazio(ideLocalizacaoGeografica) || 
				!Util.isNullOuVazio(ideLocalizacaoGeografica.getIdeLocalizacaoGeografica());
		
		}catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
		}
	}
	
	public static String criarURL(LocalizacaoGeografica ideLocalizacaoGeografica) {
		return 
			new StringBuilder()
				.append("window.open('")
				.append(GeoBahiaUtil.obterUrlGeoBahia(montarLink(ideLocalizacaoGeografica)))
				.append("');").toString();
	}
	
	private static String montarLink(LocalizacaoGeografica ideLocalizacaoGeografica) {
		if(ideLocalizacaoGeografica==null || ideLocalizacaoGeografica.getIdeLocalizacaoGeografica()==null){
			return "";
		}
		
		return SEIA_INDEX + IDE_LOC + ideLocalizacaoGeografica.getIdeLocalizacaoGeografica().toString() + TIPO_PARAMETRO;
	}
	

	/**
	 * FINAL VISUALIZAR LOCALIZAÇÂO GEOGRAFICA 
	 *
	 **/
	

	
	
	/**
	 *
	 * INICIO INPUT LOC GEO
	 **/
	

	/**
	 * FINAL INPUT LOC GEO
	 *
	 **/
	
	
	

	/**
	 *
	 * INICIO EXCLUIR LOC GEO
	 * 
	 * 
	 * Excluir ainda não funciona, é preciso descobrir uma maneira de colocar o dialog dentro do compomente 
	 * ou no template.
<<<<<<< HEAD
	 *
	public void excluirLocalizacaoGeografica(){
		try {
			
			if(objetoLocalizacao != null) {
				localizacaoGeograficaFacade.excluirTudoPorLocalizacaoGeografica(objetoLocalizacao.limparLocalizacaoGeografica());
			}
			
			if(componente != null){ 
				Html.atualizar(componente);
			}
			
			MensagemUtil.msg0005();
		
	 **/
	
	public void excluirLocalizacaoGeografica(){
		try {
			
			if(!Util.isNull(objetoLocalizacao)){
				localizacaoGeograficaFacade.excluirDadoGeografico(objetoLocalizacao.limparLocalizacaoGeografica());
			}
			
			if(componente!=null){
				Html.atualizar(componente);
			}

			MensagemUtil.msg0005();

		} catch (Exception e) {
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
		}
	}
	
	/**
	 * FINAL EXCLUIR LOC GEO
	 *
	 **/
	
	

	
	
	
	
	
	
	
	/**
	 * Aviso
	 * 
	 * Esse Metódo serve para a chamada do HTMT, Nunca o apague.
	 *
	 * */
	public ClassificacaoSecaoEnum classificacaoSecaoEnum(ClassificacaoSecaoEnum classificacaoSecaoEnum){
		return classificacaoSecaoEnum;
	}
	
	public LocalizacaoGeografica getIdeLocalizacaoGeografica() {
		return ideLocalizacaoGeografica;
	}

	public void setIdeLocalizacaoGeografica(LocalizacaoGeografica ideLocalizacaoGeografica) {
		this.ideLocalizacaoGeografica = ideLocalizacaoGeografica;
	}

	public String getComponente() {
		return componente;
	}

	public void setComponente(String componente) {
		this.componente = componente;
	}

	public LocalizacaoGeofraficaInterface getObjetoLocalizacao() {
		return objetoLocalizacao;
	}

	public void setObjetoLocalizacao(LocalizacaoGeofraficaInterface objetoLocalizacao) {
		this.objetoLocalizacao = objetoLocalizacao;
	}

}