package br.gov.ba.seia.util;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

public class SessaoUtil {
	
	private static HttpSession getSession() {
		FacesContext fc = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) fc.getExternalContext().getSession(false);
		return session;
	}

	/**
	 * Método responsável por armazenar um objeto da sessão
	 * */
	public static void adicionarObjetoSessao(String nome, Object obj) {
		getSession().setAttribute(nome, obj);
	}

	/**
	 * Método responsável por remover um objeto da sessão
	 * */
	public static void removerObjetoSessao(String nome) {
		getSession().removeAttribute(nome);
	}

	/**
	 * Método responsável por recuperar um objeto da sessão
	 * */
	public static Object recuperarObjetoSessao(String nome) {
		return getSession().getAttribute(nome);

	}

	/**
	 * Método responsável por recuperar e logo após remover um objeto da sessão
	 * */
	public static Object recuperarRemoverObjetoSessao(String nome) {
		Object obj = getSession().getAttribute(nome);
		getSession().removeAttribute(nome);
		return obj;

	}

	/**
	 * Método responsável por recuperar um ManagedBean
	 * */
	@SuppressWarnings("rawtypes")
	public static Object recuperarManagedBean(String managebean,  Class classe) {
		FacesContext fc = FacesContext.getCurrentInstance();
		ELContext elContext = fc.getELContext();
		ValueExpression ve = fc.getApplication().getExpressionFactory().createValueExpression(elContext, managebean, classe);
		return ve.getValue(elContext);
	}

	/**
	 * Método que remove o ManagedBean já construído naquela View. Utilize caso seja necessário, por exemplo, re-construir a página com o @PostConstruct.
	 * <br>
	 * <br>
	 * <b>Ex:</b>
	 * <br> removerManagedBeanFromViewScoped(<i>"bean"</i>)
	 * @author eduardo.fernandes (eduardo.fernandes@zcr.com.br)
	 * @param managedBean
	 * @since 14/07/2015
	 */
	public static void removerManagedBeanFromViewScoped(String managedBean){
		FacesContext.getCurrentInstance().getViewRoot().getViewMap().remove(managedBean);
	}
}
