package br.gov.ba.seia.util;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.context.RequestContext;

import br.gov.ba.seia.enumerator.ConfigEnum;

public class JsfUtil {

    public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
        int size = selectOne ? entities.size() + 1 : entities.size();
        SelectItem[] items = new SelectItem[size];
        int i = 0;
        if (selectOne) {
            items[0] = new SelectItem("", "---");
            i++;
        }
        for (Object x : entities) {
            items[i++] = new SelectItem(x, x.toString());
        }
        return items;
    }

    public static void addErrorMessage(Exception ex, String defaultMsg) {
        String msg = ex.getLocalizedMessage();
        if (msg != null && msg.length() > 0) {
            addErrorMessage(msg);
        } else {
            addErrorMessage(defaultMsg);
        }
    }

    public static void addErrorMessages(List<String> messages) {
        for (String message : messages) {
            addErrorMessage(message);
        }
    }

    public static boolean validaMensagemRepetida(String msg) {
    	
    	if(FacesContext.getCurrentInstance() != null && !Util.isNullOuVazio(FacesContext.getCurrentInstance().getMessageList())) {
    		
	    	for (FacesMessage m : FacesContext.getCurrentInstance().getMessageList()) {
				
	    		if(!Util.isNullOuVazio(m.getDetail()) && m.getDetail().equals(msg)) { 
					return false;
				}
			}
    	}
    	
    	return true;
    }

    public static void addErrorMessage(String msg) {
    	
    	if(JsfUtil.validaMensagemRepetida(msg)) {
    		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
    		FacesContext.getCurrentInstance().addMessage(null, facesMsg);
    	}
    }

    public static void addSuccessMessage(String msg) {
    	
    	if(JsfUtil.validaMensagemRepetida(msg)) {
	        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
	        FacesContext.getCurrentInstance().addMessage("Sucesso", facesMsg);
    	}
    }
    
    public static void addWarnMessage(String msg) {
    	
    	if(JsfUtil.validaMensagemRepetida(msg)) {
	    	FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
	        FacesContext.getCurrentInstance().addMessage("Mensagem", facesMsg);
    	}
    }
    
    @Deprecated
    public static void addSuccessMessage(String msg, String fiedlId) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext.getCurrentInstance().addMessage(fiedlId, facesMsg);  
    }

    public static String getRequestParameter(String key) {
        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(key);
    }

    public static Object getObjectFromRequestParameter(String requestParameterName, Converter converter, UIComponent component) {
        String theId = JsfUtil.getRequestParameter(requestParameterName);
        return converter.getAsObject(FacesContext.getCurrentInstance(), component, theId);
    }
    
    public static void setListenerValidadao(Boolean validacao, String message){    	
        RequestContext context = RequestContext.getCurrentInstance();        
        
        if (!validacao){
        	JsfUtil.addErrorMessage(message);
        }
        context.addCallbackParam("validacao", validacao);  
    
    }
    
    public static String getIP(){
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	    String ip = request.getLocalAddr();  
	    return ip;
	}
    
    public static String getIpExterno(){
		return getIP();
	}
    
    public static Flash flashScope (){
    	return (FacesContext.getCurrentInstance().getExternalContext().getFlash());
    }
    
    public static void redirect(String url) {
    	try{
    		FacesContext.getCurrentInstance().getExternalContext().redirect(url);    		
    	}
    	catch(Exception e){    		
    	}    	
    }
}