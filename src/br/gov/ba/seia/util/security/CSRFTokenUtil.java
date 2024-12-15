package br.gov.ba.seia.util.security;

import java.util.UUID;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

public class CSRFTokenUtil {
	
	public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public static void storeToken(String token) {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSession(true);
        session.setAttribute("csrfToken", token);
    }

    public static String getSessionToken() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSession(false);
        return (String) session.getAttribute("csrfToken");
    }

}
