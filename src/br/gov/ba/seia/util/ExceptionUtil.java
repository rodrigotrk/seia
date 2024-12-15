package br.gov.ba.seia.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Level;

import br.gov.ba.seia.exception.SeiaRuntimeException;


public class ExceptionUtil {

	public static final int SEIA_EXCEPTION = 1;
	
	private static final String SEIA_EMAILS[] = new String[] {
		"seia.logs@inema.ba.gov.br"
	};
	
	private static final String CEFIR_EMAILS[] = new String[] {
		"exception.cefir@gmail.com"
	};
	
	/**
	 * 
	 * @return emails do seia
	 */
	public static List<String> getEmailsSEIA() {
		List<String> emails = Arrays.asList(SEIA_EMAILS);
		return emails;
	}
	/**
	 * 
	 * @return emails do cefir
	 */
	public static List<String> getEmailsCEFIR() {
		List<String> emails = Arrays.asList(CEFIR_EMAILS);
		return emails;
	}
	/**
	 * 
	 * @return emails do seia e cefir
	 */
	public static List<String> getEmails() {
		List<String> emails = new ArrayList<String>(CEFIR_EMAILS.length + SEIA_EMAILS.length);
		emails.addAll(getEmailsSEIA());
		emails.addAll(getEmailsCEFIR());
		return emails;
	}
	
	/**
	 * Enviar log
	 * @param erro
	 * @param mensagem
	 * @param emails
	 */
	public static void enviarLog(Throwable erro, String mensagem, List<String> emails) {
		
		try {
			String usuarioLogado = "*ninguém logado nesta sessão*";
			
			if(!Util.isNull(ContextoUtil.getContexto()) && !Util.isNull(ContextoUtil.getContexto().getUsuarioLogado())) {
				usuarioLogado = ContextoUtil.getContexto().getUsuarioLogado().getDscLogin();
			}
			
			new EmailUtil().enviarLogEmail(emails, null, null, ExceptionUtil.getAssuntoEmail(erro, usuarioLogado), mensagem);			
		}
		catch(Exception e) {
			Log4jUtil.log(ExceptionUtil.class.getName(),Level.ERROR, e);			
		}
	}
	/**
	 * 
	 * @param e
	 * @param typeException
	 * @return
	 */
	public static RuntimeException capturarException(Throwable e, Integer typeException) {

		switch (typeException) {
		case 1:
			return new SeiaRuntimeException(e);
		default:
			return new RuntimeException(e);
		}
	}
	/**
	 * 
	 * @param message
	 * @param e
	 * @param typeException
	 * @return
	 */
	public static RuntimeException capturarException(String message, Throwable e, Integer typeException) {

		switch (typeException) {
		case 1:
			return new SeiaRuntimeException(message,e);
		default:
			return new RuntimeException(message,e);
		}
	}	
	/**
	 * 
	 * @param throwable
	 * @return
	 */
	private static Throwable desempilharErro(Throwable throwable) {
		if(throwable.getCause()==null) {
			return throwable;
		}
		return desempilharErro(throwable.getCause());
	}
	/**
	 * 
	 * @param throwable
	 * @return
	 */
	public static String getLog(Throwable throwable) {

		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);

		Throwable erro = desempilharErro(throwable);
		erro.printStackTrace(printWriter);

		return stringWriter.toString();
	}
	/**
	 * 
	 * @param erro
	 * @param usuarioLogado
	 * @return notificação de erro
	 * @throws UnknownHostException
	 */
	private static String getAssuntoEmail(Throwable erro, String usuarioLogado) throws UnknownHostException {
		return "Notificação de ERRO - SERVIDOR: " + getHostnameEmail() + ", " + getDateEmail() + " - usuario: "+usuarioLogado+" - "+ erro.getMessage();
	}

	private static String getHostnameEmail() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostName().toUpperCase();
	}
	/**
	 * 
	 * @return data formatada
	 */
	private static String getDateEmail() {
		return (new SimpleDateFormat("dd-MM-yyyy HH:mm:ss")).format(new Date());
	}
}