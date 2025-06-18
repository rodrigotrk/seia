package br.gov.ba.seia.util;

import java.io.IOException;
import java.util.Collection;

import javax.faces.context.FacesContext;
import javax.management.RuntimeErrorException;

import org.apache.log4j.Level;
import org.primefaces.context.RequestContext;

import br.gov.ba.seia.enumerator.PaginaEnum;


/**
 * @author Alexandre Queiroz
 * @author vitor.leitao
 * @author lucas.oliveira
 * @since 17/08/2016
 * 
 **/
public class Html {
	
	private static Html this_;
	
	public static Html getCurrency(){
		
		if(this_ == null){
			this_ = new Html();
		}
		
		return this_;
		
	}
	
	/**
	 * @author alexandre.queiroz
	 * Método parar atualizar uma parte especifica da tela.
	 * */	
	public static void atualizar(String... elementos){
		
		if(RequestContext.getCurrentInstance()== null){
			throw new RuntimeErrorException(null, "Não é possivel atualizar o" + elementos.toString() + " sem uma requisição ajax." );
		}		
		
		for(String elemento : elementos) {
			RequestContext.getCurrentInstance().addPartialUpdateTarget(elemento);
		}
	}	
	
	/**
	 * @author alexandre.queiroz
	 * Método parar atualizar uma parte especifica da tela.
	 * */	
	public Html update(String... elementos ){
		
		if(RequestContext.getCurrentInstance()== null){
			throw new RuntimeErrorException(null, "Não é possivel atualizar sem uma requisição ajax.");
		}		
		
		for (String elemento : elementos) {
			RequestContext.getCurrentInstance().addPartialUpdateTarget(elemento);
		}
		
		return this_;
	}
	
	
	/**
	 * @author alexandre.queiroz
	 * @param nome do dialog sem argumentos.
	 * */	
	public Html show(String elemento){
		
		if(RequestContext.getCurrentInstance()== null){
			throw new RuntimeErrorException(null, "Não é possivel exibir o dialog "+ elemento + " sem uma requisição ajax.");
		}	
		
		RequestContext.getCurrentInstance().execute(elemento + ".show();");			
		
		return this_;
	}
	
	
	/**
	 * @author alexandre.queiroz
	 * @param nome do dialog sem argumentos.
	 * */	
	public static void exibir(String elemento){
		
		if(RequestContext.getCurrentInstance()== null){
			throw new RuntimeErrorException(null, "Não é possivel exibir o dialog "+ elemento + " sem uma requisição ajax.");
		}	
		
		RequestContext.getCurrentInstance().execute(elemento + ".show();");			
		
	}
	
	/**
	 * @author alexandre.queiroz
	 * Método esconder um dialog
	 * */	
	public Html hide(String elemento){

		if(RequestContext.getCurrentInstance()== null){
			throw new RuntimeErrorException(null, "Não é possivel esconder o dialog " + elemento + " sem uma requisição ajax.");
		}
		
		RequestContext.getCurrentInstance().execute(elemento + ".hide();");
		
		return this_;
	}
	
	/**
	 * @author alexandre.queiroz
	 * Método esconder um dialog
	 * */	
	public static void esconder(String elemento){

		if(RequestContext.getCurrentInstance()== null){
			throw new RuntimeErrorException(null, "Não é possivel esconder o dialog "+ elemento + " sem uma requisição ajax.");
		}
		
		RequestContext.getCurrentInstance().execute(elemento + ".hide();");
		
	}
	
	/**
	 * @author alexandre.queiroz
	 * Método parar adcionar uma callback
	 * */	
	public static void adicionarCallBack(String elemento){
		
		if(RequestContext.getCurrentInstance()== null){
			throw new RuntimeErrorException(null, "Não é possivel adcionar o callback "+ elemento + " sem uma requisição ajax.");
		}
		
		 RequestContext.getCurrentInstance().addCallbackParam(elemento, true);
	}

	public static void adicionarCallBack(String elemento, boolean valor){
		
		if(RequestContext.getCurrentInstance()== null){
			throw new RuntimeErrorException(null, "Não é possivel adcionar o callback "+ elemento + " sem uma requisição ajax.");
		}
		
		 RequestContext.getCurrentInstance().addCallbackParam(elemento, valor);
		
	}
	
