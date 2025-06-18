package br.gov.ba.seia.handlers;

import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.apache.log4j.Level;

import br.gov.ba.seia.exception.SeiaRuntimeException;
import br.gov.ba.seia.util.ExceptionUtil;
import br.gov.ba.seia.util.Log4jUtil;
/**
 * Classe que trata as exceções no SEIA
 * @author 
 *
 */
public class SeiaExceptionHandler extends ExceptionHandlerWrapper {

	private ExceptionHandler wrapped;

	SeiaExceptionHandler(ExceptionHandler exception) {
		this.wrapped = exception;
	}

	@Override
	public ExceptionHandler getWrapped() {
		return wrapped;
	}

	@Override
	public void handle() {

		final Iterator<ExceptionQueuedEvent> iterator = getUnhandledExceptionQueuedEvents().iterator();

		while (iterator.hasNext()) {

			ExceptionQueuedEvent event = (ExceptionQueuedEvent) iterator.next();
			ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

			Throwable erro = context.getException();

			try {
							
				String mensagem = new String();
				mensagem += "Detalhamento do ERRO - " + ExceptionUtil.getLog(erro);

				if(isViewExpiredException(erro)) {
					System.out.println("Sessão expirada. Usuário redirecionado para página de login.");
				}
				else{
					ExceptionUtil.enviarLog(erro, mensagem, ExceptionUtil.getEmailsSEIA());
				}
			}
			catch (Exception e) {
				Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			}
			finally {
				iterator.remove();
			}
		}
		getWrapped().handle();
	}

	private boolean isViewExpiredException(Throwable erro) {
		return tipoErro(erro) instanceof ViewExpiredException;
	}

	private Throwable tipoErro(Throwable erro) {
		return encontrarTipoErro(erro);
	}

	private Throwable encontrarTipoErro(Throwable erro) {
		if(!(erro instanceof ViewExpiredException || erro instanceof SeiaRuntimeException)) {
			if(erro.getCause()!=null) {
				return encontrarTipoErro(erro.getCause());
			}
		}
		return erro;
	}
}
