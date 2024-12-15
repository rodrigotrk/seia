package br.gov.ba.seia.util.security;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.session.ConcurrentSessionControlStrategy;

import br.gov.ba.seia.entity.Usuario;
import br.gov.ba.seia.exception.SeiaCaptchaException;
import br.gov.ba.seia.exception.SeiaUserLimitException;
import br.gov.ba.seia.filter.RecaptchaService;
public class SeiaLimitUsers extends ConcurrentSessionControlStrategy {
	
	private int MAX_USERS = 2000;
   
	private SessionRegistry sessionRegistry;
    
	

    
    public SeiaLimitUsers(SessionRegistry sessionRegistry) {
		super(sessionRegistry);
		this.sessionRegistry = sessionRegistry;
	}

    @Override
    public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
    	setMaximumSessions(1);
    	setAlwaysCreateSession(true);
    	setExceptionIfMaximumExceeded(true);
        if (getActiveSessions().size() >= MAX_USERS) {
            throw new SeiaUserLimitException("O limite de usuários da aplicação foi excedido.");
        }
        SecurityService securityServ = SecurityService.getInstance();
        RecaptchaService service = new RecaptchaService();
		
        boolean isHuman = service.checkRecaptcha(request);
        System.out.println(authentication.getName());
        
        if(!isHuman && securityServ.getCountErroLogin(authentication.getName()) > 0) {
        
        	
        	throw new SeiaCaptchaException("Por favor preencher o Captcha!");
       
        }else{
        	
        	Usuario user= new Usuario();
        	user.setDscLogin(authentication.getName());
			securityServ.apagarRegistroTentativa(user);
        }
        
        super.onAuthentication(authentication, request, response);
        securityServ.atualizarUltimoLogin(authentication,request.getRemoteHost());
    }
    
    private List<SessionInformation> getActiveSessions() {
        final List<Object> principals = sessionRegistry.getAllPrincipals();
        if (principals != null) {
            List<SessionInformation> sessions = new ArrayList<SessionInformation>();
            for (Object principal : principals) {
                sessions.addAll(sessionRegistry.getAllSessions(principal, false));
            }
            return sessions;
        }
        return Collections.emptyList();
    }
	
	
}
