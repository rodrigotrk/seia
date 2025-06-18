package br.gov.ba.seia.handlers;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;
/**
 * Classe factory que trata as exceções no SEIA
 * @author 
 *
 */
public class SeiaExceptionHandlerFactory extends ExceptionHandlerFactory {
    
	private ExceptionHandlerFactory parent;
 
    public SeiaExceptionHandlerFactory(ExceptionHandlerFactory parent) {
        this.parent = parent;
    }
 
    @Override
    public ExceptionHandler getExceptionHandler() {
        ExceptionHandler handler = new SeiaExceptionHandler(parent.getExceptionHandler());
        return handler;
    }

}
