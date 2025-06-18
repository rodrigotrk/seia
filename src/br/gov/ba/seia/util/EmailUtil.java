 package br.gov.ba.seia.util;

import java.util.List;

import javax.activation.DataSource;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Level;

import br.gov.ba.seia.dto.ArquivoAnexarEmailDTO;
import br.gov.ba.seia.enumerator.ConfigEnum;


@Stateless
public class EmailUtil {

	private final String SMTP_SERVER_NAME = ConfigEnum.SMTP_SERVER.toString();
	private final String EMAIL_SEMA = "seia.homologa@sema.ba.gov.br";
	private final boolean DEBUG = false;
	/**
	 * Enviar email
	 * @param lEmail
	 * @throws EmailException
	 */
	private void enviarEmail(HtmlEmail lEmail) throws EmailException {
		lEmail.send();
	}
	/**
	 * Montar html do email
	 * @param emailFrom
	 * @return email 
	 * @throws EmailException
	 */
	private HtmlEmail montarHtmlEmail(String emailFrom) throws EmailException {
		HtmlEmail lEmail = new HtmlEmail();
		lEmail.setDebug(DEBUG);
		lEmail.setSmtpPort(25);
		lEmail.setHostName(SMTP_SERVER_NAME);
		lEmail.setFrom(emailFrom);
		lEmail.setCharset("UTF-8");
		return lEmail;
	}
	/**
	 * Enviar Email	
	 * @param pDestinatario
	 * @param pAssunto
	 * @param pMensagemTexto
	 * @throws Exception
	 */
	@Asynchronous
	public void enviarEmail(final String pDestinatario, final String pAssunto, final String pMensagemTexto)throws Exception {
		HtmlEmail lEmail = montarHtmlEmail(EMAIL_SEMA);
		lEmail.addTo(pDestinatario);
		lEmail.setSubject(pAssunto);
		lEmail.setTextMsg(pMensagemTexto);
	
		enviarEmail(lEmail);
	}
	/**
	 * 
	 * @param pDestinatario
	 * @param pAssunto
	 * @param pMensagemTexto
	 * @throws EmailException
	 */
	@Asynchronous
	public void enviarEmailHtml(final String pDestinatario, final String pAssunto, final String pMensagemTexto) throws EmailException{

		HtmlEmail lEmail = montarHtmlEmail(EMAIL_SEMA);
		lEmail.addTo(pDestinatario);
		lEmail.setSubject(pAssunto);
		lEmail.setHtmlMsg(pMensagemTexto);
		enviarEmail(lEmail);
	
	}
	/**
	 * 
	 * @param pListaEmailDestinatario
	 * @param pListaEmailCc
	 * @param pListaEmailCco
	 * @param pAssunto
	 * @param pMensagemTexto
	 * @throws Exception
	 */
	@Asynchronous
	public void enviarEmail(final List<String> pListaEmailDestinatario, final List<String> pListaEmailCc, final List<String> pListaEmailCco, final String pAssunto, final String pMensagemTexto) throws Exception {
		
		if (!Util.isNullOuVazio(pListaEmailDestinatario) || !Util.isNullOuVazio(pListaEmailCc) || !Util.isNullOuVazio(pListaEmailCco)) {
			HtmlEmail lEmail = montarHtmlEmail(EMAIL_SEMA);
			
			if (!Util.isNullOuVazio(pListaEmailDestinatario)) {
				for (String lEmailDestinario : pListaEmailDestinatario) {
					lEmail.addTo(lEmailDestinario);
				}
			}
			
			if (!Util.isNullOuVazio(pListaEmailCc)) {
				for (String lEmailCc : pListaEmailCc) {
					lEmail.addCc(lEmailCc);
				}
			}
			
			if (!Util.isNullOuVazio(pListaEmailCco)) {
				for (String lEmailCco : pListaEmailCco) {
					lEmail.addBcc(lEmailCco);
				}
			}
			lEmail.setSubject(pAssunto);
			lEmail.setTextMsg(pMensagemTexto);
			enviarEmail(lEmail);
		}
		else {
			throw new Exception("É necessário informar pelo menos um endereço de email para que o email seja enviado.");
		}
	}
	/**
	 * Enviar email 
	 * @param pListaEmailDestinatario
	 * @param pListaEmailCc
	 * @param pListaEmailCco
	 * @param pAssunto
	 * @param pMensagemTexto
	 * @throws Exception
	 */
	@Asynchronous
	public void enviarEmailHtml(final List<String> pListaEmailDestinatario, final List<String> pListaEmailCc, final List<String> pListaEmailCco, final String pAssunto, final String pMensagemTexto) throws Exception {

		if (!Util.isNullOuVazio(pListaEmailDestinatario) || !Util.isNullOuVazio(pListaEmailCc) || !Util.isNullOuVazio(pListaEmailCco)) {
			HtmlEmail lEmail = montarHtmlEmail(EMAIL_SEMA);

			if (!Util.isNullOuVazio(pListaEmailDestinatario)) {
				for (String lEmailDestinario : pListaEmailDestinatario) {
					lEmail.addTo(lEmailDestinario);
				}
			}

			if (!Util.isNullOuVazio(pListaEmailCc)) {
				for (String lEmailCc : pListaEmailCc) {
					lEmail.addCc(lEmailCc);
				}
			}

			if (!Util.isNullOuVazio(pListaEmailCco)) {
				for (String lEmailCco : pListaEmailCco) {
					lEmail.addBcc(lEmailCco);
				}
			}
			lEmail.setSubject(pAssunto);
			lEmail.setHtmlMsg(pMensagemTexto);
			enviarEmail(lEmail);
		}
		else {
			throw new Exception("É necessário informar pelo menos um endereço de email para que o email seja enviado.");
		}
	}
	/**
	 * Enviar log do email
	 * @param pListaEmailDestinatario
	 * @param pListaEmailCc
	 * @param pListaEmailCco
	 * @param pAssunto
	 * @param pMensagemTexto
	 * @throws Exception
	 */
	@Asynchronous
	public void enviarLogEmail(final List<String> pListaEmailDestinatario, final List<String> pListaEmailCc, final List<String> pListaEmailCco, final String pAssunto, final String pMensagemTexto) throws Exception {
		try {

			if (!Util.isNullOuVazio(pListaEmailDestinatario) || !Util.isNullOuVazio(pListaEmailCc) || !Util.isNullOuVazio(pListaEmailCco)) {
				HtmlEmail lEmail = montarHtmlEmail(EMAIL_SEMA);
				if (!Util.isNullOuVazio(pListaEmailDestinatario)) {
					for (String lEmailDestinario : pListaEmailDestinatario) {
						lEmail.addTo(lEmailDestinario);
					}
				}

				if (!Util.isNullOuVazio(pListaEmailCc)) {
					for (String lEmailCc : pListaEmailCc) {
						lEmail.addCc(lEmailCc);
					}
				}

				if (!Util.isNullOuVazio(pListaEmailCco)) {
					for (String lEmailCco : pListaEmailCco) {
						lEmail.addBcc(lEmailCco);
					}
				}

				lEmail.setSubject(pAssunto);
				lEmail.setTextMsg(pMensagemTexto);
				enviarEmail(lEmail);
			}
			else {
				throw new Exception("É necessário informar pelo menos um endereço de email para que o email seja enviado.");
			}
		}
		catch(Exception e){
			Log4jUtil.log(this.getClass().getName(),Level.ERROR, e);
			throw Util.capturarException(e,Util.SEIA_EXCEPTION);
		}
	}
	/**
	 * Enviar email com anexo
	 * @param pDestinatario
	 * @param pAssunto
	 * @param pMensagemTexto
	 * @param emailFrom
	 * @param arquivosAnexar
	 * @throws Exception
	 */
	@Asynchronous
	public void enviarEmailComAnexos(final String pDestinatario, final String pAssunto, final String pMensagemTexto, String emailFrom, List<ArquivoAnexarEmailDTO> arquivosAnexar) throws Exception{
		HtmlEmail lEmail = montarHtmlEmail(emailFrom);

		lEmail.addTo(pDestinatario);
		lEmail.setSubject(pAssunto);
		lEmail.setMsg(pMensagemTexto);
		
		for (ArquivoAnexarEmailDTO arquivoAnexarEmailDTO : arquivosAnexar) {
			DataSource source = new ByteArrayDataSource(arquivoAnexarEmailDTO.getInputStream(), arquivoAnexarEmailDTO.getType());				
	        lEmail.attach(source, arquivoAnexarEmailDTO.getNomeArquivo(), arquivoAnexarEmailDTO.getType());
		}
		
		enviarEmail(lEmail);
	}
}
