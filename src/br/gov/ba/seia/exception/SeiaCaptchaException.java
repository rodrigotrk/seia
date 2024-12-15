package br.gov.ba.seia.exception;
/**
 * Tratamento de exceção para captcha
 * @author 
 *
 */
public class SeiaCaptchaException extends RuntimeException {

	private static final long serialVersionUID = -4308632597093449384L;
	
	public SeiaCaptchaException(Throwable cause) {
		super(cause);
	}
	
	public SeiaCaptchaException(String message) {
		super(message);
	}
		
	public SeiaCaptchaException(String message, Throwable cause) {
		super(message,cause);
	}
}