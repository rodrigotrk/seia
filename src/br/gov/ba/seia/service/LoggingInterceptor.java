package br.gov.ba.seia.service;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.log4j.Level;

import br.gov.ba.seia.util.Log4jUtil;

public class LoggingInterceptor {
	
	@AroundInvoke
	public Object interceptador(InvocationContext ic) throws Exception {	
		Log4jUtil.log(LoggingInterceptor.class.getName(), Level.INFO,("**LOG**: [ " + ic.getMethod().getDeclaringClass().getSimpleName() + " ] - Método: [ "+ ic.getMethod().getName() + " ]"));	
		return ic.proceed();
	}

}