	public static void executarJS(String functionJS){
		
		if(RequestContext.getCurrentInstance()== null){
			throw new RuntimeErrorException(null, "Não é possivel executar " + functionJS + " sem uma requisição ajax.");
		}
		
		RequestContext.getCurrentInstance().execute(functionJS);
		
	}
	
	
	/**u
	 * @author alexandre.queiroz
	 * Método parar atualizar uma parte especifica da tela.
	 * */	
	public static void atualizar(String elemento){
		
		if(RequestContext.getCurrentInstance()== null){
			throw new RuntimeErrorException(null, "Não é possivel atualizar o" + elemento + " sem uma requisição ajax." );
		}		
		
		RequestContext.getCurrentInstance().addPartialUpdateTarget(elemento);
		
	}	
	
	
	/**
	 * @author alexandre.queiroz
	 * Método parar atualizar partes especificas da tela.
	 * */	
	public static void atualizar(Collection<String> elementos){
		
		if(RequestContext.getCurrentInstance()== null){
			throw new RuntimeErrorException(null, "Não é possivel atualizar os elementos sem uma requisição ajax.");
		}	

		RequestContext.getCurrentInstance().addPartialUpdateTargets(elementos);
		
	}
	
	/**
	 * Método que atribui <i>false</i> para o <b>validationFailed</b> e envia ao callbackParam, permitindo que o <i>dialog</i> feche.
	 * 
	 * <blockquote>
	 * Implementar no xhtml a function validaTela:
	 * <p>
	 * <b>oncomplete="validaTela(xhr, status, args, NOME_DO_WIDGETVAR);"</b>
	 * </p>
	 * </blockquote>
	 * Substituir NOME_DO_WIDGETVAR pelo valor dado ao <b>widgetVar</b> do <i>dialog</i>
	 * 
	 * @author eduardo.fernandes
	 * @since 22/11/2016
	 */
	public static void telaValida() {
		RequestContext.getCurrentInstance().addCallbackParam("validationFailed", false);
	}

	/**
	 * Método que atribui <i>true</i> para o <b>validationFailed</b> e envia ao callbackParam, mantendo o <i>dialog</i> aberto.
	 * 
	 * <blockquote>
	 * Implementar no xhtml a function validaTela conforme exemplo:
	 * <p>
	 * <b>oncomplete="validaTela(xhr, status, args, NOME_DO_WIDGETVAR);"</b>
	 * </p>
	 * </blockquote>
	 * Substituir NOME_DO_WIDGETVAR pelo valor dado ao <b>widgetVar</b> do <i>dialog</i>
	 * 
	 * @author eduardo.fernandes
	 * @since 22/11/2016
	 */
	public static void telaInvalida() {
		RequestContext.getCurrentInstance().addCallbackParam("validationFailed", true);
	}
	
	/**
	 * 
	 * Método para redirecionar a página do navegador para a <i>url</i> passada no parâmetro.
	 * 
	 * @author eduardo.fernandes
	 * @since 13/12/2016
	 * @param url
	 */
	public static void redirecionarPagina(String url) {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(url);
		}
		catch (IOException e) {
			Log4jUtil.log(Html.class.getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}

	public static void redirecionarPagina(PaginaEnum pagina) {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(pagina.getUrl());
		}
		catch (IOException e) {
			Log4jUtil.log(Html.class.getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);

		}
	}
	
	
	
	/**
	 * 
	 * Método para redirecionar a página do navegador para a <i>url</i> passada no parâmetro.
	 * 
	 * @author eduardo.fernandes
	 * @since 13/12/2016
	 * @param url
	 */
	public static void redirecionarPaginaComRedirect(String url) {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(url + "?faces-redirect=true");
		}
		catch (IOException e) {
			Log4jUtil.log(Html.class.getName(),Level.ERROR, e);
			throw Util.capturarException(e, Util.SEIA_EXCEPTION);
			
		}
	}

	public static void redirecionar(PaginaEnum pagina){
		redirecionar(pagina.getUrl());
	}
	
	public static void redirecionar(String pagina){
		
		if(FacesContext.getCurrentInstance() == null || FacesContext.getCurrentInstance().getExternalContext() == null){
			throw new RuntimeErrorException(null, "Não foi possível redirecionar.");
		}
		
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect(pagina);
		} catch (IOException e) {
			Log4jUtil.log(Html.class.getName(),Level.ERROR, e);
			throw new RuntimeErrorException(null, "Não foi possível redirecionar.");
		}
	}
	
	public static String recuperarParametro(String parameter){
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(parameter);
	}
}