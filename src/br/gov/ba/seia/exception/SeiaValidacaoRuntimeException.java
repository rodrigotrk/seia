package br.gov.ba.seia.exception;

import java.util.List;

import javax.ejb.ApplicationException;

/**
 * Tratamento de exceção para validar tempo de execução
 * @author 
 *
 */
@ApplicationException(rollback=true)
public class SeiaValidacaoRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 9042380383749110586L;
	
	private List<String> messages;
	
	public SeiaValidacaoRuntimeException(Throwable cause) {
		super(cause);
	}
	
	public SeiaValidacaoRuntimeException(String message) {
		super(message);
	}
	
	public SeiaValidacaoRuntimeException(List<String> messages) {
		super(returnSummaryMessage(messages));
		this.messages = messages;
	}
	
	public SeiaValidacaoRuntimeException(String message, Throwable cause) {
		super(message,cause);
	}

	private static String returnSummaryMessage(List<String> messages) {
		StringBuilder message = new StringBuilder();
		for (int i = 0; i < messages.size(); i++) {
			String string = messages.get(i);
			if(messages.size() > 1 && i != messages.size() - 1) {
				message.append(string + "<br/>");
			}
			else {
				message.append(string);
			}
		}
		return message.toString();
	}

	public List<String> getMessages() {
		return messages;
	}
	
}
