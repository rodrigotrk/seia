package br.gov.ba.seia.util.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Level;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

import br.gov.ba.seia.util.Log4jUtil;
import br.gov.ba.seia.util.Util;


public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler { 

	
	
	
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

    	String url = "/login.xhtml?erro=true";

		String[] login = request.getParameterMap().get("j_username");
		String[] password = request.getParameterMap().get("j_password");
		
		boolean validado = validarCampos(login, password, url);
		
		url = validado ? url : "/login.xhtml?error=true";
		
		if (validado) {

			if (exception.getClass().isAssignableFrom(
					BadCredentialsException.class)) {
				String ip = request.getRemoteHost();

				try {
						url = SecurityService.getInstance()
								.verificarTentativaAcesso(login[0].toString(),
										ip);

				} catch (Exception e) {
					
					Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);

				}
				setDefaultFailureUrl(url);
			} else if (exception.getClass().isAssignableFrom(
					DisabledException.class)) {
				setDefaultFailureUrl("/login.xhtml?erro=true");
			} else if (exception.getClass().isAssignableFrom(
					SessionAuthenticationException.class)) {
				setDefaultFailureUrl("/login.xhtml?erro=true");
			}
		}
		setDefaultFailureUrl(url);
		super.onAuthenticationFailure(request, response, exception);

    }
    
    private boolean validarCampos(String [] login, String [] password, String url){
    	
    	if(Util.isNullOuVazio(login[0].toString()) || Util.isNullOuVazio(password[0].toString())){
    		return false;
    	}
    	
    	return true;
    }

}